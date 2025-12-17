package com.stellar.burgers.pages;

import com.stellar.burgers.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage extends BasePage {

    // СЕЛЕКТОРЫ HTML
    @FindBy(xpath = "//label[text()='Имя']/following-sibling::input")
    private WebElement nameInput;

    @FindBy(xpath = "//label[text()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()='Зарегистрироваться']")
    private WebElement registerButton;

    @FindBy(linkText = "Войти")
    private WebElement loginLink;

    @FindBy(xpath = "//h2[text()='Регистрация']")
    private WebElement registrationHeader;

    // СЕЛЕКТОР ОШИБКИ
    @FindBy(xpath = "//p[contains(@class, 'input__error')]")
    private WebElement passwordError;

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRegistrationPageLoaded() {
        return isElementVisible(registrationHeader);
    }

    public void register(String name, String email, String password) {
        System.out.println("📝 Регистрация пользователя: " + email);

        waitAndSendKeys(nameInput, name);
        waitAndSendKeys(emailInput, email);
        waitAndSendKeys(passwordInput, password);
        waitAndClick(registerButton);
    }

    public void clickLoginLink() {
        waitAndClick(loginLink);
    }

    public boolean isPasswordErrorDisplayed() {
        return isElementVisible(passwordError);
    }

    public String getPasswordErrorText() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                    .until(ExpectedConditions.visibilityOf(passwordError));
            String errorText = passwordError.getText();
            System.out.println("🔴 Текст ошибки: '" + errorText + "'");
            return errorText;
        } catch (Exception e) {
            System.out.println("ℹ️ Ошибка не отображается");
            return "";
        }
    }
}