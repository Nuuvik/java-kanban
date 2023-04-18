package managers;

import tasks.*;

import static tasks.Status.NEW;
import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected static int generatedId = 1;  //генерация id

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // 2.1 Получение списка всех задач (tasks)
    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    // 2.1 Получение списка всех задач (epics)
    @Override
    public List<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    // 2.1 Получение списка всех задач (subtasks)
    @Override
    public List<Subtask> getListOfSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //2.3 Получение по идентификатору (tasks)
    @Override
    public Task getTaskById(int id) {
        final Task receivedId = tasks.get(id);
        historyManager.add(receivedId);
        return receivedId;
    }

    //2.3 Получение по идентификатору (epics)
    @Override
    public Epic getEpicById(int id) {
        final Epic receivedId = epics.get(id);
        historyManager.add(receivedId);
        return receivedId;
    }

    //2.3 Получение по идентификатору (subtasks)
    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask receivedId = subtasks.get(id);
        historyManager.add(receivedId);
        return receivedId;
    }

    //2.4 Создание (tasks)
    @Override
    public void createTask(Task task) {
        task.setId(generatedId++);
        tasks.put(task.getId(), task);

    }

    //2.4 Создание (epics)
    @Override
    public void createEpic(Epic epic) {
        epic.setId(generatedId++);
        epic.setStatus(NEW);
        epics.put(epic.getId(), epic);

    }

    //2.4 Создание (subtasks)
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generatedId++);
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
        updateEpicStatus(subtask.getEpicId());


    }

    //2.5 Обновление (tasks)
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    //2.5 Обновление (subtasks)
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        updateEpicStatus(subtask.getEpicId());
    }

    //2.5 Обновление (epics)
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    // 2.6 Удаление по идентификатору (tasks)
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id); // удаление из истории
    }

    // 2.6 Удаление по идентификатору (epics)
    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id); // удаление из истории
    }

    // 2.6 Удаление по идентификатору (subtasks)
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            final Epic epic = subtasks.get(id).getEpic();
            epic.getSubtaskIds().remove((Integer) id);
            updateEpicStatus(epic.getId());
            subtasks.remove(id);
            historyManager.remove(id); // удаление из истории
        }
    }

    //3.1 Получение списка всех подзадач определённого эпика
    @Override
    public ArrayList<Subtask> getSubtaskListByEpicId(Integer id) {
        ArrayList<Subtask> currentList = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtaskIds()) {
            currentList.add(subtasks.get(currentSubtask));
        }
        return currentList;
    }

    //удаление всех tasks
    @Override
    public void deleteAllTasks() {
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
        }
        tasks.clear();
    }

    //удаление всех epics
    @Override
    public void deleteAllEpics() {
        for (Integer task : epics.keySet()) {
            historyManager.remove(task);
        }
        epics.clear();
        for (Integer task : subtasks.keySet()) {
            historyManager.remove(task);
        }
        subtasks.clear();
    }

    //удаление всех subtasks
    @Override
    public void deleteAllSubtasks() {
        for (Integer task : subtasks.keySet()) {
            historyManager.remove(task);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
        }
        for (Integer epic : epics.keySet()) {
            updateEpicStatus(epic);
        }


    }


    private void updateEpicStatus(Integer id) {
        Integer countOfNew = 0;
        Integer countOfDone = 0;
        Integer countOfSubtask = epics.get(id).getSubtaskIds().size();
        for (Integer subtaskID : epics.get(id).getSubtaskIds()) {
            if (subtasks.get(subtaskID).getStatus() == NEW) {
                countOfNew++;
            } else if (subtasks.get(subtaskID).getStatus() == DONE) {
                countOfDone++;
            }
        }
        if (countOfNew.equals(countOfSubtask) || (countOfSubtask == 0)) {
            Epic epic = epics.get(id);
            epic.setStatus(NEW);
            epics.put(id, epic);
        } else if (countOfDone.equals(countOfSubtask)) {
            Epic epic = epics.get(id);
            epic.setStatus(DONE);
            epics.put(id, epic);
        } else {
            Epic epic = epics.get(id);
            epic.setStatus(IN_PROGRESS);
            epics.put(id, epic);
        }
    }


}
