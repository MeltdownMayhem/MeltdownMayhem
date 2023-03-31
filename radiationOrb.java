package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import MeltdownMayhem.GamePanel;

public class radiationOrb extends Enemy {
	protected static final int SPEED_RESET_FACTOR = 200;
	protected static final int SPEED_COEFFICIENT = 1;
	protected static final int OSCILLATION_FACTOR = 2;
	
	public radiationOrb(int x) {
		this.x = x;
		this.y = -enemySize/2; // -20 pixels
		
		this.vx = rng.nextInt(3)*Math.pow(-1, rng.nextInt(2));
		this.vy = 1 + rng.nextInt(2); // y-speed can't be negative when spawning
		
		this.lives = 1;
		
		getOrbImage();
	}
	
	public void getOrbImage() { // Credits to RyiSnow for explaining how to draw a sprite from source files
		
		try {
			orbLeft1 = ImageIO.read(getClass().getResourceAsStream("/RadiationOrb/orbLeft1.png"));
			orbRight1 = ImageIO.read(getClass().getResourceAsStream("/RadiationOrb/orbRight1.png"));
			orbLeft2 = ImageIO.read(getClass().getResourceAsStream("/RadiationOrb/orbLeft2.png"));
			orbRight2 = ImageIO.read(getClass().getResourceAsStream("/RadiationOrb/orbRight2.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		BufferedImage image = null;
		if (vx < 0) {
			if (spriteNum == 1) {
				image = orbLeft1;
			} else if (spriteNum == 2) {
				image = orbLeft2;
			}
		} else {
			if (spriteNum == 1) {
				image = orbRight1;
			} else if (spriteNum == 2) {
				image = orbRight2;
			}
		}
		g.drawImage(image, x - enemyRadius, y - enemyRadius, enemySize, enemySize, null);
	}
	
	public void stayInField() {
		/*
		De functie kijkt na of de coördinaten van een enemy nog wel binnenin het speelveld blijven.
		Indien de enemy het speelveld aan het verlaten is, wordt hij terug in het veld geplaatst. 
		Hierbij wordt hij terug het veld in gestuurd, verwijderd van de rand van het speelveld
		(langs de binnenkant van het veld) met een even grote afstand dan hij uit het veld gegaan is.
		Ook wordt het teken van de snelheid in de betreffende richting omgewisseld.
		*/
		
		// Vertical Board borders collision
		if (this.x > GamePanel.BOARD_END - margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (GamePanel.BOARD_END - margin) - this.x;
		} else if(this.x < GamePanel.BOARD_START + margin) {
			this.vx *= -1;
			this.timeSinceReset_x = 0;
			this.x = 2 * (GamePanel.BOARD_START + margin) - this.x;
		}
		
		// Horizontal Board borders collision
		if (this.y > GamePanel.BOARD_HEIGHT - margin) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (GamePanel.BOARD_HEIGHT - margin) - this.y;
		} else if (!this.appearing && this.spawning && this.y < enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * enemyRadius - this.y;
		} else if(!this.spawning && this.y < UPPER_BORDER + enemyRadius) {
			this.vy *= -1;
			this.timeSinceReset_y = 0;
			this.y = 2 * (UPPER_BORDER + enemyRadius) - this.y;
		}
	}

	public void update() {
		
		this.x += (int) SPEED_COEFFICIENT * this.vx;
		this.y += (int) SPEED_COEFFICIENT * this.vy;
		
		// Image wing flapper (the flapper-flipper 3000)
		spriteCounter++;
		if (spriteNum == 1) {
			if (spriteCounter > 40) {
				spriteNum = 2;
				spriteCounter = 0;
			}
		} else if (spriteNum == 2) {
			if (spriteCounter > 20) {
				spriteNum = 1;
				spriteCounter = 0;
			}
		}
		
		/*
		Een enemy is uit zijn appearingfase als hij boven de bovenkant van het scherm geraakt.
		
		Een enemy is uit zijn spawnfase als hij onder de UPPER_BORDER geraakt. Vanaf dan kan hij niet meer boven
		de UPPERBORDER geraken om plaats te maken voor het spawnen van andere enemies.
		*/
		if (this.appearing && this.y > enemyRadius) {
			this.appearing = false;
		} else if (this.spawning && this.y > UPPER_BORDER + enemyRadius) {
			this.spawning = false;
		}
		
		stayInField();
		
		// Volgende code laat de snelheid van de enenmies om de zoveel tijd (met enige randomisatie) veranderen
		if (timeSinceReset_x / SPEED_RESET_FACTOR > rng.nextDouble()) {
			this.vx = rng.nextInt(3) * Math.pow(-1, rng.nextInt(2));
			this.timeSinceReset_x = 0;
		} else {
			this.timeSinceReset_x++;
		}
				
		if (!this.spawning && (timeSinceReset_y / SPEED_RESET_FACTOR > rng.nextDouble())) {
			this.vy = rng.nextInt(3) * Math.pow(-1, rng.nextInt(2));
			this.timeSinceReset_y = 0;
		} else {
			this.timeSinceReset_y++;
		}
		
		// Volgende 2 lijnen laten de enemies oscilleren
		//this.x += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
		//this.y += OSCILLATION_FACTOR * rng.nextDouble() * Math.pow(-1, rng.nextInt(2));
	}
}