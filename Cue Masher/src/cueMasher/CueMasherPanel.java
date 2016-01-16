// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import java.util.*;

//This class defines the main Cue Masher GUI.
public class CueMasherPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static String SPACEBAR = "Spacebar";
	private final int SCREEN_WIDTH = 1910;	//The estimated width of the application
	private final int SCREEN_HEIGHT = 1000;	//The estimated height of the application
	private final int BUTTON_SPACE = 10;	//The space to leave between buttons and the edge of the screen
	private int[] buttonWidth;
	private int buttonHeight;
	
	//These strings determine where to place the buttons based on the key the user presses to play the sound
	private String[] rows = {"`1234567890-=", "qwertyuiop[]\\", "asdfghjkl;\'", "zxcvbnm<./", "1234567890"};
	
	private CueMasherFrame frame;
	private ProjectFileManager soundManager;
	private SoundDialogManager dialogManager;
	
	private ArrayList<BoardButton> soundList;	//Holds the list of objects containing the sounds and other information
	private BoardButton space;
	
	//Constructor
	// frame - The frame containing this panel
	public CueMasherPanel(CueMasherFrame frame) {
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
		
		this.frame = frame;
		soundList = new ArrayList<BoardButton>();
		soundManager = new ProjectFileManager();

		//Create a button for the spacebar
		JButton btnSpace = new JButton("Stop (" + SPACEBAR + ")");
		space = new StopButton(soundManager, btnSpace);
		add(btnSpace);
		
		dialogManager = new SoundDialogManager(this);
	}
	
	// Returns the object that manages all New/Edit Sound dialog boxes
	public SoundDialogManager getDialogManager() {
		return dialogManager;
	}
	
	// Clears the buttons and sounds for a new project
	public void openNewProject() {
		clearBoard();
		soundManager.openNewProject();
	}
	
	// Set the buttons displayed in the panel
	// textFilePath - The path to the sound effect definition file
	public void loadFile(String textFilePath) {
		clearBoard();
		
		// Get the sounds contained in the given project file
		ArrayList<SoundInfo> readSounds = soundManager.readFile(textFilePath);
		
		// Add buttons to the interface for each sound in the file
		for (int s=0; s < readSounds.size(); s++) {
			addSound(readSounds.get(s));
		}
	}
	
	// Save the open project if needed
	public boolean saveFile() {
		if (!soundManager.isProjectOpen()) {
			// The open project hasn't been saved to a file yet
			// Have the user select a location and file name
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new CueFileFilter());
			int choice = fileChooser.showSaveDialog(CueMasherPanel.this);
			
			if (choice == JFileChooser.APPROVE_OPTION) {
				// Get the name of the file the user selected
				File file = fileChooser.getSelectedFile();
				String filePath = file.getAbsolutePath().trim();
				
				// Make sure that it has a Cue Masher file extension
				String cueMasherExt = CueFileFilter.CUE_MASHER_FILE_EXT;
				boolean addExt = false;
				if (filePath.length() < cueMasherExt.length())
					addExt = true;
				else {
					String ext = filePath.substring(filePath.length()-cueMasherExt.length(), filePath.length());
					if (!ext.equalsIgnoreCase(cueMasherExt))
						addExt = true;
				}
				
				// Add the cue masher file extension if it doesn't exist
				if (addExt)
					filePath = filePath + cueMasherExt;
				
				// Save the project to the selected file
				soundManager.setProjectFilePath(filePath);
				return soundManager.saveFile();
			}
		}
		else {
			return soundManager.saveFile();
		}
		return false;
	}
	
	// Returns whether the given key name is valid
	// keyName - The name of the key the user is attempting to map to a new sound
	public boolean checkValidKey(String keyName) {
		// For numpad keys, remove the "N" placed before the key name first before checking its existence
		if (keyName.indexOf("N") != -1) {
			keyName = keyName.substring(1);
		}
		
		// Check if the key is in the list of acceptable keys
		for (int j=0; j < rows.length; j++) {
			int index = rows[j].indexOf(keyName);
			if (index != -1)
				return true;
		}
		return false;
	}
	
	// Removes a sound from the interface and sound list
	// keyCode - The keyboard key code associated with the sound
	public void deleteSound(int keyCode) {
		soundManager.deleteSound(keyCode);
		
		// Search the sound buttons for the button to remove
		for (int s=0; s < soundList.size(); s++) {
			BoardButton button = soundList.get(s);
			if (button.getKeyCode() == keyCode) {
				// Remove the button from the interface
				remove(button.getButton());
				soundList.remove(keyCode);

				// Update the GUI
				frame.setProjectModified();
				revalidate();
		        repaint();
		        
		        break;
			}
		}
	}

	// Add a sound to the displayed buttons, sound list, and sound file
	// soundPath - The path to the sound file on the file system
	// keyCode - The code of the key the user can press to play the sound
	// keyName - The name of the key the user can press to play the sound
	// soundName - The short name of the sound to display in the GUI
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	public void addSound(String soundPath, int keyCode, String keyName, String soundName, int stoppable) {
		
		// Add the sound to the project
		SoundInfo newSound = soundManager.addSound(soundPath, keyCode, keyName, soundName, stoppable);
		
		if (newSound != null) {
			// Add a button to the GUI
			addSound(newSound);

			// Display the new button in the GUI
			frame.setProjectModified();
			revalidate();
	        repaint();
		}
	}
	
	// Add a button to the GUI for a sound
	// soundInfo - The sound to add to the GUI
	private void addSound(SoundInfo soundInfo) {
		
		//Create a button for the sound
		JButton btnSound = new JButton(soundInfo.getSoundName() + " (" + soundInfo.getKeyName() + ")");
		SoundButton buttonContainer = new SoundButton(dialogManager, soundInfo, btnSound);
        
		//Add the new sound button to the list of buttons and the panel
		soundList.add(buttonContainer);
		add(btnSound);
	}
	
	// Clears all the buttons from the GUI
	private void clearBoard() {

		// Remove all the old buttons and refresh the panel
		for (int i=0; i < soundList.size(); i++) {
			BoardButton curr = soundList.get(i);
			remove(curr.getButton());
		}
		revalidate();
        repaint();

        // Refresh the list of sound board buttons
		soundList = new ArrayList<BoardButton>();
	}
	
	//Set the positions and sizes of the buttons and display the panel
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		int xPos;
		int yPos;
		//Subtract 1 from this int every time a sound button is placed where it belongs
		//When it equals zero all sounds have been placed
		int keysToMap = soundList.size();
		ArrayList<BoardButton> copy = (ArrayList<BoardButton>) soundList.clone();

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
		JButton btnSpace = space.getButton();
		xPos = 10;
		yPos = ((buttonHeight+BUTTON_SPACE)*5) + BUTTON_SPACE;
		btnSpace.setSize(new Dimension (SCREEN_WIDTH-BUTTON_SPACE, buttonHeight));
		btnSpace.setLocation(xPos, yPos);
	}
	
	//Listens to the keyboard keys and plays sounds at touch
	private class ToggleSound implements KeyListener {
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getKeyCode() + ", " + e.getKeyChar());				//testing
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				// Stop all stoppable sounds
				soundManager.stopSounds();
			else {
				if (dialogManager.getEditingMode()) {
					// In editing mode, open the sound in the Edit Sound dialog box
					SoundInfo sound = soundManager.getSound(e.getKeyCode());
					if (sound != null) {
						dialogManager.displayEditSoundDialog(sound);
					}
				}
				else {
					// Otherwise, play the associated sound
					soundManager.playSound(e.getKeyCode());
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
}
