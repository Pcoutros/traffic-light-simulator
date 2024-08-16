# Traffic Light Simulator - A Multithreading Project

## Description
This project showcases multithreading in Java throught the simulation of multiple cars traversing multiple inline traffic lights. The lights and cars all run on their own threads. The front end of this project is a GUI created with Java Swing. The idea is that the simulation starts and the cars begin traveling towards the lights at a constant speed (each car has a different speed). The lights are placed in a straight line 1000 meters apart from each other. Cars are free to pass through lights when they are green. When the light turns yellow the cars can still approach the light at their constant speed, if the car passes the light when it is yellow it is free to keep moving. Once the light turns red, the cars will stop only if any increase in distance would place them beyond the light otherwise the cars will continue to travel towards the red light. After the car has passed all lights, it will continue to move at its constant speed until the simulation is ended

## UML

<img width="468" alt="image" src="https://github.com/user-attachments/assets/5ceef9d3-21bb-4adc-afdd-fe5021713df0">
