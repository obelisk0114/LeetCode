package OJ0411_0420;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.PriorityQueue;

public class Third_Maximum_Number {
	/*
	 * https://leetcode.com/problems/third-maximum-number/discuss/90233/Using-Java-TreeSet/94788
	 * 
	 * Using a TreeSet which contains at most three elements at all time so the 
	 * space complexity is O(1). The time complexity should be n*(3*log(3)), which 
	 * still in O(n).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/third-maximum-number/discuss/90233/Using-Java-TreeSet
	 * 
	 * Other code:
	 * https://leetcode.com/problems/third-maximum-number/discuss/90351/Java-Solution-Using-TreeSet
	 */
	public int thirdMax_TreeSet(int[] nums) {
		TreeSet<Integer> set = new TreeSet<>();
		for (int num : nums)
			if (set.add(num) && set.size() > 3)
				set.pollFirst();
		
		return set.size() > 2 ? set.pollFirst() : set.pollLast();
	}
	
	/*
	 * https://leetcode.com/problems/third-maximum-number/discuss/90190/Java-PriorityQueue-O(n)-+-O(1)/238452
	 * 
	 * We maintain a PriorityQueue whose size is always less than 3. In this way, the 
	 * actual space complexity is O(1).
	 * 
	 * Other code:
	 * https://leetcode.com/problems/third-maximum-number/discuss/90232/Java-solution-PriorityQueue-O(1)-space/232643
	 */
	public int thirdMax_PriorityQueue(int[] nums) {
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		for (int i : nums) {
			if (!pq.contains(i)) {
				pq.offer(i);
				
				if (pq.size() > 3) {
					pq.poll();
				}
			}
		}
		
		if (pq.size() < 3) {
			while (pq.size() > 1) {
				pq.poll();
			}
		}
		return pq.peek();
	}
	
	// https://leetcode.com/problems/third-maximum-number/discuss/90202/Java-neat-and-easy-understand-solution-O(n)-time-O(1)-space
	public int thirdMax_Integer(int[] nums) {
		Integer max1 = null;
		Integer max2 = null;
		Integer max3 = null;
		for (Integer n : nums) {
			if (n.equals(max1) || n.equals(max2) || n.equals(max3))
				continue;
			
			if (max1 == null || n > max1) {
				max3 = max2;
				max2 = max1;
				max1 = n;
			} 
			else if (max2 == null || n > max2) {
				max3 = max2;
				max2 = n;
			} 
			else if (max3 == null || n > max3) {
				max3 = n;
			}
		}
		return max3 == null ? max1 : max3;
	}
	
	/*
	 * https://leetcode.com/problems/third-maximum-number/discuss/90367/Java-solution-with-minimum-value-time-complexity-O(n)-space-complexity-O(1)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/third-maximum-number/discuss/90396/O(n)-time-O(1)-space-Java-short-solution
	 */
	public int thirdMax_long(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		long first = Long.MIN_VALUE;
		long second = Long.MIN_VALUE;
		long third = Long.MIN_VALUE;

		for (int i = 0; i < nums.length; i++) {
			int val = nums[i];
			
			if (val > first) {
				third = second;
				second = first;
				first = val;
			} 
			else if (val < first) {
				if (val > second) {
					third = second;
					second = val;
				} 
				else if (val < second) {
					if (val > third) {
						third = val;
					}
				}
			}
		}
		
		if (third == Long.MIN_VALUE) {
			return ((int) first);
		}
		return ((int) third);
	}
	
	// https://leetcode.com/problems/third-maximum-number/discuss/90290/Java-solution-in-0ms-run-time-O(n)-and-space-O(1)./94815
	public int thirdMax_count(int[] nums) {
		int count = 0, max, mid, small;
		max = mid = small = Integer.MIN_VALUE;
		for (int num : nums) {
			if (count > 0 && num == max || count > 1 && num == mid)
				continue;
			
			count++;
			if (num > max) {
				small = mid;
				mid = max;
				max = num;
			} 
			else if (num > mid) {
				small = mid;
				mid = num;
			} 
			else if (num > small) {
				small = num;
			}
		}
		return count < 3 ? max : small;
	}
	
	// by myself
	public int thirdMax_self(int[] nums) {
        Set<Integer> set = new HashSet<>();
        boolean exist = false;
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        int max3 = Integer.MIN_VALUE;
        
        for (int i = 0; i < nums.length; i++) {
            max1 = Math.max(max1, nums[i]);
            
            if (nums[i] == Integer.MIN_VALUE) {
                exist = true;
            }
        }
        set.add(max1);
        
        for (int i = 0; i < nums.length; i++) {
            if (!set.contains(nums[i])) {
                max2 = Math.max(max2, nums[i]);
            }
        }
        set.add(max2);
        
        for (int i = 0; i < nums.length; i++) {
            if (!set.contains(nums[i])) {
                max3 = Math.max(max3, nums[i]);
            }
        }
        set.add(max3);
        
        if (max3 != Integer.MIN_VALUE || (exist && set.size() == 3)) {
            return max3;
        }
        else {
            return max1;
        }
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/third-maximum-number/discuss/90207/Intuitive-and-Short-Python-solution
     * https://leetcode.com/problems/third-maximum-number/discuss/90201/A-python-amusing-solution-which-actually-beats-98...
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/third-maximum-number/discuss/90209/Short-easy-C%2B%2B-using-set
     * https://leetcode.com/problems/third-maximum-number/discuss/90240/Short-Clear-C%2B%2B-solution-no-set-or-pq.
     */

}
