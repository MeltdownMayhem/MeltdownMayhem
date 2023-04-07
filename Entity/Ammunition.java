package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ammunition {
	
	private int width = 30, height = 55;
	public static int hitboxradius = 15;
	public int x,y,v;
	
	public BufferedImage blueBullet;
	
	// General Constructor
	public Ammunition(int x, int y) {
		this.x = x;
		this.y = y;
		this.v = 20;
		
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
	
	// makes the Bullet shoot Forward
	public void update() {
		y -= v;
	}
}