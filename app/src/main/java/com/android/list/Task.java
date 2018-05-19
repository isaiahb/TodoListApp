package com.android.list;

import java.util.ArrayList;

public class Task {
    private String title;
    private String description;
    private TaskState taskState;
    private ArrayList<Task> subtasks;
    private Task mainTask;
    private User user;

    Task() {
        title = "Finish This amazing app";
        description = "Create an amazing todo list app that I can use to keep track of all the projects i would like to make, and organize and plan for the projects while creating them";
        taskState = TaskState.TODO;
        subtasks = new ArrayList<Task>();
        user = MainActivity.user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public ArrayList<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Task> subtasks) {
        this.subtasks = subtasks;
    }

    public Task getMainTask() {
        return mainTask;
    }

    public void setMainTask(Task mainTask) {
        this.mainTask = mainTask;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
