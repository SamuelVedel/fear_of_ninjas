package fr.svedel.fod.play.debuff;

import fr.svedel.fod.UsefulTh;
import fr.svedel.fod.play.Entity;
import fr.svedel.fod.play.Power;
import fr.svedel.fod.play.Room;
import fr.svedel.fod.play.particle.ClassicParticle;

/**
 * 
 * Class créée le 11/02/2023
 * 
 * @author Samuel Vedel
 *
 */
public class Poison extends Debuff{
	
	/** 
	 * pourcentage de la vie max de l'énemie
	 * que le poinson lui enlève
	 */
//	private int perDammage;
	
	private static final int START_DAMMAGE = 2;
	private int dammage = START_DAMMAGE;
	
	private double tEffect = 0;
	private final int maxTEffect = 60;
	
	public Poison(Entity victim, Room room) {
		super(victim, room);
		type = POISON_TYPE;
	}
	
//	protected void preEffect() {};
	
	@Override
	protected void effect(double delta) {
		if (tEffect >= maxTEffect) {
			if (victim.life > 1) {
//				victim.life -= perDammage*victim.maxLife*0.01;
				victim.life -= dammage;
				dammage += 1;
				if (victim.life < 1) victim.life = 1;
				
				// effet de particule
				int nParticles = UsefulTh.rand.nextInt(6-3)+3;
				for (int i = 0; i < nParticles; i++) {
					double pX = victim.x+victim.w/2;
					double pY = victim.y+victim.h/2;
					double pVX = 2*(UsefulTh.rand.nextBoolean()? 1 : -1)
						*UsefulTh.rand.nextDouble()
						*0.2*UsefulTh.pixelW;
					double pVY = 2*(UsefulTh.rand.nextBoolean()? 1 : -1)
						*UsefulTh.rand.nextDouble()
						*0.2*UsefulTh.pixelH;
					room.particles.add(new ClassicParticle(pX, pY, UsefulTh.pixelW, UsefulTh.pixelH, pVX, pVY, "textures/particles/skull.texture"));
				}
			}
			tEffect = 0;
		}
		tEffect += delta;
		if (t >= tMax-1) {
//			perDammage = 0;
			dammage = START_DAMMAGE;
		}
	}
	
	public void cancel() {
		super.cancel();
		dammage = START_DAMMAGE;
	}
	
	@Override
	public void reset(Entity giver) {
		super.reset(giver);
		int num = giver.powers[Power.POISON.id];
		tMax = (3+num)*60;
//		if (num <= 3) {
////			if (num > perDammage) perDammage = 5+num;
//			if (num == 1) perDammage = 5;
//			else if (num == 2) perDammage = 7;
//			else perDammage = 8;
//			tMax = 5*60;
//		} else {
//			perDammage = 5;
//			tMax = (5+num-3)*60;
//		}
	}
}
