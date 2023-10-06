import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tloc{
    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Indiquer le nom du fichier comme suit:\njava tloc filename.java");
            System.exit(1);
        }
        if (!isJavaFile.isJavaFile(args[0])){
            System.out.println("Veuillez choisir un fichier .java");
            System.exit(1);
        }

        System.out.println("TLOC: " + countTloc(args[0]));
    }

    // Argument : String file = chemin du fichier pour tloc
    // Output : int count = Le nombre de lignes qui ne sont pas vide et ne sont pas des commentaires
    public static int countTloc(String file){
        int count = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            boolean commentStart = false;
            String line;
            while ((line = br.readLine()) != null){
                
                // Enleve les espaces 
                line = line.trim();
                
                // Check pour les cas : /* commentaires */ code sur la meme ligne
                if(line.contains("/*") && line.contains("*/")){
                    String[] parts = line.split("/\\*.*\\*/");
                    for(String part : parts){
                        if(!part.trim().isEmpty() && !part.trim().startsWith("//")){
                            count++;
                            break;
                        }
                    }
                    
                    continue;
                }

                // Check si on commence un commentaire
                if (line.startsWith("/*")){
                    commentStart = true;
                }

                // Check si c'est une ligne de code
                if (!commentStart && !line.startsWith("//") && !line.isEmpty()){
                    count++;
                }

                // Check si on n'est plus dans un commentaire
                if (commentStart && line.endsWith("*/")){
                    commentStart = false;
                }
            }

        }catch(IOException e){
            System.out.println("Fichier introuvable");
            System.exit(1);
        }
        return count;
    }
}