package edu.est.process.manager.domain.structures;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Custom implementation of a doubly linked list.
 * It provides methods to add, remove, and iterate over elements.
 *
 * @param <E> the type of elements in this list
 */
public class CustomDoublyLinkedList<E> {
    // Reference to the head node of the list
    private Node<E> head;

    // Reference to the tail node of the list
    private Node<E> tail;

    // The size of the list
    private int size;

    /**
     * A node within the doubly linked list.
     * This inner class is static because it does not need access to the instance variables of CustomDoublyLinkedList.
     */
    public static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        /**
         * Constructor for creating a new node with previous and next node references.
         *
         * @param element the element to store in this node
         * @param prev    the previous node in the list
         * @param next    the next node in the list
         */
        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        /**
         * Constructor for creating a new node with no element and no next and previous node references.
         */
        Node() {
            this(null, null, null);
        }
    }
    public CustomDoublyLinkedList() {
        head = new Node<>();
        tail = new Node<>(null, head, null);
        head.next = tail;
        size = 0;
    }

    /**
     * Adds an element to the beginning of the list.
     *
     * @param element the element to add
     */
    public void addFirst(E element) {
        addBetween(element, head, head.next);
    }


    public void addLast(E element) {
        addBetween(element, tail.prev, tail);
    }
    public E removeFirst() {
        if (isEmpty()) return null;
        return remove(head.next);
    }
    public E removeLast() {
        if (isEmpty()) return null;
        return remove(tail.prev);
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }


    /**
     * Finds the first element that matches the given predicate.
     *
     * @param predicate a predicate to apply to each element to determine if it should be returned
     * @return the first matching element, or null if no element matches
     */
    public E findFirst(Predicate<E> predicate) {
        Node<E> current = head.next;
        while (current != tail) {
            if (predicate.test(current.element)) {
                return current.element;
            }
            current = current.next;
        }
        return null;
    }

    public List<E> findAll(Predicate<E> predicate) {
        List<E> matchingElements = new ArrayList<>();
        Node<E> current = head.next;
        while (current != tail) {
            if (predicate.test(current.element)) {
                matchingElements.add(current.element);
            }
            current = current.next;
        }
        return matchingElements;
    }

    /**
     * Performs the given action for each element of the list until all elements have been processed or the action throws an exception.
     * The order of iteration is determined by the reverse parameter.
     *
     * @param action  the action to be performed for each element
     * @param reverse if true, the list is iterated in reverse order
     */
    public void forEach(Consumer<E> action, boolean reverse) {
        if (reverse) {
            Node<E> current = tail.prev; // Comenzamos por el final
            while (current != head) { // Mientras no lleguemos al nodo ficticio de cabeza
                action.accept(current.element);
                current = current.prev;
            }
        } else {
            Node<E> current = head.next; // Comenzamos por el principio
            while (current != tail) { // Mientras no lleguemos al nodo ficticio de cola
                action.accept(current.element);
                current = current.next;
            }
        }
    }

    /**
     * Removes the first element that matches the given predicate.
     *
     * @param predicate a predicate to apply to each element to determine if it should be removed
     * @return true if an element was removed, false otherwise
     */
    public boolean removeIf(java.util.function.Predicate<E> predicate) {
        Node<E> current = head.next;
        while (current != tail) {
            if (predicate.test(current.element)) {
                remove(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Converts this CustomDoublyLinkedList to a standard List.
     * @return A standard List containing the elements of the CustomDoublyLinkedList.
     */
    public List<E> toList() {
        List<E> list = new ArrayList<>();
        this.forEach(list::add,false);
        return list;
    }
    private void addBetween(E element, Node<E> predecessor, Node<E> successor) {
        // Crear y enlazar un nuevo nodo entre el predecesor y el sucesor
        Node<E> newNode = new Node<>(element, predecessor, successor);
        predecessor.next = newNode;
        successor.prev = newNode;
        size++;
    }
    private E remove(Node<E> node) {
        Node<E> predecessor = node.prev;
        Node<E> successor = node.next;
        predecessor.next = successor;
        successor.prev = predecessor;
        size--;
        return node.element;
    }

    public static <E> CustomDoublyLinkedList<E> fromList(List<E> list) {
        CustomDoublyLinkedList<E> customList = new CustomDoublyLinkedList<>();
        for (E element : list) {
            customList.addLast(element);
        }
        return customList;
    }

    private Node<E> findNode(E element) {
        Node<E> current = head.next;
        while (current != tail) {
            if (current.element.equals(element)) {
                return current;
            }
            current = current.next;
        }
        return null; // Retorna null si el elemento no se encuentra en la lista
    }

    public void swapElements(E elementA, E elementB) {
        Node<E> nodeA = findNode(elementA);
        Node<E> nodeB = findNode(elementB);

        if (nodeA != null && nodeB != null) {
            // Intercambiar los elementos de los nodos
            E temp = nodeA.element;
            nodeA.element = nodeB.element;
            nodeB.element = temp;
        }
    }

    public void moveNodeForward(E element) {
        if (head.next == tail || head.next.next == tail) {
            // La lista está vacía o solo tiene un elemento.
            return;
        }

        Node<E> current = head.next;
        while (current != tail) {
            if (current.element.equals(element)) {
                Node<E> next = current.next;
                if (next == tail) {
                    // Si 'current' es el último nodo real, no se puede mover más adelante.
                    return;
                }

                // Intercambiar 'current' y 'next'
                swapNodes(current, next);
                return;
            }
            current = current.next;
        }
    }

    public void moveNodeBackward(E element) {
        if (head.next == tail || head.next.next == tail) {
            // La lista está vacía o solo tiene un elemento.
            return;
        }

        Node<E> current = tail.prev;
        while (current != head) {
            if (current.element.equals(element)) {
                Node<E> previous = current.prev;
                if (previous == head) {
                    // Si 'current' es el primer nodo real, no se puede mover más atrás.
                    return;
                }

                // Intercambiar 'current' y 'previous'
                swapNodes(previous, current);
                return;
            }
            current = current.prev;
        }
    }

    private void swapNodes(Node<E> nodeA, Node<E> nodeB) {
        // Asumiendo que nodeA y nodeB son adyacentes y nodeA está antes de nodeB
        Node<E> aPrev = nodeA.prev;
        Node<E> bNext = nodeB.next;

        if (aPrev != null) {
            aPrev.next = nodeB;
        }
        nodeB.prev = aPrev;

        nodeB.next = nodeA;
        nodeA.prev = nodeB;

        nodeA.next = bNext;
        if (bNext != null) {
            bNext.prev = nodeA;
        }

        // Actualiza head y tail si es necesario
        if (head.next == nodeA) {
            head.next = nodeB;
        }
        if (tail.prev == nodeB) {
            tail.prev = nodeA;
        }
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public E next() {
                E data = current.element;
                current = current.next;
                return data;
            }
        };
    }
}

