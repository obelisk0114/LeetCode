package OJ0271_0280;

public class Integer_to_English_Words {
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/integer-to-english-words/discuss/70627/Short-clean-Java-solution
	 */
	private final String[] belowTen = new String[] { "", "One", "Two", "Three", 
			"Four", "Five", "Six", "Seven", "Eight", "Nine" };
	private final String[] belowTwenty = new String[] { "Ten", "Eleven", "Twelve", 
			"Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", 
			"Nineteen" };
	private final String[] belowHundred = new String[] { "", "Ten", "Twenty", 
			"Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

	public String numberToWords2(int num) {
		if (num == 0)
			return "Zero";
		return helper2(num);
	}

	private String helper2(int num) {
		String result = new String();
		
		if (num < 10)
			result = belowTen[num];
		else if (num < 20)
			result = belowTwenty[num - 10];
		else if (num < 100)
			result = belowHundred[num / 10] + " " + belowTen[num % 10];
		else if (num < 1000)
			result = helper2(num / 100) + " Hundred " + helper2(num % 100);
		else if (num < 1000000)
			result = helper2(num / 1000) + " Thousand " + helper2(num % 1000);
		else if (num < 1000000000)
			result = helper2(num / 1000000) + " Million " + helper2(num % 1000000);
		else
			result = helper2(num / 1000000000) + " Billion " + helper2(num % 1000000000);
		
		return result.trim();
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/integer-to-english-words/discuss/70647/4-ms-Java-Solution
	 */
	public String numberToWords(int num) {
		if (num == 0) {
			return "Zero";
		}
		return helper(num);
	}

	public String helper(int num) {
		String[] words = new String[] { "", "One", "Two", "Three", "Four", "Five", 
				"Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", 
				"Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", 
				"Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", 
				"Ninety" };
		StringBuilder res = new StringBuilder();

		if (num >= 1000000000) {
			res.append(helper(num / 1000000000)).append(" Billion ")
			.append(helper(num % 1000000000));
		} 
		else if (num >= 1000000) {
			res.append(helper(num / 1000000)).append(" Million ")
			.append(helper(num % 1000000));
		} 
		else if (num >= 1000) {
			res.append(helper(num / 1000)).append(" Thousand ")
			.append(helper(num % 1000));
		} 
		else if (num >= 100) {
			res.append(helper(num / 100)).append(" Hundred ").append(helper(num % 100));
		} 
		else if (num >= 20) {
			res.append(words[(num - 20) / 10 + 20]).append(" ").append(words[num % 10]);
		} 
		else {
			res.append(words[num]);
		}

		return res.toString().trim();
	}
	
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/integer-to-english-words/discuss/70625/My-clean-Java-solution-very-easy-to-understand
	 * 
	 * Other code :
	 * https://leetcode.com/problems/integer-to-english-words/discuss/70759/My-Java-Solution
	 */
	private final String[] LESS_THAN_20 = { "", "One", "Two", "Three", "Four", "Five", 
			"Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", 
			"Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
	private final String[] TENS = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", 
			"Sixty", "Seventy", "Eighty", "Ninety" };
	private final String[] THOUSANDS = { "", "Thousand", "Million", "Billion" };

	public String numberToWords3(int num) {
		if (num == 0)
			return "Zero";

		int i = 0;
		String words = "";

		while (num > 0) {
			if (num % 1000 != 0)
				words = helper3(num % 1000) + THOUSANDS[i] + " " + words;
			num /= 1000;
			i++;
		}

		return words.trim();
	}

	private String helper3(int num) {
		if (num == 0)
			return "";
		else if (num < 20)
			return LESS_THAN_20[num] + " ";
		else if (num < 100)
			return TENS[num / 10] + " " + helper3(num % 10);
		else
			return LESS_THAN_20[num / 100] + " Hundred " + helper3(num % 100);
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/integer-to-english-words/discuss/158134/Shorter-than-the-best-answer.-Pure-recursion
	 * 
	 * IM stands for Integer map and SM stands for String Map. 
	 * The reason why IM[i] >= 100 is considered is because if we have some number 
	 * like 100, 1000 etc..we say it as "one hundred" and "one thousand" but it we 
	 * have a number like 90, 80 we don't say "one ninety" or "one eighty"
	 */
	private static final int[] IM = {1000000000, 1000000, 1000, 100, 90, 80, 70, 60, 
			50, 40, 30, 20, 19, 18, 17, 16, 15, 14,13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 
			3, 2, 1};
    private static final String[] SM = {"Billion", "Million", "Thousand", "Hundred", 
    		"Ninety", "Eighty", "Seventy", "Sixty", "Fifty", "Forty", "Thirty", 
    		"Twenty", "Nineteen", "Eighteen", "Seventeen", "Sixteen", "Fifteen", 
    		"Fourteen", "Thirteen", "Twelve", "Eleven", "Ten", "Nine", "Eight", 
    		"Seven", "Six", "Five", "Four", "Three", "Two", "One"};

	public String numberToWords_recursion(int num) {
		if (num == 0) {
			return "Zero";
		}
		return util(num);
	}

	public String util(int num) {
		String ret = "";
		for (int i = 0; i < IM.length; i++) {
			if (num >= IM[i]) {
				ret = (IM[i] >= 100 ? util(num / IM[i]) + " " : "") + SM[i] + " " + 
			           util(num % IM[i]);
				break;
			}
        }
        return ret.trim();
    }
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://leetcode.com/problems/integer-to-english-words/discuss/70660/Short-clean-java-solution-using-Math.log10-and-Math.pow
	 */
	public String numberToWords_self(int num) {
        String[] one = {"Zero", "One", "Two", "Three", "Four"
        		, "Five", "Six", "Seven", "Eight", "Nine"};
        String[] ten = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen"
        		, "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        String[] tens = {"Zero", "Ten", "Twenty", "Thirty", "Forty"
        		, "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        
        if (num < 10)
            return one[num];
        if (num < 20)
            return ten[num - 10];
        
        String ans = "";
        int d = (int) Math.log10((double) num);
        if (d == 9) {
            ans = ans + one[(int) (num / Math.pow(10, 9))] + " Billion ";
            num = (int) (num % Math.pow(10, 9));
            d = (int) Math.log10((double) num);
        }
        if (d >= 6) {
            int cur = (int) (num / Math.pow(10, 6));
            ans = ans + lessThousand(cur, one, tens, ten);
            ans += "Million ";
                
            num = (int) (num % Math.pow(10, 6));
            d = (int) Math.log10((double) num);
        }
        if (d >= 3) {
            int cur = (int) (num / Math.pow(10, 3));
            ans = ans + lessThousand(cur, one, tens, ten);
            ans += "Thousand ";
                
            num = (int) (num % Math.pow(10, 3));
            d = (int) Math.log10((double) num);
        }
        ans = ans + lessThousand(num, one, tens, ten);
        return ans.trim();
    }
    
    public String lessThousand(int cur, String[] one, String[] tens, String[] ten) {
        String res = "";
        if (cur >= 100) {
            res = res + one[cur / 100] + " Hundred ";
            cur = cur % 100;
        }
        if (cur >= 20) {
            res = res + tens[cur / 10] + " ";
            cur = cur % 10;
        }
        if (cur >= 10) {
            res = res + ten[cur - 10] + " ";
            cur = 0;
        }
        if (cur != 0) {
            res = res + one[cur] + " ";
        }
        return res;
    }
    
    // https://leetcode.com/problems/integer-to-english-words/discuss/215947/Facebook-follow-up-question-and-solution
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/integer-to-english-words/discuss/70632/Recursive-Python
     * https://leetcode.com/problems/integer-to-english-words/discuss/70688/Python-Clean-Solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/integer-to-english-words/discuss/70651/Fairly-Clear-4ms-C%2B%2B-solution
     * https://leetcode.com/problems/integer-to-english-words/discuss/70756/Short-clean-C%2B%2B-code-with-explanation
     * https://leetcode.com/problems/integer-to-english-words/discuss/188334/C%2B%2B-4-lines
     * https://leetcode.com/problems/integer-to-english-words/discuss/70761/If-you-know-how-to-read-numbersyou-can-make-it.
     */

}
