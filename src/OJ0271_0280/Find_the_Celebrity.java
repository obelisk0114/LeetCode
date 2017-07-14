package OJ0271_0280;

import java.util.Stack;

public class Find_the_Celebrity {
	int findCelebrity_1(int n) {
		int candidate = 0;
		out:
		for (int i = 0; i < n; i++) {
			// Find the one that doesn't know other people
			for (int j = 0; j < n; j++) {
				if (candidate == j) {
					continue;
				}
				if (knows(candidate, j)) {     // Candidate knows j
					candidate++;
					continue out;
				}
			}
			
			// Check whether other people know candidate
			for (int k = 0; k < n; k++) {
				if (candidate == k) {
					continue;
				}
				if (!knows(k, candidate)) {   // k doesn't know candidate
					//candidate++;
					//continue out;
					return -1;
				}
			}
			
			if (candidate < n) {
				return candidate;
			}
			else {
				return -1;
			}
		}
		return -1;
	}
	
	int findCelebrity_2(int n) {
		int candidate = 0;
		out:
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (candidate == j) {
					continue;
				}
				if (knows(candidate, j)) {     // Candidate knows j
					candidate++;
					continue out;
				}
				if (!knows(j, candidate)) {    // j doesn't know candidate
					candidate++;
					continue out;
				}
			}
			
			if (candidate < n) {
				return candidate;
			}
			else {
				return -1;
			}
		}
		return -1;
	}
	
	int findCelebrity_3(int n) {
		int candidate = 0;
		out:
		while (candidate < n) {
			for (int j = 0; j < n; j++) {
				if (candidate == j) {
					continue;
				}
				if (knows(candidate, j)) {     // Candidate knows j
					if (candidate < j) {						
						candidate = j;         // 0 to j-1 were not known by candidate
					}
					else {
						candidate++;
					}
					continue out;
				}
				if (!knows(j, candidate)) {    // j doesn't know candidate
					if (candidate < j) {						
						candidate = j;         // 0 to j-1 were not known by candidate
					}
					else {
						candidate++;
					}
					continue out;
				}
			}
			
			return candidate;
		}
		return -1;
	}
	
	int findCelebrity_4(int n) {
		int candidate = 0;
		out:
		while (candidate < n) {
			for (int j = 0; j < n; j++) {
				if (candidate == j) {
					continue;
				}
				if (knows(candidate, j)) {     // Candidate knows j
					if (candidate < j) {						
						candidate = j;         // 0 to j-1 were not known by candidate
					}
					else {
						candidate++;
					}
					continue out;
				}
			}
			
			for (int k = 0; k < n; k++) {
				if (candidate == k) {
					continue;
				}
				if (!knows(k, candidate)) {    // j doesn't know candidate
					return -1;
				}
				
			}
			
			return candidate;
		}
		return -1;
	}
	
	// https://discuss.leetcode.com/topic/23534/java-solution-two-pass/
	int findCelebrity_switch(int n) {
		int candidate = 0;
		for (int i = 1; i < n; i++) {
			if (knows(candidate, i))
				candidate = i;
		}
		for (int i = 0; i < n; i++) {
			if ( i != candidate && ( knows(candidate, i) || !knows(i, candidate) ) )
				return -1;
		}
		return candidate;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/23534/java-solution-two-pass/14
	 * 
	 * In detail, suppose the candidate after the first for loop is person k, 
	 * it means 0 to k-1 cannot be the celebrity, 
	 * because they know a previous or current candidate.
	 * 
	 * Also, since k knows no one between k+1 and n-1, 
	 * k+1 to n-1 can not be the celebrity either. 
	 * Therefore, k is the only possible celebrity, if there exists one.
	 * 
	 * The remaining job is to check if k indeed does not know any other persons 
	 * and all other persons know k.
	 * 
	 * We can further shrink the calling of knows method. 
	 * For example, we don't need to check if k knows k+1 to n-1 in the second loop, 
	 * because the first loop has already done that.
	 */
	int findCelebrity_switch_enhance(int n) {
		int candidate = 0;
		for (int i = 1; i < n; i++) {
			if (knows(candidate, i))
				candidate = i;
		}
		for (int i = 0; i < n; i++) {
			if (i < candidate && knows(candidate, i) || !knows(i, candidate))
				return -1;
			if (i > candidate && !knows(i, candidate))
				return -1;
		}
		return candidate;
	}
	
	// Slow but interesting
	// https://discuss.leetcode.com/topic/23550/ac-java-solution-using-stack
	public int findCelebrity_stack(int n) {
	    // base case
	    if (n <= 0) return -1;
	    if (n == 1) return 0;
	    
	    Stack<Integer> stack = new Stack<>();
	    
	    // put all people to the stack
	    for (int i = 0; i < n; i++) stack.push(i);
	    
	    int a = 0, b = 0;
	    
	    while (stack.size() > 1) {
	        a = stack.pop(); b = stack.pop();
	        
	        if (knows(a, b)) 
	            // a knows b, so a is not the celebrity, but b may be
	            stack.push(b);
	        else 
	            // a doesn't know b, so b is not the celebrity, but a may be
	            stack.push(a);
	    }
	    
	    // double check the potential celebrity
	    int c = stack.pop();
	    
	    for (int i = 0; i < n; i++)
	        // c should not know anyone else
	        if (i != c && (knows(c, i) || !knows(i, c)))
	            return -1;
	    
	    return c;
	}
	
	// https://discuss.leetcode.com/topic/31595/java-concise-recursive-divide-and-conquer-bonus-3-line-java-8-stream-solution/2
	
	boolean knows(int a, int b) {
		// which tells you whether A knows B
		return true;
	}

}
