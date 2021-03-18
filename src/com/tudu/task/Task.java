package com.tudu.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a task. This class contains fields to store the name, due date,
 * {@link TaskStatus} and {@link } of a task.
 * @author yashaswiniseeta
 * @see TaskStatus
 * @see TaskProject
 * @see LocalDateTime
 */
public class Task{

    public String name;
    public LocalDateTime dueDate;
    public TaskStatus status;
    public TaskProject project;

    public Task(String name, LocalDateTime dueDate, int status, String project) {
        TaskStatus[] statusList = TaskStatus.values();
        this.name = name.isEmpty() ? "NO NAME" : name;
        this.dueDate = dueDate;
        this.status = statusList[status-1];
        this.project = new TaskProject(project);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.isEmpty() ? "NO NAME" : name;
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
        return project.getName();
    }

    public void setProject(String project) {
        this.project = new TaskProject(project);
    }

    @Override
    public String toString() {
        LocalDate dueDate = this.dueDate.toLocalDate();
        LocalTime dueTime = this.dueDate.toLocalTime();
        return  "Task: "+ name +", due at: " +dueDate+" "+dueTime +", current status: " +status +", project type: " +project.getName() ;
    }

    @Override
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

}

