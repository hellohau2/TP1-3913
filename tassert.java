import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tassert {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Indiquer le nom du fichier comme suit:\njava tassert filename.java");
            System.exit(1);
        }

        System.out.println(countTAssert(args[0]) + " assertions JUnit");
    }

    static String countTAssert(String fileName) {
        return "0";
    }
}