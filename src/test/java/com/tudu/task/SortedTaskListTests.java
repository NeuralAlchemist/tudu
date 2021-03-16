package com.tudu.task;


import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
     private String DEFAULT_STATUS = String.valueOf(TaskStatus.values().length-1);
     private String ONGOING_STATUS = "0";
     private String DONE_STATUS = "1";
     private int ONGOING_STATUS_INPUT = 1;
     private int DONE_STATUS_INPUT = 2;
     private int UNSTARTED_STATUS_INPUT = 3;
     private SortedTaskList sortedTaskList;
     private final String stringPathToDatabase = "tudu-database.txt";
     private final Path pathToDatabase = Paths.get(stringPathToDatabase);
     private final int numberOfTaskFields = 4;
     private final String DEFAULT_NAME = "NO NAME";
     File databaseFile = new File(stringPathToDatabase);
    @BeforeEach
    public void init(){
         sortedTaskList = new SortedTaskList();
    }

     @Test
     @DisplayName("tasks added sequentially with ascending due date are sorted by ascending due date")
     public void tasksAddedSequentiallyWithAscendingDueDateAreSortedByAscendingDueDate() {
         sortedTaskList.addTask("Workout", dates.get(0), UNSTARTED_STATUS_INPUT, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(1), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(2), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(3), UNSTARTED_STATUS_INPUT, "Work");
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
         sortedTaskList.addTask("Workout", dates.get(3), UNSTARTED_STATUS_INPUT, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(2), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(1), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(0), UNSTARTED_STATUS_INPUT, "Work");
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
         sortedTaskList.addTask("Workout", dates.get(2), UNSTARTED_STATUS_INPUT, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(0), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(3), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(1), UNSTARTED_STATUS_INPUT, "Work");
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
         sortedTaskList.addTask("Workout", dates.get(2), UNSTARTED_STATUS_INPUT, "Exercise");
         sortedTaskList.addTask("Read LOTR", dates.get(0), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Collect package", dates.get(1), UNSTARTED_STATUS_INPUT, "Personal");
         sortedTaskList.addTask("Write a good test", dates.get(1), UNSTARTED_STATUS_INPUT, "Work");
         LinkedList<Task> sortedDueDate = sortedTaskList.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(dates.get(0), sortedDueDate.get(0).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(1).getDueDate()),
                 () -> assertEquals(dates.get(1), sortedDueDate.get(2).getDueDate()),
                 () -> assertEquals(dates.get(2), sortedDueDate.get(3).getDueDate()));
     }

    @Test
    @DisplayName("duplicate tasks will not increase the number of tasks")
    void duplicateTasksWillNotIncreaseTheNumberOfTasks() {
        sortedTaskList.addTask("", dates.get(2), UNSTARTED_STATUS_INPUT, "Empty");
        int expectedNumberOfTasks = 1;
        sortedTaskList.addTask("", dates.get(2), UNSTARTED_STATUS_INPUT, "Empty");
        assertEquals(1, sortedTaskList.getNumberOfTasks());
    }
     @Test
     @DisplayName("empty task name results in default name for task")
     void emptyTaskNameResultsInDefaultNameForTask() {
         sortedTaskList.addTask("", dates.get(2), UNSTARTED_STATUS_INPUT, "Empty");
         LinkedList<Task> tasks = sortedTaskList.getSortedByDueDate();
         Assertions.assertAll(() -> assertEquals(1, sortedTaskList.getNumberOfTasks()),
                 () -> assertEquals("NO NAME", tasks.get(0).getName()));
     }

     @Test
     @DisplayName("empty project name results in default name for project")
     void emptyProjectNameResultsInDefaultNameForProject() {
         sortedTaskList.addTask("Empty", dates.get(2), UNSTARTED_STATUS_INPUT, "");
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
         sortedTaskList.addTask("", localDueDates[3], UNSTARTED_STATUS_INPUT, "");
         sortedTaskList.addTask(taskNames[2], localDueDates[2],  UNSTARTED_STATUS_INPUT, projectNames[2]);
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
         sortedTaskList.addTask("", localDueDates[3], UNSTARTED_STATUS_INPUT, "");
         sortedTaskList.addTask(taskNames[2], localDueDates[2],  UNSTARTED_STATUS_INPUT, projectNames[2]);
         List<String> actual = Arrays.asList(taskNames[2], localDueDates[2].toString(), DEFAULT_STATUS, projectNames[2],
                 DEFAULT_NAME, localDueDates[3].toString(), DEFAULT_STATUS, DEFAULT_NAME);
         ArrayList<String> content;
         sortedTaskList.saveTaskListToFile(stringPathToDatabase);
         try {
             content = new ArrayList<>(Files.readAllLines(pathToDatabase));
             Assertions.assertAll(() -> Assertions.assertLinesMatch(actual, content));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @Test
     @DisplayName("search for non existing database returns false")
     void searchForNonExistingDatabaseReturnsFalse() {
         databaseFile.delete();
         assertEquals(false, sortedTaskList.searchForDatabaseFile(stringPathToDatabase));
     }

     @Test
     @DisplayName("search for existing empty database returns true")
     void searchForexistingEmptyDatabaseReturnsTrue() {
         databaseFile.delete();
         try {
             databaseFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         assertEquals(true, sortedTaskList.searchForDatabaseFile(stringPathToDatabase));
     }

     @Test
     @DisplayName("loading a non existing file will return false")
     void loadingANonExistingFileWillReturnFalse() {
         databaseFile.delete();
         assertEquals(false, sortedTaskList.loadTaskListFromFile(stringPathToDatabase));
     }

     @Test
     @DisplayName("loading a existing file will return true")
     void loadingANonExistingFileWillThrowIoException() {
        databaseFile.delete();
        sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
        sortedTaskList.saveTaskListToFile(stringPathToDatabase);
        sortedTaskList = new SortedTaskList();
        assertEquals(true, sortedTaskList.loadTaskListFromFile(stringPathToDatabase));


     }

     @Test
     @DisplayName("empty task list remains empty after loading a non existing database")
     void emptyTaskListRemainsEmptyAfterReadingANonExistingDatabase() {
        databaseFile.delete();
        int expectedNumberOfTasksBeforeLoading = 0;
        int expectedNumberOfTasksAfterLoading = 0;
        Assertions.assertEquals(expectedNumberOfTasksBeforeLoading, sortedTaskList.getNumberOfTasks());
        Assertions.assertFalse(databaseFile.exists());
        sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
        Assertions.assertEquals(expectedNumberOfTasksAfterLoading, sortedTaskList.getNumberOfTasks());
     }

     @Test
     @DisplayName("empty task list remains empty after reading an empty database")
     void emptyTaskListRemainsEmptyAfterReadingAnEmptyDatabase() {
         databaseFile.delete();
         try {
             databaseFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         int expectedNumberOfTasksBeforeLoading = 0;
         int expectedNumberOfTasksAfterLoading = 0;
         Assertions.assertEquals(expectedNumberOfTasksBeforeLoading, sortedTaskList.getNumberOfTasks());
         Assertions.assertTrue(databaseFile.length() == 0);
         sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
         Assertions.assertEquals(expectedNumberOfTasksAfterLoading, sortedTaskList.getNumberOfTasks());
     }

     @Test
     @DisplayName("non empty task list remains unchanged after reading a non existing database")
     void nonEmptyTaskListRemainsUnchangedAfterReadingANonExistingDatabase() {
        databaseFile.delete();
        sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
        int expectedNumberOfTasksBeforeLoading = 1;
        int expectedNumberOfTasksAfterLoading = 1;
        Assertions.assertEquals(expectedNumberOfTasksBeforeLoading, sortedTaskList.getNumberOfTasks());
        Assertions.assertFalse(databaseFile.exists());
        sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
        assertEquals(expectedNumberOfTasksAfterLoading, sortedTaskList.getNumberOfTasks());

     }

     @Test
     @DisplayName("non empty task list remains unchanged after reading an empty database")
     void nonEmptyTaskListRemainsUnchangedAfterReadingAnEmptyDatabase() {
         databaseFile.delete();
         try {
             databaseFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
         int expectedNumberOfTasksBeforeLoading = 1;
         int expectedNumberOfTasksAfterLoading = 1;
         Assertions.assertEquals(expectedNumberOfTasksBeforeLoading, sortedTaskList.getNumberOfTasks());
         Assertions.assertTrue(databaseFile.length() == 0);
         sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
         Assertions.assertEquals(expectedNumberOfTasksAfterLoading, sortedTaskList.getNumberOfTasks());
     }

     @Test
     @DisplayName("empty task list includes tasks from database after reading from non-empty database")
     void emptyTaskListIncludesTasksFromDatabaseAfterReadingFromNonEmptyDatabase() {
         databaseFile.delete();
         try {
             databaseFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
         sortedTaskList.saveTaskListToFile(stringPathToDatabase);
         Assertions.assertTrue(databaseFile.length() != 0);
         int numberOfTasksToLoadFromDatabase = sortedTaskList.getNumberOfTasks();
         sortedTaskList = new SortedTaskList();
         int numberOfTasksInEmptySortedTaskList = 0;
         assertEquals(numberOfTasksInEmptySortedTaskList, sortedTaskList.getNumberOfTasks());
         sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
         assertEquals(numberOfTasksInEmptySortedTaskList + numberOfTasksToLoadFromDatabase, sortedTaskList.getNumberOfTasks());
     }

     @Test
     @DisplayName("non empty task list includes unique tasks from database after reading from non-empty database")
     void nonEmptyTaskListIncludesUniqueTasksFromDatabaseAfterReadingFromNonEmptyDatabase() {
         databaseFile.delete();
         try {
             databaseFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
         sortedTaskList.saveTaskListToFile(stringPathToDatabase);
         Assertions.assertTrue(databaseFile.length() != 0);
         int numberOfTasksToLoadFromDatabase = sortedTaskList.getNumberOfTasks();
         sortedTaskList = new SortedTaskList();
         sortedTaskList.addTask(taskNames[0], localDueDates[2], ONGOING_STATUS_INPUT, projectNames[3]);
         int numberOfTasksInSortedTaskList = 1;
         assertEquals(numberOfTasksInSortedTaskList, sortedTaskList.getNumberOfTasks());
         sortedTaskList.loadTaskListFromFile(stringPathToDatabase);
         assertEquals(numberOfTasksInSortedTaskList + numberOfTasksToLoadFromDatabase, sortedTaskList.getNumberOfTasks());
     }

     //database file should contain re-write content that is already in it.


     @Test
     @DisplayName("empty database is not loaded into tasklist")
     void emptyDatabaseIsNotLoadedIntoTasklist() {
         databaseFile.delete();
         assertEquals(false, sortedTaskList.loadTaskList(stringPathToDatabase));
     }

    @Test
    @DisplayName("setting task name does not change the task position in due date sorted list")
    void settingTaskNameDoesNotChangeTheTaskPositionInProjectSortedMap() {
        databaseFile.delete();
        sortedTaskList.addTask(taskNames[2], localDueDates[secondDate], ONGOING_STATUS_INPUT, projectNames[0]);
        sortedTaskList.addTask(taskNames[3], localDueDates[firstDate], ONGOING_STATUS_INPUT, projectNames[1]);
        int positionOfTaskUnderTest = 0;
        Task toBeUpdated = sortedTaskList.dueDateSortedList.get(positionOfTaskUnderTest);
        sortedTaskList.setTaskInTaskList(taskNames[1], localDueDates[firstDate], ONGOING_STATUS_INPUT, projectNames[1], toBeUpdated);
        assertEquals(taskNames[1], sortedTaskList.dueDateSortedList.get(positionOfTaskUnderTest).getName());
    }

    @Test
    @DisplayName("setting task status does not change the task position in project sorted map")
    void settingTaskStatusDoesNotChangeTheTaskPositionInProjectSortedMap() {
        databaseFile.delete();
        sortedTaskList.addTask(taskNames[2], localDueDates[secondDate], ONGOING_STATUS_INPUT, projectNames[0]);
        sortedTaskList.addTask(taskNames[3], localDueDates[firstDate], ONGOING_STATUS_INPUT, projectNames[1]);
        // Task is in the first index position of the arraylist mapped to the task's project
        Task toBeUpdated = sortedTaskList.projectSortedMap.get(projectNames[1]).get(0);
        sortedTaskList.setTaskInTaskList(taskNames[3], localDueDates[firstDate], DONE_STATUS_INPUT, projectNames[1], toBeUpdated);
        assertEquals(taskNames[3], sortedTaskList.projectSortedMap.get(projectNames[1]).get(0).getName());
    }

     @Test
     @DisplayName("removed task is not present in the project sorted map")
     void removedTaskIsNotPresentInTheProjectSortedMap() {
         databaseFile.delete();
         sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
         sortedTaskList.removeTaskInTaskList(sortedTaskList.dueDateSortedList.get(0));
         Assertions.assertEquals(true, sortedTaskList.projectSortedMap.get(projectNames[0]).isEmpty());
     }

    @Test
    @DisplayName("removed task is not present in the due date sorted list")
    void removedTaskIsNotPresentInTheDueDateSortedList() {
        databaseFile.delete();
        sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
        sortedTaskList.removeTaskInTaskList(sortedTaskList.dueDateSortedList.get(0));
        Assertions.assertEquals(true, sortedTaskList.dueDateSortedList.isEmpty());
        Assertions.assertNull(sortedTaskList.findTaskByName(taskNames[2]));
    }

     @Test
     @DisplayName("remove task method does not increase the number of tasks")
     void removeTaskMethodDoesNotIncreaseTheNumberOfTasks() {
         databaseFile.delete();
         sortedTaskList.addTask(taskNames[2], localDueDates[1], ONGOING_STATUS_INPUT, projectNames[0]);
         int numberOfTasksBeforeRemove = sortedTaskList.getNumberOfTasks();
         sortedTaskList.removeTaskInTaskList(sortedTaskList.dueDateSortedList.get(0));
         assertFalse(sortedTaskList.getNumberOfTasks() > numberOfTasksBeforeRemove);
     }

    @Test
    @DisplayName("display by due date in descending order returns the correct linked list")
    void displayByDueDateInDescendingOrderReturnsTheCorrectLinkedList() {
        sortedTaskList.addTask("Workout", localDueDates[secondDate], UNSTARTED_STATUS_INPUT, "Exercise");
        sortedTaskList.addTask("Read LOTR", localDueDates[firstDate], UNSTARTED_STATUS_INPUT, "Personal");
        sortedTaskList.addTask("Collect package", localDueDates[thirdDate], UNSTARTED_STATUS_INPUT, "Personal");
        sortedTaskList.addTask("Write a good test", localDueDates[secondDate], UNSTARTED_STATUS_INPUT, "Work");
        LinkedList<Task> result = sortedTaskList.displayByDueDate(false);
        Assertions.assertAll( () -> assertEquals(localDueDates[thirdDate], result.get(0).getDueDate()),
                () -> assertEquals(localDueDates[secondDate], result.get(1).getDueDate()),
                () -> assertEquals(localDueDates[secondDate], result.get(2).getDueDate()),
                () -> assertEquals(localDueDates[firstDate], result.get(3).getDueDate()));
    }
}
