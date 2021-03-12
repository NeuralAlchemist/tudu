package com.tudu.task;


import org.junit.jupiter.api.*;


import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.assertEquals;

 public class TaskListTests{

     private TaskListUI tudu;
     private String[] taskNamesForInput = {"stretch\n", "kill a zombie\n", "do not smoke\n", "eat icecream\n"};
     private String[] taskNames = {"stretch", "kill a zombie", "do not smoke", "eat icecream"};
     private LocalDateTime[] localDueDates = {LocalDateTime.of(2021,3,11,13,0),
             LocalDateTime.of(2000, 1,1,1,1),
             LocalDateTime.of(1987, 12, 12, 20, 0),
             LocalDateTime.of(2050, 10, 23, 10,0)};
     private final int firstDate = 2;
     private final int secondDate = 1;
     private final int thirdDate = 0;
     private final int fourthDate = 3;
     private String[] dueDates = {"21-3-11 13:00\n","00-1-1 1:1\n","87-12-12 20:00\n","2050-10-23 10:0\n"};
     private String[] projectNamesForInput = {"personal\n", "work\n", "health\n", "culinary\n"};
     private String[] projectNames = {"personal", "work", "health", "culinary"};

     @BeforeEach
     public void init(){
         tudu = new TaskListUI();
     }

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

    /*@Test
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


    }*/

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
