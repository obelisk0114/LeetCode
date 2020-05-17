package OJ0531_0540;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Single_Element_in_a_Sorted_Array {
	/*
	 * Modify from:
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627787/Java-Binary-Search-O(log-N)-time-and-O(1)-memory
	 * 
	 * The pairs which are on the left of the single element, will have the first 
	 * element in an even position and the second element at an odd position. 
	 * All the pairs which are on the right side of the single element will have the 
	 * first position at an odd position and the second element at an even position.
	 * 
	 * 1. when the mid is even, there are even number of elements on its left side. 
	 *    So, if they are all pairs, mid should be the same with its immediate right 
	 *    element, then we know the single number is on the right part. And vice versa.
	 * 2. when the mid is odd, there are odd number of elements on its left side. 
	 *    So, plus mid, if they are all pairs, mid should be the same with its 
	 *    immediate left element, then we know the single number is on the right part, 
	 *    And vice versa.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/431425/Only-the-trick-that-is-required-to-be-able-to-solve-this(No-code)
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/431425/Only-the-trick-that-is-required-to-be-able-to-solve-this(No-code)/537398
	 */
	public int singleNonDuplicate_self2(int[] nums) {
		int left = 0;
		int right = nums.length - 1;

		while (left < right) {
			int mid = left + (right - left) / 2;
			
			if (nums[mid] == nums[mid + 1]) {
				if (mid % 2 == 0)
					left = mid + 2;
				else
					right = mid - 1;
			} 
			else if (nums[mid] == nums[mid - 1]) {
				if ((mid - 1) % 2 == 0)
					left = mid + 1;
				else
					right = mid - 2;
			} 
			else
				return nums[mid];
		}
		return nums[left];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations
	 * 
	 * The basic idea here is that there's only one element that appears once. Suppose 
	 * a series of number that all the elements appear twice, then elements always 
	 * change at even positions. If one element only appears once, then the rule will 
	 * be broken and we can use binary search based on this rule.
	 * 
	 * At its core, it's a binary search algorithm. In conventional binary searches on 
	 * sorted arrays, we're usually trying to find a value and can determine whether 
	 * to look in the left or right subarray by comparing the target value with the 
	 * current one. However, here, we determine whether to look in the left or right 
	 * side by instead inspecting whether the indices of pairs has been shifted or not.
	 * 
	 * start can never be greater than end, after the while loop they will just equal 
	 * each other
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations/104715
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations/166472
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations/206174
	 * 
	 * Other code:
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations/235525
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100754/Java-Binary-Search-short-(7l)-O(log(n))-w-explanations/119419
	 */
	public int singleNonDuplicate_check_first(int[] nums) {
		int start = 0, end = nums.length - 1;

		while (start < end) {
            // We want the first element of the middle pair,
            // which should be at an even index if the left part is sorted.
            // Example:
            // Index: 0 1 2 3 4 5 6
            // Array: 1 1 3 3 4 8 8
            //            ^
			int mid = (start + end) / 2;
			if (mid % 2 == 1)
				mid--;

            // We didn't find a pair. The single element must be on the left.
            // (pipes mean start & end)
            // Example: |0 1 1 3 3 6 6|
            //               ^ ^
            // Next:    |0 1 1|3 3 6 6
			if (nums[mid] != nums[mid + 1])
				end = mid;

            // We found a pair. The single element must be on the right.
            // Example: |1 1 3 3 5 6 6|
            //               ^ ^
            // Next:     1 1 3 3|5 6 6|
			else
				start = mid + 2;
		}

        // 'start' should always be at the beginning of a pair.
        // When 'start >= end', start must be the single element.
		return nums[start];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100733/Java-Binary-Search-with-Detailed-Explanation
	 * 
	 * Example 1: An array with length 2*4 + 1
	 * left = 0, right = 8, mid = 4.
	 * 
	 * If the single element X is on the left hand side, nums[mid] == nums[mid-1]:
	 *   [1, 1, X, 2, 2(mid), 3, 3, 4, 4]
	 * If the single element X is on the right hand side, nums[mid] == nums[mid+1]:
	 *   [1, 1, 2, 2, 3(mid), 3, X, 4, 4]
	 * 
	 * Example 2: An array with length 2*3 + 1
	 * left = 0, right = 6, mid = 3.
	 * 
	 * If the single element X is on the left hand side, nums[mid] == nums[mid+1]:
	 *   [1, 1, X, 2(mid), 2, 3, 3]
	 * If the single element X is on the right hand side, nums[mid] == nums[mid-1]:
	 *   [1, 1, 2, 2(mid), 3, 3, X]
	 * 
	 * In general, for an array with length 2*n + 1, if n is even, the behavior of 
	 * mid will be the same as that in Example 1. Otherwise, it will be as in 
	 * Example 2.
	 */
	public int singleNonDuplicate_2n_1(int[] nums) {
		if (nums.length == 1)
			return nums[0];

		int left = 0;
		int right = nums.length - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;

			// nums[mid] is not single
			if ((mid - 1 >= 0 && nums[mid - 1] == nums[mid]) 
					|| (mid + 1 < nums.length && nums[mid + 1] == nums[mid])) {
				
				// actual length - 1
				int currLen = right - left;
				
				if ((currLen / 2) % 2 == 0) {					
					if (nums[mid - 1] == nums[mid]) {   // The element is on the left
						// Skip mid-1 and mid as we know they are not single
						right = mid - 2;
					} 
					else {                             // The element is on the right
						left = mid + 2;
					}
				} 
				else {
					if (nums[mid - 1] == nums[mid]) {  // The element is on the right
						// Skip mid
						left = mid + 1;
					} 
					else {                             // The element is on the left
						right = mid - 1;
					}
				}
			} 
			else
				return nums[mid];
		}

		return nums[left];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627787/Java-Binary-Search-O(log-N)-time-and-O(1)-memory
	 * 
	 * The pairs which are on the left of the single element, will have the first 
	 * element in an even position and the second element at an odd position. 
	 * All the pairs which are on the right side of the single element will have the 
	 * first position at an odd position and the second element at an even position. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/431425/Only-the-trick-that-is-required-to-be-able-to-solve-this(No-code)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628439/Video-Explanation-w-code-%3A-100-faster
	 */
	public int singleNonDuplicate2(int[] nums) {
		int left = 0;
		int right = nums.length - 1;
		int mid;

		while (left < right) {
			mid = left + ((right - left) >> 1);
			
			if (nums[mid] == nums[mid + 1]) {
				// from index mid to (nums.length - 1),
				// if even then check left interval
				if (((nums.length - mid) & 1) == 0)
					right = mid - 1;
				else
					left = mid + 2;
			} 
			else if (nums[mid] == nums[mid - 1]) {
				// from index (mid - 1) to (nums.length - 1),
				// if even then check left interval
				if (((nums.length - mid + 1) & 1) == 0)
					right = mid - 2;
				else
					left = mid + 1;
			} 
			else
				return nums[mid];
		}

		return nums[left];
	}
	
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627921/Java-or-C%2B%2B-or-Python3-or-Easy-explanation-or-O(logn)-or-O(1)
	 * 
	 * Suppose array is [1, 1, 2, 2, 3, 3, 4, 5, 5]
	 * we can observe that for each pair, 
	 * first element takes even position and second element takes odd position
	 * for example, 1 is appeared as a pair,
	 * so it takes 0 and 1 positions. similarly for all the pairs also.
	 * 
	 * this pattern will be missed when single element is appeared in the array.
	 * 
	 * 1. Take left and right pointers. 
	 *      left points to start of list. right points to end of the list.
	 * 2. find mid.
	 *      if mid is even, then it's duplicate should be in next index.
	 *      or if mid is odd, then it's duplicate  should be in previous index.
	 *      
	 *      if any of the conditions is satisfied, then pattern is not missed, 
	 *      so check in next half of the array. i.e, left = mid + 1
	 *      if condition is not satisfied, then the pattern is missed.
	 *      so, single number must be before mid. update end to mid.
	 * 3. At last return the nums[left]
	 * 
	 * ANOTHER VERSION:
	 * 
	 * left = 0, right = nums.length;
	 * while (left < right) {
	 * ...
	 * if ((mid % 2 == 0 && mid + 1 < n && nums[mid] == nums[mid + 1]) ||
	 *     (mid % 2 == 1 && mid - 1 >= 0 && nums[mid] == nums[mid - 1]))
	 *     left = mid + 1;
	 * ...
	 * }
	 * ...
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627921/Java-or-C++-or-Python3-or-Easy-explanation-or-O(logn)-or-O(1)/538216
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627921/Java-or-C++-or-Python3-or-Easy-explanation-or-O(logn)-or-O(1)/537741
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627921/Java-or-C++-or-Python3-or-Easy-explanation-or-O(logn)-or-O(1)/537760
	 * 
	 * Other code:
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100765/Java-Binary-Search-O(lgN)-%3A-clear-easy-explained-no-tricks
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628411/Java-two-solutions-with-explanation
	 */
	public int singleNonDuplicate3(int[] nums) {
		int left = 0, right = nums.length - 1;
		while (left < right) {
			int mid = (left + right) / 2;
			if ((mid % 2 == 0 && nums[mid] == nums[mid + 1]) 
					|| (mid % 2 == 1 && nums[mid] == nums[mid - 1]))
				left = mid + 1;    // left = (mid %2==0) ? mid + 2 : mid + 1;
			else
				right = mid;
		}
		return nums[left];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100759/Java-Binary-Search-O(log(n))-Shorter-Than-Others
	 * 
	 * lo and hi are not regular index, but the pair index here. Basically you want to 
	 * find the first even-index number not followed by the same number.
	 * 
	 * [1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6] // target in the beginning
	 * [1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6] // target in the middle
	 * [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6] // target in the end
	 * 
	 * All 3 variants can be considered as a 'broken' sequence of pairs:
	 * [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6] // clear, without target
	 * 
	 * + the number of elems is always odd
	 * + the pair indexes go from left=0 till right=nums.size()/2. 
	 *   Round down on divide helps: e.g. 11/2=5 valid pairs
	 * + we can consider elems as pairs: nums[i*2]==nums[i*2+1] till we reach the 
	 *   'broken' pair
	 * + let's use binary search to divide the sequence to 'clear' and 'already 
	 *   broken'
	 * 
	 * [|1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6] // target in the beginning
	 * 0 pairs in clear part
	 * [1, 1, 2, 2, 3, 3, | 4, 5, 5, 6, 6] // target in the middle
	 * 3 pairs in clear part
	 * [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, | 6] // target in the end
	 * 5 pairs in clear part
	 * 
	 * So our answer is the first element right after the 'clear' part.
	 * With a binary search, we narrow down the boundaries between 'clear' 
	 * (on the left) and 'broken' (on the right) till there is a gap between them 
	 * (left<right). As soon as left==right, we found the boundary and thus found the 
	 * target!
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100759/Java-Binary-Search-O(log(n))-Shorter-Than-Others/537310
	 */
	public int singleNonDuplicate_pair_index(int[] nums) {
		// binary search
		int n = nums.length, lo = 0, hi = n / 2;
		while (lo < hi) {
			int m = (lo + hi) / 2;
			if (nums[2 * m] != nums[2 * m + 1])
				hi = m;
			else
				lo = m + 1;
		}
		return nums[2 * lo];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100732/Short-compare-numsi-with-numsi1
	 * 
	 * Simply find the first index whose "partner index" (the index xor 1) holds a 
	 * different value.
	 * 
	 * num ^ 1 : flips the last bit of num
	 * If last bit is 0 (even), flipping it to 1 is same as adding 1 (+1). 
	 * If last bit is 1 (odd ), flipping it to 0 is same as subtracting 1 (-1).
	 * 
	 * every 2 numbers are partner. (even,odd), (even,odd).
	 * If mid is even, it's partner is next odd, 
	 * if mid is odd, it's partner is previous even.
	 * 
	 * Starting from index 0, which is even, every number has to appear in pairs 
	 * (ideally) - Even-odd, even-odd, even-odd..... until this trend is broken by a 
	 * single element. So if you check any odd-index and the even-index just before 
	 * it, you can know whether the single element comes in the array after the 
	 * odd-index or before the odd-index. For instance- if I check index 5, and it's 
	 * previous 4, if they are equal, that automatically means that 0-1, 2-3, 4-5 etc 
	 * are all in pairs, so the single element would be in the array arr[6:end]. So we 
	 * adjust low = mid +1 and proceed. If it wasn't equal that means that one among 
	 * 0-1, 2-3, 4-5 is a single element.
	 * The cool trick is to quickly figure out this odd-and-previous-even (or 
	 * equivalently even-and_next-odd depending on m), quickly using xor.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627935/Java-Super-Elegent-solution-(log(n)-Runtime-O(1)-extra-space-bit-trick).-Detailed-explanation
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100732/Short-compare-numsi-with-numsi1/538136
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100732/Short-compare-numsi-with-numsi1/376308
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100732/Short-compare-numsi-with-numsi1/318110
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100732/Short-compare-numsi-with-numsi1/104705
	 */
	public int singleNonDuplicate_xor(int[] nums) {
		int lo = 0, hi = nums.length - 1;
		while (lo < hi) {
			int mid = (lo + hi) >>> 1;
			if (nums[mid] == nums[mid ^ 1])
				lo = mid + 1;
			else
				hi = mid;
		}
		return nums[lo];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628911/Java-oror-Fastest-greater-O(log-n2)-runtime-and-O(1)-space-oror-Comprehensible-Explanation
	 * 
	 * Assume the nonDuplicate at ith position. Since there is only one nonDuplicate 
	 * we can deduce that all the values before i definitely are pairs (i.e No. of 
	 * values before i are always even) -> Can conclude that 'i' is always odd 
	 * (even index in array)
	 * 
	 * So let's assume index 'x' corresponds to all even indices (odd positions).
	 * For All (x < i) nums[x] is always equal to nums[x+1]
	 * 
	 * Implement binary search and consider only even indices in the array (odd 
	 * positions).
	 * 
	 * while (start <= end)
	 *     // Since we consider only odd positions.
	 *     mid = (start+end)/2 OR (start+end)/2 + 1
	 *     
	 *     if( value at mid == value at (mid+1)
	 *         then start = mid + 2
	 *     else
	 *         end = mid - 2
	 * return nums[start]
	 * 
	 * Note: start,end,mid are always even indices (odd positions in start, mid).
	 * Note: Since our algo won't work for last index, we'll check that manually.
	 */
	public int singleNonDuplicate_check_first2(int[] nums) {
		int len = nums.length, start = 0, end = len - 3, mid;
		if (len > 1) {
			if (nums[len - 1] != nums[len - 2])
				return nums[len - 1];
		}
		
		while (start <= end) {
			mid = (start + end) % 4 == 0 ? (start + end) / 2 : (start + end) / 2 + 1;
			if (nums[mid] == nums[mid + 1]) {
				start = mid + 2;
			} 
			else {
				end = mid - 2;
			}
		}
		return nums[start];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100829/Using-Collections.binarySearch-(for-fun)
	 * 
	 * Let Collections.binarySearch search in a special List object which returns -1 
	 * for indexes before the single element, 0 for the index of the single element, 
	 * and 1 for indexes after the single element. So I run binary search for 0. Since 
	 * my special List object computes its entries only on the fly when requested, 
	 * this solution takes O(log n) time and O(1) space.
	 * 
	 * The pairs which are on the left of the single element, will have the first 
	 * element in an even position and the second element at an odd position. 
	 * All the pairs which are on the right side of the single element will have the 
	 * first position at an odd position and the second element at an even position.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/431425/Only-the-trick-that-is-required-to-be-able-to-solve-this(No-code)
	 */
	public int singleNonDuplicate_collectionsBinarySearch(int[] nums) {
	    List<Integer> list = new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			public int size() {
	            return nums.length;
	        }
	        public Integer get(int index) {
	            if ((index ^ 1) < size() && nums[index] == nums[index ^ 1])
	                return -1;
	            
	            // single number will always appear in even position.
	            if (index == 0 || index % 2 == 0 && nums[index - 1] != nums[index])
	                return 0;
	            return 1;
	        }
	    };
	    return nums[Collections.binarySearch(list, 0)];
	}
	
	/*
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100829/Using-Collections.binarySearch-(for-fun)
	 * 
	 * The helper isOff tells whether the index is in the part that's "off" 
	 * (the single element and everything after it)
	 * 
	 * The pairs which are on the left of the single element, will have the first 
	 * element in an even position and the second element at an odd position. 
	 * All the pairs which are on the right side of the single element will have the 
	 * first position at an odd position and the second element at an even position.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/431425/Only-the-trick-that-is-required-to-be-able-to-solve-this(No-code)
	 */
	public int singleNonDuplicate_collectionsBinarySearch2(int[] nums) {
	    List<Integer> list = new ArrayList<Integer>() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 2L;
			public int size() {
	            return nums.length;
	        }
	        public Integer get(int index) {
	            return isOff(index) + isOff(index - 1);
	        }
	        int isOff(int i) {
	            return i == size() - 1 || i >= 0 && nums[i] != nums[i ^ 1] ? 1 : 0;
	        }
	    };
	    
	    // 1 is the result that overlapping of two ranges occurs. 
	    return nums[Collections.binarySearch(list, 1)];
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628036/Python-Binary-Search-O(logn)-explained
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100806/Binary-Search-based-approach-in-Python
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/187532/Python-binary-search-O(logn)
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627803/Python-3-today's-one-liner
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/627786/C%2B%2B-O(log-n)-time-O(1)-space-or-Simple-and-clean-or-Use-xor-to-keep-track-of-odd-even-pair
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628111/C%2B%2B-Solution-O(logn)-with-detailed-explanation
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100798/C%2B%2B-O(log-n)-5-lines
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/100766/C%2B%2B-binary-search
     * https://leetcode.com/problems/single-element-in-a-sorted-array/discuss/628648/C-Pairwise-binary-search
     */

}
