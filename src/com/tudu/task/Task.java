package com.tudu.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class Task{
    // Task class contains : title, dueDate, TaskStatus, project, IF:project color
    // All fields are private
    private String title;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private String project;

    public Task(){
        this.title = "DEFAULT";
        this.dueDate = LocalDateTime.of(2021, Month.FEBRUARY, 23, 22, 0);
        this.status = TaskStatus.UNSTARTED;
        this.project = "DEFAULT";
    }

    public Task(String title, LocalDateTime dueDate, TaskStatus status, String project) {
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
        this.project = project;
    }

    // Getters and setters for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDueDate() {
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    // toString Method to display

    @Override
    public String toString() {
        LocalDate dueDate = this.dueDate.toLocalDate();
        LocalTime dueTime = this.dueDate.toLocalTime();
        return  "Task: "+title+", due at: " +dueDate+" "+dueTime +", current status: " +status +", project type: " +project ;
    }

    // IF: add colored display based on project color
    // IF: add a font style : Italics for finished, Bold for those that need to be done, normal for ongoing
}
