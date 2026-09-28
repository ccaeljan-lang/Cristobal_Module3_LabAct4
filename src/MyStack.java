import java.util.Scanner;

public class MyStack implements StackInterface {
    Node top;
    int size;
    int max_size;
    Scanner scanner = new Scanner(System.in);

    class Node {
        Node next;
        Object data;

        Node(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    @Override
    public void push(Object j) throws StackOverflowException {
        if (size == max_size) {
            throw new StackOverflowException("Stack is full");
        }

        Node newNode = new Node(j);
        newNode.next = top;
        top = newNode;
        size++;
    }

    @Override
    public void pop() throws StackUnderflowException {
        if (top == null) {
            throw new StackUnderflowException("Stack is empty");
        }

        top = top.next;
        size--;
    }

    @Override
    public Object top() throws StackUnderflowException {
        if (top == null) {
            throw new StackUnderflowException("Stack is empty");
        }

        return top.data;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public boolean isFull() {
        return size == max_size;
    }

    @Override
    public int size() {
        return size;
    }

    public void showStack() {
        Node current = top;

        System.out.println("Stack: ");

        if (current == null) {
            System.out.println("[0]");
            return;
        }

        while (current != null) {
            System.out.print("[" + current.data + "]");
            current = current.next;
        }
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack();

        System.out.print("Enter Size: ");
        stack.max_size = stack.scanner.nextInt();
        stack.scanner.nextLine();

        boolean pushing = true;

        while (true) {
            System.out.println("\nStack Size: " + stack.size());
            System.out.println("Is Full: " + stack.isFull());
            System.out.println("Is Empty: " + stack.isEmpty());
            stack.showStack();

            if (pushing) {

                System.out.print("\nEnter value to push: ");
                String value = stack.scanner.nextLine();

                try {
                    stack.push(value);
                    System.out.println("Pushed: " + value);

                } catch (StackOverflowException e) {
                    System.out.println("\nStack Overflow!");
                    System.out.println("Switching to pop mode...");
                    pushing = false;
                }

            } else {

                System.out.print("\nPress Enter to pop: ");
                stack.scanner.nextLine();

                try {
                    stack.pop();
                    System.out.println("Popped.");

                } catch (StackUnderflowException e) {
                    System.out.println("\nStack Underflow!");
                    System.out.println("Switching to push mode...");
                    pushing = true;
                }
            }
        }
    }
}