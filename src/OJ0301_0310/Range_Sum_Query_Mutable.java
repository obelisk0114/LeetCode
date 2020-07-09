package OJ0301_0310;

public class Range_Sum_Query_Mutable {
	/*
	 * https://blog.csdn.net/Yaokai_AssultMaster/article/details/79492190
	 * 
	 * BIT, Fenwick Tree，是一種用於高效處理對一個存儲數字的列表進行更新及求前綴和的數據結構。
	 * 
	 * Binary Indexed Tree 求和的基本思想在於，給定需要求和的位置 i，例如 13，我們可以利用其二進制表示法來
	 * 進行分段（或者說分層）求和：
	 * 
	 * 13 = 2^3 + 2^2 + 2^0，則 
	 * 
	 * prefixSum(13) = RANGE(1, 8) + RANGE(9, 12) + RANGE(13, 13) 
	 * (注意此處的 RANGE(x, y) 表示數組中第 x 個位置到第 y 個位置的所有數字求和)。
	 * 
	 * 如果我們將上述的 range sum 提前計算好的話，prefixSum(13) 可以直接由它們相加得到。
	 * 
	 * 構造樹狀數組第一層的過程為例，我們首先需要填充的是數組中第一個數字開始，長度為 "2 的指數" 個數字的區間內的
	 * 數字的累加和。所以圖中分別填充了從第一個數字開始，長度為 2^0, 2^1, 2^2, 2^3的區間的區間和。
	 * 
	 * 下一步我們構造數組的第二層。與上一層類似，我們依然填充餘下的空白中，從第空白處一個位置算起長度為 "2的指數" 
	 * 的區間的區間和。例如 3-3 空白，我們只需填充從位置 3 開始，長度為 1 的區間的和。
	 * 
	 * 再如 9-14 空白，我們需要填充從 9 開始，長度為 2^0 (9-9)，2^1 (9-10)，2^2 (9-12) 的區間和。
	 * 
	 * 類似地，第三層我們填充 7-7，11-11 和 13-14 區間的空白。
	 * 到此為止，我們已經完全的構造了對應於輸入數組的一個樹狀數組。將該數組即為 BIT 
	 * (方便起見，此處對此數組的索引為從 1 開始)。
	 * 
	 * prefixSum(13) = prefixSum(0b00001101)
	 * = BIT[13] + BIT[12] + BIT[8]
	 * = BIT[0b00001101] + BIT[0b00001100] + BIT[0b00001000]
	 * 
	 * 通過上面的例子，我們得知，求前綴和的過程事實上是在樹狀數組所代表的抽象的樹形結構中，不斷移動尋找上一層
	 * 母結點並求和的過程。在這棵抽象的樹種向上移動的過程，其實就是不斷將當前數字的最後一個 1 翻轉為 0 的過程。
	 * 
	 * 給定一個 int x = 13，這個過程可以用如下運算實現：
	 * x = 13 = 0b00001101
	 * -x = -13 = 0b11110011
	 * x & (-x) = 0b00000001
	 * x - (x & (-x)) = 0b00001100
	 * 
	 * 更新數組中的元素
	 * 
	 * 當我們調用 update(idx, delta) 更新了原數組中的某一個數字後，顯然我們也需要更新 
	 * Binary Indexed Tree 中相應的區間和來應對這一改變。(往上層右側更新)
	 * 
	 * 以 update(5, 2) 為例，我們想要給原數組中第 5 個位置的數字加 2，從 5 開始，應當被更新的位置的坐標為
	 * 原坐標加上原坐標二進制表示中最後一個 1 所代表的數字。這一過程和上面求和的過程剛好相反。
	 * 
	 * 以 int x = 5 為例，我們可以用如下運算實現：
	 * x = 5 = 0b00000101
	 * -x = -5 = 0b11111011
	 * x & (-x) = 0b00000001
	 * x + (x & (-x)) = 0b00000110
	 * 
	 * Binary Indexed Tree 的建立
	 * 
	 * Binary Indexed Tree 的建立非常簡單。我們只需初始化一個全為 0 的數組，並對原數組中的每一個位置
	 * 對應的數字調用一次 update(i, delta) 操作即可。這是一個 O(nlogn) 的建立過程。
	 * 
	 * 此外，還存在一個 O(n) 時間建立 Binary Indexed Tree 的算法，其步驟如下(數組下標從 0 開始)：
	 * 
	 * 給定一個長度為 n 的輸入數組 list。
	 *   1. 初始化長度為 n + 1 的 Binary Indexed Tree 數組 bit，並將 list 中的數字對應地放在 bit[1] 
	 *      到  bit[n] 的各個位置。
	 *   2. 對於 1 到 n 的每一個 i，進行如下操作：
	 *      1. 令 j = i + (i & -i)，若 j < n + 1，則 bit[j] = bit[j] + bit[i]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75753/Java-using-Binary-Indexed-Tree-with-clear-explanation
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75766/Java-Binary-Indexed-Tree
	 * https://leetcode.com/problems/range-sum-query-mutable/discuss/75847/Java-7ms-Binary-Index-Tree-solution
	 */
	private int[] nums;
	private int[] bitArr;
	
	// O(nlogn) initialization
//	public BinaryIndexedTree(int[] list) {
//		this.bitArr = new int[list.length + 1];
//		for (int i = 0; i < list.length; i++) {
//			this.update(i, list[i]);
//		}
//	}

	public Range_Sum_Query_Mutable(int[] nums) {
		this.nums = nums;
		
		// O(n) initialization
		this.bitArr = new int[nums.length + 1];
		for (int i = 0; i < nums.length; i++) {
			this.bitArr[i + 1] = nums[i];
		}

		for (int i = 1; i < this.bitArr.length; i++) {
			int j = i + lowbit(i);
			if (j < this.bitArr.length) {
				this.bitArr[j] += this.bitArr[i];
			}
		}
    }

	/**
	 * Set the value to `val` in `idx` of original array
	 */
	public void update(int idx, int val) {
		int delta = val - nums[idx];
		nums[idx] = val;
		
        idx++;
        int j = idx;
		while (j < this.bitArr.length) {
			this.bitArr[j] += delta;
			j += lowbit(j);
		}
	}
	
	/**
	 * Get the sum of elements in the original array up to index `idx`
	 * 
	 * @param idx index of the last element that should be summed. 
	 * @return sum of elements from index 0 to `idx`.
	 */
	public int getSum(int idx) {
		int sum = 0;
		
		idx++;
		
		int j = idx;
		while (j > 0) {
			sum += this.bitArr[j];
			j -= lowbit(j);
		}
		
		return sum;
	}

	public int sumRange(int i, int j) {
		return getSum(j) - getSum(i - 1);
	}
	
	// 返回參數轉為二進位後，最後一個 1 的位置所代表的數值。
	private int lowbit(int x) {
		return x & (-x);
	}
	
	/*
	 * The following variables and functions are from this link.
	 * https://zh.wikipedia.org/wiki/%E6%A0%91%E7%8A%B6%E6%95%B0%E7%BB%84
	 * 
	 * 正如所有的整數都可以表示成 2 的冪和，我們也可以把一串序列表示成一系列子序列的和。採用這個想法，
	 * 我們可將一個前綴和劃分成多個子序列的和，而劃分的方法與數的 2 的冪和具有極其相似的方式。一方面，
	 * 子序列的個數是其二進位表示中 1 的個數，另一方面，子序列代表的 f[i] 的個數也是 2 的冪。
	 */
	class NumArray_wiki_BIT {
		int[] nums_wiki;
		int[] BIT_wiki;
		int n_wiki;

		public NumArray_wiki_BIT(int[] nums) {
			this.nums_wiki = nums;

			n_wiki = nums.length;
			BIT_wiki = new int[n_wiki + 1];
			for (int i = 1; i <= n_wiki; i++) {
	            BIT_wiki[i] = nums[i - 1];
				for (int j = i - 2; j >= i - lowbit_wiki(i); j--) {
	                BIT_wiki[i] += nums[j];
	            }
	        }
	    }

		// 假設現在要將 A[i] 的值增加 delta，那麼，需要將 BIT[i] 覆蓋的區間包含 A[i] 的值都加上 delta
		void update_wiki(int i, int val) {
			int diff = val - nums_wiki[i];
			nums_wiki[i] = val;
	        i++;
			for (int j = i; j <= n_wiki; j += lowbit_wiki(j)) {
	            BIT_wiki[j] += diff;
	        }
		}

		// 1. 首先，將 ans 初始化為 0，將 i 初始化為 k
		// 2. 將 ans 的值加上 BIT[i]
		// 3. 將 i 的值減去 lowbit(i)
		// 4. 重複步驟 2～3，直到 i 的值變為 0
		public int getSum_wiki(int i) {
			int sum = 0;
			i++;
			for (int j = i; j > 0; j -= lowbit_wiki(j)) {
				sum += BIT_wiki[j];
			}
			return sum;
		}

		public int sumRange_wiki(int i, int j) {
			return getSum_wiki(j) - getSum_wiki(i - 1);
		}
		
		// 返回參數轉為二進位後，最後一個 1 的位置所代表的數值。
		private int lowbit_wiki(int x) {
			return x & (-x);
		}
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
