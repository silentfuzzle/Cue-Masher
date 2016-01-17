// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasherlogic;

import java.io.*;
import java.util.*;

// This class reads and writes project files in a csv format.
public class CSVReaderWriter {
	private String filePath;
	
	// Constructor
	// filePath - The path to the project file
	public CSVReaderWriter(String filePath) {
		this.filePath = filePath;
	}
	
	// Returns the path to the open Cue Masher file
	public String getFilePath() {
		return filePath;
	}

	// Read the project file and returns its contents in objects
	public ArrayList<SoundInfo> readFile() {
		
		ArrayList<SoundInfo> sounds = new ArrayList<SoundInfo>();
		
		//Attempt to get the text file of information and initialize the sounds
		try {
			//Get the file and a Scanner to look through it
			File soundPaths = new File(filePath);
			Scanner scanSoundPathDoc = new Scanner(soundPaths);
			
			//Create another scanner to parse each line of the text file
			Scanner scanLine = new Scanner("");
			//Parse each line in the text file
			while(scanSoundPathDoc.hasNextLine()) {
				scanLine = new Scanner(scanSoundPathDoc.nextLine());
				//File is delimited with CSV format
				scanLine.useDelimiter(",");
				
				SoundInfo soundClip = new SoundInfo(scanLine.next(),scanLine.nextInt(),scanLine.next(),scanLine.next(),scanLine.nextInt());
				sounds.add(soundClip);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Not found");
		}
        
        return sounds;
	}
	
	// Save the given collection of sounds to the project file
	// sounds - The sound objects to write to the project file
	public boolean writeFile(Collection<SoundInfo> sounds) {
		try {
			PrintWriter print = new PrintWriter(new FileWriter(filePath, false));
			
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
}
