/**
 * File Name: TrafficLightSimulator.java
 * Date: 12/10/2023
 * Author: Pete Coutros
 * 
 * Purpose: The purpose of this class is to model a traffic light of an intersection with RED, YELLOW, and GREEN lights. It implements the Runnable interface 
 * and overrides the run() method to be implemented as a Thread. While the simulation is running and not paused, the lights will start on Green for 10 seconds, 
 * then transition to Yellow for 2 seconds, then transition to Red for 12 seconds.
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextField;

enum TrafficLightColor {
	RED, YELLOW, GREEN					//Enumerated type representing the possible colors of the traffic light
}

public class TrafficLightSimulator implements Runnable {
	
	private JTextField lightField;						//Used to update the color of the light to the GUI
	private TrafficLightColor lightColor;					//Used to store the color of the light
	private volatile boolean isProgramRunning, isProgramPaused;		//Used to control the status of the Thread
	private boolean changed = false;					//Used to determine if the color of the light has changed
   	private final Lock lock = new ReentrantLock();				//Used to lock methods so that only one thread can access at a time
    	private long remainingLightTime, lightTime;				//Used to store the light times and the time remaining on the light
	
    	/**
     	* Constructor to create a TrafficLightSimulator object. It takes in JTextField to update the light color to the GUI. It initializes the light to GREEN and
     	* the boolean flags isProgramRunning and isProgramPaused to false. It sets the remainingLightTime and lightTime to 0.
     	* 
     	* @param JTextFiled jtf
     	*/
	public TrafficLightSimulator(JTextField jtf) {
		lightField = jtf;	
		lightColor = TrafficLightColor.GREEN;
		isProgramRunning = false;
		isProgramPaused = false;
		remainingLightTime = 0;					
		lightTime = 0;
	}
	
	/**
	 * Sets the program's running status. It sets the light color to GREEN.
	 * 
	 * @param boolean isRunning
	 */
	public synchronized void setIsProgramRunning(boolean isRunning) {
		lock.lock();							//Acquires the lock, if unavailable the Thread goes dormant until it is
		try {	
			lightColor = TrafficLightColor.GREEN;			//Set the light color to GREEN
			remainingLightTime = 0;					//Set remainingLightTime to 0
			isProgramRunning = isRunning;				//Set the boolean flag value from the argument
    			notifyAll();
		} finally {
			lock.unlock();						//Release the lock
		}
	}
	
	/**
	 * Sets the program's paused status.
	 * 
	 * @param boolean isPaused
	 */
	public synchronized void setIsProgramPaused(boolean isPaused) {
        	lock.lock();								//Acquires the lock, if unavailable the Thread goes dormant until it is
        	try {
            		isProgramPaused = isPaused;					//Set the boolean flag value from the argument
            		notifyAll();
        	} finally {
            		lock.unlock();							//Release the lock
        	}
	}

	/**
	 * Returns the time the light will stay a color based off its current light color. Green for 10 seconds, Yellow for 2, and Red for 12.
	 * 
	 * @param TrafficLightColor lightColor
	 * @return	long lightTime
	 */
	private long getLightTime(TrafficLightColor lightColor) {
		switch(lightColor) {
		case GREEN:
			lightTime = 10000;		//Green for 10 seconds
			break;
		case YELLOW:
			lightTime = 2000;		//Yellow for 2 seconds
			break;
		case RED:
			lightTime = 12000;		//Red for 12 seconds
			break;
		}
		return lightTime;
	}
	
	/**
	 * Overrides the run() method of the Runnable interface to define the behavior of the TrafficLightSimulator during the simulation. When the program is
	 * running and is not paused, the Green light will stay lit for 10 seconds, the Yellow light will stay light for 2 seconds, and the Red light will stay
	 * lit for 12 seconds. There are checks every 100 milliseconds to determine if the pause status of the simulation has been activated.
	 */
	@Override
	public void run() {
		remainingLightTime = getLightTime(lightColor);			//Set remainingLightTime to the full time of the color
	    	while (isProgramRunning) {
	        	if (!isProgramPaused) {
	            		try {
	                		switch (lightColor) {
	                    			case GREEN:
	                        			lightField.setText("Green");		//If light is Green set JTF in GUI
	                       				break;
	                    			case YELLOW:
	                        			lightField.setText("Yellow");		//If light is Yellow set JTF in GUI
	                       				break;
	                    			case RED:
	                        			lightField.setText("Red");		//If light is Red set JTF in GUI
	                        			break;
	                		}
	                
	                		//Check if simulation paused while time remained on the light
	                		while (remainingLightTime > 0) {
	                    			if (isProgramPaused) {
	                        			break;					//Exit loop for remainingLightTime >0
	                    			} else {
	                    				remainingLightTime -= 100;		//Decrement by 0.1 seconds
	                        			Thread.sleep(100);			//Sleep for 0.1 seconds
	                    			}
	                		}
	                		if (remainingLightTime == 0) {
		                		changeColor(); 					//If no remaining time, change the color of the light
		                		remainingLightTime = getLightTime(lightColor);	//Reset remainingLightTime to the full time of the new color it changed to
	                		}
	            		} catch (InterruptedException ie) {}
	        	} 
	    }
	    remainingLightTime = 0;								//Reset remaining time on lights to 0 when the program is not running
	}

	
	/**
	 * Changes the color of the light based on the current light color. If it is RED it will change to GREEN, if it is YELLOW it will change to RED, if it is GREEN it
	 * will change to YELLOW.
	 */
	private synchronized void changeColor() {
		lock.lock();					//Acquires the lock, if unavailable the Thread goes dormant until it is
	        try {
	            switch (lightColor) {			//Change color based on current color
	                case RED:
	                    lightColor = TrafficLightColor.GREEN;
	                    break;
	                case YELLOW:
	                    lightColor = TrafficLightColor.RED;
	                    break;
	                case GREEN:
	                    lightColor = TrafficLightColor.YELLOW;
	                    break;
	        }
	        changed = true;					//Set boolean flag to true
		remainingLightTime = 0;
		notifyAll();					//Notify waiting threads
        	} finally {
            		lock.unlock();				//Releases the lock
        	}
	}
	
	/**
	 * Suspends Thread until the boolean flag "changed" is set to true.
	 */
	public synchronized void waitForChange() {
		lock.lock();					//Acquires the lock, if unavailable the Thread goes dormant until it is
		try {
			while(!changed) {			//If no change then suspend the Thread
				wait();
			}
			changed = false;
		} catch (InterruptedException e) {}
		finally {
			lock.unlock();				//Releases the lock
		}
	}
	
	/**
	 * Returns the current color of the traffic light.
	 * 
	 * @return TrafficLightColor lightColor
	 */
	public synchronized TrafficLightColor getColor() {
		lock.lock();					//Acquires the lock, if unavailable the Thread goes dormant until it is
		try {
			return lightColor;			//Return the color of the light
		} finally {
			lock.unlock();				//Releases the lock
		}
	}
}

