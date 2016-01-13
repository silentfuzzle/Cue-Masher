// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

// This class defines the dialog box for creating new and editing existing keyboard-sound mappings.
public class NewSoundDialog extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String DEFAULT_SOUND_PATH = "Select a sound";
	private int DEFAULT_KEY_CODE = -1;
	
	private JFrame containerFrame;
	private CueMasherPanel parentPanel;
	private JFileChooser fileChooser;
	
	private JButton btnSelectSound, btnAddSound, btnCancel;
	private JLabel lblKey, lblFinalKey, lblName, lblWarning;
	private JTextField txtSoundPath, txtKey, txtSoundName;
	private JCheckBox cbxStoppable;
	
	private int enteredKeyCode = DEFAULT_KEY_CODE;
	
	// Constructor
	// f - The frame containing this panel
	// mainPanel - The main interface
	public NewSoundDialog(JFrame f, CueMasherPanel mainPanel) {
		int width = 400;
		int height = 172;
		setPreferredSize(new Dimension(width,height));
		f.setMinimumSize(new Dimension(width+5,height+30));
		
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
		btnAddSound = new JButton("Add Sound");
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
		lblFinalKey = new JLabel("Key");
		add(lblFinalKey);
		lblName = new JLabel("Sound name:");
		add(lblName);
		lblWarning = new JLabel("");
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
		add(cbxStoppable);
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
		//loop.setLocation(102, 106);
		
		btnAddSound.setLocation(10,140);
		btnCancel.setLocation(btnAddSound.getWidth()+20,140);
		lblWarning.setLocation(btnAddSound.getWidth()+btnCancel.getWidth()+30,145);
	}
	
	// Resets the key the user has entered to map a sound to
	public void resetEnteredKey() {
		txtKey.setText("");
		lblFinalKey.setText("");
		enteredKeyCode = DEFAULT_KEY_CODE;
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
				txtSoundPath.setText(file.getAbsolutePath());
				
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
			if (enteredKeyCode == DEFAULT_KEY_CODE || lblFinalKey.getText().isEmpty() || !parentPanel.checkValidKey(keyName)) {
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
			
			// Add the sound to the interface
			parentPanel.addSound(soundPath, enteredKeyCode, keyName, soundName, stoppable);
			
			// Close this window
			containerFrame.dispose();
		}
	}
	
	// Close the dialog box without saving the user's changes
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			containerFrame.dispose();
		}
	}
}
