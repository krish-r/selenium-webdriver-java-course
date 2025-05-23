package exercises.chapter3.menu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class MenuCount {

    private WebDriver driver;

    public void printLinkCount(){
//        var driverExtension = "";
//        if(System.getenv("RUNNER_OS") != null) {
//            driverExtension = "-linux";
//        };
//        System.setProperty("webdriver.chrome.driver", "resources/chromedriver" + driverExtension);

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("Shifting Content")).click();
        driver.findElement(By.linkText("Example 1: Menu Element")).click();

        List<WebElement> menuItems = driver.findElements(By.tagName("li"));
        System.out.println("Number of menu elements: " + menuItems.size());

        driver.quit();
    }

    public static void main(String args[]){
        MenuCount test = new MenuCount();
        test.printLinkCount();
    }
}
