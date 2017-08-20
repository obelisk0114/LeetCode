package OJ0141_0150;

import java.util.LinkedList;
import java.util.Stack;

public class Evaluate_Reverse_Polish_Notation {
	// https://discuss.leetcode.com/topic/18179/accepted-clean-java-solution
	public int evalRPN(String[] a) {
		Stack<Integer> stack = new Stack<Integer>();

		for (int i = 0; i < a.length; i++) {
			switch (a[i]) {
			case "+":
				stack.push(stack.pop() + stack.pop());
				break;

			case "-":
				stack.push(-stack.pop() + stack.pop());
				break;

			case "*":
				stack.push(stack.pop() * stack.pop());
				break;

			case "/":
				int n1 = stack.pop(), n2 = stack.pop();
				stack.push(n2 / n1);
				break;

			default:
				stack.push(Integer.parseInt(a[i]));
			}
		}

		return stack.pop();
	}
	
	/*
	 * myself
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/1941/java-accepted-code-stack-implementation
	 * https://discuss.leetcode.com/topic/2334/my-accepeted-javs-solution-with-stack/2
	 */
	public int evalRPN2(String[] tokens) {
        LinkedList<Integer> stack = new LinkedList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("+")) {
                int a = stack.removeLast();
                int b = stack.removeLast();
                b = b + a;
                stack.add(b);
            }
            else if (tokens[i].equals("-")) {
                int a = stack.removeLast();
                int b = stack.removeLast();
                b = b - a;
                stack.add(b);
            }
            else if (tokens[i].equals("*")) {
                int a = stack.removeLast();
                int b = stack.removeLast();
                b = b * a;
                stack.add(b);
            }
            else if (tokens[i].equals("/")) {
                int a = stack.removeLast();
                int b = stack.removeLast();
                b = b / a;
                stack.add(b);
            }
            else {
                int cur = Integer.parseInt(tokens[i]);
                stack.add(cur);
            }
        }
        return stack.getFirst();
    }
	
	// https://discuss.leetcode.com/topic/41066/java-6ms-beats-99-64
	public int evalRPN_array(String[] tokens) {
		int[] ls = new int[tokens.length / 2 + 1];
		int index = 0;
		for (String token : tokens) {
			switch (token) {
			case "+":
				ls[index - 2] = ls[index - 2] + ls[index - 1];
				index--;
				break;
			case "-":
				ls[index - 2] = ls[index - 2] - ls[index - 1];
				index--;
				break;
			case "*":
				ls[index - 2] = ls[index - 2] * ls[index - 1];
				index--;
				break;
			case "/":
				ls[index - 2] = ls[index - 2] / ls[index - 1];
				index--;
				break;
			default:
				ls[index++] = Integer.parseInt(token);
				break;
			}
		}
		return ls[0];
	}
	
	// https://discuss.leetcode.com/topic/1986/java-accepted-code-using-stack-and-factory-pattern

}
