package OJ0441_0450;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Find_All_Numbers_Disappeared_in_an_Array {
	/*
	 * https://discuss.leetcode.com/topic/65738/java-accepted-simple-solution
	 * 
	 * All the numbers that we have seen will be marked as negative.
	 */
	public List<Integer> findDisappearedNumbers(int[] nums) {
		List<Integer> ret = new ArrayList<Integer>();

		for (int i = 0; i < nums.length; i++) {
			int val = Math.abs(nums[i]) - 1;
			if (nums[val] > 0) {
				nums[val] = -nums[val];
			}
		}

		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > 0) {
				ret.add(i + 1);
			}
		}
		return ret;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/66581/simple-java-in-place-sort-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/72969/java-two-solutions-with-o-n-time-and-o-1-space
	 */
	public List<Integer> findDisappearedNumbers_swap(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			while (nums[i] != i + 1 && nums[i] != nums[nums[i] - 1]) {
				int tmp = nums[i];
				nums[i] = nums[tmp - 1];
				nums[tmp - 1] = tmp;
			}
		}
		List<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				res.add(i + 1);
			}
		}
		return res;
	}
	
	/*
	 * Self
	 * Can use bucket sort to attain O(n) time
	 */
	public List<Integer> findDisappearedNumbers_self(int[] nums) {
        List<Integer> out = new ArrayList<Integer>();
        if (nums.length == 0)
            return out;
        Arrays.sort(nums);        // Can use linear sorting
        int i = 1;
        while (nums[0] != i) {
            out.add(i);
            i++;
        }
        for (int j = 1; j < nums.length; j++) {
            while (nums[j] != i + 1 && nums[j] > i) {
                out.add(i + 1);
                i++;
            }
            i = nums[j];
        }
        while (nums.length >= i + 1) {
            out.add(i + 1);
            i++;
        }
        return out;
    }
	
	public static void main(String[] args) {
		Find_All_Numbers_Disappeared_in_an_Array findDisappear = new Find_All_Numbers_Disappeared_in_an_Array();
		//int[] a = {4, 5, 6, 4, 5, 5};
		//int[] a = {1, 1, 2, 2};
		//int[] a = {1, 2, 2, 2, 2, 3, 4};
		int[] a = {5, 5, 6, 6, 5, 5, 6, 6, 5, 6};
		List<Integer> ans = findDisappear.findDisappearedNumbers(a);
		for (int i : ans) {
			System.out.print(i + " ");
		}
//		List<Integer> ans2 = findDisappear.findDisappearedNumbers_swap(a);
//		for (int i : ans2) {
//			System.out.print(i + " ");
//		}
//		List<Integer> ans3 = findDisappear.findDisappearedNumbers_self(a);
//		for (int i : ans3) {
//			System.out.print(i + " ");
//		}
	}

}
