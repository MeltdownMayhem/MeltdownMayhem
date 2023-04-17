package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class Ammunition {
	
	public int width = 30;
	private int height = 55;
	public static int hitboxRadius = 15;
	public int x,y;
	public double vx, vy;
	
	public BufferedImage blueBullet;
	
	// ammo for human
	public Ammunition(int x, int y) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = -20;
		
		getBulletImage();
	}
	
	// ammo for enemies
		public Ammunition(int x, int y, double vx, double vy) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
				
			getBulletImage();
		}
	
	public void getBulletImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			blueBullet = ImageIO.read(getClass().getResourceAsStream("/Human/blueBullet.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		image = blueBullet;
		
		g.drawImage(image, x - width/2, y - height/2, width, height, null);
	}
	
	public void update() {
		this.x += vx;
		this.y += vy;
	}
	
	public boolean out() {
		if (this.x < GamePanel.BOARD_START || this.y < 0 || this.x > GamePanel.BOARD_START + GamePanel.BOARD_WIDTH || this.y > GamePanel.BOARD_HEIGHT) {
			return true;
		}
		return false;
	}
}