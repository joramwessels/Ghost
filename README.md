Ghost
=====
**Ghost** is a mobile game application for Android that provides a local multiplayer version of the game of Ghost.

GUI
---
The app will consist of only three activities.
  * **WelcomeGUI**.class
  * **GameGUI**.class
  * **FinishGUI**.class

*WelcomeGUI* asks for the player names with two text fields and a button, and handles the shared preferences concerning player names.
Previous games are shown in a drop down options menu. The names are then passed on as a bundle to *GameGUI*,
where the actual game will be played.
*GameGUI* consists of the TextView that shows the current word status, a text field,
which closes its keyboard after a character has been added, enables player input in combination with the submit button below it.
Turns are indicated by two small TextViews stating the player's names, highlighted if it's their turn. As the game consists of multiple rounds, two rating bars will indicate the amount of lives left for each player.
*GameGUI* also communicates with the back end to enable game progress.
When the back end notifies the activity about a won game, *GameGUI* changes to *FinishGUI*, which merely states the winner in a TextView and shows a ranked scrollable list of high scores.

A menu bar on top of the app provides a settings menu consisting of
  * language selection (English/Nederlands)
  * restart
  * remove saved data
Language differences are stored in strings xml files. The language selection is stored in the shared preferences and defaults
to Dutch. The restart option restarts the entire app, returning to the *Welcome* activity.

Event listeners that ought to be changed are onPause(), onResume() and onCreate(). They save and load non finished games from the shared preferences.

Back end
--------
The back end is based on two java classes called **Game**.class and **Lexicon**.class. *Game* keeps track of all game data and provides public methods
for game progression. After initializing a *Game* object, providing player names, a move can be made by calling *guess()*.
~~~java
Ghost game = Ghost();
String message = game.move(String move);
if(!message.equals("OK")) {
    System.out.println(message);
}
~~~
The *guess()* function checks the input on both syntaxical and game related rules, and changes the game state if everything is valid. If not, it returns a message stating the problem.

Impressions
-----------
![Welcome.class](welcome_activity.png "The welcome screen")
![Game.class](game_activity.png "The actual game")
![Finish.class](finish_activity.png "The high scores screen")

Discovered bugs
----------
  * layout breaks when keyboard pops up

Unfinished backlog
----------
  * Multi-language GUI
  * Game state save
  * Changing the length of WORD dynamically to always fit on screen
  * Keyboard in GameGUI disappearing on entering first character
