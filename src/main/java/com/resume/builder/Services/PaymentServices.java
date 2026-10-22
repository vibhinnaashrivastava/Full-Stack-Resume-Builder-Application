package com.resume.builder.Services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.Document.Payment;
import com.resume.builder.Document.User;
import com.resume.builder.Repository.PaymentRepository;
import com.resume.builder.Repository.UserRepository;
import com.resume.builder.Util.AppConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServices {

    private final PaymentRepository paymentRepository;
    private final AuthServices authServices;
    private final UserRepository userRepository;
    @Value("${razorpay.key.id}")
    private String razorPayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorPayKeySecret;


    public Payment createOrder(
            @Nullable Object principal,
            String planType
    ) throws RazorpayException {

        AuthResponse responsee = authServices.getProfile(principal);

        RazorpayClient razorpayClient =
                new RazorpayClient(razorPayKeyId, razorPayKeySecret);

        int amount = 99900; // ₹999 in paise
        String currency = "INR";

        String receipt = AppConstant.PREMIUM + " "
                + UUID.randomUUID().toString().substring(0, 8);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment newPayment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .userId(responsee.getUserId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();

        return paymentRepository.save(newPayment);
    }


    public Boolean verifyPayment(String razorpayOrderId,
                                 String razorpayPaymentId,
                                 String razorpaySignature) throws RazorpayException {
        try{
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            Boolean isValidSignature = Utils.verifyPaymentSignature(attributes, razorPayKeySecret);

            if(isValidSignature){
                //Update the payment status.
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException("Payment from this order id is not found"));
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("paid");
                paymentRepository.save(payment);

                //Upgrade the user subscription.
                upgradeUserSubscription(payment.getUserId(), payment.getPlanType());
                return true;
            }
            return false;
        }catch(Exception exception){
            log.error("Error verifying the payment: ", exception);
            return false;
        }

    }

    private void upgradeUserSubscription(String userId, String planType) {
        User existingUser = userRepository.findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found exception"));
        existingUser.setSubscriptionPlan(planType);
        userRepository.save(existingUser);
        log.info("User {} upgraded to {} plan" , userId, planType);
    }

    public List<Payment> getUserPayment(@Nullable Object principal) {
        //STEP 01: Get the current profile.
        AuthResponse responseee = authServices.getProfile(principal);
        //STEP 02: Call the repository finder method.
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(responseee.getUserId());
    }

    public Payment getPaymentDetails(String razorpayOrderId){
        //STEP 01: Call the repo finder method.
        return paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
