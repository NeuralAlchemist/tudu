package com.tudu.task;

import org.fusesource.jansi.Ansi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class SortedTaskList extends TaskListObject {
    // Probably a set Collection of Tasks is TaskList
    // Private fields
    protected TreeMap<String, ArrayList<Task>> projectSortedMap;
    protected LinkedList<Task> dueDateSortedList;
    private int numberOfTasks = 0;


    protected SortedTaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    // Methods : ~~addTask~~, editTask, markAsDone, removeTask(IF: support removeAll, IF: support removeAllProjectFlag)
    // update to check for already present tasks and return false if not added
    @Override
    protected Task addTask(String taskName, LocalDateTime dueDate, int status, String project) {
        Task result;
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
        } else if (!dueDateSortedList.contains(task)) {
            Iterator<Task> listItr = dueDateSortedList.descendingIterator();
            int currentIndex = dueDateSortedList.size() - 1;
            while (listItr.hasNext()) {
                Task current = listItr.next();
                if (task.getDueDate().isAfter(current.getDueDate())) {
                    dueDateSortedList.add(currentIndex + 1, task);
                    addedToDueDateSortedList = true;
                    break;
                } else if (currentIndex == 0) {
                    dueDateSortedList.offerFirst(task);
                    addedToDueDateSortedList = true;
                }
                currentIndex--;
            }
        } else {
            addedToDueDateSortedList = false;
        }
        if (addedToDueDateSortedList && addedToProjectSortedMap) {
            numberOfTasks++;
            // Remove this line -> make UI use the return value to print
            System.out.println("Task has been added");
            result = task;
        } else {
            result = null;
        }
        return result;
    }

    private int getProjectSortedMapIndex(Task task){

        return projectSortedMap.get(task.getProject()).indexOf(task);
    }

    private int getDueDateSortedListIndex(Task task){
        return dueDateSortedList.indexOf(task);
    }

    // Return the element that was set
    protected void setTaskInTaskList(String taskName, LocalDateTime dueDate, int status, String project, Task oldTask){
        Task newTask = new Task(taskName, dueDate, status, project);
        if(!newTask.getName().equals(oldTask.getName())){
            oldTask.setName(newTask.getName());
        } else if ((newTask.getStatus().compareTo(oldTask.getStatus())) != 0) {
            oldTask.setStatus(newTask.getStatus());
        } else {
            removeTaskInTaskList(oldTask);
            addTask(newTask.getName(), newTask.getDueDate(), newTask.getStatus().ordinal()+1, newTask.getProject());
        }
    }


    protected boolean removeTaskInTaskList(Task task){
        return projectSortedMap.get(task.getProject()).remove(task) && dueDateSortedList.remove(task);
    }

    // Remove these
    protected LinkedList<Task> getSortedByDueDate() {
        return dueDateSortedList;
    }
    // Remove these
    protected TreeMap<String, ArrayList<Task>> getSortedByProject() {
        return projectSortedMap;
    }



    @Override
    protected LinkedList<Task> displayByDueDate(boolean ascending) {
        LinkedList<Task> result = dueDateSortedList;
        if(!ascending){
            Collections.reverse(result);
        }
        return result;
        /*Iterator<Task> itr = ascending ? dueDateSortedList.iterator() : dueDateSortedList.descendingIterator();
        while (itr.hasNext()) {
            System.out.println(itr.next().toString());
        }*/
    }

    @Override
    protected NavigableSet<String> displayByProject(boolean ascending) {

        NavigableSet<String> entries = ascending ? projectSortedMap.navigableKeySet() : projectSortedMap.descendingKeySet();
        return entries;
        /*for (String entry : entries) {
            ArrayList<Task> tasksOfProject = projectSortedMap.get(entry);
            System.out.println("tasks of project: " + entry + " total: " + tasksOfProject.size());
            for (Task task : tasksOfProject) {
                System.out.println(task.toString());
            }
        }*/
    }

    protected ArrayList<Task> findTaskByName(String taskName) {
        ArrayList<Task> listOfTasksFound = new ArrayList<>(10);
        Iterator<Task> itr = dueDateSortedList.iterator();
        Task current;
        while (itr.hasNext()) {
            current = itr.next();
            if (current.getName().toLowerCase().contains(taskName.toLowerCase())){
                listOfTasksFound.add(current);
            }
        }
        return listOfTasksFound.isEmpty() ? null : listOfTasksFound;
    }

    protected int getNumberOfTasks() {
        return numberOfTasks;
    }

    @Override
    protected void saveTaskListToFile(String stringPathToDatabase) {
        File databaseFile = new File(stringPathToDatabase);
        PrintWriter writer;
        try {
            // Creates a new file if it does not exist
            databaseFile.createNewFile();
            // Clear the file before writing data into
            new PrintWriter(databaseFile).close();
            writer = new PrintWriter(new FileOutputStream(databaseFile, true));
            Iterator<Task> itr = dueDateSortedList.iterator();
            Task current;
            while (itr.hasNext()) {
                current = itr.next();
                writer.println(current.getName());
                writer.println(current.getDueDate());
                writer.println(current.getStatus().ordinal());
                writer.println(current.getProject());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot save to file, as file not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected boolean searchForDatabaseFile(String stringPathToDatabase) {
        System.out.print("Searching for database ..");
        // Jansi code to blink the string "." slowly
        System.out.print(ansi().a(Ansi.Attribute.BLINK_SLOW).a("."));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Jansi code to clear/reset the attribute of blinking
        System.out.print(ansi().reset());
        // Jansi code to erase everything behind the current cursor
        // and set cursor to the beginning of the current line
        System.out.print(ansi().eraseLine(Ansi.Erase.BACKWARD).cursorToColumn(1));
        System.out.println("Searching for database ...");
        return new File(stringPathToDatabase).exists();
    }

    protected boolean loadTaskList(String stringPathToDatabase) {
        boolean result;
        if (searchForDatabaseFile(stringPathToDatabase)) {
            result = loadTaskListFromFile(stringPathToDatabase);
        } else {
            // Display database unavailable! Let's start fresh!
            result = false;
        }
        return result;
    }

    protected void printLoadingMessage() {
        // Fancy loading message
        String[] loadingPattern = {"-", "\\", "|", "/", "-"};
        System.out.print("Loading tasks ");
        System.out.print("/");
        for (int i = 0; i < loadingPattern.length; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(loadingPattern[i % (loadingPattern.length)]);
            System.out.print(ansi().cursorLeft(1));
        }
        System.out.println("");
    }


    /**
     * Loads tasklist details from a local text database to this SortedTaskList.
     * @param stringPathToDatabase is the relative path to the local database
     * @return Returns {@code true} if all the tasks have been loaded successfully;
     *          {@code false} otherwise
     * */
    protected boolean loadTaskListFromFile(String stringPathToDatabase) {
        boolean result = true;
        File databaseFile = new File(stringPathToDatabase);
        Path pathToDatabase = Paths.get(stringPathToDatabase);
        ArrayList<String> content;
        // Load from file only if it contains data
        if (databaseFile.length() > -1) {
            printLoadingMessage();
            try {
                content = new ArrayList<>(Files.readAllLines(pathToDatabase));
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                Task current;
                for (int i = 0; i < content.size(); i = i + 4) {
                    current = addTask(content.get(i),
                            // Due date is stored as LocalDateTime's ISO_DATE_TIME format
                            // it needs to be parsed to LocalDateTime object to be added
                            LocalDateTime.parse(content.get(i + 1), formatter),
                            Integer.parseInt(content.get(i + 2)) + 1,
                            content.get(i + 3));
                    result = result & (current != null);
                }
            } catch (IOException e) {
                result = false;
            }
        }
        return result;
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
