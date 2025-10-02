package com.money.transfer.user.presentation.request;

public record UserJoinRequest(
        String name,
        String email,
        String password
) {
}
