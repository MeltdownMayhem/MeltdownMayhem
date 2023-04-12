package MeltdownMayhem;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Entity.Barrel;
import Entity.Human;

public class KeyHandler implements KeyListener{
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		// WASD and ARROW movement of Human
		if (key == 81 || key == 37) {
			GamePanel.human.moveLeft = true;
		}
		if (key == 68 || key == 39) {
			GamePanel.human.moveRight = true;
		}
		if (key == 90 || key == 38) {
			GamePanel.human.moveUp = true;
		}
		if (key == 83 || key == 40) {
			GamePanel.human.moveDown = true;
		}
		// spaceBar - to shoot a bullet
		if (e.getKeyCode() == 32) {
			GamePanel.shooting = true;
		}
		// DEVELOPER CHEAT KEYS //
		// '+' - Add barrel
		if (e.getKeyCode() == 107) {
			GamePanel.barrelList.add(new Barrel());
		}
		// 'f' - Refill ammo
		if (e.getKeyCode() == 70) {
			GamePanel.ammo = GamePanel.max_ammo;
		}
		// 'h' - Refill lives
		if (e.getKeyCode() == 72) {
			GamePanel.human.lives = Human.max_lives;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Stop Human movement when key is released
		if (key == 81 || key == 37) {
			GamePanel.human.moveLeft = false;
		}
		if (key == 68 || key == 39) {
			GamePanel.human.moveRight = false;
		}
		if (key == 90 || key == 38) {
			GamePanel.human.moveUp = false;
		}
		if (key == 83 || key == 40) {
			GamePanel.human.moveDown = false;
		}
		// spaceBar - release to stop shooting bullets
		if (e.getKeyCode() == 32) {
			GamePanel.shooting = false;
			GamePanel.shootingCooldown = 0; // Reset on 0 to shoot instantly when tapping
		}
	}
}