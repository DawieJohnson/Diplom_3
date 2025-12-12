package com.stellar.burgers.tests;

import com.stellar.burgers.data.TestData;
import com.stellar.burgers.data.User;
import com.stellar.burgers.pages.*;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Регистрация пользователя")
@Feature("Функционал регистрации")
public class RegistrationTest extends BaseTest {

    @Test
    @Story("Успешная регистрация нового пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет успешную регистрацию пользователя с валидными данными")
    public void testSuccessfulRegistration() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        User validUser = TestData.getValidUser();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded(), "Главная страница не загрузилась");
        });

        Allure.step("2. Нажать кнопку 'Войти в аккаунт'", step -> {
            mainPage.clickLoginButton();
        });

        Allure.step("3. Проверить загрузку страницы входа", step -> {
            assertTrue(loginPage.isLoginPageLoaded(), "Страница входа не загрузилась");
        });

        Allure.step("4. Нажать ссылку 'Зарегистрироваться'", step -> {
            loginPage.clickRegistrationLink();
        });

        Allure.step("5. Проверить загрузку страницы регистрации", step -> {
            assertTrue(registrationPage.isRegistrationPageLoaded(),
                    "Страница регистрации не загрузилась");
        });

        Allure.step("6. Заполнить форму регистрации валидными данными", step -> {
            registrationPage.register(
                    validUser.getName(),
                    validUser.getEmail(),
                    validUser.getPassword()
            );
        });

        Allure.step("7. Проверить переход на страницу входа после регистрации", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "После регистрации не произошел переход на страницу входа");
        });
    }

    @Test
    @Story("Регистрация с некорректным паролем")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет отображение ошибки при вводе пароля меньше 6 символов")
    public void testRegistrationWithInvalidPassword() {
        MainPage mainPage = new MainPage(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        RegistrationPage registrationPage = new RegistrationPage(getDriver());

        User invalidUser = TestData.getUserWithShortPassword();

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
        });

        Allure.step("2. Перейти на страницу регистрации", step -> {
            mainPage.clickLoginButton();
            loginPage.clickRegistrationLink();
            assertTrue(registrationPage.isRegistrationPageLoaded());
        });

        Allure.step("3. Заполнить форму регистрации с паролем из 5 символов", step -> {
            registrationPage.register(
                    invalidUser.getName(),
                    invalidUser.getEmail(),
                    invalidUser.getPassword()
            );
        });

        Allure.step("4. Проверить отображение и текст ошибки", step -> {
            assertTrue(registrationPage.isPasswordErrorDisplayed(),
                    "Ошибка для некорректного пароля не отображается");

            String errorText = registrationPage.getPasswordErrorText();
            assertTrue(!errorText.isEmpty(), "Текст ошибки пустой");

            System.out.println("Текст ошибки пароля: " + errorText);

            assertTrue(errorText.length() > 0,
                    "Текст ошибки должен содержать сообщение о некорректном пароле");
        });
    }
}