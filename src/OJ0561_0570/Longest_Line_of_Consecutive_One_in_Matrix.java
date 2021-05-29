package OJ0561_0570;

import java.util.Set;
import java.util.HashSet;

public class Longest_Line_of_Consecutive_One_in_Matrix {
	/*
	 * The following 6 functions are from this link.
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102270/Verbose-Java-Solution-HashSet-only-search-later-cells/105766
	 * 
	 * We only need to search a direction if the previous value in this direction 
	 * is 0.
	 * Because if the previous value is 1, we have already searched on this cell and 
	 * this direction when we loop through the previous cell.
	 * 
	 * For example:
	 * The input matrix is:
	 * [0,1,1]
	 * We need to search on right for the location of (0, 1), because the location of 
	 * (0, 0) is 0.
	 * We do not need to search on right for the the location of (0, 2), because the 
	 * location of (0, 1) is 1.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/178335/O(1)-space-O(mn)-time-solution-Nice-invariant-solution-no-DP-required
	 */
	public int longestLine2(int[][] M) {
        int result = 0;
        for (int row = 0; row < M.length; row++) {
            for (int col = 0; col < M[row].length; col++) {
                if (M[row][col] == 0) {
                    continue;
                }
                
                if (col == 0 || M[row][col - 1] == 0) {
                    result = Math.max(result, findRight2(M, row, col));
                }
                if (row == 0 || col == 0 || M[row - 1][col - 1] == 0) {
                    result = Math.max(result, findRightDown2(M, row, col));
                }
                if (row == 0 || M[row - 1][col] == 0) {
                    result = Math.max(result, findDown2(M, row, col));
                }
                if (row == 0 || col == M[row].length - 1 
                		|| M[row - 1][col + 1] == 0) {
                	
                    result = Math.max(result, findLeftDown2(M, row, col));
                }
            }
        }
        return result;
    }
    
    private int findRight2(int[][] M, int row, int col) {
        return find2(M, row, col, 0, 1);
    }
    
    private int findRightDown2(int[][] M, int row, int col) {
        return find2(M, row, col, 1, 1);
    }
    
    private int findDown2(int[][] M, int row, int col) {
        return find2(M, row, col, 1, 0);
    }
    
    private int findLeftDown2(int[][] M, int row, int col) {
        return find2(M, row, col, 1, -1);
    }
    
    private int find2(int[][] M, int row, int col, int rowDelta, int colDelta) {
        int result = 0;
        while (row >= 0 && row < M.length && col >= 0 &&
                col < M[row].length && M[row][col] == 1) {
            result++;
            row += rowDelta;
            col += colDelta;
        }
        return result;
    }
    
    /*
     * The following variable and 2 functions are from this link.
     * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102291/O(mn)-time-no-extra-space-solution
     * 
     * 4 directions, which represents horizontal, vertical, diagonal, anti-diagonal.
     * 
     * basic idea is to only count a line if it is a start point. A start point could 
     * either be in the boundary or previous node in the direction is a zero.
     */
	private int[][] dirs3 = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, -1 } };

	private boolean isBoundary3(int type, int i, int j, int maxRight) {
		if ((type == 0 && (j == 0)) || 
			(type == 1 && (i == 0)) || 
			(type == 2 && (i == 0 || j == 0)) || 
			(type == 3 && (i == 0 || j == maxRight))) {
			return true;
		} 
		else {
			return false;
		}
	}

	public int longestLine3(int[][] M) {
		int maxVal = 0;
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[i].length; j++) {
				for (int k = 0; k < 4; k++)
					if (M[i][j] == 1
							&& (isBoundary3(k, i, j, M[i].length - 1) || 
									M[i - dirs3[k][0]][j - dirs3[k][1]] == 0)) {
						
						int tmpVal = 0;
						int ii = i, jj = j;
						while (ii >= 0 && ii < M.length && jj >= 0 
								&& jj < M[i].length && M[ii][jj] == 1) {
							
							ii += dirs3[k][0];
							jj += dirs3[k][1];
							tmpVal++;
						}
						
						maxVal = tmpVal > maxVal ? tmpVal : maxVal;
					}
			}
		}
		return maxVal;
	}
	
	// by myself
	public int longestLine_self(int[][] M) {
		int res = 0;
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[i].length; j++) {
				if (M[i][j] == 0) {
					continue;
				}
				
				// column
				int col = 0;
				if (i == 0 || M[i - 1][j] == 0) {
					boolean jump = false;
					
					for (int k = i + 1; k < M.length; k++) {
						if (M[k][j] == 0) {
							col = k - i;
							jump = true;
							break;
						}
					}
					
					if (!jump) {
						col = M.length - i;
					}
				}
				
				// row
				int row = 0;
				if (j == 0 || M[i][j - 1] == 0) {
					boolean jump = false;
					
					for (int k = j + 1; k < M[i].length; k++) {
						if (M[i][k] == 0) {
							row = k - j;
							jump = true;
							break;
						}
					}
					
					if (!jump) {
						row = M[i].length - j;
					}
				}
				
				// diagonal
				int diag = 0;
				if (i == 0 || j == 0 || M[i - 1][j - 1] == 0) {
					boolean jump = false;
					int k = 1;
					
					for (; i + k < M.length && j + k < M[i].length; k++) {
						if (M[i + k][j + k] == 0) {
							diag = k;
							jump = true;
							break;
						}
					}
					
					if (!jump) {
						diag = k;
					}
				}
				
				// anti-diagonal
				int reverseDiag = 0;
				if (i == 0 || j == M[i].length - 1 || M[i - 1][j + 1] == 0) {
					boolean jump = false;
					int k = 1;
					
					for (; i + k < M.length && j - k >= 0; k++) {
						if (M[i + k][j - k] == 0) {
							reverseDiag = k;
							jump = true;
							break;
						}
					}
					
					if (!jump) {
						reverseDiag = k;
					}
				}
				
				int curMax = Math.max(row, Math.max(col, 
							Math.max(diag, reverseDiag)));
				res = Math.max(res, curMax);
			}
		}
		
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102264/JavaC%2B%2B-Clean-Code-No-Cache
	 */
	public int longestLine_brute_force_skill2(int[][] M) {
		if (M.length == 0 || M[0].length == 0)
			return 0;
		
		int m = M.length, n = M[0].length;
		int max = 0, hori = 0, vert = 0, inc = 0, desc = 0;
		
		for (int i = 0; i < m; i++, hori = 0) {
			for (int j = 0; j < n; j++) {
				hori = M[i][j] > 0 ? hori + 1 : 0;
				max = Math.max(max, hori);
			}
		}
		
		for (int j = 0; j < n; j++, vert = 0) {
			for (int i = 0; i < m; i++) {
				vert = M[i][j] > 0 ? vert + 1 : 0;
				max = Math.max(max, vert);
			}
		}
		
		for (int k = 0; k < m + n; k++, inc = 0, desc = 0) {
			// increasing start from left cells then bottom cells
			for (int i = Math.min(k, m - 1), j = Math.max(0, k - m); 
					i >= 0 && j < n; i--, j++) {
				
				inc = M[i][j] > 0 ? inc + 1 : 0;
				max = Math.max(max, inc);
			}
			
			// decreasing start from left cells then top cells;
			for (int i = Math.max(m - 1 - k, 0), j = Math.max(0, k - m); 
					i < m && j < n; i++, j++) {
				
				desc = M[i][j] > 0 ? desc + 1 : 0;
				max = Math.max(max, desc);
			}
		}
		
		return max;
	}

	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/solution/
	 * Approach 1: Brute Force
	 * 
	 * We directly traverse along every valid line in the given matrix: i.e. 
	 * Horizontal, Vertical, Diagonal aline above and below the middle diagonal, 
	 * Anti-diagonal line above and below the middle anti-diagonal. Each time during 
	 * the traversal, we keep on incrementing the count if we encounter continuous 
	 * 1's. We reset the count for any discontinuity encountered. While doing this, 
	 * we also keep a track of the maximum count found so far.
	 */
	public int longestLine_brute_force_skill(int[][] M) {
		if (M.length == 0)
			return 0;
		
		int ones = 0;
		
		// horizontal
		for (int i = 0; i < M.length; i++) {
			int count = 0;
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		
		// vertical
		for (int i = 0; i < M[0].length; i++) {
			int count = 0;
			for (int j = 0; j < M.length; j++) {
				if (M[j][i] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		
		// upper diagonal
		for (int i = 0; i < M[0].length || i < M.length; i++) {
			int count = 0;
			for (int x = 0, y = i; x < M.length && y < M[0].length; x++, y++) {
				if (M[x][y] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		
		// lower diagonal
		for (int i = 0; i < M[0].length || i < M.length; i++) {
			int count = 0;
			for (int x = i, y = 0; x < M.length && y < M[0].length; x++, y++) {
				if (M[x][y] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		
		// upper anti-diagonal
		for (int i = 0; i < M[0].length || i < M.length; i++) {
			int count = 0;
			for (int x = 0, y = M[0].length - i - 1; x < M.length && y >= 0; x++, y--) {
				if (M[x][y] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		
		// lower anti-diagonal
		for (int i = 0; i < M[0].length || i < M.length; i++) {
			int count = 0;
			for (int x = i, y = M[0].length - 1; x < M.length && y >= 0; x++, y--) {
				// System.out.println(x+" "+y);
				if (M[x][y] == 1) {
					count++;
					ones = Math.max(ones, count);
				} 
				else
					count = 0;
			}
		}
		return ones;
	}
	
	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102266/Java-O(nm)-Time-DP-Solution
	 * 
	 * we can keep a track of the 1' along all the lines possible while traversing 
	 * the matrix once only. In order to do so, we make use of a 4mn sized dp array. 
	 * Here, dp[0], dp[1], dp[2], dp[3] are used to store the maximum number of 
	 * continuous 1's found so far along the Horizontal, Anti-diagonal, Vertical, 
	 * and Diagonal lines respectively.
	 * 
	 * e.g. dp[i][j][0] is used to store the number of continuous 1's found so 
	 * far(till we reach the element M[i][j]), along the horizontal lines only.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/solution/
	 */
	public int longestLine_dp(int[][] M) {
		int n = M.length, max = 0;
		if (n == 0)
			return max;
		
		int m = M[0].length;
		int[][][] dp = new int[n][m][4];
		
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				if (M[i][j] == 0)
					continue;
				
				for (int k = 0; k < 4; k++)
					dp[i][j][k] = 1;
				
				// horizontal line
				if (j > 0)
					dp[i][j][0] += dp[i][j - 1][0];
				
				// anti-diagonal line
				if (j > 0 && i > 0)
					dp[i][j][1] += dp[i - 1][j - 1][1];
				
				// vertical line
				if (i > 0)
					dp[i][j][2] += dp[i - 1][j][2];
				
				// diagonal line
				if (j < m - 1 && i > 0)
					dp[i][j][3] += dp[i - 1][j + 1][3];
				
				max = Math.max(max, Math.max(dp[i][j][0], dp[i][j][1]));
				max = Math.max(max, Math.max(dp[i][j][2], dp[i][j][3]));
			}
		
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/solution/
	 * Approach 3: Using 2D Dynamic Programming
	 * 
	 * In the previous approach, we can observe that the current dp entry is 
	 * dependent only on the entries of the just previous corresponding dp row. Thus, 
	 * instead of maintaining a 2-D dp matrix for each kind of line of 1's possible, 
	 * we can use a 1-d array for each one of them, and update the corresponding 
	 * entries in the same row during each row's traversal. Taking this into account, 
	 * the previous 3-D dp matrix shrinks to a 2-D dp matrix now.
	 * 
	 * dp: Horizontal, Vertical, Diagonal and Anti-diagonal
	 * dp[i][j][0] = j > 0 ? dp[i][j - 1][0] + 1 : 1;
     * dp[i][j][1] = i > 0 ? dp[i - 1][j][1] + 1 : 1;
     * dp[i][j][2] = (i > 0 && j > 0) ? dp[i - 1][j - 1][2] + 1 : 1;
     * dp[i][j][3] = (i > 0 && j < M[0].length - 1) ? dp[i - 1][j + 1][3] + 1 : 1;
	 */
	public int longestLine_dp2(int[][] M) {
		if (M.length == 0)
			return 0;

		int ones = 0;
		int[][] dp = new int[M[0].length][4];
		
		for (int i = 0; i < M.length; i++) {
			int old = 0;
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] == 1) {
					dp[j][0] = j > 0 ? dp[j - 1][0] + 1 : 1;
					dp[j][1] = i > 0 ? dp[j][1] + 1 : 1;
					
					int prev = dp[j][2];
					// 因為同一行的前一個元素 (j - 1) 會改掉 dp[j - 1][2], 所以要用 old 來儲存
					dp[j][2] = (i > 0 && j > 0) ? old + 1 : 1;
					
					old = prev;
					dp[j][3] = (i > 0 && j < M[0].length - 1) ? dp[j + 1][3] + 1 : 1;
					
					ones = Math.max(ones, Math.max(Math.max(dp[j][0], dp[j][1]), 
							Math.max(dp[j][2], dp[j][3])));
				} 
				else {
					old = dp[j][2];
					dp[j][0] = dp[j][1] = dp[j][2] = dp[j][3] = 0;
				}
			}
		}
		return ones;
	}
	
	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102264/JavaC++-Clean-Code-No-Cache/754260
	 * 
	 * O(numRows * numCols) time, because we iterate through all values of the matrix 
	 * exactly once, and for each value we do O(1) time work.
	 * O(numCols) space.
	 */
	public int longestLine_dp4(int[][] M) {
		if (M == null || M.length == 0)
			return 0;
		
		// up[i] is the number of consecutive 1's going upwards in the previous
		// row from column i.
		int[] up = new int[M[0].length];

		// leftDiag[i] is the number of consecutive 1's going left diagonally
		// upwards in the previous row from column i.
		int[] leftDiag = new int[M[0].length];

		// rightDiag[i] is the number of consecutive 1's going right diagonally
		// upwards in the previous row from column i.
		int[] rightDiag = new int[M[0].length];
		
		int result = 0;
		
		for (int i = 0; i < M.length; ++i) {
			// Number of consecutive 1's in the current row.
			int horizontal = 0;
			int[] newUp = new int[M[0].length];
			int[] newLeftDiag = new int[M[0].length];
			int[] newRightDiag = new int[M[0].length];
			
			for (int j = 0; j < M[0].length; ++j) {
				// Use the current 1 to extend the three lines: straight upwards,
				// left upwards diagonal, right upwards diagonal.
				if (M[i][j] == 1) {
					newUp[j] = up[j] + 1;
					newLeftDiag[j] = j - 1 >= 0 ? leftDiag[j - 1] + 1 : 1;
					newRightDiag[j] = j + 1 < M[0].length ? rightDiag[j + 1] + 1 : 1;
					horizontal++;
					
					result = Math.max(result, Math.max(horizontal, 
							Math.max(newUp[j], Math.max(newLeftDiag[j], 
									newRightDiag[j]))));
				} 
				else {
					newUp[j] = 0;
					newLeftDiag[j] = 0;
					newRightDiag[j] = 0;
					horizontal = 0;
				}
			}
			
			up = newUp;
			leftDiag = newLeftDiag;
			rightDiag = newRightDiag;
		}
		return result;
	}

	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102296/Simple-and-Concise-Java-Solution-(Easy-to-Understand-O(m%2Bn)-space)
	 * 
	 * When p1(x1, y1) and p2(x2, y2) are on the same anti-diagonal, we have 
	 * x1 + y1 == x2 + y2, points are on the same line when they have same value of 
	 * x + y.
	 * when they are on the same diagonal, we have y1 - x1 == y2 - x2.
	 * use an array to record which diagonal you are on.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102296/Simple-and-Concise-Java-Solution-(Easy-to-Understand-O(m+n)-space)/194186
	 */
	public int longestLine_dp3(int[][] M) {
	    if (M.length == 0 || M[0].length == 0) {
	        return 0;
	    }
	    
	    int max = 0;
	    int[] col = new int[M[0].length];
	    int[] diag = new int[M.length + M[0].length];
	    int[] antiD = new int[M.length + M[0].length];
	    
	    for (int i = 0; i < M.length; i++) {
	        int row = 0;
	        
	        for (int j = 0; j < M[0].length; j++) {
	            if (M[i][j] == 1) {
	                row++;
	                col[j]++;
	                diag[j + i]++;
	                antiD[j - i + M.length]++;
	                
	                max = Math.max(max, row);
	                max = Math.max(max, col[j]);
	                max = Math.max(max, diag[j + i]);
	                max = Math.max(max, antiD[j - i + M.length]);
	            } 
	            else {
	                row = 0;
	                col[j] = 0;
	                diag[j + i] = 0;
	                antiD[j - i + M.length] = 0;
	            }
	        }
	    }
	    return max;
	}
	
	/*
	 * The following variable and 3 functions are from this link.
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102310/Java-Straightforward-Solution
	 * 
	 * We are iterating from top-left to right-bottom. So you only consider direction 
	 * that is going down/right/left-down/right-down
	 * The other four directions will just be unnecessary repeat work.
	 * 
	 *  if you go to the right top, it is the same as going to the left bottom. For 
	 *  example, the diagonal 1s is the longest consecutive 1s. its the same going 
	 *  from bottom left 1 to top right 1 or going from the top right 1 to the left 
	 *  bottom 1
	 *  
	 *  0 0 0 1
	 *  0 0 1 0
	 *  0 1 0 0
	 *  1 0 0 0
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102288/DFS-straightforward/136804
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102288/DFS-straightforward/113482
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102288/DFS-straightforward
	 */
	public int longestLine_brute_force(int[][] M) {
		if (M == null)
			return 0;
		
		int res = 0;
		
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] == 1) {
					res = Math.max(res, getMaxOneLine_brute_force(M, i, j));
				}
			}
		}
		
		return res;
	}

	final int[][] dirs_brute_force = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 } };

	private int getMaxOneLine_brute_force(int[][] M, int x, int y) {
		int res = 1;
		
		for (int[] dir : dirs_brute_force) {
			int i = x + dir[0];
			int j = y + dir[1];
			int count = 1;
			
			while (isValidPosition_brute_force(M, i, j) && M[i][j] == 1) {
				i += dir[0];
				j += dir[1];
				count++;
			}
			
			res = Math.max(count, res);
		}
		
		return res;
	}

	private boolean isValidPosition_brute_force(int M[][], int i, int j) {
		return (i < M.length && j < M[0].length && i >= 0 && j >= 0);
	}
	
	/*
	 * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102270/Verbose-Java-Solution-HashSet-only-search-later-cells
	 * 
	 * For each unvisited direction of each 1, we search length of adjacent 1s and 
	 * mark those 1s as visited in that direction. And we only need to search 4 
	 * directions: right, down, down-right, down-left. We only access each cell at 
	 * max 4 times, so time complexity is O(mn). m = number of rows, 
	 * n = number of columns.
	 */
	public int longestLine_brute_force_set(int[][] M) {
		int m = M.length;
		if (m <= 0)
			return 0;
		
		int n = M[0].length;
		if (n <= 0)
			return 0;

		Set<String> horizontal = new HashSet<>();
		Set<String> vertical = new HashSet<>();
		Set<String> diagonal = new HashSet<>();
		Set<String> antidiagonal = new HashSet<>();
		int max = 0;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (M[i][j] == 0)
					continue;
				
				String pos = i + "," + j;
				
				if (!horizontal.contains(pos)) {
					int count = 0;
					for (int k = j; k < n; k++) {
						if (M[i][k] == 1) {
							count++;
							horizontal.add(i + "," + k);
						} 
						else
							break;
					}
					max = Math.max(max, count);
				}
				
				if (!vertical.contains(pos)) {
					int count = 0;
					for (int k = i; k < m; k++) {
						if (M[k][j] == 1) {
							count++;
							vertical.add(k + "," + j);
						} 
						else
							break;
					}
					max = Math.max(max, count);
				}
				
				if (!diagonal.contains(pos)) {
					int count = 0;
					for (int k = i, l = j; k < m && l < n; k++, l++) {
						if (M[k][l] == 1) {
							count++;
							diagonal.add(k + "," + l);
						} 
						else
							break;
					}
					max = Math.max(max, count);
				}
				
				if (!antidiagonal.contains(pos)) {
					int count = 0;
					for (int k = i, l = j; k < m && l >= 0; k++, l--) {
						if (M[k][l] == 1) {
							count++;
							antidiagonal.add(k + "," + l);
						} 
						else
							break;
					}
					max = Math.max(max, count);
				}
			}
		}

		return max;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102275/Python-Simple-with-Explanation
     * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102302/Short-and-simple-C%2B%2B-and-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/discuss/102312/10-line-C%2B%2B-DP-O(n)-Solution
     */

}
