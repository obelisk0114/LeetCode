package OJ0151_0160;

import java.util.Collections;
import java.util.Arrays;

public class Reverse_Words_in_a_String {
	// https://discuss.leetcode.com/topic/30410/my-10-line-java-code-that-beats-73-of-the-submissions
	public String reverseWords_substring(String s) {
		StringBuilder res = new StringBuilder();
		for (int start = s.length() - 1; start >= 0; start--) {
			if (s.charAt(start) == ' ')
				continue;
			int end = start;
			while (start >= 0 && s.charAt(start) != ' ')
				start--;
			res.append(s.substring(start + 1, end + 1)).append(" ");
		}
		return res.toString().trim();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/1152/are-we-allowed-to-use-java-build-in-functions
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/23035/simple-java-solution
	 */
	public String reverseWords(String s) {
		if (s == null || s.length() <= 0)
			return s;
		String[] words = s.split(" +");
		StringBuilder ret = new StringBuilder();
		for (int i = words.length - 1; i >= 0; i--) {
			System.out.println("***" + words[i] + "***");
			if (words[i].length() > 0)
				ret.append(words[i]).append(" ");
		}
		return ret.toString().trim();
	}
	
	// https://discuss.leetcode.com/topic/2742/my-accepted-java-solution
	public String reverseWords_string(String s) {
		String[] parts = s.trim().split("\\s+");
		String out = "";
		for (int i = parts.length - 1; i > 0; i--) {
			out += parts[i] + " ";
		}
		return out + parts[0];
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/18189/clean-java-two-pointers-solution-no-trim-no-split-no-stringbuilder
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/7610/in-place-java-solution-with-comments-just-for-fun
	 */
	public String reverseWords_word(String s) {
		if (s == null)
			return null;

		char[] a = s.toCharArray();
		int n = a.length;

		// step 1. reverse the whole string
		reverse(a, 0, n - 1);
		// step 2. reverse each word
		reverseWords(a, n);
		// step 3. clean up spaces
		return cleanSpaces(a, n);
	}
	void reverseWords(char[] a, int n) {
		int i = 0, j = 0;

		while (i < n) {
			while (i < j || i < n && a[i] == ' ')
				i++; // skip spaces
			while (j < i || j < n && a[j] != ' ')
				j++; // skip non spaces
			reverse(a, i, j - 1); // reverse the word
		}
	}
	// trim leading, trailing and multiple spaces
	String cleanSpaces(char[] a, int n) {
		int i = 0, j = 0;
		while (j < n && a[j] == ' ')   // skip spaces
			j++;

		while (j < n) {
			// while (j < n && a[j] == ' ') j++; // skip spaces
			while (j < n && a[j] != ' ')
				a[i++] = a[j++];         // keep non spaces
			while (j < n && a[j] == ' ')
				j++;                     // skip spaces
			if (j < n)
				a[i++] = ' ';            // keep only one space
		}

		return new String(a).substring(0, i);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/45395/java-in-place-3-ms-solution-beats-88
	 * 
	 * Space is tackled by trimming and shifting chars to left 
	 * i.e if i points to space and i-1 also point to space 
	 * shift chars starting from i+1 left by one place. 
	 * Decrement end pointer pointing to end of string.
	 * 
	   1. s =" Hello World "
       2. Trim s = "Hello World"
       3. Reverse each word - s ="olleH dlroW"
       4. Reverse whole string s= "World Hello"
	 * 
	 * Rf : https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#String(char[],%20int,%20int)
	 */
	public String reverseWords_word2(String s) {
		if (s == null)
			return null;

		char[] str = s.toCharArray();
		int start = 0, end = str.length - 1;

		// Trim start of string
		while (start <= end && str[start] == ' ')
			start++;

		// Trim end of string
		while (end >= 0 && str[end] == ' ')
			end--;

		if (start > end)
			return new String("");

		int i = start;
		while (i <= end) {
			if (str[i] != ' ') {
				// case when i points to a start of word - find the word reverse it
				int j = i + 1;
				while (j <= end && str[j] != ' ')
					j++;
				reverse(str, i, j - 1);
				i = j;
			} 
			else {
				if (str[i - 1] == ' ') {
					// case when prev char is also space - 
					// shift char to left by 1 and decrease end pointer
					int j = i;
					while (j <= end - 1) {
						str[j] = str[j + 1];
						j++;
						//System.out.println("j = " + j);
						//System.out.println("end = " + end);
					}
					end--;
				} 
				else    // case when there is just single space
					i++;
			}
		}
		// Now that all words are reversed, time to reverse the entire string
		// pointed by start and end - This step reverses the words in string
		reverse(str, start, end);
		// return new string object pointed by start with len = end -start + 1
		return new String(str, start, end - start + 1);
	}

	private void reverse(char[] str, int begin, int end) {
		while (begin < end) {
			char temp = str[begin];
			str[begin] = str[end];
			str[end] = temp;
			begin++;
			end--;
		}
	}
	
	/*
	 * myself
	 */
	public String reverseWords_self(String s) {
        String[] separate = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < separate.length; i++) {
            sb.append(separate[separate.length - 1 - i]);
            //System.out.println("***" + separate[i] + "***");
            if (!separate[separate.length - 1 - i].equals("")) {
            	sb.append(" ");            	
            }
        }
        return sb.toString().trim();
    }
	
	// https://discuss.leetcode.com/topic/11785/java-3-line-builtin-solution
	public String reverseWords_join(String s) {
	    String[] words = s.trim().split(" +");
	    Collections.reverse(Arrays.asList(words));
	    return String.join(" ", words);
	}
		
	public static void main(String[] args) {
		Reverse_Words_in_a_String reverse = new Reverse_Words_in_a_String();
		//String s = "   a   b ";
		String s = "   a1   b2  cd ";
		System.out.println(reverse.reverseWords(s));
		System.out.println(reverse.reverseWords_word2(s));
	}

}
