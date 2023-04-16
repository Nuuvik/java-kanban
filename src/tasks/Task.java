package tasks;

public class Task {
    protected Integer id;
    protected String title;
    protected String description;
    protected Status status;
    protected TaskType taskType;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.taskType = TaskType.TASK;
    }

    public Task(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskType = TaskType.TASK;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(Integer id, String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.taskType = TaskType.TASK;
    }

    public Task(String value) { // по ТЗ 6
        String[] task = value.split(",");
        this.id = Integer.parseInt(task[0]);
        this.title = task[2];
        this.status = Status.valueOf(task[3]);
        this.description = task[4];
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return id + ",TASK," + title + "," + status + "," + description;
    }


}

