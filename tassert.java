import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tassert {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Indiquer le nom du fichier comme suit:\njava tassert filename.java");
            System.exit(1);
        }

        countTAssert(args[0]);
    }

    static void countTAssert(String fileName) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;

            // Avec [] non consideres comme des caracteres du pattern, le pattern suivant represente:
            // Assert.[anything]([anything])
            Pattern pattern = Pattern.compile("Assert\\..*\\(.*\\)");
            
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    count++;
                }
            }
        }

        catch(IOException e){
            System.out.println("Fichier introuvable");
            System.exit(1);
        }

        System.out.println(count + " assertions JUnit");
    }
}