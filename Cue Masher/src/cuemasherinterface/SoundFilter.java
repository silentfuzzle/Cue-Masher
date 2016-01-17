// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasherinterface;

import java.io.*;

//When applied to a choose file dialog, only .wav files can be selected.
public class SoundFilter extends javax.swing.filechooser.FileFilter {

	// Returns if file is displayable
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".wav");
    }

    // Returns a description of the type of files being displayed
    public String getDescription() {
        return ".wav files";
    }
}
