package OJ0071_0080;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Combinations {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/47507/2ms-beats-90-java-solution-a-small-trick-to-end-search-early
	 * 
	 * "max" is the remain space.
	 */
	public List<List<Integer>> combine_early_end(int n, int k) {
	    List<List<Integer>> results = new ArrayList<>();
	    dfs(1, n, k, new ArrayList<Integer>(), results);
	    return results;
	}
	private void dfs(int crt, int n, int level, List<Integer> comb, List<List<Integer>> results) {
	    if (level == 0) {
	        List<Integer> newComb = new ArrayList<>(comb);
	        results.add(newComb);
	        return;
	    }
	    int size = comb.size();
	    for (int i = crt, max = n - level + 1; i <= max; i++) { 
	    //end search when its not possible to have any combination
	        comb.add(i);
	        dfs(i + 1, n, level - 1, comb, results);
	        comb.remove(size);
	    }
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/11718/backtracking-solution-java
	 */
	public List<List<Integer>> combine(int n, int k) {
		List<List<Integer>> combs = new ArrayList<List<Integer>>();
		combine(combs, new ArrayList<Integer>(), 1, n, k);
		return combs;
	}
	public void combine(List<List<Integer>> combs, List<Integer> comb, int start, int n, int k) {
		if (k == 0) {
			combs.add(new ArrayList<Integer>(comb));
			return;
		}
		for (int i = start; i <= n; i++) {
			comb.add(i);
			combine(combs, comb, i + 1, n, k - 1);
			comb.remove(comb.size() - 1);
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12537/a-short-recursive-java-solution-based-on-c-n-k-c-n-1-k-1-c-n-1-k
	 * 
	 * C(n,k) is divided into two situations. C(n,k) = C(n-1,k-1) + C(n-1,k).
	 * 1. number n is selected, so we only need to select k-1 from n-1 next. 
	 * 2. number n is not selected, and the rest job is selecting k from n-1.
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/32730/3-ms-java-solution
	 */
	public List<List<Integer>> combine_dp(int n, int k) {
		// C(n,0) is an empty set, and C(n,n) is simply universal set {1,2,3,...,n}.
        if (k == n || k == 0) {
            List<Integer> row = new LinkedList<>();
            for (int i = 1; i <= k; ++i) {
                row.add(i);
            }
            return new LinkedList<>(Arrays.asList(row));
        }
        List<List<Integer>> result = this.combine_dp(n - 1, k - 1);
        result.forEach(e -> e.add(n));
        result.addAll(this.combine_dp(n - 1, k));
        return result;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Combinations combinations = new Combinations();
		int n = 5;
		int k = 3;
		List<List<Integer>> ans = combinations.combine_early_end(n, k);
		for (int i = 0; i < ans.size(); i++) {
			for (int j = 0; j < ans.get(i).size(); j++) {
				System.out.print(ans.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		List<List<Integer>> ans2 = combinations.combine(n, k);
		for (int i = 0; i < ans2.size(); i++) {
			for (int j = 0; j < ans2.get(i).size(); j++) {
				System.out.print(ans2.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();

		List<List<Integer>> ans3 = combinations.combine_dp(n, k);
		for (int i = 0; i < ans3.size(); i++) {
			for (int j = 0; j < ans3.get(i).size(); j++) {
				System.out.print(ans3.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}

}
