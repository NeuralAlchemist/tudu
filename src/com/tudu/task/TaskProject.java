package com.tudu.task;

public class TaskProject {
    private String name, color;

    public TaskProject(String name) {
        System.out.println("This");
        this.name = name.isEmpty() ? "NO NAME" : name;
        System.out.println(this.name);
    }

    public TaskProject(String name, boolean pickColor){
        this.name = name;
        // IF: keep track of colors used (this should be read and written to a file)
        // Display available colors and read the chosen color
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
