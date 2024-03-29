package tasks;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String title, String description, Integer epicId, Instant startTime, long duration) {
        super(title, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Integer id, Status status, String title, String description, Integer epicId, Instant startTime, long duration) {
        super(id, status, title, description, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }


    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return id + "," +
                TaskType.SUBTASK + "," +
                title + "," +
                status + "," +
                description + "," +
                startTime.toEpochMilli() + "," +
                duration + "," +
                epicId;
    }
}
