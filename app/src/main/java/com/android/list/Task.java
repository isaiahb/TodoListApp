package com.android.list;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Task {
    private String title;
    private String description;
    private TaskState taskState;
    private String parentTaskID;
    private String userID;
    private String ID;
    private boolean isSubtask;

    Task() {
        title = "Finish This amazing app";
        description = "Create an amazing todo list app that I can use to keep track of all the projects i would like to make, and organize and plan for the projects while creating them";
        parentTaskID = "";
        taskState = TaskState.TODO;
        userID = MainActivity.user.getID();
        isSubtask = false;
    }

    Task(QueryDocumentSnapshot document) {
        title = (String) document.get("title");
        description = (String) document.get("description");
        taskState = TaskState.valueOf((String)document.get("taskState"));
        parentTaskID = (String) document.get("parentTaskID");
        userID = (String) document.get("userID");
        ID = document.getId();
        isSubtask = (boolean) document.get("subtask");

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

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getParentTaskID() {
        return parentTaskID;
    }

    public void setParentTaskID(String parentTaskID) {
        this.parentTaskID = parentTaskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isSubtask() {
        return isSubtask;
    }

    public void setSubtask(boolean subtask) {
        isSubtask = subtask;
    }
}


