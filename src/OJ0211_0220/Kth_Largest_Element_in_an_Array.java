package OJ0211_0220;

/*
 * https://www.quora.com/What-is-the-most-efficient-algorithm-to-find-the-kth-smallest-element-in-an-array-having-n-unordered-elements
 */

import java.util.Arrays;

public class Kth_Largest_Element_in_an_Array {
	// The following 2 functions are by myself.
	public int findKthLargest_self(int[] nums, int k) {
        int end = nums.length - 1;
        return quickSelect_self(nums, 0, end, k - 1);
    }
    
    private int quickSelect_self(int[] nums, int start, int end, int k) {
		if (start == end) {
			return nums[start];
		}
		
		int length = end - start + 1;
		int pivot = (int) (Math.random() * length) + start;
		swap(nums, end, pivot);
		
		pivot = nums[end];
		int j = start - 1;
        for (int i = start; i < end; i++) {   // for (int i = start; i <= end; i++) {
            if (nums[i] > pivot) {            //     if (nums[i] >= pivot) { 
				j++;                          //         j++;
				swap(nums, i, j);             //         swap(nums, i, j); 
			}                                 //     }
        }                                     // }
        j++;                                  //
		swap(nums, j, end);                   //
        
        if (j == k)
			return nums[j];
		else if (j > k)
			return quickSelect_self(nums, start, j - 1, k);
		else
			return quickSelect_self(nums, j + 1, end, k);        
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60312/AC-Clean-QuickSelect-Java-solution-avg.-O(n)-time
     * 
     * https://en.wikipedia.org/wiki/Quickselect
     * 
     * Other code:
     * https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60312/AC-Clean-QuickSelect-Java-solution-avg.-O(n)-time/61582
     */
	public int findKthLargest_quickSelect(int[] nums, int k) {
		return quickSelect(nums, 0, nums.length - 1, k);
	}

	int quickSelect(int[] nums, int low, int high, int k) {
		int pivot = low;

		// use quick sort's idea
		// put nums that are <= pivot to the left
		// put nums that are > pivot to the right
		for (int j = low; j < high; j++) {
			if (nums[j] <= nums[high]) {
				swap(nums, pivot++, j);
			}
		}
		swap(nums, pivot, high);

		// count the nums that are > pivot from high
		int count = high - pivot + 1;
		// pivot is the one!
		if (count == k)
			return nums[pivot];
		// pivot is too small, so it must be on the right
		if (count > k)
			return quickSelect(nums, pivot + 1, high, k);
		// pivot is too big, so it must be on the left
		return quickSelect(nums, low, pivot - 1, k - count);
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
	
	// Choose last one as pivot
	private int findKthLargest_last(int[] nums, int k) {
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
			return findKthLargest_last(subnums, k);
		}
		else {
			int[] subnums = Arrays.copyOfRange(nums, j + 1, nums.length);
			return findKthLargest_last(subnums, k - j - 1);
		}
	}
	
	// Choose first one as pivot
	private int findKthLargest2(int[] nums, int k) {
		int pivot = (int) (Math.random() * nums.length);
		System.out.println("pivot 亂數 : " + pivot);
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
			return findKthLargest_last(subnums, k);
		}
		else {
			int[] subnums = Arrays.copyOfRange(nums, j + 1, nums.length); 
			return findKthLargest_last(subnums, k-j-1);
		}
		
	}
	
	/* ------- To be continued ------- */
	/*
	private int find_Kth_Largest2(int[] nums, int k) {
		int end = nums.length - 1;
		return find_Kth_Partition2(nums, 0, end, k);
	}
	
	// Selection in worst-case linear time
	private int find_Kth_Partition2(int[] nums, int start, int end, int k) {
		int length = end - start + 1;
		int group = 5;
		if (length <= group) {
			return nums[insertionSort(nums, start, end, k)];
		}
		
		int medianLength = length / group;
		if (length % group != 0) {
			medianLength++;
		}
		int[] median = new int[medianLength];
		int counter = 0;
		for (int i = start; i <= end; i += group) {
			median[counter] = insertionSort(nums, i, i + group - 1, (group + 1) / 2);
			counter++;
		}
		
		int index = insertionSort(median, 0, medianLength - 1, medianLength / 2);
		if (k - 1 == length / 2 ) {
			return median[index];
		}
	}
	
	private int insertionSort (int[] nums, int start, int end, int k) {
		int length = end - start + 1;
		for (int i = 1; i < length; i++) {
			for (int j = i - 1; j >= 0; j--) {
				if (nums[j] < nums[i]) {
					swap(nums, i, j);
				}
			}
		}
		return k - 1;
	}
	*/
	
	private void swap(int[] arr, int a, int b) {
		int tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kth_Largest_Element_in_an_Array kth = new Kth_Largest_Element_in_an_Array();
		int[] a = {5,1,7,6,3,9,2,4,8};
		//int[] a = {7,6,5,4,3,2,1};
		//int[] a = {3,3,3,3,3,3,3,3,3};
		int b = 8;
		System.out.println("pivot = Last ; " + kth.findKthLargest_last(a, b));
		System.out.println("pivot = First ; " + kth.findKthLargest2(a, b));
		System.out.println("Expected linear ; " + kth.find_Kth_Largest(a, b));
	}

}
