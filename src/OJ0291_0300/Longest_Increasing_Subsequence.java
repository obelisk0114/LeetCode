package OJ0291_0300;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/LongestIncreasingSubsequence.html
 * http://www.csie.ntnu.edu.tw/~u91029/LongestCommonSubsequence.html
 * https://leetcode.com/articles/longest-increasing-subsequence/
 * 
 * LIS 轉 LCS：令原序列 A 排序後會變成 B。 A 和 B 的 LCS，就是 A 的 LIS。
 * LCS 轉 LIS：將序列 A 和 B 當中的相同字母配對都找出來，呈現成索引值數對，再以特殊規則排序，最後找 LIS，就是 A 和 B 的 LCS。
 * 
 * Lazy to read
 * https://discuss.leetcode.com/topic/51345/lis-general-template-that-can-record-all-the-lis-fight-for-dream
 */

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Arrays;

public class Longest_Increasing_Subsequence {
	
	/*
	 * For the element nums[j], we determine its correct position in the dp array
	 * by making use of Binary Search (which can be used since the dp array is 
	 * storing increasing subsequence) and also insert it at the correct position. 
	 * Thus, only the elements upto the ith index in the dp array can determine the 
	 * position of the current element in it. Whenever this position index i becomes 
	 * equal to the length of the LIS formed so far(len), update as "len = len + 1"
	 * 
	 * Note: dp array does not result in longest increasing subsequence, 
	 * but length of dp array will give you length of LIS.
	 * 
	 * Note: Arrays.binarySearch() method returns index of the search key, 
	 * if it is contained in the array, else it returns (-(insertion point) - 1). 
	 * The insertion point is the point at which the key would be inserted into the 
	 * array: the index of the first element greater than the key, 
	 * or a.length if all elements in the array are less than the specified key.
	 * 
	 * https://discuss.leetcode.com/topic/28719/short-java-solution-using-dp-o-n-log-n
	 * 
	 * Keep track of the minimum value a subsequence of given length might end with, 
	 * for all so far possible subsequence lengths. So dp[i] is the minimum value a 
	 * subsequence of length i+1 might end with.
	 * 
	 * https://discuss.leetcode.com/topic/35194/share-my-8-line-o-nlgn-java-code-with-comments-in-mandarin
	 */
	int lengthOfLIS_binarySearch(int[] nums) {
		// Store the increasing subsequence formed by including the current element
        int[] dp = new int[nums.length];
        int len = 0;
        for (int num : nums) {
            int i = Arrays.binarySearch(dp, 0, len, num);
            if (i < 0) {
                i = -(i + 1);
            }
            dp[i] = num;
            if (i == len) {
                len++;
            }
        }
        return len;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/31730/java-easy-version-to-understand
	 * 
	 * We implement binary search functions and use it.
	 */
	int findPositionToReplace(int[] a, int low, int high, int x) {
		int mid;
		while (low <= high) {            // low + 1 = mid; mid + 1 = high; a[mid] > x
			mid = low + (high - low) / 2;
			if (a[mid] == x)
				return mid;
			else if (a[mid] > x)
				high = mid - 1;
			else
				low = mid + 1;
		}
		return low;
	}

	int lengthOfLIS_doBinarySearch(int[] nums) {
		if (nums == null | nums.length == 0)
			return 0;
		int n = nums.length, len = 0;
		int[] increasingSequence = new int[n];
		increasingSequence[len++] = nums[0];
		for (int i = 1; i < n; i++) {
			if (nums[i] > increasingSequence[len - 1])
				increasingSequence[len++] = nums[i];
			else {
				int position = findPositionToReplace(increasingSequence, 0, len - 1, nums[i]);
				increasingSequence[position] = nums[i];
			}
		}
		return len;
	}
	
	/*
	 * https://www.youtube.com/watch?v=S9oUiVYEq7E
	 */
	public int[] LIS_storeMinValues_trace(int[] nums) {
        if (nums.length <= 1) { return nums; }
        
        int len = 0;
        int[] minLastValue = new int[nums.length];
        minLastValue[0] = 0;
        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
        	result[i] = -1;
        }

        // for each number in the main list...
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > nums[minLastValue[len]]) {
				len++;
				minLastValue[len] = i;
				result[i] = minLastValue[len - 1];
			}
			else {
				for (int j = 0; j <= len; j++) {
					if (nums[i] <= nums[minLastValue[j]]) {
						minLastValue[j] = i;
						if (j != 0) {
							result[i] = minLastValue[j - 1];	
						}
						break;
					}
				}
			}
		}
        
		len++;
		int[] res = new int[len];
		res[len - 1] = nums[minLastValue[len - 1]];
		int count = minLastValue[len - 1];
		for (int i = 1; i < len; i++) {
			if (result[count] == -1) {
				break;
			}
			res[len - 1 - i] = nums[result[count]];
			count = result[count];
		}
		System.out.println("length = " + len);
        return res;
    }
	
	// https://discuss.leetcode.com/topic/30721/my-easy-to-understand-o-n-2-solution-using-dp-with-video-explanation
	// dp[i] = max(dp[j]) + 1, 0 <= j < i ; LIS_length = max(dp[i]), 0 <= i < n
	int lengthOfLIS_article_DP(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int maxans = 1;
        for (int i = 1; i < dp.length; i++) {
            int maxval = 0;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {      // ith element must be bigger than jth
                    maxval = Math.max(maxval, dp[j]);
                }
            }
            dp[i] = maxval + 1;
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/55220/dp-java-solution-in-1ms-without-using-binary-search-in-10-lines-of-code-with-explanation
	 * 
	 * The only number that matters for a given sequence length (1 in this case) 
	 * is the smallest number at that sequence length.
	 * These minimum values for each sequence length are stored using the array.
	 */
	public int lengthOfLIS_storeMinValues_SequenceLength(int[] nums) {
        if (nums.length == 0) { return 0; }
        
        int[] mapSequenceLengthToMinTail = new int[nums.length + 1];
        int maxLength = 1;
        mapSequenceLengthToMinTail[1] = nums[0];

        // for each number in the main list...
		for (int i = 1; i < nums.length; i++) {
			// for each sequence length, starting with the biggest so far...
			for (int j = maxLength; j >= 0; j--) {
				if (mapSequenceLengthToMinTail[j] < nums[i]) {
					mapSequenceLengthToMinTail[j + 1] = nums[i];
					maxLength = Math.max(maxLength, j + 1);
					break;
				}
			}
		}
        
		System.out.println("Last element in each sequence length (including 0)");
        for (int k = 0; k <= maxLength; k++) {
        	System.out.print(mapSequenceLengthToMinTail[k] + " ");
        }
        
        return maxLength;
    }
	
	void LIS_csie_ntnu(int[] s) {
		int length[] = new int[s.length];     // 記錄 ith 長度
		int prev[] = new int[s.length];       // 記錄 ith 位置
		// 初始化。每一個數字本身就是長度為一的 LIS。
		for (int i = 0; i < s.length; i++)
			length[i] = 1;
		// -1 代表 s[i] 是開頭數字，沒有接在其他數字後面。
		for (int i = 0; i < s.length; i++)
			prev[i] = -1;
		
		for (int i = 0; i < s.length; i++)
			// 找出 s[i] 後面能接上哪些數字，
			// 若是可以接，長度就增加。
			for (int j = i + 1; j < s.length; j++)
				if (s[i] < s[j]) {
					if (length[i] + 1 > length[j]) {
						length[j] = length[i] + 1;
						// s[j] 接在 s[i] 後面
						prev[j] = i;
					}
					//length[j] = Math.max(length[j], length[i] + 1);
				}

		// length[] 之中最大的值即為 LIS 的長度。
		int n = 0;
		int pos = 0;
		for (int i = 0; i < s.length; i++) {
			if (length[i] > n) {
				n = length[i];
				pos = i;
			}
		}
		System.out.println("演算法筆記。LIS的長度是 : " + n);
		System.out.println("遞迴版本 : ");
		trace(pos, prev, s); // 印出一個LIS
		System.out.println("\n迴圈版本，但是順序會顛倒 : ");
		trace2(pos, prev, s);
	}
	
	// 遞迴版本
	void trace(int i, int[] prev, int[] s)
	{
	    if (prev[i] != -1) trace(prev[i], prev, s);
	    System.out.print(s[i] + " ");
	}
	 
	// 迴圈版本，但是順序會顛倒。
	void trace2(int i, int[] prev, int[] s)
	{
	    for (; i != -1; i = prev[i])
	    	System.out.print(s[i] + " ");
	}
	
	/*
	 * https://discuss.leetcode.com/topic/44440/java-short-nlogn-treeset-solution
	 */
	public int lengthOfLIS_TreeSet (int[] nums) {
	    TreeSet<Integer> set = new TreeSet<>();
	    for(int i : nums) {
	        Integer ceil = set.ceiling(i);
	        if(null != ceil) {
	            set.remove(ceil);
	        }
	        set.add(i);
	    }
	    
	    System.out.println("TreeSet contains : ");
	    Iterator<Integer> iterator = set.iterator(); 
        while(iterator.hasNext()){ 
                System.out.print(iterator.next() + " "); 
        }
	    return set.size();
	}
	
	/*** Store all elements ***/
	public static Integer[] findLong(int[] arr, int n) {
		ArrayList<ArrayList<Integer>> ans = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < n; i++) {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(arr[i]);
			ans.add(tmp);
		}
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (arr[i] > arr[j] && ans.get(j).size() + 1 > ans.get(i).size()) {
					ArrayList<Integer> temp = new ArrayList<Integer>();
					for (int element : ans.get(j)) {
						temp.add(element);
					}
					temp.add(arr[i]);
					ans.set(i, temp);
					System.out.print("i = " + i + " ; j = " + j + " : ");
					for (int ele : ans.get(i)) {
						System.out.print(ele + ", ");
					}
					System.out.println();
				}
			}
		}
		int k = 0;
		for (int i = 0; i < n; i++) {
			if (ans.get(k).size() <= ans.get(i).size()) {
				k = i;
			}
		}
		Integer[] res = ans.get(k).toArray(new Integer[ans.get(k).size()]); 
		return res;
	}
	
	// Ref: The following link. We are only interested in the minimum values in the end.
	// https://discuss.leetcode.com/topic/55220/dp-java-solution-in-1ms-without-using-binary-search-in-10-lines-of-code-with-explanation/2
	public int[] LIS_storeMinValues_SequenceLength(int[] nums) {
		if (nums.length <= 1) {
			return nums;
		}

		ArrayList<ArrayList<Integer>> sequence = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> ini = new ArrayList<Integer>();
		ini.add(Integer.MIN_VALUE);
		sequence.add(ini);
		int maxLength = 1;
		for (int i = 1; i < nums.length + 1; i++) {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(nums[0]);
			sequence.add(tmp);
		}

		// for each number in the main list...
		for (int i = 1; i < nums.length; i++) {
			// for each sequence length, starting with the biggest so far...
			int potential = sequence.get(maxLength).size() - 1;
			if (sequence.get(maxLength).get(potential) < nums[i]) {
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				for (Integer element : sequence.get(maxLength)) {
					tmp.add(element);
				}
				tmp.add(nums[i]);
				sequence.set(++maxLength, tmp);
				continue;
			}
			for (int j = maxLength - 1; j >= 0; j--) {
				int last = sequence.get(j).size() - 1;
				int nextLast = sequence.get(j + 1).size() - 1;
				if (sequence.get(j).get(last) < nums[i]) {
					sequence.get(j + 1).set(nextLast, nums[i]);
					break;
				}
			}
		}

		int[] res = new int[maxLength];
		for (int i = 0; i < maxLength; i++) {
			res[i] = sequence.get(maxLength).get(i);
		}
		return res;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Longest_Increasing_Subsequence lis = new Longest_Increasing_Subsequence();
		//int[] a = {13, 36, 40, 41, 19, 43, 37, 10};
		//int[] a = {3, 5, 6, 2, 5, 4, 19, 5, 6, 7, 12};
		//int[] a = {10, 9, 2, 5, 3, 7, 101, 18};
		//int[] a = {10, 9, 2, 5, 3, 7, 1, 101, 18};
		int[] a = {-7, 10, 9, 2, 3, 8, 8, 1};
		//int[] a = {-2, -1};
		//int[] a = {3, 4, -1, 5, 8, 2, 3, 12, 7, 9, 10};
		//int[] a = {10, 9, 2, 5, 3, 4};
		
		System.out.println("findLong ; 找出 s[i] 能接在哪些數字後面");
		Integer[] gg = findLong(a, a.length);
		for (int i = 0; i < gg.length; i++) {
			System.out.print(gg[i] + " ");			
		}
		System.out.println();
		System.out.println("LIS_storeMinValues_SequenceLength");
		int[] gg2 = lis.LIS_storeMinValues_SequenceLength(a);
		for (int i = 0; i < gg2.length; i++) {
			System.out.print(gg2[i] + " ");			
		}
		System.out.println();
		System.out.println("India youtube : ");
		int[] gg3 = lis.LIS_storeMinValues_trace(a);
		for (int i = 0; i < gg3.length; i++) {
			System.out.print(gg3[i] + " ");			
		}
		System.out.println();
		System.out.println("lengthOfLIS_binarySearch : " + lis.lengthOfLIS_binarySearch(a));
		System.out.println("lengthOfLIS_doBinarySearch : " + lis.lengthOfLIS_doBinarySearch(a));
		System.out.println("lengthOfLIS_article_DP : " + lis.lengthOfLIS_article_DP(a));
		lis.LIS_csie_ntnu(a);
		System.out.println();
		System.out.println("\nLength : " + lis.lengthOfLIS_storeMinValues_SequenceLength(a));
		System.out.println("\nLength : " + lis.lengthOfLIS_TreeSet(a));

	}

}
