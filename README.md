# Cue Masher

Cue Masher is a free sound effect and music player created for plays and other theater productions with complex sound designs and live sound cues. I wrote this application for a production of "Bullshot Crummond" and have also used it for "Amelia Earhart" and "The Diary of Adam and Eve." With it, you can map keys on your keyboard to sound effects and music. You can also select sound effects through the GUI. The application can play multiple sound tracks and sound effects at the same time, and sounds can be stopped by pressing the Spacebar.

This is a desktop application written in Java. Currently, it must run through an IDE such as Eclipse. It accepts .wav formatted sound files. Sound effect-keyboard mappings must be programmed manually with a specially formatted text file named "soundPaths". This file should be placed in the main "Cue Masher" directory. Each line of the text file defines a sound effect and a keyboard key and should be formatted as follows:

[Full path to the sound file, .wav only, no commas],[the key code of the keyboard key],[the name of the key to display in the GUI],[the name of the sound to display in the GUI],[0 if the sound can't be stopped with the Spacebar, 1 if it can]

For example, the following line specifies that the sound file located at "C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav" should be played when the "1" key, which has the key code "49", is pressed. This sound effect is displayed as "Milky Way" in the GUI and can be stopped before it finishes by pressing the Spacebar as specified by the final "1" at the end of the line.

C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav,49,1,Milky Way,1

## Coming Features and Known Bugs

I've worked on Cue Masher off and on as I've found bugs and needed features for the productions that I've used it for, but it still has a ways to go before it is more friendly for less technical users. The following is a list of coming features that the application could really use and known problems that need to be fixed.

### High Priority

* Needs ability to open a formatted sound-keyboard definition file and populate the interface.
* Needs ability to create a new formatted sound-keyboard definition file.
* Needs ability to save sound-keyboard definition files.
* Needs a GUI to define a new sound effect and keyboard key or modify or delete an existing one.
* Needs a new text file format such as XML so that commas in sound file paths don't cause problems.
* Needs to handle missing sound files nicer.
* Needs an executable to run outside an IDE.
* A bug that causes a sound effect to loop indefinitely when one or more keys are pressed quickly at the same time needs to be fixed.

### Low Priority

* Needs ability to fade sound effects and music rather than hard stopping them.
* Needs ability to raise or lower sound volume per sound.
* Needs ability to stop a single sound effect when multiple sound effects are playing.
* Needs ability to restart a long sound effect or music from the beginning.
* Needs ability to play sounds of different file types.
