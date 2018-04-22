package OJ0581_0590;

import java.util.Arrays;
import java.util.Stack;

public class Shortest_Unsorted_Continuous_Subarray {
	/*
	 * https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103066/Ideas-behind-the-O(n)-two-pass-and-one-pass-solutions
	 * 
	 * Find the two longest sorted subarrays starting at index 0 and ending at 
	 * index n - 1. Assume the two subarrays are nums[0, l] and nums[r, n - 1]. 
	 * If there is overlapping between these two subarrays, i.e.l >= r, then the whole 
	 * array is sorted so 0 will be returned.
	 * 
	 * Assume min and max are the minimum and maximum values of subarray nums[l, r], 
	 * then we need to decrease l as long as nums[l] > min, and increase r as long as 
	 * nums[r] < max.
	 */
	public int findUnsortedSubarray_twoPass(int[] nums) {
		int l = 0, r = nums.length - 1;
		int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;

		while (l < r && nums[l] <= nums[l + 1])
			l++;
		if (l >= r)
			return 0;

		while (nums[r] >= nums[r - 1])
			r--;

		for (int k = l; k <= r; k++) {
			max = Math.max(max, nums[k]);
			min = Math.min(min, nums[k]);
		}

		while (l >= 0 && min < nums[l])   // {1, 3, 2, 2, 2}
			l--;
		while (r < nums.length && nums[r] < max)
			r++;

		return (r - l - 1);
	}
	
	/*
	 * https://leetcode.com/articles/shortest-unsorted-continous-subarray/
	 * 
	 * Use a stack to find the turning point, and find its correct position.
	 * minimum is left point; maximum is right point
	 */
	public int findUnsortedSubarray_stack(int[] nums) {
        Stack < Integer > stack = new Stack < Integer > ();
        int l = nums.length, r = 0;
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i])
                l = Math.min(l, stack.pop());
            stack.push(i);
        }
        
        stack.clear();
        
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
                r = Math.max(r, stack.pop());
            stack.push(i);
        }
        return r - l > 0 ? r - l + 1 : 0;
    }
	
	/*
	 * https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103057/Java-O(n)-Time-O(1)-Space
	 * 
	 * max[k] is the maximum value of subarray nums[0, k].
	 * min[k] is the minimum value of subarray nums[k, n - 1].
	 * 
	 * i is the smallest index such that nums[i] != min[i];
	 * j is the largest index such that nums[j] != max[j].
	 * 
	 * Rf : https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103066/Ideas-behind-the-O(n)-two-pass-and-one-pass-solutions
	 */
	public int findUnsortedSubarray(int[] A) {
		int n = A.length, beg = -1, end = -2, min = A[n - 1], max = A[0];
		for (int i = 1; i < n; i++) {
			max = Math.max(max, A[i]);
			min = Math.min(min, A[n - 1 - i]);
			if (A[i] < max)
				end = i;
			if (A[n - 1 - i] > min)
				beg = n - 1 - i;
		}
		return end - beg + 1;
	}
	
	/*
	 * https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103070/Java-Solution-Sort.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103103/Java-Sort-Solution-easy-to-understand
	 */
	public int findUnsortedSubarray_sort(int[] nums) {
		int n = nums.length;
		int[] temp = nums.clone();
		Arrays.sort(temp);

		int start = 0;
		while (start < n && nums[start] == temp[start])
			start++;

		int end = n - 1;
		while (end > start && nums[end] == temp[end])
			end--;

		return end - start + 1;
	}
	
	// by myself
	public int findUnsortedSubarray_self(int[] nums) {
        int[] num2 = Arrays.copyOf(nums, nums.length);
        Arrays.sort(num2);
        
        int start = 0;
        int end = nums.length - 1;
        int preStart = -1;
        int preEnd = -1;
        while (start != preStart || end != preEnd) {
            preStart = start;
            if (nums[start] == num2[start]) {
                start++;
            }
            
            preEnd = end;
            if (nums[end] == num2[end]) {
                end--;
            }
            
            if (start >= end)
                return 0;
        }
        return (end - start + 1);
    }
	
	/*
	 * https://leetcode.com/articles/shortest-unsorted-continous-subarray/
	 * 
	 * determine the leftmost and rightmost elements which mismatch
	 */
	public int findUnsortedSubarray_sort_onePass(int[] nums) {
        int[] snums = nums.clone();
        Arrays.sort(snums);
        int start = snums.length, end = 0;
        for (int i = 0; i < snums.length; i++) {
            if (snums[i] != nums[i]) {
                start = Math.min(start, i);
                end = Math.max(end, i);
            }
        }
        return (end - start >= 0 ? end - start + 1 : 0);
    }
	
	/*
	 * https://leetcode.com/problems/shortest-unsorted-continuous-subarray/discuss/103082/Java-Sorting-Solution-Find-left-most-and-right-most-mismatch
	 * 
	 * Basically the idea is to compare the given array to it's sorted version, and 
	 * find out the left most mismatch, and the right most mismatch.
	 */
	public int findUnsortedSubarray_like_self(int[] nums) {
		int result = 0;
		int[] copy = Arrays.copyOf(nums, nums.length);
		Arrays.sort(nums);
		int l = 0, r = copy.length - 1;
		while (l < r) {
			if (copy[l] == nums[l])
				l++;
			if (copy[r] == nums[r])
				r--;
			if (copy[l] != nums[l] && copy[r] != nums[r])
				break;
		}
		
		result = r - l + 1;
		if (l == r)
			result = 0;
		return result;
	}

}
