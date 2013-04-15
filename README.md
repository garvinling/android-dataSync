android-gameData
================
package: com.induzione.sync;

This is the game data class implemented in my senior project.  Wrote this class to handle the raw data conversion from OBD-II car data to custom game data.  

The GameData() class is one of three classes written to sync the data from vehicle to our database. 
-GameData()
-Data()
-Level()

Detailed Description
====================
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
    
    
