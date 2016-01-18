# Cue Masher

Cue Masher is a basic, open-source digital soundboard created for plays and other theater productions with complex sound designs and live sound cues. I wrote this application for a production of "Bullshot Crummond" and have also used it for "Amelia Earhart" and "The Diary of Adam and Eve." With it, you can map keys on your keyboard to sound effects and music, creating a digital soundboard. You can also play sounds by clicking their associated buttons in the GUI. The application can play multiple sound tracks and sound effects at the same time, and sounds that you specify can be stopped by pressing the Spacebar.

This is a desktop application written in Java. Currently, it must run through an IDE such as Eclipse. It accepts .wav formatted sound files and assumes that you are using a QWERTY keyboard. Sound effect-keyboard mappings can be programmed manually by writing a specially formatted text file with the file extension ".cuemasher". You can open .cuemasher files in the application to populate the interface. These files can also be edited or created within Cue Masher.

## .cuemasher File Format

Each line of a .cuemasher file defines a sound effect and a keyboard key and should be formatted as follows:

[Full path to the sound file, .wav only, no commas],[the key code of the keyboard key],[the name of the key to display in the GUI],[the name of the sound to display in the GUI],[0 if the sound can't be stopped with the Spacebar, 1 if it can]

For example, the following line specifies that the sound file located at "C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav" should be played when the "1" key, which has the key code "49", is pressed. This sound effect is displayed as "Milky Way" in the GUI and can be stopped before it finishes by pressing the Spacebar as specified by the final "1" at the end of the line.

C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav,49,1,Milky Way,1

For convenience, if you're writing your .cuemasher file manually, the application writes the name of the key and its associated key code to the Console when you press any key while the application is running. This is true whether the key is mapped to a sound or not.

## The Sound Board

Cue Masher's main interface is laid out similarly to a standard QWERTY keyboard. Initially, when there no no sounds in a project, the interface is empty. Buttons are added as sounds are added to the board. The sounds can be played and edited by clicking on the buttons in the interface or pressing the corresponding keyboard key.

### Supported Keys

Supported keys include all the alphanumeric keys, "`", "-", "=", "[", "]", ";", "'", "\", ",", ".", and "/". On the number pad, only the numeric keys are supported.

### Adding Sounds

There are several ways that you can add new sounds to a project. The simplest way is to select Edit > Add Sound... from the menu bar. You can also hold down the Shift key and press any supported sound key that hasn't already been associated with a sound. Finally, you can toggle on Editing Mode by clicking Edit > Editing mode from the menu bar. Once you are in Editing Mode, press any supported sound key that hasn't been assigned a sound.

In the following dialogue box, you can select a .wav file from your computer, select the keyboard key you would like to use to play the sound, and name the sound. Click "Browse..." to select a .wav file from a File Chooser. When you enter a key into the text box, make sure you are not holding down the Shift key, or your entry won't be allowed. Also, make sure that you are entering a supported key, or you will receive a warning when you try to add it to the board. Sounds can have a name with a maximum length of twenty characters.

You can also indicate whether the sound is "Stoppable" or not. Stoppable sounds can be stopped by pressing the Spacebar in the main interface and resumed by pressing the sound's key. This is recommended for music and long, atmospheric sound effects but isn't recommended for short sound effects such as punches.

When you're finished, click Add to add the sound to the project's sound board. If you have assigned a new sound to a key that is already associated with a sound, you'll be asked to resolve the conflict. See Editing Sounds below for more information. Click Cancel in the New Sound dialogue to discard the new sound without making any changes. You can have a maximum of five New Sound dialogue boxes open at a time.

### Playing Sounds

To play the sounds on the sound board, click the buttons on the interface or press the corresponding keys on the keyboard. Make sure that Edit > Editing mode isn't checked and that you aren't holding down the Shift key. If a button on the board is colored pink, this means that the sound file couldn't be found or opened, and the sound isn't playable. You can stop all Stoppable sounds by pressing the Spacebar or clicking the Stop button at the bottom of the interface.

Note that there is a known bug that causes sounds to loop infinitely if they are stopped or started repeatedly. If this happens, you will need to close and reopen Cue Masher.

### Editing Sounds

There are several ways that you can select a sound to edit. You can hold down the Shift key and press an associated keyboard key, you can hold down the Shift key and click a button in the interface, or you can toggle on Editing Mode by selecting Edit > Editing mode from the menu bar. In Editing Mode, press any supported key that's been assigned a sound or click a button in the interface to select a sound to edit.

Once you select a sound, an Edit Sound dialogue box pre-populated with the information about the selected sound will appear. You can change any of the sound's attributes and then click the Apply Changes button to update the project. You can also delete the sound from the board by clicking the Delete button or click Cancel to discard all changes.

If you assign a new sound a key that is already associated with a sound or you assign an existing sound a new key that is already associated with another sound, you will be asked to resolve the conflict before the board will be updated. You can do this by selecting "Replace", "Replace and Edit", or "Cancel" from the following dialogue box. The Replace option replaces the existing sound with the new or updated sound. Replace and Edit also replaces the existing sound, but it brings up a new Edit Sound dialogue box where you can select a new key for the sound that was replaced. Clicking Cancel cancels the action and returns you to the Add or Edit Sound dialogue where you can select a new key for the sound and continue editing it.

## File Menu

### New

When you open Cue Masher, a new project is automatically open. To open a new project from the menu, select File > New. You may be prompted to save or discard the changes to your current project.

### Open

To open an existing .cuemasher file on your computer, select File > Open from the menu bar. You may be prompted to save or discard the changes to your current project. Select a .cuemasher file from your computer in the following File Chooser. The interface will be populated with the file's contents.

If the file is not a valid .cuemasher file or isn't formatted properly, the application will still attempt to read it and populate the interface from it as much as possible. Any sound-keyboard definitions in the file that it fails to read will not be imported into the project. At the end of the process the application will report that an error occurred and ask if you wish to keep the project open in a dialogue box. If you proceed and Save the project, the badly formatted file will be overwritten with what data could be imported into the project. If the file is one that you wrote by hand, you may want to close the project instead and attempt to properly format it first so that you don't lose your work.

### Save

To save the open project, select File > Save from the menu. If you have a new project open that hasn't been saved to a file yet, the save option will appear as "Save..." in the File menu and will prompt you to select a name and location to save your project at. If your project has been modified since the last time you saved the file, the save option will appear as "Save*" in the File menu. If you have no unsaved changes, the option will appear as "Save".

### Save As

Select File > Save As from the menu bar to save the open project to a new or existing .cuemasher file.

### Quit

You can close Cue Masher by selecting File > Quit from the menu bar or by clicking the exit button in the corner of the window. You may be prompted to save or discard the changes to your current project.

## Coming Features and Known Bugs

I've worked on Cue Masher off and on as I've found bugs and needed features for the productions that I've used it for, but it still has a ways to go before it is more friendly for less technical users. The following is a list of coming features that the application could really use and known problems that need to be fixed.

### High Priority

* Needs a new text file format such as XML so that commas in sound file paths don't cause problems.
* Needs an executable to run outside an IDE.
* A bug that causes a sound effect to loop indefinitely when one or more keys are pressed quickly at the same time needs to be fixed.

### Low Priority

* Needs ability to fade sound effects and music rather than hard stopping them.
* Needs ability to raise or lower sound volume per sound.
* Needs ability to loop a sound.
* Needs ability to stop a single sound effect when multiple sound effects are playing.
* Needs ability to restart a long sound effect or music from the beginning.
* Needs ability to play sounds of different file types.
* Needs ability to accept keyboards with different key configurations.
* Needs to display buttons in a layout so that buttons are always aligned.
