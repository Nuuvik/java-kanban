package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InMemoryHistoryManagerTest {
    private HistoryManager manager;
    private int id = 0;

    private Task createTask() {
        return new Task(++id, Status.NEW, "Title", "Description", Instant.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {
        id = 0;
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = createTask();
        Task task2 = createTask();
        Task task3 = createTask();

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldMoveToEndTaskIfIsAlreadyPresent() {
        Task task1 = createTask();
        Task task2 = createTask();
        Task task3 = createTask();

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task1);

        assertEquals(List.of(task2, task3, task1), manager.getHistory());
    }

    @Test
    public void shouldRemoveTask() {
        Task task1 = createTask();
        Task task2 = createTask();
        Task task3 = createTask();

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task2.getId());

        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = createTask();

        manager.add(task);
        manager.remove(task.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = createTask();

        manager.add(task);
        manager.remove(0);

        assertEquals(List.of(task), manager.getHistory());
    }

    @Test
    public void shouldClearHistory() {
        manager.add(createTask());
        manager.add(createTask());
        manager.add(createTask());

        manager.clear();

        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldConvertEmptyHistoryToString() {
        assertEquals("0", FileBackedTasksManager.historyToString(manager));
    }

    @Test
    public void shouldConvertHistoryToString() {
        Task task1 = new Task(1, Status.NEW, "Title", "Description", Instant.now(), 0);
        Task task2 = new Task(2, Status.NEW, "Title", "Description", Instant.now(), 0);
        Task task3 = new Task(3, Status.NEW, "Title", "Description", Instant.now(), 0);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        assertEquals(task1.getId() + "," + task2.getId() + "," + task3.getId(), FileBackedTasksManager.historyToString(manager));
    }

    @Test
    public void shouldConvertEmptyStringToHistory() {
        assertEquals(Collections.EMPTY_LIST, FileBackedTasksManager.historyFromString("0"));
    }

    @Test
    public void shouldConvertStringToHistory() {
        assertEquals(List.of(1, 2, 3), FileBackedTasksManager.historyFromString("1,2,3"));
    }


}
