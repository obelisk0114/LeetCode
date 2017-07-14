package OJ0291_0300;

/*
 * http://math.stackexchange.com/questions/113270/the-median-minimizes-the-sum-of-absolute-deviations
 * https://discuss.leetcode.com/topic/46212/the-theory-behind-why-the-median-works
 * 
 * https://en.wikipedia.org/wiki/Geometric_median
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Random;

public class Best_Meeting_Point {
	// Sort x, y and find the median in both x and y
	int minTotalDistance_sort(int[][] grid) {
		if(grid == null || grid[0].length == 0) { 
			return 0;
		}
		List<Integer> xLabel = new ArrayList<Integer>();
		List<Integer> yLabel = new ArrayList<Integer>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 1) {
					xLabel.add(j);
					yLabel.add(i);
				}
			}
		}
		
		int distance = 0;
		Collections.sort(xLabel);
		// Collections.sort(yLabel);    It is sorted. We can just choose the median.
		int pivotX = xLabel.get(xLabel.size() / 2);
	    int pivotY = yLabel.get(yLabel.size() / 2);
		for (int k = 0; k < xLabel.size(); k++) {
			int tmp_distance = Math.abs(pivotX - xLabel.get(k)) + Math.abs(pivotY - yLabel.get(k));
			distance += tmp_distance;
		}
		return distance;
	}
	
	// The following 2 functions are using quick select method to find the median.
	int minTotalDistance_quickSelect(int[][] grid) {
		List<Integer> x = new ArrayList<>();
		List<Integer> y = new ArrayList<>();

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					x.add(i);
					y.add(j);
				}
			}
		}

		// get median of x[] and y[] using quick select
		int mx = x.get(quickSelect(x, 0, x.size() - 1, x.size() / 2 + 1));
		int my = y.get(quickSelect(y, 0, y.size() - 1, y.size() / 2 + 1));

		// calculate the total Manhattan distance
		int total = 0;
		for (int i = 0; i < x.size(); i++) {
			total += Math.abs(x.get(i) - mx) + Math.abs(y.get(i) - my);
		}
		return total;
	}

	// return the index of the kth smallest number
	// avg. O(n) time complexity
	int quickSelect(List<Integer> a, int lo, int hi, int k) {
		// use quick sort's idea
		// randomly pick a pivot and put it to a[hi]
		// we need to do this, otherwise quick sort is slow!
		Random rand = new Random();
		int p = lo + rand.nextInt(hi - lo + 1);
		Collections.swap(a, p, hi);

		// put nums that are <= pivot to the left
		// put nums that are > pivot to the right
		int i = lo, j = hi, pivot = a.get(hi);
		while (i < j) {
			if (a.get(i++) > pivot)
				Collections.swap(a, --i, --j);
		}
		Collections.swap(a, i, hi);

		// count the nums that are <= pivot from lo
		int m = i - lo + 1;

		// pivot is the one!
		if (m == k)
			return i;
		// pivot is too big, so it must be on the left
		else if (m > k)
			return quickSelect(a, lo, i - 1, k);
		// pivot is too small, so it must be on the right
		else
			return quickSelect(a, i + 1, hi, k - m);
	}
	
	// The following 2 functions are from this link.
	// https://discuss.leetcode.com/topic/35451/java-3-ms-binary-search
	// The idea is to compute prefix sums of how many people live above i / 
	// to the left of j and then do binary search.
	public int minTotalDistance_binary_search(int[][] grid) {
	    int[] sx = new int[grid[0].length];
	    int[] sy = new int[grid.length];
	    int c = 0;
	    for (int i = 0; i < grid.length; ++i) {
	        for (int j = 0; j < grid[0].length; ++j) {
	            if (grid[i][j] == 1) {
	                ++sx[j];
	                ++sy[i];
	                ++c;
	            }
	        }
	    }
	    for (int i = 1; i < sx.length; ++i) {
	        sx[i] += sx[i - 1];
	    }
	    for (int i = 1; i < sy.length; ++i) {
	        sy[i] += sy[i - 1];
	    }
	    int mx = binarySearch(sx, c), my = binarySearch(sy, c);
	    int sum = 0;
	    for (int i = 0; i < sx.length; ++i) {
	        sum += (sx[i] - (i == 0 ? 0 : sx[i - 1])) * Math.abs(i - mx);
	    }
	    for (int i = 0; i < sy.length; ++i) {
	        sum += (sy[i] - (i == 0 ? 0 : sy[i - 1]))* Math.abs(i - my);
	    }
	    return sum;
	}

	int binarySearch(int[] sums, int total) {
	    int l = 0, r = sums.length - 1;
	    while (l <= r) {
	        int mid = (l + r) / 2;
	        int left = sums[mid];
	        int right = total - sums[mid];
	        if (left == right) {
	            return mid;
	        } else if (left > right) {
	            r = mid - 1;
	        } else {
	            l = mid + 1;
	        }
	    }
	    return l;
	}
	
	// The following 4 functions are collecting both the row and column coordinates in sorted order.
	int minTotalDistance_collectSorted(int[][] grid) {
	    List<Integer> rows = collectRows(grid);
	    List<Integer> cols = collectCols(grid);
	    int row = rows.get(rows.size() / 2);
	    int col = cols.get(cols.size() / 2);
	    return minDistance1D(rows, row) + minDistance1D(cols, col);
	}

	private int minDistance1D(List<Integer> points, int origin) {
	    int distance = 0;
	    for (int point : points) {
	        distance += Math.abs(point - origin);
	    }
	    return distance;
	}

	private List<Integer> collectRows(int[][] grid) {
	    List<Integer> rows = new ArrayList<>();
	    for (int row = 0; row < grid.length; row++) {
	        for (int col = 0; col < grid[0].length; col++) {
	            if (grid[row][col] == 1) {
	                rows.add(row);
	            }
	        }
	    }
	    return rows;
	}

	private List<Integer> collectCols(int[][] grid) {
	    List<Integer> cols = new ArrayList<>();
	    for (int col = 0; col < grid[0].length; col++) {
	        for (int row = 0; row < grid.length; row++) {
	            if (grid[row][col] == 1) {
	                cols.add(col);
	            }
	        }
	    }
	    return cols;
	}
	
	// The following 3 functions are from this link.
	// https://discuss.leetcode.com/topic/41015/share-my-very-simple-and-fast-solution-java
	public int minTotalDistance_compress_median(int[][] grid) {
	    if(grid == null || grid[0].length == 0){ return 0;}
	    int[] row = new int[grid.length];
	    int[] col = new int[grid[0].length];
	    for(int i = 0; i < grid.length; i++){
	        for(int j = 0; j < grid[0].length; j++){
	            if(grid[i][j] == 1){
	                row[i]++;
	                col[j]++;
	            }
	        }
	    }
	    int centerRow = findCentoid(row);
	    int centerCol = findCentoid(col);
	    return computeTotalDistance(row, centerRow) + computeTotalDistance(col, centerCol);
	}

	private int computeTotalDistance(int[] nums, int center){
	    int total = 0;
	    for(int i = 0; i < nums.length; i++){
	        total += nums[i] * Math.abs(i-center);
	    }
	    return total;
	}

	// find the point x which is the best point to meet.
	// The point x should satisfy: leftSum < x + rightSum && leftSum + x > rightSum
	private int findCentoid(int[] nums){
	    double count = 0;
	    for(int i = 0; i < nums.length; i++){
	        count += nums[i];
	    }
	    int i = 0;
	    int leftSum = 0;
	    while(2 * leftSum < count){
	        leftSum += nums[i];
	        i++;
	    }
	    return i - 1;
	}
	
	/*
	 * The following 2 functions are using pairs of min and max to get the sum.
	 * 
	 * Store positions and sort array.
	 * Because meeting point must be in the interval of the max and min.
	 * ex: {r_0, r_1, r_2, ..., r_n-2, r_n-1, r_n};
	 * We can just sum the distance of every pair of max and min
	 * ex: d(r_0, r_n), d(r_1, r_n-1), d(r_2, r_n-2)
	 * 
	 * https://discuss.leetcode.com/topic/27710/14ms-java-solution
	 */
	public int minTotalDistance_sortedSum(int[][] grid) {
        if (grid.length == 0) return 0;
        int count = 0;
        /*
         *  Another methods. Use Arrays.copyOf to trim the array
         * 
        int[] xIndex = new int[grid.length * grid[0].length];
        int[] yIndex = new int[grid.length * grid[0].length];
        int x = 0, y = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    xIndex[x++] = i;
                    yIndex[y++] = j;
                    count++;
                }
            }
        }
        xIndex = Arrays.copyOf(xIndex, count);
        yIndex = Arrays.copyOf(yIndex, count);
         */
        for (int i = 0; i < grid.length; i ++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    count++;
                }
            }
        }
        int[] xIndex = new int[count];
        int[] yIndex = new int[count];
        int x = 0, y = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    xIndex[x++] = i;
                    yIndex[y++] = j;
                }
            }
        }
        Arrays.sort(xIndex);
        Arrays.sort(yIndex);
        return intervalSum(xIndex, 0, count - 1) + intervalSum(yIndex, 0, count - 1);
    }
	
	// Sum the interval
    public int intervalSum(int[] grid, int i, int j) {
        int res = 0;
        while (j - i > 0) {
            res += grid[j] - grid[i];
            j--;
            i++;
        }
        return res;
    }
    
    // https://discuss.leetcode.com/topic/27722/o-mn-java-2ms
    // Solution 1
    // No need to sort the coordinates if we collect them in sorted order.
	int minTotalDistance_collectSorted_intervalSum(int[][] grid) {
		int m = grid.length, n = grid[0].length;
		int total = 0, Z[] = new int[m * n];
		for (int dim = 0; dim < 2; ++dim) {
			int i = 0, j = 0;
			if (dim == 0) {
				for (int x = 0; x < n; ++x)
					for (int y = 0; y < m; ++y)
						if (grid[y][x] == 1)
							Z[j++] = x;
			} 
			else {
				for (int y = 0; y < m; ++y)
					for (int g : grid[y])
						if (g == 1)
							Z[j++] = y;
			}
			
			j--;
			while (i < j)
				total += Z[j--] - Z[i++];
		}
		return total;
	}
	
	// https://discuss.leetcode.com/topic/27722/o-mn-java-2ms/2
	// Solution 2
	// BucketSort-ish. Count how many people live in each row and each column.
	// Only O(m+n) space.
	
	// Use pairs of min and max. Compress 2D array to 1D sorted array.
	// https://discuss.leetcode.com/topic/41496/simple-java-solution-o-mn-in-3ms-using-two-arrays-m-n
	public int minTotalDistance(int[][] grid) {
	    int m = grid.length, n = grid[0].length;
	    int[] I = new int[m], J = new int[n];
	    for (int i=0; i<m; ++i)
	        for (int j=0; j<n; ++j)
	            if (grid[i][j] == 1) {
	                ++I[i];
	                ++J[j];
	            }
	    int total = 0;
	    for (int[] K : new int[][]{ I, J }) {
	        int i = 0, j = K.length - 1;
	        while (i < j) {
	            int k = Math.min(K[i], K[j]);
	            total += k * (j - i);
	            if ((K[i] -= k) == 0) ++i;
	            if ((K[j] -= k) == 0) --j;
	        }
	    }
	    return total;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/27762/java-2ms-python-40ms-two-pointers-solution-no-median-no-sort-with-explanation
	 * 
	 * Use two pointers to solve the 1D problem, the median of all the x coordinates.
	 *  
	 * "left" and "right" are how many people one left/right side of coordinates i/j. 
	 * If we have more people on the left, we let j decrease otherwise increase i. 
	 * The time complexity is O(n) and space is O(1).
	 * 
	 * We can think i and j as two meeting points. 
     * people in [0, i] go to meet at i, people in [j, n - 1] meet at j.
	 * 
	 * We let left = sum(vec[:i+1]), right = sum(vec[j:]), 
	 * which are the number of people at each meet point, 
     * d is total distance for the left people meet at i and right people meet at j.
     * 
     * Let i == j with minimum d.
     * 
     * If we increase i by 1, the distance will increase by left 
     * since there are 'left' people at i and they just move 1 step.
     * When decrease j by 1, the distance will increase by right.
     * 
     * To make sure the total distance d is minimized 
     * we certainly want to move the point with less people.
	 */
	int minTotalDistance_two_pointer(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] row_sum = new int[n], col_sum = new int[m];

        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j) {
                row_sum[j] += grid[i][j];
                col_sum[i] += grid[i][j];
            }

        return minDistance1D_two_pointer(row_sum) + minDistance1D_two_pointer(col_sum);
    }

    public int minDistance1D_two_pointer(int[] vector) {
        int i = 0, j = vector.length - 1;
        int d = 0, left = vector[0], right = vector[vector.length - 1];

        while (i != j) {
            if (left < right) {
                d += left;
                left += vector[++i];
            }
            else {
                d += right;
                right += vector[--j];
            }
        }
        return d;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Best_Meeting_Point bestMeet = new Best_Meeting_Point();
		int[][] live = {{1, 0, 0, 1, 0}, {0, 1, 1, 0, 1}, {1, 1, 0, 0, 1}, {0, 0, 1, 0, 1}};
		System.out.println("sort : " + bestMeet.minTotalDistance_sort(live));
		System.out.println("Quick Select : " + bestMeet.minTotalDistance_quickSelect(live));
		System.out.println("Binary Search : " + bestMeet.minTotalDistance_binary_search(live));
		System.out.println("collect sorted : " + bestMeet.minTotalDistance_collectSorted(live));
		System.out.println("compress median : " + bestMeet.minTotalDistance_compress_median(live));
		System.out.println("sorted sum : " + bestMeet.minTotalDistance_sortedSum(live));
		System.out.println("collectSorted_intervalSum : " + bestMeet.minTotalDistance_collectSorted_intervalSum(live));
		System.out.println("BucketSort-ish : " + bestMeet.minTotalDistance(live));
		System.out.println("two pointer : " + bestMeet.minTotalDistance_two_pointer(live));

	}

}
