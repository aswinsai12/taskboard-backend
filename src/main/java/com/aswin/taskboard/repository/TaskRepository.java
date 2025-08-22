package com.aswin.taskboard.repository;

import com.aswin.taskboard.model.Task;
import com.aswin.taskboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user); // <-- ADD THIS LINE
}
