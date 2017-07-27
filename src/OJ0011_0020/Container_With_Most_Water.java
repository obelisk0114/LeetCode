package OJ0011_0020;

/*
 * http://www.cs.princeton.edu/~wayne/kleinberg-tardos/
 * https://discuss.leetcode.com/topic/14940/simple-and-clear-proof-explanation
 */

public class Container_With_Most_Water {
	// https://discuss.leetcode.com/topic/29962/for-someone-who-is-not-so-clear-on-this-question
	
	/*
	 * https://discuss.leetcode.com/topic/25004/easy-concise-java-o-n-solution-with-proof-and-explanation
	 * 
	 * Lets assume a10 and a20 are the max area situation.
	 * when left pointer is at a10 and right pointer is at a21, 
	 * the next move must be right pointer to a20.
	 * 
	 * if a21 > a10, then area of a10 and a20 must be less than area of a10 and a21.
	 * Because the area of a10 and a21 is at least height[a10] * (21-10) 
	 * while the area of a10 and a20 is at most height[a10] * (20-10).
	 * 
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/503/anyone-who-has-a-o-n-algorithm/3
	 * https://discuss.leetcode.com/topic/503/anyone-who-has-a-o-n-algorithm/2
	 */
	public int maxArea(int[] height) {
		int left = 0, right = height.length - 1;
		int maxArea = 0;

		while (left < right) {
			maxArea = Math.max(maxArea, 
					Math.min(height[left], height[right]) * (right - left));
			if (height[left] < height[right])
				left++;
			else
				right--;
		}

		return maxArea;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/50763/from-dp-to-greedy-o-n-with-explanation-easy-way-to-see-this-problem
	 * 
	 * S(i...j) : the max volume from index i to j
	 * v(i, j) : the volume of the container with boundary i and j
	 * 
	 *  S(0..n) = max{v(0, n), S(1...n), S(0...n-1)}
	 * we assume height(0) > height(n), then we can simplify S(0..n) as 
	 * S(0...n) = max{v(0, n), S(0...n-1)}
	 * 
	   1. if v(0, n) is the largest volume, since we have figured out the result, 
	      the simplifier is all right.
       2. if not, we could find out that n can't be the boundary of the container. 
          If n is the right boundary, because of height(0) > height(n), 
          the max volume must be height(n) * (n - 0) = v(0, n), 
          which arrives at a contradiction. 
          So S(1...n) can be simplified as S(1...n-1) which is included in S(0...n-1).
	 * 
	 */
	public int maxArea2(int[] height) {
		int lo = 0;
		int hi = height.length - 1;
		int max = 0;
		while (lo < hi) {
			int min = Math.min(height[lo], height[hi]);
			max = Math.max(max, min * (hi - lo));
			while (lo <= hi && height[lo] <= min)   // while (lo < hi && height[lo] <= min)
				lo++;
			while (lo <= hi && height[hi] <= min)   // while (lo < hi && height[hi] <= min)
				hi--;
		}
		return max;
	}
	
	// https://discuss.leetcode.com/topic/15208/java-two-pointer

}
