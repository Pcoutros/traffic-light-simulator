import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

enum TrafficLightColor {
	RED, YELLOW, GREEN
}

public class TrafficLightSimulator implements Runnable {
	
	private JTextField lightField;
	private TrafficLightColor lightColor;
	private volatile boolean isProgramRunning, isProgramPaused;
	private boolean changed = false;
    private final Lock lock = new ReentrantLock();
    private long totalPauseTime;

	
	public TrafficLightSimulator(JTextField jtf) {
		lightField = jtf;
		lightColor = TrafficLightColor.GREEN;
		isProgramRunning = false;
		isProgramPaused = false;
		totalPauseTime = 0;
	}
	
	public synchronized void setIsProgramRunning(boolean isRunning) {
		lock.lock();
		try {
			lightColor = TrafficLightColor.GREEN;
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
		while(isProgramRunning) {
			if(!isProgramPaused) {
				try {
					switch(lightColor) {
					case GREEN:
						lightField.setText("Green");
						for (int i = 0; i < 100; i++) {
					        if (isProgramPaused) {
					            waitForChange();
					        } else {
					            Thread.sleep(100); // Sleep for 100 milliseconds each iteration
					        }
						}						
						break;
					case YELLOW:
						lightField.setText("Yellow");
						for (int i = 0; i < 20; i++) {
					        if (isProgramPaused) {
					            waitForChange();
					        } else {
					            Thread.sleep(100); // Sleep for 100 milliseconds each iteration
					        }
						}						
						break;
					case RED:
						lightField.setText("Red");
						for (int i = 0; i < 120; i++) {
					        if (isProgramPaused) {
					            waitForChange();
					        } else {
					            Thread.sleep(100); // Sleep for 100 milliseconds each iteration
					        }
						}
						break;
					}
				} catch (InterruptedException ie) {}
				changeColor();
			} else {
				waitForChange();
			}
			
		}
		
	}
	
	private synchronized void changeColor() {
		lock.lock();
        try {
            switch (lightColor) {
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
            changed = true;
            notify();
        } finally {
            lock.unlock();
        }
	}
	
	public synchronized void waitForChange() {
		lock.lock();
		try {
			while(!changed) {
				wait();
				changed = false;
			}
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(lightField, e);
		} finally {
			lock.unlock();
		}
	}
	
	public synchronized TrafficLightColor getColor() {
		lock.lock();
		try {
			return lightColor;
		} finally {
			lock.unlock();
		}
	}
}

