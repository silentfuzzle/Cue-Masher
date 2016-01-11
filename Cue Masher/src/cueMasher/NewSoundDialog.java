//http://download.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
//http://download.oracle.com/javase/tutorial/uiswing/examples/components/CheckBoxDemoProject/src/components/CheckBoxDemo.java

package cueMasher;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class NewSoundDialog extends JPanel {
	JFrame containerFrame;
	private JFileChooser fileChooser;
	private JButton selectSound, addSound, cancel;
	private JLabel lblKey, lblName;
	private JTextField soundPath, key, soundName;
	private JCheckBox stoppable, loop;
	
	public NewSoundDialog(JFrame f) {
		setPreferredSize(new Dimension(300,172));
		containerFrame = f;
		
		fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new SoundFilter());
		
		ButtonListener btnListener = new ButtonListener();
		selectSound = new JButton("Browse...");
		selectSound.addActionListener(btnListener);
		add(selectSound);
		addSound = new JButton("Add Sound");
		addSound.addActionListener(btnListener);
		add(addSound);
		cancel = new JButton("Cancel");
		cancel.addActionListener(btnListener);
		add(cancel);
		
		soundPath = new JTextField();
		soundPath.setEditable(false);
		soundPath.setText("Select a sound");
		add(soundPath);
		
		lblKey = new JLabel("Cue key:");
		add(lblKey);
		lblName = new JLabel("Sound name:");
		add(lblName);
		
		key = new JTextField();
		add(key);
		soundName = new JTextField();
		add(soundName);
		
		stoppable = new JCheckBox("Stoppable");
		add(stoppable);
		loop = new JCheckBox("Loop");
		add(loop);
	}
	
	public void paintComponent(Graphics page) {
		System.out.println(cancel.getLocation() + "," + addSound.getLocation() + "," + loop.getLocation());
		selectSound.setLocation(10, 10);
		soundPath.setLocation(107, 13);
		lblKey.setLocation(10,46);
		key.setLocation(lblKey.getWidth()+20,46);
		key.setSize(20,20);
		lblName.setLocation(10,76);
		soundName.setLocation(lblName.getWidth()+20,76);
		soundName.setSize(190,20);
		stoppable.setLocation(10,106);
		loop.setLocation(102, 106);
		addSound.setLocation(10,140);
		cancel.setLocation(115,140);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == selectSound) {
				int choice = fileChooser.showOpenDialog(NewSoundDialog.this);
				
				if (choice == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					soundPath.setText(file.getAbsolutePath());
				}
			}
			else {
				containerFrame.dispose();
			}
		}
	}
	
	private class CheckListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			
		}
	}
}
