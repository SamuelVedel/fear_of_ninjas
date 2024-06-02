# Fear of ninjas
```
 ___
(° °)  <(hey salut)
 ) (
(( ))
```

## Principe du jeu
Dans ce jeux, le but est de parcourir le plus de niveau possible,
pour cela à chaque niveaux il faut tuer un certain nombre de monstres
(+2 p chaque niveaux). Entre chaque niveaux, il faut choisir entre trois améliorations,
et tout les deux niveaux, se sont les énemies qui on une amélioration aléatoire.

info importante pour ceux qui ont léditeur de niveau :
quand on augmente la taille du niveau sur l'éditeur
vient un moment ou l'écran n'est plus assez grand, mais
les barre pour scroller ne se rafraichisse pas automatiquement
donc si elle existent déja pour les rafraichir il suffit juste
de les bouger /!\ mais si elles n'exitent pas encore il faut
redimentionner la fenêtre pour les faire apparaitre /!\.
Je m'éxcuse je n'ai vraiment pas réussi à éviter ce bug

## Compilation et lancement
Pour compiler et lancer le jeu il est recommander d'avoir un os linux,
ainsi que java, javac et Makfile d'installer.

Pour compiler le code il faut d'abord installer la libraire vcomponent [ici](https://github.com/SamuelVedel/VComponent).
Ensuite pour compiler et lancer le jeu, il suffit de taper dans son terminal la commande
```shell
VC_PATH=<vcpath> make
```
en remplacant `<vcpath>` par le chemin vers l'endroit où la libraire vcomponent à était installer.

Vous pouvez aussi compiler ce qu'il faut sans lancer le jeu avec la commande
```shell
VC_PATH=<vcpath> make build
```
Et le lancer le jeu sans recompiler avec la commande
```shell
VC_PATH=<vcpath> make run
```

Si vous voulez créé un fichier jar du projet, alors il faut taper
```shell
VC_PATH=<vcpath> make jar
```
Cela créé le fichier jar `Fear_of_ninjas.jar` qui peut être renomé. Ce fichier contient dirrectement la libraire vcomponent, on peut donc simplement lancé le fichier jar avec la commande:
```shell
java -jar Fear_of_ninjas.jar
```
Attention le fichier jar doir être lancé dans le repertoire contenant les dossiers `textures` et `rooms`.

Il est bien sûr aussi possible de compiler le code en utilisant un IDE.

Si vous vouler compiler et lancé le jeu sans utiliser le Makefile,
alors alors vous pouvez compiler avec la commande
```shell
javac -encoding iso-8859-1 -d bin/ -cp src:<vcpath> /*/*/*/*.java /*/*/*/*/*.java /*/*/*/*/*/*.java
```
et vous pouvez lancer le jeu avec
```shell
java -cp bin:<vcpath> fr.svedel.fod.MainFod
```

## Voilà
si cela vous interesse vous êtes invité à me faire des retours
car ce jeu est en cours développement




































```
 /\_/\
(=*-*=) )  <(miaou miaou)
 )   ( (
(_____))
```
