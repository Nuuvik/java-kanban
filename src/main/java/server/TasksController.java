package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.*;
import tasks.*;
import utils.GsonUtils;


import java.io.IOException;
import java.io.OutputStream;

public class TasksController {
    private final TaskManager taskService;
    private final Gson gson = GsonUtils.getInstance();

    public TasksController(TaskManager taskService) {
        this.taskService = taskService;
    }

    public void getAllTasks(HttpExchange httpExchange) {
        send(httpExchange, taskService.getAllTasks());
    }

    public void getAllEpics(HttpExchange httpExchange) {
        send(httpExchange, taskService.getAllEpics());
    }

    public void getAllSubtasks(HttpExchange httpExchange) {
        send(httpExchange, taskService.getAllSubtasks());
    }

    public void getTaskById(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        send(httpExchange, taskService.getTask(id));
    }

    public void getEpicById(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        send(httpExchange, taskService.getEpic(id));
    }

    public void getSubtaskById(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        send(httpExchange, taskService.getSubtask(id));
    }

    public void getSubtasksByEpicId(HttpExchange httpExchange) {
        int epicId = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        send(httpExchange, taskService.getSubtasksByEpicId(epicId));
    }

    public void createTask(HttpExchange httpExchange) {
        Task task = parseBody(httpExchange, Task.class);

        if (task == null) return;

        send(httpExchange, taskService.createTask(task), 201);
    }

    public void createEpic(HttpExchange httpExchange) {
        Epic epic = parseBody(httpExchange, Epic.class);

        if (epic == null) return;

        send(httpExchange, taskService.createEpic(new Epic(epic)), 201);
    }

    public void createSubtask(HttpExchange httpExchange) {
        Subtask subtask = parseBody(httpExchange, Subtask.class);

        if (subtask == null) return;

        send(httpExchange, taskService.createSubtask(subtask), 201);
    }

    public void updateTask(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);

        Task task = parseBody(httpExchange, Task.class);

        if (task == null) return;

        send(httpExchange, taskService.updateTask(id, task), 200);
    }

    public void updateEpic(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);

        Epic epic = parseBody(httpExchange, Epic.class);

        if (epic == null) return;

        send(httpExchange, taskService.updateEpic(id, epic), 200);
    }

    public void updateSubtask(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);

        Subtask subtask = parseBody(httpExchange, Subtask.class);

        if (subtask == null) return;

        send(httpExchange, taskService.updateSubtask(id, subtask), 200);
    }

    public void removeAllTasks(HttpExchange httpExchange) {
        taskService.removeAllTasks();
        sendEmpty(httpExchange);
    }

    public void removeTask(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        taskService.removeTask(id);
        sendEmpty(httpExchange);
    }

    public void removeEpic(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        taskService.removeEpic(id);
        sendEmpty(httpExchange);
    }

    public void removeSubtask(HttpExchange httpExchange) {
        int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
        taskService.removeSubtask(id);
        sendEmpty(httpExchange);
    }

    public void getHistory(HttpExchange httpExchange) {
        send(httpExchange, taskService.getHistory());
    }

    private <T> T parseBody(HttpExchange httpExchange, Class<T> tClass) {
        try {
            String body = new String(httpExchange.getRequestBody().readAllBytes());
            return gson.fromJson(body, tClass);
        } catch (IOException exception) {
            send(httpExchange, exception.getMessage(), 400);
        }

        return null;
    }

    private void send(HttpExchange httpExchange, Object data) {
        String response = gson.toJson(data);

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void send(HttpExchange httpExchange, Object data, int code) {
        String response = gson.toJson(data);

        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(code, response.getBytes().length);
            os.write(response.getBytes());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void sendEmpty(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(204, -1);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}