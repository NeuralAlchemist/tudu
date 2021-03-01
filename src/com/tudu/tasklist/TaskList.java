package com.tudu.tasklist;
import com.tudu.task.Task;
import java.util.Map.Entry;
import java.util.*;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private SortedMap<String, ArrayList<Task>> taskList;
    public TaskList() {
        taskList = new TreeMap<>();
    }
    // Methods : addTask, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    public void addTask(Task task){
        String projectName = task.getProject();
        Boolean projectExistsAlready = taskList.containsKey(projectName);
        ArrayList<Task> tasksOfProject = projectExistsAlready ? taskList.get(projectName) : new ArrayList<>(100);
        if(projectExistsAlready && tasksOfProject.contains(task)){
            System.out.println("Task is already present!");
        } else if(!tasksOfProject.contains(task)) {
            // Improve adding to sort it out by dueDate
            tasksOfProject.add(task);
            taskList.put(projectName, tasksOfProject);
            System.out.println("Added Task!");
        }
    }

    public void displayTaskList(){
        System.out.println("Size of tree map: "+taskList.size());
        for( Map.Entry<String, ArrayList<Task>> entry : taskList.entrySet()){
            ArrayList<Task> tasksOfProject = entry.getValue();
            System.out.println("tasks of project: "+entry.getKey()+" total: "+tasksOfProject.size());
            for(Task task : tasksOfProject){
                System.out.println(task.toString());
            }
        }
    }
    // Methods : sortByDate(ascending/descending), sortByProjectFlag(ascending/descending), displayTaskList,

    // Methods : saveToFile (as CSV/TSV), loadFromFile(from CSV/TSV)
}
