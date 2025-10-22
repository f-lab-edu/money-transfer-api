package com.money.transfer.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserJoinRequest (

        @NotBlank(message = "{user.name.notBlank}")
        String name,

        @NotBlank(message = "{user.email.notBlank}")
        @Email(message = "{user.email.invalid}")
        String email,

        @Size(min = 8, max = 21, message = "{user.password.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!\"#$%&'()*+,-./:;<=>?@[₩]^_`{|}~]+$",
                message = "{user.password.pattern}"
        )
        String password
) {
}
