package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;
/**
 * De Drone is de tweede speler van dit spel, met een belangrijke rol.
 * Tot nu toe kan de Drone nog niet veel doen.
 * In de toekomst zal het Barrels kunnen verplaatsen en Ammo kunnen opnemen die Enemies droppen.
 * De Drone vliegt boven de Barrels en heeft dus geen collision ermee.
 * Het wordt gecontroleerd via de muis.
 */
public class Drone extends Entity {

	public Point2D MousePos;
	public boolean droneFrozen = false;
	Extra rotor = new Extra();
	Timer respawnTimer = new Timer();
	private final int DroneRespawnX = GamePanel.PANEL_WIDTH/2- width/2 + 128;
	private final int DroneRespawnY = GamePanel.BOARD_HEIGHT- height-GamePanel.BOARD_HEIGHT/15;
	
	ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	ArrayList<Integer> timeIntervalList = new ArrayList<Integer>();
	
	BufferedImage drone1;
	BufferedImage drone2;
	
	public Drone() {
		this.width = 64;
		this.height = 64;
		this.x = GamePanel.PANEL_WIDTH/2-this.width/2 + 128;
		this.y = GamePanel.BOARD_HEIGHT-this.height-GamePanel.BOARD_HEIGHT/15;
		this.lives = 1;
		this.vx = 20;
		this.vy = 17.5;
		
		getDroneImage();
	}
	
	public void getDroneImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		try {
			drone1 = ImageIO.read(getClass().getResourceAsStream("/drone/drone1.png"));
			drone2 = ImageIO.read(getClass().getResourceAsStream("/drone/drone2.png"));
			
			imageList.add(drone1);
			imageList.add(drone2);
			
			timeIntervalList.add(15);
			timeIntervalList.add(15);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		image = this.getImage(imageList,timeIntervalList);
		
		g.drawImage(image, x - width/2, y - height/2, width, height, null);
	}
	
	public void mouseMove() throws AWTException{
		Robot robot = new Robot();
		if (droneFrozen ==  false) {
			this.MousePos =  MouseInfo.getPointerInfo().getLocation();
			x = (int) MousePos.getX();
			y = (int) MousePos.getY();
		}
		else {
			robot.mouseMove(DroneRespawnX, DroneRespawnY);
			x = DroneRespawnX;
			y = DroneRespawnY; //needed to move the model
		}
	}
	
	public class SpawnFreeze extends TimerTask{
		@Override
		public void run() {
			droneFrozen = false;
			lives = 1;
		}
	}
	
	public void checkDroneCollision() throws AWTException{ //needed for Robot class
		for (Enemy enemy: GamePanel.enemyList) {
			if (x > enemy.x - enemy.enemyRadius && x < enemy.x + enemy.enemyRadius && y > enemy.y - enemy.enemyRadius && y < enemy.y + enemy.enemyRadius) {
				lives -= 1;
			if (lives == 0) {
				Robot robot = new Robot();
				robot.mouseMove(DroneRespawnX, DroneRespawnY); //needed to start moving from the respawn point
				droneFrozen = true; //freezes the drone until respawnTimer runs out
				respawnTimer.schedule(new SpawnFreeze(), 2000);
				}
			}
		}
	}
	
	@Override
	public void update() {
		try {
			mouseMove();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}