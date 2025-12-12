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

    public static User getExistingUserForLogin() {
        // Проверяем переменные окружения
        String email = System.getenv("STELLAR_TEST_EMAIL");
        String password = System.getenv("STELLAR_TEST_PASSWORD");

        // Если найдены в env - используем их
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
            System.out.println("✅ Использую учетные данные из переменных окружения");
            return User.builder()
                    .name("Автотестовый Пользователь")
                    .email(email)
                    .password(password)
                    .build();
        }

        // Fallback - тестовые данные (НУЖНО НАСТРОИТЬ!)
        System.out.println("⚠️ ВНИМАНИЕ: Переменные окружения STELLAR_TEST_EMAIL и STELLAR_TEST_PASSWORD не найдены");
        System.out.println("⚠️ Использую тестовые данные. Для реальных тестов:");
        System.out.println("   1. Зарегистрируйте пользователя в приложении");
        System.out.println("   2. Установите переменные окружения:");
        System.out.println("      export STELLAR_TEST_EMAIL=ваш@email.com");
        System.out.println("      export STELLAR_TEST_PASSWORD=ваш_пароль");

        // ВРЕМЕННЫЕ ДАННЫЕ - ЗАМЕНИТЬ НА РЕАЛЬНЫЕ ИЛИ СОЗДАТЬ ПОЛЬЗОВАТЕЛЯ
        return User.builder()
                .name("Александр")
                .email("testuser@bk.ru")  // ← ЗАМЕНИТЕ на реальный email
                .password("12345678")             // ← ЗАМЕНИТЕ на реальный пароль
                .build();
    }

    public static User getNewUserForRegistrationAndLogin() {
        User newUser = getValidUser();
        System.out.println("Создан новый пользователь для теста: " + newUser.getEmail());
        return newUser;
    }
}