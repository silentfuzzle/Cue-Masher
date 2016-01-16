// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.ActionListener;

import javax.swing.JButton;

// This is the base class for sound board button containers.
public abstract class BoardButton {
	private JButton button;
	
	// Constructor
	// button - The associated button in the GUI
	public BoardButton(JButton button) {
		this.button = button;
		this.button.addActionListener(getActionListener());
		this.button.setFocusable(false);
	}

	//Get the JButton that plays the associated sound
	public JButton getButton() {
		return button;
	}
	
	// Returns the name of the keyboard key associated with this button
	public abstract String getKeyName();
	
	// Returns the key code of the keyboard key associated with this button
	public abstract int getKeyCode();
	
	// Returns the action listener to add to the GUI button
	protected abstract ActionListener getActionListener();
}
