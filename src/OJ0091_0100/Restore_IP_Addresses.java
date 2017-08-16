package OJ0091_0100;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Restore_IP_Addresses {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/34366/java-recursive-backtracking-easy-to-read
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/37270/share-my-clean-dfs-java-code
	 * https://discuss.leetcode.com/topic/4742/very-simple-dfs-solution
	 */
	public List<String> restoreIpAddresses(String s) {
		List<String> ret = new LinkedList<>();
		int[] path = new int[4];
		helper(ret, s, 0, path, 0);
		return ret;
	}
	private void helper(List<String> acc, String s, int idx, int[] path, int segment) {
		if (segment == 4 && idx == s.length()) {
			acc.add(path[0] + "." + path[1] + "." + path[2] + "." + path[3]);
			return;
		} 
		else if (segment == 4 || idx == s.length()) {
			return;
		}

		for (int len = 1; len <= 3 && idx + len <= s.length(); len++) {
			int val = Integer.parseInt(s.substring(idx, idx + len));
			// range check, no leading 0.
			if (val > 255 || (len >= 2 && s.charAt(idx) == '0'))
				break;

			path[segment] = val;
			helper(acc, s, idx + len, path, segment + 1);
			path[segment] = -1; // for debug.
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/6304/my-concise-ac-java-code
	 * 
	 * make three cuts into the string, separating it into four parts, 
	 * each part contains 1~3 digits and it must be <255.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/3919/my-code-in-java
	 */
	public List<String> restoreIpAddresses_iterative(String s) {
		List<String> ans = new ArrayList<String>();
		int len = s.length();
		for (int i = 1; i <= 3; ++i) { // first cut
			if (len - i > 9)  // remain string is too long 
				continue;
			for (int j = i + 1; j <= i + 3; ++j) { // second cut
				if (len - j > 6)  // remain string is too long
					continue;
				for (int k = j + 1; k <= j + 3 && k < len; ++k) { // third cut
					int a, b, c, d; // the four int's separated by "."
					// notice that "01" can be parsed into 1. Need to deal with that later.
					a = Integer.parseInt(s.substring(0, i));
					b = Integer.parseInt(s.substring(i, j)); 
					c = Integer.parseInt(s.substring(j, k));
					d = Integer.parseInt(s.substring(k));
					if (a > 255 || b > 255 || c > 255 || d > 255)
						continue;
					String ip = a + "." + b + "." + c + "." + d;
					if (ip.length() < len + 3)
						continue; // this is to reject those int's parsed from "01" or "00"-like substrings
					ans.add(ip);
				}
			}
		}
		return ans;
	}
	
	// https://discuss.leetcode.com/topic/41384/simple-java-solution-beating-100-of-java-submissions
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/928/what-is-the-definition-of-a-valid-ip-address
	 */
	public List<String> restoreIpAddresses_self(String s) {
        List<String> res = new ArrayList<>();
        if (s.length() > 12 || s.length() < 4)
            return res;
        split_self(s, 0, res, new ArrayList<Long>());
        return res;
    }
    private void split_self(String s, int level, List<String> res, List<Long> list) {
        if (level == s.length()) {
            String s1 = "";
            for (Long element : list) {
                s1 = s1 + element + ".";
            }
            String appends1 = s1.substring(0, s1.length() - 1);
            if (appends1.length() != s.length() + 3)
                return;
            res.add(appends1);
            return;
        }
        for (int i = level; i < s.length(); i++) {
            if (list.size() == 4)
                return;
            long single = Long.parseLong(s.substring(level, i + 1));
            if ((single < 10 && i - level == 2) || (single < 100 && i - level == 3))
                break;
            if (single < 256) {
                list.add(single);
                split_self(s, i + 1, res, list);
                list.remove(list.size() - 1);
            }
        }
    }
	
	/*
	 * The following 2 functions are by myself.
	 */
	public List<String> restoreIpAddresses_self1(String s) {
        Set<String> res = new HashSet<>();
        if (s.length() > 12 || s.length() < 4)
            return new ArrayList<String>();
        split_self1(s, 0, res, new ArrayList<Long>());
        return new ArrayList<String>(res);
    }
    private void split_self1(String s, int level, Set<String> res, List<Long> list) {
        if (level == s.length()) {
            if (list.size() != 4)
                return;
            String s1 = "";
            for (Long element : list) {
                s1 = s1 + element + ".";
            }
            String appends1 = s1.substring(0, s1.length() - 1);
            if (appends1.length() != s.length() + 3)
                return;
            res.add(appends1);
            return;
        }
        for (int i = level; i < s.length(); i++) {
            long single = Long.parseLong(s.substring(level, i + 1));
            if (single < 256) {
                list.add(single);
                split_self1(s, i + 1, res, list);
                list.remove(list.size() - 1);
            }
        }
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Restore_IP_Addresses restoreIP = new Restore_IP_Addresses();
		//String s = "25525511135";
		String s = "010010";
		//long single = Long.parseLong(s.substring(0, s.length()));
		//System.out.println(single);
		List<String> ans = restoreIP.restoreIpAddresses(s);
		for (String s1 : ans) {
			System.out.print(s1 + " ; ");
		}

	}

}
