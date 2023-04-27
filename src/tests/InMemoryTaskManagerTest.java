package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }
}
