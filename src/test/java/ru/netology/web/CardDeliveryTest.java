package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    private String[] city = {"Санкт-Петербург", "Москва", "Казань", "Мурманск", "Вологда"};
    private LocalDate today = LocalDate.now();
    private LocalDate date = today.plusDays(3);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String validDate = date.format(formatter);

    @Test
    void shouldAcceptRequest() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldAcceptRequestIfThereIsHyphen() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Иванова-Петрова");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldAcceptRequestIfOnlyCapitalLetters() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("МАРГАРИТА КИРИЛЬЧУК");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    @Test
    void shouldAcceptRequestIfOnlySmallLetters() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("маргарита кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
    }

    //bug
    @Test
    void shouldNotAcceptRequestIfOnlyName() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    //bug
    @Test
    void shouldNotAcceptRequestIfBigAmountOfSymbols() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргаритаааааааааааааааааааааааааааааа Кирильчуууууук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotAcceptRequestIfInvalidSymbolsInName() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("*****");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotAcceptRequestIfEnglishName() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Margarita Kirilchuk");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotAcceptRequestIfNumbersInName() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("12345");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotAcceptRequestIfNoName() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement name = $("[data-test-id=name]");
        name.$(".input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotAcceptRequestIfMoreThan11Numbers() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+790627622021234");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotAcceptRequestIfLessThan11Numbers() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+790627622");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotAcceptRequestIfTelWithoutPlus() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotAcceptRequestIfLettersInNumber() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("маргарита");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotAcceptRequestIfInvalidSymbolsInTel() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("@##^%$*%");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotAcceptRequestIfNoTel() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement phone = $("[data-test-id=phone]");
        phone.$(".input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotAcceptRequestIfNoCityInTheList() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Выборг");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement city = $("[data-test-id=city]");
        city.$(".input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotAcceptRequestIfNumbersInCity() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("12345");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement city = $("[data-test-id=city]");
        city.$(".input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotAcceptRequestIfNoCity() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement city = $("[data-test-id=city]");
        city.$(".input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotAcceptRequestIfNotPossibleDate() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue("01.06.2020");
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement city = $("[data-test-id=date]");
        city.$(".input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotAcceptRequestIfNoAgreement() {
        open("http://0.0.0.0:7777/");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(validDate);
        $("[data-test-id=name] input").setValue("Маргарита Кирильчук");
        $("[data-test-id=phone] input").setValue("+79062762202");
        $$("button").find(exactText("Забронировать")).click();
        SelenideElement agreement = $("[data-test-id=agreement]");
        agreement.$("[role=presentation]").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }




}
