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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import MeltdownMayhem.GamePanel.State;

@SuppressWarnings("serial")
public class StartPanel extends JPanel implements ActionListener{
	
	private JButton playBut, storyBut, backToMenuBut;
	private JTextField humanField, droneField;
	private JLabel humanText, droneText, storyTitle, storyDate;
	private JTextArea story1, story2;
	public String nameHuman, nameDrone, storyText1, storyText2;
	private Border blackBorder;
	private Image playButImage, storyButImage, meltdown_mayhem, backgroundPaper, backToMenuImage;
	private Icon playButIcon, storyButIcon, backToMenuIcon;
	private boolean storyMode;
	private Window window;
	//private ImageIcon backToMenuIcon;
	
	
	StartPanel(Window window){
		this.window = window;
		storyMode = false;
		
		storyText1 = "6:00 AM:\n\n"
				+ "6:37 AM:\n\n"
				+ "6:44 AM:\n\n\n"
				+ "6:47 AM:\n\n\n"
				+ "6:58 AM:\n\n"
				+ "7:06 AM:\n\n"
				+ "7:20 AM:\n"
				+ "7:24 AM:\n\n"
				+ "7:26 AM:\n"
				+ "7:31 AM:\n"
				+ "7:46 AM:\n\n\n";
		
		storyText2 = "Professor Poempernikkel and his team start a nuclear experiment in \nthe Mayhem nuclear research laboratory.\n"
				+ "All communication with the Mayhem nuclear research laboratory is \nsuddenly lost.\n"
				+ "Multiple explosions are heard in the adjacent Mayhem nuclear power \nplant. The emergency services are immediately alerted. Further \ncommunication with the nuclear power plant is also lost.\n"
				+ "3 enormous explosions are heard in the Mayhem nuclear power plant, \nwhich is exactly one for each reactor… Seconds later, dark smoke erupts\n from the reactor buildings. \n"
				+ "The first emergency services arrive. They confirm what no one dared to \nbelieve: all three reactors went into meltdown at once. \n"
				+ "The state of emergency in the region has been activated. The army is \ncalled in and the inhabitants are been asked to leave the area.\n"
				+ "The emergency services report on strange little green monsters.\n"
				+ "The emergency services report that these green monsters seem to \nmultiply at a very fast rate. Orange monsters are also spotted. \n"
				+ "The emergency services report attacks from those monsters. \n"
				+ "The armed forces are called in to combat those strange monsters.\n"
				+ "You are finally awakened by screams in the street. When you look \noutside, you see those green and orange monsters. As a former \nassistant of professor Poempernikkel, you have a very good idea \nof the origin of those monsters…";
		
		// Basic Panel Settings
		this.setPreferredSize(Window.screenSize);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout(null); // Dit zorgt ervoor dat er geen standaard LayoutManager wordt gebruikt, zodat de knoppen op de gewenste plaats verschijnen.
		this.setVisible(true);
		
		// Text for entering a name for Human
		humanText = new JLabel("Enter a name for Human:");
		humanText.setBounds(Window.screenSize.width / 2 - 275, 250, 300, 25);
		humanText.setFont(new Font("Bradley Hand", Font.PLAIN, 25));
		this.add(humanText);
		
		// Text for entering a name for Drone
		droneText = new JLabel ("Enter a name for Drone:");
		droneText.setBounds(Window.screenSize.width / 2 - 275, 300, 300, 25);
		droneText.setFont(new Font("Bradley Hand", Font.PLAIN, 25));
		this.add(droneText);
		
		storyTitle = new JLabel ("STORY");
		storyTitle.setBounds(Window.screenSize.width / 2 - 410, 100, 300, 50);
		storyTitle.setFont(new Font("American Typewriter", Font.BOLD, 50));
		storyTitle.setForeground(Color.black);
		storyTitle.setVisible(false);
		this.add(storyTitle);
		
		storyDate = new JLabel ("August the 6th 2023:");
		storyDate.setBounds(Window.screenSize.width / 2 - 410, 150, 300, 50);
		storyDate.setFont(new Font("Bradley Hand", Font.PLAIN, 30));
		storyDate.setForeground(Color.black);
		storyDate.setVisible(false);
		this.add(storyDate);
		
		story1 = new JTextArea(storyText1);
		story1.setOpaque(false);
		story1.setFocusable(false);
		story1.setEditable(false);
		story1.setBounds(Window.screenSize.width / 2 - 410, 190, 200, 712);
		story1.setFont(new Font("Bradley Hand", Font.PLAIN, 24));
		story1.setVisible(false);
		this.add(story1);
		
		story2 = new JTextArea(storyText2);
		story2.setOpaque(false);
		story2.setFocusable(false);
		story2.setEditable(false);
		story2.setBounds(Window.screenSize.width / 2 - 300, 190, 750, 712);
		story2.setFont(new Font("Bradley Hand", Font.PLAIN, 24));
		story2.setVisible(false);
		this.add(story2);
		
		blackBorder = BorderFactory.createLineBorder(Color.black);
		
		// TextField for entering name Human
		humanField = new JTextField();
		humanField.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		humanField.setBorder(blackBorder);
		humanField.setBounds(Window.screenSize.width / 2 + 75, 250, 200, 25);
		this.add(humanField);
		
		// TextField for entering name Drone
		droneField = new JTextField();
		droneField.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		droneField.setBorder(blackBorder);
		droneField.setBounds(Window.screenSize.width / 2 + 75, 300, 200, 25);
		this.add(droneField);
		
		
		playButImage = new ImageIcon(this.getClass().getResource("/button/play.png")).getImage().getScaledInstance(600, 120, ABORT);
		playButIcon = new ImageIcon(playButImage);
		
		playBut = new JButton(playButIcon);
		playBut.setBounds(Window.screenSize.width / 2 - 300, 375, 600, 120);
		playBut.setBorder(BorderFactory.createEmptyBorder());
		playBut.addActionListener(this);
		this.add(playBut);
		
		storyButImage = new ImageIcon(this.getClass().getResource("/button/story.png")).getImage().getScaledInstance(600, 120, ABORT);
		storyButIcon = new ImageIcon(storyButImage);
		
		storyBut = new JButton(storyButIcon);
		storyBut.setBounds(Window.screenSize.width / 2 - 300, 500, 600, 120);
		storyBut.setBorder(BorderFactory.createEmptyBorder());
		storyBut.addActionListener(this);
		this.add(storyBut);
		
		backToMenuImage = new ImageIcon(this.getClass().getResource("/button/backToMenu.png")).getImage().getScaledInstance(400, 80, ABORT);
		backToMenuIcon = new ImageIcon(backToMenuImage);
		
		backToMenuBut = new JButton(backToMenuIcon);
		backToMenuBut.setBounds(Window.screenSize.width / 2 - 95, 895, 400, 80);
		backToMenuBut.setBorder(BorderFactory.createEmptyBorder());
		backToMenuBut.addActionListener(this);
		backToMenuBut.setVisible(false);
		this.add(backToMenuBut);
		
		try {
			meltdown_mayhem = ImageIO.read(getClass().getResourceAsStream("/gui/meltdown_mayhem.png"));
			backgroundPaper = ImageIO.read(getClass().getResourceAsStream("/background/paperBackground_horizontal.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//backgroundPaper.getScaledInstance(900, 90, ABORT);
	}
	
	
	// Credits to Bro Code to explain how to use JButtons: https://www.youtube.com/watch?v=-IMys4PCkIA
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playBut) {
        	nameHuman = humanField.getText();
        	nameDrone = droneField.getText();
        	if (nameHuman.length() == 0 || nameDrone.length() == 0) {
        		JOptionPane.showMessageDialog(playBut, "Please enter a name for Human and Drone");
        	} else {
        		System.out.println("Initializing new GamePanel");
        		window.gamePanel = null;
        		window.gamePanel = new GamePanel(nameHuman, nameDrone, window);
        		System.out.println("New GamePanel initialized");
        		window.switchPanel(window.gamePanel, window.startPanel);
        		GamePanel.gameState = State.PLAY;
        		
        	}
        } else if(e.getSource() == storyBut) {
        	storyMode = true;
        	playBut.setVisible(false);
        	storyBut.setVisible(false);
        	droneField.setVisible(false);
        	humanField.setVisible(false);
        	droneText.setVisible(false);
        	humanText.setVisible(false);
        	storyTitle.setVisible(true);
        	storyDate.setVisible(true);
        	story1.setVisible(true);
        	story2.setVisible(true);
        	backToMenuBut.setVisible(true);
        	repaint();
        	
        } else if(e.getSource() == backToMenuBut) {
        	storyMode = false;
        	playBut.setVisible(true);
        	storyBut.setVisible(true);
        	droneField.setVisible(true);
        	humanField.setVisible(true);
        	droneText.setVisible(true);
        	humanText.setVisible(true);
        	storyTitle.setVisible(false);
        	storyDate.setVisible(false);
        	story1.setVisible(false);
        	story2.setVisible(false);
        	backToMenuBut.setVisible(false);
        	repaint();
        }
        // Credits to docs.oracle for showing how to use an actionListener: https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html 
		
	}
	
	public void paintComponent(Graphics g) { // Order of drawing is important for overlapping
		super.paintComponent(g);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(Window.BOARD_START, 0, Window.BOARD_WIDTH, Window.BOARD_HEIGHT);
		if (!storyMode) {
			g.drawImage(meltdown_mayhem, Window.screenSize.width/2 - 415, 100, null);
		} else {
			g.drawImage(backgroundPaper, Window.screenSize.width/2 - 550, 0, 1100, 1050, null);
		}
	}

}