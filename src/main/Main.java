package main;

import managers.*;
import tasks.*;


import static tasks.Status.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();


        taskManager.createTask(new Task("Task 1", "qwe", DONE));
        taskManager.createTask(new Task("Task 2", "wwe", NEW));
        taskManager.createTask(new Task("Task 3", "www", IN_PROGRESS));
        taskManager.createTask(new Task("Task 4", "qqq", DONE));


        Epic epic1 = new Epic("Epic 1", "rty");
        taskManager.createEpic(epic1);
        taskManager.createSubtask(new Subtask("Sub 1.1", "sds", IN_PROGRESS, epic1));
        taskManager.createSubtask(new Subtask("Sub 1.2", "sdddds", NEW, epic1));

        taskManager.createEpic(new Epic("Epic 2", "tyty"));


        taskManager.getTaskById(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getEpicById(5);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getTaskById(1);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getSubtaskById(6);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getTaskById(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getSubtaskById(7);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getTaskById(4);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.deleteTask(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.deleteSubtask(6);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        System.out.println(taskManager.getEpicById(5));


    }


}
