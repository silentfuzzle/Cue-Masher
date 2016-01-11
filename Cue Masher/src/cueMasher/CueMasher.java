// http://www.devdaily.com/blog/post/java/maximize-frame-jframe-in-java

package cueMasher;

import javax.swing.*;

//Creates and runs the application
public class CueMasher {
	public static void main(String[] args) {
		//Create the frame
		CueMasherFrame frame = new CueMasherFrame("Cue Masher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Create the panel and add it to the frame
		frame.getContentPane().add(new CueMasherPanel());
		//Maximize the frame
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		//Make the frame visible
		frame.setVisible(true);
	}
}
