package com.tudu.task;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class TaskListTests {
    @Test
    @DisplayName("tasks added sequentially with ascending due date are sorted by ascending due date")
    void tasksAddedSequentiallyWithAscendingDueDateAreSortedByAscendingDueDate() {
        ArrayList<LocalDateTime> dates = new ArrayList<>(4);
        dates.add(LocalDateTime.of(1880, 1, 1, 12, 0));
        dates.add(LocalDateTime.of(1980, 1, 1, 12, 0));
        dates.add(LocalDateTime.of(2080, 1, 1, 12, 0));
        dates.add(LocalDateTime.of(2180, 1, 1, 12, 0));
        TaskList example = new TaskList();
        example.addTask(new Task("Workout", dates.get(0), TaskStatus.UNSTARTED, "Exercise"));
        example.addTask(new Task("Read LOTR", dates.get(1), TaskStatus.UNSTARTED, "Personal"));
        example.addTask(new Task("Collect package", dates.get(2), TaskStatus.UNSTARTED, "Personal"));
        example.addTask(new Task("Write a good test", dates.get(3), TaskStatus.UNSTARTED, "Work"));
        LinkedList<Task> sortedDueDate = example.getSortedByDueDate();
        Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                () -> assertEquals(dates.get(2), sortedDueDate.get(2).getDueDate()),
                () -> assertEquals(dates.get(3), sortedDueDate.get(3).getDueDate()));


    }
}
