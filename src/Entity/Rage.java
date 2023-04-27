package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
/**
 * De Rage is een grijze en rode Enemy soort.
 * Als de Rage grijs is beweegt het willekeurig rond zoals een RadiationOrb.
 * Ability: Rampage
 * 	        Wanneer de Rage in een Rampage gaat, kleurt hij rood en vliegt het rechtstreeks naar de human's locatie.
 * 	        Op zijn weg duwt hij de andere Enemies lichtjes opzij om ongehinderd door te kunnen.
 */
public class Rage extends Enemy {

	// Rampage Variables
	protected int rampageDuration = 120;
	protected double rampageChance = 0.994;
	protected int rampageCooldown = 230;
	protected double rampageSpeed = 9;
	protected int rampageCounter = 50; // Counter to know when to activate rampage
	protected int rampageNum = 0; // Number of times it went on a rampage
	
	// Rage images
	BufferedImage rageLeft1;
	BufferedImage rageLeft2;
	BufferedImage rageRight1;
	BufferedImage rageRight2;
	BufferedImage rageOffLeft1;
	BufferedImage rageOffLeft2;
	BufferedImage rageOffRight1;
	BufferedImage rageOffRight2;
	
	// Image-lists for the getImage(ArrayList, ArrayList) method
	List<BufferedImage> leftImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> rightImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> leftOffImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> rightOffImageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public Rage(int x) {
		enemySize = 85;
		enemyRadius = enemySize/2; // 40 pixels
		killScore = 15;
		
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		this.hitboxRadius = 30;
		
		getRageImage();
	}
	
	public void getRageImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		
		try {
			rageLeft1 = ImageIO.read(getClass().getResourceAsStream("/rage/rageLeft1.png"));
			rageRight1 = ImageIO.read(getClass().getResourceAsStream("/rage/rageRight1.png"));
			rageLeft2 = ImageIO.read(getClass().getResourceAsStream("/rage/rageLeft2.png"));
			rageRight2 = ImageIO.read(getClass().getResourceAsStream("/rage/rageRight2.png"));
			rageOffLeft1 = ImageIO.read(getClass().getResourceAsStream("/rage/rageOffLeft1.png"));
			rageOffRight1 = ImageIO.read(getClass().getResourceAsStream("/rage/rageOffRight1.png"));
			rageOffLeft2 = ImageIO.read(getClass().getResourceAsStream("/rage/rageOffLeft2.png"));
			rageOffRight2 = ImageIO.read(getClass().getResourceAsStream("/rage/rageOffRight2.png"));
			
			leftImageList = Arrays.asList(rageLeft1,rageLeft2);
			rightImageList = Arrays.asList(rageRight1,rageRight2);
			leftOffImageList = Arrays.asList(rageOffLeft1,rageOffLeft2);
			rightOffImageList = Arrays.asList(rageOffRight1,rageOffRight2);
			timeIntervalList = Arrays.asList(100,40);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		
		if (vx < 0) {
			if (rampage == false) {
				image = this.getImage(leftOffImageList,timeIntervalList);
			} else {
				image = this.getImage(leftImageList,timeIntervalList);
			}
		} else {
			if (rampage == false) {
				image = this.getImage(rightOffImageList,timeIntervalList);
			} else {
				image = this.getImage(rightImageList,timeIntervalList);
			}
		}
		g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
	
	@Override
	public void update(Human human) {
		
		// When rampage is inactive, the Rage just wanders around
		if (rampage == false) {
			
			rampageCounter++;
			
			randomSpeed();
			
			// Rampage skill activation
			if (rampageCounter >= rampageCooldown - (0.15 * rampageNum * rampageCooldown) && rng.nextDouble() > rampageChance) {
				rampage = true;
				enemySize += 10;
				hitboxRadius += 5;
				rampageCounter = 0;
				rampageNum ++;
				
				double x_direction = human.x + human.width/2 - (double) x;
				double y_direction = human.y + human.height/2  - (double) y;
				double norm = Math.sqrt(Math.pow(x_direction,2) + Math.pow(y_direction,2));
				vx = (x_direction/norm) * rampageSpeed;
				vy = (y_direction/norm) * rampageSpeed;
			}
		
		// When rampage is active, the Rage moves towards the Human
		} else {
			rampageCounter++;
			
			// Rampage skill deactivation
			if (rampageCounter >= rampageDuration) {
				rampage = false;
				enemySize -= 10;
				hitboxRadius -= 5;
				rampageCounter = 0;

				vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
				vy = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
			}
		}
		this.x += (int) SPEED_COEFFICIENT * this.vx;
		this.y += (int) SPEED_COEFFICIENT * this.vy;
		
		stayInField();
	}
}