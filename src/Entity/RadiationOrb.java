package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
/**
 * De RadiationOrb is een groene Enemy type dat willekeurig rondvliegt.
 * Ability: projectielen schieten naar de player.
 */
public class RadiationOrb extends Enemy {
	
	// RadiationOrb images
	BufferedImage orbLeft1;
	BufferedImage orbLeft2;
	BufferedImage orbRight1;
	BufferedImage orbRight2;
	
	// Image-lists for the getImage(ArrayList, ArrayList) method
	List<BufferedImage> leftImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> rightImageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public RadiationOrb(int x) {
		enemySize = 85;
		enemyRadius = enemySize/2; // 43 pixels
		killScore = 10;
		
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		this.hitboxRadius = 30;
		
		getOrbImage();
	}
	
	public void getOrbImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		
		try {
			orbLeft1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft1.png"));
			orbLeft2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft2.png"));
			orbRight1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight1.png"));
			orbRight2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight2.png"));
			
			leftImageList = Arrays.asList(orbLeft1,orbLeft2);
			rightImageList = Arrays.asList(orbRight1,orbRight2);
			timeIntervalList = Arrays.asList(100,40);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Human human) {
		
		this.shootingCooldown++;
		
		this.x += (int) SPEED_COEFFICIENT * this.vx;
		this.y += (int) SPEED_COEFFICIENT * this.vy;
		
		spawnPriority();
		
		stayInField();
		
		randomSpeed();
		
		// These 2 lines makes the Enemies oscillate
		//this.x += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
		//this.y += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		
		if (vx < 0) {
			image = this.getImage(leftImageList,timeIntervalList);
		} else {
			image = this.getImage(rightImageList,timeIntervalList);
		}
		g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}