import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumDropdownTests {
    WebDriver driver;
    String baseUrl = "https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get(baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }

    @Test
    void dpopdownLeftClickTests() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement dropdown1 = driver.findElement(By.id("my-dropdown-1"));

        dropdown1.click();

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#my-dropdown-1 + .dropdown-menu")));

        List<String> items = menu.findElements(By.cssSelector("a.dropdown-item"))
                .stream()
                .map(e -> e.getText())
                .toList();

        assertTrue(items.contains("Action"));
        assertTrue(items.contains("Another action"));
        assertTrue(items.contains("Something else here"));
        assertTrue(items.contains("Separated link"));
    }

    @Test
    void dropdownRightClickTests() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));


        new Actions(driver)
                .contextClick(dropdown2)
                .perform();

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("context-menu-2")));

        List<WebElement> items = menu.findElements(By.cssSelector("a.dropdown-item"));
        assertFalse(items.isEmpty());
        assertEquals("Action", items.get(0).getText());
    }

    @Test
    void dropdownDoubleClickTests() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));

        new Actions(driver)
                .doubleClick(dropdown3)
                .perform();

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("context-menu-3")));

        List<WebElement> items = menu.findElements(By.cssSelector("a.dropdown-item"));
        assertFalse(items.isEmpty());
        assertEquals("Action", items.get(0).getText());

    }
}
