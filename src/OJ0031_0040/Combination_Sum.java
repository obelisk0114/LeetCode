package OJ0031_0040;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Combination_Sum {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/35666/share-my-15-line-dfs-java-code
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/7277/a-recursive-yet-efficient-java-solution-with-explanation
	 * https://discuss.leetcode.com/topic/7698/java-solution-using-recursive
	 * https://discuss.leetcode.com/topic/25900/if-asked-to-discuss-the-time-complexity-of-your-solution-what-would-you-say
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/3043/a-solution-avoid-using-set
	 * https://discuss.leetcode.com/topic/19329/simple-java-solution
	 */
	public List<List<Integer>> combinationSum(int[] candidates, int target) {
		List<List<Integer>> ans = new ArrayList<>();
		Arrays.sort(candidates);
		dfs(ans, new ArrayList<Integer>(), candidates, target, 0);
		return ans;
	}
	private void dfs(List<List<Integer>> ans, List<Integer> list, int[] cand, int remain, int from) {
		if (remain < 0) {
			return;
		}
		if (remain == 0) {
			ans.add(new ArrayList<Integer>(list));
			return;
		}
		
		// cand[] sorted; from is the starting point of picking elements at this level
		for (int i = from; i < cand.length; ++i) { 
			list.add(cand[i]);
			dfs(ans, list, cand, remain - cand[i], i);
			list.remove(list.size() - 1);
		}
	}
	
	// https://discuss.leetcode.com/topic/4997/non-recursive-java-solution
	public List<List<Integer>> combinationSum_iterative(int[] candidates, int target) {
		Arrays.sort(candidates);
		int i = 0, size = candidates.length, sum = 0;
		Stack<Integer> combi = new Stack<>(), indices = new Stack<>();
		List<List<Integer>> result = new ArrayList<>();
		while (i < size) {
			if (sum + candidates[i] >= target) {
				if (sum + candidates[i] == target) {
					combi.push(candidates[i]);
					result.add(new ArrayList<>(combi));
					combi.pop();
				}
				// indices stack and combination stack should have the same size all the time
				if (!indices.empty()) {
					sum -= combi.pop();
					i = indices.pop();
					while (i == size - 1 && !indices.empty()) {
						i = indices.pop();
						sum -= combi.pop();

					}
				}
				i++;
			} else {
				combi.push(candidates[i]);
				sum += candidates[i];
				indices.push(i);
			}
		}
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/8200/iterative-java-dp-solution
	 * 
	 * The main idea reminds an approach for solving coins/knapsack problem -
	 *   to store the result for all i < target and create the solution from them. 
	 * For that for each t from 1 to our target we try every candidate 
	 *   which is less or equal to t in ascending order. 
	 * For each candidate "c" we run through all combinations for target t-c 
	 *   starting with the value greater or equal than c to avoid duplicates 
	 *   and store only ordered combinations.
	 */
	public List<List<Integer>> combinationSum_dp(int[] cands, int t) {
		Arrays.sort(cands); // sort candidates to try them in asc order
		List<List<List<Integer>>> dp = new ArrayList<>();
		for (int i = 1; i <= t; i++) { // run through all targets from 1 to t
			List<List<Integer>> newList = new ArrayList<>(); // combs for curr i
			// run through all candidates <= i
			for (int j = 0; j < cands.length && cands[j] <= i; j++) {
				// special case when curr target is equal to curr candidate
				if (i == cands[j])
					newList.add(Arrays.asList(cands[j]));
				// if current candidate is less than the target use prev results
				else
					for (List<Integer> l : dp.get(i - cands[j] - 1)) {
						if (cands[j] <= l.get(0)) {    // avoid duplicate
							List<Integer> cl = new ArrayList<>();
							cl.add(cands[j]);
							cl.addAll(l);
							newList.add(cl);
						}
					}
			}
			dp.add(newList);
		}
		return dp.get(t - 1);
    }

}
