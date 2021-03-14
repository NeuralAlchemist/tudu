package com.tudu.task;


import org.junit.jupiter.api.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

 public class SortedTaskListTests {

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
     private List<LocalDateTime> dates = Arrays.asList(LocalDateTime.of(1880, 1, 1, 12, 0),
             LocalDateTime.of(1980, 1, 1, 12, 0),
             LocalDateTime.of(2080, 1, 1, 12, 0),
             LocalDateTime.of(2180, 1, 1, 12, 0));
     private SortedTaskList sortedTaskList;
     private final String stringPathToDatabase = "tudu-database.txt";
     private final Path pathToDatabase = Paths.get(stringPathToDatabase);
     private final int numberOfTaskFields = 4;
     private final String DEFAULT_NAME = "NO NAME";
    @BeforeEach
    public void init(){
         sortedTaskList = new SortedTaskList();
    }

     @Test
     @DisplayName("tasks added sequentially with ascending due date are sorted by ascending due date")
     public void tasksAddedSequentiallyWithAscendingDueDateAreSortedByAscendingDueDate() {
         sortedTaskList.addTask("Workout", dates.get(0), false, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(1), false, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(2), false, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(3), false, "Work");
         LinkedList<Task> sortedDueDate = sortedTaskList.getSortedByDueDate();
         List<LocalDateTime> actual = Arrays.asList(
                 sortedDueDate.get(0).getDueDate(),
                 sortedDueDate.get(1).getDueDate(),
                 sortedDueDate.get(2).getDueDate(),
                 sortedDueDate.get(3).getDueDate()
         );
         Assertions.assertEquals(dates, actual);
     }

     @Test
     @DisplayName("tasks added sequentially with descending due date are sorted by ascending due date")
     void tasksAddedSequentiallyWithDescendingDueDateAreSortedByAscendingDueDate() {
         sortedTaskList.addTask("Workout", dates.get(3), false, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(2), false, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(1), false, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(0), false, "Work");
         LinkedList<Task> sortedDueDate = sortedTaskList.getSortedByDueDate();
         List<LocalDateTime> actual = Arrays.asList(
                 sortedDueDate.get(0).getDueDate(),
                 sortedDueDate.get(1).getDueDate(),
                 sortedDueDate.get(2).getDueDate(),
                 sortedDueDate.get(3).getDueDate()
         );
         Assertions.assertEquals(dates, actual);
     }


     @Test
     @DisplayName("tasks added sequentially with random due date are sorted by ascending due date")
     void tasksAddedSequentiallyWithRandomDueDateAreSortedByAscendingDueDate() {
         sortedTaskList.addTask("Workout", dates.get(2), false, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(0), false, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(3), false, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(1), false, "Work");
         LinkedList<Task> sortedDueDate = sortedTaskList.getSortedByDueDate();
         List<LocalDateTime> actual = Arrays.asList(
                 sortedDueDate.get(0).getDueDate(),
                 sortedDueDate.get(1).getDueDate(),
                 sortedDueDate.get(2).getDueDate(),
                 sortedDueDate.get(3).getDueDate()
         );
         Assertions.assertEquals(dates, actual);
     }

     @Test
     @DisplayName("tasks added sequentially with duplicate due dates in random order are sorted by ascending due date")
     void tasksAddedSequentiallyWithDuplicateDueDatesInRandomOrderAreSortedByAscendingDueDate() {
         sortedTaskList.addTask("Workout", dates.get(2), false, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(0), false, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(1), false, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(1), false, "Work");
         LinkedList<Task> sortedDueDate = sortedTaskList.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(2).getDueDate()),
                 () -> assertEquals(dates.get(2), sortedDueDate.get(3).getDueDate()));
     }

     @Test
     @DisplayName("empty task name results in default name for task")
     void emptyTaskNameResultsInDefaultNameForTask() {
         sortedTaskList.addTask("", dates.get(2), false, "Empty");
         LinkedList<Task> tasks = sortedTaskList.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(1, sortedTaskList.getNumberOfTasks()),
                 () -> assertEquals("NO NAME", tasks.get(0).getName()));
     }

     @Test
     @DisplayName("empty project name results in default name for project")
     void emptyProjectNameResultsInDefaultNameForProject() {
         sortedTaskList.addTask("Empty", dates.get(2), false, "");
         Assertions.assertEquals(true, sortedTaskList.getSortedByProject().containsKey("NO NAME"));
         assertEquals(1, sortedTaskList.getNumberOfTasks());
     }

     @Test
     @DisplayName("saved file contains all the entered task details in newline delimited form")
     void savedFileContainsAllTheEnteredTaskDetailsInNewlineDelimitedForm() {
         //Delete contents of file to start with a clean file
         try {
             new FileOutputStream(stringPathToDatabase).close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         sortedTaskList.addTask("", localDueDates[3], false, "");
         sortedTaskList.addTask(taskNames[2], localDueDates[2],  false, projectNames[2]);
         int numberOfTasksAdded = 2;
         assertEquals(2, sortedTaskList.getNumberOfTasks());
         ArrayList<String> content;
         sortedTaskList.saveTaskListToFile(stringPathToDatabase);
         try {
             content = new ArrayList<>(Files.readAllLines(pathToDatabase));
             assertEquals(numberOfTasksAdded * numberOfTaskFields, content.size());
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @Test
     @DisplayName("saved file contains the inputted task details in due date sorted order")
     void savedFileContainsTheInputtedTaskDetailsInDueDateSortedOrder() {
         //Delete contents of file to start with a clean file
         try {
             new FileOutputStream(stringPathToDatabase).close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         sortedTaskList.addTask("", localDueDates[3], false, "");
         sortedTaskList.addTask(taskNames[2], localDueDates[2],  false, projectNames[2]);
         List<String> actual = Arrays.asList(taskNames[2], localDueDates[2].toString(), TaskStatus.UNSTARTED.toString(), projectNames[2],
                 DEFAULT_NAME, localDueDates[3].toString(), TaskStatus.UNSTARTED.toString(), DEFAULT_NAME);
         ArrayList<String> content;
         sortedTaskList.saveTaskListToFile(stringPathToDatabase);
         try {
             content = new ArrayList<>(Files.readAllLines(pathToDatabase));
             Assertions.assertAll(() -> Assertions.assertLinesMatch(actual, content));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
