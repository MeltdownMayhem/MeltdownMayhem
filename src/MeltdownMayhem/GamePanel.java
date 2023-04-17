package MeltdownMayhem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Entity.Ammunition;
import Entity.Barrel;
import Entity.Drone;
import Entity.Enemy;
import Entity.Human;
import Entity.Rage;

public class GamePanel extends JPanel{

	public static final int PANEL_WIDTH = 1940; // Panel sizes are for the window
	public static final int PANEL_HEIGHT =1080;
	public static final int BOARD_WIDTH = 1188; // Board sizes are for the playable area
	public static final int BOARD_HEIGHT = PANEL_HEIGHT-250;
	public static final int BOARD_START = 0;
			//(PANEL_WIDTH-BOARD_WIDTH)/2;
	public static final int BOARD_END = (PANEL_WIDTH-BOARD_WIDTH)/2 + BOARD_WIDTH;
	
	// Game variables
	boolean gameOver = false;
	public static int score = 0;
	public static int ammo = 100;
	public static int max_ammo = 100;
	public static int max_enemies = 12;
	public static int max_barrels = 3; // the max-variables aren't final, they will change when the difficulty rises
	
	// Technical variables
	private static final double barrelSpawnChance = 0.005;
	private static final double shootChance = 0.05;
	static int shootingCooldown = 0; // Start on 0, to shoot instantly when tapping
	static boolean shooting = false;
	KeyHandler keyH = new KeyHandler();
	public BufferedImage background1;
	public Image noCursor = Toolkit.getDefaultToolkit().createImage(
			new MemoryImageSource(16, 16, null, 0, 16));
	public Cursor transparentCursor =
			Toolkit.getDefaultToolkit().createCustomCursor(noCursor, getLocation(), "transparentCursor");
	//Credit to 'Réal Gagnon' for the code to hide the cursor
	
	// Objects and lists
	public static Human human = new Human();
	public static Drone drone = new Drone();
	public static GUI gui = new GUI();
	public static ArrayList<Ammunition> ammoList;
	public static ArrayList<Ammunition> projectileList; // Enemy ammo
	public static ArrayList<Ammunition> delProjectileList; // Enemy ammo to delete
	public static ArrayList <Enemy> enemyList;
	public static ArrayList <ArrayList<Enemy>> enemiesInCollision;
	public static ArrayList<Barrel> barrelList;
	
	public Random rng = new Random();

	public GamePanel() {
		// Panel Settings
		this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
		this.setBackground(new Color(215,215,215)); // Light-Gray
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		setCursor(transparentCursor);
		
		// ObjectLists creation
		barrelList = new ArrayList<Barrel>();
		enemyList = new ArrayList<Enemy>();
		enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		ammoList = new ArrayList<Ammunition>();
		projectileList = new ArrayList<Ammunition>();
		delProjectileList = new ArrayList<Ammunition>();
		
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
				checkGameOver();
				update();
				repaint();
			}
		}
	}
	//-----------------------------------ENEMY OPERATIONS-----------------------------------
	public void bulletInteractWithEnemy() {
		if (! enemyList.isEmpty()) {
			for(Ammunition bullet: ammoList) {
				int enemyIndexCounter = 0;
				for(Enemy enemy: enemyList) {
					enemy.checkBulletCollision(bullet, enemyIndexCounter);
					enemyIndexCounter += 1;
					if (Enemy.enemyKilled == true) {
						bullet.y = -1; //at y = -1 the bullet will automatically be removed
						break; //needed, or else enemyIndexCounter will get broken, restarts the for loop making it check on enemy #0 again
					}	
				}
				enemyIndexCounter += 1;
				Enemy.enemyKilled = false;
			}
		}
	}
	public void enemyFire() {
		for (Enemy e: enemyList) {
			if (e.shootCooldown > 150 && rng.nextDouble() < shootChance) {
				System.out.println("Shot!");
				e.aimAndShoot(e, human.x, human.y);
				e.shootCooldown = 0;
			}
		}
	}
	//-----------------------------------BARREL OPERATIONS-----------------------------------
	public void spawnBarrel() {
		if (rng.nextDouble() <= barrelSpawnChance && GamePanel.barrelList.size() < GamePanel.max_barrels) {
			GamePanel.barrelList.add(new Barrel());
		}
	}
	public void updateBarrel() {
		if (! barrelList.isEmpty()) {
			for (Barrel barrel:barrelList) {
				barrel.update();
			}
			if (barrelList.get(0).y > GamePanel.BOARD_HEIGHT + 30) {
				barrelList.remove(0);
			}
		}
	}
	//-----------------------------------BULLET OPERATIONS-----------------------------------
	public void shootBullet() {
		if (shooting == true && ammo > 0) {
			shootingCooldown--;
			if (shootingCooldown < 0) {
				ammo -= 1;
				ammoList.add(new Ammunition(human.x+human.width*76/100 - 2,human.y));
				shootingCooldown = 15;
			}
		}
	}
	public void updateBullet() {
		if (! ammoList.isEmpty()) {
			for (Ammunition ammo:ammoList) {
				ammo.update();
			}
			if (ammoList.get(0).y < 0) {
				ammoList.remove(0);
			}
		}
	}
	public void updateProjectile() {
	if (!projectileList.isEmpty()) {
		for (Ammunition p: projectileList) {
			p.update();
			if (p.out()) {
				delProjectileList.add(p);
			} else if (Extra.distance(p.x, p.y, (int)(human.x - 10 + human.width * 0.6), (int)(human.y + human.width * 1.15)) < human.width / 2 + p.width / 2) {
				human.loseLife();
				delProjectileList.add(p);
			}
		}
	}
	for (Ammunition a: delProjectileList) {
		projectileList.remove(a);
	}
	delProjectileList.clear();
	
}
	//-----------------------------------------------------------------------------------------
	
	public void update() {
		// Character Updates
		human.update();
		drone.update();	
		
		// Drone collision with Enemies
		try {
			drone.checkDroneCollision();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		// Enemy Spawning
		if (GamePanel.enemyList.size() == 0) {
			Event.spawnEnemy();
		} else if (GamePanel.enemyList.size() < GamePanel.max_enemies && rng.nextDouble() * Math.exp(GamePanel.score) >= Enemy.SPAWN_CHANCE) {
			Event.spawnEnemy();
		}
		for (Enemy enemy: enemyList) {
			enemy.HumanCollision();
		}
		// Enemy collision with Bullets from Human
		bulletInteractWithEnemy();	
		
		// Enemy Updates
		if (! enemyList.isEmpty()) {
			for (Enemy enemy:enemyList) {
				enemy.update();
			}
		}
		
		// Enemy collision with other Enemies
		/* 
		Algoritme voor botsingen: Enemies die binnen elkaars 'botsingsgebied' liggen komen in eenzelfde ArrayList.
		Nadien gaan alle ballen weg van het zwaartepunt van de enemies in elke ArrayList.
		*/
		enemiesInCollision = Event.getEnemiesInCollision();
		Event.avoidEnemyCollision(enemiesInCollision);

		// Human collision with Barrels
		for (Barrel barrel: barrelList) {
			barrel.checkBarrelCollision();
		}
		
		// Barrel Spawning
		spawnBarrel();
		
		// Barrel Updates
		updateBarrel();

		// Check for shooting Bullet
		shootBullet();
		
		// Bullet Updates
		updateBullet();
		
		//enemy shoot
		enemyFire();
				
		// Update Enemy ammo + remove if outside board + check if hit Human
		updateProjectile();
	}
	
	public void checkGameOver() {
		if (human.lives == 0) {
			gameOver = true;
			System.out.println("GAME OVER!");
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
		
		// Draw Ammunition, Characters and Barrels (Order of drawing is important!)
		for (Ammunition ammo:ammoList) {
			ammo.draw(g);
        }
		human.draw(g);
		
		for (Ammunition p:projectileList) {
			p.draw(g);
		}
		
		for (Barrel barrel:barrelList) {
			barrel.draw(g);
        }
		for (Enemy enemy: enemyList) {
			enemy.draw(g);
		}
		drone.draw(g);
		
		// GUI
     	gui.draw(g);
		
        // Board border lines (To hide barrels, bullets etc. when out of GameBoard)
     	g.setColor(Color.black);
     	g.fillRect(0, 0, BOARD_START, PANEL_HEIGHT);
     	g.fillRect(BOARD_END, 0, PANEL_WIDTH, PANEL_HEIGHT);
     	g.fillRect(BOARD_START, BOARD_HEIGHT, BOARD_END, PANEL_HEIGHT);
     	
     	// Disposal of the Graphics (Performance-related)
     	g.dispose();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.requestFocusInWindow();
	}
}