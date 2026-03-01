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

public class AddRestaurantTest {

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginAndAddRestaurant() {
        driver.get("http://localhost:3000/login");

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("johndoe123");
        driver.findElement(By.id("password")).sendKeys("securepassword123");

        wait.until(ExpectedConditions.elementToBeClickable(By.className("login-button"))).click();

        WebElement restaurantButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("restaurant-button"))
        );
        Assert.assertTrue(restaurantButton.isDisplayed(), "Login failed.");
        restaurantButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("name"))).sendKeys("My New Restaurant");
        driver.findElement(By.id("email")).sendKeys("newrestaurant@example.com");
        driver.findElement(By.id("cuisine")).sendKeys("Italian");
        driver.findElement(By.id("phoneNumber")).sendKeys("1234567890");
        driver.findElement(By.id("description")).sendKeys("A cozy Italian restaurant.");
        driver.findElement(By.id("instagram")).sendKeys("@newrestaurant");
        driver.findElement(By.id("priceRange")).sendKeys("$$");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success"))
        );
        Assert.assertTrue(successMessage.isDisplayed(), "Success message not displayed.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}