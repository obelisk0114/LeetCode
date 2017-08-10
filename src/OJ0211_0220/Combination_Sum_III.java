package OJ0211_0220;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Combination_Sum_III {
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/26351/simple-and-clean-java-code-backtracking
	 * https://discuss.leetcode.com/topic/37962/fast-easy-java-code-with-explanation
	 * https://discuss.leetcode.com/topic/44036/combination-sum-i-ii-and-iii-java-solution-see-the-similarities-yourself
	 */
	public List<List<Integer>> combinationSum3_self(int k, int n) {
        List<List<Integer>> ans = new ArrayList<>();
        combine(k, n, 1, new ArrayList<Integer>(), ans);
        return ans;
    }
    
    private void combine(int k, int n, int start, List<Integer> list, List<List<Integer>> ans) {
        if (k == 0 && n == 0) {
            ans.add(new ArrayList<>(list));
            return;
        }
        if (n < 0 || k == 0) {
            return;
        }
        for (int i = start; i <= 9; i++) {
            if (n - i < 0)
                break;
            list.add(i);
            combine(k - 1, n - i, i + 1, list, ans);
            list.remove(list.size() - 1);
        }
    }
    
    /*
     * The following variable and 2 functions are from this link.
     * https://discuss.leetcode.com/topic/15023/accepted-recursive-java-solution-easy-to-understand
     * 
     * The idea is to choose proper number for 1,2..kth position in ascending order, 
     * and for each position, we only iterate through (prev_num, n/k].
     */
	private List<List<Integer>> res_avg = new ArrayList<List<Integer>>();
	public List<List<Integer>> combinationSum3_avg(int k, int n) {
		findCombo(k, n, 1, new LinkedList<Integer>());
		return res_avg;
	}
	public void findCombo(int k, int n, int start, List<Integer> list) {
		if (k == 1) {
			if (n < start || n > 9)
				return;
			list.add(n);
			res_avg.add(list);
			return;
		}
		for (int i = start; i <= n / k && i < 10; i++) {
			List<Integer> subList = new LinkedList<Integer>(list);
			subList.add(i);
			findCombo(k - 1, n - i, i + 1, subList);
		}
	}

}
