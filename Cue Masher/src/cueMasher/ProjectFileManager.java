// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cueMasher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

// This class manages the open project file.
public class ProjectFileManager {
	private HashMap<Integer, SoundInfo> soundList;	//Holds the current list of sounds in the open project
	private boolean projectModified;
	
	// Constructor
	public ProjectFileManager() {
		soundList = new HashMap<Integer, SoundInfo>();
		projectModified = false;
	}
	
	// Read the given project file and create objects from it in memory
	public SoundInfo[] readFile(String textFilePath) {
		soundList = new HashMap<Integer, SoundInfo>();
		
		//Attempt to get the text file of information and initial the sounds
		try {
			//Get the file and a Scanner to look through it
			File soundPaths = new File(textFilePath);
			Scanner scanSoundPathDoc = new Scanner(soundPaths);
			
			//Create another scanner to parse each line of the text file
			Scanner scanLine = new Scanner("");
			//Parse each line in the text file
			while(scanSoundPathDoc.hasNextLine()) {
				scanLine = new Scanner(scanSoundPathDoc.nextLine());
				//File is delimited with CSV format
				scanLine.useDelimiter(",");
				
				addSound(scanLine.next(),scanLine.nextInt(),scanLine.next(),scanLine.next(),scanLine.nextInt());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Not found");
		}
		
		projectModified = false;
		
		// Return an array of the sound objects that were added from the text file
		SoundInfo[] readSounds = new SoundInfo[soundList.size()];
		Collection<SoundInfo> sounds = soundList.values();
		
		int s = 0;
        for(SoundInfo sound: sounds){
            readSounds[s] = sound;
            s++;
        }
        
        return readSounds;
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
