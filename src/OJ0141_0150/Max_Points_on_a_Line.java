package OJ0141_0150;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;

public class Max_Points_on_a_Line {
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47113/A-java-solution-with-notes/46906
	 * 
	 * Other code:
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47113/A-java-solution-with-notes
	 */
	public int maxPoints_self2(int[][] points) {
        if (points == null || points.length == 0)
            return 0;
        
        Map<String, Integer> map = new HashMap<>();
        int max = 1;
        for (int i = 0; i < points.length; i++) {
            map.clear();
            int dup = 1;
            int localMax = 0;
            
            for (int j = i + 1; j < points.length; j++) {
                int x = points[i][0] - points[j][0];
                int y = points[i][1] - points[j][1];
                
                if (x == 0 && y == 0) {
                    dup++;
                    continue;
                }
                
                int cd = gcd_self2(x, y);
                int reduceY = y / cd;
                int reduceX = x / cd;
                String slope = reduceY + "/" + reduceX;
                
                int count = map.getOrDefault(slope, 0);
                map.put(slope, count + 1);
                
                localMax = Math.max(localMax, map.get(slope));
            }
            max = Math.max(max, localMax + dup);
        }
        return max;
    }
    
    private int gcd_self2(int a, int b) {
        if (a == 0) 
            return b;
        
        return gcd_self2(b % a, a);
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/494635/Come-on-in-correct-and-natural-solution-in-Java
	 * 
	 * The options of the key of map
	 * 1. int[]: Wrong! Map<int[], Integer> map = new HashMap<>(). But int[] actually 
	 *    cannot be the key of hashMap, because hashCode() and equals() methods are 
	 *    not override. Its members only have length field and clone() method
	 *    (https://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.7)
	 * 2. List<Integer>: Map<List<Integer>, Integer> = new HashMap<>(), it works 
	 *    compared with int[] as the key, since hashCode is element-wise for list. 
	 *    (https://docs.oracle.com/javase/1.5.0/docs/api/java/util/List.html#hashCode%28%29) 
	 *    However, set an mutable data type as the key is really a bad idea, because 
	 *    if it is modified after insertion, it will result in undefined behavior in 
	 *    the Map. (However, even it is not recommended, it can still work.)
	 * 3. String: Map<String, Integer> = new HashMap<>(), it is immutable compared to 
	 *    List<Integer>, so String should the best option.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47125/Share-my-accepted-Java-code-with-explanation
	 */
	public int maxPoints_List(int[][] points) {
		// corner case
		if (points == null || points.length == 0)
			return 0;

		// key: slope<dy, dx>, value: freq
		Map<List<Integer>, Integer> map = new HashMap<>();
		int res = 0;

		int m = points.length;
		for (int i = 0; i < m; i++) {
			int sameCount = 0;
			int max = 0;
			for (int j = i + 1; j < m; j++) {
				int dy = points[j][1] - points[i][1];
				int dx = points[j][0] - points[i][0];
				if (dx == 0 && dy == 0) {
					sameCount++;
				} 
				else {
					List<Integer> slope = getSlope_List(dy, dx);
					map.put(slope, map.getOrDefault(slope, 0) + 1);
					max = Math.max(max, map.get(slope));
				}
			}
			res = Math.max(res, sameCount + max + 1);
			map.clear();
		}

		return res;
	}
    
	// return the irreducible slope by reducing dy and dx
	public List<Integer> getSlope_List(int dy, int dx) {
		if (dx == 0)
			return Arrays.asList(1, 0);
		if (dy == 0)
			return Arrays.asList(0, 1);

		int d = gcd_List(dy, dx);
		return Arrays.asList(dy / d, dx / d);
	}
    
	// return the most common divisor of m and n using Euclidean algorithm
	public int gcd_List(int m, int n) {
		if (n == 0)
			return m;
		return gcd_List(n, m % n);
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Not AC (has information)
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47204/Easy-Java-Solution
	 * 
	 * In most cases, for two instances of class Double, d1 and d2,
	 * the value of d1.equals(d2) is true if and only if 
	 * d1.doubleValue() == d2.doubleValue() also has the value true. 
	 * However, there are two exceptions:
	 * 
	 * If d1 and d2 both represent Double.NaN, then the equals method returns
	 * true, even though Double.NaN==Double.NaN has the value false. If d1
	 * represents +0.0 while d2 represents -0.0, or vice versa, the equal
	 * test has the value false, even though +0.0==-0.0 has the value true.
	 * This definition allows hash tables to operate properly.
	 * 
	 * Not AC (has information)
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47243/Have-an-O(n2)-accepted-solution-but-feel-terrible-about-it.-What-do-others-think/47077
	 * 
	 * -0.0 == +0.0 for the primitive types double and float but not for their object 
	 * wrappers Double and Float
	 * 
	 * Double.POSITIVE_INFINITY is a legitimate value. 
	 * 
	 * Other code:
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/328269/A-Java-solution-with-my-understanding
	 */
	public int maxPoints_self(int[][] points) {
        if (points == null || points.length == 0)
            return 0;
        
        Map<String, Integer> map = new HashMap<>();
        int max = 1;
        int self = 1;
        for (int i = 0; i < points.length; i++) {       // i < points.length - max
            map.clear();
            self = 1;
            for (int j = i + 1; j < points.length; j++) {
                int x = points[i][0] - points[j][0];
                int y = points[i][1] - points[j][1];
                
                if (x == 0 && y == 0) {
                    self++;
                    continue;
                }
                
                int cd = gcd_self(x, y);
                int reduceY = y / cd;
                int reduceX = x / cd;
                String slope = reduceY + "/" + reduceX;
                
                int count = map.getOrDefault(slope, 0);
                map.put(slope, count + 1);
            }
            
            max = Math.max(max, self);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                max = Math.max(max, entry.getValue() + self);
            }
        }
        return max;
    }
    
    private int gcd_self(int a, int b) {
        if (a == 0) 
            return b;
        
        return gcd_self(b % a, a);
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47256/Java-solution-with-no-double.-If-you-are-concerned-with-using-doublesfloats-fear-not-there-is-a-way-around.
     * 
     * It guarantees that there will not be any precision error, as long as we are 
     * dealing with integers as point coordinates.
     * 
     * About the pairing functions that uniquely represent any pair of integers. 
     * https://en.wikipedia.org/wiki/Pairing_function#Cantor_pairing_function
     * 
     * We can use a pairing function to represent two integers uniquely.
     * In our case we use a pairing function to represent a fraction, 
     * with first integer being the numerator and second integer being the denominator.
     * Hence, we avoid precision concerns that may come up if we use floats / doubles.
     * 
     * You can speed up things a bit by changing the the loops to be 
     * for (int i = 0; i < points.length; i++) and 
     * for (int j = i + 1; j < points.length; j++). So that we don't check previous 
     * pairs again. For this to work you need to change the max calculation to 
     * max = Math.max(max, localMax + 1 + slopes.getOrDefault(0, 0));
     * 
     * Rf :
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47256/Java-solution-with-no-double.-If-you-are-concerned-with-using-doublesfloats-fear-not-there-is-a-way-around./137575
     */
	public int maxPoints_Cantor_pairing(int[][] points) {
		if (points.length < 2)
			return points.length;
		
		int max = 2;
		for (int[] p1 : points) {
			Map<Integer, Integer> slopes = new HashMap<>(points.length);
			int localMax = 0;
			for (int[] p2 : points) {
				int num = p2[1] - p1[1];
				int den = p2[0] - p1[0];
                
                // pairing functions only work with non-negative integers
                // we store the sign in a separate variable
				int sign = 1;
				if ((num > 0 && den < 0) || (num < 0 && den > 0))
					sign = -1;
				
				num = Math.abs(num);
				den = Math.abs(den);
                
                // pairing functions represent a pair of any two integers uniquely;
                // they can be used as hash functions for any sequence of integers;
                // therefore, a pairing function from 1/2 doesn't equal to that from
                // 3/6, even though the slope 1/2 and 3/6 is the same.
                // => we need to convert each fraction to its simplest form, 
				// i.e. 3/6 => 1/2
				int gcd = GCD_Cantor_pairing(num, den);
				num = gcd == 0 ? num : num / gcd;
				den = gcd == 0 ? den : den / gcd;
                
                // We can use Cantor pairing function 
				// pi(k1, k2) = 1/2(k1 + k2)(k1 + k2 + 1) + k2
                // and include the sign
				int m = sign * (num + den) * (num + den + 1) / 2 + den;
				if (slopes.containsKey(m))
					slopes.put(m, slopes.get(m) + 1);
				else
					slopes.put(m, 1);
				if (m == 0)
					continue;

				localMax = Math.max(slopes.get(m), localMax);
			}
			max = Math.max(max, localMax + slopes.get(0));
		}
		return max;
	}

	public int GCD_Cantor_pairing(int a, int b) {
		if (b == 0)
			return a;
		return GCD_Cantor_pairing(b, a % b);
	}
	
	/*
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/221044/
	 * 
	 * 三點共線 : 外積大小為 0 
	 * 
	 * 3 points in the same line => magnitudes (cross product of vector) = 0
	 * 
	 * 求直線方程：
	 * https://zh.wikihow.com/%E6%B1%82%E7%9B%B4%E7%BA%BF%E6%96%B9%E7%A8%8B
	 * https://www.shuxuele.com/algebra/line-equation-point-slope.html
	 * 
	 * 最大公約數(我們後面會用歐幾里得法)：
	 * https://blog.csdn.net/Holmofy/article/details/76401074
	 * 
	 * 判斷三點共線的傳送門：
	 * https://yiminghe.iteye.com/blog/568666
	 */
	public int maxPoints_cross_product(int[][] points) {
		int res = 0, n = points.length;
		for (int i = 0; i < n; ++i) {
			int duplicate = 1;
			for (int j = i + 1; j < n; ++j) {
				int cnt = 0;
				long x1 = points[i][0], y1 = points[i][1];
				long x2 = points[j][0], y2 = points[j][1];
				
				if (x1 == x2 && y1 == y2) {
					++duplicate;
					continue;
				}
				for (int k = 0; k < n; ++k) {
					int x3 = points[k][0], y3 = points[k][1];
					if (x1 * y2 + x2 * y3 + x3 * y1 - x3 * y2 - x2 * y1 - x1 * y3 == 0) {
						++cnt;
					}
				}
				res = Math.max(res, cnt);
			}
			res = Math.max(res, duplicate);
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47098/Accepted-Java-solution-easy-to-understand.
	 * 
	 * Initialize the HashMap every time for every point i. Since 2 parallel lines 
	 * cannot pass through the same point therefore only considering the slope works.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47098/Accepted-Java-solution-easy-to-understand./46795
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47098/Accepted-Java-solution-easy-to-understand./46793
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47098/Accepted-Java-solution-easy-to-understand./137257
	 * https://leetcode.com/problems/max-points-on-a-line/discuss/47098/Accepted-Java-solution-easy-to-understand./46790
	 */
	public int maxPoints_BigDecimal_precision(int[][] points) {
		if (points.length <= 2)
			return points.length;
		
		int result = 0;
		for (int i = 0; i < points.length; i++) {
			HashMap<BigDecimal, Integer> hm = new HashMap<BigDecimal, Integer>();
			int samex = 1;
			int samep = 0;
			
			for (int j = 0; j < points.length; j++) {
				if (j != i) {
					if ((points[j][0] == points[i][0]) 
							&& (points[j][1] == points[i][1])) {
						samep++;
					}
					if (points[j][0] == points[i][0]) {
						samex++;
						continue;
					}
					
					BigDecimal dy = new BigDecimal(points[j][1] - points[i][1]);
					BigDecimal dx = new BigDecimal(points[j][0] - points[i][0]);
					BigDecimal k = dy.divide(dx, MathContext.DECIMAL128);
					if (hm.containsKey(k)) {
						hm.put(k, hm.get(k) + 1);
					} 
					else {
						hm.put(k, 2);
					}
					result = Math.max(result, hm.get(k) + samep);
				}
			}
			result = Math.max(result, samex);
		}
		return result;
	}
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47205/11ms-java-solution-without-any-map-structure
     */
	public int maxPoints_n_3(int[][] points) {
		if (points == null || points.length == 0) {
			return 0;
		}
		if (points.length <= 2) {
			return points.length;
		}

		int ret = 0;

		int n = points.length;
		int count = 0;
		int duplicates = 0;

		for (int i = 0; i < n; i++) {
			int[] p = points[i];
			count = 0;
			duplicates = 0;

			for (int j = i + 1; j < n; j++) {
				int[] q = points[j];
				if (q[0] == p[0] && q[1] == p[1]) {
					duplicates++;
					ret = Math.max(ret, duplicates + 1);
					continue;
				}

				// count point q
				count = 1;

				for (int k = j + 1; k < n; k++) {
					int[] r = points[k];
					count += isCoLinear_n_3(p, q, r) ? 1 : 0;
				}

				// count point p
				ret = Math.max(ret, count + duplicates + 1);
			}

		}
		return ret;
	}

	private boolean isCoLinear_n_3(int[] p, int[] q, int[] r) {
		long val = (long) (q[1] - p[1]) * (r[0] - q[0]) 
				- (long) (r[1] - q[1]) * (q[0] - p[0]);
		return val == 0;
	}
    
    // https://leetcode.com/problems/max-points-on-a-line/discuss/47169/My-java-accepted-solution-for-your-reference-(only-using-array).
	public int maxPoints_n_3_2(int[][] points) {
		int n = points.length;
		if (n < 2)
			return n;
		
		int currentL = 0, maxL = 2, overlap = 0, upperB = n;
		long x = 0, y = 0, dx = 0, dy = 0;
		for (int i = 0; i < upperB; i++) {
			for (int j = i + 1; j < n; j++) {
				currentL = 1;
				
				/*
				 * Given two points: (a,b) and (c,d), the corresponding normal vector 
				 * is (b-d , c-a) If another point (s,t) is in the same line uniquely 
				 * defined by (a,b) and (c,d), then (s-a , t-b) dot (b-d , c-a) = 0
				 */
				x = (long) points[i][1] - points[j][1];
				y = (long) points[j][0] - points[i][0];

				/*
				 * If two points are the same, there is no need to check further, 
				 * since a line has to be defined by exactly two distinct points.
				 */
				if (x == 0 && y == 0)
					overlap++;

				/*
				 * It might be the case that duplicates are not consecutive, but as 
				 * long as we can have a non-trivial normal vector, it won't matter.
				 */
				else {
					currentL++;

					/*
					 * Explaining (currentL + n - k > maxL): no further checking is 
					 * necessary when there isn't enough left to make it surpass maxL.
					 */
					for (int k = j + 1; k < n && currentL + n - k > maxL; k++) {
						dx = (long) points[k][0] - points[i][0];
						dy = (long) points[k][1] - points[i][1];
						if (x * dx + y * dy == 0)
							currentL++;
					}
				}
				maxL = Math.max(currentL + overlap, maxL);
			}

			/*
			 * Explaining (upperB = n - maxL): it would be crystal clear as soon as 
			 * you draw a table for combinations of case n > 3.
			 */
			upperB = n - maxL;
			overlap = 0;
		}
		return maxL;
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47268/Two-concise-python-solutions.
     * https://leetcode.com/problems/max-points-on-a-line/discuss/224773/Python-Easy-and-Concise-with-Detailed-Explanation-Algebra
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47147/11-lines-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47106/C%2B%2B-O(n2)-solution-for-your-reference
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47124/C%2B%2B-slope-counter
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47101/7-line-C%2B%2B-concise-solution-NO-double-hash-key-or-GCD-for-slope
     * https://leetcode.com/problems/max-points-on-a-line/discuss/47102/20-line-C++-O(n2)-Hashing-Solution/46847
     */

}
