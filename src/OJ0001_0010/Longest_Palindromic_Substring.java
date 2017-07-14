package OJ0001_0010;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Palindrome.html
 * 
 * Use Manacher's Algorithm 
 * Last = L = j ; RightMost = R = j+z(j)-1 ; Gusfield's Algorithm
 * nL = n = z[i]
 * length of palindrome that we use in the algorithm = z[i]-1
 * 
 */

import java.util.Scanner;

public class Longest_Palindromic_Substring {
	private static int[] z;
	private char[] s;         // Insert another character which is not in the string 
	
	public Longest_Palindromic_Substring(char[] line) {
		int n = line.length;
		z = new int[2 * n + 1];
		z[0] = 1;
		z[2 * n] = 1;
		s = new char[2 * n + 1];
		for (int i = 0; i < 2 * n + 1; i++) {
			if (i % 2 == 0)
				s[i] = '.';
			else
				s[i] = line[i / 2];
		}
	}
	
	private int match(int a, int b) {
		int i = 0;
		int n = s.length;
		while (a-i >= 0 && b+i < n && s[a-i] == s[b+i])
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
				z[i] = match(i, i);
				Last = i;
				RightMost = i + z[i] - 1;
			}
			else if (z[ii] == nL) {
				z[i] = nL + match(i - nL, i + nL);
				Last = i;
				RightMost = i + z[i] - 1;
			}
			else {
				z[i] = Math.min(z[ii], nL);
			}
		}
	}
	
	int longestPosition() {
		int maxPosition = 0;
		for (int i = 1; i < z.length; i++) {
			if (z[i] > z[maxPosition])
				maxPosition = i;
		}
		return maxPosition;
	}
	
	void printLongest(int p) {
		int n = z[p];
		for (int i = 0; i < 2 * n - 1; i++) {
			if ( i % 2 == 1)
				System.out.print(s[p - n + 1 + i]);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("This is Longest_Palindromic_Substring.\nInput the String.");
		String line = keyboard.nextLine();
		int length = line.length();
		char[] ch = line.toCharArray();
		Longest_Palindromic_Substring lps = new Longest_Palindromic_Substring(ch);
		lps.find(length);
		int longest = (z[lps.longestPosition()] - 1) / 2;
		System.out.println("length of Longest Palindromic Substring : " + longest);
		lps.printLongest(lps.longestPosition());
		
		keyboard.close();

	}

}
