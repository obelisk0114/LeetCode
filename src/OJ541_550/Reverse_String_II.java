package OJ541_550;

public class Reverse_String_II {
	// https://discuss.leetcode.com/topic/82570/verbose-java-solution-stringbuilder-s
	public String reverseStr_StringBuilder(String s, int k) {
        StringBuilder sb = new StringBuilder();
        
        int i = 0, j = 0;
        while (i < s.length()) {
            // first k
            j = i + k <= s.length() ? i + k : s.length();
            sb.append((new StringBuilder(s.substring(i, j))).reverse().toString());
            
            if (j >= s.length()) break;
            
            // second k
            i = j;
            j = i + k <= s.length() ? i + k : s.length();
            sb.append(s.substring(i, j));
            
            i = j;
        }
        
        return sb.toString();
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/82626/java-concise-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/83870/java-solution
	 */
	public String reverseStr(String s, int k) {
		char[] arr = s.toCharArray();
		int n = arr.length;
		int i = 0;
		while (i < n) {
			int j = Math.min(i + k - 1, n - 1);
			swap(arr, i, j);
			i += 2 * k;
		}
		return String.valueOf(arr);
	}
	private void swap(char[] arr, int l, int r) {
		while (l < r) {
			char temp = arr[l];
			arr[l++] = arr[r];
			arr[r--] = temp;
		}
	}
	
	// https://discuss.leetcode.com/topic/83739/6-lines-java-solution-with-o-n-runtime
	public String reverseStr_insertString(String s, int k) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i % (2 * k) < k) res.insert(i - i % (2 * k), s.charAt(i));
            else res.append(s.charAt(i));
        }
        return res.toString();
    }
	
	// Self
	public String reverseStr_self(String s, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i += (2 * k)) {
			int end = i + k;
			if (end > s.length()) {
				end = s.length();
			}
			for (int j = end - 1; j >= i; j--) {
				sb.append(s.charAt(j));
			}
			if (end < s.length()) {
				int end2 = end + k;
				if (end2 > s.length()) {
					end2 = s.length();
				}
				for (int j = end; j < end2; j++) {
					sb.append(s.charAt(j));
				}
				
			}
		}
		return sb.toString();
	}
	
	// https://discuss.leetcode.com/topic/94302/easy-java-solution-using-stack

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Reverse_String_II reverse2 = new Reverse_String_II();
		String s = "abcdefg";
		System.out.println(reverse2.reverseStr(s, 2));
		System.out.println(reverse2.reverseStr_insertString(s, 2));
		System.out.println(reverse2.reverseStr_self(s, 2));

	}

}
