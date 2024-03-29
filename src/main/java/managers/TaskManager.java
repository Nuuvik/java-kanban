package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Task getSubtask(Integer id);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    Task updateTask(int id, Task task);

    Epic updateEpic(int id, Epic epic);

    Subtask updateSubtask(int id, Subtask subtask);

    void removeTask(int id);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();


}
