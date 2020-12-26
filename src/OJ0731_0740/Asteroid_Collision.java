package OJ0731_0740;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Asteroid_Collision {
	/*
	 * https://leetcode.com/problems/asteroid-collision/discuss/193403/Java-easy-to-understand-solution
	 * 
	 * use a stack to keep track of the previous and compare current value with 
	 * previous ones
	 * 
	 * 1. element > 0, push it to stack and wait till you find a asteroid < 0 which 
	 *    can kill this
	 * 2. element < 0, see in stack how many asteroid it can destroy. 
	 *    If you find any two asteroid which are opposite in sign but abs value is 
	 *    same. we stop there
	 * 
	 * Rf :
	 * https://leetcode.com/problems/asteroid-collision/discuss/109694/JavaC++-Clean-Code/120966
	 * https://leetcode.com/problems/asteroid-collision/discuss/374043/Java-or-Full-thought-process-or-Brute-force-to-100-beat-or-Various-solutions
	 * 
	 * Other code:
	 * https://leetcode.com/problems/asteroid-collision/discuss/109694/JavaC%2B%2B-Clean-Code
	 * https://leetcode.com/problems/asteroid-collision/solution/
	 */
	public int[] asteroidCollision_stack(int[] asteroids) {
		Stack<Integer> s = new Stack<>();
		for (int i : asteroids) {
			
			// Current asteroid moving in right direction, 
			// it won't destroy any previous asteroid
			if (i > 0) {
				s.push(i);
			} 
			// Current asteroid moving in left direction, 
			// it may destroy any previous asteroid
			else {
				while (!s.isEmpty() && s.peek() > 0 && s.peek() < Math.abs(i)) {
					s.pop();
				}
				
				if (s.isEmpty() || s.peek() < 0) {
					s.push(i);
				} 
				else if (i + s.peek() == 0) { // equal
					s.pop();
				}
			}
		}
		
		int[] res = new int[s.size()];
		for (int i = res.length - 1; i >= 0; i--) {
			res[i] = s.pop();
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/asteroid-collision/discuss/109694/JavaC%2B%2B-Clean-Code
	 * 
	 * at the end, all the negative star has to be on the left, and all the positive 
	 * star has to be on the right.
	 * from the left, a negative star will pass through if no positive star on the 
	 * left;
	 * keep track of all the positive stars moving to the right, the right most one 
	 * will be the 1st confront the challenge of any future negative star.
	 * if it survives, keep going, otherwise, any past positive star will be exposed 
	 * to the challenge, by being popped out of the stack.
	 */
	public int[] asteroidCollision_stack3(int[] a) {
		// use LinkedList to simulate stack so that we don't need to reverse at end.
		LinkedList<Integer> s = new LinkedList<>();
		
		for (int i = 0; i < a.length; i++) {
			// a[i] is positive star 
			// or a[i] is negative star and there is no positive on stack
			if (a[i] > 0 || s.isEmpty() || s.getLast() < 0)
				s.add(a[i]);
			
			// a[i] is negative star and stack top is positive star
			else if (s.getLast() <= -a[i])
				
				// only positive star on stack top get destroyed, 
				// stay on i to check more on stack.
				if (s.pollLast() < -a[i])
					i--;
			
			// else : positive on stack bigger, negative star destroyed.
		}
		return s.stream().mapToInt(i -> i).toArray();
	}
	
	/*
	 * https://leetcode.com/problems/asteroid-collision/discuss/376501/Java-solution-stack-with-clean-explanation
	 * 
	 * loop the array from left to right, keep valid element in the stack.
	 * if the stack is empty, push element into stack.
	 * There are four cases regard with the peek of stack and current element.
	 * 
	 * stack peek > 0, current > 0 --- push into stack
	 * stack peek < 0, current < 0 --- push into stack
	 * stack peek < 0, current > 0 --- push into stack
	 * stack peek > 0, current < 0 --- most tricky part
	 *    if |current| = peek --- destroy both, which mean we pop the peek and check 
	 *                            next element.
	 *    if |current| < peek --- destroy current, we skip current and continue check 
	 *                            next element
	 *    if |current| > peek --- destroy peek, we pop the peek and still need to 
	 *                            compare current with the peek. so we don't increase 
	 *                            array index.
	 */
	public int[] asteroidCollision_stack2(int[] a) {
		Stack<Integer> stack = new Stack<>();
		int i = 0;
		while (i < a.length) {
			if (stack.isEmpty() || a[i] > 0 || stack.peek() < 0) {
				stack.push(a[i]);
				i++;
			} 
			else {
				if (-a[i] == stack.peek()) {
					stack.pop();
					i++;
				} 
				else if (-a[i] > stack.peek()) {
					stack.pop();
				} 
				else {
					i++;
				}
			}
		}
		
		int[] res = new int[stack.size()];
		for (int j = res.length - 1; j >= 0; j--) {
			res[j] = stack.pop();
		}
		return res;
	}

	// by myself
	public int[] asteroidCollision_self(int[] asteroids) {
        LinkedList<Integer> stack = new LinkedList<>();
        List<Integer> res = new ArrayList<>();
        
        for (int i = 0; i < asteroids.length; i++) {
            if (asteroids[i] > 0) {
                stack.offerLast(asteroids[i]);
            }
            else {
                while (!stack.isEmpty() && stack.peekLast() < Math.abs(asteroids[i])) {
                    stack.pollLast();
                }
                
                if (stack.isEmpty()) {
                    res.add(asteroids[i]);
                }
                else if (stack.peekLast() == Math.abs(asteroids[i])) {
                    stack.pollLast();
                }
            }
        }
        
        for (int asteroid : stack) {
            res.add(asteroid);
        }
        
        int[] ans = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            ans[i] = res.get(i);
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/problems/asteroid-collision/discuss/326986/Java-solution-using-Stack-with-explanation-beats-100
	 */
	public int[] asteroidCollision_array_stack(int[] asteroids) {
		int[] s = new int[asteroids.length];
		int top = -1;
		for (int a : asteroids) {
			// Stack only contains asteroids going left or those going right 
			// but starting from a more left position => No collision problem.
			if (a > 0)
				s[++top] = a;
			else {
				// Kill all smaller asteroids going right.
				while (top > -1 && s[top] > 0 && s[top] < -a)
					top--;
				
				// If nearest asteroid is also going left or there's none,
				// this asteroid can keep moving left without any collision.
				if (top == -1 || s[top] < 0)
					s[++top] = a;
				
				// If there's an asteroid going right of the same size, both die.
				else if (s[top] == -a)
					top--;
			}
		}
		return Arrays.copyOf(s, top + 1);
	}
	
	// https://leetcode.com/problems/asteroid-collision/discuss/109662/Java-solution-beat-90-No-extra-space.
	public int[] asteroidCollision_input_stack(int[] asteroids) {
		if (asteroids.length < 2) {
			return asteroids;
		}

		int index = 1;
		int end = 0;
		while (index < asteroids.length) {
			if (end == -1) {
				end++;
				asteroids[end] = asteroids[index];
				index++;
			} 
			else {
				if (asteroids[end] > 0 && asteroids[index] < 0) {
					if (Math.abs(asteroids[end]) == Math.abs(asteroids[index])) {
						end--;
						index++;
					} 
					else if (Math.abs(asteroids[end]) > Math.abs(asteroids[index])) {
						index++;
					} 
					else {
						end--;
					}
				} 
				else {
					end++;
					asteroids[end] = asteroids[index];
					index++;
				}
			}
		}

		return Arrays.copyOf(asteroids, end + 1);
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/asteroid-collision/discuss/189609/Python-stack-solution.-Extremely-simple.
     * https://leetcode.com/problems/asteroid-collision/discuss/109666/Python-O(n)-Stack-based-with-explanation
     * https://leetcode.com/problems/asteroid-collision/discuss/904475/Python-3-or-Stack-Simply-Clean-O(N)-or-Explanation
     * https://leetcode.com/problems/asteroid-collision/discuss/588839/Python-single-loop-and-stack-easy-to-understand-and-intuitive-O(n)-solution
     * https://leetcode.com/problems/asteroid-collision/discuss/109674/Iterative-python-solution-and-the-stack-based-version
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/asteroid-collision/discuss/365776/c%2B%2B-stack-implementation
     * https://leetcode.com/problems/asteroid-collision/discuss/909337/C%2B%2B-STACK-SOLUTION
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/asteroid-collision/discuss/206078/Readable-Javascript-Solution
	 * https://leetcode.com/problems/asteroid-collision/discuss/304369/JSJavascript-Simple-solution-with-single-loop-and-explanation
	 */

}
