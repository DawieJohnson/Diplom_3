package com.stellar.burgers.pages;

import com.stellar.burgers.config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        try {
            new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                    .until(webDriver ->
                            ((JavascriptExecutor) webDriver)
                                    .executeScript("return document.readyState")
                                    .equals("complete"));
        } catch (Exception e) {
        }
    }

    protected void waitAndClick(WebElement element) {
        int maxAttempts = 2;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                System.out.println("🔄 Попытка клика #" + (attempt + 1));

                WebElement clickableElement = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                        .until(ExpectedConditions.refreshed(
                                ExpectedConditions.elementToBeClickable(element)
                        ));

                try {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                            clickableElement
                    );
                } catch (Exception jsEx) {
                    // Если JS скролл не сработал, пробуем Actions
                    System.out.println("⚠️ JS scroll не сработал, использую Actions");
                }

                clickableElement.click();
                System.out.println("✅ Успешный клик");
                return;

            } catch (StaleElementReferenceException e) {
                attempt++;
                System.out.println("🔄 StaleElement, попытка " + attempt + " из " + maxAttempts);

                if (attempt >= maxAttempts) {
                    throw new RuntimeException("Не удалось кликнуть на элемент после " +
                            maxAttempts + " попыток", e);
                }

                new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                        .until(ExpectedConditions.refreshed(
                                ExpectedConditions.stalenessOf(element)
                        ));

                PageFactory.initElements(driver, this);

            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Элемент перекрыт, пробую клик через Actions");
                try {
                    new org.openqa.selenium.interactions.Actions(driver)
                            .moveToElement(element)
                            .click()
                            .perform();
                    return;
                } catch (Exception actionsEx) {
                    System.out.println("⚠️ Actions не сработал, пробую JavaScript клик");
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].click();", element);
                    return;
                }
            }
        }
    }

    protected void waitAndSendKeys(WebElement element, String text) {
        int maxAttempts = 2;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                WebElement visibleElement = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                        .until(ExpectedConditions.refreshed(
                                ExpectedConditions.visibilityOf(element)
                        ));

                visibleElement.clear();

                visibleElement.sendKeys(text);
                System.out.println("✅ Введен текст: " +
                        (text.length() > 3 ? text.substring(0, 3) + "..." : text));
                return;

            } catch (StaleElementReferenceException e) {
                attempt++;
                System.out.println("🔄 StaleElement при вводе текста, попытка " + attempt);

                if (attempt >= maxAttempts) {
                    throw new RuntimeException("Не удалось ввести текст после " +
                            maxAttempts + " попыток", e);
                }

                new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                        .until(ExpectedConditions.refreshed(
                                ExpectedConditions.stalenessOf(element)
                        ));

                PageFactory.initElements(driver, this);
            }
        }
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                    .until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getElementText(WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOf(element))
                .getText();
    }
}