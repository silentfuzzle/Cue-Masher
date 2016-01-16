// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.util.HashSet;
import javax.swing.JFrame;

// This class manages all open New/Edit Sound dialog boxes.
public class SoundDialogManager {
	private CueMasherPanel panel;
	private HashSet<NewSoundDialog> openDialogs;
	private boolean editingMode;
	
	// Constructor
	// panel - The main interface
	public SoundDialogManager(CueMasherPanel panel) {
		this.panel = panel;
		openDialogs = new HashSet<NewSoundDialog>();
		editingMode = false;
	}
	
	// Sets if the interface is in sound editing mode
	// editingMode - True if in editing mode, false if not
	public void setEditingMode(boolean editingMode) {
		this.editingMode = editingMode;
	}
	
	// Returns if the interface is in sound editing mode
	public boolean getEditingMode() {
		return editingMode;
	}
	
	// Removes the given Sound dialog box from the list of open sound dialog boxes
	public void closeSoundDialog(NewSoundDialog dialog) {
		openDialogs.remove(dialog);
	}
	
	// Displays a New Sound dialog box
	public void displayNewSoundDialog() {
		JFrame selectDialog = new JFrame("Add Sound");
		NewSoundDialog dialog = new NewSoundDialog(selectDialog, panel);
		displaySoundDialog(selectDialog, dialog);
	}
	
	// Displays an Edit Sound dialog box for the given sound
	// soundInfo - The sound to display in the Edit Sound dialog
	public void displayEditSoundDialog(SoundInfo soundInfo) {
		JFrame selectDialog = new JFrame("Edit Sound");
		NewSoundDialog dialog = new NewSoundDialog(selectDialog, panel, soundInfo);
		displaySoundDialog(selectDialog, dialog);
	}
	
	// Performs common actions before displaying a New/Edit Sound dialog box
	// selectDialog - The frame that encapsulates the dialog box
	// dialog - The object defining the New/Edit Sound dialog box
	private void displaySoundDialog(JFrame selectDialog, NewSoundDialog dialog) {
		// Add the dialog to the list of open dialogs
		openDialogs.add(dialog);
		
		// Setup and display the dialog
		selectDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		selectDialog.getContentPane().add(dialog);
		selectDialog.pack();
		selectDialog.setVisible(true);
	}
}
