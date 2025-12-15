package com.stellar.burgers.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ConstructorPage extends BasePage {

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Булки']/parent::div")
    private WebElement bunsTab;

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Соусы']/parent::div")
    private WebElement saucesTab;

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Начинки']/parent::div")
    private WebElement fillingsTab;

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Булки']")
    private WebElement bunsSection;

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Соусы']")
    private WebElement saucesSection;

    @FindBy(xpath = "//div[contains(@class, 'tab_tab__')]//span[text()='Начинки']")
    private WebElement fillingsSection;

    private static final String ACTIVE_CLASS = "tab_tab_type_current__2BEPc";
    private static final String ACTIVE_COLOR = "rgba(255, 255, 255, 1)";
    private static final String INACTIVE_COLOR = "rgba(133, 133, 173, 1)";

    public ConstructorPage(WebDriver driver) {
        super(driver);
    }

    public void clickBunsSection() {
        waitAndClick(bunsSection);
        waitForSectionAnimation();
        waitForTabToBecomeActive(bunsTab);
    }

    public void clickSaucesSection() {
        waitAndClick(saucesSection);
        waitForSectionAnimation();
        waitForTabToBecomeActive(saucesTab);
    }

    public void clickFillingsSection() {
        waitAndClick(fillingsSection);
        waitForSectionAnimation();
        waitForTabToBecomeActive(fillingsTab);
    }

    // Методы проверки активности
    public boolean isBunsTabActive() {
        return isTabActive(bunsTab);
    }

    public boolean isSaucesTabActive() {
        return isTabActive(saucesTab);
    }

    public boolean isFillingsTabActive() {
        return isTabActive(fillingsTab);
    }

    // УПРОЩЕННЫЕ методы проверки неактивности - проверяем только, что таб НЕ активен
    public boolean isBunsTabInactive() {
        return !isTabActive(bunsTab);
    }

    public boolean isSaucesTabInactive() {
        return !isTabActive(saucesTab);
    }

    public boolean isFillingsTabInactive() {
        return !isTabActive(fillingsTab);
    }

    // Вспомогательные приватные методы
    private boolean isTabActive(WebElement tab) {
        try {
            wait.until(ExpectedConditions.visibilityOf(tab));

            String classAttribute = tab.getAttribute("class");
            String color = tab.getCssValue("color");

            return classAttribute.contains(ACTIVE_CLASS) &&
                    ACTIVE_COLOR.equals(color);
        } catch (Exception e) {
            return false;
        }
    }

    // Ожидание активности таба
    private void waitForTabToBecomeActive(WebElement tab) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver -> isTabActive(tab));
    }

    private void waitForSectionAnimation() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Получение имени активного раздела
    public String getActiveSectionName() {
        if (isBunsTabActive()) return "Булки";
        if (isSaucesTabActive()) return "Соусы";
        if (isFillingsTabActive()) return "Начинки";
        return "Не определен";
    }

    // Вывод отладочной информации
    public void printTabStates() {
        System.out.println("=== СОСТОЯНИЕ ТАБОВ ===");
        System.out.println("Булки - активен: " + isBunsTabActive());
        System.out.println("Соусы - активен: " + isSaucesTabActive());
        System.out.println("Начинки - активен: " + isFillingsTabActive());
        System.out.println("Активный раздел: " + getActiveSectionName());
        System.out.println("=====================");
    }
}