package com.stellar.burgers.api;

import com.stellar.burgers.data.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UserApiClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String DELETE_ENDPOINT = "/api/auth/user";

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static User createRandomUser() {
        User user = com.stellar.burgers.data.TestData.getValidUser();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", user.getEmail());
        requestBody.put("password", user.getPassword());
        requestBody.put("name", user.getName());

        System.out.println("📤 Отправка запроса на регистрацию через API: " + user.getEmail());

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(REGISTER_ENDPOINT);

        if (response.statusCode() == 200) {
            String token = response.path("accessToken");
            if (token != null) {
                // Убираем "Bearer " префикс если он есть
                String cleanToken = token.toString().replace("Bearer ", "");
                user.setAccessToken(cleanToken);
                System.out.println("✅ Создан пользователь через API: " + user.getEmail());
                System.out.println("🔑 Получен токен: " + user.getAccessToken().substring(0, Math.min(user.getAccessToken().length(), 20)) + "...");
            } else {
                System.out.println("⚠️ Токен не получен при регистрации через API");
            }
            return user;
        } else {
            System.out.println("❌ Не удалось создать пользователя через API. Код: " + response.statusCode());
            System.out.println("📄 Ответ сервера: " + response.getBody().asString());
            return user; // Возвращаем пользователя без токена
        }
    }

    public static String loginAndGetToken(User user) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", user.getEmail());
        requestBody.put("password", user.getPassword());

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(LOGIN_ENDPOINT);

        if (response.statusCode() == 200) {
            String token = response.path("accessToken");
            if (token != null) {
                return token.toString().replace("Bearer ", "");
            }
        }
        return null;
    }

    public static void deleteUser(User user) {
        if (user == null || user.getEmail() == null) {
            System.out.println("⚠️ Нечего удалять: пользователь пустой");
            return;
        }

        if (user.getAccessToken() == null || user.getAccessToken().isEmpty()) {
            System.out.println("⚠️ Не удалось удалить пользователя " + user.getEmail() + ": отсутствует токен");
            return;
        }

        try {
            System.out.println("🗑️ Попытка удаления пользователя через API: " + user.getEmail());

            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", user.getAccessToken())
                    .delete(DELETE_ENDPOINT);

            int statusCode = response.statusCode();

            if (statusCode == 202) {
                System.out.println("✅ Пользователь успешно удален: " + user.getEmail());
            } else if (statusCode == 401) {
                System.out.println("❌ Ошибка 401: Неавторизованный запрос для пользователя " + user.getEmail());
                System.out.println("📄 Ответ: " + response.getBody().asString());
            } else {
                System.out.println("⚠️ Неожиданный код ответа при удалении: " + statusCode);
                System.out.println("📄 Ответ: " + response.getBody().asString());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Ошибка при удалении пользователя " + user.getEmail() + ": " + e.getMessage());
        }
    }
}