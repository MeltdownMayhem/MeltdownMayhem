package Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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

	public boolean moveRight, moveLeft, moveUp, moveDown;
	public int max_lives = 3;
	boolean hasSpawnProtection;
	
	public int ammo = 100;
	public int max_ammo = 100;
	public int shootingCooldown = 0; // Starts at 0, for instant shooting when tapping key
	public boolean isShooting = false;
	
	BufferedImage human1;
	
	public Human() {
		this.width = 82;
		this.height = 135;
		
		this.x = GamePanel.screenSize.width/2 - this.width/2 - 128;
		this.y = GamePanel.BOARD_HEIGHT - this.height - GamePanel.BOARD_HEIGHT/15;
		this.vx = 4;
		this.vy = 4.3;
		
		this.lives = 3; // Starting amount of lives
		
		this.hitbox = new Rectangle(x + 10, y + 15, width - 15, height - 20);
		
		getHumanImage();
	}
	
	public void getHumanImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			human1 = ImageIO.read(getClass().getResourceAsStream("/human/human1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public class SpawnProtection extends TimerTask {
		@Override
		public void run() {
			hasSpawnProtection = false;
		}
	}
	
	public void takeDamage() {
		if (hasSpawnProtection == false) {
			lives --;
			x = GamePanel.screenSize.width/2 - width/2;
			y = GamePanel.BOARD_HEIGHT - height - GamePanel.BOARD_HEIGHT/15;
			hasSpawnProtection = true;
			respawnTimer.schedule(new SpawnProtection(), 2000);
		}
	}
	
	public void shootBullet(ArrayList<Ammunition> ammoList) {
		if (isShooting == true && ammo > 0) {
			shootingCooldown--;
			if (shootingCooldown < 0) {
				ammoList.add(new Ammunition(x + width*76/100 - 2, y));
				shootingCooldown = 15;
				ammo --;
			}
		}
	}
	
	// Human Collisions
	public void humanCollisions(ArrayList<Enemy> enemyList, ArrayList<Barrel> barrelList, ArrayList<Ammunition> projectileList, GamePanel gp) {
		for (Enemy enemy: enemyList) {
			if (this.collision(enemy)) {
				this.takeDamage();
				break;
			}
		}
		for (Barrel barrel: barrelList) {
			if (this.collision(barrel)) {
				this.takeDamage();
				break;
			}
		}
		for (Ammunition bullet: projectileList) {
			if (this.collision(bullet)) {
				this.takeDamage();
				gp.delProjectileList.add(bullet);
				break;
			}
		}
	}
	
	@Override
	public void update() {
		if (this.moveRight==true && x < GamePanel.BOARD_END - width - 2) { // 2-pixel draw accuracy
			x += vx;
		}
		if (this.moveLeft==true && x > GamePanel.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < GamePanel.BOARD_HEIGHT - height) {
			y += vy + 1;
		}
		hitbox.x = x + 10;
		hitbox.y = y + 15;
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		image = human1;
		
		g.drawImage(image, x, y, width, height, null);
		//g.drawRect(x + 10, y + 15, width - 15, height - 20);
	}
}