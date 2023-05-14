package MeltdownMayhem;
/*
 * Creates the main Window in which Panels are selected and displayed.
 */
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame {
	protected JPanel gamePanel, startPanel;
	
	// Window settings
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int BOARD_WIDTH = 1188; // Board refers to the playable area
	public static final int BOARD_HEIGHT = Window.screenSize.height - 80;
	public static final int BOARD_START = (Window.screenSize.width - BOARD_WIDTH)/2;
	public static final int BOARD_END = (Window.screenSize.width - BOARD_WIDTH)/2 + BOARD_WIDTH;
	
	Window(){
		this.setTitle("Meltdown Mayhem");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBounds(0,0, screenSize.width, screenSize.height);
		startPanel = new StartPanel(this);
		this.add(startPanel);
		this.setVisible(true);
	}
	
	// Credits to Jan Lemaire for the example code to switch between different panels 
	public void switchPanel(JPanel toActivate, JPanel toRemove) {
		this.remove(toRemove);
		this.add(toActivate);
		
		validate();
		repaint();
	}
	
	public static void main(String args[]) {
		Window window = new Window();
	}
}