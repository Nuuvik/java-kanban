package managers;

import exceptions.TaskOverlapAnotherTaskException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {
    protected TaskManager manager;

    protected Task createTask() {
        return new Task("Title", "Description", Instant.now(), 0);
    }

    protected Epic createEpic() {
        return new Epic("Title", "Description");
    }

    protected Subtask createSubtask(Epic epic) {
        return new Subtask("Title", "Description", epic.getId(), Instant.now(), 0);
    }

    @Test
    public void shouldCreateTask() {
        Task task = manager.createTask(createTask());

        List<Task> tasks = manager.getAllTasks();

        assertNotNull(task.getId());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldCreateEpic() {
        Epic epic = manager.createEpic(createEpic());

        List<Epic> epics = manager.getAllEpics();

        assertNotNull(epic.getId());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtasks());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void shouldCreateSubtask() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtask.getId());
        assertEquals(epic.getId(), subtask.getEpicId());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(List.of(subtask), subtasks);
        assertEquals(List.of(subtask.getId()), epic.getSubtasks());
    }


    @Test
    public void shouldNotCreateSubtaskWithInvalidEpicId() {
        assertThrows(NullPointerException.class, () -> {
            Epic epic = manager.createEpic(createEpic());
            Subtask subtask = createSubtask(epic);
            subtask.setEpicId(333);
            manager.createSubtask(subtask);
        });
    }


    @Test
    public void shouldUpdateTask() {
        Task task = manager.createTask(createTask());
        Task updatedTask = new Task(1, Status.IN_PROGRESS, "Updated Task", "Updated Task desc", Instant.ofEpochMilli(-386310686000L), 30);
        manager.updateTask(updatedTask);
        Task retrievedTask = manager.getTask(task.getId());
        assertEquals("Updated Task", retrievedTask.getTitle());
        assertEquals("Updated Task desc", retrievedTask.getDescription());
        assertEquals(Instant.ofEpochMilli(-386310686000L), retrievedTask.getStartTime());
        assertEquals(Status.IN_PROGRESS, retrievedTask.getStatus());
        assertEquals(30, retrievedTask.getDuration());

    }

    @Test
    public void shouldNotUpdateTaskWithInvalidId() {
        Task task = manager.createTask(createTask());
        Task updatedTask = new Task(999, Status.IN_PROGRESS, "Updated Task", "Updated Task desc", Instant.ofEpochMilli(-386310686000L), 30);
        manager.updateTask(updatedTask);
        Task retrievedTask = manager.getTask(task.getId());
        assertNotEquals("Updated Task", retrievedTask.getTitle());
        assertNotEquals("Updated Task desc", retrievedTask.getDescription());
        assertNotEquals(Instant.ofEpochMilli(-386310686000L), retrievedTask.getStartTime());
        assertNotEquals(Status.IN_PROGRESS, retrievedTask.getStatus());
        assertNotEquals(30, retrievedTask.getDuration());

    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = manager.createEpic(createEpic());
        Epic updatedEpic = new Epic(1, Status.IN_PROGRESS, "Updated Epic", "Updated Epic desc", Instant.ofEpochMilli(-386310666000L), 30);
        manager.updateEpic(updatedEpic);
        Epic retrievedEpic = manager.getEpic(epic.getId());
        assertEquals("Updated Epic", retrievedEpic.getTitle());
        assertEquals("Updated Epic desc", retrievedEpic.getDescription());
        assertEquals(Instant.ofEpochMilli(-386310666000L), retrievedEpic.getStartTime());
        assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus());
        assertEquals(30, retrievedEpic.getDuration());
    }

    @Test
    public void shouldNotUpdateEpicWithInvalidId() {
        Epic epic = manager.createEpic(createEpic());
        Epic updatedEpic = new Epic(999, Status.IN_PROGRESS, "Updated Epic", "Updated Epic desc",
                Instant.ofEpochMilli(-386310676000L), 30);
        epic.setSubtasks(updatedEpic.getSubtasks());
        manager.updateEpic(updatedEpic);
        Epic retrievedEpic = manager.getEpic(epic.getId());
        assertNotEquals("Updated Epic", retrievedEpic.getTitle());
        assertNotEquals("Updated Epic desc", retrievedEpic.getDescription());
        assertNotEquals(Instant.ofEpochMilli(-386310676000L), retrievedEpic.getStartTime());
        assertNotEquals(Status.IN_PROGRESS, retrievedEpic.getStatus());
        assertNotEquals(30, retrievedEpic.getDuration());

    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, manager.getSubtask(subtask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToDone() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);

        assertEquals(Status.DONE, manager.getSubtask(subtask.getId()).getStatus());
        assertEquals(Status.DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask1 = manager.createSubtask(createSubtask(epic));
        Subtask subtask2 = manager.createSubtask(createSubtask(epic));

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldRemoveAllTasksEpicsSubtasks() {
        manager.createTask(createTask());
        Epic epic = manager.createEpic(createEpic());
        manager.createSubtask(createSubtask(epic));

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();


        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getAllSubtasks());
    }

    @Test
    public void shouldCalculateStartAndEndTimeOfEpicWith1Subtask() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask1 = manager.createSubtask(createSubtask(epic));

        assertEquals(subtask1.getStartTime(), epic.getStartTime());
        assertEquals(subtask1.getEndTime(), epic.getEndTime());
    }

    @Test
    public void shouldCalculateStartAndEndTimeOfEpicWith2Subtasks() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask1 = manager.createSubtask(new Subtask("Title", "Description", epic.getId(), Instant.now(), 1));
        Subtask subtask2 = manager.createSubtask(new Subtask("Title", "Description", epic.getId(), Instant.now().plusSeconds(300), 500));

        assertEquals(subtask1.getStartTime(), epic.getStartTime());
        assertEquals(subtask2.getEndTime(), epic.getEndTime());
    }

    @Test
    public void shouldRemoveTask() {
        Task task = manager.createTask(createTask());

        manager.removeTask(task.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
    }

    @Test
    public void shouldNotRemoveTaskIfBadId() {
        Task task = manager.createTask(createTask());
        manager.removeTask(0);

        assertEquals(List.of(task), manager.getAllTasks());
    }

    @Test
    public void shouldRemoveEpic() {
        Epic epic = manager.createEpic(createEpic());
        manager.createSubtask(createSubtask(epic));
        manager.createSubtask(createSubtask(epic));

        manager.removeEpic(epic.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getAllSubtasks());
    }

    @Test
    public void shouldNotRemoveEpicIfBadId() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask1 = manager.createSubtask(createSubtask(epic));
        Subtask subtask2 = manager.createSubtask(createSubtask(epic));

        manager.removeEpic(0);

        assertEquals(List.of(epic), manager.getAllEpics());
        assertEquals(List.of(subtask1, subtask2), manager.getAllSubtasks());
    }

    @Test
    public void shouldRemoveSubtask() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        manager.removeSubtask(subtask.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getAllSubtasks());
        assertEquals(Collections.EMPTY_LIST, manager.getEpic(epic.getId()).getSubtasks());
    }

    @Test
    public void shouldNotRemoveSubtaskIfBadId() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        manager.removeSubtask(0);

        assertEquals(List.of(subtask), manager.getAllSubtasks());
        assertEquals(List.of(subtask.getId()), manager.getEpic(epic.getId()).getSubtasks());
    }

    @Test
    public void shouldReturnSubtasksByEpicId() {
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask1 = manager.createSubtask(createSubtask(epic));
        Subtask subtask2 = manager.createSubtask(createSubtask(epic));
        Subtask subtask3 = manager.createSubtask(createSubtask(epic));

        assertEquals(List.of(subtask1, subtask2, subtask3), manager.getSubtasksByEpicId(epic.getId()));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnHistoryWith3Tasks() {
        Task task = manager.createTask(createTask());
        Epic epic = manager.createEpic(createEpic());
        Subtask subtask = manager.createSubtask(createSubtask(epic));

        manager.getTask(task.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask.getId());

        assertEquals(List.of(task, epic, subtask), manager.getHistory());
    }

    @Test
    public void shouldReturnHistoryWith1TaskAfterRemoving() {
        Task task = manager.createTask(createTask());
        Epic epic = manager.createEpic(createEpic());
        manager.createSubtask(createSubtask(epic));

        manager.getTask(task.getId());
        manager.removeEpic(epic.getId());

        assertEquals(List.of(task), manager.getHistory());
    }

    @Test
    public void shouldConvertEmptyTasksToEmptyString() {
        assertEquals("", FileBackedTasksManager.tasksToString(manager));
    }


    @Test
    public void shouldThrowExceptionIfTasksAreIntersect() {
        assertThrows(TaskOverlapAnotherTaskException.class, () -> {
            manager.createTask(new Task("Title", "Description", Instant.ofEpochMilli(0), 1000));
            manager.createTask(new Task("Title", "Description", Instant.ofEpochMilli(100), 1000));
        });
    }


}
