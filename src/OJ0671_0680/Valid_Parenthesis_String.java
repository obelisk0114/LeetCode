package OJ0671_0680;

import java.util.LinkedList;
import java.util.Stack;

public class Valid_Parenthesis_String {
	/*
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/139759/Java-Very-easy-solution.-No-recursion-dp.
	 * 
	 * Check from left to right and then check from right to left.
	 * When check from left to right, take all '*'s as '(', to see whether can match 
	 * all ')'s.
	 * When check from right to left, take all '*'s as ')', to see whether can match 
	 * all '('s.
	 * If both checks are valid, then the string is valid.
	 * 
	 * A string is a validate string iff
	 * 1. From begin to any position, count of '(' + count of '*' >= count of ')'
	 * 2. From end back to any position, count of ')' + count of '*' >= count of '('
	 * 
	 * Proof:
	 * If 1, any ')' will always be matched by a '(' or '*' on its left
	 * If 2, any '(' will always be matched by a ')' or '*' on its right
	 * So if 1 and 2 are satisfied, the string is valid
	 * 
	 * Rf : https://leetcode.com/problems/valid-parenthesis-string/discuss/107612/O(n)-Java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/107581/O(n)-time-O(1)-space-no-Recursion-just-scan-from-left-and-then-scan-from-right
	 */
	public boolean checkValidString(String s) {
		int sum = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '*' || s.charAt(i) == '(')
				sum++;
			else
				sum--;
			
			if (sum < 0)
				return false;
		}
		if (sum == 0)
			return true;
		
		sum = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == '*' || s.charAt(i) == ')')
				sum++;
			else
				sum--;
			
			if (sum < 0)
				return false;
		}
		return true;
	}
	
	/*
	 * by myself 2
	 * 
	 * Rf : https://leetcode.com/problems/valid-parenthesis-string/discuss/107582/Simple-Python-O(n)-solution-without-DP-or-greedy
	 */
	public boolean checkValidString_self2(String s) {
		if (s == null || s.length() == 0)
            return true;
        
        return check_self2(s, true) && check_self2(new StringBuilder(s).reverse().toString(), false);
    }
	private boolean check_self2(String s, boolean left) {
        char target = '(';
        if (!left) {
            target = ')';
        }
        
        int star = 0;
        int open = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == target) {
                open++;
            }
            else if (c == '*') {
                star++;
            }
            else {
                if (open == 0) {
                    if (star > 0) {
                        star--;
                    }
                    else {
                        return false;
                    }
                }
                else {                	
                	open--;
                }
            }
        }
        
        if (open <= star)
            return true;
        else
            return false;
    }
	
	/*
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/107572/Java-using-2-stacks.-O(n)-space-and-time-complexity.
	 * 
	 * Push all the indices of the star and left bracket to their stack respectively.
	 * 
	 * 1. Once a right bracket comes, pop left bracket stack first if it is not empty. 
	 *    If the left bracket stack is empty, pop the star stack if it is not empty. 
	 *    A false return can be made provided that both stacks are empty.
	 * 
	 * 2. Now attention is paid to the remaining stuff in these two stacks. Note that 
	 *    the left bracket CANNOT appear after the star as there is NO way to balance 
	 *    the bracket. In other words, whenever there is a left bracket index appears 
	 *    after the Last star, a false statement can be made. Otherwise, pop out each 
	 *    from the left bracket and star stack.
	 *
	 * 3. A correct sequence should have an empty left bracket stack. You don't need 
	 * to take care of the star stack.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/112540/Straightforward-Java-O(n)-solution-with-explanation
	 */
	public boolean checkValidString_stack(String s) {
		Stack<Integer> leftID = new Stack<>();
		Stack<Integer> starID = new Stack<>();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			
			if (ch == '(')
				leftID.push(i);
			else if (ch == '*')
				starID.push(i);
			else {
				if (leftID.isEmpty() && starID.isEmpty())
					return false;
				
				if (!leftID.isEmpty())
					leftID.pop();
				else
					starID.pop();
			}
		}
		
		while (!leftID.isEmpty() && !starID.isEmpty()) {
			if (leftID.pop() > starID.pop())
				return false;
		}
		return leftID.isEmpty();
	}
	
	/*
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/107577/Short-Java-O(n)-time-O(1)-space-one-pass
	 * 
	 * '*' can be used as one of '(', ')', empty. The number of unbalanced '(' becomes
	 * a range. when the count is -1, that means there are more ')'s than '('s, and we 
	 * need to stop early at that route, since it is invalid.
	 * 
	 * low : considers each '*' as ')' as much as possible (treat it as empty string 
	 *       if low<0.
	 * high : considers each '*' as '(', which should never be negative
	 * 
	 * if high < 0 means too much ')'
	 * if low > 0 , after the count finished, means too much '('
	 * 
	 * since low take '*' as ')', there might be too much ')', so that low might less 
	 * than 0. That's why low-- should happen only low > 0. This can thought as, low 
	 * only take as much as '(''s ')' and ignore other ')' s. This will not cause 
	 * problem since :
	 * 1. '*' can be treated as empty
	 * 2. high has deal with the situation that too much ')' exist
	 * 
	 * Rf :
	 * leetcode.com/problems/valid-parenthesis-string/discuss/107570/Python-easy-understand-solution/109695
	 * leetcode.com/problems/valid-parenthesis-string/discuss/107577/Short-Java-O(n)-time-O(1)-space-one-pass/109718
	 * https://leetcode.com/problems/valid-parenthesis-string/discuss/107611/Very-concise-C++-solution-with-explaination.-No-DP
	 */
	public boolean checkValidString_1Pass(String s) {
		int low = 0;
		int high = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				low++;
				high++;
			} 
			else if (s.charAt(i) == ')') {
				if (low > 0) {
					low--;
				}
				high--;
			} 
			else {
				if (low > 0) {
					low--;
				}
				high++;
			}
			
			if (high < 0) {
				return false;
			}
		}
		return low == 0;
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public boolean checkValidString_self(String s) {
        if (s == null || s.length() == 0)
            return true;
        
        return check_self(s, true) && check_self(new StringBuilder(s).reverse().toString(), false);
    }
    private boolean check_self(String s, boolean left) {
        char target = '(';
        if (!left) {
            target = ')';
        }
        
        int count = 0;
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == target) {
                stack.offer(c);
            }
            else if (c == '*') {
                count++;
            }
            else {
                if (stack.isEmpty()) {
                    if (count > 0) {
                        count--;
                    }
                    else {
                        return false;
                    }
                }
                
                stack.pollLast();
            }
        }
        
        if (stack.isEmpty())
            return true;
        else if (stack.size() <= count)
            return true;
        else
            return false;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/valid-parenthesis-string/discuss/107566/Java-12-lines-solution-backtracking
     * 
     * 1. When we see (, count++; when we see ) count--; if count < 0, it is 
     *    invalid ) is more than (; At last, count should == 0.
     * 2. This problem added *. The easiest way is to try 3 possible ways when we see 
     *    it. Return true if one of them is valid.
     *    
     * Other code:
     * leetcode.com/problems/valid-parenthesis-string/discuss/107566/Java-12-lines-solution-backtracking/117829
     * https://leetcode.com/problems/valid-parenthesis-string/discuss/107602/AC-JAVA-solution-using-recursion
     */
	public boolean checkValidString_recursive(String s) {
		return check_recursive(s, 0, 0);
	}

	private boolean check_recursive(String s, int start, int count) {
		if (count < 0)
			return false;

		for (int i = start; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(') {
				count++;
			} 
			else if (c == ')') {
				if (count == 0)
					return false;
				count--;
			} 
			else if (c == '*') {
				return check_recursive(s, i + 1, count + 1) 
						|| check_recursive(s, i + 1, count - 1) 
						|| check_recursive(s, i + 1, count);
			}
		}

		return count == 0;
	}
    
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/valid-parenthesis-string/discuss/107566/Java-12-lines-solution-backtracking/113674
	 * 
	 * We can improved recursive solution by adding a 2D dp array, the worst time 
	 * complexity will be improved to O(n^2).
	 */
	public boolean checkValidString_memory(String s) {
		Boolean[][] dp = new Boolean[s.length() + 1][s.length() + 1];
		return check_memory(s, 0, 0, dp);
	}
	private boolean check_memory(String s, int start, int count, Boolean[][] dp) {
		if (count < 0)
			return false;
		
		if (dp[start][count] != null)
			return dp[start][count];
		
		int y = count;
		for (int i = start; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (c == '(') {
				count++;
			} 
			else if (c == ')') {
				count--;
				if (count < 0)
					return false;
			} 
			else {                 // if (c == '*')
				dp[start][y] = (check_memory(s, i + 1, count + 1, dp) 
						|| check_memory(s, i + 1, count - 1, dp)
						|| check_memory(s, i + 1, count, dp));
				
				return dp[start][y];
			}
		}
		
		dp[start][y] = (count == 0);
		return dp[start][y];
	}
	
	/*
	 * https://leetcode.com/articles/valid-parenthesis-string/
	 * 
	 * Approach #2: Dynamic Programming
	 * 
	 * Let dp[i][j] be true if and only if the interval s[i], s[i+1], ..., s[j] can 
	 * be made valid. Then dp[i][j] is true only if:
	 * 
	 * 1. s[i] is '*', and the interval s[i+1], s[i+2], ..., s[j] can be made valid;
	 * 2. s[i] can be made to be '(', and there is some k in [i+1, j] such that s[k] 
	 *    can be made to be ')', plus the two intervals cut by s[k] (s[i+1 : k] and 
	 *    s[k+1 : j+1]) can be made valid;
	 */
	public boolean checkValidString_dp(String s) {
		int n = s.length();
		if (n == 0)
			return true;
		
		boolean[][] dp = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			if (s.charAt(i) == '*')
				dp[i][i] = true;
			
			if (i < n - 1 && (s.charAt(i) == '(' || s.charAt(i) == '*')
					&& (s.charAt(i + 1) == ')' || s.charAt(i + 1) == '*')) {
				dp[i][i + 1] = true;
			}
		}

		for (int size = 2; size < n; size++) {
			for (int i = 0; i + size < n; i++) {
				if (s.charAt(i) == '*' && dp[i + 1][i + size] == true) {
					dp[i][i + size] = true;
				} 
				else if (s.charAt(i) == '(' || s.charAt(i) == '*') {
					for (int k = i + 1; k <= i + size; k++) {
						if ((s.charAt(k) == ')' || s.charAt(k) == '*') 
								&& (k == i + 1 || dp[i + 1][k - 1])
								&& (k == i + size || dp[k + 1][i + size])) {
							dp[i][i + size] = true;
						}
					}
				}
			}
		}
		return dp[0][n - 1];
	}
}
