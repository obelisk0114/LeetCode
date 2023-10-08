package OJ0331_0340;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Palindrome_Pairs {
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79210/The-Easy-to-unserstand-JAVA-Solution
	 * 
	 * Case 1: If s1 is a blank string, then for any string that is palindrome s2, 
	 *         s1 + s2 and s2 + s1 are palindrome.
	 * Case 2: If s2 is the reversing string of s1, then s1 + s2 and s2 + s1 are 
	 *         palindrome.
	 * Case 3: If s1[0:cut] is palindrome and there exists s2 is the reversing string 
	 *         of s1[cut + 1:] , then s2 + s1 is palindrome.
	 * Case 4: Similiar to case 3. If s1[cut + 1: ] is palindrome and there exists s2 
	 *         is the reversing string of s1[0:cut] , then s1 + s2 is palindrome.
	 */
	public List<List<Integer>> palindromePairs2(String[] words) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (words == null || words.length == 0) {
			return res;
		}
		
		// build the map save the key-val pairs: String - idx
		HashMap<String, Integer> map = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			map.put(words[i], i);
		}

		// special cases: "" can be combine with any palindrome string
		if (map.containsKey("")) {
			int blankIdx = map.get("");
			for (int i = 0; i < words.length; i++) {
				if (isPalindrome2(words[i]) && i != blankIdx) {
					res.add(Arrays.asList(blankIdx, i));
					res.add(Arrays.asList(i, blankIdx));
				}
			}
		}

		// find all string and reverse string pairs
		for (int i = 0; i < words.length; i++) {
			String reverse = reverseStr(words[i]);
			if (map.containsKey(reverse) && map.get(reverse) != i) {
				res.add(Arrays.asList(i, map.get(reverse)));
			}
		}

		// find the pair s1, s2 that
		// case1 : s1[0:cut] is palindrome and s1[cut + 1:] = reverse(s2) => (s2, s1)
		// case2 : s1[cut + 1:] is palindrome and s1[0:cut] = reverse(s2) => (s1, s2)
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			for (int cut = 1; cut < word.length(); cut++) {
				if (isPalindrome2(word.substring(0, cut))) {
					String cut_r = reverseStr(word.substring(cut));
					if (map.containsKey(cut_r) && map.get(cut_r) != i) {
						res.add(Arrays.asList(map.get(cut_r), i));
					}
				}
				
				if (isPalindrome2(word.substring(cut))) {
					String cut_r = reverseStr(word.substring(0, cut));
					if (map.containsKey(cut_r) && map.get(cut_r) != i) {
						res.add(Arrays.asList(i, map.get(cut_r)));
					}
				}
			}
		}

		return res;
	}

	public String reverseStr(String str) {
		StringBuilder sb = new StringBuilder(str);
		return sb.reverse().toString();
	}

	public boolean isPalindrome2(String s) {
		int i = 0;
		int j = s.length() - 1;
		while (i <= j) {
			if (s.charAt(i) != s.charAt(j)) {
				return false;
			}
			i++;
			j--;
		}
		return true;
	}
	
	/*
	 * The following class and 4 functions are from this link.
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79195/O(n-*-k2)-java-solution-with-Trie-structure
	 * 
	 * ["ba", "a", "aaa"]
	 * 
                          root (-1,[1,2])
                            | 'a'
                          n1 (1,[0,1,2])
                    ---------------------
                'b' |                   | 'a'
                  n2 (0,[0])      n3 (-1,[2])
                                        | 'a'
                                   n4 (2,[2])
	 * 
	 * Case 1: the reverse of s2 is a suffix of s1 and the rest part of s1 is a 
	 *    palindrome (the prefix of s1 excluding the previous suffix is a palindrome)
	 * Case 2: the reverse of s1 is a suffix of s2 and the rest part of s2 is a 
	 *    palindrome (the prefix of s2 excluding the previous suffix is a palindrome)
	 * 
	 * The searching process in the original post deals only with the first case. 
	 * If we don't have the list containing words whose prefix is a palindrome, then 
	 * we're forced to do extra checking for the second case.
	 * 
	 * 1st case : prefix of String a + latter part of a(palindrome) + whole String b 
	 *            = new palindrome.
	 * 2nd case : whole String a + prefix of String b(palindrome) + suffix of String b 
	 *            = new palindrome.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79195/O(n-*-k2)-java-solution-with-Trie-structure/248711
	 * https://leetcode.com/problems/palindrome-pairs/discuss/170996/Java-solution-with-Trie-structure-and-FULL-explanation
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79195/O(n-*-k2)-java-solution-with-Trie-structure/195469
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79195/O(n-*-k2)-java-solution-with-Trie-structure/84095
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79246/Java-Trie-solution
	 */
	private static class TrieNode {
		TrieNode[] next;
		int index;
		
		// list of indexes to words in the words array which has a suffix finishing on
        // this trie node and the rest of the word is palindrome on its own
		List<Integer> list;

		TrieNode() {
			next = new TrieNode[26];
			index = -1;
			list = new ArrayList<>();
		}
	}

	public List<List<Integer>> palindromePairs_trie(String[] words) {
		List<List<Integer>> res = new ArrayList<>();

		TrieNode root = new TrieNode();

		for (int i = 0; i < words.length; i++) {
			addWord_trie(root, words[i], i);
		}

		for (int i = 0; i < words.length; i++) {
			search_trie(words, i, root, res);
		}

		return res;
	}

	private void addWord_trie(TrieNode root, String word, int index) {
		// Build the trie BACKWARDS (during pairing we look for a matching front)
        // and the center part of the pair to be palindrome on its own
		for (int i = word.length() - 1; i >= 0; i--) {
			int j = word.charAt(i) - 'a';

			if (root.next[j] == null) {
				root.next[j] = new TrieNode();
			}

			if (isPalindrome_trie(word, 0, i)) {
				root.list.add(index);
			}

			root = root.next[j];
		}

		root.list.add(index);
		root.index = index;
	}

	private void search_trie(String[] words, int i, TrieNode root, 
			List<List<Integer>> res) {
		for (int j = 0; j < words[i].length(); j++) {
			// full word && avoid pairing words with themselves && 
            // substring remaining from word is palindrome
			if (root.index >= 0 && root.index != i 
					&& isPalindrome_trie(words[i], j, words[i].length() - 1)) {
				res.add(Arrays.asList(i, root.index));
			}

			root = root.next[words[i].charAt(j) - 'a'];
			if (root == null)
				return;
		}

		// processed the whole word, we are at its last character
        // here we add all palindromes to the result list from this trie node
		for (int j : root.list) {
			if (i == j)
				continue;
			
			res.add(Arrays.asList(i, j));
		}
	}

	private boolean isPalindrome_trie(String word, int i, int j) {
		while (i < j) {
			if (word.charAt(i++) != word.charAt(j--))
				return false;
		}

		return true;
	}
	
	/*
	 * The following class, variable and 6 functions are from this link.
	 * https://leetcode.com/problems/palindrome-pairs/discuss/176205/Beats-80-Trie-Java-with-Explanations
	 * 
	 * Case 1. A must be prefix of reversed B, and the rest of reversed B should be 
	 *         palindrome. For example,
	 * (B:oooabc - cbaooo,    A:cba       AB:cba|oooabc)
	 * Case 2. Or, reversed B must be prefix of A, and the rest of A should be 
	 *         palindrome. For example,
	 * (B:abc - cba           A:cbaooo,   AB:cbaooo|abc)
	 * 
	 * Each word in words can be B. We put all reversed words in a trie. 
	 * Each word in words can be A. So we search A in trie, 
	 * 
	 * Case 1. if A in trie, and the branch under the end node is a palindrome
	 * Case 2. if we reach a leaf of trie, and the rest of A is palindrome 
	 * 
	 * For Case 1., we modify TrieNode data structure by adding belowPalindromeWordIds
	 *  - list of word indices such that nodes below can construct a palindrome.
	 * For Case 2., we create a method isPalindrome(str, start, end) .
	 * 
	 * Corner cases of empty string. Both ("", self-palindrome) and 
	 * (self-palindrome, "") are still palindrome.
	 */
	private Node root_trie2;

	public List<List<Integer>> palindromePairs_trie2(String[] words) {
		if (words == null || words.length == 0)
			return new ArrayList<>();

		root_trie2 = new Node();
		int n = words.length;
		List<List<Integer>> finalResult = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			if (words[i].isEmpty()) {
				// Pair with all self-palindrome.
				List<Integer> selfPalindromeWordIndices = 
						getSelfPalindrome_trie2(words);
				for (int pairId : selfPalindromeWordIndices) {
					finalResult.add(new ArrayList<>(Arrays.asList(i, pairId)));
					finalResult.add(new ArrayList<>(Arrays.asList(pairId, i)));
				}
			} 
			else {
				insert_trie2(reverse_trie2(words[i]), i);
			}
		}
		
		for (int i = 0; i < n; i++) {
			List<Integer> wordIndices = search_trie2(words[i], i);
			for (int pairId : wordIndices) {
				finalResult.add(new ArrayList<>(Arrays.asList(i, pairId)));
			}
		}

		return finalResult;
	}

	/****************** Trie-related *******************/

	private List<Integer> search_trie2(String word, int index) {
		List<Integer> wordIndices = new ArrayList<>();
		Node ptr = root_trie2;
		int n = word.length();
		
		for (int i = 0; i < n; i++) {
			int label = word.charAt(i) - 'a';
			if (ptr.endWordId > -1 && isPalindrome_trie2(word, i, n - 1)) {
				wordIndices.add(ptr.endWordId);
			}
			
			if (ptr.children[label] == null) {
				return wordIndices;
			}
			ptr = ptr.children[label];
		}
		
		if (ptr.endWordId > -1 && ptr.endWordId != index)
			wordIndices.add(ptr.endWordId);
		if (!ptr.belowPalindromeWordIds.isEmpty())
			wordIndices.addAll(ptr.belowPalindromeWordIds);

		return wordIndices;
	}

	private void insert_trie2(String word, int index) {
		Node ptr = root_trie2;
		int n = word.length();
		for (int i = 0; i < n; i++) {
			int label = word.charAt(i) - 'a';
			if (ptr.children[label] == null)
				ptr.children[label] = new Node();
			
			ptr = ptr.children[label];
			
			if (isPalindrome_trie2(word, i + 1, n - 1))
				ptr.belowPalindromeWordIds.add(index);
		}
		ptr.endWordId = index;
	}

	class Node {
		Node[] children;
		// -1 in default. If it is a word's end, it is the index of it.
		int endWordId;
		
		// List of word indices such that nodes below can construct a palindrome.
		List<Integer> belowPalindromeWordIds;

		public Node() {
			children = new Node[26];
			endWordId = -1;
			belowPalindromeWordIds = new ArrayList<>();
		}
	}
    
    /****************** Utility *******************/
    
	private String reverse_trie2(String str) {
		return new StringBuilder(str).reverse().toString();
	}

	private boolean isPalindrome_trie2(String str, int start, int end) {
		if (start > end) {
			return false;
		}

		while (start < end) {
			if (str.charAt(start) != str.charAt(end))
				return false;
			start++;
			end--;
		}

		return true;
	}

	private List<Integer> getSelfPalindrome_trie2(String[] words) {
		List<Integer> wordIndices = new ArrayList<>();
		for (int i = 0; i < words.length; i++) {
			if (isPalindrome_trie2(words[i], 0, words[i].length() - 1)) {
				wordIndices.add(i);
			}
		}
		return wordIndices;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79199/150-ms-45-lines-JAVA-solution
	 * 
	 * For each word in the array, split into two parts str1 and str2. Check whether 
	 * str1 and str2 is palindrome
	 * If str1 is palindrome, we can use str1 as middle part, str2 as right part, and 
	 * find if map contains reversed str2.
	 * If contains, then we can use that string as left part, combine with middle 
	 * part, right part, it will form a correct palindrome string. 
	 * 
	 * 比如對於 "abacd", 左側 "aba" 是回文，還剩下 "cd", 所以想要和另一個字符串組成一個長的回文串，只需要 
	 * Map 中有 "dc" 即可加到原字符串的左邊: "dc", "abacd" => "dcabacd"
	 * 對於右半部分 substring 的分析同理
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindrome-pairs/discuss/279554/O(n-*-k2)-Java-Solution-Beats-67
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79199/150-ms-45-lines-JAVA-solution/242203
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79199/150-ms-45-lines-JAVA-solution/84125
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindrome-pairs/discuss/202266/Simple-Java-Solution-using-HashMap
	 */
	public List<List<Integer>> palindromePairs3(String[] words) {
		List<List<Integer>> ret = new ArrayList<>();
		if (words == null || words.length < 2)
			return ret;
		
		// store every word with its index
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < words.length; i++)
			map.put(words[i], i);
		
		for (int i = 0; i < words.length; i++) {
			
			// notice it should be "j <= words[i].length()"
			// <= is aimed to handle empty string in the input. test case ["a", ""]
			for (int j = 0; j <= words[i].length(); j++) {
				String str1 = words[i].substring(0, j);
				String str2 = words[i].substring(j);
				
				// 左側為回文，需要找到右側的逆序在 map 中
				if (isPalindrome3(str1)) {
					String str2rvs = new StringBuilder(str2).reverse().toString();
					
					if (map.containsKey(str2rvs) && map.get(str2rvs) != i) {
						List<Integer> list = new ArrayList<Integer>();
						list.add(map.get(str2rvs));
						list.add(i);
						ret.add(list);
					}
				}
				if (isPalindrome3(str2)) {
					String str1rvs = new StringBuilder(str1).reverse().toString();
					
					// check "str2.length() != 0" to avoid duplicates
					if (map.containsKey(str1rvs) && map.get(str1rvs) != i 
							&& str2.length() != 0) {
						List<Integer> list = new ArrayList<Integer>();
						list.add(i);
						list.add(map.get(str1rvs));
						ret.add(list);
					}
				}
			}
		}
		return ret;
	}

	private boolean isPalindrome3(String str) {
		int left = 0;
		int right = str.length() - 1;
		while (left <= right) {
			if (str.charAt(left++) != str.charAt(right--))
				return false;
		}
		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79217/Accepted-short-Java-solution-using-HashMap
	 * 
	 * Use 2 pointers l and r to iterate all possible substrings of reversed String. 
	 * Note that either l == 0 or r == s.length() because otherwise the substring will 
	 * definitely fail to form a palindrome with W.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindrome-pairs/discuss/79217/Accepted-short-Java-solution-using-HashMap/249293
	 */
	public List<List<Integer>> palindromePairs_2_pointer(String[] words) {
		List<List<Integer>> pairs = new LinkedList<>();
		if (words == null)
			return pairs;
		
		HashMap<String, Integer> map = new HashMap<>();
		for (int i = 0; i < words.length; ++i)
			map.put(words[i], i);
		
		for (int i = 0; i < words.length; ++i) {
			int l = 0, r = 0;
			while (l <= r) {
				String s = words[i].substring(l, r);
				Integer j = map.get(new StringBuilder(s).reverse().toString());
				if (j != null && i != j 
						&& isPalindrome_2_pointer(words[i].substring(l == 0 ? r : 0, 
								l == 0 ? words[i].length() : l)))
					pairs.add(Arrays.asList(l == 0 ? 
							new Integer[] { i, j } : new Integer[] { j, i }));
				
				if (r < words[i].length())
					++r;
				else
					++l;
			}
		}
		return pairs;
	}

	private boolean isPalindrome_2_pointer(String s) {
		for (int i = 0; i < s.length() / 2; ++i)
			if (s.charAt(i) != s.charAt(s.length() - 1 - i))
				return false;
		return true;
	}
	
	// The following 2 functions are by myself.
	public List<List<Integer>> palindromePairs_self(String[] words) {
        List<List<Integer>> ans = new ArrayList<>();
        if (words == null || words.length == 0)
            return ans;
        
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (i != j) {
                    //String s = words[i] + words[j];
                    if (check_self(words[i], words[j])) {
                        List<Integer> list = new ArrayList<>();
                        list.add(i);
                        list.add(j);
                        ans.add(list);
                    }
                }
            }
        }
        return ans;
    }
    
    private boolean check_self(String s1, String s2) {
        int i = 0;
        int j = s1.length() + s2.length() - 1;
        while (i < j) {
            char c1, c2;
            if (i < s1.length()) {
                c1 = s1.charAt(i);
            }
            else {
                c1 = s2.charAt(i - s1.length());
            }
            if (j >= s1.length()) {
                c2 = s2.charAt(j - s1.length());
            }
            else {
                c2 = s1.charAt(j);
            }
            
            if (c1 != c2)
                return false;
            
            i++;
            j--;
        }
        return true;
    }
	
	// The following 2 functions are by myself.
	public List<List<Integer>> palindromePairs_self2(String[] words) {
        if (words == null || words.length < 2)
            return new ArrayList<>();
        
        List<List<Integer>> ans = new ArrayList<>();
        
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (i == j)
                    continue;
                if (words[i].length() == 0) {
                    if (check_self2(words[j])) {
                        List<Integer> list = new ArrayList<>();
                        list.add(i);
                        list.add(j);
                        ans.add(list);
                    }
                    continue;
                }
                if (words[j].length() == 0) {
                    if (check_self2(words[i])) {
                        List<Integer> list = new ArrayList<>();
                        list.add(i);
                        list.add(j);
                        ans.add(list);
                    }
                    continue;
                }
                
                if (words[i].charAt(0) == words[j].charAt(words[j].length() - 1)) {
                    String s = words[i] + words[j];
                    if (check_self2(s)) {
                        List<Integer> list = new ArrayList<>();
                        list.add(i);
                        list.add(j);
                        ans.add(list);
                    }
                }
            }
        }
        return ans;
    }
    
    private boolean check_self2(String s) {
    	// return s.equals((new StringBuilder(s)).reverse().toString());
    	for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
    
    // https://leetcode.com/problems/palindrome-pairs/discuss/79254/Java-naive-154-ms-O(nk2-%2B-r)-and-126-ms-O(nk-%2B-r)-Manacher-%2B-suffixesprefixes
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/palindrome-pairs/discuss/79209/Accepted-Python-Solution-With-Explanation
     * https://leetcode.com/problems/palindrome-pairs/discuss/148357/17-line-python-solution-Trie-and-non-Trie-explained-with-diagrams
     * https://leetcode.com/problems/palindrome-pairs/discuss/79219/Python-solution~
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/palindrome-pairs/discuss/79215/Easy-to-understand-AC-C%2B%2B-solution-O(n*k2)-using-map
     * https://leetcode.com/problems/palindrome-pairs/discuss/79202/Clean-C%2B%2B-implementation
     */

}
