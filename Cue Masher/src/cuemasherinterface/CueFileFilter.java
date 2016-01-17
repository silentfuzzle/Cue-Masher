// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>
	
package cuemasherinterface;

import java.io.File;

// When applied to a choose file dialog, only Cue Masher files can be selected.
public class CueFileFilter extends javax.swing.filechooser.FileFilter {
	
	public static String CUE_MASHER_FILE_EXT = ".cuemasher";
	
	// Returns if file is displayable
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(CUE_MASHER_FILE_EXT);
    }

    // Returns a description of the type of files being displayed
    public String getDescription() {
        return "Cue Masher files";
    }
}
