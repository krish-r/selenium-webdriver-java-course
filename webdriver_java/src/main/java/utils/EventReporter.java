package utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class EventReporter implements WebDriverListener {
    @Override
    public void beforeClick(WebElement webElement) {
        System.out.println("Clicking on " + webElement.getText());
    }
}
