package Entity;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class AmmoDrop extends Entity {
	List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();

	Timer DespawnTimer = new Timer();
	BufferedImage ammoDrop1;
	BufferedImage ammoDrop2;
	public boolean stickToDrone = false;
	
	public AmmoDrop(Barrel barrel) {
		this.x = barrel.x + barrel.width/2;
		this.y = barrel.y + barrel.height/2;
		this.width = 15;
		this.height = 15;
		
		this.hitbox = new Rectangle(x, y, width, height);
		getAmmoDropImage();
	}
	public void getAmmoDropImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			ammoDrop1 = ImageIO.read(getClass().getResourceAsStream("/ammoDrop/ammoDrop1.png"));
			ammoDrop2 = ImageIO.read(getClass().getResourceAsStream("/ammoDrop/ammoDrop2.png"));
			
			imageList = Arrays.asList(ammoDrop1, ammoDrop2, ammoDrop1, ammoDrop2, ammoDrop1, ammoDrop2);
			timeIntervalList = Arrays.asList(300,50,50,50,50,50);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	} 
	public class Despawn extends TimerTask {
		@Override
		public void run() {
			stickToDrone = false;
			x = 0;
			y = 0;
		}
	}
	public static void spawnAmmoDrop(Barrel barrel, ArrayList<AmmoDrop> ammoDropList) {
		if (rng.nextDouble() > 0.50) {
			AmmoDrop ammoDrop = new AmmoDrop(barrel);
			ammoDropList.add(ammoDrop);
			ammoDrop.DespawnTimer.schedule(ammoDrop.new Despawn(), 10000);
		}
	}
	
	public void droneCollision(Drone drone) {
		if (drone.collision(this)) {
			stickToDrone = true;
		}
	}
	public void move(Drone drone) {
		if (stickToDrone == true && drone.droneFrozen == false) {
		x = drone.x;
		y = drone.y -20;
		}
		else {
			stickToDrone = false;
					}
		hitbox.x = x;
		hitbox.y = y;
	}
	public void giveToHuman(Human human, int score) {
		if (human.collision(this)) {
			stickToDrone = false;
			x = -1;
			score += 50;
			human.ammo = human.max_ammo;
			
		}
	}
	
	public void update(Drone drone, ArrayList<AmmoDrop> ammoDropList, Human human, int score) {
		move(drone);
		droneCollision(drone);
		giveToHuman(human, score);
	}
	public void draw(Graphics g) {
		BufferedImage image = null;
		image = getImage(imageList,timeIntervalList);
		g.drawImage(image, x - width/2, y - height/2, width, height, null);
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}
