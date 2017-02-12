package OJ001_010;

/*
 * https://leetcode.com/articles/two-sum/
 */

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Two_Sum {
	// Two-pass Hash Table
	private static int[] twoSum_TwoPass(int[] nums, int target) {
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
	private static int[] twoSum_OnePass(int[] nums, int target) {
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
        int[] ans = twoSum_TwoPass(line, sum);
        String out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("Two-pass Hash Table Answer : " + out);
        ans = twoSum_OnePass(line, sum);
        out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("One-pass Hash Table Answer : " + out);
        
        keyboard.close();

	}

}
