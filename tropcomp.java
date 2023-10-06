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
        if (args.length == 0 || (args.length != 2 && args.length != 4)) {
            System.out.println("Usage : -o yourfilename.csv filepath threshold \nor\nUsage : filepath threshold");
            System.exit(1);
        }
        else if (args[0].equals("-o") && args.length == 4 && args[1].endsWith(".csv")){
            try {
                float arg3Float = Float.parseFloat(args[3]);
                if (arg3Float < 0.0f || arg3Float > 99.99999f) {
                    System.out.println("Le threshold doit etre positif et inferieur a 100");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Le threshold doit etre une valeur numerique\nUsage : -o yourfilename.csv filepath threshold \nor\nUsage : filepath threshold");
                System.exit(1);
            }
            
            tls.computeTls(args[2], true, args[1], Float.parseFloat(args[3]));
        }
        else if (args.length == 2) {
            try {
                float arg1Float = Float.parseFloat(args[1]);
                if (arg1Float < 0.0f || arg1Float > 99.99999f) {
                    System.out.println("Le threshold doit etre positif et inferieur a 100");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Le threshold doit etre une valeur numerique\nUsage : -o yourfilename.csv filepath threshold \nor\nUsage : filepath threshold");
                System.exit(1);
            }
            tls.computeTls(args[0], false, null, Float.parseFloat(args[1]));
        }
        else {
            System.out.println("Veuillez respecter le format suivant:\ntropcomp -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée> threshold\nou\ntropcomp <chemin-de-l'entrée> threshold\nou\ntls");
            System.exit(1);
        }
    }
}