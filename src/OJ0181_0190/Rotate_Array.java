package OJ0181_0190;

import java.util.Scanner;

public class Rotate_Array {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/14341/easy-to-read-java-solution
	 * 
	 * Ref :
	 * https://discuss.leetcode.com/topic/41716/java-o-n-in-place-solution
	 */
	void rotate1(int[] nums, int k) {
		k %= nums.length;
		reverse1(nums, 0, nums.length - 1);
		reverse1(nums, 0, k - 1);
		reverse1(nums, k, nums.length - 1);
	}
	void reverse1(int[] nums, int start, int end) {
		while (start < end) {
			int temp = nums[start];
			nums[start] = nums[end];
			nums[end] = temp;
			start++;
			end--;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/9205/java-solution-with-o-1-space
	 * 
	 * Ref : 
	 * https://discuss.leetcode.com/topic/40050/simple-and-most-elegant-logic
	 * https://discuss.leetcode.com/topic/26365/java-o-1-space-solution-of-rotate-array
	 * https://discuss.leetcode.com/topic/18238/my-accepted-java-solution-o-n-time-and-o-1-space
	 */
	void rotate2(int[] nums, int k) {
		if (nums == null || nums.length == 0) {
			return;
		}
		k = k % nums.length;
		reverse2(nums, 0, nums.length - k - 1);
		reverse2(nums, nums.length - k, nums.length - 1);
		reverse2(nums, 0, nums.length - 1);
    }
    private void reverse2(int[] num, int left, int right) {
		while (left < right) {
			int t = num[left];
			num[left] = num[right];
			num[right] = t;
			left++;
			right--;
		}
    }
	
	// https://leetcode.com/articles/rotate-array/
	// https://discuss.leetcode.com/topic/19258/my-accepted-solution-in-java-an-easy-solution-by-doubling-the-array
	void rotate_with_extra_array(int[] nums, int k) {
        int[] a = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            a[(i + k) % nums.length] = nums[i];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = a[i];
        }
    }
	
	// https://discuss.leetcode.com/topic/32539/beats-98-of-java-soutions
	void rotate_arraycopy(int[] nums, int k) {
        int n = nums.length;
        int[] newList = new int[n];
        if (n > 1) {
            k = k % n;
            System.arraycopy(nums, n - k, newList, 0, k);
            System.arraycopy(nums, 0, newList, k, n - k);
            System.arraycopy(newList, 0, nums, 0, n);
        }
    }
	
	/*
	 * https://discuss.leetcode.com/topic/24283/a-7-line-time-o-n-in-place-solution-no-reversing/
	 * 
	 * Rf: https://leetcode.com/articles/rotate-array/
	 */
	void rotate_Cyclic_Replacements(int[] nums, int k) {
		if(nums.length == 0 || k % nums.length == 0) return;
	    int start = 0, i = start, curNum = nums[i], count = 0;
	    while(count < nums.length){
	        i = (i + k) % nums.length;
	        int tmp = nums[i];
	        nums[i] = curNum;
	        if(i == start){
	            start++;
	            i = start;
	            curNum = nums[i];
	        }
	        else curNum = tmp;
	        count++;
	    }
    }
	
	/*
	 * [1,2,3,4,5,6,7] k = 3; put n-k=4 elements to the final places at the end. 
	 * Will have to start from the last element. [7, 5, 6, 1, 2, 3, 4]
	 * 
	 * the # of out-of-order elements being put to the beginning of the array are 
	 * (range % k)
	 * https://discuss.leetcode.com/topic/9652/java-solution-in-one-pass-o-1-space-o-n-time
	 * 
	 * Rf : https://discuss.leetcode.com/topic/11349/my-three-way-to-solve-this-problem-the-first-way-is-interesting-java
	 */
	void rotate_replace2(int[] nums, int k) {
		if (nums.length == 0) return;
	    int n = nums.length;
	    while ((k %= n) > 0 && n > 1) {
	        int range = n - k;
	        for (int i = 1; i <= range; i++) {
	            int val = nums[n - i];
	            nums[n - i] = nums[n - i - k];
	            nums[n - i - k] = val;
	        }
	        n = k;
	        k = n - (range % k);
	    }
	}
	
	private static int[] rotate_my_code(int[] arr, int step) {
		int length = arr.length;
		int res = step % length;
		int order = length - res;
		int k = 0;
		int[] result = new int[length];
		for (int i = 0; i < length; i++) {
			if (order < length) {
				result[i] = arr[order];
				order++;
			}
			else {
				result[i] = arr[k];
				k++;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Input the total amount of elements.");
		int total = keyboard.nextInt();
		System.out.println("Enter the rotation steps.");
		int step = keyboard.nextInt();
		System.out.println("Enter an array of elements: ");
        int[] line = new int[total];
        for (int i = 0; i < total; i++) {
        	line[i] = keyboard.nextInt();
        }
        int[] ans = rotate_my_code(line, step);
        String out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("Answer : " + out);
        
        keyboard.close();

	}

}
