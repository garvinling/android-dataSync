package com.induzione.sync;

import java.util.HashMap;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GameData {
	/**
	 * @author Garvin Ling
	 * 
	 * Class: GameData()
	 * 
	 * Description: This class handles all of the data calculations
	 * for the game parameters and statistics.  
	 * 
	 * User:
	 * Just create a new GameData object with the passed parameters. 
	 * The object will automatically retrieve the correct user from the Parse table.
	 * 
	 * In your Main: 
	 * All you need to do is call 
	 * calculateXP(), calculateCurrency(), uploadData()   <--in that order
	 * 
	 */
	
	public GameData(String ID,int mpg,int load,int throttle){
	/** Set Object ID for ParseObject, calculate Efficiency Rating and Driver Habit **/ 
		setObjectID(ID);
		initStructures();
		calculateEfficiencyRating(mpg);
		calculatedriverHabit(load,throttle);
		getUser();
		getUserData();			//Sets the instance variables for XP,Level,Currency
		
	}
	
	public void setObjectID(String ID){
		
		objectID = ID;
	}
	
	public String getObjectID(){
		
		return objectID;
	}
	
	
	public void getUserData(){
	  	  /**
         * getUserData()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the newly created ParseObject from getUser()
         * and retrieves the user's current XP, Level, and Currency information
         * and stores it to the instance variables.
         * 
         */
		
	currentXP = userStats.getInt("xp");
	currentLevel = userStats.getInt("level");
	currentCC = userStats.getInt("currency");
	System.out.println("User is level:" +currentLevel);	
	System.out.println("User has:" +currentXP +" XP");
	System.out.println("User has:" +currentCC +" cc");
	}
	
	
	public void getUser(){
	  	  /**
         * getUser()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the instance variable ObjectID to
         * create a new Parse Object to retrieve XP, Level, Currency Stats
         * 
         */
		ParseQuery query = new ParseQuery("Userdata");
		try {
			userStats = query.get(objectID);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not get userStat");
			e.printStackTrace();
		}
			
	}
	
	public void calculateEfficiencyRating(int MPG){
		
	  	  /**
         * calculateEfficiencyRating(int MPG)
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the MPG Reading and outputs the Efficiency Score
         * Scale
         * 1 - <15mpg
         * 2 - 16-20mpg
         * 3 - 21-26mpg
         * 4 - 27-35mpg
         * 5 - >35mpg
         * 
         */

		if(MPG < 15){
			efficiency = 1;
		}
		else if(MPG>=16 && MPG<=20){
			efficiency = 2;
		}
		else if(MPG>=21 && MPG<=26){
			efficiency = 3;
		}
		else if(MPG>=27 && MPG<=35){
			efficiency = 4;
		}
		else if(MPG>=35){
			efficiency = 5;
		}
			
	}
	
	public void calculatedriverHabit(int throttle, int load){
	  	  /**
         * calculatedriverHabit(int throttle, int load)
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the throttle and load Reading and outputs the driver habit Score
         * 
         * EngineLoad 
         * Need to drive and test the car under load for accurate numbers. 
         * 0 = >80%
         * 1 = 60%> <80%
         * 2 = <60%
         * 
         * Throttle Position 
         * 1 = >80%
         * 2 = 60%> <80%
         * 3 = <60%
         * 
         * Will sum two values together to get driver habit rating
         * 
         */
		int throttleScore = 0;
		int loadScore = 0;
		
		if(throttle>80){
			throttleScore = 1;
		}
		else if(throttle>60 && throttle<80){
			throttleScore = 2;
		}
		else if(throttle<60){
			throttleScore = 3;
		}
		
		if(load>80){
			loadScore = 0;
		}
		else if(load>60 && load<80){
			loadScore = 1;
		}
		else if(load<60){
			loadScore = 2;
		}
		
		driverHabit = throttleScore + loadScore;	
	}
	
	public void calculateAchievement(){
		
		/**Parse code to check if achieved **/ 
		boolean achieved = false;
		
		if(achieved)
			achievement = 1;
		else
			achievement = 0;
		
		
	}
	
	
	public void printRatings(){
		
		System.out.println("Efficiency Rating:" +efficiency);
		System.out.println("Driver Habit Rating:" +driverHabit);
	}
	
	public boolean checkLevelUp(int xp){
	  	  /**
         * checkLevelUp()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method is to be called after the stats have been calculated/updated
         * checks the XP against the scale to see if a level up is needed. If it is, increments
         * the level, add bonuses and stores it to the database.
         * 
         * Returns true if there is a level up needed
         */
		  int newLevel = 0;
		  Level level = new Level(xp);
		  newLevel = level.getLevel();
		  
		  
		if(currentLevel!=newLevel){
			currentLevel = newLevel; //set new Level 
			currentCC = currentCC + levelBonusCC[currentLevel]; //Add bonus for leveling up
			System.out.println("Leveled up!");
			return true;
		}
		else{
			return false;	
		}
	}
	
	public double getXPMultiplier(int level){
	  	  /**
         * getXPMultiplier()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the current user's level and returns the appropriate
         * XP multiplier. 
         * 
         */
		if(level<=5)
			return 1.5;
		else
			return 1.8;
		
		
	}
	
	public double getCCMultiplier(int level){
	  	  /**
       * getCCMultiplier()
       * Author: Garvin Ling
       * Date: 03/27/13
       * 
       * This method takes in the current user's level and returns the appropriate
       * CC multiplier. 
       * 
       */
		if(level<=5)
			return 1.2;
		else
			return 1.4;
		
		
	}
	
	
	public void calculateXP(){
	  	/**
         * calculateXP()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method first checks the user's current level to get the correct multiplier.
         * 1-5:1.5
         * 6-10:1.8
         * 
         * Parse Object:UserStats
         * 
         * Checks the efficiency rating and gets the amount of xp. 
         * Checks the driver habits rating and gets the amount of xp.
         * Checks the achievement rating and gets the amount of xp
         * Add the XP to the current XP
         * Check if there is a level up.
         * If there is, update the level.
         * 
         * cast to int after
         */
		 double multiplier;
		 int efficiencyBonus;
		 int driverHabitBonus;
		 boolean levelUp;
		 int xpGained;
		 multiplier = getXPMultiplier(currentLevel);
		 efficiencyBonus = efficiencyXP[efficiency];		//Retrieve Bonus based on efficiency score 
		 driverHabitBonus = driverHabitXP[driverHabit];		//Retrieve Bonus based on driver habit score
		 xpGained = (int)(multiplier)*(efficiencyBonus+driverHabitBonus);
		 expGained = xpGained;
		 currentXP = currentXP + xpGained;
		 levelUp = checkLevelUp(currentXP);
	
		 if(levelUp){
		 }
		 System.out.println("You have gained: " +xpGained +" xp!");
		
	}
	
	public void calculateCurrency(){
	  	/**
         * calculateCurrency()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method takes in the three game stats and calculates 
         * the currency bonuses
         * 
         * Everyone starts with 200cc
         * 
         * Check the level to get the CC multiplier. 
         * 1-5: 1.2 (20%)
         * 6-10: 1.4 (40%)
         * 
         * Check the efficiency rating and gets the amount of cc; add to total.
         * Check the driver habits rating and gets the amount of cc;
         * Check the achievement rating and gets the amount of cc
         */
		 double multiplier;
		 int efficiencyBonus;
		 int driverHabitBonus;
		 boolean levelUp;
		 multiplier = getCCMultiplier(currentLevel);
		 int CCGained;
		 
		 efficiencyBonus = efficiencyCC[efficiency];		//Retrieve Bonus based on efficiency score 
		 driverHabitBonus = driverHabitCC[driverHabit];		//Retrieve Bonus based on driver habit score
		 CCGained = (int)(multiplier)*(efficiencyBonus+driverHabitBonus);
		 currentCC = currentCC + CCGained;
		 levelUp = checkLevelUp(currentXP);
		 
		
		System.out.println("You have gained:" +CCGained +"currency!");
		 
		
		 
	}
	
	public void uploadData(){
		
		userStats.put("level", currentLevel);
		userStats.put("xp",currentXP);
		userStats.put("currency",currentCC);
        userStats.saveInBackground();


	}
	
	public int getxpGained(){
		
		
		return expGained;
	}
	
	public void initStructures(){
	  	  /**
         * initStructures()
         * Author: Garvin Ling
         * Date: 03/27/13
         * 
         * This method initializes the structures for the
         * bonus XP/CC scaling
         */

		/**Init the Efficiency Scale for XP**/
		efficiencyXP[0] = 0;
		efficiencyXP[1] = 0;
		efficiencyXP[2] = 4;
		efficiencyXP[3] = 8;
		efficiencyXP[4] = 10;
		efficiencyXP[5] = 12;
		
		driverHabitXP[0] = 0;
		driverHabitXP[1] = 0;
		driverHabitXP[2] = 2;
		driverHabitXP[3] = 3;
		driverHabitXP[4] = 4;
		driverHabitXP[5] = 5;
		
		efficiencyCC[0] = 0;
		efficiencyCC[1] = 5;
		efficiencyCC[2] = 15;
		efficiencyCC[3] = 25;
		efficiencyCC[4] = 40;
		efficiencyCC[5] = 45;
		
		
		driverHabitCC[0] = 0;
		driverHabitCC[1] = 5;
		driverHabitCC[2] = 20;
		driverHabitCC[3] = 30;
		driverHabitCC[4] = 40;
		driverHabitCC[5] = 50;
		
		
		levelBonusCC[0] = 0;
		levelBonusCC[1] = 0;
		levelBonusCC[2] = 300;
		levelBonusCC[3] = 375;
		levelBonusCC[4] = 400;
		levelBonusCC[5] = 500;
		levelBonusCC[6] = 600;
		levelBonusCC[7] = 700;
		levelBonusCC[8] = 800;
		levelBonusCC[9] = 900;
		levelBonusCC[10] = 1000;
		
	}
	
	
	
/**Instance Variables**/
	
	
	
	private String objectID;
	private int achievement;   
	private int efficiency;  
	private int driverHabit; 
	private int expGained;
	private int currencyBonus;
	private int xpBonus;
	
	private int currentLevel;
	private int currentXP;
	private int currentCC;
	
	private int[] efficiencyXP = new int[6];
	private int[] driverHabitXP = new int[6];
	
	private int[] efficiencyCC = new int[6];
	private int[] driverHabitCC = new int[6];
	
	
	private int[] levelBonusCC = new int[11];
	
	private ParseObject userStats;
	/** need to supply a max / min from Level()? **/
	
}
