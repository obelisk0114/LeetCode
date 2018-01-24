package OJ0041_0050;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Group_Anagrams {
	/*
	 * https://leetcode.com/articles/group-anagrams/
	 * 
	 * Other code (may overflow) : 
	 * https://discuss.leetcode.com/topic/45639/java-beat-100-use-prime-number
	 */
	public List<List<String>> groupAnagrams(String[] strs) {
		if (strs.length == 0)
			return new ArrayList<>();
		Map<String, List<String>> ans = new HashMap<String, List<String>>();
		int[] count = new int[26];
		for (String s : strs) {
			Arrays.fill(count, 0);
			for (char c : s.toCharArray())
				count[c - 'a']++;

			StringBuilder sb = new StringBuilder("");
			for (int i = 0; i < 26; i++) {
				sb.append('#');
				sb.append(count[i]);
			}
			String key = sb.toString();
			if (!ans.containsKey(key))
				ans.put(key, new ArrayList<>());
			ans.get(key).add(s);
		}
		return new ArrayList<>(ans.values());
	}
	
	// https://discuss.leetcode.com/topic/24494/share-my-short-java-solution
	public List<List<String>> groupAnagrams_sort(String[] strs) {
		if (strs == null || strs.length == 0)
			return new ArrayList<List<String>>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String s : strs) {
			char[] ca = s.toCharArray();
			Arrays.sort(ca);
			String keyStr = String.valueOf(ca);
			if (!map.containsKey(keyStr))
				map.put(keyStr, new ArrayList<String>());
			map.get(keyStr).add(s);
		}
		return new ArrayList<List<String>>(map.values());
	}
	
	// https://discuss.leetcode.com/topic/26795/share-my-simple-java-solution-pretty-easy-to-understand
	public List<List<String>> groupAnagrams_sort2(String[] strs) {
		Arrays.sort(strs);
		HashMap<String, List<String>> hm = new HashMap<>();
		for (int i = 0; i < strs.length; i++) {
			String currString = strs[i];
			char[] charArr = strs[i].toCharArray();
			Arrays.sort(charArr);
			String sortString = new String(charArr);
			List<String> tempList = hm.getOrDefault(sortString, new ArrayList<String>());
			tempList.add(currString);
			hm.put(sortString, tempList);
		}
		return new ArrayList<>(hm.values());
	}

}
