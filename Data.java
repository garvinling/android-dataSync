package com.induzione.sync;


/**
 * @author Garvin Ling
 * 
 * Class: Data()
 * 
 * Description: This class handles all of the parameters 
 * received from the firmware handles the adding and averaging functions 
 * required by the Induzione Program. 
 * 
 */
public class Data {

	
	public Data(){
		
		mpg = 0;
		speed = 0;
		throttle = 0;
		load = 0;
		
	}
	

	public void decode(int data,int index){
		
	  	  /**
         * decode(int data,int index)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method takes in the actual data and decides which method to call
         */
		
		if(index==0){
		//lengthbyte do nothing	
		}
		else if(index ==1){
		//MPG
		setMPG(data);
		System.out.println("Set MPG: " +data);
		}
		else if(index ==2){
		//Speed
		setSpeed(data);
		System.out.println("Set Speed: " +data);
		}
		else if(index ==3){
		//Throttle	
		setThrottle(data);
		System.out.println("Set Throttle: " +data);

		}
		else if(index ==4){
		//Load	
		setLoad(data);
		System.out.println("Set Throttle: " +data);

		}
		else if(index ==5){
		//checksum	
			System.out.println("Unknown:");
	
		}else{
			System.out.println("No decode value found");
			
		}
		
	}
	
	
	public void calculateMPG( double VSS, double MAF ){
	  	  /**
         * calculateMPG(int LTFT,int VSS, int MAF)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method takes in PID parameters to calculate the MPG
         *
         *	Alternative: This value can be calculated in the firmware. 
			LTFT given in a percentage 
			VSS -   0-255		km/h  PID:010D
			MAF - 	0-655.35	grams/sec PID:0110
         */
			double test;
			test = 71.07 * VSS / MAF;
		System.out.println("Calculated MPG:" +test);
	}
	
	
	
	public void setMPG(int MPG){
	  	  /**
         * setMPG(int MPG)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method keeps adding the MPG value to the instance variable
         */
		mpg += MPG;
	
	}
	
	public void setSpeed(int SPEED){
	  	  /**
         * setMPG(int MPG)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method keeps adding the Speed value to the instance variable
         */
		speed +=SPEED;
		
	}
	
	public void setThrottle(int THROTTLE){
	    /**
         * setThrottle(int THROTTLE))
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method keeps adding the Throttle value to the instance variable
         */
		throttle+=THROTTLE;
		
	}
	
	public void setLoad(int LOAD){	
	  /**
         * setLoad(int LOAD)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method keeps adding the Load value to the instance variable
         */
		load+=LOAD;
	}
	
	public void setAverages(int numSamples){
		
	  	/**
         * setAverages(void)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method evaluates the accumulated values with the numSamples from the main.
         */
	
		 avgMpg = mpg/numSamples;
		 avgSpeed = speed/numSamples;
		 avgThrottle = throttle/numSamples;
		 avgLoad = load/numSamples;	
	}
	
	/**GET Functions **/
	public int getMPG(){
	  	/**
         * getMPG()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method returns the MPG value to the instance variable
         */
		return avgMpg;
	
	}
	
	public int getSpeed(){
	  	/**
         * getSpeed()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method returns the speed value to the instance variable
         */
		
		return avgSpeed;
		
	}
	
	public int getThrottle(){
	  	/**
         * getThrottle()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method returns the throttle value to the instance variable
         */
		
		return avgThrottle;
		
	}
	public int getLoad(){
	  	/**
         * getLoad()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method returns the Load value to the instance variable
         */
		
		return avgLoad;
		
	}
	
	
	public void print(){
		System.out.println(avgMpg);
		System.out.println(avgSpeed);
		System.out.println(avgThrottle);
		System.out.println(avgLoad);   
	}
	
	
	/**Instance Variables**/
	private int mpg;
	private int speed;
	private int throttle;
	private int load;
	
	private int avgMpg;
	private int avgSpeed;
	private int avgThrottle;
	private int avgLoad;
	
}
