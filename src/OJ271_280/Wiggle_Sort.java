package OJ271_280;

import java.util.Arrays;

public class Wiggle_Sort {
	private void wiggleSort_sort(int[] nums) {
		Arrays.sort(nums);

		for (int i = 0; i < nums.length - 1; i++) {
			if (i % 2 == 1) {
				int temp = nums[i];
				nums[i] = nums[i + 1];
				nums[i + 1] = temp;
			}
		}
	}
	
	private void wiggleSort(int[] nums) {
	    boolean less = true;
	    for (int i = 0; i < nums.length - 1; i++) {
	        if (less) {
	            if (nums[i] > nums[i + 1]) {
	                swap(nums, i, i + 1);
	            }
	        } else {
	            if (nums[i] < nums[i + 1]) {
	                swap(nums, i, i + 1);
	            }
	        }
	        less = !less;
	    }
	}
	
	private void wiggleSort_modify(int[] nums) {
	    for (int i = 0; i < nums.length - 1; i++) {
	        if (((i % 2 == 0) && nums[i] > nums[i + 1])
	                || ((i % 2 == 1) && nums[i] < nums[i + 1])) {
	            swap(nums, i, i + 1);
	        }
	    }
	}
	
	private void wiggleSort_modify_enhance(int[] nums) {
	    for (int i = 0; i < nums.length - 1; i++) {
	        if ((i % 2 == 0) == (nums[i] > nums[i + 1])) {
	            swap(nums, i, i + 1);
	        }
	    }
	}
	
	private void swap(int[] arr, int a, int b) {
		int tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	
	public boolean verify(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			if (i % 2 == 0) {
				if (arr[i] > arr[i + 1]) {
					return false;
				}
			}
			else {
				if (arr[i] < arr[i + 1]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Wiggle_Sort wigglesort = new Wiggle_Sort();
		int[] arr = {1,2,2,1,2,1,1,1,1,3,2,2};
		wigglesort.wiggleSort_sort(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
		System.out.println(wigglesort.verify(arr));
		
		int[] arr2 = {1,2,2,1,2,1,1,1,1,3,2,2};
		wigglesort.wiggleSort(arr2);
		for (int i = 0; i < arr2.length; i++) {
			System.out.print(arr2[i] + " ");
		}
		System.out.println();
		System.out.println(wigglesort.verify(arr2));
		
		int[] arr3 = {1,2,2,1,2,1,1,1,1,3,2,2};
		wigglesort.wiggleSort_modify(arr3);
		for (int i = 0; i < arr3.length; i++) {
			System.out.print(arr3[i] + " ");
		}
		System.out.println();
		System.out.println(wigglesort.verify(arr3));
		
		int[] arr4 = {1,2,2,1,2,1,1,1,1,3,2,2};
		wigglesort.wiggleSort_modify_enhance(arr4);
		for (int i = 0; i < arr4.length; i++) {
			System.out.print(arr4[i] + " ");
		}
		System.out.println();
		System.out.println(wigglesort.verify(arr4));
	}

}
