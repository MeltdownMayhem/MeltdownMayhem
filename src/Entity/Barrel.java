package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
/**
 * De Barrel class maakt de Barrels aan die naar beneden 'rollen' op het scherm.
 * Zijn doel is een obstakel te zijn in de weg van de Human.
 * De Drone vliegt en heeft dus geen collision met de Barrels.
 * Momenteel is er nog geen collision tussen de ammo van de Human en de Barrels, maar het wordt overwogen om later toe te voegen.
 */
public class Barrel extends Entity {

	public int width = 130, height = 70;
	BufferedImage barrel1;
	Random rng = new Random();
	
	// General Constructor
	public Barrel() {
		this.x = rng.nextInt(GamePanel.BOARD_START, GamePanel.BOARD_END - width);
		this.y = -height;
		this.vx = 0;
		this.vy = 2 * rng.nextDouble() + 3;
		
		// Retrieve barrel image <Credits to RyiSnow>
		try {
			barrel1 = ImageIO.read(getClass().getResourceAsStream("/barrel/barrel1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkBarrelCollision() {
		if (GamePanel.human.x + GamePanel.human.width - 5 > x && GamePanel.human.x < x + width - 5 && GamePanel.human.y < y + height*3/4 && GamePanel.human.y + GamePanel.human.height*3/4 > y) {
			GamePanel.human.takeDamage();		// 3/4 factor because of barrel hitbox
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(barrel1, x, y, width, height, null);
	}
}