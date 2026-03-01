package com.example.pfabackend.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.time.Duration;

public class AddRestaurantTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @Test
    public void testLoginAndAddRestaurant() throws InterruptedException {
        driver.get("http://localhost:3000/login");

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.sendKeys("johndoe123");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("securepassword123");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("login-button")));
        loginButton.click();

        WebElement restaurantButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("restaurant-button")));
        Assert.assertTrue(restaurantButton.isDisplayed(), "Login failed, restaurant button is not displayed.");
        restaurantButton.click();

        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));
        nameInput.sendKeys("My New Restaurant");

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("newrestaurant@example.com");

        /*WebElement mapElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("map"))); // Replace with actual map element ID
        Actions actions = new Actions(driver);
        actions.moveToElement(mapElement, 200, 300) // Adjust the coordinates (200, 300) as needed to the location on the map
                .doubleClick()
                .perform();
        */
        Thread.sleep(10000);
        WebElement cuisineInput = driver.findElement(By.id("cuisine"));
        cuisineInput.sendKeys("Italian");

        WebElement phoneInput = driver.findElement(By.id("phoneNumber"));
        phoneInput.sendKeys("1234567890");

        WebElement descriptionInput = driver.findElement(By.id("description"));
        descriptionInput.sendKeys("A cozy Italian restaurant in the heart of the city.");

        WebElement instagramInput = driver.findElement(By.id("instagram"));
        instagramInput.sendKeys("@newrestaurant");

        WebElement priceRangeInput = driver.findElement(By.id("priceRange"));
        priceRangeInput.sendKeys("$$");

        String basePath = Paths.get("").toAbsolutePath().toString();
        WebElement logoFileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(), 'Logo')]/following-sibling::input[@type='file']")));
        String logoPath = basePath + "/photos/frenchLogo1.jpg";
        logoFileInput.sendKeys(logoPath);
        WebElement coverFileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[contains(text(), 'Cover')]/following-sibling::input[@type='file']")));
        String coverPath = basePath + "/photos/1l.jpg";
        coverFileInput.sendKeys(coverPath);
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        Assert.assertTrue(successMessage.isDisplayed(), "Success message is not displayed");
        WebElement restaurantName = driver.findElement(By.xpath("//h2[contains(text(), 'My New Restaurant')]"));
        Assert.assertTrue(restaurantName.isDisplayed(), "Restaurant name is not displayed after submission");
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}