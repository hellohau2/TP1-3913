Membres de l'équipe : 

Haunui Louis 20189376
Vyncent Larose 20189960

Lien Github : https://github.com/hellohau2/TP1-3913
À noter que les versions finales du code sont sur la branche "latest" dû à des problèmes de permissions github.

Pour compiler le code : javac *.java
Les fichiers jars n'ont pas besoin d'être compilé.

Pour lancer les jars : 

tloc.jar : java -jar tloc.jar javaFilePath
tassert.jar : java -jar tassert.jar javaFilePath

Il y a deux façons de lancer tls.jar :
Sans output CSV : java -jar tls.jar filePath
Avec output CSV : java -jar tls.jar -o csvName.csv filePath

Il y a aussi deux façons de lancer tropcomp.jar : 
Sans output CSV : java -jar tropcomp.jar filePath threshold
Avec output CSV : java -jar tropcomp.jar -o csvName.csv filePath threshold

À noter que les fichiers qui ont un tassert nul ne sont pas compté comme des fichiers Test, et ne sont donc pas inclus dans le comptage de tls et tropcomp.
