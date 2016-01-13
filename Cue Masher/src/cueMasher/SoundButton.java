// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

// This class defines a sound board button that plays a user-defined sound.
public class SoundButton extends BoardButton {
	private SoundInfo soundInfo;
	
	// Constructor
	// soundInfo - The information about the sound this button plays
	// button - The GUI button that plays the sound
	public SoundButton(SoundInfo soundInfo, JButton button) {
		super(button);
		this.soundInfo = soundInfo;
	}
	
	// Returns the name of the keyboard key that plays the sound
	public String getKeyName() {
		return soundInfo.getKeyName();
	}

	// Returns an action that plays the sound when the GUI button is clicked
	protected ActionListener getActionListener() {
		return new ButtonListener();
	}
	
	// Plays the associated sound effect when the GUI button is clicked
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			soundInfo.play();
		}
	}
}
