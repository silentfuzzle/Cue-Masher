// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.util.*;

// This class manages the open project file.
public class ProjectFileManager {
	private HashMap<Integer, SoundInfo> soundList;	//Holds the current list of sounds in the open project
	private boolean projectModified;
	private CSVReaderWriter projectFileIO = null;
	
	// Constructor
	public ProjectFileManager() {
		soundList = new HashMap<Integer, SoundInfo>();
		projectModified = false;
	}
	
	// Returns if a project file is open
	public boolean isProjectOpen() {
		return (projectFileIO != null);
	}
	
	// Sets the project file path
	// textFilePath - The path to the project file
	public void setProjectFilePath(String textFilePath) {
		projectFileIO = new CSVReaderWriter(textFilePath);
	}
	
	// Read the given project file and create objects from it in memory
	// textFilePath - The project file to open and read
	public ArrayList<SoundInfo> readFile(String textFilePath) {
		
		// Dispose of all the sound objects in the project that is currently open
		Collection<SoundInfo> sounds = soundList.values();
        for(SoundInfo sound: sounds){
            sound.close();
        }
        
        // Set the newly opened project variables
		soundList = new HashMap<Integer, SoundInfo>();
		setProjectFilePath(textFilePath);
		projectModified = false;
		
		// Read the project file and save its contents to memory
		ArrayList<SoundInfo> readSounds = projectFileIO.readFile();
		for (int s=0; s < readSounds.size(); s++) {
			SoundInfo currSound = readSounds.get(s);
			int keyCode = currSound.getKeyCode();
			
			if (!soundList.containsKey(keyCode)) {
				soundList.put(keyCode, currSound);
			}
		}
		
        return readSounds;
	}
	
	// Saves the open project to the project file if needed
	public boolean saveFile() {
		if (isProjectOpen() && projectModified) {
			boolean written = projectFileIO.writeFile(soundList.values());
			if (written)
				projectModified = false;
			return written;
		}
		return false;
	}
	
	// Add a sound to the open project in memory
	// soundPath - The path to the sound file on the file system
	// keyCode - The code of the key the user can press to play the sound
	// keyName - The name of the key the user can press to play the sound
	// soundName - The short name of the sound to display in the GUI
	// stoppable - 1 if the sound can be stopped with the Spacebar, 0 if not
	public SoundInfo addSound(String soundPath, int keyCode, String keyName, String soundName, int stoppable) {

		//Store the sound path and other information in the text file in an object
		SoundInfo soundClip = new SoundInfo(soundPath, keyCode, keyName, soundName, stoppable);
		
		if (soundList.containsKey(keyCode))
		{
			// A sound is already associated with the given key
			// Don't do anything if the sound info hasn't been modified
			SoundInfo existingClip = soundList.get(keyCode);
			if (!existingClip.equals(soundClip))
			{
				// Update the existing sound's information with the new given information
				existingClip.setPath(soundPath);
				existingClip.setKeyName(keyName);
				existingClip.setSoundName(soundName);
				existingClip.setStoppable(stoppable);
				projectModified = true;
			}
			return null;
		}
		else {
			// The given sound is associated with a new key
			// Add it to the list of sounds in the project in memory
			projectModified = true;
			soundList.put(keyCode, soundClip);
			return soundClip;
		}
	}
	
	// Plays the sound associated with the given keyboard key code
	public void playSound(int keyCode) {
		if (soundList.containsKey(keyCode)) {
			soundList.get(keyCode).play();
		}
	}
	
	// Stop all stoppable sounds
	public void stopSounds() {
		Collection<SoundInfo> sounds = soundList.values();
        for(SoundInfo sound: sounds){
            sound.stop();
        }
	}
}
