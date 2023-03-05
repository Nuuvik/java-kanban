package tasks;

public class Subtask extends Task {
    protected int epicId;
    protected Status status;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }


}
