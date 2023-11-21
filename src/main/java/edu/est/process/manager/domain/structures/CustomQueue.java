package edu.est.process.manager.domain.structures;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Custom implementation of a queue data structure (FIFO - First In, First Out).
 * This class is designed to store elements of any generic type {@code E}.
 *
 * @param <E> The type of elements stored by the queue.
 */
public class CustomQueue<E> {

    private Node<E> head; // Reference to the start of the queue
    private Node<E> tail; // Reference to the end of the queue
    private int size;     // Number of elements in the queue



    /**
     * Static inner class representing a node in the queue.
     */
    private static class Node<E> {
        E element;
        Node<E> next;

        /**
         * Constructor to create a new node.
         *
         * @param element The element to be stored in the node.
         * @param next The next node in the queue.
         */
        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    /**
     * Constructor for creating an instance of CustomQueue.
     * Initializes the queue with head and tail as null and size as 0.
     */
    public CustomQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return The size of the queue.
     */
    public int size() {
        return size;
    }

    /**
     * Adds a new element to the end of the queue.
     *
     * @param element The element to be added.
     */
    public void enqueue(E element) {
        Node<E> newNode = new Node<>(element, null);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return The element at the front of the queue, or {@code null} if the queue is empty.
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            E element = head.element;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
            return element;
        }
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns {@code null} if this queue is empty.
     *
     * @return The head of the queue, or {@code null} if the queue is empty.
     */
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return head.element;
    }

    public void updateElement(E oldElement, E newElement) {
        if (isEmpty() || oldElement == null || newElement == null) {
            return;
        }

        Node<E> current = head;
        while (current != null) {
            if (current.element.equals(oldElement)) {
                current.element = newElement;
                return;
            }
            current = current.next;
        }
    }

    /**
     * Converts the queue into a {@link List}.
     * This method traverses the queue from head to tail and adds each element
     * to a new list, maintaining the FIFO (First In, First Out) order of the elements.
     *
     * @return A new {@link List} containing all the elements of the queue
     *         in the same order. The returned list is a new instance and any modifications
     *         to it will not affect the original queue.
     */
    public List<E> toList() {
        List<E> list = new ArrayList<>();
        Node<E> current = head;

        while (current != null) {
            list.add(current.element);
            current = current.next;
        }

        return list;
    }

    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }

        Node<E> current = head.next;
        for (int index = 0; index < i; index++) {
            current = current.next;
        }
        return current.element;
    }

    public void remove(E element) {
        // Manejar casos especiales: cola vacía o elemento en la cabeza
        if (isEmpty() || element == null) {
            return;
        }

        if (head.element.equals(element)) {
            dequeue(); // Reutiliza el método dequeue si el elemento está en la cabeza
            return;
        }

        // Buscar el elemento y mantener referencia al nodo anterior
        Node<E> current = head;
        Node<E> prev = null;
        while (current != null && !current.element.equals(element)) {
            prev = current;
            current = current.next;
        }

        // Si el elemento no se encuentra en la cola
        if (current == null) {
            return;
        }

        // Remover el nodo del enlace
        prev.next = current.next;

        // Si el elemento a remover es el último, actualizar la cola
        if (current == tail) {
            tail = prev;
        }

        size--;

    }

    /**
     * Filtra los elementos de la cola según el predicado dado.
     *
     * @param predicate El predicado que define la condición que deben cumplir los elementos.
     * @return Una lista de elementos que cumplen con la condición.
     */
    public List<E> filter(Predicate<E> predicate) {
        List<E> filteredList = new ArrayList<>();
        Node<E> current = head;

        while (current != null) {
            if (predicate.test(current.element)) {
                filteredList.add(current.element);
            }
            current = current.next;
        }

        return filteredList;
    }
}

