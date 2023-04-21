package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
/**
 * De Ammunition class is een verzamelclasse voor al de soorten kogels en projectielen dat het spel bevat.
 * Tot nu toe is er de ammo voor de Human en projectiles voor de RadiationOrbs.
 */
public class Ammunition extends Entity{

	public static int hitboxRadius = 15;
	BufferedImage ammoImage;
	
	// players ammo
	public Ammunition(int x, int y) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = -20;
		
		this.width = 30;
		this.height = 55;
		
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
		
		this.width = 35;
		this.height = 35;
		
		// Retrieve ammo Image <Credits to RyiSnow>
		try {
			ammoImage = ImageIO.read(getClass().getResourceAsStream("/ammo/enemyAmmo.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(ammoImage, x - width/2, y - height/2, width, height, null);
	}
	
	public boolean isOutBoard() {
		if (this.x < GamePanel.BOARD_START || this.y < 0 || this.x > GamePanel.BOARD_START + GamePanel.BOARD_WIDTH || this.y > GamePanel.BOARD_HEIGHT) {
			return true;
		}
		return false;
	}
}