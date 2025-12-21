package com.stellar.burgers.tests;

import com.stellar.burgers.api.UserApiClient;
import com.stellar.burgers.data.User;
import com.stellar.burgers.pages.*;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

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

        // Для UI регистрации создаем пользователя через TestData
        User userForUiRegistration = com.stellar.burgers.data.TestData.getValidUser();

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
                    userForUiRegistration.getName(),
                    userForUiRegistration.getEmail(),
                    userForUiRegistration.getPassword()
            );
        });

        Allure.step("7. Проверить переход на страницу входа после регистрации", step -> {
            assertTrue(loginPage.isLoginPageLoaded(),
                    "После регистрации не произошел переход на страницу входа");
        });

        // Сохраняем пользователя для очистки через API
        // Ждем немного перед попыткой логина, чтобы сервер обработал регистрацию
        Allure.step("8. Сохранить пользователя для последующей очистки", step -> {
            try {
                Thread.sleep(2000); // Даем время серверу обработать регистрацию
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            setCurrentUser(userForUiRegistration);
            System.out.println("✅ Пользователь сохранен для очистки: " + userForUiRegistration.getEmail());
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

        // Используем данные для теста с некорректным паролем
        User invalidUser = com.stellar.burgers.data.TestData.getUserWithShortPassword();

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
            assertFalse(errorText.isEmpty(), "Текст ошибки пустой");

            // Проверяем, что текст ошибки содержит ключевые слова
            assertTrue(errorText.toLowerCase().contains("некорректный") ||
                            errorText.toLowerCase().contains("парол"),
                    "Текст ошибки не соответствует ожидаемому: '" + errorText + "'");
            System.out.println("ℹ️ Проверен текст ошибки: " + errorText);
        });

        // Не сохраняем пользователя для очистки, так как регистрация не прошла
    }
}