package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class Barrel {
	
	public int x,y;
	public double vy;
	public int width = 130;
	public int height = 70;
	
	public BufferedImage barrel1;
	
	Random rng = new Random();
	
	// General Constructor
	public Barrel() {
		int x = rng.nextInt(GamePanel.BOARD_START, GamePanel.BOARD_END - width);
		this.x = x;
		this.y = -height;
		this.vy = 2 * rng.nextDouble() + 3;
		
		getBarrelImage();
	}
	// TODO: THIS DOES NOTHING, CAN IT BE DELETED?
	// Constructor to place a barrel on specific position and certain speed
	/*public Barrel(int x, int y, int vy) {
		this.x = x;
		this.y = y;
		this.vy = vy;
	}*/ 
	
	public void getBarrelImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		try {
			barrel1 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkBarrelCollision() {
		if (GamePanel.human.x > x - width*3/4 && GamePanel.human.x < x + width && GamePanel.human.y < y + height && GamePanel.human.y > y) {
			GamePanel.human.lives -= 1;		//3/4 factor is because of the human hitbox
			GamePanel.human.x = GamePanel.PANEL_WIDTH/2 - GamePanel.human.width/2;
			GamePanel.human.y = GamePanel.BOARD_HEIGHT -GamePanel. human.depth - GamePanel.BOARD_HEIGHT/15;
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		image = barrel1;
		
		g.drawImage(image, x, y, width, height, null);
	}
	
	// make the barrel roll down
	public void update() {
		y += (int) Entity.SPEED_COEFFICIENT * vy;
	}

	public void updateBarrels1(Barrel barrel) {
		// TODO Auto-generated method stub
		
	}
}