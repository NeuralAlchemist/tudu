package com.tudu.task;

import java.util.*;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private TreeMap<String, ArrayList<Task>> projectSortedMap;
    private LinkedList<Task> dueDateSortedList;

    protected TaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    // Methods : ~~addTask~~, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    protected void addTask(Task task) {
        String projectName = task.getProject();
        Boolean projectExistsAlready = projectSortedMap.containsKey(projectName);
        ArrayList<Task> tasksOfProject = projectExistsAlready ? projectSortedMap.get(projectName) : new ArrayList<>(100);
        if (projectExistsAlready && tasksOfProject.contains(task)) {
        } else if (!tasksOfProject.contains(task)) {
            // Improve adding to sort it out by dueDate
            tasksOfProject.add(task);
            projectSortedMap.put(projectName, tasksOfProject);
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

    protected LinkedList<Task> getSortedByDueDate() {
        return dueDateSortedList;
    }

    protected SortedMap<String, ArrayList<Task>> getSortedByProject() {
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

    protected boolean findTask(Task task){
        return dueDateSortedList.contains(task);
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
