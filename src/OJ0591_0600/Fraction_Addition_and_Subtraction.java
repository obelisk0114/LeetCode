package OJ0591_0600;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Fraction_Addition_and_Subtraction {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/articles/kill-process-3/
	 */
	public String fractionAddition(String expression) {
        List < Character > sign = new ArrayList < > ();
        if (expression.charAt(0) != '-')
            sign.add('+');
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-')
                sign.add(expression.charAt(i));
        }
        
        int prev_num = 0, prev_den = 1, i = 0;
        for (String sub: expression.split("(\\+)|(-)")) {
            if (sub.length() > 0) {
                String[] fraction = sub.split("/");
                int num = (Integer.parseInt(fraction[0]));
                int den = (Integer.parseInt(fraction[1]));
                int g = Math.abs(gcd(den, prev_den));
                
                if (sign.get(i++) == '+')
                    prev_num = prev_num * den / g + num * prev_den / g;
                else
                    prev_num = prev_num * den / g - num * prev_den / g;
                
                prev_den = den * prev_den / g;
                g = Math.abs(gcd(prev_den, prev_num));
                prev_num /= g;
                prev_den /= g;
            }
        }
        return prev_num + "/" + prev_den;
    }
    public int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/90061/small-simple-c-java-python
	 * 
	 * The (?=) part is a zero-width positive lookahead. Since [-+] means - or +, 
	 * the regex (?=[-+]) means the next element is either - or +.
	 * 
	 * Since | is logical or operator, "/|(?=[-+])" means the element is "/", or the 
	 * next element is either - or +. For example, when expression = "-1/2+1/2-1/3",
	 * generates [-1, 2, +1, 2, -1, 3 ]. Note that the signs - and + are preserved.
	 * 
	 * Rf :
	 * https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
	 * https://discuss.leetcode.com/topic/90061/small-simple-c-java-python/14
	 */
	public String fractionAddition2(String expression) {
		Scanner scanner = new Scanner(expression);
		Scanner sc = scanner.useDelimiter("/|(?=[-+])");
		int A = 0, B = 1;
		while (sc.hasNext()) {
			int a = sc.nextInt(), b = sc.nextInt();
			A = A * b + a * B;
			B *= b;
			int g = gcd2(A, B);
			A /= g;
			B /= g;
		}
		scanner.close();
		return A + "/" + B;
	}

	private int gcd2(int a, int b) {
		return a != 0 ? gcd2(b % a, a) : Math.abs(b);
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/89993/java-solution-fraction-addition-and-gcd
	 */
	public String fractionAddition_substring(String expression) {
		List<String> nums = new ArrayList<>();
		int i = 0, j = 0;
		while (j <= expression.length()) {
			if (j == expression.length() || 
				j != i && (expression.charAt(j) == '+' || expression.charAt(j) == '-')) {
				if (expression.charAt(i) == '+') {
					nums.add(expression.substring(i + 1, j));
				} 
				else {
					nums.add(expression.substring(i, j));
				}

				i = j;
			}
			j++;
		}

		String result = "0/1";

		for (String num : nums) {
			result = add_substring(result, num);
		}

		return result;
	}
	private String add_substring(String s1, String s2) {
		String[] sa1 = s1.split("/");
		String[] sa2 = s2.split("/");
		int n1 = Integer.parseInt(sa1[0]);
		int d1 = Integer.parseInt(sa1[1]);
		int n2 = Integer.parseInt(sa2[0]);
		int d2 = Integer.parseInt(sa2[1]);

		int n = n1 * d2 + n2 * d1;
		int d = d1 * d2;

		if (n == 0)
			return "0/1";

		boolean isNegative = n * d < 0;
		n = Math.abs(n);
		d = Math.abs(d);
		int gcd = getGCD_substring(n, d);

		return (isNegative ? "-" : "") + (n / gcd) + "/" + (d / gcd);
	}
	private int getGCD_substring(int a, int b) {
		if (a == 0 || b == 0)
			return a + b;         // base case
		return getGCD_substring(b, a % b);
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/93361/very-concise-java-solution
	 */
	public String fractionAddition_self(String expression) {
        List<Integer> numerator = new ArrayList<>();
        List<Integer> denominator = new ArrayList<>();
        int i = 0;
        int pre = 0;
        int sign = 1;
        if (expression.charAt(i) == '-') {
            sign = -1;
            i++;
        }
        
        boolean up = true;
        while (i < expression.length()) {
            char now = expression.charAt(i);
            if (now == '/') {
                up = false;
                i++;
            }
            else if (now == '-') {
                sign = -1;
                up = true;
                i++;
            }
            else if (now == '+') {
                sign = 1;
                up = true;
                i++;
            }
            else {
                pre = now - '0';
                i++;
                if (up) {
                	while (i < expression.length() && expression.charAt(i) != '/') {
                        pre = pre * 10 + (expression.charAt(i++) - '0');
                    }
                    pre *= sign;
                    numerator.add(pre);
                }
                else {
                    while (i < expression.length() 
                    	&& expression.charAt(i) != '+' && expression.charAt(i) != '-') {
                        pre = pre * 10 + (expression.charAt(i++) - '0');
                    }
                    denominator.add(pre);
                }
            }
        }
        
        int lower = 1;
        int upper = 0;
        for (Integer element : denominator) {
            lower *= element;
        }
        for (int j = 0; j < numerator.size(); j++) {
            upper = upper + numerator.get(j) * lower / denominator.get(j);
        }
        int GCD = findGCD_self(lower, Math.abs(upper));
        lower /= GCD;
        upper /= GCD;
        return upper + "/" + lower;
    }
    private int findGCD_self(int a, int b) {
        if (a == b)
            return a;
        
        while (b != 0) {
            a %= b;
            int tmp = a;
            a = b;
            b = tmp;
        }
        return a;
    }
    
    // https://discuss.leetcode.com/topic/89991/concise-java-solution
    
    // https://discuss.leetcode.com/topic/94591/java-code-new-class-fraction
    
    // https://discuss.leetcode.com/topic/91526/not-fastest-but-play-around-with-design-patterns

}
