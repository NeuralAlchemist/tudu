# TuDu - A text based todo list application
TuDu is a simple todo list TUI. This java application is being developed as part of my SDA course. My goals for making this application is to learn JUnit testing and working with different implementations of the Collection interface. Expected date of completetion for the project is 19th March 2021. Untill then please refer to the [Current status](#current-status).  

### Table of Contents
1. [Technologies](#technologies)
2. [Setup](#setup)
3. [Current status](#current-status)
4. [Features](#features)

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

#### Current status
User can view the startup menu and choose an option. Currently options 1, 2 & 3 (viewing, adding, saving tasks) are functional. 

#### Features

- Display all tasks by ascending/ descending order of project/ due date
- Add a unique task with name, due date, status and project
- Save the tasks added to a local database



   

