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

## The solution

This is a perfect situation to demonstrate the Open / Closed Principle. Our code needs to be extended by a variation of
behaviour.

This is the solution using an interface.

#### Thoughts while coding this

- I created the interface and then the new class for the new sensor. Pretty soon it deems to me that I also have to create
  other methods from the other sensor model, like: `isLastestConfiguration()`
- After implementing the new sensor model, we need to use it. We made a variation to validating the firmware. In other
  words, we abstracted what stays true (the abstraction is valid firmware returning a boolean) and we added a variation
  of that abstraction. A different implementation. We have our use case from which we don't want it to be aware of which
  sensor it is processing. So after we created and implemented this abstraction, we need to use it (replace the implementation
  with the abstraction in the use case).
- This means that our repository shouldn't be returning the implementation TS50X (otherwise will never be able to use the 
  the other sensor implementation). The repository should be returning the abstraction...
- This turns out to trigger more changes than initially thought, right? The use case is a cohesive unit of code. One operation
  usually 'depends' a bit on the result of the previous one. 
- This is normal. It is not really coupling, as the entire use case is all about delegating different code units for the 
- same purpose. Adding a variation to a purpose, to something that is already happening when
  there was no variation before, it is expected that change will ripple through the code. But it should only ripple through
  the code which serves the purpose.
- The key idea is not to prepare code for change that might never come. But when it comes, it is ok if there is a bit more
  work to it. When it comes again though, that is when it should go really easy.
- These other changes is something you don't hear much about. Usually tutorials stop at the implementation of the
  interface. But that is only half of the work of setting up the Open / Closed Principle. We now need to think about how
  these variations will enter our system. In other words, we will have to check the type of the sensor at some place in 
  our codebase and take action according.
- Deciding the actual type of the sensors is now done in the SensorInformationClient. This switch statement to decide which
  implementation to take is in this problem context almost inevitable. Each implementation holds a variation to validate 
  firmware, so in order to use it, we need to define the different variations somewhere. 
- The implementation with the interface is not the best implementation in this situation. We abstracted the most important
  element in the flow (the sensors). Because we used an interface as our abstraction, we abstracted away the necessary 
  behaviour, like getting the id, updating the status, etc. 

