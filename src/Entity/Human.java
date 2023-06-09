package Entity;

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
import Entity.RadiationOrb.Model;
import MeltdownMayhem.Extra;
import MeltdownMayhem.Window;
/**
 * Human is controlled by the first player.
 * Its job is to shoot enemy and gather score to progress to the next level
 * The human can lose lives by colliding with enemies, barrels and enemy bullets
 * If human reaches 0 lives, the game is over.
 * Human is a subclass of Entity
 */
public class Human extends Entity {

	public boolean moveRight, moveLeft, moveUp, moveDown;
	public int max_lives = 3;
	public int absorptionLives = 0;
	boolean shieldActive;
	boolean isTiny;
	
	public int ammo = 50;
	public int max_ammo = 50;
	public int shootingCooldown = 0; // Starts at 0, for instant shooting when tapping key
	public boolean isShooting = false;
	
	private BufferedImage hazmat;
	private BufferedImage hazmatForward1;
	private BufferedImage hazmatForward2;
	private BufferedImage shieldBubble;
	
	private List<BufferedImage> horizontalImageList = new ArrayList<BufferedImage>();
	private List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	public Timer timer = new Timer();
	
	private enum deathCauses {ORBBULLET, SNIPERBULLET, RAMPAGE, ORB, SNIPER, RAGE, BARREL}
	private deathCauses killer;
	private RadiationOrb orb;
	private Rage rage;
		
	public Human() {
		this.width = 90;
		this.height = 165;
		
		this.x = Window.screenSize.width/2 - this.width/2 - 128;
		this.y = Window.BOARD_HEIGHT - this.height - Window.BOARD_HEIGHT/15;
		this.vx = 4;
		this.vy = 4.3;
		
		this.lives = 3; // Starting amount of lives
		this.hitbox = new Rectangle(x + 5, y + 45, width - 10, height - 60);
		
		getHumanImage();
	}
	
	public void getHumanImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		try {
			hazmat = ImageIO.read(getClass().getResourceAsStream("/human/hazmat.png"));
			hazmatForward1 = ImageIO.read(getClass().getResourceAsStream("/human/hazmatForward1.png"));
			hazmatForward2 = ImageIO.read(getClass().getResourceAsStream("/human/hazmatForward2.png"));
			shieldBubble = ImageIO.read(getClass().getResourceAsStream("/human/shieldBubble.png"));
			
			horizontalImageList = Arrays.asList(hazmat,hazmatForward1,hazmat,hazmatForward2);
			timeIntervalList = Arrays.asList(5,6,5,6);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	// Bunch of timers related to the power-ups for Human
	public class ShieldTimerTask extends TimerTask {
		static int shieldNumber;
		@Override
		public void run() {
			shieldNumber--;
			if (shieldNumber == 0) {
				shieldActive = false;
			}
		}
	}
	
	public class TinyTimerTask extends TimerTask {
		static int shrinkNumber;
		@Override
		public void run() {
			shrinkNumber--;
			if (shrinkNumber == 0) {
				width = 90;
				height = 165;
				hitbox.width = width - 10;
				hitbox.height = height - 60;
				vx = 4;
				vy = 4.3;
				isTiny = false;
			}
		}
	}
	// checks if Human is taking damage from any possible source
	public void takeDamage(deathCauses d, String nameHuman, ArrayList<String> chatText, ArrayList<Integer> chatTimer) {
		if (shieldActive == false) {
			if (absorptionLives == 0) {
				lives--;
			} else {
				absorptionLives--;
			}
			x = Window.screenSize.width/2 - width/2;
			y = Window.BOARD_HEIGHT - height - Window.BOARD_HEIGHT/15;
			activateShield(2500);
			if (d == deathCauses.ORB) {
				chatText.add(nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.SNIPER) {
				chatText.add(nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.RAGE) {
				chatText.add(nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.ORBBULLET) {
				chatText.add(nameHuman + " was hit by a radiation orb");
			} else if (d == deathCauses.SNIPERBULLET) {
				chatText.add(nameHuman + " was hit by a radiation sniper");
			} else if (d == deathCauses.RAMPAGE) {
				chatText.add(nameHuman + " wasn't fast enough");
			} else if (d == deathCauses.BARREL) {
				chatText.add(nameHuman + " got rolled over with a radioactive waste barrel");
			}
			chatTimer.add(0);
			Extra.playSound("/DeathHuman.wav");
		}
	}
	// 2 functions related to power-ups
	public void activateShield(int time) {
			shieldActive = true;
			ShieldTimerTask.shieldNumber++;
			timer.schedule(new ShieldTimerTask(), time);
	}
	
	public void shrink(int time, double shrinkFactor, double speedFactor) {
		if (isTiny == false) {
			width /= shrinkFactor;
			height /= shrinkFactor;
			hitbox.width /= shrinkFactor;
			hitbox.height /= shrinkFactor;
			vx *= speedFactor;
			vy *= speedFactor;
			isTiny = true;
		}
		TinyTimerTask.shrinkNumber++;
		timer.schedule(new TinyTimerTask(), time);
	}
	
	// defines a cool-down period between friendly shots
	public void shootBullet(ArrayList<Ammunition> ammoList) {
		if (isShooting == true && ammo > 0) {
			shootingCooldown--;
			if (shootingCooldown < 0) {
				ammoList.add(new Ammunition(x + width*76/100 - 2, y));
				Extra.playSound("/Shoot.wav");
				shootingCooldown = 15;
				ammo --;
			}
		}
	}
	
	// Human Collisions
	public ArrayList<Ammunition> humanCollisions(String nameHuman, ArrayList<String> chatText, ArrayList<Integer> chatTimer, ArrayList<Enemy> enemyList, ArrayList<Barrel> barrelList, ArrayList<Ammunition> projectileList, ArrayList<Ammunition> delProjectileList) {
		for (Enemy enemy: enemyList) {
			if (this.collision(enemy)) {
				if (enemy instanceof RadiationOrb) {
					orb = (RadiationOrb) enemy;
					if (orb.type == Model.ORB) {
						killer = deathCauses.ORB;
					} else {
						killer = deathCauses.SNIPER;
					}
				} else if (enemy instanceof Rage){
					rage = (Rage) enemy;
					if (rage.rampage) {
						killer = deathCauses.RAMPAGE;
					} else {
						killer = deathCauses.RAGE;
					}
				}
				this.takeDamage(killer, nameHuman, chatText, chatTimer);
				break;
			}
		}
		for (Barrel barrel: barrelList) {
			if (this.collision(barrel)) {
				this.takeDamage(deathCauses.BARREL, nameHuman, chatText, chatTimer);
				break;
			}
		}
		for (Ammunition bullet: projectileList) {
			if (this.collision(bullet)) {
				if (bullet.green) {
					killer = deathCauses.ORBBULLET;
				} else {
					killer = deathCauses.SNIPERBULLET;
				}
				this.takeDamage(killer, nameHuman, chatText, chatTimer);
				delProjectileList.add(bullet);
				break;
			}
		}
		return delProjectileList;
	}
	
	//update for movement
	@Override
	public void update() {
		if (this.moveRight==true && x < Window.BOARD_END - width - 2) { // 2-pixel draw accuracy
			x += vx;
		}
		if (this.moveLeft==true && x > Window.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < Window.BOARD_HEIGHT - height) {
			y += vy + 1;
		}
		hitbox.x = x + 5;
		hitbox.y = y + 45;
	}
	
	public void draw(Graphics g) {
		BufferedImage image;
		if (moveUp == true && moveDown == false || moveUp == false && moveDown == true || moveRight == true && moveLeft == false || moveRight == false && moveLeft == true) {
			image = this.getImage(horizontalImageList, timeIntervalList);
		} else {
			image = hazmat;
		}
		
		g.drawImage(image, x, y, width, height, null);
		
		// Shield Bubble
		if (shieldActive) {
			g.drawImage(shieldBubble, x - (height - width)/2, y + height/8, height, height, null);
		}
	}
}