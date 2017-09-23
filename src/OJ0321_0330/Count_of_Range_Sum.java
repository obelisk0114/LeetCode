package OJ0321_0330;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

/*
 * https://discuss.leetcode.com/topic/34108/summary-of-the-divide-and-conquer-based-and-binary-indexed-tree-based-solutions
 * https://discuss.leetcode.com/topic/37200/summary-of-bit-divide-and-conquer-and-elses
 */

public class Count_of_Range_Sum {
	// Rf : https://discuss.leetcode.com/topic/31173/my-simple-ac-java-binary-search-code
	public int countRangeSum_binarySearch(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        List<Long> list = new ArrayList<>();
        int ans = 0;
        for (int i = 0; i < n; ++i)
            sums[i + 1] = sums[i] + nums[i];
        
        for (int i = 0; i < sums.length; i++) {
            int low = find(list, sums[i] - upper);
            int up = find(list, sums[i] - lower + 1);
            ans = ans + up - low;
            int index = find(list, sums[i]);
            list.add(index, sums[i]);
        }
        return ans;
    }
    private int find(List<Long> sorted, long target) {
		if (sorted.size() == 0)
			return 0;
		int start = 0;
		int end = sorted.size() - 1;
		if (sorted.get(end) < target)
			return end + 1;
		if (sorted.get(start) >= target)
			return 0;
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if (sorted.get(mid) < target) {
				start = mid + 1;
			} else {
				end = mid;
			}
		}
		if (sorted.get(start) >= target)
			return start;
		return end;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/33738/share-my-solution
	 * 
	 * Preprocess to calculate the prefix sums S[i] = S(0, i), 
	 * then S(i, j) = S[j] - S[i]. Note that here we define S(i, j) as the sum of 
	 * range [i, j) where j exclusive and j > i.
	 * 
	 * Here, after we did the preprocess, we need to solve the problem
	 *    count[i] = count of a <= S[j] - S[i] <= b with j > i
	 *    ans = sum(count[:])
	 * 
	 * The merge sort based solution counts the answer while doing the merge. 
	 * During the merge stage, we have already sorted the left half [start, mid) and 
	 * right half [mid, end). We then iterate through the left half with index i. 
	 * For each i, we need to find two indices k and j in the right half where
	 * 
	 *    j is the first index satisfy sums[j] - sums[i] > upper
	 *    k is the first index satisfy sums[k] - sums[i] >= lower.
	 *    
	 * Then the number of sums in [lower, upper] is j-k. 
	 * 
	 * We also use another index t to copy the elements satisfy sums[t] < sums[i] 
	 * to a cache in order to complete the merge sort.
	 * 
	 * Because the indices k, j, t will only increase but not decrease, each of them 
	 * will only traversal the right half once at most.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/34108/summary-of-the-divide-and-conquer-based-and-binary-indexed-tree-based-solutions/3 
	 * https://discuss.leetcode.com/topic/33738/share-my-solution/42
	 */
	public int countRangeSum_merge(int[] nums, int lower, int upper) {
		int n = nums.length;
		long[] sums = new long[n + 1];
		for (int i = 0; i < n; ++i)
			sums[i + 1] = sums[i] + nums[i];
		return countWhileMergeSort(sums, 0, n + 1, lower, upper);
	}
	private int countWhileMergeSort(long[] sums, int start, int end, int lower, int upper) {
		if (end - start <= 1) // the sum of [nums[0], nums[end]) and the sum of [nums[0], nums[start]]
			return 0;
		int mid = (start + end) / 2;
		int count = countWhileMergeSort(sums, start, mid, lower, upper)
				+ countWhileMergeSort(sums, mid, end, lower, upper);
		int j = mid, k = mid, t = mid;
		long[] cache = new long[end - start];
		
		// i is in the left half, and j is in the right half.
		for (int i = start, r = 0; i < mid; ++i, ++r) {
			// If sums[i_current] = sums[i_pre], k, j and t will not move.
			// Therefore, j-k is the same.
			
			// lower <= S[j] - S[i] <= upper
			while (k < end && sums[k] - sums[i] < lower)
				k++;
			while (j < end && sums[j] - sums[i] <= upper)
				j++;
			while (t < end && sums[t] < sums[i])
				cache[r++] = sums[t++];
			cache[r] = sums[i];
			count += j - k;
		}
		// Copy from sorted cache to sums. Last index is "t" and first index is "start".
		// Therefore, length is t - start.
		System.arraycopy(cache, 0, sums, start, t - start);
		return count;
	}
	
	/*
	 * The following class and 5 functions are from this link.
	 * https://discuss.leetcode.com/topic/34107/java-bst-solution-averagely-o-nlogn
	 */
	private class TreeNode {
		long val = 0;
		int count = 1;
		int leftSize = 0;
		int rightSize = 0;
		TreeNode left = null;
		TreeNode right = null;

		public TreeNode(long v) {
			this.val = v;
			this.count = 1;
			this.leftSize = 0;
			this.rightSize = 0;
		}
	}
	private TreeNode insert(TreeNode root, long val) {
		if (root == null) {
			return new TreeNode(val);
		} else if (root.val == val) {
			root.count++;
		} else if (val < root.val) {
			root.leftSize++;
			root.left = insert(root.left, val);
		} else if (val > root.val) {
			root.rightSize++;
			root.right = insert(root.right, val);
		}
		return root;
	}
	private int countSmaller(TreeNode root, long val) {
		if (root == null) {
			return 0;
		} else if (root.val == val) {
			return root.leftSize;
		} else if (root.val > val) {
			return countSmaller(root.left, val);
		} else {
			return root.leftSize + root.count + countSmaller(root.right, val);
		}
	}
	private int countLarger(TreeNode root, long val) {
		if (root == null) {
			return 0;
		} else if (root.val == val) {
			return root.rightSize;
		} else if (root.val < val) {
			return countLarger(root.right, val);
		} else {
			return countLarger(root.left, val) + root.count + root.rightSize;
		}
	}
    private int rangeSize(TreeNode root, long lower, long upper) {
        int total = root.count + root.leftSize + root.rightSize;
        int smaller = countSmaller(root, lower);    // Exclude everything smaller than lower
        int larger = countLarger(root, upper);      // Exclude everything larger than upper
		return total - smaller - larger;
	}
	public int countRangeSum_BST(int[] nums, int lower, int upper) {
		if (nums.length == 0) {
			return 0;
		}
		long[] sums = new long[nums.length + 1];
		for (int i = 0; i < nums.length; i++) {
			sums[i + 1] = sums[i] + nums[i];
		}
		TreeNode root = new TreeNode(sums[0]);
		int output = 0;
		for (int i = 1; i < sums.length; i++) {
			output += rangeSize(root, sums[i] - upper, sums[i] - lower);
			insert(root, sums[i]);
		}
		return output;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/33749/an-o-n-log-n-solution-via-fenwick-tree
	 * 
	 * Starting from some given index in the "bit" array, when you traverse toward 
	 * the root of the tree, the cumulated quantity will correspond to the mapped 
	 * value.
	 * If the mapping function returns 1 for elements in "cand" that comes from 
	 * sum[i] and 0 for those from (upper + sum[i1]) or (lower + sum[i1] - 1), then 
	 * the cumulated quantity will give the total number of elements only from the 
	 * "sum" array.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/34108/summary-of-the-divide-and-conquer-based-and-binary-indexed-tree-based-solutions/4
	 */
	public int countRangeSum_BIT(int[] nums, int lower, int upper) {
		List<Long> cand = new ArrayList<>();
		cand.add(Long.MIN_VALUE); // make sure no number gets a 0-index.
		cand.add(0L);
		long[] sum = new long[nums.length + 1];
		for (int i = 1; i < sum.length; i++) {
			sum[i] = sum[i - 1] + nums[i - 1];
			cand.add(sum[i]);
			// lower <= sum[i2] - sum[i1] <= upper (with i2 > i1) ; equivalently:
			// lower + sum[i1] - 1 < sum[i2] <= upper + sum[i1] (with i2 > i1)
			cand.add(lower + sum[i - 1] - 1);
			cand.add(upper + sum[i - 1]);
		}
		Collections.sort(cand); // finish discretization

		int[] bit = new int[cand.size()];
		
		// build up the binary indexed tree with only elements from the prefix array "sum"
		for (int i = 0; i < sum.length; i++)
			plus(bit, Collections.binarySearch(cand, sum[i]), 1);
		
		int ans = 0;
		for (int i = 1; i < sum.length; i++) {
			// get rid of visited elements by adding -1 to the corresponding tree nodes
			plus(bit, Collections.binarySearch(cand, sum[i - 1]), -1);
			
			ans += query(bit, Collections.binarySearch(cand, upper + sum[i - 1]));
			ans -= query(bit, Collections.binarySearch(cand, lower + sum[i - 1] - 1));
		}
		return ans;
	}
	private void plus(int[] bit, int i, int delta) {
		for (; i < bit.length; i += i & -i)
			bit[i] += delta;
	}
	private int query(int[] bit, int i) {
		int sum = 0;
		for (; i > 0; i -= i & -i)
			sum += bit[i];
		return sum;
	}
	
	/*
	 * The following class and 4 functions are from this link.
	 * https://discuss.leetcode.com/topic/33734/java-segmenttree-solution-36ms
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/33734/java-segmenttree-solution-36ms/4
	 * https://en.wikipedia.org/wiki/Segment_tree
	 * https://www.quora.com/Why-does-4-*-N-space-have-to-be-allocated-for-a-segment-tree-where-N-is-the-size-of-the-original-array
	 * https://kerzol.github.io/markdown-mathjax/editor.html
	 */
	class SegmentTreeNode {
		SegmentTreeNode left;
		SegmentTreeNode right;
		int count;
		long min;
		long max;

		public SegmentTreeNode(long min, long max) {
			this.min = min;
			this.max = max;
		}
	}
	private SegmentTreeNode buildSegmentTree(Long[] valArr, int low, int high) {
		if (low > high)
			return null;
		SegmentTreeNode stn = new SegmentTreeNode(valArr[low], valArr[high]);
		if (low == high)
			return stn;
		int mid = (low + high) / 2;
		stn.left = buildSegmentTree(valArr, low, mid);
		stn.right = buildSegmentTree(valArr, mid + 1, high);
		return stn;
	}
	private void updateSegmentTree(SegmentTreeNode stn, Long val) {
		if (stn == null)
			return;
		if (val >= stn.min && val <= stn.max) {
			stn.count++;
			updateSegmentTree(stn.left, val);
			updateSegmentTree(stn.right, val);
		}
	}
	private int getCount(SegmentTreeNode stn, long min, long max) {
		if (stn == null)
			return 0;
		if (min > stn.max || max < stn.min)
			return 0;
		if (min <= stn.min && max >= stn.max)
			return stn.count;
		return getCount(stn.left, min, max) + getCount(stn.right, min, max);
	}
	public int countRangeSum_Segment_Tree(int[] nums, int lower, int upper) {
		if (nums == null || nums.length == 0)
			return 0;
		
		int ans = 0;
		Set<Long> valSet = new HashSet<Long>();
		long sum = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += (long) nums[i];
			valSet.add(sum);
		}
		Long[] valArr = valSet.toArray(new Long[0]);

		Arrays.sort(valArr);
		SegmentTreeNode root = buildSegmentTree(valArr, 0, valArr.length - 1);

		for (int i = nums.length - 1; i >= 0; i--) {
			updateSegmentTree(root, sum);
			sum -= (long) nums[i];
			ans += getCount(root, (long) lower + sum, (long) upper + sum);
		}
		return ans;
	}
	
	// https://discuss.leetcode.com/topic/54668/share-my-java-solution-using-treemap
	public int countRangeSum_TreeMap(int[] nums, int lower, int upper) {
		if (nums == null || nums.length == 0)
			return 0;
		TreeMap<Long, Long> tr = new TreeMap<Long, Long>();
		tr.put((long) 0, (long) 1);
		long sum = 0;
		long count = 0;
		for (int i = 0; i < nums.length; i++) {
			/*
			 * lower <= S[j] - S[i] <= upper ; lower - S[j] <= -S[i] <= upper - S[j]
			 * S[j] - lower >= S[i] >= S[j] - upper 
			 */
			sum += nums[i];
			long from = sum - upper;
			long to = sum - lower;
			Map<Long, Long> sub = tr.subMap(from, true, to, true);
			for (Long value : sub.values()) {
				count += value;
			}
			if (tr.containsKey(sum)) {
				tr.put(sum, tr.get(sum) + 1);
			} else {
				tr.put(sum, (long) 1);
			}
		}
		return (int) count;
	}
	
	// https://discuss.leetcode.com/topic/34241/java-red-black-tree-72-ms-solution

}
