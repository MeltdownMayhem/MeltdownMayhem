package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import MeltdownMayhem.Extra;
/**
 * De RadiationOrb is een groene Enemy type dat willekeurig rondvliegt.
 * Ability: projectielen schieten naar de player.
 */
public class RadiationOrb extends Enemy {
	
	// RadiationOrb images
	BufferedImage orbLeft1, orbLeft2, orbRight1, orbRight2;
	BufferedImage sniperLeft1, sniperLeft2, sniperRight1, sniperRight2;
	
	// Image-lists for the getImage(ArrayList, ArrayList) method
	List<BufferedImage> orbLeftImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> orbRightImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> sniperLeftImageList = new ArrayList<BufferedImage>();
	List<BufferedImage> sniperRightImageList = new ArrayList<BufferedImage>();
	List<Integer> timeIntervalList = new ArrayList<Integer>();
	
	enum Model{ORB, SNIPER};
	Model type;
	
	private int shootingCooldown = 0;
	private final double shootingChance = 0.045;
	private static double shootingAngle, shootingDistance, vxbullet, vybullet;
	private static int distanceOffTarget, shootingTarget_x, shootingTarget_y;
	
	public RadiationOrb(int x, boolean sniper) {
		super(x);
		killScore = 10;
		getOrbImage();
		if (sniper) {
			this.type = Model.SNIPER;
		} else {
			this.type = Model.ORB;
		}
	}
	
	public void getOrbImage() { // <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
		
		try {
			orbLeft1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft1.png"));
			orbLeft2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbLeft2.png"));
			orbRight1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight1.png"));
			orbRight2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/orbRight2.png"));
			
			sniperLeft1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/sniperLeft1.png"));
			sniperLeft2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/sniperLeft2.png"));
			sniperRight1 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/sniperRight1.png"));
			sniperRight2 = ImageIO.read(getClass().getResourceAsStream("/radiation_orb/sniperRight2.png"));
			
			orbLeftImageList = Arrays.asList(orbLeft1,orbLeft2);
			orbRightImageList = Arrays.asList(orbRight1,orbRight2);
			sniperLeftImageList = Arrays.asList(sniperLeft1,sniperLeft2);
			sniperRightImageList = Arrays.asList(sniperRight1,sniperRight2);
			timeIntervalList = Arrays.asList(100,40);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Human human) {
		
		this.shootingCooldown++;
		
		this.x += (int) SPEED_COEFFICIENT * this.vx;
		this.y += (int) SPEED_COEFFICIENT * this.vy;
		
		spawnPriority();
		stayInField();
		randomSpeed();
		
		// These 2 lines makes the Enemies oscillate
		//this.x += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
		//this.y += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		
		if (this.type == Model.ORB) {
			if (vx < 0) {
				image = this.getImage(orbLeftImageList,timeIntervalList);
			} else {
				image = this.getImage(orbRightImageList,timeIntervalList);
			}
			g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
			//g.drawOval(x - hitboxRadius, y - hitboxRadius, hitboxRadius*2, hitboxRadius*2);
		} else {
			if (vx < 0) {
				image = this.getImage(sniperLeftImageList,timeIntervalList);
			} else {
				image = this.getImage(sniperRightImageList,timeIntervalList);
			}
			g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
		}
	}
	
	
	// Enemy Shooting
	public void shootBullet(ArrayList<Ammunition> projectileList, Human human) {
		if (this.shootingCooldown > 150 && rng.nextDouble() < shootingChance) {
			this.aimAndShoot(projectileList, human);
			this.shootingCooldown = 0;
		}
	}
	
	public void aimAndShoot(ArrayList<Ammunition> projectileList, Human human) {
		if (this.type == RadiationOrb.Model.SNIPER) {
			shootingAngle = rng.nextDouble() * 2 * Math.PI;
			distanceOffTarget = rng.nextInt(300);
			shootingTarget_x = (int) (human.x + distanceOffTarget * Math.cos(shootingAngle));
			shootingTarget_y = (int) (human.y + distanceOffTarget * Math.sin(shootingAngle));
			shootingDistance = Extra.distance(this.x, this.y, (int)(shootingTarget_x), (int)(shootingTarget_y));
			vxbullet = (shootingTarget_x - this.x)/shootingDistance * 5;
			vybullet = (shootingTarget_y - this.y)/shootingDistance * 5;
			projectileList.add(new Ammunition(this.x, this.y, vxbullet, vybullet));
			this.shootingCooldown = 0;
		} else {
			projectileList.add(new Ammunition(this.x, this.y, 0, 5));
		}
	}
}