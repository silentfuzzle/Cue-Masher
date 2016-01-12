// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>
	
package cueMasher;

import java.io.File;

// When applied to a choose file dialog, only Cue Masher files can be selected.
public class CueFileFilter extends javax.swing.filechooser.FileFilter {
	
	// Returns if file is displayable
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".cuemasher");
    }

    // Returns a description of the type of files being displayed
    public String getDescription() {
        return "Cue Masher files";
    }
}
