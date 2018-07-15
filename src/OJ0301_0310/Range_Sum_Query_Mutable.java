package OJ0301_0310;

public class Range_Sum_Query_Mutable {
	/*
	 * The following variables and functions are from this link.
	 * https://zh.wikipedia.org/wiki/%E6%A0%91%E7%8A%B6%E6%95%B0%E7%BB%84
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75753/Java-using-Binary-Indexed-Tree-with-clear-explanation
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75766/Java-Binary-Indexed-Tree
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75847/Java-7ms-Binary-Index-Tree-solution
	 */
	int[] nums;
	int[] BIT;
	int n;

	public Range_Sum_Query_Mutable(int[] nums) {
		this.nums = nums;

		n = nums.length;
		BIT = new int[n + 1];
		for (int i = 1; i <= n; i++) {
            BIT[i] = nums[i - 1];
			for (int j = i - 2; j >= i - (i & -i); j--) {
                BIT[i] += nums[j];
            }
        }
    }

	void update(int i, int val) {
		int diff = val - nums[i];
		nums[i] = val;
        i++;
		for (int j = i; j <= n; j += (j & -j)) {
            BIT[j] += diff;
        }
	}

	public int getSum(int i) {
		int sum = 0;
		i++;
		for (int j = i; j > 0; j -= (j & -j)) {
			sum += BIT[j];
		}
		return sum;
	}

	public int sumRange(int i, int j) {
		return getSum(j) - getSum(i - 1);
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/articles/range-sum-query-mutable/
	 * 
	 * Split the array in blocks with length of sqrt(n). Then we calculate the sum of 
	 * each block and store it in auxiliary memory b. To query RSQ(i, j), we will add 
	 * the sums of all the blocks lying inside and those that partially overlap with 
	 * range [i...j].
	 * 
	 * Rf : https://leetcode.com/problems/range-sum-query-mutable/discuss/75717/C++-solution-using-%22buckets%22.-O(1)-for-updating-and-O(n0.5)-for-query-in-the-worst-case-(not-the-fast).
	 */
	public class NumArray_Block {
		private int[] b;
		private int len;
		private int[] nums;

		public NumArray_Block(int[] nums) {
			this.nums = nums;
			double l = Math.sqrt(nums.length);
			len = (int) Math.ceil(nums.length / l);
			b = new int[len];
			for (int i = 0; i < nums.length; i++)
				b[i / len] += nums[i];
		}

		public int sumRange(int i, int j) {
			int sum = 0;
			int startBlock = i / len;
			int endBlock = j / len;
			if (startBlock == endBlock) {
				for (int k = i; k <= j; k++)
					sum += nums[k];
			} 
			else {
				for (int k = i; k <= (startBlock + 1) * len - 1; k++)
					sum += nums[k];
				for (int k = startBlock + 1; k <= endBlock - 1; k++)
					sum += b[k];
				for (int k = endBlock * len; k <= j; k++)
					sum += nums[k];
			}
			return sum;
		}

		public void update(int i, int val) {
			int b_l = i / len;
			b[b_l] = b[b_l] - nums[i] + val;
			nums[i] = val;
		}
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75724/17-ms-Java-solution-with-segment-tree
	 * 
	 * Rf :
	 * https://leetcode.com/articles/a-recursive-approach-to-segment-trees-range-sum-queries-lazy-propagation/ 
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75824/Java-Segmeng-Tree-Solution-with-Explaination
	 */
	public class NumArray_SegmentTree {

		class SegmentTreeNode {
			int start, end;
			SegmentTreeNode left, right;
			int sum;

			public SegmentTreeNode(int start, int end) {
				this.start = start;
				this.end = end;
				this.left = null;
				this.right = null;
				this.sum = 0;
			}
		}

		SegmentTreeNode root = null;

		public NumArray_SegmentTree(int[] nums) {
			root = buildTree(nums, 0, nums.length - 1);
		}

		private SegmentTreeNode buildTree(int[] nums, int start, int end) {
			if (start > end) {
				return null;
			}
			
			SegmentTreeNode ret = new SegmentTreeNode(start, end);
			if (start == end) {
				ret.sum = nums[start];
			} 
			else {
				int mid = start + (end - start) / 2;
				ret.left = buildTree(nums, start, mid);
				ret.right = buildTree(nums, mid + 1, end);
				ret.sum = ret.left.sum + ret.right.sum;
			}
			return ret;
		}

		void update(int i, int val) {
			update(root, i, val);
		}

		void update(SegmentTreeNode root, int pos, int val) {
			if (root.start == root.end) {
				root.sum = val;
			} 
			else {
				int mid = root.start + (root.end - root.start) / 2;
				if (pos <= mid) {
					update(root.left, pos, val);
				} 
				else {
					update(root.right, pos, val);
				}
				root.sum = root.left.sum + root.right.sum;
			}
		}

		public int sumRange(int i, int j) {
			return sumRange(root, i, j);
		}

		public int sumRange(SegmentTreeNode root, int start, int end) {
			if (root.end == end && root.start == start) {
				return root.sum;
			}
			
			int mid = root.start + (root.end - root.start) / 2;
			if (end <= mid) {
				return sumRange(root.left, start, end);
			} 
			else if (start >= mid + 1) {
				return sumRange(root.right, start, end);
			} 
			else {
				return sumRange(root.right, mid + 1, end) + sumRange(root.left, start, mid);
			}
		}
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/articles/range-sum-query-mutable/
	 * 
	 * Rf : 
	 * http://codeforces.com/blog/entry/18051
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75763/7ms-Java-solution-using-bottom-up-segment-tree
	 */
	public class NumArray_SegmentTree_array {

		int[] tree;
		int n;

		public NumArray_SegmentTree_array(int[] nums) {
			if (nums.length > 0) {
				n = nums.length;
				tree = new int[n * 2];
				buildTree(nums);
			}
		}

		private void buildTree(int[] nums) {
			for (int i = n, j = 0; i < 2 * n; i++, j++)
				tree[i] = nums[j];
			for (int i = n - 1; i > 0; i--)
				tree[i] = tree[i * 2] + tree[i * 2 + 1];
		}

		void update(int pos, int val) {
			pos += n;
			tree[pos] = val;
			while (pos > 0) {
				int left = pos;
				int right = pos;
				
				if (pos % 2 == 0) {
					right = pos + 1;
				} 
				else {
					left = pos - 1;
				}
				
				// parent is updated after child is updated
				tree[pos / 2] = tree[left] + tree[right];
				pos /= 2;
			}
		}

		public int sumRange(int l, int r) {
			// get leaf with value 'l'
			l += n;
			// get leaf with value 'r'
			r += n;
			int sum = 0;
			
			while (l <= r) {
				if ((l % 2) == 1) {
					sum += tree[l];
					l++;
				}
				if ((r % 2) == 0) {
					sum += tree[r];
					r--;
				}
				
				l /= 2;
				r /= 2;
			}
			return sum;
		}
	}
	
	// https://leetcode.com/problems/range-sum-query-mutable/discuss/75795/Three-implementations:-binary-index-tree-7ms-segment-tree-array-14ms-segment-tree-20ms-with-comments
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75846/Simple-recursive-Java-solution
	 * 
	 * Each NumArray object of size n > 1 stores a pointer to another NumArray object 
	 * of size (n + 1)/2 such that element i in the parent object stores the sum of 
	 * the elements 2i and 2i + 1 in the original object. Sums and updates are rapid 
	 * because they just amount to climbing the tree.
	 */
	public class NumArray_recursive {
		int n;
		int[] raw;
		NumArray_recursive parent;

		public NumArray_recursive(int[] nums) {
			n = nums.length;
			raw = nums;
			if (n > 1) {
				int[] parRaw = new int[(n + 1) / 2];
				for (int i = 0; i < n; i++) {
					parRaw[i / 2] += nums[i];
				}
				
				parent = new NumArray_recursive(parRaw);
			}
		}

		public void update(int i, int val) {
			if (n > 1) {
				parent.update(i / 2, parent.get(i / 2) - raw[i] + val);
			}
			raw[i] = val;
		}

		public int get(int i) {
			return raw[i];
		}

		public int sumRange(int i, int j) {
			if (i > 0) {
				return sumRange(0, j) - sumRange(0, i - 1);
			} 
			else if (j == 0) {
				return raw[0];
			} 
			else {
				int sum = parent.sumRange(0, j / 2);
				if (j % 2 == 0 && j + 1 < n) {
					sum -= raw[j + 1];
				}
				
				return sum;
			}
		}
	}
	
	// The following class and functions are by myself.
	class NumArray_self {
	    private int[] nums;
	    private int[] sums;

	    public NumArray_self(int[] nums) {
	        this.nums = nums;
	        sums = new int[nums.length + 1];
	        for (int i = 1; i < sums.length; i++) {
	            sums[i] = sums[i - 1] + nums[i - 1];
	        }
	    }
	    
	    public void update(int i, int val) {
	        for (int j = i + 1; j < sums.length; j++) {
	            sums[j] = sums[j] + (val - nums[i]);
	        }
	        nums[i] = val;
	    }
	    
	    public int sumRange(int i, int j) {
	        return sums[j + 1] - sums[i];
	    }
	}
	
	/**
	 * Your NumArray object will be instantiated and called as such:
	 * NumArray obj = new NumArray(nums);
	 * obj.update(i,val);
	 * int param_2 = obj.sumRange(i,j);
	 */

}
