package MeltdownMayhem;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Entity.Human;
import MeltdownMayhem.GamePanel.Phase;
/**
 * De KeyHandler class 'handelt' de 'keys'.
 * De volledige toetsenbord in 1 classe verzamelt.
 * (De cheat keys zullen later verwijderd worden)
 */
public class KeyHandler implements KeyListener{
	
	Human human;
	
	public KeyHandler(Human man) {
		human = man;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		// WASD and ARROW keys - movement of Human
		if (key == 81 || key == 37) {
			human.moveLeft = true;
		}
		if (key == 68 || key == 39) {
			human.moveRight = true;
		}
		if (key == 90 || key == 38) {
			human.moveUp = true;
		}
		if (key == 83 || key == 40) {
			human.moveDown = true;
		}
		// spaceBar - shoot a bullet
		if (e.getKeyCode() == 32) {
			human.isShooting = true;
		}
		
		// escape_key - pause game
		if (e.getKeyCode() == 27) {
			if (GamePanel.phaseOfGame == Phase.PLAY) {
				GamePanel.phaseOfGame = Phase.PAUSE;
			} else if(GamePanel.phaseOfGame == Phase.PAUSE) {
				GamePanel.phaseOfGame = Phase.PLAY;
			}
		}
		
		// DEVELOPER'S CHEATING KEYS //
		// 'r' - refill ammo
		if (e.getKeyCode() == 82) {
			human.ammo = human.max_ammo;
		}
		// 'h' - heal human
		if (e.getKeyCode() == 72) {
			human.lives = human.max_lives;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		// WASD and ARROW keys - stop moving when released
		if (key == 81 || key == 37) {
			human.moveLeft = false;
		}
		if (key == 68 || key == 39) {
			human.moveRight = false;
		}
		if (key == 90 || key == 38) {
			human.moveUp = false;
		}
		if (key == 83 || key == 40) {
			human.moveDown = false;
		}
		// space_bar - stop shooting when released
		if (e.getKeyCode() == 32) {
			human.isShooting = false;
			human.shootingCooldown = 0; // reset to 0 for no delay
		}
	}
}