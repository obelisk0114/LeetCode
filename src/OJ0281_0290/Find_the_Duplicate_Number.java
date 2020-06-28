package OJ0281_0290;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Function.html#2
 */

public class Find_the_Duplicate_Number {
	/*
	 * https://leetcode.com/articles/find-the-duplicate-number/
	 * 
	 * Let's use the function f(x) = nums[x] to construct the sequence: 
	 * x, nums[x], nums[nums[x]], nums[nums[nums[x]]], ...
	 * 
	 * Each new element in the sequence is an element in nums at the index of the 
	 * previous element.
	 * 
	 * If one starts from x = nums[0], such a sequence will produce a linked list with 
	 * a cycle. We can use Floyd's algorithm.
	 */
	public int findDuplicate(int[] nums) {
		// Find the intersection point of the two runners.
		int tortoise = nums[0];
		int hare = nums[0];
		do {
			tortoise = nums[tortoise];
			hare = nums[nums[hare]];
		} while (tortoise != hare);

		// Find the "entrance" to the cycle.
		tortoise = nums[0];
		while (tortoise != hare) {
			tortoise = nums[tortoise];
			hare = nums[hare];
		}

		return hare;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/25685/java-o-n-time-and-o-1-space-solution-similar-to-find-loop-in-linkedlist
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/26481/tortoise-haire-cycle-detection-algorithm
	 * https://discuss.leetcode.com/topic/25601/a-java-solution-o-n-time-and-o-1-space
	 * 
	 * Leetcode 142 Linked List Cycle II
	 */
	public int findDuplicate2(int[] nums) {
		int n = nums.length;
		int slow = n;
		int fast = n;
		do {
			slow = nums[slow - 1];
			fast = nums[nums[fast - 1] - 1];
		} while (slow != fast);
		slow = n;
		while (slow != fast) {
			slow = nums[slow - 1];
			fast = nums[fast - 1];
		}
		return slow;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/27420/java-o-1-space-using-binary-search
	 * 
	 * Rf : https://discuss.leetcode.com/topic/25580/two-solutions-with-explanation-o-nlog-n-and-o-n-time-o-1-space-without-changing-the-input-array
	 */
	public int findDuplicate_binarySearch(int[] nums) {
		int low = 1, high = nums.length - 1;
		while (low <= high) {
			int mid = (int) (low + (high - low) * 0.5);
			int cnt = 0;
			for (int a : nums) {
				if (a <= mid)
					++cnt;
			}
			if (cnt <= mid)
				low = mid + 1;
			else
				high = mid - 1;
		}
		return low;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/38493/o-32-n-solution-using-bit-manipulation-in-10-lines
	 * 
	 * We can count the sum of each 32 bits separately for the given array 
	 * (stored in "b" variable) and for the array [1, 2, 3, ..., n] 
	 * (stored in "a" variable). 
	 * If "b" is greater than "a", 
	 * it means that duplicated number has 1 at the current bit position 
	 * (otherwise, "b" couldn't be greater than "a").
	 */
	public int findDuplicate_bit(int[] nums) {
		int n = nums.length - 1, res = 0;
		for (int p = 0; p < 32; ++p) {
			int bit = (1 << p), a = 0, b = 0;
			for (int i = 0; i <= n; ++i) {
				if ( (i & bit) > 0)
					++a;
				if ((nums[i] & bit) > 0)
					++b;
			}
			if (b > a)
				res += bit;
		}
		return res;
	}

}
