package com.money.transfer.user.domain;

import com.money.transfer.authentication.domain.PasswordEncoder;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {

    private final String ALLOWED_PASSWORD = "!\"#$%&'()*+,-./:;<=>?@[₩]^_`{|}~";

    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(final String name, final String email, final String password) {
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = PasswordEncoder.encode(password);
    }

    private void validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다.");
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 20자 이하이어야 합니다.");
        }

        final String regex = "^[a-zA-Z0-9" + ALLOWED_PASSWORD + "]+$";

        if (!password.matches(regex)) {
            throw new IllegalArgumentException("비밀번호에 허용되지 않은 문자가 포함되어 있습니다.");
        }
    }
}
