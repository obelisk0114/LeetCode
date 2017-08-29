package OJ0091_0100;

import java.util.Map;
import java.util.HashMap;

public class Unique_Binary_Search_Trees {
	/*
	 * https://discuss.leetcode.com/topic/8398/dp-solution-in-6-lines-with-explanation-f-i-n-g-i-1-g-n-i
	 * 
	 * Taking 1~n as root respectively:
        1 as root: # of trees = F(0) * F(n-1)  // F(0) == 1
        2 as root: # of trees = F(1) * F(n-2) 
        3 as root: # of trees = F(2) * F(n-3)
        ...
        n-1 as root: # of trees = F(n-2) * F(1)
        n as root:   # of trees = F(n-1) * F(0)
 
     * So, the formulation is:
       F(n) = F(0) * F(n-1) + F(1) * F(n-2) + F(2) * F(n-3) + ... + F(n-2) * F(1) + F(n-1) * F(0)
       
	 * Rf :
	 * https://discuss.leetcode.com/topic/5673/dp-problem-10-lines-with-comments
	 * https://discuss.leetcode.com/topic/37310/fantastic-clean-java-dp-solution-with-detail-explaination
	 * https://discuss.leetcode.com/topic/29801/a-very-nice-explanation
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/25670/9-line-clean-java-dp-solution
	 */
	public int numTrees(int n) {
		int[] G = new int[n + 1];
		G[0] = G[1] = 1;

		for (int i = 2; i <= n; ++i) {
			for (int j = 1; j <= i; ++j) {
				G[i] += G[j - 1] * G[i - j];
			}
		}

		return G[n];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/9474/simple-recursion-java-solution-with-explanation
	 * 
	 * The idea is to use each number i as root node, then the left branch will be 
	 * what's less than i, the right branch will be what's larger than i. 
	 * The total number of distinct structure is their product. 
	 * Sum up the product for all numbers. Use a map to memorize the visited number.
	 */
	public int numTrees_map(int n) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, 1);
		map.put(1, 1);
		return numTrees_map(n, map);
	}
	private int numTrees_map(int n, Map<Integer, Integer> map) {
		// check memory
		if (map.containsKey(n))
			return map.get(n);
		// recursion
		int sum = 0;
		for (int i = 1; i <= n; i++)
			sum += numTrees_map(i - 1, map) * numTrees_map(n - i, map);
		map.put(n, sum);
		return sum;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/32057/easy-to-understand-top-down-dp-solution
	 * 
	 * For a valid BST, there must be a node that is the root. The root can be any 
	 * number between 1 and n. What if the root is k where k is in between 1 and n? 
	 * It becomes obvious that 1 to k-1 should be k's left children and k+1 to n 
	 * should be k's right children. 
	 * 
	 * number of BSTs with k being the root =
     * count of BSTs of k-1 consecutive numbers * count of BSTs of n-k consecutive numbers
	 */
	public int numTrees_top_down(int n) {
		return numTrees_top_down(n, new int[n]);
	}
	private int numTrees_top_down(int n, int[] arr) {
		if (n <= 1)
			return 1;
		if (arr[n - 1] > 0)
			return arr[n - 1];

		int num = 0;
		for (int i = 1; i <= n; i++)
			num += numTrees_top_down(i - 1, arr) * numTrees_top_down(n - i, arr);

		arr[n - 1] = num; // store for reuse
		return num;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/25779/catalan-numbers-4-lines-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/5822/it-s-catalan-number
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/13321/a-very-simple-and-straight-ans-based-on-math-catalan-number-o-n-times-o-1-space
	 */
	public int numTrees_Catalan_number_formula(int n) {
		double count = 1;
		for (double i = 1; i <= n; i++)
			count *= ((i + n) / i);
		return (int) Math.round(count / (n + 1));
	}

}
