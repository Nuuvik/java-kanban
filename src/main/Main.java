package main;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();


        Integer Task1 = manager.createTask(new Task("Первый обычный таск",""));
        Integer Task2 = manager.createTask(new Task("Второй обычный таск","qwe"));

        Integer Epic1 = manager.createEpic(new Epic("first epic","--"));
        Integer Sub1 = manager.createSubtask(new Subtask("firs sub","^_^",Epic1));
        Integer Sub2 = manager.createSubtask(new Subtask("sec sub","^_^",Epic1));

        Integer Epic2 = manager.createEpic(new Epic("sec epic","--"));
        Integer Sub3 = manager.createSubtask(new Subtask("third sub","^_^",Epic2));


        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getListOfSubtasks());
        System.out.println();

        manager.getSubtaskListByEpicId(Epic1);
        manager.getSubtaskById(Sub2);
        manager.getEpicById(Epic1);
        manager.getTaskById(Task1);

        manager.updateTask(new Task(Task1, "Новая первая таска", ""));
        manager.updateSubtask(new Subtask(Sub3, "Новый первый сабтаск","--=-", Epic2));

        manager.updateEpic(new Epic(Epic2,"ff", "Новый второй эпик"));

        //Выводим все задачи
        System.out.println();
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getListOfSubtasks());

        //Удаляем вторую задачу и эпик
        manager.deleteTask(Task2);
        manager.deleteEpic(Epic2);
        manager.deleteSubtask(Sub1);

        System.out.println();
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpics());
        System.out.println(manager.getListOfSubtasks());

     
    }


}
