package com.tudu;


import com.tudu.task.TaskListUI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        TaskListUI tudu = new TaskListUI();
        tudu.readInput(System.in);
    }
}
