package fr.SamuelVedel.FOD;

import fr.SamuelVedel.Menu.Menu;
import fr.SamuelVedel.Play.Play;

/*  ___
 * (° °)
 *  ) (
 * (( ))
 */

/**
 * <style>
 * p {
 * color:red
 * }
 * strong {
 * color:cyan
 * }
 * body {
 * background-color:black
 * }
 * li {
 * color:magenta
 * }
 * </style>
 * 
 * <p>
 * le rôle de cette class est de lancer le progamme
 * </p>
 * <p>
 * code commencé le <strong>17/06/2022</strong>
 * </p>
 * 
 * <ul>
 * <strong>To do list :</strong>
 * <br>---- facile  ----
 * <li>ajouter un pouvoir sur les dégats</li>
 * <li>mettre un pouvoir de dash</li>
 * <li>mettre un timer de non mouvement pour les énemies</li>
 * <li>ajouter plein de commentaire</li>
 * ---- faudra attendre pour ça ----
 * <li>peut-être ajouter un timer</li>
 * <li>peut-être ajouter les stats en fin de partie</li>
 * <li>pouvoir changer les touches</li>
 * <li>ajouter un mode où faut attendre</li>
 * <li>un mode pause qui soit un peu plus cali</li>
 * <li>option de la partie</li>
 * <li>ajouter des crédits</li>
 * </ul>
 * 
 * <ul>
 * <strong>Ajout à essayer :</strong>
 * <li>le pouvoir sur la cadence</li>
 * </ul>
 * 
 * @author Samuel Vedel
 * 
 * @see <a href="https://hey.fr">hey</a>
 */
public class MainFOD {
	
	public static void main(String[] args) {
		while (true) {
			new Menu();
			new Play();
		}
		
//		int level = 400;
//		int n = 0;
//		int tot = 1000000;
//		for (int i = 0; i < tot; ++i) {
//			if (UsefulTh.rand.nextInt(500/(level/10+1)) == 0) {
//				++n;
////				System.out.println("hehe");
//			}
//		}
//		System.out.println((double)n/tot);
//		n = 0;
//		int tot2 = tot/8;
//		double delta = (double)tot/tot2;
//		for (int i = 0; i < tot2; ++i) {
//			if (UsefulTh.deltaRandom((500/(level/10+1)), delta)) {
//				++n;
//				//System.out.println("hoho");
//			}
//		}
//		System.out.println((double)n/(double)tot);
	}
}
