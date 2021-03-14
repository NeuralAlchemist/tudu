package com.tudu.task;

import org.fusesource.jansi.Ansi;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import static org.fusesource.jansi.Ansi.ansi;

public class SortedTaskListUI extends SortedTaskList {
    private final static String DATE_FORMAT = "yy-MM-dd HH:mm";
    private final static int DEFAULT_NUMBER_OPTION = 26;
    private final static String DEFAULT_STRING_OPTION = "n";
    private BufferedReader bufferedReader;

    // Class to display TaskList and read user input from terminal
    // Must be an infinite loop until "Save and Quit" option is chosen
    // Private or local fields
    // Methods : readInput, printMenu
    private void displayMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1 -> View all tasks (sort by project/due date)");
        System.out.println("2 -> Add a new task");
        System.out.println("3 -> Modify a task (includes changing task fields/removing a task)");
        System.out.println("4 -> Save and quit");
    }

    public void readInput(InputStream in) {
        bufferedReader = new BufferedReader(new InputStreamReader(in));
        System.out.println(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));
        System.out.println("Get organizing with TuDu! \n");
        //Display "welcome back if loading from a file
        boolean cont = true;
        while (cont) {
            displayMenu();
            int input = 0;
            boolean successful;
            do{
                try {
                    String temp = bufferedReader.readLine();
                    input = temp.isEmpty()? DEFAULT_NUMBER_OPTION : Integer.parseInt(temp); //DEFAULT
                    successful = true;
                } catch (IOException | NumberFormatException e) {
                    System.out.println("Invalid input! Please type a number in the range [1-4]");
                    successful = false;
                }
            }while(!successful);
            switch (input) {
                case 1:
                    System.out.println("Viewing all tasks");
                    break;
                case 2:
                    System.out.println("Add a new task");
                    addTaskMenu();
                    break;
                case 3:
                    System.out.println("Modifying a task");
                    break;
                case 4:
                    System.out.println("Saving and quitting");
                    saveTaskListToFile();
                    cont = false;
                    break;
                default:
                    System.out.println("Invalid input! Please type a number in the range [1-4]");
            }
        }

    }

    private void addTaskMenu() {
        boolean addTask = true;
        while (addTask) {
            String taskName = questionPrompt("Enter name of task: ");
            boolean successful;
            LocalDateTime dueDate;
            do {
                dueDate = validateDate(questionPrompt("What is the due date of the task? (enter in " + DATE_FORMAT + ")"));
                successful = (dueDate != null);
                if (!successful) {
                    System.out.println("The chosen date does not exist (OR) please input a valid date in the format" + DATE_FORMAT);
                }
            } while (!successful);
            boolean status = yesOrNoPrompt("Have you already begun the task?(y/n)");
            String project = questionPrompt("What type of project is this task?");
            this.addTask(taskName, dueDate, status, project);
            addTask = yesOrNoPrompt("Add more tasks?(y/n)");
        }
    }

    private LocalDateTime validateDate(String in) {
        LocalDateTime result;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        try {
            Date example = format.parse(in);
            System.out.println(example);
            result = example.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        } catch (ParseException e) {
            System.out.println("Exception for date");
            result = null;
        }
        return result;
    }

    private String questionPrompt(String prompt) {
        String result;
        System.out.println(prompt);
        try{
            result = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private boolean yesOrNoPrompt(String prompt) {
        boolean result;
        String input = null;
        boolean successful = false;
        do {
            System.out.println(prompt);
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = input.isEmpty() ? DEFAULT_STRING_OPTION : input; //DEFAULT
            if (input.substring(0, 1).equalsIgnoreCase("y")) {
                result = true;
                successful = true;
            } else if (input.substring(0, 1).equalsIgnoreCase("n")) {
                result = false;
                successful = true;
            } else {
                System.out.println("Invalid input! Please input yes/no.");
                result = false;
            }
        } while (!successful);
        return result;
    }



}
