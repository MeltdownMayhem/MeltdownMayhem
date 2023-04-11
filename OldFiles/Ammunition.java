package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class Ammunition {
	
	public int width = 30;
	public int height = 55;
	public int x, y;
	public double vx, vy;

	
	public BufferedImage blueBullet;
	
	// General Constructor
	public Ammunition(int x, int y) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = -15;
		
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
		
		g.drawImage(image, x - width /2, y - width/2, width, height, null);
		
		g.setColor(new Color(255,0,0));
		g.drawOval(x - width/4, y, width/2, width/2);
		g.setColor(new Color(0,0,255));
		g.fillOval(x, y, 3, 3);
	}
	
	// makes the Bullet shoot Forward
	public void update() {
		x += vx;
		y += vy;
	}
	
	public boolean outsideBoard() {
		if (this.x + this.width / 2 < GamePanel.BOARD_START || this.x - this.width / 2 > GamePanel.BOARD_END || this.y + this.height / 2 < 0 || this.y - this.height / 2 > GamePanel.BOARD_HEIGHT) {
			return true;
		}
		return false;
	}
}
