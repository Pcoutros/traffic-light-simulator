import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

public class GUI {
	
	private JFrame frame;
	private JPanel left, center,right, bottom;
	private JButton start, pause, resume, stop, addIntersection, addCar;
	private JTextField timeField, firstLight, secondLight, thirdLight, firstCar, secondCar, thirdCar;
	private JLabel firstLightLabel, secondLightLabel, thirdLightLabel, firstCarLabel, secondCarLabel, thirdCarLabel;
	private int carCount = 3;
	private int intersectionCount = 3;
	
	public GUI() {
		frame = new JFrame("Project 3 - Concurrency");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		left = new JPanel();
		Border leftBorder = BorderFactory.createTitledBorder("Time");
		left.setBorder(leftBorder);
		
		timeField = new JTextField(15);
		timeField.setEditable(false);
		left.add(timeField, BorderLayout.CENTER);
		
		center = new JPanel();
		Border centerBorder = BorderFactory.createTitledBorder("Trafic Lights");
		center.setBorder(centerBorder);
		
		firstLight = new JTextField();
		firstLight.setEditable(false);
		secondLight = new JTextField();
		secondLight.setEditable(false);
		thirdLight = new JTextField();
		thirdLight.setEditable(false);
		
		firstLightLabel = new JLabel("Intersection 1");
		secondLightLabel = new JLabel("Intersection 2");
		thirdLightLabel = new JLabel("Intersection 3");

		center.setLayout(new GridLayout(0,2));
		center.add(firstLightLabel);
		center.add(firstLight);
		center.add(secondLightLabel);
		center.add(secondLight);
		center.add(thirdLightLabel);
		center.add(thirdLight);
		
		right = new JPanel();
		Border rightBorder = BorderFactory.createTitledBorder("X, Y Positions");
		right.setBorder(rightBorder);
		
		firstCar = new JTextField();
		firstCar.setEditable(false);
		secondCar = new JTextField();
		secondCar.setEditable(false);
		thirdCar = new JTextField();
		thirdCar.setEditable(false);
		
		firstCarLabel = new JLabel("Car 1");
		secondCarLabel = new JLabel("Car 2");
		thirdCarLabel = new JLabel("Car 3");
		
		right.setLayout(new GridLayout(0,2));
		right.add(firstCarLabel);
		right.add(firstCar);
		right.add(secondCarLabel);
		right.add(secondCar);
		right.add(thirdCarLabel);
		right.add(thirdCar);
		
		bottom = new JPanel();
		Border bottomBorder = BorderFactory.createTitledBorder("Controls");
		bottom.setBorder(bottomBorder);
		bottom.setLayout(new FlowLayout());
		
		left.setPreferredSize(new Dimension(200, 300));
		center.setPreferredSize(new Dimension(200, 300));
		firstLight.setPreferredSize(new Dimension(200, 100));
		right.setPreferredSize(new Dimension(400, 300));
		firstCar.setPreferredSize(new Dimension(200, 100));
		bottom.setPreferredSize(new Dimension(600, 100));
	
		start = new JButton("Start");
		start.setEnabled(true);
		pause = new JButton("Pause");
		pause.setEnabled(false);
		resume = new JButton("Continue");
		resume.setEnabled(false);
		stop = new JButton("Stop");
		stop.setEnabled(false);
		addIntersection = new JButton("Add Intersection");
		addIntersection.setEnabled(true);
		addCar = new JButton("Add Car");
		addCar.setEnabled(true);
		bottom.add(start);
		bottom.add(pause);
		bottom.add(resume);
		bottom.add(stop);
		bottom.add(addIntersection);
		bottom.add(addCar);
		
		frame.add(left, BorderLayout.WEST);
		frame.add(center, BorderLayout.CENTER);
		frame.add(right, BorderLayout.EAST);
		frame.add(bottom, BorderLayout.SOUTH);
		
		addButtonFunctionality();
	}
	
	private void addButtonFunctionality() {
		CurrentTime currentTime = new CurrentTime(timeField);
		
		ArrayList<TrafficLightSimulator> trafficLights = new ArrayList<>();
		ArrayList<Car> cars = new ArrayList<>();
		
		TrafficLightSimulator lightOne = new TrafficLightSimulator(firstLight);
		TrafficLightSimulator lightTwo = new TrafficLightSimulator(secondLight);
		TrafficLightSimulator lightThree = new TrafficLightSimulator(thirdLight);
		
		trafficLights.add(lightOne);
		trafficLights.add(lightTwo);
		trafficLights.add(lightThree);
				
		Car carOne = new Car(firstCar, 20, trafficLights);
		Car carTwo = new Car(secondCar, 70, trafficLights);
		Car carThree = new Car(thirdCar, 90, trafficLights);
		
		cars.add(carOne);
		cars.add(carTwo);
		cars.add(carThree);
		
		start.addActionListener((ActionEvent e) -> {
			
			for (TrafficLightSimulator trafficLight : trafficLights) {
				Thread trafficThread = new Thread(trafficLight);
				trafficLight.setIsProgramRunning(true);
				trafficThread.start();
			}
			
			for (Car car : cars) {
				Thread carThread = new Thread(car);
				car.setIsProgramRunning(true);
				carThread.start();
			}
			
			Thread time = new Thread(currentTime);
			currentTime.setIsProgramRunning(true);
			time.start();
	
			start.setEnabled(false);
			stop.setEnabled(true);
			pause.setEnabled(true);
			resume.setEnabled(true);
			addIntersection.setEnabled(false);
			addCar.setEnabled(false);
			
		});
		
		pause.addActionListener((ActionEvent e) -> {
			currentTime.setIsProgramPaused(true);
			
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramPaused(true);
			}
			
			for (Car car : cars) {
				car.setIsProgramPaused(true);
			}
			
			pause.setEnabled(false);
			start.setEnabled(false);
			resume.setEnabled(true);
			stop.setEnabled(true);
			addIntersection.setEnabled(false);
			addCar.setEnabled(false);
		});
		
		resume.addActionListener((ActionEvent e) -> {
			currentTime.setIsProgramPaused(false);
			
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramPaused(false);
			}
			
			for (Car car : cars) {
				car.setIsProgramPaused(false);
			}
			
			resume.setEnabled(false);
			start.setEnabled(false);
			stop.setEnabled(true);
			pause.setEnabled(true);
			addIntersection.setEnabled(false);
			addCar.setEnabled(false);
		});
		
		stop.addActionListener((ActionEvent e) -> {
			currentTime.setIsProgramRunning(false);
			
			for (TrafficLightSimulator trafficLight : trafficLights) {
				trafficLight.setIsProgramRunning(false);
			}
			
			for (Car car : cars) {
				car.setIsProgramRunning(false);
			}

			stop.setEnabled(false);
			start.setEnabled(true);
			pause.setEnabled(false);
			resume.setEnabled(false);
			addIntersection.setEnabled(true);
			addCar.setEnabled(true);
		});
		
		addIntersection.addActionListener((ActionEvent e) -> {
			intersectionCount++;
			JLabel newLightLabel = new JLabel("Intersection " + intersectionCount);
			JTextField newLightField = new JTextField();
			newLightField.setEditable(false);
			TrafficLightSimulator addedLight = new TrafficLightSimulator(newLightField);
			trafficLights.add(addedLight);
			
			center.add(newLightLabel);
			center.add(newLightField);
			frame.revalidate();
			frame.repaint();
		});
		
		addCar.addActionListener((ActionEvent e) -> {
			carCount++;
			JLabel newCarLabel = new JLabel("Car " + carCount);
			JTextField newCarField = new JTextField();
			newCarField.setEditable(false);
			Car addedCar = new Car(newCarField, 50, trafficLights);
			cars.add(addedCar);
			
			right.add(newCarLabel);
			right.add(newCarField);
			frame.revalidate();
			frame.repaint();
			
		});
	}
	
	public void showGUI() {
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.showGUI();
	}

}
