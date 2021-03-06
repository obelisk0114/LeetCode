package OJ0461_0470;

import java.util.Arrays;

/*
 * https://discuss.leetcode.com/topic/69332/why-median-is-better-than-average/4
 * 
 * https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/discuss/94932/Why-median-is-better-than-average/99363
 * 
 * The meet point must be somewhere between current min and max. No matter which point 
 * you pick, the total running length for min and max is the same, i.e. 
 * abs(min_point-meet_point) + abs(max_point-meet_point) = SOME_CONSTANT.
 * 
 * So, we can effectively reduce the problem size from n to n-2 by discarding min and 
 * max points. That is the definition of median.
 * 
 * Suppose we have an array, [1,2,3,4,5,6,7]. The meet point must lie between 1 and 7. 
 * For any value in this range, the total running length for 1 and 7 is the same. So, 
 * array => [2,3,4,5,6]...
 */

public class Minimum_Moves_to_Equal_Array_Elements_II {
	// https://discuss.leetcode.com/topic/68736/java-just-like-meeting-point-problem
	public int minMoves2(int[] nums) {
        Arrays.sort(nums);
        int i = 0, j = nums.length-1;
        int count = 0;
        while(i < j){
            count += nums[j]-nums[i];
            i++;
            j--;
        }
        return count;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/68758/java-o-n-time-using-quickselect
	 */
	public int minMoves2_quickSelect(int[] A) {
	    int sum = 0, median = quickselect(A, A.length/2+1, 0, A.length-1);
	    for (int i=0;i<A.length;i++) sum += Math.abs(A[i] - median);
	    return sum;
	}
	public int quickselect(int[] A, int k, int start, int end) {
	    int l = start, r = end, pivot = A[(l+r)/2];
	    while (l<=r) {
	        while (A[l] < pivot) l++;
	        while (A[r] > pivot) r--;
	        if (l>=r) break;
	        swap(A, l++, r--);
	    }
	    if (l-start+1 > k) return quickselect(A, k, start, l-1);
	    if (l-start+1 == k && l==r) return A[l];
	    return quickselect(A, k-r+start-1, r+1, end);
	}
	public void swap(int[] A, int i, int j) {
	    int temp = A[i];
	    A[i] = A[j];
	    A[j] = temp;
	}
	
	// https://discuss.leetcode.com/topic/68764/5-line-solution-with-comment
	public int minMoves2_2(int[] nums) {
        int sum = 0;
        Arrays.sort(nums);
        int medium = nums[nums.length / 2];
        for(int n : nums) sum += Math.abs(n - medium);
        return sum;
    }
	
	// https://discuss.leetcode.com/topic/81248/very-short-java-solution-with-o-nlogn

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Minimum_Moves_to_Equal_Array_Elements_II minimumMove = new Minimum_Moves_to_Equal_Array_Elements_II();
		int[] a = {0, 0, 0, 5, -1, 0, 10, 5};
		System.out.println(minimumMove.minMoves2(a));

	}

}
