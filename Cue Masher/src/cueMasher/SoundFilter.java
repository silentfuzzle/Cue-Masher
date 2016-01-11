//http://www.leepoint.net/notes-java/GUI/containers/20dialogs/35filefilter.html

package cueMasher;

import java.io.*;

public class SoundFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".wav");
    }
    
    public String getDescription() {
        return ".wav files";
    }
}
