package OJ0031_0040;

public class Search_in_Rotated_Sorted_Array {
	/*
	 * Rf :
	 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/discuss/48484/A-concise-solution-with-proof-in-the-comment
	 * 
	 * (1) loop is left < right, which means inside the loop, left always < right
	 * (2) since we use round up for mid, and left < right from (1), right would never 
	 *     be the same as mid
	 * (3) Therefore, we compare mid with right, since they will never be the same 
	 *     from (2)
	 * 
	 * test cases:
	 * [1,3]
	 * 3
	 * 
	 * [3,5,1]
	 * 3
	 */
	public int search_modify(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            
            if (nums[end] >= nums[mid]) {
                if (target <= nums[end] && target > nums[mid]) {
                    start = mid + 1;
                }
                else {
                    end = mid - 1;
                }
            }
            else {
                if (target >= nums[start] && target < nums[mid]) {
                    end = mid - 1;
                }
                else {
                    start = mid + 1;
                }
            }
        }
        return -1;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/16580/java-ac-solution-using-once-binary-search
	 * 
	 * check if the target is in the sorted part, if so keep doing the binary search
	 * otherwise throw away the sorted part and do the same on the other part of the array
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/1790/my-algorithm-using-binary-search-is-accepted-want-some-suggestions-as-it-has-a-lot-of-if-else/9
	 * https://discuss.leetcode.com/topic/7711/revised-binary-search
	 */
	public int search2(int[] nums, int target) {
		int start = 0;
		int end = nums.length - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (nums[mid] == target)
				return mid;

			// mid & start are the same order
			if (nums[start] <= nums[mid]) {
				if (target < nums[mid] && target >= nums[start])
					end = mid - 1;
				else
					start = mid + 1;
			}
			// mid and end are the same order
			else {                  // if (nums[mid] <= nums[end])
				if (target > nums[mid] && target <= nums[end])
					start = mid + 1;
				else
					end = mid - 1;
			}
		}
		return -1;
	}
	
	// https://discuss.leetcode.com/topic/33612/java-intuitive-closest-to-unflavored-binary-search
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/6555/solution-o-logn-binary-search-solution-with-explanation-java
	 */
	public int searchHelper(int[] A, int target, int start, int end) {
		if (start > end) {
			return -1;
		}
		int mid = (start + end) / 2;
		if (A[mid] == target) {
			return mid;
		}

		// Case 1: Left half is sorted
		if (A[mid] >= A[start]) {
			if (target >= A[start] && target <= A[mid]) {
				return searchHelper(A, target, start, mid - 1);
			} else {
				return searchHelper(A, target, mid + 1, end);
			}
		}
		
		// Case 2: Right half is sorted
		if (A[end] >= A[mid]) {
			if (target >= A[mid] && target <= A[end]) {
				return searchHelper(A, target, mid + 1, end);
			} else {
				return searchHelper(A, target, start, mid - 1);
			}
		}
		return -1;
	}
	public int search_recursive(int[] A, int target) {
		return searchHelper(A, target, 0, A.length - 1);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/34491/clever-idea-making-it-simple
	 * 
	 * If target is on the left, we adjust the right part to inf.
	 *                     right               left          -inf.
	 * 
	 * If nums[mid] and target are "on the same side" of nums[0], 
	 * we just take nums[mid]. Otherwise we use -infinity or +infinity as needed.
	 */
	public int search_inf(int[] nums, int target) {
	    int lo = 0, hi = nums.length - 1;
	    while (lo <= hi) {
	        int mid = lo + (hi - lo) / 2;
	        
	        int num = nums[mid];
	        // If nums[mid] and target are "on the same side" of nums[0], we just take nums[mid].
	        if ((nums[mid] < nums[0]) == (target < nums[0])) {
	            num = nums[mid];
	        } else {
	            num = target < nums[0] ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	        }

	        if (num < target)
	            lo = mid + 1;
	        else if (num > target)
	            hi = mid - 1;
	        else
	            return mid;
	    }
	    return -1;
	}
	
	// https://discuss.leetcode.com/topic/8889/binary-search-java-solusion-o-log-n
	public int search_start(int[] A, int target) {
		if (A.length == 0)
			return -1;
		int L = 0, R = A.length - 1;
		//
		if (target < A[L] && target > A[R])
			return -1;

		while (L < R) {
			int M = (L + R) / 2;
			if (A[M] <= A[R]) {
				if (target > A[M] && target <= A[R]) {
					L = M + 1;
				} else {
					R = M;
				}

			} else {
				if (target <= A[M] && target >= A[L]) {
					R = M;
				} else {
					L = M + 1;
				}
			}
		}
		if (A[L] == target)
			return L;
		else
			return -1;
	}
	
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/24341/simple-and-easy-understanding-java-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/3538/concise-o-log-n-binary-search-solution
	 * https://discuss.leetcode.com/topic/26777/share-my-simple-o-logn-java-solution
	 */
	public int search_findPivot(int[] nums, int target) {
		int pivot = findPivot(nums);
		if (nums.length == 0)
			return -1;
		return binarySearch(nums, 0, pivot, target) + binarySearch(nums, pivot + 1, nums.length - 1, target) + 1;
    }
 	public int findPivot(int nums[]){
		int i = 0, j = nums.length - 1;
		while (i < j - 1) {
			int mid = i + (j - i) / 2;
			if (nums[i] < nums[mid] && nums[j] < nums[mid]) {
				i = mid;
			} else {
				j = mid;
			}
		}
		return i;
 	}
 	public int binarySearch(int a[], int start, int end, int key){
		int i = start, j = end;
		while (i <= j) {
			int mid = i + (j - i) / 2;
			if (a[mid] > key) {
				j = mid - 1;
			} else if (a[mid] < key) {
				i = mid + 1;
			} else
				return mid;
		}
		return -1;
	}
 	
 	/*
 	 * https://discuss.leetcode.com/topic/34467/pretty-short-c-java-ruby-python
 	 * 
 	 * If nums[0] <= nums[i], then nums[0..i] is sorted
 	 * We should keep searching in nums[0..i] if the target lies in this sorted range, 
 	 * i.e., if nums[0] <= target <= nums[i].
 	 * 
 	 * If nums[i] < nums[0], then nums[0..i] contains a drop, 
 	 * and nums[i+1..end] is sorted and lies strictly between nums[i] and nums[0]. 
 	 * We should keep searching in nums[0..i] if the target doesn't lie strictly 
 	 * between them, 
 	 * i.e., if target <= nums[i] < nums[0] or nums[i] < nums[0] <= target
 	 * 
 	 * I have the three checks (nums[0] <= target), (target <= nums[i]) and 
 	 * (nums[i] < nums[0]), and I want to know whether exactly two of them are true.
 	 * 
 	 * just need to distinguish between "two true" and "one true". Use xor to do this
 	 */
	public int search_xor(int[] nums, int target) {
		int lo = 0, hi = nums.length - 1;
		while (lo < hi) {
			int mid = (lo + hi) / 2;
			if ((nums[0] > target) ^ (nums[0] > nums[mid]) ^ (target > nums[mid]))
				lo = mid + 1;
			else
				hi = mid;
		}
		return lo == hi && nums[lo] == target ? lo : -1;
	}
	
	// self, no binary search
	public int search_linear(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target)
                return i;
        }
        return -1;
    }

}
