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
    public TaskList taskList = new TaskList();

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
                case 2:
                    System.out.println("Add a new task");
                    addTaskMenu(r);
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

    private void addTaskMenu(BufferedReader r) {
        boolean addTask = true;
        while (addTask) {
            String taskName = questionPrompt("Enter name of task: ", r);
            boolean successful;
            LocalDateTime dueDate;
            do {
                dueDate = validateDate(questionPrompt("What is the due date of the task? (enter in " + DATE_FORMAT + ")", r));
                successful = (dueDate != null);
                if (!successful) {
                    System.out.println("The chosen date does not exist, please input a valid date in the form" + DATE_FORMAT);
                }
            } while (!successful);
            TaskStatus status = yesOrNoPrompt("Have you already begun the task?(y/n)", TaskStatus.ONGOING, TaskStatus.UNSTARTED, r);
            String project = questionPrompt("What type of project is this task?", r);
            taskList.addTask(new Task(taskName, dueDate, status, project));
            System.out.println("Task has been added to Tudu!");
            addTask = yesOrNoPrompt("Add more tasks?(y/n)", true, false, r);
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

    private static String questionPrompt(String prompt, BufferedReader r) {
        String result = null;
        System.out.println(prompt);
        try{
            result = r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private static <T> T yesOrNoPrompt(String prompt, T yesOption, T noOption, BufferedReader r) {
        T result = null;
        String input = null;
        boolean successful = false;
        do {
            System.out.println(prompt);
            try {
                input = r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
