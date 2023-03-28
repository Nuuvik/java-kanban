package managers;

import tasks.*;

import static tasks.Status.NEW;
import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();


        Integer simpleTask1 = taskManager.createTask(new Task("Первый обычный таск", "", NEW));
        Integer simpleTask2 = taskManager.createTask(new Task("Второй обычный таск", "", NEW));


        Integer epic1 = taskManager.createEpic(new Epic("Мой первый эпик", ""));
        Integer subtask1 = taskManager.createSubtask(new Subtask("Первый сабтаск первого эпика", "",
                NEW, epic1));
        Integer subtask2 = taskManager.createSubtask(new Subtask("Второй сабтаск первого эпика", "", NEW,
                epic1));
        Integer subtask3 = taskManager.createSubtask(new Subtask("Третий сабтаск первого эпика", "", NEW,
                epic1));

        Integer epic2 = taskManager.createEpic(new Epic("Второй эпик", ""));


        taskManager.getTaskById(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getEpicById(3);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getTaskById(1);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getSubtaskById(4);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getTaskById(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getSubtaskById(6);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.deleteTask(2);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());


        taskManager.deleteEpic(3);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());


    }


}
