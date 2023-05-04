package MeltdownMayhem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Entity;
import Entity.Human;

public class Portal extends Entity{

	BufferedImage portalImage;
	Random rng;
	
	public Portal(){
		rng = new Random();
		this.x = Window.BOARD_START + rng.nextInt(Window.BOARD_WIDTH - 100);
		this.y = rng.nextInt (Window.BOARD_HEIGHT - 150);
		this.vx = 0;
		this.vy = 0;
		this.width = 74;
		this.height = 116;
		this.hitboxRadius = 35;
		
		try {
			portalImage = ImageIO.read(getClass().getResourceAsStream("/Portal/portal.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public boolean portalCollision(Human human) {
		if (human.collision(this)) {
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		g.drawImage(portalImage, x - width/2 + 50, y - height/2 + 75, width, height, null);
	}
}
