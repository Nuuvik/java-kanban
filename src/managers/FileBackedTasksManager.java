package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager  {
    private final Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public FileBackedTasksManager(
            Path path,
            Map<Integer, Task> tasks,
            Map<Integer, Epic> epics,
            Map<Integer, Subtask> subtasks,
            HistoryManager history,
            int startId
    ) {
        this.path = path;
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
        this.history = history;
        this.id = startId;
        this.prioritizedTasks.addAll(tasks.values());
        this.prioritizedTasks.addAll(epics.values());
        this.prioritizedTasks.addAll(subtasks.values());
    }

    public static FileBackedTasksManager loadFromFile(Path path) {
        Map<Integer, Task> allTasks = new HashMap<>();
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();
        Map<Integer, Subtask> subtasks = new HashMap<>();
        HistoryManager historyManager = new InMemoryHistoryManager();
        int startId = 0;

        try {
            String file = Files.readString(path);
            String[] rows = file.split(System.lineSeparator());

            for (int i = 1; i < rows.length - 2; i++) {
                Task task = fromString(rows[i]);
                String type = rows[i].split(",")[1];

                if (task.getId() > startId) startId = task.getId();

                allTasks.put(task.getId(), task);

                switch (TaskType.valueOf(type)) {
                    case TASK -> {

                        tasks.put(task.getId(), task);
                    }
                    case EPIC -> {

                        epics.put(task.getId(), (Epic) task);
                    }
                    case SUBTASK -> {
                        Subtask subtask = (Subtask) task;

                        subtasks.put(task.getId(), subtask);
                        epics.get(subtask.getEpicId()).addSubtask(subtask);
                    }
                }
            }

            List<Integer> historyList = historyFromString(rows[rows.length - 1]);

            historyList.forEach(id -> historyManager.add(allTasks.get(id)));

        } catch (IOException err) {
            throw new ManagerSaveException("Ошибка при восстановлении данных");
        }

        return new FileBackedTasksManager(path, tasks, epics, subtasks, historyManager, startId);
    }

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Task updateTask(int id, Task task) {
        Task updatedTask = super.updateTask(id, task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(int id, Epic epic) {
        Epic updatedEpic = super.updateEpic(id, epic);
        save();
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(int id, Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(id, subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    protected void save() {
        try {
            String head = "id,type,name,status,description,start time,duration,epic" + System.lineSeparator();
            String data = head +
                    tasksToString(this) +
                    System.lineSeparator() +
                    historyToString(history);

            Files.writeString(path, data);
        } catch (IOException err) {
            throw new ManagerSaveException("Ошибка при сохранении данных");
        }
    }

    static private Task fromString(String value) {
        String[] splittedValue = value.split(",");
        int id = Integer.parseInt(splittedValue[0]);
        String type = splittedValue[1];
        String title = splittedValue[2];
        Status status = Status.valueOf(splittedValue[3]);
        String description = splittedValue[4];
        Instant startTime = Instant.ofEpochMilli(Long.parseLong(splittedValue[5]));
        long duration = Long.parseLong(splittedValue[6]);
        Integer epicId = TaskType.valueOf(type) == TaskType.SUBTASK ? Integer.parseInt(splittedValue[7]) : null;

        return switch (TaskType.valueOf(type)) {
            case TASK -> new Task(id, status, title, description, startTime, duration);
            case EPIC -> new Epic(id, status, title, description, startTime, duration);
            case SUBTASK -> new Subtask(id, status, title, description, epicId, startTime, duration);
        };
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }

        if (sb.length() == 0) {
            sb.append(0);
        } else {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        if (value.equals("0")) return new ArrayList<>();

        List<Integer> history = new ArrayList<>();
        Arrays.stream(value.split(",")).forEach(id -> history.add(Integer.parseInt(id)));

        return history;
    }

    public static String tasksToString(TaskManager manager) {
        StringBuilder sb = new StringBuilder();
        List<Task> tasksList = new ArrayList<>();

        tasksList.addAll(manager.getAllTasks());
        tasksList.addAll(manager.getAllEpics());
        tasksList.addAll(manager.getAllSubtasks());

        for (Task task : tasksList) {
            sb.append(task).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
