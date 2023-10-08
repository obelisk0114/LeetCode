package OJ0211_0220;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Word_Search_II {
	/*
	 * The following 3 functions and class are from this link.
	 * https://leetcode.com/problems/word-search-ii/discuss/59780/java-15ms-easiest-solution-10000
	 * 
	 * Intuitively, start from every cell and try to build a word in the dictionary. 
	 * Backtracking (dfs) is the powerful way to exhaust every possible ways. 
	 * Apparently, we need to do pruning when current character is not in any word.
	 * 
	 * How do we instantly know the current character is invalid? HashMap?
	 * How do we instantly know what's the next valid character? LinkedList?
	 * But the next character can be chosen from a list of characters. "Mutil-LinkedList"?
	 * 
	 * Combing them, Trie is the natural choice.
	 * 
	 * Rf : https://leetcode.com/problems/word-search-ii/discuss/59914/java-solution-with-trie-structure
	 */
	public List<String> findWords(char[][] board, String[] words) {
		List<String> res = new ArrayList<>();
		TrieNode root = buildTrie(words);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				dfs(board, i, j, root, res);
			}
		}
		return res;
	}

	public void dfs(char[][] board, int i, int j, TrieNode p, List<String> res) {
		char c = board[i][j];
		if (c == '#' || p.next[c - 'a'] == null)
			return;
		p = p.next[c - 'a'];
		if (p.word != null) { // found one
			res.add(p.word);
			p.word = null;    // de-duplicate
		}

		board[i][j] = '#';
		if (i > 0)
			dfs(board, i - 1, j, p, res);
		if (j > 0)
			dfs(board, i, j - 1, p, res);
		if (i < board.length - 1)
			dfs(board, i + 1, j, p, res);
		if (j < board[0].length - 1)
			dfs(board, i, j + 1, p, res);
		board[i][j] = c;
	}

	public TrieNode buildTrie(String[] words) {
		TrieNode root = new TrieNode();
		for (String w : words) {
			TrieNode p = root;
			for (char c : w.toCharArray()) {
				int i = c - 'a';
				if (p.next[i] == null)
					p.next[i] = new TrieNode();
				p = p.next[i];
			}
			p.word = w;
		}
		return root;
	}

	class TrieNode {
		TrieNode[] next = new TrieNode[26];
		String word;
	}
	
	/*
	 * The following variable, 2 functions and 2 classes are from this link.
	 * https://leetcode.com/problems/word-search-ii/discuss/59784/my-simple-and-clean-java-code-using-dfs-and-trie
	 * 
	 * Compared with Word Search, I make my DFS with a tire but a word. 
	 * The Trie is formed by all the words in given words. 
	 * During the DFS, for each current formed word, I check if it is in the Trie.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-search-ii/discuss/59881/my-java-solution-using-trie
	 */
	Set<String> res = new HashSet<String>();

	public List<String> findWords2(char[][] board, String[] words) {
		Trie2 trie = new Trie2();
		for (String word : words) {
			trie.insert(word);
		}

		int m = board.length;
		int n = board[0].length;
		boolean[][] visited = new boolean[m][n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				dfs2(board, visited, "", i, j, trie);
			}
		}

		return new ArrayList<String>(res);
	}

	public void dfs2(char[][] board, boolean[][] visited, String str, int x, int y, Trie2 trie) {
		if (x < 0 || x >= board.length || y < 0 || y >= board[0].length)
			return;
		if (visited[x][y])
			return;

		str += board[x][y];
		if (!trie.startsWith(str))
			return;

		if (trie.search(str)) {
			res.add(str);
		}

		visited[x][y] = true;
		dfs2(board, visited, str, x - 1, y, trie);
		dfs2(board, visited, str, x + 1, y, trie);
		dfs2(board, visited, str, x, y - 1, trie);
		dfs2(board, visited, str, x, y + 1, trie);
		visited[x][y] = false;
	}

	class TrieNode2 {
		public TrieNode2[] children = new TrieNode2[26];
		public String item = "";

		// Initialize your data structure here.
		public TrieNode2() {
		}
	}

	class Trie2 {
		private TrieNode2 root;

		public Trie2() {
			root = new TrieNode2();
		}

		// Inserts a word into the trie.
		public void insert(String word) {
			TrieNode2 node = root;
			for (char c : word.toCharArray()) {
				if (node.children[c - 'a'] == null) {
					node.children[c - 'a'] = new TrieNode2();
				}
				node = node.children[c - 'a'];
			}
			node.item = word;
		}

		// Returns if the word is in the trie.
		public boolean search(String word) {
			TrieNode2 node = root;
			for (char c : word.toCharArray()) {
				if (node.children[c - 'a'] == null)
					return false;
				node = node.children[c - 'a'];
			}
			return node.item.equals(word);
		}

		// Returns if there is any word in the trie that starts with the given prefix.
		public boolean startsWith(String prefix) {
			TrieNode2 node = root;
			for (char c : prefix.toCharArray()) {
				if (node.children[c - 'a'] == null)
					return false;
				node = node.children[c - 'a'];
			}
			return true;
		}
	}
	
	// https://leetcode.com/problems/word-search-ii/discuss/59906/java-solution-with-trie
	
	// https://leetcode.com/problems/word-search-ii/discuss/59877/java-simple-solution-triedfs

}
