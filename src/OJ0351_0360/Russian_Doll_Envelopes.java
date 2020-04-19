package OJ0351_0360;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Russian_Doll_Envelopes {
	/*
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82763/Java-NLogN-Solution-with-Explanation
	 * 
	 * 1. Sort the array. Ascend on width and descend on height if width are same.
	 * 2. Find the longest increasing subsequence based on height.
	 * 
	 * Since the width is increasing, we only need to consider height.
	 * [3, 4] cannot contains [3, 3], so we need to put [3, 4] before [3, 3] when 
	 * sorting otherwise it will be counted as an increasing number if the order is 
	 * [3, 3], [3, 4]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82763/Java-NLogN-Solution-with-Explanation/87015
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82763/Java-NLogN-Solution-with-Explanation/188360
	 */
	public int maxEnvelopes_LIS(int[][] envelopes) {
		if (envelopes == null || envelopes.length == 0 
				|| envelopes[0] == null || envelopes[0].length != 2)
			return 0;
		
		Arrays.sort(envelopes, new Comparator<int[]>() {
			public int compare(int[] arr1, int[] arr2) {
				if (arr1[0] == arr2[0])
					return arr2[1] - arr1[1];
				else
					return arr1[0] - arr2[0];
			}
		});
		
		int dp[] = new int[envelopes.length];
		int len = 0;
		for (int[] envelope : envelopes) {
			int index = Arrays.binarySearch(dp, 0, len, envelope[1]);
			if (index < 0)
				index = -(index + 1);
			
			dp[index] = envelope[1];
			if (index == len)
				len++;
		}
		return len;
	}
	
	/*
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/348428/O(N-LOG-N)-JAVA-SOLUTION-%2B-EXAMPLE
	 * 
	 * 1. Sort by increasing height, decreasing width. So if we have [width, height] 
	 *    of [2, 5], [2, 6], [2,7], they would be sorted to [2, 7], [2, 6], [2,5]
	 * 2. Since the widths are already in increasing order, we can use the Longest 
	 *    Increasing Subsequence (LIS) algorithm on the heights to get our answer. 
	 * 
	 * LIS O(nlogn) solution
	 * 1. make the all the elements in the collector as small as possible, especially 
	 *    the last one which is the gate to control the size of the collector - the 
	 *    longest length;
	 * 2. append the bigger ones to the collector;
	 * 
	 * If we do not sort the sub-arrays (with the same width) in descending order, the 
	 * LIS in the height is then invalid. Suppose the sub-arrays are also sorted in 
	 * ascending order, the height in the same width will be appended in our LIS 
	 * method, wrong result. To sort the heights in the same width in descending order 
	 * will avoid this case 
	 * 
	 * [width, height] sorted by width increasing, height decreasing:
	 * [1 2] [1 1]   [2 2]   [3 4] [3 3]   [4 4]
	 * 
	 * The heights are:
	 * [_ 2] [_ 1]   [_ 2]   [_ 4] [_ 3]   [_ 4]
	 * 
	 * Longest Increasing Subsequence (LIS) on heights gives us:
	 * [_ 1]   [_ 2]         [_ 3]   [_ 4]
	 * 
	 * solution:
	 * [1 1]   [2 2]         [3 3]   [4 4]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82778/Two-solutions-in-C%2B%2B-well-explained
	 * 
	 * Other code:
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82763/Java-NLogN-Solution-with-Explanation/87032
	 */
	public int maxEnvelopes_LIS2(int[][] envelopes) {
		if (envelopes == null || envelopes.length == 0 || envelopes[0].length != 2) {
			return 0;
		}
		Arrays.sort(envelopes, (a, b) -> {
			if (a[0] != b[0]) {
				return a[0] - b[0];
			} else {
				return b[1] - a[1];
			}
		});

		// Longest Increasing Subsequence Algorithm
		int[] sortedArray = new int[envelopes.length];
		int size = 0;
		
		for (int[] envelope : envelopes) {
			int num = envelope[1];
			int start = 0;
			int end = size; // 1 element past end of our sortedArray
			
			while (start != end) {
				int mid = (start + end) / 2;
				if (sortedArray[mid] < num) {
					start = mid + 1;
				} 
				else {
					end = mid;
				}
			}
			sortedArray[start] = num;
			
			if (start == size) {
				size++;
			}
		}
		return size;
	}
	
	/*
	 * by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82796/A-Trick-to-solve-this-problem.
	 */
	public int maxEnvelopes_self(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0)
            return 0;
        
        Arrays.sort(envelopes, (int[] a, int[] b) -> {
            if (a[0] != b[0])
                return a[0] - b[0];
            else
                return b[1] - a[1];
        });
        
        List<Integer> dp = new ArrayList<>();
        dp.add(envelopes[0][1]);
        for (int i = 1; i < envelopes.length; i++) {
            int cur = envelopes[i][1];
            if (cur > dp.get(dp.size() - 1)) {
                dp.add(cur);
            }
            else {
                int pos = Collections.binarySearch(dp, cur);
                if (pos < 0) {
                    pos = -(pos + 1);
                }
                dp.set(pos, cur);
            }
        }
        return dp.size();
    }
	
	/*
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82759/Simple-DP-solution
	 * 
	 * sort the widths and then longest increasing subseq problem for heights
	 * 
	 * Rf :
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82759/Simple-DP-solution/218786
	 * 
	 * Other code:
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/380421/Java-DP-Solution-Extension-of-Longest-Increasing-Subsequence
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/82810/Short-and-simple-Java-solution-(15-lines)
	 */
	public int maxEnvelopes_dp(int[][] envelopes) {
	    if (   envelopes           == null
	        || envelopes.length    == 0
	        || envelopes[0]        == null
	        || envelopes[0].length == 0){
	        return 0;    
	    }
	    
		Arrays.sort(envelopes, new Comparator<int[]>() {
			@Override
			public int compare(int[] e1, int[] e2) {
				return Integer.compare(e1[0], e2[0]);
			}
		});
	    
	    int   n  = envelopes.length;
	    int[] dp = new int[n];
	    
	    int ret = 0;
	    for (int i = 0; i < n; i++){
	        dp[i] = 1;
	        
	        for (int j = 0; j < i; j++){
	            if (   envelopes[i][0] > envelopes[j][0]
	                && envelopes[i][1] > envelopes[j][1]){
	                dp[i] = Math.max(dp[i], 1 + dp[j]);    
	            }
	        }
	        
	        ret = Math.max(ret, dp[i]);
	    }
	    return ret;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/465134/DFS-or-topological-sorting
	 * 
	 * Consider each doll is a node in a graph. Two nodes are connected if one can 
	 * contain another. It is easy to come up with a O(n^2) solution with 
	 * DFS / Topological sorting
	 * 
	 * Other code:
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/294000/JAVA-dp-solution-with-comments
	 * https://leetcode.com/problems/russian-doll-envelopes/discuss/373703/Simple-Java-Graph-based-solution-(bad-performance)
	 */
	public int maxEnvelopes_dfs(int[][] envelopes) {
		// we need to memorize the longest path of each node.
		int[] visited = new int[envelopes.length];
		
		int max = 0;
		for (int i = 0; i < envelopes.length; i++) {
			max = Math.max(max, dfs(i, envelopes, visited));
		}
		return max;
	}

	private int dfs(int cur, int[][] envelopes, int[] visited) {
		if (visited[cur] > 0) {
			return visited[cur];
		}

		// max = 1 for itself
		int max = 1;
		for (int i = 0; i < envelopes.length; i++) {
			if (envelopes[i][0] < envelopes[cur][0] 
					&& envelopes[i][1] < envelopes[cur][1]) {
				max = Math.max(max, dfs(i, envelopes, visited) + 1);
			}
		}
		visited[cur] = max;

		return max;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/russian-doll-envelopes/discuss/82761/Python-O(nlogn)-O(n)-solution-beats-97-with-explanation
     * https://leetcode.com/problems/russian-doll-envelopes/discuss/82753/10-lines-Python-code-beats-96.
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/russian-doll-envelopes/discuss/82778/Two-solutions-in-C%2B%2B-well-explained
     * https://leetcode.com/problems/russian-doll-envelopes/discuss/82801/C%2B%2B-Time-O(NlogN)-Space-O(N)-similar-to-LIS-nlogn-solution
     * https://leetcode.com/problems/russian-doll-envelopes/discuss/82817/My-Three-C%2B%2B-Solutions%3A-DP-Binary-Search-and-Lower_bound
     */

}
