package OJ0371_0380;

import java.util.Collections;
import java.util.PriorityQueue;

public class Kth_Smallest_Element_in_a_Sorted_Matrix {
	/*
	 * The following function and class are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code
	 * 
	 * This problem can be easily converted to 'Kth Smallest Number in M Sorted Lists'. 
	 * As each row (or column) of the given matrix can be seen as a sorted list, we 
	 * essentially need to find the Kth smallest number in 'N' sorted lists.
	 * 
	 * 1. Build a minHeap of elements from the first row.
	 * 2. Do the following operations k-1 times :
	 * Every time when you poll out the root(Top Element in Heap), you need to know 
	 * the row number and column number of that element(so we can create a tuple class 
	 * here), replace that root with the next element from the same column.
	 * 
	 * You can also build a min Heap from the first column, and do the similar 
	 * operations as above.(Replace the root with the next element from the same row)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/301357/Java-0ms-(added-Python-and-C%2B%2B)%3A-Easy-to-understand-solutions-using-Heap-and-Binary-Search
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/246413
	 */
	public int kthSmallest_PriorityQueue(int[][] matrix, int k) {
		int n = matrix.length;
		PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>();
		
		// put the 1st element of each row in the min heap
	    // we don't need to push more than 'k' elements in the heap
		for (int j = 0; j < n && j < k; j++)
			pq.offer(new Tuple(0, j, matrix[0][j]));
		
		for (int i = 0; i < k - 1; i++) {
			Tuple t = pq.poll();
			if (t.x == n - 1)
				continue;
			
			pq.offer(new Tuple(t.x + 1, t.y, matrix[t.x + 1][t.y]));
		}
		return pq.poll().val;
	}

	class Tuple implements Comparable<Tuple> {
		int x, y, val;

		public Tuple(int x, int y, int val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}

		@Override
		public int compareTo(Tuple that) {
			return this.val - that.val;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/412502/Simple-binary-search-solution-for-slow-learners-like-myself
	 * 
	 * The kth smallest element is between matrix[0][0] and matrix[m - 1][n - 1], 
	 * where m is number of rows of the matrix, n is number of columns of the matrix. 
	 * Let low = matrix[0][0], and high = matrix[m - 1][n - 1], using binary search to 
	 * calculate mid value, and find the count of elements which are less than and 
	 * equal to mid.
	 * 
	 * The mid is just a value calculated by using low and high, not a true element in 
	 * the matrix, so when count < k, it means that the mid is small, let 
	 * low = mid + 1 to find the true element in matrix one by one. And when count > k, 
	 * it means that the mid is big enough, let high = mid to ensure the element we 
	 * need is between low and high(mid). And when count = k, it means that there are 
	 * k elements less than or equal to mid in the matrix, but we don't know whether 
	 * the element we need is equal to mid. So let high = mid, it can ensure that the 
	 * element we need is between low and high(mid).
	 * 
	 * Notice that using (low < high) in while loop rather than using (low <= high) to 
	 * avoid stay in the loop.
	 * 
	 * Every time the algorithm narrows the range, the target value must in the new 
	 * range.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/394294/Using-Binary-Search-in-Java-and-analysis
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/394294/Using-Binary-Search-in-Java-and-analysis/362974
	 */
	public int kthSmallest_binarySearch2(int[][] matrix, int k) {
		if (matrix == null || matrix.length == 0 
				|| matrix[0] == null || matrix[0].length == 0)
			return -1;

		int m = matrix.length, n = matrix[0].length;
		int left = matrix[0][0], right = matrix[m - 1][n - 1];

		while (left < right) {
			int mid = left + (right - left) / 2;
			int count = getCountLessThanOrEqualToTarget(matrix, mid);
			
			if (count < k)
				left = mid + 1; // we have not met our quota k so expand our search space
			else
				right = mid;
		}

		return left;
	}

	private int getCountLessThanOrEqualToTarget(int[][] matrix, int target) {
		int m = matrix.length, n = matrix[0].length, count = 0, j = n - 1;
		for (int i = 0; i < m; i++) {
			while (j >= 0 && matrix[i][j] > target)
				j--;
			
			count += j + 1;
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85177/Java-1ms-nlog(max-min)-solution
	 * 
	 * Main loop is binary search of [min, max]. min: top left; max: bottom right cell
	 * 
	 * Start at the bottom left, for the bigger, we can only move right, for the 
	 * smaller, we can only move up, and accumulate the number of elements upon the 
	 * cell (inclusive) within the same column.
	 * 
	 * complexity: O(nlogm) while m = max - min.
	 * 
	 * mid = a_mid, which is not the kth smallest element, a_k, in the array.
	 * lo <= a_k < a_mid <= hi, and the loop ends at lo = hi, which means a_mid has to 
	 * equal to a_k.
	 * 
	 * The element found by this algorithm has to be in the input matrix because the 
	 * range converges to the minimum value that satisfies (or most closely follows) 
	 * the condition count == k. The first value to satisfy count == k must be found 
	 * in the range if exists.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/167784/Java-Binary-Search-with-Explanations
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85177/Java-1ms-nlog(max-min)-solution/89957
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85177/Java-1ms-nlog(max-min)-solution/89954
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85177/Java-1ms-nlog(max-min)-solution/253998
	 */
	public int kthSmallest_binarySearch(int[][] matrix, int k) {
		int n = matrix.length;
		int lo = matrix[0][0], hi = matrix[n - 1][n - 1];
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int count = getLessEqual(matrix, mid);
			
			if (count < k)
				lo = mid + 1;
			else
				hi = mid - 1;
		}
		return lo;
	}

	private int getLessEqual(int[][] matrix, int val) {
		int res = 0;
		int n = matrix.length, i = n - 1, j = 0;
		while (i >= 0 && j < n) {
			if (matrix[i][j] > val)
				i--;
			else {
				res += i + 1;
				j++;
			}
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/301357/Java-0ms-(added-Python-and-C%2B%2B)%3A-Easy-to-understand-solutions-using-Heap-and-Binary-Search
	 * 
	 * An alternate could be to apply the Binary Search on the "number range" instead 
	 * of the "index range". As we know that the smallest number of our matrix is at 
	 * the top left corner and the biggest number is at the bottom right corner. 
	 * These two number can represent the "range" i.e., the start and the end for the 
	 * Binary Search.
	 * 
	 * 1. Start the Binary Search with start = matrix[0][0] and end = matrix[n-1][n-1]
	 * 2. Find middle of the start and the end. This middle number is NOT necessarily 
	 *    an element in the matrix.
	 * 3. Count all the numbers smaller than or equal to middle in the matrix. As the 
	 *    matrix is sorted, we can do this in O(N).
	 * 4. While counting, we can keep track of the "smallest number greater than the 
	 *    middle" (let's call it n2) and at the same time the "biggest number less 
	 *    than or equal to the middle" (let's call it n1). These two numbers will be 
	 *    used to adjust the "number range" for the Binary Search in the next 
	 *    iteration.
	 * 5. If the count is equal to 'K', n1 will be our required number as it is the 
	 *    "biggest number less than or equal to the middle", and is definitely present 
	 *    in the matrix.
	 * 6. If the count is less than 'K', we can update start = n2 to search in the 
	 *    higher part of the matrix and if the count is greater than 'K', we can 
	 *    update end = n1 to search in the lower part of the matrix in the next 
	 *    iteration.
	 *    
	 * Rf :
	 * https://www.educative.io/courses/grokking-the-coding-interview/x1NJVYKNvqz
	 */
	public int kthSmallest_binarySearch_linear2(int[][] matrix, int k) {
		int n = matrix.length;
		int start = matrix[0][0], end = matrix[n - 1][n - 1];
		while (start < end) {
			int mid = start + (end - start) / 2;
			
			// first number is the smallest and the second number is the largest
			int[] smallLargePair = { matrix[0][0], matrix[n - 1][n - 1] };

			int count = countLessEqual(matrix, mid, smallLargePair);

			if (count == k)
				return smallLargePair[0];

			if (count < k)
				start = smallLargePair[1]; // search higher
			else
				end = smallLargePair[0]; // search lower
		}

		return start;
	}

	private int countLessEqual(int[][] matrix, int mid, int[] smallLargePair) {
		int count = 0;
		int n = matrix.length, row = n - 1, col = 0;
		while (row >= 0 && col < n) {
			if (matrix[row][col] > mid) {
				// matrix[row][col] > mid, let's keep track of the
				// smallest number greater than the mid
				smallLargePair[1] = Math.min(smallLargePair[1], matrix[row][col]);
				row--;
			} 
			else {
				// matrix[row][col] <= mid, let's keep track of the
				// biggest number less than or equal to the mid
				smallLargePair[0] = Math.max(smallLargePair[0], matrix[row][col]);
				count += row + 1;
				col++;
			}
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/150126
	 * 
	 * Rf :
	 * https://leetcode.com/problems/find-k-th-smallest-pair-distance/discuss/109082/Approach-the-problem-using-the-%22trial-and-error%22-algorithm
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85170/O(n)-from-paper.-Yes-O(rows)./89842
	 */
	public int kthSmallest_binarySearch4(int[][] matrix, int k) {
        // num of rows and cols in matrix
        int rows = matrix.length, cols = matrix[0].length;
        
        // get the lowest and highest possible num, will shrink search space
        // according to the two nums [lo, hi] is our initial search range
        int lo = matrix[0][0], hi = matrix[rows - 1][cols - 1] ;
        
        while(lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int count = 0,  maxNum = lo;
            
            // for each row, we are going to find # of nums <= mid in that row
            for (int r = 0, c = cols - 1; r < rows; r++) {
            	// this row's c has to be smaller than the c found in last row 
            	// due to the sorted property of the matrix 
                while (c >= 0 && matrix[r][c] > mid)
                	c--;
                
                if (c >= 0) {
                	// count of nums <= mid in matrix
                    count += (c + 1);
                    
                    // mid might be value not in matrix, we need to record
                    // the actually max num;
                    maxNum = Math.max(maxNum, matrix[r][c]);
                }
            }
            
            // adjust search range
			if (count == k)
				return maxNum;
			else if (count < k)
				lo = mid + 1;
			else
				hi = mid - 1;
        }
        
        // 1) Q: Why we return lo at the end?  ex: [[1,2],[1,3]] 1
        //    A: Here lo = hi + 1, for hi, we found < k elems, for lo, we found >= k 
        //       elem, lo must have duplicates in matrix, return lo
        // 2) Q: Why lo exist in the matrix
        //    A: for lo which is only 1 more than hi, we could find some extra nums 
        //       in matrix so that there are >= k elems, so lo it self must exist in 
        //       the matrix to meet the requirement
        return lo;
    }
	
	/*
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code
	 * 
	 * There are two kind of "Search Space" -- index and 
	 * range(the range from the smallest number to the biggest number). 
	 * Most usually, when the array is sorted in one direction, we can use index as 
	 * "search space", when the array is unsorted and we are going to find a specific 
	 * number, we can use "range".
	 * 
	 * The reason why we did not use index as "search space" for this problem is the 
	 * matrix is sorted in two directions, we can not find a linear way to map the 
	 * number and its index.
	 * 
	 * The count is the number of elements less or equal to mid
	 * 
	 * We guarantee the Kth value is in [lo, hi] in every step. 
	 * Then finally io == hi, so the Kth value must be the mid==lo==hi
	 * 
	 * The lo we returned is guaranteed to be an element in the matrix is because:
	 * Let us assume element m is the kth smallest number in the matrix, and x is the 
	 * number of element m in the matrix. When we are about to reach convergence, if 
	 * mid=m-1, its count value (the number of elements which are <= mid) would be 
	 * k-x, so we would set lo as (m-1)+1=m, in this case the hi will finally reach 
	 * lo; and if mid=m+1, its count value would be larger than k, so we would set hi 
	 * as m+1, in this case the lo will finally reach m.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/181852
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/198348
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/122873
	 */
	public int kthSmallest_binarySearch3(int[][] matrix, int k) {
		int lo = matrix[0][0];
		int hi = matrix[matrix.length - 1][matrix[0].length - 1] + 1; // [lo, hi)
		while (lo < hi) {
			int mid = lo + (hi - lo) / 2;
			int count = 0, j = matrix[0].length - 1;
			
			for (int i = 0; i < matrix.length; i++) {
				while (j >= 0 && matrix[i][j] > mid)
					j--;
				
				count += (j + 1);
			}
			
			if (count < k)
				lo = mid + 1;
			else
				hi = mid;
		}
		return lo;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/394294/Using-Binary-Search-in-Java-and-analysis/420850
	 * 
	 * Use binary search to find the kth smallest value
	 * Use binary search to find the rightmost target position can be inserted in 
	 * each row, and use it to count
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173/Share-my-thoughts-and-Clean-Java-Code/172019
	 */
	public int kthSmallest_binarySearch_rightmost_insert(int[][] matrix, int k) {
		int n = matrix.length;
		int min = matrix[0][0], max = matrix[n - 1][n - 1], avg;
		while (min <= max) {
			avg = min + (max - min) / 2;
			int count = 0;
			for (int i = 0; i < n; i++) {
				count += bsCountSeq(matrix[i], avg);
			}

			if (count >= k)
				max = avg - 1;
			else
				min = avg + 1;
		}
		
		// max would be pushed less than target k, min is the k count element
		return min;
	}

	// return rightmost target position can be inserted
	private int bsCountSeq(int[] arr, int target) {
		int n = arr.length;
		if (arr[n - 1] <= target)
			return n;
		if (arr[0] > target)
			return 0;
		
		int l = 0, r = n - 1, mid;
		while (l <= r) {
			mid = l + (r - l) / 2;
			
			if (arr[mid] <= target)
				l = mid + 1;
			else
				r = mid - 1;
		}
		return l;   // first one right to the target
	}
	
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85301/Java-PriorityQueue-Solution/90157
	 * 
	 * Create a PriorityQueue which will sort the inserted elements based on lowest 
	 * matrix[][] value. At first, we insert the smallest value which is at 
	 * matrix[0][0]. To iteratively find the next smallest element, we need to take out 
	 * the smallest value from the PriorityQueue, and add the value toward its 
	 * bottom / right (if we are at the first row) into the PriorityQueue 
	 * (as long as they are within range). The next smallest value must be either 
	 * already inside the PriorityQueue, or be to the right/bottom of the current 
	 * smallest value. Either way, the PriorityQueue will make the correct ordering so 
	 * that the next smallest value is ready to be removed. We do this loop k-1 times, 
	 * and remove the kth smallest from the PriorityQueue in the end.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85301/Java-PriorityQueue-Solution
	 * http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85178/Java-heap-klog(k)
	 */
	private class Value implements Comparable<Value> {
		int val;
		int row;
		int col;

		public Value(int val, int row, int col) {
			this.val = val;
			this.row = row;
			this.col = col;
		}

		public int compareTo(Value other) {
			return this.val - other.val;
		}
	}

	public int kthSmallest_PriorityQueue2(int[][] matrix, int k) {
		PriorityQueue<Value> minHeap = new PriorityQueue<Value>();
		minHeap.add(new Value(matrix[0][0], 0, 0));
		for (int x = 1; x < k; x++) {
			Value val = minHeap.poll();
			if (val.row + 1 < matrix.length) {
				minHeap.add(
					new Value(matrix[val.row + 1][val.col], val.row + 1, val.col));
			}
			
			// avoid duplicates
			if (val.row == 0 && val.col + 1 < matrix[0].length) {
				minHeap.add(new Value(matrix[0][val.col + 1], 0, val.col + 1));
			}
		}
		return minHeap.peek().val;
	}
	
	// by myself
	public int kthSmallest_self(int[][] matrix, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                pq.offer(matrix[i][j]);
                if (pq.size() > k) {
                    pq.poll();
                }
            }
        }
        return pq.peek();
    }
	
	/*
	 * O(n) from paper
	 * 
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85170/O(n)-from-paper.-Yes-O(rows).
	 * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85201/C%2B%2B-O(n)-time-O(n)-space-solution-with-detail-intuitive-explanation
	 */
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85193/Binary-Search-Heap-and-Sorting-comparison-with-concise-code-and-1-liners-Python-72-ms
     * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85296/Shortest-possible-solution-in-Python-(seriously!)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85182/My-solution-using-Binary-Search-in-C%2B%2B
     * https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85222/C%2B%2B-priority-queue-solution-O(klogn)
     */

}
