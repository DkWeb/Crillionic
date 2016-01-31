# Crillionic
Crillionic is a casual game inspired by the classic title "Crillion" for the Commodore C64 (https://www.c64-wiki.com/index.php/Crillion). The main game loop is very easy (and is itself some kind of clone of the well-known Breakout theme): 

**control a ball and destroy all blocks of a level to reach the next one.**

But in contrast to Breakout you have neither a paddle nor do you have to be afraid of losing the game by touching the ground. Instead, you must punch your ball against special "marker" bricks, colorizing it. After this transformation you can use the colored ball to destroy all bricks of the very same color. While you are cleaning up the level you must take care to not touch "killer" bricks, immediately destroying you.

This remake is using Libgdx for targeting the Android mobile platform instead of the good old C64. So, the controls have been adapted to support touch gestures: while your ball is jumping around the level you must "control" it by dragging your screen and so giving the ball some kind of impulse. Dragging a longer distance will lead to a stronger impulse, dragging just a short track will give it only a little "clap".

## Some Screenshots
![The main menu](/screenshots/menu.png?raw=true "The main menu")
![Colorize the ball](/screenshots/play.png?raw=true "Colorize the ball")
![Level completed](/screenshots/level_completed.png?raw=true "Level completed")

Unfortunately I'm not a designer, so I had to start with some "hand-made" textures for the blocks, walls and particle
effects. Hopefully I will find someone in the future who can improve the look of the game.

## Why?
I've had two main reasons to start with this project: first of all, I really love the original "Crillion" because of its simplicity and the immense "only-just-one-more-level-for-today"-factor. But even if you can play it today with an emulator like "Frodo C64" on your Android phone this isn't fully satisfying: using the little joystick emulation on a 5" display is not as accurate as it was in the old days and so you will die very often just because of your clumsy fingers.

The second one is, that I think it is a good project to train my Libgdx skills: the game loop is simple and so you can fully concentrate on the technical aspects.

## Current state
The project itself can be compiled to an APK and works well on an Android device running Marshmallow (Android 6.0.1). The game itself is not in a very advanced state. Up to now there is just a simple main menu, some sort of highscore and a basic first level to have a playground for debugging and testing. Obviously there is still a lot of things to be done but I've tried to lay a "solid foundation" for the further work, including i18n resources, using box2d for physics and extracting the level description into an external file format. 

## You want to help?
That would be great! This is just some kind of spare-time-project, so having some help would increase the speed of development enormously. So, if you are a fan of the original Crillion and want a up-to-date version for your android device, just feel free to clone this repo :-).