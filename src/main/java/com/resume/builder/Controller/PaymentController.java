package com.resume.builder.Controller;

import com.razorpay.RazorpayException;
import com.resume.builder.Document.Payment;
import com.resume.builder.Services.PaymentServices;
import com.resume.builder.Util.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AppConstant.PAYMENT)
public class PaymentController {

    private final PaymentServices paymentServices;


    @Value("${razorpay.key.id}")
    private String razorPayKeyId;

    @PostMapping(AppConstant.CREATE_PAYMENT)
    public ResponseEntity<?> createOrder(
            @RequestBody Map<String, String> request,
            Authentication authentication
    ) throws RazorpayException {

        String planType = request.get("planType");

        if (planType == null ||
                !AppConstant.PREMIUM.equalsIgnoreCase(planType)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid plan type"));
        }

        if (authentication == null ||
                authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please login first"));
        }

        Payment payment = paymentServices.createOrder(
                authentication.getPrincipal(),
                planType
        );

        Map<String, Object> response = Map.of(
                "keyId", razorPayKeyId,
                "orderId", payment.getRazorpayOrderId(),
                "amount", payment.getAmount(),
                "currency", payment.getCurrency(),
                "receipt", payment.getReceipt()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping(AppConstant.VERIFY_PAYMENT)
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String ,String> request) throws RazorpayException {
        //Additional STEP: Validate the request.
        String razorpayOrderId = request.get("razorpay_order_id");
        String razorpayPaymentId = request.get("razorpay_payment_id");
        String razorpaySignature = request.get("razorpay_signature");
        if(Objects.isNull(razorpayOrderId) ||
                Objects.isNull(razorpayPaymentId) ||
                Objects.isNull(razorpaySignature)){
            return ResponseEntity.badRequest().body(Map.of("message", "missing parameters"));
        }
        //STEP 01: Call the service method.
        Boolean isValid = paymentServices.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
        //STEP 02: Return the response.
        if(isValid){
            return ResponseEntity.ok().body(Map.of("Message", "Payment verification successfully completed",
                    "STATUS", "Success"));
        }else{
            return ResponseEntity.badRequest().body(Map.of("Message", "Payment failed"));
        }
    }

    @GetMapping(AppConstant.HISTORY_PAYMENT)
    public ResponseEntity<?> getPaymentHistory(Authentication authentication){
        //STEP 01: Call the service method.
        List<Payment> payments = paymentServices.getUserPayment(authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.ok().body(payments);
    }

    @GetMapping(AppConstant.ORDER_PAYMENT)
    public ResponseEntity<?> orderById(@PathVariable String razorpayOrderId){
        //STEP 01: Call the service method.
        Payment paymentDetails = paymentServices.getPaymentDetails(razorpayOrderId);
        //STEP 02: Return the response.
        return ResponseEntity.ok(paymentDetails);
    }
}
