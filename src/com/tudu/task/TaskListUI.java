package com.tudu.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

public class TaskListUI {
    private final static String DATE_FORMAT = "yy-MM-dd HH:mm";
    private TaskList taskList = new TaskList();

    // Class to display TaskList and read user input from terminal
    // Must be an infinite loop until "Save and Quit" option is chosen
    // Private or local fields
    // Methods : readInput, printMenu
    public void displayMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1 -> View all tasks (sort by project/due date)");
        System.out.println("2 -> Add a new task");
        System.out.println("3 -> Modify a task (includes changing task fields/removing a task)");
        System.out.println("4 -> Save and quit");
    }

    public void readInput(InputStream in) {
        System.out.println("inside method");
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        System.out.println("TuDu! So many tasks so little time!");
        boolean cont = true;
        while (cont) {
            displayMenu();
            /*Scanner scan = new Scanner(System.in);
            int input = scan.nextInt();*/
            int input = 0;
            try {
                input = Integer.parseInt(r.readLine());
                System.out.println(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (input) {
                case 1:
                    System.out.println("Viewing all tasks");
                    break;
                //Call method to go through adding task
                case 2:
                    System.out.println("Add a new task");
                    addTaskMenu();
                    break;
                case 3:
                    System.out.println("Modifying a task");
                    break;
                case 4:
                    System.out.println("Saving and quitting");
                    cont = false;
                    break;
            }
        }

    }

    //Check that the inputted task is indeed present in the tasklist
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
                    System.out.println("The chosen date does not exist, please input a valid date in the form" + DATE_FORMAT);
                }
            } while (!successful);
            TaskStatus status = yesOrNoPrompt("Have you already begun the task?(y/n)", TaskStatus.ONGOING, TaskStatus.UNSTARTED);
            String project = questionPrompt("What type of project is this task?");
            taskList.addTask(new Task(taskName, dueDate, status, project));
            System.out.println("Task has been added to Tudu!");
            addTask = yesOrNoPrompt("Add more tasks?(y/n)", true, false);
        }
    }

    private static LocalDateTime validateDate(String in) {
        LocalDateTime result;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        try {
            Date example = format.parse(in);
            result = example.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        } catch (ParseException e) {
            result = null;
        }
        return result;
    }

    private static String questionPrompt(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.println(prompt);
        return scan.nextLine();
    }

    private static <T> T yesOrNoPrompt(String prompt, T yesOption, T noOption) {
        Scanner scan = new Scanner(System.in);
        T result = null;
        boolean successful = false;
        do {
            System.out.println(prompt);
            String input = scan.nextLine();
            if (input.substring(0, 1).equalsIgnoreCase("y")) {
                result = yesOption;
                successful = true;
            } else if (input.substring(0, 1).equalsIgnoreCase("n")) {
                result = noOption;
                successful = true;
            } else {
                System.out.println("Invalid input! Please input yes/no.");
            }
        } while (!successful);
        return result;
    }
}
