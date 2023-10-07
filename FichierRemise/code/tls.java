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

public class tls {

    public static void main(String[] args) {
        if (args.length == 0) {
            computeTls("./", false, null, -1.0f);
        } else if (args[0].equals("-o")) {
            if (args.length != 3) {
                System.out.println("Veuillez respecter le format suivant:\ntls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>\nou\ntls <chemin-de-l'entrée>\nou\ntls");
                System.exit(1);
            }
        if (!args[1].endsWith(".csv")) {
            System.out.println("Veuillez respecter le format suivant pour le fichier .csv:\nfileName.csv");
            System.exit(1);
        }
            computeTls(args[2], true, args[1], -1.0f);
        } else if (args.length == 1) {
            computeTls(args[0], false, null, -1.0f);
        } else {
            System.out.println("Veuillez respecter le format suivant:\ntls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>\nou\ntls <chemin-de-l'entrée>\nou\ntls");
            System.exit(1);
        }
    }

    public static void computeTls(String filepath, Boolean toCsv, String csvName, float threshold){
        Path startDir = Paths.get(filepath);

        try (Stream<Path> stream = Files.walk(startDir)) {
            Result[] results = stream
                    .filter(file -> file.toString().endsWith("Test.java"))
                    .map(file -> new Result(file.toString(), tloc.countTloc(file.toString()), tassert.countTassert(file.toString())))
                    .toArray(Result[]::new);

            // Arrays utilises pour calculer les top n%
            String[] cleanedStrings = new String[results.length];

            int[] TLOCSfromCleaned = new int[results.length];
            float[] TCMPfromCleaned = new float[results.length];

            for(int i = 0; i < results.length; i++) {
                cleanedStrings[i] = cleanString(results[i].filePath, results[i].tlocResult, results[i].tassertResult);

                TLOCSfromCleaned[i] = results[i].tlocResult;
                if(results[i].tassertResult != 0) TCMPfromCleaned[i] = (float) results[i].tlocResult / results[i].tassertResult;
                else TCMPfromCleaned[i] = 0f;
            }

            // Threshold de -1.0 signifie qu'on n'utilise pas le threshold
            if (threshold!=-1.0f){
                float percentile = 100 - threshold;  // To get the threshold for the top 10%

                // Sort the array
                Arrays.sort(TLOCSfromCleaned);
                Arrays.sort(TCMPfromCleaned);

                // Calculate the index that corresponds to the percentile
                int indexTLOCS = (int) Math.ceil((percentile / 100) * TLOCSfromCleaned.length) - 1;
                int indexTCMPS = (int) Math.ceil((percentile / 100) * TCMPfromCleaned.length) - 1;

                // Get the value at the calculated index
                float thresholdTLOCS = TLOCSfromCleaned[indexTLOCS];
                float thresholdTCMPS = TCMPfromCleaned[indexTCMPS];
                
                if(toCsv){
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {                        
                        // Premier ligne
                        writer.write("Chemin, Paquet, Classe, TLOC, TASSERT, tcmp");

                        // Use the results array as needed
                        for(int i = 0; i < results.length; i++) {
                            if(results[i].tassertResult != 0 && results[i].tlocResult >= thresholdTLOCS && (float)results[i].tlocResult/results[i].tassertResult >= thresholdTCMPS){
                                writer.newLine();
                                writer.write(cleanedStrings[i]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    for(int i = 0; i < results.length; i++) {
                        if(results[i].tassertResult != 0 && results[i].tlocResult >= thresholdTLOCS && (float)results[i].tlocResult/results[i].tassertResult >= thresholdTCMPS){
                            System.out.println(cleanedStrings[i]);
                        }
                    }
                }

            } else {
                if(toCsv){
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {
                    
                        
                        // Premier ligne
                        writer.write("Chemin, Paquet, Classe, TLOC, TASSERT, tcmp");

                        // Use the results array as needed
                        for(int i = 0; i < results.length; i++) {
                            if(cleanedStrings[i] != null){
                                writer.newLine();
                                writer.write(cleanedStrings[i]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    for(int i = 0; i < results.length; i++) {
                        System.out.println(cleanedStrings[i]);
                }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String cleanString(String path, int TLOC, int TASSERT) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Le filepath ne peut pas etre vide ou null");
        }
        if (TASSERT <= 0) {
            // throw new IllegalArgumentException("TASSERT doit etre un int positif superieur a 0");
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
        return String.format("%s, %s, %s, %d, %d, %.2f", path, packageName, fileName, TLOC, TASSERT, TCMP);
    }
}
