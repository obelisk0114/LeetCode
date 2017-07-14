package OJ0001_0010;

/*
 * https://discuss.leetcode.com/topic/3151/2147447412-is-not-a-palindromic-number
 */

public class Palindrome_Number {
	/*
	 * https://discuss.leetcode.com/topic/8090/9-line-accepted-java-code-without-the-need-of-handling-overflow
	 * 
	 * Rf : https://discuss.leetcode.com/topic/40845/9-ms-java-beats-99-5-java-solutions-easy-to-understand
	 */
	public boolean isPalindrome(int x) {
		if (x < 0 || (x != 0 && x % 10 == 0))
			return false;
		int rev = 0;
		while (x > rev) {
			rev = rev * 10 + x % 10;
			x = x / 10;
		}
		return (x == rev || x == rev / 10);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/45465/beat-90-in-java
	 */
	public boolean isPalindrome2(int x) {
		if (x < 0)
			return false;
		int ans = 0;
		int num = x;
		while (num > 0) {
			ans = ans * 10 + num % 10;
			num = num / 10;
			System.out.println("ans = " + ans);
		}
		if (ans != x)           // If overflow occurs, ans will not be equals to x.
			return false;
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Palindrome_Number palindromeNum = new Palindrome_Number();
		//int a = -2147447412;        false
		//int a = 120030221;
		int a = 2147483647;
		System.out.println(palindromeNum.isPalindrome(a));
		System.out.println(palindromeNum.isPalindrome2(a));

	}

}
