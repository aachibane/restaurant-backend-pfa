package com.example.pfabackend.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class RegisterTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("http://localhost:3000/register");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
            nameField.sendKeys("John Doe");

            WebElement usernameField = driver.findElement(By.id("username"));
            usernameField.sendKeys("johndoe123");

            WebElement emailField = driver.findElement(By.id("email"));
            emailField.sendKeys("johndoe@example.com");

            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("securepassword123");

            WebElement phoneField = driver.findElement(By.id("phone"));
            phoneField.sendKeys("1234567890");
            Thread.sleep(6000);

            WebElement registerButton = driver.findElement(By.xpath("//button[@type='submit']"));
            registerButton.click();

            Thread.sleep(6000);

            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".text-sm.text-center.text-gray-700, .text-sm.text-center.text-gray-200")
            ));
            System.out.println("Message: " + message.getText());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
