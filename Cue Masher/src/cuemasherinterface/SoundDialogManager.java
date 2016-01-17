// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.util.*;
import javax.swing.JFrame;

// This class manages all open New/Edit Sound dialog boxes.
public class SoundDialogManager {
	private int MAX_NEW_DIALOGS = 5;
	
	private CueMasherPanel panel;
	private HashMap<Integer, NewSoundDialog> openEditDialogs;
	private HashSet<NewSoundDialog> openNewDialogs;
	
	// Constructor
	// panel - The main interface
	public SoundDialogManager(CueMasherPanel panel) {
		this.panel = panel;
		openEditDialogs = new HashMap<Integer, NewSoundDialog>();
		openNewDialogs = new HashSet<NewSoundDialog>();
	}
	
	// Returns if the interface is in sound editing mode
	public boolean getEditingMode() {
		return panel.getEditingMode();
	}
	
	// Removes the given Sound dialog box from the list of open sound dialog boxes
	// dialog - The dialog box to remove from the open dialogs
	// keyCode - The keyCode the Edit Sound dialog box is editing or -1 if it is a New Sound dialog box
	public void closeSoundDialog(NewSoundDialog dialog, int keyCode) {
		if (keyCode == SoundInfo.DEFAULT_KEY_CODE) {
			openNewDialogs.remove(dialog);
		}
		else {
			if (openEditDialogs.containsKey(keyCode)) {
				openEditDialogs.remove(keyCode);
			}
		}
	}
	
	// Removes an Edit Sound dialog from the list if it exists
	// keyCode - The keyboard key code associated with the dialog to remove
	public void closeSoundDialog(int keyCode) {
		if (openEditDialogs.containsKey(keyCode)) {
			NewSoundDialog dialog = openEditDialogs.get(keyCode);
			dialog.closeFrame();
		}
	}
	
	// Displays a New Sound dialog box
	public void displayNewSoundDialog() {
		if (openNewDialogs.size() >= MAX_NEW_DIALOGS) {
			// The user has five or more New Sound dialog boxes open, bring one into focus
			NewSoundDialog dialog = openNewDialogs.iterator().next();
			dialog.focusFrame();
		}
		else {
			// Open a New Sound dialog box
			JFrame selectDialog = new JFrame("Add Sound");
			NewSoundDialog dialog = new NewSoundDialog(selectDialog, panel);
			openNewDialogs.add(dialog);
			displaySoundDialog(selectDialog, dialog);
		}
	}
	
	// Displays an Edit Sound dialog box for the given sound
	// soundInfo - The sound to display in the Edit Sound dialog
	public void displayEditSoundDialog(SoundInfo soundInfo) {
		int keyCode = soundInfo.getKeyCode();
		if (openEditDialogs.containsKey(keyCode)) {
			// The user is already editing a sound associated with this key, bring it into focus
			NewSoundDialog dialog = openEditDialogs.get(keyCode);
			dialog.focusFrame();
		}
		else {
			// Open an Edit Sound dialog box associated with the given sound
			JFrame selectDialog = new JFrame("Edit Sound");
			NewSoundDialog dialog = new NewSoundDialog(selectDialog, panel, soundInfo);
			openEditDialogs.put(keyCode, dialog);
			displaySoundDialog(selectDialog, dialog);
		}
	}
	
	// Performs common actions before displaying a New/Edit Sound dialog box
	// selectDialog - The frame that encapsulates the dialog box
	// dialog - The object defining the New/Edit Sound dialog box
	private void displaySoundDialog(JFrame selectDialog, NewSoundDialog dialog) {
		selectDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		selectDialog.getContentPane().add(dialog);
		selectDialog.pack();
		selectDialog.setVisible(true);
	}
}
