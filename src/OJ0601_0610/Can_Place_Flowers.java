package OJ0601_0610;

public class Can_Place_Flowers {
	/*
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution
	 * 
	 * There are `count` zeroes in between two 1s.
	 * 
	 * Total 3 cases need to be considered:
	 * 
	 * 1. 0s in between two 1s , we can place (count - 1)/2
	 * 2. leading or trailing 0s, we can place count/2
	 * 3. all '0s', we can put (count + 1)/2
	 * 
	 * To generalize the algorithm and to simplify code inside loop, count has been 
	 * initialized to 1 for the first time and result += (count-1)/2 effectively 
	 * becomes result += count/2 for leading 0s. For trailing 0s, result is updated 
	 * outside the loop, again by count/2 times.
	 * 
	 * For the last case, count has been initialized to 1, so after the for loop, 
	 * result += count/2; becomes result += (count + 1)/2;
	 * 
	 * Rf :
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution/236171
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution/137406
	 * https://leetcode.com/problems/can-place-flowers/discuss/960440/Java-Two-solutions%3A-Straightforward-approach-AND-zero-count-approach
	 */
	public boolean canPlaceFlowers_empty_length(int[] flowerbed, int n) {
		int result = 0;
		
		// we assume the first position has an empty plot before it
		int count = 1;
		for (int i = 0; i < flowerbed.length; i++) {
			if (flowerbed[i] == 0) {
				count++;
			} 
			else {
				result += (count - 1) / 2;
				count = 0;
			}
		}
		
		// we assume the last position has an empty plot after it
		if (count != 0)
			result += count / 2;
		
		return result >= n;
	}
	
	/*
	 * https://leetcode.com/problems/can-place-flowers/discuss/103893/C%2B%2BJava-Clean-Code
	 * 
	 * we can traverse over all the elements of the bed and find out those elements 
	 * which are 0 (implying an empty position). For every such element, we check if 
	 * its both adjacent positions are also empty. If so, we can plant a flower at 
	 * the current position without violating the no-adjacent-flowers-rule. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/can-place-flowers/discuss/678202/my-proof-why-greedy-solution-works
	 * https://leetcode.com/problems/can-place-flowers/solution/
	 * https://leetcode.com/problems/can-place-flowers/solution/129521
	 * https://leetcode.com/problems/can-place-flowers/discuss/103898/Java-Greedy-solution-O(flowerbed)-beats-100/106895
	 * 
	 * Other code:
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution/106875
	 */
	public boolean canPlaceFlowers_3_empty2(int[] bed, int n) {
		for (int i = 0; i < bed.length; i++) {
			if (bed[i] == 0 && (i == 0 || bed[i - 1] == 0) 
					&& (i == bed.length - 1 || bed[i + 1] == 0)) {
				
				bed[i] = 1;
				n--;
			}
		}
		return n <= 0;
	}
	
	/*
	 * https://leetcode.com/problems/can-place-flowers/discuss/103898/Java-Greedy-solution-O(flowerbed)-beats-100
	 * 
	 * Greedily place a flower at every vacant spot encountered from left to right!
	 * 
	 * Rf :
	 * https://leetcode.com/problems/can-place-flowers/discuss/678202/my-proof-why-greedy-solution-works
	 * https://leetcode.com/problems/can-place-flowers/discuss/103898/Java-Greedy-solution-O(flowerbed)-beats-100/106895
	 * 
	 * Other code:
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution/106875
	 * https://leetcode.com/problems/can-place-flowers/discuss/960667/Java-2-Solutions-or-Data-preprocessing-or-Check-Prev-and-Next
	 * https://leetcode.com/problems/can-place-flowers/discuss/912546/Java-Very-simple-clean-and-beginner-friendly-solution-Less-lines-of-code
	 * https://leetcode.com/problems/can-place-flowers/discuss/960526/Java-video-solution-explained-like-a-cake-walk-O(n)-time-O(1)-space
	 */
	public boolean canPlaceFlowers_3_empty(int[] flowerbed, int n) {
		int count = 0;
		for (int i = 0; i < flowerbed.length && count < n; i++) {
			if (flowerbed[i] == 0) {
				
				// get next and prev flower bed slot values. 
				// If i lies at the ends the next and prev are considered as 0.
				int next = (i == flowerbed.length - 1) ? 0 : flowerbed[i + 1];
				int prev = (i == 0) ? 0 : flowerbed[i - 1];
				
				if (next == 0 && prev == 0) {
					flowerbed[i] = 1;
					count++;
				}
			}
		}

		return count == n;
	}
	
	/*
	 * https://leetcode.com/problems/can-place-flowers/discuss/960667/Java-2-Solutions-or-Data-preprocessing-or-Check-Prev-and-Next
	 * 
	 * Append 0s to allow for an inner scan (i = 1 to len - 1)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/can-place-flowers/discuss/324794/Python-O(n)-with-Explanation
	 */
	public boolean canPlaceFlowers_append(int[] flowerbed, int n) {
		int l = flowerbed.length;
		int[] A = new int[l + 2];
		for (int i = 0; i < l; i++) {
			A[i + 1] = flowerbed[i];
		}

		int count = 0;
		for (int i = 1; i <= l; i++) {
			if (A[i] == 0 && A[i - 1] == 0 && A[i + 1] == 0) {
				count++;
				A[i] = 1;
			}
		}

		return count >= n;
	}
	
	/*
	 * Modified by myself
	 * 
	 * 1. when we have empty cases in the start (or end), like 1.., 01.., 001.., 
	 *    0001.., 00001.., ... We can notice, that if we have k zeroes, than we can 
	 *    put no more than k/2 flowers.
	 * 2. Case in the middle, like ..11.., ..101.., ..1001.., ..10001.., ..100001..: 
	 *    we can notice, that we can put no more than (k-1)/2 flowers in each group.
	 * 
	 * How to handle this two groups so they work in the same way? Add 0 to the 
	 * beginning and to the end of our flowerbed. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/can-place-flowers/discuss/960419/Python-2-lines-using-groupby-explained
	 */
	public boolean canPlaceFlowers_modified_append(int[] flowerbed, int n) {
		int len = flowerbed.length;
		int[] exFlowerbed = new int[len + 2];
		for (int i = 0; i < len; i++) {
			exFlowerbed[i + 1] = flowerbed[i];
		}

		int count = 0;
		int interval = 0;
		for (int i = 0; i < exFlowerbed.length; i++) {
			if (exFlowerbed[i] == 0) {
				interval++;
			}
			else {
				count = count + (interval - 1) / 2;
				interval = 0;
			}
		}
		
		// If last one is empty
		if (interval > 0) {
            count = count + (interval - 1) / 2;
        }

		return count >= n;
	}
	
	/*
	 * https://leetcode.com/problems/can-place-flowers/discuss/103895/Java-DP-solution-for-reference
	 * 
	 * K: number of flowers to plant
	 * N: length of flower bed
	 * O(K) space, O(NK) time
	 */
	public boolean canPlaceFlowers_dp(int[] flowerbed, int n) {
	    // dp[i][j] : can put j flowers in first i place, i, j starting from 1
	    // dp[i][j] = 
	    //            dp[i-1][j] || dp[i-2][j-1], if flower[i] == 0 
	    //            dp[i-2][j], if flower[i] == 1
	    
		// nothing to plant
		if (n == 0) {
			return true;
		}

		// no place to plant
		if (flowerbed.length == 0) {
			return false;
		}

		boolean[][] dp = new boolean[3][n + 1];
		dp[0][0] = true;
	    
		// init: first j flowers put into non-space
		for (int j = 1; j <= n; j++) {
			dp[0][j] = false;
		}

		// init: first j flowers put into first space
		for (int j = 1; j <= n; j++) {
			dp[1][j] = j == 1 && flowerbed[j - 1] == 0;
		}

		// init: no flowers put into first i space
		for (int i = 1; i < 3; i++) {
			dp[i][0] = true;
		}

		// dp
		for (int i = 2; i <= flowerbed.length; i++) {
			for (int j = 1; j <= n; j++) {
				if (flowerbed[i - 1] == 0) {
					dp[i % 3][j] = dp[(i - 1) % 3][j] 
							|| (dp[(i - 2) % 3][j - 1] && flowerbed[i - 2] == 0);
				} 
				else {
					dp[i % 3][j] = dp[(i - 2) % 3][j];
				}
			}
		}
	    
	    return dp[flowerbed.length % 3][n];
	}
	
	// by myself
	public boolean canPlaceFlowers_self2(int[] flowerbed, int n) {
        int start = 0;
        while (start < flowerbed.length && flowerbed[start] == 0) {
            start++;
        }
        
        if (start == flowerbed.length) {
            return (flowerbed.length + 1) / 2 >= n;
        }
        
        int end = flowerbed.length - 1;
        while (end >= 0 && flowerbed[end] == 0) {
            end--;
        }
        
        int res = start / 2 + (flowerbed.length - 1 - end) / 2;
        
        int i = start;
        while (i < end) {
            int j = i + 1;
            while (j < flowerbed.length && flowerbed[j] == 0) {
                j++;
            }
            
            res = res + (j - i - 2) / 2;
            i = j;
        }
        
        return res >= n;
    }
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/can-place-flowers/discuss/103883/Java-Very-easy-solution/239618
	 */
	public boolean canPlaceFlowers_self(int[] flowerbed, int n) {
        int max = 0;
        
        int left = 0;
        for (; left < flowerbed.length && flowerbed[left] == 0; left++) { }
        
        if (left == flowerbed.length) {
            max += (left + 1) / 2;
        }
        else {
            max += left / 2;
        }
        
        for (int i = left + 1; i < flowerbed.length; i++) {
            if (flowerbed[i] == 1) {
                if (i - left - 1 > 1) {
                    max += (i - left - 2) / 2;
                }
                left = i;
            }
        }
        max += (flowerbed.length - left - 1) / 2;
        
        return max >= n;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/can-place-flowers/discuss/103969/simple-count-zero-solution-python-and-c%2B%2B
     * https://leetcode.com/problems/can-place-flowers/discuss/324794/Python-O(n)-with-Explanation
     * https://leetcode.com/problems/can-place-flowers/discuss/960419/Python-2-lines-using-groupby-explained
     * https://leetcode.com/problems/can-place-flowers/discuss/103890/Python-Straightforward-with-Explanation
     * https://leetcode.com/problems/can-place-flowers/discuss/184330/Python-easy-solution-beats-100
     * https://leetcode.com/problems/can-place-flowers/discuss/380474/Three-Solutions-in-Python-3-(beats-~100)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/can-place-flowers/discuss/292300/No-need-to-mutate-array-C%2B%2B-99.6-C-99.6-Javascript-100
     * https://leetcode.com/problems/can-place-flowers/discuss/960942/C%2B%2B-Greedy-Solution-with-step-by-step-optimization
     * https://leetcode.com/problems/can-place-flowers/discuss/103933/simplest-c%2B%2B-code
     * https://leetcode.com/problems/can-place-flowers/discuss/960583/C%2B%2B-Simple-and-Easy-to-understand-solution
     * https://leetcode.com/problems/can-place-flowers/discuss/960530/C%2B%2B-or-Recursion-%2B-Memoization-or-DP
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/can-place-flowers/discuss/292300/No-need-to-mutate-array-C%2B%2B-99.6-C-99.6-Javascript-100
	 * https://leetcode.com/problems/can-place-flowers/discuss/501184/JavaScript-solution-easy-1-pass
	 */

}
