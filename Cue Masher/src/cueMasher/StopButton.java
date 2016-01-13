// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

// This class defines a sound board button to use to stop all stoppable sounds.
public class StopButton extends BoardButton {
	private ProjectFileManager manager;
	
	// Constructor
	// manager - The project manager object
	// button - The GUI button in the interface
	public StopButton(ProjectFileManager manager, JButton button) {
		super(button);
		this.manager = manager;
	}
	
	// Returns the name of the button that stops all stoppable sounds
	public String getKeyName() {
		return CueMasherPanel.SPACEBAR;
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
