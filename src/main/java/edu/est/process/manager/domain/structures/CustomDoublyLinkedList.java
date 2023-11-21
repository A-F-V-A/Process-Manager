package edu.est.process.manager.domain.structures;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementación personalizada de una lista doblemente enlazada.
 * Proporciona métodos para agregar, eliminar e iterar sobre elementos.
 *
 * @param <E> el tipo de elementos en esta lista
 */

public class CustomDoublyLinkedList<E> {
    // Reference to the head node of the list
    private Node<E> head;

    // Reference to the tail node of the list
    private Node<E> tail;

    // The size of the list
    private int size;

    /**
     * Un nodo dentro de la lista doblemente enlazada.
     * Esta clase interna es estática porque no necesita acceder a las variables de instancia de CustomDoublyLinkedList.
     */
    public static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        /**
         * Constructor para crear un nuevo nodo con referencias de nodo anterior y siguiente.
         *
         * @param element el elemento para almacenar en este nodo
         * @param prev    el nodo anterior en la lista
         * @param next    el nodo siguiente en la lista
         */
        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        /**
         * Constructor para crear un nuevo nodo sin elemento y sin referencias de nodo siguiente y anterior.
         */
        Node() {
            this(null, null, null);
        }
    }

    /**
     * Constructor por defecto de CustomDoublyLinkedList.
     * Inicializa la lista con nodos de cabeza y cola ficticios.
     */
    public CustomDoublyLinkedList() {
        head = new Node<>();
        tail = new Node<>(null, head, null);
        head.next = tail;
        size = 0;
    }

    /**
     * Agrega un elemento al principio de la lista.
     *
     * @param element el elemento a añadir
     */
    public void addFirst(E element) {
        addBetween(element, head, head.next);
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param element el elemento a añadir
     */
    public void addLast(E element) {
        addBetween(element, tail.prev, tail);
    }


    /**
     * Elimina el primer elemento de la lista.
     *
     * @return el elemento eliminado, o null si la lista está vacía
     */
    public E removeFirst() {
        if (isEmpty()) return null;
        return remove(head.next);
    }

    /**
     * Elimina el último elemento de la lista.
     *
     * @return el elemento eliminado, o null si la lista está vacía
     */
    public E removeLast() {
        if (isEmpty()) return null;
        return remove(tail.prev);
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si la lista está vacía, false de lo contrario
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Obtiene el tamaño actual de la lista.
     *
     * @return el tamaño de la lista
     */
    public int size() {
        return size;
    }

    /**
     * Encuentra el primer elemento que cumple con el predicado dado.
     *
     * @param predicate el predicado a aplicar a cada elemento
     * @return el primer elemento que cumple con el predicado, o null si ningún elemento cumple
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

    /**
     * Encuentra todos los elementos que cumplen con el predicado dado.
     *
     * @param predicate el predicado a aplicar a cada elemento
     * @return una lista de elementos que cumplen con el predicado
     */
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
     * Convierte esta CustomDoublyLinkedList a una List estándar.
     *
     * @return una List estándar que contiene los elementos de CustomDoublyLinkedList
     */
    public List<E> toList() {
        List<E> list = new ArrayList<>();
        this.forEach(list::add,false);
        return list;
    }

    /**
     * Agrega un nuevo nodo con el elemento dado entre el predecesor y el sucesor dados.
     *
     * @param element     el elemento a agregar en un nuevo nodo
     * @param predecessor el nodo predecesor al nuevo nodo a agregar
     * @param successor   el nodo sucesor al nuevo nodo a agregar
     */
    private void addBetween(E element, Node<E> predecessor, Node<E> successor) {
        // Crear y enlazar un nuevo nodo entre el predecesor y el sucesor
        Node<E> newNode = new Node<>(element, predecessor, successor);
        predecessor.next = newNode;
        successor.prev = newNode;
        size++;
    }

    /**
     * Elimina el nodo dado de la lista y devuelve su elemento.
     *
     * @param node el nodo a eliminar de la lista
     * @return el elemento del nodo eliminado
     */
    private E remove(Node<E> node) {
        Node<E> predecessor = node.prev;
        Node<E> successor = node.next;
        predecessor.next = successor;
        successor.prev = predecessor;
        size--;
        return node.element;
    }

    /**
     * Crea una CustomDoublyLinkedList a partir de una List estándar.
     *
     * @param list la List de la cual se creará la CustomDoublyLinkedList
     * @return una nueva CustomDoublyLinkedList que contiene los elementos de la List dada
     */
    public static <E> CustomDoublyLinkedList<E> fromList(List<E> list) {
        CustomDoublyLinkedList<E> customList = new CustomDoublyLinkedList<>();
        for (E element : list) {
            customList.addLast(element);
        }
        return customList;
    }

    /**
     * Encuentra el primer nodo que contiene el elemento dado.
     *
     * @param element el elemento a buscar en la lista
     * @return el nodo que contiene el elemento, o null si no se encuentra en la lista
     */
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

    /**
     * Intercambia los elementos de dos nodos que contienen los elementos dados.
     *
     * @param elementA el primer elemento a intercambiar
     * @param elementB el segundo elemento a intercambiar
     */
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
    /**
     * Obtiene el elemento en la posición dada.
     *
     * @param i la posición del elemento deseado
     * @return el elemento en la posición dada
     * @throws IndexOutOfBoundsException si el índice está fuera de los límites de la lista
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
     * Mueve hacia adelante el nodo que contiene el elemento dado.
     *
     * @param element el elemento del nodo a mover hacia adelante
     */
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

    /**
     * Mueve hacia atrás el nodo que contiene el elemento dado.
     *
     * @param element el elemento del nodo a mover hacia atrás
     */
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

    /**
     * Intercambia los nodos que contienen los elementos dados.
     *
     * @param nodeA el primer nodo a intercambiar
     * @param nodeB el segundo nodo a intercambiar
     */
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

    /**
     * Retorna un iterador sobre los elementos de la lista.
     *
     * @return un iterador que recorre los elementos de la lista
     */
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

