import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    WebDriver driver;
    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }
    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    public void unregisteredUser() {
        var randomLogin = DataGenerator.getRandomLogin();
        var randomPassword = DataGenerator.getRandomPassword();
        $("[data-test-id=login] input").setValue(randomLogin);
        $("[data-test-id=password] input").setValue(randomPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан логин или пароль"));

    }
    @Test
    public void registeredUserWithActiveStatus(){
        var user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
        $(By.tagName("h2")).shouldBe(Condition.visible).shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    public void registeredAndBlockedUser(){
        var user = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Пользователь заблокирован"));

    }

    @Test
    public void loginWithInvalidUsername(){
        var user = DataGenerator.Registration.getRegisteredUser("active");
        var randomLogin = DataGenerator.getRandomLogin();
        $("[data-test-id=login] input").setValue(randomLogin);
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void loginWithInvalidPassword(){
        var user = DataGenerator.Registration.getRegisteredUser("active");
        var randomPassword = DataGenerator.getRandomPassword();
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(randomPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void loginWithEmptyPassword(){
        var user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue("");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=password]").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void loginWithEmptyUsername(){
        var user = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").setValue("");
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=login]").shouldHave(Condition.text("Поле обязательно для заполнения"));

    }

}
