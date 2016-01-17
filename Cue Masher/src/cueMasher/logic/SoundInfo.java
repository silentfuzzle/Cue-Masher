// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.logic;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Stores a sound: its path, button, player, keycode, label, and associated information
public class SoundInfo {
	public static int DEFAULT_KEY_CODE = -1;
	
	private String path, keyName, soundName;
	private int keyCode;
	private PCMFilePlayer sound;
	private boolean stoppable; //Indicates whether this sound can be stopped with spacebar or not
	//Stopping short sound clips creates problems with where the file will begin playing again
	
	//Constructor
	// soundPath - The path to the sound file on the file system
	// keyCode - The code of the key the user can press to play the sound
	// keyName - The name of the key the user can press to play the sound
	// soundName - The short name of the sound to display in the GUI
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	public SoundInfo(String soundPath, int keyCode, String keyName, String soundName, int stoppable) {
		this.keyCode = keyCode;
		setPath(soundPath);
		setKeyName(keyName);
		setSoundName(soundName);
		setStoppable(stoppable);
	}

	// Returns if all the information about this sound is equal to another sound's information
	// otherSound - The sound to compare this sound's information to
	public boolean equals(SoundInfo otherSound) {
		boolean equal = otherSound.getPath().equalsIgnoreCase(path);
		equal = (equal && otherSound.getKeyName().equalsIgnoreCase(keyName));
		equal = (equal && otherSound.getSoundName().equalsIgnoreCase(soundName));
		equal = (equal && (otherSound.getKeyCode() == keyCode));
		equal = (equal && (otherSound.stoppable == stoppable));
		
		return equal;
	}
	
	//Set where this sound is located on the hard drive
	// path - The full path to the sound
	public void setPath(String path) {
		if (this.path == null || !this.path.equalsIgnoreCase(path)) {
			this.path = path;
			
			// Close the open sound player
			if (sound != null)
				sound.close();

			// Don't create the sound player if this is a dummy object
			if (keyCode != DEFAULT_KEY_CODE) {
				
		        //Create the player that will stop, play and reset the sound file
				File soundFile = new File(this.path);
				try {
					PCMFilePlayer clip = new PCMFilePlayer(soundFile);
		            sound = clip;
				} catch (IOException e) {
					e.printStackTrace();
					sound = null;
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
					sound = null;
				} catch (LineUnavailableException e) {
					e.printStackTrace();
					sound = null;
				}
			}
		}
	}

	//Get where this sound is located on the hard drive
	public String getPath() {
		return path;
	}

	//Set the name of the key to press to play the sound
	// kN - The name of the key used to play this sound
	public void setKeyName(String kN) {
		this.keyName = kN;
	}

	//Get the name of the key to press to play the sound
	public String getKeyName() {
		return keyName;
	}

	//Set the name of the sound to display
	// soundName - The name of the sound
	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}

	//Get the name of the sound to display
	public String getSoundName() {
		return soundName;
	}

	//Get the code of the key to press to play the sound
	public int getKeyCode() {
		return keyCode;
	}
	
	// Sets whether this sound is stoppable
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	public void setStoppable(int stoppable) {
		this.stoppable = (stoppable != 0);
	}
	
	// Gets an integer representing whether this sound is stoppable
	public int getStoppable() {
		if (stoppable)
			return 1;
		else
			return 0;
	}

	//Play the sound if it is playable
	public void play() {
		if (sound != null)
		{
			sound.start();
		}
	}
	
	//Stop the sound if it is stoppable
	public void stop() {
		if (stoppable && sound != null)
			sound.stop();
	}
	
	// Stops and disposes of the sound player
	public void close() {
		sound.close();
	}
}
