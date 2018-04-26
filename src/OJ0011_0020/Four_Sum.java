package OJ0011_0020;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/*
 * https://leetcode.com/problems/4sum/discuss/8565/Lower-bound-n3
 * 
 * https://leetcode.com/problems/4sum/discuss/8723/There-maybe-isn't-a-n2lgn-or-better-time-complexity-solution
 * https://leetcode.com/problems/4sum/discuss/8761/Any-better-solution-than-O(n3)
 */

public class Four_Sum {
	/*
	 * https://leetcode.com/problems/4sum/discuss/8575/Clean-accepted-java-O(n3)-solution-based-on-3sum
	 * 
	 * Rf : https://leetcode.com/problems/4sum/discuss/8547/7ms-java-code-win-over-100
	 */
	public List<List<Integer>> fourSum(int[] num, int target) {
		ArrayList<List<Integer>> ans = new ArrayList<>();
		if (num.length < 4)
			return ans;
		
		Arrays.sort(num);
		for (int i = 0; i < num.length - 3; i++) {
			if (num[i] + num[i + 1] + num[i + 2] + num[i + 3] > target)
				break; // first candidate too large, search finished
			
			if (num[i] + num[num.length - 1] + num[num.length - 2] + num[num.length - 3] < target)
				continue; // first candidate too small
			if (i > 0 && num[i] == num[i - 1])
				continue; // prevents duplicate result in ans list
			
			for (int j = i + 1; j < num.length - 2; j++) {
				if (num[i] + num[j] + num[j + 1] + num[j + 2] > target)
					break; // second candidate too large
				
				if (num[i] + num[j] + num[num.length - 1] + num[num.length - 2] < target)
					continue; // second candidate too small
				if (j > i + 1 && num[j] == num[j - 1])
					continue; // prevents duplicate results in ans list
				
				int low = j + 1, high = num.length - 1;
				while (low < high) {
					int sum = num[i] + num[j] + num[low] + num[high];
					if (sum == target) {
						ans.add(Arrays.asList(num[i], num[j], num[low], num[high]));
						while (low < high && num[low] == num[low + 1])
							low++; // skipping over duplicate on low
						while (low < high && num[high] == num[high - 1])
							high--; // skipping over duplicate on high
						
						low++;
						high--;
					}
					// move window
					else if (sum < target)
						low++;
					else
						high--;
				}
			}
		}
		return ans;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/4sum/discuss/8608/Java-backtracking-solution-for-K-sum-beat-94
	 * 
	 * 1. avoid sum cases by viewing nums[start]*k and nums[end]*k
	 * 2. when backtracking, first add nums[i] to current list, after that then remove
	 */
	public List<List<Integer>> fourSum_kSum(int[] nums, int target) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		Arrays.sort(nums);
		kSum(result, new ArrayList<Integer>(), nums, target, 4, 0);
		return result;
	}
	private void kSum(List<List<Integer>> result, List<Integer> cur, int[] nums, int target, int k, int start) {
		if (k == 0 || nums.length == 0)
			return;
		
		if (k == 1) {
			for (int i = start; i < nums.length; i++) {
				if (nums[i] == target) {
					cur.add(nums[i]);
					result.add(new ArrayList<Integer>(cur));
					cur.remove(cur.size() - 1);
				}
			}
			return;
		}

		if (k == 2) { // 2 sum
			int sum;
			int end = nums.length - 1;
			while (start < end) {
				sum = nums[start] + nums[end];

				if (sum == target) {
					cur.add(nums[start]);
					cur.add(nums[end]);
					result.add(new ArrayList<Integer>(cur));
					cur.remove(cur.size() - 1);
					cur.remove(cur.size() - 1);

					// avoid duplicate
					while (start < end && nums[start] == nums[start + 1]) {						
						start++;
					}
					start++;
					
					while (start < end && nums[end] == nums[end - 1]) {
						end--;
					}
					end--;
				} 
				else if (sum < target) {
					// avoid duplicate
					while (start < end && nums[start] == nums[start + 1]) {						
						start++;
					}
					start++;
				} 
				else {
					while (start < end && nums[end] == nums[end - 1]) {
						end--;
					}
					end--;
				}
			}
			return;
		}

		// 避免一些不必要case
		if (k * nums[start] > target || k * nums[nums.length - 1] < target)
			return;

		// k > 2 : choose nums[i] and do k-1 sum on the rest at right
		for (int i = start; i < (nums.length - (k - 1)); i++) {
			// avoid duplicate
			if (i > start && nums[i] == nums[i - 1])
				continue;
			
			// 重點
			if (k * nums[i] <= target) { // 避免掉一些不必要case
				cur.add(nums[i]);
				kSum(result, cur, nums, target - nums[i], k - 1, i + 1);
				cur.remove(cur.size() - 1);
			}
		}
	}
	
	// by myself
	public List<List<Integer>> fourSum_self(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        Arrays.sort(nums);
        
        if (nums == null || nums.length < 4 || 
        		4 * nums[0] > target || 4 * nums[nums.length - 1] < target)
            return ans;
        
        if (nums[0] == nums[nums.length - 1] && 4 * nums[0] == target) {
            List<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < 4; i++) {
                list.add(nums[0]);
            }
            ans.add(list);
            return ans;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            for (int j = i + 1; j < nums.length; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                
                int start = j + 1;
                int end = nums.length - 1;
                while (start < end) {
                    if (nums[i] + nums[j] + nums[start] + nums[end] == target) {
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[start]);
                        list.add(nums[end]);
                        ans.add(list);
                        
                        start++;
                        while (start + 1 < nums.length && nums[start - 1] == nums[start]) { // start < end
                            start++;
                        }
                        
                        end--;
                        while (end - 1 > j + 1 && nums[end + 1] == nums[end]) { // end > start
                            end--;
                        }
                    }
                    else if (nums[i] + nums[j] + nums[start] + nums[end] > target) {
                        end--;
                        while (end - 1 > j + 1 && nums[end + 1] == nums[end]) {
                            end--;
                        }
                    }
                    else {
                        start++;
                        while (start + 1 < nums.length && nums[start - 1] == nums[start]) {
                            start++;
                        }
                    }
                }
            }
        }
        return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/4sum/discuss/8609/My-solution-generalized-for-kSums-in-JAVA
	 * 
	 * 1. 2sum Problem
	 * 2. Reduce K sum problem to K - 1 sum Problem
	 * 
	 * We could use recursive to solve this problem. Time complexity is O(N^(K-1)).
	 * 
	 * Rf : https://leetcode.com/problems/4sum/discuss/8545/Python-140ms-beats-100-and-works-for-N-sum-(Ngreater2)
	 */
	public List<List<Integer>> fourSum_kSum2(int[] nums, int target) {
		Arrays.sort(nums);
		return kSum2(nums, 0, 4, target);
	}
	private List<List<Integer>> kSum2(int[] nums, int start, int k, int target) {
		int len = nums.length;
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		
		if (k == 2) { // two pointers from left and right
			int left = start, right = len - 1;
			
			while (left < right) {
				int sum = nums[left] + nums[right];
				
				// find a pair
				if (sum == target) {
					List<Integer> path = new ArrayList<Integer>();
					path.add(nums[left]);
					path.add(nums[right]);
					res.add(path);
					
					// skip duplication
					while (left < right && nums[left] == nums[left + 1])
						left++;
					while (left < right && nums[right] == nums[right - 1])
						right--;
					left++;
					right--;
				} else if (sum < target) { // move left
					left++;
				} else {                   // move right
					right--;
				}
			}
		} else {
			for (int i = start; i < len - (k - 1); i++) {
				//skip duplicated numbers
				if (i > start && nums[i] == nums[i - 1])
					continue;
				
				// use current number to reduce ksum into (k-1) sum
				List<List<Integer>> temp = kSum2(nums, i + 1, k - 1, target - nums[i]);
				// add previous results
				for (List<Integer> t : temp) {
					t.add(0, nums[i]);
				}
				res.addAll(temp);
			}
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/4sum/discuss/8653/On-average-O(n2)-and-worst-case-O(n3)-java-solution-by-reducing-4Sum-to-2Sum
	 * 
	 * Basic idea is to reduce the 4Sum problem to 2Sum one. In order to achieve that, 
	 * we can use an array (size of n^2) to store the pair sums and this array will 
	 * act as the array in 2Sum case (Here n is the size of the original 1D array and 
	 * it turned out that we do not even need to explicitly use the n^2 sized array). 
	 * We also use a hashmap to mark if a pair sum has been visited or not (the same 
	 * as in the 2Sum case). The tricky part here is that we may have multiple pairs 
	 * that result in the same pair sum. So we will use a list to group these pairs 
	 * together. For every pair with a particular sum, check if the pair sum that is 
	 * needed to get the target has been visited. If so, further check if there is 
	 * overlapping between these two pairs. If not, record the result.
	 */
	public List<List<Integer>> fourSum_2Sum_Map(int[] num, int target) {
		Arrays.sort(num);

		// for holding visited pair sums. All pairs with the same pair sum are grouped together
		Map<Integer, List<int[]>> twoSumMap = new HashMap<>();

		// for holding the results
		Set<List<Integer>> res = new HashSet<>();

		for (int i = 0; i < num.length; i++) {
			// get rid of repeated pair sums
			if (i > 1 && num[i] == num[i - 2])
				continue;

			for (int j = i + 1; j < num.length; j++) {
				// get rid of repeated pair sums
				if (j > i + 2 && num[j] == num[j - 2])
					continue;

				// for each pair sum, check if the pair sum that is needed to get
				// the target has been visited.
				if (twoSumMap.containsKey(target - (num[i] + num[j]))) {
					// if so, get all the pairs that contribute to this visited pair sum.
					List<int[]> ls = twoSumMap.get(target - (num[i] + num[j]));

					for (int[] pair : ls) {
						// we have two pairs: one is indicated as (pair[0], pair[1]), 
						// the other is (i, j).
						// we first need to check if they are overlapping with each other.
						int m1 = Math.min(pair[0], i); // m1 will always be the smallest index
						int m2 = Math.min(pair[1], j); // m2 will be one of the middle two indices
						int m3 = Math.max(pair[0], i); // m3 will be one of the middle two indices
						int m4 = Math.max(pair[1], j); // m4 will always be the largest index

						// two pairs are overlapping, so just ignore this case
						if (m1 == m3 || m1 == m4 || m2 == m3 || m2 == m4)
							continue;

						// else record the result
						res.add(Arrays.asList(num[m1], num[Math.min(m2, m3)], num[Math.max(m2, m3)], num[m4]));
					}
				}

				// mark that we have visited current pair and add it to the 
				// corresponding pair sum group. Here we've encoded the pair
				// indices i and j into an integer array of length 2.
				twoSumMap.computeIfAbsent(num[i] + num[j], key -> new ArrayList<>()).add(new int[] { i, j });
			}
		}

		return new ArrayList<List<Integer>>(res);
	}

}
