package Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class radiationOrb extends Enemy {
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
		
		// Show hitboxes
		g.setColor(new Color(255,0,0));
		g.drawOval(x - (int)(enemyRadius*0.7), y - (int)(enemyRadius*0.7), (int)(enemySize * 0.7), (int)(enemySize * 0.7));
		g.setColor(new Color(0,0,255));
		g.fillOval(x, y, 3, 3);
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
		
		spawnPriority();
		
		stayInField();
		
		randomSpeedChange();
		
	}
}