package com.tudu.tasklist;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SortedTaskListUITests {

    private SortedTaskListUI tudu;
    private String[] taskNamesForInput = {"stretch\n", "kill a zombie\n", "do not smoke\n", "eat icecream\n"};
    private String[] taskNames = {"stretch", "kill a zombie", "do not smoke", "eat icecream"};
    private LocalDateTime[] localDueDates = {LocalDateTime.of(2021, 3, 11, 13, 0),
            LocalDateTime.of(2000, 1, 1, 1, 1),
            LocalDateTime.of(1987, 12, 12, 20, 0),
            LocalDateTime.of(2050, 10, 23, 10, 0)};
    private final int firstDate = 2;
    private final int secondDate = 1;
    private final int thirdDate = 0;
    private final int fourthDate = 3;
    private String[] dueDates = {"21-3-11 13:00\n", "00-1-1 1:1\n", "87-12-12 20:00\n", "2050-10-23 10:0\n"};
    private String DEFAULT_STATUS = "3\n";
    private String ONGOING_STATUS = "1\n";
    private String DONE_STATUS = "2\n";
    private String[] projectNamesForInput = {"personal\n", "work\n", "health\n", "culinary\n"};
    private String[] projectNames = {"personal", "work", "health", "culinary"};
    private final String VIEW = "1\n";
    private final String ADD = "2\n";
    private final String EDIT = "3\n";
    private final String CHOOSE_EDIT_OR_REMOVE = "1\n";
    private final String CHOOSE_REMOVE = "2\n";
    private final String EDIT_SEARCH_TERM = "3\n";
    private final String EDIT_OUT_SEARCH = "n\n";
    private final String EDIT_ALL_PROJECTS = "1\n";
    private final String QUIT = "4\n";
    private final String YES = "yah\n";
    private final String NO = "nah\n";
    private final String EMPTY = "\n";
    private final String DEFAULT_NAME = "NO NAME";
    private final String stringPathToDatabase = "tudu-database.txt";
    File databaseFile = new File(stringPathToDatabase);

    @BeforeEach
    public void init() {
        tudu = new SortedTaskListUI();
        databaseFile.delete();
        try {
            databaseFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("validate date result is not null when input is proper format")
    void validateDateResultIsNotNullWhenInputIsProperFormat() {
        assertEquals(0, tudu.getSize());
        String input = ADD + taskNamesForInput[3] + dueDates[3] + DEFAULT_STATUS + projectNamesForInput[3] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        assertNotEquals(null, tudu.dueDateSortedList.get(0).getDueDate());
    }

    @Test
    @DisplayName("task is added with correct inputs and should be in the tasklist")
    void taskIsAddedWithCorrectInputsAndShouldBeInTheTasklist() {
        String input = ADD + taskNamesForInput[0] + dueDates[3] + DEFAULT_STATUS + projectNamesForInput[0] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        ArrayList<Task> foundTasks = tudu.findTaskByName(taskNames[0]);
        Assertions.assertAll(() -> Assertions.assertEquals(foundTasks.get(0).getDueDate(), localDueDates[3]),
                () -> Assertions.assertEquals(foundTasks.get(0).getStatus(), TaskStatus.UNSTARTED),
                () -> Assertions.assertEquals(foundTasks.get(0).getProject(), projectNames[0]));
        Assertions.assertEquals(1, tudu.getSize());
    }

    @Test
    @DisplayName("task with wrong year is forced to correct by validate Date")
    void taskWithWrongYearIsForcedToCorrectByValidateDate() {
        String input = ADD + taskNamesForInput[3] + "-00-1-1 1:1\n" + dueDates[1] + DEFAULT_STATUS + projectNamesForInput[2] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.dueDateSortedList;
        Assertions.assertAll(() -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, tudu.getSize()));
    }


    @Test
    @DisplayName("task with wrong month is forced to correct by validate Date")
    void taskWithWrongMonthIsForcedToCorrectByValidateDate() {
        String input = ADD + taskNamesForInput[3] + "00-14-1 1:1\n" + dueDates[1] + DEFAULT_STATUS + projectNamesForInput[2] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.dueDateSortedList;
        Assertions.assertAll(() -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, tudu.getSize()));
    }

    @Test
    @DisplayName("task with wrong day of month is forced to correct by validate Date")
    void taskWithWrongDayOfMonthIsForcedToCorrectByValidateDate() {
        String input = ADD + taskNamesForInput[3] + "00-12-300 1:1\n" + dueDates[1] + DEFAULT_STATUS + projectNamesForInput[2] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.dueDateSortedList;
        Assertions.assertAll(() -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @DisplayName("task with wrong hour is forced to correct by validate Date")
    void taskWithWrongHourIsForcedToCorrectByValidateDate() {
        String input = ADD + taskNamesForInput[3] + "00-12-20 26:1\n" + dueDates[1] + DEFAULT_STATUS + projectNamesForInput[2] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.dueDateSortedList;
        Assertions.assertAll(() -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @DisplayName("task with wrong minute is forced to correct by validate date")
    void taskWithWrongMinuteIsForcedToCorrectByValidateDate() {
        String input = ADD + taskNamesForInput[3] + "00-12-20 1:60\n" + dueDates[1] + DEFAULT_STATUS + projectNamesForInput[2] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.dueDateSortedList;
        Assertions.assertAll(() -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @DisplayName("Input to add task can be empty except for due date")
    void inputToAddTaskCanBeEmptyExceptForDueDate() {
        String input = ADD + EMPTY + dueDates[1] + DEFAULT_STATUS + EMPTY + NO + QUIT;
        Assertions.assertEquals(0, tudu.getSize());
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(1, tudu.getSize());
    }

    @Test
    @DisplayName("After displaying descending order of due date the stored linked list should remain unchanged")
    void afterDisplayingDescendingOrderOfDueDateTheStoredLinkedListShouldRemainUnchanged() {
        String input = ADD + taskNamesForInput[0] + dueDates[fourthDate] + DEFAULT_STATUS + projectNamesForInput[3] + YES
                + taskNamesForInput[1] + dueDates[firstDate] + ONGOING_STATUS + projectNamesForInput[0] + YES
                + taskNamesForInput[2] + dueDates[secondDate] + ONGOING_STATUS + projectNamesForInput[1] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        tudu.viewTaskByDueDate(false, false);
        Assertions.assertAll(() -> assertEquals(localDueDates[firstDate], tudu.dueDateSortedList.get(0).getDueDate()),
                () -> assertEquals(localDueDates[secondDate], tudu.dueDateSortedList.get(1).getDueDate()),
                () -> assertEquals(localDueDates[fourthDate], tudu.dueDateSortedList.get(2).getDueDate()));
    }

    @Test
    @DisplayName("After displaying descending order of project the stored tree map should remain unchanged")
    void afterDisplayingDescendingOrderOfProjectTheStoredTreeMapShouldRemainUnchanged() {
        String input = ADD + taskNamesForInput[0] + dueDates[fourthDate] + DEFAULT_STATUS + projectNamesForInput[3] + YES
                + taskNamesForInput[1] + dueDates[firstDate] + ONGOING_STATUS + projectNamesForInput[0] + YES
                + taskNamesForInput[2] + dueDates[secondDate] + ONGOING_STATUS + projectNamesForInput[1] + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        tudu.viewTaskByProject(false, false);
        assertEquals(3, tudu.getSize());
        Assertions.assertAll(() -> assertEquals(projectNames[3], tudu.projectSortedMap.firstKey()),
                () -> assertEquals(projectNames[1], tudu.projectSortedMap.lastKey()));
    }

    @Test
    @DisplayName("checking for word not in task list does not throw exception")
    void checkingForWordNotInTaskListDoesNotThrowExceptionIfListIsNull() {
        String input = ADD + taskNamesForInput[0] + dueDates[fourthDate] + DEFAULT_STATUS + projectNamesForInput[3] + YES
                + taskNamesForInput[1] + dueDates[firstDate] + ONGOING_STATUS + projectNamesForInput[0] + YES
                + taskNamesForInput[2] + dueDates[secondDate] + ONGOING_STATUS + projectNamesForInput[1] + NO
                + EDIT + CHOOSE_EDIT_OR_REMOVE + EDIT_SEARCH_TERM + "champagne\n" + EDIT_OUT_SEARCH + CHOOSE_EDIT_OR_REMOVE +
                EDIT_ALL_PROJECTS + "1\n" + "1\n" + taskNamesForInput[3] + NO + NO + QUIT;
        try {
            tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            fail("This should not have happened");
        }
    }

    @Test
    @DisplayName("returns the correct task with index from project sorted map")
    void returnsTheCorrectTaskWithIndexFromProjectSortedMap() {
        String input = ADD + EMPTY + dueDates[1] + DEFAULT_STATUS + EMPTY
                + YES + taskNamesForInput[2] + dueDates[2] + DEFAULT_STATUS + projectNamesForInput[2]
                + YES + taskNamesForInput[1] + dueDates[3] + ONGOING_STATUS + projectNamesForInput[1]
                + NO + QUIT;
        tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        assertEquals(3, tudu.getSize());
        Task actual = tudu.getTaskFromDisplayedProjectSortedMap(2);
        assertEquals(projectNames[2], actual.getProject());
    }

    @Test
    @DisplayName("do while condition is false does not throw number format exception")
    void doWhileConditionIsFalseDoesNotThrowNumberFormatException() {
        String input = "#\n" + "we\n" + QUIT;
        try {
            tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        } catch (NumberFormatException e) {
            fail("Exception should have been caught");
        }
    }

    @Test
    @DisplayName("start application will never return negative one")
    void startApplicationWillNeverReturnNegativeOne() {
        String input = ADD + EMPTY + dueDates[1] + DEFAULT_STATUS + EMPTY + NO + "5\n" + "%\n" + QUIT;
        int result = tudu.startApplication(new ByteArrayInputStream(input.getBytes()));
        if (result == -1) {
            fail("The program exited forcefully, this should not have happened");
        }
    }
}
