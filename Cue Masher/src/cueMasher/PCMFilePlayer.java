// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>
// Original code source: http://codeidol.com/java/swing/Audio/Play-Non-Trivial-Audio/

package cueMasher;

import java.io.*;

import javax.sound.sampled.*;

public class PCMFilePlayer implements Runnable { 
	private File file;
	private AudioInputStream in;
	private SourceDataLine line;
	private int frameSize;
	private byte[] buffer = new byte [32 * 1024]; // 32k is arbitrary
	private Thread playThread;
	private boolean playing;
	private boolean notYetEOF;

	public PCMFilePlayer (File f)
		throws IOException,
			UnsupportedAudioFileException,
			LineUnavailableException {
		file = f;
		in = AudioSystem.getAudioInputStream (f);
		AudioFormat format = in.getFormat();
		AudioFormat.Encoding formatEncoding = format.getEncoding();
		if (! (formatEncoding.equals (AudioFormat.Encoding.PCM_SIGNED) ||
			   formatEncoding.equals (AudioFormat.Encoding.PCM_UNSIGNED))) 
		   throw new UnsupportedAudioFileException (
                              file.getName() + " is not PCM audio");     
	   frameSize = format.getFrameSize(); 
	   DataLine.Info info = new DataLine.Info (SourceDataLine.class, format); 
	   line = (SourceDataLine) AudioSystem.getLine (info);      
	   line.open(); 
	   playThread = new Thread (this); 
	   playing = false; 
	   notYetEOF = true;        
	   playThread.start();
	}
	public void run() {
		int readPoint = 0;
		int bytesRead = 0;

		try {
			while (notYetEOF) {
				if (playing) {
					bytesRead = in.read (buffer, readPoint, buffer.length - readPoint);
	                if (bytesRead == -1) {
						notYetEOF = false; 
						break;
					}
					// how many frames did we get,
					// and how many are left over?
					int frames = bytesRead / frameSize;
					int leftover = bytesRead % frameSize;
					// send to line
					line.write (buffer, readPoint, bytesRead-leftover);
					// save the leftover bytes
					System.arraycopy (buffer, bytesRead,
							  buffer, 0, 
							  leftover); 
	                    readPoint = leftover;
				} else { 
					// if not playing                   
					// Thread.yield(); 
					try { Thread.sleep (10);} 
					catch (InterruptedException ie) {}
				}
			} // while notYetEOF
			line.drain();
			line.stop();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			// line.close();
		}
	} // run

	public void start() {
		playing = true;
		if (! playThread.isAlive()) {
			reset();
		}
		
		line.start();
	}

	public void stop() {
		playing = false;
		line.stop();
	}
	
	// Stop and dispose of the sound this player plays
	public void close() {
		stop();
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		try {
			in.close();
			in = AudioSystem.getAudioInputStream (file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notYetEOF = true;
		playThread = new Thread (this);
		playThread.start();
	}
   
	public SourceDataLine getLine() {
		return line;
	}

	public File getFile() {		
		return file; 
	} 
}
