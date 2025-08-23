package org.example.forDB.Service;

import java.util.List;

import org.example.forDB.Repository.TaskRepository;
import org.example.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findByTaskLanguage(String taskLanguage) { 
        return taskRepository.findByTaskLanguage(taskLanguage);
    }

    public List<Task> getTasksByLanguageSortedByDate(String taskLanguage) {
        return taskRepository.findByTaskLanguageOrderByTaskDateAsc(taskLanguage);
    }

}
