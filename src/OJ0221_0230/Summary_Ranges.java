package OJ0221_0230;

import java.util.List;
import java.util.ArrayList;

public class Summary_Ranges {
	// https://discuss.leetcode.com/topic/17151/accepted-java-solution-easy-to-understand
	public List<String> summaryRanges(int[] nums) {
		List<String> list = new ArrayList<>();
		if (nums.length == 1) {
			list.add(nums[0] + "");
			return list;
		}
		
		for (int i = 0; i < nums.length; i++) {
			int a = nums[i];
			while (i + 1 < nums.length && (nums[i + 1] - nums[i]) == 1) {
				i++;
			}
			if (a != nums[i]) {
				list.add(a + "->" + nums[i]);
			} 
			else {
				list.add(a + "");
			}
		}
		return list;
	}
	
	// https://discuss.leetcode.com/topic/29766/easy-java-solution-9-lines
	public List<String> summaryRanges2(int[] nums) {
		List<String> list = new ArrayList<>();
		int n = nums.length;
		for (int i = 0, j = 1; j <= n; j++) {
			if (j == n || nums[j] > nums[j - 1] + 1) {
				list.add(Integer.toString(nums[i]) + 
						(i == j - 1 ? "" : "->" + Integer.toString(nums[j - 1])));
				i = j;
			}
		}
		return list;
	}
	
	// by myself
	public List<String> summaryRanges_self(int[] nums) {
        List<String> ans = new ArrayList<>();
        if (nums.length == 0)
            return ans;
        
        int start = nums[0];
        int end = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            if (end + 1 != nums[i]) {
                String s = Integer.toString(start);
                if (start != end) {
                    s = s + "->" + end;
                }
                ans.add(s);
                start = nums[i];
                end = nums[i];
            }
            else {
                end = nums[i];
            }
        }
        
        String ss = Integer.toString(start);
        if (start != end) {
            ss = ss + "->" + end;
        }
        ans.add(ss);
        return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/37436/my-java-0ms-not-always-luckily-you-are-here-your-runtime-beats-97-90-of-java-submissions
	 */
	public List<String> summaryRanges_binarySearch(int[] nums) {
		List<String> list = new ArrayList<>();
		for (int i = 0, len = nums.length, k; i < len; i = k + 1) {
			k = help_binarySearch(nums, i, len);
			if (i != k)
				list.add("" + nums[i] + "->" + nums[k]);
			else
				list.add("" + nums[i]);
		}
		return list;
	}
	private int help_binarySearch(int[] nums, int l, int r) {
		while (l + 1 < r) {
			int m = (l + r) / 2;
			if (nums[m] - nums[l] == m - l)
				l = m;
			else
				r = m;
		}
		return l;
	}
	
	// https://discuss.leetcode.com/topic/17103/using-binary-search-but-worst-case-o-n

}
