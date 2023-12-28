 ___
(° °)  <(hey salut)
 ) (
(( ))

Dans ce jeux, le but est de parcourir le plus de niveau possible,
pour cela à chaque niveau il faut tuer un certain nombre de monstres
(+2 par niveau). Entre chaque niveau, il faut choisir entre trois amélioration,
et tout les deux niveau, se sont les énemies qui on une amélioration aléatoire.

info importante :
quand on augmente la taille du niveau sur l'éditeur
vient un moment ou l'écran n'est plus assez grand, mais
les barre pour scroller ne se rafraichisse pas automatiquement
donc si elle existent déja pour les rafraichir il suffit juste
de les bouger /!\ mais si elles n'exitent pas encore il faut
redimentionner la fenêtre pour les faire apparaitre /!\.
Je m'éxcuse je n'ai vraiment pas réussi à éviter ce bug


maj d'avant :
 - les énemies ne spawn plus dans la geule
 - ajout du power up canard potable
 - ajout du power up pas de lune (qui est un saut multiple)
 - ajout du bloc trampoline
 - ajout d'un bloc plein
 - la gille qui est affiché de base dans l'éditeur de niveaux
 - les énemies spawn de plus en plus vite au fil des niveaux

maj d'avant :
 - légère augmentation du saut (faudra voir si c'est assez)
 - augmentation de la vitesse des balles
 - les ninja et les shield man ont moins de chances de spawn
   que les autres énnemies
 - les énnemies ne peuvent pas avoir les power up qui ne leurs sont inutiles
   (c'est à dire pas de lune et canard potable)
 - on ne peut plus avoir plusieur fois le même choix quand
   on choisit une amélioration
 - maintenant on voit quelle est le numéro du niveau

maj d'avant :
 - ajout de l'énemie invocateur qui n'apparait qu'a partir du niveau 10
 - ajout du pouvoir d'une pierre deux coup
 - on peut maintenant attaquer un shieldMan depuis le haut
 - j'ai codé une option pause en 2 seconde
 - agmenter de deux blocs la zone ou les énemies ne peuvent pas spawn atour de toi

maj d'avant (16/01/2023) :
 - modification des trampoline
 - ajout d'un bloc qui change tout les 5 seconde entre bloc qu'on traverse
   et bloc avec toutes les collisions
 - ajout de rien
 - ajout de l'énemie âme
 - ajout de l'amélioration cadence
 - ajout de l'énemie bombardier

maj d'avant (28/01/2023) :
 - maintenant les bombes peuvent être critque
 - on voit les amélioratoin actuelles dans l'écran de choix
 - ajout de l'amélioration tourelle
 - les énemies ne peuvent plus spawner à moitié encastré dans un mur
 - maintenant les dégats des bombes décrois linéairement en fonction
   de la distance
 - maintenant on ne peut pas boire un canard potable si on à notre vie au max
   et les tourelles peuvent aussi en profiter
 - correction d'une légère erreur dans l'animaiton de marche du perso
   jouable (oui il y à deux img dans l'animation et j'ai fait une erreur)
 - modification du trampoline

maj d'avant (23/02/2023) :
 - modification du mouvement des bombardier
 - augmentation du rayon de tp des âmes
 - maintenant les âmes apparraissent à la mort de d'autres énemies
 - ajout d'un chat et de commandes
 - on peut faire clic droit sur certain blocs de l'éditeur de niveau
   pour avoir une rapide description
 - maintenant les map dont le nom commance par 'N' ne peuvent pas être lancée
 - ajout du pouvoir poison (potentiellement à poffiner)
 - les balles traversent les blocs qui n'ont aucune capacité (genre les blocs avec 0
   collisions, mais possibilité de rentrée les fps en arguments si on execute le jeux
   avec cmd, sinon 60 fps)

maj d'avant (08/03/2023) :
 - ajout des énemies slimes
 - modification du poison
 - on ne peut à nouveau plus tirer dans les blocs qui ne font rien

maj d'avant (25/07/2023) :
 - diminution du spawn des slimes
 - ajout du pouvoir bombe
 - ajout de particules lors de la téléportation des âmes
 - ajout d'une map
 - optimisation
 - amélioration de l'affichage du texte et des tourelles
 - assombrissement de la couleur du fond
 - ajout de particule de lumière qui tombent dans les niveaux
 - ajout du boss serpent
 - maintenant quant les blob se suicide, les balles partent en diagoanle
 - maintenant quand les gros et moyen slime meurt, cela n'incrémente plus le score
 - les invocateurs ne préviennet plus quand ils apparraissent

cette maj :
 - maintenant si une salle à des variante, on peut les regroupé dans un fichier
   afin de ne pas acroitre la probabilité de tombé dessus 
 - quand les énemies obtiennent un pouvoir, ou que l'on ramasse un iteme
   une description du pouvoir apparaît dans le chat
 - ajout du pouvoir petrification
 - le pouvoir d'une pierre de coup passe de +10% à +20% à chaque
   fois qu'on le prend
 - ajout de l'énemie ésprit de la forêt, qui soigne les énemies et nous vole de la vie
 - réduction de l'apparition des canard potable
 - amélioration du code pour supporter les différences et les fluctuations de fps
   (fps modifiable via la commande setFps)
 - les actions sourie ne marche plus qu'avec le clic gauche
 - modification de l'extention pour les fichier des texutres et des salles.
   .txt -> .texture pour les textures
   .txt -> .room pour les salles
 - ajout du pouvoir TP
 - ajout d'une nouvelle couleur, qui est la couleur aléatoire mais en plus sombre
 - lorsqu'on stack le pouvoir bomb ou tp, on peut l'utiliser plusieurs fois
 - les bombes tirée par le joueur vont dans la direction de son
 - y'avais un bug où le panneau de selection de pouvoir pouvais descendre trop bas
 - le pleine ecran marche sur ubuntu


si cela vous interesse vous êtes invité à me faire des retours
car ce jeux est en cours développement





































 /\_/\
(=*-*=) )  <(miaou miaou)
 )   ( (
(_____))