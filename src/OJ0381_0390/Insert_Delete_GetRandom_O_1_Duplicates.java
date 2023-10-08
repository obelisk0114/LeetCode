package OJ0381_0390;

import java.util.Set;
import java.util.HashSet;
//import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Insert_Delete_GetRandom_O_1_Duplicates {
	/**
	 * Your RandomizedCollection object will be instantiated and called as such:
	 * RandomizedCollection obj = new RandomizedCollection();
	 * boolean param_1 = obj.insert(val);
	 * boolean param_2 = obj.remove(val);
	 * int param_3 = obj.getRandom();
	 */
	
	/*
	 * https://discuss.leetcode.com/topic/53688/java-haspmap-linkedhashset-arraylist-155-ms
	 * 
	 * Replacing HashSet with LinkedHashSet because the set.iterator() might be costly 
	 * when a number has too many duplicates. Using LinkedHashSet can be considered as 
	 * O(1) if we only get the first element to remove.
	 * 
	 * When iterator() is called, HashSet needs to link the its elements while 
	 * LinkedHashSet has the linkage ready. Therefore, HashSet's iterator() might 
	 * cost O(n).
	 * 
	 * Rf :
	 * https://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashSet.html
	 * https://discuss.leetcode.com/topic/53216/java-solution-using-a-hashmap-and-an-arraylist-along-with-a-follow-up-131-ms/22
	 * https://discuss.leetcode.com/topic/53688/java-haspmap-linkedhashset-arraylist-155-ms/37
	 * https://discuss.leetcode.com/topic/53717/clean-o-1-java-solution-with-hashmap-and-set
	 * https://discuss.leetcode.com/topic/53717/clean-o-1-java-solution-with-hashmap-and-set/9
	 */
	ArrayList<Integer> nums;
	HashMap<Integer, Set<Integer>> locs;
	java.util.Random rand = new java.util.Random();

    /** Initialize your data structure here. */
    public Insert_Delete_GetRandom_O_1_Duplicates() {  // public RandomizedCollection()
		nums = new ArrayList<Integer>();
		locs = new HashMap<Integer, Set<Integer>>();
    }
    
    /** Inserts a value to the collection. Returns true if the collection did not already contain the specified element. */
    public boolean insert(int val) {
		boolean contain = locs.containsKey(val);
		if (!contain) {
			locs.put(val, new HashSet<Integer>());
			// 下面一行是原始解法, 使用 LinkedHashSet
			//locs.put(val, new LinkedHashSet<Integer>());
		}
		
		locs.get(val).add(nums.size());
		nums.add(val);
		return !contain;
    }
    
    /** Removes a value from the collection. Returns true if the collection contained the specified element. */
    public boolean remove(int val) {
		if (!locs.containsKey(val))
			return false;
		int loc = locs.get(val).iterator().next();
		locs.get(val).remove(loc);
		if (loc < nums.size() - 1) {
			int lastone = nums.get(nums.size() - 1);
			nums.set(loc, lastone);
			locs.get(lastone).remove(nums.size() - 1);
			locs.get(lastone).add(loc);
		}
		nums.remove(nums.size() - 1);

		if (locs.get(val).isEmpty())
			locs.remove(val);
		return true;
    }
    
    /** Get a random element from the collection. */
    public int getRandom() {
        return nums.get( rand.nextInt(nums.size()) );
    }
	
    /*
     * Original use HashSet, but use iterator() may cost O(n). (needs to link its elements)
     * 
     * https://discuss.leetcode.com/topic/53768/easy-understanding-java-solution-using-hashset
     * 
     */
    
	// https://discuss.leetcode.com/topic/57032/java-arraylist-and-two-maps-solution

}
