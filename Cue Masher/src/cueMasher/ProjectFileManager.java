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
	
	// Returns if the project has been modified
	public boolean getProjectModified() {
		return projectModified;
	}
	
	// Returns if a project file is open
	public boolean isProjectOpen() {
		return (projectFileIO != null);
	}
	
	// Sets the project file path
	// textFilePath - The path to the project file
	public void setProjectFilePath(String textFilePath) {
		projectFileIO = new CSVReaderWriter(textFilePath);
		projectModified = true;
	}
	
	// Returns the path to the open project file if one is open
	public String getProjectFilePath() {
		if (projectFileIO != null) {
			return projectFileIO.getFilePath();
		}
		else {
			return null;
		}
	}
	
	// Stops and disposes of sounds and clears sound list for new project
	public void openNewProject() {

		// Dispose of all the sound objects in the project that is currently open
		Collection<SoundInfo> sounds = soundList.values();
        for(SoundInfo sound: sounds){
            sound.close();
        }
        
        // Set the newly opened project variables
		soundList = new HashMap<Integer, SoundInfo>();
		projectModified = false;
		projectFileIO = null;
	}
	
	// Read the given project file and create objects from it in memory
	// textFilePath - The project file to open and read
	public ArrayList<SoundInfo> readFile(String textFilePath) {
		
		openNewProject();
		projectFileIO = new CSVReaderWriter(textFilePath);
		
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
	// soundClip - An object storing information about the added or updated sound
	public SoundInfo addSound(SoundInfo soundClip) {
		int keyCode = soundClip.getKeyCode();
		
		if (soundList.containsKey(keyCode))
		{
			// A sound is already associated with the given key
			// Don't do anything if the sound info hasn't been modified
			SoundInfo existingClip = soundList.get(keyCode);
			if (!existingClip.equals(soundClip))
			{
				// Update the existing sound's information with the new given information
				existingClip.setPath(soundClip.getPath());
				existingClip.setKeyName(soundClip.getKeyName());
				existingClip.setSoundName(soundClip.getSoundName());
				existingClip.setStoppable(soundClip.getStoppable());
				projectModified = true;
			}
			soundClip.close();
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
	
	// Delete the sound with the given key code if it exists
	// keyCode - The keyboard key code associated with the sound to delete
	public void deleteSound(int keyCode) {
		if (soundList.containsKey(keyCode)) {
			SoundInfo sound = soundList.get(keyCode);
			sound.close();
			soundList.remove(keyCode);
			projectModified = true;
		}
	}
	
	// Returns the sound associated with the given keycode if it exists
	// keyCode - The keyboard key code associated with the sound to get
	public SoundInfo getSound(int keyCode) {
		SoundInfo sound = null;
		if (soundList.containsKey(keyCode)) {
			sound = soundList.get(keyCode);
		}
		return sound;
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
