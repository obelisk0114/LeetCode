package OJ0161_0170;

import java.util.HashMap;

public class Fraction_to_Recurring_Decimal {
	/*
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51106/My-clean-Java-solution
	 * 
	 * The important thing is to consider all edge cases while thinking this problem 
	 * through, including: negative integer, possible overflow, etc.
	 * 
	 * Use HashMap to store a remainder and its associated index while doing the 
	 * division so that whenever a same remainder comes up, we know there is a 
	 * repeating fractional part.
	 * 
	 * Rf : https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51109/Accepted-cpp-solution-with-explainations
	 */
	public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder res = new StringBuilder();
        
        // determine the sign
        res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
        
        // remove sign of operands
        long num = Math.abs((long)numerator);
        long den = Math.abs((long)denominator);
        
        // integral part
        res.append(num / den);
        num %= den;
        if (num == 0) {
            return res.toString();
        }
        
        // fractional part
        res.append(".");
        HashMap<Long, Integer> map = new HashMap<Long, Integer>();
        map.put(num, res.length());
        while (num != 0) {
            num *= 10;
            res.append(num / den);
            num %= den;
            if (map.containsKey(num)) {
                int index = map.get(num);
                res.insert(index, "(");
                res.append(")");
                break;
            }
            else {
                map.put(num, res.length());
            }
        }
        return res.toString();
    }
	
	/*
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51235/Online-judge-pass-java-version
	 * 
	 * 1. check the sign first
	 * 2. can get integer part directly by a/b, then deal with decimal part
	 * 3. get remainder by a%b
	 * 4. for each loop, we can get the digit by remainder*10 / b and update new 
	 *    remainder = remainder*10 % b, if we get the same remainder again, 
	 *    previous result is repeated
	 * 5. use a map to store the remainder(s) that already appeared and int that need 
	 *    to append to res
	 * 6. check remainder for each loop, if 0 break the loop
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51140/Short-Java-solution
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51128/Simple-and-Short-Solution-in-JAVA
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51228/My-java-solution
	 */
	public String fractionToDecimal2(int numerator, int denominator) {
		if (denominator == 0)
			return "";
		
		StringBuilder str = new StringBuilder();
		HashMap<Long, Integer> map = new HashMap<Long, Integer>();
		
		if (numerator < 0 && denominator > 0 || numerator > 0 && denominator < 0) {
			str.append('-');
		}
		
		long num = Math.abs((long) numerator);
		long den = Math.abs((long) denominator);
		long n = num / den;
		long reminder = num % den;
		
		str.append(n);
		if (reminder == 0)
			return str.toString();
		
		str.append('.');
		
		while (reminder != 0 && !map.containsKey(reminder)) {
			map.put(reminder, str.length());
			n = reminder * 10 / den;
			str.append(n);
			reminder = (reminder * 10) % den;
		}
		
		if (map.containsKey(reminder)) {
			str.insert(map.get(reminder), "(");
			str.append(')');
		}
		return str.toString();
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/51113/Fastest-Java-solution-(1-ms)-and-O(1)-space
	 * 
	 * Base on the Floyd's cycle-finding algorithm. 
	 * Instead of using hashMap to store the result of each step, It uses two pointer, 
	 * fast (iterate twice each time) and slow (iterate once) to detect cycle. 
	 * It finds out the non-cycle part and cycle part. 
	 * Therefore, it's O(1) space and is faster than those of using hash.
	 */
	public String fractionToDecimal_cycleFinding(int numerator, int denominator) {
		// negative sign
		boolean negative = (numerator < 0) ^ (denominator < 0);
		
		long n = Math.abs((long) numerator);
		long d = Math.abs((long) denominator);
		long intPart = n / d;
		long rest = n - intPart * d;
		
		// Integer result
		if (rest == 0)
			return negative ? String.valueOf(intPart * (-1)) : String.valueOf(intPart);
			
		StringBuilder res = new StringBuilder();
		if (negative)
			res.append("-");
		
		res.append(intPart);
		res.append(".");
		
		long slow;
		long fast;
		long[] temp = new long[2];
		slow = Decimal(rest * 10, d)[1];
		fast = Decimal(Decimal(rest * 10, d)[1], d)[1];
		
		while (slow != fast) {
			slow = Decimal(slow, d)[1];
			fast = Decimal(Decimal(fast, d)[1], d)[1];
		}
		slow = rest * 10;
		while (slow != fast && slow != 0) {
			temp = Decimal(slow, d);
			slow = temp[1];
			res.append(temp[0]); // non-cycle part
			fast = Decimal(fast, d)[1];
		}
		
		// return when result is finite decimal
		if (slow == 0)
			return res.toString();
		
		temp = Decimal(slow, d);
		fast = temp[1];
		res.append("(");
		res.append(temp[0]);
		while (slow != fast) {
			temp = Decimal(fast, d);
			fast = temp[1];
			res.append(temp[0]); // cycle part
		}
		res.append(")");
		return res.toString();
	}
	public long[] Decimal(long rest, long denominator) {
		// return the quotient and remainder (multiplied by 10)
		long r1;
		long r2;
		if (rest < denominator) {
			r1 = 0;
			r2 = rest * 10;
		} 
		else {
			r1 = rest / denominator;
			r2 = (rest - denominator * r1) * 10;
		}
		return new long[] { r1, r2 };
	}

}
