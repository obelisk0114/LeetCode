package OJ0541_0550;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Split_Array_with_Equal_Sum {
	/*
	 * https://leetcode.com/problems/split-array-with-equal-sum/solution/
	 * Approach #5 Using Cumulative Sum and HashSet
	 * 
	 * 1 <= i <= n - 6
	 * i + 2 <= j <= n - 4
	 * j + 2 <= k <= n - 2
	 * 
	 * Here j is used for middle cut, i for left cut and k for right cut.
	 * Iterate middle cuts and then find left cuts which divides the first half into 
	 * two equal quarters, store that quarter sums in the hashset. Then find right 
	 * cuts which divides the second half into two equal quarters and check if 
	 * quarter sum is present in the hashset. If yes return true.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/split-array-with-equal-sum/discuss/101481/Simple-Java-solution-O(n2)
	 * https://leetcode.com/problems/split-array-with-equal-sum/discuss/621783/Java-very-simple-O(n2)-solution-divide-the-array-into-2-parts-and-get-equal-sums/667250
	 * 
	 * Other code:
	 * https://leetcode.com/problems/split-array-with-equal-sum/discuss/621783/Java-very-simple-O(n2)-solution-divide-the-array-into-2-parts-and-get-equal-sums
	 */
	public boolean splitArray_set(int[] nums) {
		if (nums.length < 7)
			return false;

		int[] sum = new int[nums.length];

		sum[0] = nums[0];
		for (int i = 1; i < nums.length; i++) {
			sum[i] = sum[i - 1] + nums[i];
		}

		for (int j = 3; j < nums.length - 3; j++) {
			
			// Stores all sums that we can get by splitting the left half into 
			// two such that the sums are equal.
			HashSet<Integer> set = new HashSet<>();
			
			// All splits of the left half nums[0, j - 1].
			for (int i = 1; i < j - 1; i++) {
				// sum(nums[0, i - 1]) == sum(nums[i + 1, j - 1])
				if (sum[i - 1] == sum[j - 1] - sum[i])
					set.add(sum[i - 1]);
			}
			
			// All splits of the right half nums[j + 1, nums.length - 1].
			for (int k = j + 2; k < nums.length - 1; k++) {
				// sum(nums[k + 1, nums.length - 1]) == sum(nums[j + 1, k - 1])
				if (sum[nums.length - 1] - sum[k] == sum[k - 1] - sum[j] 
						&& set.contains(sum[k - 1] - sum[j]))
					
					return true;
			}
		}
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/split-array-with-equal-sum/discuss/101484/Java-solution-DFS
	 * 
	 * 1. Calculate left on the fly. Thus at last we don't need to calc summary of 
	 *    the 4th part.
	 * 2. Skip 0 during calculate target because adding zero won't change it.
	 */
	public boolean splitArray_dfs_trick(int[] nums) {
		int sum = 0, target = 0;
		for (int num : nums)
			sum += num;
		
		for (int i = 1; i + 5 < nums.length; i++) {
			if (i != 1 && nums[i - 1] == 0 && nums[i] == 0)
				continue;
			
			target += nums[i - 1];
			
			if (dfs_dfs(nums, i + 1, target, sum - target - nums[i], 1))
				return true;
		}
		return false;
	}

	private boolean dfs_dfs(int[] nums, int start, int target, int left, int depth) {
		if (depth == 3) {
			if (left == target)
				return true;
			
			return false;
		}

		int sub = 0;
		for (int j = start + 1; j + 5 - depth * 2 < nums.length; j++) {
			sub += nums[j - 1];
			
			if (sub == target) {
				if (dfs_dfs(nums, j + 1, target, left - sub - nums[j], depth + 1)) {
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * https://leetcode.com/problems/split-array-with-equal-sum/solution/
	 * Approach #4 Using HashMap [Time limit Exceeded]
	 * 
	 * In this approach, we create a data structure called map which is simply a 
	 * HashMap, with data arranged in the format:
	 * 
	 * { csum(i) : [i1, i2, i3, ...] }
	 * csum(i) represents the cumulative sum in the given array nums upto the ith 
	 * index and its corresponding value represents indices upto which cumulative 
	 * sum = csum(i).
	 * 
	 * Consider only the first two cuts formed by i and j. Then, the cumulative sum 
	 * upto the (j - 1)th index will be given by: 
	 * csum(j-1) = sum(part1) + nums[i] + sum(part2)
	 * 
	 * if we want the first two parts to have the same sum, the same cumulative sum 
	 * can be rewritten as:
	 * csum'(j-1) = csum(i-1) + nums[i] + csum(i-1) = 2 * csum(i-1) + nums[i]
	 * 
	 * Thus, we traverse over the given array, changing the value of the index i 
	 * forming the first cut, and look if the map formed initially contains a 
	 * cumulative sum equal to csum'(j-1). If map contains such a cumulative sum, we 
	 * consider every possible index j satisfying the given constraints and look for 
	 * the equalities of the first cumulative sum with the third and the fourth parts.
	 * 
	 * the cumulative sum upto the third cut by kth index is given by
	 * csum(k-1) = sum(part1) + nums[i] + sum(part2) + nums[j] + sum(part3)
	 * 
	 * For equality of sum, the condition becomes:
	 * csum'(k-1) = 3 * csum(i - 1) + nums[i] + nums[j]
	 * 
	 * the cumulative sum upto the last index becomes:
	 * csum(end) = sum(part1) + nums[i] + sum(part2) + nums[j] + 
	 *             sum(part3) + nums[k] + sum(part4)
	 * 
	 * For equality of sum, the condition becomes:
	 * csum'(end) = 4 * csum(i - 1) + nums[i] + nums[j] + nums[k]
	 * 
	 * For every cut chosen, we look if the required cumulative sum exists in map. 
	 * Thus, we need not calculate sums again and again or traverse over the array 
	 * for all the triplets (i, j, k) possible. Rather, now, we directly know, what 
	 * cumulative sum to look for in the map, which reduces a lot of computations.
 	 */
	public boolean splitArray_map(int[] nums) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		int summ = 0, tot = 0;

		for (int i = 0; i < nums.length; i++) {
			summ += nums[i];
			
			if (map.containsKey(summ))
				map.get(summ).add(i);
			else {
				map.put(summ, new ArrayList<Integer>());
				map.get(summ).add(i);
			}
			
			tot += nums[i];
		}
        
        summ = nums[0];
        for (int i = 1; i < nums.length - 5; i++) {
            if (map.containsKey(2 * summ + nums[i])) {
                for (int j: map.get(2 * summ + nums[i])) {
                    j++;
                    
                    if (j > i + 1 && j < nums.length - 3 
                    		&& map.containsKey(3 * summ + nums[i] + nums[j])) {
                    	
                        for (int k: map.get(3 * summ + nums[j] + nums[i])) {
                            k++;
                            
                            if (k < nums.length - 1 && k > j + 1 
                            		&& 4 * summ + nums[i] + nums[j] + nums[k] == tot)
                                return true;
                        }
                    }
                }
            }
            
            summ += nums[i];
        }
        return false;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/split-array-with-equal-sum/discuss/101495/5-lines-simple-Python
     * https://leetcode.com/problems/split-array-with-equal-sum/discuss/101488/Python-Simple-Explanation
     */
    
}
