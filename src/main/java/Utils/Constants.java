package Utils;

import java.util.Arrays;

public class Constants {
    public static final String projectDir = System.getProperty("user.dir");
    public static final String resourcesDir = projectDir + "/src/main/resources";
    public static String stackTraceElementArrayToString(StackTraceElement[] arr){
        return Arrays.stream(arr)
                .map(StackTraceElement::toString)
                .reduce("", (s1, s2) -> s1 + s2 + "\n");
    }
}
