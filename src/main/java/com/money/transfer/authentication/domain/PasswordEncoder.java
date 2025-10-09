package com.money.transfer.authentication.domain;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public static String encode(final String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
