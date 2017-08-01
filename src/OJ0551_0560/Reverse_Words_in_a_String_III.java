package OJ0551_0560;

import java.util.ArrayList;

public class Reverse_Words_in_a_String_III {
	/*
	 * Approach #1 Simple Solution
	 * https://leetcode.com/articles/reverse-words-in-a-string/
	 */
	public String reverseWords(String s) {
		String words[] = s.split(" ");
		StringBuilder res = new StringBuilder();
		for (String word : words)
			res.append(new StringBuffer(word).reverse().toString() + " ");
		return res.toString().trim();
	}
	
	// https://discuss.leetcode.com/topic/86985/java-stringbuilder-string-join
	public String reverseWords1(String s) {
        //check if input is valid
        if (s == null || s.length() == 0)
            return "";
            
        String[] str = s.split(" ");

        for (int i = 0; i < str.length; i++) {
            StringBuilder temp = new StringBuilder(str[i]);
            str[i] = temp.reverse().toString();
        }
        
        return String.join(" ", str);
    }
	
	/*
	 * Approach #3 Using StringBuilder and reverse method
	 * https://leetcode.com/articles/reverse-words-in-a-string/
	 */
	public String reverseWords2(String input) {
		final StringBuilder result = new StringBuilder();
		final StringBuilder word = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != ' ') {
				word.append(input.charAt(i));
			} else {
				result.append(word.reverse());
				result.append(" ");
				word.setLength(0);
			}
		}
		result.append(word.reverse());
		return result.toString();
	}
	
	/*
	 * self
	 * 
	 * Rf : https://discuss.leetcode.com/topic/85753/java-solution-stringbuilder
	 */
	public String reverseWords_self(String s) {
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            StringBuilder tmp = new StringBuilder(word);
            tmp.reverse();
            tmp.append(" ");
            sb.append(tmp);
        }
        sb.setLength(s.length());
        return sb.toString();
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/88727/very-simple-java-solution-with-self-explanatory-and-clean-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/85911/easiest-java-solution-9ms-similar-to-reverse-words-in-a-string-ii
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/85784/c-java-clean-code/2
	 * https://discuss.leetcode.com/topic/85831/java-simple-solution
	 */
	public String reverseWords_define_reverse(String s) {
		char[] input = s.toCharArray();
		int start = 0;
		int end = 0;
		int i = 0;

		while (i < input.length) {

			while (i < input.length && Character.isWhitespace(input[i])) {
				i++;
			}
			start = i;

			while (i < input.length && !Character.isWhitespace(input[i])) {
				i++;
			}
			end = i - 1;
			reverse_define_char(input, start, end);
		}
		return new String(input);
	}
	public void reverse_define_char(char[] input, int start, int end) {
		while (start <= end) {
			char temp = input[start];
			input[start] = input[end];
			input[end] = temp;
			start++;
			end--;
		}
	}
	
	/*
	 * Approach #2 Without using pre-defined split and reverse function
	 * The following 3 functions are from this link.
	 * https://leetcode.com/articles/reverse-words-in-a-string/
	 */
	public String reverseWords_define_split_reverse(String s) {
		String words[] = split(s);
		StringBuilder res = new StringBuilder();
		for (String word : words)
			res.append(reverse_StringBuilder(word) + " ");
		return res.toString().trim();
	}
	public String[] split(String s) {
		ArrayList<String> words = new ArrayList<>();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				words.add(word.toString());
				word = new StringBuilder();
			} else
				word.append(s.charAt(i));
		}
		words.add(word.toString());
		return words.toArray(new String[words.size()]);
	}
	public String reverse_StringBuilder(String s) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
			res.insert(0, s.charAt(i));
		return res.toString();
	}

}
