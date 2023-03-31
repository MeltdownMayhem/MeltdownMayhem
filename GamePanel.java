package MeltdownMayhem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Entity.Ammunition;
import Entity.Barrel;
import Entity.Enemy;
import Entity.Human;

public class GamePanel extends JPanel{
	
	public static final int PANEL_WIDTH = 1920; // Panel sizes are for the window
	public static final int PANEL_HEIGHT =1080;
	public static final int BOARD_WIDTH = 1188; // Board sizes are for the playable area
	public static final int BOARD_HEIGHT = PANEL_HEIGHT-120;
	public static final int BOARD_START = (PANEL_WIDTH-BOARD_WIDTH)/2;
	public static final int BOARD_END = (PANEL_WIDTH-BOARD_WIDTH)/2 + BOARD_WIDTH;
	
	// Game variables
	public static boolean gameOver = false;
	public static int score = 0;
	public static int ammo = 100;
	public static int max_ammo = 100;
	public static final int max_enemies = 12;
	public static final int max_barrels = 3;
	
	// Technical variables
	private static final double SPAWN_CHANCE = 0.995;
	protected static double shootChance = 0.05;
	static int shootingCooldown = 0; // Start on 0, to shoot instantly when tapping
	static boolean shooting = false;
	KeyHandler keyH = new KeyHandler();
	public BufferedImage background1;
	
	double alfa, r, distance;
	double vx, vy;
	double target_x, target_y;
	
	// Objects and lists
	public static Human human = new Human();
	public static GUI gui = new GUI();
	public static ArrayList<Ammunition> ammoList; // Player ammo
	public static ArrayList<Ammunition> projectileList; // Enemy ammo
	public static ArrayList <Enemy> enemyList;
	public static ArrayList <ArrayList<Enemy>> enemiesInCollision;
	public static ArrayList<Barrel> barrelList;
	
	public Random rng = new Random();
	
	ArrayList<Ammunition> delAmmo = new ArrayList<Ammunition>();
	ArrayList<Enemy> delEnemy = new ArrayList<Enemy>();

	public GamePanel() {
		// Panel Settings
		this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
		this.setBackground(new Color(215,215,215)); // Light-Gray
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		
		// ObjectLists creation
		barrelList = new ArrayList<Barrel>();
		enemyList = new ArrayList<Enemy>();
		enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		ammoList = new ArrayList<Ammunition>();
		projectileList = new ArrayList<Ammunition>();
		
		// FPS
		Timer t = new Timer();
		t.scheduleAtFixedRate(new UpdateTimerTask(), 0, 20);
		
		try {
			background1 = ImageIO.read(getClass().getResourceAsStream("/GUI/background1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class UpdateTimerTask extends TimerTask{
		@Override
		public void run() {
			if (gameOver == false) {
				update();
				checkCollision();
				repaint();
			}
		}
	}
	
	
	
	public void update() {
		
		// Update Characters
		human.update();
		
		
		// Update Enemies
		if (! enemyList.isEmpty()) {
			for (Enemy enemy:enemyList) {
				enemy.update();
			}
		}
		
		// Spawn Enemy
		if (GamePanel.enemyList.size() < GamePanel.max_enemies && rng.nextDouble() * Math.exp(GamePanel.score / 5000) >= SPAWN_CHANCE) {
			Event.spawnEnemy();
		}
		
		// Check for Collision between Enemies
		enemiesInCollision = Event.getEnemiesInCollision();
		Event.avoidEnemyCollision(enemiesInCollision);
		
		// Enemy Shoot
		for (Enemy e: enemyList) {
			if (e.shootCooldown > 150 && rng.nextDouble() < shootChance) {
				System.out.println("shot");
				alfa = rng.nextDouble() * 2 * Math.PI;
				r = rng.nextInt(300);
				target_x = human.x + r * Math.cos(alfa);
				target_y = human.y + r * Math.sin(alfa);
				distance = Extra.distance(e.x, e.y, (int)(target_x), (int)(target_y));
				vx = (target_x - e.x)/distance * 5;
				vy = (target_y - e.y)/distance * 5;
				projectileList.add(new Ammunition(e.x, e.y, vx, vy));
				e.shootCooldown = 0;
			}
		}
				
		
		// Chance to add Barrels to the Game
		if (rng.nextDouble() >= 0.995 && barrelList.size() < max_barrels) {
			barrelList.add(new Barrel());
		}
		
		// Update Barrels + remove/delete Barrel when out Panel
		if (! barrelList.isEmpty()) {
			for (Barrel barrel:barrelList) {
				barrel.update();
			}
			if (barrelList.get(0).y > GamePanel.BOARD_HEIGHT + 30) {
				barrelList.remove(0);
			}
		}
		
		if (shooting == true && ammo > 0) {
			shootingCooldown--;
			if (shootingCooldown < 0) {
				ammo -= 1;
				ammoList.add(new Ammunition(human.x+human.width*76/100 - 2,human.y));
				shootingCooldown = 15;
			}
		}
		
		// Update Bullets + remove/delete Bullet when out Panel
		if (! ammoList.isEmpty()) {
			for (Ammunition ammo:ammoList) {
				ammo.update();
			}
			if (ammoList.get(0).y < 0) {
				ammoList.remove(0);
			}
		}
		
		
		// Update Enemy ammo + remove if outside board
		if (! projectileList.isEmpty()) {
			for (Ammunition projectile: projectileList) {
				projectile.update();
				//if (projectile.outsideBoard()) {
					//delAmmo.add(projectile);
				//}
			}
		}
	}
	
	
	
	public void checkCollision() {
		for (Barrel barrel:barrelList) {
			if (human.x > barrel.x && human.x < barrel.x + barrel.width && human.y < barrel.y + barrel.height && human.y > barrel.y) {
				human.hit();
			}
		}
		
		// Check if Human hit by Enemy
		for (Ammunition p: projectileList) {
			if (Extra.distance(p.x, p.y, (int)(human.x - 10 + human.width * 0.6), (int)(human.y + human.width * 1.15)) < human.width / 2 + p.width / 2) {
				human.hit();
				delAmmo.add(p);
			}
		}
		
		// Check if Enemy hit
		for (Ammunition a: ammoList) {
			for (Enemy e: enemyList) {
				if (Extra.distance(a.x, a.y - a.height /4, e.x, e.y) < (Enemy.enemyRadius * 0.7 + a.width / 2)) {
					delAmmo.add(a);
					delEnemy.add(e);
					score += 100;
				}
			}
		}
		for (Enemy e: delEnemy) {
			enemyList.remove(e);
		}
		for (Ammunition a: delAmmo) {
			ammoList.remove(a);
		}
		
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Background
		g.drawImage(background1, GamePanel.BOARD_START, 0, 547, 555, null);
		g.drawImage(background1, GamePanel.BOARD_START + 546, 0, 547, 555, null);
		g.drawImage(background1, GamePanel.BOARD_START + 1092, 0, 547, 555, null);
		g.drawImage(background1, GamePanel.BOARD_START, 553, 547, 555, null);
		g.drawImage(background1, GamePanel.BOARD_START + 546, 553, 547, 555, null);
		g.drawImage(background1, GamePanel.BOARD_START + 1092, 553, 547, 555, null);
		
		
		// Show boardarea wherein enemies can freely move
//		g.setColor(new Color(255,0,0));
//		g.drawLine(0, Enemy.ENEMY_BOARD_UPPER_BORDER, PANEL_WIDTH, Enemy.ENEMY_BOARD_UPPER_BORDER);
//		g.drawLine(0, Enemy.ENEMY_BOARD_BOTTOM_BORDER, PANEL_WIDTH, Enemy.ENEMY_BOARD_BOTTOM_BORDER);
		
		// Draw Ammunition, Characters and Barrels
		for (Ammunition ammo:ammoList) {
			ammo.draw(g);
        }
		
		for (Ammunition projectile: projectileList) {
			projectile.draw(g);
		}
		
		human.draw(g);
		
		for (Barrel barrel:barrelList) {
			barrel.draw(g);
        }
		for (Enemy enemy: enemyList) {
			enemy.draw(g);
		}
        // Board border lines
     	g.setColor(Color.black);
     	g.fillRect(0, 0, BOARD_START, PANEL_HEIGHT);
     	g.fillRect(BOARD_END, 0, PANEL_WIDTH, PANEL_HEIGHT);
     	g.fillRect(BOARD_START, BOARD_HEIGHT, BOARD_END, PANEL_HEIGHT);
     	
     	// Health-bar
     	gui.draw(g);
     	
     	// Dispose of the Graphics
     	g.dispose();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.requestFocusInWindow();
	}
}