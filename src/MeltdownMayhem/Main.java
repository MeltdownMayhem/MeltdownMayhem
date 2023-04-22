package MeltdownMayhem;

import javax.swing.JFrame;
/**
 * De Main class zorgt voor de Frame creation.
 */
public class Main {
	
	public static void main(String args[]) {
		// Frame creation
		JFrame f = new JFrame();
		f.setTitle("Meltdown Mayhem");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		
		// Panel creation
		GamePanel panel = new GamePanel();
		
		f.add(panel);
		f.pack();
		f.setLocation(-10,0);
		f.setVisible(true);
	}
}