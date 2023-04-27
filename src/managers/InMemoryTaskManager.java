package managers;

import exceptions.TaskOverlapAnotherTaskException;
import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager history = Managers.getDefaultHistory();
    protected int id = 0;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTask(Integer id) {
        Task task = tasks.get(id);
        history.add(task);

        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = epics.get(id);
        history.add(epic);

        return epic;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        history.add(subtask);

        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(getNextId());
        addNewPrioritizedTask(task);
        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);

        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());

        subtask.setId(getNextId());
        addNewPrioritizedTask(subtask);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask);
        epic.updateEpicStatus(subtasks);

        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        addNewPrioritizedTask(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        addNewPrioritizedTask(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.updateEpicStatus(subtasks);
    }

    @Override
    public void removeTask(int taskId) {
        prioritizedTasks.removeIf(task -> task.getId() == taskId);
        tasks.remove(taskId);
        history.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic == null) return;

        epic.getSubtasks().forEach(subtaskId -> {
            prioritizedTasks.removeIf(task -> task.getId().equals(subtaskId));
            subtasks.remove(subtaskId);
            history.remove(subtaskId);
        });

        epics.remove(epicId);
        history.remove(epicId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask == null) return;

        Epic epic = epics.get(subtask.getEpicId());

        epic.removeSubtask(subtask);
        prioritizedTasks.remove(subtask);
        subtasks.remove(subtaskId);
        epic.updateEpicStatus(subtasks);
        history.remove(subtaskId);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        history.clear();
        prioritizedTasks.clear();
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> subtasksOfEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);

        for (Integer subtaskId : epic.getSubtasks()) {
            subtasksOfEpic.add(subtasks.get(subtaskId));
        }

        return subtasksOfEpic;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = task.getStartTime().isBefore(tasks.get(i - 1).getEndTime());

            if (taskHasIntersections) {
                throw new TaskOverlapAnotherTaskException("Задачи #" + task.getId() + " и #" + tasks.get(i - 1) + "пересекаются");
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        List<Task> prioritizedTasksList = new ArrayList<>(prioritizedTasks);
        return prioritizedTasksList;
    }

    private int getNextId() {
        return ++id;
    }
}