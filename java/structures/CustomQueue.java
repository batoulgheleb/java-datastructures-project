package structures;

public class CustomQueue<E> {
    
    
    private CustomList<E> queueList = new CustomList<>();

    public void enqueue(E element) {
        queueList.add(element);
    }

    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E head = queueList.getHead();
        queueList.remove(head);
        return head;
    }

    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return queueList.getHead();
    }

    public boolean isEmpty() {
        return queueList.isEmpty();
    }

    public int size() {
        return queueList.size();
    }

}
