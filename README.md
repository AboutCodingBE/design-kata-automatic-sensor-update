# Design Kata: Automated Sensor Updates
This is a simple project intended as a practice design kata

## Problem:
You are part of an IoT company shipping sensors to a vast amount of clients. These sensors, which are of type TS50X, 
have a firmware from their original manufacturer, which is pretty trustworthy. In your company these sensors have to be  
configured so that they send valuable information which is of interest to the vast amount of clients.

While testing the sensors with the latest configuration, people noticed a compatibility problem with older firmware. There 
are new features which can be configured, but some older firmware doesn't know how to deal with those new features. 
So the sensors only do a part of what they should be doing. We can’t have that, our company stands for high quality and 
happy customers. So we need to prepare these sensors before shipping. 

What we need is a web service which, given a list of sensor ids, will check if the firmware version is recent enough. It 
schedules an update if it is not recent enough. When all of that checks out, schedule a configuration update. The result
of the request should be an overview of the current state of the sensors. Are they ready to be shipped? If not, then what
needs to be done?

