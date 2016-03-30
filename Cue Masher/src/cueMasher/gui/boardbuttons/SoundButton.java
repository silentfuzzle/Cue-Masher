// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.gui.boardbuttons;

import java.awt.Color;
import java.awt.event.*;
import cuemasher.logic.SoundInfo;
import cuemasher.gui.SoundDialogManager;
import javax.sound.sampled.*;

// This class defines a sound board button that plays a user-defined sound.
public class SoundButton extends BoardButton {
	private SoundDialogManager dialogManager;
	private SoundInfo soundInfo;
	
	// Constructor
	// dialogManager - The object that manages all New/Edit Sound Dialog boxes
	// soundInfo - The information about the sound this button plays
	public SoundButton(SoundDialogManager dialogManager, SoundInfo soundInfo) {
		this.dialogManager = dialogManager;
		this.soundInfo = soundInfo;
		updateButtonText();
		
		// The sound file couldn't be opened, color the button to inform the user
		if (!soundInfo.getSoundOpen()) {
			getButton().setBackground(Color.pink);
		}
		else {
			// Listen for when the sound stops and starts
			soundInfo.addSoundListener(new SoundListener());
		}
	}

	// Returns the name of the sound that this button plays
	public String getSoundName() {
		return soundInfo.getSoundName();
	}
	
	// Returns the name of the keyboard key that plays the sound
	public String getKeyName() {
		return soundInfo.getKeyName();
	}
	
	// Returns the key code of the keyboard key that plays this sound
	public int getKeyCode() {
		return soundInfo.getKeyCode();
	}

	// Returns an action that plays the sound when the GUI button is clicked
	protected ActionListener getActionListener() {
		return new ButtonListener();
	}
	
	// Plays the associated sound effect when the GUI button is clicked
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			dialogManager.handleSoundEvent(soundInfo);
		}
	}
	
	// Colors the button to show whether the sound is playing or not
	private class SoundListener implements LineListener {
		public void update(LineEvent event) {
			// Get the color to set the button to
			Color newBackground = null;
			if (soundInfo.getPlaying()) {
				newBackground = Color.green;
			}
			
			Color currBackground = getButton().getBackground();
			if (currBackground != newBackground) {
				// Update the button color if it has changed
				getButton().setBackground(newBackground);
				dialogManager.refreshSoundBoard();
			}
		}
	}
}
