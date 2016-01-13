// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

// This class defines the frame of the Cue Masher GUI.
public class CueMasherFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CueMasherPanel panel;

	// Constructor
	// n The name of the frame
	public CueMasherFrame(String n) {
		super(n);

		// Create the panel storing all the sound effect buttons
		panel = new CueMasherPanel();
		this.getContentPane().add(panel);
		
		// Create the file menu
		JMenuBar cueMasherMenu = new JMenuBar();
		JMenu menu = new JMenu("File");
		cueMasherMenu.add(menu);

		/*JMenuItem newSound = new JMenuItem("New");
		newSound.addActionListener(new NewListener());
		menu.add(newSound);*/
		
		JMenuItem openFile = new JMenuItem("Open");
		openFile.addActionListener(new OpenListener());
		openFile.setActionCommand("open");
		menu.add(openFile);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitListener());
		quit.setActionCommand("quit");
		menu.add(quit);
		
		setJMenuBar(cueMasherMenu);
	}
	
	private class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame selectDialog = new JFrame("Add Sound");
			selectDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			selectDialog.getContentPane().add(new NewSoundDialog(selectDialog, panel));
			selectDialog.pack();
			selectDialog.setVisible(true);
		}
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
				panel.populatePanelFromTextFile(file.getAbsolutePath());
			}
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
