package com.money.transfer.user.domain;

import static java.util.regex.Pattern.*;

import com.money.transfer.user.domain.entity.UserEntity;

public class User {

    private final String ALLOWED_PASSWORD = "!\"#$%&'()*+,-./:;<=>?@[₩]^_`{|}~";

    private Long id;

    private String name;

    private String email;

    private String password;

    public User(String name, String email, String password) {
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다.");
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 20자 이하이어야 합니다.");
        }

        String regex = "^[a-zA-Z0-9" + ALLOWED_PASSWORD + "]+$";

        if (!password.matches(regex)) {
            throw new IllegalArgumentException("비밀번호에 허용되지 않은 문자가 포함되어 있습니다.");
        }
    }

    public UserEntity toEntity() {
        return new UserEntity(name, email, password);
    }
}
