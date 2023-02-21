package tasks;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(int taskId, String title, String description, int epicId) {
        super(taskId, title, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


}
