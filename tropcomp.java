import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;ß
import java.util.Arrays;

public class tropcomp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage : -o yourfilename.csv filepath threshold \n or \n Usage : filepath threshold");
            System.exit(1);
        }
        if (args[0].equals("-o")){
            tls.computeTls(args[2], true, args[1], Float.parseFloat(args[3]));
        }
        else{
            tls.computeTls(args[0], false, null, Float.parseFloat(args[1]));
        }
    }
}