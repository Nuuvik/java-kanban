package managers;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FileBackedTasksManagerTest extends TaskManagerTest {
    private final Path path = Path.of("data.csv");

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(path);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() {
        Task task = manager.createTask(createTask());
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));
        manager.getTask(task.getId());
        manager.getSubtask(subtask.getId());

        manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(List.of(task), manager.getAllTasks());
        assertEquals(List.of(epic), manager.getAllEpics());
        assertEquals(List.of(subtask), manager.getAllSubtasks());
        assertEquals(List.of(task, subtask), manager.getHistory());
    }

    @Test
    public void shouldCorrectlyLoadDataFromFileWithEmptyHistory() {
        Task task = manager.createTask(createTask());
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(List.of(task), manager.getAllTasks());
        assertEquals(List.of(epic), manager.getAllEpics());
        assertEquals(List.of(subtask), manager.getAllSubtasks());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldThrowManagerSaveExceptionIfFileDoesNotExist() {
        assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(path));
    }
}
