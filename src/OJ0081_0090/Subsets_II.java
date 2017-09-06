package OJ0081_0090;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Subsets_II {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/46162/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partioning
	 * 
	 * Rf : https://discuss.leetcode.com/topic/13543/accepted-10ms-c-solution-use-backtracking-only-10-lines-easy-understand
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/16666/standard-dfs-java-solution
	 */
	public List<List<Integer>> subsetsWithDup(int[] nums) {
		List<List<Integer>> list = new ArrayList<>();
		Arrays.sort(nums);
		backtrack(list, new ArrayList<>(), nums, 0);
		return list;
	}
	private void backtrack(List<List<Integer>> list, List<Integer> tempList, int[] nums, int start) {
		list.add(new ArrayList<>(tempList));
		for (int i = start; i < nums.length; i++) {
			if (i > start && nums[i] == nums[i - 1])
				continue; // skip duplicates
			tempList.add(nums[i]);
			backtrack(list, tempList, nums, i + 1);
			tempList.remove(tempList.size() - 1);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/22638/very-simple-and-fast-java-solution
	 */
	public List<List<Integer>> subsetsWithDup_while_loop(int[] nums) {
		Arrays.sort(nums);
		List<List<Integer>> res = new ArrayList<>();
		List<Integer> each = new ArrayList<>();
		helper(res, each, 0, nums);
		return res;
	}
	public void helper(List<List<Integer>> res, List<Integer> each, int pos, int[] n) {
		if (pos <= n.length) {
			res.add(each);
		}
		int i = pos;
		while (i < n.length) {
			each.add(n[i]);
			helper(res, new ArrayList<>(each), i + 1, n);
			each.remove(each.size() - 1);
			i++;
			while (i < n.length && n[i] == n[i - 1]) {      // avoid the duplicate
				i++;
			}
		}
		return;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/39672/share-my-2ms-java-iteration-solution-very-simple-and-short
	 * 
	 * If we want to insert an element which is a dup, we can only insert it after 
	 * the newly inserted elements from last step.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/3601/simple-iterative-solution
	 */
	public List<List<Integer>> subsetsWithDup_iterative(int[] nums) {
		Arrays.sort(nums);
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		result.add(new ArrayList<Integer>());
		int begin = 0;
		for (int i = 0; i < nums.length; i++) {
			if (i == 0 || nums[i] != nums[i - 1])
				begin = 0;
			int size = result.size();
			for (int j = begin; j < size; j++) {
				List<Integer> cur = new ArrayList<Integer>(result.get(j));
				cur.add(nums[i]);
				result.add(cur);
			}
			begin = size;  // If duplicate occurs, only add number to the last. 
		}
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12706/java-solution-using-bit-manipulation
	 * 
	 * using every bit to represent one element in the set
	 */
	public List<List<Integer>> subsetsWithDup_bit_manipulation(int[] num) {
		Arrays.sort(num);
		List<List<Integer>> lists = new ArrayList<>();
		int subsetNum = 1 << num.length;
		for (int i = 0; i < subsetNum; i++) {
			List<Integer> list = new ArrayList<>();
			boolean illegal = false;
			for (int j = 0; j < num.length; j++) {
				if ((i >> j & 1) == 1) {
					// Current number is the same as previous one. We choose one.
					if (j > 0 && num[j] == num[j - 1] && ((i >> (j - 1)) & 1) == 0) {
						illegal = true;
						break;
					} else {
						list.add(num[j]);
					}
				}
			}
			if (!illegal) {
				lists.add(list);
			}
		}
		return lists;
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public List<List<Integer>> subsetsWithDup_self(int[] nums) {
        Set<List<Integer>> res = new HashSet<>();
        Arrays.sort(nums);
        addSub2(nums, 0, new ArrayList<Integer>(), res);
        return new ArrayList<List<Integer>>(res);
    }
    private void addSub2(int[] nums, int level, List<Integer> list, Set<List<Integer>> res) {
        if (level == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        
        list.add(nums[level]);
        addSub2(nums, level + 1, list, res);
        list.remove(list.size() - 1);
        addSub2(nums, level + 1, list, res);
    }
    
    // https://discuss.leetcode.com/topic/54624/subsets-vs-subsets-ii-add-only-3-more-lines-to-subsets-solution
	public List<List<Integer>> subsetsWithDup_iterative_contains(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		res.add(new ArrayList<Integer>());
		Arrays.sort(nums); // important: sort nums

		for (int num : nums) {
			List<List<Integer>> resDup = new ArrayList<>(res);
			for (List<Integer> list : resDup) {
				List<Integer> tmp = new ArrayList<>(list);
				tmp.add(num);
				if (!res.contains(tmp)) // check duplicates
					res.add(tmp);
			}
		}
		return res;
	}
    
    // https://discuss.leetcode.com/topic/2634/my-solution-using-bit-masks
	public List<List<Integer>> subsetsWithDup_bit_contains(int[] num) {
		// Sort the input
		Arrays.sort(num);
		int numberSets = 1 << num.length;
		List<List<Integer>> solution = new LinkedList<>();
		for (int i = 0; i < numberSets; i++) {
			List<Integer> subset = new LinkedList<Integer>();
			for (int j = 0; j < num.length; j++) {
				if ((i & (1 << j)) > 0) {
					subset.add(num[j]);
				}
			}
			if (!solution.contains(subset))
				solution.add(subset);
		}

		return solution;
	}

}
