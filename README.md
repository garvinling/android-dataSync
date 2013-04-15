android-gameData
================
package: com.induzione.sync;

This is the package from my senior project that handles the data syncing from the firmware of the ARM7
to the Android application. 

-MainActivity(): Perform communication and syncing functionality from ARM firmware to the Android app.

-GameData():   Handle the raw data conversion from OBD-II car data to custom game data.

-Data():       Decodes and stores the passed value into its proper parameter.

-Level():      Data structure that holds the Experience point ranges for each level.


Details
====================

MainActivity()
--------------------
The main activity is the home screen for our senior project application.  This activity is responsible for 
"syncing" the data from the ARM firmware and sending it off to the classes mentioned below in order to be properly
calculated and converted.  

The data being read in is space-delimited.  That is, the application reads in data parameters one char at a time until
it reads a space or a '0x20'.  Once it hits a space, we know that we have retrieved one parameter.  We then send it off
to the Data class to be decoded/converted.  




GameData()
--------------------
The GameData() class takes in the calculated averages of the mpg, engine load, throttle position from the Data() class
and calculates the correct game values. 

Calculates total xp and currency gained from each sync.  Uses the Level() class to check if the user's level has increased.

Use the GameData() class after obd-II values have been calculated from Data().

Input 
    MPG
    Engine Load
    Throttle Position 

Output 
    Efficiency Rating (1-5)
    Driver Habit      (1-5)
    

Data()
------------------
The Data() class handles the decoding of each data byte received from the firmware. 
The decode function takes in the byte and the current index of the array to determine what type of data 
we are dealing with.  The Data() class also handles the addition and averaging of all the data values received from the
OBD-II port. 

Level()
------------------
The Level() class provides us with the data structure to hold the experience point ranges for each level.  This class is used
whenever we want to check whether or not a user leveled up. Provides a min/max value for each level for an experience-point based 
progress bar.
