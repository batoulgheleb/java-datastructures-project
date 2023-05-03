package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomList<E> implements IList<E>, Iterable<E> {
    
    //--------------------------node nested class 
    
    private static class Node<E> {
        //each node within the list stores a value and a pointer
        private E element;
        private Node<E> nextNode;

        public Node(E element, Node<E> nextNode) {
            this.element = element;
            this.nextNode = nextNode;
        }

        public E getElement() {
            return element;
        }

        public Node<E> getNext() {
            return nextNode;
        }

        public void setNext(Node<E> node) {
            nextNode = node;
        }

    }

    //---------------------------end of node nested class 

    private int size;
    private Node<E> head = null;
    private Node<E> tail = null;

    public CustomList() {
        this.size = 0;
    }

    public E getHead() {
        return head.getElement();
    }

    public E getTail() {
        return tail.getElement();
    }


    //adds an element to the end of the list 
    @Override
    public boolean add(E element) {
        
        Node<E> newNode = new Node<>(element, null);
      
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        
        tail = newNode;
        size++;
        return true;
    }

    //clears the list so that its empty 
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }


    @Override
    public boolean contains(E element) {
        
        Node<E> current = head;
        while (current != null ) {
            if (current.getElement().equals(element)) {
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(E element) {
        Node<E> current = head;
        Node<E> previous = null;

        while ( current != null ) {
           
            if (current.getElement().equals(element)) {

                //if previous node is null then element being removed from the head
                //new head is set and true is returned
                if (previous == null) {
                    head = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                //else if the tail of the list 
                if (current.getNext() == null) {
                    tail = previous;
                }

                size--;
                return true;
            }

            //moving on to the next node
            previous = current;
            current = current.getNext();
        }

        //element doesnt exist therefore no match
        System.out.println("Element is not found");
        return false;
        
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public int indexOf(E element) {
        throw new UnsupportedOperationException("Unimplemented method 'indexOf'");
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    //as to make the list iterable 
    @Override
    public Iterator<E> iterator() {
        return new CustomListIterator();
    }

    //------------------------custom list iterator 
    private class CustomListIterator implements Iterator<E> {

        private Node<E> node = head;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException(); 
            }
            E element = node.getElement();
            node = node.getNext();
            return element;
        }
        
    }
  
    //-------------------------end of custom list iterator 

    @Override
    public String toString() {
        System.out.print("[");
        Node<E> current = head;
        while ( current != null ) {
            System.out.print(current.getElement());
            System.out.print(" ");
            current = current.getNext();
        }
       
        System.out.println("]");
        return " ";

    }

    public void reverse() {
        Node<E> current = head;
        Node<E> previous = null;
        Node<E> next = null;
    
        while (current != null) {
            next = current.getNext();
            current.setNext(previous);
            previous = current;
            current = next;
        }
    
        tail = head;
        head = previous;
    } 

}