package MeltdownMayhem;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String args[]) {
		// Standard frame creation
		JFrame f = new JFrame();
		f.setTitle("Meltdown Mayhem");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		//f.setLayout(new BorderLayout());

		GamePanel panel = new GamePanel();
		f.add(panel);
		f.pack();
		
		f.setLocation(-10,0);
		f.setVisible(true);
	}
}