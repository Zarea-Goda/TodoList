package com.appizona.openvision.model;

/**
 * Created by yehia on 15/09/17.
 */

public class Todo {

    private String title;
    private String timeStamp;
    private String dueDate;
    private String description;
    private String isDone;

    public String getTitle() {
        return title;
    }

    public Todo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Todo setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Todo setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Todo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String isDone() {
        return isDone;
    }

    public Todo setDone(String done) {
        isDone = done;
        return this;
    }
}
