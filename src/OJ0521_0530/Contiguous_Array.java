package OJ0521_0530;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Contiguous_Array {
	/*
	 * Approach #3 Using HashMap
	 * https://leetcode.com/articles/contiguous-array/
	 * 
	 * If we encounter the same count twice while traversing the array, it means that 
	 * the number of 0s and 1s are equal between the indices corresponding to the 
	 * equal count values.
	 * 
	 * put(0, -1), before we loop this array, the sum is 0 in initial, and because we 
	 * haven't starting loop, so the index = -1.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/contiguous-array/discuss/99646/Easy-Java-O(n)-Solution-PreSum-+-HashMap/362913
	 * 
	 * Other code:
	 * https://leetcode.com/problems/contiguous-array/discuss/99658/Python-and-Java-with-little-tricks-(incl.-a-oneliner-%3A-)
	 * https://leetcode.com/problems/contiguous-array/discuss/99646/Easy-Java-O(n)-Solution-PreSum-%2B-HashMap
	 */
	public int findMaxLength_Map(int[] nums) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, -1);
		int maxlen = 0, count = 0;
		for (int i = 0; i < nums.length; i++) {
			count = count + (nums[i] == 1 ? 1 : -1);
			if (map.containsKey(count)) {
				maxlen = Math.max(maxlen, i - map.get(count));
			} 
			else {
				map.put(count, i);
			}
		}
		return maxlen;
	}
	
	/*
	 * https://leetcode.com/problems/contiguous-array/discuss/99652/One-passuse-a-HashMap-to-record-0-1-count-difference
	 * 
	 * 遇到 1 加 1，遇到 0 減 1，如果兩個 sum 值一樣，說明這兩個 sum 之間剛好有相同的 0 和 1。 然後算距離就行了。
	 * 唯一注意的為了算最長距離，我們不會一直更新 map，只有這個 sum 沒出現的時候才添加。
	 * 
	 * If we ever see the current sum in the HashMap, it means that there were equal 
	 * number of zeros and ones in between the current index i and the index in the 
	 * HashMap, because only the sum of the same number of 1's and 0's would make 
	 * the sum equal again.
	 * 
	 * If the whole array is equal 1 and 0 like [0,1] or [0,0,1,1]
	 * we want this to match with index 0, the start.
	 * 
	 * so we initialize index 0 : map.put(0, -1);
	 * because : i-map.get(sum)
	 * we want to get i - (-1) = i + 1 = full length of the array, which is the 
	 * correct answer.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/contiguous-array/discuss/99652/One-passuse-a-HashMap-to-record-0-1-count-difference/103787
	 * https://leetcode.com/problems/contiguous-array/discuss/577990/Java-6-ms-faster-than-99-less-space-than-100-plus-3-line-solution-for-fun/505423
	 * https://leetcode.com/problems/contiguous-array/discuss/577990/Java-6-ms-faster-than-99-less-space-than-100-plus-3-line-solution-for-fun
	 * 
	 * Other code:
	 * https://leetcode.com/problems/contiguous-array/discuss/99652/One-passuse-a-HashMap-to-record-0-1-count-difference/103786
	 * https://leetcode.com/problems/contiguous-array/discuss/99688/Share-my-DPandMap-solution-one-pass/103811
	 */
	public int findMaxLength_Map2(int[] nums) {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(0, -1);

		int zero = 0;
		int one = 0;
		int len = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == 0) {
				zero++;
			} 
			else {
				one++;
			}

			if (map.containsKey(zero - one)) {
				len = Math.max(len, i - map.get(zero - one));
			} 
			else {
				map.put(zero - one, i);
			}
		}

		return len;
	}
	
	/*
	 * Approach #2 Using Extra Array
	 * https://leetcode.com/articles/contiguous-array/
	 * 
	 * count, which is used to store the relative number of 1s and 0s encountered
	 * so far while traversing the array. The count variable is incremented by one for 
	 * every 1 encountered and is decremented by one for every 0.
	 * 
	 * If at any moment, the count becomes 0, it implies that we've encountered equal 
	 * number of 0s and 1s from the beginning till the current index of the array(i).
	 * 
	 * If we encounter the same count twice while traversing the array, it means that 
	 * the number of 0s and 1s are equal between the indices corresponding to the 
	 * equal count values. 
	 * 
	 * If we keep a track of the indices corresponding to the same count values that 
	 * lie farthest apart, we can determine the size of the largest subarray with 
	 * equal no. of 0s and 1s.
	 * 
	 * Now, the count values can range between -n to +n, with the extreme points 
	 * corresponding to the complete array being filled with all 0's and all 1's. 
	 * Thus, we make use of an array of size 2n+1 to keep track of the various 
	 * count's encountered so far.
	 * 
	 * We make an entry containing the current element's index (i) in the arr for a 
	 * new count encountered every time. Whenever, we come across the same count value 
	 * later while traversing the array, we determine the length of the subarray lying 
	 * between the indices corresponding to the same count values.
	 */
	public int findMaxLength_array(int[] nums) {
		int[] arr = new int[2 * nums.length + 1];
		Arrays.fill(arr, -2);
		arr[nums.length] = -1;
		int maxlen = 0, count = 0;
		for (int i = 0; i < nums.length; i++) {
			count = count + (nums[i] == 0 ? -1 : 1);
			if (arr[count + nums.length] >= -1) {
				maxlen = Math.max(maxlen, i - arr[count + nums.length]);
			} 
			else {
				arr[count + nums.length] = i;
			}

		}
		return maxlen;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/contiguous-array/discuss/99701/Java-Divide-and-conquer
	 * 
	 * Not optimal, a divide-and-conquer approach.
	 * Time = O(n log n), peak space = O(n).
	 */
	public int findMaxLength_Divide_and_Conquer(int[] nums) {
		return dfs(nums, 0, nums.length - 1);
	}

	private int dfs(int[] a, int l, int r) {
		if (l >= r)
			return 0;
		
		int mid = (l + r) / 2;
		Map<Integer, Integer> map = new HashMap<>();
		
		// left part
		for (int i = mid, one = 0, zero = 0; i >= l; i--) {
			if (a[i] == 0)
				zero++;
			else
				one++;
			
			// "number of 0" - "number of 1" : total number
			map.put(zero - one, zero + one);
		}
		
		int max = 0;
		
		// right part
		for (int i = mid + 1, one = 0, zero = 0; i <= r; i++) {
			if (a[i] == 0)
				zero++;
			else
				one++;
			
			// combine with left part
			// find "number of 1" - "number of 0"
			if (map.containsKey(one - zero))
				max = Math.max(max, map.get(one - zero) + zero + one);
		}

		return Math.max(max, Math.max(dfs(a, l, mid), dfs(a, mid + 1, r)));
	}
	
	/**
	 * Similar problems
	 * 
	 * https://leetcode.com/problems/contiguous-array/discuss/577799/Java-Prefix-sum-and-Hash-Map-Clean-code-Easy-to-understand
	 * https://leetcode.com/problems/contiguous-array/discuss/343679/Simmilar-questions-conclution-using-HashMap-and-Prefixsum-idea
	 */
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/contiguous-array/discuss/577489/Python-O(n)-by-partial-sum-and-dictionary.-90%2B-w-Visualization
     * https://leetcode.com/problems/contiguous-array/discuss/99655/Python-O(n)-Solution-with-Visual-Explanation
     * https://leetcode.com/problems/contiguous-array/discuss/577318/python-short-explained
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/contiguous-array/discuss/577239/Short-C%2B%2B-easy-to-understand-code.
     * https://leetcode.com/problems/contiguous-array/discuss/577279/C%2B%2B-Crystal-Clear-Explanation
     * https://leetcode.com/problems/contiguous-array/discuss/99689/C%2B%2B-O(N)-array-instead-of-unordered_map.
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/contiguous-array/discuss/472855/JavaScript-Solution
	 * https://leetcode.com/problems/contiguous-array/discuss/351906/Javascript-beats-90-simple-using-presum-and-HashMap
	 */

}
