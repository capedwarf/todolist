package org.jboss.sample.todolist.domain;

/**
 * @author Matej Lazar
 */
public class Task {

    private String id;
    private boolean taskDone = false;
    private String message;

    public Task(String message) {
        this.message = message;
    }

    public Task(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone() {
        this.taskDone = true;
    }

    public void setTaskDone(String taskDone) {
        this.taskDone = taskDone != null && taskDone.equals("true");
    }

    public void setTaskNotDone() {
        this.taskDone = false;
    }
}
