package OJ0031_0040;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Combination_Sum_II {
	// https://discuss.leetcode.com/topic/60535/understanding-the-differences-between-the-dp-solution-and-simple-recursive-which-one-is-really-better
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/19845/java-solution-using-dfs-easy-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/82533/a-smaller-trick-to-improve-a-lot-beat-92-35
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/24059/java-short-and-recursive-clean-code
	 */
	public List<List<Integer>> combinationSum2(int[] cand, int target) {
		Arrays.sort(cand);
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		List<Integer> path = new ArrayList<Integer>();
		dfs_com(cand, 0, target, path, res);
		return res;
	}
	void dfs_com(int[] cand, int cur, int target, List<Integer> path, 
			List<List<Integer>> res) {
		
		if (target == 0) {
			res.add(new ArrayList<>(path));
			return;
		}
		
		// 可以加上這段
		//if (target < 0)
			//return;
		
		for (int i = cur; i < cand.length; i++) {
			if (target - cand[i] < 0)
				break;
			if (i > cur && cand[i] == cand[i - 1])
				continue;
			
			path.add(cand[i]);
			dfs_com(cand, i + 1, target - cand[i], path, res);
			path.remove(path.size() - 1);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/32435/5ms-java-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/34364/java-solutions-beats-99-87
	 */
	public List<List<Integer>> combinationSum2_preCheck(int[] candidates, int target) {
		Arrays.sort(candidates);
		List<List<Integer>> ans = new ArrayList<List<Integer>>();
		ch(candidates, target, 0, new ArrayList<Integer>(), ans);
		return ans;
	}
	private void ch(int[] candidates, int remain, int rindex, List<Integer> tmp, List<List<Integer>> ans) {
		if (remain == 0) {
			List<Integer> a = new ArrayList<Integer>(tmp);
			ans.add(a);
			return;
		}
		int entered = 0; // get rid of duplicate combinations
		for (int i = rindex; i < candidates.length; i++) {
			if (entered != candidates[i]) {    // get rid of duplicate combinations
				if (remain - candidates[i] < 0)
					break; // This line of code can reduce 7ms from execution time!
				tmp.add(candidates[i]);
				entered = candidates[i];
				ch(candidates, remain - candidates[i], i + 1, tmp, ans);
				tmp.remove(tmp.size() - 1);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/2598/my-solution-in-java
	 */
	public List<List<Integer>> combinationSum2_DFS(int[] candidates, int target) {
		if (candidates == null || candidates.length == 0)
			return Collections.emptyList(); // Or throw exception();
		List<List<Integer>> results = new LinkedList<>();
		LinkedList<Integer> work = new LinkedList<>();
		Arrays.sort(candidates);

		for (int i = 0, len = candidates.length; i < len; i++) {
			if (i > 0 && candidates[i] == candidates[i - 1])
				continue; // Avoid duplicates;
			combinationSumHelper(candidates, i, target, work, results);// DFS
		}
		return results;
	}
    //Use DFS
	private void combinationSumHelper(int[] candidates, int index, int target, 
			LinkedList<Integer> work, List<List<Integer>> results) {
		// Compare candidates[index] and target;
		// If equals, terminate the search,return result
		// If candidates[index] > target, terminate the search, no result
		// Otherwise, study rest of elements.
		if (candidates[index] > target) {
			return;
		} 
		else if (candidates[index] == target) {     // Update the results
			work.addLast(candidates[index]);
			results.add(new ArrayList<Integer>(work));
			work.removeLast();
			return;
		}
		work.addLast(candidates[index]);
		for (int i = index + 1, len = candidates.length; i < len; i++) {
			if (i > index + 1 && candidates[i] == candidates[i - 1])
				continue;     // Avoid duplicates
			if (candidates[i] <= target - candidates[index]) {
				combinationSumHelper(candidates, i, target - candidates[index], work, results);
			}
		}
		work.removeLast();
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2081/my-thoughts-and-solution-to-the-problem-java
	 */
	public List<List<Integer>> combinationSum2_self(int[] candidates, int target) {
		Set<List<Integer>> ans = new HashSet<>();
		Arrays.sort(candidates);
		combine_Set_self(candidates, target, 0, new ArrayList<Integer>(), ans);
		return new ArrayList<>(ans);
	}

	private void combine_Set_self(int[] nums, int target, int start, List<Integer> list, Set<List<Integer>> ans) {
		if (target < 0) {
			return;
		}
		if (target == 0) {
			List<Integer> tmp = new ArrayList<>(list);
			ans.add(tmp);
			return;
		}
		for (int i = start; i < nums.length; i++) {
			list.add(nums[i]);
			combine_Set_self(nums, target - nums[i], i + 1, list, ans);
			list.remove(list.size() - 1);
		}
	}

}
