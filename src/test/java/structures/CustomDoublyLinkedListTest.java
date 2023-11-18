package structures;


import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CustomDoublyLinkedListTest {
    @Test
    public void addAndRemoveElements() {
        CustomDoublyLinkedList<String> list = new CustomDoublyLinkedList<>();
        list.addLast("a");
        list.addLast("b");
        list.addLast("c");

        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("a", list.removeFirst());
        Assertions.assertEquals("c", list.removeLast());
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void convertListToAndFromCustomLinkedList() {
        List<String> originalList = List.of("a", "b", "c");
        CustomDoublyLinkedList<String> customList = CustomDoublyLinkedList.fromList(originalList);
        Assertions.assertEquals(3, customList.size());

        List<String> convertedList = customList.toList();
        Assertions.assertEquals(originalList, convertedList);
    }

    @Test
    public void iterateOverElements() {
        CustomDoublyLinkedList<String> list = CustomDoublyLinkedList.fromList(List.of("a", "b", "c"));
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append, false);
        Assertions.assertEquals("abc", sb.toString());

        sb.setLength(0); // Clear the StringBuilder
        list.forEach(sb::append, true);
        Assertions.assertEquals("cba", sb.toString());
    }
}

