package tasks;

public class Task {
    protected Integer taskId;
    protected String title;
    protected String description;
    protected String status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(Integer taskId, String title, String description) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(Integer taskId, String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskId = taskId;
    }

    public void setId(int taskId) {
        this.taskId = taskId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +

                " id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + "'" +
                ", status='" + getStatus() +
                "'}";
    }
}

