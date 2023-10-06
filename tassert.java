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
        if (!isJavaFile.isJavaFile(args[0])){
            System.out.println("Veuillez choisir un fichier .java");
            System.exit(1);
        }
        System.out.println(countTassert(args[0]) + " assertions JUnit");
    }

    public static int countTassert(String fileName) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            boolean commentStart = false;
            String line;
            Pattern pattern = Pattern.compile("(?: |^)assert[a-zA-Z]*\\(.*\\)|fail\\(.*\\)");

            while ((line = br.readLine()) != null){
                line = line.trim();
                // Check si on commence un commentaire
                if (line.startsWith("/*")){
                    commentStart = true;
                }
                // Check si c'est une ligne de code
                if (!commentStart && !line.startsWith("//") && !line.isEmpty()){
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        // TODO: faire si plusieurs Assert sur une meme ligne
                        // TODO: faire si pattern trouvé dans un commentaire à la fin d'une ligne
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