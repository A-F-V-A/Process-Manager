package structures;
import edu.est.process.manager.domain.structures.CustomQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomQueueTest {
    private CustomQueue<String> queue;

    @BeforeEach
    public void setUp() {
        queue = new CustomQueue<>();
    }

    @Test
    public void newQueueIsEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void enqueueAddsElements() {
        queue.enqueue("elemento1");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
    }

    @Test
    public void dequeueRemovesElements() {
        queue.enqueue("elemento1");
        queue.enqueue("elemento2");
        assertEquals("elemento1", queue.dequeue());
        assertEquals(1, queue.size());
    }

    @Test
    public void peekReturnsFrontElement() {
        queue.enqueue("elemento1");
        queue.enqueue("elemento2");
        assertEquals("elemento1", queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    public void dequeueEmptyQueue() {
        assertNull(queue.dequeue());
    }
}

