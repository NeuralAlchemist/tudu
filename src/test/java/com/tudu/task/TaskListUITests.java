package com.tudu.task;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TaskListUITests {

    private TaskListUI tudu;
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

    @BeforeEach
    public void init(){
        tudu = new TaskListUI();
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
    @DisplayName("task with wrong minute is forced to correct byu validate date")
    void taskWithWrongMinuteIsForcedToCorrectByuValidateDate() {
        String input = ADD+taskNamesForInput[3]+"00-12-20 1:60\n"+dueDates[1]+NO+projectNamesForInput[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        LinkedList<Task> tasks = tudu.getSortedByDueDate();
        Assertions.assertAll( () -> Assertions.assertEquals(tasks.getFirst().getDueDate(), localDueDates[1]),
                () -> Assertions.assertNotEquals(0, 1));
    }

    @Test
    @Disabled
    @DisplayName("task can have all fields blank except due date")
    void taskCanHaveAllFieldsBlankExceptDueDate() {
        String input = ADD+EMPTY+dueDates[1]+EMPTY+EMPTY+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        TreeMap<String, ArrayList<Task>> tasks = tudu.getSortedByProject();
        ArrayList<Task> current = tasks.get("");
        Assertions.assertAll( () -> assertEquals("", current.get(0).getProject()),
                () -> assertEquals("", current.get(0).getStatus()));
    }

}
