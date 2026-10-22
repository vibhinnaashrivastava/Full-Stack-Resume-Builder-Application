package com.resume.builder.Services;

import com.resume.builder.DTO.AuthResponse;
import com.resume.builder.Util.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {

    private final AuthServices authServices;

    public Map<String, Object> getTemplates(Object principal){
        //STEP 01: Get the current profile.
        AuthResponse authResponse = authServices.getProfile(principal);
        //STEP 02: Get the available template based on subscription.
        List<String> allTemplates = List.of("01", "02", "03");
        List<String> availableTemplates;
        Boolean isPremium = AppConstant.PREMIUM.equalsIgnoreCase(authResponse.getSubscriptionPlan());
        if(isPremium){
            availableTemplates = allTemplates;
        }
        else{
            availableTemplates = List.of("01");
        }
        //STEP 03: Add the data into map.
        Map<String, Object> restrictions = new HashMap<>();
        restrictions.put("availableTemplates", availableTemplates);
        restrictions.put("allTemplates", List.of("01", "02", "03"));
        restrictions.put("SubscriptionPlan", authResponse.getSubscriptionPlan());
        restrictions.put("isPremium", isPremium);
        //STEP 04: Return the result.
        return restrictions;
    }
}
