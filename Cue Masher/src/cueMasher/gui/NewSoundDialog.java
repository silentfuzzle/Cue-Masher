// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import cuemasher.logic.SoundInfo;
import cuemasher.gui.filefilters.SoundFilter;

// This class defines the dialog box for creating new and editing existing keyboard-sound mappings.
public class NewSoundDialog extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String DEFAULT_SOUND_PATH = "Select a sound";
	
	private JFrame containerFrame;
	private CueMasherPanel parentPanel;
	private JFileChooser fileChooser;
	
	private JButton btnSelectSound, btnAddSound, btnDeleteSound, btnCopySound, btnCancel;
	private JLabel lblKey, lblFinalKey, lblName, lblWarning;
	private JTextField txtSoundPath, txtKey, txtSoundName;
	private JCheckBox cbxStoppable, cbxToggleable;
	private SoundInfo soundInfo;
	
	private int enteredKeyCode = SoundInfo.DEFAULT_KEY_CODE;
	
	// Constructor for editing an existing sound
	// f - The frame containing this panel
	// mainPanel - The main interface
	// soundInfo - The sound being edited
	public NewSoundDialog(JFrame f, CueMasherPanel mainPanel, SoundInfo soundInfo) {
		this(f, mainPanel);
		
		btnAddSound.setText("Apply Changes");
		
		if (soundInfo.getKeyCode() != -1) {
			// This existing sound is associated with a key
			
			// Define the button to remove the sound from the board
			btnDeleteSound = new JButton("Delete");
			btnDeleteSound.addActionListener(new DeleteListener());
			add(btnDeleteSound);
			
			btnCopySound = new JButton("Copy");
			btnCopySound.addActionListener(new CopyListener());
			add(btnCopySound);
			
			// Display the information about the key associated with the sound
			this.enteredKeyCode = soundInfo.getKeyCode();
			String keyName = soundInfo.getKeyName();
			if (keyName.length() > 1)
				txtKey.setText(keyName.substring(1));
			else
				txtKey.setText(keyName);
			lblFinalKey.setText(keyName);
			
			this.soundInfo = soundInfo;
		}

		// Display the current information about the sound being edited
		setPath(soundInfo.getPath());
		txtSoundName.setText(soundInfo.getSoundName());
		cbxStoppable.setSelected(soundInfo.isStoppable());
		cbxToggleable.setSelected(soundInfo.isToggleable());
	}
	
	// Constructor for creating a new sound
	// f - The frame containing this panel
	// mainPanel - The main interface
	public NewSoundDialog(JFrame f, CueMasherPanel mainPanel) {
		int width = 400;
		int height = 195;
		setPreferredSize(new Dimension(width,height));
		f.setMinimumSize(new Dimension(width+5,height+30));
		
		// Inform the sound dialog manager that this dialog box is closing before closing it
		f.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        closeFrame();
		    }
		});
		
		containerFrame = f;
		parentPanel = mainPanel;
		
		// Define the file chooser used to select the sound file
		fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new SoundFilter());
		
        // Define the button to invoke the file chooser
		btnSelectSound = new JButton("Browse...");
		btnSelectSound.addActionListener(new PathListener());
		add(btnSelectSound);
		
		// Define the button to add the sound to the sound board
		btnAddSound = new JButton("Add");
		btnAddSound.addActionListener(new UpdateSoundListener());
		add(btnAddSound);
		
		// Define the button to cancel creating or editing the sound
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new CancelListener());
		add(btnCancel);
		
		// Define the text field to display the path to the sound file
		txtSoundPath = new JTextField();
		txtSoundPath.setEditable(false);
		txtSoundPath.setText(DEFAULT_SOUND_PATH);
		Dimension size = new Dimension(280,20);
		txtSoundPath.setPreferredSize(size);
		txtSoundPath.setMaximumSize(size);
		add(txtSoundPath);
		
		// Define explanatory labels
		lblKey = new JLabel("Cue key:");
		add(lblKey);
		lblFinalKey = new JLabel("");
		add(lblFinalKey);
		lblName = new JLabel("Sound name:");
		add(lblName);
		lblWarning = new JLabel();
		lblWarning.setForeground(Color.red);
		add(lblWarning);
		
		// Define the text field for entering the key to use to play the sound with
		txtKey = new JTextField();
		txtKey.addKeyListener(new KeyCodeListener());
		txtKey.setDocument(new JTextFieldLimit(1));
		add(txtKey);
		
		// Define the text field for entering the short name of the sound
		txtSoundName = new JTextField();
		txtSoundName.setDocument(new JTextFieldLimit(20));
		add(txtSoundName);
		
		// Define the checkbox determining whether the sound is stoppable or not
		cbxStoppable = new JCheckBox("Stoppable");
		cbxStoppable.setToolTipText("This sound can be stopped by pressing the Spacebar key or button.");
		add(cbxStoppable);
		cbxToggleable = new JCheckBox("Toggleable");
		cbxToggleable.setToolTipText("This sound can be stopped and started by pressing its corresponding key or button.");
		add(cbxToggleable);
		/*loop = new JCheckBox("Loop");
		add(loop);*/
	}
	
	// Display the dialog box controls
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		btnSelectSound.setLocation(10, 10);
		txtSoundPath.setLocation(btnSelectSound.getWidth()+20, 13);
		
		lblKey.setLocation(10,46);
		txtKey.setLocation(lblKey.getWidth()+20,46);
		txtKey.setSize(20,20);
		lblFinalKey.setLocation(lblKey.getWidth()+txtKey.getWidth()+30,46);
		
		lblName.setLocation(10,76);
		txtSoundName.setLocation(lblName.getWidth()+20,76);
		txtSoundName.setSize(190,20);
		
		cbxStoppable.setLocation(10,106);
		cbxToggleable.setLocation(20+cbxStoppable.getWidth(),106);
		//loop.setLocation(102, 106);
		
		btnAddSound.setLocation(10,140);
		if (btnDeleteSound != null) {
			// This dialog box is in editing mode
			// Make sure there is room for the Delete and Copy buttons
			btnDeleteSound.setLocation(btnAddSound.getWidth()+20,140);
			btnCopySound.setLocation(btnAddSound.getWidth()+btnDeleteSound.getWidth()+30,140);
			btnCancel.setLocation(btnAddSound.getWidth()+btnDeleteSound.getWidth()+btnCopySound.getWidth()+40,140);
		}
		else {
			// A new sound is being defined, the delete button isn't needed
			btnCancel.setLocation(btnAddSound.getWidth()+20,140);
		}
		
		lblWarning.setLocation(10,170);
	}
	
	// Sets the path to the selected sound file
	public void setPath(String soundPath) {
		txtSoundPath.setText(soundPath);
		txtSoundPath.setToolTipText(soundPath);
	}
	
	// Resets the key the user has entered to map a sound to
	public void resetEnteredKey() {
		txtKey.setText("");
		lblFinalKey.setText("");
		enteredKeyCode = SoundInfo.DEFAULT_KEY_CODE;
	}
	
	// Displays the given sound in a dummy object in a new Sound Dialog
	// existingSound - The sound to copy to a new Sound Dialog
	// Returns if the sound was successfully displayed
	public boolean copySoundToNewDialog(SoundInfo existingSound) {
		SoundInfo copy = new SoundInfo(existingSound.getPath(), 
				SoundInfo.DEFAULT_KEY_CODE, "", 
				existingSound.getSoundName(), 
				existingSound.getStoppable(),
				existingSound.getToggleable());
		
		// Attempt to display the sound in a new Sound Dialog
		boolean displayed = parentPanel.getDialogManager().displayEditSoundDialog(copy);
		if (!displayed) {
			lblWarning.setText("There are too many sound editors open to continue with this action.");
		}
		return displayed;
	}
	
	// Brings this dialog into focus
	public void focusFrame() {
		containerFrame.toFront();
	}
	
	// Inform the sound dialog manager that this dialog box is closing and close it
	public void closeFrame() {
		int keyCode = -1;
		if (soundInfo != null) {
			keyCode = soundInfo.getKeyCode();
		}
		
		parentPanel.getDialogManager().closeSoundDialog(this, keyCode);
		containerFrame.dispose();
	}

	// Saves the key code associated with the key to play the sound with
	private class KeyCodeListener implements KeyListener {
		public void keyPressed(KeyEvent e) {}
		
		public void keyReleased(KeyEvent e) {
			String keyName = String.valueOf(e.getKeyChar());
			String existingKeyName = txtKey.getText();
			if (!e.isShiftDown())
			{
				int keyCode = e.getKeyCode();
				if (keyName.equalsIgnoreCase(existingKeyName) &&
						keyCode != KeyEvent.VK_SPACE) {
					enteredKeyCode = keyCode;
					
					// Set the final key name that will be displayed in the GUI
					if (keyCode == KeyEvent.VK_NUMPAD0 || keyCode == KeyEvent.VK_NUMPAD1 ||
							keyCode == KeyEvent.VK_NUMPAD2 || keyCode == KeyEvent.VK_NUMPAD3 ||
							keyCode == KeyEvent.VK_NUMPAD4 || keyCode == KeyEvent.VK_NUMPAD5 ||
							keyCode == KeyEvent.VK_NUMPAD6 || keyCode == KeyEvent.VK_NUMPAD7 ||
							keyCode == KeyEvent.VK_NUMPAD8 || keyCode == KeyEvent.VK_NUMPAD9) {
						keyName = "N" + keyName;
					}
					lblFinalKey.setText(keyName);
				}
				else {
					// The Spacebar isn't a valid key that sounds can be mapped to
					txtKey.setText(txtKey.getText().trim());
				}
			}
			else if (keyName.equalsIgnoreCase(existingKeyName)) {
				resetEnteredKey();
			}
		}
		
		public void keyTyped(KeyEvent e) {}
	}
	
	// Displays a file chooser where the user can select the sound file they would like to add to the sound board
	private class PathListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int choice = fileChooser.showOpenDialog(NewSoundDialog.this);
			
			if (choice == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				setPath(file.getAbsolutePath());
				
				// Update the contents of the sound file text box
				revalidate();
				repaint();
			}
		}
	}
	
	// Adds a sound to the sound board when the user has finished editing the sound
	private class UpdateSoundListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			// Make sure that a sound file has been selected
			String soundPath = txtSoundPath.getText();
			if (soundPath.equalsIgnoreCase(DEFAULT_SOUND_PATH))
			{
				lblWarning.setText("Please select a sound file.");
				return;
			}
			
			// Make sure that a key has been entered
			String keyName = lblFinalKey.getText();
			if (enteredKeyCode == SoundInfo.DEFAULT_KEY_CODE || lblFinalKey.getText().isEmpty() || !parentPanel.checkValidKey(keyName)) {
				lblWarning.setText("Please enter a valid key.");
				resetEnteredKey();
				return;
			}
			
			// Make sure that a sound name has been entered
			String soundName = txtSoundName.getText().trim();
			if (soundName.isEmpty())
			{
				lblWarning.setText("Please enter a sound name.");
				txtSoundName.setText(txtSoundName.getText().trim());
				return;
			}

			// Get whether the sound is stoppable or not
			int stoppable = 0;
			if (cbxStoppable.isSelected()) {
				stoppable = 1;
			}
			
			// Get whether the sound is toggleable or not
			int toggleable = 0;
			if (cbxToggleable.isSelected()) {
				toggleable = 1;
			}
			
			// Check if the key the user has selected is already associated with a sound
			// Don't complain if the user is updating an existing sound and not changing its key
			SoundInfo existingSound = parentPanel.getSound(enteredKeyCode);
			if (existingSound != null && (soundInfo == null || (soundInfo.getKeyCode() != enteredKeyCode))) {
				lblWarning.setText("");
				
				// Ask the user to resolve the conflict
				Object[] options = new Object[] {"Replace",
		                    "Replace and Edit",
		                    "Cancel"};
				int n = JOptionPane.showOptionDialog(
					    containerFrame,
					    "You've selected a key that is already associated with a sound. What would you like to do?",
					    "Resolve Key Conflict",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null, options, options[2]);
				
				if (n == 0) {
					// The other key and sound is being replaced
					// Make sure the sound associated with the other key isn't displayed in another sound dialog
					parentPanel.getDialogManager().closeSoundDialog(existingSound.getKeyCode());
				}
				else if (n == 1) {
					// The other key is being replaced while allowing the user to assign a new key to its current sound
					// Make sure the sound associated with the other key isn't displayed in another sound dialog
					parentPanel.getDialogManager().closeSoundDialog(existingSound.getKeyCode());
					
					// Copy the sound to a dummy object and display it in a new Edit Sound dialog
					boolean displayed = copySoundToNewDialog(existingSound);
					if (!displayed) {
						// There were too many open sound editors to open a new editor
						return;
					}
				}
				else if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION) {
					// The user canceled creating the sound
					return;
				}
			}
			
			// The user has changed what key is used to play the sound being edited
			// Remove the current key from the sound board
			if (soundInfo != null && enteredKeyCode != soundInfo.getKeyCode()) {
				parentPanel.deleteSound(soundInfo.getKeyCode());
			}
			
			// Add or update the sound in the interface
			SoundInfo soundClip = new SoundInfo(soundPath, enteredKeyCode, keyName, soundName, stoppable, toggleable);
			parentPanel.updateSound(soundClip);
			
			closeFrame();
		}
	}
	
	// Delete this sound from the interface and sound list
	private class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			parentPanel.deleteSound(soundInfo.getKeyCode());
			closeFrame();
		}
	}
	
	// Copy this sound a new sound editor where the user to assign another key to it
	private class CopyListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			boolean displayed = copySoundToNewDialog(soundInfo);
			if (displayed) {
				lblWarning.setText("");
			}
		}
	}
	
	// Close the dialog box without saving the user's changes
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			closeFrame();
		}
	}
}
