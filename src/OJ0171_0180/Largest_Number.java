package OJ0171_0180;

import java.util.Arrays;
import java.util.Comparator;

public class Largest_Number {
	/*
	 * https://leetcode.com/problems/largest-number/discuss/53195/Mathematical-proof-of-correctness-of-sorting-method/137866
	 * 
	 * Let us define f(X) = 10^(lgX + 1), then XY = f(Y)X + Y
	 * 
	 * If AB <= BA, then we have
	 * f(B)A + B <= f(A)B + A
	 * (f(B) - 1)A <= (f(A) - 1)B
	 * that is
	 * A <= B¡P(f(A) - 1) / (f(B) - 1)   (1)
	 * 
	 * If BC <= CB, then we have
	 * f(C)B + C <= f(B)C + B
	 * (f(C) - 1)B <= (f(B) - 1)C
	 * that is
	 * B <= C¡P(f(B) - 1) / (f(C) - 1)   (2)
	 * 
	 * Combine (1), (2), we have
	 * A <= C¡P(f(A) - 1) / (f(C) - 1)
	 * (f(C) - 1)A <= (f(A) - 1)C
	 * f(C)A + C <= f(A)C + A
	 * AC <= CA
	 * 
	 * Once the array is sorted, the most "signficant" number will be at the front.
	 */
	
	/*
	 * https://leetcode.com/problems/largest-number/discuss/53158/My-Java-Solution-to-share
	 * 
	 * implement a String comparator to decide which String should come first during 
	 * concatenation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/largest-number/discuss/53347/Simple-solution-with-java-(O(NlogN)-and-no-need-for-BigInteger)
	 * https://leetcode.com/problems/largest-number/discuss/53336/Simple-Java-solution-using-Arrays.sort()-and-custom-object-implementing-comparable
	 */
	public String largestNumber(int[] num) {
		if (num == null || num.length == 0)
			return "";

		// Convert int array to String array, so we can sort later on
		String[] s_num = new String[num.length];
		for (int i = 0; i < num.length; i++)
			s_num[i] = String.valueOf(num[i]);

		// Comparator to decide which string should come first in concatenation
		Comparator<String> comp = new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				String s1 = str1 + str2;
				String s2 = str2 + str1;
				return s2.compareTo(s1); // reverse order here, so we can do append() later
			}
		};

		Arrays.sort(s_num, comp);
		// An extreme edge case by lc, say you have only a bunch of 0 in your int array
		if (s_num[0].charAt(0) == '0')
			return "0";

		StringBuilder sb = new StringBuilder();
		for (String s : s_num)
			sb.append(s);

		return sb.toString();
	}
	
	/*
	 * https://leetcode.com/problems/largest-number/discuss/53162/My-3-lines-code-in-Java-and-Python
	 * 
	 * Just compare number by convert it to string.
	 */
	public String largestNumber_java8(int[] num) {
        String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
        Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
        return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
    }

}
