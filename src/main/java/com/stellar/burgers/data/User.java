package com.stellar.burgers.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String name;
    private String email;
    private String password;

    public static User createDefaultUser() {
        return User.builder()
                .name("Иван Иванов")
                .email("test@example.com")
                .password("password123")
                .build();
    }
}