package com.tudu.tasklist;

import com.tudu.task.Task;
import com.tudu.task.TaskStatus;
import java.time.LocalDateTime;

/**
 * A collection of tasks called as TaskList. It provides task add, set, remove and mark as
 * done operations. This class also provides methods to load and save this object's contents
 * from and to a local database. This class does not provide any direct implementation, so
 * classes inheriting this parent class are free to implement an ordered/unordered
 * collection with unique or duplicate tasks.
 */

abstract class TaskListObject {

    /**
     * Ensures that this TaskList contains a task constructed from the specified
     * arguments. Returns the task added to the TaskList, if the operation
     * failed the task returned is {@code null}. TaskLists that support this
     * operation may place limitations on what tasks can be added. In
     * particular, duplicate elements may be refused. Classes should clearly specify
     * in their documentation any restrictions on what tasks may be added. If a TaskList
     * refuses to add a particular task for any reason other than that it already contains
     * the task, it must throw an exception (rather than returning a {@code null} task).
     * @param taskName The name to be set for the task; argument can be an empty string
     * @param dueDate The date by which this task should be done
     * @param status The current status of the task
     * @param project The project to which this task belongs
     * @return The added task is returned if the operation is successful, otherwise a
     * {@code null} task is returned.
     */
    protected abstract Task addTask(String taskName, LocalDateTime dueDate, TaskStatus status, String project);

    /**
     * Replaces the specified task in the TaskList with a task constructed from the
     * specified arguments. This method supports replacing only specified fields.
     * If a TaskList is unable to replace the specified task because it does not exist,
     * it need not throw an exception.
     * @param taskName The name to replace the name of the {@code oldTask}, if not it should
     *                 be the same as the {@code oldTask}'s name field
     * @param dueDate The due date to replace the due date of the {@code oldTask}, if not it
     *                should be the same as the {@code oldTask}'s due date field
     * @param status The status to replace the status of the {@code oldTask}, if not it
     *               should be the same as the {@code oldTask}'s status field
     * @param project The project to replace the project of the {@code oldTask}, if not it
     *               should be the same as the {@code oldTask}'s project field
     * @param oldTask The task to be replaced in the TaskList
     */
    protected abstract void setTask(String taskName, LocalDateTime dueDate, TaskStatus status, String project, Task oldTask);

    /**
     * Updates the status of the specified task if it is present in the TaskList
     * to {@link com.tudu.task.TaskStatus#DONE}.
     * @param task The task whose status is to be updated to
     * {@link com.tudu.task.TaskStatus#DONE};
     * @return The updated task if it present in the TaskList, otherwise
     * {@code null}
     */
    protected abstract Task markTaskAsDone(Task task);

    /**
     * Removes the specified task from the TaskList, if it is present in it.
     * @param task The task to be removed from the TaskList
     * @return {@code true} if the task is removed; {@code false} otherwise
     */
    protected abstract boolean removeTask(Task task);

    /**
     * Saves the TaskList's tasks in to a local database. The method
     * should not throw any error if a save operation fails instead
     * it should handle it.
     * @param stringPathToDatabase The path to the local database
     *
     */
    protected abstract void saveTaskListToFile(String stringPathToDatabase);

    /**
     * Loads the tasks from a local database to this TaskList. The method
     * should not throw any error if a load operation fails instead it
     * should handle it.
     * @param stringPathToDatabase The path to the local database
     * @return
     */
    protected abstract boolean loadTaskListFromFile(String stringPathToDatabase);
}
