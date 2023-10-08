package OJ0301_0310;

public class Range_Sum_Query_2D_Mutable {
	/*
	 * The following class is modified by myself.
	 * 
	 * tree[i][j] saves the rangeSum of (i-(i&-i), i] x (j-(j&-j), j]
	 * the left bound shouldn't be inclusive
	 * BIT index == matrix index + 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/186791
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/199947
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/780254
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79035
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/solution/805834
	 * 
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79036
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79050
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79045
	 * 
	 * https://cs.stackexchange.com/questions/10538/bit-what-is-the-intuition-behind-a-binary-indexed-tree-and-how-was-it-thought-a
	 * https://www.topcoder.com/community/data-science/data-science-tutorials/binary-indexed-trees/
	 * https://www.geeksforgeeks.org/two-dimensional-binary-indexed-tree-or-fenwick-tree/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/808221
	 */
	class NumMatrix_BIT3 {
		private int[][] tree;
		private int[][] nums;
		
		public NumMatrix_BIT3(int[][] matrix) {
			if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
				return;
			
			int m = matrix.length;
			int n = matrix[0].length;
			tree = new int[m + 1][n + 1];
			
			// deep clone matrix for reference, to prevent other process change matrix
			nums = new int[m][n];
			for (int i = 0; i < nums.length; i++) {
				for (int j = 0; j < nums[0].length; j++) {
					nums[i][j] = matrix[i][j];
				}
			}
			
			// initialization, 也可以用 init2(); 或 init3();
			// init2();
			// init3();
			init();
		}
		
		// first creating all the "rows" BIT separately. 
		// After all the rows are done, then we do the same thing, 
		// but now do it column-wise for all the "columns" BIT.
		public void init() {
			for (int i = 0; i < nums.length; i++) {
				for (int j = 0; j < nums[0].length; j++) {
					tree[i + 1][j + 1] = nums[i][j];
				}
			}

			for (int i = 1; i < tree.length; i++) {
				for (int j = 1; j < tree[0].length; j++) {
					int parentJ = j + (j & -j);
					if (parentJ < tree[0].length) {
						tree[i][parentJ] += tree[i][j];
					}
				}
			}

			// treat each column as separate BIT and accumulate the values 
			// just like 1-D BIT
			for (int j = 1; j < tree[0].length; j++) {
				for (int i = 1; i < tree.length; i++) {
					int parentI = i + (i & -i);
					if (parentI < tree.length) {
						tree[parentI][j] += tree[i][j];
					}
				}
			}
		}

		// notice how the tree is initialized in O(mn), rather than O(mnlog(m)log(n))
		// Traverse the cells row by row, when stepping on each cell, 
		// only update the next column's cell and next row's cell who directly 
		// "take charge of" this cell.
		public void init2() {
			for (int i = 1; i < tree.length; i++) {
				int[] temp = new int[nums[0].length + 1];
				
				for (int j = 1; j < tree[0].length; j++) {
					temp[j] += nums[i - 1][j - 1];
					if (j + (j & -j) < temp.length)
						temp[j + (j & -j)] += temp[j];
					
					tree[i][j] += temp[j];
					if (i + (i & -i) < tree.length)
						tree[i + (i & -i)][j] += tree[i][j];
				}
			}
		}
		
		public void init3() {
			// (0, 0) 到 (i, j) 這個矩陣的總和
			for (int i = 0; i < nums.length; ++i) {
				for (int j = 0, cum = 0; j < nums[0].length; ++j) {
					cum += nums[i][j];
					tree[i + 1][j + 1] = tree[i][j + 1] + cum;
				}
			}
			
			// i & i - 1 = i & (i - 1) = i - (i & -i)
			// 求 sub-matrix 的總和，由最大往最小，避免後來的取到錯誤的資料
			for (int i = nums.length; i > 0; --i) {
				for (int j = nums[0].length; j > 0; --j) {
					tree[i][j] = tree[i][j] - tree[i & i - 1][j] 
							     - tree[i][j & j - 1] + tree[i & i - 1][j & j - 1];
				}
			}
		}
		
		public void update(int row, int col, int val) {
			int delta = val - nums[row][col];
			nums[row][col] = val;
			
			// tree is indexed by rLen & cLen, off-by-one index
			// i & (-i) 是 i 轉為二進位後，最後一個 1 的位置所代表的數值。
			for (int i = row + 1; i < tree.length; i += i & (-i)) {
				for (int j = col + 1; j < tree[i].length; j += j & (-j)) {
					tree[i][j] += delta;
				}
			}
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			return sum(row2, col2) + sum(row1 - 1, col1 - 1) 
					- sum(row1 - 1, col2) - sum(row2, col1 - 1);
		}

		public int sum(int row, int col) {
			int sum = 0;
			
			// i & (-i) 是 i 轉為二進位後，最後一個 1 的位置所代表的數值。
			for (int i = row + 1; i > 0; i -= i & (-i)) {
				for (int j = col + 1; j > 0; j -= j & (-j)) {
					sum += tree[i][j];
				}
			}
			
			return sum;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms
	 * 
	 * Using 2D Binary Indexed Tree, 2D BIT Def:
     * bit[i][j] saves the rangeSum of (i-(i&-i), i] x (j-(j&-j), j]
     * note bit index == matrix index + 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79036
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79050
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/79045
	 * https://www.topcoder.com/community/data-science/data-science-tutorials/binary-indexed-trees/
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75870/Java-2D-Binary-Indexed-Tree-Solution-clean-and-short-17ms/147673
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75905/15ms-Very-Concise-Java-Code-Using-BIT
	 */
	class NumMatrix_BIT {

		int[][] tree;
		int[][] nums;
		int m;
		int n;

		public NumMatrix_BIT(int[][] matrix) {
			if (matrix.length == 0 || matrix[0].length == 0)
				return;
			
			m = matrix.length;
			n = matrix[0].length;
			tree = new int[m + 1][n + 1];
			
			// deep clone matrix for reference, to prevent other process change matrix
			nums = new int[m][n];
			
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					update(i, j, matrix[i][j]);
				}
			}
		}

		public void update(int row, int col, int val) {
			if (m == 0 || n == 0)
				return;
			
			int delta = val - nums[row][col];
			nums[row][col] = val;
			
			// tree is indexed by rLen & cLen, off-by-one index
			for (int i = row + 1; i <= m; i += i & (-i)) {
				for (int j = col + 1; j <= n; j += j & (-j)) {
					tree[i][j] += delta;
				}
			}
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			if (m == 0 || n == 0)
				return 0;
			
			return sum(row2 + 1, col2 + 1) + sum(row1, col1) 
					- sum(row1, col2 + 1) - sum(row2 + 1, col1);
		}

		public int sum(int row, int col) {
			int sum = 0;
			for (int i = row; i > 0; i -= i & (-i)) {
				for (int j = col; j > 0; j -= j & (-j)) {
					sum += tree[i][j];
				}
			}
			return sum;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75917/Share-my-Java-2-D-Binary-Indexed-Tree-Solution
	 * 
	 * Initializing the binary indexed tree takes O(mn * logm * logn) time, both 
	 * update() and getSum() take O(logm * logn) time. The arr[][] is used to keep 
	 * a backup of the matrix[][] so that we know the difference of the updated 
	 * element and use that to update the binary indexed tree. The idea of 
	 * calculating sumRegion() is the same as in Range Sum Query 2D - Immutable.
	 * 
	 * In 1D case, if we wanna get sum of first 12 elements, we get first 8 elements 
	 * and then the next 4 elements. For 2D, we just accumulate on the rows.
	 * BITree[4] stores answer for accumulative sum for the first 4 rows.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75917/Share-my-Java-2-D-Binary-Indexed-Tree-Solution/212376
	 */
	class NumMatrix_BIT2 {
		int m, n;
		int[][] arr;     // stores matrix[][]
		int[][] BITree;  // 2-D binary indexed tree

		public NumMatrix_BIT2(int[][] matrix) {
			if (matrix.length == 0 || matrix[0].length == 0) {
				return;
			}

			m = matrix.length;
			n = matrix[0].length;

			arr = new int[m][n];
			BITree = new int[m + 1][n + 1];

			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					update(i, j, matrix[i][j]);  // init BITree[][]
					arr[i][j] = matrix[i][j];    // init arr[][]
				}
			}
		}

		public void update(int i, int j, int val) {
			int diff = val - arr[i][j];  // get the diff
			arr[i][j] = val;             // update arr[][]

			i++;
			j++;
			while (i <= m) {
				int k = j;
				
				while (k <= n) {
					BITree[i][k] += diff; // update BITree[][]
					k += k & (-k);        // update column index to that of parent
				}
				
				i += i & (-i);            // update row index to that of parent
			}
		}

		int getSum(int i, int j) {
			int sum = 0;

			i++;
			j++;
			while (i > 0) {
				int k = j;
				
				while (k > 0) {
					sum += BITree[i][k];  // accumulate the sum
					k -= k & (-k);        // move column index to parent node
				}
				
				i -= i & (-i);            // move row index to parent node
			}
			return sum;
		}

		public int sumRegion(int i1, int j1, int i2, int j2) {
			return getSum(i2, j2) - getSum(i1 - 1, j2) 
					- getSum(i2, j1 - 1) + getSum(i1 - 1, j1 - 1);
		}
	}

	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75874/A-Segment-Tree-Solution-in-Java
	 * 
	 * Each TreeNode stores the sum of the submatrix defined by the corners (x0, y0) 
	 * and (x1, y1)
	 * 
	 * If a given input is out of the range of the node, it returns 0. If the input 
	 * intersects with the range of the node, we consider their intersection. We call 
	 * all the children, and let the recursion does the if-else check.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/436604/Segment-Tree-%2B-Python-%2B-Shorter-intuitive-code
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75863/Segment-Tree-Solution-in-Java/288816
	 */
	class NumMatrix_Segment_Tree2 {

		class TreeNode {
			int row1, row2, col1, col2, sum;
			TreeNode c1, c2, c3, c4;

			public TreeNode(int row1, int col1, int row2, int col2) {
				this.row1 = row1;
				this.col1 = col1;
				this.row2 = row2;
				this.col2 = col2;
				this.sum = 0;
			}
		}

		TreeNode myroot;

		public NumMatrix_Segment_Tree2(int[][] matrix) {
			if (matrix.length == 0 || matrix[0].length == 0)
				return;

			myroot = buildTree(matrix, 0, 0, matrix.length - 1, matrix[0].length - 1);
		}

		private TreeNode buildTree(int[][] matrix, int row1, int col1, 
				int row2, int col2) {
			
			if (row2 < row1 || col2 < col1)
				return null;

			TreeNode node = new TreeNode(row1, col1, row2, col2);
			
			if (row1 == row2 && col1 == col2) {
				node.sum = matrix[row1][col1];
				return node;
			}

			int rowMid = row1 + (row2 - row1) / 2;
			int colMid = col1 + (col2 - col1) / 2;
			
			node.c1 = buildTree(matrix, row1, col1, rowMid, colMid);
			node.c2 = buildTree(matrix, row1, colMid + 1, rowMid, col2);
			node.c3 = buildTree(matrix, rowMid + 1, col1, row2, colMid);
			node.c4 = buildTree(matrix, rowMid + 1, colMid + 1, row2, col2);

			node.sum += (node.c1 == null) ? 0 : node.c1.sum;
			node.sum += (node.c2 == null) ? 0 : node.c2.sum;
			node.sum += (node.c3 == null) ? 0 : node.c3.sum;
			node.sum += (node.c4 == null) ? 0 : node.c4.sum;
			
			return node;
		}

		public void update(int row, int col, int val) {
			updateTree(myroot, row, col, val);
		}

		private void updateTree(TreeNode root, int row, int col, int val) {
			if (root == null)
				return;

			if (row < root.row1 || row > root.row2 
					|| col < root.col1 || col > root.col2) {
				
				return;
			}

			if (root.row1 == root.row2 && root.row1 == row 
					&& root.col1 == root.col2 && root.col1 == col) {
				
				root.sum = val;
				return;
			}

			updateTree(root.c1, row, col, val);
			updateTree(root.c2, row, col, val);
			updateTree(root.c3, row, col, val);
			updateTree(root.c4, row, col, val);

			root.sum = 0;
			root.sum += (root.c1 == null) ? 0 : root.c1.sum;
			root.sum += (root.c2 == null) ? 0 : root.c2.sum;
			root.sum += (root.c3 == null) ? 0 : root.c3.sum;
			root.sum += (root.c4 == null) ? 0 : root.c4.sum;
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			return sumRegionTree(myroot, row1, col1, row2, col2);
		}

		private int sumRegionTree(TreeNode root, int row1, int col1, 
				int row2, int col2) {
			
			if (root == null)
				return 0;

			if (root.row2 < row1 || root.row1 > row2 
					|| root.col2 < col1 || root.col1 > col2) {
				
				return 0;
			}

			if (root.row1 >= row1 && root.row2 <= row2 
					&& root.col1 >= col1 && root.col2 <= col2) {
				
				return root.sum;
			}

			return sumRegionTree(root.c1, row1, col1, row2, col2) 
					+ sumRegionTree(root.c2, row1, col1, row2, col2)
					+ sumRegionTree(root.c3, row1, col1, row2, col2) 
					+ sumRegionTree(root.c4, row1, col1, row2, col2);
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75852/15ms-easy-to-understand-java-solution/307728
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75852/15ms-easy-to-understand-java-solution/79016
	 */
	class NumMatrix_row_sum2 {
		private int[][] rowSums;
		private int[][] matrix;

		public NumMatrix_row_sum2(int[][] matrix) {
			if (matrix.length == 0 || matrix[0].length == 0)
				return;

			this.matrix = matrix;
			int m = matrix.length;
			int n = matrix[0].length;
			rowSums = new int[m][n + 1];

			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					rowSums[i][j + 1] = rowSums[i][j] + matrix[i][j];
				}
			}
		}

		public void update(int row, int col, int val) {
			int diff = val - matrix[row][col];
			for (int j = col + 1; j < rowSums[0].length; j++) {
				rowSums[row][j] = rowSums[row][j] + diff;
			}
			matrix[row][col] = val;
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			int res = 0;
			for (int i = row1; i <= row2; i++) {
				res += rowSums[i][col2 + 1] - rowSums[i][col1];
			}
			return res;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75889/13ms-update()-O(n)-sumRegion()-O(m)-time-row-sum-JAVA-solution
	 * 
	 * 1. There are a lot of sumRegion function call but very little update function 
	 *    call.
	 *    So you want to move all the heavy calculations into update function call. To 
	 *    do this, you have to maintain a diagnose sum matrix and the sumRegion will 
	 *    take O(1) time because you just need to return the value in your buffer 
	 *    which you calculated in update function
	 * 
	 * 2. There are a lot of update but only a few sumRegion function call.
	 *    Just update the original matrix and calculate every time, update function 
	 *    take O(1) time
	 * 
	 * 3. As this problem indicates, the update and sumRegion function calls are 
	 *    distributed evenly.
	 *    What you want to do is to distribute some of the work to update function and 
	 *    some of other work to sumRegion function so that both of the function will 
	 *    take less than O(mn) time
	 *    You can do this by maintaining a row sum matrix. When a cell gets updated, 
	 *    only update the row sum in the same row which takes O(n) time where n is the 
	 *    length of the matrix
	 *    When sumRegion function gets called just add all the row sum with a small 
	 *    trick and it takes O(m) time where m is the height of the matrix
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75855/Seriously-Really-straight-forward-solution-and-beat-99-C%2B%2B-solutions...
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75852/15ms-easy-to-understand-java-solution/79016
	 */
	class NumMatrix_row_sum {
		int[][] matrix;
		int[][] lSumMatrix;

		public NumMatrix_row_sum(int[][] matrix) {
			this.matrix = matrix;
			int h = matrix.length;
			
			if (h > 0) {
				lSumMatrix = new int[h][matrix[0].length];
				
				// rowSums[i][j] = rowSums[i][0] + rowSums[i][1] + ... + rowSums[i][j]
				for (int i = 0; i < lSumMatrix.length; ++i) {
					lSumMatrix[i][0] = matrix[i][0];
					for (int j = 1; j < lSumMatrix[i].length; ++j) {
						lSumMatrix[i][j] = lSumMatrix[i][j - 1] + matrix[i][j];
					}
				}
			}
		}

		public void update(int row, int col, int val) {
			int tv = val - matrix[row][col];
			matrix[row][col] = val;
			
			for (int j = col; j < lSumMatrix[row].length; ++j) {
				lSumMatrix[row][j] += tv;
			}
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			int sum = 0;
			for (int i = row1; i <= row2; ++i) {
				sum += (lSumMatrix[i][col2] 
						- (col1 > 0 ? lSumMatrix[i][col1 - 1] : 0));
			}
			return sum;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75863/Segment-Tree-Solution-in-Java
	 * 
	 * The idea is quite similar to 1D solution. 
	 * The major difference is that each TreeNode now has 4 children instead of 2.
	 * 
	 * Each TreeNode stores the sum of the submatrix defined by the corners (x0, y0) 
	 * and (x1, y1)
	 * 
	 * If a given input is out of the range of the node, it returns 0. If the input 
	 * intersects with the range of the node, we consider their intersection.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/436604/Segment-Tree-%2B-Python-%2B-Shorter-intuitive-code
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75863/Segment-Tree-Solution-in-Java/213169
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75863/Segment-Tree-Solution-in-Java/288816
	 */
	class NumMatrix_Segment_Tree {
		TreeNode root;

		public NumMatrix_Segment_Tree(int[][] matrix) {
			if (matrix.length == 0) {
				root = null;
			} 
			else {
				root = buildTree(matrix, 0, 0, 
						matrix.length - 1, matrix[0].length - 1);
			}
		}

		public void update(int row, int col, int val) {
			update(root, row, col, val);
		}

		private void update(TreeNode root, int row, int col, int val) {
			if (root.row1 == root.row2 && root.row1 == row 
					&& root.col1 == root.col2 && root.col1 == col) {
				
				root.sum = val;
				return;
			}
			
			int rowMid = (root.row1 + root.row2) / 2;
			int colMid = (root.col1 + root.col2) / 2;
			
			TreeNode next;
			if (row <= rowMid) {
				if (col <= colMid) {
					next = root.c1;
				} 
				else {
					next = root.c2;
				}
			} 
			else {
				if (col <= colMid) {
					next = root.c3;
				} 
				else {
					next = root.c4;
				}
			}
			
			root.sum -= next.sum;
			update(next, row, col, val);
			root.sum += next.sum;
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			return sumRegion(root, row1, col1, row2, col2);
		}

		private int sumRegion(TreeNode root, int row1, int col1, int row2, int col2) {
			if (root.row1 == row1 && root.col1 == col1 
					&& root.row2 == row2 && root.col2 == col2) {
				
				return root.sum;
			}
			
			int rowMid = (root.row1 + root.row2) / 2;
			int colMid = (root.col1 + root.col2) / 2;
			if (rowMid >= row2) {
				if (colMid >= col2) {
					return sumRegion(root.c1, row1, col1, row2, col2);
				} 
				else if (colMid + 1 <= col1) {
					return sumRegion(root.c2, row1, col1, row2, col2);
				} 
				else {
					return sumRegion(root.c1, row1, col1, row2, colMid)
							+ sumRegion(root.c2, row1, colMid + 1, row2, col2);
				}
			} 
			else if (rowMid + 1 <= row1) {
				if (colMid >= col2) {
					return sumRegion(root.c3, row1, col1, row2, col2);
				} 
				else if (colMid + 1 <= col1) {
					return sumRegion(root.c4, row1, col1, row2, col2);
				} 
				else {
					return sumRegion(root.c3, row1, col1, row2, colMid)
							+ sumRegion(root.c4, row1, colMid + 1, row2, col2);
				}
			} 
			else {
				if (colMid >= col2) {
					return sumRegion(root.c1, row1, col1, rowMid, col2)
							+ sumRegion(root.c3, rowMid + 1, col1, row2, col2);
				} 
				else if (colMid + 1 <= col1) {
					return sumRegion(root.c2, row1, col1, rowMid, col2)
							+ sumRegion(root.c4, rowMid + 1, col1, row2, col2);
				} 
				else {
					return sumRegion(root.c1, row1, col1, rowMid, colMid)
							+ sumRegion(root.c2, row1, colMid + 1, rowMid, col2)
							+ sumRegion(root.c3, rowMid + 1, col1, row2, colMid)
							+ sumRegion(root.c4, rowMid + 1, colMid + 1, row2, col2);
				}
			}
		}

		private TreeNode buildTree(int[][] matrix, int row1, int col1, 
				int row2, int col2) {
			
			if (row2 < row1 || col2 < col1)
				return null;
			
			TreeNode node = new TreeNode(row1, col1, row2, col2);
			if (row1 == row2 && col1 == col2) {
				node.sum = matrix[row1][col1];
				return node;
			}
			
			int rowMid = (row1 + row2) / 2;
			int colMid = (col1 + col2) / 2;
			
			node.c1 = buildTree(matrix, row1, col1, rowMid, colMid);
			node.c2 = buildTree(matrix, row1, colMid + 1, rowMid, col2);
			node.c3 = buildTree(matrix, rowMid + 1, col1, row2, colMid);
			node.c4 = buildTree(matrix, rowMid + 1, colMid + 1, row2, col2);
			
			node.sum += node.c1 != null ? node.c1.sum : 0;
			node.sum += node.c2 != null ? node.c2.sum : 0;
			node.sum += node.c3 != null ? node.c3.sum : 0;
			node.sum += node.c4 != null ? node.c4.sum : 0;
			return node;
		}

		public class TreeNode {
			int row1, row2, col1, col2, sum;
			TreeNode c1, c2, c3, c4;

			public TreeNode(int row1, int col1, int row2, int col2) {
				this.row1 = row1;
				this.col1 = col1;
				this.row2 = row2;
				this.col2 = col2;
				this.sum = 0;
			}
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75852/15ms-easy-to-understand-java-solution
	 * 
	 * colSums[i][j] = the sum of ( matrix[0][j], matrix[1][j], matrix[2][j], ... ,
	 *                              matrix[i - 1][j] ).
	 * 
	 * row-based is slightly better because the computer stores the array in a 
	 * row-based way, which means you will have less cache miss if you update in a 
	 * one row instead of one column.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75852/15ms-easy-to-understand-java-solution/127051
	 */
	class NumMatrix_col_sum {
		private int[][] colSums;
		private int[][] matrix;

		public NumMatrix_col_sum(int[][] matrix) {
			if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
				return;
			}

			this.matrix = matrix;

			int m = matrix.length;
			int n = matrix[0].length;
			
			colSums = new int[m + 1][n];
			for (int i = 1; i <= m; i++) {
				for (int j = 0; j < n; j++) {
					colSums[i][j] = colSums[i - 1][j] + matrix[i - 1][j];
				}
			}
		}

		// time complexity for the worst case scenario: O(m)
		public void update(int row, int col, int val) {
			for (int i = row + 1; i < colSums.length; i++) {
				colSums[i][col] = colSums[i][col] - matrix[row][col] + val;
			}

			matrix[row][col] = val;
		}

		// time complexity for the worst case scenario: O(n)
		public int sumRegion(int row1, int col1, int row2, int col2) {
			int ret = 0;

			for (int j = col1; j <= col2; j++) {
				ret += colSums[row2 + 1][j] - colSums[row1][j];
			}

			return ret;
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75872/Python-94.5-Simple-sum-array-on-one-dimension-O(n)-for-both-update-and-sum
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75913/C%2B%2B-Binary-indexed-Tree-implementation
     * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75899/C%2B%2B-Quad-tree-(736ms-)-and-indexed-tree-(492ms)-based-solutions
     * https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75855/Seriously-Really-straight-forward-solution-and-beat-99-C%2B%2B-solutions...
     */
	
	/**
	 * Your NumMatrix object will be instantiated and called as such:
	 * NumMatrix obj = new NumMatrix(matrix);
	 * obj.update(row,col,val);
	 * int param_2 = obj.sumRegion(row1,col1,row2,col2);
	 */

}
