// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.logic.filereaders;

import java.io.*;
import java.util.*;
import cuemasher.logic.SoundInfo;

// This class reads and writes project files in a csv format.
// Toggleable is not supported.
public class CSVReaderWriter extends FileReaderWriter {
	
	// Constructor
	// filePath - The path to the project file
	public CSVReaderWriter(String filePath) {
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
			
			//Create another scanner to parse each line of the text file
			Scanner scanLine = new Scanner("");
			//Parse each line in the text file
			while(scanSoundPathDoc.hasNextLine()) {
				String line = scanSoundPathDoc.nextLine().trim();
				
				// Ignore empty lines
				if (!line.isEmpty()) {
					// Split the line by commas and iterate through it
					scanLine = new Scanner(line);
					scanLine.useDelimiter(",");
					
					// Get information about the sound
					String soundPath = getNextFromScanner(scanLine);
					int keyCode = parseNumber(getNextFromScanner(scanLine));
					String keyName = getKeyName(getNextFromScanner(scanLine));
					if (getValidSound(soundPath, keyCode, keyName)) {
						String soundName = getSoundName(getNextFromScanner(scanLine));
						int stoppable = getBinaryFromString(getNextFromScanner(scanLine));
						
						// Save the information to an object
						SoundInfo soundClip = new SoundInfo(soundPath, keyCode, keyName, soundName, stoppable, 0);
						sounds.add(soundClip);
					}
				}
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
						sound.getPath() + "," + sound.getKeyCode() + "," + 
						sound.getKeyName() + "," + sound.getSoundName() + "," + 
						sound.getStoppable());
	        }
			
			print.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Returns the next item in a comma delimited line
	// scanLine - The scanner scanning the comma delimited line
	private String getNextFromScanner(Scanner scanLine) {
		if (scanLine.hasNext()) {
			return scanLine.next().trim();
		}
		
		// Another item wasn't found as expected
		setReadError();
		return null;
	}
}
