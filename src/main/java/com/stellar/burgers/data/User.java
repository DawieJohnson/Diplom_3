package com.stellar.burgers.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private String accessToken = "";

    public static User createDefaultUser() {
        return User.builder()
                .name("Иван Иванов")
                .email("test@example.com")
                .password("password123")
                .build();
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }
}