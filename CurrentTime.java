import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;

public class CurrentTime implements Runnable {
	
	private volatile boolean isProgramRunning, isProgramPaused;
	private JTextField timeField;
	
	public CurrentTime(JTextField jtf) {
		timeField = jtf;
		isProgramRunning = false;
		isProgramPaused = false;
	}
	
	public synchronized void setIsProgramRunning(boolean isRunning) {
		isProgramRunning = isRunning;
	}
	
	public synchronized void setIsProgramPaused(boolean isPaused) {
		isProgramPaused = isPaused;
	}


	@Override
	public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        while(isProgramRunning) {
        	if(!isProgramPaused) {
        		timeField.setText(dateFormat.format(new Date()));
                
                try {
                	Thread.sleep(1000);
                } catch (InterruptedException ie) {}
        	}
        }
	}

}
