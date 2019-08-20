package OJ0001_0010;

import java.util.Scanner;

public class Longest_Palindromic_Substring2 {
	/*
	 * http://www.csie.ntnu.edu.tw/~u91029/Palindrome.html
	 * 
	 * Use Manacher's Algorithm 
	 * Last = L = j ; RightMost = R = j+z(j)-1 ; Gusfield's Algorithm
	 * nL = n = z[i]
	 * length of palindrome that we use in the algorithm = z[i]-1
	 * 
	 */
	public class LPS1 {
		private int[] z1;
		private char[] s1;      // Insert another character which is not in the string
		
		public LPS1(char[] line) {
			int n = line.length;
			z1 = new int[2 * n + 1];
			z1[0] = 1;
			z1[2 * n] = 1;
			s1 = new char[2 * n + 1];
			for (int i = 0; i < 2 * n + 1; i++) {
				if (i % 2 == 0)
					s1[i] = '.';
				else
					s1[i] = line[i / 2];
			}
		}
		
		private int match(int a, int b) {
			int i = 0;
			int n = s1.length;
			while (a-i >= 0 && b+i < n && s1[a-i] == s1[b+i])
				i++;
			return i;
		}
		
		void find(int n) {
			int Last = 0;
			int RightMost = 0;
			for (int i = 1; i < 2 * n; i++) {
				int ii = Last - (i - Last);
				int nL = RightMost + 1 - i;
				
				if (i > RightMost) {
					z1[i] = match(i, i);
					Last = i;
					RightMost = i + z1[i] - 1;
				}
				else if (z1[ii] == nL) {
					z1[i] = nL + match(i - nL, i + nL);
					Last = i;
					RightMost = i + z1[i] - 1;
				}
				else {
					z1[i] = Math.min(z1[ii], nL);
				}
			}
		}
		
		int longestPosition() {
			int maxPosition = 0;
			for (int i = 1; i < z1.length; i++) {
				if (z1[i] > z1[maxPosition])
					maxPosition = i;
			}
			return maxPosition;
		}
		
		void printLongest(int p) {
			int n = z1[p];
			for (int i = 0; i < 2 * n - 1; i++) {
				if ( i % 2 == 1)
					System.out.print(s1[p - n + 1 + i]);
			}
		}
	}
	
	/*
	 * http://www.csie.ntnu.edu.tw/~u91029/Palindrome.html
	 * 
	 * Use Manacher's Algorithm
	 * Last = L = j ; RightMost = R = j+z(j)-1 ; Gusfield's Algorithm
	 * nL = n = z[i]
	 * 
	 */
	private static int[] z2;
	private char[] s2;
	public Longest_Palindromic_Substring2(char[] charline) {
		int n = charline.length;
		z2 = new int[2 * n + 1];
		// z[0] = 1;            We don't need to calculate it.
		// z[2 * n] = 1;        We don't need to calculate it.
		s2 = new char[2 * n + 1];
		for (int i = 0; i < 2 * n + 1; i++) {
			if (i % 2 == 0)
				s2[i] = '.';
			else
				s2[i] = charline[i / 2];
		}
	}
	
	String find2() {
		int Last = 0;
		int RightMost = 0;
		int n = z2.length;
		for (int i = 1; i < n-1; i++) {
			if (RightMost > i)
				z2[i] = Math.min(z2[2 * Last - i], RightMost - i);
			else
				z2[i] = 1;
			
			while (i - z2[i] >= 0 && i + z2[i] < n && s2[i - z2[i]] == s2[i + z2[i]])
				z2[i]++;
		    if (i + z2[i] > RightMost) {		    	
		    	Last = i;
		    	RightMost = i + z2[i];
		    }
		}
		
		String str = ""; 
		int length = z2[longestPosition2()];
		for (int i = 0; i < 2 * length - 1; i++) {
			if (i % 2 == 1)
				str += s2[longestPosition2() + 1 - length + i];
		}
		return str;
	}
	
	int longestPosition2() {
		int maxPosition = 0;
		for (int i = 1; i < z2.length; i++) {
			if (z2[i] > z2[maxPosition])
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
		int longest = (z2[lps2.longestPosition2()] - 1) / 2;
		System.out.println("length of Longest Palindromic Substring : " + longest);
		
		keyboard.close();

	}

}
