package OJ0631_0640;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/*
 * https://leetcode.com/articles/design-log-storage/
 * 
 * If we modify the following code in method "public long granularity(String s, String gra, boolean end)"
 * String[] res = new String[] {"1999", "00", "00", "00", "00", "00"};
 * to
 * String[] res = new String[] {"1999", "01", "01", "00", "00", "00"};
 * 
 * , We don't need "st[1] = st[1] - (st[1] == 0 ? 0 : 1);" and "st[2] = st[2] - (st[2] == 0 ? 0 : 1);"
 * in method "public long convert(int[] st)".
 * 
 */

public class LogSystem {
	/*
	 * https://discuss.leetcode.com/topic/94434/java-code-using-hashmap
	 * 
	 * Rf : https://discuss.leetcode.com/topic/94449/concise-java-solution
	 * https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#compareTo(java.lang.String)
	 */
	Map<Integer, String> map = new HashMap<>();
    public LogSystem() {
        
    }
    
	public void put(int id, String timestamp) {
		map.put(id, timestamp);
	}

	public List<Integer> retrieve(String s, String e, String gra) {
		int x = 0;
		switch (gra) {
		case "Year":
			x = 4;
			break;
		case "Month":
			x = 7;
			break;
		case "Day":
			x = 10;
			break;
		case "Hour":
			x = 13;
			break;
		case "Minute":
			x = 16;
			break;
		case "Second":
			x = 19;
			break;
		}
		s = s.substring(0, x);
		e = e.substring(0, x);
		List<Integer> ans = new ArrayList<>();
		for (Integer i : map.keySet()) {
			String ss = map.get(i).substring(0, x);
			if (ss.compareTo(s) >= 0 && ss.compareTo(e) <= 0)
				ans.add(i);
		}
		return ans;
	}

}
