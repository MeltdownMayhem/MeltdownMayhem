package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class Human extends Character {
	
	public static int max_lives = 3; // max amount of lives, can be changed
	
	public Human() {
		this.width = 78;
		this.depth = 129;
		
		this.x = GamePanel.PANEL_WIDTH/2-this.width/2;
		this.y = GamePanel.BOARD_HEIGHT-this.depth-GamePanel.BOARD_HEIGHT/15;
		this.vx = 3.5;
		this.vy = 4;
		
		this.lives = 3; // Starting amount of lives
		
		getHumanImage();
	}

	public void getHumanImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			human1 = ImageIO.read(getClass().getResourceAsStream("/Human/human1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		image = human1;
		
		g.drawImage(image, x, y, width, depth, null);
		g.setColor(new Color(255,0,0));
		g.drawOval(x - 5, (int)(y + width * 0.6), (int)(width * 1.1), (int)(width * 1.1));
		g.setColor(new Color(0,0,255));
		g.drawOval((int)(x - 10 + width * 0.6), (int)(y + width * 1.15), 3, 3);
	}
	
	public void hit() {
		this.lives -= 1;
		this.x = GamePanel.PANEL_WIDTH/2 - this.width/2;
		this.y = GamePanel.BOARD_HEIGHT - this.depth - GamePanel.BOARD_HEIGHT/15;
		if (this.lives == 0) {
			GamePanel.gameOver = true;
			System.out.println("GAME OVER!");
		}
	}
}