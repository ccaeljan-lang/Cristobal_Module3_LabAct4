import java.util.Scanner;

public class MyStack implements StackInterface {
    // Stack parameters
    Node top;
    int size;
    int max_size;

    // Stack implementation using linked list
    class Node {
        Node next;
        Object data;

        Node(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    // Stack push implementation
    @Override
    public void push(Object j) throws StackOverflowException {
        // Checks if stack is full depending on the length of the postfix equation
        if (size == max_size) {
            throw new StackOverflowException("Stack is full");
        }

        // Creates a stack with the pushed data
        Node newNode = new Node(j);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Stack popping implementation
    @Override
    public void pop() throws StackUnderflowException {
        // Check if stack is empty
        if (top == null) {
            throw new StackUnderflowException("Stack is empty");
        }

        // Makes the current top data the next data after the previous data and reduce size
        top = top.next;
        size--;
    }

    // Stack implementation of top
    @Override
    public Object top() throws StackUnderflowException {
        // Check is stack is empty
        if (top == null) {
            throw new StackUnderflowException("Stack is empty");
        }

        // Returns the data of the top of the stack
        return top.data;
    }

    // Stack check if empty implementation
    @Override
    public boolean isEmpty() {
        return top == null;
    }

    // Stack check if full implementation
    @Override
    public boolean isFull() {
        return size == max_size;
    }

    // Stack size check implementation
    @Override
    public int size() {
        return size;
    }

    // Evaluate logical operators using logical precedence
    static boolean operate(char operator, boolean a, boolean b) {
        if (operator == '∧') {
            return a && b;
        } else if (operator == '∨') {
            return a || b;
        } else if (operator == '↑') {
            return !(a && b);
        } else if (operator == '↓') {
            return !(a || b);
        } else if (operator == '⊻') {
            return (a || b) && !(a && b);
        } else if (operator == '→') {
            return !a || b;
        } else if (operator == '⇔') {
            return (a && b) || (!a && !b);
        }

        return false;
    }

    // Evaluate postfix logical expression
    static boolean evaluate(String expression, boolean p, boolean q) {
        // Creates new stack
        MyStack stack = new MyStack();

        // Set maximum size be equal to the length of the equation
        stack.max_size = expression.length();

        // try-catch block for exceptions in push, pop, and top
        try {
            // Goes over the postfix expression
            for (int i = 0; i < expression.length(); i++) {
                // Sets current character
                char c = expression.charAt(i);

                // if space, ignore
                // if the current character is a logical variable then it pushes its truth value
                // if the current character is a negation it pops the current top and pushes its reverse truth value
                // if the current character is a logical operator then it pops the two top data and evaluates it using
                // the operate function, then pushes the overall truth value
                if (c == ' ') {
                    continue;
                }
                if (c == 'p') {
                    stack.push(p);
                } else if (c == 'q') {
                    stack.push(q);
                } else if (c == '¬') {
                    boolean a = (boolean) stack.top();
                    stack.pop();

                    stack.push(!a);
                } else if (c == '∧' || c == '∨' || c == '↑' || c == '↓' || c == '→' || c == '⇔' || c == '⊻') {
                    boolean b = (boolean) stack.top();
                    stack.pop();

                    boolean a = (boolean) stack.top();
                    stack.pop();

                    stack.push(operate(c, a, b));
                }
            }

            return (boolean) stack.top();
        } catch (StackOverflowException | StackUnderflowException e) {
            // Outputs the exception message
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static String infixToPostfix(String infix) {
        MyStack stack = new MyStack();
        stack.max_size = infix.length();

        String postfix = "";

        try {
            for (int i = 0; i < infix.length(); i++) {
                char c = infix.charAt(i);
                // Operand
                if (Character.isLetter(c)) {
                    postfix += c;
                    // Left parenthesis
                } else if (c == '(') {
                    stack.push(c);
                    // Right parenthesis
                } else if (c == ')') {
                    while (!stack.isEmpty() && (char) stack.top() != '(') {
                        postfix += (char) stack.top();
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        stack.pop(); // remove '('
                    }
                } else if (c == '¬') {
                    stack.push(c);
                } else { // existing binary operator handling
                    while (!stack.isEmpty() && (char) stack.top() != '(' && precedence((char) stack.top()) <= precedence(c)) {
                        postfix += (char) stack.top();
                        stack.pop();
                    }

                    stack.push(c);
                }
            }
            // Pop remaining operators
            while (!stack.isEmpty()) {
                postfix += (char) stack.top();
                stack.pop();
            }

        } catch (StackOverflowException | StackUnderflowException e) {
            System.out.println(e.getMessage());
        }

        return postfix;
    }

    // Precedence ranking for infix to postfix conversion
    public static int precedence(char op) {
        switch (op) {
            case '¬': return 1;
            case '∧': return 2;
            case '∨': return 3;
            case '↑': return 4;
            case '↓': return 5;
            case '⊻': return 6;
            case '→': return 7;
            case '⇔': return 8;
            default: return 9;
        }
    }

    // Checks if the character is a logical operator
    public static boolean isOperator(char c) {
        // Checks if the current character is one of the logical operators
        return c == '¬' || c == '∧' || c == '∨' || c == '↑' || c == '↓' || c == '⊻' || c == '→' || c == '⇔';
    }

    public static boolean isInfix(String expression) {
        // Parentheses only appear in infix
        if (expression.contains("(") || expression.contains(")")) {
            return true;
        }

        // Simulate postfix evaluation by counting operands on the stack
        int depth = 0;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == 'p' || c == 'q') {
                depth++;
            } else if (c == '¬') {
                // Negation needs one operand before it in postfix
                if (depth < 1) {
                    return true;
                }
            } else if (isOperator(c)) {
                // Binary operator needs two operands before it in postfix
                if (depth < 2) {
                    return true;
                }
                depth--;
            }
        }

        // A valid postfix expression leaves exactly one value
        return depth != 1;
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter logical expression: ");
        String expression = scanner.nextLine().replaceAll("\\s+", "");
        // Deletes spaces to not mess with operation handling

        String postfix;

        // Checks if the input is infix or postfix
        if (isInfix(expression)) {
            System.out.println("Detected: Infix expression");

            // Converts the infix expression to postfix
            postfix = infixToPostfix(expression);

            System.out.println("Postfix: " + postfix);
        } else {
            System.out.println("Detected: Postfix expression");

            // Uses the input directly as the postfix expression
            postfix = expression;

            System.out.println("Postfix: " + postfix);
        }

        System.out.println("\n  Truth Table: " + expression);
        System.out.println("|===========================|");
        System.out.println("|\tp\t|\tq\t|\tResult\t|");
        System.out.println("|===========================|");

        boolean result;

        result = evaluate(postfix, true, true);
        System.out.println("|\tT\t|\tT\t|\t  " + (result ? "T" : "F") + "\t\t|");

        result = evaluate(postfix, true, false);
        System.out.println("|\tT\t|\tF\t|\t  " + (result ? "T" : "F") + "\t\t|");

        result = evaluate(postfix, false, true);
        System.out.println("|\tF\t|\tT\t|\t  " + (result ? "T" : "F") + "\t\t|");

        result = evaluate(postfix, false, false);
        System.out.println("|\tF\t|\tF\t|\t  " + (result ? "T" : "F") + "\t\t|");
        System.out.println("|===========================|");
    }
}