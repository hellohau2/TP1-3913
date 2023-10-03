public class tls {

    public static void main(String[] args) {
        if (args.length == 0) {
            // tls
            // Faire tls a l'interieur du dossier actuel
        }
        else if (args.length == 1) {
            String folderPath = args[0];
            // tls path
            // Sortie en ligne de commande, pas de fichier .csv produit
        }
        else if (args.length == 3 && args[0] == "-o") {
            String folderPath = args[2];
            // tls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>
            // Sortie dans un fichier .csv
        }
        else {
            System.out.println("Fichier inexistant");
            System.exit(1);
        }
    }

    public void allJavaFiles(String folderPath) {
        // TODO: retourne ou cree une liste contenant tous les noms de fichiers Java
    }
}
