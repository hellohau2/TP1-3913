import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Arrays;

public class tropcomp {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage : -o yourfilename.csv filepath threshold \n or \n Usage : filepath threshold");
            System.exit(1);
        }

        // for(int i = 0; i <args.length;i++){
        //     System.out.println(args[i] + "- " + i);
        // }

        if (args[0].equals("-o")){
            computeTropcomp(args[2], true, args[1], Float.parseFloat(args[3]));
        }
        else{
            computeTropcomp(args[0], false, null, Float.parseFloat(args[1]));
        }
    }

    // Overwrites already existing csv files with same name  
    public static void computeTropcomp(String filepath, Boolean toCsv, String csvName, float threshold){
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
                TCMPfromCleaned[i] = (float) results[i].tlocResult / results[i].tassertResult;
            }

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
            
            // System.out.println(TLOCSfromCleaned.length);
            // System.out.println(indexTLOCS);
            // System.out.println(indexTCMPS);
            // System.out.println(threshold);

            if(toCsv){
                // Use try-with-resources statement to automatically close the resources
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvName))) {
                    
                    // Premier ligne
                    writer.write("Chemin, Paquet, Classe, TLOC, TASSERT, tcmp");
                    writer.newLine();
                    
                    // Use the results array as needed
                    for(int i = 0; i < results.length; i++) {
                        if(results[i].tassertResult != 0 && results[i].tlocResult >= thresholdTLOCS && (float)results[i].tlocResult/results[i].tassertResult >= thresholdTCMPS){
                            writer.write(cleanedStrings[i]);
                            writer.newLine();
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for(int i = 0; i < results.length; i++) {
                    // Si on est dans les top n% pour TLOC et TCMP
                    if(results[i].tassertResult != 0 && results[i].tlocResult >= thresholdTLOCS && (float)results[i].tlocResult/results[i].tassertResult >= thresholdTCMPS){
                        System.out.println(cleanedStrings[i]);
                    }
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

}

class Result {
    String filePath;
    int tlocResult;
    int tassertResult;
    
    Result(String filePath, int tlocResult, int tassertResult) {
        this.filePath = filePath;
        this.tlocResult = tlocResult;
        this.tassertResult = tassertResult;
    }
}
