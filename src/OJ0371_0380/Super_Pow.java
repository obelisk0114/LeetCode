package OJ0371_0380;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.math.BigInteger;

/*
 * https://discuss.leetcode.com/topic/52054/what-s-the-point-of-this-kind-of-question
 * http://sparklingwind.github.io/2016/05/12/how-to-calc-power-with-mod/
 */

public class Super_Pow {
	/*
	 * The following variable and 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/50504/java-4ms-solution-using-the-remainder-repeat-pattern
	 */
	int DIV = 1337;
	List<Integer> findLoop(int a) {
		List<Integer> index = new ArrayList<>();
		boolean[] set = new boolean[DIV];
		int rem = a % DIV;
		while (!set[rem]) {
			set[rem] = true;
			index.add(rem);
			rem = (rem * a) % DIV;
		}
		return index;
	}
	int modBy(int[] b, int m) {
		int rem = 0;
		for (int i = 0; i < b.length; i++) {
			rem = (rem * 10 + b[i]) % m;
		}
		return rem;
	}
	public int superPow(int a, int[] b) {
		if (a == 0 || a == DIV || b == null || b.length == 0)
			return 0;
		if (a == 1)
			return 1;
		if (a > DIV)
			return superPow(a % DIV, b);
		List<Integer> index = findLoop(a);
		int loopsize = index.size();
		int rem = modBy(b, loopsize);
		rem = rem == 0 ? loopsize : rem;
		return index.get(rem - 1);
	}
	
	// https://discuss.leetcode.com/topic/51460/java-solution-pigeonhole-principle
	public int superPow_bigInteger(int a, int[] b) {
		int[] pows = new int[1337]; // max cycle is 1337
		Set<Integer> set = new HashSet<Integer>();

		// pigeon hole principle dictates that must be a duplicate among the
		// power from 1 to 1337 if moded by 1337
		int cycle = 0;
		int val = 1;
		for (int i = 0; i < 1337; i++) {
			val = (int) (((long) val * a) % 1337);  // a = Integer.MAX_VALUE
			// cycle found
			if (set.contains(val))
				break;
			set.add(val);
			pows[cycle++] = val;
		}

		// b: String -> BigInteger
		StringBuilder str = new StringBuilder();
		for (int v : b)
			str.append(v);
		BigInteger bVal = new BigInteger(str.toString());

		bVal = bVal.subtract(new BigInteger("1")).mod(new BigInteger("" + cycle));
		return pows[bVal.intValue()];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/50543/8ms-java-solution-using-fast-power
	 * 
	 * Rf : https://discuss.leetcode.com/topic/53187/java-16-lines-ac-solution-11ms
	 */
	private static final int M = 1337;
    public int normalPow(int a, int b) {
        int result = 1;
        while (b != 0) {
            if (b % 2 != 0)
                result = result * a % M;
            a = a * a % M;
            b /= 2;
        }
        return result;
    }
    public int superPow2(int a, int[] b) {
        a %= M;
        int result = 1;
        for (int i = b.length - 1; i >= 0; i--) {
            result = result * normalPow(a, b[i]) % M;
            a = normalPow(a, 10);
        }
        return result;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://discuss.leetcode.com/topic/50586/math-solusion-based-on-euler-s-theorem-power-called-only-once-c-java-1-line-python
     * 
     */
	public int superPow_Euler_Theorem(int a, int[] b) {
		if (a % 1337 == 0)
			return 0;
		int p = 0;
		for (int i : b)
			p = (p * 10 + i) % 1140;
		if (p == 0)
			p += 1440;
		return power_Euler(a, p, 1337);
	}
	public int power_Euler(int a, int n, int mod) {
		a %= mod;
		int ret = 1;
		while (n != 0) {
			if ((n & 1) != 0)
				ret = ret * a % mod;
			a = a * a % mod;
			n >>= 1;
		}
		return ret;
	}
    
    /*
     * The following 2 functions are from this link.
     * https://discuss.leetcode.com/topic/50591/fermat-and-chinese-remainder
     */
    public int superPow_Fermat_little_theorem(int a, int[] b) {
		return (764 * superPow(a, b, 7) + 574 * superPow(a, b, 191)) % 1337;
    }
    public int superPow(int a, int[] b, int prime) {
		if ((a %= prime) == 0)
			return 0;
		int e = 0, mod = prime - 1;
		for (int digit : b)
			e = (e * 10 + digit) % mod;
		int pow = 1;
		while (e != 0) {
			if ((e & 1) == 1)
				pow = pow * a % prime;
			a = a * a % prime;
			e >>= 1;
		}
		return pow;
    }
	
	public static void main(String[] args) {
		Super_Pow superpow = new Super_Pow();
		int a = 91;
		int[] b = {1, 1, 7};
		System.out.println(superpow.superPow(a, b));
		System.out.println(superpow.superPow_bigInteger(a, b));
		System.out.println(superpow.superPow2(a, b));
		System.out.println(superpow.superPow_Euler_Theorem(a, b));
		System.out.println(superpow.superPow_Fermat_little_theorem(a, b));
	}

}
