package OJ0081_0090;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Permutation.html#1
 * https://en.wikipedia.org/wiki/Gray_code
 */

public class Gray_Code {
	/*
	 * https://discuss.leetcode.com/topic/3021/share-my-solution
	 * 
	 * All we need to do is to add an '1' to the top digit of the binary string 
	 * and reversely added the new number to the list.
	 * 
	 * For example, when n=3, we can get the result based on n=2.
       00,01,11,10 -> (000,001,011,010 ) (110,111,101,100). 
       The middle two numbers only differ at their highest bit, 
       while the rest numbers of part two are exactly symmetric of part one.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/608/what-if-i-have-no-knowledge-over-gray-code-before/3
	 */
	public List<Integer> grayCode(int n) {
		List<Integer> rs = new ArrayList<Integer>();
		rs.add(0);
		for (int i = 0; i < n; i++) {
			int size = rs.size();
			for (int k = size - 1; k >= 0; k--)
				rs.add(rs.get(k) | 1 << i);     // ans.add(ans.get(j) + (1 << i));
		}
		return rs;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/31329/java-easy-version-to-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/31329/java-easy-version-to-understand/4
	 */
	public List<Integer> grayCode_recursive(int n) {
		if (n < 0)
			return new ArrayList<Integer>();
		if (n == 0) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(0);
			return list;
		}
		List<Integer> tmp = grayCode(n - 1);
		List<Integer> result = new ArrayList<Integer>(tmp);
		int addNumber = 1 << (n - 1);
		for (int i = tmp.size() - 1; i >= 0; i--) {
			result.add(addNumber + tmp.get(i));
		}

		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/8557/an-accepted-three-line-solution-in-java
	 * 
	 * G(i) = i ^ (i/2)
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8557/an-accepted-three-line-solution-in-java/10
	 */
	public List<Integer> grayCode_formula(int n) {
		List<Integer> result = new LinkedList<>();
		for (int i = 0; i < (1 << n); i++)
			result.add(i ^ (i >> 1));
		return result;
	}
	
	public static void main(String[] args) {
		int N = 4;
		
		// gray code : 0 < N < 32
		for (int i = 0, c = 0; i < (1 << N); i += 2) {
			/*****   System.out.println( c ^= 1 );   *****/
			String s = Integer.toBinaryString( (1 << N) | (c ^= ((c & -c) << 1)) ).substring( 1 );
			System.out.println(s);
		    /*****   System.out.println( (c ^= ((c & -c) << 1)) );   *****/
			s = Integer.toBinaryString( (1 << N) | (c ^= 1) ).substring( 1 );
		    System.out.println(s);
		}
		System.out.println();
		
		// gray code : 0 <= N <= 32
		for (int i = 0; i < (1 << N); i++) {
			/*****   System.out.println(i ^ (i >> 1));   *****/
			String s = Integer.toBinaryString( (1 << N) | (i ^ (i >> 1)) ).substring( 1 );
			System.out.println(s);
		}
		System.out.println();
	}

}
