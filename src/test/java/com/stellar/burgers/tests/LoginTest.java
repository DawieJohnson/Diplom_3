package com.stellar.burgers.tests;

import com.stellar.burgers.api.UserApiClient;
import com.stellar.burgers.data.User;
import com.stellar.burgers.pages.*;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Авторизация пользователя")
@Feature("Различные способы входа в систему")
public class LoginTest extends BaseTest {

    private User testUser;

    @BeforeMethod
    public void prepareTestUser() {
        // Создаем пользователя через API перед каждым тестом
        testUser = UserApiClient.createRandomUser();
        setCurrentUser(testUser);
    }

    @Test
    @Story("Вход через кнопку 'Войти в аккаунт' на главной")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет вход через основную кнопку на главной странице")
    public void testLoginFromMainPageButton() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());

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

        Allure.step("4. Заполнить форму входа данными пользователя, созданного через API", step -> {
            loginPage.login(testUser.getEmail(), testUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел в систему. Кнопка 'Оформить заказ' не отображается.");
        });
    }

    @Test
    @Story("Вход через кнопку 'Личный кабинет'")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет вход через кнопку личного кабинета")
    public void testLoginFromPersonalAccountButton() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());

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

        Allure.step("4. Заполнить форму входа данными пользователя, созданного через API", step -> {
            loginPage.login(testUser.getEmail(), testUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел в систему через личный кабинет");
        });
    }

    @Test
    @Story("Вход через кнопку в форме регистрации")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход на страницу входа из формы регистрации и вход")
    public void testLoginFromRegistrationForm() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        Allure.step("1. Перейти на страницу регистрации через главную", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            mainPage.clickLoginButton();
            assertTrue(loginPage.isLoginPageLoaded());
            loginPage.clickRegistrationLink();
            assertTrue(registrationPage.isRegistrationPageLoaded());
        });

        Allure.step("2. Нажать ссылку 'Войти' на странице регистрации", step -> {
            registrationPage.clickLoginLink();
        });

        Allure.step("3. Проверить возврат на страницу входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Не вернулись на страницу входа после клика на ссылку 'Войти'");
        });

        Allure.step("4. Заполнить форму входа данными пользователя, созданного через API", step -> {
            loginPage.login(testUser.getEmail(), testUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел после перехода из формы регистрации");
        });
    }

    @Test
    @Story("Вход через кнопку в форме восстановления пароля")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход на страницу входа из формы восстановления пароля и вход")
    public void testLoginFromForgotPasswordForm() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(getDriver());

        Allure.step("1. Перейти на страницу восстановления пароля через главную", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            mainPage.clickLoginButton();
            assertTrue(loginPage.isLoginPageLoaded());
            loginPage.clickForgotPasswordLink();
            assertTrue(forgotPasswordPage.isForgotPasswordPageLoaded());
        });

        Allure.step("2. Нажать ссылку 'Войти' на странице восстановления пароля", step -> {
            forgotPasswordPage.clickLoginLink();
        });

        Allure.step("3. Проверить возврат на страницу входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "Не вернулись на страницу входа после клика на ссылку 'Войти'");
        });

        Allure.step("4. Заполнить форму входа данными пользователя, созданного через API", step -> {
            loginPage.login(testUser.getEmail(), testUser.getPassword());
        });

        Allure.step("5. Проверить успешный вход", step -> {
            assertTrue(mainPage.isUserLoggedIn(),
                    "Пользователь не вошел после перехода из формы восстановления пароля");
        });
    }
}