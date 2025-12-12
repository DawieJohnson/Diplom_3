package com.stellar.burgers.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ForgotPasswordPage extends BasePage {

    @FindBy(xpath = "//h2[text()='Восстановление пароля']")
    private WebElement forgotPasswordHeader;

    @FindBy(linkText = "Войти")
    private WebElement loginLink;

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public boolean isForgotPasswordPageLoaded() {
        return isElementVisible(forgotPasswordHeader);
    }

    public void clickLoginLink() {
        waitAndClick(loginLink);
    }
}