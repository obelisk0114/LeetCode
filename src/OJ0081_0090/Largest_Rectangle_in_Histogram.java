package OJ0081_0090;

import java.util.Stack;

public class Largest_Rectangle_in_Histogram {
	/*
	 * https://leetcode.com/problems/largest-rectangle-in-histogram/discuss/28953/java-on-leftright-arrays-solution-4ms-beats-96
	 * 
	 * 1. Scan from left to right to compute left[], which represents the left most 
	 *    boundary of current height.
	 * 2. Scan from right to left to compute right[], which represents the right most 
	 *    boundary of current height.
	 * 3. Scan from left to right again to compute rectangle area based on the height 
	 *    of that each position.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/39151/5ms-o-n-java-solution-explained-beats-96
	 */
	public int largestRectangleArea(int[] heights) {
		// validate input
		if (heights == null || heights.length == 0) {
			return 0;
		}

		// init
		int n = heights.length;
		int[] left = new int[n];
		int[] right = new int[n];
		int result = 0;

		// build left
		left[0] = 0;
		for (int i = 1; i < n; i++) {
			int currentLeft = i - 1;
			while (currentLeft >= 0 && heights[currentLeft] >= heights[i]) {
				currentLeft = left[currentLeft] - 1;
			}

			left[i] = currentLeft + 1;
		}

		// build right
		right[n - 1] = n - 1;
		for (int i = n - 2; i >= 0; i--) {
			int currentRight = i + 1;
			while (currentRight < n && heights[i] <= heights[currentRight]) {
				currentRight = right[currentRight] + 1;
			}

			right[i] = currentRight - 1;
		}

		// compare height
		for (int i = 0; i < n; i++) {
			result = Math.max(result, (right[i] - left[i] + 1) * heights[i]);
		}

		// return
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/7599/o-n-stack-based-java-solution
	 * 
	 * Use a stack to save the index of each array entry in a ascending order; 
	 * once the current entry is smaller than the one with the index s.top(), 
	 * that means the rectangle with the height height[s.top()] ends 
	 * at the current position, so calculate its area and update the maximum.
	 * 
	 * Rf : 
	 * http://www.geeksforgeeks.org/largest-rectangle-under-histogram/
	 * https://discuss.leetcode.com/topic/14406/my-concise-code-20ms-stack-based-o-n-one-trick-used
	 * https://discuss.leetcode.com/topic/2424/my-modified-answer-from-geeksforgeeks-in-java
	 */
	public int largestRectangleArea_stack(int[] height) {
		int len = height.length;
		Stack<Integer> s = new Stack<Integer>();
		int maxArea = 0;
		for (int i = 0; i <= len; i++) {       // Use i = length to pop out the remain elements
			int h = (i == len ? 0 : height[i]);
			if (s.isEmpty() || h >= height[s.peek()]) {
				s.push(i);
			} else {
				int tp = s.pop();
				// Use i - 1 to exclude i, and use i-- to turn back
				maxArea = Math.max(maxArea, height[tp] * (s.isEmpty() ? i : i - 1 - s.peek()));
				i--;
			}
		}
		return maxArea;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/39836/share-my-2ms-java-solution-beats-100-java-submissions
	 * 
	 * Find the minimum height column with index min. 
	 * The max area should exist among three possible answers:

         1. sub-problem in index [0 ~ min]
         2. sub-problem in index [min + 1 ~ len - 1]
         3. height[min] * (len - 0)
     *
     * Rf : http://www.geeksforgeeks.org/largest-rectangular-area-in-a-histogram-set-1/
	 */
	public int largestRectangleArea_Divided_Conquer(int[] heights) {
		if (heights == null || heights.length == 0)
			return 0;
		return getMax(heights, 0, heights.length);
	}
	int getMax(int[] heights, int s, int e) {
		if (s + 1 == e)
			return heights[s];
		int min = s;
		boolean sorted = true;
		for (int i = s + 1; i < e; i++) {
			if (heights[i] < heights[i - 1])
				sorted = false;
			if (heights[min] > heights[i])
				min = i;
		}
		if (sorted) {
			int max = heights[s] * (e - s);
			for (int i = s + 1; i < e; i++) {
				max = Math.max(max, heights[i] * (e - i));
			}
			return max;
		}
		int left = (min > s) ? getMax(heights, s, min) : 0;
		int right = (min < e - 1) ? getMax(heights, min + 1, e) : 0;
		return Math.max(Math.max(left, right), (e - s) * heights[min]);
	}

}
