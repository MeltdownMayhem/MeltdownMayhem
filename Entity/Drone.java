package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.Extra;
import MeltdownMayhem.GamePanel;
import MeltdownMayhem.GamePanel.UpdateTimerTask;

public class Drone extends Character {
	public Point2D MousePos =  MouseInfo.getPointerInfo().getLocation();
	public double MouseX = MousePos.getX();
	public double MouseY = MousePos.getY(); 
	public boolean droneFrozen = false;
	Extra rotor = new Extra();
	Timer respawnTimer = new Timer();
	
	
	public Drone() {
		this.width = 64;
		this.depth = 64;
		this.x = GamePanel.PANEL_WIDTH/2-this.width/2 + 128;
		this.y = GamePanel.BOARD_HEIGHT-this.depth-GamePanel.BOARD_HEIGHT/15;
		this.lives = 1;
		this.vx = 20;
		this.vy = 17.5;
		
		getDroneImage();
	}
	
	public void getDroneImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			drone1 = ImageIO.read(getClass().getResourceAsStream("/Drone/drone1.png"));
			drone2 = ImageIO.read(getClass().getResourceAsStream("/Drone/drone2.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics g) {
	BufferedImage image = null;
	if (rotor.spriteNum % 2 == 1) {
		image = drone1;
	} else if (rotor.spriteNum % 2 == 0) {
		image = drone2;
	}
	g.drawImage(image, x - width/2, y - depth/2, width, depth, null);
}
	
	public void mouseMove() {
		this.MousePos =  MouseInfo.getPointerInfo().getLocation();
		MouseX = MousePos.getX();
		MouseY = MousePos.getY();
		x = (int) MouseX;
		y = (int) MouseY;
	}
	public class SpawnFreeze extends TimerTask{
		@Override
		public void run() {
			droneFrozen = false;
			lives = 1;
		}
		
	}
	public void checkDroneCollision() throws AWTException{ //needed for Robot class
		final int DroneRespawnX = GamePanel.PANEL_WIDTH/2- width/2 + 128;
		final int DroneRespawnY = GamePanel.BOARD_HEIGHT- depth-GamePanel.BOARD_HEIGHT/15;
		for (Enemy enemy: GamePanel.enemyList) {
			if (x > enemy.x - Enemy.enemyRadius && x < enemy.x +Enemy.enemyRadius && y > enemy.y - Enemy.enemyRadius && y < enemy.y + Enemy.enemyRadius) {
				lives -= 1;
			if (lives == 0) {
				Robot robot = new Robot();
				robot.mouseMove(DroneRespawnX, DroneRespawnY); //needed to start moving from the respawn point
				droneFrozen = true; //freezes the drone until respawnTimer runs out
				x = DroneRespawnX;
				y = DroneRespawnY; //needed to move the model
				respawnTimer.schedule(new SpawnFreeze(), 2000);
				}
			}
		}
	}
	public void update() {
		rotor.updateGraphs(5, 1);
		if (droneFrozen ==  false) {
		mouseMove();
		}
	}
}
