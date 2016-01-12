# Cue Masher

Cue Masher is a free sound effect and music player created for plays and other theater productions with complex sound designs. With it, you can map keys on your keyboard to sound effects and music. You can also select sound effects throug the GUI. The application can play multiple sound tracks and sound effects at the same time, and sounds can be stopped by pressing the Spacebar.

This is a desktop application written in Java. Currently, it must run through an IDE such as Eclipse. It accepts .wav formatted sound files. Sound effect-keyboard mappings must be programmed manually with a specially formatted text file named "soundPaths". This file should be placed in the main "Cue Masher" directory. Each line of the text file defines a sound effect and a keyboard key and should be formatted as follows:

[Full path to the sound file, .wav only, no commas],[the key code of the keyboard key],[the name of the key to display in the GUI],[the name of the sound to display in the GUI],[0 if the sound can't be stopped with the Spacebar, 1 if it can]

For example, the following line specifies that the sound file located at "C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav" should be played when the "1" key, which has the key code "49", is pressed. This sound effect is displayed as "Milky Way" in the GUI and can be stopped before it finishes by pressing the Spacebar as specified by the final "1" at the end of the line.

C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav,49,1,Milky Way,1
