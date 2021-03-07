package com.tudu.task;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

 public class TaskListTests{
     static ArrayList<LocalDateTime> dates = new ArrayList<>(4); ;
     public void testDates(){
         dates.add(LocalDateTime.of(1880, 1, 1, 12, 0));
         dates.add(LocalDateTime.of(1980, 1, 1, 12, 0));
         dates.add(LocalDateTime.of(2080, 1, 1, 12, 0));
         dates.add(LocalDateTime.of(2180, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("tasks added sequentially with ascending due date are sorted by ascending due date")
    public void tasksAddedSequentiallyWithAscendingDueDateAreSortedByAscendingDueDate() {
        testDates();
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

     @Test
     @DisplayName("tasks added sequentially with descending due date are sorted by ascending due date")
     void tasksAddedSequentiallyWithDescendingDueDateAreSortedByAscendingDueDate() {
         testDates();
         TaskList example = new TaskList();
         example.addTask(new Task("Workout", dates.get(3), TaskStatus.UNSTARTED, "Exercise"));
         example.addTask(new Task("Read LOTR", dates.get(2), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Collect package", dates.get(1), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Write a good test", dates.get(0), TaskStatus.UNSTARTED, "Work"));
         LinkedList<Task> sortedDueDate = example.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                 () -> assertEquals(dates.get(2), sortedDueDate.get(2).getDueDate()),
                 () -> assertEquals(dates.get(3), sortedDueDate.get(3).getDueDate()));
     }


     @Test
     @DisplayName("tasks added sequentially with random due date are sorted by ascending due date")
     void tasksAddedSequentiallyWithRandomDueDateAreSortedByAscendingDueDate() {
         testDates();
         TaskList example = new TaskList();
         example.addTask(new Task("Workout", dates.get(2), TaskStatus.UNSTARTED, "Exercise"));
         example.addTask(new Task("Read LOTR", dates.get(0), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Collect package", dates.get(3), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Write a good test", dates.get(1), TaskStatus.UNSTARTED, "Work"));
         LinkedList<Task> sortedDueDate = example.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                 () -> assertEquals(dates.get(2), sortedDueDate.get(2).getDueDate()),
                 () -> assertEquals(dates.get(3), sortedDueDate.get(3).getDueDate()));
     }

     @Test
     @DisplayName("tasks added sequentially with duplicate due dates in random order are sorted by ascending due date")
     void tasksAddedSequentiallyWithDuplicateDueDatesInRandomOrderAreSortedByAscendingDueDate() {
         testDates();
         TaskList example = new TaskList();
         example.addTask(new Task("Workout", dates.get(2), TaskStatus.UNSTARTED, "Exercise"));
         example.addTask(new Task("Read LOTR", dates.get(0), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Collect package", dates.get(1), TaskStatus.UNSTARTED, "Personal"));
         example.addTask(new Task("Write a good test", dates.get(1), TaskStatus.UNSTARTED, "Work"));
         LinkedList<Task> sortedDueDate = example.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(2).getDueDate()),
                 () -> assertEquals(dates.get(2), sortedDueDate.get(3).getDueDate()));
     }

}
