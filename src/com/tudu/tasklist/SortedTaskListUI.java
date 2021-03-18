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

public class SortedTaskListUI extends SortedTaskList {
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
    // Class to display TaskList and read user input from terminal
    // Must be an infinite loop until "Save and Quit" option is chosen
    // Private or local fields
    // Methods : readInput, printMenu
    private void displayMainMenu() {
        System.out.println(START_MENU_MESSAGE);
        System.out.println("1 -> View all tasks (sort by project/due date)");
        System.out.println("2 -> Add a new task");
        System.out.println("3 -> Modify a task (includes changing task fields, removing a task, marking a task as done)");
        System.out.println("4 -> Save and quit");
    }

    public int startApplication(InputStream in) {
        int exitStatus = -1;
        bufferedReader = new BufferedReader(new InputStreamReader(in));
        System.out.println(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1, 1));
        System.out.println("Get organizing with TuDu! \n");
        boolean isRunning = true;
        loadTaskListFromFile(stringPathToDatabase);
        while (isRunning) {
            displayMainMenu();
            int input = doWhileConditionIsFalse(CHOOSE_MESSAGE+"[1-4]:", 1, 4);
            switch (input) {
                case 1:
                    viewTaskMenu();
                    break;
                case 2:
                    addTaskMenu();
                    break;
                case 3:
                    editTaskMenu();
                    break;
                case 4:
                    saveTaskListToFile(stringPathToDatabase);
                    isRunning = false;
                    exitStatus = 0;
                    break;
                default:
                    System.out.println("Invalid input! Please type a number in the range [1-4]");
            }
        }
        return exitStatus;
    }

    private void addTaskMenu() {
        boolean isAddingTask = true;
        while (isAddingTask) {
            //Change what questions to enter
            String taskName = promptOpenAnswer("Enter name of task: ");
            LocalDateTime dueDate = getValidDate();
            int status = doWhileConditionIsFalse(STATUS_QUESTION, 1, 3);
            String project = promptOpenAnswer("What type of project is this task?");
            Task added = this.addTask(taskName, dueDate, taskStatuses[status-1], project);
            System.out.println("Following task was added:\n"+added);
            isAddingTask = promptBooleanAnswer("Add more tasks?(y/n)");
        }
    }

    private void viewTaskMenu() {
        boolean isViewingTasks = true;
        while (isViewingTasks) {
            int displayOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n1 -> Display all tasks by project\n" +
                    "2 -> Display all tasks by due date\n"+CHOOSE_MESSAGE+"[1-2]:", 1, 2);
            boolean isAscending = promptBooleanAnswer("Display tasks in ascending order?(y/n)\nOBS! No will imply descending order");
            if (displayOption == 1) {
                viewTaskByProject(isAscending, false);
            } else {
                viewTaskByDueDate(isAscending, false);
            }
            isViewingTasks = promptBooleanAnswer("Continue viewing tasks?(y/n)");
        }
    }

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

    protected void viewArrayListOfTask(ArrayList<Task> listOfTasks) {
        AtomicInteger i = new AtomicInteger();
        listOfTasks.stream().forEach(task -> System.out.println((i.getAndIncrement() + 1) + " -> " + task));
    }

    protected Task getTaskFromProjectSortedMap(int index) {
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

    private void editTaskMenu() {
        boolean isEditingTask = true;
        // check tasklist is not zero before doing this
        editaskloop:
        while (isEditingTask) {
            Task chosenTask = null;
            int editOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                    "1 -> edit a task's fields/ remove a task\n" +
                    "2 -> mark a task as done\n"+CHOOSE_MESSAGE+"[1-2]:", 1, 2);
            int searchOption = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                    "1 -> Show all tasks by project\n" +
                    "2 -> Show all tasks by due date\n" +
                    "3 -> Search using task name\n" +
                    "4 -> Quit editing\n" +
                    CHOOSE_MESSAGE+" [1-4]:", 1, 4);
            //Add option to escape from edit here
            switch (searchOption) {
                case 1:
                    viewTaskByProject(true, true);
                    int chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + getSize() + "]", 1, getSize()); // or quit?
                    chosenTask = getTaskFromProjectSortedMap(chosenTaskIndex);
                    break;
                case 2:
                    viewTaskByDueDate(true, true);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + getSize() + "]", 1, getSize()); // or quit?
                    chosenTask = dueDateSortedList.get(chosenTaskIndex - 1);
                    break;
                case 3:
                    ArrayList<Task> listOfTasks = getPossibleListOfTasks();
                    if (listOfTasks == null || listOfTasks.isEmpty()) {
                        continue;
                    }
                    viewArrayListOfTask(listOfTasks);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit/mark/remove[1-"
                            + (listOfTasks.size()) + "]", 1, listOfTasks.size()); // or quit?
                    chosenTask = listOfTasks.get(chosenTaskIndex - 1);
                    break;
                case 4:
                    isEditingTask = false;
                    break editaskloop;
            }
            boolean isEditingChosenTask = true;
            if(editOption == 2){
                System.out.println("Following task is marked done:\n" +markTaskAsDone(chosenTask).toString());
                removeDuplicateTasks(chosenTask);
                isEditingChosenTask = false;
                isEditingTask = false;
                break;
            }
            while (isEditingTask && isEditingChosenTask) {
                System.out.println("Task chosen for editing is:\n" + chosenTask.toString());
                int fieldToEdit = doWhileConditionIsFalse(START_MENU_MESSAGE+"\n" +
                        "\n1 ->Edit name\n2 ->Edit due date\n3 ->Edit status\n4 ->Edit project\n5 -> Remove whole task" +
                        CHOOSE_MESSAGE+" [1-5]:", 1, 5);
                if(fieldToEdit == 5){
                    removeTask(chosenTask);
                    System.out.println("Task was removed!");
                    isEditingChosenTask = false;
                    break;
                }else {
                    TaskStatus newStatus;
                    String newValue = null;
                    LocalDateTime newTime;
                    if (fieldToEdit == 3) {
                        newStatus = taskStatuses[doWhileConditionIsFalse(STATUS_QUESTION, 1, 3)-1];
                        newTime = chosenTask.getDueDate();
                    } else if (fieldToEdit == 2) {
                        newTime = getValidDate();
                        newStatus = chosenTask.getStatus();
                    } else {
                        newStatus = chosenTask.getStatus();
                        newTime = chosenTask.getDueDate();
                        newValue = promptOpenAnswer("Enter new value: ");
                    }
                    String newName = fieldToEdit == 1 ? newValue : chosenTask.getName();
                    String newProject = fieldToEdit == 4 ? newValue : chosenTask.getProject();
                    System.out.println(newStatus);
                    setTask(newName, newTime, newStatus, newProject, chosenTask);
                    System.out.println("Following task has been edited:\n"+chosenTask.toString());
                    removeDuplicateTasks(chosenTask);
                    isEditingChosenTask = promptBooleanAnswer("Continue editing other fields of this task?(y/n)");
                }
            }
            isEditingTask = promptBooleanAnswer("Continue editing tasks?(y/n)");
        }
    }



    protected ArrayList<Task> getPossibleListOfTasks() {
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

    private int doWhileConditionIsFalse(String prompt, int lowerLimit, int upperLimit) {
        boolean isWithinLimits;
        int status;
        do {
            try {
                status = Integer.parseInt(promptOpenAnswer(prompt));
                if (status < lowerLimit || status > upperLimit) {
                    System.out.println("Invalid option was chosen, please select a valid option");
                    isWithinLimits = false;
                } else {
                    isWithinLimits = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option was chosen, please select a valid option");
                isWithinLimits = false;
                status = 0;
            }
        } while (!isWithinLimits);
        return status;
    }


}
