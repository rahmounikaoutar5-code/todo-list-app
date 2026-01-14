package com.todolist.models;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private int id;
    private String title;
    private String description;
    private Date dueDate;
    private Priority priority;
    private boolean completed;
    private Date createdAt;
    private boolean hasReminder;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // Constructeurs
    public Task() {
        this.createdAt = new Date();
        this.priority = Priority.MEDIUM;
        this.completed = false;
    }

    public Task(String title, String description, Date dueDate, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean hasReminder() { return hasReminder; }
    public void setHasReminder(boolean hasReminder) { this.hasReminder = hasReminder; }

    // Méthodes utilitaires
    public boolean isToday() {
        if (dueDate == null) return false;
        Date today = new Date();
        return dueDate.getDate() == today.getDate() &&
               dueDate.getMonth() == today.getMonth() &&
               dueDate.getYear() == today.getYear();
    }

    public boolean isImportant() {
        return priority == Priority.HIGH && !completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                '}';
    }
}
