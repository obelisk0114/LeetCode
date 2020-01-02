package OJ0091_0100;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Decode_Ways {
	/*
	 * https://leetcode.com/problems/decode-ways/discuss/30358/Java-clean-DP-solution-with-explanation
	 * 
	 * dp[0] means an empty string will have one way to decode, dp[1] means the way to 
	 * decode a string of size 1.
	 * 
	 * For the last digit in the string,
	 * 1. if we take it as an individual letter, then the possibility depends on the 
	 *    rest of the string,
	 * 2. if we take it along with the previous letter (which is from 10 to 26), the 
	 *    possibility depends on the rest of string except the last 2.
	 * So f(s) = f(s - last letter) + f(s - last 2 letters)
	 * 
	 * But there are special cases:
	 * 1. if the last 2 digits are not a letter, which is not from 10 to 26, then 
	 *    f(s) = f(s - last letter)
	 * 2. if the last digital is 0, which means it has to go with the previous letter, 
	 *    then f(s) = f(s - last 2 letter).
	 * 
	 * Whenever we encounter dp[i] = 0, which means the sub-sequence from the 
	 * beginning to current position is not decodable, we can return 0.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/decode-ways/discuss/30661/My-Java-DP-solution
	 * https://leetcode.com/problems/decode-ways/discuss/30358/Java-clean-DP-solution-with-explanation/173239
	 * 
	 * Other code:
	 * https://leetcode.com/problems/decode-ways/discuss/30405/DP-with-easy-understand-Java-Solution
	 */
	public int numDecodings_dp(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		int n = s.length();
		int[] dp = new int[n + 1];
		dp[0] = 1;
		dp[1] = s.charAt(0) != '0' ? 1 : 0;
		
		for (int i = 2; i <= n; i++) {
			int first = Integer.valueOf(s.substring(i - 1, i));
			int second = Integer.valueOf(s.substring(i - 2, i));
			
			if (first >= 1 && first <= 9) {
				dp[i] += dp[i - 1];
			}
			if (second >= 10 && second <= 26) {
				dp[i] += dp[i - 2];
			}
		}
		return dp[n];
	}
	
	/*
	 * by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/decode-ways/discuss/30522/Java-2ms-DP-solution-with-detailed-explanation-and-inline-comments
	 * 
	 * 1. 00 => return 0;
	 * 2. 0X => can not use 0
	 * 3. X0 => must use 0 (if X0 > 26 return 0)
	 * 4. XX => separate use, if XX <= 26 can together 
	 */
	public int numDecodings_self2_dp2(String s) {
		if (s.isEmpty() || s.charAt(0) - '0' == 0) {
			return 0;
		}

		int[] waysToDecode = new int[s.length() + 1];
		
		// There is only 1 way to decode an empty string.
		waysToDecode[0] = 1;
		waysToDecode[1] = 1;
		
		for (int i = 1; i < s.length(); i++) {
			int curr = s.charAt(i) - '0';
			int prev = s.charAt(i - 1) - '0';
            
			if (prev == 0 && curr == 0) {
				return 0;
			}
			// can't use previous value
			else if (prev == 0) {
				waysToDecode[i + 1] = waysToDecode[i];
			}
			// can't use current state
			else if (curr == 0) {
                if (prev < 3) {
                    waysToDecode[i + 1] = waysToDecode[i - 1];
                }
				else {
                    return 0;
                }
			}
			else {
				waysToDecode[i + 1] = waysToDecode[i];
                if (prev * 10 + curr <= 26) {
                    waysToDecode[i + 1] += waysToDecode[i - 1];
                }
			}
		}

		return waysToDecode[waysToDecode.length - 1];
	}
	
	/*
	 * https://leetcode.com/problems/decode-ways/discuss/30522/Java-2ms-DP-solution-with-detailed-explanation-and-inline-comments
	 * 
	 * There are four possibilities to consider: 
	 * 1) The previous value is 0 and the current value is 0, we can't make progress, 
	 *    return 0. 
	 * 2) The current value is 0, we have to use the previous value, if it is greater 
	 *    than 2, we can't make progress, return 0, otherwise we have to transition to 
	 *    this state from waysToDecode[i - 1]. 
	 * 3) The previous value is 0, we can't use the previous, so the only way to 
	 *    transition to current state is from the previous, so use waysToDecode[i]. 
	 * 4) lastly, both previous and curr can be used so there are two ways to 
	 *    transition to the current state, thus at waysToDecode[i + 1] we can get here 
	 *    by using all the ways we can get to waysToDecode[i] + all the ways to get to 
	 *    waysToDecode[i - 1].
	 */
	public int numDecodings_dp2(String s) {
		if (s.isEmpty() || s.charAt(0) - '0' == 0) {
			return 0;
		}

		int[] waysToDecode = new int[s.length() + 1];
		
		// There is only 1 way to decode an empty string.
		waysToDecode[0] = 1;
		waysToDecode[1] = 1;
		
		for (int i = 1; i < s.length(); i++) {
			int curr = s.charAt(i) - '0';
			int prev = s.charAt(i - 1) - '0';

			// can't make progress, return 0
			if (prev == 0 && curr == 0 || (curr == 0 && prev > 2)) {
				return 0;
			}
			// can't use previous value, can only get to this state from the previous
			else if (prev == 0 || (prev * 10 + curr) > 26) {
				waysToDecode[i + 1] = waysToDecode[i];
			}
			// can't use current state, can only get to this state from i - 1 state
			else if (curr == 0) {
				waysToDecode[i + 1] = waysToDecode[i - 1];
			}
			// can get to this state from the previous two states
			else {
				waysToDecode[i + 1] = waysToDecode[i] + waysToDecode[i - 1];
			}
		}

		return waysToDecode[waysToDecode.length - 1];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/decode-ways/discuss/30625/Sharing-my-java-memoized-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/decode-ways/discuss/30451/Evolve-from-recursion-to-dp/172980
	 */
	public int numDecodings_top_down(String s) {
		if (s == null || s.length() == 0)
			return 0;

		Set<String> symbols = new HashSet<String>();
		for (int i = 1; i <= 26; i++) {
			symbols.add(String.valueOf(i));
		}

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		return numDec(s, 0, map, symbols);
	}

	private int numDec(String str, int start, Map<Integer, Integer> map, 
			Set<String> symbols) {
		if (map.containsKey(start)) {
			return map.get(start);
		}
		if (start == str.length()) {
			return 1;
		}

		int numWays = 0;
		if ((start + 1 <= str.length()) && 
				symbols.contains(str.substring(start, start + 1)))
			numWays += numDec(str, start + 1, map, symbols);

		if ((start + 2 <= str.length()) && 
				symbols.contains(str.substring(start, start + 2)))
			numWays += numDec(str, start + 2, map, symbols);

		map.put(start, numWays);
		return numWays;
	}
	
	/*
	 * https://leetcode.com/problems/decode-ways/discuss/30357/DP-Solution-(Java)-for-reference
	 * 
	 * build up from right: memo[i] means ways of decoding for substring(i, end).
	 * 
	 * if s[i:i+1] (both included) <= 26, 
	 *     num_ways[i+2] + num_ways[i+1]
	 * else:
	 *     num_ways[i+1]
	 * 
	 * case with 0:
	 * If i = '0', let memo[i] = 0, also implements for a string where the ith 
	 * character == '0', string[i:end] can be decoded in 0 ways.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/decode-ways/discuss/30357/DP-Solution-(Java)-for-reference/29441
	 * https://leetcode.com/problems/decode-ways/discuss/30357/DP-Solution-(Java)-for-reference/29449
	 * https://leetcode.com/problems/decode-ways/discuss/30357/DP-Solution-(Java)-for-reference/29432
	 */
	public int numDecodings_dp_back(String s) {
		int n = s.length();
		if (n == 0)
			return 0;

		int[] memo = new int[n + 1];
		memo[n] = 1;
		memo[n - 1] = s.charAt(n - 1) != '0' ? 1 : 0;

		for (int i = n - 2; i >= 0; i--)
			// Case 1: individual 0 does not work
            // Case 2: 0 preceding any number does not work
			if (s.charAt(i) == '0')
				continue;
			// memo[i + 1] + memo[i + 2]:
			//   The character pair works, so we need to add up the total ways
			//   of decoding for the two pairs.
			// memo[i + 1]:
			//   The pair does not work, just need to move the previous total
			//   so that memo[0] will eventually have the right count.
			else
				memo[i] = (Integer.parseInt(s.substring(i, i + 2)) <= 26) ? 
						memo[i + 1] + memo[i + 2] : memo[i + 1];

		return memo[0];
	}
	
	/*
	 * https://leetcode.com/problems/decode-ways/discuss/30416/Simple-java-solution-with-O(1)-space
	 * 
	 * If all of two adjacent digits in input are valid for a letter, it will be same 
	 * as Fibonacci problem.
	 * Example: input is "11111". Result is equal to "1111" plus "111".
	 * 
	 * Other code:
	 * https://leetcode.com/problems/decode-ways/discuss/30416/Simple-java-solution-with-O(1)-space/29527
	 * https://leetcode.com/problems/decode-ways/discuss/30357/DP-Solution-(Java)-for-reference/29447
	 */
	public int numDecodings_constant(String s) {
		int n1 = 1, n2 = 1, newResult = 0;
		if (s.length() == 0 || s.charAt(0) == '0')
			return 0;
		
		for (int i = 2; i <= s.length(); i++) {
			newResult = 0;
			if (s.charAt(i - 1) != '0')
				newResult = n2;
			
			int num = Integer.parseInt(s.substring(i - 2, i));
			if (num >= 10 && num <= 26)
				newResult += n1;
			
			n1 = n2;
			n2 = newResult;
		}
		return n2;
	}

	// by myself
	public int numDecodings_self(String s) {
        if (s == null || s.length() == 0)
            return 0;
        
        int[] map = new int[s.length()];
        if (s.charAt(0) != '0')
            map[0] = 1;
        else
            map[0] = 0;
        
        if (s.length() == 1)
            return map[0];
        
        if (s.charAt(0) == '0' && s.charAt(1) == '0')
            map[1] = 0;
        else if (s.charAt(0) == '0' && s.charAt(1) != '0')
            map[1] = 0;
        else if (s.charAt(0) == '1' && s.charAt(1) == '0')
            map[1] = 1;
        else if (s.charAt(0) == '1' && s.charAt(1) != '0')
            map[1] = 2;
        else if (s.charAt(0) == '2' && s.charAt(1) == '0')
            map[1] = 1;
        else if (s.charAt(0) == '2' && s.charAt(1) <= '6')
            map[1] = 2;
        else if (s.charAt(0) == '2' && s.charAt(1) > '6')
            map[1] = 1;
        else if (s.charAt(1) == '0')
            map[1] = 0;
        else
            map[1] = 1;
        
        for (int i = 2; i < map.length; i++) {
            if (s.charAt(i - 1) == '0') {
                if (s.charAt(i) == '0')
                    return 0;
                else
                    map[i] = map[i - 1];
            }
            else {
                if (s.charAt(i) != '0') {
                    map[i] = map[i - 1];
                }
            
                int two = Integer.parseInt(s.charAt(i - 1) + Character.toString(s.charAt(i)));
                if (two <= 26 && two > 0)
                    map[i] += map[i - 2];
            }
        }
        return map[map.length - 1];
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/decode-ways/discuss/30352/Accpeted-Python-DP-solution
     * https://leetcode.com/problems/decode-ways/discuss/253018/Python%3A-Easy-to-understand-explanation-bottom-up-dynamic-programming
     * https://leetcode.com/problems/decode-ways/discuss/30379/1-liner-O(1)-space
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/decode-ways/discuss/30451/Evolve-from-recursion-to-dp
     * https://leetcode.com/problems/decode-ways/discuss/30474/My-c%2B%2B-0ms-DP-solution-O(n)
     * https://leetcode.com/problems/decode-ways/discuss/30384/A-concise-dp-solution
     * https://leetcode.com/problems/decode-ways/discuss/30519/Share-my-4-ms-simple-C%2B%2B-code
     */

}
