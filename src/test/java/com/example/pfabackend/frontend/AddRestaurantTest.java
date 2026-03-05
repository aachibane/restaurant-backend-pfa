package com.example.pfabackend.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.util.List;

public class AddRestaurantTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    // Handles both <input> and <textarea>
    private void setReactInputValue(WebElement element, String value) {
        String tagName = element.getTagName().toLowerCase();
        String prototype = tagName.equals("textarea")
                ? "window.HTMLTextAreaElement.prototype"
                : "window.HTMLInputElement.prototype";

        js.executeScript(
                "var nativeValueSetter = Object.getOwnPropertyDescriptor(" + prototype + ", 'value').set;" +
                        "nativeValueSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                element, value
        );
    }

    // Injects a mock File into a file input and fires change event
    private void mockFileUpload(WebElement fileInput, String fileName, String mimeType) {
        js.executeScript(
                "var dt = new DataTransfer();" +
                        "var base64 = '/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8U" +
                        "HRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwh" +
                        "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAED" +
                        "ASIAAhEBAxEB/8QAFAABAAAAAAAAAAAAAAAAAAAACf/EABQQAQAAAAAAAAAAAAAAAAAAAAD/xAAUAQEA" +
                        "AAAAAAAAAAAAAAAAAAAA/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8AJQAB/9k=';" +
                        "var binary = atob(base64);" +
                        "var array = new Uint8Array(binary.length);" +
                        "for(var i = 0; i < binary.length; i++) { array[i] = binary.charCodeAt(i); }" +
                        "var file = new File([array], arguments[1], { type: arguments[2] });" +
                        "dt.items.add(file);" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'files').set;" +
                        "if (nativeInputValueSetter) {" +
                        "  nativeInputValueSetter.call(arguments[0], dt.files);" +
                        "} else {" +
                        "  Object.defineProperty(arguments[0], 'files', { value: dt.files, writable: false });" +
                        "}" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                fileInput, fileName, mimeType
        );
    }

    // Walks React fiber tree to find GeoLocation's onLocationChange prop and calls it
    private void mockLocation() {
        js.executeScript(
                "function findReactFiber(el) {" +
                        "  var key = Object.keys(el).find(k => k.startsWith('__reactFiber') || k.startsWith('__reactInternalInstance'));" +
                        "  return key ? el[key] : null;" +
                        "}" +
                        "function findHandlerInFiber(fiber, handlerName) {" +
                        "  var node = fiber;" +
                        "  while (node) {" +
                        "    if (node.memoizedProps && node.memoizedProps[handlerName]) {" +
                        "      return node.memoizedProps[handlerName];" +
                        "    }" +
                        "    node = node.return;" +
                        "  }" +
                        "  return null;" +
                        "}" +
                        "var allElements = document.querySelectorAll('*');" +
                        "for (var i = 0; i < allElements.length; i++) {" +
                        "  var fiber = findReactFiber(allElements[i]);" +
                        "  if (!fiber) continue;" +
                        "  var handler = findHandlerInFiber(fiber, 'onLocationChange');" +
                        "  if (handler) {" +
                        "    handler({ latitude: 33.5731, longitude: -7.5898 });" +
                        "    break;" +
                        "  }" +
                        "}"
        );
    }

    private void login() {
        driver.get("http://localhost:3000/login");

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        setReactInputValue(usernameField, "johndoe123");
        setReactInputValue(driver.findElement(By.id("password")), "securepassword123");

        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[text()='Login']]")
                )
        );
        js.executeScript("arguments[0].scrollIntoView(true);", loginButton);
        try {
            loginButton.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", loginButton);
        }

        wait.until(ExpectedConditions.urlContains("/home"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main.profile-page")));
    }

    @Test
    public void testLoginAndAddRestaurant() {
        login();

        driver.get("http://localhost:3000/restaurant/new");

        // Wait for form to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));

        // Fill all text fields
        setReactInputValue(driver.findElement(By.id("name")),        "My New Restaurant");
        setReactInputValue(driver.findElement(By.id("email")),       "newrestaurant@example.com");
        setReactInputValue(driver.findElement(By.id("cuisine")),     "Italian");
        setReactInputValue(driver.findElement(By.id("phoneNumber")), "1234567890");
        setReactInputValue(driver.findElement(By.id("description")), "A cozy Italian restaurant.");
        setReactInputValue(driver.findElement(By.id("instagram")),   "@newrestaurant");
        setReactInputValue(driver.findElement(By.id("priceRange")),  "$$");

        // Mock location via React fiber — injects { latitude, longitude } into React state
        mockLocation();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        // Mock file uploads — logo first, cover second (no ids on these inputs)
        List<WebElement> fileInputs = driver.findElements(By.cssSelector("input[type='file']"));
        mockFileUpload(fileInputs.get(0), "logo.jpg",  "image/jpeg");
        mockFileUpload(fileInputs.get(1), "cover.jpg", "image/jpeg");

        // Submit the form
        WebElement submitButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        js.executeScript("arguments[0].scrollIntoView(true);", submitButton);
        try {
            submitButton.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", submitButton);
        }

        // Assert success — matches your form's: bg-green-500 + role="alert"
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div[role='alert'].bg-green-500")
                )
        );
        Assert.assertTrue(successMessage.isDisplayed(), "Restaurant was not added successfully.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}