// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

//The main panel
public class CueMasherPanel extends JPanel {
	private ArrayList<SoundInfo> soundList;	//Holds the list of objects containing the sounds and other information
	private final int SCREEN_WIDTH = 1910;	//The estimated width of the application
	private final int SCREEN_HEIGHT = 1000;	//The estimated height of the application
	private final int BUTTON_SPACE = 10;	//The space to leave between buttons and the edge of the screen
	private int[] buttonWidth;
	private int buttonHeight;
	private JButton space;
	
	//Constructor
	public CueMasherPanel() {
		//Listens to the keyboard
		addKeyListener(new ToggleSound());
		//Panel is always in focus so the key listener works
		setFocusable(true);
		
		//Calculate the button width and height so the buttons span the screen
		buttonWidth = new int[5];
		buttonWidth[0] = (SCREEN_WIDTH-(BUTTON_SPACE*12))/13;
		buttonWidth[1] = buttonWidth[0];
		buttonWidth[2] = (SCREEN_WIDTH-(BUTTON_SPACE*10))/11;
		buttonWidth[3] = (SCREEN_WIDTH-(BUTTON_SPACE*10))/10;
		buttonWidth[4] = (SCREEN_WIDTH-(BUTTON_SPACE*10))/10;
		buttonHeight = (SCREEN_HEIGHT-(BUTTON_SPACE*7))/6;
		
		soundList = new ArrayList<SoundInfo>();

		//Create a button for the spacebar
		space = new JButton("Stop (spacebar)");
		space.addActionListener(new ButtonListener());
		//The panel must always be in focus for the keys to work and play sounds
		space.setFocusable(false);
		//Add the spacebar button to the panel
		add(space);
		
		populatePanelFromTextFile("./soundPaths");
	}
	
	// Set the buttons displayed in the panel
	// textFilePath - The path to the sound effect definition file
	public void populatePanelFromTextFile(String textFilePath) {
		
		// Remove all the old buttons and refresh the panel
		for (int i=0; i < soundList.size(); i++) {
			SoundInfo curr = soundList.get(i);
			remove(curr.getButton());
		}
		revalidate();
        repaint();

		soundList = new ArrayList<SoundInfo>();
		ButtonListener btnListener = new ButtonListener();
		
		//Attempt to get the text file of information and initial the sounds
		try {
			//Get the file and a Scanner to look through it
			File soundPaths = new File(textFilePath);
			Scanner scanSoundPathDoc = new Scanner(soundPaths);
			
			//Create another scanner to parse each line of the text file
			Scanner scanLine = new Scanner("");
			//Parse each line in the text file
			while(scanSoundPathDoc.hasNextLine()) {
				scanLine = new Scanner(scanSoundPathDoc.nextLine());
				//File is delimited with CSV format
				scanLine.useDelimiter(",");
				//Store the sound path and other information in the text file in an object
				SoundInfo soundClip = new SoundInfo(scanLine.next(),scanLine.nextInt(),scanLine.next(),scanLine.next(),scanLine.nextInt());
				//Create a button for the sound
				JButton btn = new JButton(soundClip.getBtnLabel() + " (" + soundClip.getKeyName() + ")");
				btn.addActionListener(btnListener);
				//The panel must always be in focus for the keys to work and play sounds
				btn.setFocusable(false);
				//Store this button in the sound's object
				soundClip.setButton(btn);
				//Add the sound object to the list of objects
				soundList.add(soundClip);
				//Add the button to the panel
				add(btn);
			}
			
			//For each sound object, find the sound file associated with the stored path
			for (int i=0; i < soundList.size(); i++) {
				SoundInfo curr = soundList.get(i);
	            File soundPath = new File(curr.getPath());
	            //Create the player that will stop, play and reset the sound file
	            PCMFilePlayer clip = new PCMFilePlayer(soundPath);
	            //Store the player in the sound object
	            soundList.get(i).setSound(clip);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Set the positions and sizes of the buttons and display the panel
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		//These strings determine where to place the button based on the key the user presses to play the sound
		//The end layout will be similar to the center part of the keyboard
		String[] rows = {"`1234567890-=", "qwertyuiop[]\\", "asdfghjkl;\'", "zxcvbnm<./", "1234567890"};
		
		int xPos;
		int yPos;
		//Subtract 1 from this int everytime a sound button is placed where it belongs
		//When it equals zero all sounds have been placed
		int keysToMap = soundList.size();
		ArrayList<SoundInfo> copy = (ArrayList<SoundInfo>) soundList.clone();

		//For each string in the rows array, check if any of the key strings for the sound match
		for (int j=0; j < rows.length; j++) {
			int i = 0;
			
			//For each string in the rows array, check each sound object to see if it is played with a key in that row
			while ((copy.size() > 0) && (i < soundList.size())) {
				int index = 0;
				if (soundList.get(i).getKeyName().indexOf("N") != -1) {
					if (j == 4)
						index = rows[j].indexOf(soundList.get(i).getKeyName().substring(1));
					else
						index = -1;
				}
				else {
					if (j != 4)
						//Get where the key string is in the rows array
						index = rows[j].indexOf(soundList.get(i).getKeyName());
					else
						index = -1;
				}
				//If the key string doesn't exist, the key is not in this row and the button won't be placed
				if (index != -1) {
					//Place the button based on the corresponding key's position on the keyboard
					xPos = ((buttonWidth[j]+BUTTON_SPACE) * index) + BUTTON_SPACE;
					yPos = ((buttonHeight+BUTTON_SPACE) * j) + BUTTON_SPACE ;
					
					JButton curr = soundList.get(i).getButton();
					//Set the button's size and position to the size and position calculated
					curr.setSize(new Dimension (buttonWidth[j], buttonHeight));
					curr.setLocation(xPos, yPos);
					//Decrement the number of sounds left to map
					copy.remove(soundList.get(i));
					keysToMap -= 1;
				}
				
				//Increment the index of the sound to check
				i++;
			}
		}
		
		//Set the x-position, y-position, and size of the spacebar key
		//It will span the bottom of the screen and be the same height as the other buttons
		xPos = 10;
		yPos = ((buttonHeight+BUTTON_SPACE)*5) + BUTTON_SPACE;
		space.setSize(new Dimension (SCREEN_WIDTH-BUTTON_SPACE, buttonHeight));
		space.setLocation(xPos, yPos);
	}
	
	//Listens to the keyboard keys and plays sounds at touch
	private class ToggleSound implements KeyListener {
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getKeyCode() + ", " + e.getKeyChar());				//testing
			
			//If the key pressed is the spacebar, stop all sounds that are stoppable
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				//Loop through each sound and stop all that are stoppable
				for (int j=0; j < soundList.size(); j++) {
					soundList.get(j).stop();
				}
			else {	//Otherwise, loop through the sound objects until the associated key that was pressed is found
				int i=0;
				boolean found = false;
				
				//Loop through all the sound objects, examining each toggle key
				while ((i < soundList.size()) && (found == false)) {
					SoundInfo curr = soundList.get(i);
					
					//If the key pressed is equal to the toggle key for the sound being examined...
					if (e.getKeyCode() == curr.getKeyCode()) {
						found = true;
						//Play the sound
						curr.getSound().start();
					}
					
					//Increment the sound to be examined
					i++;
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
	
	//Listens to the buttons on the panel
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//If the button pressed is the spacebar, stop all stoppable sounds
			if (event.getSource() == space) {
				//For each stoppable sound, stop it
				for (int j=0; j < soundList.size(); j++) {
					soundList.get(j).stop();
				}
			}
			else { //Otherwise, find the button that was pressed and play its associated sound
				int i=0;
				boolean found = false;
				
				//Loop through the sound objects until the object storing the pressed button is found
				while ((i < soundList.size()) && (found == false)) {
					SoundInfo curr = soundList.get(i);
					
					//If the button pressed is equal to the button stored in the object being examined...
					if (event.getSource() == curr.getButton()) {
						found = true;
						//Play the sound stored in the object
						curr.getSound().start();
					}
					
					//Increment the sound object to be examined
					i++;
				}
			}
		}
	}
}
