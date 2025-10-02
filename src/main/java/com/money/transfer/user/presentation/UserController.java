package com.money.transfer.user.presentation;

import com.money.transfer.user.application.UserService;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public void join(@RequestBody UserJoinRequest request) {
        userService.join(request);
    }
}
