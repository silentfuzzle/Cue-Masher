// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.logic;

import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

//Stores a sound: its path, button, player, keycode, label, and associated information
public class SoundInfo {
	public static int DEFAULT_KEY_CODE = -1;
	public static String DEFAULT_KEY_NAME = "?";
	public static String DEFAULT_SOUND_NAME = "Unnamed";
	
	private String path, keyName, soundName;
	private int keyCode;
	private PCMFilePlayer sound;
	private ArrayList<LineListener> lineListeners;
	private boolean stoppable; //Indicates whether this sound can be stopped with spacebar or not
	private boolean toggleable; //Indicates whether this sound can be toggled on and off with its corresponding key/button
	//Stopping short sound clips isn't recommended
	
	//Constructor
	// soundPath - The path to the sound file on the file system
	// keyCode - The code of the key the user can press to play the sound
	// keyName - The name of the key the user can press to play the sound
	// soundName - The short name of the sound to display in the GUI
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	// toggleable - 1 if the sound can be toggled on and off with its corresponding key/button
	public SoundInfo(String soundPath, int keyCode, String keyName, String soundName, int stoppable, int toggleable) {
		this.keyCode = keyCode;
		lineListeners = new ArrayList<LineListener>();
		setPath(soundPath);
		setKeyName(keyName);
		setSoundName(soundName);
		setStoppable(stoppable);
		setToggleable(toggleable);
	}

	// Returns if all the information about this sound is equal to another sound's information
	// otherSound - The sound to compare this sound's information to
	public boolean equals(SoundInfo otherSound) {
		boolean equal = otherSound.getPath().equalsIgnoreCase(path);
		equal = (equal && otherSound.getKeyName().equalsIgnoreCase(keyName));
		equal = (equal && otherSound.getSoundName().equals(soundName));
		equal = (equal && (otherSound.getKeyCode() == keyCode));
		equal = (equal && (otherSound.stoppable == stoppable));
		equal = (equal && (otherSound.toggleable == toggleable));
		
		return equal;
	}
	
	//Set where this sound is located on the hard drive
	// path - The full path to the sound
	public void setPath(String path) {
		if (this.path == null || !this.path.equalsIgnoreCase(path)) {
			this.path = path;
			
			// Open the sound file
			open();
		}
	}

	//Get where this sound is located on the hard drive
	public String getPath() {
		return path;
	}
	
	// Add a listener to the sound to perform an action when it stops or starts
	// listener - The listener to add
	public void addSoundListener(LineListener listener) {
		if (sound != null) {
			lineListeners.add(listener);
			sound.getLine().addLineListener(listener);
		}
	}
	
	// Returns whether the sound file could be opened
	public boolean getSoundOpen() {
		return (sound != null);
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
	
	// Sets whether this sound is stoppable with Spacebar
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	public void setStoppable(int stoppable) {
		this.stoppable = (stoppable != 0);
	}
	
	// Returns an integer representing whether this sound is stoppable with Spacebar
	public int getStoppable() {
		if (stoppable)
			return 1;
		else
			return 0;
	}
	
	// Returns if this sound can be stopped
	public boolean isStoppable() {
		return stoppable;
	}
	
	// Sets whether this sound is stoppable with the corresponding key/button
	// toggleable - 1 if the sound can be stopped with its key/button, 0 if not
	public void setToggleable(int toggleable) {
		this.toggleable = (toggleable != 0);
	}
	
	// Returns an integer representing whether this sound can be toggled on and off
	public int getToggleable() {
		if (toggleable)
			return 1;
		else
			return 0;
	}
	
	// Returns if this sound can be toggled
	public boolean isToggleable() {
		return toggleable;
	}
	
	// Open the sound file
	private void open() {
		
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
	            
	            // Add all the listeners that were attached to the previous sound
	            for (int i=0; i < lineListeners.size(); i++) {
	            	sound.getLine().addLineListener(lineListeners.get(i));
	    		}
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

	// Play the sound if it is playable
	public void play() {
		if (sound != null) {
			sound.start();
		}
	}
	
	// Reset the sound to the beginning
	public void reset() {
		if ((stoppable || toggleable) && sound != null) {
			open();
		}
	}
	
	// Stop the sound if it is stoppable
	public void stop() {
		if (sound != null)
			sound.stop();
	}
	
	// Stops and disposes of the sound player
	public void close() {
		if (sound != null) {
			sound.close();
		}
	}

	// Returns if the sound is playing
	public boolean getPlaying() {
		return sound.getPlaying();
	}
}
