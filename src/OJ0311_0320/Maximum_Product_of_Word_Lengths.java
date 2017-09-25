package OJ0311_0320;

import java.util.Arrays;
import java.util.Comparator;

public class Maximum_Product_of_Word_Lengths {
	/*
	 * https://discuss.leetcode.com/topic/32616/java-solution-with-comments
	 *
	 * 1. Use 1 bit to represent each letter, and use 32 bit to represent the set of each word
	 * 2. If the ANDing of two checker element equals to 0, these two words do not 
	 * have same letter, then calculate the product of their lengths
	 * 
	 * We can use the lowest 26 bit of int indicates that the word has how many kinds of 
	 * lower case letters. If the lowest bit of int is 1, it indicates the word has 
	 * lower case letter 'a'... the order of lower case letter is from right to left, 
	 * like zyx...cba.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/35539/java-easy-version-to-understand/3
	 * https://discuss.leetcode.com/topic/35539/java-easy-version-to-understand/5
	 * https://discuss.leetcode.com/topic/35539/java-easy-version-to-understand/8
	 * https://discuss.leetcode.com/topic/35539/java-easy-version-to-understand
	 */
	/**
	 * @param words
	 * 
	 * 		The solution is calculated by doing a product of the length of
	 *      each string to every other string. Anyhow the constraint given is
	 *      that the two strings should not have any common character. This
	 *      is taken care by creating a unique number for every string. Image
	 *      a an 32 bit integer where 0 bit corresponds to 'a', 1st bit
	 *      corresponds to 'b' and so on.
	 * 
	 *      Thus if two strings contain the same character when we do and
	 *      "AND" the result will not be zero and we can ignore that case.
	 */
	public int maxProduct(String[] words) {
		int[] checker = new int[words.length];
		int max = 0;
		// populating the checker array with their respective numbers
		for (int i = 0; i < checker.length; i++) {
			int num = 0;
			for (int j = 0; j < words[i].length(); j++) {
				num |= 1 << (words[i].charAt(j) - 'a');
			}
			checker[i] = num;
		}

		for (int i = 0; i < words.length; i++) {
			for (int j = i + 1; j < words.length; j++) {
				// checking if the two strings have common character
				if ((checker[i] & checker[j]) == 0) 
					max = Math.max(max, words[i].length() * words[j].length());
			}
		}
		return max;
	}
	
	// https://discuss.leetcode.com/topic/31769/32ms-java-ac-solution
	public int maxProduct_sort(String[] words) {
		int max = 0;

		Arrays.sort(words, new Comparator<String>() {
			public int compare(String a, String b) {
				return b.length() - a.length();
			}
		});

		int[] masks = new int[words.length]; // alphabet masks

		for (int i = 0; i < masks.length; i++) {
			for (char c : words[i].toCharArray()) {
				masks[i] |= 1 << (c - 'a');
			}
		}

		for (int i = 0; i < masks.length; i++) {
			if (words[i].length() * words[i].length() <= max)
				break; // prunning
			for (int j = i + 1; j < masks.length; j++) {
				if ((masks[i] & masks[j]) == 0) {
					max = Math.max(max, words[i].length() * words[j].length());
					break; // prunning
				}
			}
		}

		return max;
	}
	
	// https://discuss.leetcode.com/topic/40451/java-solution-without-bit-manipulation

}
