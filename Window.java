package MeltdownMayhem;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame {
	private JPanel activePanel;
	private Image imageApplication;
	private Icon iconApplication;
	
	Window(){
		this.setTitle("Meltdown Mayhem");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBounds(0,0, GamePanel.screenSize.width, GamePanel.screenSize.height);
		activePanel = new StartPanel(this);
		this.add(activePanel);
		
		imageApplication = new ImageIcon(this.getClass().getResource("/gui/number0.png")).getImage().getScaledInstance(600, 100, ABORT);
		iconApplication = new ImageIcon(imageApplication);
		
		this.setIconImage(imageApplication);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/gui/number0.png")));
		this.setVisible(true);
	}
	
	// Credits to Jan Lemaire for the example code to switch between different panels 
	public void switchPanel(JPanel toActivate) {
		this.remove(activePanel);
		activePanel = toActivate;
		this.add(activePanel);
		
		validate();
		repaint();
	}
}