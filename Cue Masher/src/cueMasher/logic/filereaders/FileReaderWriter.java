// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.logic.filereaders;

import java.util.ArrayList;
import java.util.Collection;
import cuemasher.logic.SoundInfo;

// This class defines how to read and write project files
public abstract class FileReaderWriter {
	private String filePath;
	private boolean readError;

	// Constructor
	// filePath - The path to the project file
	public FileReaderWriter(String filePath) {
		this.filePath = filePath;
		readError = false;
	}
	
	// Returns the path to the open Cue Masher file
	public String getFilePath() {
		return filePath;
	}
	
	// Sets that an error occurred while reading the open Cue Masher file
	protected void setReadError() {
		this.readError = true;
	}
	
	// Returns whether an error occurred while reading the open Cue Masher file
	public boolean getReadError() {
		return readError;
	}
	
	// Returns whether to proceed creating a sound object from the values retrieved from the open Cue Masher file
	// keyCode - The key code retrieved from the file
	// soundPath - The sound file path retrieved from the file
	protected boolean getValidSound(String soundPath, int keyCode, String keyName) {
		// Currently, all sounds must have a key code, file path, and key name in order to be placed on the sound board
		if (keyCode == SoundInfo.DEFAULT_KEY_CODE || soundPath == null || soundPath.isEmpty() || keyName == null) {
			setReadError();
			return false;
		}
		return true;
	}
	
	// Returns the key name to add to the sound object, transforming it to make it valid if needed
	// keyName - The key name retrieved from the open Cue Masher file
	protected String getKeyName(String keyName) {
		if (keyName == null || keyName.isEmpty()) {
			// The key name couldn't be retrieved or isn't valid
			//keyName = SoundInfo.DEFAULT_KEY_NAME;
			setReadError();
			return null;
		}
		return keyName;
	}
	
	// Returns the sound name to add to the sound object, transforming it to make it valid if needed
	// soundName - The sound name retrieved from the open Cue Masher file
	protected String getSoundName(String soundName) {
		if (soundName == null || soundName.isEmpty()) {
			// The sound name couldn't be retrieved or isn't valid
			soundName = SoundInfo.DEFAULT_SOUND_NAME;
			setReadError();
		}
		return soundName;
	}
	
	// Returns a binary attribute to add to the sound object, transforming it to make it valid if needed
	// binaryString - The attribute retrieved from the open Cue Masher file, a number is expected
	protected int getBinaryFromString(String binaryString) {
		int stoppable = parseNumber(binaryString);
		if (stoppable < 0) {
			// The attribute couldn't be retrieved or isn't valid
			stoppable = 0;
			setReadError();
		}
		else if (stoppable > 0) {
			stoppable = 1;
		}
		return stoppable;
	}
	
	// Returns the given string from the project file transformed to a number
	// numberString - The number to transform
	protected int parseNumber(String numberString) {
		int number = SoundInfo.DEFAULT_KEY_CODE;
		if (numberString != null) {
			try {
				number = Integer.parseInt(numberString);
			}
			catch (NumberFormatException e) {
				// A number wasn't specified in the project file where it was expected
				setReadError();
			}
		}
		else {
			// A number wasn't specified in the project file where it was expected
			setReadError();
		}
		return number;
	}

	// Read the project file and returns its contents in objects
	public abstract ArrayList<SoundInfo> readFile();

	// Save the given collection of sounds to the project file
	// sounds - The sound objects to write to the project file
	public abstract boolean writeFile(Collection<SoundInfo> sounds);
}
