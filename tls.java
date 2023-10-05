import java.io.File;
import java.io.FilenameFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tls {

    public static void main(String[] args) {
        String folderPath = "";
        String csvName = "";
        String folderPathPrint = "";
        Boolean createCsv = false;
        if (args.length == 0) {
            folderPath = "./";
            folderPathPrint = folderPath;
            // tls
            // Faire tls a l'interieur du dossier actuel
        }
        else if (args.length == 1) {
            folderPath = args[0];
            if (folderPath.equals("./")) {folderPathPrint = folderPath;} else {folderPathPrint = folderPath+"/";}
            // tls path
            // Sortie en ligne de commande, pas de fichier .csv produit
        }
        else if (args[0].equals("-o")) {
            folderPath = args[2];
            if (folderPath.equals("./")) {folderPathPrint = folderPath;} else {folderPathPrint = folderPath+"/";}
            csvName = args[1];
            createCsv = true;
            // tls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>
            // Sortie dans un fichier .csv
        }
        else {
            System.out.println("Fichier inexistant");
            System.exit(1);
        }

        // Fonctionne pour un path sur MacOS, \ verifier sur Windows
        /*File dossier = new File(folderPath);
        System.setProperty( "user.dir", folderPath );
        FilenameFilter filter = (dir, nomFichier) -> nomFichier.endsWith(".java");
        File[] fichiersJava = dossier.listFiles(filter);*/

        File dossier = new File(folderPath);
        //List of all files and directories
        String[] fichiers = dossier.list();
        List<String> fichiersJava = new ArrayList<>();
        Pattern pattern = Pattern.compile(".*\\.java");
        for(int i = 0; i < fichiers.length; i++) {
            Matcher matcher = pattern.matcher(fichiers[i]);
            if (matcher.find()) {
                fichiersJava.add(fichiers[i]);
            }
        }

        if (fichiersJava.size() > 0) {
            String x = "Chemin, Paquet, Classe, tloc, tassert, tcmp";
            for (String fichier : fichiersJava) {
                int t1 = TLOC.countTLOC(folderPathPrint+fichier);
                int t2 = tassert.countTAssert(folderPathPrint+fichier);
                int t3 = t2;
                // TODO: valeur de 1 correct?
                if (t2 == 0) {t3 = 1;}
                float t4 = (float) t1/t3;

                // TODO: path relatif a la place

                String ligne = String.format("\n%s, %s, %s, %s, %s, %s", folderPathPrint+fichier, getPackageName(folderPathPrint+fichier), fichier, t1, t2, t4);
                x += ligne;
            }

            if (createCsv) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {
                    writer.write(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else {
                System.out.println(x);
            }
        }
        else {
            // TODO: faire deux cas séparés
            System.out.println("Ce dossier n'existe pas ou ne contient aucun fichier Java.");
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
