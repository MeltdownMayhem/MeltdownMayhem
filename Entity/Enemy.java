package Entity;

import java.awt.Graphics;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;

public abstract class Enemy extends Entity{
	protected static final int UPPER_BORDER = 75;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	protected static final double SPAWN_CHANCE = 0.995;
	
	protected static int enemySize = 80;
	public static int enemyRadius = enemySize/2; // 40 pixels
	public static int margin = 10 + enemyRadius; // 50 pixels
	
	// actual_x and actual_y gives the coordinates of the center of the figure and not its upper-left corner
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	
	boolean appearing = true;
	public boolean spawning = true;
	public static boolean enemyKilled = false;

	
	
	public Enemy() {
	}
	public void checkBulletCollision(Ammunition bullet, int hitEnemyIndex) {
		if (Extra.distance(bullet.x, bullet.y, x, y) < Ammunition.hitboxradius + enemyRadius) {
			killEnemy(hitEnemyIndex);
			enemyKilled = true;
		}

	}
	public void killEnemy(int hitEnemyIndex) {
		GamePanel.enemyList.remove(hitEnemyIndex);
	}
	
	public void update() {
	}
	
	public void draw(Graphics g) {
		
	}
	
}