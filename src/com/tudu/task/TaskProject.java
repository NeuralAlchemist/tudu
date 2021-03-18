package com.tudu.task;

/**
 * Represents the project of a task.
 * <p>
 *     Note: This setup is to enable adding fields to customize a project.
 *     For example, each project can also include a color field which can be
 *     used to colorize the {@link com.tudu.task.Task#toString()}.
 * </p>
 */
public class TaskProject {
    private String name;

    /**
     * Set the name of a project to the specified name.
     * @param name the name to be set for the project; argument can be empty, in that case a default {@code "NO NAME"} name is given
     */
    public TaskProject(String name) {
        this.name = name.isEmpty() ? "NO NAME" : name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
