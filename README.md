# Traffic Light Simulator - A Multithreading Project

## Description
  This project showcases multithreading in Java throught the simulation of multiple cars traversing multiple inline traffic lights. The lights and cars all run on their own threads. The front end of this project is a GUI created with Java Swing. The idea is that the simulation starts and the cars begin traveling towards the lights at a constant speed (each car has a different speed). The lights are placed in a straight line 1000 meters apart from each other. Cars are free to pass through lights when they are green. When the light turns yellow the cars can still approach the light at their constant speed, if the car passes the light when it is yellow it is free to keep moving. Once the light turns red, the cars will stop only if any increase in distance would place them beyond the light otherwise the cars will continue to travel towards the red light. After the car has passed all lights, it will continue to move at its constant speed until the simulation is ended

## UML

<img width="468" alt="image" src="https://github.com/user-attachments/assets/5ceef9d3-21bb-4adc-afdd-fe5021713df0">

## Assumptions

1) Cars move at steady pace between intersections, through green lights, and through yellow lights.
2) Cars stop immediately at the closest location to a Red light without going into/over the intersection.
3) Cars will continue toward a Red light at their constant rate of speed until any further travel would put them past the light, they stop at the light and wait until it is green
4) Car speed is in meter per second and all lights are 1000 meters apart with the first light being located at 1000 meters, second at 2000, third at 3000 etc.
5) xPositions of cars will not be entirely accurate due to the time keeping nature of GUI and Threads. However the distances traveled each second should be approximate to their speed. Ie     if it is traveling at 90mps we should expect to see somewhere around 90 meters gained every second (if not stopped at a light).
6) The program should act as if nothing happened when paused and then subsequently continued.
7) The program should restart and clear values when stopped and then restarted.
8) Added intersections are added 1000 meters away from the last intersection.
9) Add cars start at 1000 meters prior to the first intersection like the 3 default cars.
10) All added cars have a speed of 50 meters per second, while car 1 has a speed of 20mps, car 2 has a speed of 70mps, and car 3 has a speed of 90mps.
11) If an Intersection or Car is added they will remain on the simulation. Even if stopped and restarted. They are cleared each time the program is exited and then restarted.

## How to Use

1) Clone the repository
2) Open terminal and change directories to the desired destination
3) Type: git clone and paste the URL copied earlier
4) Change directory into the repository name
5) Compile the GUI.java file using: javac GUI.java
6) Run the program using: java GUI.java
