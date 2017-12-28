package OJ0011_0020;

public class Integer_to_Roman {
	// https://discuss.leetcode.com/topic/12384/simple-solution
	public static String intToRoman(int num) {
		String M[] = { "", "M", "MM", "MMM" };
		String C[] = { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
		String X[] = { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
		String I[] = { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };
		return M[num / 1000] + C[(num % 1000) / 100] + X[(num % 100) / 10] + I[num % 10];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/10102/a-solution-applies-to-numbers-in-any-range
	 */
	public String intToRoman_digit(int num) {
        return (romanDigit((num / 1000) % 10, "M", "XX", "XX")) + 
               (romanDigit((num / 100) % 10, "C", "D", "M")) +
               (romanDigit((num / 10) % 10, "X", "L", "C")) +
               (romanDigit(num % 10, "I", "V", "X"));
    }
    public String romanDigit(int n, String onechar, String fivechar, String tenchar) {
        if (n == 0) return "";
        if (n == 1) return onechar;
        if (n == 2) return onechar + onechar;
        if (n == 3) return onechar + onechar + onechar;
        if (n == 4) return onechar + fivechar;
        if (n == 5) return fivechar;
        if (n == 6) return fivechar + onechar;
        if (n == 7) return fivechar + onechar + onechar;
        if (n == 8) return fivechar + onechar + onechar + onechar;
        if (n == 9) return onechar + tenchar;
        
        return null;
    }
    
    /*
     * https://discuss.leetcode.com/topic/20510/my-java-solution-easy-to-understand
     * 
     * Other code :
     * https://discuss.leetcode.com/topic/20510/my-java-solution-easy-to-understand/3
     * https://discuss.leetcode.com/topic/43014/my-java-solution
     */
	public String intToRoman_keep_minus(int num) {
		int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
	    String[] strs = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
	    
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < values.length; i++) {
			while (num >= values[i]) {
				num -= values[i];
				sb.append(strs[i]);
			}
		}
		return sb.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/34713/my-solutions-in-java
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/2858/share-my-iterative-solution
	 */
	public String intToRoman_residual(int num) {
		String data[] = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
		int value[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1}, 
				base = -1;
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			if ((base = num / value[i]) != 0) {
				while (base-- != 0)
					result.append(data[i]);
				num = num % value[i];
			}
		}
		return result.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/13230/my-easy-understanding-solution
	 * 
	 * Other code (recursive) :
	 * https://discuss.leetcode.com/topic/23879/straight-forward-java-recursive-solution
	 */
	public String intToRoman_loop(int num) {
		String s = "";

		while (num > 0) {
			if (num >= 1000) {
				s += "M";
				num -= 1000;
				continue;
			}
			if (num >= 900) {
				s += "CM";
				num -= 900;
				continue;
			}
			if (num >= 500) {
				s += "D";
				num -= 500;
				continue;
			}
			if (num >= 400) {
				s += "CD";
				num -= 400;
				continue;
			}
			if (num >= 100) {
				s += "C";
				num -= 100;
				continue;
			}
			if (num >= 90) {
				s += "XC";
				num -= 90;
				continue;
			}
			if (num >= 50) {
				s += "L";
				num -= 50;
				continue;
			}
			if (num >= 40) {
				s += "XL";
				num -= 40;
				continue;
			}
			if (num >= 10) {
				s += "X";
				num -= 10;
				continue;
			}
			if (num >= 9) {
				s += "IX";
				num -= 9;
				continue;
			}
			if (num >= 5) {
				s += "V";
				num -= 5;
				continue;
			}
			if (num >= 4) {
				s += "IV";
				num -= 4;
				continue;
			}
			// For faster
			if (num == 3) {
				s += "III";
				num -= 3;
				continue;
			}
			if (num == 2) {
				s += "II";
				num -= 2;
				continue;
			}
			if (num == 1) {
				s += "I";
				num -= 1;
				continue;
			}
		}

		return s;
	}
	
	// https://discuss.leetcode.com/topic/26543/easy-to-understand-java-solution

}
