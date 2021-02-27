package com.tudu.tasklist;
import com.tudu.task.Task;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private Set<Task> taskList;
    public TaskList() {
        taskList = new HashSet<Task>(100);
    }
    // Methods : addTask, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    public void addTask(Task task){
        if(taskList.contains(task)){
            System.out.println("Task is already present!");
        } else {
            System.out.println(task.toString()+"\nAbove task is now in your TuDu list");
            taskList.add(task);
        }

    }

    public void displayTaskList(){
        for (Task value : taskList) {
            System.out.println(value.toString());
        }
    }
    // Methods : sortByDate(ascending/descending), sortByProjectFlag(ascending/descending), displayTaskList,

    // Methods : saveToFile (as CSV/TSV), loadFromFile(from CSV/TSV)
}
