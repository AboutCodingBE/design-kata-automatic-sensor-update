# Design Kata: Automated Sensor Updates
This is a simple project intended as a practice design kata
The solution on this branch is a demonstration of a poorly designed solution. 

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

## Notes on this particular solution

This solution is merely a example of how NOT to do it. It is an obvious example, yet it is something I have encountered 
numerous times in the past. In this example, most of the process logic of the use case is stuffed in one file. This has 
some consequences on understanding the solution and what it does, but also on the ability to change/extend the behaviour. 

Because there is no clear separation between the different steps in this process, changes on the axis of how can spread
out and have unintended consequences. 

Note the following problems in this codebase: 

The biggest problem with this version of the solution is that it is harder to change. Here is why:

* The process to validate the firmware is harder to follow because it is hard to see the different steps of the process.
  This adds to cognitive load. 
* Also, these different steps represent different points on the axis of 'how' this use case is going to be completed. 
  Because they are not clearly separated, it is harder to change in isolation. Because of that, there is a bit more 
  chance to create bugs in other parts of the process. Because at this point the problem is relatively simple, this problem 
  isn't that noticeable. You can try this yourself and push through some changes (cfr: the [evolutions](https://aboutcoding.be/automated-sensor-updates-project/))
* It is also more vulnerable to change from outside the problem domain. This solution depends on a 3rd party API. What if
  all of a sudden there is no more API to pull information about the sensors, but everything is now in a database with 
  a bit of a different schema. In this solution, the 'schema' or the returned information from the API is known by 
  multiple classes. Just try and change one or 2 things in that schema (the class is called SensorInformation) and see what happens. 
* This code base is not fit to deal with variations of parts of the process that need to co-exist. The most obvious example
  of a variation would be a different type of sensor which we also need to validate, but the versioning is different. Or 
  the location of the actual information of this new type of sensor is a different API. It would be a pain to implement 
  this in this version of the solution. 

Here are some other, but smaller code smells: 

- Information is being pulled from the now anemic domain model
- The SensorService directly pulls in an information model from a 3rd party webservice. This violates the dependency rule.
- The target configuration is defined in the SensorService. But the TaskService also needs it. So it is now being passed 
  as a parameter and the TaskService is now aware of an implementation detail that can cahnge. This can be avoided if it was 
  part of a cohesive domain model.
- The flow of the use case ends in the TaskService. That is one extra class 'further away' from where it started and it adds
  to the cognitive load. We should aim for a shallow component tree.


