package OJ0201_0210;

import java.util.HashMap;

public class Implement_Trie_Prefix_Tree {
	/*
	 * The following 2 classes are from this link.
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58832/AC-JAVA-solution-simple-using-single-array/60221
	 * 
	 * insert: After evaluating the entire String the Node we left off on is marked as 
	 * a word this allows our Trie to know which words exist in our "dictionary"
	 * 
	 * search: After checking each Char in the String I check to see if the Node I 
	 * left off on was marked as a word returning the result.
	 * 
	 * Starts with is identical to search except it doesn't matter if the Node I left 
	 * off was marked as a word or not if entire string evaluated i always return true;
	 * 
	 * Rf :
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58832/AC-JAVA-solution-simple-using-single-array
	 * 
	 * Other code:
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58840/AC-Java-DFS-solution-(beat-91.31-submissions)
	 */
	class TrieNode {
		public boolean isWord;
		public TrieNode[] children = new TrieNode[26];

		public TrieNode() {
		}
	}
	
	class Trie {
		private TrieNode root;

	    /** Initialize your data structure here. */
	    public Trie() {
	    	root = new TrieNode();
	    }
	    
	    /** Inserts a word into the trie. */
		public void insert(String word) {
			TrieNode ws = root;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (ws.children[c - 'a'] == null) {
					ws.children[c - 'a'] = new TrieNode();
				}
				ws = ws.children[c - 'a'];
			}
			ws.isWord = true;
		}
	    
	    /** Returns if the word is in the trie. */
		public boolean search(String word) {
			TrieNode ws = root;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (ws.children[c - 'a'] == null)
					return false;
				ws = ws.children[c - 'a'];
			}
			return ws.isWord;
		}
	    
	    /** Returns if there is any word in the trie that starts with the given prefix. */
		public boolean startsWith(String prefix) {
			TrieNode ws = root;
			for (int i = 0; i < prefix.length(); i++) {
				char c = prefix.charAt(i);
				if (ws.children[c - 'a'] == null)
					return false;
				ws = ws.children[c - 'a'];
			}
			return true;
		}
	}
	
	/*
	 * https://leetcode.com/articles/implement-trie-prefix-tree/
	 * 
	 * + Maximum of R links to its children, where each link corresponds to one of R 
	 *   character values from dataset alphabet.
	 * + Boolean field which specifies whether the node corresponds to the end of the 
	 *   key, or is just a key prefix.
	 * 
	 * We insert a key by searching into the trie. We start from the root and search 
	 * a link, which corresponds to the first key character.
	 * A link does not exist. Then we create a new node and link it with the parent's 
	 * link matching the current key character. We repeat this step until we encounter 
	 * the last character of the key, then we mark the current node as an end node.
	 * 
	 * Search for a key in a trie
	 * A link does not exist. If there are no available key characters and current 
	 * node is marked as isEnd we return true. Otherwise there are possible two cases 
	 * in each of them we return false :
	 * + There are key characters left, but it is impossible to follow the key path in 
	 *   the trie, and the key is missing.
	 * + No key characters left, but current node is not marked as isEnd. Therefore 
	 *   the search key is only a prefix of another key in the trie.
	 * 
	 * Search for a key prefix in a trie
	 * The only difference with search for a key algorithm is that when we come to an 
	 * end of the key prefix, we always return true. We don't need to consider the 
	 * isEnd mark of the current trie node, because we are searching for a prefix of 
	 * a key, not for a whole key.
	 */
	class article {
		class TrieNode {

		    // R links to node children
		    private TrieNode[] links;

		    private final int R = 26;

		    private boolean isEnd;

		    public TrieNode() {
		        links = new TrieNode[R];
		    }

		    public boolean containsKey(char ch) {
		        return links[ch -'a'] != null;
		    }
		    public TrieNode get(char ch) {
		        return links[ch -'a'];
		    }
		    public void put(char ch, TrieNode node) {
		        links[ch -'a'] = node;
		    }
		    public void setEnd() {
		        isEnd = true;
		    }
		    public boolean isEnd() {
		        return isEnd;
		    }
		}
		
		class Trie {
		    private TrieNode root;

		    public Trie() {
		        root = new TrieNode();
		    }

		    // Inserts a word into the trie.
		    public void insert(String word) {
		        TrieNode node = root;
		        for (int i = 0; i < word.length(); i++) {
					char currentChar = word.charAt(i);
					if (!node.containsKey(currentChar)) {
						node.put(currentChar, new TrieNode());
					}
					node = node.get(currentChar);
				}
				node.setEnd();
			}

			// search a prefix or whole key in trie and
			// returns the node where search ends
			private TrieNode searchPrefix(String word) {
				TrieNode node = root;
				for (int i = 0; i < word.length(); i++) {
					char curLetter = word.charAt(i);
					if (node.containsKey(curLetter)) {
						node = node.get(curLetter);
					} 
					else {
						return null;
					}
				}
				return node;
			}

			// Returns if the word is in the trie.
			public boolean search(String word) {
				TrieNode node = searchPrefix(word);
				return node != null && node.isEnd();
			}
			
			// Returns if there is any word in the trie
		    // that starts with the given prefix.
		    public boolean startsWith(String prefix) {
		        TrieNode node = searchPrefix(prefix);
		        return node != null;
		    }
		}
	}
	
	/*
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/343170/JAVA-SOLUTION-ITERATIVE-HASHMAP
	 * 
	 * A Trie is a set of connected TrieNodes. The root node has no letter. 
	 * A Trie that contains the words "food", "fox", and "he" is shown below
	 * 
	 *           ROOT
	 *           /  \
     *          f    h
     *         /      \
     *        o        e
     *       / \
     *      o   x
     *     /
     *    d
     * 
     * One minor trick in creating a Trie is to omit storing any letters in the 
     * TrieNodes. Instead, the connections between TrieNodes will represent the 
     * letters
     * 
     * Time Complexity
     *    + insert(): O(n)
     *    + search(): O(n)
     *    + startsWith(): O(n)
     *    
     * Space Complexity
     *    + insert(): O(n)
     *    + search(): O(1)
     *    + startsWith(): O(1)
     *    
     * Other Applications of Trie
     *    1. Finding all words (or the number of words) with a common prefix.
     *    2. Enumerating a dataset of strings in alphabetical order.
     *    
     * Other code:
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58832/AC-JAVA-solution-simple-using-single-array/141278
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58986/Concise-O(1)-JAVA-solution-based-on-HashMap
	 */
	class Solution_HashMap {
		class TrieNode {
		    private HashMap<Character, TrieNode> children = new HashMap<>();
		    public boolean isEnd = false; // "public" for simplicity

		    public void putChildIfAbsent(char ch) {
		        children.putIfAbsent(ch, new TrieNode());
		    }

		    public TrieNode getChild(char ch) {
		        return children.get(ch);
		    }
		}

		class Trie {
		    TrieNode root = new TrieNode();

		    public void insert(String word) {
		        TrieNode curr = root;
		        for (char ch : word.toCharArray()) {
		            curr.putChildIfAbsent(ch);
		            curr = curr.getChild(ch);
		        }
		        curr.isEnd = true;
		    }

		    public boolean search(String word) {
		        TrieNode curr = root;
		        for (char ch : word.toCharArray()) {
		            curr = curr.getChild(ch);
		            if (curr == null) {
		                return false;
		            }
		        }
		        return curr.isEnd;
		    }

		    public boolean startsWith(String prefix) {
		        TrieNode curr = root;
		        for (char ch : prefix.toCharArray()) {
		            curr = curr.getChild(ch);
		            if (curr == null) {
		                return false;
		            }
		        }
		        return true;
		    }
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58989/My-python-solution
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58834/AC-Python-Solution
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/632380/PYTHON-Faster-than-97.69-TRIE-with-nested-dictionary-Prefix-Tree
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58842/Maybe-the-code-is-not-too-much-by-using-%22next26%22-C%2B%2B
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58945/Share-my-C%2B%2B-solutioneasy-to-understand
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/631328/Easy-To-Understand-C%2B%2B-Solution-With-Images
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58868/Implement-Trie-(Prefix-Tree)-C%2B%2B-Clean-Code-(array-or-map)
     * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58852/C%2B%2B-My-solution-easy-to-understand%3A)
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/58965/Concise-JavaScript-solution
	 * https://leetcode.com/problems/implement-trie-prefix-tree/discuss/399178/Clean-JavaScript-solution
	 */
	
	/**
	 * Your Trie object will be instantiated and called as such:
	 * Trie obj = new Trie();
	 * obj.insert(word);
	 * boolean param_2 = obj.search(word);
	 * boolean param_3 = obj.startsWith(prefix);
	 */

}
