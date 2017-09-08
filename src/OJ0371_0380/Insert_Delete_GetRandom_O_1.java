package OJ0371_0380;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Insert_Delete_GetRandom_O_1 {
	/**
	 * Your RandomizedSet object will be instantiated and called as such:
	 * RandomizedSet obj = new RandomizedSet();
	 * boolean param_1 = obj.insert(val);
	 * boolean param_2 = obj.remove(val);
	 * int param_3 = obj.getRandom();
	 */
	
	/*
	 * https://discuss.leetcode.com/topic/53216/java-solution-using-a-hashmap-and-an-arraylist-along-with-a-follow-up-131-ms
	 * 
	 * remove(m) where m is the index of the last element, takes O(1).
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/53235/java-with-hashtable-arraylist
	 * https://discuss.leetcode.com/topic/54069/java-solution-concise-code-with-hashmap-and-arraylist-easy-to-understand
	 */
	private List<Integer> nums;
    private Map<Integer, Integer> locs;

    /** Initialize your data structure here. */  // public RandomizedSet()
    public Insert_Delete_GetRandom_O_1() {
        nums = new ArrayList<Integer>();
        locs = new HashMap<Integer, Integer>();
    }
    
    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if ( locs.containsKey(val) ) return false;
        locs.put( val, nums.size());
        nums.add(val);
        return true;
    }
    
    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
		if ( ! locs.containsKey(val))
			return false;
		int loc = locs.get(val);
        if (loc < nums.size() - 1 ) { // not the last one than swap the last one with this val
            int lastone = nums.get(nums.size() - 1 );
            nums.set( loc , lastone );
            locs.put(lastone, loc);
        }
        locs.remove(val);
        nums.remove(nums.size() - 1);
        return true;
    }
    
    /** Get a random element from the set. */
    public int getRandom() {
        return nums.get( (int) (Math.random() * nums.size()) );
    }
    
    // https://discuss.leetcode.com/topic/53206/java-solution-with-two-hashmaps-easy-to-understand
	
	/*
	 * https://discuss.leetcode.com/topic/95722/java-solution-beats-99-20-using-hashmap-and-arraylist-with-explanation
	 * 
	 * The List is used to store numbers and serve the getRandom() method. The Map 
	 * contains the mapping between the value and its index in the ArrayList. 
	 * The Map helps to check whether a value is already inserted or not.
	 * 
	 * ArrayList's remove method is O(n) if you remove from random location. 
	 * To overcome that, we swap the values between (randomIndex, lastIndex) 
	 * and always remove the entry from the end of the list. 
	 * After the swap, you need to update the new index of the swapped value 
	 * (which was previously at the end of the list) in the map.
	 */

}
