import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class tropcomp {
    public static void main(String[] args) {
        Path startDir = Paths.get("jfreechart-master");
        
        try (Stream<Path> stream = Files.walk(startDir)) {
            Result[] results = stream
                .filter(file -> file.toString().endsWith("Test.java"))
                .map(file -> new Result(file.toString(), TLOC.countTLOC(file.toString()), tassert.countTAssert(file.toString())))
                .toArray(Result[]::new);
            
            // Use the results array as needed
            for(int i = 0; i < results.length; i++) {
                System.out.println("File " + (i+1) + ": " 
                                   + results[i].filePath + ", TLOC: " 
                                   + results[i].tlocResult + ", TASSERT: " 
                                   + results[i].tassertResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
