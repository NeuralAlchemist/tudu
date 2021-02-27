package com.tudu;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import com.tudu.tasklist.TaskList;

import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {
        LocalDateTime n = LocalDateTime.of(2021, Month.FEBRUARY, 23, 22, 0);

        //Task example = new Task("Go to sleep", n, TaskStatus.UNSTARTED, "Personal" );
        TaskList tl = new TaskList();
        tl.displayTaskList();
        Task example = new Task();
        tl.addTask(example);
        tl.addTask(example);
        tl.displayTaskList();

    }
}
