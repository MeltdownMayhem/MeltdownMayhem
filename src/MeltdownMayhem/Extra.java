package MeltdownMayhem;

import java.io.File;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JOptionPane;

/**
 * De Extra class bevat (momenteel zeer weinig) extra methodes die regelmatig gebruikt worden doorheen de verschillende classes.
 */
public class Extra {
	
	// Distance calculator between 2 given coordinates
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2), 0.5);
	}
	// Plays a sound clip; credit to Max O'Didily for explaining how this code works, M. https://www.youtube.com/watch?v=TErboGLHZGA
	// Also a special thanks to Melati Saerens who voiced some lines for us
	public static void playSound(String musicLocation) {
		try {
			File musicPath = new File(musicLocation);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInput);
			clip.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}