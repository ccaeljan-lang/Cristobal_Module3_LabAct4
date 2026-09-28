
public interface StackInterface<E> {
    public void push(E j) throws StackOverflowException;

    public void pop() throws StackUnderflowException;

    public E top() throws StackUnderflowException;

    public boolean isEmpty();

    public boolean isFull();

    public int size();
}