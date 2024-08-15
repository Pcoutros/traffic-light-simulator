/**
 * File Name: GUI.java
 * Date: 12/10/2023
 * Author: Pete Coutros
 * 
 * Purpose: The purpose of this class is to model a GUI object running a traffic light simulation. The GUI utilizes a JFrame to house 4 main panels: 1) displays
 * the current date and time, 2) displays the current color of all the traffic lights, 3) displays the current X, Y positions of all the cars, and 4) displays the
 * different buttons used to control the simulation. The buttons include: 1) Start to start the simulation, 2) Pause to pause the simulation, 3) Continue to continue
 * the simulation, 4) Stop to stop the simulation, 5) Add Intersection to add an intersection to the simulation, and 6) Add Car to add a car to the simulation. The
 * buttons are either enabled or disabled depending on what prior button was pressed. This is done to control the flow of the simultion. For example, you cannot add
 * a Car or and Intersection while the simulation has started, you would need to do it prior to the start.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

public class GUI {
	
	private JFrame frame;								//Used to house all panels
	private JPanel left, center,right, bottom;					//Used to house info on CurrentTime, Traffic light colors, X, Y position of Cars, and Button controls
	private JButton start, pause, resume, stop, 
			addIntersection, addCar;					//Buttons used to control the simulation
	private JTextField timeField, firstLight, secondLight, thirdLight;		//JTextFields to display the current status of the simulation (Time and Lights)
	private JTextArea firstCar, secondCar, thirdCar;				//JTextFields to display the current status of the simulation (Car and the Current intersection)
	private JLabel firstLightLabel, secondLightLabel, thirdLightLabel, 
			firstCarLabel, secondCarLabel, thirdCarLabel;			//JLabels to organize the data
	private int carCount = 3;							//Each simulation starts with 3 cars
	private int intersectionCount = 3;						//Each simulation starts with 3 intersections
	
	/**
	 * Constructor to create the GUI. It places the needed components in each panel and places the panels in the JFrame. It also adds functionality to the buttons
	 * used to control the simulation.
	 */
	public GUI() {
		
		//Instantiate the Frame that will hold all components, name it, and set its close operation
		frame = new JFrame("Project 3 - Concurrency");			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		//Instantiate the Current Time panel and give it a border with a title
		left = new JPanel();
		Border leftBorder = BorderFactory.createTitledBorder("Time");
		left.setBorder(leftBorder);
		
		//Instantiate the JTF that will display the current time, make it non-editable, and add it to the Current Time panel
		timeField = new JTextField(15);
		timeField.setEditable(false);
		left.add(timeField, BorderLayout.CENTER);
		
		//Instantiate the Traffic Light panel and give it a border with a title
		center = new JPanel();
		Border centerBorder = BorderFactory.createTitledBorder("Traffic Lights");
		center.setBorder(centerBorder);
		
		//Instantiate the three JTF that will display the colors of the 3 lights and make them non editable
		firstLight = new JTextField();
		firstLight.setEditable(false);
		secondLight = new JTextField();
		secondLight.setEditable(false);
		thirdLight = new JTextField();
		thirdLight.setEditable(false);
		
		//Instantiate the three Labels for the intersections
		firstLightLabel = new JLabel("Intersection 1");
		secondLightLabel = new JLabel("Intersection 2");
		thirdLightLabel = new JLabel("Intersection 3");

		//Give the Traffic Light panel a Grid Layout and add the 3 light's Labels and JTFs
		center.setLayout(new GridLayout(0,2));
		center.add(firstLightLabel);
		center.add(firstLight);
		center.add(secondLightLabel);
		center.add(secondLight);
		center.add(thirdLightLabel);
		center.add(thirdLight);
		
		//Instantiate the Car panel and give it a border with a title
		right = new JPanel();
		Border rightBorder = BorderFactory.createTitledBorder("X, Y Positions");
		right.setBorder(rightBorder);
		
		//Instantiate the three JTA that will display the X, Y Positions of the cars as well as current intersection and make them non-editable
		firstCar = new JTextArea();
		firstCar.setEditable(false);
		secondCar = new JTextArea();
		secondCar.setEditable(false);
		thirdCar = new JTextArea();
		thirdCar.setEditable(false);
		
		//Instantiate the three Labels for the cars
		firstCarLabel = new JLabel("Car 1");
		secondCarLabel = new JLabel("Car 2");
		thirdCarLabel = new JLabel("Car 3");
		
		//Give the Car panel a Grid Layout and add the 3 car's Labels and JTFs
		right.setLayout(new GridLayout(0,2));
		right.add(firstCarLabel);
		right.add(firstCar);
		right.add(secondCarLabel);
		right.add(secondCar);
		right.add(thirdCarLabel);
		right.add(thirdCar);
		
		//Instantiate the Button panel, give it a border with a title, and set a FlowLayout
		bottom = new JPanel();
		Border bottomBorder = BorderFactory.createTitledBorder("Controls");
		bottom.setBorder(bottomBorder);
		bottom.setLayout(new FlowLayout());
		
		//Set the dimension sizes of each of the panels
		left.setPreferredSize(new Dimension(200, 300));
		center.setPreferredSize(new Dimension(200, 300));
		firstLight.setPreferredSize(new Dimension(200, 100));		//Only one light needs to be set and the others will take the same size
		right.setPreferredSize(new Dimension(600, 300));
		firstCar.setPreferredSize(new Dimension(200, 100));		//Only one car needs to be set and the others will take the same size
		bottom.setPreferredSize(new Dimension(800, 100));
	
		//Instantiate the buttons, set them as enabled/disabled, and add them to the Button panel
		start = new JButton("Start");
		start.setEnabled(true);						//Upon creating the GUI, the Start should be enabled
		pause = new JButton("Pause");
		pause.setEnabled(false);					//Pause is disabled as the simulation would first need to be Started
		resume = new JButton("Continue");
		resume.setEnabled(false);					//Continue is disabled as the simulation would first need to be Started
		stop = new JButton("Stop");
		stop.setEnabled(false);						//Stop is disabled as the simulation would first need to be Started
		addIntersection = new JButton("Add Intersection");
		addIntersection.setEnabled(true);				//Add Intersection is enabled as you would need to add prior to Starting
		addCar = new JButton("Add Car");
		addCar.setEnabled(true);					//Add Car is enabled as you would need to add prior to Starting
		bottom.add(start);
		bottom.add(pause);
		bottom.add(resume);
		bottom.add(stop);
		bottom.add(addIntersection);
		bottom.add(addCar);
		
		//Add the panels to the Frame
		frame.add(left, BorderLayout.WEST);
		frame.add(center, BorderLayout.CENTER);
		frame.add(right, BorderLayout.EAST);
		frame.add(bottom, BorderLayout.SOUTH);
		
		addButtonFunctionality();					//Add functionality to the buttons
	}
	
	/**
	 * Adds functionality to each of the 6 button options available in the simulation. The Start button is responsible for beginning the simulation, the Pause button
	 * is responsible for pausing the simulation, the Continue button is responsible for continuing the simulation, the Stop button is responsible for stopping the
	 * simulation, the Add Intersection button is responsible for adding a new intersection and updating the ArrayList of intersections, and finally the Add Car button
	 * is responsible for add a new car and updating the ArrayList of Cars in the simulation. Each button also controls what other buttons are enabled when pressed.
	 */
	private void addButtonFunctionality() {
		
		CurrentTime currentTime = new CurrentTime(timeField);				//Create a new currentTime object and pass in the respective JTextField for it to update to
		
		ArrayList<TrafficLightSimulator> trafficLights = new ArrayList<>();		//Start a list of traffic lights
		ArrayList<Car> cars = new ArrayList<>();					//Start a list of cars
		
		//Create three new TrafficLightSimulator objects and pass in their respective JTextFields for them to update to
		TrafficLightSimulator lightOne = new TrafficLightSimulator(firstLight);		
		TrafficLightSimulator lightTwo = new TrafficLightSimulator(secondLight);
		TrafficLightSimulator lightThree = new TrafficLightSimulator(thirdLight);
		
		//Add the lights to the list
		trafficLights.add(lightOne);
		trafficLights.add(lightTwo);
		trafficLights.add(lightThree);
			
		//Create three new Car objects and pass in their respective JTextFields for them to update to
		Car carOne = new Car(firstCar, 20, trafficLights);
		Car carTwo = new Car(secondCar, 70, trafficLights);
		Car carThree = new Car(thirdCar, 90, trafficLights);
		
		//Add the Cars to the list
		cars.add(carOne);
		cars.add(carTwo);
		cars.add(carThree);
		
		//Give functionality to the Start button
		start.addActionListener((ActionEvent e) -> {
			
			//Loop through the TrafficLightSimulator objects to create new Threads for them, set the flag to true, and start the Threads
			for (TrafficLightSimulator trafficLight : trafficLights) {
				Thread trafficThread = new Thread(trafficLight);
				trafficLight.setIsProgramRunning(true);
				trafficThread.start();
			}
			
			//Loop through the Car objects to create new Threads for them, set the flag to true, and start the Threads
			for (Car car : cars) {
				Thread carThread = new Thread(car);
				car.setIsProgramRunning(true);
				carThread.start();
			}
			
			//Create a new Thread for CurrentTime, set the flag to true, and start the Thread
			Thread time = new Thread(currentTime);
			currentTime.setIsProgramRunning(true);
			time.start();
	
			start.setEnabled(false);			//Disables the Start button
			stop.setEnabled(true);				//Enables the Stop button
			pause.setEnabled(true);				//Enables the Pause button
			resume.setEnabled(false);			//Disables the Continue button
			addIntersection.setEnabled(false);		//Disables the Add Intersection button
			addCar.setEnabled(false);			//Disables the Add Car button
			
		});
		
		//Give functionality to the Pause button
		pause.addActionListener((ActionEvent e) -> {
			
			currentTime.setIsProgramPaused(true);	//Set the pause flag of the CurrentTime object to true
			
			//Loop through the TrafficLightSimulator objects and set the pause flag to true
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramPaused(true);
			}
			
			//Loop through the Car objects and set the pause flag to true
			for (Car car : cars) {
				car.setIsProgramPaused(true);
			}
			
			pause.setEnabled(false);			//Disables the Pause button
			start.setEnabled(false);			//Disables the Start button
			resume.setEnabled(true);			//Enables the Continue button
			stop.setEnabled(true);				//Enables the Stop button
			addIntersection.setEnabled(false);		//Disables the Add Intersection button
			addCar.setEnabled(false);			//Disables the Add Car button
		});
		
		//Give functionality to the Continue button
		resume.addActionListener((ActionEvent e) -> {
			
			currentTime.setIsProgramPaused(false);	//Set the pause flag of the CurrentTime object to false
			
			//Loop through the TrafficLightSimulator objects and set the pause flag to false
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramPaused(false);
			}
			
			//Loop through the Car objects and set the pause flag to false
			for (Car car : cars) {
				car.setIsProgramPaused(false);
			}
			
			resume.setEnabled(false);			//Disables the Continue button
			start.setEnabled(false);			//Disables the Start button
			stop.setEnabled(true);				//Enables the Stop button
			pause.setEnabled(true);				//Enables the Pause button
			addIntersection.setEnabled(false);		//Disables the Add Intersection button
			addCar.setEnabled(false);			//Disables the Add Car button
		});
		
		//Give functionality to the Stop button
		stop.addActionListener((ActionEvent e) -> {
			
			currentTime.setIsProgramRunning(false); //Set the running flag of the CurrentTime object to false
			
			//Loop through the TrafficLightSimulator objects and set the running flag to false
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramRunning(false);
			}
			
			//Loop through the Car objects and set the running flag to false
			for (Car car : cars) {
				car.setIsProgramRunning(false);
			}

			stop.setEnabled(false);				//Disables the Stop button
			start.setEnabled(true);				//Enables the Start button
			pause.setEnabled(false);			//Disables the Pause button
			resume.setEnabled(false);			//Disables the Continue button
			addIntersection.setEnabled(true);		//Enables the Add Intersection button
			addCar.setEnabled(true);			//Enables the Add Car button
		});
		
		//Give functionality to the Add Intersection button
		addIntersection.addActionListener((ActionEvent e) -> {
			
			intersectionCount++;									//Increment the count of intersections
			JLabel newLightLabel = new JLabel("Intersection " + intersectionCount);			//Create a new JLabel for the intersection
			JTextField newLightField = new JTextField();						//Create a new JTextField for the intersection
			newLightField.setEditable(false);							//Set the JTF to disable editing
			TrafficLightSimulator addedLight = new TrafficLightSimulator(newLightField);		//Create a new TrafficLightSimulator object
			trafficLights.add(addedLight);								//Add the new light to the ArrayList of traffic lights
			
			center.add(newLightLabel);				//Add the new traffic light label to the panel containing the lights
			center.add(newLightField);				//Add the new traffic light JTF to the panel containing the lights
			
			//Update the frame to show the added components
			frame.revalidate();				
			frame.repaint();
		});
		
		//Give functionality to the Add Car button
		addCar.addActionListener((ActionEvent e) -> {
			
			carCount++;							//Increment the count of cars
			JLabel newCarLabel = new JLabel("Car " + carCount);		//Create a new JLabel for the car
			JTextArea newCarField = new JTextArea();			//Create a new JTextArea for the car
			newCarField.setEditable(false);					//Set the JTF to disable editing
			Car addedCar = new Car(newCarField, 50, trafficLights);		//Create a new Car object. ALL ADDED CARS WILL HAVE A SPEED OF 50 meters per second
			cars.add(addedCar);						//Add the new Car to the ArrayList of cars
				
			right.add(newCarLabel);					//Add the new car label to the panel containing the cars
			right.add(newCarField);					//Add the new car JTF to the panel containing the cars
			
			//Update the frame to show the added components
			frame.revalidate();
			frame.repaint();
		});
	}
	
	/**
	 * Displays the GUI to the user.
	 */
	public void showGUI() {
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Main method to run the GUI. It creates the GUI object and displays it to the user.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.showGUI();
	}

}
