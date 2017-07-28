package OJ0401_0410;

import java.util.List;
import java.util.ArrayList;

public class Binary_Watch {
	/*
	 * https://discuss.leetcode.com/topic/59611/straightforward-java-answer
	 * 
	 * Rf : https://discuss.leetcode.com/topic/59374/simple-python-java
	 */
	public List<String> readBinaryWatch_bitCount(int num) {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 60; j++) {
				if (Integer.bitCount(i) + Integer.bitCount(j) == num) {
					result.add(String.format("%d:%02d", i, j));
				}
			}
		}
		return result;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/60562/3m-java-recursion-solution-easy-to-understand
	 */
	final int[] watch = { 1, 2, 4, 8, 1, 2, 4, 8, 16, 32 };
	public List<String> readBinaryWatch(int num) {
		List<String> list = new ArrayList<String>();
		if (num >= 0 && num <= 8)
			read_recursion(num, 0, list, 0, 0);
		return list;
	}
	private void read_recursion(int num, int start, List<String> list, int hour, int minute) {
		if (num == 0) {
			if (hour < 12 && minute < 60) {
				if (minute < 10)
					list.add(hour + ":0" + minute);
				else
					list.add(hour + ":" + minute);
			}
		} else {
			for (int i = start; i < watch.length; i++) {
				if (i < 4)
					read_recursion(num - 1, i + 1, list, hour + watch[i], minute);
				else
					read_recursion(num - 1, i + 1, list, hour, minute + watch[i]);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/59309/simple-java-ac-solution-with-explanation/2
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/59494/3ms-java-solution-using-backtracking-and-idea-of-permutation-and-combination 
	 * https://discuss.leetcode.com/topic/59498/easy-backtracking-solution-with-comments
	 */
	public List<String> readBinaryWatch2(int num) {
		List<String> res = new ArrayList<>();
		helper(res, new boolean[10], 0, num);
		return res;
	}
	void helper(List<String> res, boolean[] choose, int start, int k) {
		if (k > 8)
			return;
		if (k == 0) {
			// (10 choose num) is done, check if time is valid
			int[] cache = new int[] { 8, 4, 2, 1, 32, 16, 8, 4, 2, 1 };
			int hh = 0, mm = 0;
			for (int i = 0; i < 10; i++) {
				if (choose[i]) {
					if (i < 4)
						hh += cache[i];
					else
						mm += cache[i];
				}
			}
			if (hh < 12 && mm < 60) {
				if (mm < 10)
					res.add("" + hh + ":0" + mm);
				else
					res.add("" + hh + ":" + mm);
			}
		} else {
			for (int i = start; i < choose.length - k + 1; i++) {
				choose[i] = true;
				helper(res, choose, i + 1, k - 1);
				choose[i] = false;
			}
		}
	}
	
	// https://discuss.leetcode.com/topic/63062/concise-backtracking
	
	/*
	 * The following 2 variables and function are from this link.
	 * https://discuss.leetcode.com/topic/59761/just-for-fun-java-1ms-beats-100
	 */
	String[][] hour_cheat = { { "0" },             // hours contains 0 1's
			{ "1", "2", "4", "8" },          // hours contains 1 1's
			{ "3", "5", "6", "9", "10" },    // hours contains 2 1's
			{ "7", "11" } };                 // hours contains 3 1's
	String[][] minute_cheat = { { "00" },                 // mins contains 0 1's
			{ "01", "02", "04", "08", "16", "32" }, // mins contains 1 1's
			{ "03", "05", "06", "09", "10", "12", "17", "18", "20", "24", "33", "34", "36", "40", "48" }, // mins contains 2 1's
			{ "07", "11", "13", "14", "19", "21", "22", "25", "26", "28", "35", "37", "38", "41", "42", "44", "49", "50", "52", "56" }, // mins contains 3 1's
			{ "15", "23", "27", "29", "30", "39", "43", "45", "46", "51", "53", "54", "57", "58" }, // mins contains 4 1's
			{ "31", "47", "55", "59" } };           // mins contains 5 1's
	public List<String> readBinaryWatch_cheat(int n) {
		List<String> ret = new ArrayList<>();
		// loop from 0 to 3 which is the max number of bits can be set in hours (4 bits)
		for (int i = 0; i <= 3 && i <= n; i++) {
			// this if condition is to make sure the index from minutes array would be valid
			if (n - i <= 5) {
				// if we have i 1's in hours, then we need n - i 1's in minutes,
				// that's why the arrays were created by grouping the number of
				// 1's bits
				for (String str1 : hour_cheat[i]) {
					for (String str2 : minute_cheat[n - i]) {
						ret.add(str1 + ":" + str2);
					}
				}
			}
		}
		return ret;
	}

}
