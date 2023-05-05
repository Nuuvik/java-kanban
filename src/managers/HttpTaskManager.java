package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.ManagerSaveException;
import server.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.GsonUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = GsonUtils.getInstance();
    private final KVTaskClient taskClient;

    public HttpTaskManager(URI uri) {
        super(null);
        try {
            taskClient = new KVTaskClient(uri);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка при подключении к KVServer");
        }
    }

    private <T> T loadData(String key, TypeToken<T> typeToken) throws IOException, InterruptedException {
        String jsonData = taskClient.load(key);
        return gson.fromJson(jsonData, typeToken.getType());
    }

    public void load() {
        try {
            Map<Integer, Task> tasks = loadData("tasks", new TypeToken<HashMap<Integer, Task>>() {
            });
            Map<Integer, Epic> epics = loadData("epics", new TypeToken<HashMap<Integer, Epic>>() {
            });
            Map<Integer, Subtask> subtasks = loadData("subtasks", new TypeToken<HashMap<Integer, Subtask>>() {
            });
            List<Task> historyList = loadData("history", new TypeToken<List<Task>>() {
            });

            HistoryManager history = new InMemoryHistoryManager();
            historyList.forEach(history::add);

            int startId = Integer.parseInt(loadData("startId", new TypeToken<String>() {
            }));

            this.tasks = tasks;
            this.epics = epics;
            this.subtasks = subtasks;
            this.history = history;
            this.prioritizedTasks.addAll(tasks.values());
            this.prioritizedTasks.addAll(epics.values());
            this.prioritizedTasks.addAll(subtasks.values());
            this.id = startId;
        } catch (IOException | InterruptedException exception) {
            System.out.println("Ошибка при восстановлении данных");
            /* реализую после прохождения соответствующей темы :)
        Обработка исключений: вместо того чтобы выводить сообщения об ошибках напрямую на консоль,
         рассмотрите возможность использования логгера (например, java.util.logging.Logger или org.slf4j.Logger)
          для логирования исключений и сообщений об ошибках. Это позволит контролировать уровень логирования
           и более гибко настраивать его для различных сред выполнения.
         */
        }
    }

    @Override
    protected void save() {
        try {
            taskClient.put("tasks", gson.toJson(tasks));
            taskClient.put("epics", gson.toJson(epics));
            taskClient.put("subtasks", gson.toJson(subtasks));
            taskClient.put("history", gson.toJson(history.getHistory()));
            taskClient.put("startId", gson.toJson(id));
        } catch (IOException | InterruptedException err) {
            throw new ManagerSaveException("Ошибка при сохранении данных");
        }
    }
}