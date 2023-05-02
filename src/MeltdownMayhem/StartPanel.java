package MeltdownMayhem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import MeltdownMayhem.GamePanel.Phase;

@SuppressWarnings("serial")
public class StartPanel extends JPanel implements ActionListener{
	
	private JButton button;
	private JTextField humanField, droneField;
	private JLabel humanText, droneText;
	public String nameHuman, nameDrone;
	private Border blackBorder;
	private Image buttonImage, meltdown_mayhem;
	private Icon buttonIcon;
	private Window mainWindow;
	
	
	StartPanel(Window mainWindow){
		this.mainWindow = mainWindow;
		
		// Basic Panel Settings
		this.setPreferredSize(GamePanel.screenSize);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout(null); // Dit zorgt ervoor dat er geen standaard LayoutManager wordt gebruikt, zodat de knoppen op de gewenste plaats verschijnen.
		this.setVisible(true);
		
		// Text for entering a name for Human
		humanText = new JLabel("Enter a name for Human:");
		humanText.setBounds(GamePanel.screenSize.width / 2 - 275, 250, 300, 25);
		humanText.setFont(new Font("Bradley Hand", Font.PLAIN, 25));
		this.add(humanText);
		
		// Text for entering a name for Drone
		droneText = new JLabel ("Enter a name for Drone:");
		droneText.setBounds(GamePanel.screenSize.width / 2 - 275, 300, 300, 25);
		droneText.setFont(new Font("Bradley Hand", Font.PLAIN, 25));
		this.add(droneText);
		
		blackBorder = BorderFactory.createLineBorder(Color.black);
		
		// TextField for entering name Human
		humanField = new JTextField();
		humanField.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		humanField.setBorder(blackBorder);
		humanField.setBounds(GamePanel.screenSize.width / 2 + 75, 250, 200, 25);
		this.add(humanField);
		
		// TextField for entering name Drone
		droneField = new JTextField();
		droneField.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		droneField.setBorder(blackBorder);
		droneField.setBounds(GamePanel.screenSize.width / 2 + 75, 300, 200, 25);
		this.add(droneField);
		
		
		buttonImage = new ImageIcon(this.getClass().getResource("/button/play.png")).getImage().getScaledInstance(600, 100, ABORT);
		buttonIcon = new ImageIcon(buttonImage);
		
		button = new JButton(buttonIcon);
		button.setBounds(GamePanel.screenSize.width / 2 - 300, 375, 600, 100);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(this);
		this.add(button);
		
		try {
			meltdown_mayhem = ImageIO.read(getClass().getResourceAsStream("/gui/meltdown_mayhem.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// Credits to Bro Code to explain how to use JButtons: https://www.youtube.com/watch?v=-IMys4PCkIA
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
        	nameHuman = humanField.getText();
        	nameDrone = droneField.getText();
        	if (nameHuman.length() == 0 || nameDrone.length() == 0) {
        		JOptionPane.showMessageDialog(button, "Please enter a name for Human and Drone");
        	} else {
        		mainWindow.switchPanel(new GamePanel(mainWindow, nameHuman, nameDrone, this));
        		GamePanel.phaseOfGame = Phase.PLAY;
        	}
        }
        // Credits to docs.oracle for showing how to use an actionListener: https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html 
		
	}
	
	public void paintComponent(Graphics g) { // Order of drawing is important for overlapping
		super.paintComponent(g);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(GamePanel.BOARD_START, 0, GamePanel.BOARD_WIDTH, GamePanel.BOARD_HEIGHT);
		g.drawImage(meltdown_mayhem, GamePanel.screenSize.width/2 - 415, 100, null);
	}

}