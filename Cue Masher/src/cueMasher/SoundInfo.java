package cueMasher;

import javax.sound.sampled.Clip;
import javax.swing.JButton;

//Stores a sound: it's path, button, player, keycode, label, and associated information
public class SoundInfo {
	private String path, keyName, btnLabel;
	private int keyCode;
	private PCMFilePlayer sound;
	private JButton button;
	private boolean stoppable; //Indicates whether this sound can be stopped with spacebar or not
	//Stopping short sound clips creates problems with where the file will begin playing again
	
	//Constructor
	public SoundInfo(String p, int kC, String kN, String bL, int s) {
		setPath(p);
		setKeyName(kN);
		setBtnLabel(bL);
		setKeyCode(kC);
		if (s == 0)
			stoppable = false;
		else
			stoppable = true;
	}

	//Set where this sound is located on the hard drive
	public void setPath(String path) {
		this.path = path;
	}

	//Get where this sound is located on the hard drive
	public String getPath() {
		return path;
	}

	//Set the name of the key to press to play the sound
	public void setKeyName(String kN) {
		this.keyName = kN;
	}

	//Get the name of the key to press to play the sound
	public String getKeyName() {
		return keyName;
	}

	//Set the name of the sound to display
	public void setBtnLabel(String btnLabel) {
		this.btnLabel = btnLabel;
	}

	//Get the name of the sound to display
	public String getBtnLabel() {
		return btnLabel;
	}

	//Set the code of the key to press to play the sound
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	//Get the code of the key to press to play the sound
	public int getKeyCode() {
		return keyCode;
	}

	//Set the JButton that plays the sound on press
	public void setButton(JButton button) {
		this.button = button;
	}

	//Get the JButton that plays the sound on press
	public JButton getButton() {
		return button;
	}

	//Set the player for the sound
	public void setSound(PCMFilePlayer sound) {
		this.sound = sound;
	}

	//Get the player for the sound
	public PCMFilePlayer getSound() {
		return sound;
	}
	
	//Stop the sound if it is stoppable
	public void stop() {
		//If the sound is stoppable, stop it
		if (stoppable)
			sound.stop();
		//Otherwise, do nothing
	}
}
