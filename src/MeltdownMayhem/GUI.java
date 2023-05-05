package MeltdownMayhem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Entity.Human;
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
	
	BufferedImage score_blue, score_orange;
	BufferedImage number0_blue, number0_orange;
	BufferedImage number1_blue, number1_orange;
	BufferedImage number2_blue, number2_orange;
	BufferedImage number3_blue, number3_orange;
	BufferedImage number4_blue, number4_orange;
	BufferedImage number5_blue, number5_orange;
	BufferedImage number6_blue, number6_orange;
	BufferedImage number7_blue, number7_orange;
	BufferedImage number8_blue, number8_orange;
	BufferedImage number9_blue, number9_orange;
	
	List<BufferedImage> numberList_blue, numberList_orange;
	
	public GUI() {
		numberList_blue = new ArrayList<BufferedImage>();
		numberList_orange = new ArrayList<BufferedImage>();
		
		try {
			// Retrieve the images for the GUI <Credits to RyiSnow | https://www.youtube.com/@RyiSnow>
			heart1 = ImageIO.read(getClass().getResourceAsStream("/gui/heart1.png"));
			heart2 = ImageIO.read(getClass().getResourceAsStream("/gui/heart2.png"));
			
			bulletBar = ImageIO.read(getClass().getResourceAsStream("/gui/bulletBar.png"));
			ammoBar = ImageIO.read(getClass().getResourceAsStream("/gui/ammoBar.png"));
			
			score_blue = ImageIO.read(getClass().getResourceAsStream("/gui/score_blue.png"));
			score_orange = ImageIO.read(getClass().getResourceAsStream("/gui/score_orange.png"));
			number0_blue = ImageIO.read(getClass().getResourceAsStream("/number/number0_blue.png"));
			number1_blue = ImageIO.read(getClass().getResourceAsStream("/number/number1_blue.png"));
			number2_blue = ImageIO.read(getClass().getResourceAsStream("/number/number2_blue.png"));
			number3_blue = ImageIO.read(getClass().getResourceAsStream("/number/number3_blue.png"));
			number4_blue = ImageIO.read(getClass().getResourceAsStream("/number/number4_blue.png"));
			number5_blue = ImageIO.read(getClass().getResourceAsStream("/number/number5_blue.png"));
			number6_blue = ImageIO.read(getClass().getResourceAsStream("/number/number6_blue.png"));
			number7_blue = ImageIO.read(getClass().getResourceAsStream("/number/number7_blue.png"));
			number8_blue = ImageIO.read(getClass().getResourceAsStream("/number/number8_blue.png"));
			number9_blue = ImageIO.read(getClass().getResourceAsStream("/number/number9_blue.png"));
			
			numberList_blue = Arrays.asList(number0_blue,number1_blue,number2_blue,number3_blue,number4_blue,number5_blue,number6_blue,number7_blue,number8_blue,number9_blue);
			
			number0_orange = ImageIO.read(getClass().getResourceAsStream("/number/number0_orange.png"));
			number1_orange = ImageIO.read(getClass().getResourceAsStream("/number/number1_orange.png"));
			number2_orange = ImageIO.read(getClass().getResourceAsStream("/number/number2_orange.png"));
			number3_orange = ImageIO.read(getClass().getResourceAsStream("/number/number3_orange.png"));
			number4_orange = ImageIO.read(getClass().getResourceAsStream("/number/number4_orange.png"));
			number5_orange = ImageIO.read(getClass().getResourceAsStream("/number/number5_orange.png"));
			number6_orange = ImageIO.read(getClass().getResourceAsStream("/number/number6_orange.png"));
			number7_orange = ImageIO.read(getClass().getResourceAsStream("/number/number7_orange.png"));
			number8_orange = ImageIO.read(getClass().getResourceAsStream("/number/number8_orange.png"));
			number9_orange = ImageIO.read(getClass().getResourceAsStream("/number/number9_orange.png"));
			
			numberList_orange = Arrays.asList(number0_orange,number1_orange,number2_orange,number3_orange,number4_orange,number5_orange,number6_orange,number7_orange,number8_orange,number9_orange);

			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g, Human human, GamePanel gp) {
		// Health-bar
		for (int i = 0; i < human.lives; i++) {
			g.drawImage(heart1, Window.BOARD_START + 20 + i*70, Window.BOARD_HEIGHT - 75, 50, 50, null);
		}
		for (int i = human.lives; i < human.max_lives; i++) {
			g.drawImage(heart2, Window.BOARD_START + 20 + i*70, Window.BOARD_HEIGHT - 75, 50, 50, null);
		}
		// Ammo-bar
		g.drawImage(ammoBar, Window.BOARD_START, Window.BOARD_HEIGHT - 10, 5*human.max_ammo, 14, null);
		for (int i = 0; i < human.ammo; i++) {
			g.drawImage(bulletBar, Window.BOARD_START + i*5, Window.BOARD_HEIGHT - 10, 6, 14, null);
		}
		// Score-display
		//g.drawImage(score_blue, Window.BOARD_START + 20, 20, 200, 40, null);
		//g.drawImage(score_blue, Window.BOARD_START + 50, 25, 200, 40, null);
		String scoreString = Integer.toString(gp.score);
		int index = 0;
		if (gp.level == 1) {
			g.drawImage(score_blue, Window.BOARD_START + 20, 20, 200, 40, null);
			for (String a: scoreString.split("")) {
				int number = Integer.parseInt(a);
				g.drawImage(numberList_blue.get(number), Window.BOARD_START + 250 + index*45, 20, numberList_blue.get(number).getWidth()*8, 40, null);
				index++;
			}
		} else {
			g.drawImage(score_orange, Window.BOARD_START + 20, 20, 200, 40, null);
			for (String a: scoreString.split("")) {
				int number = Integer.parseInt(a);
				g.drawImage(numberList_orange.get(number), Window.BOARD_START + 250 + index*45, 20, numberList_orange.get(number).getWidth()*8, 40, null);
				index++;
			}
		}
	}
}