package com.aswin.taskboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean completed;

    private LocalDate dueDate;    // Add this
    private String category;      // Add this
    private String priority;      // Add this

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore               // Prevent circular reference in JSON
    private User user;

    public Task() {}

    // Getters and setters for all fields below (including new fields)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
