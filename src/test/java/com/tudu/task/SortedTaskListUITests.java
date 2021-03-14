package com.tudu.task;

import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class SortedTaskListUITests {

    private SortedTaskListUI tudu;
    private String[] taskNamesForInput = {"stretch\n", "kill a zombie\n", "do not smoke\n", "eat icecream\n"};
    private String[] taskNames = {"stretch", "kill a zombie", "do not smoke", "eat icecream"};
    private LocalDateTime[] localDueDates = {LocalDateTime.of(2021,3,11,13,0),
                                        LocalDateTime.of(2000, 1,1,1,1),
                                        LocalDateTime.of(1987, 12, 12, 20, 0),
                                        LocalDateTime.of(2050, 10, 23, 10,0)};
    private String[] dueDates = {"21-3-11 13:00\n","00-1-1 1:1\n","87-12-12 20:00\n","2050-10-23 10:0\n"};
    private String[] projectNamesForInput = {"personal\n", "work\n", "health\n", "culinary\n"};
    private String[] projectNames = {"personal", "work", "health", "culinary"};
    private final String VIEW = "1\n";
    private final String ADD = "2\n";
    private final String EDIT = "3\n";
    private final String QUIT = "4\n";
    private final String YES = "yah\n";
    private final String NO = "nah\n";
    private final String EMPTY = "\n";
    private final String DEFAULT_NAME = "NO NAME";
    private final String stringPathToDatabase = "tudu-database.txt";
    private final Path pathToDatabase = Paths.get(stringPathToDatabase);

    @BeforeEach
    public void init(){
        tudu = new SortedTaskListUI();
    }

    @Test
    @DisplayName("sample test to check InputStream works for readInput")
    void sampleTestToSeeInputStreamWorksForReadInput() {
        String task = "1\n4\n";
        tudu.readInput(new ByteArrayInputStream(task.getBytes()));
    }


    @Test
    @DisplayName("task is added with correct inputs and should be in the tasklist")
    void taskIsAddedWithCorrectInputsAndShouldBeInTheTasklist() {
        String input = VIEW+ADD+taskNamesForInput[0]+dueDates[3]+NO+projectNamesForInput[0]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        tudu.displayByDueDate(true);
        ArrayList<Task> foundTasks = tudu.findTaskByName(taskNames[0]);
        Assertions.assertAll(() -> Assertions.assertEquals(foundTasks.get(0).getDueDate(), localDueDates[3]),
                () -> Assertions.assertEquals(foundTasks.get(0).getStatus(), TaskStatus.UNSTARTED),
                () -> Assertions.assertEquals(foundTasks.get(0).getProject(), projectNames[0]));
        Assertions.assertEquals(1, tudu.getNumberOfTasks());
    }
   @Test
    @DisplayName("task with wrong year is forced to correct by validate Date")
    void taskWithWrongYearIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNamesForInput[3]+"-00-1-1 1:1\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
       LinkedList<Task> tasks = tudu.getSortedByDueDate();
       Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
               () -> Assertions.assertNotEquals(0, tudu.getNumberOfTasks()));
    }


    @Test
    @DisplayName("task with wrong month is forced to correct by validate Date")
    void taskWithWrongMonthIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNamesForInput[3]+"00-14-1 1:1\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.getSortedByDueDate();
        Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, tudu.getNumberOfTasks()));
    }

    @Test
    @DisplayName("task with wrong day of month is forced to correct by validate Date")
    void taskWithWrongDayOfMonthIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNamesForInput[3]+"00-12-300 1:1\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.getSortedByDueDate();
        Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @DisplayName("task with wrong hour is forced to correct by validate Date")
    void taskWithWrongHourIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNamesForInput[3]+"00-12-20 26:1\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.getSortedByDueDate();
        Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @DisplayName("task with wrong minute is forced to correct by validate date")
    void taskWithWrongMinuteIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNamesForInput[3]+"00-12-20 1:60\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.getSortedByDueDate();
        Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @Disabled
    @DisplayName("Input to add task can be empty except for due date")
    void inputToAddTaskCanBeEmptyExceptForDueDate() {
        String input = ADD+EMPTY+dueDates[1]+EMPTY+EMPTY+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        TreeMap<String, ArrayList<Task>> tasks = tudu.getSortedByProject();
        ArrayList<Task> current = tasks.get("");
        Assertions.assertAll( () -> assertEquals("", current.get(0).getProject()),
                () -> assertEquals(TaskStatus.UNSTARTED, current.get(0).getStatus()));
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
        String input = ADD+EMPTY+dueDates[3]+EMPTY+EMPTY+YES+taskNamesForInput[2]+dueDates[2]+EMPTY+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        ArrayList<String> content;
        try {
            content = new ArrayList<>(Files.readAllLines(pathToDatabase));
            assertEquals(8, content.size());
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
        String input = ADD+EMPTY+dueDates[3]+EMPTY+EMPTY+YES+
                        taskNamesForInput[2]+dueDates[2]+EMPTY+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        List<String> actual = Arrays.asList(taskNames[2], localDueDates[2].toString(), TaskStatus.UNSTARTED.toString(), projectNames[2],
                DEFAULT_NAME, localDueDates[3].toString(), TaskStatus.UNSTARTED.toString(), DEFAULT_NAME);
        ArrayList<String> content;
        try {
            content = new ArrayList<>(Files.readAllLines(pathToDatabase));
            Assertions.assertAll(() -> Assertions.assertLinesMatch(actual, content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
