package edu.est.process.manager.domain.structures;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implementación personalizada de una estructura de datos cola (FIFO - First In, First Out).
 * Esta clase está diseñada para almacenar elementos de cualquier tipo genérico {@code E}.
 *
 * @param <E> El tipo de elementos almacenados por la cola.
 */
public class CustomQueue<E> {

    private Node<E> head; // Reference to the start of the queue
    private Node<E> tail; // Reference to the end of the queue
    private int size;     // Number of elements in the queue



    /**
     * Clase interna estática que representa un nodo en la cola.
     */
    private static class Node<E> {
        E element;
        Node<E> next;

        /**
         * Constructor para crear un nuevo nodo.
         *
         * @param element El elemento que se almacenará en el nodo.
         * @param next El siguiente nodo en la cola.
         */
        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    /**
     * Constructor para crear una instancia de CustomQueue.
     * Inicializa la cola con head y tail como null y size como 0.
     */
    public CustomQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Comprueba si la cola está vacía.
     *
     * @return {@code true} si la cola está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Devuelve el número de elementos en la cola.
     *
     * @return El tamaño de la cola.
     */
    public int size() {
        return size;
    }

    /**
     * Agrega un nuevo elemento al final de la cola.
     *
     * @param element El elemento a añadir.
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
     * Elimina y devuelve el elemento al frente de la cola.
     *
     * @return El elemento al frente de la cola, o {@code null} si la cola está vacía.
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
     * Obtiene, pero no elimina, la cabeza de esta cola, o devuelve {@code null} si esta cola está vacía.
     *
     * @return La cabeza de la cola, o {@code null} si la cola está vacía.
     */
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return head.element;
    }

    /**
     * Actualiza un elemento en la cola con un nuevo valor.
     *
     * @param oldElement El elemento a buscar y reemplazar.
     * @param newElement El nuevo elemento que reemplazará al anterior.
     */
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
     * Convierte la cola en una {@link List}.
     * Este método recorre la cola desde el inicio hasta el final y agrega cada elemento
     * a una nueva lista, manteniendo el orden FIFO (First In, First Out) de los elementos.
     *
     * @return Una nueva {@link List} que contiene todos los elementos de la cola
     *         en el mismo orden. La lista devuelta es una nueva instancia y cualquier modificación
     *         a ella no afectará la cola original.
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

    /**
     * Obtiene el elemento en la posición especificada en la cola.
     *
     * @param i El índice del elemento que se desea obtener.
     * @return El elemento en la posición especificada.
     * @throws IndexOutOfBoundsException Si el índice está fuera de los límites de la cola.
     */
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


    /**
     * Elimina la primera ocurrencia del elemento dado de la cola.
     *
     * @param element El elemento a eliminar.
     */
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

