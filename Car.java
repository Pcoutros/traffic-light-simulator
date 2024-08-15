/**
 * File Name: Car.java
 * Course: CMSC335 7381
 * Date: 12/10/2023
 * Author: Pete Coutros
 * 
 * Purpose: The purpose of this class is to model a car object traveling through intersections that are spaced 1000 meters apart. It 
 * implements the Runnable interface and overrides the run() method to be implemented as a Thread. The Car travels in a straight line 
 * at a constant speed in meters per second as long as it is not at a red light. The Car's xPosition is calculated by the speed of the
 * car and the elapsed time it has been traveling. If the car is stopped at a Red light, the xPosition should not increase until that 
 * light turns Green. The yPosition will remain 0 throughout the Car's journey as it only moves in the xPosition. The Car will continue 
 * at its speed through Green and Yellow lights. It will stop on a dime at the Red lights. If the Car's xPosition is not at an interval 
 * of 1000 (meaning it is not near an intersection) when the light ahead is Red, the car will continue to travel towards that Red light. 
 * When it reaches said intersection, if the light is still Red the Car will stop and wait for a Green light, otherwise it will continue 
 * through it. When the Car's stop at the Red lights, they will leave distance between them and the light that corresponds to their speed. 
 * This program updates every second, so if the next update in xPosition would put the car past the Red light it will stop short of the 
 * light where the meters directly correspond to the speed of the car. For example, if the car is traveling at 50 meters per second, and 
 * the light in front turns Red when the Car's xPostion is 900, the Car will travel until 950 and then stop and wait for the light to turn Green.
 * 
 * @author Pete Coutros
 */

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

public class Car implements Runnable {
	
	private final static long RED_LIGHT_LENGTH = 12000; 			//The length of the Red Light
	private JTextArea carField;						//Used to update the X, Y position of the Car to the GUI
	private int xPosition, yPosition, lastKnownPosition;			//Used to store the X, Y, and last known positions of the car
	private int speed;							//Used to store the speed of the car in meters per second
	private volatile boolean isProgramRunning, isProgramPaused;		//Used to control the status of the Thread
	private long startTime, totalPauseTime;					//Used to store when the program starts and the total time paused
	private ArrayList<TrafficLightSimulator> trafficLights;			//Used to determine the amount of lights needed to traverse
	private int trafficLightIndex;						//Used to get the specific light from the ArrayList of trafficLights
        private final Lock lock = new ReentrantLock();				//Used to lock methods so that only one thread can access at a time
	
    	/**
    	* Constructor to create a Car object from the parameters. It takes in a JTextField to update the X, Y positions of the Car to a GUI, 
     	* speed in meters per second to calculate the Car's distance, and an ArrayList of trafficLights to determine the amount of intersection 
     	* the car traverses. In addition, it initially sets the X, Y, and last known positions to 0 (the car is initially at a dead stop 1000 
     	* meters before the first intersection). The booleans used to determine the status of the Thread are initialized to false indicating
     	* that the program is not running and the program is not paused. The start time is initialized to the current time and the total paused
     	* time is initialized to 0 as there has not been a pause yet. The trafficLightIndex is initialized to 0 so that the intersections start 
     	* with the first one.
     	*	 
     	* @param JTextField jtf, double mps, ArrayList<TrafficLightSimulatior> lights
     	*/
	public Car(JTextArea jtf, int mps, ArrayList<TrafficLightSimulator> lights) {
		
		carField = jtf;								//initialize the JTextField so the X, Y positions can be updated in the GUI								
		xPosition = 0;								//Initialize the xPosition to 0 as the car is not moving yet
		yPosition = 0;								//Initialize the yPosition to 0 as the car is not moving yet
		lastKnownPosition = 0;							//Initialize the lastKnownPosition to 0 as the car has not moved prior
		speed = mps;								//Initialize the speed from the value in the arguments (METERS PER SECOND!!!)
		isProgramRunning = false;						//Initialize the isProgramRunning to false as the start button has not been pressed yet
		isProgramPaused = false;						//Initialize the isProgramPaused to false as the program has not been paused yet
		startTime = 0;								//Initialize the startTime to 0 as the start button has not been pressed yet
		totalPauseTime = 0;							//Initialize the pauseTime to 0 as the program has not been paused yet
		trafficLights = lights;							//Initialize the ArrayList of traffic lights the car will need to traverse
		trafficLightIndex = 0;							//Initialize the index of the ArrayList to 0 so it starts at the beginning
	}
	
	/**
	 * Sets the program's running status and resets relevant variables if the program is starting or restarting. The variables are reset because 
	 * the simulation could have been started (using the start button) and then ended (using the stop button) and then restarted again (using the 
	 * start button) without closing the GUI. Therefore it should behave like a new simulation by clearing the previous simulation's values.
	 * 
	 * @param boolean isRunning 
	 */
	public synchronized void setIsProgramRunning(boolean isRunning) {
	lock.lock();										//Acquires the lock, if unavailable the Thread goes dormant until it is
        try {
        	if (isRunning) {								//If the argument is True:
        		xPosition = 0;								//Reset xPosition (yPosition is always 0 so no need to reset it) 
                	totalPauseTime = 0;							//Reset totalPause time to 0 as the simulation starts or restarts
                	lastKnownPosition = 0;							//Reset the lastKnownPosition to 0 as the simulation starts or restarts
                	trafficLightIndex = 0;							//Reset the trafficLightIndex to 0 as the simulation starts or restarts
                	startTime = System.currentTimeMillis();					//The program has started/restarted so record the start time as the current time
                	isProgramRunning = isRunning;						//Set the boolean flag value from the arguments
        	} else {									//If the argument is False:
        		startTime = 0;								//Reset the startTime to 0 as the simulation has ended
        		isProgramRunning = isRunning;						//Set the boolean flag value from the arguments
        	}
    		notifyAll();
        } finally {
            	lock.unlock();									//Release the lock
        }
	}
	
	/**
	 * Set the progam's paused status.
	 * 
	 * @param boolean isPaused
	 */
	public synchronized void setIsProgramPaused(boolean isPaused) {
		lock.lock();						//Acquires the lock, if unavailable the Thread goes dormant until it is
        try {
        	isProgramPaused = isPaused;				//Set the boolean flag value from the arguments
    		notifyAll();

        } finally {
            	lock.unlock();						//Released the lock
        }
	}
	
	/**
	 * Overrides the run() method of the Runnable interface to define the behavior of the Car during the simulation. Manages the car's movement, 
	 * interaction with traffic lights, and updates the corresponding JTextField in the GUI. Handles the logic for red lights, green lights, and 
	 * yellow lights. Control flow is determined by the values of the boolean flags isProgramRunning and isProgramPaused. The method runs in a 
	 * loop until isProgramRunning is set to false. if the simulation is paused, the method will keep track of the time paused and subtract that
	 * from the elapsed time so that there is no jump in xPositons when the simulation resumes. When the simulation is paused and then subsequently 
	 * restarted, the simulation should behave like there was no pause, picking up where it last left off.
	 */
	@Override
	public void run() {
		int futureDistance = 0;				//Used to determine if the car can continue traveling toward a light when that light is red
		long redLightWaitingTime = 0;			//Used to keep track of the time spent waiting at a red light
		long movingTowardRedLightTime = 0;		//Used to keep track of the time spent traveling towards a light when the light is red
		
		while(isProgramRunning) {
			if (!isProgramPaused) {
				
				//Keep track of the time elapsed since the simulation began. Adjusts for time spent waiting at red lights and pause time
				long elapsedTime = System.currentTimeMillis() - startTime - redLightWaitingTime - totalPauseTime;
				//Keep track of the distance to the next intersection, they're all spaced 1000 meters apart
				int distanceToNextLight = 1000 * (trafficLightIndex + 1);
				
				//If the Car has passed all of the intersections, continue traveling at its steady speed
				if (trafficLightIndex >= trafficLights.size()) {
					xPosition = (int) (lastKnownPosition + (speed * elapsedTime)/ 1000);
					carField.setText("X, Y Position: " + xPosition + ", " + yPosition + "\nSpeed: " + speed + "mps\nAll intersections have been passed!");
				
				//Otherwise the car is still within the range of intersections and needs to adhere to the lights
				} else {
					
					//Get the current color of the first traffic light in front of the car
					TrafficLightColor currentLightColor = trafficLights.get(trafficLightIndex).getColor();
					
					//If the light is Green or Yellow continue traveling at its steady speed
					if(currentLightColor != TrafficLightColor.RED) {
						xPosition = (int) (lastKnownPosition + (speed * elapsedTime)/ 1000);
						carField.setText("X, Y Position: " + xPosition + ", " + yPosition + "\nSpeed: " + speed + "mps\nUpcoming Intersection: " + (trafficLightIndex + 1));
					
					//Otherwise the light is Red and the car needs to either continue traveling towards the light or stop
					} else {
						
						//Calculate the future distance of the car if it were to continue traveling
						futureDistance = (int) (lastKnownPosition + (speed * elapsedTime)/ 1000);
						
						//If that distance is less than the distance of the traffic light, continue traveling
						if (futureDistance < distanceToNextLight) {
							
							//Increment the time moving towards the red light by 1 second (updates happen every 1 second)
							movingTowardRedLightTime += 1000;
							
							//Continue traveling toward the Red light at its steady speed
							xPosition = (int) (lastKnownPosition + (speed * elapsedTime)/ 1000);
							carField.setText("X, Y Position: " + xPosition + ", " + yPosition + "\nSpeed: " + speed +  "mps\nTraveling toward Red Light: " + (trafficLightIndex + 1));
							
							//If the time spent moving toward the red light exceeds the length of the red light light, the car never reached the light
							//so reset the time
							if (movingTowardRedLightTime >= RED_LIGHT_LENGTH) {
								movingTowardRedLightTime = 0;
							}
						
						//Otherwise the Car is at/near the intersection when the light is Red and any further travel would put them past the red light
						} else {
							
							//Calculate the time spent waiting at the Red light taking into account the time spent moving toward it
							redLightWaitingTime += RED_LIGHT_LENGTH - movingTowardRedLightTime;
							
							carField.setText("X, Y Position: " + xPosition + ", " + yPosition + "\nSpeed: 0mps\nStopped at Red Light: " + (trafficLightIndex + 1));
							
							//Stop at the light for the amount of time remaining before it turns Green
							try {
								Thread.sleep(RED_LIGHT_LENGTH - movingTowardRedLightTime);  
							} catch(InterruptedException ie) {}
							
							//Reset the time moving toward the Red light as it is no longer Red
							movingTowardRedLightTime = 0;
						}
					}
					
					//If the position of the car is past the current traffic light, increase the index to monitor the upcoming light
					if (xPosition >= distanceToNextLight) {
						trafficLightIndex++;					//Can be larger than the size of the ArrayList, meaning the car has passed all lights
					}
				}
				
				//Sleep Thread for 1 second so that updates occur every one second
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ie) {}
			
			//Otherwise the program is paused 
			} else {
				
				//Increment the time paused by 0.1 second
				totalPauseTime += 100;
				
				//Sleep Thread for 0.1 second while paused so that updates occur every point one second
				try {
					Thread.sleep(100);
				} catch(InterruptedException ie) {}
			}

		}
	}

}

