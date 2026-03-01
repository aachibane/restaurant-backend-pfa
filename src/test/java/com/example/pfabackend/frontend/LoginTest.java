package com.example.pfabackend.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testLogin() {
        driver.get("http://localhost:3000/login");

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("johndoe123");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("securepassword123");

        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("login-button"))
        );
        loginButton.click();

        WebElement homeElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("restaurant-button"))
        );
        Assert.assertTrue(homeElement.isDisplayed(), "Login failed, home page not loaded.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}