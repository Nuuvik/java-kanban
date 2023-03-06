package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static tasks.Status.NEW;
import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int generatedId = 1;  //генерация id

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // 2.1 Получение списка всех задач (tasks)
    @Override
    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Integer task : tasks.keySet()) {
            list.add(tasks.get(task));
        }
        return list;
    }

    // 2.1 Получение списка всех задач (epics)
    @Override
    public ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Integer epic : epics.keySet()) {
            list.add(epics.get(epic));
        }
        return list;
    }

    // 2.1 Получение списка всех задач (subtasks)
    @Override
    public ArrayList<Subtask> getListOfSubtasks() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subtask : subtasks.keySet()) {
            list.add(subtasks.get(subtask));
        }
        return list;
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
    public Integer createTask(Task task) {
        task.setId(generatedId++);
        tasks.put(task.getId(), task);
        return generatedId - 1;
    }

    //2.4 Создание (epics)
    @Override
    public Integer createEpic(Epic epic) {
        epic.setId(generatedId++);
        ArrayList<Integer> arrayList = updateSubtasksInEpic(epic);
        epic.setSubtaskIds(arrayList);
        epics.put(epic.getId(), epic);
        return generatedId - 1;
    }

    //2.4 Создание (subtasks)
    @Override
    public Integer createSubtask(Subtask subtask) {
        subtask.setId(generatedId++);
        subtasks.put(subtask.getId(), subtask);
        ArrayList<Integer> newSubtaskList = updateSubtasksInEpic(epics.get(subtask.getEpicId()));
        final Epic epic = epics.get(subtask.getEpicId());
        newSubtaskList.add(subtask.getId());
        epic.setSubtaskIds(newSubtaskList);
        updateEpicStatus(subtask.getEpicId());
        epics.put(subtask.getEpicId(), epic);
        return generatedId - 1;

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
        final Epic epic = epics.get(subtask.getEpicId());
        updateSubtasksInEpic(epic);
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
    }

    // 2.6 Удаление по идентификатору (epics)
    @Override
    public void deleteEpic(int id) {
        for (Integer key : subtasks.keySet()) {
            if (Objects.equals(subtasks.get(key).getEpicId(), id)) {
                subtasks.remove(id);
            }
        }
        epics.remove(id);
    }

    // 2.6 Удаление по идентификатору (subtasks)
    @Override
    public void deleteSubtask(int id) {
        Subtask deletedId = subtasks.remove(id);
        updateEpicStatus(deletedId.getEpicId());
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


    private ArrayList<Integer> updateSubtasksInEpic(Epic epic) {
        ArrayList<Integer> listOfSubtaskId = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            listOfSubtaskId.add(subtask.getId());
        }
        epic.setSubtaskIds(listOfSubtaskId);

        return epic.getSubtaskIds();
    }


    private void updateEpicStatus(Integer id) {
        try {
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
        } catch (NullPointerException e) {

        }
    }


}
