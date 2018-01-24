package OJ0351_0360;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

public class Data_Stream_as_Disjoint_Intervals {
	/*
	 * https://discuss.leetcode.com/topic/46887/java-solution-using-treemap-real-o-logn-per-adding
	 * 
	 * Use TreeMap to easily find the lower and higher keys, 
	 * the key is the start of the interval.
	 * 
	 * Merge the lower and higher intervals when necessary. 
	 * The time complexity for adding is O(logN) since lowerKey(), higherKey(), put() 
	 * and remove() are all O(logN). It would be O(N) if you use an ArrayList and 
	 * remove an interval from it.
	 * 
	 * Rf : https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
	 */
	/** Initialize your data structure here. */
	TreeMap<Integer, Interval> tree;
	
	public Data_Stream_as_Disjoint_Intervals() {
        tree = new TreeMap<>();
    }
	
	public void addNum(int val) {
        if(tree.containsKey(val)) return;
        Integer l = tree.lowerKey(val);
        Integer h = tree.higherKey(val);
        if(l != null && h != null && tree.get(l).end + 1 == val && h == val + 1) {
            tree.get(l).end = tree.get(h).end;
            tree.remove(h);
        } 
        else if(l != null && tree.get(l).end + 1 >= val) {
            tree.get(l).end = Math.max(tree.get(l).end, val);
        } 
        else if(h != null && h == val + 1) {
            tree.put(val, new Interval(val, tree.get(h).end));
            tree.remove(h);
        } 
        else {
            tree.put(val, new Interval(val, val));
        }
    }

    public List<Interval> getIntervals() {
        return new ArrayList<>(tree.values());
    }
	
	// https://discuss.leetcode.com/topic/46934/not-very-concise-but-easy-to-read-java-solution-o-logn-per-input
	
	// https://discuss.leetcode.com/topic/47084/java-fast-log-n-solution-186ms-without-using-the-treemap-but-a-customized-bst
	
	// https://discuss.leetcode.com/topic/47608/java-binary-search-upon-list
	
	class Interval {
		int start;
		int end;
		Interval() { start = 0; end = 0; }
		Interval(int s, int e) { start = s; end = e; }
	}
	
	/**
	 * Your SummaryRanges object will be instantiated and called as such:
	 * SummaryRanges obj = new SummaryRanges();
	 * obj.addNum(val);
	 * List<Interval> param_2 = obj.getIntervals();
	 */

}
