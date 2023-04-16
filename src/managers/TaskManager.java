package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    // 2.1 Получение списка всех задач (tasks)
    List<Task> getListOfTasks();

    // 2.1 Получение списка всех задач (epics)
    List<Epic> getListOfEpics();

    // 2.1 Получение списка всех задач (subtasks)
    List<Subtask> getListOfSubtasks();

    //2.3 Получение по идентификатору (tasks)
    Task getTaskById(int id);

    //2.3 Получение по идентификатору (epics)
    Epic getEpicById(int id);

    //2.3 Получение по идентификатору (subtasks)
    Subtask getSubtaskById(int id);

    //2.4 Создание (tasks)
    void createTask(Task task);

    //2.4 Создание (epics)
    void createEpic(Epic epic);

    //2.4 Создание (subtasks)
    void createSubtask(Subtask subtask);

    //2.5 Обновление (tasks)
    void updateTask(Task task);

    //2.5 Обновление (subtasks)
    void updateSubtask(Subtask subtask);

    //2.5 Обновление (epics)
    void updateEpic(Epic epic);

    // 2.6 Удаление по идентификатору (tasks)
    void deleteTask(int id);

    // 2.6 Удаление по идентификатору (epics)
    void deleteEpic(int id);

    // 2.6 Удаление по идентификатору (subtasks)
    void deleteSubtask(int id);

    //3.1 Получение списка всех подзадач определённого эпика
    ArrayList<Subtask> getSubtaskListByEpicId(Integer id);

    //удаление всех tasks
    void deleteAllTasks();

    //удаление всех epics
    void deleteAllEpics();

    //удаление всех subtasks
    void deleteAllSubtasks();

    List<Task> getHistory();


}
