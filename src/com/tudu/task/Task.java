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


    protected Task(String name, LocalDateTime dueDate, int status, String project) {
        TaskStatus[] statusList = TaskStatus.values();
        this.name = name.isEmpty() ? "NO NAME" : name;
        this.dueDate = dueDate;
        this.status = statusList[status-1];
        this.project = new TaskProject(project);
    }

    // Getters and setters for all fields
    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected LocalDateTime getDueDate() {
        return dueDate;
    }

    protected void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    protected TaskStatus getStatus() {
        return status;
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    protected String getProject() {
        return project.getName();
    }

    protected void setProject(String project) {
        this.project = new TaskProject(project);
    }

    // toString Method to display

    @Override
    public String toString() {
        LocalDate dueDate = this.dueDate.toLocalDate();
        LocalTime dueTime = this.dueDate.toLocalTime();
        return  "Task: "+ name +", due at: " +dueDate+" "+dueTime +", current status: " +status +", project type: " +project.getName() ;
    }


    public boolean equals(Object o){
        if(o == this){
            return true;
        }

        if(!(o instanceof Task)){
            return false;
        }

        Task task = (Task) o;
        return ((task.getName().equals(this.name)) &&
                (task.getDueDate().isEqual(this.dueDate)) &&
                ((task.getStatus().compareTo(this.status)) == 0) &&
                (task.getProject().equals(this.getProject())));
    }

    // IF: add colored display based on project color
    // IF: add a font style : Italics for finished, Bold for those that need to be done, normal for ongoing
}

