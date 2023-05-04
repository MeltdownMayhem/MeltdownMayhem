package Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
import MeltdownMayhem.Window;
/**
 * Class to create a moving Barrel on the Board.
 * Serves as an obstacle to the Human and can be destroyed by the Drone.
 * When destroyed, it has a chance to drop some Ammo.
 */
public class Barrel extends Entity {

	public boolean gettingDamaged = false;
	
	GamePanel gp;
	Drone drone;
	
	BufferedImage barrel1;
	BufferedImage barrel2;
	BufferedImage barrel3;
	
	public Barrel(Drone ron, GamePanel panel) {
		this.width = 140;
		this.height = 75;
		
		this.x = rng.nextInt(Window.BOARD_START, Window.BOARD_END - width);
		this.y = -height;
		this.vx = 0;
		this.vy = 2 * rng.nextDouble() + 3;
		
		this.lives = 3;
		gp = panel;
		drone = ron;
		
		this.hitbox = new Rectangle(x, y + 5, width, height - 10);
		
		// Retrieve barrel image <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			barrel1 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel1.png"));
			barrel2 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel2.png"));
			barrel3 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel3.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Barrel Collisions
	public void barrelCollisions(ArrayList<Barrel> barrelList, ArrayList<Ammunition> ammoList) {
		for (Ammunition bullet: ammoList) {
			for (Barrel barrel: barrelList) {
				if (bullet.collision(barrel)) {
					bullet.y = -1000;
				}
			}
		}
	}
	
	public class DestroyBarrelTimerTask extends TimerTask {
		@Override
		public void run() {
			gettingDamaged = false;
			if (collision(drone) && drone.damageBarrel == true) {
				lives--;
				if (lives == 0) {
					gp.score += 5;
				}
			} else {
				vy = 2 * rng.nextDouble() + 3 - (3 - lives);
				drone.barrelSlot = null;
			}
		}
	}
	
	@Override
	public void update() {
		this.x += (int)(SPEED_COEFFICIENT * vx);
		this.y += (int)(SPEED_COEFFICIENT * vy);
		
		if (lives == 0) {
			PowerUp.spawnPowerUp(this, gp);
			y = Window.BOARD_HEIGHT + 100;
		}
		
		if (hitboxRadius == 0) {
			hitbox.x = x;
			hitbox.y = y + 5;
		}
	}
	
	public void draw(Graphics g) {
		if (lives == 3) {
			g.drawImage(barrel1, x, y, width, height, null);
		} else if (lives == 2) {
			g.drawImage(barrel2, x, y, width, height, null);
		} else {
			g.drawImage(barrel3, x, y, width, height, null);
		}
		//g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}
}