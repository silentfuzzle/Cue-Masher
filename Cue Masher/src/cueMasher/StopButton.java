// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.*;

// This class defines a sound board button to use to stop all stoppable sounds.
public class StopButton extends BoardButton {
	private static String SPACEBAR = "Spacebar";
	
	private ProjectFileManager manager;
	
	// Constructor
	// manager - The project manager object
	public StopButton(ProjectFileManager manager) {
		this.manager = manager;
		updateButtonText();
	}

	// Returns the action this button performs
	public String getSoundName() {
		return "Stop";
	}
	
	// Returns the name of the button that stops all stoppable sounds
	public String getKeyName() {
		return SPACEBAR;
	}
	
	// Returns the key code of the keyboard key that stops all stoppable sounds
	public int getKeyCode() {
		return KeyEvent.VK_SPACE;
	}

	// Returns an action listener that stops all sounds to apply to the GUI button
	protected ActionListener getActionListener() {
		return new ButtonListener();
	}
	
	// Stops all stoppable sounds when the GUI button is clicked
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			manager.stopSounds();
		}
	}
}
