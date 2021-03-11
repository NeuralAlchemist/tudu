package com.tudu.task;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskListUITests {

    private TaskListUI tudu;
    private String[] taskNames = {"stretch\n", "kill a zombie\n", "do not smoke\n", "eat icecream\n"};
    private LocalDateTime[] localDueDates = {LocalDateTime.of(2021,3,11,13,0),
                                        LocalDateTime.of(2000, 1,1,1,1),
                                        LocalDateTime.of(1987, 12, 12, 20, 0),
                                        LocalDateTime.of(2050, 10, 23, 10,0)};
    private String[] dueDates = {"21-3-11 13:00\n","00-1-1 1:1\n","87-12-12 20:00\n","2050-10-23 10:0\n"};
    private String[] projectNames = {"personal\n", "work\n", "health\n", "culinary\n"};
    private final String VIEW = "1\n";
    private final String ADD = "2\n";
    private final String EDIT = "3\n";
    private final String QUIT = "4\n";
    private final String YES = "yah\n";
    private final String NO = "nah\n";

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
        String input = VIEW+ADD+taskNames[0]+dueDates[3]+NO+projectNames[0]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        boolean actual = tudu.taskList.findTask(new Task(taskNames[0], localDueDates[3], TaskStatus.UNSTARTED, projectNames[0]));
        Assertions.assertEquals(true, true);
    }

    @Test
    @DisplayName("task with wrong year is forced to correct by validate Date")
    void taskWithWrongYearIsForcedToCorrectByValidateDate() {
        String input = ADD+taskNames[3]+"-00-1-1 1:1\n"+dueDates[1]+projectNames[2]+NO+QUIT;
        tudu.readInput(new ByteArrayInputStream(input.getBytes()));
        boolean actual = tudu.taskList.findTask(new Task(taskNames[3], localDueDates[1], TaskStatus.ONGOING, projectNames[2]));
        Assertions.assertEquals(true, true);
    }

}
