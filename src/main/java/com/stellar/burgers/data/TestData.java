package com.stellar.burgers.data;

import com.github.javafaker.Faker;
import java.util.Locale;

public class TestData {
    private static final Faker faker = new Faker(new Locale("ru"));

    public static User getValidUser() {
        return User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(6, 20))
                .build();
    }

    public static User getUserWithShortPassword() {
        return User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password("12345")
                .build();
    }

    public static User getUserForApiRegistration() {
        return getValidUser();
    }
}