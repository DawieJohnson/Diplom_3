package com.stellar.burgers.tests;

import com.stellar.burgers.data.TestData;
import com.stellar.burgers.data.User;
import com.stellar.burgers.pages.*;
import io.qameta.allure.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

@Epic("Авторизация пользователя")
@Feature("Различные способы входа в систему")
public class LoginTest extends BaseTest {

    @Test
    @Story("Вход через кнопку 'Войти в аккаунт' на главной")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет вход через основную кнопку на главной странице")
    public void testLoginFromMainPageButton() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());

        User realUser = TestData.getExistingUserForLogin();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded(),
                    "Главная страница не загрузилась");
        });

        Allure.step("2. Нажать кнопку 'Войти в аккаунт'", step -> {
            mainPage.clickLoginButton();
        });

        Allure.step("3. Проверить загрузку страницы входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Страница входа не загрузилась после клика на кнопку");
        });

        Allure.step("4. Заполнить форму входа реальными данными", step -> {
            loginPage.login(realUser.getEmail(), realUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            WebDriverWait loginWait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            try {
                loginWait.until(ExpectedConditions.visibilityOf(mainPage.getPlaceOrderButton()));
            } catch (Exception e) {
                System.out.println("Не удалось найти кнопку через явное ожидание, используем стандартную проверку");
            }

            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел в систему. Кнопка 'Оформить заказ' не отображается.");

            assertTrue(mainPage.isPlaceOrderButtonVisible(),
                    "Кнопка оформления заказа не видна после входа.");
        });
    }

    @Test
    @Story("Вход через кнопку 'Личный кабинет'")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет вход через кнопку личного кабинета")
    public void testLoginFromPersonalAccountButton() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());

        User realUser = TestData.getExistingUserForLogin();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded(),
                    "Главная страница не загрузилась");
        });

        Allure.step("2. Нажать кнопку 'Личный кабинет'", step -> {
            mainPage.clickPersonalAccountButton();
        });

        Allure.step("3. Проверить загрузку страницы входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Страница входа не загрузилась после клика на 'Личный кабинет'");
        });

        Allure.step("4. Заполнить форму входа реальными данными", step -> {
            loginPage.login(realUser.getEmail(), realUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            try {
                wait.until(d -> mainPage.isUserLoggedIn());
            } catch (Exception e) {
                // Игнорируем, проверка будет выполнена в assert
            }

            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел в систему через личный кабинет");
        });
    }

    @Test
    @Story("Вход через кнопку в форме регистрации")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход на страницу входа из формы регистрации")
    public void testLoginFromRegistrationForm() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        User realUser = TestData.getExistingUserForLogin();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
        });

        Allure.step("2. Перейти на страницу входа", step -> {
            mainPage.clickLoginButton();
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Страница входа не загрузилась");
        });

        Allure.step("3. Перейти на страницу регистрации", step -> {
            loginPage.clickRegistrationLink();
            assertTrue(registrationPage.isRegistrationPageLoaded(),
                    "Страница регистрации не загрузилась");
        });

        Allure.step("4. Нажать ссылку 'Войти' на странице регистрации", step -> {
            registrationPage.clickLoginLink();
        });

        Allure.step("5. Проверить возврат на страницу входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Не вернулись на страницу входа после клика на ссылку 'Войти'");
        });

        Allure.step("6. Заполнить форму входа", step -> {
            loginPage.login(realUser.getEmail(), realUser.getPassword());
        });

        Allure.step("7. Проверить успешный вход", step -> {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            try {
                wait.until(d -> mainPage.isUserLoggedIn());
            } catch (Exception e) {
                // Игнорируем, проверка будет выполнена в assert
            }

            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел после перехода из формы регистрации");
        });
    }

    @Test
    @Story("Вход через кнопку в форме восстановления пароля")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход на страницу входа из формы восстановления пароля")
    public void testLoginFromForgotPasswordForm() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(getDriver());

        User realUser = TestData.getExistingUserForLogin();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
        });

        Allure.step("2. Перейти на страницу входа", step -> {
            mainPage.clickLoginButton();
            assertTrue(loginPage.isLoginPageLoaded());
        });

        Allure.step("3. Перейти на страницу восстановления пароля", step -> {
            loginPage.clickForgotPasswordLink();
            assertTrue(forgotPasswordPage.isForgotPasswordPageLoaded(),
                    "Страница восстановления пароля не загрузилась");
        });

        Allure.step("4. Нажать ссылку 'Войти' на странице восстановления пароля", step -> {
            forgotPasswordPage.clickLoginLink();
        });

        Allure.step("5. Проверить возврат на страницу входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Не вернулись на страницу входа после клика на ссылку 'Войти'");
        });

        Allure.step("6. Заполнить форму входа", step -> {
            loginPage.login(realUser.getEmail(), realUser.getPassword());
        });

        Allure.step("7. Проверить успешный вход", step -> {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            try {
                wait.until(d -> mainPage.isUserLoggedIn());
            } catch (Exception e) {
                // Игнорируем, проверка будет выполнена в assert
            }

            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел после перехода из формы восстановления пароля");
        });
    }
}