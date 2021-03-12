package com.tudu.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task{
    // Task class contains : name, dueDate, TaskStatus, project, IF:project color
    // All fields are private
    private String name;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private TaskProject project;


    protected Task(String name, LocalDateTime dueDate, boolean status, String project) {
        this.name = name.isEmpty() ? "NO NAME" : name;
        this.dueDate = dueDate;
        this.status = status ? TaskStatus.ONGOING : TaskStatus.UNSTARTED;
        this.project = new TaskProject(project);
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    protected String getProject() {
        return project.getName();
    }

    public void setProject(String project) {
        this.project = new TaskProject(project);
    }

    // toString Method to display

    @Override
    public String toString() {
        LocalDate dueDate = this.dueDate.toLocalDate();
        LocalTime dueTime = this.dueDate.toLocalTime();
        return  "Task: "+ name +", due at: " +dueDate+" "+dueTime +", current status: " +status +", project type: " +project.getName() ;
    }

    // IF: add colored display based on project color
    // IF: add a font style : Italics for finished, Bold for those that need to be done, normal for ongoing
}

