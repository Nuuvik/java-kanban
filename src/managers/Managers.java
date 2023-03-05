package managers;

import tasks.Task;

import java.util.List;

public class Managers {
    TaskManager taskManager = new InMemoryTaskManager();
    HistoryManager historyManager = new InMemoryHistoryManager();

    TaskManager getDefault() {
        return taskManager;
    }

    List<Task> getDefaultHistory() {
        return historyManager.getHistory();
    }
}
