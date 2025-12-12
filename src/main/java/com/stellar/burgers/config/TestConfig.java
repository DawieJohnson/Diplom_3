package com.stellar.burgers.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Не удалось загрузить config.properties: " + e.getMessage());
        }
    }

    public static final String BASE_URL = props.getProperty("app.url",
            "https://stellarburgers.education-services.ru/");

    // Константы браузеров
    public static final String CHROME = "chrome";
    public static final String YANDEX = "yandex";
    public static final String FIREFOX = "firefox";

    public static final int IMPLICIT_WAIT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 20;
    public static final int EXPLICIT_WAIT = 15;

    // Путь к Яндекс.Браузеру
    public static final String YANDEX_BROWSER_PATH = "C:\\Users\\Александр\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe";

    // Реальные учетные данные (опционально)
    public static String getTestUserEmail() {
        return props.getProperty("test.user.email", "testuser@bk.ru");
    }

    public static String getTestUserPassword() {
        return props.getProperty("test.user.password", "12345678");
    }
}