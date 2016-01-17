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

	private boolean editingMode;
	
	//Constructor
	// frame - The frame containing this panel
	public CueMasherPanel(CueMasherFrame frame) {
		//Listens to the keyboard
		addKeyListener(new ToggleSound());
		
		// Tweak focus so keyboard listener works
		addFocusListener(new PanelFocusListener());
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
		soundManager = new ProjectFileManager();
		dialogManager = new SoundDialogManager(this);
		
		soundList = new ArrayList<BoardButton>();

		//Create a button for the spacebar
		space = new StopButton(soundManager);
		add(space.getButton());
		
		editingMode = false;
	}
	
	// Set if the application is in editing mode based on the state of the shift key
	public void setEditingMode() {
		try {
			Robot robot = new Robot();
			robot.keyRelease(KeyEvent.VK_SHIFT);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	// Returns if the interface is in sound editing mode
	public boolean getEditingMode() {
		return editingMode || frame.getEditingMode();
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
	
	// Returns the sound associated with the given key code
	// keyCode - The keyboard key code associated with the sound to retrieve
	public SoundInfo getSound(int keyCode) {
		return soundManager.getSound(keyCode);
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
				soundList.remove(s);

				// Update the GUI
				frame.setProjectModified();
				revalidate();
		        repaint();
		        
		        break;
			}
		}
	}

	// Update a sound in the displayed buttons, sound list, and sound file
	// soundClip - An object storing information about the added or updated sound
	public void updateSound(SoundInfo soundClip) {
		
		// Add the sound to the project
		SoundInfo newSound = soundManager.addSound(soundClip);
		
		if (newSound != null) {
			// Add a new button to the GUI
			addSound(newSound);
		}
		
		if (soundManager.getProjectModified()) {
			
			if (newSound == null) {
				// An existing sound was updated
				// Update the button label and tooltip
				for (int s=0; s < soundList.size(); s++) {
					BoardButton button = soundList.get(s);
					if (button.getKeyCode() == soundClip.getKeyCode()) {
						button.updateButtonText();
				        break;
					}
				}
			}
			
			// Update the GUI
			frame.setProjectModified();
			revalidate();
	        repaint();
		}
	}
	
	// Add a button to the GUI for a sound
	// soundInfo - The sound to add to the GUI
	private void addSound(SoundInfo soundInfo) {
		
		//Create a button for the sound
		SoundButton buttonContainer = new SoundButton(dialogManager, soundInfo);
        
		//Add the new sound button to the list of buttons and the panel
		soundList.add(buttonContainer);
		add(buttonContainer.getButton());
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
	
	//Listens to the keyboard input from the user
	private class ToggleSound implements KeyListener {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			System.out.println(keyCode + ", " + e.getKeyChar());				//testing
			
			// When shift is held, the user has entered editing mode
			if (e.isShiftDown()) {
				editingMode = true;
			}
			else {
				editingMode = false;
			}
			
			if (keyCode == KeyEvent.VK_SPACE)
				// Stop all stoppable sounds
				soundManager.stopSounds();
			else {
				if (getEditingMode()) {
					// In editing mode, open the sound in the Edit Sound dialog box
					SoundInfo sound = soundManager.getSound(keyCode);
					if (sound != null) {
						dialogManager.displayEditSoundDialog(sound);
					}
					else if (keyCode != KeyEvent.VK_SHIFT && keyCode != KeyEvent.VK_CAPS_LOCK){
						dialogManager.displayNewSoundDialog();
					}
				}
				else {
					// Otherwise, play the associated sound
					soundManager.playSound(keyCode);
				}
			}
		}
		
		// Exit editing mode when shift is released
		public void keyReleased(KeyEvent e) {
			if (!e.isShiftDown()) {
				editingMode = false;
			}
		}
		
		public void keyTyped(KeyEvent e) {}
	}
	
	// Make sure the editing mode state is up to date when the panel is focused
	private class PanelFocusListener implements FocusListener {

		public void focusGained(FocusEvent arg0) {
			setEditingMode();
		}

		public void focusLost(FocusEvent arg0) {}
	}
}
