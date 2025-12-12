package com.stellar.burgers.tests;

import com.stellar.burgers.pages.ConstructorPage;
import com.stellar.burgers.pages.MainPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Конструктор бургеров")
@Feature("Навигация по разделам конструктора")
public class ConstructorTest extends BaseTest {

    @Test
    @Story("Переход к разделу 'Булки'")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход и отображение раздела 'Булки'")
    public void testBunsSectionNavigation() {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Открыть главную страницу и проверить начальное состояние", step -> {
            assertTrue(mainPage.isMainPageLoaded(), "Главная страница не загрузилась");
            assertTrue(constructorPage.isBunsTabActive(),
                    "Раздел 'Булки' должен быть активным при загрузке");
        });

        Allure.step("2. Перейти в раздел 'Соусы' и проверить смену активности", step -> {
            constructorPage.clickSaucesSection();
            assertTrue(constructorPage.isSaucesTabActive(),
                    "Раздел 'Соусы' должен стать активным после клика");
            assertTrue(constructorPage.isBunsTabInactive(),
                    "Раздел 'Булки' должен стать неактивным после перехода к 'Соусам'");
        });

        Allure.step("3. Вернуться в раздел 'Булки' и проверить смену активности", step -> {
            constructorPage.clickBunsSection();
            assertTrue(constructorPage.isBunsTabActive(),
                    "Раздел 'Булки' должен стать активным после клика");
            assertTrue(constructorPage.isSaucesTabInactive(),
                    "Раздел 'Соусы' должен стать неактивным после возврата к 'Булкам'");
        });
    }

    @Test
    @Story("Переход к разделу 'Соусы'")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход и отображение раздела 'Соусы'")
    public void testSaucesSectionNavigation() {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            assertTrue(constructorPage.isBunsTabActive(),
                    "Раздел 'Булки' должен быть активным при загрузке");
        });

        Allure.step("2. Нажать на раздел 'Соусы' и проверить активность", step -> {
            constructorPage.clickSaucesSection();
            assertTrue(constructorPage.isSaucesTabActive(),
                    "Раздел 'Соусы' должен стать активным после клика");
            assertTrue(constructorPage.isBunsTabInactive(),
                    "Раздел 'Булки' должен стать неактивным");
        });
    }

    @Test
    @Story("Переход к разделу 'Начинки'")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход и отображение раздела 'Начинки'")
    public void testFillingsSectionNavigation() {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            assertTrue(constructorPage.isBunsTabActive(),
                    "Раздел 'Булки' должен быть активным при загрузке");
        });

        Allure.step("2. Нажать на раздел 'Начинки' и проверить активность", step -> {
            constructorPage.clickFillingsSection();
            assertTrue(constructorPage.isFillingsTabActive(),
                    "Раздел 'Начинки' должен стать активным после клика");
            assertTrue(constructorPage.isBunsTabInactive(),
                    "Раздел 'Булки' должен стать неактивным");
        });
    }

    @Test
    @Story("Последовательная навигация по всем разделам")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет полный цикл навигации по всем разделам")
    public void testCompleteNavigationCycle() {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Загрузить страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            assertTrue(constructorPage.isBunsTabActive(), "Булки активны изначально");
        });

        Allure.step("2. Перейти к Соусам", step -> {
            constructorPage.clickSaucesSection();
            assertTrue(constructorPage.isSaucesTabActive(), "Соусы активны");
            assertTrue(constructorPage.isBunsTabInactive(), "Булки неактивны");
        });

        Allure.step("3. Перейти к Начинкам", step -> {
            constructorPage.clickFillingsSection();
            assertTrue(constructorPage.isFillingsTabActive(), "Начинки активны");
            assertTrue(constructorPage.isSaucesTabInactive(), "Соусы неактивны");
        });

        Allure.step("4. Вернуться к Булочкам", step -> {
            constructorPage.clickBunsSection();
            assertTrue(constructorPage.isBunsTabActive(), "Булки снова активны");
            assertTrue(constructorPage.isFillingsTabInactive(), "Начинки неактивны");
        });
    }
}