package com.tudu.task;

/**
 * TaskStatus is a enum representing the 3 stages of a task - Ongoing, Done and Unstarted.
 */
public enum TaskStatus {
    /**
     * The singleton instance for representing an ongoing task.
     */
    ONGOING,

    /**
     * The singleton instance for representing a finished task.
     */
    DONE,

    /**
     * The singleton instance for representing a task that has not begun.
     */
    UNSTARTED;
}
