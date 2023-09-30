import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TLOC{
    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("Provide an argument");
            System.exit(1);
        }

        System.out.println("TLOC : " + countTLOC(args[0]));
    }

    // Argument : String file = chemin du fichier pour TLOC 
    // Output : int tloc = Le nombre de ligne qui ne sont pas vide et ne sont pas des commentaires
    static int countTLOC(String file){
        int tloc = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
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
                    tloc++;
                }

                // Check si on n'est plus dans un commentaire
                if (commentStart && line.endsWith("*/")){
                    commentStart = false;
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
        return tloc;
    }
}