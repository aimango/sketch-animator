CS 349 Assignment 3 README
Elisa Lou 20372456


How to Run:
-The makefile is located in the "src" folder. 
-"make run" or "make" will compile and run the program.
-"make clean" will remove all .class files in the "src", "src/view", and "src/model" folders.

Notes:
-The top toolbar contains buttons for drawing (pencil), erasing (eraser), selecting (dotted square), and inserting frame (copy), as required. Users can deselect selected objects by toggling the select button if they want to reselect different objects. There is also a button for deleting the animation (trash can) to allow users to easily restart from scratch.
-The slider below the canvas area is only activated when there is at least one frame. Frame ticks appear as users begin to animate. The tick spacing is set to 20 frames, and I chose not to show the labels since it would become cluttered once the animation consists of hundreds of frames.
-The bottom toolbar contains buttons typical of any video player.  |< allows the user to skip to frame 0, while >| allows user to skip to the last frame. << allows users to decrease by 1 frame, and >> allows users to increase by 1 frame. The play/pause button will allow user to play the animation and pause when desired. if the slider is at the last frame and the play button is pressed, the animation will be played from the beginning. Otherwise, it will be played from wherever the slider left off.
-The right toolbar is reserved for drawing options. Users may choose from preselected colours, or choose their own colour via the colour picker dialog that appears when clicking the default grey "Picker". They also have the option of selecting from 3 stroke sizes. These stroke sizes are also reflected in the cursor size once selected.

Enhancements:
-Colour and stroke customization for drawing.
-Custom cursors for drawing (varies for stroke size) and erasing.
-Button enable/disable logic: 
	-disallow users from clicking play until an animation exists.
	-guides users to tell if they are in draw or erase mode as the respective buttons would be disabled during either mode. 



