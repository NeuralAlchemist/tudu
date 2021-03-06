package com.tudu.task;

import java.util.*;

public class TaskList {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    private SortedMap<String, ArrayList<Task>> projectSortedMap;
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
            for (int i = dueDateSortedList.size() - 1; i >= 0; i--) {
                Task current = dueDateSortedList.get(i);
                if (task.getDueDate().isAfter(current.getDueDate())) {
                    dueDateSortedList.add(i + 1, task);
                    break;
                } else if (i == 0) {
                    dueDateSortedList.offerFirst(task);
                }
            }
        }
    }

    public LinkedList<Task> getSortedByDueDate() {
        return dueDateSortedList;
    }

    public SortedMap<String, ArrayList<Task>> getSortedByProject() {
        return projectSortedMap;
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
    // Methods : sortByDate(ascending/descending), sortByProjectFlag(ascending/descending), displayTaskList,

    // Methods : saveToFile (as CSV/TSV), loadFromFile(from CSV/TSV)
}
