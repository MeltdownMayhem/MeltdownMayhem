package Entity;

import java.awt.Graphics;

import MeltdownMayhem.GamePanel;

public abstract class Enemy extends Entity{
	public static final int ENEMY_BOARD_UPPER_BORDER = 75;
	public static final int ENEMY_BOARD_BOTTOM_BORDER = 700;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	protected static final double SPAWN_CHANCE = 0.995;
	
	protected static int enemySize = 80;
	public static int enemyRadius = enemySize/2; // 40 pixels
	public static int margin = 10 + enemyRadius; // 50 pixels
	protected static final int SPEED_RESET_FACTOR = 200;
	
	// actual_x and actual_y gives the coordinates of the center of the figure and not its upper-left corner
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	public int shootCooldown = 0;
	
	boolean appearing = true;
	public boolean spawning = true;
	
	
	public Enemy() {

	}
	
	public void update() {
		//shootCooldown ++;
	}
	
	public void draw(Graphics g) {
		
	}
	
	public void stayInField() {
		
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
		if (this.y > ENEMY_BOARD_BOTTOM_BORDER - margin) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (ENEMY_BOARD_BOTTOM_BORDER - margin) - this.y;
		} else if (!this.appearing && this.spawning && this.y < enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * enemyRadius - this.y;
		} else if(!this.spawning && this.y < ENEMY_BOARD_UPPER_BORDER + enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (ENEMY_BOARD_UPPER_BORDER + enemyRadius) - this.y;
		}
	}
	
	public void spawnPriority() {
		/*
		Een enemy is uit zijn appearingsfase als hij onder de bovenkant van het scherm geraakt.
		
		Een enemy is uit zijn spawnfase als hij onder de ENEMY_BOARD_UPPER_BORDER geraakt. Vanaf dan kan hij niet
		meer boven de UPPERBORDER geraken om plaats te maken voor het spawnen van andere enemies.
		
		Of een enemy zich al dan niet in zijn appearingsfase of spawnfase bevindt, heeft een invloed op zijn gedrag
		bij het veranderen van zijn snelheid (en bij botsingen).
		*/
		
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
		} else if (this.spawning && this.y > ENEMY_BOARD_UPPER_BORDER + enemyRadius) {
			this.spawning = false;
		}
	}
	
	public void randomSpeedChange() {
		// Volgende code laat de snelheid van de enemies om de zoveel tijd (met enige randomisatie) veranderen
		
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