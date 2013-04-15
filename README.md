android-gameData
================
package: com.induzione.sync;

This is the package from my senior project that handles the data syncing from the firmware of the ARM7
to the Android application. 


-GameData():   Handle the raw data conversion from OBD-II car data to custom game data.

-Data():       Decodes and stores the passed value into its proper parameter.

-Level():      Data structure that holds the Experience point ranges for each level.


Details
====================

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

Level()
------------------
