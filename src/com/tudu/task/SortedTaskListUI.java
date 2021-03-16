package com.tudu.task;

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
    private BufferedReader bufferedReader;
    private final static String STATUS_QUESTION = "Choose one of the following options about your task status[1-3]" +
            "\n1 -> I'm currently doing it" +
            "\n2 -> I have finished it" +
            "\n3 -> I have not begun it yet";

    // Class to display TaskList and read user input from terminal
    // Must be an infinite loop until "Save and Quit" option is chosen
    // Private or local fields
    // Methods : readInput, printMenu
    private void displayMenu() {
        System.out.println("You can do the following: ");
        System.out.println("1 -> View all tasks (sort by project/due date)");
        System.out.println("2 -> Add a new task");
        System.out.println("3 -> Modify a task (includes changing task fields, removing a task, marking a task as done)");
        System.out.println("4 -> Save and quit");
    }

    public void readInput(InputStream in) {
        bufferedReader = new BufferedReader(new InputStreamReader(in));
        System.out.println(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1, 1));
        System.out.println("Get organizing with TuDu! \n");
        boolean isRunning = true;
        //Display welcome/loading message
        loadTaskList(stringPathToDatabase);
        while (isRunning) {
            displayMenu();
            int input = doWhileConditionIsFalse("Select an option in range [1-4]:", 1, 4);
            switch (input) {
                case 1:
                    System.out.println("Viewing all tasks");
                    viewTask();
                    break;
                case 2:
                    System.out.println("Add a new task");
                    addTaskMenu();
                    break;
                case 3:
                    System.out.println("Edit a task");
                    editTask();
                    break;
                case 4:
                    System.out.println("Saving and quitting");
                    saveTaskListToFile(stringPathToDatabase);
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid input! Please type a number in the range [1-4]");
            }
        }

    }

    private void addTaskMenu() {
        boolean isAddingTask = true;
        while (isAddingTask) {
            String taskName = questionPrompt("Enter name of task: ");
            LocalDateTime dueDate = validateDate(questionPrompt("What is the due date of the task? (enter in " + DATE_FORMAT + ")"));
            int status = doWhileConditionIsFalse(STATUS_QUESTION, 1, 3);
            String project = questionPrompt("What type of project is this task?");
            this.addTask(taskName, dueDate, status, project);
            isAddingTask = yesOrNoPrompt("Add more tasks?(y/n)");
        }
    }

    private void viewTask() {
        boolean isViewingTasks = true;
        while (isViewingTasks) {
            int displayOption = doWhileConditionIsFalse("Choose one of the following[1-2]:\n1 -> Display all tasks by project\n2 -> Display all tasks by due date", 1, 2);
            boolean isAscending = yesOrNoPrompt("Display tasks in ascending order?(y/n)\nOBS! No will imply descending order");
            if (displayOption == 1) {
                viewTaskByProject(isAscending, false);
            } else {
                viewTaskByDueDate(isAscending, false);
            }
            isViewingTasks = yesOrNoPrompt("Continue viewing tasks?(y/n)");
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
    }

    protected void viewArrayListOfTask(ArrayList<Task> listOfTasks) {
        AtomicInteger i = new AtomicInteger();
        listOfTasks.stream().forEach(task -> System.out.println((i.getAndIncrement()+1) + " -> " + task));
    }
    
    protected Task getTaskFromProjectSortedMap(int index){
        int i = 1;
        Task result = null;
        outerloop:
        for(Map.Entry<String, ArrayList<Task>> project : projectSortedMap.entrySet()){
            for(Task task : project.getValue()){
                if(i == index){
                    result = task;
                    break outerloop;
                }
                i++;
            }
        }
        return result;
    }

    private void editTask() {
        boolean isEditingTask = true;
        while (isEditingTask) {
            Task chosenTask;
            int searchOption = doWhileConditionIsFalse("Choose one of the following to begin editing[1-2]:\n" +
                    "1 -> Show all tasks by project\n" +
                    "2 -> Show all tasks by due date\n" +
                    "3 -> Search using task name\n", 1, 3);
                    //Add option to escape from edit here
            switch (searchOption) {
                case 1:
                    viewTaskByProject(true, true);
                    int chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit[1-"
                            + getNumberOfTasks() + "]", 1, getNumberOfTasks());
                    chosenTask = getTaskFromProjectSortedMap(chosenTaskIndex);
                    break;
                case 2:
                    viewTaskByDueDate(true, true);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit[1-"
                            + getNumberOfTasks() + "]", 1, getNumberOfTasks());
                    chosenTask = dueDateSortedList.get(chosenTaskIndex-1);
                    break;
                case 3:
                    ArrayList<Task> listOfTasks = getPossibleListOfTasks();
                    if (listOfTasks == null || listOfTasks.isEmpty() ){
                        continue;
                    }
                    viewArrayListOfTask(listOfTasks);
                    chosenTaskIndex = doWhileConditionIsFalse("Enter the task's number that you want to edit[1-"
                            + (listOfTasks.size()) + "]", 1, listOfTasks.size());
                    chosenTask = listOfTasks.get(chosenTaskIndex-1);
                    break;
                default:
                    chosenTask = null;
            }
            boolean isEditingChosenTask;
            do{
                int fieldToEdit = doWhileConditionIsFalse("Enter the task's field that you want to edit[1-4]" +
                        "\n1 -> Name\n2 -> Due date\n3 -> Status\n4 -> Project", 1, 4);
                int newStatus;
                String newValue = null;
                if(fieldToEdit == 3){
                    newStatus = doWhileConditionIsFalse(STATUS_QUESTION, 1, 3);
                } else {
                    newStatus = chosenTask.getStatus().ordinal()+1;
                    newValue = questionPrompt("Enter new value: ");
                }
                String newName = fieldToEdit == 1 ? newValue : chosenTask.getName();
                LocalDateTime newTime = fieldToEdit == 2 ? validateDate(newValue) : chosenTask.getDueDate();
                String newProject = fieldToEdit == 4 ? newValue : chosenTask.getProject();
                System.out.println(newStatus);
                setTaskInTaskList(newName, newTime, newStatus, newProject, chosenTask);
                /*if (fieldToEdit == 3) {
                    newStatus = doWhileConditionIsFalse(STATUS_QUESTION, 1, 3);
                } else {
                    String newValue, newProject;
                    String newName;
                    LocalDateTime newTime;
                    newValue = questionPrompt("Enter new value: ");
                    newName = fieldToEdit == 1 ? newValue : chosenTask.getName();
                    newTime = fieldToEdit == 2 ? validateDate(newValue) : chosenTask.getDueDate();
                    newProject = fieldToEdit == 4 ? newValue : chosenTask.getProject();
                    setTaskInTaskList(newName, newTime, newStatus, newProject, chosenTask);
                }*/
                isEditingChosenTask = yesOrNoPrompt("Continue editing other fields of this task?(y/n)");
            }while(isEditingChosenTask);
            isEditingTask = yesOrNoPrompt("Continue editing tasks?(y/n)");
        }
    }


    protected ArrayList<Task> getPossibleListOfTasks() {
        boolean isSearchingWithTaskName;
        ArrayList<Task> possibleTasks;
        do {
            String searchTerm = questionPrompt("Enter a word you remember from the task's name");
            possibleTasks = this.findTaskByName(searchTerm);
            if (possibleTasks == null) {
                isSearchingWithTaskName = yesOrNoPrompt("Looks like no task contains the word you gave!" +
                        "\nDo you want to search with another word?(y/n)" +
                        " \nIf not you will be taken to the edit sub-menu.");
            } else {
                isSearchingWithTaskName = false;
            }
        } while (isSearchingWithTaskName);
        return possibleTasks;
    }

    // This method needs to have a way of getting the String - failing test!
    private LocalDateTime validateDate(String in) {
        LocalDateTime result;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        do {
            try {
                Date example = format.parse(in);
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

    private String questionPrompt(String prompt) {
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

    private boolean yesOrNoPrompt(String prompt) {
        boolean isTrue;
        String input = null;
        boolean hasTrueOrFalse = false;
        do {
            // add(y/n) here
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

    // Maybe improve with varargs, if empty varargs check != null?
    protected int doWhileConditionIsFalse(String prompt, int lowerLimit, int upperLimit) {
        boolean isWithinLimits;
        int status;
        do {
            try {
                status = Integer.parseInt(questionPrompt(prompt));
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
