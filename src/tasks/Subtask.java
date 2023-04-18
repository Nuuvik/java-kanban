package tasks;

public class Subtask extends Task {
    private Epic epic;
    private Integer epicId;


    public Subtask(Integer id, String title, String description, Status status, Epic epic) {
        super(id, title, description, status);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }


    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(String title, String description, Status status, Integer epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String value) { // по ТЗ 6
        super(value);
        String[] subTask = value.split(",");
        this.epicId = Integer.parseInt(subTask[5]);

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Epic getEpic() {
        return epic;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + ",SUBTASK," + title + "," + status + "," + description + "," + epicId;
    }
}
