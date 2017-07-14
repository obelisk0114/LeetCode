package OJ0041_0050;

/*
 * Rf : 
 * https://discuss.leetcode.com/topic/12422/share-a-code-with-fft
 * https://discuss.leetcode.com/topic/30289/java-implementation-of-the-karatsuba-algorithm-o-n-1-585
 * 
 * https://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication
 */

public class Multiply_Strings {
	/*
	 * https://discuss.leetcode.com/topic/30508/easiest-java-solution-with-graph-explanation
	 * Rf : https://discuss.leetcode.com/topic/21008/simple-clear-java-solution
	 */
	public String multiply(String num1, String num2) {
		int m = num1.length(), n = num2.length();
		int[] pos = new int[m + n];

		for (int i = m - 1; i >= 0; i--) {
			for (int j = n - 1; j >= 0; j--) {
				int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
				int p1 = i + j, p2 = i + j + 1;
				int sum = mul + pos[p2];

				pos[p1] += sum / 10;
				pos[p2] = (sum) % 10;
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int p : pos)
			if (!(sb.length() == 0 && p == 0))
				sb.append(p);
		return sb.length() == 0 ? "0" : sb.toString();
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/37609/3ms-java-solution-beats-100
	 */
	public String multiply2(String num1, String num2) {
		int m = num1.length(), n = num2.length(), zero = 0;
		int[] a = new int[m], c = new int[m + n];
		for (int i = 0, k = m; i < m; i++)
			a[--k] = num1.charAt(i) - '0'; // reverse the first number
		for (int i = n - 1; i >= 0; i--)
			add(c, a, num2.charAt(i) - '0', zero++); // multiply each digits of num2 to num1
		carry(c);                       // handle all carry operation together
		int i = m + n;
		while (i > 0 && c[--i] == 0)
			;                          // find the highest digit
		i++;
		StringBuilder ret = new StringBuilder(i);
		while (i > 0)
			ret.append((char) (c[--i] + '0'));
		return ret.toString();
    }
	void carry(int[] a) {
		int i;
		for (int k = 0, d = 0; k < a.length; k++) {
			i = a[k] + d;
			a[k] = i % 10;
			d = i / 10;
		}
	}
	void add(int[] c, int[] a, int b, int zero) {
		for (int i = zero, j = 0; j < a.length; j++, i++)
			c[i] += a[j] * b;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Multiply_Strings multiplyString = new Multiply_Strings();
		String a = "1023456789";
		String b = "987654321";
		System.out.println(multiplyString.multiply(a, b));
		System.out.println(multiplyString.multiply2(a, b));

	}

}
