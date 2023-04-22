package MeltdownMayhem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Entity.Human;
import MeltdownMayhem.GamePanel.Phase;
/**
 * De GUI class zorgt voor de UI-Elements van de Board.
 * Het zorgt momenteel voor de Health-bar en de Ammo-bar, maar later ook de score_display, respawn_time, etc.
 */
public class GUI {

	BufferedImage heart1;
	BufferedImage heart2;
	
	BufferedImage bulletBar;
	BufferedImage ammoBar;
	
	BufferedImage gamePaused;
	
	BufferedImage score;
	BufferedImage number0;
	BufferedImage number1;
	BufferedImage number2;
	BufferedImage number3;
	BufferedImage number4;
	BufferedImage number5;
	BufferedImage number6;
	BufferedImage number7;
	BufferedImage number8;
	BufferedImage number9;
	
	List<BufferedImage> numberList = new ArrayList<BufferedImage>();
	
	public GUI() {
		try {
			// Retrieve the images for the GUI <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
			heart1 = ImageIO.read(getClass().getResourceAsStream("/gui/heart1.png"));
			heart2 = ImageIO.read(getClass().getResourceAsStream("/gui/heart2.png"));
			
			bulletBar = ImageIO.read(getClass().getResourceAsStream("/gui/bulletBar.png"));
			ammoBar = ImageIO.read(getClass().getResourceAsStream("/gui/ammoBar.png"));
			
			gamePaused = ImageIO.read(getClass().getResourceAsStream("/gui/gamePaused.png"));
			
			score = ImageIO.read(getClass().getResourceAsStream("/gui/score.png"));
			number0 = ImageIO.read(getClass().getResourceAsStream("/gui/number0.png"));
			number1 = ImageIO.read(getClass().getResourceAsStream("/gui/number1.png"));
			number2 = ImageIO.read(getClass().getResourceAsStream("/gui/number2.png"));
			number3 = ImageIO.read(getClass().getResourceAsStream("/gui/number3.png"));
			number4 = ImageIO.read(getClass().getResourceAsStream("/gui/number4.png"));
			number5 = ImageIO.read(getClass().getResourceAsStream("/gui/number5.png"));
			number6 = ImageIO.read(getClass().getResourceAsStream("/gui/number6.png"));
			number7 = ImageIO.read(getClass().getResourceAsStream("/gui/number7.png"));
			number8 = ImageIO.read(getClass().getResourceAsStream("/gui/number8.png"));
			number9 = ImageIO.read(getClass().getResourceAsStream("/gui/number9.png"));
			
			numberList = Arrays.asList(number0,number1,number2,number3,number4,number5,number6,number7,number8,number9);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g, Human human, GamePanel gp) {
		// Health-bar
		for (int i = 0; i < human.lives; i++) {
			g.drawImage(heart1, GamePanel.BOARD_START + 20 + i*70, GamePanel.BOARD_HEIGHT - 75, 50, 50, null);
		}
		for (int i = human.lives; i < human.max_lives; i++) {
			g.drawImage(heart2, GamePanel.BOARD_START + 20 + i*70, GamePanel.BOARD_HEIGHT - 75, 50, 50, null);
		}
		// Ammo-bar
		g.drawImage(ammoBar, GamePanel.BOARD_START, GamePanel.BOARD_HEIGHT - 10, 5*human.max_ammo, 14, null);
		for (int i = 0; i < human.ammo; i++) {
			g.drawImage(bulletBar, GamePanel.BOARD_START + i*5, GamePanel.BOARD_HEIGHT - 10, 6, 14, null);
		}
		// Game Paused display
		if (GamePanel.phaseOfGame == Phase.PAUSE) {
			g.drawImage(gamePaused, GamePanel.BOARD_START + GamePanel.BOARD_WIDTH/2 - 300, GamePanel.BOARD_HEIGHT/2 - 90, 600, 90, null);
		} 
		// Score-display
		g.drawImage(score, GamePanel.BOARD_START + 20, 20, 200, 40, null);
		String scoreString = Integer.toString(gp.score);
		int index = 0;
		for (String a: scoreString.split("")) {
			int number = Integer.parseInt(a);
			g.drawImage(numberList.get(number), GamePanel.BOARD_START + 240 + index*45, 20, numberList.get(number).getWidth()*8, 40, null);
			index++;
		}
	}
}