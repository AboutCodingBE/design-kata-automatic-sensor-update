# Design Kata: Automated Sensor Updates
This is a simple project intended as a practice design kata

## This is a possible solution for the 2nd evolution 

#### The original problem: 

You have a list of sensor IDs and you want to know if these sensors are ready to be shipped or not. If not, you need to 
schedule the next step in order to make it shippable. A sensor is ready to be shipped when its firmware is appropriate 
and if the configuration is the latest configuration.

So for each sensor with its id in the list, you need to check if the firmware is appropriate so that the latest 
configuration can run without problems. Currently, firmware with version `59.1.12Rev4` or higher is appropriate. Don’t forget 
to schedule a task for the configuration and if needed, a task for to updte the firmware.

You need to use the API of the original manufacturer to get the current sensor information and to schedule tasks such as 
a firmware update and a configuration update.

##### But change is coming!

The solution is a success! Therefore your managers would like to use this solution for other sensors as well, namely the 
P100T. It turns out that these sensors have a different way of versioning firmware. They use dates followed by a release 
number. For example: `2023-09-10R2`.

You have to extend the solution so that we can give a list of P100T ids to check and update. We are lucky though: This 
type of sensor is coming from the same manufacturer and so we can use the same API to get information about these sensors 
and to schedule the updates.

