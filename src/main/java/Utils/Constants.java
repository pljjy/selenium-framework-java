package Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class Constants {
    public static final String projectDir = System.getProperty("user.dir");
    public static final String resourcesDir = projectDir + "/src/main/resources";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String stackTraceElementArrayToString(StackTraceElement[] arr) {
        return Arrays.stream(arr)
                .map(StackTraceElement::toString)
                .reduce("", (s1, s2) -> s1 + s2 + "\n");
    }

    public static boolean compareJsons(String json1, String json2) throws JsonProcessingException {
        return mapper.writeValueAsString(mapper.readTree(json1))
                .equals(mapper.writeValueAsString(mapper.readTree(json1)));
    }
}
