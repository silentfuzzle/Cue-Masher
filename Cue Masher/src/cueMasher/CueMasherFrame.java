// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;

// This class defines the frame of the Cue Masher GUI.
public class CueMasherFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private String SAVE = "Save";
	private String SAVE_MOD = SAVE + "*";
	
	private CueMasherPanel panel;
	private JMenuItem saveFile;

	// Constructor
	// n - The name of the frame
	public CueMasherFrame(String n) {
		super(n);

		// Create the panel storing all the sound effect buttons
		panel = new CueMasherPanel(this);
		this.getContentPane().add(panel);
		
		// Create the file menu
		JMenuBar cueMasherMenu = new JMenuBar();
		JMenu menu = new JMenu("File");
		cueMasherMenu.add(menu);
		
		JMenuItem openFile = new JMenuItem("Open");
		openFile.addActionListener(new OpenListener());
		menu.add(openFile);
		
		saveFile = new JMenuItem(SAVE_MOD);
		saveFile.addActionListener(new SaveListener());
		menu.add(saveFile);

		JMenuItem newSound = new JMenuItem("Add Sound...");
		newSound.addActionListener(new AddSoundListener());
		menu.add(newSound);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitListener());
		menu.add(quit);
		
		setJMenuBar(cueMasherMenu);
	}
	
	// Sets the save label to indicate that the project has been modified
	public void setProjectModified() {
		saveFile.setText(SAVE_MOD);
	}
	
	// Defines how the Open File menu item behaves when selected
	private class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Allow the user to choose a Cue Masher file to open
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new CueFileFilter());
			int choice = fileChooser.showOpenDialog(CueMasherFrame.this);
			
			if (choice == JFileChooser.APPROVE_OPTION) {
				// Populate the sound effect panel with the contents of the new file
				File file = fileChooser.getSelectedFile();
				panel.loadFile(file.getAbsolutePath());
				saveFile.setText(SAVE);
			}
		}
	}
	
	// Saves the project file when the user selects the Save option in the File menu
	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			boolean saved = panel.saveFile();
			if (saved) {
				saveFile.setText(SAVE);
			}
		}
	}
	
	// Adds a new sound to the open project when the user selects the Add Sound option in the File Menu
	private class AddSoundListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame selectDialog = new JFrame("Add Sound");
			selectDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			selectDialog.getContentPane().add(new NewSoundDialog(selectDialog, panel));
			selectDialog.pack();
			selectDialog.setVisible(true);
		}
	}
	
	// Defines how the Quit menu item behaves when selected
	private class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Close the application
			System.exit(0);
		}
	}
}
