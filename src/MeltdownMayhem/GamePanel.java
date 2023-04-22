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

import Entity.*;
/**
 * Dit is de belangrijkste classe van het project, met het grootste deel van de mechanismen.
 * Ten eerste wordt hier de Panel aangemaakt en bewerkt.
 * Verder bevat het ook de Game-Loop (Update en Draw).
 * Ten slotte bevinden de meeste collisions zich nog in deze classe.
 * Er zijn plannen om dit aan te passen en de collisions op een 'efficientere' manier ergens bij te houden (zoals echte hitboxes gebruiken).
 */
public class GamePanel extends JPanel{

	// Window settings
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int BOARD_WIDTH = 1188; // Board refers to the playable area
	public static final int BOARD_HEIGHT = screenSize.height - 30;
	public static final int BOARD_START = (screenSize.width - BOARD_WIDTH)/2;
	public static final int BOARD_END = (screenSize.width - BOARD_WIDTH)/2 + BOARD_WIDTH;
	
	// Game State
	static enum Phase{START, PLAY, PAUSE, GAMEOVER};
	static Phase phaseOfGame = Phase.PLAY;
	
	// Basic Game variables
	public int score = 0;
	public int max_enemies = 13;
	public int max_barrels = 5;
	
	// Technical variables
	private double enemySpawnChance = 0.005;
	private double barrelSpawnChance = 0.005;
	
	// Hiding the Cursor <Credits to 'Réal Gagnon'>
	public Image noCursor = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, null, 0, 16));
	public Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(noCursor, getLocation(), "transparentCursor");
	
	// Creation of Objects
	public Human human = new Human();
	public Drone drone = new Drone();
	
	GUI gui = new GUI();
	KeyHandler keyH = new KeyHandler(human, drone);
	Random rng = new Random();
	BufferedImage background1;
	
	// Creation of Lists
	public ArrayList<Ammunition> ammoList;
	public ArrayList<Ammunition> projectileList; // enemyAmmoList
	public ArrayList<Ammunition> delProjectileList; // toDeleteEnemyAmmoList
	public ArrayList<Barrel> barrelList;
	public ArrayList<Enemy> enemyList; // All different Enemy types in 1 list
	public ArrayList<ArrayList<Enemy>> enemiesInCollision;
	
	public GamePanel() {
		// Basic Panel settings
		this.setPreferredSize(screenSize);
		this.setBackground(new Color(215,215,215)); // Light-Gray
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		setCursor(transparentCursor);
		
		// List initializations
		ammoList = new ArrayList<Ammunition>();
		projectileList = new ArrayList<Ammunition>();
		delProjectileList = new ArrayList<Ammunition>();
		barrelList = new ArrayList<Barrel>();
		enemyList = new ArrayList<Enemy>();
		enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		
		// FPS
		Timer t = new Timer();
		t.scheduleAtFixedRate(new UpdateTimerTask(), 0, 20);
		
		// Retrieving the background image
		try {
			background1 = ImageIO.read(getClass().getResourceAsStream("/background/background1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class UpdateTimerTask extends TimerTask{
		@Override
		public void run() {
			if (phaseOfGame == Phase.PLAY) {
				checkGameOver();
				update();
				setCursor(transparentCursor);
			}
			else if (phaseOfGame == Phase.PAUSE) {
				if (drone.droneFrozen == true) {
				drone.freeze(2000);
				}
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			repaint();
		}
	}
	
	public void checkGameOver() {
		if (human.lives == 0) {
			phaseOfGame = Phase.GAMEOVER;
			System.out.println("GAME OVER!");
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	//-------------------------------------UPDATE-------------------------------------
	public void update() {
		// Entity Spawning
		if (enemyList.size() == 0) {
			Enemy.spawnEnemy(enemyList, this);
		} else if (enemyList.size() < max_enemies && rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance) {
			Enemy.spawnEnemy(enemyList, this);
		}
		if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance && barrelList.size() < max_barrels) {
			barrelList.add(new Barrel());
		}
		
		// Human
		human.update();
		human.humanCollisions(enemyList, barrelList, projectileList, this);
		
		// Drone
		drone.update();	
		try {
			drone.droneCollisions(enemyList, projectileList, this);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		// Enemy
		if (!enemyList.isEmpty()) {
			ArrayList<Enemy> killedEnemyList = new ArrayList<>();
			for (Enemy enemy: enemyList) {
				enemy.update(human);
				if (enemy.enemyCollisions(enemyList, ammoList, this)) {
					killedEnemyList.add(enemy);
				}
			}
			for (Enemy enemy: killedEnemyList) {
				enemyList.remove(enemy);
			}
			killedEnemyList.clear();
		}
		
		// Enemy collision with other Enemies
		enemiesInCollision = Event.getEnemiesInCollision(enemyList);
		Event.avoidEnemyCollision(enemiesInCollision);
		/* Botsing-algoritme: Enemies in elkaars 'botsingsgebied' komen samen in een ArrayList.
			Nadien worden de enemies weggeduuwt van hun zwaartepunt, voor elke gevormde ArrayList.*/
		
		// Barrel
		for (Barrel barrel:barrelList) {
			barrel.update();
			barrel.barrelCollisions(barrelList, ammoList);
		}
		if (!barrelList.isEmpty() && barrelList.get(0).y > BOARD_HEIGHT + 30) {
			barrelList.remove(0);
		}

		// Human Ammo
		human.shootBullet(ammoList);
		
		for (Ammunition ammo:ammoList) {
			ammo.update();
		}
		if (!ammoList.isEmpty() && ammoList.get(0).y < 0) {
			ammoList.remove(0);
		}
		
		
		// Enemy Ammo
		for (Enemy enemy: enemyList) {
			enemy.shootBullet(projectileList, human);
		}
		for (Ammunition bullet: projectileList) {
			bullet.update();
			bullet.ammoCollisions(ammoList, this);
			if (bullet.isOutBoard()) {
				delProjectileList.add(bullet);
			}
		}
		for (Ammunition a: delProjectileList) {
			projectileList.remove(a);
		}
		delProjectileList.clear();
	}
	
	//-------------------------------------PAINT-------------------------------------
	public void paintComponent(Graphics g) { // Order of drawing is important for overlapping
		super.paintComponent(g);
		
		// Background (Very Temporary)
		g.drawImage(background1, BOARD_START, 0, 547, 555, null);
		g.drawImage(background1, BOARD_START + 546, 0, 547, 555, null);
		g.drawImage(background1, BOARD_START + 1092, 0, 547, 555, null);
		g.drawImage(background1, BOARD_START, 553, 547, 555, null);
		g.drawImage(background1, BOARD_START + 546, 553, 547, 555, null);
		g.drawImage(background1, BOARD_START + 1092, 553, 547, 555, null);
		
		// Draw Entities
		for (Ammunition ammo:ammoList) {
			ammo.draw(g);
        }
		human.draw(g);
		
		for (Barrel barrel:barrelList) {
			barrel.draw(g);
        }
		for (Ammunition p:projectileList) {
			p.draw(g);
		}
		for (Enemy enemy: enemyList) {
			enemy.draw(g);
		}
		drone.draw(g);
		
		// GUI
     	gui.draw(g, human, this);
		
        // Board border lines (To hide barrels, bullets etc. when out of GameBoard)
     	g.setColor(Color.black);
     	g.fillRect(0, 0, BOARD_START, screenSize.height);
     	g.fillRect(BOARD_END, 0, screenSize.width, screenSize.height);
     	g.fillRect(BOARD_START, BOARD_HEIGHT, BOARD_END, screenSize.height);
     	
     	// Disposal of the Graphics (Performance-related)
     	g.dispose();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.requestFocusInWindow();
	}
}