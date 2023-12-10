import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextField;

public class Car implements Runnable {
	
	private final static long RED_LIGHT_LENGTH = 12000;
	
	private JTextField carField;
	private double xPosition, yPosition, lastKnownPosition;
	private double speed;
	private volatile boolean isProgramRunning, isProgramPaused;
	private long startTime, totalPauseTime;
	private ArrayList<TrafficLightSimulator> trafficLights;
	private int trafficLightIndex;
    private final Lock lock = new ReentrantLock();
	
	public Car(JTextField jtf, double mps, ArrayList<TrafficLightSimulator> lights) {
		carField = jtf;
		xPosition = 0;
		yPosition = 0;
		lastKnownPosition = 0;
		speed = mps;
		isProgramRunning = false;
		isProgramPaused = false;
		startTime = System.currentTimeMillis();
		totalPauseTime = 0;
		trafficLights = lights;
		trafficLightIndex = 0;
	}
	
	public synchronized void setIsProgramRunning(boolean isRunning) {
		lock.lock();
        try {
            xPosition = 0;
            totalPauseTime = 0;
            lastKnownPosition = 0;
            trafficLightIndex = 0;
            startTime = System.currentTimeMillis();
            isProgramRunning = isRunning;
        } finally {
            lock.unlock();
        }
	}
	
	public synchronized void setIsProgramPaused(boolean isPaused) {
		lock.lock();
        try {
            isProgramPaused = isPaused;
        } finally {
            lock.unlock();
        }
	}
	
	@Override
	public void run() {
		double futureDistance = 0;
		long redLightWaitingTime = 0;
		long movingTowardRedLightTime = 0;
		while(isProgramRunning) {
			if (!isProgramPaused) {
				long elapsedTime = System.currentTimeMillis() - startTime - redLightWaitingTime - totalPauseTime;
				double distanceToNextLight = 1000 * (trafficLightIndex + 1);
				
				if (trafficLightIndex >= trafficLights.size()) {
					xPosition = lastKnownPosition + (speed * elapsedTime)/ 1000.0;
					carField.setText("X, Y Position: " + xPosition + ", " + yPosition);
				} else {
					TrafficLightColor currentLightColor = trafficLights.get(trafficLightIndex).getColor();
					
					if(currentLightColor != TrafficLightColor.RED) {
						xPosition = lastKnownPosition + (speed * elapsedTime)/ 1000.0;
						carField.setText("X, Y Position: " + xPosition + ", " + yPosition + (trafficLightIndex + 1));
					} else {
						futureDistance = lastKnownPosition + (speed * elapsedTime)/ 1000.0;
						if (futureDistance < distanceToNextLight) {
							movingTowardRedLightTime += 1000;
							xPosition = lastKnownPosition + (speed * elapsedTime)/ 1000.0;
							carField.setText("X, Y Position: " + xPosition + ", " + yPosition);
							if (movingTowardRedLightTime >= 12000) {
								movingTowardRedLightTime = 0;
							}
						} else {
							redLightWaitingTime += RED_LIGHT_LENGTH - movingTowardRedLightTime;
							try {
								Thread.sleep(RED_LIGHT_LENGTH - movingTowardRedLightTime);  
							} catch(InterruptedException ie) {}
							movingTowardRedLightTime = 0;
						}
					}
					if (xPosition >= distanceToNextLight) {
						trafficLightIndex++;
					}
				}
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ie) {}
			} else {
				totalPauseTime += 1000;
				
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ie) {}
			}

		}
	}

}

