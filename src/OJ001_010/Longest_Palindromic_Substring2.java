package OJ001_010;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Palindrome.html
 * 
 * Use Manacher's Algorithm 
 * Last = L = j ; RightMost = R = j+z(j)-1 ; Gusfield's Algorithm
 * nL = n = z[i]
 * 
 */

import java.util.Scanner;

public class Longest_Palindromic_Substring2 {
	private static int[] z;
	private char[] s;
	public Longest_Palindromic_Substring2(char[] charline) {
		int n = charline.length;
		z = new int[2 * n + 1];
		// z[0] = 1;            We don't need to calculate it.
		// z[2 * n] = 1;        We don't need to calculate it.
		s = new char[2 * n + 1];
		for (int i = 0; i < 2 * n + 1; i++) {
			if (i % 2 == 0)
				s[i] = '.';
			else
				s[i] = charline[i / 2];
		}
	}
	
	String find2() {
		int Last = 0;
		int RightMost = 0;
		int n = z.length;
		for (int i = 1; i < n-1; i++) {
			if (RightMost > i)
				z[i] = Math.min(z[2 * Last - i], RightMost - i);
			else
				z[i] = 1;
			
			while (i - z[i] >= 0 && i + z[i] < n && s[i - z[i]] == s[i + z[i]])
				z[i]++;
		    if (i + z[i] > RightMost) {		    	
		    	Last = i;
		    	RightMost = i + z[i];
		    }
		}
		
		String str = ""; 
		int length = z[longestPosition2()];
		for (int i = 0; i < 2 * length - 1; i++) {
			if (i % 2 == 1)
				str += s[longestPosition2() + 1 - length + i];
		}
		return str;
	}
	
	int longestPosition2() {
		int maxPosition = 0;
		for (int i = 1; i < z.length; i++) {
			if (z[i] > z[maxPosition])
				maxPosition = i;
		}
		return maxPosition;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("It's Longest_Palindromic_Substring2.\nInput the String.");
		String line = keyboard.nextLine();
		char[] ch = line.toCharArray();
		Longest_Palindromic_Substring2 lps2 = new Longest_Palindromic_Substring2(ch);
		System.out.println(lps2.find2());
		int longest = (z[lps2.longestPosition2()] - 1) / 2;
		System.out.println("length of Longest Palindromic Substring : " + longest);
		
		keyboard.close();

	}

}
