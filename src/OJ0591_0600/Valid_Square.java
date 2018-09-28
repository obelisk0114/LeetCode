package OJ0591_0600;

import java.util.Arrays;
import java.util.HashSet;

public class Valid_Square {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/valid-square/discuss/103461/Java-easy-to-understand-:)
	 * 
	 * first three if to judge whether is a rectangle
	 * last if for square
	 * 
	 * Rf :
	 * https://zh.wikipedia.org/wiki/%E5%B9%B3%E8%A1%8C%E5%9B%9B%E8%BE%B9%E5%BD%A2
	 * https://zh.wikipedia.org/wiki/%E7%9F%A9%E5%BD%A2
	 */
	public boolean validSquare_dist(int[] p1, int[] p2, int[] p3, int[] p4) {
		int d1 = getDist(p1, p2);
		if (d1 == 0 || d1 != getDist(p3, p4))
			return false;
		
		int d2 = getDist(p1, p3);
		if (d2 == 0 || d2 != getDist(p2, p4))
			return false;
		
		int d3 = getDist(p1, p4);
		if (d3 == 0 || d3 != getDist(p2, p3))
			return false;
		
		if (d1 == d2 || d1 == d3 || d2 == d3)
			return true;
		return false;
	}

	private int getDist(int[] p1, int[] p2) {
		return (p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]);
	}
	
	/*
	 * Approach #3 Checking every case
	 * 
	 * The following 3 functions are from this link.
	 * https://leetcode.com/articles/valid-square/
	 * 
	 * If we consider all the permutations describing the arrangement of points as in 
	 * the brute force approach, we can come up with the set of 24 arrangements.
	 * 
	 * We can see that only three unique cases exist. Thus, instead of generating all 
	 * the 24 permutations, we check for the equality of edges and diagonals for only 
	 * the three distinct cases.
	 */
	public double dist(int[] p1, int[] p2) {
		return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
	}

	public boolean check(int[] p1, int[] p2, int[] p3, int[] p4) {
		return dist(p1, p2) > 0 && dist(p1, p2) == dist(p2, p3) && dist(p2, p3) == dist(p3, p4)
				&& dist(p3, p4) == dist(p4, p1) && dist(p1, p3) == dist(p2, p4);
	}

	public boolean validSquare3(int[] p1, int[] p2, int[] p3, int[] p4) {
		return check(p1, p2, p3, p4) || check(p1, p3, p2, p4) || check(p1, p2, p4, p3);
	}
	
	/*
	 * Approach #2 Using Sorting
	 * 
	 * The following 2 functions are from this link.
	 * https://leetcode.com/articles/valid-square/
	 * 
	 * If we sort the given set of points based on their x-coordinate values, and in 
	 * the case of a tie, based on their y-coordinate value, we can obtain an 
	 * arrangement, which directly reflects the arrangement of points on a valid 
	 * square boundary possible.
	 * 
	 * In each case, after sorting, we obtain the following conclusion regarding the 
	 * connections of the points:
	 *   1. p0p1, p1p3, p3p2 and p2p0 form the four sides of any valid square.
	 *   2. p0p3 and p1p2 form the diagonals of the square.
	 */
	public double dist_sort(int[] p1, int[] p2) {
		return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
	}

	public boolean validSquare_sort(int[] p1, int[] p2, int[] p3, int[] p4) {
		int[][] p = { p1, p2, p3, p4 };
		Arrays.sort(p, (l1, l2) -> l2[0] == l1[0] ? l1[1] - l2[1] : l1[0] - l2[0]);
		return dist_sort(p[0], p[1]) != 0 
				&& dist_sort(p[0], p[1]) == dist_sort(p[1], p[3]) 
				&& dist_sort(p[1], p[3]) == dist_sort(p[3], p[2])
				&& dist_sort(p[3], p[2]) == dist_sort(p[2], p[0]) 
				&& dist_sort(p[0], p[3]) == dist_sort(p[1], p[2]);
	}
	
	/*
	 * https://leetcode.com/problems/valid-square/discuss/103465/12ms-short-Java-Solution.-No-multiplication-needed.
	 * 
	 * The addition of two short vector equals long one. (parallelogram)
	 * The square can be surrounded by a larger square.
	 */
	public boolean validSquare_surround(int[] p1, int[] p2, int[] p3, int[] p4) {
		int[][] p = { p1, p2, p3, p4 };
		Arrays.sort(p, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]));
		
		int[][] d = new int[4][2];
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				d[i][j] = p[i][j] - p[0][j];
			}
			
			if (d[i][0] == 0 && d[i][1] == 0) {
				return false;
			}
		}     
        return d[3][0] == d[1][0] + d[2][0] 
            && d[3][1] == d[1][1] + d[2][1]
            && (d[1][0] == d[2][1] && d[2][0] + d[1][1] == 0 
                || d[2][0] == d[1][1] && d[1][0] + d[2][1] == 0);
    }
	
	// by myself
	public boolean validSquare_self(int[] p1, int[] p2, int[] p3, int[] p4) {
        if (p1[0] == p2[0] && p1[1] == p2[1])
            return false;
        if (p1[0] == p3[0] && p1[1] == p3[1])
            return false;
        if (p1[0] == p4[0] && p1[1] == p4[1])
            return false;
        if (p2[0] == p3[0] && p2[1] == p3[1])
            return false;
        if (p2[0] == p4[0] && p2[1] == p4[1])
            return false;
        if (p3[0] == p4[0] && p3[1] == p4[1])
            return false;
        
        int[] pair12 = {p1[0] - p2[0], p1[1] - p2[1]};
        int[] pair13 = {p1[0] - p3[0], p1[1] - p3[1]};
        int[] pair14 = {p1[0] - p4[0], p1[1] - p4[1]};
        
        int length12 = pair12[0] * pair12[0] + pair12[1] * pair12[1];
        int length13 = pair13[0] * pair13[0] + pair13[1] * pair13[1];
        int length14 = pair14[0] * pair14[0] + pair14[1] * pair14[1];
        
        if (length12 == length13) {
            int[] pair24 = {p2[0] - p4[0], p2[1] - p4[1]};
            int[] pair34 = {p3[0] - p4[0], p3[1] - p4[1]};
            
            int length24 = pair24[0] * pair24[0] + pair24[1] * pair24[1];
            int length34 = pair34[0] * pair34[0] + pair34[1] * pair34[1];
            
            if (length24 != length34)
                return false;
            
            if (pair12[0] * pair13[0] + pair12[1] * pair13[1] != 0)
                return false;
            else
                return true;
        }
        else if (length12 == length14) {
            int[] pair23 = {p2[0] - p3[0], p2[1] - p3[1]};
            int[] pair43 = {p4[0] - p3[0], p4[1] - p3[1]};
            
            int length23 = pair23[0] * pair23[0] + pair23[1] * pair23[1];
            int length43 = pair43[0] * pair43[0] + pair43[1] * pair43[1];
            
            if (length23 != length43)
                return false;
            
            if (pair12[0] * pair14[0] + pair12[1] * pair14[1] != 0)
                return false;
            else
                return true;
        }
        else if (length13 == length14) {
            int[] pair24 = {p2[0] - p4[0], p2[1] - p4[1]};
            int[] pair23 = {p2[0] - p3[0], p2[1] - p3[1]};
            
            int length24 = pair24[0] * pair24[0] + pair24[1] * pair24[1];
            int length23 = pair23[0] * pair23[0] + pair23[1] * pair23[1];
            
            if (length24 != length23)
                return false;
            
            if (pair13[0] * pair14[0] + pair13[1] * pair14[1] != 0)
                return false;
            else
                return true;
        }
        else {
            return false;
        }
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/valid-square/discuss/135399/Java-Solution-by-using-math-vectors-(beats-100)
	 * 
	 * If a quadrilateral is a parallelogram, then it should satisfy the following:
	 *   (x1 + x3 == x2 + x4) && (y1 + y3 == y2 + y4), where xi and yi is the x-y 
	 *   coordinate of point i on 2D plane, and Point1 and Point 3 
	 *   (Or Point 2 and Point 4) are on the diagonal
	 * 
	 * If a parallelogram is a square, its four sides must be equal length and the 
	 * adjacent sides must be perpendicular. We can use vectors to easily decide this:
	 *   1. If vector a and vector b are verticle, then their dot product equals 0.
	 *   2. Use the distance formula to calculate the length of the vector
	 */
	public boolean validSquare_vector(int[] p1, int[] p2, int[] p3, int[] p4) {
		// in case any two of the points are the same
		if ((p1[0] == p2[0] && p1[1] == p2[1]) || (p1[0] == p3[0] && p1[1] == p3[1])
			|| (p1[0] == p4[0] && p1[1] == p4[1]) || (p3[0] == p2[0] && p3[1] == p2[1])
			|| (p4[0] == p2[0] && p4[1] == p2[1]) || (p3[0] == p4[0] && p3[1] == p4[1]))
			return false;
		
		if (vector(p1, p2, p3, p4)) {// p1 and p2 diagonal
			return true;
		}
		if (vector(p1, p3, p2, p4)) {// p1 and p3 diagonal
			return true;
		}
		if (vector(p1, p4, p2, p3)) {// p1 and p4 diagonal
			return true;
		}
		
		return false;
	}

	public boolean vector(int[] p1, int[] p2, int[] p3, int[] p4) {
		// if it's a parallelogram
		if (p1[0] + p2[0] == p3[0] + p4[0] && p1[1] + p2[1] == p3[1] + p4[1]) {
			int[] vecA = { p3[0] - p1[0], p3[1] - p1[1] };
			int[] vecB = { p1[0] - p4[0], p1[1] - p4[1] };
			if (vecA[0] * vecB[0] + vecA[1] * vecB[1] == 0
					&& vecA[0] * vecA[0] + vecA[1] * vecA[1] == vecB[0] * vecB[0] + vecB[1] * vecB[1]) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/valid-square/discuss/103432/4-Liner-Java
	 * 
	 * If we calculate all distances between 4 points, 4 smaller distances should be 
	 * equal (sides), and 2 larger distances should be equal too (diagonals).
	 * 
	 * It is not possible to construct an equilateral triangle with vertices on 
	 * lattice points (integers)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/valid-square/discuss/103442/C++-3-lines-(unordered_set)
	 * https://math.stackexchange.com/questions/105330/equilateral-triangle-whose-vertices-are-lattice-points
	 * https://leetcode.com/problems/valid-square/discuss/103435/Simple-Java-Solution-Square-distances
	 */
	public boolean validSquare_integer(int[] p1, int[] p2, int[] p3, int[] p4) {
		HashSet<Integer> hs = new HashSet<>(
				Arrays.asList(dis(p1, p2), dis(p1, p3), dis(p1, p4), dis(p2, p3), dis(p2, p4), dis(p3, p4)));
		return !hs.contains(0) && hs.size() == 2; // One each for side & diagonal
	}

	int dis(int[] a, int[] b) {
		return (a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/valid-square/discuss/103486/Java-Solution-calculate-distance-from-rest-of-the-points
	 * 
	 * Rf : http://www.geeksforgeeks.org/check-given-four-points-form-square/
	 */
	public boolean validSquare2(int[] p1, int[] p2, int[] p3, int[] p4) {
        if (p1[0] == p2[0] && p1[1] == p2[1]
            || p1[0] == p3[0] && p1[1] == p3[1]
            || p1[0] == p4[0] && p1[1] == p4[1]) return false;
        
        int d2 = distSq(p1, p2);  // from p1 to p2
        int d3 = distSq(p1, p3);  // from p1 to p3
        int d4 = distSq(p1, p4);  // from p1 to p4
     
        // If lengths if (p1, p2) and (p1, p3) are same, then
        // following conditions must met to form a square.
        // 1) Square of length of (p1, p4) is same as twice
        //    the square of (p1, p2)
        // 2) p4 is at same distance from p2 and p3
		if (d2 == d3 && 2 * d2 == d4) {
			int d = distSq(p2, p4);
			return (d == distSq(p3, p4) && d == d2);
		}
     
        // The below two cases are similar to above case
		if (d3 == d4 && 2 * d3 == d2) {
			int d = distSq(p2, p3);
			return (d == distSq(p2, p4) && d == d3);
		}

		if (d2 == d4 && 2 * d2 == d3) {
			int d = distSq(p2, p3);
			return (d == distSq(p3, p4) && d == d2);
		}

		return false;
	}
    
	int distSq(int[] p, int[] q) {
		return (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
	}
	
	// https://leetcode.com/problems/valid-square/discuss/103470/Solution-without-calculating-any-distances-4-lines-(Python)

}
