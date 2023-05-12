package MeltdownMayhem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Entity.*;
/**
 * Class for Panel creation to add to the main Frame.
 * It contains the main Game-Loop (Update and Paint).
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener{
	
	// Game State
	public enum State{PLAY, PAUSE, GAMEOVER};
	static State gameState = State.PLAY;

	public enum droneKiller{ORB, SNIPER, RAGE, RAMPAGE, ORBBULLET, SNIPERBULLET};
	private droneKiller deathDrone;
	
	// Basic Game variables
	public int score, level;
	private final int scoreLevel1 = 500;
	private final int scoreLevel2 = 1500;
	private String nameHuman, nameDrone, chatText;
	
	private int max_enemies, max_barrels;
	private double enemySpawnChance = 0.005;
	private double barrelSpawnChance = 0.004;
	private Random rng = new Random();
	
	private Window window;
	private UpdateTimerTask updateTimerTask;
	
	// Hiding the Cursor <Credits to 'Réal Gagnon' | https://www.rgagnon.com/javadetails/java-0440.html>
	public Image noCursor;
	public Cursor transparentCursor;
	
	// Objects
	Human human;
	Drone drone;
	Portal portal;
	GUI gui;
	KeyHandler keyH;
	
	// Creation of Lists
	public ArrayList<Ammunition> ammoList;
	public ArrayList<Ammunition> projectileList; // enemyAmmoList
	public ArrayList<Ammunition> delProjectileList; // toDeleteEnemyAmmoList
	public ArrayList<PowerUp> powerUpList;
	public ArrayList<Barrel> barrelList;
	public ArrayList<Enemy> enemyList; // All different Enemy types in 1 list
	public ArrayList<ArrayList<Enemy>> enemiesInCollision;
	public ArrayList<String> chatList;
	public ArrayList<Integer> chatTimer;
	
	private JLabel ESC, endScore;
	public JTextArea chat;
	
	public JButton resumeBut, ragequitBut, backToMenuBut;
	private ImageIcon resumeIcon, ragequitIcon, backToMenuIcon;

	private BufferedImage cave, city, powerplant, background_end;
	private BufferedImage gamePaused, gameOver;
	
	protected GamePanel(String nameHuman, String nameDrone, Window window) {
		
		// Basic Game Variables
		this.score = 0;
		this.level = 1;
		this.max_enemies = 7;
		this.max_barrels = 3;
		
		// Creation of Objects
		human = new Human();
		drone = new Drone();
		gui = new GUI();
		keyH = new KeyHandler(human, drone, this);
		
		// Basic Panel settings
		this.setLayout(null);
		this.setPreferredSize(Window.screenSize);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addMouseListener(keyH);
		this.setFocusable(true);
		this.setCursor(transparentCursor);
		
		this.nameHuman = nameHuman;
		this.nameDrone = nameDrone;
		this.window = window;
		
		// List initializations
		ammoList = new ArrayList<Ammunition>();
		projectileList = new ArrayList<Ammunition>();
		delProjectileList = new ArrayList<Ammunition>();
		powerUpList = new ArrayList<PowerUp>();
		barrelList = new ArrayList<Barrel>();
		enemyList = new ArrayList<Enemy>();
		enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		chatList = new ArrayList<String>();
		chatTimer = new ArrayList<Integer>();
		
		// Hiding the Cursor <Credits to 'Réal Gagnon' | https://www.rgagnon.com/javadetails/java-0440.html>
		noCursor = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, null, 0, 16));
		transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(noCursor, getLocation(), "transparentCursor");
		
		// Images
		resumeIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/button/resume.png")).getImage().getScaledInstance(400, 80, ABORT));
		backToMenuIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/button/backToMenu.png")).getImage().getScaledInstance(400, 80, ABORT));
		ragequitIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/button/ragequit.png")).getImage().getScaledInstance(400, 80, ABORT));

		// JButtons <Credits to Bro Code | https://www.youtube.com/watch?v=-IMys4PCkIA>
		resumeBut = new JButton(resumeIcon);
		resumeBut.setBounds(Window.screenSize.width / 2 - 200, 400, 400, 80);
		resumeBut.setBorder(BorderFactory.createEmptyBorder());
		resumeBut.addActionListener(this);
		resumeBut.setVisible(false);
		resumeBut.setContentAreaFilled(false);
		this.add(resumeBut);
		
		backToMenuBut = new JButton(backToMenuIcon);
		backToMenuBut.setBounds(Window.screenSize.width / 2 - 200, 500, 400, 80);
		backToMenuBut.setBorder(BorderFactory.createEmptyBorder());
		backToMenuBut.addActionListener(this);
		backToMenuBut.setVisible(false);
		backToMenuBut.setContentAreaFilled(false);
		this.add(backToMenuBut);
		
		ragequitBut = new JButton(ragequitIcon);
		ragequitBut.setBounds(Window.screenSize.width / 2 - 200, 600, 400, 80);
		ragequitBut.setBorder(BorderFactory.createEmptyBorder());
		ragequitBut.addActionListener(this);
		ragequitBut.setVisible(false);
		ragequitBut.setContentAreaFilled(false);
		this.add(ragequitBut);
		
		// JLabels
		ESC = new JLabel ("Press ESC to pause");
		ESC.setBounds(Window.BOARD_END - 235, 5, 235, 25);
		ESC.setFont(new Font("American TypeWriter", Font.PLAIN, 25));
		ESC.setForeground(Color.white);
		this.add(ESC);
		
		endScore = new JLabel();
		endScore.setBounds(Window.screenSize.width / 2 - 200, 370, 400, 40);
		endScore.setHorizontalAlignment(JLabel.CENTER);
		endScore.setFont(new Font("Times New Roman", Font.BOLD, 40));
		endScore.setForeground(Color.white);
		endScore.setVisible(false);
		this.add(endScore);
		
		// Create chat (JTextArea)
		chat = new JTextArea("");
		chat.setLayout(null);
		chat.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Credits to Craig Wood for explaining how to align text inside a JTextArea: https://coderanch.com/t/339752/java/Textarea-Text-Alignment
		chat.setOpaque(false);
		chat.setFocusable(false);
		chat.setBounds(Window.BOARD_END - 510, Window.BOARD_HEIGHT - 195, 500, 190);
		chat.setFont(new Font("American Typewriter", Font.PLAIN, 16));
		chat.setForeground(Color.CYAN);
		chat.setEditable(false);
		this.add(chat);
		
		// Drone start position
		try {
			drone.teleportMouse(Window.screenSize.width/2 - drone.width/2 + 128, Window.BOARD_HEIGHT - drone.height - Window.BOARD_HEIGHT/15);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		// FPS
		Timer t = new Timer();
		updateTimerTask = new UpdateTimerTask();
		t.scheduleAtFixedRate(updateTimerTask, 0, 20);
		
		// Retrieving the background image
		try {
			cave = ImageIO.read(getClass().getResourceAsStream("/background/cave.png"));
			city = ImageIO.read(getClass().getResourceAsStream("/background/city.png"));
			powerplant = ImageIO.read(getClass().getResourceAsStream("/background/powerplant.png"));
			background_end = ImageIO.read(getClass().getResourceAsStream("/background/paperBackground.png"));
			gamePaused = ImageIO.read(getClass().getResourceAsStream("/gui/gamePaused.png"));
			gameOver = ImageIO.read(getClass().getResourceAsStream("/gui/gameOver.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
        // <Credits to docs.oracle for showing how to use an actionListener | https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html>
		Extra.playSound("SFX/Button.wav");
		if (e.getSource() == resumeBut) {
        	gameState = State.PLAY;
        	resumeBut.setVisible(false);
        	ragequitBut.setVisible(false);
        	backToMenuBut.setVisible(false);
        	this.setCursor(transparentCursor);
        	
			try {
				drone.teleportMouse(drone.x, drone.y);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
        } else if (e.getSource() == ragequitBut) {
        	System.exit(0);  // <Credits to JavaGuides for showing how to close an application | https://www.javaguides.net/2019/06/java-swing-exit-button.html>
        } else if(e.getSource() == backToMenuBut) {
        	gameState = State.PAUSE;
        	window.switchPanel(window.startPanel, window.gamePanel);
        	window.gamePanel.removeAll();
        	updateTimerTask.cancel();
        }		
	}
	
	
	public class UpdateTimerTask extends TimerTask{
		@Override
		public void run() {
			if (gameState == State.PLAY) {
				checkGameOver();
				update();
			} else if (gameState == State.PAUSE) {
				if (drone.droneFrozen == true) { // zorgt dat drone frozen blijft na een hit, om misbruik van pauze te vermijden
					drone.freeze(2000);
				}
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			repaint();
		}
	}
	
	public void checkGameOver() {
		if (human.lives == 0) {
			gameState = State.GAMEOVER;
			Extra.playSound("SFX/GameOverVoice.wav");
			ragequitBut.setVisible(true);
			backToMenuBut.setVisible(true);
			endScore.setText("YOUR SCORE: " + score);
			endScore.setVisible(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	//-------------------------------------UPDATE-------------------------------------
	public void update() {
		// Set Cursor again to transparent
		if (gameState == State.PLAY) {
			this.setCursor(transparentCursor);
		}
		
		// Spawn Portal
		if (score >= scoreLevel1 && portal == null && level == 1) {
			portal = new Portal();
			chatList.add("Pass through the portal to reach Level 2");
			chatTimer.add(0);
		} else if (score >= scoreLevel2 && portal == null && level == 2) {
			portal = new Portal();
			chatList.add("Pass through the portal to reach Level 3");
			chatTimer.add(0);
		}
		
		// Check collision with portal
		if (portal != null) {
			if (portal.portalCollision(human)) {
				enemyList.clear();
				barrelList.clear();
				projectileList.clear();
				drone.barrelSlot = null;
				level++;
				portal = null;
				chatTimer.add(0);
				human.x = Window.screenSize.width/2 - human.width/2 - 128;
				human.y = Window.BOARD_HEIGHT - human.height - Window.BOARD_HEIGHT/15;
				if (level == 2) {
					chatList.add("You reached Level 2");
					max_barrels = 4;
					max_enemies = 9;
				} else if (level == 3) {
					chatList.add("You reached Level 3");
					max_barrels = 5;
					max_enemies = 11;
				}
			}
		}
		
		// Entity Spawning (only when no portal is on the map)
		if (portal == null) {
			if (enemyList.size() == 0 || (enemyList.size() == 1 && level == 3)) {
				enemyList = Enemy.spawnEnemy(enemyList, level);
			} else if(enemyList.size() < max_enemies){
				if (level == 1) {
					if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + score / 125000) {
						enemyList = Enemy.spawnEnemy(enemyList, level);
					}
				} else if(level == 2) {
					if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + (score-250) / 100000) {
						enemyList = Enemy.spawnEnemy(enemyList, level);
					}
				} else {
					if (rng.nextDouble() / (1 + ((max_enemies - enemyList.size())/max_enemies)) <= enemySpawnChance + (Math.log10((score-1500)/100 + 1))/150) {
						enemyList = Enemy.spawnEnemy(enemyList, level);
					}
				}
			}
			
			if (barrelList.size() < max_barrels) {
				if (level == 1) {
					if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance + score/200000) {
						barrelList.add(new Barrel());
					}
				} else if(level == 2){ 
					if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels)) <= barrelSpawnChance + (score-250) / 200000) {
						barrelList.add(new Barrel());
					}
				} else	{
					if (rng.nextDouble() / (1 + ((max_barrels - barrelList.size())/max_barrels/1.5))<= barrelSpawnChance + Math.log10((score-1500)/100 + 1)/300) {
						// / (1 + ((max_barrels - barrelList.size())/max_barrels/2))
						barrelList.add(new Barrel());
					}
				}
			}
		}
		
		// Human
		human.update();
		delProjectileList = human.humanCollisions(nameHuman, chatList, chatTimer, enemyList, barrelList, projectileList, delProjectileList);
		
		// Drone
		drone.update();	
		try {
			deathDrone = drone.droneCollisions(nameDrone, enemyList, projectileList, score);
			if (deathDrone != null) {
				// Remove score for Drone dying
				if (score > 20) {
					score -= 20;
				} else {
					score = 0;
				}
				// Add death message for death Drone
				if (deathDrone == droneKiller.ORB) {
					chatList.add(nameDrone + " should learn about the dangers of radiation");
				} else if (deathDrone == droneKiller.SNIPER) {
					chatList.add(nameDrone + " should learn about the dangers of radiation");
				} else if (deathDrone == droneKiller.RAMPAGE) {
					chatList.add(nameDrone + " wasn't fast enough");
				} else if (deathDrone == droneKiller.RAGE) {
					chatList.add(nameDrone + " should learn about the dangers of radiation");
				} else if (deathDrone == droneKiller.ORBBULLET) {
					chatList.add(nameDrone + " was hit by a radiation orb");
				} else if (deathDrone == droneKiller.SNIPERBULLET) {
					chatList.add(nameDrone + " was hit by a radiation sniper");
				}
				chatTimer.add(0);
				
			}
			deathDrone = null;
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
		if (!barrelList.isEmpty() && drone.damageBarrel == true) {
			for (Barrel barrel: barrelList) {
				drone.destroyBarrel(barrel, barrelList, drone, this);
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
		if (!barrelList.isEmpty() && barrelList.get(0).y > Window.BOARD_HEIGHT + 30) {
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
			chatList.remove(0);
		}
		for (int i = 0; i < chatTimer.size(); i++) {
			chatTimer.set(i, chatTimer.get(i) + 1);
		}
		chatText = "";
		for (int i = 0; i < 8 - chatList.size(); i++) {
			chatText += "\n";
		}
		for (String s: chatList) {
			chatText += "\n" + s;
		}
		chat.setText(chatText);
		
		//Block score until in new Level
		if (score != scoreLevel1 && portal != null && level == 1) {
			score = scoreLevel1;
		} else if (score != scoreLevel2 && portal != null && level == 2) {
			score = scoreLevel2;
		} else if (level == 2 && score < scoreLevel1) {
			score = scoreLevel1;
		} else if (level == 3 && score < scoreLevel2) {
			score = scoreLevel2;
		}
	}
	
	
	//-------------------------------------PAINT-------------------------------------
	public void paintComponent(Graphics g) { // Order of drawing is important for overlapping
		super.paintComponent(g);
		if (level == 1) {
			g.drawImage(cave, Window.BOARD_START, 0, 1198, Window.BOARD_HEIGHT, null);
		}
		if (level == 2) {
			g.drawImage(city, Window.BOARD_START, 0, 1198, Window.BOARD_HEIGHT, null);
		}
		if (level == 3) {
			g.drawImage(powerplant, Window.BOARD_START, 0, 1198, Window.BOARD_HEIGHT, null);
		}

		if (portal != null) {
			portal.draw(g);
		}
		
		
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
     	g.fillRect(0, 0, Window.BOARD_START, Window.screenSize.height);
     	g.fillRect(Window.BOARD_END, 0, Window.screenSize.width, Window.screenSize.height);
     	g.fillRect(Window.BOARD_START, Window.BOARD_HEIGHT, Window.BOARD_END, Window.screenSize.height);
     	
     	if (gameState == State.PAUSE) {
     		g.drawImage(background_end, Window.screenSize.width / 2 - 300, 75, 600, 900, null);
     		g.drawImage(gamePaused, Window.screenSize.width / 2 - 240, 250, 480, 60, null);
     	} else if (gameState == State.GAMEOVER) {
     		g.drawImage(background_end, Window.screenSize.width / 2 - 300, 75, 600, 900, null);
     		g.drawImage(gameOver, Window.screenSize.width / 2 - 240, 250, 480, 60, null);
     	}
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.requestFocusInWindow();
	}
}