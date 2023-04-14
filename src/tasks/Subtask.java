package tasks;

public class Subtask extends Task {
    private Epic epic;


    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }

    public Subtask(Integer id, String title, String description, Status status, Epic epic) {
        super(id, title, description, status);
        this.epic = epic;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, epic.getId());
    }
}
