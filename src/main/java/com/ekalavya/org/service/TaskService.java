package com.ekalavya.org.service;

import com.ekalavya.org.entity.Task;
import com.ekalavya.org.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task save(Task task){
        return taskRepository.save(task);
    }

}
