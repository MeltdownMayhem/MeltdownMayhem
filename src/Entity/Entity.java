package Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Entity {
	
	public int x, y;
	public double vx, vy;
	public int lives;
	protected static double SPEED_COEFFICIENT = 1;
	
	// Character Images
	public BufferedImage human1;
	public BufferedImage drone1;
	public BufferedImage drone2;
	
	// Enemy Images
	public BufferedImage orbLeft1;
	public BufferedImage orbLeft2;
	public BufferedImage orbRight1;
	public BufferedImage orbRight2;
	public BufferedImage rageLeft1;
	public BufferedImage rageLeft2;
	public BufferedImage rageRight1;
	public BufferedImage rageRight2;
	public BufferedImage rageOffLeft1;
	public BufferedImage rageOffLeft2;
	public BufferedImage rageOffRight1;
	public BufferedImage rageOffRight2;
	
	public int spriteCounter = 0;
	public int spriteNum = 0;
	
	Random rng = new Random();
	
	public Entity () {
		
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