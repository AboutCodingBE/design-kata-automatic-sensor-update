package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.UpdateTask;
import org.springframework.stereotype.Component;

@Component
class UpdateVerification {

    private final TaskRepository taskRepository;
    private final FileRepository fileRepository;

    public UpdateVerification(TaskRepository taskRepository, FileRepository fileRepository) {
        this.taskRepository = taskRepository;
        this.fileRepository = fileRepository;
    }

    void forSensor(TS50X sensor) {
        //Step 1: are there running tasks?
        var listOfTasks = sensor.getTaskQueue();
        if (listOfTasks.size() == 1) {
            // If there is only 1, handle it
            setUpdateStatusFor(sensor, listOfTasks.get(0));
        }
        else if (listOfTasks.size() > 1) {
            // If there is more than one, delete all of them
            listOfTasks.forEach(taskRepository::deleteTask);
        }
    }

    private void setUpdateStatusFor(TS50X sensor, Long taskId) {
        // Get the task information
        var task = taskRepository.getTask(taskId);
        if (task.isPresent()) {
            //Get the update file information
            var updateTask = task.get();
            var fileName = fileRepository.getFileNameOf(updateTask.getFileId());

            //Delete task if the filename is unknown or if the task is neither a configuration nor firmware update
            if (fileName.isEmpty() || (!updateTask.isConfigurationUpdate() && !updateTask.isFirmwareUpdate())) {
                taskRepository.deleteTask(updateTask.getId());
            }

            //Check if the task needs to be deleted or not.
            var fileReference = fileName.get().substring(0, fileName.get().indexOf(".zip"));
            isValidUpdateFor(sensor, updateTask, fileReference);

        }
    }

    private void isValidUpdateFor(TS50X sensor, UpdateTask updateTask, String fileReference) {
        if (updateTask.isFirmwareUpdate()) {
            if (!TS50X.isTargetVersionValid(fileReference)) {
                taskRepository.deleteTask(updateTask.getId());
            }
            else {
                sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
            }
        }
        else if (updateTask.isConfigurationUpdate()) {
            if (!TS50X.isTargetConfiguration(fileReference)) {
                taskRepository.deleteTask(updateTask.getId());
            }
            else {
                sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
            }
        }
        else {
            taskRepository.deleteTask(updateTask.getId());
        }
    }
}
