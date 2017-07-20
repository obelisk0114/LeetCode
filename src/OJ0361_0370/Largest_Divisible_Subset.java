package OJ0361_0370;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class Largest_Divisible_Subset {
	/*
	 * https://discuss.leetcode.com/topic/49652/classic-dp-solution-similar-to-lis-o-n-2
	 * 
	   For an increasingly sorted array of integers a[1 .. n]
       T[n] = the length of the largest divisible subset whose largest number is a[n]
       T[n+1] = max { 1 + T[i] } if a[n+1] mod a[i] == 0 ; else 1
       
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/49456/c-solution-with-explanations 
	 * https://discuss.leetcode.com/topic/49467/concise-java-solution-o-n-2-time-o-n-space
	 */
	public List<Integer> largestDivisibleSubset(int[] nums) {
		int n = nums.length;
		int[] count = new int[n];
		int[] pre = new int[n];
		Arrays.sort(nums);
		int max = 0, index = -1;
		for (int i = 0; i < n; i++) {
			count[i] = 1;
			pre[i] = -1;
			for (int j = i - 1; j >= 0; j--) {      // for (int j = 0; j < i; j++) 
				if (nums[i] % nums[j] == 0) {
					if (1 + count[j] > count[i]) {
						count[i] = count[j] + 1;
						pre[i] = j;
					}
				}
			}
			if (count[i] > max) {
				max = count[i];
				index = i;
			}
		}
		
		List<Integer> res = new ArrayList<>();
		while (index != -1) {
			res.add(nums[index]);
			index = pre[index];
		}
		return res;
	}
	
	// https://discuss.leetcode.com/topic/49741/easy-understood-java-dp-solution-in-28ms-with-o-n-2-time
	
	/*
	 * https://discuss.leetcode.com/topic/54071/java-20ms-beats-99-28
	 * 
	 * Process nums increasingly.
     * lens[i] denotes the max length of the subset constructed so far containing nums[i].
     * If nums[j] (j > i) is multiple of nums[i], then lens[j] = max(lens[j], lens[i] + 1)
	 */
	public List<Integer> largestDivisibleSubset_binarySearch(int[] nums) {
		int n = nums.length, maxIdx = 0;
		List<Integer> ans = new LinkedList<>();
		if (n == 0)
			return ans;
		Arrays.sort(nums);
		int[] lens = new int[n], prevs = new int[n];
		Arrays.fill(prevs, -1);
		for (int i = 0; nums[i] <= nums[n - 1] / 2; ++i) {
			for (int j = i + 1, f = 2; nums[i] <= nums[n - 1] / f; f = (nums[j] + nums[i] - 1) / nums[i]) {
				int idx = Arrays.binarySearch(nums, j, n, f * nums[i]);
				if (idx > 0 && lens[idx] <= lens[i]) {
					prevs[idx] = i;
					lens[idx] = lens[i] + 1;
					if (lens[idx] > lens[maxIdx])
						maxIdx = idx;
				}
				j = idx >= 0 ? idx + 1 : -(idx + 1);
				if (j >= n)
					break;
			}
		}
		for (int i = maxIdx; i >= 0; i = prevs[i])
			ans.add(0, nums[i]);
		return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/49702/java-dfs-solution-86-ms-with-explanation
	 * 
	 * Rf : https://discuss.leetcode.com/topic/49650/java-dfs-solution-62ms-by-considering-the-problem-as-finding-the-longest-path-in-a-dag
	 */
	public List<Integer> largestDivisibleSubset_DFS(int[] nums) {
		if (nums == null || nums.length == 0) {
			return Collections.<Integer>emptyList();
		}
		if (nums.length == 1) {
			List<Integer> ls = new ArrayList<Integer>(1);
			ls.add(nums[0]);
			return ls;
		}
		Arrays.sort(nums);

		HashMap<Integer, List<Integer>> mp = new HashMap<Integer, List<Integer>>();
		List<Integer> maxSubset = null;
		for (int i = 0; i < nums.length; i++) {
			List<Integer> ls = null;
			if (!mp.containsKey(i)) {
				ls = dfs(i, nums, mp);
			} 
			else {
				ls = mp.get(i);
			}

			if (maxSubset == null || ls.size() > maxSubset.size()) {
				maxSubset = ls;
			}
		}
		return maxSubset;
	}
	private List<Integer> dfs(int idx, int[] arr, HashMap<Integer, List<Integer>> mp) {
		if (mp.containsKey(idx)) {
			return mp.get(idx);
		}
		List<Integer> ls = new ArrayList<Integer>();

		for (int i = idx + 1; i < arr.length; i++) {
			if ((arr[i] % arr[idx]) == 0) {
				List<Integer> r = dfs(i, arr, mp);
				if (r.size() > ls.size()) {
					ls = r;
				}
			}
		}

		ls = new ArrayList<Integer>(ls);
		ls.add(0, arr[idx]);
		mp.put(idx, ls);
		return ls;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Largest_Divisible_Subset largestSubset = new Largest_Divisible_Subset();
		//int[] nums = { 2, 3, 8, 9, 27 };
		int[] nums = { 1, 3, 9, 18, 90, 180, 360, 720, 54, 108, 540 }; // [1,3,9,18,90,180,360,720]
		//List<Integer> ans = largestSubset.largestDivisibleSubset(nums);
		List<Integer> ans = largestSubset.largestDivisibleSubset_DFS(nums);
		for (Integer i : ans) {
			System.out.print(i + " ");
		}

	}

}
