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
	
	private boolean warnModified = false;
	
	private CueMasherPanel panel;
	private JMenuItem saveFile;

	// Constructor
	// n - The name of the frame
	public CueMasherFrame(String n) {
		super(n);
		
		// Warn users if they have unsaved changes before closing the frame
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        if (displayWarning()) {
		        	System.exit(0);
		        }
		        else {
		        	// The user canceled closing the frame, do nothing
		    		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		        }
		    }
		});

		// Create the panel storing all the sound effect buttons
		panel = new CueMasherPanel(this);
		this.getContentPane().add(panel);
		
		// Create the menu bar
		JMenuBar cueMasherMenu = new JMenuBar();

		// Create the file menu
		JMenu fileMenu = new JMenu("File");
		cueMasherMenu.add(fileMenu);
		
		JMenuItem newFile = new JMenuItem("New");
		newFile.addActionListener(new NewListener());
		fileMenu.add(newFile);
		
		JMenuItem openFile = new JMenuItem("Open");
		openFile.addActionListener(new OpenListener());
		fileMenu.add(openFile);
		
		saveFile = new JMenuItem(SAVE_MOD);
		saveFile.addActionListener(new SaveListener());
		fileMenu.add(saveFile);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitListener());
		fileMenu.add(quit);
		
		// Create the edit menu
		JMenu editMenu = new JMenu("Edit");
		cueMasherMenu.add(editMenu);

		JMenuItem newSound = new JMenuItem("Add Sound...");
		newSound.addActionListener(new AddSoundListener());
		editMenu.add(newSound);
		
		/*JCheckBoxMenuItem editingMode = new JCheckBoxMenuItem("Editing mode");
		editMenu.add(editingMode);*/
		
		setJMenuBar(cueMasherMenu);
	}
	
	// Sets the save label to indicate that the project has been modified
	public void setProjectModified() {
		saveFile.setText(SAVE_MOD);
		warnModified = true;
	}
	
	// Saves the contents of the open project to the project file
	public void saveProject() {
		boolean saved = panel.saveFile();
		if (saved) {
			saveFile.setText(SAVE);
			warnModified = false;
		}
	}
	
	// Prompt users to handle unsaved changes before closing the open project
	public boolean displayWarning() {
		boolean proceed = true;
		if (warnModified) {
			int n = JOptionPane.showConfirmDialog(
				    this,
				    "You have unsaved changes. Do you want to save your changes before proceeding?",
				    "Unsaved Changes",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.WARNING_MESSAGE);
			
			if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION) {
				// Cancel the action following this warning
				proceed = false;
			}
			else if (n == JOptionPane.YES_OPTION) {
				saveProject();
			}
		}
		return proceed;
	}
	
	// Creates a new project
	private class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Warn users if they have unsaved changes before creating a new project
			if (displayWarning()) {
				panel.openNewProject();
				saveFile.setText(SAVE_MOD);
				warnModified = false;
			}
		}
	}
	
	// Defines how the Open File menu item behaves when selected
	private class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Warn users if they have unsaved changes before opening another project
			if (displayWarning()) {
				// Allow the user to choose a Cue Masher file to open
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new CueFileFilter());
				int choice = fileChooser.showOpenDialog(CueMasherFrame.this);
				
				if (choice == JFileChooser.APPROVE_OPTION) {
					// Populate the sound effect panel with the contents of the new file
					File file = fileChooser.getSelectedFile();
					panel.loadFile(file.getAbsolutePath());
					saveFile.setText(SAVE);
					warnModified = false;
				}
			}
		}
	}
	
	// Saves the project file when the user selects the Save option in the File menu
	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			saveProject();
		}
	}
	
	// Adds a new sound to the open project when the user selects the Add Sound option in the File Menu
	private class AddSoundListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			panel.getDialogManager().displayNewSoundDialog();
		}
	}
	
	// Save and close the application when the Quit option is selected
	private class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Warn users if they have unsaved changes before closing the frame
			if (displayWarning()) {
				System.exit(0);
			}
		}
	}
}
