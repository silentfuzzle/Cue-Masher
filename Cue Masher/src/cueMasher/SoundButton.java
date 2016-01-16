// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

// This class defines a sound board button that plays a user-defined sound.
public class SoundButton extends BoardButton {
	private SoundDialogManager dialogManager;
	private SoundInfo soundInfo;
	
	// Constructor
	// dialogManager - The object that manages all New/Edit Sound Dialog boxes
	// soundInfo - The information about the sound this button plays
	// button - The GUI button that plays the sound
	public SoundButton(SoundDialogManager dialogManager, SoundInfo soundInfo, JButton button) {
		super(button);
		this.dialogManager = dialogManager;
		this.soundInfo = soundInfo;
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
			if (dialogManager.getEditingMode()) {
				// Open the sound editing dialog in editing mode
				dialogManager.displayEditSoundDialog(soundInfo);
			}
			else {
				// Otherwise, play the sound
				soundInfo.play();
			}
		}
	}
}
