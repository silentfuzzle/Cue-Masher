//http://download.oracle.com/javase/tutorial/uiswing/examples/components/InternalFrameDemoProject/src/components/InternalFrameDemo.java

package cueMasher;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class CueMasherFrame extends JFrame {
	public CueMasherFrame(String n) {
		super(n);

		SelectionListener menuListener = new SelectionListener();
		
		JMenuBar cueMasherMenu = new JMenuBar();
		JMenu menu = new JMenu("File");
		cueMasherMenu.add(menu);

		JMenuItem newSound = new JMenuItem("New");
		newSound.addActionListener(menuListener);
		newSound.setActionCommand("new");
		menu.add(newSound);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(menuListener);
		quit.setActionCommand("quit");
		menu.add(quit);
		
		setJMenuBar(cueMasherMenu);
	}
	
	private class SelectionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if ("new".equals(event.getActionCommand())) {
				JFrame selectDialog = new JFrame("Add Sound");
				selectDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				selectDialog.getContentPane().add(new NewSoundDialog(selectDialog));
				selectDialog.pack();
				selectDialog.setVisible(true);
			}
			else
				System.exit(0);
		}
	}

}
