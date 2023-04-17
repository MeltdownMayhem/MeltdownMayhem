package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;

public class Rage extends Enemy {
	
	// Rampage Variables
	protected static int rampageDuration = 120;
	protected static double rampageChance = 0.995;
	protected static int rampageCooldown = 240;
	protected static double rampageSpeed = 9;
	protected int rampageCounter = 50; // Counter to know when to activate rampage
	protected int rampageNum = 0; // Number of times it did a rampage
	
	// Image-Lists to send to the getImage(ArrayList, ArrayList) function
	ArrayList<BufferedImage> leftImageList = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> rightImageList = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> leftOffImageList = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> rightOffImageList = new ArrayList<BufferedImage>();
	ArrayList<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public Rage(int x) {
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		getRageImage();
	}
	
	public void getRageImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			rageLeft1 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageLeft1.png"));
			rageRight1 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageRight1.png"));
			rageLeft2 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageLeft2.png"));
			rageRight2 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageRight2.png"));
			rageOffLeft1 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageOffLeft1.png"));
			rageOffRight1 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageOffRight1.png"));
			rageOffLeft2 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageOffLeft2.png"));
			rageOffRight2 = ImageIO.read(getClass().getResourceAsStream("/Rage/rageOffRight2.png"));
			
			leftImageList.add(rageLeft1);
			leftImageList.add(rageLeft2);
			
			rightImageList.add(rageRight1);
			rightImageList.add(rageRight2);
			
			leftOffImageList.add(rageOffLeft1);
			leftOffImageList.add(rageOffLeft2);
			
			rightOffImageList.add(rageOffRight1);
			rightOffImageList.add(rageOffRight2);
			
			timeIntervalList.add(100);
			timeIntervalList.add(40);
			
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
	}
	
	public void update() {
		// Checking for Appearing and/or Spawning 
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
		} else if (this.spawning && this.y > ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.spawning = false;
		}
		
		// When rampage is inactive, the Rage just wanders around
		if (rampage == false) {
			
			rampageCounter++;
			
			randomSpeed();
			
			// Rampage skill activation
			if (rampageCounter >= rampageCooldown - (0.15 * rampageNum * rampageCooldown) && rng.nextDouble() > rampageChance) {
				rampage = true;
				rampageCounter = 0;
				rampageNum += 1;
				
				double x_direction = GamePanel.human.x + GamePanel.human.width/2 - (double) x;
				double y_direction = GamePanel.human.y + GamePanel.human.depth/2  - (double) y;
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