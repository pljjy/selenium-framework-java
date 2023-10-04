import Source.BaseTests.DriverBaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class ScreenshotTest extends DriverBaseTest {
    @Test
    public void test1(){
        driver.get("https://google.com");
        driver.sendKeys(By.xpath("//*[@name='q']"), "cats");
        driver.submit();
        driver.takeScreenshot(this.getClass().getName());
    }
}
