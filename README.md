# Cue Masher

Cue Masher is a basic, open-source digital soundboard created for plays and other theater productions with complex sound designs and live sound cues. I wrote this application for a production of "Bullshot Crummond" and have also used it for "Amelia Earhart," "The Diary of Adam and Eve," and "Absurd Person Singular." With it, you can map keys on your keyboard to sound effects and music, creating a digital soundboard. Sounds can be played by clicking their associated buttons in the GUI or using the keyboard keys. Cue Masher allows you to quickly create and edit your projects/sound boards even while simultaneously playing sounds. The application can play multiple sound tracks and sound effects at the same time. Sounds you specify can be stopped by pressing the Spacebar or toggled with their corresponding key or button.

This is a desktop application written in Java and runs with an executable .jar file (see releases). It accepts .wav formatted sound files and assumes that you are using a QWERTY keyboard.

## The Sound Board

Cue Masher's main interface is laid out similarly to a standard QWERTY keyboard. Initially, when there no no sounds in a project, the interface is empty. Buttons are added as sounds are added to the board. The sounds can be played and edited by clicking on the buttons in the interface or pressing the corresponding keyboard key.

### Supported Keys

Supported keys include all the alphanumeric keys, "`", "-", "=", "[", "]", ";", "'", "\", ".", and "/". On the number pad, only the numeric keys are supported.

### Adding Sounds

There are several ways that you can add new sounds to a project. The simplest way is to select Edit > Add Sound... from the menu bar. You can also hold down the Shift key and press any supported sound key that hasn't already been associated with a sound. Finally, you can toggle on Editing Mode by clicking Edit > Editing mode from the menu bar. Once you are in Editing Mode, press any supported sound key that hasn't been assigned a sound.

In the following dialogue box, you can select a .wav file from your computer, select the keyboard key you would like to use to play the sound, and name the sound. Click "Browse..." to select a .wav file from a File Chooser. When you enter a key into the text box, make sure you are not holding down the Shift key, or your entry won't be allowed. Also, make sure that you are entering a supported key, or you will receive a warning when you try to add it to the board. Sounds can have a name with a maximum length of twenty characters.

You can also indicate whether the sound is "Stoppable" or "Toggleable." Stoppable sounds can be stopped by pressing the Spacebar in the main interface and resumed by pressing the sound's key. Toggleable sounds can be played by pressing the sound's key and stopped by pressing the key again. This allows you to program multiple sounds to stop by pressing the Spacebar, or stop individual sounds by pressing their corresponding button, while other sounds continue playing. One or both of these options are recommended for music and long, atmospheric sound effects but aren't recommended for short sound effects such as punches.

When you're finished, click Add to add the sound to the project's sound board. If you have assigned a new sound to a key that is already associated with a sound, you'll be asked to resolve the conflict. See Editing Sounds below for more information. Click Cancel in the New Sound dialogue to discard the new sound without making any changes. You can have a maximum of five New Sound dialogue boxes open at a time.

### Playing Sounds

To play the sounds on the sound board, click the buttons on the interface or press the corresponding keys on the keyboard. Make sure that Edit > Editing mode isn't checked and that you aren't holding down the Shift key. If a button on the board is colored pink, this means that the sound file couldn't be found or opened, and the sound isn't playable. While a sound is playing, its corresponding board button will be colored green. You can stop all Stoppable sounds by pressing the Spacebar or clicking the Stop button at the bottom of the interface. You can also stop Toggleable sounds by pressing their corresponding keys on the keyboard or clicking their corresponding buttons in the interface.

### Editing Sounds

There are several ways that you can select a sound to edit. You can hold down the Shift key and press an associated keyboard key, you can hold down the Shift key and click a button in the interface, or you can toggle on Editing Mode by selecting Edit > Editing mode from the menu bar. In Editing Mode, press any supported key that's been assigned a sound or click a button in the interface to select a sound to edit.

Once you select a sound, an Edit Sound dialogue box pre-populated with the information about the selected sound will appear. You can change any of the sound's attributes and then click the Apply Changes button to update the project. You can also delete the sound from the board by clicking the Delete button, or click Cancel to discard all changes. Clicking Copy opens the sound in another Edit Sound dialogue box where you can assign it to another key on the board and modify it as desired.

If you assign a new sound a key that is already associated with a sound or you assign an existing sound a new key that is already associated with another sound, you will be asked to resolve the conflict before the board will be updated. You can do this by selecting "Replace", "Replace and Edit", or "Cancel" from the following dialogue box. The Replace option replaces the existing sound with the new or updated sound. Replace and Edit also replaces the existing sound, but it brings up a new Edit Sound dialogue box where you can select a new key for the sound that was replaced. Clicking Cancel cancels the action and returns you to the Add or Edit Sound dialogue where you can select a new key for the sound and continue editing it.

You can have as many Edit Sound dialogue boxes open as assigned keys on the board, but you can have only a maximum of five New Sound dialogue boxes open at a time. These dialogue boxes include ones that contain copies of sounds created by the "Replace and Edit" and "Copy" actions. If you have too many New Sound dialogue boxes open when you attempt to perform either of these actions, you will be asked to close some of them first.

## File Menu

The File Menu on the menu bar allows you to Open, Save, and Close Cue Masher ".cuemasher" project files.

### New

When you open Cue Masher, a new project is automatically open. To open a new project from the menu, select File > New. You may be prompted to save or discard the changes to your current project.

### Open

To open an existing .cuemasher file on your computer, select File > Open from the menu bar. You may be prompted to save or discard the changes to your current project. Select a .cuemasher file from your computer in the following File Chooser. The interface will be populated with the file's contents.

If the file is not a valid .cuemasher file or isn't formatted properly, the application will still attempt to read it and populate the interface from it as much as possible. Any sound-keyboard definitions in the file that it fails to read will not be imported into the project. At the end of the process the application will report that an error occurred and ask if you wish to keep the project open in a dialogue box. If you proceed and Save the project, the badly formatted file will be overwritten with what data could be imported into the project. If the file is one that you wrote manually, you may want to close the project instead and attempt to properly format it first so that you don't lose your work.

### Save

To save the open project, select File > Save from the menu. If you have a new project open that hasn't been saved to a file yet, the save option will appear as "Save..." in the File menu and will prompt you to select a name and location to save your project at. If your project has been modified since the last time you saved the file, the save option will appear as "Save*" in the File menu. If you have no unsaved changes, the option will appear as "Save".

### Save As

Select File > Save As from the menu bar to save the open project to a new or existing .cuemasher file. If you are seeing error messages when trying to save your project file, try this option to select a different file location.

### Reset Sounds

Click this option to reset all Stoppable and Toggleable sounds to the beginning of their sound files.

### Quit

You can close Cue Masher by selecting File > Quit from the menu bar or by clicking the exit button in the corner of the window. You may be prompted to save or discard the changes to your current project.

## Cue Masher Project File Formats

.cuemasher files are text files with a ".cuemasher" extension that define the sound-keyboard mappings that will be displayed in the Cue Masher interface when the project file is open in the application. Each sound-keyboard mapping in the file includes such information as the full path to the .wav file, the key code on the keyboard that plays the sound, and the short name of the sound to display in the interface. 

Cue Masher reads and writes these files when projects are opened and saved, but the format is simple enough that you can write and edit .cuemasher files in any text editor. Advanced users may find this useful for mapping sounds to keys that otherwise would not be allowed (sounds probably won't show up in the interface but may still be playable from the keyboard) and otherwise getting around the limits enforced in the interface :). For convenience, if you're writing your .cuemasher file manually, the application writes the name of the key and its associated key code to the Console when you press any key while the application is running. This is true whether the key is mapped to a sound or not.

### XML Format

Each sound in a .cuemasher XML file is expected to be encapsulated in "Sound" open and closing tags. These tags and their contents can span multiple lines, and the contents of each "Sound" tag don't need to be in a particular order. The format for each sound should be as follows:

```
<Sound>
    <Path>full path to the sound file, .wav only</Path>
    <KeyCode>the key code of the keyboard key</KeyCode>
    <KeyName>the name of the key to display in the GUI</KeyName>
    <SoundName>the name of the sound to display in the GUI</SoundName>
    <Stoppable>0 if the sound can't be stopped with the Spacebar, 1 if it can</Stoppable>
    <Toggleable>0 if the sound can't be toggled with its key/button, 1 if it can</Toggleable>
</Sound>
```

For example, the following sound definition specifies that the sound file located at "C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav" should be played when the "1" key, which has the key code "49", is pressed. This sound effect is displayed as "Milky Way" in the GUI and can be stopped before it finishes by pressing the Spacebar as specified by the Stoppable attribute "1". It's not Toggleable and so can't be stopped by pressing "1" again.

```
<Sound>
    <Path>C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav</Path>
    <KeyCode>49</KeyCode>
    <KeyName>1</KeyName>
    <SoundName>Milky Way</SoundName>
    <Stoppable>1</Stoppable>
    <Toggleable>0</Toggleable>
</Sound>
```

### CSV Format (deprecated)

The current version of Cue Masher isn't set up to read or write CSV files anymore, but the code for this still exists in the code base. Under the .cuemasher CSV file format, the ',' isn't a supported key, the Toggleable attribute isn't supported, and file paths and sound names with commas in them will not be read correctly. Each line should be formatted as follows:

[Full path to the sound file, .wav only, no commas],[the key code of the keyboard key],[the name of the key to display in the GUI, no commas],[the name of the sound to display in the GUI, no commas],[0 if the sound can't be stopped with the Spacebar, 1 if it can]

For example, the following line specifies that the sound file located at "C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav" should be played when the "1" key, which has the key code "49", is pressed. This sound effect is displayed as "Milky Way" in the GUI and can be stopped before it finishes by pressing the Spacebar as specified by the final "1" at the end of the line.

C:\\Amelia Earhart\\FinalAudio\\Music\\Along the Milky Way.wav,49,1,Milky Way,1

## Feature Requests and Known Bugs

The following is a list of feature requests and known problems. These I will work on as I find that I need them for my own use or as requested.

* Add support for ',' key.
* Needs ability to fade sound effects and music rather than hard stopping them.
* Needs ability to raise or lower sound volume per sound.
* Needs ability to loop a sound.
* Needs ability to play sounds of different file types.
* Needs ability to accept keyboards with different key configurations.
* Needs to display buttons in a layout so that buttons are always aligned.

You can send your feature requests, bug reports, questions, and comments to me at gatesmediablog (at) gmail (dot) com.