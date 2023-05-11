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
import MeltdownMayhem.GamePanel;
/**
 * Class to make PowerUp Drop on the Board.
 * Power Ups gets dropped when Barrels are destroyed.
 * When it makes contact with the Human, an special effect will be given.
 */
public class PowerUp extends Entity {
	
	enum Power{ammoDrop, healthSyringe, shield, shrinkShroom, absorptionHeart, extraAmmo};
	Power powerUp;

	public boolean pickedUp = false;
	Timer DespawnTimer = new Timer();
	
	// ammoDrop
	public int ammoAmount = 10;
	private static double ammoDropChance = 0.54; //0.54
	
	// healthSyringe
	private int healAmount = 1;
	private static double syringeDropChance = 0.10; //0.10
	
	// shield
	private int shieldDuration = 8000;
	private static double shieldDropChance = 0.14; //0.14
	
	// shrinkShroom
	private int shrinkDuration = 10000;
	private double shrinkFactor = 1.8;
	private double speedFactor = 1.3;
	private static double shrinkShroomDropChance = 0.15; //0.15
	
	// absorptionHeart
	private static int max_absorptionHearts = 2;
	private static double absorptionHeartDropChance = 0.04; //0.04
	
	// extraAmmo
	private int extraAmmoAmount = 15;
	private static int extraAmmoDropped = 0;
	private static int max_extraAmmo = 3;
	private static double extraAmmoDropChance = 0.03; //0.03
	
	// images
	BufferedImage ammoDrop;
	BufferedImage ammoDropDespawn;
	BufferedImage healthSyringe;
	BufferedImage healthSyringeDespawn;
	BufferedImage shield;
	BufferedImage shieldDespawn;
	BufferedImage shrinkShroom;
	BufferedImage shrinkShroomDespawn;
	BufferedImage absorptionHeart;
	BufferedImage absorptionHeartDespawn;
	BufferedImage extraAmmo;
	
	List<BufferedImage> ammoDropImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> healthSyringeImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> shieldImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> shrinkShroomImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> absorptionHeartImageList = new ArrayList<BufferedImage>();
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
			ammoDrop = ImageIO.read(getClass().getResourceAsStream("/power_up/ammoDrop.png"));
			ammoDropDespawn = ImageIO.read(getClass().getResourceAsStream("/power_up/ammoDropDespawn.png"));
			healthSyringe = ImageIO.read(getClass().getResourceAsStream("/power_up/healthSyringe.png"));
			healthSyringeDespawn = ImageIO.read(getClass().getResourceAsStream("/power_up/healthSyringeDespawn.png"));
			shield = ImageIO.read(getClass().getResourceAsStream("/power_up/shield.png"));
			shieldDespawn = ImageIO.read(getClass().getResourceAsStream("/power_up/shieldDespawn.png"));
			shrinkShroom = ImageIO.read(getClass().getResourceAsStream("/power_up/shrinkShroom.png"));
			shrinkShroomDespawn = ImageIO.read(getClass().getResourceAsStream("/power_up/shrinkShroomDespawn.png"));
			absorptionHeart = ImageIO.read(getClass().getResourceAsStream("/power_up/absorptionHeart.png"));
			absorptionHeartDespawn = ImageIO.read(getClass().getResourceAsStream("/power_up/absorptionHeartDespawn.png"));
			extraAmmo = ImageIO.read(getClass().getResourceAsStream("/power_up/extraAmmo.png"));
			
			ammoDropImageList = Arrays.asList(ammoDrop, ammoDropDespawn, ammoDrop, ammoDropDespawn, ammoDrop, ammoDropDespawn);
			healthSyringeImageList = Arrays.asList(healthSyringe, healthSyringeDespawn, healthSyringe, healthSyringeDespawn, healthSyringe, healthSyringeDespawn);
			shieldImageList = Arrays.asList(shield, shieldDespawn, shield, shieldDespawn, shield, shieldDespawn);
			shrinkShroomImageList = Arrays.asList(shrinkShroom, shrinkShroomDespawn, shrinkShroom, shrinkShroomDespawn, shrinkShroom, shrinkShroomDespawn);
			absorptionHeartImageList = Arrays.asList(absorptionHeart, absorptionHeartDespawn, absorptionHeart, absorptionHeartDespawn, absorptionHeart, absorptionHeartDespawn);
			timeIntervalList = Arrays.asList(1200, 30, 50, 30, 50, 30);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void spawnPowerUp(Barrel barrel, GamePanel gp) {
		double dropChance,chance;
		if (gp.score >= 1500) {
			dropChance = 0.75;
		} else {
			dropChance = 0.6;
		}
		if (rng.nextDouble() < dropChance) {
			chance = rng.nextDouble();
			if (chance < ammoDropChance) {
				PowerUp ammoDrop = new PowerUp(barrel, Power.ammoDrop);
				gp.powerUpList.add(ammoDrop);
				ammoDrop.DespawnTimer.schedule(ammoDrop.new Despawn(), 15000);
			} else if (chance < ammoDropChance + syringeDropChance) {
				PowerUp syringe = new PowerUp(barrel, Power.healthSyringe);
				gp.powerUpList.add(syringe);
				syringe.DespawnTimer.schedule(syringe.new Despawn(), 15000);
			} else if (chance < ammoDropChance + syringeDropChance + shieldDropChance) {
				PowerUp shield = new PowerUp(barrel, Power.shield);
				gp.powerUpList.add(shield);
				shield.DespawnTimer.schedule(shield.new Despawn(), 15000);
			} else if (chance < ammoDropChance + syringeDropChance + shieldDropChance + shrinkShroomDropChance) {
				PowerUp shroom = new PowerUp(barrel, Power.shrinkShroom);
				gp.powerUpList.add(shroom);
				shroom.DespawnTimer.schedule(shroom.new Despawn(), 15000);
			} else if (chance < ammoDropChance + syringeDropChance + shieldDropChance + shrinkShroomDropChance + absorptionHeartDropChance) {
				PowerUp heart = new PowerUp(barrel, Power.absorptionHeart);
				gp.powerUpList.add(heart);
				heart.DespawnTimer.schedule(heart.new Despawn(), 15000);
			} else if (chance < ammoDropChance + syringeDropChance + shieldDropChance + shrinkShroomDropChance + absorptionHeartDropChance + extraAmmoDropChance && extraAmmoDropped < max_extraAmmo) {
				PowerUp ammo = new PowerUp(barrel, Power.extraAmmo);
				gp.powerUpList.add(ammo);
				extraAmmoDropped++;
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
					if (gp.level == 3) {
						human.ammo += 5;
						// more ammo in level 3
					}
				} else {
					human.ammo = human.max_ammo;
				}
			} else if (powerUp == Power.healthSyringe) {
				if (human.lives < human.max_lives) {
					human.lives += healAmount;
				} else {
					gp.score += 50;
				}
			} else if (powerUp == Power.shield) {
				human.activateShield(shieldDuration);
			} else if (powerUp == Power.shrinkShroom) {
				human.shrink(shrinkDuration, shrinkFactor, speedFactor);
			} else if (powerUp == Power.absorptionHeart) {
				if (human.absorptionLives < max_absorptionHearts) {
					human.absorptionLives++;
				} else {
					gp.score += 100;
				}
			} else if (powerUp == Power.extraAmmo) {
				human.max_ammo += extraAmmoAmount;
				human.ammo += extraAmmoAmount;
			} 
		}
	}
	
	public void draw(Graphics g) {
		if (powerUp == Power.ammoDrop) {
			BufferedImage image = getImage(ammoDropImageList,timeIntervalList);
			g.drawImage(image, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.healthSyringe) {
			BufferedImage image = getImage(healthSyringeImageList,timeIntervalList);
			g.drawImage(image, x - width, y - height, width*2, height*2, null);
		} else if (powerUp == Power.shield) {
			BufferedImage image = getImage(shieldImageList,timeIntervalList);
			g.drawImage(image, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.shrinkShroom) {
			BufferedImage image = getImage(shrinkShroomImageList,timeIntervalList);
			g.drawImage(image, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.absorptionHeart) {
			BufferedImage image = getImage(absorptionHeartImageList,timeIntervalList);
			g.drawImage(image, x - width/2, y - height/2, width, height, null);
		} else if (powerUp == Power.extraAmmo) {
			g.drawImage(extraAmmo, x - width/2, y - height/2, width, height, null);
		}
		//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
	}
}