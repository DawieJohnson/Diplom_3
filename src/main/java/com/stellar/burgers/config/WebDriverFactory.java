package com.stellar.burgers.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    public static WebDriver createDriver(String browserName) {
        WebDriver driver;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("СОЗДАНИЕ ДРАЙВЕРА ДЛЯ: " + browserName.toUpperCase());
        System.out.println("=".repeat(60));

        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver();
                break;

            case "yandex":
                driver = createYandexDriver();
                break;

            case "firefox":
                driver = createFirefoxDriver();
                break;

            default:
                System.out.println("⚠️ Неизвестный браузер '" + browserName + "', использую Chrome");
                driver = createChromeDriver();
                break;
        }

        configureDriver(driver);
        return driver;
    }

    private static WebDriver createChromeDriver() {
        System.out.println("🔵 Настраиваю Google Chrome...");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        configureCommonChromeOptions(options);

        return new ChromeDriver(options);
    }

    private static WebDriver createYandexDriver() {
        System.out.println("🔵 Настраиваю Яндекс.Браузер...");

        String yandexDriverPath = System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\yandexdriver.exe";
        File yandexDriverFile = new File(yandexDriverPath);

        if (!yandexDriverFile.exists()) {
            System.err.println("❌ Яндекс.Драйвер не найден по пути: " + yandexDriverPath);
            System.err.println("📥 Скачайте его с: https://github.com/yandex/YandexDriver/releases");
            System.err.println("📁 И положите в: src/test/resources/drivers/yandexdriver.exe");
            throw new RuntimeException("Яндекс.Драйвер не найден!");
        }

        System.setProperty("webdriver.chrome.driver", yandexDriverPath);
        System.out.println("✅ Яндекс.Драйвер найден: " + yandexDriverPath);

        File yandexBrowserFile = new File(TestConfig.YANDEX_BROWSER_PATH);

        if (!yandexBrowserFile.exists()) {
            System.err.println("❌ Яндекс.Браузер не найден по пути: " + TestConfig.YANDEX_BROWSER_PATH);
            throw new RuntimeException("Яндекс.Браузер не найден!");
        }

        ChromeOptions options = new ChromeOptions();
        options.setBinary(TestConfig.YANDEX_BROWSER_PATH);

        options.addArguments("--lang=ru");
        options.addArguments("--disable-blink-features=AutomationControlled");

        configureCommonChromeOptions(options);

        System.out.println("✅ Яндекс.Браузер настроен: " + TestConfig.YANDEX_BROWSER_PATH);
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        System.out.println("🔵 Настраиваю Firefox...");
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver();
    }

    private static void configureCommonChromeOptions(ChromeOptions options) {
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");

        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("intl.accept_languages", "ru,ru_RU");
        options.setExperimentalOption("prefs", prefs);
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.IMPLICIT_WAIT));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            System.out.println("✅ Окно браузера развернуто");
        } catch (Exception e) {
            System.out.println("⚠️ Не удалось развернуть окно: " + e.getMessage());
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1366, 768));
        }

        System.out.println("✅ Драйвер настроен и готов к работе");
        System.out.println("🕒 Таймауты: Implicit=" + TestConfig.IMPLICIT_WAIT +
                "s, PageLoad=" + TestConfig.PAGE_LOAD_TIMEOUT + "s");
    }
}
