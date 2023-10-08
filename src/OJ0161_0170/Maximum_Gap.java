package OJ0161_0170;

import java.util.Arrays;

public class Maximum_Gap {
	/*
	 * https://leetcode.com/articles/maximum-gap/
	 * 
	 * 假設排序過後為等差數列
	 * 等差數列：首項為 min，末項為 max，共有 n 項；可得到 公差 t = (max - min)/(n - 1)
	 * 我們可以將 bucket 內部的 maximum gap 設為這個 t 
	 * 這樣我們只需要比較 bucket 間的差 (difference) 就好
	 * bucket 間的差 (difference) = minimum_in_bucket_i+1 - maximum_in_bucket_i
	 * 
	 * 若此數列除了 a[i - 1], a[i], a[i + 1] 外，差 (difference) 都為 t
	 * 而 a[i] - a[i - 1] = t - p, a[i + 1] - a[i] = t + p
	 * 因此，排序過後相鄰兩數的 maximum gap 只會大於 t，不會變小
	 * 
	 * 考慮放入 bucket 的情況，繼續上面的例子
	 * 因為 bucket 內部的 maximum gap 為 t = (max - min)/(n - 1)
	 * 所以只要相鄰兩數 a[j], a[j + 1] 的差 a[j + 1] - a[j] <= t，這兩數就會在同一 bucket 中
	 * a[i - 1] 和 a[i] 會被放到同一個 bucket, 其他的都是自己一個 bucket
	 * 比較 bucket 之間的 difference，可以得到 a[i + 1] - a[i] = t + p
	 * 
	 * 以此類推，只要有任意兩數 a[j], a[j + 1]，而 a[j + 1] - a[j] < t
	 * a[j] 和 a[j + 1] 就會在同一個 bucket 中
	 * 而且必定會有另外兩數 a[k], a[k + 1]，而 a[k + 1] - a[k] > t
	 * a[k] 和 a[k + 1] 就會在相鄰的 bucket 中
	 * 所以 a[k + 1] - a[k] 就是 bucket 間的差 (difference)
	 * 
	 * 因此只要數列的最大 max，最小 min，共有 n 項。不管分布如何
	 * 可以將  bucket 內部的 maximum gap 設為 t = (max - min)/(n - 1)
	 * 我們就可以只比較 bucket 間的差 (difference) 來得到排序過後相鄰 2 數的 maximum gap
	 * 
	 * Sorted array and every adjacent pair of elements differ by the same value. 
	 * n elements of the array, there are (n - 1) gaps, each of width, 
	 * t = (max - min)/(n - 1) is the smallest value of maximum gap in the same 
	 * number of elements and the same range.
	 * 
	 * Setting the buckets to be smaller than t = (max - min)/(n - 1). Since the gaps 
	 * (between elements) within the same bucket would only be <= t, we could deduce 
	 * that the maximal gap would indeed occur only between two adjacent buckets.
	 * bucket size b to be 1 < b <= (max - min)/(n-1)
	 * 
	 * n elements would be divided among k = ceil((max - min)/b) buckets.
	 * ith bucket: [min + (i - 1) * b, min + i * b) (1-based indexing).
	 * the index of the bucket to which a particular element belongs:
	 * floor((num - min)/b) (0-based indexing) where num is the element
	 */
	public int maximumGap(int[] num) {
	    if (num == null || num.length < 2)
	        return 0;
	    
	    // get the max and min value of the array
	    int min = num[0];
	    int max = num[0];
	    for (int i : num) {
	        min = Math.min(min, i);
	        max = Math.max(max, i);
	    }
	    
	    // 假設是等差數列，求出公差。公差至少為 1，這樣才能決定 bucket 數量
	    int gap = Math.max(1, (max - min) / (num.length - 1)); // [1,1,1,1,1,5,5,5,5,5]
	    // 已知首項、末項、公差，求出共有幾項
	    int bucketNum = ((max - min) / gap) + 1;  // [100,3,2,1]
	    
	    int[] bucketsMIN = new int[bucketNum]; // store the min value in that bucket
	    int[] bucketsMAX = new int[bucketNum]; // store the max value in that bucket
	    Arrays.fill(bucketsMIN, Integer.MAX_VALUE);
	    Arrays.fill(bucketsMAX, Integer.MIN_VALUE);
	    
	    // put numbers into buckets
	    for (int i : num) {
	        int idx = (i - min) / gap; // index of the right position in the buckets
	        bucketsMIN[idx] = Math.min(i, bucketsMIN[idx]);
	        bucketsMAX[idx] = Math.max(i, bucketsMAX[idx]);
	    }
	    
	    // scan the buckets for the max gap
	    int maxGap = 0;
	    int previous = min;
	    for (int i = 0; i < bucketNum; i++) {
	    	// empty bucket
	        if (bucketsMIN[i] == Integer.MAX_VALUE 
	        		&& bucketsMAX[i] == Integer.MIN_VALUE)
	            continue;
	        
	        // min value minus the previous value is the current gap
	        maxGap = Math.max(maxGap, bucketsMIN[i] - previous);
	        // update previous bucket value
	        previous = bucketsMAX[i];
	    }
	    return maxGap;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space
	 * 
	 * The maximum gap will be no smaller than ceiling[(max - min ) / (N - 1)].
	 * If bucket size is smaller than this gap, we could deduce that the maximal gap 
	 * would indeed occur only between two adjacent buckets.
	 * k-th bucket contains all numbers in [min + (k-1) * gap, min + k * gap)
	 * 
	 * Rf : 
	 * https://leetcode.com/articles/maximum-gap/
	 * leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/51216
	 */
	public int maximumGap_bucket(int[] num) {
	    if (num == null || num.length < 2)
	        return 0;
	    
	    // get the max and min value of the array
	    int min = num[0];
	    int max = num[0];
	    for (int i : num) {
	        min = Math.min(min, i);
	        max = Math.max(max, i);
	    }
	    
	    // the minimum possible gap, ceiling of the integer division
	    int gap = (int)Math.ceil((double)(max - min)/(num.length - 1));
	    int[] bucketsMIN = new int[num.length - 1]; // store the min value in that bucket
	    int[] bucketsMAX = new int[num.length - 1]; // store the max value in that bucket
	    Arrays.fill(bucketsMIN, Integer.MAX_VALUE);
	    Arrays.fill(bucketsMAX, Integer.MIN_VALUE);
	    
	    // put numbers into buckets
	    for (int i : num) {
	        if (i == min || i == max)
	            continue;
	        
	        int idx = (i - min) / gap; // index of the right position in the buckets
	        bucketsMIN[idx] = Math.min(i, bucketsMIN[idx]);
	        bucketsMAX[idx] = Math.max(i, bucketsMAX[idx]);
	    }
	    
	    // scan the buckets for the max gap
	    int maxGap = Integer.MIN_VALUE;
	    int previous = min;
	    for (int i = 0; i < num.length - 1; i++) {
	        if (bucketsMIN[i] == Integer.MAX_VALUE && bucketsMAX[i] == Integer.MIN_VALUE)
	            // empty bucket
	            continue;
	        
	        // min value minus the previous value is the current gap
	        maxGap = Math.max(maxGap, bucketsMIN[i] - previous);
	        // update previous bucket value
	        previous = bucketsMAX[i];
	    }
	    maxGap = Math.max(maxGap, max - previous); // update the final max value gap
	    return maxGap;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-gap/discuss/50642/Radix-sort-solution-in-Java-with-explanation
	 * 
	 * Other code:
	 * leetcode.com/problems/maximum-gap/discuss/50642/Radix-sort-solution-in-Java-with-explanation/51199
	 */
	public int maximumGap_radix_sort(int[] nums) {
		if (nums == null || nums.length < 2) {
			return 0;
		}

		// m is the maximal number in nums. The threshold to end while loop.
		int m = nums[0];
		for (int i = 1; i < nums.length; i++) {
			m = Math.max(m, nums[i]);
		}

		// Radix sort algorithm to sort based on each digit
		
		int exp = 1; // 1, 10, 100, 1000 ...
		int R = 10; // 10 digits

		int[] aux = new int[nums.length];

		while (m / exp > 0) { // Go through all digits from LSB to MSB
			// Count array stores the index to access aux array which stores the
			// numbers after sorting based on the current digit.
			int[] count = new int[R];

			for (int i = 0; i < nums.length; i++) {
				count[(nums[i] / exp) % 10]++;
			}

			for (int i = 1; i < count.length; i++) {
				count[i] += count[i - 1];
			}

			// From back to front for stable sort
			for (int i = nums.length - 1; i >= 0; i--) {
				aux[--count[(nums[i] / exp) % 10]] = nums[i];
			}

			for (int i = 0; i < nums.length; i++) {
				nums[i] = aux[i];
			}
			exp *= 10;
		}

		int max = 0;
		for (int i = 1; i < aux.length; i++) {
			max = Math.max(max, aux[i] - aux[i - 1]);
		}

		return max;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/maximum-gap/discuss/50680/java-radix-sort
	 * 
	 * The question told us that every number has 32 bits. 
	 * This can remind us to use radix sort whose run time and space are both linear.
	 */
	public int maximumGap_radix_base_2(int[] nums) {
		if (nums == null || nums.length == 0 || nums.length == 1) {
			return 0;
		}
		
		radixsort_base2(nums);
		
		int res = 0;
		for (int i = 1; i < nums.length; i++) {
			res = Math.max(res, nums[i] - nums[i - 1]);
		}
		return res;
	}

	protected void radixsort_base2(int[] nums) {
		int[] temp = new int[nums.length];
		for (int i = 0; i < 32; i++) {
			int[] c = new int[2];
			
			for (int num : nums) {
				int digit = num >> i;
		
				if ((digit & 1) == 0)
					c[0]++;
				else
					c[1]++;
			}
			
			c[1] = c[0] + c[1];
			
			for (int j = nums.length - 1; j >= 0; j--) {
				int digit = (nums[j] >> i) & 1;
				
				temp[c[digit] - 1] = nums[j];
				c[digit]--;
			}
			
			for (int j = 0; j < nums.length; j++) {
				nums[j] = temp[j];
			}
		}
	}
	
	// by myself
	public int maximumGap_self(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            max = Math.max(nums[i], max);
        }
        
        int bucketNum = (int) Math.log10(max) + 1;
        int[][] bucket = new int[bucketNum][nums.length];
        int[] order = new int[bucketNum];
        for (int i = 0; i < nums.length; i++) {
            int tmp = 0;
            if (nums[i] > 0) {
                tmp = (int) Math.log10(nums[i]);
            }
            bucket[tmp][order[tmp]] = nums[i];
            order[tmp]++;
        }
        
        int k = 0;
        for (int i = 0; i < bucketNum; i++) {
            if (order[i] != 0) {
                for (int j = 1; j < order[i]; j++) {
                    int cur = j;
                    for (int j2 = j - 1; j2 >= 0; j2--) {
                        if (bucket[i][cur] < bucket[i][j2]) {
                            int tmp = bucket[i][cur];
                            bucket[i][cur] = bucket[i][j2];
                            bucket[i][j2] = tmp;
                            cur--;
                        }
                        else {
                            break;
                        }
                    }
                }
                
                for (int j = 0; j < order[i]; j++) {
                    nums[k] = bucket[i][j];
                    k++;
                }
            }
        }
        
        int diff = nums[1] - nums[0];
        for (int i = 1; i < nums.length - 1; i++) {
            diff = Math.max(nums[i + 1] - nums[i], diff);
        }
        return diff;
    }
	
	// Cheat, time: O(n log n)
	public int maximumGap_cheat(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        Arrays.sort(nums);
        int diff = nums[1] - nums[0];
        for (int i = 1; i < nums.length - 1; i++) {
            diff = Math.max(nums[i + 1] - nums[i], diff);
        }
        return diff;
    }
	
	/**
	 * Python collections
	 * 
	 * https://leetcode.com/problems/maximum-gap/discuss/50647/I-solved-it-using-radix-sort
	 * https://leetcode.com/problems/maximum-gap/discuss/50650/Python-bucket-sort-from-official-solution
	 */
	
	/**
	 * C++ collections
	 * 
	 * https://leetcode.com/problems/maximum-gap/discuss/50690/Clean-C++-implementation-of-3-linear-time-sort-alg-with-detailed-explaination
	 * 
	 * C
	 * 
	 * https://leetcode.com/problems/maximum-gap/discuss/50663/Very-simple-solution-accepted-as-best-in-C-well-explained
	 */

}
