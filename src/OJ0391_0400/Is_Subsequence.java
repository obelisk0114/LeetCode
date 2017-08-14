package OJ0391_0400;

public class Is_Subsequence {
	/*
	 * https://discuss.leetcode.com/topic/57205/java-only-2ms-much-faster-than-normal-2-pointers
	 * 
	 * "indexOf" and "charAt" solution both traversed the char of String one by one 
	 * to search the first occurrence specific char.
	 * The difference is that indexOf only call once function then traversed in 
	 * "String.value[]" arr, but we used multiple calling function "charAt" to 
	 * get the value in "String.value[]" arr.
	 */
	public boolean isSubsequence(String s, String t) {
		if (t.length() < s.length())
			return false;
		int prev = 0;
		for (int i = 0; i < s.length(); i++) {
			char tempChar = s.charAt(i);
			prev = t.indexOf(tempChar, prev);
			if (prev == -1)
				return false;
			prev++;
		}
		return true;
	}
	
	// https://discuss.leetcode.com/topic/57310/do-we-really-need-dp-bs
	public boolean isSubsequence_improve(String s, String t) {
		//if (t.length() == 0 && s.length() == 0)
			//return true;
		//if (t.length() == 0)
			//return false;
		if (s.length() == 0)
			return true;

		int target_index = 0;
		for (int i = 0; i < t.length(); i++) {
			if (s.charAt(target_index) == t.charAt(i)) {
				if (target_index == s.length() - 1)
					return true;
				target_index++;
			}
		}
		return false;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/57167/java-7-lines-solution-38ms
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/57147/straight-forward-java-simple-solution
	 * https://discuss.leetcode.com/topic/57304/37ms-simple-java-solution
	 */
	public boolean isSubsequence_two_pointer(String s, String t) {
        int p1 = 0, p2 = 0;
        while(p1 < s.length() && p2 < t.length()){
            if(s.charAt(p1) == t.charAt(p2))
                p1++;
            p2++;
        }
        return p1 == s.length();
    }
	
	// https://discuss.leetcode.com/topic/57240/java-easy-understanding-recursion-solution-38ms
	public boolean isSubsequence_recursive(String s, String t) {
		if (s == null || s.length() == 0)
			return true;
		for (int i = 0; i < t.length(); i += 1) {
			if (t.charAt(i) == s.charAt(0))
				return isSubsequence_recursive(s.substring(1), t.substring(i + 1));
		}
		return false;
	}
	
	/*
	 * follow up
	 * https://discuss.leetcode.com/topic/58367/binary-search-solution-for-follow-up-with-detailed-comments
	 * https://discuss.leetcode.com/topic/73897/hashmap-binary-search-solution-for-the-follow-up-question
	 */
	
	// self
	public boolean isSubsequence_self(String s, String t) {
        int pos = 0;
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            for (int j = pos; j < t.length(); j++) {
                if (t.charAt(j) == c) {
                    count++;
                    pos = j + 1;
                    break;
                }
            }
        }
        if (count == s.length())
            return true;
        else
            return false;
    }

}
