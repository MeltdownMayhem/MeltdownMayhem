package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;
/**
 * De Drone is de tweede speler van dit spel, met een belangrijke rol.
 * In de toekomst zal het Barrels kunnen verplaatsen en Ammo kunnen opnemen die Enemies droppen.
 * De Drone vliegt boven de Barrels en heeft dus geen collision ermee.
 * Het wordt gecontroleerd via de muis.
 */
public class Drone extends Entity {

	public Point2D MousePos;
	public boolean droneFrozen = false;
	public boolean droneDestructsBarrel = false;
	public boolean spawnNewAmmoDrop = false;
	private boolean xOutOfBounds = false;
	private boolean yOutOfBounds = false;
	private Timer respawnTimer = new Timer();
	private Timer destructionTimer = new Timer();
	private final int DroneRespawnX = GamePanel.screenSize.width/2 - width/2 + 128;
	private final int DroneRespawnY = GamePanel.BOARD_HEIGHT - height - GamePanel.BOARD_HEIGHT/15;
	private int MouseX = DroneRespawnX;
	private int MouseY = DroneRespawnY;
	
	List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	BufferedImage drone1;
	BufferedImage drone2;
	
	public Drone() {
		this.width = 64;
		this.height = 64;
		
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
	public void respawn() throws AWTException{ // Freeze de drone + breng hem terug naar respawnpunt
		Robot robot = new Robot();
		freeze(2000);
		lives += 1;
		robot.mouseMove(DroneRespawnX, DroneRespawnY);
		x = DroneRespawnX; //needed to move the model
		y = DroneRespawnY;
	}
	public void checkInBounds() { //Checkt of drone zich binnen het spelpaneel bevindt
		if (MouseX < GamePanel.BOARD_START + width/2 || MouseX > GamePanel.BOARD_END - width/2) {
			xOutOfBounds = true;
			// if statements push drone hitbox back into boundaries in case mouse was moving too fast
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
				if (gp.score > 10) {
					gp.score -= 10;
				} else {
					gp.score = 0;
				}
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
				}
			}
		}
		if (lives == 0) {
			respawn();
		}	
	}
	
	// Barrel destruction
	public Barrel destructedBarrel;
	public void destructBarrel(Barrel barrel, ArrayList<Barrel> barrelList) {
		if (this.collision(barrel)) {
			freeze(3000);
			destructionTimer.schedule(barrel.new DestructingBarrel(), 3000); 
			barrel.vy = 0;
			droneDestructsBarrel = false;
			spawnNewAmmoDrop = true;
			destructedBarrel = barrel;
		
		}
	}
	
	@Override
	public void update() {
		mouseMove();
		checkInBounds();
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		image = this.getImage(imageList,timeIntervalList);
		g.drawImage(image, x - width/2, y - height/2, width, height, null);
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}