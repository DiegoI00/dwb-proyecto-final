package com.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("12345678");
        System.out.println(hash);
        System.out.println(encoder.matches("12345678", hash));
    }
}
