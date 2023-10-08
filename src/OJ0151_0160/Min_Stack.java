package OJ0151_0160;

import java.util.Stack;

/*
 * https://github.com/azl397985856/leetcode/blob/master/problems/155.min-stack.md
 */

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
	 * 
	 * Other codes:
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/177718
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
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack
	 * 
	 * Core Idea:
	 * 1. Minimum value is always followed by the second minimum value (duplicate 
	 *    value of the second minimum value, to ensure that when pop function 
	 *    removes the 2nd min, it does not disrupt the stack order).
	 * 2. And while popping you pop min and 2nd min so that, you get the correct 
	 *    min value for the remaining stack and the remaining stack top also points 
	 *    to the right place.
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * Each time the min changes due to a new min being added, the old min is added 
	 * to the stack in addition to the new value. So each time the min changes due to 
	 * an item being removed, we know that the next value in the stack is the old min.
	 * 
	 * push 的時候就已經確定了。如果最小值更新的話，先 push 原先最小值，再 push 現在存入的當前最小值。
	 * 所以 pop 的時候同理，pop 出全局最小值時，棧頂就變成了當前最小值之前的最小值，即倒數第二小的值
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * A flaw in this method is that if you pop all values, getmin will return 
	 * MAX_VALUE, which was never a min, it was just a convenient default.
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * However, beside overhead, this algorithm does not save memory when compare to 
	 * 2 stacks solution. Basically, the additional push you make when (x <= min) is 
	 * the same as you push the new min value (i.e., x) to the min stack.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/252495
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/118201
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/479346
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/121798
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/49118
	 * https://leetcode.com/problems/min-stack/discuss/49014/Java-accepted-solution-using-one-stack/202855
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
    
    /*
     * https://leetcode.com/problems/min-stack/discuss/49010/Clean-6ms-Java-solution
     * 
     * Min value is a property associated with the existing values in stack, which 
     * means it changes as elements come and go. But in stack elements changes in 
     * LIFO order, so the min value keeps repeating in the elements above it but 
     * gets updated in the elements below it when it is removed.
     * 
     * -----------------------------------------------------------------
     * 
     * When you provide your own custom constructor java wont provide you default 
     * constructor. This program will work until you create object with constructor 
     * with no args like when you do new Node() this will not work as you have not 
     * supplied default constructor.
     * 
     * Java automatically generates a default constructor.
     * https://beginnersbook.com/2014/01/default-constructor-java-example/
     * https://stackoverflow.com/questions/4488716/java-default-constructor
     * 
     * Rf :
     * https://leetcode.com/problems/min-stack/discuss/49010/Clean-6ms-Java-solution/803538
     * https://leetcode.com/problems/min-stack/discuss/49010/Clean-6ms-Java-solution/681647
     * https://leetcode.com/problems/min-stack/discuss/49010/Clean-6ms-Java-solution/49074
     * 
     * Other codes:
     * https://leetcode.com/problems/min-stack/discuss/49217/6ms-Java-Solution-using-Linked-List.-Clean-self-explanatory-and-efficient./453282
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/49212
     * https://leetcode.com/problems/min-stack/discuss/49217/6ms-java-solution-using-linked-list-clean-self-explanatory-and-efficient
     */
    class MinStack_Node {
    	private Node head;
            
        public void push(int x) {
            if (head == null) 
                head = new Node(x, x, null);
            else 
                head = new Node(x, Math.min(x, head.min), head);
        }
        
        public void pop() {
            head = head.next;
        }
        
        public int top() {
            return head.val;
        }
        
        public int getMin() {
            return head.min;
        }
            
        private class Node {
            int val;
            int min;
            Node next;
                
            private Node(int val, int min, Node next) {
                this.val = val;
                this.min = min;
                this.next = next;
            }
        }
    }
    
    /*
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack
     * 
     * The idea is to store the gap between the min value and the current value;
     * The problem is the cast. I have no idea to avoid the cast. Since the possible 
     * gap between the current value and the min value could be 
     * Integer.MAX_VALUE - Integer.MIN_VALUE;
     * 
     * ---------------------------------------------------------------------------
     * 
     * getMin() will return a value even when the stack is empty. 
     * eg. after push(2) and pop(), getMin() returns 2
     * 
     * Rf :
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/177596
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/218535
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/882875
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/223911
     * 
     * Other codes:
     * https://leetcode.com/problems/min-stack/discuss/49031/Share-my-Java-solution-with-ONLY-ONE-stack/49183
     */
    public class MinStack_stack_gap {
		long min;   // 可以不為 long, 但是要轉換型態
		Stack<Long> stack;

		public MinStack_stack_gap() {
			stack = new Stack<>();
		}

		public void push(int x) {
			// 0L will convert 0 into long type.
			// 0L is pushed in empty stack because first value will always be 
			// minimum and we need to make sure if top gets called with only one 
			// element in stack we should make it work
			if (stack.isEmpty()) {
				stack.push(0L);
				min = x;
			} 
			else {
				//Could be negative if min value needs to change
				stack.push(x - min);
				
				if (x < min)
					min = x;
			}
		}

		public void pop() {
			if (stack.isEmpty())
				return;

			long pop = stack.pop();

			// If negative, increase the min value
			
			// if (pop < 0), the popped value is the min
            // Recall pop is added by this statement: stack.push(x - min);
            // So, pop = x - old_min
            // old_min = x - pop
            // again, if (pop < 0), x is the min so:
            // old_min = min - pop
			if (pop < 0)
				min = min - pop;
        }

		public int top() {
			long top = stack.peek();
			
			// top = x - min
            // x = top + min
			if (top > 0) {
				return (int) (top + min);
			} 
			else {
				return (int) (min);
			}
		}

		public int getMin() {
			return (int) min;
		}
	}
	
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/min-stack/discuss/49022/My-Python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/min-stack/discuss/49016/C%2B%2B-using-two-stacks-quite-short-and-easy-to-understand
     * https://leetcode.com/problems/min-stack/discuss/49221/C%2B%2B-solution-using-pair-and-one-stack
     */
    
	/**
	 * Your MinStack object will be instantiated and called as such:
	 * MinStack obj = new MinStack();
	 * obj.push(x);
	 * obj.pop();
	 * int param_3 = obj.top();
	 * int param_4 = obj.getMin();
	 */

}
