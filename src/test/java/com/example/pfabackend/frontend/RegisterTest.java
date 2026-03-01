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

public class RegisterTest {

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
    public void testRegister() {
        driver.get("http://localhost:3000/register");

        WebElement nameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("name"))
        );
        nameField.sendKeys("John Doe");

        driver.findElement(By.id("username")).sendKeys("johndoe123");
        driver.findElement(By.id("email")).sendKeys("johndoe@example.com");
        driver.findElement(By.id("password")).sendKeys("securepassword123");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".text-sm.text-center.text-gray-700, .text-sm.text-center.text-gray-200")
                )
        );
        Assert.assertNotNull(message.getText(), "No confirmation message displayed after register.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}