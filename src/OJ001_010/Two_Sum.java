package OJ001_010;

/*
 * https://leetcode.com/articles/two-sum/
 */

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Two_Sum {
	// Two-pass Hash Table
	private int[] twoSum_TwoPass(int[] nums, int target) {
	    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	    for (int i = 0; i < nums.length; i++) {
	        map.put(nums[i], i);
	    }
	    for (int i = 0; i < nums.length; i++) {
	        int complement = target - nums[i];
	        if (map.containsKey(complement) && map.get(complement) != i) {
	            return new int[] { i, map.get(complement) };
	        }
	    }
	    throw new IllegalArgumentException("No two sum solution");
	}
	
	// One-pass Hash Table
	private int[] twoSum_OnePass(int[] nums, int target) {
	    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	    for (int i = 0; i < nums.length; i++) {
	        int complement = target - nums[i];
	        if (map.containsKey(complement)) {
	            return new int[] { map.get(complement), i };
	        }
	        map.put(nums[i], i);
	    }
	    throw new IllegalArgumentException("No two sum solution");
	}
	
	// https://discuss.leetcode.com/topic/44971/java-o-nlogn-beats-98-85/2
	private int[] twoSum_n2(int[] nums, int target) {
		if (nums == null)
			return null;
		int[] nums2 = Arrays.copyOf(nums, nums.length);
		Arrays.sort(nums2);
		int a = 0, b = 0;
		int start = 0, end = nums2.length - 1;
		// find two nums
		while (start < end) {
			int sum = nums2[start] + nums2[end];
			if (sum < target)
				start++;
			else if (sum > target)
				end--;
			else {
				a = nums2[start];
				b = nums2[end];
				break;
			}
		}
		// find the index of two numbers
		int[] res = new int[2];
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == a) {
				res[0] = i;
				break;
			}
		}
		if (a != b) {
			for (int i = 0; i < nums.length; i++) {
				if (nums[i] == b) {
					res[1] = i;
					break;
				}
			}
		} else {
			for (int i = 0; i < nums.length; i++) {
				if (nums[i] == b && i != res[0]) {
					res[1] = i;
					break;
				}
			}
		}

		return res;
	}
	
	/*
	 *  Define a new class to store the pair of original position and content.
	 *  Use comparator to compare. It's slower than twoSum_n2
	 */
	private int[] twoSum_n2_modify(int[] nums, int target) {
		if (nums == null)
			return null;
		List<twoSumArray> numsList = new ArrayList<twoSumArray>();
		for (int i = 0; i < nums.length; i++) {
			numsList.add(new twoSumArray(i, nums[i]));
		}
		numsList.sort(Comparator.<twoSumArray, Integer>comparing(p -> p.content)
				.thenComparing(p -> p.position));
		int start = 0, end = nums.length - 1;
		int[] res = new int[2];
		// find two nums
		while (start < end) {
			int sum = numsList.get(start).content + numsList.get(end).content;
			if (sum < target)
				start++;
			else if (sum > target)
				end--;
			else {
				res[0] = numsList.get(start).position;
				res[1] = numsList.get(end).position;
				break;
			}
		}

		return res;
	}
	
	private class twoSumArray {
		private int position;
		private int content;
		
		public twoSumArray (int position, int content) {
			this.position = position;
			this.content = content;
		}		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Two_Sum twosum = new Two_Sum();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Input the total amount of numbers.");
		int total = keyboard.nextInt();
		System.out.println("Enter an array of integers: ");
        int[] line = new int[total];
        for (int i = 0; i < total; i++) {
        	line[i] = keyboard.nextInt();
        }
        System.out.println("Enter the sum : ");
        int sum = keyboard.nextInt();
        int[] ans = twosum.twoSum_TwoPass(line, sum);
        String out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("Two-pass Hash Table Answer : " + out);
        
        ans = twosum.twoSum_OnePass(line, sum);
        out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("One-pass Hash Table Answer : " + out);
        
        ans = twosum.twoSum_n2(line, sum);
        out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("twoSum_n2 Answer : " + out);
        
        ans = twosum.twoSum_n2_modify(line, sum);
        out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("twoSum_n2_modify Answer : " + out);
        
        keyboard.close();

	}

}
