package com.tudu.tasklist;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import com.tudu.tasklist.TaskListObject;
import org.fusesource.jansi.Ansi;

import java.io.*;
import java.nio.file.Files;
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
    private int size = 0;

    protected SortedTaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    @Override
    protected Task addTask(String taskName, LocalDateTime dueDate, int status, String project) {
        Task result;
        Task task = new Task(taskName, dueDate, status, project);
        String projectName = task.getProject();
        boolean isAddedToProjectSortedMap = false;
        boolean isAddedToDueDateSortedList = false;
        boolean hasProjectAlready = projectSortedMap.containsKey(projectName);
        ArrayList<Task> tasksOfProject = hasProjectAlready ? projectSortedMap.get(projectName) : new ArrayList<>(100);
        if (hasProjectAlready && tasksOfProject.contains(task)) {
            isAddedToProjectSortedMap = false;
        } else if (!tasksOfProject.contains(task)) {
            tasksOfProject.add(task);
            projectSortedMap.put(projectName, tasksOfProject);
            isAddedToProjectSortedMap = true;
        }

        if (dueDateSortedList.isEmpty()) {
            dueDateSortedList.add(task);
            isAddedToDueDateSortedList = true;
        } else if (!dueDateSortedList.contains(task)) {
            Iterator<Task> listItr = dueDateSortedList.descendingIterator();
            int currentIndex = dueDateSortedList.size() - 1;
            while (listItr.hasNext()) {
                Task current = listItr.next();
                if (task.getDueDate().isAfter(current.getDueDate())) {
                    dueDateSortedList.add(currentIndex + 1, task);
                    isAddedToDueDateSortedList = true;
                    break;
                } else if (currentIndex == 0) {
                    dueDateSortedList.offerFirst(task);
                    isAddedToDueDateSortedList = true;
                }
                currentIndex--;
            }
        } else {
            isAddedToDueDateSortedList = false;
        }
        if (isAddedToDueDateSortedList && isAddedToProjectSortedMap) {
            size++;
            result = task;
        } else {
            result = null;
        }
        return result;
    }

    @Override
    protected void setTask(String taskName, LocalDateTime dueDate, int status, String project, Task oldTask){
        if(contains(oldTask)){
            Task newTask = new Task(taskName, dueDate, status, project);
            if(!newTask.getName().equals(oldTask.getName())){
                oldTask.setName(newTask.getName());
            } else if ((newTask.getStatus().compareTo(oldTask.getStatus())) != 0) {
                oldTask.setStatus(newTask.getStatus());
            } else {
                removeTask(oldTask);
                addTask(newTask.getName(), newTask.getDueDate(), newTask.getStatus().ordinal()+1, newTask.getProject());
            }
        } else {
            System.out.println("Task you are trying to set does not exist!");
            System.out.println("If this occurs check the method where the arguments are passed for this method");
        }
    }

    @Override
    protected Task markTaskAsDone(Task task){
        task.setStatus(TaskStatus.DONE);
        return task;
    }


    @Override
    protected boolean removeTask(Task task){
        boolean isRemoved;
        if(contains(task)){
            isRemoved = projectSortedMap.get(task.getProject()).remove(task) && dueDateSortedList.remove(task);
            System.out.println(isRemoved);
            size = isRemoved ? size-- : size;
        }else {
            System.out.println("Does not find the task!");
            isRemoved = false;
        }
        return isRemoved;
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

    protected int getSize() {
        return size;
    }

    protected void removeDuplicateTasks(Task task){
        int lastOccurrence = dueDateSortedList.lastIndexOf(task);
        int firstOccurence = dueDateSortedList.indexOf(task);
        System.out.println(firstOccurence);
        System.out.println(lastOccurrence);
        if(firstOccurence != lastOccurrence){
            removeTask(task);
            System.out.println("The updated task exists twice in the tasklist.\n" +
                    "Removed one of them.");
        }
    }

    protected boolean contains(Task task){
        ArrayList<Task> tasksOfProject =  projectSortedMap.get(task.getProject()) ;
        if(tasksOfProject == null){
            return false;
        }else{
            return (tasksOfProject.contains(task) && dueDateSortedList.contains(task));
        }
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

    @Override
    protected boolean loadTaskListFromFile(String stringPathToDatabase) {
        boolean hasLoadedFromDatabase;
        if (searchForDatabaseFile(stringPathToDatabase)) {
            hasLoadedFromDatabase = loadTaskList(stringPathToDatabase);
        } else {
            // Display database unavailable! Let's start fresh!
            hasLoadedFromDatabase = false;
        }
        return hasLoadedFromDatabase;
    }

    // A simple fancy loading message
    protected void printLoadingMessage() {
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
    protected boolean loadTaskList(String stringPathToDatabase) {
        boolean hasLoadedFromDatabase = true;
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
                    hasLoadedFromDatabase = hasLoadedFromDatabase & (current != null);
                }
            } catch (IOException e) {
                hasLoadedFromDatabase = false;
            }
        }
        return hasLoadedFromDatabase;
    }
}
