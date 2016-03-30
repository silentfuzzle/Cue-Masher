// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.util.*;
import cuemasher.logic.*;
import cuemasher.gui.boardbuttons.*;
import cuemasher.gui.filefilters.CueFileFilter;

//This class defines the main Cue Masher GUI.
public class CueMasherPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int BUTTON_SPACE = 10;	//The space to leave between buttons and the edge of the screen
	
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
		
		this.frame = frame;
		this.frame.setMinimumSize(new Dimension(500, 500));
		
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
	// Returns 0 (an error occurred and a new project was opened), 1 (an error occurred), 2 (the project opened normally)
	public int loadFile(String textFilePath) {
		clearBoard();
		
		// Get the sounds contained in the given project file
		ArrayList<SoundInfo> readSounds = soundManager.readFile(textFilePath);
		
		// Add buttons to the interface for each sound in the file
		for (int s=0; s < readSounds.size(); s++) {
			SoundInfo curr = readSounds.get(s);
			if (checkValidKey(curr.getKeyName())) {
				addSound(readSounds.get(s));
			}
			else {
				soundManager.deleteSound(curr.getKeyCode());
			}
		}
		
		// Report if an error occurred while opening the project
		// Allow the user to close the project without proceeding
		if (soundManager.getProjectModified()) {
			int choice = JOptionPane.showConfirmDialog(frame, "One or more errors were encountered while reading the project file. " +
					"The file may be missing, not well-formatted, or not a Cue Masher project file. Do you want to proceed?",
				    "Keep Project Open?",
				    JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.NO_OPTION) {
				openNewProject();
				return 0;
			}
			return 1;
		}
		return 2;
	}
	
	// Save the open project if needed
	public boolean saveFile() {
		if (!soundManager.isProjectOpen()) {
			return saveFileAs();
		}
		else {
			return soundManager.saveFile();
		}
	}
	
	// Saves the open project to a new file selected by the user
	public boolean saveFileAs() {
		// Filter the file chooser by Cue Masher files
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new CueFileFilter());
		
		// Display the existing file location by default if it exists
		String filePath = soundManager.getProjectFilePath();
		if (filePath != null) {
			File openProjectFile = new File(filePath);
			fileChooser.setSelectedFile(openProjectFile);
		}
		
		int choice = fileChooser.showSaveDialog(CueMasherPanel.this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			// Get the name of the file the user selected
			File file = fileChooser.getSelectedFile();
			filePath = file.getAbsolutePath().trim();
			
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

		//Calculate the button width and height so the buttons span the screen
		int screenWidth = getWidth();
		int screenHeight = getHeight();
		int[] buttonWidth = new int[5];
		buttonWidth[0] = (screenWidth-(BUTTON_SPACE*13))/13;
		buttonWidth[1] = buttonWidth[0];
		buttonWidth[2] = (screenWidth-(BUTTON_SPACE*11))/11;
		buttonWidth[3] = (screenWidth-(BUTTON_SPACE*11))/10;
		buttonWidth[4] = (screenWidth-(BUTTON_SPACE*11))/10;
		int buttonHeight = (screenHeight-(BUTTON_SPACE*7))/6;
		
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
		btnSpace.setSize(new Dimension (screenWidth-(BUTTON_SPACE*2), buttonHeight));
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
				SoundInfo sound = soundManager.getSound(keyCode);
				if (sound != null) {
					// Play, stop, or edit the sound
					dialogManager.handleSoundEvent(sound);
				}
				else if (getEditingMode() && keyCode != KeyEvent.VK_SHIFT && 
						keyCode != KeyEvent.VK_CAPS_LOCK) {
					// Create a new sound
					dialogManager.displayNewSoundDialog();
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
