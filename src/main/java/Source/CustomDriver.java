package Source;

import Utils.Reporter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Objects;

public class CustomDriver {
    public WebDriver driver;
    public JavascriptExecutor js;
    private final Reporter log;

    public CustomDriver(WebDriver driver, Reporter log) {
        this.driver = driver;
        this.log = log;
        js = (JavascriptExecutor) driver;
    }

    public void quit() {
        log.info("driver quit");
        driver.quit();
    }

    public WebDriverWait wait(int timeout) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public void sendKeys(By locator, String text, boolean softAssert){

    }

    public void sendKeys(By locator, String text, boolean softAssert, boolean clear){
        try{
            var ele = driver.findElement(locator);
            if(clear){
                ele.clear();
            }

            ele.sendKeys(text);
            log.info(String.format("successfully entered %s to element %s", text, locator));
        }
        catch (ElementNotInteractableException ignored){
            if(!softAssert){
                log.error(String.format("element is not interactable %s", locator));
                Assert.fail();
            }

            log.warn(String.format("element is not interactable %s", locator));
        }
        catch (StaleElementReferenceException ignored){
            if(softAssert){
                log.error(String.format("element is stale, unable to send input\n locator = %s", locator));
                Assert.fail();
            }

            log.warn(String.format("element is stale, unable to send input\nlocator = %s", locator));
        }
        catch(NoSuchElementException ignored){

        }
    }


    public void scrollElementIntoView(By locator) {
        var ele = driver.findElement(locator);
        String scrollElementIntoMiddle =
                "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                        + "var elementTop = arguments[0].getBoundingClientRect().top;"
                        + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        js.executeScript(scrollElementIntoMiddle, ele);
    }

    public void setAttribute(By locator, String attribute, String value) {
        var ele = driver.findElement(locator);
        js.executeScript(String.format("arguments[0].setAttribute('%s', '%s')", attribute, value), ele);
    }

    public void changeInnerHTML(By locator, String html) {
        var ele = driver.findElement(locator);
        js.executeScript(String.format("arguments[0].innerHTML = '%s';", html), ele);
    }

    public void closeOtherWindows() {
        String homepage = driver.getWindowHandle();
        var allPages = driver.getWindowHandles();
        for (var page : allPages) {
            if (Objects.equals(page, homepage)) continue;
            driver.switchTo().window(page);
            driver.close();
        }
    }

    public void closeOtherWindows(String homepage) {
        var allPages = driver.getWindowHandles();
        for (var page : allPages) {
            if (Objects.equals(page, homepage)) continue;
            driver.switchTo().window(page);
            driver.close();
        }
    }

    public boolean isVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isInteractable(By locator, int timeout) {
        try {
            wait(timeout).until($ ->
                    driver.findElement(locator).isEnabled() && driver.findElement(locator).isDisplayed());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void get(String url) {
        driver.get(url);
        log.info(String.format("driver got to %s", url));
    }

    public void click(By locator, boolean softAssert, int timeout) {
        if (isInteractable(locator, timeout)) {
            try {
                driver.findElement(locator);
            } catch (ElementClickInterceptedException ignored) { // TODO: add other possible exceptions for click()
                if (!softAssert) {
                    log.error(String.format("element click is intercepted:\nlocator: '%s'", locator));
                    Assert.fail();
                }

                log.warn(String.format("element click is intercepted:\nlocator: '%s'", locator));
            }
        }
    }

    public void click(By locator, boolean softAssert) {
        if (isInteractable(locator, 10)) {
            try {
                driver.findElement(locator);
            } catch (ElementClickInterceptedException ignored) { // TODO: add other possible exceptions for click()
                if (!softAssert) {
                    log.error(String.format("element click is intercepted:\nlocator: '%s'", locator));
                    Assert.fail();
                }

                log.warn(String.format("element click is intercepted:\nlocator: '%s'", locator));
            }
        }
    }

    public void submit(By locator) {
        driver.findElement(locator).submit();
        log.debug(String.format("element %s submitted", locator));
    }


    // ASSERTIONS
    public void assertElementIsInteractable(By locator, boolean expected, int timeout) {
        boolean actual = isInteractable(locator, timeout);
        log.info(String.format("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, expected));
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsInteractable(By locator, int timeout) {
        boolean actual = isInteractable(locator, timeout);
        log.info(String.format("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, true));
        Assert.assertTrue(actual);
    }

    public void assertElementIsInteractable(By locator, boolean expected) {
        boolean actual = isInteractable(locator, 10);
        log.info(String.format("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, expected));
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsInteractable(By locator) {
        boolean actual = isInteractable(locator, 10);
        log.info(String.format("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, true));
        Assert.assertTrue(actual);
    }


    public void assertElementIsPresent(By locator, boolean expected){
        boolean actual = isPresent(locator);
        log.info(String.format("element %s\nisPresent: %s\nshouldBePresent: %s", locator, actual, expected));
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsPresent(By locator){
        boolean actual = isPresent(locator);
        log.info(String.format("element %s\nisPresent: %s\nshouldBePresent: %s", locator, actual, true));
        Assert.assertTrue(actual);
    }
}
