package com.stellar.burgers.tests;

import com.stellar.burgers.config.TestConfig;
import com.stellar.burgers.config.WebDriverFactory;
import com.stellar.burgers.utils.AllureAttachments;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {
    // Поддержка параллельного запуска
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    private void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser) {
        // Используем браузер из параметра теста или системного свойства
        String browserToUse = browser != null ? browser : System.getProperty("browser", "chrome");

        System.out.println("=".repeat(50));
        System.out.println("[" + Thread.currentThread().getId() + "] Запуск тестов в браузере: " + browserToUse.toUpperCase());
        System.out.println("=".repeat(50));

        // Создаем драйвер
        WebDriver driver = WebDriverFactory.createDriver(browserToUse);
        setDriver(driver);

        // Открываем сайт
        System.out.println("[" + Thread.currentThread().getId() + "] Открываем URL: " + TestConfig.BASE_URL);
        driver.get(TestConfig.BASE_URL);

        // Добавляем информацию в Allure
        Allure.addAttachment("Браузер", "text/plain", browserToUse);
        Allure.addAttachment("Поток", "text/plain", String.valueOf(Thread.currentThread().getId()));
        Allure.addAttachment("URL", "text/plain", TestConfig.BASE_URL);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        try {
            // Проверяем, что драйвер еще жив
            if (driver != null) {
                try {
                    // Делаем скриншот только если сессия активна
                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl != null && !currentUrl.isEmpty()) {
                        AllureAttachments.attachScreenshot(driver);
                        AllureAttachments.attachPageSource(driver);
                    }
                } catch (Exception e) {
                    System.out.println("Не удалось сделать скриншот: " + e.getMessage());
                }

                // Добавляем URL и заголовок для отладки
                try {
                    Allure.addAttachment("Последний URL", "text/plain", driver.getCurrentUrl());
                    Allure.addAttachment("Заголовок страницы", "text/plain", driver.getTitle());
                } catch (Exception e) {
                    // Игнорируем ошибки при получении URL/заголовка
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка в tearDown: " + e.getMessage());
        } finally {
            // Если тест упал - добавляем детали ошибки
            if (!result.isSuccess()) {
                Allure.addAttachment("Ошибка теста", "text/plain",
                        result.getThrowable().getMessage());
                System.err.println("[" + Thread.currentThread().getId() + "] Тест упал: " + result.getName());
                System.err.println("[" + Thread.currentThread().getId() + "] Причина: " + result.getThrowable().getMessage());
            }

            // Закрываем браузер и очищаем ThreadLocal
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    System.out.println("Ошибка при закрытии драйвера: " + e.getMessage());
                } finally {
                    threadLocalDriver.remove();
                }
            }

            System.out.println("[" + Thread.currentThread().getId() + "] Тест завершен: " + result.getName() +
                    " - " + (result.isSuccess() ? "PASS" : "FAIL"));
        }
    }
}