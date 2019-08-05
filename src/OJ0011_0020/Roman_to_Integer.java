package OJ0011_0020;

import java.util.Map;
import java.util.HashMap;

public class Roman_to_Integer {
	/*
	 * https://leetcode.com/problems/roman-to-integer/discuss/6520/JAVA-Easy-Version-To-Understand!!!!
	 * 
	 * Rf : https://leetcode.com/problems/roman-to-integer/discuss/6520/JAVA-Easy-Version-To-Understand!!!!/202358
	 * 
	 * Other code :
	 * https://leetcode.com/problems/roman-to-integer/discuss/6529/My-solution-for-this-question-but-I-don't-know-is-there-any-easier-way/141371
	 */
	public static int romanToInt_map(String s) {
		if (s == null || s.length() == 0)
			return -1;
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		map.put('I', 1);
		map.put('V', 5);
		map.put('X', 10);
		map.put('L', 50);
		map.put('C', 100);
		map.put('D', 500);
		map.put('M', 1000);
		
		int len = s.length(), result = map.get(s.charAt(len - 1));
		for (int i = len - 2; i >= 0; i--) {
			if (map.get(s.charAt(i)) >= map.get(s.charAt(i + 1)))
				result += map.get(s.charAt(i));
			else
				result -= map.get(s.charAt(i));
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/roman-to-integer/discuss/6760/Share-my-fast-java-solution-scan-string-once.
	 * 
	 * if previous character is greater than current character, we add the value of 
	 *    current character,
	 * else we subtract the previous char twice(since we added it once).
	 * 
	 * Other code :
	 * https://leetcode.com/problems/roman-to-integer/discuss/6776/Java-clean-and-fast-solution
	 * https://leetcode.com/problems/roman-to-integer/discuss/281138/Simple-java-solution-100-time
	 */
	public int romanToInt_switch(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		int pre = charToInt(s.charAt(0));
		int sum = pre;
		for (int i = 1; i < s.length(); i++) {
			int cur = charToInt(s.charAt(i));
			if (pre < cur) {
				sum -= 2 * pre;
			}
			sum += cur;
			pre = cur;
		}
		return sum;
	}

	public int charToInt(char cha) {
		switch (cha) {
		case 'I':
			return 1;
		case 'V':
			return 5;
		case 'X':
			return 10;
		case 'L':
			return 50;
		case 'C':
			return 100;
		case 'D':
			return 500;
		case 'M':
			return 1000;
		}
		return 0;
	}
	
	/*
	 * https://leetcode.com/problems/roman-to-integer/discuss/6529/My-solution-for-this-question-but-I-don't-know-is-there-any-easier-way
	 * 
	 * Count every Symbol and add its value to the sum, and minus the extra part of 
	 * special cases.
	 * 
	 * Rf : https://leetcode.com/problems/roman-to-integer/discuss/6529/My-solution-for-this-question-but-I-don't-know-is-there-any-easier-way/7863
	 */
	public int romanToInt_indexOf(String s) {
		int sum = 0;
		if (s.indexOf("IV") != -1) {
			sum -= 2;
		}
		if (s.indexOf("IX") != -1) {
			sum -= 2;
		}
		if (s.indexOf("XL") != -1) {
			sum -= 20;
		}
		if (s.indexOf("XC") != -1) {
			sum -= 20;
		}
		if (s.indexOf("CD") != -1) {
			sum -= 200;
		}
		if (s.indexOf("CM") != -1) {
			sum -= 200;
		}

		char c[] = s.toCharArray();
		int count = 0;

		for (; count <= s.length() - 1; count++) {
			if (c[count] == 'M')
				sum += 1000;
			if (c[count] == 'D')
				sum += 500;
			if (c[count] == 'C')
				sum += 100;
			if (c[count] == 'L')
				sum += 50;
			if (c[count] == 'X')
				sum += 10;
			if (c[count] == 'V')
				sum += 5;
			if (c[count] == 'I')
				sum += 1;
		}
		return sum;
	}
	
	/*
	 * https://leetcode.com/problems/roman-to-integer/discuss/6802/Java-Solution-Clean-and-Simple-%3A)-(-7-ms-)
	 * 
	 * If a current character value is greater than that of the previous, we have to 
	 * subtract it. We subtract twice, because previously iteration had blindly 
	 * added it.
	 * 
	 * Rf : https://leetcode.com/problems/roman-to-integer/discuss/6802/Java-Solution-Clean-and-Simple-:)-(-7-ms-)/7983
	 * 
	 * Other code :
	 * https://leetcode.com/problems/roman-to-integer/discuss/6746/Easy-to-understand-Java-solution-beats-98-Well-explained-with-comments.
	 */
	public int romanToInt_array(String str) {
	    int[] a = new int[26];
	    a['I' - 'A'] = 1;
	    a['V' - 'A'] = 5;
	    a['X' - 'A'] = 10;
	    a['L' - 'A'] = 50;
	    a['C' - 'A'] = 100;
	    a['D' - 'A'] = 500;
	    a['M' - 'A'] = 1000;
	    
	    char prev = 'A';
	    int sum = 0;
	    for(char s : str.toCharArray()) {
	        if(a[s - 'A'] > a[prev - 'A']) {
	            sum = sum - 2 * a[prev - 'A'];
	        }
	        sum = sum + a[s - 'A'];
	        prev = s;
	    }
	    return sum;
	}
	
	// https://leetcode.com/problems/roman-to-integer/discuss/6509/7ms-solution-in-Java.-easy-to-understand
	public int romanToInt_2_pass(String s) {
		int nums[] = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case 'M':
				nums[i] = 1000;
				break;
			case 'D':
				nums[i] = 500;
				break;
			case 'C':
				nums[i] = 100;
				break;
			case 'L':
				nums[i] = 50;
				break;
			case 'X':
				nums[i] = 10;
				break;
			case 'V':
				nums[i] = 5;
				break;
			case 'I':
				nums[i] = 1;
				break;
			}
		}
		
		int sum = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] < nums[i + 1])
				sum -= nums[i];
			else
				sum += nums[i];
		}
		return sum + nums[nums.length - 1];
	}
	
	// by myself
	public int romanToInt_self(String s) {
        if (s == null || s.length() == 0)
            return 0;
        
        Map<Character, Integer> map = new HashMap<>();
        
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i != s.length() - 1) {
                if (c == 'I' && s.charAt(i + 1) == 'V') {
                    ans += 4;
                    i++;
                }
                else if (c == 'I' && s.charAt(i + 1) == 'X') {
                    ans += 9;
                    i++;
                }
                
                else if (c == 'X' && s.charAt(i + 1) == 'L') {
                    ans += 40;
                    i++;
                }
                else if (c == 'X' && s.charAt(i + 1) == 'C') {
                    ans += 90;
                    i++;
                }
                
                else if (c == 'C' && s.charAt(i + 1) == 'D') {
                    ans += 400;
                    i++;
                }
                else if (c == 'C' && s.charAt(i + 1) == 'M') {
                    ans += 900;
                    i++;
                }
                
                else {
                    ans += map.get(c);
                }
            }
            else {
                ans += map.get(c);
            }
        }
        return ans;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/roman-to-integer/discuss/6537/My-Straightforward-Python-Solution
     * https://leetcode.com/problems/roman-to-integer/discuss/264743/Clean-Python-beats-99.78.
     * https://leetcode.com/problems/roman-to-integer/discuss/6542/4-lines-in-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/roman-to-integer/discuss/6811/My-easy-to-understand-C%2B%2B-solutions
     * https://leetcode.com/problems/roman-to-integer/discuss/6547/Clean-O(n)-c%2B%2B-solution
     * https://leetcode.com/problems/roman-to-integer/discuss/6521/A-36ms-C%2B%2B-Solution-No-Hashmap-Clean-to-understand
     */

}
