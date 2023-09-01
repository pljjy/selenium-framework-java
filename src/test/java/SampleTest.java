import Source.DriverAddons.DriverFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.projectDir;

public class SampleTest {
    @Test
    public void DriverTest() throws JsonProcessingException {
        String jsonFilePath = projectDir + "\\src\\main\\resources\\cfg.json";
        Map<String, Object> cfgs = null;
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            Gson gson = new Gson();
            cfgs = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert cfgs != null;
        var driver = DriverFactory.GetBrowser(
                DriverFactory.stringToEtypeDriver(cfgs.get("browser").toString()),
                (int) ((double) cfgs.get("msTimeout")));
        // beautiful java language requires me to convert Double to double AND THEN to actual int
        driver.driver.get("https://google.com");
    }
}
