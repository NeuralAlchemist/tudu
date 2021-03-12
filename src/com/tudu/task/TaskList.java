package com.tudu.task;

import java.time.LocalDateTime;
import java.util.*;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private TreeMap<String, ArrayList<Task>> projectSortedMap;
    private LinkedList<Task> dueDateSortedList;
    private int numberOfTasks = 0;

    protected TaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    // Methods : ~~addTask~~, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    // update to check for already present tasks and return false if not added
    protected void addTask(String taskName, LocalDateTime dueDate, boolean status, String project) {
        Task task = new Task(taskName, dueDate, status, project);
        String projectName = task.getProject();
        boolean addedToProjectSortedMap = false;
        boolean addedToDueDateSortedList = false;
        boolean projectExistsAlready = projectSortedMap.containsKey(projectName);
        ArrayList<Task> tasksOfProject = projectExistsAlready ? projectSortedMap.get(projectName) : new ArrayList<>(100);
        if (projectExistsAlready && tasksOfProject.contains(task)) {
            addedToProjectSortedMap = false;
        } else if (!tasksOfProject.contains(task)) {
            // Improve adding to sort it out by dueDate
            tasksOfProject.add(task);
            projectSortedMap.put(projectName, tasksOfProject);
            addedToProjectSortedMap = true;
        }

        if (dueDateSortedList.isEmpty()) {
            dueDateSortedList.add(task);
            addedToDueDateSortedList = true;
        } else if(!dueDateSortedList.contains(task)) {
            Iterator<Task> listItr = dueDateSortedList.descendingIterator();
            int currentIndex = dueDateSortedList.size()-1;
            while(listItr.hasNext()){
                Task current = listItr.next();
                if(task.getDueDate().isAfter(current.getDueDate())){
                    dueDateSortedList.add(currentIndex+1, task);
                    addedToDueDateSortedList = true;
                    break;
                } else if(currentIndex == 0){
                    dueDateSortedList.offerFirst(task);
                    addedToDueDateSortedList = true;
                }
                currentIndex--;
            }
        } else {
            addedToDueDateSortedList = false;
        }
        if(addedToDueDateSortedList && addedToProjectSortedMap){
            numberOfTasks++;
            System.out.println("Task has been added");
            //Implement boolean return
        } else {
            // Implement boolean return
        }
    }

    protected LinkedList<Task> getSortedByDueDate() {
        return dueDateSortedList;
    }

    protected TreeMap<String, ArrayList<Task>> getSortedByProject() {
        return projectSortedMap;
    }


    // Methods : sortByDate(ascending/descending), sortByProjectFlag(ascending/descending)
    protected void displayByDueDate(boolean ascending){
        Iterator<Task> itr = ascending ? dueDateSortedList.iterator() : dueDateSortedList.descendingIterator();
        while(itr.hasNext()){
            System.out.println(itr.next().toString());
        }
    }

    protected void displayByProject(boolean ascending){
        NavigableSet<String> entries = ascending ? projectSortedMap.navigableKeySet() : projectSortedMap.descendingKeySet();
        for(String entry : entries){
            ArrayList<Task> tasksOfProject = projectSortedMap.get(entry);
            System.out.println("tasks of project: " + entry + " total: " + tasksOfProject.size());
            for (Task task : tasksOfProject) {
                System.out.println(task.toString());
            }
        }
    }

    protected ArrayList<Task> findTaskByName(String taskName){
        ArrayList<Task> listOfTasksFound = new ArrayList<>(10);
        Iterator<Task> itr = dueDateSortedList.iterator();
        Task current;
        while(itr.hasNext()){
            current = itr.next();
            if(current.getName().equalsIgnoreCase(taskName)){
                listOfTasksFound.add(current);
            }
        }
        return listOfTasksFound.isEmpty()? null : listOfTasksFound;
    }

    protected int getNumberOfTasks(){
        return numberOfTasks;
    }

    /*public void editByProject(){
        NavigableSet<String> entries = projectSortedMap.navigableKeySet();
        int numberOfProject = 1;
        for(String entry : entries){
            System.out.println(numberOfProject+" -> "+entry);
            numberOfProject++;
        }
    }*/


    // Methods : saveToFile (as CSV/TSV), loadFromFile(from CSV/TSV)
}
