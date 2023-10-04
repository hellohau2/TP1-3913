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

        System.out.println(countTAssert(args[0]) + " assertions JUnit");
    }

    // TODO: pour le moment, compte les "Assert...." en commentaires
    public static int countTAssert(String fileName) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){

            Boolean commentStart = false;
            String line;
            while ((line = br.readLine()) != null){
                
                // Enleve les espaces 
                line = line.trim();

                // Check si on commence un commentaire
                if (line.startsWith("/*")){
                    commentStart = true;
                }

                // Check si c'est une ligne de code
                if (!commentStart && !line.startsWith("//") && !line.isEmpty()){
                    // Avec [] non consideres comme des caracteres du pattern, le pattern suivant represente:
                    // Assert.[anything]([anything])
                    Pattern pattern = Pattern.compile("\\bassert(?:\\w+)?\\s*\\(");
                    
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        count++;
                    }
                }

                // Check si on n'est plus dans un commentaire
                if (commentStart && line.endsWith("*/")){
                    commentStart = false;
                }

            }
        }
        catch(IOException e){
            System.out.println("Fichier introuvable");
            System.exit(1);
        }

        return count;
        }
}