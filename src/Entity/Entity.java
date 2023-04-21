package Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * De Entity class is een Superclass waarvan de Subclasses allemaal een vorm van beweging bevatten (een positie x,y en een snelheid vx,vy).
 * Subclasses: Enemy, Human, Drone, Ammunition en Barrel
 */
public class Entity {

	public int x, y;
	public double vx, vy;
	public int width, height, lives;
	protected static double SPEED_COEFFICIENT = 1; // Changing the speed of the entire game
	
	// RadiationOrb images
	public BufferedImage orbLeft1;
	public BufferedImage orbLeft2;
	public BufferedImage orbRight1;
	public BufferedImage orbRight2;
	
	// Rage Images
	public BufferedImage rageLeft1;
	public BufferedImage rageLeft2;
	public BufferedImage rageRight1;
	public BufferedImage rageRight2;
	public BufferedImage rageOffLeft1;
	public BufferedImage rageOffLeft2;
	public BufferedImage rageOffRight1;
	public BufferedImage rageOffRight2;
	
	// Every entity has its own counter and number for the getImage function
	int spriteCounter = 0;
	int spriteNum = 0;
	
	// Technical variables
	Random rng = new Random();
	Timer respawnTimer = new Timer();
	
	public Entity () {
		this.lives = 1;
	}
	
	// Default update method when not overwritten in subclasses
	public void update() {
		this.x += (int)(Entity.SPEED_COEFFICIENT * vx);
		this.y += (int)(Entity.SPEED_COEFFICIENT * vy);
	}
	
	// Give it a list of images and a list of time-intervals, and it will return you the image that needs to be drawn
	public BufferedImage getImage(ArrayList<BufferedImage> imageList, ArrayList<Integer> timeIntervalList) {
		spriteCounter++;

		if (spriteCounter >= timeIntervalList.get(spriteNum)) {
			spriteNum++;
			spriteCounter = 0;
			if (spriteNum >= imageList.size()) {
				spriteNum = 0;
			}
		}
		return imageList.get(spriteNum);
	}
}