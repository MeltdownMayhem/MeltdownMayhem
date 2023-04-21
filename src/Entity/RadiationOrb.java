package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * De RadiationOrb is een groene Enemy type dat willekeurig rondvliegt.
 * Ability: projectielen schieten naar de player.
 */
public class RadiationOrb extends Enemy {

	ArrayList<BufferedImage> leftImageList = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> rightImageList = new ArrayList<BufferedImage>();
	ArrayList<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public RadiationOrb(int x) {
		enemySize = 80;
		enemyRadius = enemySize/2; // 40 pixels
		killScore = 100;
		
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		getOrbImage();
	}
	
	public void getOrbImage() { // <Credits to RyiSnow>
		
		try {
			orbLeft1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft1.png"));
			orbLeft2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft2.png"));
			orbRight1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight1.png"));
			orbRight2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight2.png"));
			
			leftImageList.add(orbLeft1);
			leftImageList.add(orbLeft2);
			
			rightImageList.add(orbRight1);
			rightImageList.add(orbRight2);
			
			timeIntervalList.add(100);
			timeIntervalList.add(40);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		
		if (vx < 0) {
			image = this.getImage(leftImageList,timeIntervalList);
		} else {
			image = this.getImage(rightImageList,timeIntervalList);
		}
		g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
	}
	
	@Override
	public void update() {
		
		this.shootCooldown++;
		
		this.x += (int) SPEED_COEFFICIENT * this.vx;
		this.y += (int) SPEED_COEFFICIENT * this.vy;
		
		/*
		Een enemy is uit zijn appearingfase als hij boven de bovenkant van het scherm geraakt.
		
		Een enemy is uit zijn spawnfase als hij onder de UPPER_BORDER geraakt. Vanaf dan kan hij niet meer boven
		de UPPERBORDER geraken om plaats te maken voor het spawnen van andere enemies.
		*/
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
		} else if (this.spawning && this.y > ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.spawning = false;
		}
		
		spawnPriority();
		
		stayInField();
		
		randomSpeed();
		
		// Volgende 2 lijnen laten de enemies oscilleren
		//this.x += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
		//this.y += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
	}
}