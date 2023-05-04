package managers;


import java.net.URI;

public class Managers {


    public static TaskManager getDefault(URI uri) {
        return new HttpTaskManager(uri);
    }

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
