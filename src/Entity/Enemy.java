package Entity;

import java.awt.Graphics;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;

public abstract class Enemy extends Entity{
	
	protected static final int UPPER_BORDER = 75;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	public static final double SPAWN_CHANCE = 0.995;
	
	protected static final int SPEED_RESET_FACTOR = 200;
	protected static int OSCILLATION_FACTOR = 2;
	
	protected static int enemySize = 80;
	public static int enemyRadius = enemySize/2; // 40 pixels
	public static int margin = 10 + enemyRadius; // 50 pixels
	
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	
	boolean appearing = true;
	public boolean spawning = true;
	public static boolean enemyKilled = false;
	
	public boolean rampage = false;
	
	public Enemy() {
		
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		
	}
	
	public void checkBulletCollision(Ammunition bullet, int hitEnemyIndex) {
		if (Extra.distance(bullet.x, bullet.y, x, y) < Ammunition.hitboxRadius + enemyRadius) {
			killEnemy(hitEnemyIndex);
			enemyKilled = true;
		}
	}
	
	public void killEnemy(int hitEnemyIndex) {
		GamePanel.enemyList.remove(hitEnemyIndex);
	}
	
	public void stayInField() {
		/*s
		De functie kijkt na of de coördinaten van een enemy nog wel binnenin het speelveld blijven.
		Indien de enemy het speelveld aan het verlaten is, wordt hij terug in het veld geplaatst. 
		Hierbij wordt hij terug het veld in gestuurd, verwijderd van de rand van het speelveld
		(langs de binnenkant van het veld) met een even grote afstand dan hij uit het veld gegaan is.
		Ook wordt het teken van de snelheid in de betreffende richting omgewisseld.
		*/
		
		// Vertical Board borders collision
		if (this.x > GamePanel.BOARD_END - margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (GamePanel.BOARD_END - margin) - this.x;
		} else if(this.x < GamePanel.BOARD_START + margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (GamePanel.BOARD_START + margin) - this.x;
		}
		
		// Horizontal Board borders collision
		if (this.y > GamePanel.BOARD_HEIGHT - margin) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (GamePanel.BOARD_HEIGHT - margin) - this.y;
		} else if (!this.appearing && this.spawning && this.y < enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * enemyRadius - this.y;
		} else if(!this.spawning && this.y < UPPER_BORDER + enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (UPPER_BORDER + enemyRadius) - this.y;
		}
	}
	
	public void randomSpeed() {
		// Random speed modifications
		if (timeSinceReset_x / SPEED_RESET_FACTOR > rng.nextDouble()) {
			this.vx = rng.nextInt(3) * Math.pow(-1, rng.nextInt(2));
			this.timeSinceReset_x = 0;
		} else {
			this.timeSinceReset_x++;
		}		
		if (!this.spawning && (timeSinceReset_y / SPEED_RESET_FACTOR > rng.nextDouble())) {
			this.vy = rng.nextInt(3) * Math.pow(-1, rng.nextInt(2));
			this.timeSinceReset_y = 0;
		} else {
			this.timeSinceReset_y++;
		}
	}
}