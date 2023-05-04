package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Entity.PowerUp.Despawn;
import Entity.PowerUp.Power;
import MeltdownMayhem.GamePanel;
/**
 * Class to make PowerUp Drop on the Board.
 * Power Ups gets dropped when Barrels are destroyed.
 * When it makes contact with the Human, an special effect will be given.
 */
public class PowerUp extends Entity {
	
	enum Power{ammoDrop, healthSyringe, shield, shrinkShroom, extraAmmo, extraHeart};
	Power powerUp;

	public boolean pickedUp = false;
	Timer DespawnTimer = new Timer();
	
	// ammoDrop
	private int ammoAmount = 10;
	//private static double ammoDropChance = 0.45;
	private static double ammoDropChance = 0.60;
	
	// healthSyringe
	private int healAmount = 1;
	//private static double syringeDropChance = 0.15;
	private static double syringeDropChance = 0.05;
	
	// shield
	private int shieldDuration = 8000;
	//private static double shieldDropChance = 0.20;
	private static double shieldDropChance = 0.15;
	
	// shrinkShroom
	private int shrinkDuration = 10000;
	private double shrinkFactor = 1.8;
	private double speedFactor = 1.3;
	//private static double shrinkShroomDropChance = 0.20;
	private static double shrinkShroomDropChance = 0.15;
	
	// extraAmmo
	private int extraAmmoAmount = 15;
	private static int extraAmmoDropped = 0;
	private static int max_extraAmmo = 3;
	//private static double extraAmmoDropChance = 0.08;
	private static double extraAmmoDropChance = 0.03;
	
	// extraHeart
	private static int extraHeartsDropped = 0;
	private static int max_extraHearts = 2;
	//private static double extraHeartDropChance = 0.05;
	private static double extraHeartDropChance = 0.02;
	
	private static double chance;
	
	// images
	BufferedImage ammoDrop1;
	BufferedImage ammoDrop2;
	BufferedImage healthSyringe;
	BufferedImage shield;
	BufferedImage shrinkShroom;
	BufferedImage extraAmmo;
	BufferedImage extraHeart;
	
	List<BufferedImage> ammoDropImageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public PowerUp(Barrel barrel, Power powerUp) {
		this.width = 30;
		this.height = 30;
		
		this.x = barrel.x + barrel.width/2;
		this.y = barrel.y + barrel.height/2;
		
		this.powerUp = powerUp;
		
		this.hitboxRadius = 17;
		
		getPowerUpImage();
	}
	
	public void getPowerUpImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			ammoDrop1 = ImageIO.read(getClass().getResourceAsStream("/power_up/ammoDrop1.png"));
			ammoDrop2 = ImageIO.read(getClass().getResourceAsStream("/power_up/ammoDrop2.png"));
			healthSyringe = ImageIO.read(getClass().getResourceAsStream("/power_up/healthSyringe.png"));
			shield = ImageIO.read(getClass().getResourceAsStream("/power_up/shield.png"));
			shrinkShroom = ImageIO.read(getClass().getResourceAsStream("/power_up/shrinkShroom.png"));
			extraAmmo = ImageIO.read(getClass().getResourceAsStream("/power_up/extraAmmo.png"));
			extraHeart = ImageIO.read(getClass().getResourceAsStream("/power_up/extraHeart.png"));
			
			ammoDropImageList = Arrays.asList(ammoDrop1, ammoDrop2, ammoDrop1, ammoDrop2, ammoDrop1, ammoDrop2);
			timeIntervalList = Arrays.asList(500, 30, 50, 30, 50, 30);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void spawnPowerUp(Barrel barrel, GamePanel gp) {
//		if (rng.nextDouble() > 1 - ammoDropChance) {
//			PowerUp ammoDrop = new PowerUp(barrel, Power.ammoDrop);
//			gp.powerUpList.add(ammoDrop);
//			ammoDrop.DespawnTimer.schedule(ammoDrop.new Despawn(), 15000);
//		} else if (rng.nextDouble() > 1 - syringeDropChance) {
//			PowerUp syringe = new PowerUp(barrel, Power.healthSyringe);
//			gp.powerUpList.add(syringe);
//			syringe.DespawnTimer.schedule(syringe.new Despawn(), 15000);
//		} else if (rng.nextDouble() > 1 - shieldDropChance) {
//			PowerUp shield = new PowerUp(barrel, Power.shield);
//			gp.powerUpList.add(shield);
//			shield.DespawnTimer.schedule(shield.new Despawn(), 15000);
//		} else if (rng.nextDouble() > 1 - shrinkShroomDropChance) {
//			PowerUp shroom = new PowerUp(barrel, Power.shrinkShroom);
//			gp.powerUpList.add(shroom);
//			shroom.DespawnTimer.schedule(shroom.new Despawn(), 15000);
//		} else if (rng.nextDouble() > 1 - extraAmmoDropChance && extraAmmoDropped < max_extraAmmo) {
//			PowerUp ammo = new PowerUp(barrel, Power.extraAmmo);
//			gp.powerUpList.add(ammo);
//			extraAmmoDropped++;
//		} else if (rng.nextDouble() > 1 - extraHeartDropChance && extraHeartsDropped < max_extraHearts) {
//			PowerUp heart = new PowerUp(barrel, Power.extraHeart);
//			gp.powerUpList.add(heart);
//			extraHeartsDropped++;
//		}
		
		if (rng.nextDouble() > 0.4) {
			chance = rng.nextDouble();
			if (chance > 1 - ammoDropChance) {
				PowerUp ammoDrop = new PowerUp(barrel, Power.ammoDrop);
				gp.powerUpList.add(ammoDrop);
				ammoDrop.DespawnTimer.schedule(ammoDrop.new Despawn(), 15000);
			} else if (chance > 1 - ammoDropChance - syringeDropChance) {
				PowerUp syringe = new PowerUp(barrel, Power.healthSyringe);
				gp.powerUpList.add(syringe);
				syringe.DespawnTimer.schedule(syringe.new Despawn(), 15000);
			} else if (chance > 1 - ammoDropChance - syringeDropChance - shieldDropChance) {
				PowerUp shield = new PowerUp(barrel, Power.shield);
				gp.powerUpList.add(shield);
				shield.DespawnTimer.schedule(shield.new Despawn(), 15000);
			} else if (chance > 1 - ammoDropChance - syringeDropChance - shieldDropChance - shrinkShroomDropChance) {
				PowerUp shroom = new PowerUp(barrel, Power.shrinkShroom);
				gp.powerUpList.add(shroom);
				shroom.DespawnTimer.schedule(shroom.new Despawn(), 15000);
			} else if (chance > 1 - ammoDropChance - syringeDropChance - shieldDropChance - shrinkShroomDropChance - extraAmmoDropChance && extraAmmoDropped < max_extraAmmo) {
				PowerUp ammo = new PowerUp(barrel, Power.extraAmmo);
				gp.powerUpList.add(ammo);
				extraAmmoDropped++;
			} else if (chance > 1 - ammoDropChance - syringeDropChance - shieldDropChance - shrinkShroomDropChance - extraAmmoDropChance - extraHeartDropChance && extraHeartsDropped < max_extraHearts) {
				PowerUp heart = new PowerUp(barrel, Power.extraHeart);
				gp.powerUpList.add(heart);
				extraHeartsDropped++;
			}
		}
	}
	
	public class Despawn extends TimerTask {
		@Override
		public void run() {
			pickedUp = false;
			x = 0;
			y = 0;
		}
	}
	
	public void update(Drone drone, Human human, GamePanel gp) {
		// Update Movement
		if (pickedUp == true && drone.droneFrozen == false) {
			x = drone.x;
			y = drone.y - 20;
		} else if (pickedUp == true && drone.droneFrozen == true) {
			pickedUp = false;
			drone.invSlot = null;
		}
		
		// Human Collision with Drops
		if (this.collision(human)) {
			pickedUp = false;
			if (drone.invSlot == this) {
				drone.invSlot = null;
			}
			x = -1;
			
			// The different effects on the human from the PowerUps
			if (powerUp == Power.ammoDrop) {
				if (human.ammo + ammoAmount <= human.max_ammo) {
					human.ammo += ammoAmount;
				} else {
					human.ammo = human.max_ammo;
				}
			} else if (powerUp == Power.healthSyringe) {
				if (human.lives != human.max_lives) {
					human.lives += healAmount;
				} else {
					gp.score += 50;
				}
				
			} else if (powerUp == Power.shield) {
				human.activateShield(shieldDuration);
			} else if (powerUp == Power.shrinkShroom) {
				human.shrink(shrinkDuration, shrinkFactor, speedFactor);
			} else if (powerUp == Power.extraAmmo) {
				human.max_ammo += extraAmmoAmount;
				human.ammo += extraAmmoAmount;
			} else if (powerUp == Power.extraHeart) {
				human.max_lives++;
				human.lives++;
			}
		}
	}
	
	public void draw(Graphics g) {
		if (powerUp == Power.ammoDrop) {
			BufferedImage image = getImage(ammoDropImageList,timeIntervalList);
			g.drawImage(image, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.healthSyringe) {
			g.drawImage(healthSyringe, x - width, y - height, width*2, height*2, null);
		} else if (powerUp == Power.shield) {
			g.drawImage(shield, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.shrinkShroom) {
			g.drawImage(shrinkShroom, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.extraAmmo) {
			g.drawImage(extraAmmo, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.extraHeart) {
			g.drawImage(extraHeart, x - width/2, y - height/2, width, height, null);
		}
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}