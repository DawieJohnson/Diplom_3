package com.stellar.burgers.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // СЕЛЕКТОРЫ ДЛЯ СТРАНИЦЫ ЛОГИНА
    @FindBy(xpath = "//label[text()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()='Войти']")
    private WebElement loginSubmitButton;

    @FindBy(linkText = "Зарегистрироваться")
    private WebElement registrationLink;

    @FindBy(linkText = "Восстановить пароль")
    private WebElement forgotPasswordLink;

    @FindBy(xpath = "//h2[text()='Вход']")
    private WebElement loginHeader;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginPageLoaded() {
        return isElementVisible(loginHeader);
    }

    public void login(String email, String password) {
        waitAndSendKeys(emailInput, email);
        waitAndSendKeys(passwordInput, password);
        waitAndClick(loginSubmitButton);
    }

    public void clickRegistrationLink() {
        waitAndClick(registrationLink);
    }

    public void clickForgotPasswordLink() {
        waitAndClick(forgotPasswordLink);
    }
}