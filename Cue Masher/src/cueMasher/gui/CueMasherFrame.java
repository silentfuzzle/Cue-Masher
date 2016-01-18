// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.gui;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import cuemasher.gui.filefilters.CueFileFilter;

// This class defines the frame of the Cue Masher GUI.
public class CueMasherFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private String SAVE = "Save";
	private String SAVE_MOD = SAVE + "*";
	private String SAVE_NEW = SAVE + "...";
	private String SAVE_ERROR = "An error occurred while saving the project. Make sure that Cue Masher has permission to write to the project's location.";
	
	private boolean warnModified = false;
	
	private CueMasherPanel panel;
	private JMenuItem saveFile;
	private JCheckBoxMenuItem editingMode;

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
		
		JMenuItem openFile = new JMenuItem("Open...");
		openFile.addActionListener(new OpenListener());
		fileMenu.add(openFile);
		
		saveFile = new JMenuItem(SAVE_NEW);
		saveFile.addActionListener(new SaveListener());
		fileMenu.add(saveFile);
		
		JMenuItem saveFileAs = new JMenuItem("Save As...");
		saveFileAs.addActionListener(new SaveAsListener());
		fileMenu.add(saveFileAs);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitListener());
		fileMenu.add(quit);
		
		// Create the edit menu
		JMenu editMenu = new JMenu("Edit");
		cueMasherMenu.add(editMenu);

		JMenuItem newSound = new JMenuItem("Add Sound...");
		newSound.addActionListener(new AddSoundListener());
		editMenu.add(newSound);
		
		editingMode = new JCheckBoxMenuItem("Editing mode");
		editMenu.add(editingMode);
		
		setJMenuBar(cueMasherMenu);
	}
	
	// Returns if editing mode is toggled on through the Edit menu
	public boolean getEditingMode() {
		return editingMode.isSelected();
	}
	
	// Sets the save label to indicate that the project has been modified
	public void setProjectModified() {
		if (saveFile.getText().equalsIgnoreCase(SAVE))
			saveFile.setText(SAVE_MOD);
		warnModified = true;
	}
	
	// Saves the contents of the open project to the project file
	// Returns true if an error occurred while saving the project file
	public boolean saveProject() {
		boolean saved = panel.saveFile();
		return getSaveProjectError(saved);
	}
	
	// Returns true if the project failed to be saved, returns false if the project didn't need to be saved or was saved successfully
	// saved - The saved status returned from the panel
	public boolean getSaveProjectError(boolean saved) {
		if (saved) {
			// Update the Save menu item to reflect that the project file is up to date
			setSaved();
		}
		else if (warnModified) {
			// The project needs to be saved but couldn't be saved
			return true;
		}
		return false;
	}
	
	// Sets the interface to show that the project has been saved
	public void setSaved() {
		saveFile.setText(SAVE);
		warnModified = false;
	}
	
	// Prompt users to handle unsaved changes before closing the open project
	// Returns true if the next action after this warning message should be executed
	public boolean displayWarning() {
		boolean proceed = true;
		if (warnModified) {
			int n = JOptionPane.showConfirmDialog(
				    this,
				    "You have unsaved changes. Do you want to save your changes before closing the project?",
				    "Unsaved Changes",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.WARNING_MESSAGE);
			
			if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION) {
				// Cancel the action following this warning
				proceed = false;
			}
			else if (n == JOptionPane.YES_OPTION) {
				boolean saveFailed = saveProject();
				if (saveFailed) {
					// An error occurred while saving the project, check if the user wishes to proceed closing the project
					n = JOptionPane.showConfirmDialog(this, 
							SAVE_ERROR + " " + "Do you want to continue closing the project?",
							"Save Failed",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE);
					if (n == JOptionPane.NO_OPTION) {
						// Cancel the action following this warning
						proceed = false;
					}
				}
			}
		}
		return proceed;
	}
	
	// Report to the user than an error occurred while saving the project file
	public void displaySaveError() {
		JOptionPane.showMessageDialog(this, SAVE_ERROR);
	}
	
	// Creates a new project
	private class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Warn users if they have unsaved changes before creating a new project
			if (displayWarning()) {
				panel.openNewProject();
				saveFile.setText(SAVE_NEW);
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
					int state = panel.loadFile(file.getAbsolutePath());
					if (state == 0) {
						// An error occurred while opening the project and a new project was opened instead
						saveFile.setText(SAVE_NEW);
						warnModified = false;
					}
					else if (state == 1) {
						// An error occurred while opening the project
						// The project file needs to be updated to make it valid
						saveFile.setText(SAVE);
						setProjectModified();
					}
					else {
						// The project opened normally
						setSaved();
					}
				}
			}
		}
	}
	
	// Saves the project file when the user selects the Save option in the File menu
	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			boolean saveFailed = saveProject();
			if (saveFailed) {
				displaySaveError();
			}
		}
	}
	
	// Saves the project to a new or existing Cue Masher file selected by the user when Save As... is selected
	private class SaveAsListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			boolean saved = panel.saveFileAs();
			boolean saveFailed = getSaveProjectError(saved);
			if (saveFailed) {
				displaySaveError();
			}
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
