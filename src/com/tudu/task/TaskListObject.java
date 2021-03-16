package com.tudu.task;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.NavigableSet;

abstract class TaskListObject {
    protected abstract Task addTask(String taskName, LocalDateTime dueDate, int status, String project);
    protected abstract LinkedList<Task> displayByDueDate(boolean ascending);
    protected abstract NavigableSet<String> displayByProject(boolean ascending);
    // Method to edit fields of a Task
    // Method to mark task as done
    // Method to remove tasks
    protected abstract void saveTaskListToFile(String stringPathToDatabase);
    // Method to load from a file
}
