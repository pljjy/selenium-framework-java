package Source;

import Utils.Constants;
import Utils.Reporter;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CustomDriver {
    public WebDriver driver;
    public JavascriptExecutor js;
    private final Reporter log;
    public WebDriverWait wait;

    public CustomDriver(WebDriver driver, Reporter log) {
        this.driver = driver;
        this.log = log;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
    }

    public CustomDriver(WebDriver driver, Reporter log, int timeout) {
        this.driver = driver;
        this.log = log;
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        js = (JavascriptExecutor) driver;
    }

    public void quit() {
        log.info("driver quit");
        driver.quit();
    }

    public void sendKeys(By locator, String text) {
        try {
            var ele = driver.findElement(locator);
            ele.sendKeys(text);
            log.info("successfully entered %s to element %s", text, locator);
        } catch (ElementNotInteractableException ignored) {
            log.warn("element is not interactable %s", locator);
        } catch (StaleElementReferenceException ignored) {
            log.warn("element is stale, unable to send input\nlocator = %s", locator);
        } catch (NoSuchElementException ignored) {
            log.warn("no element found %s", locator);
        } catch (Exception ex) {
            log.warn("element - %s\n%s\n\n%s", locator, ex.getMessage(),
                    Constants.stackTraceElementArrayToString(ex.getStackTrace()));
        }
    }

    public void sendKeys(By locator, String text, boolean softAssert) {
        try {
            var ele = driver.findElement(locator);
            ele.sendKeys(text);
            log.info("successfully entered %s to element %s", text, locator);
        } catch (ElementNotInteractableException ignored) {
            if (!softAssert) {
                log.error("element is not interactable %s", locator);
                Assert.fail(String.format("element is not interactable %s", locator));
            }

            log.warn("element is not interactable %s", locator);
        } catch (StaleElementReferenceException ignored) {
            if (softAssert) {
                log.error("element is stale, unable to send input\n locator = %s", locator);
                Assert.fail(String.format("element is stale, unable to send input\n locator = %s", locator));
            }

            log.warn("element is stale, unable to send input\nlocator = %s", locator);
        } catch (NoSuchElementException ignored) {
            if (!softAssert) {
                log.error("no element found %s", locator);
                Assert.fail(String.format("no element found %s", locator));
            }

            log.warn("no element found %s", locator);
        } catch (Exception ex) {
            if (!softAssert) {
                log.error("%s\n\n%s", ex.getMessage(),
                        Constants.stackTraceElementArrayToString(ex.getStackTrace()));
                Assert.fail(ex.getMessage());
            }

            log.warn("element - %s\n%s\n\n%s", locator, ex.getMessage(),
                    Constants.stackTraceElementArrayToString(ex.getStackTrace()));
        }
    }

    public void sendKeys(By locator, String text, boolean softAssert, boolean clear) {
        try {
            var ele = driver.findElement(locator);
            if (clear) {
                ele.clear();
            }

            ele.sendKeys(text);
            log.info("successfully entered %s to element %s", text, locator);
        } catch (ElementNotInteractableException ignored) {
            if (!softAssert) {
                log.error("element is not interactable %s", locator);
                Assert.fail(String.format("element is not interactable %s", locator));
            }

            log.warn("element is not interactable %s", locator);
        } catch (StaleElementReferenceException ignored) {
            if (softAssert) {
                log.error("element is stale, unable to send input\n locator = %s", locator);
                Assert.fail(String.format("element is stale, unable to send input\n locator = %s", locator));
            }

            log.warn("element is stale, unable to send input\nlocator = %s", locator);
        } catch (NoSuchElementException ignored) {
            if (!softAssert) {
                log.error("no element found %s", locator);
                Assert.fail(String.format("no element found %s", locator));
            }

            log.warn("no element found %s", locator);
        } catch (Exception ex) {
            if (!softAssert) {
                log.error("%s\n\n%s", ex.getMessage(),
                        Constants.stackTraceElementArrayToString(ex.getStackTrace()));
                Assert.fail(ex.getMessage());
            }

            log.warn("element - %s\n%s\n\n%s", locator, ex.getMessage(),
                    Constants.stackTraceElementArrayToString(ex.getStackTrace()));
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
            wait.until($ ->
                    driver.findElement(locator).isEnabled() && driver.findElement(locator).isDisplayed());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void get(String url) {
        driver.get(url);
        log.info("driver got to %s", url);
    }

    public void click(By locator, boolean softAssert, int timeout) {
        if (isInteractable(locator, timeout)) {
            try {
                driver.findElement(locator);
            } catch (ElementClickInterceptedException ignored) { // TODO: add other possible exceptions for click()
                if (!softAssert) {
                    log.error("element click is intercepted:\nlocator: '%s'", locator);
                    Assert.fail(String.format("element click is intercepted:\nlocator: '%s'", locator));
                }

                log.warn("element click is intercepted:\nlocator: '%s'", locator);
            } catch (NoSuchElementException ignored) {
                if (!softAssert) {
                    log.error("no element found %s", locator);
                    Assert.fail(String.format("no element found %s", locator));
                }

                log.warn("no element found %s", locator);
            } catch (Exception ex) {
                if (!softAssert) {
                    log.error("%s\n\n%s", ex.getMessage(),
                            Constants.stackTraceElementArrayToString(ex.getStackTrace()));
                    Assert.fail(ex.getMessage());
                }

                log.warn("element - %s\n%s\n\n%s", locator, ex.getMessage(),
                        Constants.stackTraceElementArrayToString(ex.getStackTrace()));
            }
        }
    }

    public void click(By locator, boolean softAssert) {
        if (isInteractable(locator, 10)) {
            try {
                driver.findElement(locator);
            } catch (ElementClickInterceptedException ignored) { // TODO: add other possible exceptions for click()
                if (!softAssert) {
                    log.error("element click is intercepted:\nlocator: '%s'", locator);
                    Assert.fail(String.format("element click is intercepted:\nlocator: '%s'", locator));
                }

                log.warn("element click is intercepted:\nlocator: '%s'", locator);
            } catch (NoSuchElementException ignored) {
                if (!softAssert) {
                    log.error("no element found %s", locator);
                    Assert.fail(String.format("no element found %s", locator));
                }

                log.warn("no element found %s", locator);
            } catch (Exception ex) {
                if (!softAssert) {
                    log.error("%s\n\n%s", ex.getMessage(),
                            Constants.stackTraceElementArrayToString(ex.getStackTrace()));
                    Assert.fail(ex.getMessage());
                }

                log.warn("element - %s\n%s\n\n%s", locator, ex.getMessage(),
                        Constants.stackTraceElementArrayToString(ex.getStackTrace()));
            }
        }
    }

    public void submit(By locator) {
        driver.findElement(locator).submit();
        log.info("element %s submitted", locator);
    }

    public void submit() {
        driver.switchTo().activeElement().submit();
        log.info("active element submitted");
    }

    public void dragAndDrop(By source, By target, boolean softAssert) {
        var _source = driver.findElement(source);
        var _target = driver.findElement(target);

        try {
            new Actions(driver)
                    .dragAndDrop(_source, _target)
                    .perform();

            log.info("successfully dragged and dropped from from %s to %s", source, target);
        } catch (Exception ignored) {
            if (!softAssert) {
                log.error("can't drag from %s and drop to %s", source, target);
                Assert.fail(String.format("can't drag from %s and drop to %s", source, target));
            }

            log.warn("can't drag from %s and drop to %s", source, target);
        }
    }

    public void takeScreenshot(boolean fullPage, String testName) {
        if (fullPage) {
            takeFullPageScreenshot(testName);
        } else {
            Allure.addAttachment(testName, new ByteArrayInputStream(((TakesScreenshot) driver).
                    getScreenshotAs(OutputType.BYTES)));
        }
    }

    // god forgive me for this
    private void takeFullPageScreenshot(String testName) {
        String html2canvasJs;
        try {
            html2canvasJs = FileUtils.readFileToString(new File("src/main/resources/html2canvas.js"), "utf-8");
        } catch (IOException e) {
            log.error("Got IOException, couldn't take a full page screenshot\n\n%s\n%s", e.getMessage(), e.getStackTrace());
            return;
        }

        // something here is wrong
        String generateScreenshotJs = "var canvasImgContentDecoded;function genScreenshot () {html2canvas(document.body).then(function(canvas) {window.canvasImgContentDecoded = canvas.toDataURL('image/png');console.log(window.canvasImgContentDecoded);});}genScreenshot();";
        String getScreenshot = "return window.canvasImgContentDecoded;";
        AtomicReference<Object> encodedPngContent = new AtomicReference<>(null);

        js.executeScript(html2canvasJs);
        js.executeScript(generateScreenshotJs);

        wait.until($ -> {
            encodedPngContent.set(js.executeScript(getScreenshot));
            return encodedPngContent.get() != null;
        });

        String pngContent = encodedPngContent.toString();
        pngContent = pngContent.replace("data:image/png;base64,", "");
        // TODO: find a way to attach the screenshot instead of the text
        Allure.addAttachment(testName, new ByteArrayInputStream(pngContent.getBytes()));
    }


    // ASSERTIONS
    public void assertElementIsInteractable(By locator, boolean expected, int timeout) {
        boolean actual = isInteractable(locator, timeout);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, expected);
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsInteractable(By locator, int timeout) {
        boolean actual = isInteractable(locator, timeout);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, true);
        Assert.assertTrue(actual);
    }

    public void assertElementIsInteractable(By locator, boolean expected) {
        boolean actual = isInteractable(locator, 10);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, expected);
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsInteractable(By locator) {
        boolean actual = isInteractable(locator, 10);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, true);
        Assert.assertTrue(actual);
    }


    public void assertElementIsPresent(By locator, boolean expected) {
        boolean actual = isPresent(locator);
        log.info("element %s\nisPresent: %s\nshouldBePresent: %s", locator, actual, expected);
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsPresent(By locator) {
        boolean actual = isPresent(locator);
        log.info("element %s\nisPresent: %s\nshouldBePresent: %s", locator, actual, true);
        Assert.assertTrue(actual);
    }

    public void assertElementIsVisible(By locator, boolean expected) {
        boolean actual = isVisible(locator);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, expected);
        Assert.assertEquals(actual, expected);
    }

    public void assertElementIsVisible(By locator) {
        boolean actual = isVisible(locator);
        log.info("element %s\nisVisible: %s\nshouldBeVisible: %s", locator, actual, true);
        Assert.assertTrue(actual);
    }
}
