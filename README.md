# TuDu - A text based todo list application
TuDu is a simple todo list TUI. This java application is being developed as part of my SDA course. My goals for making this application is to learn JUnit testing and working with different implementations of the Collection interface.  

![Tudu Preview](https://imgur.com/kfNNQW1.jpg)

### Table of Contents
1. [Technologies](#technologies)
2. [Setup](#setup)
4. [Features](#features)
4. [Getting Started](#getting-started)

#### Technologies
- JDK 15.0.2
- JUnit 5.7.1 
- Gradle 6.8.3
- Git  2.24.3

#### Setup
To run this application
- Clone this repository  

  ```git clone git@github.com:NeuralAlchemist/tudu.git```

- On terminal, change directory to the cloned local repository

- Run 

  -  ```./gradlew run --console=plain``` for  Linux/maOS
  - ```./gradlew.bat run -consol=plain``` for Windows 

To see other functions available with Gradle, execute ```./gradlew``` on Linux/macOS and ```./gradlew.bat``` in the cloned local repository.

#### Features

- Display all tasks by ascending/ descending order of project/ due date
- Add a unique task with name, due date, status and project
- Validate due date while being entered (allows you to correct right away!)
- View immediately the added task's fields
- Query to edit/ remove/ mark as done a task that contains a word/letter
- Displays all tasks in ascending order of due date/ project to be selected for editing/making as done/removal
- Edit all fields of a task (one-by-one) after it has been selected for editing (No need to query/select it again!)
- Remove a task
- Mark a task as done
- View immediately the edited/marked as done task
- Save/Load the tasks to/from a local database

#### Getting Started

After going through the [setup](#setup) you are ready to get organising! Here's how to use the application! 

Want to see all the tasks? Enter 1 in the main menu and perform actions similar to below

![tudu-view-all](https://imgur.com/CooOpKZ.png)

Type 2 and press enter in the main menu to add tasks as shown below:

![tudu-add-task](https://imgur.com/0KNqfXj.png)

Oops! Made a mistake? Edit a task by entering 3 in the main menu and  search for your task using one of the three possible search modes. The following scrrenshot shows searching by viewing all tasks sorted according to ascending due date.

![tudu-edit-field](https://imgur.com/hdTpL8W.png)

Finished a task and want to mark it as done? The options for finding the task to mark as done are similar to above. Enter 3 in the main menu and follow steps similar to the following screenshot. It shows searching for a task by querying a word.

![tudu-mark-as-done](https://imgur.com/WHU0xMT.png)

To quit the application simply enter 4 in the main menu and your tasks will be saved locally!  TuDu will look for this database and load it automatically the next time you start the application and all your information will be there!

Hope you enjoy TuDu! 

Developers are welcome to open issues on this repo.