import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TLOC{
    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("Provide an argument");
            System.exit(1);
        }

        System.out.println("TLOC : " + countTLOC(args[0]));
    }

    static int countTLOC(String file){
        int tloc = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String text = "";
            String line;
            while ((line = br.readLine()) != null){
                text += line;
            }

            System.out.println(text);
        }catch(IOException e){
            e.printStackTrace();
        }
        return tloc;
    }
}