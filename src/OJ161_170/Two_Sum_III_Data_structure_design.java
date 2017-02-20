package OJ161_170;

/*
 * https://crane-yuan.github.io/2016/08/15/The-map-of-java-sorted-by-value/
 */

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class Two_Sum_III_Data_structure_design {
	/** Initialize your data structure here. */
	/*
    public Two_Sum_III_Data_structure_design() {
        
    }
    */
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    
    /** Add the number to an internal data structure. */
    public void add(int number) {
    	if (map.containsKey(number)) {
    		int value = map.get(number) + 1;
    		map.put(number, value);
    	}
    	else {
    		map.put(number, 1);
    	}
        
    }
    
    /** Find if there exists any pair of numbers which sum is equal to the value. */
    public boolean find(int value) {
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int i = entry.getKey();
			int j = value - i;
			if ((i == j && entry.getValue() > 1) || (i != j && map.containsKey(j))) {
				return true;
			}
		}
		return false;
    }
    
    /*
     * Another implementation, use TreeMap
     * Slow
     */
    Map<Integer, Integer> treemap = new TreeMap<Integer, Integer>();
    
    /** Add the number to an internal data structure. */
    public void add2(int number) {
    	if (treemap.containsKey(number)) {
    		int value = treemap.get(number) + 1;
    		treemap.put(number, value);
    	}
    	else {
    		treemap.put(number, 1);
    	}
        
    }
    
    /** Find if there exists any pair of numbers which sum is equal to the value. */
    public boolean find2(int value) {
		for (Map.Entry<Integer, Integer> entry : treemap.entrySet()) {
			int i = entry.getKey();
			int j = value - i;
			if ((i == j && entry.getValue() > 1) || (i != j && treemap.containsKey(j))) {
				return true;
			}
		}
		return false;
    }

}
