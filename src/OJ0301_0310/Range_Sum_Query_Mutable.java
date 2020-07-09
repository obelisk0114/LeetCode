package OJ0301_0310;

public class Range_Sum_Query_Mutable {
	/*
	 * https://blog.csdn.net/Yaokai_AssultMaster/article/details/79492190
	 * 
	 * BIT, Fenwick Tree�A�O�@�إΩ󰪮ĳB�z��@�Ӧs�x�Ʀr���C��i���s�ΨD�e��M���ƾڵ��c�C
	 * 
	 * Binary Indexed Tree �D�M���򥻫�Q�b��A���w�ݭn�D�M����m i�A�Ҧp 13�A�ڭ̥i�H�Q�Ψ�G�i���ܪk��
	 * �i����q�]�Ϊ̻����h�^�D�M�G
	 * 
	 * 13 = 2^3 + 2^2 + 2^0�A�h 
	 * 
	 * prefixSum(13) = RANGE(1, 8) + RANGE(9, 12) + RANGE(13, 13) 
	 * (�`�N���B�� RANGE(x, y) ��ܼƲդ��� x �Ӧ�m��� y �Ӧ�m���Ҧ��Ʀr�D�M)�C
	 * 
	 * �p�G�ڭ̱N�W�z�� range sum ���e�p��n���ܡAprefixSum(13) �i�H�����ѥ��̬ۥ[�o��C
	 * 
	 * �c�y�𪬼ƲղĤ@�h���L�{���ҡA�ڭ̭����ݭn��R���O�Ʋդ��Ĥ@�ӼƦr�}�l�A���׬� "2 ������" �ӼƦr���϶�����
	 * �Ʀr���֥[�M�C�ҥH�Ϥ����O��R�F�q�Ĥ@�ӼƦr�}�l�A���׬� 2^0, 2^1, 2^2, 2^3���϶����϶��M�C
	 * 
	 * �U�@�B�ڭ̺c�y�Ʋժ��ĤG�h�C�P�W�@�h�����A�ڭ̵̨M��R�l�U���ťդ��A�q�ĪťճB�@�Ӧ�m��_���׬� "2������" 
	 * ���϶����϶��M�C�Ҧp 3-3 �ťաA�ڭ̥u�ݶ�R�q��m 3 �}�l�A���׬� 1 ���϶����M�C
	 * 
	 * �A�p 9-14 �ťաA�ڭ̻ݭn��R�q 9 �}�l�A���׬� 2^0 (9-9)�A2^1 (9-10)�A2^2 (9-12) ���϶��M�C
	 * 
	 * �����a�A�ĤT�h�ڭ̶�R 7-7�A11-11 �M 13-14 �϶����ťաC
	 * �즹����A�ڭ̤w�g�������c�y�F�������J�Ʋժ��@�Ӿ𪬼ƲաC�N�ӼƲէY�� BIT 
	 * (��K�_���A���B�惡�Ʋժ����ެ��q 1 �}�l)�C
	 * 
	 * prefixSum(13) = prefixSum(0b00001101)
	 * = BIT[13] + BIT[12] + BIT[8]
	 * = BIT[0b00001101] + BIT[0b00001100] + BIT[0b00001000]
	 * 
	 * �q�L�W�����Ҥl�A�ڭ̱o���A�D�e��M���L�{�ƹ�W�O�b�𪬼ƲթҥN����H����ε��c���A���_���ʴM��W�@�h
	 * �����I�èD�M���L�{�C�b�o�ʩ�H����ئV�W���ʪ��L�{�A���N�O���_�N��e�Ʀr���̫�@�� 1 ½�ର 0 ���L�{�C
	 * 
	 * ���w�@�� int x = 13�A�o�ӹL�{�i�H�Φp�U�B���{�G
	 * x = 13 = 0b00001101
	 * -x = -13 = 0b11110011
	 * x & (-x) = 0b00000001
	 * x - (x & (-x)) = 0b00001100
	 * 
	 * ��s�Ʋդ�������
	 * 
	 * ��ڭ̽ե� update(idx, delta) ��s�F��Ʋդ����Y�@�ӼƦr��A��M�ڭ̤]�ݭn��s 
	 * Binary Indexed Tree ���������϶��M������o�@���ܡC(���W�h�k����s)
	 * 
	 * �H update(5, 2) ���ҡA�ڭ̷Q�n����Ʋդ��� 5 �Ӧ�m���Ʀr�[ 2�A�q 5 �}�l�A����Q��s����m�����Ь�
	 * �짤�Х[�W�짤�ФG�i���ܤ��̫�@�� 1 �ҥN���Ʀr�C�o�@�L�{�M�W���D�M���L�{��n�ۤϡC
	 * 
	 * �H int x = 5 ���ҡA�ڭ̥i�H�Φp�U�B���{�G
	 * x = 5 = 0b00000101
	 * -x = -5 = 0b11111011
	 * x & (-x) = 0b00000001
	 * x + (x & (-x)) = 0b00000110
	 * 
	 * Binary Indexed Tree ���إ�
	 * 
	 * Binary Indexed Tree ���إ߫D�`²��C�ڭ̥u�ݪ�l�Ƥ@�ӥ��� 0 ���ƲաA�ù��Ʋդ����C�@�Ӧ�m
	 * �������Ʀr�եΤ@�� update(i, delta) �ާ@�Y�i�C�o�O�@�� O(nlogn) ���إ߹L�{�C
	 * 
	 * ���~�A�٦s�b�@�� O(n) �ɶ��إ� Binary Indexed Tree ����k�A��B�J�p�U(�ƲդU�бq 0 �}�l)�G
	 * 
	 * ���w�@�Ӫ��׬� n ����J�Ʋ� list�C
	 *   1. ��l�ƪ��׬� n + 1 �� Binary Indexed Tree �Ʋ� bit�A�ñN list �����Ʀr�����a��b bit[1] 
	 *      ��  bit[n] ���U�Ӧ�m�C
	 *   2. ��� 1 �� n ���C�@�� i�A�i��p�U�ާ@�G
	 *      1. �O j = i + (i & -i)�A�Y j < n + 1�A�h bit[j] = bit[j] + bit[i]
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
	
	// ��^�Ѽ��ର�G�i���A�̫�@�� 1 ����m�ҥN���ƭȡC
	private int lowbit(int x) {
		return x & (-x);
	}
	
	/*
	 * The following variables and functions are from this link.
	 * https://zh.wikipedia.org/wiki/%E6%A0%91%E7%8A%B6%E6%95%B0%E7%BB%84
	 * 
	 * ���p�Ҧ�����Ƴ��i�H��ܦ� 2 �����M�A�ڭ̤]�i�H��@��ǦC��ܦ��@�t�C�l�ǦC���M�C�ĥγo�ӷQ�k�A
	 * �ڭ̥i�N�@�ӫe��M�������h�Ӥl�ǦC���M�A�ӹ�������k�P�ƪ� 2 �����M�㦳����ۦ����覡�C�@�譱�A
	 * �l�ǦC���ӼƬO��G�i���ܤ� 1 ���ӼơA�t�@�譱�A�l�ǦC�N�� f[i] ���ӼƤ]�O 2 �����C
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

		// ���]�{�b�n�N A[i] ���ȼW�[ delta�A����A�ݭn�N BIT[i] �л\���϶��]�t A[i] ���ȳ��[�W delta
		void update_wiki(int i, int val) {
			int diff = val - nums_wiki[i];
			nums_wiki[i] = val;
	        i++;
			for (int j = i; j <= n_wiki; j += lowbit_wiki(j)) {
	            BIT_wiki[j] += diff;
	        }
		}

		// 1. �����A�N ans ��l�Ƭ� 0�A�N i ��l�Ƭ� k
		// 2. �N ans ���ȥ[�W BIT[i]
		// 3. �N i ���ȴ�h lowbit(i)
		// 4. ���ƨB�J 2��3�A���� i �����ܬ� 0
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
		
		// ��^�Ѽ��ର�G�i���A�̫�@�� 1 ����m�ҥN���ƭȡC
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
