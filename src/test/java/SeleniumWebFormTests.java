import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumWebFormTests {
    WebDriver driver;
    String baseUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";

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

    @ParameterizedTest
    @CsvSource({
            "12345",
            "!@#$%^&*",
            "abc12345",
            "тест-12345",
            "test0-123",
            "A_b-C.12345"
    })
    void textInputShouldAcceptVariousValues(String input) {
        WebElement element = driver.findElement(By.cssSelector("[name='my-text']"));

        element.clear();
        element.sendKeys(input);

        assertEquals(input, element.getAttribute("value"));
    }

    @ParameterizedTest
    @CsvSource({
            "qwerty123",
            "P@ssw0rd",
            "123456",
            "тест-12345",
            "test0-123",
            "A_b-C.12345"
    })

    void passwordInputTests(String password) {
        WebElement pwd = driver.findElement(By.cssSelector("[name='my-password']"));

        assertEquals("password", pwd.getAttribute("type"));

        pwd.clear();
        pwd.sendKeys(password);

        assertEquals(password, pwd.getAttribute("value"));
    }

    @ParameterizedTest
    @CsvSource({
            "12345",
            "!@#$%^&*",
            "abc12345",
            "тест-12345",
            "test0-123",
            "A_b-C.12345"
    })
    void textAreaParameterizedTests(String text) {
        WebElement ta = driver.findElement(By.cssSelector("[name='my-textarea']"));

        ta.clear();
        ta.sendKeys(text);

        assertEquals(text, ta.getAttribute("value"));
    }

    @Test
    void textAreaMultilineTest() {
        WebElement ta = driver.findElement(By.cssSelector("[name='my-textarea']"));

        ta.clear();
        ta.sendKeys("Line1");
        ta.sendKeys(Keys.ENTER);
        ta.sendKeys("Line2");

        assertEquals("Line1\nLine2", ta.getAttribute("value"));
    }

    @Test
    void disableInputIsNotEnabledAndNotNullTests() {
        WebElement di = driver.findElement(By.cssSelector("[name='my-disabled']"));

        assertFalse(di.isEnabled());
        assertNotNull(di.getAttribute("disabled"));
    }

    @Test
    void disableInputException() {
        WebElement di = driver.findElement(By.cssSelector("[name='my-disabled']"));

        assertThrows(WebDriverException.class, () -> di.sendKeys("123"));
    }

    @Test
    void readonlyInputShouldNotChangeValue() {
        WebElement ri = driver.findElement(By.cssSelector("[name='my-readonly']"));

        assertNotNull(ri.getAttribute("readonly"));
        assertTrue(ri.isEnabled());

        String original = ri.getAttribute("value");

        try {
            ri.sendKeys("Test123");
        } catch (WebDriverException ignored) { }

        String actual = ri.getAttribute("value");
        assertEquals(original, actual);
    }

    @Test
    void dropdownSelectDefoltOption() {
        WebElement element = driver.findElement(By.cssSelector("[name='my-select']"));
        Select select = new Select(element);

        String selectedText = select.getFirstSelectedOption().getText();
        assertEquals("Open this select menu", selectedText);
    }

    @Test
    void dropdownSelectContainOptionsTests() {
        Select select = new Select(driver.findElement(By.cssSelector("[name='my-select']")));

        List<String> values = select.getOptions()
                .stream()
                .map(v -> v.getAttribute("value"))
                .toList();

        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
        assertTrue(values.contains("3"));
    }

    @Test
    void dropdownSelectSelectionTest() {
        WebElement element = driver.findElement(By.cssSelector("[name='my-select']"));

        Select select = new Select(element);

        select.selectByValue("2");
        String selectValue = select.getFirstSelectedOption().getAttribute("value");

        assertEquals("2", selectValue);
    }

    @Test
    void dropdownDatalistContainOptionsTest() {
        WebElement dataList = driver.findElement(By.cssSelector("datalist#my-options"));

        List<String> values = dataList.findElements(By.tagName("option"))
                .stream()
                .map(v -> v.getAttribute("value"))
                .toList();

        assertTrue(values.contains("San Francisco"));
        assertTrue(values.contains("New York"));
        assertTrue(values.contains("Seattle"));
        assertTrue(values.contains("Los Angeles"));
        assertTrue(values.contains("Chicago"));
    }

    @Test
    void dropdownDatalistInputTest() {
        WebElement dataList = driver.findElement(By.cssSelector("[name='my-datalist']"));

        String input = "Chicago";
        dataList.clear();
        dataList.sendKeys(input);

        assertEquals(input, dataList.getAttribute("value"));
    }

    @Test
    void fileInputTypeFileTest() {
        WebElement fileInput = driver.findElement(By.cssSelector("[name='my-file']"));

        assertEquals("file", fileInput.getAttribute("type"));
    }

    @Test
    void fileInputAcceptFileTest() {
        WebElement fileInput = driver.findElement(By.cssSelector("[name='my-file']"));

        File file = new File("src/test/resources/mando.jpg");
        fileInput.sendKeys(file.getAbsolutePath());

        String actualValue = fileInput.getAttribute("value");
        assertTrue(actualValue.contains("mando.jpg"));
    }

    @Test
    void checkboxesDisplayedEnabledTypeTests() {
        List<WebElement> boxes = driver.findElements(By.cssSelector(".form-check-input[type='checkbox']"));

        assertEquals(2, boxes.size());
        for (WebElement box : boxes) {
            assertEquals("checkbox", box.getAttribute("type"));
            assertTrue(box.isDisplayed());
            assertTrue(box.isEnabled());
        }
    }

    @Test
    void checkboxesInitialStateTests() {
        WebElement checked = driver.findElement(By.id("my-check-1"));
        WebElement unchecked = driver.findElement(By.id("my-check-2"));

        assertTrue(checked.isSelected());
        assertFalse(unchecked.isSelected());
    }

    @Test
    void checkboxClickTests() {
        WebElement box = driver.findElement(By.id("my-check-2"));

        assertFalse(box.isSelected());
        box.click();
        assertTrue(box.isSelected());

        box.click();
        assertFalse(box.isSelected());
    }

    @Test
    void radioTypeAndNameTests() {
        WebElement r1 = driver.findElement(By.id("my-radio-1"));
        WebElement r2 = driver.findElement(By.id("my-radio-2"));

        assertEquals("radio", r1.getAttribute("type"));
        assertEquals("radio", r2.getAttribute("type"));

        assertEquals("my-radio", r1.getAttribute("name"));
        assertEquals("my-radio", r2.getAttribute("name"));

    }

    @Test
    void radioSelectedTests() {
        WebElement r1 = driver.findElement(By.id("my-radio-1"));
        WebElement r2 = driver.findElement(By.id("my-radio-2"));

        assertTrue(r1.isSelected());
        assertFalse(r2.isSelected());

        r2.click();
        assertTrue(r2.isSelected());
        assertFalse(r1.isSelected());

        r1.click();
        assertTrue(r1.isSelected());
        assertFalse(r2.isSelected());
    }

    @Test
    void radioTextTest() {
        WebElement r1 = driver.findElement(By.id("my-radio-1"));
        WebElement l1 = r1.findElement(By.xpath("./ancestor::label[1]"));
        WebElement r2 = driver.findElement(By.id("my-radio-2"));
        WebElement l2 = r2.findElement(By.xpath("./ancestor::label[1]"));

        assertTrue(l1.getText().contains("Checked radio"));
        assertTrue(l2.getText().contains("Default radio"));
    }

    @Test
    void buttonVisibleAndEnabledTests() {
        WebElement btn = driver.findElement(By.cssSelector("button[type = 'submit'].btn"));

        assertTrue(btn.isDisplayed());
        assertTrue(btn.isEnabled());
    }

    @Test
    void buttonTextTest() {
        WebElement btn = driver.findElement(By.cssSelector("button[type = 'submit'].btn"));
        assertEquals("Submit", btn.getText());
    }

    @Test
    void buttonTypeSubmitTest() {
        WebElement btn = driver.findElement(By.cssSelector("button[type = 'submit'].btn"));

        assertEquals("submit", btn.getAttribute("type"));
    }

    @Test
    void buttonClickTest() {
        WebElement btn = driver.findElement(By.cssSelector("button[type = 'submit'].btn"));

        btn.click();

        assertEquals("Form submitted", driver.findElement(By.cssSelector("h1.display-6")).getText());
    }

    @Test
    void colorTypeAndDefaultTests() {
        WebElement color = driver.findElement(By.cssSelector("[name='my-colors']"));

        assertEquals("color", color.getAttribute("type"));
        assertEquals("#563d7c", color.getAttribute("value"));
    }

    @Test
    void colorInputTest() {
        WebElement color = driver.findElement(By.cssSelector("[name='my-colors']"));

        String newColor = "#228b22";
        color.sendKeys(newColor);

        assertEquals(newColor, color.getAttribute("value"));
    }

    @Test
    void dataPickerTypeAndNameTests() {
        WebElement date = driver.findElement(By.cssSelector(".form-control[name='my-date']"));

        assertEquals("text", date.getAttribute("type"));
        assertEquals("my-date", date.getAttribute("name"));
    }

    @Test
    void dataPickerInputTest() {
        WebElement date = driver.findElement(By.cssSelector(".form-control[name='my-date']"));

        String newDate = "12/15/2025";
        date.clear();
        date.sendKeys(newDate);

        assertEquals(newDate, date.getAttribute("value"));
    }

    @Test
    void sliderDefaultAttributesTests() {
        WebElement slider = driver.findElement(By.cssSelector("[name='my-range']"));

        assertEquals("range", slider.getAttribute("type"));
        assertEquals("my-range", slider.getAttribute("name"));
        assertEquals("0", slider.getAttribute("min"));
        assertEquals("10", slider.getAttribute("max"));
        assertEquals("1", slider.getAttribute("step"));
        assertEquals("5", slider.getAttribute("value"));
    }

    @Test
    void exampleRangeLocatorTest() {
        WebElement slider = driver.findElement(By.cssSelector("[name='my-range']"));

        int rangeValue = 3;

        slider.click();

        int actualValue = Integer.parseInt(slider.getAttribute("value"));
        Keys direction = (rangeValue < actualValue) ? Keys.ARROW_LEFT : Keys.ARROW_RIGHT;

        while (actualValue != rangeValue) {
            slider.sendKeys(direction);
            actualValue = Integer.parseInt(slider.getAttribute("value"));
        }

        assertEquals(rangeValue, actualValue);
    }

    @Test
    void rangeSliderNotGoBelowMinOrAboveMax () {
        WebElement slider = driver.findElement(By.cssSelector("[name='my-range']"));

        slider.click();

        for (int i = 0; i < 15; i++) {
            slider.sendKeys(Keys.ARROW_LEFT);
        }
        assertEquals("0", slider.getAttribute("value"));

        for (int i = 0; i < 15; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }
        assertEquals("10", slider.getAttribute("value"));

    }

    @Test
    void labelSliderTest() {
        WebElement slider = driver.findElement(By.cssSelector("[name='my-range']"));
        WebElement label = slider.findElement(By.xpath("./ancestor::label[1]"));

        assertTrue(label.getText().contains("Example range"));
    }

}
