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
	public static final int max_enemies = 12;
	public static final int max_barrels = 3;
	
	// Technical variables
	private static final double SPAWN_CHANCE = 0.995;
	static int shootingCooldown = 0; // Start on 0, to shoot instantly when tapping
	static boolean shooting = false;
	KeyHandler keyH = new KeyHandler();
	public BufferedImage background1;
	public Image noCursor = Toolkit.getDefaultToolkit().createImage(
			new MemoryImageSource(16, 16, null, 0, 16));
	public Cursor transparentCursor =
			Toolkit.getDefaultToolkit().createCustomCursor(noCursor, getLocation(), "transparentCursor");
	//Credit to Réal Gagnon for code on how to hide the cursor
	// Objects and lists
	public static Human human = new Human();
	public static Drone drone = new Drone();
	public static GUI gui = new GUI();
	public static ArrayList<Ammunition> ammoList;
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
				checkBarrelCollision();
				try {
					checkDroneCollision();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
				
				
				
			}
		}
	}
	
	public void update() {
		// Update Characters
		human.update();
		//if (dronefreeze == false) {
		drone.update();
		//}
		// Update Enemies
		if (! enemyList.isEmpty()) {
			for (Enemy enemy:enemyList) {
				enemy.update();
			}
		}
		
		if (GamePanel.enemyList.size() < GamePanel.max_enemies && rng.nextDouble() * Math.exp(GamePanel.score) >= SPAWN_CHANCE) {
			Event.spawnEnemy();
		}
		
		enemiesInCollision = Event.getEnemiesInCollision();
		Event.avoidEnemyCollision(enemiesInCollision);
		
		// Chance to add Barrels to the Game
		Random rng = new Random();
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
	}
	
	public void checkBarrelCollision() {
		for (Barrel barrel:barrelList) {
			if (human.x > barrel.x - barrel.width*3/4 && human.x < barrel.x + barrel.width && human.y < barrel.y + barrel.height && human.y > barrel.y) {
				human.lives -= 1;		//3/4 factor is because of the human hitbox
				human.x = PANEL_WIDTH/2 - human.width/2;
				human.y = BOARD_HEIGHT - human.depth - BOARD_HEIGHT/15;
				if (human.lives == 0) {
					gameOver = true;
					System.out.println("GAME OVER!");
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}
	
	public void checkDroneCollision() throws AWTException{ //needed for Robot class
		final int DroneRespawnX = PANEL_WIDTH/2- drone.width/2 + 128;
		final int DroneRespawnY = BOARD_HEIGHT- drone.depth-BOARD_HEIGHT/15;
		for (Enemy enemy: enemyList) {
			if(drone.x > enemy.x - enemy.enemyRadius && drone.x < enemy.x +enemy.enemyRadius && drone.y > enemy.y - enemy.enemyRadius && drone.y < enemy.y + enemy.enemyRadius) {
				drone.lives -= 1;
			if (drone.lives == 0) {
				Robot robot = new Robot();
				robot.mouseMove(DroneRespawnX, DroneRespawnY);
				drone.lives = 1;
					}
			}
				
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
		
		// Draw Ammunition, Characters and Barrels
		for (Ammunition ammo:ammoList) {
			ammo.draw(g);
        }
		
		human.draw(g);
		
		for (Barrel barrel:barrelList) {
			barrel.draw(g);
        }
		for (Enemy enemy: enemyList) {
			enemy.draw(g);
		}
		drone.draw(g); //drone needs to be on top of barrels etc.
		
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