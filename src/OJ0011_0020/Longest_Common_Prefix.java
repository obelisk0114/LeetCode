package OJ0011_0020;

// https://leetcode.com/problems/longest-common-prefix/discuss/7156/Optimal-Solution-()

public class Longest_Common_Prefix {
	// https://leetcode.com/articles/longest-common-prefix/
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0)
			return "";
		
		for (int i = 0; i < strs[0].length(); i++) {
			char c = strs[0].charAt(i);
			for (int j = 1; j < strs.length; j++) {
				if (i == strs[j].length() || strs[j].charAt(i) != c)
					return strs[0].substring(0, i);
			}
		}
		return strs[0];
	}
	
	// https://leetcode.com/problems/longest-common-prefix/discuss/7004/My-2ms-Java-solution-may-help-u
	public String longestCommonPrefix_min(String[] strs) {
		int len = strs.length;
		if (strs == null || len == 0 || strs[0].length() == 0)
			return "";
		
		int minlen = 0x7fffffff;
		for (int i = 0; i < len; ++i)
			minlen = Math.min(minlen, strs[i].length());
		
		for (int j = 0; j < minlen; ++j)
			for (int i = 1; i < len; ++i)
				if (strs[0].charAt(j) != strs[i].charAt(j))
					return strs[0].substring(0, j);
		
		return strs[0].substring(0, minlen);
	}
	
	/*
	 * by myself
	 * 
	 * Rf : https://leetcode.com/problems/longest-common-prefix/discuss/6946/Fast-and-simple-Java-code-231ms
	 */
	public String longestCommonPrefix_self(String[] strs) {
		if (strs == null || strs.length == 0 || strs[0].length() == 0)
            return "";
        
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        while (true) {
            char c = strs[0].charAt(counter);
            for (int i = 1; i < strs.length; i++) {
                if (counter == strs[i].length())
                    return strs[i];
                
                if (strs[i].charAt(counter) != c) {
                    return sb.toString();
                }
            }
            
            sb.append(c);
            
            counter++;
            if (counter == strs[0].length())
                return strs[0];
        }
    }
	
	/*
	 * https://leetcode.com/problems/longest-common-prefix/discuss/7167/My-6-lines-Java-solution-90
	 * 
	 * Rf : https://leetcode.com/problems/longest-common-prefix/discuss/6910/Java-code-with-13-lines
	 */
	public String longestCommonPrefix_back_delete(String[] strs) {
		int n = strs.length;
		if (n == 0)
			return "";
		
		StringBuilder st = new StringBuilder(strs[0]);
		for (int i = 1; i < n; i++) {
			while (!strs[i].startsWith(st.toString()))
				st.deleteCharAt(st.length() - 1);
		}
		return st.toString();
	}
	
	/*
	 * leetcode.com/problems/longest-common-prefix/discuss/6940/Java-We-Love-Clear-Code!/150722
	 * 
	 * Rf :
	 * leetcode.com/problems/longest-common-prefix/discuss/6924/Sorted-the-array-Java-solution-2-ms/8160
	 */
	public String longestCommonPrefix_compareTo(String[] strs) {
		if (strs == null)
			return null;
		if (strs.length == 0)
			return "";

		String first = strs[0], last = strs[0];

		for (String str : strs) {
			if (str.compareTo(first) < 0)
				first = str;
			if (str.compareTo(last) > 0)
				last = str;
		}

		int i = 0, len = Math.min(first.length(), last.length());

		while (i < len && first.charAt(i) == last.charAt(i))
			i++;

		return first.substring(0, i);
	}

}
