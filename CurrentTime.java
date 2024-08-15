/**
 * File Name: CurrentTime.java
 * Course: CMSC335 7381
 * Date: 12/10/2023
 * Author: Pete Coutros
 * 
 * Purpose: The purpose of this class is to model the current time of the system. It implements the Runnable interface and overrides the run()
 * method to be implemented as a Thread. While the simulation has started and not been paused, the current date and time will be updated to a
 * JTextField in the GUI every one second. 
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;

public class CurrentTime implements Runnable {
	
	private volatile boolean isProgramRunning, isProgramPaused;		//Used to control the status of the Thread
	private JTextField timeField;									//Used to update the date and time to the GUI
	
	/**
	 * Constructor to create a CurrentTime object. It takes in JTextField to update the time to a GUI. It initializes the boolean flags of isProgramRunning
	 * and isProgramPaused to false. 
	 * 
	 * @param JTextField jtf
	 */
	public CurrentTime(JTextField jtf) {
		timeField = jtf;
		isProgramRunning = false;
		isProgramPaused = false;
	}
	
	/**
	 * Sets the program's running status.
	 * 
	 * @param boolean isRunning
	 */
	public synchronized void setIsProgramRunning(boolean isRunning) {
		
		isProgramRunning = isRunning;		//Set the running flag to the value of the argument
		notifyAll();

	}
	
	/**
	 * Sets the program's paused status.
	 * 
	 * @param boolean isPaused
	 */
	public synchronized void setIsProgramPaused(boolean isPaused) {
		
		isProgramPaused = isPaused;			//Set the paused flag to the value of the argument
		notifyAll();

	}


	/**
	 * Overrides the run() method of the Runnable interface to define the behavior of the CurrentTime during the simulation. If the simulation is running
	 * and not paused, the current date and time will be displayed on the GUI in one second intervals in the format of MM/dd/yyy HH:mm:ss.
	 */
	@Override
	public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");		//Set the format for the date/time output	
        while(isProgramRunning) {
        	if(!isProgramPaused) {
        		timeField.setText(dateFormat.format(new Date()));						//Display the date/time in the JTextField of the GUI
                
                try {
                	Thread.sleep(1000);													//Sleep for 1 second so that updates on time are given every 1 second
                } catch (InterruptedException ie) {}
        	}
        }
	}

}
