package com.money.transfer.authentication.presentation;

import com.money.transfer.authentication.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestParam final String email) {
        authService.sendEmail(email);
    }

    @PostMapping("/email/verify")
    public void verifyEmail(@RequestParam final String email, final String authCode) {
        authService.verifyEmail(email, authCode);
    }
}
