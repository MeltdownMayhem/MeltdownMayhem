package MeltdownMayhem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entity.Human;

public class GUI {
	
	public BufferedImage heart1;
	public BufferedImage heart2;
	public BufferedImage bulletBar;
	public BufferedImage ammoBar;
	
	public GUI() {
		getGUIImage();
	}
	
	public void getGUIImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			heart1 = ImageIO.read(getClass().getResourceAsStream("/GUI/heart1.png"));
			heart2 = ImageIO.read(getClass().getResourceAsStream("/GUI/heart2.png"));
			bulletBar = ImageIO.read(getClass().getResourceAsStream("/GUI/bulletBar.png"));
			ammoBar = ImageIO.read(getClass().getResourceAsStream("/GUI/ammoBar.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		// Health-bar
		for (int i = 0; i< GamePanel.human.lives; i++) {
			g.drawImage(heart1, GamePanel.BOARD_START + 20 + i*70, GamePanel.BOARD_HEIGHT - 75, 50, 50, null);
		}
		for (int i = GamePanel.human.lives; i < Human.max_lives; i++) {
			g.drawImage(heart2, GamePanel.BOARD_START + 20 + i*70, GamePanel.BOARD_HEIGHT - 75, 50, 50, null);
		}
		// Ammo-bar
		g.drawImage(ammoBar, GamePanel.BOARD_START, GamePanel.BOARD_HEIGHT - 10, 5*GamePanel.max_ammo, 14, null);
		for (int i = 0; i< GamePanel.ammo; i++) {
			g.drawImage(bulletBar, GamePanel.BOARD_START + i*5, GamePanel.BOARD_HEIGHT - 10, 6, 14, null);
		}
	}
}
