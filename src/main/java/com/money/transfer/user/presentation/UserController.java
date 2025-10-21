package com.money.transfer.user.presentation;

import com.money.transfer.user.application.UserService;
import com.money.transfer.user.common.UserMapper;
import com.money.transfer.user.domain.User;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import com.money.transfer.user.presentation.request.UserResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@Valid @RequestBody final UserJoinRequest request) {
        final User savedUser = userService.join(request);

        final UserResponse response = UserMapper.INSTANCE.toResponse(savedUser);

        return ResponseEntity
                .created(URI.create("/api/users/" + response.getId()))
                .body(response);
    }
}
