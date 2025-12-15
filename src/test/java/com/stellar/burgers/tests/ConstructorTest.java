package com.stellar.burgers.tests;

import com.stellar.burgers.pages.ConstructorPage;
import com.stellar.burgers.pages.MainPage;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Конструктор бургеров")
@Feature("Навигация по разделам конструктора")
public class ConstructorTest extends BaseTest {

    @DataProvider(name = "constructorSections")
    public Object[][] getConstructorSections() {
        return new Object[][] {
                {"Булки", "buns"},
                {"Соусы", "sauces"},
                {"Начинки", "fillings"}
        };
    }

    @Test(dataProvider = "constructorSections")
    @Story("Навигация по разделам конструктора")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет переход к разделу '{0}'")
    public void testConstructorSectionNavigation(String sectionName, String sectionType) {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Открыть главную страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded(), "Главная страница не загрузилась");
            assertTrue(constructorPage.isBunsTabActive(),
                    "Раздел 'Булки' должен быть активным при загрузке");
        });

        Allure.step("2. Перейти в раздел '" + sectionName + "'", step -> {
            switch (sectionType) {
                case "sauces":
                    constructorPage.clickSaucesSection();
                    break;
                case "fillings":
                    constructorPage.clickFillingsSection();
                    break;
                default:
                    constructorPage.clickBunsSection();
                    break;
            }
        });

        Allure.step("3. Проверить активность раздела '" + sectionName + "'", step -> {
            boolean isActive = false;
            switch (sectionType) {
                case "buns":
                    isActive = constructorPage.isBunsTabActive();
                    break;
                case "sauces":
                    isActive = constructorPage.isSaucesTabActive();
                    break;
                case "fillings":
                    isActive = constructorPage.isFillingsTabActive();
                    break;
            }
            assertTrue(isActive,
                    "Раздел '" + sectionName + "' должен стать активным после клика");
        });
    }

    @Test
    @Story("Последовательная навигация по всем разделам")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет полный цикл навигации по всем разделам конструктора")
    public void testCompleteNavigationCycle() {
        MainPage mainPage = new MainPage(getDriver());
        ConstructorPage constructorPage = new ConstructorPage(getDriver());

        Allure.step("1. Загрузить страницу", step -> {
            assertTrue(mainPage.isMainPageLoaded());
            assertTrue(constructorPage.isBunsTabActive(), "Булки должны быть активны изначально");
        });

        Allure.step("2. Перейти к Соусам", step -> {
            constructorPage.clickSaucesSection();
            assertTrue(constructorPage.isSaucesTabActive(), "Соусы должны стать активными");
        });

        Allure.step("3. Перейти к Начинкам", step -> {
            constructorPage.clickFillingsSection();
            assertTrue(constructorPage.isFillingsTabActive(), "Начинки должны стать активными");
        });

        Allure.step("4. Вернуться к Булочкам", step -> {
            constructorPage.clickBunsSection();
            assertTrue(constructorPage.isBunsTabActive(), "Булки должны снова стать активными");
        });
    }
}