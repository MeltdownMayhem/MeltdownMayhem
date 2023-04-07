package Entity;

import MeltdownMayhem.GamePanel;

public 	abstract class Character extends Entity{
	
	public int width, depth;
	public boolean moveRight, moveLeft, moveUp, moveDown;
	
	
	public Character() {
		this.lives = 1;
	}
	
	public void update() {
		if (this.moveRight==true && x < GamePanel.BOARD_END-width-2) { // 2-pixel draw accuracy error
			x += vx + 1;
		}
		if (this.moveLeft==true && x > GamePanel.BOARD_START) {
			x -= vx;
		}
		if (this.moveUp==true && y > 0) {
			y -= vy;
		}
		if (this.moveDown==true && y < GamePanel.BOARD_HEIGHT-depth) {
			y += vy;
		}
	}
}