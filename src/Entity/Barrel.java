package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
/**
 * De Barrel class maakt de Barrels aan die naar beneden 'rollen' op het scherm.
 * Zijn doel is een obstakel te zijn in de weg van de Human.
 * De Drone vliegt en heeft dus geen collision met de Barrels.
 * Momenteel is er nog geen collision tussen de ammo van de Human en de Barrels, maar het wordt overwogen om later toe te voegen.
 */
public class Barrel extends Entity {

	BufferedImage barrel1;
	
	public Barrel() {
		this.width = 140;
		this.height = 75;
		
		this.x = rng.nextInt(GamePanel.BOARD_START, GamePanel.BOARD_END - width);
		this.y = -height;
		this.vx = 0;
		this.vy = 2 * rng.nextDouble() + 3;
		
		this.hitbox = new Rectangle(x, y + 5, width, height - 10);
		
		// Retrieve barrel image <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			barrel1 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Barrel Collisions
	public void barrelCollisions(ArrayList<Barrel> barrelList, ArrayList<Ammunition> ammoList) {
		for (Ammunition bullet: ammoList) {
			for (Barrel barrel: barrelList) {
				if (bullet.collision(barrel)) {
					bullet.y = -1;
				}
			}
		}
	}
	public class DestructingBarrel extends TimerTask {
		@Override
		public void run() {
			y = GamePanel.BOARD_HEIGHT + 100;
		}
	}
	public void draw(Graphics g) {
		g.drawImage(barrel1, x, y, width, height, null);
		//g.drawRect(x, y + 5, width, height - 10);
	}
}