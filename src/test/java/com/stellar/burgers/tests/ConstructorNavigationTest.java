package com.stellar.burgers.tests;

import com.stellar.burgers.pages.ConstructorPage;
import com.stellar.burgers.pages.MainPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Конструктор бургеров")
@Feature("Полная навигация по конструктору")
public class ConstructorNavigationTest extends BaseTest {

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