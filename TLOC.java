public class TLOC{
    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("Provide an argument");
            System.exit(1);
        }

        System.out.println("TLOC : " + countTLOC(args[0]));
    }

    static int countTLOC(String file){
        System.out.println(file);
        int tloc = 0;
        return tloc;
    }
}