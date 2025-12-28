import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumNavigatorTests {
    WebDriver driver;
    String baseUrl = "https://bonigarcia.dev/selenium-webdriver-java";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
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
    void navigationPage1Tests() {
        driver.get(baseUrl + "/navigation1.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement h1 = driver.findElement(By.cssSelector("h1.display-6"));

        assertEquals("Navigation example", h1.getText());

        WebElement p1 = driver.findElement(By.xpath("//a[text()='1']/.."));
        assertTrue(p1.getAttribute("class").contains("active"));

        WebElement previous = driver.findElement(By.xpath("//a[text()='Previous']/.."));
        assertTrue(previous.getAttribute("class").contains("disabled"));

        WebElement next = driver.findElement(By.xpath("//a[text()='Next']/.."));

        next.click();
        wait.until(ExpectedConditions.urlContains("navigation2.html"));

        assertTrue(driver.getCurrentUrl().endsWith("navigation2.html"));
    }

    @Test
    void navigationPage1BackToIndexClickTest() {
        driver.get(baseUrl + "/navigation1.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.cssSelector("a[href='index.html']")).click();
        wait.until(ExpectedConditions.urlContains("index.html"));

        assertTrue(driver.getCurrentUrl().endsWith("/index.html"));
    }

    @Test
    void navigationPage2Tests() {
        driver.get(baseUrl + "/navigation1.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement page1P2 = driver.findElement(By.xpath("//a[text()='2']/.."));
        page1P2.click();
        wait.until(ExpectedConditions.urlContains("navigation2.html"));

        assertTrue(driver.getCurrentUrl().endsWith("/navigation2.html"));

        WebElement p2 = driver.findElement(By.xpath("//a[text()='2']/.."));
        assertTrue(p2.getAttribute("class").contains("active"));

        WebElement previous = driver.findElement(By.xpath("//a[text()='Previous']/.."));
        previous.click();

        wait.until(ExpectedConditions.urlContains("navigation1"));
        assertTrue(driver.getCurrentUrl().endsWith("navigation1.html"));
    }

    @Test
    void navigationPage2NextText() {
        driver.get(baseUrl + "/navigation2.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.xpath("//a[text()='Next']/..")).click();

        wait.until(ExpectedConditions.urlContains("navigation3.html"));

        assertTrue(driver.getCurrentUrl().endsWith("/navigation3.html"));

        WebElement p3 = driver.findElement(By.xpath("//a[text()='3']/.."));
        assertTrue(p3.getAttribute("class").contains("active"));

        WebElement next = driver.findElement(By.xpath("//a[text()='Next']/.."));
        assertTrue(next.getAttribute("class").contains("disabled"));

        WebElement previous = driver.findElement(By.xpath("//a[text()='Previous']/.."));
        previous.click();

        wait.until(ExpectedConditions.urlContains("navigation2"));
        assertTrue(driver.getCurrentUrl().endsWith("navigation2.html"));
    }
}
