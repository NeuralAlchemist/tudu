package com.tudu.tasklist;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import org.fusesource.jansi.Ansi;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * {@inheritDoc} This implementation of the TaskListObject ensures that
 * only unique tasks are added into the list. The SortedTaskList contains
 * two collections, one which has all tasks sorted in the ascending order
 * of due dates and another which had all tasks sorted according to their
 * project names in the alphabetical order.
 */
public class SortedTaskList extends TaskListObject {

    /**
     * Represents the tasks stored in the SortedTaskList as a
     * key, value mapping. Each key is a unique project name
     * and all the tasks that have the key's project name are mapped
     * to that key.
     */
    protected TreeMap<String, ArrayList<Task>> projectSortedMap;
    protected LinkedList<Task> dueDateSortedList;
    private int size = 0;

    /**
     * Constructs empty instances of a TreeMap and a LinkedList
     * as the {projectSortedMap} and {dueDateSortedList}.
     */
    protected SortedTaskList() {
        projectSortedMap = new TreeMap<>();
        dueDateSortedList = new LinkedList<>();
    }

    /**
     * {@inheritDoc} This SortedTaskList performs sorting before adding a task to its
     * {projectSortedMap} and {dueDateSortedList}. Effectively, this method inserts
     * the constructed task in to this {projectSortedMap} and {dueDateSortedList}
     * according to an {@code ascending order} of tasks based on their project name
     * and due date respectively.
     * @param taskName The name to be set for the task; argument can be an empty string
     * @param dueDate The date by which this task should be done
     * @param status The current status of the task
     * @param project The project to which this task belongs
     * @return The added task is returned if the operation is successful, otherwise a
     * {@code null} task is returned.
     */
    @Override
    protected Task addTask(String taskName, LocalDateTime dueDate, TaskStatus status, String project) {
        Task result;
        Task task = new Task(taskName, dueDate, status, project);
        String projectName = task.getProject();
        boolean isAddedToProjectSortedMap = false;
        boolean isAddedToDueDateSortedList = false;
        boolean hasProjectAlready = projectSortedMap.containsKey(projectName);
        // If a project already exists as a key a new ArrayList need not be mapped to the project (key)
        ArrayList<Task> tasksOfProject = hasProjectAlready ? projectSortedMap.get(projectName) : new ArrayList<>(100);
        if (hasProjectAlready && tasksOfProject.contains(task)) {
            isAddedToProjectSortedMap = false;
        } else if (!tasksOfProject.contains(task)) {
            /* A TreeMap stores its keys in a natural order, so it is not
               necessary to specify the position of the key */
            tasksOfProject.add(task);
            projectSortedMap.put(projectName, tasksOfProject);
            isAddedToProjectSortedMap = true;
        }
        // If the list is empty add the task directly to it
        if (dueDateSortedList.isEmpty()) {
            dueDateSortedList.add(task);
            isAddedToDueDateSortedList = true;
        } else if (!dueDateSortedList.contains(task)) {
            /* Iterate through the list if it does not contain the task
               until, it reaches a task whose due date is before it's own
               due date. The iteration begins from the last element of the
               linked list to ensure that this algorithm works. Also, it is
               fairly okay to assume that new tasks that are usually added
               will all have a due date later than the latest due date task
               of the SortedTaskList
             */
            Iterator<Task> listItr = dueDateSortedList.descendingIterator();
            int currentIndex = dueDateSortedList.size() - 1;
            while (listItr.hasNext()) {
                Task current = listItr.next();
                if (task.getDueDate().isAfter(current.getDueDate())) {
                    dueDateSortedList.add(currentIndex + 1, task);
                    isAddedToDueDateSortedList = true;
                    break;
                } else if (currentIndex == 0) {
                    /* If there is no task that satisfies the above conditions
                       then the specified task should be added at the
                       very beginning of the linked list, as it is the one
                       with the due date furthest into the "past"
                     */
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
    protected void setTask(String taskName, LocalDateTime dueDate, TaskStatus status, String project, Task oldTask){
        if(contains(oldTask)){
            Task newTask = new Task(taskName, dueDate, status, project);
            if(!newTask.getName().equals(oldTask.getName())){
                oldTask.setName(newTask.getName());
            } else if ((newTask.getStatus().compareTo(oldTask.getStatus())) != 0) {
                oldTask.setStatus(newTask.getStatus());
            } else {
                removeTask(oldTask);
                addTask(newTask.getName(), newTask.getDueDate(), newTask.getStatus(), newTask.getProject());
            }
        } else {
            System.out.println("Task you are trying to set does not exist!");
            System.out.println("If this occurs check the method where the arguments are passed for this method");
        }
    }

    @Override
    protected Task markTaskAsDone(Task task){
        if(contains(task)){
            task.setStatus(TaskStatus.DONE);
            return task;
        } else {
            return null;
        }


    }

    /**
     * {@inheritDoc}This method also decrements the size of this
     * SortedTaskList.
     */
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

    /**
     * Finds if this SortedTaskList has any task that contains the
     * entire specified string.
     * @param taskName The name used to search for tasks
     * @return An ArrayList of Tasks that contain the entire
     * specified string as part of their name; if no task
     * is found then {@code null} is returned
     */
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

    // Returns the size/number of unique tasks in the SortedTaskList
    protected int getSize() {
        return size;
    }

    /**
     * Removes the duplicate of the specified task if present.
     * @param task The task whose duplicate is to be removed
     */
    protected void removeDuplicateTasks(Task task){
        int lastOccurrence = dueDateSortedList.lastIndexOf(task);
        int firstOccurence = dueDateSortedList.indexOf(task);
        /* If the last occurrence and first occurrence of the same task
        are not the same, this implies that there is at least
        two instances of the same task in the SortedTaskList.
        */
        if(firstOccurence != lastOccurrence){
            removeTask(task);
            System.out.println("The updated task exists twice in the tasklist.\n" +
                    "Removed one of them.");
        }
    }

    /**
     * Returns {code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list
     * contains at least one element {code e} such that
     * {@code (o==null ? e==null : o.equals(e))}.
     * @param task The task whose presence in the SortedTaskList is
     *             to be tested
     * @return {@code true} if this SortedTaskList contains the task;
     *  {@code false} otherwise
     */
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
            /* The following lines ensure that the database is in a
               newline delimited form. As newlines are the only type
               of invalid inputs, they cannot be part of the task's
               fields. */
            while (itr.hasNext()) {
                current = itr.next();
                writer.println(current.getName());
                writer.println(current.getDueDate());
                writer.println(current.getStatus());
                writer.println(current.getProject());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot save to file, as file not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Searches for a file at the specified path. It also loads a
     * simple searching message before it performs the operation.
     * @param stringPathToDatabase The path to the local database
     * @return {@code true} if the file exists; {@code false}
     * otherwise
     */
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
        /* Jansi code to erase everything behind the current cursor
          and set cursor to the beginning of the current line */
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
            /* Jansi code to move the cursor left one space
               and make the code print again at the same point
               achieving an animation effect */
            System.out.print(ansi().cursorLeft(1));
        }
        System.out.println("");
    }

    /**
     * Loads tasks from a local newline delimited text database to this SortedTaskList.
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
                            /* Due date is stored as LocalDateTime's ISO_DATE_TIME format
                               it needs to be parsed to LocalDateTime object to be added */
                            LocalDateTime.parse(content.get(i + 1), formatter),
                            /* Status is stored as the String value of the enum and it
                               needs to be parsed appropriately before being added */
                            TaskStatus.valueOf(content.get(i + 2)),
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
