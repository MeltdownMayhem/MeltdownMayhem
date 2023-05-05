package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Entity.RadiationOrb.Model;
import MeltdownMayhem.GamePanel;
import MeltdownMayhem.Window;
import MeltdownMayhem.GamePanel.droneKiller;
/**
 * The Drone is the second player of the Game and is controlled with the Mouse.
 * Skill: Destroy Barrels with Left-Click and move Ammo to the Human.
 * It has collision with all Entities, except Human, HumanAmmo and Barrels.
 */
public class Drone extends Entity {

	public Point2D MousePos;
	
	private final int DroneRespawnX = Window.screenSize.width/2 - width/2 + 128;
	private final int DroneRespawnY = Window.BOARD_HEIGHT - height - Window.BOARD_HEIGHT/15;
	
	private int MouseX = DroneRespawnX;
	private int MouseY = DroneRespawnY;
	
	public boolean droneFrozen = false;
	public boolean damageBarrel = false;
	public boolean spawnPowerUp = false;
	private boolean xOutOfBounds = false;
	private boolean yOutOfBounds = false;
	
	public PowerUp invSlot = null;
	public Barrel barrelSlot = null;
	
	private Timer respawnTimer, destructionTimer;
	
	List<BufferedImage> imageList;
	List<Integer> timeIntervalList;
	
	BufferedImage drone1, drone2;
	
	RadiationOrb orb;
	Rage rage;
	
	private GamePanel.droneKiller deathDrone;
	
	public Drone() {
		this.x = Window.screenSize.width/2 - this.width/2 + 128;
		this.y = Window.BOARD_HEIGHT - this.height - Window.BOARD_HEIGHT/15;
		this.width = 65;
		this.height = 65;
		
		this.lives = 1;
		this.hitboxRadius = 30;
		
		respawnTimer = new Timer();
		destructionTimer = new Timer();
		
		imageList = new ArrayList<BufferedImage>();
		timeIntervalList = new ArrayList<Integer>();
		
		getDroneImage();
	}
	
	public void getDroneImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			drone1 = ImageIO.read(getClass().getResourceAsStream("/drone/drone1.png"));
			drone2 = ImageIO.read(getClass().getResourceAsStream("/drone/drone2.png"));
			
			imageList = Arrays.asList(drone1,drone2);
			timeIntervalList = Arrays.asList(15,15);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void freeze(int duration) { //Freeze de drone
		droneFrozen = true;
		respawnTimer.schedule(new SpawnFreeze(), duration);
	}
	
	public void pickUp(GamePanel gp) {
		if (invSlot == null) {
			for (PowerUp powerUp: gp.powerUpList) {
				if (this.collision(powerUp)) {
					invSlot = powerUp;
					powerUp.pickedUp = true;
				}
			}
		} else {
			invSlot.pickedUp = false;
			invSlot = null;
		}
	}
	
	public void respawn() throws AWTException{ // Freeze de drone + breng hem terug naar respawnpunt
		Robot robot = new Robot();
		freeze(2000);
		lives += 1;
		robot.mouseMove(DroneRespawnX, DroneRespawnY);
		x = DroneRespawnX; // needed to move the model
		y = DroneRespawnY;
	}
	
	public void checkInBounds() { // Check if drone is in Board
		if (MouseX < Window.BOARD_START + width/2 || MouseX > Window.BOARD_END - width/2) {
			xOutOfBounds = true;
			// if statements to push drone back into boundaries in case mouse was moving too fast
			if (x < Window.BOARD_START + width/2) {
				x = Window.BOARD_START + width/2;
			}
			if (x > Window.BOARD_END - width/2) {
				x = Window.BOARD_END - width/2;
			}
		}
		else {
			xOutOfBounds = false;
		}
		if (MouseY < height/2 || MouseY > Window.BOARD_HEIGHT - height/2) {
			yOutOfBounds = true;
			// if statements push drone hitbox back into boundaries in case mouse was moving too fast
			if (y < height/2) {
				y = height/2;
			}
			if (y > Window.BOARD_HEIGHT - height/2) {
				y = Window.BOARD_HEIGHT - height/2;
			}
		}
		else {
			yOutOfBounds = false;
		}
	}
	
	public void mouseMove() { //beweegt de drone volgens de cursor, wanneer dat moet
		this.MousePos =  MouseInfo.getPointerInfo().getLocation();
		MouseX = (int) MousePos.getX();
		MouseY = (int) MousePos.getY();
		if (droneFrozen == false) {
			if (xOutOfBounds == false) {
				x = MouseX;
			}
			if (yOutOfBounds == false) {
				y = MouseY;
			}
		}
	}
	
	// Dit stuk vermijdt dat de drone teleporteert na pauzes/hits
	public void teleportMouse(int x, int y) throws AWTException {
		Robot robot = new Robot();
		robot.mouseMove(x, y);
	}
	
	public class SpawnFreeze extends TimerTask {
		int lastX = x;
		int lastY = y;
		@Override
		public void run() {
			try {
				teleportMouse(x, y);
			} catch (AWTException e) {
				e.printStackTrace();
			}
			droneFrozen = false;
			lives = 1;
		}
	}
	
	// Collision
	public droneKiller droneCollisions(String nameDrone, ArrayList<Enemy> enemyList, ArrayList<Ammunition> projectileList, int score) throws AWTException { // needed for Robot class
		this.deathDrone = null;
		for (Enemy enemy: enemyList) {
			if (this.collision(enemy)) {
				lives -= 1;
				
				// add death message for Drone
				if (enemy instanceof RadiationOrb) {
					orb = (RadiationOrb) enemy;
					if (orb.type == Model.ORB) {
						this.deathDrone = GamePanel.droneKiller.ORB;
					} else {
						this.deathDrone = GamePanel.droneKiller.SNIPER;
					}
				} else if (enemy instanceof Rage){
					rage = (Rage) enemy;
					if (rage.rampage) {
						this.deathDrone = GamePanel.droneKiller.RAMPAGE;
					} else {
						this.deathDrone = GamePanel.droneKiller.RAGE;
					}
				}
				break;
			}

		}
		if (lives > 0) {
			for (Ammunition bullet: projectileList) {
				if (this.collision(bullet)) {
					lives -= 1;
					bullet.y = -100;
					if (bullet.green) {
						this.deathDrone = GamePanel.droneKiller.ORBBULLET;
					} else {
						this.deathDrone = GamePanel.droneKiller.SNIPERBULLET;
					}
				}
			}
		}
		if (lives == 0) {
			respawn();
		}
		return this.deathDrone;
	}
	
	// destroy Barrel skill
	public void destroyBarrel(Barrel barrel, ArrayList<Barrel> barrelList) {
		if (this.collision(barrel) && invSlot == null && (barrelSlot == null || barrelSlot == barrel)) {
			if (barrel.gettingDamaged == false) {
				barrelSlot = barrel;
				barrel.gettingDamaged = true;
				barrel.vy = 0;
				destructionTimer.schedule(barrel.new DestroyBarrelTimerTask(), 800);
			}
		}
	}
	
	@Override
	public void update() {
		mouseMove();
		checkInBounds();
	}
	
	public void draw(Graphics g) {
		BufferedImage image = this.getImage(imageList,timeIntervalList);
		g.drawImage(image, x - width/2, y - height/2, width, height, null);
		
		// Show hitbox
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}