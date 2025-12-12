package com.stellar.burgers.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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

    // Константы из лога
    private static final String ACTIVE_CLASS = "tab_tab_type_current__2BEPc";
    private static final String ACTIVE_COLOR = "rgba(255, 255, 255, 1)";
    private static final String INACTIVE_COLOR = "rgba(133, 133, 173, 1)";

    public ConstructorPage(WebDriver driver) {
        super(driver);
    }

    public void clickBunsSection() {
        waitAndClick(bunsSection);
        waitForSectionAnimation(); // НОВЫЙ МЕТОД
        waitForTabToBecomeActive(bunsTab);
    }

    public void clickSaucesSection() {
        waitAndClick(saucesSection);
        waitForSectionAnimation(); // НОВЫЙ МЕТОД
        waitForTabToBecomeActive(saucesTab);
    }

    public void clickFillingsSection() {
        waitAndClick(fillingsSection);
        waitForSectionAnimation(); // НОВЫЙ МЕТОД
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

    // Методы проверки неактивности
    public boolean isBunsTabInactive() {
        return isTabInactive(bunsTab);
    }

    public boolean isSaucesTabInactive() {
        return isTabInactive(saucesTab);
    }

    public boolean isFillingsTabInactive() {
        return isTabInactive(fillingsTab);
    }

    // Вспомогательные приватные методы
    private boolean isTabActive(WebElement tab) {
        try {
            String classAttribute = tab.getAttribute("class");
            String color = tab.getCssValue("color");

            return classAttribute.contains(ACTIVE_CLASS) &&
                    ACTIVE_COLOR.equals(color);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTabInactive(WebElement tab) {
        try {
            String classAttribute = tab.getAttribute("class");
            String color = tab.getCssValue("color");

            return !classAttribute.contains(ACTIVE_CLASS) &&
                    INACTIVE_COLOR.equals(color);
        } catch (Exception e) {
            return false;
        }
    }

    // Ожидание активности таба
    private void waitForTabToBecomeActive(WebElement tab) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(driver -> isTabActive(tab));
    }

    private void waitForSectionAnimation() {
        try {
            // Ждем завершения CSS-переходов
            Thread.sleep(300);

            // Дополнительная проверка через JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "return new Promise(resolve => {" +
                            "  requestAnimationFrame(() => {" +
                            "    setTimeout(resolve, 100);" +
                            "  });" +
                            "});"
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Ожидание анимации прервано");
        } catch (Exception e) {
            System.out.println("⚠️ Не удалось дождаться анимации: " + e.getMessage());
        }
    }
}