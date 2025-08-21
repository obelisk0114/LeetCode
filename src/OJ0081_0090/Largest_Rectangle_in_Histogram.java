package OJ0081_0090;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Largest_Rectangle_in_Histogram {
	/*
	 * 窮舉每個 heights[i] 所能做出的最大矩形
	 * left[i] = heights[i] 的左邊界
	 * right[i] = heights[i] 的右邊界
	 * 
	 * heights[i] 所能做出的最大矩形, 長度 = 左 - 右 + 1
	 * 
	 * 找出邊界, 即為找出第一個高度比當前高度小的, 因此可套用 Monotonic Stack
	 * 因為更高的, 都被 pop 了, 所以 stack.top 是最大的
	 * 
	 * Rf : https://leetcode.cn/problems/largest-rectangle-in-histogram/solutions/2695467/dan-diao-zhan-fu-ti-dan-pythonjavacgojsr-89s7/
	 */
	public int largestRectangleArea_self_mod(int[] heights) {
        int n = heights.length;
        Deque<Integer> st = new ArrayDeque<>();

		// heights[i] 的左邊界
        int[] left = new int[n];
        for (int i = 0; i < n; i++) {
            int h = heights[i];
            while (!st.isEmpty() && heights[st.peek()] >= h) {
                st.pop();
            }

            // st.peek() 是第一個高度比他小的, + 1 變成高度延伸的左邊界
            left[i] = st.isEmpty() ? 0 : st.peek() + 1;
            st.push(i);
        }

        st.clear();

		// heights[i] 的右邊界
        int[] right = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int h = heights[i];
            while (!st.isEmpty() && heights[st.peek()] >= h) {
                st.pop();
            }

            // st.peek() 是第一個高度比他小的, - 1 變成高度延伸的右邊界
            right[i] = st.isEmpty() ? n - 1 : st.peek() - 1;
            st.push(i);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, heights[i] * (right[i] - left[i] + 1));
        }
        return ans;
    }

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
	 * 只有當前高度 <= 之前高度，矩形的高度才是當前高度
	 * 因此用 stack 儲存遞增高度的 index (height[i] <= height[j], i < j)
	 * 
	 * 若當前高度 (height[i]) < height[stack.top_old]
	 * 表示之後矩形若要伸展到 index < i 的話，高度 <= height[i]
	 * 因此可以先求在 index < i 且 height[index] > height[i] 作為高度的矩形的最大面積
	 * 
	 * 開始 pop，來求以 height[stack.top_old] 為高度的矩形的寬度
	 * 先 pop，並記錄此 index 為 tp，相對應的高度為 height[tp] (height[stack.top_old])
	 * 由於 stack 遞增，因此 height[stack.top_new] <= height[tp]
	 * 
	 * 所以以 height[tp] 為高度的矩形，寬度為 (i - 1) - stack.top_new
	 * 就是 i - 1 到 stack.top_new + 1，因為 height[i] < height[stack.top_old] = height[tp] 
	 * i - 1 到 stack.top_new + 1 之間不會有某個高度 j，height[j] < height[tp]
	 * 因為這個 j 會將 stack top 不斷 pop 直到可以被 push 進 stack 為止
	 * 
	 * 之後將 i-- 來固定 i，持續 pop 並計算以 height[stack.top_newI] 為高度的最大矩形
	 * 直到 height[i] >= height[stack.top]，將 i 放入 stack
	 * 因此若 height[stack.top_new] = height[tp] 時，
	 * 以 height[tp] 為高度的最大矩形會在以  height[stack.top_new] 為高度時，被更新
	 * 
	 * 若只儲存嚴格遞增高度，之後計算矩形寬度會多算
	 * test case: [0,1,2,3,3,1,2] 因為第二個 1 沒放入，導致第二個 2 多算寬度
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
		
		// Use i = length to pop out the remain elements
		for (int i = 0; i <= len; i++) {
			int h = (i == len ? 0 : height[i]);
			if (s.isEmpty() || h >= height[s.peek()]) {
				s.push(i);
			} 
			else {
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
