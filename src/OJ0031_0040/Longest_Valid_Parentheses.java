package OJ0031_0040;

import java.util.LinkedList;
import java.util.Stack;

public class Longest_Valid_Parentheses {
	/*
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14227/Why-people-give-conclusion-that-this-cannot-be-done-with-O(1)-space-my-AC-solution:-O(n)-run-time-O(1)-space
	 * 
	 * When right parentheses are more than left parentheses in the forward pass, we 
	 * can discard previous parentheses. In the backward pass, when left parentheses 
	 * are more than right parentheses, we can discard previous parentheses.
	 * 
	 * First time from left to right, second from right to left. The second iteration
	 * is only needed if the first iteration has ends with unclosed left bracket.
	 * 
	 * when the first iteration ends (left to right), we have 2 scenarios: 
	 * 1. all left brackets are closed (every left bracket matches a right bracket) 
	 * 2. some left brackets are open (couldn't find enough right brackets to finish 
	 * them). 
	 * In the first case, things are perfect, we just return the max value. In the 
	 * second case, we start the second iteration from right to left. This time, we 
	 * try to find left brackets to match right brackets. Remember, the condition to 
	 * start the second iteration is that we are having more left brackets than right 
	 * brackets. Therefore, we know each right bracket will guarantee to find a left 
	 * bracket to form a pair.
	 * 
	 * Rf :
	 * leetcode.com/problems/longest-valid-parentheses/discuss/14227/Why-people-give-conclusion-that-this-cannot-be-done-with-O(1)-space-my-AC-solution:-O(n)-run-time-O(1)-space/14603
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14140/Constant-space-O(n)-time-with-forward-and-backward-pass
	 */
	public int longestValidParentheses(String s) {
		int left = 0;
		int open = 0;
		int max = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(')
				open++;
			else
				open--;
			
			if (open == 0) {
				int length = i - left + 1;
				max = Math.max(max, length);
			}
			else if (open < 0) {
				left = i + 1;
				open = 0;
			}
		}
		
		if (open > 0) {
			int right = s.length() - 1;
			open = 0;
			for (int i = s.length() - 1; i >= 0; i--) {
				if (s.charAt(i) == ')')
					open++;
				else
					open--;
				
				if (open == 0) {
					int length = right - i + 1;
					max = Math.max(max, length);
				}
				else if (open < 0) {
					right = i - 1;
					open = 0;
				}
			}
		}
		
		return max;
	}
	
	/*
	 * https://leetcode.com/articles/longest-valid-parentheses/
	 * 
	 * For every '(' encountered, we push its index onto the stack.
	 * 
	 * For every ')' encountered, we pop the topmost element and subtract the current 
	 * element's index from the top element of the stack, which gives the length of 
	 * the currently encountered valid string of parentheses. If while popping the 
	 * element, the stack becomes empty, we push the current element's index onto the 
	 * stack. In this way, we keep on calculating the lengths of the valid substrings, 
	 * and return the length of the longest valid string at the end.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14131/Explaining-solution-using-Stack
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14167/Simple-JAVA-solution-O(n)-time-one-stack
	 */
	public int longestValidParentheses_stack(String s) {
		int maxans = 0;
		Stack<Integer> stack = new Stack<>();
		stack.push(-1);
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				stack.push(i);
			} 
			else {
				stack.pop();
				
				if (stack.empty()) {
					stack.push(i);
				} 
				else {
					maxans = Math.max(maxans, i - stack.peek());
				}
			}
		}
		return maxans;
	}
	
	/*
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14278/Two-Java-solutions-with-explanation.-Stack-and-DP.-Short-and-easy-to-understand.
	 * 
	 * We only update the result (max) when we find a "pair".
	 * If we find a pair. We throw this pair away and see how big the gap is between 
	 * current and previous invalid.
	 * EX: "( )( )"
	 * stack: -1, 0,
	 * when we get to index 1 ")", the peek is "(" so we pop it out and see what's 
	 * before "(". In this example it's -1. So the gap is "current_index" - (-1) = 2.
	 * 
	 * The idea only update the result (max) when we find a "pair" and push -1 to 
	 * stack first covered all edge cases.
	 */
	public int longestValidParentheses_stack2(String s) {
		LinkedList<Integer> stack = new LinkedList<>();
		int result = 0;
		stack.push(-1);
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ')' && stack.size() > 1 && s.charAt(stack.peek()) == '(') {
				stack.pop();
				result = Math.max(result, i - stack.peek());
			} 
			else {
				stack.push(i);
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/articles/longest-valid-parentheses/
	 * 
	 * The valid substrings must end with ')'. This further leads to the conclusion 
	 * that the substrings ending with '(' will always contain '0' at their 
	 * corresponding dp indices.
	 * 
	 * 1. s[i] = ')' and s[i - 1] = '(', i.e. string looks like .......()
	 *    dp[i] = dp[i - 2] + 2
	 * 2. s[i] = ')' and s[i - 1] = ')', i.e. string looks like .......))
	 *    if s[i - dp[i - 1] - 1] = '(', dp[i] = dp[i - 1] + dp[i - dp[i - 1] - 2] + 2
	 */
	public int longestValidParentheses_dp(String s) {
		int maxans = 0;
		int[] dp = new int[s.length()];
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == ')') {
				if (s.charAt(i - 1) == '(') {
					dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
				} 
				else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
					dp[i] = dp[i - 1] + ((i - dp[i - 1]) >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
				}
				maxans = Math.max(maxans, dp[i]);
			}
		}
		return maxans;
	}
	
	/*
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14278/Two-Java-solutions-with-explanation.-Stack-and-DP.-Short-and-easy-to-understand.
	 * 
	 * The idea is go through the string and use DP to store the longest valid 
	 * parentheses at that point. ex: "()(())"
	 * i : [0, 1, 2, 3, 4, 5 ]
	 * s : [( ,) ,( ,( ,) ,) ]
	 * DP: [0 ,2, 0, 0, 2, 6 ]
	 * 
	 * If s[i] is '(', set dp[i] to 0, because any string end with '(' cannot be a 
	 * valid one.
	 * 
	 * 1. We count all the '('.
	 * 2. If we find a ')' and '(' counter is not 0, we have at least a valid result 
	 *    size of 2. "()"
	 * 3. Check the the one before (i - 1). If DP[i - 1] is not 0 means we have 
	 *    something like this "(())" . This will have DP "0024"
	 * 4. We might have something before "(())". Take "()(())" example, Check the 
	 *    i = 1 because this might be a consecutive valid string.
	 *    
	 * Rf : 
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14133/My-DP-O(n)-solution-without-using-stack
	 */
	public int longestValidParentheses_dp2(String s) {
		int[] dp = new int[s.length()];
		int result = 0;
		int leftCount = 0;
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				leftCount++;
			} 
			else if (leftCount > 0) {
				dp[i] = dp[i - 1] + 2;
				dp[i] += (i - dp[i]) >= 0 ? dp[i - dp[i]] : 0;
				
				result = Math.max(result, dp[i]);
				leftCount--;
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/longest-valid-parentheses/discuss/14256/My-easy-O(n)-java-solution-with-explanation
	 * 
	 * Imaging we are coloring the original string, each substring that has valid 
	 * parentheses is colored with '1' and other characters are colored by '0'.
	 * For example "( ) ( ( ) "would become "11011". Thus, the problem has converted 
	 * to finding the longest subsequence that all elements are '1', which could be 
	 * easily solved.
	 * 
	 * Rf : https://leetcode.com/problems/longest-valid-parentheses/discuss/14126/My-O(n)-solution-using-a-stack
	 */
	public int longestValidParentheses_count(String s) {
		int res = 0;
		int tep = 0;
		Stack<Integer> s1 = new Stack<>();
		int data[] = new int[s.length()];
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (c == '(')
				s1.push(i);
			else {
				if (!s1.empty()) {
					data[i] = 1;
					data[s1.pop()] = 1;
				}
			}
		}
		
		for (int i : data) {
			if (i == 1)
				tep++;
			else {
				res = Math.max(tep, res);
				tep = 0;
			}
		}
		return Math.max(tep, res);
	}
	
	// by myself
	public int longestValidParentheses_self(String s) {
        if (s == null || s.length() == 0)
            return 0;
        
        char[] ch = s.toCharArray();
        int i = 0;
        while (i < ch.length && ch[i] == ')')
            i++;
        
        if (i == ch.length)
            return 0;
        
        int last = ch.length - 1;
        while (last >= 0 && ch[last] == '(')
            last--;
        
        if (last <= 0)
            return 0;
        
        int counter = 0;
        int max = 0;
        int count = 0;
        for (; i < last; i++) {
            if (ch[i] == ')')
                continue;
            
            count = 0;
            counter = 0;
            for (int j = i; j <= last; j++) {
                char c = ch[j];
                count++;
            
                if (c == '(') {
                    counter++;
                }
                else {
                    counter--;
                
                    if (counter == 0) {
                        //System.out.println("i = " + i + " ; j = " + j);
                        max = Math.max(max, count);
                    }
                    else if (counter < 0) {
                        i = j;
                        break;
                    }
                }
            }
        }
        return max;
    }

}
