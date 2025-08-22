package com.aswin.taskboard.controller;

import com.aswin.taskboard.model.Task;
import com.aswin.taskboard.model.User;
import com.aswin.taskboard.repository.TaskRepository;
import com.aswin.taskboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Task> getTasks(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) return List.of();
        return taskRepository.findByUser(user);
    }

    @PostMapping
    public Task addTask(@RequestParam String username, @RequestBody Task task) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");
        task.setUser(user);
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestParam String username, @RequestBody Task updatedTask) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!existingTask.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        existingTask.setText(updatedTask.getText());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCategory(updatedTask.getCategory());
        existingTask.setPriority(updatedTask.getPriority());

        return taskRepository.save(existingTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!existingTask.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        taskRepository.delete(existingTask);
    }
}
