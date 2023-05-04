package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;


public class TasksRequests implements HttpHandler {
    TasksController tasksController;

    public TasksRequests(TaskManager taskService) {
        tasksController = new TasksController(taskService);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        Requests requests = new Requests(httpExchange);

        requests.get("/tasks/task", tasksController::getAllTasks);
        requests.get("/tasks/epic", tasksController::getAllEpics);
        requests.get("/tasks/subtask", tasksController::getAllSubtasks);
        requests.get("/tasks/task/:id", tasksController::getTaskById);
        requests.get("/tasks/epic/:id", tasksController::getEpicById);
        requests.get("/tasks/subtask/:id", tasksController::getSubtaskById);
        requests.get("/tasks/epic/:id/subtasks", tasksController::getSubtasksByEpicId);
        requests.get("/tasks/history", tasksController::getHistory);

        requests.post("/tasks/task", tasksController::createTask);
        requests.post("/tasks/epic", tasksController::createEpic);
        requests.post("/tasks/subtask", tasksController::createSubtask);

        requests.put("/tasks/task/:id", tasksController::updateTask);
        requests.put("/tasks/epic/:id", tasksController::updateEpic);
        requests.put("/tasks/subtask/:id", tasksController::updateSubtask);

        requests.delete("/tasks", tasksController::removeAllTasks);
        requests.delete("/tasks/task/:id", tasksController::removeTask);
        requests.delete("/tasks/epic/:id", tasksController::removeEpic);
        requests.delete("/tasks/subtask/:id", tasksController::removeSubtask);
    }
}