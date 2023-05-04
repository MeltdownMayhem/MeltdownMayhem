package MeltdownMayhem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entity.Entity;
import Entity.Human;

public class Portal extends Entity{

	BufferedImage portalImage;
	public boolean resetSpawns;
	
	public Portal(){
		this.x = GamePanel.screenSize.width/2;
		this.y = GamePanel.screenSize.height/2;
		this.vx = 0;
		this.vy = 0;
		this.width = 200;
		this.height = 200;
		this.hitboxRadius = 25;
		
		try {
			portalImage = ImageIO.read(getClass().getResourceAsStream("/Portal/portal.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void portalCollision(Human human) {
		if (human.collision(this)) {
			resetSpawns = true;
		}
	}

	public void draw(Graphics g) {
		g.drawImage(portalImage, x - width/2, y - height/2, width, height, null);
	}
}
