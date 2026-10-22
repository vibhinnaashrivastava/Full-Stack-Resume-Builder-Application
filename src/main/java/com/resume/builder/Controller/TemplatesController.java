package com.resume.builder.Controller;

import com.resume.builder.Services.TemplatesService;
import com.resume.builder.Util.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.TEMPLATES)
@Slf4j
public class TemplatesController {

    private final TemplatesService templatesService;

    @GetMapping(AppConstant.GET_TEMPLATES)
    public ResponseEntity<?> getTemplates(Authentication authentication){
        //STEP 01: Call the service method.
        Map<String, Object> response = templatesService.getTemplates(authentication.getPrincipal());
        //STEP 02: Return the response.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
