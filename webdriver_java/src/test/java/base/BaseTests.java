package base;

import com.google.common.io.Files;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.HomePage;
import utils.CookieManager;
import utils.EventReporter;
import utils.WindowManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseTests {

    private WebDriver driver;
    protected HomePage homePage;

    @BeforeClass
    public void setUp() throws MalformedURLException{
//        var driverExtension = "";
//        if(System.getenv("RUNNER_OS") != null) {
//            driverExtension = "-linux";
//        };
//        System.setProperty("webdriver.chrome.driver", "resources/chromedriver" + driverExtension);

        // WebDriverManager.chromedriver().setup();
        // driver = new EventFiringWebDriver(new ChromeDriver(getChromeOptions()));

        System.setProperty("otel.traces.exporter", "jaeger");
        System.setProperty("otel.exporter.jaeger.endpoint", "http://localhost:14250");
        System.setProperty("otel.resource.attributes", "service.name=selenium-java-client");

        EventFiringDecorator<WebDriver> eventFiringDecorator = new EventFiringDecorator<>(new EventReporter());
        driver = eventFiringDecorator.decorate(new RemoteWebDriver(getUrl(), getChromeOptions()));
    }

    @BeforeMethod
    public void goHome(){
        driver.get("https://the-internet.herokuapp.com/");
        homePage = new HomePage(driver);
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    @AfterMethod
    public void recordFailure(ITestResult result){
        if(ITestResult.FAILURE == result.getStatus())
        {
            var camera = (TakesScreenshot)driver;
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            try{
                Files.move(screenshot, new File("resources/screenshots/" + result.getName() + ".png"));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public WindowManager getWindowManager(){
        return new WindowManager(driver);
    }

    private ChromeOptions getChromeOptions(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");

        // Default headless mode off, set to true based on env var
        var headless = Boolean.parseBoolean(System.getenv("HEADLESS_CHROME")) | false;
        options.setHeadless(headless);
        return options;
    }

    public CookieManager getCookieManager(){
        return new CookieManager(driver);
    }

    private URL getUrl() throws MalformedURLException {
        String urlString = "http://%s:4444";

        String host = "localhost";
        String hubHost = System.getenv("HUB_HOST");
        if (hubHost != null) {
            host = hubHost;
        }
        urlString = String.format(urlString, host);
        return new URL(urlString);
    }
}
