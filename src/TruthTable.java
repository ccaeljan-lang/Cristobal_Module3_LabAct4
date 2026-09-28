import java.util.Scanner;

public class TruthTable {

    public static boolean evaluate(String postfix, boolean p, boolean q) {

        MyStack stack = new MyStack();
        stack.max_size = 20;

        for (int i = 0; i < postfix.length(); i++) {

            char x = postfix.charAt(i);

            // Push variables
            if (x == 'p') {
                stack.push(p);
            }

            else if (x == 'q') {
                stack.push(q);
            }

            // NOT
            else if (x == '¬') {
                boolean a = (boolean) stack.top();
                stack.pop();

                stack.push(!a);
            }

            // Binary operators
            else {

                boolean b = (boolean) stack.top();
                stack.pop();

                boolean a = (boolean) stack.top();
                stack.pop();

                if (x == '∧') {
                    stack.push(a && b);
                }

                else if (x == '∨') {
                    stack.push(a || b);
                }

                else if (x == '↑') {
                    stack.push(!(a && b));
                }

                else if (x == '↓') {
                    stack.push(!(a || b));
                }

                else if (x == '→') {
                    stack.push(!a || b);
                }

                else if (x == '⇔') {
                    stack.push(a == b);
                }

                else if (x == '⊻') {
                    stack.push(a != b);
                }
            }
        }

        return (boolean) stack.top();
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter postfix logical expression: ");
        String postfix = scanner.nextLine();

        System.out.println("\np\tq\tResult");

        boolean result;

        result = evaluate(postfix, false, false);
        System.out.println("F\tF\t" + (result ? "T" : "F"));

        result = evaluate(postfix, false, true);
        System.out.println("F\tT\t" + (result ? "T" : "F"));

        result = evaluate(postfix, true, false);
        System.out.println("T\tF\t" + (result ? "T" : "F"));

        result = evaluate(postfix, true, true);
        System.out.println("T\tT\t" + (result ? "T" : "F"));
    }
}