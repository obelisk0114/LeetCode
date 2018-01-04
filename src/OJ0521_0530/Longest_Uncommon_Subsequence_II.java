package OJ0521_0530;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Longest_Uncommon_Subsequence_II {
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/85136/java-15ms-sort-check-subsequence
	 * 
	 * Sort the strings in the reverse order. 
	 * 1. If there is not duplicates in the array, the longest string is the answer.
	 * 2. But if there are duplicates, and if the longest string is not the answer, 
	 * we need to check other strings. But the smaller strings can be subsequence of 
	 * the bigger strings. For this reason, we need to check if the string is a 
	 * subsequence of all the strings bigger than itself. 
	 * If it's not, that is the answer.
	 */
	public int findLUSlength(String[] strs) {
		Arrays.sort(strs, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o2.length() - o1.length();
			}
		});

		Set<String> duplicates = getDuplicates(strs);
		for (int i = 0; i < strs.length; i++) {
			if (!duplicates.contains(strs[i])) {
				int j = 0;
				for (; j < i; j++) {
					if (isSubsequence(strs[j], strs[i]))
						break;
				}
				if (j == i)
					return strs[i].length();
			}
		}
		return -1;
	}
	public boolean isSubsequence(String a, String b) {
		int i = 0, j = 0;
		while (i < a.length() && j < b.length()) {
			if (a.charAt(i) == b.charAt(j))
				j++;
			i++;
		}
		return j == b.length();
	}
	private Set<String> getDuplicates(String[] strs) {
		Set<String> set = new HashSet<String>();
		Set<String> duplicates = new HashSet<String>();
		for (String s : strs) {
			if (set.contains(s))
				duplicates.add(s);
			set.add(s);
		}
		return duplicates;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/105187/java-10ms-beat-90-without-using-hash
	 * 
	 * 1. the LUS must not duplicate in the giving strings, otherwise, it's the 
	 * subsequence of itself.
	 * 2. if length of String A larger than the length of String B then A is 
	 * not a subsequence of B.
	 * 
	 * Base on these two observations, the solution is:
	 * 1. Sort the string array, based on the length of the string.
	 * 2. go thought the array start from the longest to the shortest, 
	 * if it's unique and it's not a subsequence of the strings with the length 
	 * larger than it. then we found the LUS, just return it's length.
	 * 
	 * If the sort is based on both length(first) and alphabet(second) order, then 
	 * the duplicated ones are neighbors in the sorted array. So, you can simply 
	 * check if array[i] == array[i + 1] to determine if the string is duplicate.
	 */
	public int findLUSlength_without_hash(String[] strs) {
		Arrays.sort(strs, new Comparator<String>() {
			public int compare(String a, String b) {
				return a.length() != b.length() ? 
						b.length() - a.length() : b.compareTo(a);
			}
		});

		for (int i = 0; i < strs.length; i++) {
			if (i == strs.length - 1 || !strs[i].equals(strs[i + 1])) {
				int j = i - 1;
				for (; j >= 0; j--)
					if (isSubSequence_without_hash(strs[i], strs[j]))
						break;
				if (j == -1)
					return strs[i].length();
			}
		}

		return -1;
	}
	private boolean isSubSequence_without_hash(String a, String b) {
		if (a.equals(b))
			return true;
		int p = 0;
		for (int i = 0; i < b.length() && p < a.length(); i++)
			if (b.charAt(i) == a.charAt(p))
				p++;
		return p == a.length();
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/100402/java-easy-solution-sort
	 */
	public int findLUSlength_without_hash2(String[] strs) {
		Arrays.sort(strs, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.length() - s1.length();
			}
		});

		for (int i = 0; i < strs.length; i++) {
			boolean flag = false;
			for (int j = 0; j < strs.length && strs[j].length() >= strs[i].length(); j++) {
				if (j == i)
					continue;
				if (helper(strs[j], strs[i])) {
					flag = true;
					break;
				}
			}
			if (!flag)
				return strs[i].length();
		}

		return -1;
	}
	public boolean helper(String s1, String s2) {  // true if s2 is a subsequence of s1
		int index = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) == s2.charAt(index))
				index++;
			if (index == s2.length())
				return true;
		}
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/96466/java-solution
	 */
	public int findLUSlength_return_long(String[] strs) {
		Arrays.sort(strs, (s1, s2) -> s2.length() - s1.length());

		for (int i = 0; i < strs.length; i++) {
			boolean found = false;
			for (int j = 0; j < strs.length; j++) {
				if (i != j && isSubsequence_return_long(strs[j], strs[i])) {
					found = true;
					break;
				}
			}
			if (!found) {
				return strs[i].length();
			}
		}

		return -1;
	}

    /**
     *
     * @return true if str2 is a subsequence of str1, false otherwise.
     */
    private boolean isSubsequence_return_long(String str1, String str2) {
        if (str1.equals(str2)) {
            return true;
        }

        if (str1.length() < str2.length()) {
            return false;
        }

        int i = 0;
        for (char ch : str1.toCharArray()) {
            if (i < str2.length() && str2.charAt(i) == ch) {
                i++;
            }
        }
        return i == str2.length();
    }
    
    /*
     * The following 2 functions are from this link.
     * https://discuss.leetcode.com/topic/85027/java-hashing-solution
     * 
     * We simply maintain a map of all subsequence frequencies and get the 
     * subsequence with frequency 1 that has longest length.
     * 
     * NOTE: This solution does not take advantage of the fact that the optimal 
     * length subsequence (if it exists) is always going to be the length of some 
     * string in the array. Thus, the time complexity of this solution is non-optimal.
     */
	public int findLUSlength_HashMap(String[] strs) {
		Map<String, Integer> subseqFreq = new HashMap<>();
		
		for (String s : strs)
			for (String subSeq : getSubseqs_HashMap(s))
				subseqFreq.put(subSeq, subseqFreq.getOrDefault(subSeq, 0) + 1);
		
		int longest = -1;
		for (Map.Entry<String, Integer> entry : subseqFreq.entrySet())
			if (entry.getValue() == 1)
				longest = Math.max(longest, entry.getKey().length());
		
		return longest;
	}
	public Set<String> getSubseqs_HashMap(String s) {
		Set<String> res = new HashSet<>();
		if (s.length() == 0) {
			res.add("");
			return res;
		}
		
		Set<String> subRes = getSubseqs_HashMap(s.substring(1));
		res.addAll(subRes);
		
		for (String seq : subRes)
			res.add(s.charAt(0) + seq);
		
		return res;
	}
	
	// https://discuss.leetcode.com/topic/91054/simple-java-solution-by-stream-api

}
