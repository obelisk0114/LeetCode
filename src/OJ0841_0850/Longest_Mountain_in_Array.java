package OJ0841_0850;

public class Longest_Mountain_in_Array {
	/*
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/165667/1-pass-Java-Two-Point-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/420466/Java-clear-3-pointers-solution
	 */
	public int longestMountain_3_while(int[] A) {
		int n = A.length;
		
		/* Can be removed
		if (n < 3)
			return 0;
		*/

		int left = 0;
		int right;
		int max = 0;

		while (left < n - 2) {
			// skip descending and equal array
			while (left < n - 1 && A[left] >= A[left + 1]) {
				left++;
			}
			
			right = left + 1;
			
			// mountain up
			while (right < n - 1 && A[right] < A[right + 1]) {
				right++;
			}
			
			// mountain down
			while (right < n - 1 && A[right] > A[right + 1]) {
				right++;
				
				// update the max value
				max = Math.max(max, right - left + 1);
			}
			left = right;
		}
		return max;
	}

	/*
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/244213/java-find-peak-and-stretch
	 * 
	 * All elements between i and right, were in a strictly decreasing manner.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/150136/Simple-O(n)-one-pass-O(1)-space-Java-AC-solution-beats-99.05/179015
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/760497/Detailed-Explanation-with-Java-O(n)-Time-and-O(1)-Space
	 */
	public int longestMountain_peak(int[] A) {
		int ans = 0;
		for (int i = 1; i < A.length - 1; i++) {
			if (A[i] > A[i - 1] && A[i] > A[i + 1]) { // i is a peak
				int left = i - 1;
				while (left > 0 && A[left - 1] < A[left])
					left--;

				int right = i + 1;
				while (right < A.length - 1 && A[right + 1] < A[right])
					right++;

				ans = Math.max(ans, right - left + 1);
				
				// move i
                i = right;
			}
		}
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/longest-mountain-in-array/solution/
	 * 
	 * For a starting index base, let's calculate the length of the longest mountain 
	 * A[base], A[base+1], ..., A[end].
	 * 
	 * If such a mountain existed, the next possible mountain will start at 
	 * base = end; if it didn't, then either we reached the end, or we have 
	 * A[base] >= A[base+1] and we can start at base + 1.
	 */
	public int longestMountain_level_while(int[] A) {
		int N = A.length;
		int ans = 0, base = 0;
		while (base < N) {
			int end = base;
			
			// if base is a left-boundary
			if (end + 1 < N && A[end] < A[end + 1]) {
				// set end to the peak of this potential mountain
				while (end + 1 < N && A[end] < A[end + 1])
					end++;

				// if end is really a peak..
				if (end + 1 < N && A[end] > A[end + 1]) {
					// set end to the right-boundary of mountain
					while (end + 1 < N && A[end] > A[end + 1])
						end++;
					
					// record candidate answer
					ans = Math.max(ans, end - base + 1);
				}
			}

			base = Math.max(end, base + 1);
		}

		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/135593/C%2B%2BJavaPython-1-pass-and-O(1)-space
	 * 
	 * Count up length and down length.
	 * Both up and down length are clear to 0 when A[i - 1] == A[i] or 
	 * down > 0 && A[i - 1] < A[i].
	 * 
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/135593/C++JavaPython-1-pass-and-O(1)-space/143182
	 */
	public int longestMountain_up_down(int[] A) {
		int res = 0, up = 0, down = 0;
		for (int i = 1; i < A.length; ++i) {
			if (down > 0 && A[i - 1] < A[i] || A[i - 1] == A[i])
				up = down = 0;
			
			if (A[i - 1] < A[i])
				up++;
			if (A[i - 1] > A[i])
				down++;
			
			if (up > 0 && down > 0 && up + down + 1 > res)
				res = up + down + 1;
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/637837/Java-3-passes-O(n)
	 * 
	 * left[i] means the length of the left part of the mountain whose peak is a[i]
	 * right[i] means the length of the right part of the mountain whose peak is a[i]
	 * If left[i] or right[i] is 0, this means that a[i] can't be the peak.
	 * Then just traversing again to get the longest mountain length;
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/135593/C%2B%2BJavaPython-1-pass-and-O(1)-space
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-mountain-in-array/discuss/540621/Java-DP-solution%3A-O(N)-time-and-O(N)-memory
	 */
	public int longestMountain_3_pass(int[] a) {
		int[] left = new int[a.length];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > a[i - 1]) {
				left[i] = left[i - 1] + 1;
			}
		}

		int[] right = new int[a.length];
		for (int i = a.length - 2; i >= 0; i--) {
			if (a[i] > a[i + 1]) {
				right[i] = right[i + 1] + 1;
			}
		}

		int max = 0;
		for (int i = 0; i < a.length; i++) {
			if (left[i] != 0 && right[i] != 0) {
				max = Math.max(left[i] + right[i] + 1, max);
			}
		}
		return max;
    }
	
	// by myself
	public int longestMountain_self(int[] A) {
        int left = 0;
        int max = 0;
        boolean up = true;
        for (int i = 1; i < A.length; i++) {
            if (up) {
                if (A[i] == A[i - 1]) {
                    left = i;
                }
                else if (A[i] < A[i - 1]) {
                    if (i >= 2 && A[i - 1] > A[i - 2]) {
                        up = false;
                    }
                    else {
                        left = i;
                    }
                }
            }
            else {
                if (A[i] == A[i - 1]) {
                    max = Math.max(max, i - left);
                    left = i;
                    up = true;
                }
                else if (A[i] > A[i - 1]) {
                    max = Math.max(max, i - left);
                    left = i - 1;
                    up = true;
                }
            }
        }
        
        if (!up) {
            max = Math.max(max, A.length - left);
        }
        
        if (max >= 3) {
            return max;
        }
        else {
            return 0;
        }
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-mountain-in-array/discuss/749845/Python-3-%2B-Explanation
     * https://leetcode.com/problems/longest-mountain-in-array/discuss/706751/Python%3A-straight-forward-solution
     * https://leetcode.com/problems/longest-mountain-in-array/discuss/571402/Python-3-State-Machine
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-mountain-in-array/discuss/419003/Easy-to-understand-C%2B%2B-Solution-20ms-beats-91
     * https://leetcode.com/problems/longest-mountain-in-array/discuss/271110/Easy-to-Understand-Code-O(n)
     */

}
