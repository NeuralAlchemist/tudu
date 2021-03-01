package com.tudu;


import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import com.tudu.tasklist.TaskList;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        LocalDateTime n = LocalDateTime.of(2021, Month.FEBRUARY, 23, 22, 0);

        //Task example = new Task("Go to sleep", n, TaskStatus.UNSTARTED, "Personal" );
        TaskList tl = new TaskList();
        tl.displayTaskList();
        Task example = new Task();
        tl.addTask(example);
        tl.addTask(new Task("Hope it works", LocalDateTime.now(), TaskStatus.UNSTARTED, "Work"));
        tl.addTask(new Task("Zumba", LocalDateTime.now(), TaskStatus.UNSTARTED, "Cardio"));
        tl.addTask(new Task("Running", LocalDateTime.now(), TaskStatus.UNSTARTED, "Cardio"));
        tl.addTask(new Task("Strength", LocalDateTime.now(), TaskStatus.UNSTARTED, "Workout"));
        System.out.println("Sorting by project");
        tl.displayTaskList();



    }
}
