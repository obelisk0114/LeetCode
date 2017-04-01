package OJ511_520;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Palindrome.html#2
 * 
 * https://www.zhihu.com/question/34580085
 * 原本字串與反轉字串，兩者的 LCS 長度即是 LPS 長度，兩者的所有 LCS 包含了所有 LPS、有一些 LCS 不是 LPS。
 * LCS 只求前半段，再自行回文得到後半段，就得到 LPS 了。
 */

public class Longest_Palindromic_Subsequence {
	// https://discuss.leetcode.com/topic/79952/short-java-solution-beats-99-with-explanation
	static int[][] dp;
	static int[][] p;
	private static int f(char[] s, int i, int j) {
		if (i == j) 
			return 1;
	    if (i > j) 
	    	return 0;
	    if (dp[i][j] != -1) 
	    	return dp[i][j];
	    
	    // 左右兩端字元相等，定能形成更長迴文，同時從兩端縮小問題範疇。
	    if (s[i] == s[j]) {
	    	dp[i][j] = f(s, i+1, j-1) + 2;
	    	p[i][j] = 0;	
	    }
	 
	    // 刪除左端字元比較好。
	    else if (f(s, i+1, j) > f(s, i, j-1)) {
	    	dp[i][j] = f(s, i+1, j);
	    	p[i][j] = 1;	    	
	    }
	 
	    // 刪除右端字元比較好。
	    else if (f(s, i+1, j) < f(s, i, j-1)) {
	    	dp[i][j] = f(s, i, j-1);
	    	p[i][j] = 2;	    	
	    }
	 
	    // 可以刪除其中一端的字元，都一樣好。
	    else /* if (f(i+1, j) == f(i, j-1)) */ {
	    	dp[i][j] = f(s, i, j-1);
	    	p[i][j] = 3;	    	
	    }
	 
	    return dp[i][j];
	}
	
	private static void print(char[] s, int i, int j) {
		if (i > j) 
			return;
		 
	    // 當迴文長度為奇數，最中間的字母。
	    if (i == j)
	        System.out.print(s[i]);
	 
	    // 兩端字母一樣。
	    else if (p[i][j] == 0) {
	    	System.out.print(s[i]);
	    	print(s, i+1, j-1);
	    	System.out.print(s[i]);	    	
	    }
	 
	    // 刪除左端字元。
	    else if (p[i][j] == 1)
	        print(s, i+1, j);
	 
	    // 刪除右端字元。
	    else
	        print(s, i, j-1);
	}
	
	// https://discuss.leetcode.com/topic/78603/straight-forward-java-dp-solution
	public int longestPalindromeSubseq(String s) {
        int[][] dp2 = new int[s.length()][s.length()];
        
        for (int i = s.length() - 1; i >= 0; i--) {
            dp2[i][i] = 1;
            for (int j = i+1; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp2[i][j] = dp2[i+1][j-1] + 2;
                } else {
                    dp2[i][j] = Math.max(dp2[i+1][j], dp2[i][j-1]);
                }
            }
        }
        return dp2[0][s.length()-1];
    }
	
	// https://discuss.leetcode.com/topic/78603/straight-forward-java-dp-solution/17
	public int longestPalindromeSubseq_edit(String s) {
		int n = s.length();
		char[] sc = s.toCharArray();
		int[][] dp3 = new int[n][n];

		for(int j = 0; j < n; j++){
			dp3[j][j] = 1;
			for(int i = j - 1; i >= 0; i--){
				if(sc[i] == sc[j]) dp3[i][j] = dp3[i + 1][j - 1] + 2;
				else dp3[i][j] = Math.max(dp3[i + 1][j], dp3[i][j - 1]);
			}
		}

		return dp3[0][n - 1];
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String s1 = "naobxijknncixdofnh";
		String s1 = "abab";
		char[] s = new char[s1.length()];
		for (int i = 0; i < s1.length(); i++) {
			s[i] = s1.charAt(i);
		}
		
		dp = new int[s1.length()][s1.length()];
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[0].length; j++) {
				dp[i][j] = -1;
			}
		}
		
		p = new int[s1.length()][s1.length()];
		
		System.out.println("The length of Longest Palindromic Subsequence : " + f(s, 0, s1.length() - 1));
		print(s, 0, s1.length() - 1);

	}

}
