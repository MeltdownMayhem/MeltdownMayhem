package Entity;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;

public abstract class Enemy extends Entity{
	
	protected static final int ENEMYBOARD_UPPERBORDER = 125;
	protected static final double ENEMYBOARD_BOTTOMBORDER = 0.8 * GamePanel.BOARD_HEIGHT;
	public static final double COLLISION_AREA_FACTOR = 2.5;
	public static final double SPAWN_CHANCE = 0.995;
	
	protected static final int SPEED_RESET_FACTOR = 200;
	protected static int OSCILLATION_FACTOR = 2;
	
	protected static int enemySize = 80;
	public static int enemyRadius = enemySize/2; // 40 pixels
	public static int margin = 10 + enemyRadius; // 50 pixels
	
	int timeSinceReset_x = 0;
	int timeSinceReset_y = 0;
	public int shootCooldown = 0;
	
	protected static double shootingAngle, shootingDistance;
	protected static int distanceOffTarget, shootingTarget_x, shootingTarget_y;
	
	boolean appearing = true;
	public boolean spawning = true;
	public static boolean enemyKilled = false;
	public static Timer respawnTimer = new Timer();
	
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
	public void HumanCollision() {
			if (Extra.distance(GamePanel.human.x + GamePanel.human.width/2, GamePanel.human.y + GamePanel.human.depth/2, x + enemyRadius/2, y + enemyRadius/2) < Human.hitboxRadius + enemyRadius) {
				if (Human.humanSpawnProtection == false) {
					AttackHuman();
				}
			}
	}
	public void AttackHuman() {
		GamePanel.human.loseLife();
	}
	public void spawnPriority() {
		/*
		Een enemy is uit zijn appearingsfase als hij onder de bovenkant van het scherm geraakt.
		
		Een enemy is uit zijn spawnfase als hij onder de ENEMYBOARD_UPPERBORDER geraakt. Vanaf dan kan hij niet
		meer boven de UPPERBORDER geraken om plaats te maken voor het spawnen van andere enemies.
		
		Of een enemy zich al dan niet in zijn appearingsfase of spawnfase bevindt, heeft een invloed op zijn gedrag
		bij het veranderen van zijn snelheid (en bij botsingen).
		*/
		
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
			System.out.println("Status changed to spawning");
		} else if (this.spawning && this.y > ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.spawning = false;
			System.out.println("Status changed to fully spawned");
		}
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
		if (this.y > ENEMYBOARD_BOTTOMBORDER - margin) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = (int) (2 * (ENEMYBOARD_BOTTOMBORDER - margin) - this.y);
		} else if (!this.appearing && this.spawning && this.y < enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * enemyRadius - this.y;
		} else if(!this.spawning && this.y < ENEMYBOARD_UPPERBORDER + enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (ENEMYBOARD_UPPERBORDER + enemyRadius) - this.y;
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
	
	public void aimAndShoot(Enemy e, int human_x, int human_y) {
		if (!rampage) {
			shootingAngle = rng.nextDouble() * 2 * Math.PI;
			distanceOffTarget = rng.nextInt(300);
			shootingTarget_x = (int) (human_x + distanceOffTarget * Math.cos(shootingAngle));
			shootingTarget_y = (int) (human_y + distanceOffTarget * Math.sin(shootingAngle));
			shootingDistance = Extra.distance(e.x, e.y, (int)(shootingTarget_x), (int)(shootingTarget_y));
			vx = (shootingTarget_x - e.x)/shootingDistance * 5;
			vy = (shootingTarget_y - e.y)/shootingDistance * 5;
			GamePanel.projectileList.add(new Ammunition(e.x, e.y, vx, vy));
			e.shootCooldown = 0;
		}
	}
}