package OJ0941_0950;

import java.util.LinkedList;
import java.util.Stack;

public class Validate_Stack_Sequences {
	/*
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/197685/C%2B%2BJavaPython-Simulation-O(1)-Space
	 * 
	 * If 2 is at the top of the stack, then if we have to pop that value next, we 
	 * must do it now. That's because any subsequent push will make the top of the 
	 * stack different from 2, and we will never be able to pop again.
	 * 
	 * Simulate stack operations:
	 * 
	 * Initialize an empty stack,
	 * iterate and push elements from pushed one by one.
	 * Each time, we'll try to pop the elements from as many as possible popped.
	 * In the end, we we'll check if stack is empty.
	 * 
	 * Stack Class is not recommended in more recent jdks, think using ArrayDeque to 
	 * implement Stack is better.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/validate-stack-sequences/solution/
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/197685/C++JavaPython-Simulation-O(1)-Space/202716
	 * 
	 * Other code:
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/197667/JavaPython-3-straight-forward-stack-solution.
	 */
	public boolean validateStackSequences_stack(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int i = 0;
        for (int x : pushed) {
            stack.push(x);
            while (!stack.empty() && stack.peek() == popped[i]) {
                stack.pop();
                i++;
            }
        }
        return stack.empty();
    }
	
	/*
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/197685/C%2B%2BJavaPython-Simulation-O(1)-Space
	 * 
	 * Using `pushed` as the stack.
	 * 
	 * x 會是 pushed 依序取出的元素
	 * 
	 * The idea is to overwrite the popped elements with new elements to be pushed in 
	 * the subsequent iterations.
	 * Everytime we encounter a new element to be pushed we increment i and assign to 
	 * x
	 * 
	 * Rf :
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/512173/C%2B%2BPython-O(n)-time-and-O(1)-space-with-detailed-explanation-Beats-100-space-and-time
	 */
	public boolean validateStackSequences_pushed_change(int[] pushed, int[] popped) {
		int i = 0, j = 0;
		for (int x : pushed) {
			pushed[i++] = x;
			while (i > 0 && pushed[i - 1] == popped[j]) {
				--i;
				++j;
			}
		}
		return i == 0;
	}
	
	// https://leetcode.com/problems/validate-stack-sequences/discuss/748175/Intuitive-Java-Solution-or-Handle-3-Simple-cases-or-Commented
	public boolean validateStackSequences_3_while(int[] pushed, int[] popped) {
		if (pushed == null || popped == null)
			return false;
		
		Stack<Integer> stack = new Stack<Integer>();
		int i = 0, j = 0;
		int len = pushed.length;
		
		// Run for the entire length
		while (i < len) {
			// Case 1: When there are elements in the Pushed Array, just push them
			while (i < len && pushed[i] != popped[j]) {
				stack.push(pushed[i]);
				i++;
			}
			
			// Case 2: When we have a push matching a pop just move on 1 step at a time
			while (i < len && pushed[i] == popped[j]) {
				i++;
				j++;
			}
			
			// Case 3: when the stack top matched the Pop meaning we are popping 
			// elements, pop till they match
			while (!stack.isEmpty() && stack.peek() == popped[j]) {
				stack.pop();
				j++;
			}
		}
		
		// The stack is empty ? Return true else false
		return stack.isEmpty();
	}
	
	// by myself
	public boolean validateStackSequences_self(int[] pushed, int[] popped) {
        LinkedList<Integer> stack = new LinkedList<>();
        
        int i = 0;
        int j = 0;
        while (i < pushed.length) {
            while (i < pushed.length && pushed[i] != popped[j]) {
                stack.offerLast(pushed[i]);
                i++;
            }
            
            if (i < pushed.length) {
                stack.offerLast(pushed[i]);
                i++;
            }
            
            // j < popped.length can be removed, since popped.length == pushed.length
            while (j < popped.length && !stack.isEmpty() 
            		&& stack.peekLast() == popped[j]) {
            	
                stack.pollLast();
                j++;
            }
        }
        
        return j == popped.length;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/708935/O(n3)-time-O(1)-space-without-modifying-input
	 * 
	 * For every num in popped (left -> right), we need to search in pushed to see if 
	 * we can pop it (use 3 pointers)
	 * 
	 * 1. go right to push all the left to stack, this pointer can only go right
	 * 2. go left to pop, find the first one in stack (some nums might be popped 
	 *    already)
	 * 
	 * [2,1,3,0]
	 * [1,0,3,2]
	 * 
	 * [1,2,3,4,5]
	 * [4,5,3,2,1]
	 * 
	 * [1,2,3,4,5,6,7]
	 * [4,3,5,2,6,1,7]
	 * 
	 * ???
	 * [1,2,3,4,5,6,7,8]
	 * [3,5,4,2,7,6,1]
	 */
	public boolean validateStackSequences_3_pointer(int[] pushed, int[] popped) {
		int n = pushed.length, left = 0, right = 0, p = 0;

		while (p < n && (left >= 0 || right < n)) {
			// search right
			int tmp = right;
			while (right < n && pushed[right] != popped[p]) {
				right++;
			}
			if (right < n) {
				p++;
				
				// reset left if found the popped one on the right and there are 
				// new elements need to be pushed into stack
				if (right > tmp + 1)
					left = right - 1;
				
				continue;
			}
			right = tmp; // restore right if not found

			// search left
			while (left >= 0 && !inStack(pushed, left, popped, p - 1)) {
				left--;
			}
			if (left >= 0 && pushed[left] == popped[p]) {
				left--;
				p++;
				continue;
			}

			return false;
		}

		return p == n;
	}

	// check if current num (pushed) is in stack by checking if it is already popped
	private boolean inStack(int[] pushed, int left, int[] popped, int p) {
		while (p >= 0 && pushed[left] != popped[p]) {
			p--;
		}

		return p < 0;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/validate-stack-sequences/discuss/323223/Python-O(n)-with-Explanation
     * https://leetcode.com/problems/validate-stack-sequences/discuss/576145/Python-single-loop-solution
     * https://leetcode.com/problems/validate-stack-sequences/discuss/512173/C%2B%2BPython-O(n)-time-and-O(1)-space-with-detailed-explanation-Beats-100-space-and-time
     * https://leetcode.com/problems/validate-stack-sequences/discuss/748243/Python-3-Short-Simple-easy-to-understand-without-using-extra-memory-(just-pointers)
     * https://leetcode.com/problems/validate-stack-sequences/discuss/742575/FAST-Python-solution-with-and-without-stack
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/validate-stack-sequences/discuss/197835/C%2B%2B-5-lines-O(n)-stack-and-two-pointers
     * https://leetcode.com/problems/validate-stack-sequences/discuss/752911/C%2B%2B-Simple-Clear-O(n)Space-Solution
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/validate-stack-sequences/discuss/788519/JavaScript-Clean-O(1)-Space-Solution
	 */

}
