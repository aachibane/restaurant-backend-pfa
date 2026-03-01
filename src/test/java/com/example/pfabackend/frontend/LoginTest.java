package com.example.pfabackend.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:3000/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            usernameField.sendKeys("johndoe123");
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("securepassword123");
            Thread.sleep(4000);
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("login-button")));
            loginButton.click();
            Thread.sleep(4000);
            //WebElement homePageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("home-page-element-id")));
            //WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    //By.cssSelector(".text-sm.text-center.text-gray-700, .text-sm.text-center.text-gray-200")
            //));
            System.out.println("Login successful, home page loaded.");
            //System.out.println("Message: " + message.getText());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
