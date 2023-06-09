package Entity;

import java.awt.Graphics;
import java.util.ArrayList;

import Entity.RadiationOrb.Model;
import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;
import MeltdownMayhem.Window;
/**
 * Enemies are the main obstacle for the players. 
 * Enemies give score when killed.
 * There are 3 types of enemies, 2 of which are defined in the RadiationOrb class, the other in the Rage class.
 */
public abstract class Enemy extends Entity {

	protected static final int ENEMYBOARD_UPPERBORDER = 125;
	//enemies can't pass below the BOTTOMBORDER
	protected static final double ENEMYBOARD_BOTTOMBORDER = 0.8 * Window.BOARD_HEIGHT;
	protected static final double ORB_BOTTOMBORDER = Window.BOARD_HEIGHT/3;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	
	protected static final int SPEED_RESET_FACTOR = 200;
	public double x_speedFactor;
	
	public int enemySize, enemyRadius;
	public static int margin = 50;
	
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	
	public boolean appearing, spawning, rampage;
	
	private static double spawnChanceOrbLevel1 = 0.8;
	private static double spawnChanceRageLevel2 = 0.15;
	private static double spawnChanceOrbLevel2 = 0.7;
	private static double spawnChanceRageLevel3 = 0.3;
	private static double spawnChanceOrbLevel3 = 0.6;
	
	public int killScore;
	
	private RadiationOrb orb;
	
	Enemy(int x){
		enemySize = 85;
		enemyRadius = enemySize/2; // 43 pixels
		
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		this.hitboxRadius = 30;
		this.x_speedFactor = 1;
		
		this.appearing = true;
		this.spawning = true;
		this.rampage = false;
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
		
		if (this instanceof RadiationOrb) {
			orb = (RadiationOrb) this;
		}
		
		// Vertical Board borders collision
		if (this.x > Window.BOARD_END - margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (Window.BOARD_END - margin) - this.x;
		} else if(this.x < Window.BOARD_START + margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (Window.BOARD_START + margin) - this.x;
		}
		
		// Bottom horizontal Board border collision
		if (this.rampage == true) {
			if (this.y > Window.BOARD_HEIGHT - margin) {
				this.vy *= -1;
				this.timeSinceReset_y = 0;
				this.y = (int) (2 * (Window.BOARD_HEIGHT - margin) - this.y);
			}
		} else if (this instanceof RadiationOrb && orb.type == Model.ORB){
			if ((this.y > ORB_BOTTOMBORDER) && (vy >= 0)){
				this.vy *= -1;
				this.timeSinceReset_y = 0;
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
			this.vx = rng.nextInt(3) * Math.pow(-1, rng.nextInt(2)) * x_speedFactor;
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
	public static ArrayList<Enemy> spawnEnemy(ArrayList<Enemy> enemyList, int level) {
		boolean enoughSpaceToSpawn = true;
		int spawning_x = 0;
			
		do {
			enoughSpaceToSpawn = true;
			spawning_x = 2 * margin + rng.nextInt(Window.BOARD_WIDTH - 4 * margin) + Window.BOARD_START;
			for (Enemy E: enemyList) {
				if (Extra.distance(spawning_x, -20, E.x, E.y) < 4 * E.enemyRadius) {
					enoughSpaceToSpawn = false;
					break;
				}
			}
		} while (!enoughSpaceToSpawn);
		
		if (level == 1) {
			if (rng.nextDouble() < spawnChanceOrbLevel1) {
				enemyList.add(new RadiationOrb(spawning_x, false));
			} else {
				enemyList.add(new RadiationOrb(spawning_x, true));
			}
		} else if (level == 2) {
			if (rng.nextDouble() < spawnChanceRageLevel2) {
				enemyList.add(new Rage(spawning_x));
			} else if (rng.nextDouble() < spawnChanceOrbLevel2) {
				enemyList.add(new RadiationOrb(spawning_x, false));
			} else {
				enemyList.add(new RadiationOrb(spawning_x, true));
			}
		} else if (level == 3) {
			if (rng.nextDouble() < spawnChanceRageLevel3) {
				enemyList.add(new Rage(spawning_x));
			} else if (rng.nextDouble() < spawnChanceOrbLevel3) {
				enemyList.add(new RadiationOrb(spawning_x, false));
			} else {
				enemyList.add(new RadiationOrb(spawning_x, true));
			}
		}
		return enemyList;
	}
		
	
	// Enemy Collisions
	public boolean enemyCollisions(ArrayList<Enemy> enemyList, ArrayList<Ammunition> ammoList, GamePanel gp) {
		for(Ammunition bullet: ammoList) {
			if (this.collision(bullet)) {
				gp.score += this.killScore;
				bullet.y = -1000; // at y = -1 the bullet is automatically removed
				return true;
			}
		}
		return false;
	}
}