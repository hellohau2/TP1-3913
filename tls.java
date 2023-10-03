import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tls {

    public static void main(String[] args) {
        String folderPath = "";
        Boolean createCsv = false;
        if (args.length == 0) {
            folderPath = "./";
            // tls
            // Faire tls a l'interieur du dossier actuel
        }
        else if (args.length == 1) {
            folderPath = args[0];
            // tls path
            // Sortie en ligne de commande, pas de fichier .csv produit
        }
        else if (args.length == 3 && args[0] == "-o") {
            folderPath = args[2];
            createCsv = true;
            // tls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>
            // Sortie dans un fichier .csv
        }
        else {
            System.out.println("Fichier inexistant");
            System.exit(1);
        }

        // Fonctionne pour un path sur MacOS, \ verifier sur Windows
        File dossier = new File(folderPath);
        FilenameFilter filter = (dir, nomFichier) -> nomFichier.endsWith(".java");
        File[] fichiersJava = dossier.listFiles(filter);

        if (fichiersJava != null) {
            if (createCsv == true) {
                // TODO
            }
            else {
                String x = "chemin du fichier, nom du paquet, nom de la classe, tloc de la classe, tassert de la classe, tcmp de la classe";
                for (File fichier : fichiersJava) {
                    String paquet = getPackageName(fichier.getPath());
                    int t1 = TLOC.countTLOC(fichier.getName());
                    int t2 = tassert.countTAssert(fichier.getName());
                    int t3 = t2;
                    // TODO: valeur de 1 correct?
                    if (t2 == 0) {t3 = 1;}
                    float t4 = (float) t1/t3;

                    // TODO: path relatif a la place

                    String ligne = String.format("\n%s, %s, %s, %s, %s, %s", fichier.getPath(), getPackageName(fichier.getName()), fichier.getName(), t1, t2, t4);
                    x += ligne;
                }
                System.out.println(x);
            }
        }
        else {
            System.out.println("Ce dossier ne contient aucun fichier Java.");
        }
    }
    public static String getPackageName(String fileName) {
        String packageName = "No package found";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches("package .*;")) {
                    packageName = line.substring(8, line.length() - 1);
                    break;
                }
            }
        }

        catch(IOException e){
            System.out.println("Fichier introuvable");
            System.exit(1);
        }
        return packageName;
    }
}
