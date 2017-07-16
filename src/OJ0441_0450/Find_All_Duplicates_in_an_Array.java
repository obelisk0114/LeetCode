package OJ0441_0450;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Find_All_Duplicates_in_an_Array {
	/*
	 * https://discuss.leetcode.com/topic/64735/java-simple-solution
	 * 
	 * when find a number i, flip the number at position i-1 to negative.
	 * if the number at position i-1 is already negative, i is the number that occurs twice.
	 * 
	 * we need a way to "hash" the same number together without using extra space.
	 * if flip it to the opposite I can still recover it using Math.abs
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/64735/java-simple-solution/3
	 * https://discuss.leetcode.com/topic/64805/java-easy-to-understand-solution-without-extra-space-and-in-o-n-time
	 */
	public List<Integer> findDuplicates(int[] nums) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < nums.length; ++i) {
			int index = Math.abs(nums[i]) - 1;
			if (nums[index] < 0)
				res.add(Math.abs(index + 1));
			nums[index] = -nums[index];
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/71381/java-solution-beats-94-easy-to-understand-than-abs
	 * 
	 * Use modulo operation to retrieve original data
	 * 
	 * After increment "len", the elements that have appeared once will range from len+1 to 2len.
	 * Hence the duplicates will range from 2len+1 to 3len
	 */
	public List<Integer> findDuplicates_add(int[] nums) {
		int len = nums.length;
		for (int i = 0; i < len; i++) {
			nums[(nums[i] - 1) % len] += len;
		}
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > 2 * len) {
				res.add(i + 1);
			}
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/70485/java-o-1-space-o-n-time-solution-with-swapping
	 * 
	 * Put each element to the corresponding position, 
	 * so that a[0] = 1, a[1] = 2, a[2] = 3 ... etc. (1<=a[i]<=n).
	 */
	public List<Integer> findDuplicates_swap(int[] nums) {
		List<Integer> res = new ArrayList<>();
		if (nums == null || nums.length == 0)
			return res;
		int i = 0;
		int n = nums.length;
		while (i < n) { // traverse the array till the end
			if (nums[i] == i + 1) { // if number stays at it's supposed position, continue
				i++;
				continue;
			}
			int num = nums[i];
			if (num == -1) { // if the duplicate number in that position is already found, continue
				i++;
				continue;
			}
			if (nums[num - 1] == num) { // if current num is equals to the number at supposed position,
				res.add(num); // then it is duplicate.
				nums[i] = -1; // mark this position, in order to denote that duplicate has found
				i++;
				continue;
			}
			swap(nums, i, num - 1); // if current numbers supposed position is occupied by another number swap and consider that number
		}
		return res;
	}
	public void swap(int nums[], int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}
	
	// self
	public List<Integer> findDuplicates_self(int[] nums) {
        List<Integer> out = new ArrayList<>();
        Arrays.sort(nums);                       // Can use linear sorting
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1])
                out.add(nums[i]);
        }
        return out;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
