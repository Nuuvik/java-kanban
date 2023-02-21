package main;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int zeroId = 1;  //генерация id

    // 2.1 Получение списка всех задач (tasks)
    public ArrayList<Task> getListOfTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Integer task : tasks.keySet()) {
            list.add(tasks.get(task));
        }
        return list;
    }

    // 2.1 Получение списка всех задач (epics)
    public ArrayList<Epic> getListOfEpics() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Integer epic : epics.keySet()) {
            list.add(epics.get(epic));
        }
        return list;
    }

    // 2.1 Получение списка всех задач (subtasks)
    public ArrayList<Subtask> getListOfSubtasks() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subtask : subtasks.keySet()) {
            list.add(subtasks.get(subtask));
        }
        return list;
    }

    //2.3 Получение по идентификатору (tasks)
    public void getTaskById(int id) {
        System.out.println(tasks.get(id));
    }

    //2.3 Получение по идентификатору (epics)
    public void getEpicById(int id) {
        System.out.println(epics.get(id));
    }

    //2.3 Получение по идентификатору (subtasks)
    public void getSubtaskById(int id) {
        System.out.println(subtasks.get(id));
    }

    //2.4 Создание (tasks)
    public Integer createTask(Task task) {
        task.setId(zeroId++);
        task.setStatus("NEW");
        tasks.put(task.getId(), task);
        return zeroId - 1;
    }

    //2.4 Создание (epics)
    public Integer createEpic(Epic epic) {
        epic.setId(zeroId++);
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        return zeroId - 1;
    }

    //2.4 Создание (subtasks)
    public Integer createSubtask(Subtask subtask) {
        subtask.setId(zeroId++);
        subtask.setStatus("NEW");
        subtasks.put(subtask.getId(), subtask);
        ArrayList<Integer> newSubtaskList = new ArrayList<>();
        Epic epic = epics.get(subtask.getEpicId());
        newSubtaskList.add(subtask.getId());
        epic.setSubtaskIds(newSubtaskList);
        epics.put(subtask.getEpicId(), epic);
        return zeroId - 1;

    }

    //2.5 Обновление (tasks)
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            task.setStatus("IN_PROGRESS");
            tasks.put(task.getId(), task);
        }
    }

    //2.5 Обновление (subtasks)
    public void updateSubtask(Subtask subtask) {
        subtask.setStatus("IN_PROGRESS");
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateSubtasksInEpic(epic);
        updateEpicStatus(subtask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        epic.setStatus("IN_PROGRESS");
        Epic oldEpic = epics.get(epic.getId());
        for (Integer idSubtask : oldEpic.getSubtaskIds()) {
            deleteSubtask(idSubtask);
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    // 2.6 Удаление по идентификатору (tasks)
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    // 2.6 Удаление по идентификатору (epics)
    public void deleteEpic(int id) {
        for (Integer key : subtasks.keySet()) {
            if (Objects.equals(subtasks.get(key).getEpicId(), id)) {
                subtasks.remove(key);
                if (subtasks.size() <= 1) {
                    subtasks.clear();

                }
            }
        }
        epics.remove(id);
    }

    // 2.6 Удаление по идентификатору (subtasks)
    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    //3.1 Получение списка всех подзадач определённого эпика
    public void getSubtaskListByEpicId(Integer id) {
        ArrayList<Subtask> currentList = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtaskIds()) {
            currentList.add(subtasks.get(currentSubtask));
        }
        System.out.println(currentList);
    }


    //обновление id subtask в epic (нужен для корректного создания subtask)
    private void updateSubtasksInEpic(Epic epic) {
        ArrayList<Integer> listOfSubtaskId = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            listOfSubtaskId.add(subtask.getId());
        }
        epic.setSubtaskIds(listOfSubtaskId);
    }

    private void updateEpicStatus(Integer id) {
        Integer countOfNEW = 0;
        Integer countOfDONE = 0;
        Integer countOfSubtask = epics.get(id).getSubtaskIds().size();
        for (Integer subtaskID : epics.get(id).getSubtaskIds()) {
            if (Objects.equals(subtasks.get(subtaskID).getStatus(), "NEW")) {
                countOfNEW++;
            } else if (Objects.equals(subtasks.get(subtaskID).getStatus(), "DONE")) {
                countOfDONE++;
            }
        }
        if (countOfNEW.equals(countOfSubtask) || (countOfSubtask == 0)) {
            Epic epic = epics.get(id);
            epic.setStatus("NEW");
            epics.put(id, epic);
        } else if (countOfDONE.equals(countOfSubtask)) {
            Epic epic = epics.get(id);
            epic.setStatus("DONE");
            epics.put(id, epic);
        } else {
            Epic epic = epics.get(id);
            epic.setStatus("IN_PROGRESS");
            epics.put(id, epic);
        }
    }


}
