package OJ0761_0770;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Partition_Labels {
	/*
	 * https://leetcode.com/problems/partition-labels/discuss/113259/Java-2-pass-O(n)-time-O(1)-space-extending-end-pointer-solution
	 * 
	 * 1. traverse the string record the last index of each char.
	 * 2. using pointer to record end of the current sub string.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/partition-labels/discuss/323528/Short-and-Sweet-lessJava-Solutiongreater
	 * https://leetcode.com/articles/partition-labels/
	 */
	public List<Integer> partitionLabels(String S) {
		if (S == null || S.length() == 0) {
			return null;
		}
		
		List<Integer> list = new ArrayList<>();
		
		int[] map = new int[26]; // record the last index of the each char
		for (int i = 0; i < S.length(); i++) {
			map[S.charAt(i) - 'a'] = i;
		}
		
		// record the end index of the current sub string
		int last = 0;
		int start = 0;
		for (int i = 0; i < S.length(); i++) {
			last = Math.max(last, map[S.charAt(i) - 'a']);
			if (last == i) {     // at the end of the partition
				list.add(last - start + 1);
				start = last + 1;
			}
		}
		return list;
	}
	
	/*
	 * https://leetcode.com/problems/partition-labels/discuss/342688/Java-Two-pointers-using-LastIndexOf
	 * 
	 * Other code :
	 * https://leetcode.com/problems/partition-labels/discuss/113294/a-concise-easy-understanding-and-efficient-Java-solution
	 */
	public List<Integer> partitionLabels_lastIndexOf(String S) {
		int left = 0;
		List<Integer> list = new ArrayList<>();
		while (left < S.length()) {
			int start = left;
			int right = S.lastIndexOf(S.charAt(left));
			while (left < right) {
				if (S.lastIndexOf(S.charAt(left)) > right) {
					right = S.lastIndexOf(S.charAt(left));
				}
				
				left++;
			}
			
			list.add(right + 1 - start);
			left = right + 1;
		}
		return list;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/partition-labels/discuss/310476/Simple-Java-Solution-3ms-37.1M
	 */
	public List<Integer> partitionLabels_dfs(String S) {
		List<Integer> list = new ArrayList<>();
		dfs(S, list);
		return list;
	}

	private void dfs(String s, List<Integer> list) {
		if (s.length() == 0)
			return;
		
		char first = s.charAt(0);
		int last = s.lastIndexOf(first);
		for (int i = 0; i < last; i++) {
			if (s.lastIndexOf(s.charAt(i)) <= last)
				continue;
			
			last = s.lastIndexOf(s.charAt(i));
		}
		
		s = s.substring(last + 1);
		list.add(last + 1);
		dfs(s, list);
	}
	
	/*
	 * https://leetcode.com/problems/partition-labels/discuss/198587/Java-HashSet-mapping-solution
	 * 
	 * Use sliding window to add all the chars which are not exhausted yet
	 * 
	 * Use a HashSet to know if current char is new or we have seen it before in the 
	 * current window. If we exhausted all char c (table[c-'a'] == 0) in the current 
	 * window, we remove it from HashSet. Therefore, if the size of HashSet is 0, 
	 * it means that current window is a partition, add it to the result list and 
	 * reset window
	 * 
	 * Rf : https://leetcode.com/problems/partition-labels/discuss/113264/Easy-O(n)-Java-solution-using-sliding-window-(two-pointers)-comments-and-explanation-given
	 */
	public List<Integer> partitionLabels_Set_store_seen(String S) {
		HashMap<Character, Integer> letterMap = new HashMap<>();
		Set<Character> backStack = new HashSet<>();
		List<Integer> output = new ArrayList<>();
		int partitionCount = 0;

		for (char l : S.toCharArray())
			letterMap.put(l, letterMap.getOrDefault(l, 0) + 1);

		for (char letter : S.toCharArray()) {
			int letterCount = letterMap.get(letter) - 1;
			letterMap.put(letter, letterCount);

			backStack.add(letter);
			partitionCount++;

			// we have exhausted the char and remove char from set
			if (letterCount == 0) {
				backStack.remove(letter);
			}

			if (backStack.isEmpty()) {
				output.add(partitionCount);
				partitionCount = 0;
			}
		}
		return output;
	}
	
	/*
	 * by myself
	 * 
	 * Rf : https://leetcode.com/problems/partition-labels/discuss/155831/Python-O(1)-space-and-O(n)-time-complexity
	 */
	public List<Integer> partitionLabels_self(String S) {
        List<Integer> ans = new ArrayList<>();
        if (S == null || S.length() == 0)
            return ans;
        
        Map<Character, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < S.length(); i++) {
            char c = S.charAt(i);
            if (map.containsKey(c)) {
                List<Integer> list = map.get(c);
                list.set(1, i);
            }
            else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                list.add(i);
                map.put(c, list);
            }
        }
        
        List<List<Integer>> intervals = new ArrayList<>();
        for (List<Integer> value : map.values() ) {
            intervals.add(value);
        }
        Collections.sort(intervals, new Comparator<List<Integer>>() {
            public int compare(List<Integer> a, List<Integer> b) {
                return a.get(0) - b.get(0);
            }
        });
        
        List<List<Integer>> combines = new ArrayList<>();
        combines.add(intervals.get(0));
        int j = 0;
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals.get(i).get(0) < combines.get(j).get(1)) {
                int pre = intervals.get(i).get(1);
                int post = combines.get(j).get(1);
                combines.get(j).set(1, Math.max(pre, post));
            }
            else {
                List<Integer> cur = combines.get(j);
                ans.add(cur.get(1) - cur.get(0) + 1);
                combines.add(intervals.get(i));
                j++;
            }
        }
        List<Integer> last = combines.get(j);
        ans.add(last.get(1) - last.get(0) + 1);
        return ans;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/partition-labels/discuss/298474/Python-two-pointer-solution-with-explanation
     * https://leetcode.com/problems/partition-labels/discuss/113308/Python-solution-using-HashTable-with-explanation
     * https://leetcode.com/problems/partition-labels/discuss/113258/Short-easy-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/partition-labels/discuss/113293/C%2B%2B-6-lines-O(n)-or-O(1)-two-simple-passes
     * https://leetcode.com/problems/partition-labels/discuss/113261/A-simple-C-solutionAccepted
     */

}
