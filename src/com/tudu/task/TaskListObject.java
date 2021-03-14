package com.tudu.task;

import java.time.LocalDateTime;

abstract class TaskListObject {
    protected abstract void addTask(String taskName, LocalDateTime dueDate, int status, String project);
    protected abstract void displayByDueDate(boolean ascending);
    protected abstract void displayByProject(boolean ascending);
    // Method to edit fields of a Task
    // Method to mark task as done
    // Method to remove tasks
    protected abstract void saveTaskListToFile(String stringPathToDatabase);
    // Method to load from a file
}
