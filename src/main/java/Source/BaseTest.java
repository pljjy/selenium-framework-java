package Source;

import Source.DriverAddons.DriverFactory;
import Utils.Reporter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.projectDir;

/*

This class should be inherited by every test

Override _cleanLogs and set it to FALSE if you have tests that are related to each other
Override _driverTest if you are doing a non webdriver test and set it to FALSE

 */
public class BaseTest {
    boolean _cleanLogs = true;
    boolean _driverTest = true;
    public CustomDriver driver;
    public Reporter log = new Reporter();

    @BeforeMethod
    public void setUpDriver() {
        if (!_driverTest) return;

        String jsonFilePath = projectDir + "\\src\\main\\resources\\cfg.json";
        Map<String, Object> cfgs;
        int timeout = 10;
        String browserName = "chrome";
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            Gson gson = new Gson();
            cfgs = gson.fromJson(reader, type);

            browserName = cfgs.get("browser").toString();
            timeout = (int) ((double) cfgs.get("timeout"));
            // beautiful java language requires me to convert re Double to double AND THEN to an actual int
        } catch (Exception ignored) {
            log.warn("couldn't find \\src\\main\\resources\\cfg.json OR couldn't get values from it");
        }

        var _driver = DriverFactory.GetBrowser(DriverFactory.stringToEtypeDriver(browserName), timeout);
        driver = new CustomDriver(_driver, log);
    }

    @AfterMethod
    public void Logs(ITestResult result) {
        if (_driverTest)
            driver.quit();

        switch (result.getStatus()) {
            case ITestResult.FAILURE -> log.testFailed(result);
            case ITestResult.SKIP -> log.testSkipped(result);
            default -> log.testSucceed(result);
        }

        Allure.addAttachment("Log-File", log.getReport(_cleanLogs));
    }
}
