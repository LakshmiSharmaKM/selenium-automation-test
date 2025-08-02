package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ErnxLoginTest {

    WebDriver driver;
    WebDriverWait wait;
    Logger log = LoggerFactory.getLogger(ErnxLoginTest.class);

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://ernx-consumer.vercel.app/login");
        log.info("Opened ERNX Consumer login page");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        log.info("Browser closed successfully");
    }

    // ✅ Test 1: Verify login page loads and button visible
    @Test(priority = 1)
    public void testLoginPageLoad() {
        log.info("Running test: Verify login page loads");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "URL does not contain 'login'");

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Login')]")));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not visible");

        log.info("✅ Login page loaded successfully and button is visible");
    }

    // ✅ Test 2: Validate empty form submission
    @Test(priority = 2)
    public void testEmptyFormValidation() throws InterruptedException {
        log.info("Running test: Validate empty form submission");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Login')]")));
        loginButton.click();
        Thread.sleep(2000);

        // Adjust locator if actual page has specific error message elements
        java.util.List<WebElement> errors = driver.findElements(By.xpath("//*[contains(text(),'required') or contains(text(),'Please')]"));
        Assert.assertTrue(errors.size() > 0, "No validation message displayed for empty fields");

        log.info("✅ Validation messages displayed for empty login form");
    }

    // ✅ Test 3: Successful login (UI interaction)
    @Test(priority = 3)
    public void testSuccessfulLogin() throws InterruptedException {
        log.info("Running test: Valid login flow");
        WebElement email = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        WebElement password = driver.findElement(By.name("password"));

        email.clear();
        password.clear();

        email.sendKeys("testuser@example.com");
        password.sendKeys("Test@1234");

        log.info("Entered valid credentials");

        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        loginButton.click();

        Thread.sleep(3000);
        String newUrl = driver.getCurrentUrl();

        Assert.assertFalse(newUrl.contains("login"), "Still on login page after login attempt");
        log.info("✅ Successfully logged in and redirected from login page");
    }
}