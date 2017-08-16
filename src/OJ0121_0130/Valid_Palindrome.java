package OJ0121_0130;

public class Valid_Palindrome {
	/*
	 * https://discuss.leetcode.com/topic/32952/java-9ms-solution-with-some-of-my-thoughts
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8282/accepted-pretty-java-solution-271ms
	 */
	public boolean isPalindrome(String s) {
		int start = 0;
		int end = s.length() - 1;
		while (start < end) {
			while (start < end && !Character.isLetterOrDigit(s.charAt(start))) {
				start++;
			}
			while (start < end && !Character.isLetterOrDigit(s.charAt(end))) {
				end--;
			}
			if (start < end && Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
				return false;
			}
			start++;
			end--;
		}
		return true;
	}
	
	// myself
	public boolean isPalindrome_self(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char cur = s.charAt(i);
			if ((0 <= cur - 'a' && cur - 'a' <= 25) || (0 <= cur - 'A' && cur - 'A' <= 25)
					|| (0 <= cur - '0' && cur - '0' <= 9)) {
				sb.append(cur);
			}
		}

		StringBuilder rev = new StringBuilder(sb);
		rev.reverse();
		if (sb.toString().equalsIgnoreCase(rev.toString()))
			return true;
		else
			return false;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/11466/my-2-line-java-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/5117/this-is-my-accepted-java-code-just-for-reference-only
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/25405/my-three-line-java-solution
	 */
	public boolean isPalindrome_regex(String s) {
		String ss = s.toLowerCase().replaceAll("[^a-z0-9]", "");
		return ss.equals(new StringBuilder(ss).reverse().toString());
	}
	
	// https://discuss.leetcode.com/topic/31060/3ms-java-solution-beat-100-of-java-solution

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Valid_Palindrome validPalindrome = new Valid_Palindrome();
		String s = "1. A man, a plan, ^^ a canal: &Panama. 1";
		System.out.println(validPalindrome.isPalindrome(s));

	}

}
