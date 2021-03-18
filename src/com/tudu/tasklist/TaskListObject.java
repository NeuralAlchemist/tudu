package com.tudu.tasklist;

import com.tudu.task.Task;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.NavigableSet;

abstract class TaskListObject {
    protected abstract Task addTask(String taskName, LocalDateTime dueDate, int status, String project);
    protected abstract void setTask(String taskName, LocalDateTime dueDate, int status, String project, Task oldTask);
    protected abstract Task markTaskAsDone(Task task);
    protected abstract boolean removeTask(Task task);
    protected abstract void saveTaskListToFile(String stringPathToDatabase);
    protected abstract boolean loadTaskListFromFile(String stringPathToDatabase);
}
