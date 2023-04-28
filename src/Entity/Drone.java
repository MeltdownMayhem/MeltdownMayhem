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

import Entity.Human.deathCauses;
import Entity.RadiationOrb.Model;
import MeltdownMayhem.GamePanel;
/**
 * The Drone is the second player of the Game and is controlled with the Mouse.
 * Skill: Destroy Barrels with Left-Click and move Ammo to the Human.
 * It has collision with all Entities, except Human, HumanAmmo and Barrels.
 */
public class Drone extends Entity {

	public Point2D MousePos;
	
	private final int DroneRespawnX = GamePanel.screenSize.width/2 - width/2 + 128;
	private final int DroneRespawnY = GamePanel.BOARD_HEIGHT - height - GamePanel.BOARD_HEIGHT/15;
	
	private int MouseX = DroneRespawnX;
	private int MouseY = DroneRespawnY;
	
	public boolean droneFrozen = false;
	public boolean damageBarrel = false;
	public boolean spawnPowerUp = false;
	private boolean xOutOfBounds = false;
	private boolean yOutOfBounds = false;
	
	public PowerUp invSlot = null;
	public Barrel barrelSlot = null;
	
	private Timer respawnTimer = new Timer();
	private Timer destructionTimer = new Timer();
	
	List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	BufferedImage drone1;
	BufferedImage drone2;
	
	RadiationOrb orb;
	Rage rage;
	
	public Drone() {
		this.width = 65;
		this.height = 65;
		
		this.x = GamePanel.screenSize.width/2 - this.width/2 + 128;
		this.y = GamePanel.BOARD_HEIGHT - this.height - GamePanel.BOARD_HEIGHT/15;
		
		this.lives = 1;
		
		this.hitboxRadius = 30;
		
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
		if (MouseX < GamePanel.BOARD_START + width/2 || MouseX > GamePanel.BOARD_END - width/2) {
			xOutOfBounds = true;
			// if statements to push drone back into boundaries in case mouse was moving too fast
			if (x < GamePanel.BOARD_START + width/2) {
				x = GamePanel.BOARD_START + width/2;
			}
			if (x > GamePanel.BOARD_END - width/2) {
				x = GamePanel.BOARD_END - width/2;
			}
		}
		else {
			xOutOfBounds = false;
		}
		if (MouseY < height/2 || MouseY > GamePanel.BOARD_HEIGHT - height/2) {
			yOutOfBounds = true;
			// if statements push drone hitbox back into boundaries in case mouse was moving too fast
			if (y < height/2) {
				y = height/2;
			}
			if (y > GamePanel.BOARD_HEIGHT - height/2) {
				y = GamePanel.BOARD_HEIGHT - height/2;
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
	public void droneCollisions(ArrayList<Enemy> enemyList, ArrayList<Ammunition> projectileList, GamePanel gp) throws AWTException { // needed for Robot class
		for (Enemy enemy: enemyList) {
			if (this.collision(enemy)) {
				lives -= 1;
				if (gp.score > 20) {
					gp.score -= 20;
				} else {
					gp.score = 0;
				}
				
				// add death message for Drone
				if (enemy instanceof RadiationOrb) {
					orb = (RadiationOrb) enemy;
					if (orb.type == Model.ORB) {
						gp.chatText.add(gp.nameDrone + " should learn about the dangers of radiation");
					} else {
						gp.chatText.add(gp.nameDrone + " should learn about the dangers of radiation");
					}
				} else if (enemy instanceof Rage){
					rage = (Rage) enemy;
					if (rage.rampage) {
						gp.chatText.add(gp.nameDrone + " wasn't fast enough");
					} else {
						gp.chatText.add(gp.nameDrone + " should learn about the dangers of radiation");
					}
				}
				gp.chatTimer.add(0);
			}

		}
		
		if (lives > 0) {
			for (Ammunition bullet: projectileList) {
				if (this.collision(bullet)) {
					lives -= 1;
					bullet.y = -1;
					if (gp.score > 10) {
						gp.score -= 10;
					} else {
						gp.score = 0;
					}
					if (bullet.green) {
						gp.chatText.add(gp.nameDrone + " was hit by a radiation orb");
					} else {
						gp.chatText.add(gp.nameDrone + " was hit by a radiation sniper");
					}
					gp.chatTimer.add(0);
				}
			}
		}
		if (lives == 0) {
			respawn();
		}	
	}
	
	// destroy Barrel skill
	public void destroyBarrel(Barrel barrel, ArrayList<Barrel> barrelList, GamePanel gp) {
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
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}