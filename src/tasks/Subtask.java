package tasks;

public class Subtask extends Task {
    protected int epicId;
    protected Status status;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description);
        this.status = status;
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Status status, int epicId) {
        super(id, title, description);
        this.status = status;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


}
