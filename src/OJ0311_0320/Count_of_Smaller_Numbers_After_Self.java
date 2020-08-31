package OJ0311_0320;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;

public class Count_of_Smaller_Numbers_After_Self {
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Traverse from the back to the beginning of the array, maintain an sorted array 
	 * of numbers have been visited. Use findIndex() to find the first element in the 
	 * sorted array which is larger or equal to target number.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76576/my-simple-ac-java-binary-search-code
	 */
	public List<Integer> countSmaller_binarySearch_self(int[] nums) {
		Integer[] ans = new Integer[nums.length];
		
		List<Integer> sorted = new ArrayList<Integer>();
		for (int i = nums.length - 1; i >= 0; i--) {
			int index = findIndex_binarySearch_self(sorted, nums[i]);
			ans[i] = index;
			sorted.add(index, nums[i]);
		}
		
		return Arrays.asList(ans);
	}
	private int findIndex_binarySearch_self(List<Integer> sorted, int target) {
		int start = 0;
        int end = sorted.size();
		while (start < end) {
			int mid = start + (end - start) / 2;
			if (sorted.get(mid) < target) {
				start = mid + 1;
			} 
            else {
				end = mid;
			}
		}
		return start;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/31173/my-simple-ac-java-binary-search-code
	 * 
	 * Traverse from the back to the beginning of the array, maintain an sorted array 
	 * of numbers have been visited. Use findIndex() to find the first element in the 
	 * sorted array which is larger or equal to target number.
	 */
	public List<Integer> countSmaller_binarySearch2(int[] nums) {
		Integer[] ans = new Integer[nums.length];
		List<Integer> sorted = new ArrayList<Integer>();
		for (int i = nums.length - 1; i >= 0; i--) {
			int index = findIndex_binarySearch2(sorted, nums[i]);
			ans[i] = index;
			sorted.add(index, nums[i]);
		}
		return Arrays.asList(ans);
	}
	private int findIndex_binarySearch2(List<Integer> sorted, int target) {
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
	 * The following class and 2 functions are from this link.
	 * https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76580/9ms-short-java-bst-solution-get-answer-when-building-bst
	 * 
	 * Every node will maintain a val `sum` recording the total of number on it's left 
	 * bottom side, `dup` counts the duplication.
	 * 
	 * When we try to insert a number, the total number of smaller number would be 
	 * adding `dup` and `sum` of the nodes where we turn right.
	 * 
	 * [3, 2, 2, 6, 1], from back to beginning
	 * 
	 *         1(0, 1)
                   \
                    6(3, 1)
                    /
                   2(0, 2)
                     \
                      3(0, 1)
	 * 
	 * if we insert 5, it should be inserted on the way down to the right of 3, the 
	 * nodes where we turn right is 1(0,1), 2,(0,2), 3(0,1), so the answer should be 
	 * (0 + 1)+(0 + 2)+ (0 + 1) = 4
	 * 
	 * if we insert 7, the right-turning nodes are 1(0,1), 6(3,1), so answer should 
	 * be (0 + 1) + (3 + 1) = 5
	 * 
	 * Add the nodes to the tree using the reverse order of the array.
	 * So, all the numbers after it self will be store in the tree before it.
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/31160/share-my-ac-bst-solution
	 * https://discuss.leetcode.com/topic/31422/easiest-java-solution
	 * 
	 * Rf : https://en.wikipedia.org/wiki/AVL_tree
	 */
	class Node {
        Node left, right;
        int val, sum, dup = 1;
        public Node(int v, int s) {
            val = v;
            sum = s;
        }
    }
    public List<Integer> countSmaller_BST(int[] nums) {
        Integer[] ans = new Integer[nums.length];
        Node root = null;
        for (int i = nums.length - 1; i >= 0; i--) {
            root = insert(nums[i], root, ans, i, 0);
        }
        return Arrays.asList(ans);
    }
    private Node insert(int num, Node node, Integer[] ans, int i, int preSum) {
        if (node == null) {
            node = new Node(num, 0);
            ans[i] = preSum;
        } else if (node.val == num) {
            node.dup++;            // duplicates
            ans[i] = preSum + node.sum;
        } else if (node.val > num) {
            node.sum++;            // remember to update the path nodes info
            node.left = insert(num, node.left, ans, i, preSum);
        } else {
            node.right = insert(num, node.right, ans, i, preSum + node.dup + node.sum);
        }
        return node;
    }
    
    /*
     * The following variable and 3 functions are from this link.
     * https://discuss.leetcode.com/topic/31554/11ms-java-solution-using-merge-sort-with-explanation
     * 
     * The basic idea is to do merge sort to nums[]. To record the result, we need to 
     * keep the index of each number in the original array. So instead of sort the 
     * number in nums, we sort the indexes of each number.
     * 
     * While doing the merge part, say that we are merging left[] and right[], 
     * left[] and right[] are already sorted.
     * 
     * We keep a rightcount to record how many numbers from right[] we have added 
     * and keep an array count[] to record the result.
     * 
     * When we move a number from right[] into the new sorted array, we increase 
     * rightcount by 1.
     * When we move a number from left[] into the new sorted array, we increase 
     * count[ index of the number ] by rightcount.
     * 
     * Others : https://discuss.leetcode.com/topic/31162/mergesort-solution
     */
	int[] count_merge;
	public List<Integer> countSmaller_merge(int[] nums) {
		List<Integer> res = new ArrayList<Integer>();
		count_merge = new int[nums.length];
		int[] indexes = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			indexes[i] = i;
		}
		
		mergesort(nums, indexes, 0, nums.length - 1);
		for (int i = 0; i < count_merge.length; i++) {
			res.add(count_merge[i]);
		}
		return res;
	}
	private void mergesort(int[] nums, int[] indexes, int start, int end) {
		if (end <= start) {
			return;
		}
		int mid = (start + end) / 2;
		mergesort(nums, indexes, start, mid);
		mergesort(nums, indexes, mid + 1, end);

		merge(nums, indexes, start, end);
	}
	private void merge(int[] nums, int[] indexes, int start, int end) {
		int mid = (start + end) / 2;
		int left_index = start;
		int right_index = mid + 1;
		int rightcount = 0;
		
		int[] new_indexes = new int[end - start + 1];
		int sort_index = 0;
		
		while (left_index <= mid && right_index <= end) {
			if (nums[indexes[right_index]] < nums[indexes[left_index]]) {
				new_indexes[sort_index] = indexes[right_index];
				rightcount++;
				right_index++;
			} else {
				new_indexes[sort_index] = indexes[left_index];
				count_merge[indexes[left_index]] += rightcount;
				left_index++;
			}
			sort_index++;
		}
		while (left_index <= mid) {
			new_indexes[sort_index] = indexes[left_index];
			count_merge[indexes[left_index]] += rightcount;
			left_index++;
			sort_index++;
		}
		while (right_index <= end) {
			new_indexes[sort_index++] = indexes[right_index++];
		}
		
		for (int i = start; i <= end; i++) {
			indexes[i] = new_indexes[i - start];
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/31924/o-nlogn-divide-and-conquer-java-solution-based-on-bit-by-bit-comparison
	 * 
	 * Start from the most significant bit and check bit by bit towards the least 
	 * significant bit. The first bit will tell you the sign of the two numbers 
	 * and you know positive numbers (with sign bit value of 0) will be greater 
	 * than negative numbers (with sign bit value 1). 
	 * If they have the same sign, then you continue to the next bit with the idea 
	 * in mind that numbers with bit value 1 will be greater than those with bit 
	 * value 0.
	 * 
	 * We partition the integers further into two groups depending on the next-bit 
	 * value: those with bit value 1 and those with bit value 0 
	 * (to unify sign partition and other bits partitions, we will call the two 
	 * groups after each partition as highGroup and lowGroup to indicate that all 
	 * the integers in the highGroup will be greater than those in the lowGroup).
	 * 
	 * Since for each integer in the group, we only care about integers to its right, 
	 * it would be better if we scan the group from right to left.
	 */
	public List<Integer> countSmaller_divided_to_1_and_0(int[] nums) {
		List<Integer> res = new ArrayList<>(nums.length);
		List<Integer> index = new ArrayList<>(nums.length);
		for (int i = nums.length - 1; i >= 0; i--) {
			res.add(0);
			index.add(i);
		}

		countSmallerSub(nums, index, 1 << 31, res); // highest bit is sign bit.
		return res;
	}
	private void countSmallerSub(int[] nums, List<Integer> index, int mask, List<Integer> res) {
		if (mask != 0 && index.size() > 1) {
			List<Integer> lowGroup = new ArrayList<>(index.size());
			List<Integer> highGroup = new ArrayList<>(index.size());

			int high = mask < 0 ? 0 : mask;
			for (int i = 0; i < index.size(); i++) {
				if ((nums[index.get(i)] & mask) == high) {
					res.set(index.get(i), res.get(index.get(i)) + lowGroup.size());
					highGroup.add(index.get(i));
				} else {
					lowGroup.add(index.get(i));
				}
			}

			countSmallerSub(nums, lowGroup, mask >>> 1, res);
			countSmallerSub(nums, highGroup, mask >>> 1, res);
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation
	 * 
	 * You can sort the array firstly and then map them to their order number, so 
	 * that this can be solved by a tree which size is equal to the array size.
	 * 
	 * 盢┮Τ计キ簿场 0  map  array, array  index ボ计
	 * 硂 array ボ–计瞷Ω计
	 * "э" ボ盢赣计┮癸莱じ + 1, 瞷Ω计 + 1
	 * "―㎝" ボ盢赣计玡┮Τじ场癬ㄓ, 参璸ゑ硂计临じ瞷Ω计
	 * 
	 * Rf : 
	 * https://zh.wikipedia.org/wiki/%E6%A0%91%E7%8A%B6%E6%95%B0%E7%BB%84
	 * https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/11
	 * https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/5
	 * 
	 */
	public List<Integer> countSmaller_BIT(int[] nums) {
		List<Integer> res = new LinkedList<Integer>();
		if (nums == null || nums.length == 0) {
			return res;
		}
		
		// find min value and minus min by each elements, plus 1 to avoid 0 element
		int min = nums[0];        // int min = Integer.MAX_VALUE
		int max = nums[0];        // int max = Integer.MIN_VALUE 
		for (int i = 1; i < nums.length; i++) {
			min = (nums[i] < min) ? nums[i] : min;
		}
		
		int[] nums2 = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			nums2[i] = nums[i] - min + 1;
			max = Math.max(nums2[i], max);
		}
		
		int[] tree = new int[max];
		for (int i = nums2.length - 1; i >= 0; i--) {
			res.add(0, get(nums2[i] - 1, tree));
			update(nums2[i], tree);
		}
		return res;
	}
	private int get(int i, int[] tree) {      // ―㎝
		int num = 0;
		while (i > 0) {
			num += tree[i];
			i -= i & (-i);
		}
		return num;
	}
	private void update(int i, int[] tree) {  // э
		while (i < tree.length) {
			tree[i]++;
			i += i & (-i);
		}
	}
	
	// https://discuss.leetcode.com/topic/31154/complicated-segmentree-solution-hope-to-find-a-better-one
	
	// https://discuss.leetcode.com/topic/86205/java-5-methods-merge-sort-binary-indexed-tree-binary-search-tree
	
	/*
	 * The following 2 functions are by myself.
	 */
	public List<Integer> countSmaller_self(int[] nums) {
        if (nums.length == 0)
            return new ArrayList<Integer>();
        int[][] store = new int[nums.length][2];
        for (int i = 0; i < nums.length; i++) {
            store[i][0] = nums[i];
            store[i][1] = i;
        }
        
        Arrays.sort(store, new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        
        Integer[] res = new Integer[nums.length];
        int[] indexList = new int[nums.length];
        //System.out.println(nums.length);
        for (int i = 0; i < nums.length; i++) {
            indexList[i] = store[i][1];
            int insert = -(Arrays.binarySearch(indexList, 0, i, store[i][1]) + 1);
            for (int j = insert; j < i; j++) {
                int tmp = indexList[j];
                indexList[j] = indexList[i];
                indexList[i] = tmp;
            }
            //int pos = Arrays.binarySearch(indexList, 0, i + 1, store[i][1]);
            int pos = find_self(indexList, store[i][1], 0, i);
            res[store[i][1]] = i - pos;
        }
        return Arrays.asList(res);
    }
    
    private int find_self(int[] list, int target, int start, int end) {
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (target <= list[mid]) {
                end = mid;
            }
            else {
                start = mid + 1;
            }
        }
        return start;
    }
	
	public static void main(String[] args) {
		Count_of_Smaller_Numbers_After_Self countSmall = new Count_of_Smaller_Numbers_After_Self();
		int[] nums = { -749, -727, -98, 9630, 1824, 1857, 2942, 4598, 2336, 9992, 8654,
				3797, 1109, 3298, 3956, -36, 4351, -353, 2273, 1962, -374, 1866 };
		List<Integer> ans = countSmall.countSmaller_self(nums);
		for (int i : ans) {
			System.out.print(i + " ");
		}
	}

}
