package com.tudu.task;

import java.util.*;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private TreeMap<String, ArrayList<Task>> projectSortedMap;
    private LinkedList<Task> dueDateSortedList;

    public TaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    // Methods : addTask, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    public void addTask(Task task) {
        String projectName = task.getProject();
        Boolean projectExistsAlready = projectSortedMap.containsKey(projectName);
        ArrayList<Task> tasksOfProject = projectExistsAlready ? projectSortedMap.get(projectName) : new ArrayList<>(100);
        if (projectExistsAlready && tasksOfProject.contains(task)) {
            System.out.println("Task is already present!");
        } else if (!tasksOfProject.contains(task)) {
            // Improve adding to sort it out by dueDate
            tasksOfProject.add(task);
            projectSortedMap.put(projectName, tasksOfProject);
            System.out.println("Added Task!");
        }

        if (dueDateSortedList.isEmpty()) {
            dueDateSortedList.add(task);
        } else {
            Iterator<Task> listItr = dueDateSortedList.descendingIterator();
            int currentIndex = dueDateSortedList.size()-1;
            while(listItr.hasNext()){
                Task current = listItr.next();
                if(task.getDueDate().isAfter(current.getDueDate())){
                    dueDateSortedList.add(currentIndex+1, task);
                    break;
                } else if(currentIndex == 0){
                    dueDateSortedList.offerFirst(task);
                }
                currentIndex--;
            }
        }
    }

    public LinkedList<Task> getSortedByDueDate() {
        return dueDateSortedList;
    }

    public SortedMap<String, ArrayList<Task>> getSortedByProject() {
        return projectSortedMap;
    }

    public void displayByDueDate(boolean ascending){
        Iterator<Task> itr = ascending ? dueDateSortedList.iterator() : dueDateSortedList.descendingIterator();
        while(itr.hasNext()){
            System.out.println(itr.next().toString());
        }
    }

    public void displayByProject(boolean ascending){
        NavigableSet<String> entries = ascending ? projectSortedMap.navigableKeySet() : projectSortedMap.descendingKeySet();
        for(String entry : entries){
            ArrayList<Task> tasksOfProject = projectSortedMap.get(entry);
            System.out.println("tasks of project: " + entry + " total: " + tasksOfProject.size());
            for (Task task : tasksOfProject) {
                System.out.println(task.toString());
            }
        }
    }



    public void displayTaskList() {
        System.out.println("Size of tree map: " + projectSortedMap.size());
        for (Map.Entry<String, ArrayList<Task>> entry : projectSortedMap.entrySet()) {
            ArrayList<Task> tasksOfProject = entry.getValue();
            System.out.println("tasks of project: " + entry.getKey() + " total: " + tasksOfProject.size());
            for (Task task : tasksOfProject) {
                System.out.println(task.toString());
            }
        }
    }
    // Methods : sortByDate(ascending/descending), sortByProjectFlag(ascending/descending)

    // Methods : saveToFile (as CSV/TSV), loadFromFile(from CSV/TSV)
}
