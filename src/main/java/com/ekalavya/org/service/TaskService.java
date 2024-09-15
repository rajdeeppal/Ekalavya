package com.ekalavya.org.service;

import com.ekalavya.org.DTO.TaskDTO;
import com.ekalavya.org.entity.Activity;
import com.ekalavya.org.entity.Task;
import com.ekalavya.org.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findByActivity(Activity activityEntity) {
        return taskRepository.findByActivity(activityEntity);
    }

    public void updateTask(Long taskId, TaskDTO updatedTask) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setUnits(updatedTask.getUnits());
        existingTask.setRatePerUnit(updatedTask.getRatePerUnit());
        taskRepository.save(existingTask);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
}
