import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class SeleniumTest {

    private WebDriver driver;

    @BeforeEach
    public void startWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void quitWebDriver() {
        driver.quit();
    }

    @Test
    public void round01_testLogin() {
        driver.get("https://demo.applitools.com/");

        driver.findElement(By.id("username")).sendKeys("andy");
        driver.findElement(By.id("password")).sendKeys("panda<3");
        driver.findElement(By.id("log-in")).click();

        assertEquals(
                "Financial Overview",
                driver.findElement(By.className("element-header")).getText());
    }

    @Test
    public void round02_testDropdown() {
        driver.get("https://kitchen.applitools.com/ingredients/select");

        Select dropdown = new Select(driver.findElement(By.id("spices-select-single")));
        dropdown.selectByValue("ginger");

        String value = dropdown.getAllSelectedOptions().get(0).getAttribute("value");
        assertEquals("ginger", value);
    }

    @Test
    public void round03_testFileUpload() {
        File fileToUpload = new File("src/test/resources/pic.jpg");
        driver.get("https://kitchen.applitools.com/ingredients/file-picker");
        driver.findElement(By.id("photo-upload")).sendKeys(fileToUpload.getAbsolutePath());
    }

    @Test
    public void round04_testIframe() {
        driver.get("https://kitchen.applitools.com/ingredients/iframe");

        assertTrue(
                driver.switchTo().frame("the-kitchen-table")
                        .findElement(By.id("fruits-vegetables"))
                        .isDisplayed()
        );
    }

    @Test
    public void round05_testExplicitWaiting() {
        driver.get("https://automationbookstore.dev/");
        driver.findElement(By.id("searchBar")).sendKeys("testing");

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(presenceOfElementLocated(
                        By.cssSelector("li.ui-screen-hidden"))
                );

        assertEquals(1,
                driver.findElements(By.cssSelector("li:not(.ui-screen-hidden)"))
                        .size());
    }

    @Test
    public void round06_testAcceptAlert() {
        driver.get("https://kitchen.applitools.com/ingredients/alert");
        driver.findElement(By.id("alert-button")).click();
        driver.switchTo().alert().accept();
    }

    @Test
    public void round06_testDismissAlert() {
        driver.get("https://kitchen.applitools.com/ingredients/alert");
        driver.findElement(By.id("confirm-button")).click();
        driver.switchTo().alert().dismiss();
    }

    @Test
    public void round06_testAnswerPrompt() {
        driver.get("https://kitchen.applitools.com/ingredients/alert");
        driver.findElement(By.id("prompt-button")).click();

        Alert alert = driver.switchTo().alert();
        alert.sendKeys("nachos");
        alert.accept();
    }

    @Test
    public void round07_testNewTab()
    {
        driver.get("https://kitchen.applitools.com/ingredients/links");
        driver.findElement(By.id("button-the-kitchen-table")).click();
        driver.getWindowHandles().forEach(tab->driver.switchTo().window(tab));
        assertTrue(driver.findElement(By.id("fruits-vegetables")).isDisplayed());
    }

//    @Test
//    public void round08_testApi() {
//        // Selenium WebDriver does NOT support API testing!
//    }

    @Test
    public void round09_testScreenshots() throws IOException {
        driver.get("https://kitchen.applitools.com/ingredients/table");
        driver.findElement(By.id("column-button-name")).click();

        // Create directory for screenshots
        new File("screenshots").mkdirs();

        // Full page
        File fullPage = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        Path fullPagePath = Paths.get("screenshots", "fullPage.png");
        Files.move(fullPage.toPath(), fullPagePath, StandardCopyOption.REPLACE_EXISTING);

        // Individual element
        WebElement table = driver.findElement(By.id("fruits-vegetables"));
        File oneElement = table.getScreenshotAs(OutputType.FILE);
        Path oneElementPath = Paths.get("screenshots", "individualElement.png");
        Files.move(oneElement.toPath(), oneElementPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
