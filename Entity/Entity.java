package Entity;

import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Entity {
	
	public int x, y;
	public double vx, vy;
	public int lives;
	
	public BufferedImage human1;
	public BufferedImage orbLeft1;
	public BufferedImage orbLeft2;
	public BufferedImage orbRight1;
	public BufferedImage orbRight2;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	Random rng = new Random();
	
	public Entity () {
		
	}
}
