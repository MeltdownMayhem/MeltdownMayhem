package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import MeltdownMayhem.GamePanel;

public class Human extends Character {
	
	public static int max_lives = 3; // max amount of lives, can be changed
	public static int hitboxRadius = 40;
	public static boolean humanSpawnProtection;
	
	public Human() {
		this.width = 78;
		this.depth = 129;
		
		this.x = GamePanel.PANEL_WIDTH/2-this.width/2 - 128;
		this.y = GamePanel.BOARD_HEIGHT-this.depth-GamePanel.BOARD_HEIGHT/15;
		this.vx = 3.5;
		this.vy = 4;
		
		this.lives = 3; // Starting amount of lives
		
		getHumanImage();
		
	}
	public class SpawnProtection extends TimerTask{
		@Override
		public void run() {
			humanSpawnProtection = false;
		}
	}
	public void getHumanImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		try {
			human1 = ImageIO.read(getClass().getResourceAsStream("/Human/human1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void loseLife() {
		if (humanSpawnProtection == false) {
		lives -= 1;
		x = GamePanel.PANEL_WIDTH/2 - GamePanel.human.width/2;
		y = GamePanel.BOARD_HEIGHT -GamePanel. human.depth - GamePanel.BOARD_HEIGHT/15;
		humanSpawnProtection = true;
		respawnTimer.schedule(new SpawnProtection(), 2000);
		}
	}
	public void update() {
		if (this.moveRight==true && x < GamePanel.BOARD_END-width-2) { // 2-pixel draw accuracy error
			x += vx + 1;
		}
		if (this.moveLeft==true && x > GamePanel.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < GamePanel.BOARD_HEIGHT-depth) {
			y += vy;
		}
	}
	public void draw(Graphics g) {
		BufferedImage image;
		image = human1;
		
		g.drawImage(image, x, y, width, depth, null);
	}
}