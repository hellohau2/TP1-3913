import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class tls2 {

    public static void main(String[] args) {
        String folderPath = "";
        String folderPathPrint = "";
        String csvName = "";
        boolean createCsv = false;
        if (args.length == 0) {
            computeTls("./", false, null);
        } else if (args.length == 1) {
            computeTls(args[0], false, null);
        } else if (args[0].equals("-o")) {
            computeTls(args[2], true, args[1]);
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

    public static void computeTls(String filepath, Boolean toCsv, String csvName){
        Path startDir = Paths.get(filepath);

        try (Stream<Path> stream = Files.walk(startDir)) {
            Result[] results = stream
                    .filter(file -> file.toString().endsWith("Test.java"))
                    .map(file -> new Result(file.toString(), tloc.countTloc(file.toString()), tassert.countTassert(file.toString())))
                    .toArray(Result[]::new);

            // Use the results array as needed
            // Arrays utilises pour calculer les top n%
            String[] cleanedStrings = new String[results.length];

            int[] TLOCSfromCleaned = new int[results.length];
            float[] TCMPfromCleaned = new float[results.length];

            for(int i = 0; i < results.length; i++) {
                cleanedStrings[i] = cleanString(results[i].filePath, results[i].tlocResult, results[i].tassertResult);

                TLOCSfromCleaned[i] = results[i].tlocResult;
                TCMPfromCleaned[i] = (float) results[i].tlocResult / results[i].tassertResult;
            }

            for(int i = 0; i < results.length; i++) {
                System.out.println(cleanedStrings[i]);
            }
            if(toCsv){
                // Use try-with-resources statement to automatically close the resources
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {

                    // Premier ligne
                    writer.write("Chemin, Paquet, Classe, TLOC, TASSERT, tcmp");
                    writer.newLine();

                    // Use the results array as needed
                    for(int i = 0; i < results.length; i++) {
                        assert cleanedStrings[i] != null;
                        writer.write(cleanedStrings[i]);
                        writer.newLine();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String cleanString(String path, int TLOC, int TASSERT) {
        // Validate inputs
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Le filepath ne peut pas etre vide ou null");
        }
        if (TASSERT <= 0) {
            // throw new IllegalArgumentException("TASSERT doit etre un int positif superieur a 0");
            // System.out.println("TASSERT doit etre un int positif superieur a 0");
            return null;
        }

        // Extraction nom du fichier
        String[] pathParts = path.split("\\\\");
        String fileName = pathParts[pathParts.length - 1].replace(".java", "");

        // Extraction package
        String packageName = "";
        int sourceIndex = path.indexOf("\\src\\test\\java\\");
        if (sourceIndex == -1) {
            sourceIndex = path.indexOf("\\src\\main\\java\\");
        }
        else {
            packageName = path.substring(sourceIndex + 15, path.length() - fileName.length() - 5).replace("\\", ".");
        }

        // Calcul TCMP
        float TCMP = (float) TLOC / TASSERT;

        // Combine everything
        return String.format("%s,  %s, %s, %d, %d, %.2f", path, packageName, fileName, TLOC, TASSERT, TCMP);
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
