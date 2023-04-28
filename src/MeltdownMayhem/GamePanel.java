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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Entity.*;
/**
 * Class for Panel creation to add to the main Frame.
 * It contains the main Game-Loop (Update and Paint).
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel{

	// Window settings
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int BOARD_WIDTH = 1188; // Board refers to the playable area
	public static final int BOARD_HEIGHT = screenSize.height - 80;
	public static final int BOARD_START = (screenSize.width - BOARD_WIDTH)/2;
	public static final int BOARD_END = (screenSize.width - BOARD_WIDTH)/2 + BOARD_WIDTH;
	
	// Game State
	static enum Phase{START, PLAY, PAUSE, GAMEOVER};
	static Phase phaseOfGame = Phase.PLAY;
	
	public int level = 1;
	
	// Basic Game variables
	public int score = 2000;
	public int max_enemies = 8;
	public int max_barrels = 3;
	
	// Technical variables
	private double enemySpawnChance = 0.005;
	private double barrelSpawnChance = 0.004;
	
	// Hiding the Cursor <Credits to 'Réal Gagnon'>
	public Image noCursor = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, null, 0, 16));
	public Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(noCursor, getLocation(), "transparentCursor");
	
	// Creation of Objects
	public Human human = new Human();
	public Drone drone = new Drone();
	public String nameHuman, nameDrone;
	
	GUI gui = new GUI();
	KeyHandler keyH = new KeyHandler(human, drone, this);
	Random rng = new Random();
	BufferedImage background1;
	
	// Creation of Lists
	public ArrayList<Ammunition> ammoList;
	public ArrayList<Ammunition> projectileList; // enemyAmmoList
	public ArrayList<Ammunition> delProjectileList; // toDeleteEnemyAmmoList
	public ArrayList<PowerUp> powerUpList;
	public ArrayList<Barrel> barrelList;
	public ArrayList<Enemy> enemyList; // All different Enemy types in 1 list
	public ArrayList<ArrayList<Enemy>> enemiesInCollision;
	
	public JTextArea chat;
	protected String chatString;
	public ArrayList<String> chatText;
	public ArrayList<Integer> chatTimer;
	
	//public String chatText = "";
	
	public GamePanel(Window mainVenster, String nameHuman, String nameDrone) {
		
		this.nameHuman = nameHuman;
		this.nameDrone = nameDrone;
		
		// Basic Panel settings
		this.setLayout(null);
		this.setPreferredSize(screenSize);
		this.setBackground(new Color(215,215,215)); // Light-Gray
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addMouseListener(keyH);
		this.setFocusable(true);
		this.setCursor(transparentCursor);
		
		// Create chat
		chat = new JTextArea("");
		chat.setLayout(null);
		chat.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		// Credits to Craig Wood for explaining how to align text inside a JTextArea: https://coderanch.com/t/339752/java/Textarea-Text-Alignment
		chat.setOpaque(false);
		chat.setBounds(BOARD_END - 510, BOARD_HEIGHT - 185, 500, 160);
		chat.setFont(new Font("American Typewriter", Font.PLAIN, 16));
		chat.setForeground(Color.white);
		this.add(chat);
		
		// List initializations
		ammoList = new ArrayList<Ammunition>();
		projectileList = new ArrayList<Ammunition>();
		delProjectileList = new ArrayList<Ammunition>();
		powerUpList = new ArrayList<PowerUp>();
		barrelList = new ArrayList<Barrel>();
		enemyList = new ArrayList<Enemy>();
		enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		chatText = new ArrayList<String>();
		chatTimer = new ArrayList<Integer>();
		
		// Drone start position
		try {
			drone.teleportMouse(GamePanel.screenSize.width/2 - drone.width/2 + 128, GamePanel.BOARD_HEIGHT - drone.height - GamePanel.BOARD_HEIGHT/15);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
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
			//System.out.println(chatText);
			if (phaseOfGame == Phase.PLAY) {
				checkGameOver();
				update();
			} else if (phaseOfGame == Phase.PAUSE) {
				if (drone.droneFrozen == true) { //zorgt dat drone frozen blijft na een hit, om misbruik van pauze te vermijden
					drone.freeze(2000); // WTF?? Veel te cheap!
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
		///////////////////////////////
		if (score > 2000) {
			level = 3;
			max_barrels = 5;
			max_enemies = 14;
		} else if(score > 1000) {
			level = 2;
			max_barrels = 4;
			max_enemies = 11;
		}
		
		/////////////////////
		
		// Entity Spawning
		if (enemyList.size() == 0) {
			Enemy.spawnEnemy(enemyList, this);
		} else if(enemyList.size() < max_enemies){
			if (level == 1) {
				if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + score / 150000) {
					Enemy.spawnEnemy(enemyList, this);
				}
			} else if(level == 2) {
				if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + (score-500) / 100000) {
					Enemy.spawnEnemy(enemyList, this);
				}
			} else {
				if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + (Math.log10((score-2000)/100 + 1))/150) {
					Enemy.spawnEnemy(enemyList, this);
				}
			}
		}
		
		if (barrelList.size() < max_barrels) {
			if (level == 1) {
				if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance + score/300000) {
					barrelList.add(new Barrel(drone, this));
				}
			} else if(level == 2){ 
				if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance + (score-500) / 200000) {
					barrelList.add(new Barrel(drone, this));
				}
			} else	{
				if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance + (1 + Math.log10((score-2000)/100 + 1))/300) {
					barrelList.add(new Barrel(drone, this));
				}
			}
		}

//		if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance && barrelList.size() < max_barrels) {
//			barrelList.add(new Barrel(drone, this));
//		}
		
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
		if (!barrelList.isEmpty() && drone.damageBarrel == true) {
			for (Barrel barrel: barrelList) {
				drone.destroyBarrel(barrel, barrelList, this);
			}
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
		
		// Barrel
		for (Barrel barrel:barrelList) {
			barrel.update();
			barrel.barrelCollisions(barrelList, ammoList);
		}
		if (!barrelList.isEmpty() && barrelList.get(0).y > BOARD_HEIGHT + 30) {
			barrelList.remove(0);
		}
		
		// PowerUp
		ArrayList<PowerUp> deletedPowerUpList = new ArrayList<PowerUp>();
		for (PowerUp powerUp: powerUpList) {
			powerUp.update(drone, human, this);
			if (powerUp.x <= 0 && powerUpList.size() > 1) {
				deletedPowerUpList.add(powerUp);
			}
		}
		for (PowerUp powerUp: deletedPowerUpList) {
			powerUpList.remove(powerUp);
		}
		deletedPowerUpList.clear();
		
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
			if (enemy instanceof RadiationOrb) {
				RadiationOrb orb = (RadiationOrb) enemy;
				orb.shootBullet(projectileList, human);
			}
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
		
		// update timer chat
		if (chatTimer.size() > 0 && chatTimer.get(0) > 250) {
			chatTimer.remove(0);
			chatText.remove(0);
		}
		for (int i = 0; i < chatTimer.size(); i++) {
			chatTimer.set(i, chatTimer.get(i) + 1);
		}
		chatString = "";
		for (int i = 0; i < 8 - chatText.size(); i++) {
			chatString += "\n";
		}
		for (String s: chatText) {
			chatString += "\n" + s;
		}
		chat.setText(chatString);
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
		for (PowerUp powerUp: powerUpList) {
			if (powerUp.pickedUp == false) {
				powerUp.draw(g);
			}
		}
		for (Barrel barrel:barrelList) {
			barrel.draw(g);
        }
		for (PowerUp powerUp: powerUpList) {
			if (powerUp.pickedUp == true) {
				powerUp.draw(g);
			}
		}
		for (Ammunition ammo:ammoList) {
			ammo.draw(g);
		}
		human.draw(g);
		
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
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.requestFocusInWindow();
	}
}