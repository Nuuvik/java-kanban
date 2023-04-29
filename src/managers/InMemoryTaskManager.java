package managers;

import exceptions.TaskOverlapAnotherTaskException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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
        epic.updateEpicStatusAndTiming(subtasks);

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
        Epic oldEpic = epics.get(epic.getId());
        epic.setSubtasks(oldEpic.getSubtasks());

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        addNewPrioritizedTask(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.updateEpicStatusAndTiming(subtasks);
    }

    @Override
    public void removeTask(int taskId) {
        prioritizedTasks.removeIf(task -> task.getId().equals(taskId));
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
        epic.updateEpicStatusAndTiming(subtasks);
        history.remove(subtaskId);
    }

    @Override
    public void removeAllTasks() {
        for (Map.Entry<Integer, Task> removedTask : tasks.entrySet()) {
            prioritizedTasks.remove(removedTask.getValue());
            history.remove(removedTask.getKey());
        }
        tasks.clear();
    }


    @Override
    public void removeAllEpics() {
        for (Map.Entry<Integer, Epic> removedEpic : epics.entrySet()) {
            prioritizedTasks.remove(removedEpic.getValue());
            history.remove(removedEpic.getKey());
        }
        for (Map.Entry<Integer, Subtask> removedSubtask : subtasks.entrySet()) {
            prioritizedTasks.remove(removedSubtask.getValue());
            history.remove(removedSubtask.getKey());
        }
        epics.clear();
        subtasks.clear();
    }


    @Override
    public void removeAllSubtasks() {
        for (Map.Entry<Integer, Subtask> removedSubtask : subtasks.entrySet()) {
            prioritizedTasks.remove(removedSubtask.getValue());
            history.remove(removedSubtask.getKey());
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.updateEpicStatusAndTiming(subtasks);
        }
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
        validateTaskPriority(task);
        prioritizedTasks.add(task);
    }

    private void validateTaskPriority(Task newTask) {
        List<Task> tasks = getPrioritizedTasks();

        for (Task task : tasks) {
            boolean taskHasIntersections = newTask.getStartTime().isBefore(task.getEndTime()) &&
                    newTask.getEndTime().isAfter(task.getStartTime());

            if (taskHasIntersections) {
                throw new TaskOverlapAnotherTaskException("Задача #" + newTask.getId() + " пересекается с задачей #" +
                        task.getId());
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private int getNextId() {
        return ++id;
    }
}