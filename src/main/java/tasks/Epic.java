package tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasks = new ArrayList<>();
    private Instant endTime = Instant.ofEpochMilli(0);

    public Epic(String title, String description) {
        super(title, description, Instant.ofEpochMilli(0), 0);
    }

    public Epic(int id, Status status, String title, String description, Instant startTime, long duration) {
        super(title, description, startTime, duration);
        this.id = id;
        this.status = status;
        this.endTime = super.getEndTime();
    }

    public Epic(Epic epic) {
        this(epic.title, epic.description);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks.clear();
        this.subtasks.addAll(subtasks);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask.getId());
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask.getId());
    }

    public void updateEpicStatusAndTiming(Map<Integer, Subtask> allSubtasks) { //при добавлении новых параметров
        if (getSubtasks().isEmpty()) {                                         //измени название метода
            this.status = Status.NEW;
            return;
        }

        int newCount = 0;
        int doneCount = 0;
        long totalDuration = 0; //для расчета продолжительности всех сабтасков в эпике
        Instant startTime = allSubtasks.get(subtasks.get(0)).getStartTime();
        Instant endTime = allSubtasks.get(subtasks.get(0)).getEndTime();

        for (Integer subtaskId : getSubtasks()) {
            Subtask subtask = allSubtasks.get(subtaskId);
            if (subtask.getStatus() == Status.NEW) newCount += 1;
            if (subtask.getStatus() == Status.DONE) doneCount += 1;
            if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
            totalDuration += Duration.between(subtask.getStartTime(), subtask.getEndTime()).toMinutes(); //расчет
        }

        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = totalDuration;

        if (getSubtasks().size() == newCount) {
            this.status = Status.NEW;
            return;
        }

        if (getSubtasks().size() == doneCount) {
            this.status = Status.DONE;
            return;
        }

        this.status = Status.IN_PROGRESS;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return id + "," + TaskType.EPIC + "," + title + "," + status + "," + description + "," + startTime.toEpochMilli() + "," + duration;
    }
}
