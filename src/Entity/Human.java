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
import MeltdownMayhem.GamePanel;
import MeltdownMayhem.StartPanel;
/**
 * De Human is de belangrijkste speler van dit spel.
 * Wordt gecontroleerd volgens de pijljtes en kan plasma schieten met de space_bar.
 * Wanneer hij geraakt wordt teleporteert hij terug naar zijn beginplaats, verliest het een leven en krijgt het spawn_protection.
 * Als zijn levens (3 in totaal) opzijn is het spel afgelopen.
 */
public class Human extends Entity {

	public boolean moveRight, moveLeft, moveUp, moveDown;
	public int max_lives = 3;
	boolean shieldActive;
	boolean isTiny;
	
	public int ammo = 50;
	public int max_ammo = 50;
	public int shootingCooldown = 0; // Starts at 0, for instant shooting when tapping key
	public boolean isShooting = false;
	
	BufferedImage hazmat;
	BufferedImage hazmatForward1;
	BufferedImage hazmatForward2;
	BufferedImage shieldBubble;
	
	List<BufferedImage> horizontalImageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	Timer timer = new Timer();
	
	enum deathCauses {ORBBULLET, SNIPERBULLET, RAMPAGE, ORB, SNIPER, RAGE, BARREL}
	deathCauses killer;
	RadiationOrb orb;
	Rage rage;
	
	public Human() {
		this.width = 90;
		this.height = 165;
		
		this.x = GamePanel.screenSize.width/2 - this.width/2 - 128;
		this.y = GamePanel.BOARD_HEIGHT - this.height - GamePanel.BOARD_HEIGHT/15;
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
	
	public class ShieldTimerTask extends TimerTask {
		@Override
		public void run() {
			shieldActive = false;
		}
	}
	
	public class TinyTimerTask extends TimerTask {
		@Override
		public void run() {
			width = 90;
			height = 165;
			hitbox.width = width - 10;
			hitbox.height = height - 60;
			vx = 4;
			vy = 4.3;
			isTiny = false;
		}
	}
	
	public void takeDamage(deathCauses d, GamePanel gp) {
		if (shieldActive == false) {
			lives --;
			x = GamePanel.screenSize.width/2 - width/2;
			y = GamePanel.BOARD_HEIGHT - height - GamePanel.BOARD_HEIGHT/15;
			activateShield(2500);
			if (d == deathCauses.ORB) {
				gp.chatText.add(gp.nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.SNIPER) {
				gp.chatText.add(gp.nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.RAGE) {
				gp.chatText.add(gp.nameHuman + " should learn about the dangers of radiation");
			} else if (d == deathCauses.ORBBULLET) {
				gp.chatText.add(gp.nameHuman + " was hit by a radiation orb");
			} else if (d == deathCauses.SNIPERBULLET) {
				gp.chatText.add(gp.nameHuman + " was hit by a radiation sniper");
			} else if (d == deathCauses.RAMPAGE) {
				gp.chatText.add(gp.nameHuman + " wasn't fast enough");
			} else if (d == deathCauses.BARREL) {
				gp.chatText.add(gp.nameHuman + " got rolled over with a radioactive waste barrel");
			}
			gp.chatTimer.add(0);
		}
	}
	
	public void activateShield(int time) {
			shieldActive = true;
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
			timer.schedule(new TinyTimerTask(), time);
		}
	}
	
	public void shootBullet(ArrayList<Ammunition> ammoList) {
		if (isShooting == true && ammo > 0) {
			shootingCooldown--;
			if (shootingCooldown < 0) {
				ammoList.add(new Ammunition(x + width*76/100 - 2, y));
				shootingCooldown = 15;
				ammo --;
			}
		}
	}
	
	// Human Collisions
	public void humanCollisions(ArrayList<Enemy> enemyList, ArrayList<Barrel> barrelList, ArrayList<Ammunition> projectileList, GamePanel gp) {
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
				this.takeDamage(killer, gp);
				break;
			}
		}
		for (Barrel barrel: barrelList) {
			if (this.collision(barrel)) {
				this.takeDamage(deathCauses.BARREL, gp);
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
				this.takeDamage(killer, gp);
				gp.delProjectileList.add(bullet);
				break;
			}
		}
	}
	
	@Override
	public void update() {
		if (this.moveRight==true && x < GamePanel.BOARD_END - width - 2) { // 2-pixel draw accuracy
			x += vx;
		}
		if (this.moveLeft==true && x > GamePanel.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < GamePanel.BOARD_HEIGHT - height) {
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
		//g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		
		// Shield Bubble
		if (shieldActive) {
			g.drawImage(shieldBubble, x - (height - width)/2, y + height/8, height, height, null);
		}
	}
}