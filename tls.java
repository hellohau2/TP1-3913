import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

        File dossier = new File(folderPath);
        FilenameFilter filter = (dir, nomFichier) -> nomFichier.endsWith(".java");
        File[] fichiersJava = dossier.listFiles(filter);

        if (fichiersJava != null) {
            if (createCsv == true) {
                outputCsv();
            }
            else {
                outputTerminal();
            }
        }
        else {
            System.out.println("Ce dossier ne contient aucun fichier Java.");
        }
    }

    public static void outputTerminal() {
        String x = "chemin du fichier, nom du paquet, nom de la classe, tloc de la classe, tassert de la classe, tcmp de la classe\n";
        // Do everything else
        System.out.println(x);
    }

    public static void outputCsv() {

    }
}
