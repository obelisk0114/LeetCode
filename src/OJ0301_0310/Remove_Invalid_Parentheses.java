package OJ0301_0310;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

// https://leetcode.com/problems/remove-invalid-parentheses/discuss/75038/Evolve-from-intuitive-solution-to-optimal-a-review-of-all-solutions

public class Remove_Invalid_Parentheses {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75032/Share-my-Java-BFS-solution
	 * 
	 * With the input string s, we generate all possible states by removing one ( or 
	 * ), check if they are valid, if found valid ones on the current level, put them 
	 * to the final result list and we are done, otherwise, add them to a queue and 
	 * carry on to the next level.
	 * 
	 * T(n) = n x C(n, n) + (n-1) x C(n, n-1) + ... + 1 x C(n, 1) = n x 2^(n-1).
	 * 
	 * Other code:
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75084/Clean-Java-solution-BFS-+-optimization-40ms
	 */
	public List<String> removeInvalidParentheses(String s) {
		List<String> res = new ArrayList<>();

		// sanity check
		if (s == null)
			return res;

		Set<String> visited = new HashSet<>();
		Queue<String> queue = new LinkedList<>();

		// initialize
		queue.add(s);
		visited.add(s);

		boolean found = false;

		while (!queue.isEmpty()) {
			s = queue.poll();

			if (isValid(s)) {
				// found an answer, add to the result
				res.add(s);
				found = true;
			}

			if (found)
				continue;

			// generate all possible states
			for (int i = 0; i < s.length(); i++) {
				// we only try to remove left or right paren
				if (s.charAt(i) != '(' && s.charAt(i) != ')')
					continue;

				String t = s.substring(0, i) + s.substring(i + 1);

				if (!visited.contains(t)) {
					// for each state, if it's not visited, add it to the queue
					queue.add(t);
					visited.add(t);
				}
			}
		}

		return res;
	}

	// helper function checks if string s contains valid parantheses
	boolean isValid(String s) {
		int count = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				count++;
			if (c == ')' && count-- == 0)
				return false;
		}

		return count == 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75098/Java-BFS(40ms)-and-DFS(17ms)-without-HashSet
	 * 
	 * 1. continuous (((+ or )))+ are guaranteed to be duplicate
	 * 2. if we define an extra element to identify the position of last removed 
	 *    index, then next BFS, we'll only need to start from there, instead of loop 
	 *    from 0 to str.length. According to combination 
	 *    C(n,k) = C(n-1,k-1) + C(n-1,k)
	 */
	public List<String> removeInvalidParentheses_store_index(String s) {
		Queue<String> queue = new LinkedList<>();
		Queue<Integer> ends = new LinkedList<>(); // here consider ends is index of last removed element!

		List<String> res = new ArrayList<>();
		int size = 1;
		
		queue.offer(s);
		ends.add(s.length());

		while (!queue.isEmpty()) {
			String str = queue.poll();
			int end = ends.poll();

			if (isValid_store_index(str))
				res.add(str);

			// only when not found, we need to consider next level
			if (res.size() > 0)
				continue;
			
			for (int i = end - 1; i >= 0; i--) {
				if (str.charAt(i) != '(' && str.charAt(i) != ')')
					continue;
				
				String next = (new StringBuilder()).append(str.substring(0, i)).append(str.substring(i + 1)).toString();
				queue.offer(next);
				ends.add(i);

				// skip continuous ')' and '('
				while (i > 0 && str.charAt(i) == str.charAt(i - 1))
					i--;
			}

			// check level finish, if not found, go on to next level
			if (--size == 0) {
				if (res.size() > 0)
					break;
				
				size = queue.size();
			}
		}
		return res;
	}

	private boolean isValid_store_index(String s) {
		int open = 0;
		for (char c : s.toCharArray()) {
			if (c == '(')
				open++;
			else if (c == ')')
				open--;
			
			if (open < 0)
				return false;
		}
		return open == 0;
	}
	
	/*
	 * The following 2 functions and class are from this link.
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75041/Java-BFS-solution-16ms-avoid-generating-duplicate-strings
	 * 
	 * 1. "())" ---> "()"
	 * only remove the first one of '))'
	 * 
	 * 2. we need remove 2 from "(())(("
	 * We want to remove positions combination i,j with no duplicate
	 * So we let i < j then it will not generate duplicate combinations
	 * In practice, we record the position i and put it in to queue
	 * which is then polled out and used as the starting point of the next removal
	 * 
	 * 3. If the previous step we removed a "(", we should never remove a ")" in the 
	 * following steps. This is obvious since otherwise we could just save these two 
	 * removals and still be valid with less removals. Possible removals will be 
	 * something like this     ")))))))))((((((((("
	 * All the removed characters forming a string with consecutive left bracket 
	 * followed by consecutive right bracket.
	 */
	public List<String> removeInvalidParentheses2(String s) {
		if (isValid(s))
			return Collections.singletonList(s);
		
		List<String> ans = new ArrayList<>();
		
		// The queue only contains invalid middle steps
		Queue<Tuple> queue = new LinkedList<>();
		// The 3-Tuple is (string, startIndex, lastRemovedChar)
		queue.add(new Tuple(s, 0, ')'));
		
		while (!queue.isEmpty()) {
			Tuple x = queue.poll();
			// Observation 2, start from last removal position
			for (int i = x.start; i < x.string.length(); ++i) {
				char ch = x.string.charAt(i);
				
				// Not parentheses
				if (ch != '(' && ch != ')')
					continue;
				// Observation 1, do not repeatedly remove from consecutive ones
				if (i != x.start && x.string.charAt(i - 1) == ch)
					continue;
				// Observation 3, do not remove a pair of valid parentheses
				if (x.removed == '(' && ch == ')')
					continue;
				
				String t = x.string.substring(0, i) + x.string.substring(i + 1);
				
				// Check isValid before add
				if (isValid2(t))
					ans.add(t);
				// Avoid adding leaf level strings
				else if (ans.isEmpty())
					queue.add(new Tuple(t, i, ch));
			}
		}
		return ans;
	}

	public boolean isValid2(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			
			if (c == '(')
				++count;
			if (c == ')' && count-- == 0)
				return false;
		}
		return count == 0;
	}

	private class Tuple {
		public final String string;
		public final int start;
		public final char removed;

		public Tuple(String string, int start, char removed) {
			this.string = string;
			this.start = start;
			this.removed = removed;
		}
	}
	
	// The following 2 functions are by myself.
	public List<String> removeInvalidParentheses_self(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            ans.add("");
            return ans;
        }
        if (check_self(s)) {
            ans.add(s);
            return ans;
        }
        
        LinkedList<String> queueTail = new LinkedList<>();
        queueTail.offer(s);
        LinkedList<String> queueHead = new LinkedList<>();
        queueHead.offer("");
        
        boolean find = false;
        while (!queueTail.isEmpty() && !find) {
            int size = queueTail.size();
            for (int i = 0; i < size; i++) {
                String cur = queueTail.poll();
                String storeHead = queueHead.poll();
                
                for (int j = 0; j < cur.length(); j++) {
                    if (cur.charAt(j) != '(' && cur.charAt(j) != ')')
                        continue;
                    if (j > 0 && cur.charAt(j) == cur.charAt(j - 1))
                        continue;
                    
                    String head = storeHead + cur.substring(0, j);
                    String tail = cur.substring(j + 1, cur.length());
                    String next = head + tail;
                    
                    queueHead.offer(head);
                    queueTail.offer(tail);
                    
                    if (check_self(next)) {
                        ans.add(next);
                        find = true;
                    }
                }
            }
        }
        return ans;
    }
    
    private boolean check_self(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            
            if (cur != '(' && cur != ')')
                continue;
            if (cur == '(')
                stack.offer(cur);
            else if (cur == ')' && stack.isEmpty())
                return false;
            else if (cur == ')' && stack.peekLast() == '(')
                stack.pollLast();
        }
        return stack.isEmpty();
    }
    
    /*
     * The following 2 functions are from this link.
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/137487
     * 
     * Other code:
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/147484
     */
    public List<String> removeInvalidParentheses_reverse_beautiful(String s) {
        List<String> ans = new ArrayList<String>();
        remove2(s, ans, 0, 0, 0, new char[] {'(', ')'});
        return ans;
    }
    
	private void remove2(String s, List<String> ans, int count, int last_i, 
			int last_j, char[] par) {
		// count means the current number of par[0] - that of par[1]
		if (last_i == s.length() && par[0] == ')') {
			ans.add(new StringBuilder(s).reverse().toString());
			return;
		}
		if (last_i == s.length() && par[0] == '(') {
			String reverse = new StringBuilder(s).reverse().toString();
			remove2(reverse, ans, 0, 0, 0, new char[] { ')', '(' });
			return;
		}
		
		char c = s.charAt(last_i);
		if (c == par[1] && count == 0) {
			for (int j = last_j; j <= last_i; j++) {
				if (s.charAt(j) == par[1] && (j == last_j || s.charAt(j - 1) != par[1])) {
					remove2(s.substring(0, j) + s.substring(j + 1), ans, 0, last_i, j, par);
				}
			}
		} 
		else {
			if (c == par[0])
				count++;
			if (c == par[1])
				count--;
			
			remove2(s, ans, count, last_i + 1, last_j, par);
		}
	}
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution
     * 
     * The counter will increase when it is '(' and decrease when it is ')'. Whenever 
     * the counter is negative, we have more ')' than '(' in the prefix.
     * To make the prefix valid, we need to remove a ')'. We restrict ourself to 
     * remove the first ) in a series of consecutive )s.
     * 
     * After the removal, the prefix is then valid. We then call the function 
     * recursively to solve the rest of the string. However, we need to keep another 
     * information: the last removal position. If we do not have this position, we 
     * will generate duplicate by removing two ')' in two steps only with a different 
     * order.
     * 
     * We keep tracking the last removal position and only remove ')' after that.
     * 
     * Do the same from right to left in which we need remove '('.
     * Reverse the string and reuse the code!
     * 
     * We've abandoned one character, so the index of i,j are exactly new begin point 
     * for searching.
     * 
     * Rf :
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/113024
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/156556
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/78043
     * leetcode.com/problems/remove-invalid-parentheses/discuss/75027/Easy-Short-Concise-and-Fast-Java-DFS-3-ms-solution/78108
     */
	public List<String> removeInvalidParentheses_reverse(String s) {
		List<String> ans = new ArrayList<>();
		remove(s, ans, 0, 0, new char[] { '(', ')' });
		return ans;
	}

	public void remove(String s, List<String> ans, int last_i, int last_j, char[] par) {
		for (int stack = 0, i = last_i; i < s.length(); ++i) {
			if (s.charAt(i) == par[0])
				stack++;
			if (s.charAt(i) == par[1])
				stack--;
			
			if (stack >= 0)
				continue;
			
			for (int j = last_j; j <= i; ++j)
				if (s.charAt(j) == par[1] && (j == last_j || s.charAt(j - 1) != par[1]))
					remove(s.substring(0, j) + s.substring(j + 1, s.length()), ans, i, j, par);
			return;
		}
		
		String reversed = new StringBuilder(s).reverse().toString();
		if (par[0] == '(') // finished left to right
			remove(reversed, ans, 0, 0, new char[] { ')', '(' });
		else               // finished right to left
			ans.add(reversed);
	}
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75095/Java-optimized-DFS-solution-3-ms
     * 
     * 1. Before starting DFS, calculate the total numbers of opening and closing 
     *    parentheses that need to be removed in the final solution, then these two 
     *    numbers could be used to speed up the DFS process.
     * 2. Use while loop to avoid duplicate result in DFS, instead of using HashSet.
     * 3. Use count variable to validate the parentheses dynamically.
     */
	public List<String> removeInvalidParentheses_pre_calculate(String s) {
		int count = 0, openN = 0, closeN = 0;

		// calculate the total numbers of opening and closing parentheses
		// that need to be removed in the final solution
		for (char c : s.toCharArray()) {
			if (c == '(') {
				count++;
			} 
			else if (c == ')') {
				if (count == 0)
					closeN++;
				else
					count--;
			}
		}
		openN = count;
		count = 0;

		if (openN == 0 && closeN == 0)
			return Arrays.asList(s);

		List<String> result = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		dfs_pre_calculate(s.toCharArray(), 0, count, openN, closeN, result, sb);
		return result;
	}

	private void dfs_pre_calculate(char[] s, int p, int count, int openN, int closeN, 
			List<String> result, StringBuilder sb) {
		if (count < 0)
			return; // the parentheses is invalid

		if (p == s.length) {
			// the minimum number of invalid parentheses have been removed
			if (openN == 0 && closeN == 0) {
				result.add(sb.toString());
			}
			return;
		}

		if (s[p] != '(' && s[p] != ')') {
			sb.append(s[p]);
			dfs_pre_calculate(s, p + 1, count, openN, closeN, result, sb);
			sb.deleteCharAt(sb.length() - 1);
		} 
		else if (s[p] == '(') {
			int i = 1;
			// use while loop to avoid duplicate result in DFS, instead of using HashSet
			while (p + i < s.length && s[p + i] == '(')
				i++;
			
			sb.append(s, p, i);
			dfs_pre_calculate(s, p + i, count + i, openN, closeN, result, sb);
			sb.delete(sb.length() - i, sb.length());

			if (openN > 0) {
				// remove the current opening parenthesis
				dfs_pre_calculate(s, p + 1, count, openN - 1, closeN, result, sb);
			}
		} 
		else {
			int i = 1;
			// use while loop to avoid duplicate result in DFS, instead of using HashSet
			while (p + i < s.length && s[p + i] == ')')
				i++;
			
			sb.append(s, p, i);
			dfs_pre_calculate(s, p + i, count - i, openN, closeN, result, sb);
			sb.delete(sb.length() - i, sb.length());

			if (closeN > 0) {
				// remove the current closing parenthesis
				dfs_pre_calculate(s, p + 1, count, openN, closeN - 1, result, sb);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75034/Easiest-9ms-Java-Solution
	 * 
	 * 1. Limit max removal rmL and rmR for backtracking boundary. Otherwise it will 
	 *    exhaust all possible valid substrings, not shortest ones.
	 * 2. Scan from left to right, avoiding invalid strs (on the fly) by checking num 
	 *    of open parens.
	 * 3. If it's '(', either use it, or remove it.
	 * 4. If it's '(', either use it, or remove it.
	 * 5. Otherwise just append it.
	 * 6. Lastly set StringBuilder to the last decision point.
	 * 
	 * In each step, make sure:
	 * 1. i does not exceed s.length().
	 * 2. Max removal rmL rmR and num of open parens are non negative.
	 * 3. De-duplicate by adding to a HashSet.
	 * 
	 * If open < 0 that indicates your # of closing parenthesis is greater than open 
	 * parenthesis, so the recursive will stop because it's not possible to become 
	 * valid.
	 * 
	 * Rf :
	 * leetcode.com/problems/remove-invalid-parentheses/discuss/75034/Easiest-9ms-Java-Solution/78199
	 * leetcode.com/problems/remove-invalid-parentheses/discuss/75034/Easiest-9ms-Java-Solution/157095
	 */
	public List<String> removeInvalidParentheses_HashSet(String s) {
	    int rmL = 0, rmR = 0;
	    for (int i = 0; i < s.length(); i++) {
	        if (s.charAt(i) == '(') {
	            rmL++;
	        } 
	        else if (s.charAt(i) == ')') {
	            if (rmL != 0) {
	                rmL--;
	            } 
	            else {
	                rmR++;
	            }
	        }
	    }
	    Set<String> res = new HashSet<>();
	    dfs(s, 0, res, new StringBuilder(), rmL, rmR, 0);
	    return new ArrayList<String>(res);
	}

	public void dfs(String s, int i, Set<String> res, StringBuilder sb, int rmL, 
			int rmR, int open) {
	    if (rmL < 0 || rmR < 0 || open < 0) {
	        return;
	    }
	    if (i == s.length()) {
	        if (rmL == 0 && rmR == 0 && open == 0) {
	            res.add(sb.toString());
	        }        
	        return;
	    }

	    char c = s.charAt(i); 
	    int len = sb.length();

	    if (c == '(') {
	        dfs(s, i + 1, res, sb, rmL - 1, rmR, open);		        // not use (
	    	dfs(s, i + 1, res, sb.append(c), rmL, rmR, open + 1);       // use (
	    } 
	    else if (c == ')') {
	        dfs(s, i + 1, res, sb, rmL, rmR - 1, open);	            // not use )
	    	dfs(s, i + 1, res, sb.append(c), rmL, rmR, open - 1);  	    // use )
	    } 
	    else {
	        dfs(s, i + 1, res, sb.append(c), rmL, rmR, open);	
	    }

	    sb.setLength(len);        
	}

    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75098/Java-BFS(40ms)-and-DFS(17ms)-without-HashSet
     * 
     * 1. continuous (((+ or )))+ are guaranteed to be duplicate
     * 2. if we define an extra element to identify the position of last removed 
     *    index, then next BFS, we'll only need to start from there, instead of loop 
     *    from 0 to str.length. According to combination 
     *    C(n,k) = C(n-1,k-1) + C(n-1,k)
     */
	public List<String> removeInvalidParentheses_dfs_check_length(String s) {
		List<String> res = new ArrayList<>();
		dfs_length(res, s, 0, new StringBuilder(), 0);
		return res;
	}

	private void dfs_length(List<String> res, String s, int start, 
			StringBuilder path, int open) {
		if (open < 0)
			return;
		if (!res.isEmpty() && path.length() + s.length() - start < res.get(0).length())
			return;
		if (start == s.length()) {
			if (open == 0)
				res.add(path.toString());
			return;
		}

		path.append(s.charAt(start));
		if (s.charAt(start) != '(' && s.charAt(start) != ')')
			dfs_length(res, s, start + 1, path, open);
		else {
			dfs_length(res, s, start + 1, path, open + (s.charAt(start) == '(' ? 1 : -1));
			
			while (start < s.length() - 1 && s.charAt(start) == s.charAt(start + 1))
				start++;
		}
		path.deleteCharAt(path.length() - 1);
		
		dfs_length(res, s, start + 1, path, open);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/remove-invalid-parentheses/discuss/75104/DFS-and-BFS-java-solutions-add-one-more-Optimized-fast-DFS-solution
	 */
	public List<String> removeInvalidParentheses_HashMap(String s) {
		if (s.length() == 0)
			return new ArrayList<String>(Arrays.asList(""));
		
		Map<Integer, Set<String>> dics = new HashMap<Integer, Set<String>>();
		int[] min = { Integer.MAX_VALUE };
		char[] str = s.toCharArray();
		
		helper(dics, str, 0, "", 0, 0, min, 0);
		
		return new ArrayList<String>(dics.get(min[0]));
	}

	private void helper(Map<Integer, Set<String>> dics, char[] str, int start, 
			String cur, int left, int right, int[] min, int delete) {
		// Base Cases
		if (start == str.length) {
			if (left != right)
				return;
			
			if (!dics.containsKey(delete))
				dics.put(delete, new HashSet<String>());
			
			dics.get(delete).add(cur);
			min[0] = Math.min(min[0], delete);
			return;
		}
		
		if (left < right)
			return;
		
		if (str[start] == '(') {
			helper(dics, str, start + 1, cur + "(", left + 1, right, min, delete);
			helper(dics, str, start + 1, cur, left, right, min, delete + 1);
		} 
		else if (str[start] == ')') {
			helper(dics, str, start + 1, cur + ")", left, right + 1, min, delete);
			helper(dics, str, start + 1, cur, left, right, min, delete + 1);
		} 
		else {
			helper(dics, str, start + 1, cur + str[start], left, right, min, delete);
		}
	}

}
