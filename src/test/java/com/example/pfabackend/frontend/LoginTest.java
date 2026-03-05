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
        usernameField.clear();
        usernameField.sendKeys("johndoe123");
        usernameField.sendKeys(org.openqa.selenium.Keys.TAB);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("securepassword123");
        passwordField.sendKeys(org.openqa.selenium.Keys.TAB);

        // Use CSS selector instead of className
        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[text()='Login']]")
                )
        );

        // Scroll into view + JS click as fallback
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", loginButton);

        try {
            loginButton.click();
        } catch (Exception e) {
            // Fallback to JS click if regular click fails
            js.executeScript("arguments[0].click();", loginButton);
        }

        // After clicking login, wait for the URL to change to /home
        wait.until(ExpectedConditions.urlContains("/home"));

        // Then wait for the element
        WebElement homeElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main.profile-page")));

        Assert.assertTrue(homeElement.isDisplayed(), "Login failed, home page not loaded.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}