import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumDragAndDropTests {
    WebDriver driver;
    String baseUrl = "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";

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
    void dragAndDropTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement target = driver.findElement(By.id("target"));

        new Actions(driver)
                .dragAndDrop(draggable, target)
                .perform();

        Rectangle d = draggable.getRect();
        Rectangle t = target.getRect();

        boolean fullyInside =
                d.getX() >= t.getX() &&
                d.getY() >= t.getY() &&
                (d.getX() + d.getWidth()) <= (t.getX() + t.getWidth()) &&
                (d.getY() + d.getHeight()) <= (t.getY() + t.getHeight());

        assertTrue(fullyInside);


    }
}
