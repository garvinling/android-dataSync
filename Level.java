package com.induzione.sync;

public class Level {
	/**
	 * @author Garvin Ling
	 * 
	 * Class: Level()
	 * 
	 * Description: Data structure to hold the level ranges for any given user.  
	 * Also holds the min-max value for setting progress bar.  
	 * 
	 */
	public Level(int xp){
		
		setXP(xp);
		setLevel();
		
	}
	
	public void setXP(int xp){
		
		experience = xp;
	}
	
	public int getXP(){
		
		return experience;
	}
	
	public void setLevel(){
		
		
		if(experience>=0 && experience<=100){
			level = 1;
			lower=0;
			upper=100;
		}
		else if(experience>=100 && experience<=200){
			level = 2;
			lower=100;
			upper=200;
		}
		else if(experience>=200 && experience<=400){
			level = 3;
			lower=200;
			upper=400;
		}
		else if(experience>=400 && experience<=600){
			level = 4;
			lower=400;
			upper=800;
		}
		else if(experience>=600 && experience<=800){
			level = 5;
			lower=600;
			upper=800;
		}
		else if(experience>=800 && experience<=1000){
			level = 6;
			lower=800;
			upper=1000;
		}
		else if(experience>=1100 && experience<=1500){
			level = 7;
			lower=1100;
			upper=1500;
		}
		else if(experience>=1500 && experience<=2000){
			level = 8;
			lower=1500;
			upper=2000;
		}
		else if(experience>=2000 && experience<=2600){
			level = 9;
			lower=2000;
			upper=2600;
		}
		else if(experience>=2600 && experience<=3100){
			level = 10;
			lower=2600;
			upper=3100;
		}
		else
			level = 1;

		
	}
	
	public int getLower(){
		
		return lower;
		
	}
	
	public int getUpper(){
		
		return upper;
	}
	
	public int compareLevel(int currentLevel){
		
		return level;
		
	}
	
	
	public int getLevel(){
		return level;
	}
	
	/** Instance Variables**/
private	int experience;
private	int level;
private int upper;
private int lower;
	
}
