package OJ0641_0650;

//import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/String.html#6
 */

public class Replace_Words {
	/*
	 * The following 4 functions and class are from this link.
	 * https://discuss.leetcode.com/topic/97090/java-simple-classical-trie-question-solution-beat-96
	 * 
	 * Rf : https://discuss.leetcode.com/topic/96809/simple-java-8-and-trie-based-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/96860/share-java-solution-using-trietree
	 */
	public String replaceWords(List<String> dict, String sentence) {
        String[] tokens = sentence.split(" ");
        TrieNode trie = buildTrie(dict);
        return replaceWords(tokens, trie);
    }

    private String replaceWords(String[] tokens, TrieNode root) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String token : tokens) {
            stringBuilder.append(getShortestReplacement(token, root));
            stringBuilder.append(" ");
        }
        return stringBuilder.substring(0, stringBuilder.length()-1);
    }

    private String getShortestReplacement(String token, final TrieNode root) {
        TrieNode temp = root;
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : token.toCharArray()) {
            stringBuilder.append(c);
            if (temp.children[c - 'a'] != null) {
                if (temp.children[c - 'a'].isWord) {
                    return stringBuilder.toString();
                }
                temp = temp.children[c - 'a'];
            } else {
                return token;
            }
        }
        return token;
    }

    private TrieNode buildTrie(List<String> dict) {
        TrieNode root = new TrieNode(' ');
        for (String word : dict) {
            TrieNode temp = root;
            for (char c : word.toCharArray()) {
                if (temp.children[c - 'a'] == null) {
                    temp.children[c - 'a'] = new TrieNode(c);
                }
                temp = temp.children[c - 'a'];
            }
            temp.isWord = true;
        }
        return root;
    }

    public class TrieNode {
        char val;
        TrieNode[] children;
        boolean isWord;

        public TrieNode(char val) {
            this.val = val;
            this.children = new TrieNode[26];
            this.isWord = false;
        }
    }
	
	// https://discuss.leetcode.com/topic/96848/java-solution-using-trie-data-structure
	
	// https://discuss.leetcode.com/topic/96813/java-solution-12-lines-hashset
	public String replaceWords_set(List<String> dict, String sentence) {
		if (dict == null || dict.size() == 0)
			return sentence;

		Set<String> set = new HashSet<>();
		for (String s : dict)
			set.add(s);

		StringBuilder sb = new StringBuilder();
		String[] words = sentence.split("\\s+");

		for (String word : words) {
			String prefix = "";
			for (int i = 1; i <= word.length(); i++) {
				prefix = word.substring(0, i);
				if (set.contains(prefix))
					break;
			}
			sb.append(" " + prefix);
		}

		return sb.deleteCharAt(0).toString();
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/96876/greedy-java-solution-99ms
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/96897/simple-java-sort-solution
	 */
	public String replaceWords_startsWith(List<String> dict, String sentence) {
		String[] dicts = new String[dict.size()];
		dict.toArray(dicts);

		//Arrays.sort(dicts, (a, b) -> a.length() - b.length());

		StringBuilder newSentence = new StringBuilder();
		String[] words = sentence.split(" ");
		newSentence.append(this.transform(dicts, words[0]));
		for (int i = 1; i < words.length; i++) {
			newSentence.append(" " + this.transform(dicts, words[i]));
		}
		return newSentence.toString();
	}
	private String transform(String[] dicts, String word) {
		for (String root : dicts) {
			if (word.startsWith(root)) {
				return root;
			}
		}
		return word;
	}
	
	// https://discuss.leetcode.com/topic/96815/simple-java-solution-with-comments

}
