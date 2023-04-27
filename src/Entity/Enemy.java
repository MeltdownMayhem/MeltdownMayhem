package Entity;

import java.awt.Graphics;
import java.util.ArrayList;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;
/**
 * De Enemy class is een Subclass van de Entity class en een Superclass voor de RadiationOrb en Rage class.
 */
public abstract class Enemy extends Entity {

	protected static final int ENEMYBOARD_UPPERBORDER = 125;
	protected static final double ENEMYBOARD_BOTTOMBORDER = 0.8 * GamePanel.BOARD_HEIGHT;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	
	protected static final int SPEED_RESET_FACTOR = 200;
	protected static final int OSCILLATION_FACTOR = 2;
	
	public int enemySize, enemyRadius;
	public static int margin = 50;
	
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	
	boolean appearing = true;
	public boolean spawning = true;
	public boolean rampage = false;
	
	public int killScore;
	
	Enemy(int x){
		enemySize = 85;
		enemyRadius = enemySize/2; // 43 pixels
		
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		this.hitboxRadius = 30;
	}
	
	public abstract void update(Human human);
	
	public abstract void draw(Graphics g);
	
	public void spawnPriority() {
		/* Appearing: when above the screen (higher than the visible Board)
		 * Spawning: when above the ENEMY_UPPERBORDER (once spawning is done, it can't go back up again) */
		
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
		} else if (this.spawning && this.y > ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.spawning = false;
		}
	}
	
	public void stayInField() {
		/* This function checks if the Enemies are in the playable area.
		 * If this isn't the case, then their position and speed gets modified to get back on the Board. */
		
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
		
		// Bottom horizontal Board border collision
		if (this.rampage == true) {
			if (this.y > GamePanel.BOARD_HEIGHT - margin) {
				this.vy *= -1;
				this.timeSinceReset_y = 0;
				this.y = (int) (2 * (GamePanel.BOARD_HEIGHT - margin) - this.y);
			}
		} else if ((this.y > ENEMYBOARD_BOTTOMBORDER - margin) && (vy >= 0)){
			this.vy *= -1;
			this.timeSinceReset_y = 0;
		}
		// Top horizontal Board border collision
		if (!this.appearing && this.spawning && this.y < enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * enemyRadius - this.y;
		} else if(!this.spawning && this.y < ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (ENEMYBOARD_UPPERBORDER + enemyRadius) - this.y;
		}
	}
	
	// Random speed modifications
	public void randomSpeed() {
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
	
	// Enemy Spawning
	public static void spawnEnemy(ArrayList<Enemy> enemyList, GamePanel gp) {
		boolean enoughSpaceToSpawn = true;
		int spawning_x = 0;
			
		do {
			enoughSpaceToSpawn = true;
			spawning_x = 2 * margin + rng.nextInt(GamePanel.BOARD_WIDTH - 4 * margin) + GamePanel.BOARD_START;
			for (Enemy E: enemyList) {
				if (Extra.distance(spawning_x, -20, E.x, E.y) < 4 * E.enemyRadius) {
					enoughSpaceToSpawn = false;
					break;
				}
			}
		} while (!enoughSpaceToSpawn);
		if (gp.score > 100 && rng.nextDouble() > 0.85) {
			enemyList.add(new Rage(spawning_x));
		} else {
			if (rng.nextDouble() < 0.5) {
				enemyList.add(new RadiationOrb(spawning_x, true));
			} else {
				enemyList.add(new RadiationOrb(spawning_x, false));
			}
		}
	}
	
	// Enemy Collisions
	public boolean enemyCollisions(ArrayList<Enemy> enemyList, ArrayList<Ammunition> ammoList, GamePanel gp) {
		for(Ammunition bullet: ammoList) {
			if (this.collision(bullet)) {
				gp.score += this.killScore;
				bullet.y = -1; // at y = -1 the bullet is automatically removed
				return true;
			}
		}
		return false;
	}
}