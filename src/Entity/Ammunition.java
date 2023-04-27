package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
/**
 * Class that serves to create a moving Bullet on the Board.
 * It is possible to create both Human Bullets and Enemy Projectiles.
 */
public class Ammunition extends Entity {

	BufferedImage ammoImage;
	
	// players ammo
	public Ammunition(int x, int y) {
		this.width = 22;
		this.height = 32;
		
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = -20;
		
		this.hitboxRadius = 10;
		
		// Retrieve ammo image <Credits to RyiSnow>
		try {
			ammoImage = ImageIO.read(getClass().getResourceAsStream("/ammo/humanAmmo.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// enemies ammo
	public Ammunition(int x, int y, double vx, double vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		this.width = 22;
		this.height = 22;
		
		this.hitboxRadius = 11;
		
		// Retrieve enemy ammo image <Credits to RyiSnow>
		try {
			ammoImage = ImageIO.read(getClass().getResourceAsStream("/ammo/enemyAmmo.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Ammunition Collisions
	public void ammoCollisions(ArrayList<Ammunition> ammoList, GamePanel gp) {
		for (Ammunition bullet: ammoList) {
			if (this.collision(bullet)) {
				bullet.y = -1;
				gp.delProjectileList.add(this);
			}
		}
	}
	
	public boolean isOutBoard() {
		if (this.x < GamePanel.BOARD_START || this.y < 0 || this.x > GamePanel.BOARD_START + GamePanel.BOARD_WIDTH || this.y > GamePanel.BOARD_HEIGHT) {
			return true;
		}
		return false;
	}
	
	public void draw(Graphics g) {
		g.drawImage(ammoImage, x - width/2, y - height/2, width, height, null);
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}