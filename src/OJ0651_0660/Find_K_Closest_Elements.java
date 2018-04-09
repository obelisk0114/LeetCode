package OJ0651_0660;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

public class Find_K_Closest_Elements {
	/*
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106451/Binary-Search-and-Two-Pointers-18-ms
	 * 
	 * If the left element is more close or equal to the target number x than 
	 * the right element, then move the right index to the left one step. 
	 * Otherwise, move the left index to right one step. 
	 * Once the element between the left and right is k, then return the result.
	 * 
	 * Rf : https://leetcode.com/articles/find-k-closest-elements/
	 */
	public List<Integer> findClosestElements_trim(int[] arr, int k, int x) {
		List<Integer> result = new ArrayList<Integer>();
        if(x <= arr[0]) {
            for (int i = 0; i < k; i++) {
                result.add(arr[i]);
            }
            return result;
        }
		else if (x >= arr[arr.length - 1]) {
			for (int i = arr.length - k; i < arr.length; i++) {
				result.add(arr[i]);
			}
			return result;
        }
		else {
			int index = Arrays.binarySearch(arr, x);
			if (index < 0)
				index = -index - 1;
			int low = Math.max(0, index - k);  // (index - k - 1, 0) 
			int high = Math.min(arr.length - 1, index + k); // (index + k - 1, arr.length - 1)

			while (high - low > k - 1) {
				if ((x - arr[low]) <= (arr[high] - x))
					high--;
				else if ((x - arr[low]) > (arr[high] - x))
					low++;
				else
					System.out.println("unhandled case: " + low + " " + high);
			}
			
            for (int i = low; i < high + 1; i++) {
                result.add(arr[i]);
            }
		}
        return result;
    }
	
	/*
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106439/JavaC++-Very-simple-binary-search-solution
	 * 
	 * The idea is to find the first number which is equal to or greater than x in arr. 
	 * Then, we determine the indices of the start and the end of a subarray in arr, 
	 * where the subarray is our result. The time complexity is O(logn + k).
	 * 
	 * In the following code, arr[index] is the first number which is euqal to 
	 * or geater than x (if all numbers are less than x, index is arr.length), and 
	 * the result is arr[i+1, i+2, ... j].
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/112476/My-solution-O(logn-+-k)-is-easy-to-understand
	 */
	public List<Integer> findClosestElements_expand(int[] arr, int k, int x) {
		int index = Arrays.binarySearch(arr, x);
		if (index < 0)
			index = -(index + 1);
		
		int i = index - 1, j = index;
		while (k-- > 0) {
			if (i < 0 || (j < arr.length && Math.abs(arr[i] - x) > Math.abs(arr[j] - x)))
				j++;
			else
				i--;
		}
		
		List<Integer> list = new ArrayList<>();
		for (int i1 = i + 1; i1 < j; i1++) {
			list.add(arr[i1]);
		}
		return list;
	}
	
	/*
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106430/Updated-Java-Solution
	 * 
	 * Rf : 
	 * https://www.topcoder.com/community/data-science/data-science-tutorials/binary-search/
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106419/O(log-n)-Java-1-line-O(log(n)-+-k)-Ruby
	 */
	public List<Integer> findClosestElements_start(int[] arr, int k, int x) {
		int start = 0, end = arr.length - k;

		while (start < end) {
			int mid = (start + end) / 2;
			if (x - arr[mid] > arr[mid + k] - x)
				start = mid + 1;
			else
				end = mid;
		}

		List<Integer> results = new ArrayList<Integer>();
		for (int i = start; i < start + k; i++) {
			results.add(arr[i]);
		}
		return results;
	}
	
	// The following function and "find_self" are from this link.
	public List<Integer> findClosestElements_self2(int[] arr, int k, int x) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        int pos = find_self(arr, x);
        list.add(arr[pos]);
        
        int pre = pos - 1;
        int post = pos + 1;
        for (int i = 0; i < k - 1; i++) {
            if (pre < 0) {
                list.addLast(arr[post]);
                post++;
                continue;
            }
            if (post >= arr.length) {
                list.addFirst(arr[pre]);
                pre--;
                continue;
            }
            
            if (Math.abs(arr[post] - x) < Math.abs(arr[pre] - x)) {
                list.addLast(arr[post]);
                post++;
            }
            else {
                list.addFirst(arr[pre]);
                pre--;
            }
        }
        
        return list;
    }
	
	// https://leetcode.com/problems/find-k-closest-elements/discuss/121974/a-bit-more-than-10-lines-the-easiest-java-solution
	public List<Integer> findClosestElements_trim_all(int[] arr, int k, int x) {
		List<Integer> res = new ArrayList<Integer>();
		int len = arr.length, start = 0, end = len - 1;
		while (end - start > k - 1) {
			if ((x - arr[start]) <= (arr[end] - x)) {
				end--;
			} 
			else {
				start++;
			}
		}
		for (int i = start; i < end + 1; i++) {
			res.add(arr[i]);
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106435/Java-short-O(NlogN)-solution-and-O(logN-+-k)-solution
	 * 
	 * We use binary search to find the nearest location of x in the array. (logN) 
	 * Then we expand on both sides using 2 pointers, to get the k nearest elements.
	 */
	public List<Integer> findClosestElements2(int[] arr, int k, int x) {
		List<Integer> result = new ArrayList<Integer>();
		if (x < arr[0]) {
			for (int i = 0; i < k; i++) {
				result.add(arr[i]);
			}
			return result;
		}
		if (x > arr[arr.length - 1]) {
			for (int i = arr.length - k; i < arr.length; i++) {
				result.add(arr[i]);
			}
			return result;
		}

		int index = binSearch(arr, x);
		// System.out.println(index);
		int i = 0;
		int j = 0;
		if (arr[index] == x) {
			result.add(x);
			i = index - 1;
			j = index + 1;
		} else {
			i = index - 1;
			j = index;
		}

		while (i >= 0 && j < arr.length && result.size() != k) {
			if (Math.abs(arr[i] - x) <= Math.abs(arr[j] - x)) {
				result.add(0, arr[i--]);
			} else {
				result.add(arr[j++]);
			}
		}

		int len = result.size();
		if (i < 0) {
			for (int i1 = j; i1 < j + k - len; i1++) {
				result.add(arr[i1]);
			}
		} else if (j >= arr.length) {
			for (int i1 = i; i1 >= i + 1 - k + len; i1--) {
				result.add(0, arr[i1]);
			}
		}

		return result;
	}
	public int binSearch(int[] a, int target) {
		int st = 0;
		int end = a.length - 1;
		int mid = 0;
		while (st <= end) {
			mid = (st + end) / 2;
			if (a[mid] == target) {
				break;
			} else if (a[mid] > target) {
				end = mid - 1;
			} else {
				st = mid + 1;
			}
		}
		return mid;
	}
	
	/*
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106424/Java-4-Liner-and-O(n)-Time-Solution
	 * 
	 * The idea here is to simply sort the array based on the distance to the target 
	 * and grab the top k elements.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106438/easy-java
	 */
	public List<Integer> findClosestElements_sort(int[] arr, int k, int x) {
		List<Integer> ans = new ArrayList<Integer>();
		for (int i : arr) {
			ans.add(i);
		}
		Collections.sort(ans, (a, b) -> a == b ? a - b : Math.abs(a - x) - Math.abs(b - x));
		ans = ans.subList(0, k);
		Collections.sort(ans);
		return ans;
	}
	
	// https://leetcode.com/problems/find-k-closest-elements/discuss/106424/Java-4-Liner-and-O(n)-Time-Solution
	public List<Integer> findClosestElements_separate_list(int[] arr, int k, int x) {
		List<Integer> less = new ArrayList<>(), greater = new ArrayList<>(), 
				lessResult = new LinkedList<>(), greaterResult = new LinkedList<>();

		for (int i : arr) {
			if (i <= x)
				less.add(i);
			else
				greater.add(i);
		}

		Collections.reverse(less);
		int i = 0, j = 0, n = less.size(), m = greater.size();
		for (int size = 0; size < k; size++) {
			if (i < n && j < m) {
				if (Math.abs(less.get(i) - x) <= Math.abs(greater.get(j) - x))
					lessResult.add(less.get(i++));
				else
					greaterResult.add(greater.get(j++));
			} else if (i < n)
				lessResult.add(less.get(i++));
			else
				greaterResult.add(greater.get(j++));
		}

		Collections.reverse(lessResult);
		lessResult.addAll(greaterResult);
		return lessResult;
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public List<Integer> findClosestElements_self(int[] arr, int k, int x) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        int pos = find_self(arr, x);
        list.add(arr[pos]);
        
        int pre = pos - 1;
        int post = pos + 1;
        for (int i = 0; i < k - 1; i++) {
            long preNumber = (long) Integer.MAX_VALUE;
            long postNumber = (long) Integer.MAX_VALUE;
            if (pre >= 0) {
                preNumber = arr[pre];
            }
            if (post < arr.length) {
                postNumber = arr[post];
            }
            
            if (Math.abs(postNumber - x) < Math.abs(preNumber - x)) {
                list.addLast((int) postNumber);
                post++;
            }
            else {
                list.addFirst((int) preNumber);
                pre--;
            }
        }
        
        return list;
    }
    private int find_self(int[] arr, int x) {
    	int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (arr[mid] == x) {
                return mid;
            }
            else if (arr[mid] > x) {
                end = mid - 1;
            }
            else {
                start = mid + 1;
            }
        }
        return start;
    }

}
