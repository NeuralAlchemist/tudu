package com.tudu.tasklist;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import org.fusesource.jansi.Ansi;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * This implementation of the {@link com.tudu.tasklist.SortedTaskList} adds TUI functionality
 * on top of the parent class behaviour. This method is effectively the Tudu application.
 * The only public method is used to begin the application.
 */
public class SortedTaskListUI extends SortedTaskList {
    private final static int LOWEST_POSSIBLE_OPTION = 1;
    private final static String DATE_FORMAT = "yy-MM-dd HH:mm";
    private final static int DEFAULT_NUMBER_OPTION = 26;
    private final static String DEFAULT_STRING_OPTION = "n";
    private static String stringPathToDatabase = "tudu-database.txt";
    private static TaskStatus[] taskStatuses = TaskStatus.values();
    private BufferedReader bufferedReader;
    private final static String STATUS_QUESTION = "Choose one of the following options about your task status[1-3]" +
            "\n1 -> I'm currently doing it" +
            "\n2 -> I have finished it" +
            "\n3 -> I have not begun it yet";
    private final String START_MENU_MESSAGE = "You can: ";
    private final String CHOOSE_MESSAGE = "Select an option in range ";
    private final int VIEW_TASK = 1;
    private final int ADD_TASK = 2;
    private final int EDIT_TASK = 3;
    private final int SAVE_QUIT = 4;
    private final int SHOW_ALL_PROJECTS = 1;
    private final int SHOW_ALL_DUEDATE = 2;
    private final int SEARCH_BY_WORD = 3;
    private final int QUIT_EDIT = 4;
    private final int CHOOSE_REMOVE = 2;
    private final int EDIT_DUEDATE = 2;
    private final int EDIT_STATUS = 3;
    private final int REMOVE_TASK = 5;

    // Display Main Menu
    private void displayMainMenu() {
        System.out.println(START_MENU_MESSAGE);
        System.out.println("1 -> View all tasks (sort by project/due date)");
        System.out.println("2 -> Add a new task");
        System.out.println("3 -> Modify a task (includes changing task fields, removing a task, marking a task as done)");
        System.out.println("4 -> Save and quit");
    }

    // Begin the application and read for user input continuosly until exit is chosen
    public int startApplication(InputStream in) {
        int exitStatus = -1;
        bufferedReader = new BufferedReader(new InputStreamReader(in));
        // Jansi code to clear the terminal screen and go the top left corner to begin printing
        System.out.println(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1, 1));
        System.out.println("Get organizing with TuDu! \n");
        boolean isRunning = true;
        loadTaskListFromFile(stringPathToDatabase);
        while (isRunning) {
            displayMainMenu();
            int input = doWhileConditionIsFalse(CHOOSE_MESSAGE+"[1-4]:", 4);
            switch (input) {
                case VIEW_TASK:
                    viewTaskMenu();
                    break;
                case ADD_TASK:
                    addTaskMenu();
                    break;
                case EDIT_TASK:
                    editTaskMenu();
                    break;
                case SAVE_QUIT:
                    saveTaskListToFile(stringPathToDatabase);
                    // Exit is chosen, set boolean to break the while loop
                    isRunning = false;
                    exitStatus = 0;
                    break;
                default:
                    System.out.println("Invalid input! Please type a number in the range [1-4]");
            }
        }
        return exitStatus;
    }

    /**
     * Displays the add menu and guides the user for inputs for a task's
     * fields.
     */
    private void addTaskMenu() {
        boolean isAddingTask = true;
        while (isAddingTask) {
            //Change what questions to enter
            String taskName = promptOpenAnswer("Enter name of task: ");
            LocalDateTime dueDate = getValidDate();
            int status = doWhileConditionIsFalse(STATUS_QUESTION, 3);
            String project = promptOpenAnswer("What type of project is this task?");
            Task added = this.addTask(taskName, dueDate, taskStatuses[status-1], project);
            System.out.println("Following task was added:\n"+added);
            isAddingTask = promptBooleanAnswer("Add more tasks?(y/n)");
        }
    }

    /**
     * Displays the view menu and guides the user for an input to display
     * tasks according to ascending/ descending order of project or due date
     */
    private void viewTaskMenu() {
        boolean isViewingTasks = true;
        while (isViewingTasks) {
            int displayOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                    "1 -> Display all tasks by project\n" +
                    "2 -> Display all tasks by due date\n"+
                    CHOOSE_MESSAGE+"[1-2]:", 2);
            boolean isAscending = promptBooleanAnswer("Display tasks in ascending order?(y/n)\nOBS! No will imply descending order");
            if (displayOption == SHOW_ALL_PROJECTS) {
                viewTaskByProject(isAscending, false);
            } else {
                viewTaskByDueDate(isAscending, false);
            }
            isViewingTasks = promptBooleanAnswer("Continue viewing tasks?(y/n)");
        }
    }

    // A simple method to display the projectSortedMap with a index number for each task
    protected void viewTaskByProject(boolean isAscending, boolean canShowTaskNumber) {
        NavigableSet<String> navigableSet = isAscending ? projectSortedMap.navigableKeySet() : projectSortedMap.descendingKeySet();
        int i = 1;
        for (String entry : navigableSet) {
            ArrayList<Task> tasksOfProject = projectSortedMap.get(entry);
            System.out.println("tasks of project: " + entry + " total: " + tasksOfProject.size());
            for (Task task : tasksOfProject) {
                if (canShowTaskNumber) {
                    System.out.print((i++) + " -> ");
                }
                System.out.println(task.toString());
            }
            System.out.println();
        }
        if(projectSortedMap.isEmpty()){
            System.out.println("Your tasklist is empty!");
        }
    }

    // A simple method to display the dueDateSortedList with a index number for each task
    protected void viewTaskByDueDate(boolean isAscending, boolean canShowTaskNumber) {
        Iterator<Task> itr = isAscending ? dueDateSortedList.iterator() : dueDateSortedList.descendingIterator();
        int i = 1;
        while (itr.hasNext()) {
            if (canShowTaskNumber) {
                System.out.print((i++) + " -> ");
            }
            System.out.println(itr.next().toString());
        }
        if(dueDateSortedList.isEmpty()){
            System.out.println("Your tasklist is empty!");
        }
    }

    // A simple method to display an ArrayList of Tasks with a index number
    private void viewArrayListOfTask(ArrayList<Task> listOfTasks) {
        AtomicInteger i = new AtomicInteger();
        listOfTasks.stream().forEach(task -> System.out.println((i.getAndIncrement() + 1) + " -> " + task));
    }

    /**
     * This method fetches a task that was displayed to be at a
     * particular index in the {@code projectSortedMap} when displayed
     * by the {@link #viewTaskByProject(boolean, boolean)}
     * @param index The index at which the task to be returned was displayed
     * @return The task which was displayed at the specified index
     */
    protected Task getTaskFromDisplayedProjectSortedMap(int index) {
        int i = 1;
        Task result = null;
        outerloop:
        for (Map.Entry<String, ArrayList<Task>> project : projectSortedMap.entrySet()) {
            for (Task task : project.getValue()) {
                if (i == index) {
                    result = task;
                    break outerloop;
                }
                i++;
            }
        }
        return result;
    }

    /* This method guides the user through the different options available to use
       to edit a task that is already present */
    private void editTaskMenu() {
        boolean isEditingTask = true;
        // check tasklist is not zero before doing this
        editaskloop:
        while (isEditingTask) {
            Task chosenTask = null;
            // First the choice of editing/removing or marking as done is asked
            int editOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                    "1 -> edit a task's fields/ remove a task\n" +
                    "2 -> mark a task as done\n"+CHOOSE_MESSAGE+"[1-2]:", 2);
            /* Then the different ways to find the task to be edited is presented
               User can choose to quit editing mode at this point
             */
            int searchOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                    "1 -> Show all tasks by project\n" +
                    "2 -> Show all tasks by due date\n" +
                    "3 -> Search using task name\n" +
                    "4 -> Quit editing\n" +
                    CHOOSE_MESSAGE+" [1-4]:", 4);
            /* Depending on the option chosen to search for the task to be edited
               different paths are taken, but they all end up with finding the
               task to be edited. Or is quit is chosen, the editaskloop is broken
             */
            switch (searchOption) {
                case SHOW_ALL_PROJECTS:
                    viewTaskByProject(true, true);
                    int chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + getSize() + "]", getSize()); // or quit?
                    chosenTask = getTaskFromDisplayedProjectSortedMap(chosenTaskIndex);
                    break;
                case SHOW_ALL_DUEDATE:
                    viewTaskByDueDate(true, true);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + getSize() + "]", getSize()); // or quit?
                    chosenTask = dueDateSortedList.get(chosenTaskIndex - 1);
                    break;
                case SEARCH_BY_WORD:
                    ArrayList<Task> listOfTasks = getPossibleListOfTasks();
                    if (listOfTasks == null || listOfTasks.isEmpty()) {
                        continue;
                    }
                    viewArrayListOfTask(listOfTasks);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + (listOfTasks.size()) + "]", listOfTasks.size()); // or quit?
                    chosenTask = listOfTasks.get(chosenTaskIndex - 1);
                    break;
                case QUIT_EDIT:
                    isEditingTask = false;
                    break editaskloop;
            }
            boolean isEditingChosenTask = true;
            /* If the user chose the selected task to be marked done, the operation
               is performed and the edit menu/mode is exited to the main menu */
            if(editOption == CHOOSE_REMOVE){
                System.out.println("Following task is marked done:\n" +markTaskAsDone(chosenTask).toString());
                removeDuplicateTasks(chosenTask);
                isEditingChosenTask = false;
                isEditingTask = false;
                break;
            }
            // If user chose to remove or edit a task the following loop is entered
            while (isEditingTask && isEditingChosenTask) {
                System.out.println("Task chosen for editing is:\n" + chosenTask.toString());
                int fieldToEdit = doWhileConditionIsFalse(START_MENU_MESSAGE +
                        "\n1 -> Edit name" +
                        "\n2 -> Edit due date" +
                        "\n3 -> Edit status" +
                        "\n4 -> Edit project" +
                        "\n5 -> Remove whole task\n" +
                        CHOOSE_MESSAGE+" [1-5]:", 5);
                /* If the user chooses to remove a task, the action is completed
                  and the user is prompted to either to continue or stop editing any more tasks */
                if(fieldToEdit == REMOVE_TASK){
                    removeTask(chosenTask);
                    System.out.println("Task was removed!");
                    isEditingChosenTask = false;
                    break;
                }else {
                    // If a task's field was chosen to be edited, the following loop occurs
                    TaskStatus newStatus;
                    String newValue = null;
                    LocalDateTime newTime;
                    // If status has to be changed it should be within certain limits otherwise it is in invalid
                    if (fieldToEdit == EDIT_STATUS) {
                        newStatus = taskStatuses[doWhileConditionIsFalse(STATUS_QUESTION, 3)-1];
                        newTime = chosenTask.getDueDate();
                    } // Similarly,if due date has to be changed it should be validated
                    else if (fieldToEdit == EDIT_DUEDATE) {
                        newTime = getValidDate();
                        newStatus = chosenTask.getStatus();
                    } else {
                        // Otherwise, the due date and status are copied from the task chosen for editing
                        newStatus = chosenTask.getStatus();
                        newTime = chosenTask.getDueDate();
                        // Other fields have no such limitation and all input is valid
                        newValue = promptOpenAnswer("Enter new value: ");
                    }
                    String newName = fieldToEdit == 1 ? newValue : chosenTask.getName();
                    String newProject = fieldToEdit == 4 ? newValue : chosenTask.getProject();
                    System.out.println(newStatus);
                    setTask(newName, newTime, newStatus, newProject, chosenTask);
                    System.out.println("Following task has been edited:\n"+chosenTask.toString());
                    /* After a task has been edited, it could be duplicate to another task
                       so the following method is called to remove any duplicates */
                    removeDuplicateTasks(chosenTask);
                    isEditingChosenTask = promptBooleanAnswer("Continue editing other fields of this task?(y/n)");
                }
            }
            isEditingTask = promptBooleanAnswer("Continue editing tasks?(y/n)");
        }
    }


    /**
     * Asks the user for a search phrase and calls the {@link #findTaskByName(String)}
     * method. If the method returns an empty ArrayList, the user is presented with
     * options to continue the search again or to go back to the edit menu.
     * @return an ArrayList of all tasks that contain the complete search phrase.
     */
    private ArrayList<Task> getPossibleListOfTasks() {
        boolean isSearchingWithTaskName;
        ArrayList<Task> possibleTasks;
        do {
            String searchTerm = promptOpenAnswer("Enter a word/letter you remember from the task's name");
            possibleTasks = this.findTaskByName(searchTerm);
            if (possibleTasks == null) {
                isSearchingWithTaskName = promptBooleanAnswer("Looks like no task contains the word you gave!" +
                        "\nDo you want to search with another word?(y/n)" +
                        " \nIf not you will be taken to the edit sub-menu.");
            } else {
                isSearchingWithTaskName = false;
            }
        } while (isSearchingWithTaskName);
        return possibleTasks;
    }

    /**
     * Asks the user for a valid date until one is provided.
     * A valid date needs to be in the format "yyyy-MM-dd" and it should
     * exist in the ISO-8601 calendar system/proleptic Gregorian calendar
     * system. This is not suitable for historical dates.
     * @return The inputted date in a LocalDateTime format
     */
    private LocalDateTime getValidDate() {
        LocalDateTime result;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        String inputDate;
        do {
            inputDate = promptOpenAnswer("What is the due date of the task? (enter in " + DATE_FORMAT + ")");
            try {
                Date example = format.parse(inputDate);
                result = example.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            } catch (ParseException e) {
                result = null;
            }
            if (result == null) {
                System.out.println("The chosen date does not exist (OR) please input a valid date in the format" + DATE_FORMAT);
            }
        } while (result == null);
        return result;
    }

    /**
     * Continues to prompt and wait for user input.
     * The user input can be anything even empty.
     * @param prompt The statement to be made to guide the user for input
     * @return The user input in string format
     */
    private String promptOpenAnswer(String prompt) {
        String result;
        System.out.println(prompt);
        try {
            result = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * Continues to prompt and wait for boolean user input.
     * The required input for this method to end is always a "yes" or a
     * "no". More specifically, it will run untill a string beginning
     * with "y" or "n" is inputted.
     * @param prompt The statement to be made to guide the user for input
     * @return The boolean equivalent of the answer inputted by the user
     */
    private boolean promptBooleanAnswer(String prompt) {
        boolean isTrue;
        String input = null;
        boolean hasTrueOrFalse = false;
        do {
            System.out.println(prompt);
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = input.isEmpty() ? DEFAULT_STRING_OPTION : input; //DEFAULT
            if (input.substring(0, 1).equalsIgnoreCase("y")) {
                isTrue = true;
                hasTrueOrFalse = true;
            } else if (input.substring(0, 1).equalsIgnoreCase("n")) {
                isTrue = false;
                hasTrueOrFalse = true;
            } else {
                System.out.println("Invalid input! Please input yes/no.");
                isTrue = false;
            }
        } while (!hasTrueOrFalse);
        return isTrue;
    }

    /**
     * Continues to prompt and wait for user input of an integer
     * that is in the inclusive range of the specified {@code LOWEST_POSSIBLE_OPTION} and upper limit.
     * @param prompt The statement to be made to guide the user for input
     * @param upperLimit The highest number which the input can be
     * @return The integer inputted by the user
     */
    private int doWhileConditionIsFalse(String prompt, int upperLimit) {
        boolean isWithinLimits;
        int status;
        do {
            try {
                status = Integer.parseInt(promptOpenAnswer(prompt));
                if (status < LOWEST_POSSIBLE_OPTION || status > upperLimit) {
                    System.out.println("Invalid option was chosen, please select a valid option");
                    isWithinLimits = false;
                } else {
                    isWithinLimits = true;
                }
            } catch (NumberFormatException e) {
                /* If the given input is not a number then the parseInt throws
                   a number format exception which can be caught and used to
                   keep this loop running
                 */
                System.out.println("Invalid option was chosen, please select a valid option");
                isWithinLimits = false;
                status = 0;
            }
        } while (!isWithinLimits);
        return status;
    }


}
