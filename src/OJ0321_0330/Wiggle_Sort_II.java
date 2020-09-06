package OJ0321_0330;

import java.util.Arrays;
import java.util.Random;
import java.util.Queue;
import java.util.PriorityQueue;

// reorder it such that nums[0] < nums[1] > nums[2] < nums[3]...
/*
 * https://discuss.leetcode.com/topic/71990/summary-of-the-various-solutions-to-wiggle-sort-for-your-reference
 * 
 * That would require some number M to appear both in the smaller and the larger half. 
 * It would be the largest in the smaller half and the smallest in the larger half. 
 * S means some number smaller than M and L means some number larger than M.
 * 
 * https://en.wikipedia.org/wiki/Dutch_national_flag_problem#Pseudocode
 */

public class Wiggle_Sort_II {
	/*
	 * The following 3 functions and swap are from this link.
	 * https://discuss.leetcode.com/topic/41464/step-by-step-explanation-of-index-mapping-in-java
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/wiggle-sort-ii/discuss/77678/3-lines-Python-with-Explanation-Proof
	 * https://discuss.leetcode.com/topic/41464/step-by-step-explanation-of-index-mapping-in-java/14
	 * https://discuss.leetcode.com/topic/32929/o-n-o-1-after-median-virtual-indexing/34
	 * https://discuss.leetcode.com/topic/71990/summary-of-the-various-solutions-to-wiggle-sort-for-your-reference
	 * 
	 * Index :       0   1   2   3   4   5
	 * Small half:   M       S       S    
	 * Large half:       L       L       M
	 * 
	 * (n|1) is related to n being even or odd. 
	 * When n is even, we only need nums[(1 + 2i) % (n+1)] instead of nums[(1 + 2i) % (n|1)]; 
	 * when n is odd, we need nums[(1 + 2i) % n] instead of nums[(1 + 2i) % (n|1)]. 
	 * To combine the two cases together, we can simply use one formula nums[(1 + 2i) % (n|1)].
	 * 
	   1. for any i1, i2 that belong to S, i1 != i2 <==> f(i1) != f(i2).
	   2. if i covers all the elements in S exactly once, f(i) will do the same.
	   
	   If our traversing order follows that of the linear scan with f applied for 
	   each position i in the linear scan, the first property will guarantee 
	   each element will be visited at most once while the second will guarantee 
	   each element be visited at least once. 
	   Therefore, at the end each element will be visited exactly once.
	   
	   https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java/81844
	   
	   Notice that by placing the median in it's place in the array we divided the 
	   array in 3 chunks: all numbers less than median are in one side, all numbers 
	   larger than median are on the other side, median is in the dead center of the 
	   array.
	   
	   We want to place any a group of numbers (larger than median) in odd slots, and 
	   another group of numbers (smaller than median) in even slots. So all numbers on 
	   left of the median < n / 2 should be in odd slots, all numbers on right of the 
	   median > n / 2 should go into even slots (remember that median is its correct 
	   place at n / 2)
	   
	   So let's think about the first group in the odd slots, all numbers is the left 
	   side of the array should go into these odd slots.
	   
	   All these indexes are less than n / 2 so multiplying by 2 and add 1 (to make 
	   them go to odd place) and then mod by n will always guarantee that they are 
	   less than n.
	   
	   If we continue this with indexes larger than median we will cycle again and we 
	   don't want that, so for indexes larger than n/2 we want them to be even, 
	   (n|1) does that exactly. What n|1 does it that it gets the next odd number to n 
	   if it was even
	 */
	public void wiggleSort(int[] nums) {
		int median = partition(nums, 0, nums.length - 1, (nums.length + 1) / 2);
		int n = nums.length;

		int left = 0, i = 0, right = n - 1;

		while (i <= right) {
			if (nums[newIndex(i, n)] > median) {
				swap(nums, newIndex(left++, n), newIndex(i++, n));
			} 
			else if (nums[newIndex(i, n)] < median) {
				swap(nums, newIndex(right--, n), newIndex(i, n));
			} 
			else {
				i++;
			}
		}

	}
	
	// for descending order
	private int newIndex(int index, int n) {
		return (1 + 2 * index) % (n | 1);
	}
	
	private int partition(int[] arr, int start, int end, int k) {
		if (start == end) {
			return arr[start];
		}
		
		Random random = new Random();
		int pivot = random.nextInt(end - start + 1) + start;
		swap(arr, end, pivot);
		pivot = arr[end];
		int j = start - 1;
		for (int i = start; i < end; i++) {
			// if (arr[i] < pivot) also works
			if (arr[i] > pivot) {
				j++;
				swap(arr, i, j);
			}
		}
		j++;
		swap(arr, j, end);
		int index = j - start + 1;     // Find index of subarray
		if (index == k) {
			return arr[j];
		}
		else if (index > k) {
			return partition(arr, start, j - 1, k);
		}
		else {
			return partition(arr, j + 1, end, k - index);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/52116/o-n-o-1-after-median-without-virtual-indexing/2
	 */
	public void wiggleSort_3Partition(int[] nums) {
		int n = nums.length;
		int med = findKthLargest(nums, (n + 1) / 2);
		int i = 1; // (2) elements larger than the 'median' are put into the first odd slots
		int j = (n - 1) / 2 * 2; // (1) elements smaller than the 'median' are put into the last even slots
		int x = j;

		for (int k = 0; k < n; k++) {
			if (nums[x] > med) {
				swap(nums, x, i);
				i += 2;
			} else if (nums[x] < med) {
				swap(nums, x, j);
				j = j - 2;
				x = x - 2;
				if (x < 0)
					x = n / 2 * 2 - 1;
			} else {
				x = x - 2;
				if (x < 0)
					x = n / 2 * 2 - 1;
			}
		}
	}
	/********   Another format   ********/
	/* 
	public void wiggleSort_3Partition(int[] nums) {
		int n = nums.length;
		if (n == 1)
			return;
		int med = findKthLargest(nums, (n + 1) / 2);
		int i = 1; // (2) elements larger than the 'median' are put into the first odd slots
		int j = (n - 1) / 2 * 2; // (1) elements smaller than the 'median' are put into the last even slots
		int x = i;

		for (int k = 0; k < n; k++) {
			if (nums[x] > med) {
				swap(nums, x, i);
				i += 2;
				x += 2;
				if (x >= n)
					x = 0;
			} else if (nums[x] < med) {
				swap(nums, x, j);
				j = j - 2;
			} else {
				x = x + 2;
				if (x >= n)
					x = 0;
			}
		}
	}
	*/
	public int findKthLargest(int[] nums, int k) {
		Queue<Integer> q = new PriorityQueue<>(k);
		for (int n : nums) {
			if (q.size() == k) {
				if (q.peek() == null) {
					q.offer(n);
				} else if (n > q.peek()) {
					q.poll();
					q.offer(n);
				}
			} else {
				q.offer(n);
			}
		}

		return q.peek();
	}
	
	/*
	 * The following 3 functions and swap are from this link.
	 * https://discuss.leetcode.com/topic/71990/summary-of-the-various-solutions-to-wiggle-sort-for-your-reference
	 * 
	 * III -- O(n) time and O(n) space solution by median partition
	 * As for elements within each group, we don't care whether they are sorted or not. 
	 * Suppose m_ele is the m-th smallest element in the array. 
	 * We partition the array such that all elements less than m_ele go to its left 
	 *  and those greater than it end up in its right (or the other way around). 
	 * After partition, the first m elements will form the S group 
	 *  while the rest will be the L group.
	 */
	public void wiggleSort_shuffleQuickSelect(int[] nums) {
		int n = nums.length, m = (n + 1) >> 1;
		int[] copy = Arrays.copyOf(nums, n);
		int median = kthSmallestNumber(nums, m);

		for (int i = 0, j = 0, k = n - 1; j <= k;) {
			if (copy[j] < median) {
				swap(copy, i, j);
				i++;
				j++;
			} 
			else if (copy[j] > median) {
				swap(copy, j, k);
				k--;
			} 
			else {
				j++;
			}
		}

		for (int i = m - 1, j = 0; i >= 0; i--, j += 2)
			nums[j] = copy[i];
		for (int i = n - 1, j = 1; i >= m; i--, j += 2)
			nums[j] = copy[i];
	}
	private int kthSmallestNumber(int[] nums, int k) {
		Random random = new Random();

		for (int i = nums.length - 1; i >= 0; i--) {
			swap(nums, i, random.nextInt(i + 1));
		}

		int l = 0, r = nums.length - 1;
		k--;

		while (l < r) {
			int m = getMiddle(nums, l, r);

			if (m < k) {
				l = m + 1;
			} else if (m > k) {
				r = m - 1;
			} else {
				break;
			}
		}

		return nums[k];
	}
	private int getMiddle(int[] nums, int l, int r) {
		int i = l;

		for (int j = l + 1; j <= r; j++) {
			if (nums[j] < nums[l])
				swap(nums, ++i, j);
		}

		swap(nums, l, i);
		return i;
	}
	
	// https://discuss.leetcode.com/topic/35910/java-9-ms-wiggled-and-randomized-quickselect
	public void wiggleSort_onePass_quickSelect(int[] nums) {
	    if (nums.length <= 1) {
	        return;
	    }
	    final Random random = new Random();
	    final int n = nums.length, d = n | 1; // now (1 + 2 * i) % d will go like 1, 3,... wrap-around, 0, 2,...
	    int left = 0, right = nums.length - 1, k = n / 2; // note: 0-based k
	    // Do quickselect for kth LARGEST element. Use wiggle-indexing to get the answer right away.
	    while (true) {
	        //assert left <= right;
	        int p = nums[(1 + 2 * (left + random.nextInt(right - left + 1))) % d];
	        int l = left, m = l;
	        for (int r = right, lw = (1 + 2 * l) % d, mw = lw, rw = (1 + 2 * r) % d; m <= r; ) {
	        	// descending order. next l and m map to lw and mw.
	            if (nums[mw] > p) {
	                int tmp = nums[mw];
	                nums[mw] = nums[lw];
	                nums[lw] = tmp;
	                ++l; lw = (lw + 2) % d;
	                ++m; mw = (mw + 2) % d;
	            } else if (nums[mw] < p) {
	                int tmp = nums[mw];
	                nums[mw] = nums[rw];
	                nums[rw] = tmp;
	                --r; rw = (rw - 2 + d) % d;  // prevent negative rw
	            } else { // ==
	                ++m; mw = (mw + 2) % d;
	            }
	        }
	        // l, m, r are the new index for next exchange. So we don't need to +1.
	        if (k < l - left) {
	            right = l - 1;
	        } else if (k >= m - left) {
	            k -= m - left;
	            left = m;
	        } else {
	            // else we got lucky - p is the median
	            return;
	        }
	    }
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/33557/java-18-ms-true-o-1-space-cheated-o-n-time-using-binary-search
	 */
	public void wiggleSort_binarySearch(int[] nums) {
	    if (nums.length <= 1) {
	        return;
	    }
	    int p = bsSelect(nums, (nums.length - 1) / 2 + 1);
	    // Reverse Dutch National Flag with Wiggle Indexing (StefanPochmann's Virtual Indexing).
	    // Thanks to apolloydy for reversing this thing.
	    final int n = nums.length;
	    int m = 0, r = nums.length - 1;    // for condition
	    int lw = 1, mw = 1, rw = (1 + 2 * (nums.length - 1)) % (n | 1);  // swap index
	    while (m <= r) {
	        if (nums[mw] > p) {
	            int tmp = nums[mw];
	            nums[mw] = nums[lw];
	            nums[lw] = tmp;
	            mw = (mw + 2) % (n | 1);
	            ++m;
	            lw = (lw + 2) % (n | 1);
	        } else if (nums[mw] < p) {
	            int tmp = nums[mw];
	            nums[mw] = nums[rw];
	            nums[rw] = tmp;
	            rw = (rw - 2 + (n | 1)) % (n | 1);
	            --r;
	        } else {
	            mw = (mw + 2) % (n | 1);
	            ++m;
	        }
	    }
	}
	private int bsSelect(int[] nums, int k) {
	    if (k < 1 || k > nums.length) {
	        throw new IllegalArgumentException("length=" + nums.length + " k=" + k);
	    }
	    int left = Integer.MIN_VALUE, right = Integer.MAX_VALUE;
	    while (left <= right) {
	        int mid = (left < 0 && right > 0) ? (left + right) / 2 : left + (right - left) / 2;  // avoid overflow
	        int cl = 0, cg = 0, d = 0;
	        for (int n : nums) {
	        	// doesn't count n = mid
	            if (n < mid) {
	                if (++cl > k - 1) {
	                    d = +1; // mid larger than kth
	                    break;
	                }
	            } else if (n > mid) {
	                if (++cg > (nums.length - k)) {
	                    d = -1; // mid smaller than kth
	                    break;
	                }
	            }
	        }
	        if (d == 0) {
	            return mid;
	        } else if (d < 0) {
	            left = mid + 1;
	        } else {
	            right = mid - 1;
	        }
	    }
	    throw new AssertionError();
	}
	
	/*
	 * Similar : https://discuss.leetcode.com/topic/66299/my-java-solution-it-is-fast-and-easy-to-understand/
	 */
	void wiggleSort_sort(int[] nums) {
		int[] result = new int[nums.length];
		Arrays.sort(nums);
		int i = nums.length - 1;
		for (int k = 1; k < nums.length; k += 2) {
			result[k] = nums[i];
			i--;
		}
		for (int j = 0; j < nums.length; j += 2) {
			result[j] = nums[i];
			i--;
		}
		for (int m = 0; m < nums.length; m++) {
		    nums[m] = result[m];
		}
	}
	
	void swap (int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/33689/randomized-solution-maybe-o-n-and-o-1
	 */
	void wiggleSort_back_shuffle(int[] nums) {
        if (nums == null) {
            return;
        }
        int len = nums.length;
        int tmp;
        int j;
        boolean odd;
        boolean done = false;
        if (len == 1) {
            done = true;
        }
        
        while (!done) {
            for (int i = 0; i < len - 1; i++) {
                odd = !(i % 2 == 0);
                if (!odd && nums[i] > nums[i + 1]
                    || odd && nums[i] < nums[i + 1]) {
                    tmp = nums[i];
                    nums[i] = nums[i + 1];
                    nums[i + 1] = tmp;
                }
                if (nums[i] == nums[i + 1]) {
                    j = i + 2;
                    if (odd) {
                        while (j < len && nums[j] >= nums[i]) {
                            j++;
                        }
                    }
                    else {
                        while (j < len && nums[j] <= nums[i]) {
                            j++;
                        }
                    }
                    if (j >= len) {
                        shuffle(nums);
                        break;
                    }
                    tmp = nums[i + 1];
                    nums[i + 1] = nums[j];
                    nums[j] = tmp;
                }
                if (i == len - 2) {
                    done = true;
                }
            }
        }
	}
	private void shuffle(int[] n) {
		Random r = new Random();
		int len = n.length;
		int j;
		int tmp;
		for (int i = 0; i < len - 1; i++) {
			j = r.nextInt(len - i - 1) + i + 1;
			tmp = n[i];
			n[i] = n[j];
			n[j] = tmp;
		}
	}
	
	// verify
	static boolean verify(int[] nums) {
		for (int i = 0; i < nums.length - 1; i++) {
			if ((i % 2 == 0) && nums[i] >= nums[i + 1]) {
				return false;
			}
			if ((i % 2 == 1) && nums[i] <= nums[i + 1]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Wiggle_Sort_II wiggle = new Wiggle_Sort_II();
		int[] a = {4, 5, 5, 6};
		//int[] a = { 1, 5, 1, 1, 6, 4 };
		//int[] a = { 1, 3, 2, 2, 2, 1, 1, 3, 1, 1, 2 };
		//int[] a = {1,1,2,1,2,2,1};    // 1 2 1 2 1 2 1
		//int[] a = {1,5,1,1,6,4};
		
		wiggle.wiggleSort(a);
		System.out.println(verify(a));
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}

	}

}
