package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import MeltdownMayhem.GamePanel;
/**
 * De Human is de belangrijkste speler van dit spel.
 * Wordt gecontroleerd volgens de pijljtes en kan plasma schieten met de space_bar.
 * Wanneer hij geraakt wordt teleporteert hij terug naar zijn beginplaats, verliest het een leven en krijgt het spawn_protection.
 * Als zijn levens (3 in totaal) opzijn is het spel afgelopen.
 */
public class Human extends Entity {

	public static int max_lives = 3;
	public static int hitboxRadius = 40;
	public static boolean hasSpawnProtection;
	public boolean moveRight, moveLeft, moveUp, moveDown;
	
	BufferedImage human1;
	
	public Human() {
		this.width = 78;
		this.height = 129;
		
		this.x = GamePanel.PANEL_WIDTH/2 - this.width/2 - 128;
		this.y = GamePanel.BOARD_HEIGHT - this.height-GamePanel.BOARD_HEIGHT/15;
		this.vx = 4;
		this.vy = 4.3;
		
		this.lives = 3; // Starting amount of lives
		
		getHumanImage();
	}
	
	public class SpawnProtection extends TimerTask{
		@Override
		public void run() {
			hasSpawnProtection = false;
		}
	}
	
	public void getHumanImage() { // <Credits to RyiSnow>
		try {
			human1 = ImageIO.read(getClass().getResourceAsStream("/human/human1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void takeDamage() {
		if (hasSpawnProtection == false) {
			lives -= 1;
			x = GamePanel.PANEL_WIDTH/2 - GamePanel.human.width/2;
			y = GamePanel.BOARD_HEIGHT - GamePanel. human.height - GamePanel.BOARD_HEIGHT/15;
			hasSpawnProtection = true;
			respawnTimer.schedule(new SpawnProtection(), 2000);
		}
	}
	
	@Override
	public void update() {
		if (this.moveRight==true && x < GamePanel.BOARD_END - width - 2) { // 2-pixel draw accuracy
			x += vx + 1;
		}
		if (this.moveLeft==true && x > GamePanel.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < GamePanel.BOARD_HEIGHT-height) {
			y += vy;
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		image = human1;
		
		g.drawImage(image, x, y, width, height, null);
	}
}