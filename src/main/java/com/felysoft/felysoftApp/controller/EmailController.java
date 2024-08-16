package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.EmailRequest;
import com.felysoft.felysoftApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getEmailUser(), emailRequest.getSubject(), emailRequest.getText());


    }
}
