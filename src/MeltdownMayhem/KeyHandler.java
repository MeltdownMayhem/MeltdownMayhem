package MeltdownMayhem;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Entity.Barrel;
import Entity.Drone;
import Entity.Human;
import MeltdownMayhem.GamePanel.Phase;
/**
 * De KeyHandler class 'handelt' de 'keys'.
 * De volledige toetsenbord in 1 classe verzamelt.
 * (De cheat keys zullen later verwijderd worden)
 */
public class KeyHandler implements KeyListener, MouseListener{

	Drone drone;
	Human human;
	
	public KeyHandler(Human man, Drone ron) {
		human = man;
		drone = ron;
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
				try {
					drone.teleportMouse(drone.x, drone.y);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
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
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		int mouseButton = e.getButton();
		if (mouseButton == MouseEvent.BUTTON1) {
			drone.droneDestructsBarrel = true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		drone.droneDestructsBarrel = false;
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
}