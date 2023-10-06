import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tls2 {

    public static void main(String[] args) {
        String folderPath = "";
        String folderPathPrint = "";
        String csvName = "";
        boolean createCsv = false;
        if (args.length == 0) {
            computeTls();
        } else if (args.length == 1) {
            computeTls();s
        } else if (args[0].equals("-o")) {
            computeTls();
        } else {
            System.out.println("Veuillez respecter le format suivant:\ntls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>");
            System.exit(1);
        }

        // TODO: try catch pour mauvais path?
        File dossier = new File(folderPath);
        //List of all files and directories
        String[] fichiers = dossier.list();
        List<String> fichiersJava = new ArrayList<>();
        assert fichiers != null;
        for (String s : fichiers) {
            if (isJavaFile.isJavaFile(s)) {
                fichiersJava.add(s);
            }
        }

        if (fichiersJava.size() > 0) {
            String x = "Chemin, Paquet, Classe, tloc, tassert, tcmp";
            for (String fichier : fichiersJava) {
                int t1 = tloc.countTloc(folderPathPrint + fichier);
                int t2 = tassert.countTassert(folderPathPrint + fichier);
                int t3 = t2;
                // TODO: valeur de 1 correct?
                if (t2 == 0) {
                    t3 = 1;
                }
                float t4 = (float) t1 / t3;

                // TODO: Faut-il vérifier si le path suit les normes Maven pour ensuite ne pas imprimer le path au complet?
                String ligne = String.format("\n%s, %s, %s, %s, %s, %s", folderPathPrint + fichier, getPackageName(folderPathPrint + fichier), fichier.substring(0, fichier.length()-5), t1, t2, t4);
                x += ligne;
            }

            if (createCsv) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {
                    writer.write(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(x);
            }
        } else {
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
