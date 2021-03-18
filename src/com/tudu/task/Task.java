package com.tudu.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a task. This class contains fields to store the name, due date,
 * status and project of a task.
 * @author yashaswiniseeta
 * @see TaskStatus
 * @see TaskProject
 * @see LocalDateTime
 */
public class Task{
    /**
     * Represents the name of a task.
     */
    public String name;
    /**
     * Represents the due date of a task.
     */
    public LocalDateTime dueDate;
    /**
     * Represents the status of a task.
     */
    public TaskStatus status;
    /**
     * Represents the project of a task.
     */
    public TaskProject project;

    /**
     * Constructs a task by and sets its fields to the specified arguments.
     * @param name The name to be set for the task; argument can be an empty string, in that case a default {@code "NO NAME"} name is given
     * @param dueDate The date by which this task should be done
     * @param status The current status of the task
     * @param project The project to which this task belongs
     */
    public Task(String name, LocalDateTime dueDate, TaskStatus status, String project) {
        this.name = name.isEmpty() ? "NO NAME" : name;
        this.dueDate = dueDate;
        this.status = status;
        this.project = new TaskProject(project);
    }

    public String getName() {
        return name;
    }

    /**
     * Set the name of a task to the specified name.
     * @param name the name to be set for the task; argument can be empty, in that case a default {@code "NO NAME"} name is given
     */
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

    /**
     * Returns the name of the project of this task.
     * @return the name of the project of this task.
     */
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

