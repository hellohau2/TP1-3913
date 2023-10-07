import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class isJavaFile {
    public static Boolean isJavaFile(String filename) {
        Pattern pattern = Pattern.compile(".*\\.java");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return true;
        }
        else {
            return false;
        }
    }
}