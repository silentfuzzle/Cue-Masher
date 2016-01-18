// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.logic.filereaders;

import java.io.*;
import java.util.*;
import cuemasher.logic.SoundInfo;

// This class reads and writes project files in an XML format
public class XMLReaderWriter extends FileReaderWriter {
	private String SOUND_TAG = "Sound";
	private String PATH_TAG = "Path";
	private String KEY_CODE_TAG = "KeyCode";
	private String KEY_NAME_TAG = "KeyName";
	private String SOUND_NAME_TAG = "SoundName";
	private String STOPPABLE_TAG = "Stoppable";
	
	// Constructor
	// filePath - The path to the project file
	public XMLReaderWriter(String filePath) {
		super(filePath);
	}

	// Read the project file and returns its contents in objects
	public ArrayList<SoundInfo> readFile() {
		
		ArrayList<SoundInfo> sounds = new ArrayList<SoundInfo>();
		
		//Attempt to get the text file of information and initialize the sounds
		try {
			//Get the file and a Scanner to look through it
			File soundPaths = new File(getFilePath());
			Scanner scanSoundPathDoc = new Scanner(soundPaths);
			
			// Get all the text out of the file
			String fileText = "";
			while(scanSoundPathDoc.hasNextLine()) {
				fileText += scanSoundPathDoc.nextLine();
			}
			fileText = fileText.trim();
			
			// Iterate through and store all the sounds in the file's text
			int soundIndex = 0;
			while (soundIndex != -1) {
				// Get the text defining a sound
				soundIndex = fileText.indexOf(wrapInStartTag(SOUND_TAG), soundIndex);
				if (soundIndex == -1) {
					continue;
				}
				
				// Ignore empty sound tags
				String soundLine = getTagContents(fileText, SOUND_TAG, soundIndex);
				if (soundLine != null && !soundLine.isEmpty()) {
					// Get the information about the sound
					String soundPath = getTagContents(soundLine, PATH_TAG, 0);
					int keyCode = parseNumber(getTagContents(soundLine, KEY_CODE_TAG, 0));
					String keyName = getKeyName(getTagContents(soundLine, KEY_NAME_TAG, 0));
					
					if (getValidSound(soundPath, keyCode, keyName)) {
						String soundName = getSoundName(getTagContents(soundLine, SOUND_NAME_TAG, 0));
						int stoppable = getStoppable(getTagContents(soundLine, STOPPABLE_TAG, 0));
						
						// Store the sound in an object
						SoundInfo soundClip = new SoundInfo(soundPath, keyCode, keyName, soundName, stoppable);
						sounds.add(soundClip);
					}
				}
				
				// Look for the next sound in the text
				soundIndex++;
			}
			
			// There was text in the file but none of it was in the expected format
			if (!fileText.isEmpty() && sounds.size() == 0) {
				setReadError();
			}
			
		} catch (FileNotFoundException e) {
			setReadError();
		}
        
        return sounds;
	}
	
	// Save the given collection of sounds to the project file
	// sounds - The sound objects to write to the project file
	public boolean writeFile(Collection<SoundInfo> sounds) {
		try {
			PrintWriter print = new PrintWriter(new FileWriter(getFilePath(), false));
			
			for(SoundInfo sound: sounds){
				print.printf("%s" + "%n", 
						wrapInStartTag(SOUND_TAG) +
						wrapInStartTag(PATH_TAG) + sound.getPath() + wrapInEndTag(PATH_TAG) +
						wrapInStartTag(KEY_CODE_TAG) + sound.getKeyCode() + wrapInEndTag(KEY_CODE_TAG) + 
						wrapInStartTag(KEY_NAME_TAG) + sound.getKeyName() + wrapInEndTag(KEY_NAME_TAG) + 
						wrapInStartTag(SOUND_NAME_TAG) + sound.getSoundName() + wrapInEndTag(SOUND_NAME_TAG) + 
						wrapInStartTag(STOPPABLE_TAG) + sound.getStoppable() + wrapInEndTag(STOPPABLE_TAG) + 
						wrapInEndTag(SOUND_TAG));
	        }
			
			print.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Wrap the given string as an XML start tag
	// tagName - The string to wrap
	private String wrapInStartTag(String tagName) {
		return "<" + tagName + ">";
	}
	
	// Wrap the given string as an XML end tag
	// tagName - The string to wrap
	private String wrapInEndTag(String tagName) {
		return "</" + tagName + ">";
	}
	
	// Get the text found between an XML start and end taag
	// searchText - The text to look for the tags in
	// tagName - The tag to look for in the text
	// searchIndex - The index to begin searching for the start tag at
	private String getTagContents(String searchText, String tagName, int searchIndex) {
		
		// Look for the location of the start tag
		String startTag = wrapInStartTag(tagName);
		searchIndex = searchText.indexOf(startTag, searchIndex);
		if (searchIndex != -1) {
			// Look for the location of the end tag
			int endSearchIndex = searchText.indexOf(wrapInEndTag(tagName), searchIndex);
			if (endSearchIndex != -1) {
				// Return the contents between the start and end tags
				String tagContents = searchText.substring(searchIndex+startTag.length(), endSearchIndex).trim();
				return tagContents;
			}
		}
		
		// The start or end tag couldn't be found
		setReadError();
		return null;
	}
}
