# Vision
## To Do
- Enable OpenCV to control Camera to prevent windows from accidentally not setting the correct exposure (if possible)
- Add ability to load single images for testing filters w/out a camera //////////Objective completed
- Average multiple frames for denoising
- Rewrite Main class to remove spaghetti code//In progress
- Add ability for user to click on one part of the image and receive pixel values in order to quicken filter tuning
- Move image capture and image processing to seperate threads in order to improve system performance

## Installation
https://drive.google.com/open?id=0B42XozG4U9uCVE1pUHJmY0xEUGM
-Note: that dpending upon your installation camAngle in Main.math() should be modified to represent the angle formed between the vector
of the camera pointing towards the goal and the level floor upon which the robot is located.

## Hardware
- Microsoft LifeCam HD-3000
- Green Led Ring https://www.superbrightleds.com/moreinfo/led-headlight-accent-lights/led-halo-angel-eye-headlight-accent-lights/49/
- Led Rings should be Green 80mm
- Computer running windows with the software listed above installed 
- Note: (Linux has some support integrated into the code but, has not been fully tested)
- Note: A Kangaroo + running windows 10 may be used for mobile applications

## Purpose 
The purpose of this project was to further FRC Team 3140's understanding of Vision Processing and to increase team readiness
for the 2017 competition season. It was also developed for personal education and later use in projects by myself.

## Credit to others and the openSource nature of this code
- Feel free to experiment with my code as long as my team and I recieve credit.
- Notable help with this project should go to https://sites.google.com/site/pdopencvjava/home aswell as all of the other great forums and resources that the internet provides especially, http://stackoverflow.com/.
- Other information about the FIRST Competitions can be found at http://www.firstinspires.org/
- Other technical help regarding FRC can be found at https://www.chiefdelphi.com/forums/portal.php
- OpenCV API and main website http://opencv.org/
- OpenCV Forums http://answers.opencv.org/questions/

## Code capabilities
The code currently allows for a webcam to be streamed, filtered, denoised, and then go through the process of analyzing contours to 
determine the location of the camera relative to the retroreflective tape goal described here. https://static1.squarespace.com/static/5660f08ee4b02bc8dd3e4879/t/5696e22b1c121044d442eff0/1452728893521/
The code has a fully fleshed out user interface to allow for easy modification of image filters while viewing before and after video streams aswell as a new debug feature which will allow users to analyze one image at a time.
-Note: the code also has accidental camera disconnect protections so that at anytime if the camera is temporarily disconnected once reinserted video processing shoudl recommence automatically.
