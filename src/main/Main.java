package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.GsonUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Поехали!");

        KVServer kvServer;
        HttpTaskServer taskServer;
        Gson gson = GsonUtils.getInstance();

        kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager manager = new HttpTaskManager(URI.create("http://localhost:8078"));
        taskServer = new HttpTaskServer(manager);
        HttpClient client = HttpClient.newHttpClient();
        Task task = new Task("Task", "Task description", Instant.ofEpochMilli(12345678), 25);
        HttpRequest taskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        client.send(taskCreateRequest, HttpResponse.BodyHandlers.ofString());

        Epic epic = new Epic("Epic", "Epic description");
        HttpRequest epicCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        int epicId = gson.fromJson(client.send(epicCreateRequest, HttpResponse.BodyHandlers.ofString()).body(), Epic.class).getId();

        Subtask subtask = new Subtask("Subtask", "Subtask description", epicId, Instant.ofEpochMilli(87654321), 25);
        HttpRequest subtaskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        client.send(subtaskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getTasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .GET()
                .build();
        HttpRequest getEpicsRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .GET()
                .build();
        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .GET()
                .build();

        List<Task> tasksResponse = gson.fromJson(
                client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Task>>() {
                }.getType()
        );

        List<Epic> epicsResponse = gson.fromJson(
                client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Epic>>() {
                }.getType()
        );

        List<Subtask> subtasksResponse = gson.fromJson(
                client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Subtask>>() {
                }.getType()
        );

        System.out.println(tasksResponse);
        System.out.println(epicsResponse);
        System.out.println(subtasksResponse);

    }


}
