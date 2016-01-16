// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.ActionListener;

import javax.swing.JButton;

// This is the base class for sound board button containers.
public abstract class BoardButton {
	private JButton button;
	
	// Constructor
	public BoardButton() {
		this.button = new JButton("");
		this.button.addActionListener(getActionListener());
		this.button.setFocusable(false);
	}

	//Get the JButton that plays the associated sound
	public JButton getButton() {
		return button;
	}
	
	// Update the text and tool tip displayed in the GUI to reflect the latest sound this button is associated with
	public void updateButtonText() {
		String text = getSoundName() + " (" + getKeyName() + ")";
		setButtonText(text);
	}
	
	// Set the text displayed in the button in the GUI and it's tooltip
	private void setButtonText(String text) {
		button.setText(text);
		button.setToolTipText(text);
	}
	
	public abstract String getSoundName();
	
	// Returns the name of the keyboard key associated with this button
	public abstract String getKeyName();
	
	// Returns the key code of the keyboard key associated with this button
	public abstract int getKeyCode();
	
	// Returns the action listener to add to the GUI button
	protected abstract ActionListener getActionListener();
}
