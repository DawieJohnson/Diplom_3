package com.stellar.burgers.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BasePage {

    @FindBy(xpath = "//button[text()='Войти в аккаунт']")
    private WebElement loginButton;

    @FindBy(xpath = "//a[@href='/account']")
    private WebElement personalAccountButton;

    @FindBy(xpath = "//h1[text()='Соберите бургер']")
    private WebElement mainHeader;

    @FindBy(xpath = "//button[contains(text(), 'Оформить заказ')]")
    private WebElement placeOrderButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isMainPageLoaded() {
        return isElementVisible(mainHeader);
    }

    public void clickLoginButton() {
        waitAndClick(loginButton);
    }

    public void clickPersonalAccountButton() {
        waitAndClick(personalAccountButton);
    }

    public boolean isUserLoggedIn() {
        return isElementVisible(placeOrderButton);
    }

    public boolean isPlaceOrderButtonVisible() {
        return isElementVisible(placeOrderButton);
    }

    public WebElement getPlaceOrderButton() {
        return placeOrderButton;
    }
}