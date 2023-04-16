package managers;

import exceptions.*;
import tasks.*;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static final String fileName = "./data/data.csv";

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(fileName));
        for (Task task : fileBackedTasksManager.getListOfTasks()) {
            System.out.println(task);
        }
        for (Epic epic : fileBackedTasksManager.getListOfEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : fileBackedTasksManager.getListOfSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();
        for (Task task : fileBackedTasksManager.getHistory()) {
            System.out.print(task.getId() + ",");
        }

        System.out.println("\n------UPDATE------");
        testsSixSprint();
    }

    public static void testsSixSprint() {
        Task task1 = new Task("Task1", "Task1", Status.NEW);
        Task task2 = new Task("Task2", "Task2", Status.DONE);

        Epic epic1 = new Epic("epic1", "epic1");
        Epic epic2 = new Epic("epic2", "epic2");

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1", Status.NEW, epic1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, epic1);
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3", Status.NEW, epic2);

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);

        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);

        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);

        fileBackedTasksManager.getTaskById(1);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getTaskById(2);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getEpicById(3);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getEpicById(4);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getEpicById(4);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.deleteEpic(4);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getSubtaskById(5);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getSubtaskById(6);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
        fileBackedTasksManager.getTaskById(1);
        System.out.println();
        System.out.println(fileBackedTasksManager.getHistory().toString());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String taskLine = bufferedReader.readLine();
                if (!taskLine.isEmpty()) {
                    tasksFromString(taskLine, fileBackedTasksManager);
                } else {
                    String historyLine = bufferedReader.readLine();
                    historyFromString(historyLine, fileBackedTasksManager);
                }
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Не удалось загрузить из файла ");
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
        return fileBackedTasksManager;
    }

    public static void tasksFromString(String taskLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] taskFields = taskLine.split(",");
        if (taskFields[1].equals(String.valueOf(TaskType.TASK))) {
            Task task = new Task(taskLine);
            fileBackedTasksManager.addTaskFromFile(task);
        }
        if (taskFields[1].equals(String.valueOf(TaskType.EPIC))) {
            Epic epic = new Epic(taskLine);
            fileBackedTasksManager.addEpicFromFile(epic);
        }
        if (taskFields[1].equals(String.valueOf(TaskType.SUBTASK))) {
            Subtask subtask = new Subtask(taskLine);
            fileBackedTasksManager.addSubtaskFromFile(subtask);
        }
    }

    public static void historyFromString(String historyLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] historyFields = historyLine.split(",");
        for (String field : historyFields) {
            int id = Integer.parseInt(field);
            fileBackedTasksManager.getTaskById(id);
            fileBackedTasksManager.getEpicById(id);
            fileBackedTasksManager.getSubtaskById(id);
        }
    }


    public void addTaskFromFile(Task taskFromFile) {
        tasks.put(taskFromFile.getId(), taskFromFile);
    }

    public void addEpicFromFile(Epic epicFromFile) {
        epics.put(epicFromFile.getId(), epicFromFile);
    }

    public void addSubtaskFromFile(Subtask subtaskFromFile) {
        subtasks.put(subtaskFromFile.getId(), subtaskFromFile);
    }


    public void save() {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getListOfTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Epic epic : getListOfEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getListOfSubtasks()) {
                fileWriter.write(subtask.toString() + "\n");
            }
            fileWriter.write("\n");
            for (Task task : getHistory()) {
                fileWriter.write(task.getId() + ",");
            }
        } catch (IOException e) {

            throw new ManagerSaveException("Не удалось сохранить в файл " + e.getMessage());
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

}

