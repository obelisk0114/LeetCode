package OJ0981_0990;

import java.util.List;
import java.util.ArrayList;

public class Interval_List_Intersections {
	/*
	 * https://leetcode.com/articles/interval-list-intersections/
	 * 
	 * A[i][0]     A[i][1]
	 *     |----------|
	 *          |-------------|
	 *       B[j][0]       B[j][1]
	 * 
	 * overlapping from B[j][0] to A[i][1], we can use Math.max(A[i + 1][0], B[j][0])
	 * to get next start of overlapping.
	 * Because A[i + 1][0] > A[i][1] (pairwise disjoint and in sorted order)
	 * 
	 * Use 2 pointers to loop through A and B
	 * 1. Locate the first Intervals from A and B that have intersection; Only when 
	 *    the bigger start <= smaller end, there exists an intersection.
	 * 2. Store the intersection, and forward 1 step the one pointing to smaller end.
	 * 
	 * It would be better to use toArray(new T[0]) instead of toArray(new T[size]).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/interval-list-intersections/discuss/231299/JavaPython-3-2-pointers-time-O(A-%2B-B).
	 * https://leetcode.com/problems/interval-list-intersections/discuss/231122/Java-two-pointers-O(m-+-n)/230925
	 * https://leetcode.com/problems/interval-list-intersections/discuss/231122/Java-two-pointers-O(m-+-n)/236271
	 * leetcode.com/articles/interval-list-intersections/231071/Interval-List-Intersections/236260
	 * 
	 * Other code:
	 * https://leetcode.com/problems/interval-list-intersections/discuss/276324/Simple-JAVA-two-pointer
	 * https://leetcode.com/problems/interval-list-intersections/discuss/647506/JAVA-Detailed-Explanation-w-code
	 */
	public int[][] intervalIntersection(int[][] A, int[][] B) {
		List<int[]> ans = new ArrayList<>();
		int i = 0, j = 0;

		while (i < A.length && j < B.length) {
			// Let's check if A[i] intersects B[j].
			// lo - the startpoint of the intersection
			// hi - the endpoint of the intersection
			int lo = Math.max(A[i][0], B[j][0]);
			int hi = Math.min(A[i][1], B[j][1]);
			if (lo <= hi)
				ans.add(new int[] { lo, hi });

			// Remove the interval with the smallest endpoint
			if (A[i][1] < B[j][1])
				i++;
			else
				j++;
		}

		return ans.toArray(new int[0][0]);
	}
	
	/*
	 * https://leetcode.com/problems/interval-list-intersections/discuss/343499/Java-simple-O-(m-%2B-n)-solution-with-explanation
	 * 
	 * Move the intervals such that both of them intersect.
	 * 
	 * If A is behind move it forward, else if B is behind move it forward
	 * 
	 * There are three cases:
	 * case 1 : interval A is on the right side of B --> 
	 *          the start of A is > the end of B, which means there is no overlap
	 * case 2 : interval A is on the left side of B --> 
	 *          the end of A is < the start of B, which means there is no overlap
	 * case 3 : interval A and B have overlap, and we always pick the 
	 *          Max(start of A, start of B), Min(end of A, end of B) --> move the 
	 *          interval with smaller end to the next one if there is any.
	 *          Since, the other could overlap once again
	 * 
	 * Rf :
	 * https://leetcode.com/problems/interval-list-intersections/discuss/231634/Java-2-Pointer-with-comments-beats-100
	 */
	public int[][] intervalIntersection2(int[][] A, int[][] B) {
		int i = 0, j = 0;
		List<int[]> result = new ArrayList<>();
		
		while (i < A.length && j < B.length) {
			if (B[j][0] > A[i][1]) {
				i++; // Go to the next interval in A
			} 
			else if (A[i][0] > B[j][1]) {
				j++; // Go to the next interval in B
			} 
			else {
				int a = Math.max(A[i][0], B[j][0]);
				int b = Math.min(A[i][1], B[j][1]);
				result.add(new int[] { a, b });
				
				if (A[i][1] < B[j][1])
					i++;
				else
					j++;
			}
		}
		
		int k = 0;
		int[][] ans = new int[result.size()][];
		for (int[] t : result) {
			ans[k++] = t;
		}
		return ans;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/interval-list-intersections/discuss/254665/Java-O(M-%2B-N)-and-O(M*logN)-solutions-with-some-explanation
	 * 
	 * Binary search to find the interval
	 */
	public int[][] intervalIntersection_binarySearch_interval(int[][] A, int[][] B) {
		List<int[]> list = new ArrayList<>();
		int m = A.length;
		int n = B.length;
		if (m > n) {
			return intervalIntersection_binarySearch_interval(B, A);
		}
		
		int i = 0;
		int j = 0;
		while (i < A.length) {
			int[] a = A[i];
			int target = a[0];
			int position = binarySearch_binarySearch_interval(B, j, B.length, target);
			
			// no interval in B meets condition: interval's end >= target
			if (position == B.length) {
				break;
			}
			
			int[] b = B[position];
			if (b[0] <= a[1]) {
				list.add(new int[] { Math.max(a[0], b[0]), Math.min(a[1], b[1]) });
			}
			
			if (a[1] > b[1]) {
				j = position + 1;
			} 
			else {
				j = position;
				i += 1;
			}
		}
		
		int[][] res = new int[list.size()][];
		for (i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		return res;
	}
    
    // find min element (B.end) that is not less than target in B
	private int binarySearch_binarySearch_interval(int[][] B, int left, int right, int target) {
		// search space: O[left, right)
		while (left < right) {
			int mid = left + (right - left) / 2;
			if (B[mid][1] >= target) {
				right = mid;
			} 
			else {
				left = mid + 1;
			}
		}
		return left;
	}
	
	// by myself
	public int[][] intervalIntersection_self(int[][] A, int[][] B) {
        if (A == null || B == null || A.length == 0 || B.length == 0)
            return new int[0][0];
        
        List<int[]> ans = new ArrayList<>();
        
        int i = 0;
        int j = 0;
        int a = A[0][0];
        int b = B[0][0];
        
        while (i < A.length && j < B.length) {
            //System.out.println("a = " + a + " ; b = " + b);
            int[] next = {-1, -1};
            if (a < b) {
                next[0] = b;
                //System.out.println("if ; next[0] = " + next[0]);
            }
            else {
                next[0] = a;
                //System.out.println("else ; next[0] = " + next[0]);
            }
            
            a = A[i][1];
            b = B[j][1];
            
            if (a < b) {
                next[1] = a;
                i++;
                //System.out.println("if ; next[1] = " + next[1]);
                
                if (i < A.length) {
                    a = A[i][0];
                }
                b = Math.max(next[1], B[j][0]);
            }
            else {
                next[1] = b;
                j++;
                //System.out.println("else ; next[1] = " + next[1]);
                
                if (j < B.length) {
                    b = B[j][0];
                }
                a = Math.max(next[1], A[i][0]);
            }
            
            if (next[0] <= next[1]) {
                ans.add(next);
                //System.out.println("Add ");
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/interval-list-intersections/discuss/647482/Python-Two-Pointer-Approach-%2B-Thinking-Process-Diagrams
     * https://leetcode.com/problems/interval-list-intersections/discuss/415099/Short-Python-O(m%2Bn)-Solution
     * https://leetcode.com/problems/interval-list-intersections/discuss/231532/Python3-Really-easy-concept%3A-Overlapping
     * https://leetcode.com/problems/interval-list-intersections/discuss/647147/Python-Short-and-simple-solution-without-pointers
     * https://leetcode.com/problems/interval-list-intersections/discuss/232866/simply-python-solution-with
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/interval-list-intersections/discuss/646988/C%2B%2B-or-Easy-or-6-lines-or-Two-pointer-or-100
     * https://leetcode.com/problems/interval-list-intersections/discuss/231108/C%2B%2B-O(n)-%22merge-sort%22
     * https://leetcode.com/problems/interval-list-intersections/discuss/647342/C%2B%2B-Two-pointer-approach
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/interval-list-intersections/discuss/472262/JavaScript-Solution
	 * https://leetcode.com/problems/interval-list-intersections/discuss/315908/clean-javascript-solution
	 */

}
