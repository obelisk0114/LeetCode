package OJ0021_0030;

/*
 * KMP
 * https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
 * http://www.geeksforgeeks.org/searching-for-patterns-set-2-kmp-algorithm/
 */

public class Implement_strStr {
	/*
	 * https://discuss.leetcode.com/topic/3576/accepted-kmp-solution-in-java-for-reference/7
	 * 
	 * KMP code from CLRS
	 * 
	 * Rf : https://discuss.leetcode.com/topic/30471/java-and-python-solution-using-kmp-with-o-m-n-time-complexity
	 */
	public int strStr(String haystack, String needle) {
		if (needle.length() == 0)
			return 0;
		if (needle.length() > haystack.length() || haystack.length() == 0)
			return -1;
		char[] ndl = needle.toCharArray();
		char[] hay = haystack.toCharArray();
		int[] pai = new int[ndl.length];
		pai[0] = -1;
		int k = -1;
		for (int i = 1; i < ndl.length; i++) {
			while (k > -1 && ndl[k + 1] != ndl[i]) {
				k = pai[k];
			}
			if (ndl[k + 1] == ndl[i]) {
				k++;
			}
			pai[i] = k;
		}
		k = -1;         // numbers of characters matched
		for (int i = 0; i < hay.length; i++) {  // scan the text from left to right
			while (k > -1 && ndl[k + 1] != hay[i]) { // next character does not match
				k = pai[k];            // look for the next match
			}
			if (ndl[k + 1] == hay[i]) {       // next character matches
				k++;
				if (k == ndl.length - 1) {    // is all of P matched?
					return i - k;
				}
			}
		}
		return -1;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/67730/o-m-n-and-o-mn-solutions
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/10816/kmp-solution-in-java
	 */
	public int strStr2(String haystack, String needle) {
		if (haystack == null || needle == null || needle.length() > haystack.length())
			return -1;

		int[] parr = kmpPreprocess(needle);
		int i = 0, j = 0;
		while (i < haystack.length() && j < needle.length()) {
			if (haystack.charAt(i) == needle.charAt(j)) {
				i++;
				j++;
			} else if (j > 0) {
				j = parr[j - 1];
			} else {
				i++;
			}
		}
		return j == needle.length() ? i - j : -1;
	}
	private int[] kmpPreprocess(String pattern) {
		int i = 1, j = 0;
		int[] res = new int[pattern.length()];
		while (i < pattern.length()) {
			if (pattern.charAt(i) == pattern.charAt(j)) {
				res[i] = j + 1;
				i++;
				j++;
			} else if (j > 0) {
				j = res[j - 1];
			} else {
				res[i] = 0;
				i++;
			}
		}
		return res;
	}
	
	// myself
	public int strStr_self(String haystack, String needle) {
		if (needle.length() == 0)
            return 0;
        if (haystack.length() == 0)
            return -1;
        
        int trace = 0;
        int length = haystack.length() - needle.length() + 1;
        for (int i = 0; i < length; i++) {
            System.out.println("i = " + i);
            if (haystack.charAt(i) == needle.charAt(0)) {
                int compare = i;
                trace = i;
                for (int j = 0; j < needle.length(); j++) {
                    if (trace == i && haystack.charAt(compare) == needle.charAt(0))
                        trace = compare;
                    if (haystack.charAt(compare) == needle.charAt(j)) {
                        compare++;
                        if (j == needle.length() - 1) 
                            return i;
                    }
                    else {
                        if (trace != i) {
                            i = trace - 1;
                        }
                        break;
                    }
                }
            }
        }
        return -1;
    }
	
	// https://discuss.leetcode.com/topic/9872/share-my-accepted-java-solution
	public int strStr_Rabin_Karp(String haystack, String needle) {
        int l1 = haystack.length(), l2 = needle.length();
        if (l1 < l2) {
            return -1;
        } else if (l2 == 0) {
            return 0;
        }
        int threshold = l1 - l2;
        for (int i = 0; i <= threshold; ++i) {
            if (haystack.substring(i,i+l2).equals(needle)) {
                return i;
            }
        }
        return -1;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/18839/elegant-java-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/41463/java-easy-to-understand-solutions
	 */
	public int strStr_brute_force(String s, String t) {
        if (t.isEmpty()) return 0; // edge case: "",""=>0  "a",""=>0
        for (int i = 0; i <= s.length() - t.length(); i++) {
            for (int j = 0; j < t.length() && s.charAt(i + j) == t.charAt(j); j++)
                if (j == t.length() - 1) return i;
        }
        return -1;
    }
	
	public static void main(String[] args) {
		Implement_strStr implestr = new Implement_strStr();
		
		//String needle = "mississippi";
		//String haystack = "pi";
		//String needle = "mississippi";
		//String haystack = "issip";
		//String needle = "mississippi";
		//String haystack = "sippj";
		String haystack = "aabaabbbaabbbbabaaab";
		String needle = "abaa";
		System.out.println("Answer : " + haystack.indexOf(needle));
		System.out.println(implestr.strStr(haystack, needle));
	}

}
