package OJ0211_0220;

/*
 * https://www.quora.com/What-is-the-most-efficient-algorithm-to-find-the-kth-smallest-element-in-an-array-having-n-unordered-elements
 */

import java.util.Arrays;

public class Kth_Largest_Element_in_an_Array {
	private void swap(int[] arr, int a, int b) {
		int tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	
	// Choose last one as pivot
	private int findKthLargest(int[] nums, int k) {
		int pivot = (int) (Math.random() * nums.length);
		swap(nums, nums.length - 1, pivot);
		pivot = nums[nums.length - 1];
		int j = -1;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] > pivot) {
				j++;
				swap(nums, i, j);
			}
		}
		j++;
		swap(nums, j, nums.length - 1);
		if (j + 1 == k) {
			return nums[j];
		}
		else if (j + 1 > k) {
			int[] subnums = Arrays.copyOfRange(nums, 0, j);
			return findKthLargest(subnums, k);
		}
		else {
			int[] subnums = Arrays.copyOfRange(nums, j + 1, nums.length);
			return findKthLargest(subnums, k - j - 1);
		}
	}
	
	// Choose first one as pivot
	private int findKthLargest2(int[] nums, int k) {
		int pivot = (int) (Math.random() * nums.length);
		System.out.println("pivot ¶Ã¼Æ : " + pivot);
		System.out.println("length : " + nums.length);
		swap(nums, 0, pivot);
		pivot = nums[0];
		int j = 0;
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > pivot) {
				j++;
				swap(nums, i, j);
			}
		}
		//j++;
		swap(nums, j, 0);
		
		System.out.println("j = " + j);
		System.out.println("k = " + k);
		for (int jj = 0; jj < nums.length; jj++) {
			System.out.print(nums[jj] + " ");
		}
		System.out.println();
		
		if (j + 1 == k) {
			System.out.println("Ans = " + nums[j]);
			return nums[j];
		}
		else if (j + 1 > k) {
			int[] subnums = Arrays.copyOfRange(nums, 0, j);
			return findKthLargest(subnums, k);
		}
		else {
			int[] subnums = Arrays.copyOfRange(nums, j + 1, nums.length); 
			return findKthLargest(subnums, k-j-1);
		}
		
	}
	
	/*
	 * The following 2 functions are randomized partition. The original method
	 * Selection in expected linear time
	 */
	private int find_Kth_Largest(int[] nums, int k) {
		int end = nums.length - 1;
		return find_Kth_Partition(nums, 0, end, k);
	}
	private int find_Kth_Partition(int[] nums, int start, int end, int k) {
		if (start == end) {
			return nums[start];
		}
		
		int length = end - start + 1;
		int pivot = (int) (Math.random() * length) + start;
		swap(nums, end, pivot);
		pivot = nums[end];
		int j = start - 1;
		for (int i = start; i < end; i++) {
			if (nums[i] > pivot) {
				j++;
				swap(nums, i, j);
			}
		}
		j++;
		swap(nums, j, end);
		
		int index = j - start + 1;      // We need to find the index of the subarray.		
		if (index == k) {
			return nums[j];
		}
		else if (index > k) {
			return find_Kth_Partition(nums, start, j - 1, k);
		}
		else {
			return find_Kth_Partition(nums, j + 1, end, k - index);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kth_Largest_Element_in_an_Array kth = new Kth_Largest_Element_in_an_Array();
		int[] a = {5,1,7,6,3,9,2,4,8};
		//int[] a = {7,6,5,4,3,2,1};
		//int[] a = {3,3,3,3,3,3,3,3,3};
		int b = 8;
		System.out.println("pivot = Last ; " + kth.findKthLargest(a, b));
		System.out.println("pivot = First ; " + kth.findKthLargest2(a, b));
		System.out.println("Expected linear ; " + kth.find_Kth_Largest(a, b));
	}

}
