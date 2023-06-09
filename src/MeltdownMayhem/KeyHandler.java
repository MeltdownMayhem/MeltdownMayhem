package MeltdownMayhem;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Entity.Drone;
import Entity.Human;
import MeltdownMayhem.GamePanel.State;
/**
 * Class that 'handles' the actions of whatever 'key' is pressed/released.
 * Also handles mouse buttons.
 */
public class KeyHandler implements KeyListener, MouseListener{
	
	Human human;
	Drone drone;
	GamePanel gp;
	
	public KeyHandler(Human man, Drone ron, GamePanel panel) {
		human = man;
		drone = ron;
		gp = panel;
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
			if (GamePanel.gameState == State.PLAY) {
				GamePanel.gameState = State.PAUSE;
				gp.resumeBut.setVisible(true);
				gp.ragequitBut.setVisible(true);
				gp.backToMenuBut.setVisible(true);
			} else if(GamePanel.gameState == State.PAUSE) {
				GamePanel.gameState = State.PLAY;
				gp.resumeBut.setVisible(false);
				gp.ragequitBut.setVisible(false);
				gp.backToMenuBut.setVisible(false);
				gp.setCursor(gp.transparentCursor);
				try {
					drone.teleportMouse(drone.x, drone.y);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
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

	

	@Override
	public void mousePressed(MouseEvent e) {
		int mouseButton = e.getButton();
		if (mouseButton == 3) {
			drone.pickUp(gp);
		}
		if (mouseButton == MouseEvent.BUTTON1) {
			drone.damageBarrel = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int mouseButton = e.getButton();
		if (mouseButton == MouseEvent.BUTTON1) {
			drone.damageBarrel = false;
		}
	}
// This is required to be here, but doesn't do anything functional
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

}