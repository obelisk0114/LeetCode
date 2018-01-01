package OJ0151_0160;

import java.util.Stack;

public class Min_Stack {
	
	/** initialize your data structure here. */
	/*
	 * The following variables and functions are from this link.
	 * https://discuss.leetcode.com/topic/4945/simple-java-solution-using-two-build-in-stacks
	 * 
	 * During push: push a new item if it's smaller or equals than the current minimum. 
	 * During pop: if the current item to be popped equals to the top of minStack then 
	 * pop that one aswell.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/9495/very-concise-java-solution-with-deque-interface
	 */
	private Stack<Integer> stack;         // stack: store the stack numbers
	private Stack<Integer> minStack;      // minStack: store the current min values
	
	public Min_Stack() {
		stack = new Stack<Integer>();
		minStack = new Stack<Integer>();
	}

	public void push(int x) {
        // store current min value into minStack
        if (minStack.isEmpty() || x <= minStack.peek())
            minStack.push(x);
        stack.push(x);
    }

	public void pop() {
        // use equals to compare the value of two object, if equal, pop both of them
        if (stack.peek().equals(minStack.peek()))
            minStack.pop();
        stack.pop();
    }

	public int top() {
		return stack.peek();
	}

	public int getMin() {
		return minStack.peek();
	}
	
	/*
	 * The following variables and functions are from this link.
	 * https://discuss.leetcode.com/topic/7020/java-accepted-solution-using-one-stack
	 */
	int min2 = Integer.MAX_VALUE;
	Stack<Integer> stack2 = new Stack<Integer>();

	public void push2(int x) {
        // only push the old minimum value when the current 
        // minimum value changes after pushing the new value x
		if (x <= min2) {
			stack2.push(min2);
			min2 = x;
		}
		stack2.push(x);
    }

    public void pop2() {
        // if pop operation could result in the changing of the current minimum value, 
        // pop twice and change the current minimum value to the last minimum value.
		if (stack2.pop() == min2)
			min2 = stack2.pop();
    }

    public int top2() {
        return stack2.peek();
    }

    public int getMin2() {
        return min2;
    }
	
	// https://discuss.leetcode.com/topic/4953/share-my-java-solution-with-only-one-stack
	
	/** Use linked list, without stack **/
	// https://discuss.leetcode.com/topic/33199/clean-6ms-java-solution
	// https://discuss.leetcode.com/topic/26866/6ms-java-solution-using-linked-list-clean-self-explanatory-and-efficient
	
	/**
	 * Your MinStack object will be instantiated and called as such:
	 * MinStack obj = new MinStack();
	 * obj.push(x);
	 * obj.pop();
	 * int param_3 = obj.top();
	 * int param_4 = obj.getMin();
	 */

}
