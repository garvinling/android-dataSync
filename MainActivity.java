package com.induzione.homescreen;
/**
import giac.fwa.GIACFlashloader.Accessories.Accessories;
import giac.fwa.GIACFlashloader.RacersToolbox.RacersToolbox;
import giac.fwa.GIACFlashloader.Switcher.DeviceListActivity;
import giac.fwa.GIACFlashloader.Switcher.Flashloader;
import giac.fwa.GIACFlashloader.Switcher.SwitcherDemo;
import giac.fwa.GIACFlashloader.UpdateFirmware.UpdateFirmware;
**/
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.induzione.sync.*;
import com.induzione.LiveData.LiveParameters;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.parse.*;



import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	String url = "http://www.giacusa.com";
	Intent mIntent = new Intent(Intent.ACTION_VIEW);
	Uri u = Uri.parse(url);
	Context context = this;
	
	
	private int testing = 0;
	public String tmp;
	private static final boolean D = true;
	private static final String TAG = "GIAC Flashloader Bluetooth MOD";
	private int buff= 0;
	private int[] c = new int[10];
	private int[] testData = {6,10,22,32,13,55};
	private int lengthByte = 6;
	private boolean dataComplete = false;
	private boolean packetComplete = false;

	private int level;
	private String  userName;
	private int currency1;
	private int upper;
	private int lower;
	private int xpPerLevel;
	private int xpGained;
	private int dataCounter=0;
	
	/**Graph Variables**/
	private int numOfSyncs;
	private int[] xpHistory = new int[5];
	private GraphViewData[] gData = new GraphViewData[5];

	

	/**Game Variables **/
	private int avgMPG;
	private int avgSpeed;
	private int avgThrottle;
	private int avgLoad;
	private int numSamples;

	/**Latitude/Longitude into a data structure**/ 
	private int latitude;
	private int longitude; 
	
	/**UI variables **/
	private Button send;
	private Button liveData;
	private Button blackbox;
	private Button getTest;
	private TextView title;
	private TextView connected;
	private TextView currency;
	private TextView lvl;
	private TextView welcome;
	private ProgressBar exp;
	private TextView xpUntil;
	private TextView upperXp;
	private ProgressBar syncProg;
	private TextView experience;
	
	/** DB Variables **/
	private String username;
	private String objectID;
	private String objectIDUserdata;
	ParseObject userDat;
	
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket mBluetoothSocket = null;
	private BluetoothDevice mBluetoothDevice = null;
	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	private InputStream tmpIn = null;
	ParseUser currentUser = ParseUser.getCurrentUser();

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // RovingNetworks MAC ADDRESS
    private static String address = "00:06:66:06:BD:71";
    private static String address3 = null;	
    
    // Booleans for threads
    boolean done = false;
    boolean conPushed = false;
    boolean conn = false;
    boolean isTryingToConnect = false;
    
	AlertDialog demoRequest;
	AlertDialog faqAlert;

    final BluetoothGlobals bluetoothGlobals = new BluetoothGlobals();
    

	private final Handler mHandler = new Handler();
	private Runnable mTimer1;
	private Runnable mTimer2;
	private GraphView graphView;
	private GraphViewSeries exampleSeries1;
	private GraphViewSeries exampleSeries2;
	private double graph1LastXValue = 6;
    
	/* Called when the activity
	 * is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
      
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_main);
    	Parse.initialize(this,"8OF6j6sr5I4REke65juG7MJAuykYY75KAnV1Sz8J", "s9dlSCUhQiBUpnV6KyP3CyRICNI9g44zZNsDBaa7");
		
    	initViews();   //Initialize the text/fonts 
    	updateInfo(currentUser);
    	setProgressBar(userDat.getInt("xpProgress")); 
    	
    	
    	setHistoryGraph();

        // Get local BluetoothAdapter
        mBluetoothAdapter = bluetoothGlobals.getDefaultAdapter();        
        // If the adapter is null, then Bluetooth is not supported
        if(mBluetoothAdapter == null) {
        	Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }
   
        // If Bluetooth is not on, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled())
        {
        	Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        	startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else if (mBluetoothAdapter.isEnabled()) {
        	return;
        }
           
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
       
   
    	/** Test Cases
    	 * 
    	 * #1: Pass Checksum : PASS				 	-gling 3/27/13
    	 * #2: Data accumulation/averaging: PASS 	-gling 3/27/13
    	 * #3: Get User from Parse: PASS		 	-gling 3/27/13
    	 * #4: Retrieve User data from Parse: PASS 	-gling 3/27/13
    	 * #5: Write out new data to Parse: PASS 	-gling 3/27/13
    	 * #6: Break Checksum: PASS					-gling 3/27/13
    	 * #7: MPG
    	 */
  
    	

     	/**userDat is now an object pointed to the userData table**/ 
     	
     	bluetoothGlobals.cancelDiscovery();    
     	checkConnection();
     
     	getTest.setOnClickListener(new OnClickListener(){
            
          	 public void onClick(View v){
          		 //TODO Code
          		/**TESTING: Calculate MPG.**/

                new Thread(new Runnable() {
     			    public void run() {
     			    	
     	                System.out.println("Checking Game Data...");
     	                int mpg = (int)getRandom();
     	                int load = (int)getRandom();
     	                int throttle = (int)getRandom();
     	              
     	                GameData data = new GameData(userDat.getObjectId(), mpg,load,throttle);
     	                data.calculateXP();
     	                data.calculateCurrency();
     	                data.uploadData();
     	                data.printRatings();
     	                xpGained = data.getxpGained();
     	         
			     	    runOnUiThread(new Runnable() {
			   			        
			     	    		public void run() { 	 
					   			          	                
					   	                updateInfo(currentUser);
					   	                setProgressBar(xpGained);			
							   	}
			     	 
			     	    }); //end runonUIThread
			     	    
			     	    
			            
			     	    runOnUiThread(new Runnable() {
			   			        
			     	    		public void run() { 	 
					   			          	                
			     	    		    updateGraph(); 
							
							   	}
			     	 
			     	    }); //end runonUIThread	    
				     	                
     			    }
				}).start(); //end thread

          	 }
          	 
    });//end onclick

           
       liveData.setOnClickListener(new OnClickListener(){
    
        	 public void onClick(View v){
        		 //TODO Code
        		 // Starts the Live Data Logging Activity
        		 	
			    	Intent myIntent = new Intent(v.getContext(), LiveParameters.class);
	                startActivityForResult(myIntent, 0);

        	 }
        	 
         });
        		 
	 
        		 	        
         send.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
            	 send.setEnabled(false);

            	 new Thread(new Runnable() {
     			    public void run() {
     	              
                    //Starts the Syncing activity for the firmware

     			    if(BluetoothGlobals.mBluetoothSocket!=null)
     			        syncData();	//Sync data with Database 
     			    else{
     			    	System.out.println("Bluetooth is not connected.");
     			    }
   
 			    }
 			  }).start();             
            }
         });
     	 new Thread (new Runnable() {
     		 public void run() {
     			 //mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
     			 mBluetoothDevice = bluetoothGlobals.getRemoteDevice(address);
     		 }
     	 }).start();

    }//end onResume
    
  
    public void syncData(){
        /**
         * syncData()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method will sync the data from the firmware in the ARM7 and upload it 
         * to the user's profile in the database. 
         */
    	 
         int message = 0x42;
         int counter = 0;
         int length = 0;
         boolean checksum = false;
         int OKIES;
         Data dataPacket = new Data();						//create new Data object -gling 3/25/13
         bluetoothGlobals.writeByte(message);
         
         length = 100;
         
         if(length > 0){         
        	 		System.out.println("We got the length! Length:" +length);
		 
        while(dataComplete!=true){
		        	
        	//Need a handshake to indicate the end of packet AND end of data
        	//~ = end of data, # = end of packet
        			 retrieveData(length,dataPacket); //Loop until done...Retrieve the data buffer and return checksum -gling 3/21/13   
		         
		         if(packetComplete == true){
		        	 numSamples+=1;				  	  			 //Increment number of samples taken after every loop -gling 3/25/13  
		          	 dataCounter = 0; 							 //Reset the data counter for the Data() decoding -gling 4/2/13
		        	 packetComplete = false;
		     		 System.out.println("Built " +counter +" string");
		          }
		             
         }//end while
       	   
         numSamples+=1;  //Add the last packet counter because we receive the ~ instead of the #. 
         
        	runOnUiThread(new Runnable() {
			         public void run() { 	 

			        	 	send.setEnabled(true);
			         }
			    });	 
       	 
         System.out.println("# of Samples:" +numSamples);
         dataPacket.setAverages(numSamples);             //Average all the values of the dataBytes
         dataPacket.print();							 //Prints the averaged values.  These values should be uploaded to the database.  
         											
         System.out.println("Uploading data to database...");
        
       /**Set the global instance variables **/
         avgMPG = dataPacket.getMPG();
         avgThrottle = dataPacket.getThrottle();
         avgLoad = dataPacket.getLoad();
         dataPacket.print();
  
         updateGameData();
         numOfSyncs+=1;
         System.out.println("Data uploaded to Parse.");
         System.out.println();
         System.out.println("-------NEW DATA------");

         
			    runOnUiThread(new Runnable() {
			         public void run() { 
			             updateGraph();

			         }
			    });
		
	}//IF: check that the length is > 0           
         else
        	 System.out.println("Invalid Data Length. Check connections");  
}//end syncData()
     
    
	private double getRandom() {
		double high = 3;
		double low = 0.5;
		return Math.random() * (high - low) + low;
	}
    
    public void retrieveData(int length,Data dataPacket){
        /**
         * retrieveData(int length)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method retrieves the data bytes from the firmware
         * c[length-1] = checksum byte
         * Checksum is the XOR of all the bytes
         * Send each byte over to calculateData() with index?
         * Returns: Checksum byte 
         * 
         * Length includes the length byte 
         * [0][1][2][3][4][5]
         */
    	
    	StringBuilder str = new StringBuilder();
    	
    	int tempData;
    	int decodeCounter=0;   //keeps track of the # of the built string to send to decode() in Data() -gling 
    	int checkSum =0;
    	int intConvert = 0;
        int message = 0x4B;
    	char ch1;
    	
    	for(int i = 0; i<length;i++){
    		
    		bluetoothGlobals.getInputStream();
        	tempData = bluetoothGlobals.readByte_timed(15000);
     		//System.out.println("Temp Data:" +tempData);

        	if(tempData == '#'){
        	/**End of packet handshake detected 
        	 * 
        	 * Behavior:Exit to get next data packet. Firmware needs to append # after each packet.
        	 */
		        	packetComplete = true;
		        	i = length + 1;
		        	System.out.println("Packet Termination Received.");
		        	bluetoothGlobals.writeByte(message);
		        	bluetoothGlobals.getInputStream();
		        	tempData = bluetoothGlobals.readByte_timed(15000);
		      
		        	if(tempData == 'K'){
		        	
		        		System.out.println("Firmware acknowledged packet.");
		        	}

        	}
        	else if(tempData == '~'){
            	/**End of data handshake detected 
            	 * 
            	 * Behavior: Data complete. Exit loop. 
	             */
	     				int message = 0x4B;
	     				dataComplete = true;
	     				i = length+1;
	     				System.out.println("Data Termination Received");
	     		        bluetoothGlobals.writeByte(message);

     		}
     		else{
			        		
     			/** Keep logging data. No handshake detected **/
     						 if(tempData == ' '){
			        			
			        			dataCounter+=1;
			        			System.out.println("String built: " +str);
			        			//Convert into integer/double data here;
									        		try{
									        			intConvert = Integer.parseInt(str.toString());
									          			System.out.println("Integer Data Built:" +intConvert);
									          			dataPacket.decode(intConvert, dataCounter);
									        			i = length+1;
									        		}
									        		catch(NumberFormatException e){
									        			//throw new NumberFormatException();
									        			System.out.println("Invalid Number Exception.");
									        			System.out.println("Str:" +str);
									        			System.out.println("Integer:" +intConvert);
									        		}
			        			}//end if statement
							        		
			        					else{
							        					ch1 = (char)tempData;
											    		System.out.println("tempData:" +ch1);
											    		str = str.append(ch1);
											    		System.out.println("Str = " +str);
							        		}//end else
    		
     				}//end else
    	
    }//end for loop
    	
}//end retrieveData
    
    
    public void updateInfo(ParseUser currentUser){
    	
        /**
         * updateInfo(ParseUser currentUser)
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method retrieves the current user data(xp,currency,level,number of syncs) from the database and updates the user interface.
         * Sets the user object with the appropriate user stats.  
         * 
         * 
         */


      	String levelString;
    	String currencyString;
    	String XP;
    	int exp;
    	
    	
    	objectID = currentUser.getObjectId();
    	System.out.println("objectID" +objectID); 	
    	/**Sets userDat to appropriate objectID in userData Parse Table**/
    	
     	getUserData(objectID);
     	
     	numOfSyncs = userDat.getInt("numOfSync");
     	System.out.println("Retrieved numOfSyncs:" +numOfSyncs);
    	currency1 = userDat.getInt("currency");
    	level = userDat.getInt("level");
    	exp = userDat.getInt("xp");
    	userName = currentUser.getUsername();
    	
    	levelString = Integer.toString(level);
    	currencyString = Integer.toString(currency1);
    	XP = Integer.toString(exp);
    	
    	welcome.setText("Welcome, "+userName);
    	currency.setText(currencyString);
    	lvl.setText(levelString);
    	experience.setText(XP);
    	
    	/**Increment the number of syncs by one **/
        
        
    	
    	
    }
    
    
    
    
    public boolean isDone(){
  	  /**
         * isDone()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method checks the firmware after EVERY data packet to see if there are anymore 
         * packets to send. 
         * 
         * Firwmare will wait for a doneCheck byte = 0x31; 
         * Application will receive a 
         * 			0x32 if DONE '2'
         * 			0x33 if NOT DONE '3'
         */
    	int data = 0;
    	buff=0;
        //Wait for the length byte from the firmware.
        while(buff!=2 || buff!=3){
       
       	 System.out.println("Waiting for a 2 or 3");
       	 bluetoothGlobals.getInputStream();
       	 data = bluetoothGlobals.readByte_timed(15000);
       	 
        }//end while
        
        if(buff==2)
        	return true;
        else
        	return false;
    	
}//end Method
   
    
    public int getLengthByte(){
    	  /**
         * getLengthByte()
         * Author: Garvin Ling
         * Date: 03/22/13
         * 
         * This method retrieves the initial length byte from the firmware
         */
    	int data = 0;
    	int message = 0x42;
        //Wait for the length byte from the firmware.
        while(data!=7){
       
       	 System.out.println("Waiting for a 7");
       	 bluetoothGlobals.getInputStream();
       	 data = bluetoothGlobals.readByte_timed(15000);
       	 if(data==7){
             bluetoothGlobals.writeByte(message);

       	 }
        
        } 	
        return data;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:

            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                address3 = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                // Attempt to connect to the device
                bluetoothService(address3);
                
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                //setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                //Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
      
    public int readChar(){

       	bluetoothGlobals.getInputStream();
 

       	  int OKIES;
       	  int buff= 0;
       	             	  		
   	  		 OKIES= bluetoothGlobals.readByte_timed(15000);
   	  		 buff = OKIES;              	  	 
			
		    	System.out.println("3");
		    
		  return buff;

}//end Read char
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(isTryingToConnect)
    	{
    		menu.getItem(0).setEnabled(false);
    	}
    	else if(!isTryingToConnect)
    	{
    		menu.getItem(0).setEnabled(true);
    	}
		return true;
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_main, menu);
 	   

    	return true;
    }
        
    public void bluetoothService(final String address) {
   		
        
        bluetoothGlobals.conPushed = true;
        bluetoothGlobals.done = false;
        isTryingToConnect = true;
   		
   		 new Thread(new Runnable() {
   			    public void run() {
   			    	
   			    	bluetoothGlobals.cancelDiscovery();
			        
			          try {
			   			Thread.sleep(200);
			   		} catch (InterruptedException e1) {
			   			// TODO Auto-generated catch block
			   			e1.printStackTrace();
			   		}
				          
			   		   bluetoothGlobals.getRemoteDevice(address3);
			   		
			   		   bluetoothGlobals.createRfcommSocketToServiceRecord(MY_UUID);

   		  
			   		
			   	      try {
			   				Thread.sleep(200);
			   			} catch (InterruptedException e1) {
			   				// TODO Auto-generated catch block
			   				e1.printStackTrace();
			   			}
   	       	 
			   	    	 bluetoothGlobals.connect();

			   	   	  
			   	        try {
			   				Thread.sleep(200);
			   			} catch (InterruptedException e1) {
			   				// TODO Auto-generated catch block
			   				e1.printStackTrace();
			   			}
   	        
   	        
   	        
			   	    	 Log.e(TAG, "outputstream");
			   	    	 bluetoothGlobals.getOutputStream();
			   	      			   	     
			   	   	  if(BluetoothGlobals.conn)
			   	   	  {	  
				   	     runOnUiThread(new Runnable() {
				   	         public void run() {
				   	        	 Log.e(TAG, "runonuitrhead");
				   	        	 System.out.println("Just connected");
				   	    
		   	                   		isTryingToConnect=false;
			   	         }
			   	     });
			   	     
			   	     
			   	   	  }
			   	
   	   		  if(!BluetoothGlobals.conn)
   	   		  {
   	               runOnUiThread(new Runnable() {
   	                   public void run() {
   	                	   
                	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                	   builder.setMessage("Bluetooth connection failed, would you like to start the Switcher Demo?").show();
   	               	   bluetoothGlobals.close();  	          
   	            	   BluetoothGlobals.mBluetoothSocket = null;
   	            	   isTryingToConnect = false;
   	                   	
   	                   }
   	               });
   	   		  }
   	   		 
   		}
   	  }).start();
    }

    @Override
    public void onStop() {
    	super.onStop();
    	if (D)
    		Log.e(TAG, "-- ON STOP --");
    }
    
    @Override
    public void onPause() {
  
    	super.onPause();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            return true;
        }
        return false;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	if (D)
        	  Log.e(TAG, "--- ON DESTROY ---");
    	
    	if (BluetoothGlobals.mBluetoothSocket != null) {
           	
        	try {
        		BluetoothGlobals.mBluetoothSocket.close();
        		BluetoothGlobals.mBluetoothSocket = null;
        	}
        	catch (IOException e2) {
        		Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        	}
    	}
    	bluetoothGlobals.resetState();
    	Log.e(TAG, "AFTER close");
    }
    
    public void getUserData(String objectID){
  	  /**
         * getUserData()
         * Author: Garvin Ling
         * Date: 03/24/13
         * 
         * This method retrieves an object from the data table passing the ObjectID
         * Sets the userData object so that we can retrieve and set data through the ParseObject.
         */

    	List<ParseObject> objectList = null;
    	ParseQuery query = new ParseQuery("Userdata");
    	try {
			objectList = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Query Error");
			e.printStackTrace();
		}

    	for(int i = 0; i<objectList.size();i++){
    		
    		ParseObject new2 = objectList.get(i);
    		
    	if(new2.getString("userID").equals(objectID)){
    		userDat = new2;		   //set object
    		i=objectList.size()+1; //exit loop
    	}
	
     }
    
  }
    
    public String getParseUsername(ParseUser user){
    	
        String tmp = new String();
    	
     	if(user!=null){
     		
     		//currentUser.get
     		tmp = user.getUsername();
     	
     		
     	}else{
     		
     		System.out.println("Sorry, no user info.");
     	}
    	
     	return tmp;
    	
    }
    
    public void checkConnection(){
    	
    	 try {
    		 	Thread.sleep(200);
    	 } catch (InterruptedException e) {
    		 e.printStackTrace();
    	 }
    	 
        if (BluetoothGlobals.mBluetoothSocket != null)
        {
        	alert("Bluetooth Connected");
        }
                
        if (BluetoothGlobals.mBluetoothSocket == null)
        {

       	 alert("Bluetooth is null");
       	 
        }
    	
    }
    
   
    public void alert(String text){
    	
		 Toast.makeText(MainActivity.this,text , Toast.LENGTH_SHORT).show();
    	
    }
    

    public void updateGraph(){
    	//Update graph by appending and then upload the new data to the database
        //numOfSyncs has already been incremented by 1 before calling this method
 
       numOfSyncs+=1;
	   exampleSeries1.appendData(new GraphViewData(numOfSyncs, xpGained), true);
	   userDat.add("xpHistory",xpGained);
	   System.out.println("Putting numSyncs:" +numOfSyncs);
	   userDat.put("numOfSync", numOfSyncs);  //num of syncs should have been incremented before this method was called
	   userDat.saveInBackground();
	
			
			
    }
    
    
    public void setHistoryGraph(){
    	
    	/**Display 5 history values for xp gained**/
    	/**Set the number of syncs from the database**/
    	
    	numOfSyncs = userDat.getInt("numOfSync");
    	System.out.println("Number of syncs:" +numOfSyncs);
    	Object[] xp_history;
    	
    	xp_history = userDat.getList("xpHistory").toArray();
    	int length = xp_history.length;
    	int indexStart = 0;
    	int j = 0;

    	System.out.println("XP array length:" +xp_history.length);
    
    	if(numOfSyncs == 0){
    	    		alert("You have no sync history.");
    		initGraph();

    	}//end if
    	else{
			    		
			    		if(length>5){
							//get the starting index if greater than 5
			    			indexStart = length - 5;
										
						}
			    		System.out.println("Index Start:" +indexStart);
			    				for(int i = indexStart; i<length;i++){
			    					
			    					//if more than 5 samples, grab the 5 most recent
						    		xpHistory[j] = (Integer)xp_history[i];
						    		System.out.println("XPHISTORY:" +xpHistory[j]); 
						    		gData[j] = new GraphViewData(j,xpHistory[j]);
						    		j++;
						    	
						    	}

			    				  // init example series data
			    					exampleSeries1 = new GraphViewSeries(gData);
			    				
			    				// graph with dynamically genereated horizontal and vertical labels
			    					graphView = new LineGraphView(
			    							this // context
			    							, "XP Earned per Sync" // heading
			    					);			//((LineGraphView) graphView).setDrawBackground(false);
			    				
			    					graphView.setScalable(true);
			    					graphView.setManualYAxisBounds(30, 0);
			    					graphView.setManualYAxis(true);
			    					graphView.addSeries(exampleSeries1); // data
			    					graphView.setViewPort(1, 4);
			    					graphView.redrawAll();
			    					LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
			    					layout.addView(graphView); 				
			    				
			    				
			    				
			    				
			    				
    	}//end else
    	
    }
    
    public void initGraph(){
    	
    	  // init example series data
		exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
				  new GraphViewData(0, 2)
				, new GraphViewData(1, 100)
				, new GraphViewData(2, 200) // another frequency
				, new GraphViewData(3, 300)
				, new GraphViewData(4, 400)
				, new GraphViewData(5, 500)
		});
		// graph with dynamically genereated horizontal and vertical labels
			graphView = new LineGraphView(
					this // context
					, "XP Earned per Sync" // heading
			);			//((LineGraphView) graphView).setDrawBackground(false);
		
			graphView.setScalable(true);
			graphView.setManualYAxisBounds(30, 0);
			graphView.setManualYAxis(true);
			graphView.addSeries(exampleSeries1); // data
			graphView.setViewPort(1, 4);
			LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
			layout.addView(graphView); 
    	
    	
    }
    
    
    public void getTestUser(){
    	
    	ParseQuery query = new ParseQuery("Userdata");
    	int test = 0 ;
    	try {
			ParseObject obj = query.get("CqoLJpweQN");
			test = (Integer) obj.get("data1");
			System.out.println("Databse Data1:" +test);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			alert("failed");
			e.printStackTrace();
			
		}

    }
    
    
    
    public void putTestUser(){
    		ParseObject obj = new ParseObject("Userdata");
    		obj.put("objectID", "399");
    		obj.put("data1", 33);
    		obj.saveInBackground();
    		alert("Created user with ID:" +"399");
    		
    }
    
    

    
    public void updateGameData(){
    			
    			
        new Thread(new Runnable() {
			    public void run() {
			    	
	                System.out.println("Checking Game Data...");
	               
	                GameData data = new GameData(userDat.getObjectId(), avgMPG,avgLoad,avgThrottle);
	                data.calculateXP();
	                data.calculateCurrency();
	                data.uploadData();
	                data.printRatings();
	                xpGained = data.getxpGained();
	                
	     	    runOnUiThread(new Runnable() {
	   			        
	     	    		public void run() { 	 
			   			          	                
			   	                updateInfo(currentUser);
			   	                setProgressBar(xpGained);
					
					   	}
	     	    }); //end runonUIThread
		     	                
			    }
		
        }).start(); //end thread
    	
    	
    	
    }
    
    public void setProgressBar(int xpGained){
    	
    	int maxProgress;
    	int nextlevel;
    	int xp = userDat.getInt("xp");
    	int prevProgress;
    	prevProgress = exp.getProgress();
    	Level lvl = new Level(xp);
    
    	//update lower and upper
    	lower = lvl.getLower();
    	upper = lvl.getUpper();  
    	
    	//xpPerLevel = userDat.getInt("xpProgress");
    	xpPerLevel = xp;
    	xpPerLevel = xpPerLevel - lower;

    	//upperXp.setText("/" +upper);
    	maxProgress = upper-lower;

    		exp.setMax(maxProgress);
    		exp.setProgress(xpPerLevel);
	    	userDat.put("xpProgress",xpPerLevel);
	    	userDat.saveInBackground();
	    	nextlevel = maxProgress-xpPerLevel;
	    	xpUntil.setText(nextlevel +" xp until next level!");
    }
    
    
    public void initViews(){
    	
    	send = (Button)findViewById(R.id.sendChar);
    	liveData = (Button)findViewById(R.id.liveData);
    	currency = (TextView)findViewById(R.id.vehicle);
    	lvl = (TextView)findViewById(R.id.lvl);
    	welcome = (TextView)findViewById(R.id.welcome);
    	getTest = (Button) findViewById(R.id.gettest);
    	title = (TextView)findViewById(R.id.title);
    	exp = (ProgressBar)findViewById(R.id.progressBar1);
    	xpUntil = (TextView)findViewById(R.id.xpuntil);
    	experience = (TextView)findViewById(R.id.experience);
    	//upperXp = (TextView)findViewById(R.id.upperxp);
    	//syncProg = (ProgressBar)findViewById(R.id.progressBar2);

    	Typeface face=Typeface.createFromAsset(getAssets(), "fonts/BEBAS.ttf"); 
    	Typeface title2 = Typeface.createFromAsset(getAssets(), "fonts/BEBAS.ttf");
 
    	
    	//send.setBackgroundColor(Color.TRANSPARENT);
    	getTest.setTypeface(face);
    	xpUntil.setTypeface(face);
    	experience.setTypeface(face);
    	title.setTypeface(title2);
    	send.setTypeface(face);
     	currency.setTypeface(face);
    	lvl.setTypeface(face);
    	liveData.setTypeface(face);
    	welcome.setTypeface(face);
    }
    
}
