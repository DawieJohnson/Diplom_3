package com.stellar.burgers.tests;

import com.stellar.burgers.api.UserApiClient;
import com.stellar.burgers.config.TestConfig;
import com.stellar.burgers.config.WebDriverFactory;
import com.stellar.burgers.data.User;
import com.stellar.burgers.utils.AllureAttachments;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final ThreadLocal<User> threadLocalUser = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    private void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }

    protected User getCurrentUser() {
        return threadLocalUser.get();
    }

    protected void setCurrentUser(User user) {
        threadLocalUser.set(user);
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        String browserToUse = browser != null ? browser : System.getProperty("browser", "chrome");

        WebDriver driver = WebDriverFactory.createDriver(browserToUse);
        setDriver(driver);

        driver.get(TestConfig.BASE_URL);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        // Удаляем пользователя через API если он был создан
        User user = getCurrentUser();
        if (user != null && user.getEmail() != null) {
            cleanUpUser(user);
        }

        try {
            // Делаем скриншот только если тест упал
            if (!result.isSuccess() && driver != null) {
                AllureAttachments.attachScreenshot(driver);
                AllureAttachments.attachPageSource(driver);

                // Добавляем информацию об ошибке
                if (result.getThrowable() != null) {
                    Allure.addAttachment("Ошибка теста", "text/plain",
                            result.getThrowable().getMessage());
                }
            }
        } catch (Exception e) {
            // Игнорируем ошибки при создании скриншотов
            System.out.println("⚠️ Ошибка при создании скриншота: " + e.getMessage());
        } finally {
            // Закрываем браузер и очищаем ThreadLocal
            if (driver != null) {
                try {
                    driver.quit();
                    System.out.println("✅ Браузер закрыт");
                } finally {
                    threadLocalDriver.remove();
                    threadLocalUser.remove();
                    System.out.println("✅ ThreadLocal очищен");
                }
            }
        }
    }

    private void cleanUpUser(User user) {
        System.out.println("🧹 Очистка: попытка удаления тестового пользователя: " + user.getEmail());

        // Если у пользователя уже есть токен - используем его
        if (user.getAccessToken() != null && !user.getAccessToken().isEmpty()) {
            UserApiClient.deleteUser(user);
            return;
        }

        // Если токена нет - пробуем получить его через логин
        try {
            String token = UserApiClient.loginAndGetToken(user);
            if (token != null) {
                user.setAccessToken(token);
                UserApiClient.deleteUser(user);
            } else {
                System.out.println("⚠️ Не удалось получить токен для пользователя: " + user.getEmail());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Ошибка при очистке пользователя " + user.getEmail() + ": " + e.getMessage());
        }
    }
}