package managers;

import tasks.*;

import static tasks.Status.NEW;
import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();

        //Создаем две таски
        Integer simpleTask1 = taskManager.createTask(new Task("Первый обычный таск", "", NEW));
        Integer simpleTask2 = taskManager.createTask(new Task("Второй обычный таск", "", NEW));

        //Создаем эпик с двумя новыми сабтасками
        Integer epic1 = taskManager.createEpic(new Epic("Мой первый эпик", ""));
        Integer subtask1 = taskManager.createSubtask(new Subtask("Первый сабтаск первого эпика", "", NEW, epic1));
        Integer subtask2 = taskManager.createSubtask(new Subtask("Второй сабтаск первого эпика", "", NEW, epic1));

        //Создаем второй эпик с одной сабтаской
        Integer epic2 = taskManager.createEpic(new Epic("Второй эпик", ""));
        Integer subtask3 = taskManager.createSubtask(new Subtask("Первый сабтаск второго эпика", "", NEW, epic2));

        taskManager.getTaskById(1);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getEpicById(3);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        taskManager.getSubtaskById(4);
        System.out.println("Печатаем историю поиска");
        System.out.println("Истоия" + taskManager.getHistory());

        System.out.println("Печатаем все задачи");
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        //Обновляем первый таск, сабтаск первого эпика и обновляем второй эпик
        taskManager.updateTask(new Task(simpleTask1, "Новая первая таска", "", IN_PROGRESS));
        taskManager.updateSubtask(new Subtask(subtask1, "Новый сабтаск", "", DONE, epic1));
        taskManager.updateEpic(new Epic(epic2, "Новый второй эпик", ""));

        System.out.println("Печатаем все задачи");
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfSubtasks());

        System.out.println("CHECKING EPIC & SUBTASKS STATUS");

        taskManager.updateSubtask(new Subtask(subtask1, "Новый first сабтаск", "", NEW, epic1));
        taskManager.updateSubtask(new Subtask(subtask2, "Новый second сабтаск", "", NEW, epic1));

        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getSubtaskById(5));


    }


}
