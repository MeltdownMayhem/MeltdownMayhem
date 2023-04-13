package Entity;

import java.util.Timer;
import MeltdownMayhem.GamePanel;

public 	abstract class Character extends Entity{
	
	public int width;
	public int depth;
	public boolean moveRight, moveLeft, moveUp, moveDown;
	public Timer respawnTimer = new Timer();
	
	public Character() {
		this.lives = 1;
	}
	
	public void update() {
		
	}
}
