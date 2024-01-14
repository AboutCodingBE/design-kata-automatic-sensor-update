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

## What needs to be done: 

- Create an automated solution to check firmware version a batch of sensors.
- Automatically schedule firmware updates for sensors that have incompatible versions.
- Also schedule configuration updates if needed. 
- Give feedback of the current status to the user 

## Details you need to know 

- The version of the firmware that is actually compatible with the latest configuration is `59.1.12Rev4`
- Any version equal or higher (for example: `59.2.12Rev0`) is valid. Anything lower is not.
- The name of the latest configuration file is `ts50x-20230811T10301211.cfg`. 
- Information about the sensors can be fetched by doing a REST api call: GET `www.mysensor.io/api/sensors/{id}`. 
- The response type of fetching sensor information looks like this:
```json
{
  "serial": 123456789098789,
  "type": "TS50X",
  "status_id": 1,
  "current_configuration": "some_oonfiguration.cfg",
  "current_firmware": "59.1.12Rev4",
  "created_at": "2022-03-31 11:26:08",
  "updated_at": "2022-10-18 17:53:48",
  "status_name": "Idle",
  "next_task": null,
  "task_count": 5,
  "activity_status": "Online",
  "task_queue": [124355, 44435322] 
}
```
- Scheduling updates for both firmware and configuration can be done using the same REST API: PUT `www.mysensor.io/api/tasks`
- The request body for scheduling a task should look like this: 
```json
{
  "sensor_serial": 234545767890987,
  "file_id": "a3e4aed2-b091-41a6-8265-2185040e2c32",
  "type": "update_configuration"
}
```
In the case of a firmware update, you don't need to add a file id as the original manufacturer will install the latest firmware. 



## Some notes on the solution on this branch

- This solution is aiming to be the textbook Clean Architecture solution.
- In my opinion, this is not the best solution as it doesn't take context into account
- The purpose of this branch is to demonstrate and to trigger discussion. 
- This solution doesn't take any security in consideration (yet). This is on purpose, the focus is on simple design. 

