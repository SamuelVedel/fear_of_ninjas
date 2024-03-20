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
d'avoir java, javac et Makfile d'installer, et il suffit de taper la commande
```bash
make
```
dans son terminal pour compiler et lancer le jeu.

Vous pouvez aussi compiler ce qu'il faut sans lancer le jeu avec la commande
```bash
make compile
```
Et le lancer le jeu sans rien compiler avec la commande
```bash
make launch
```

Il est bien sûr aussi possible de compiler le code en utilisant un IDE.

Si vous vouler compiler et lancé le jeu sans utiliser le Makefile,
alors alors vous pouvez compiler avec la commande
```bash
javac -encoding iso-8859-1 -d bin/ -cp src/ /*/*/*/*.java /*/*/*/*/*.java /*/*/*/*/*/*.java
```
et vous pouvez lancer le jeu avec
```bash
java -cp bin fr.svedel.fod.MainFod
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
