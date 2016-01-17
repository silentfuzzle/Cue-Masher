// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher;

import javax.swing.*;
import cuemasherinterface.CueMasherFrame;

//Creates and runs the application
public class CueMasher {
	public static void main(String[] args) {
		//Create the frame
		CueMasherFrame frame = new CueMasherFrame("Cue Masher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Maximize the frame
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		//Make the frame visible
		frame.setVisible(true);
	}
}
