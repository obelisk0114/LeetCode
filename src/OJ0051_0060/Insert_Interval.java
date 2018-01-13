package OJ0051_0060;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Insert_Interval {
	// https://discuss.leetcode.com/topic/7808/short-and-straight-forward-java-solution
	public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
	    List<Interval> result = new LinkedList<>();
	    int i = 0;
	    
	    // add all the intervals ending before newInterval starts
	    while (i < intervals.size() && intervals.get(i).end < newInterval.start)
	        result.add(intervals.get(i++));
	    
	    // merge all overlapping intervals to one considering newInterval
	    while (i < intervals.size() && intervals.get(i).start <= newInterval.end) {
	        newInterval = new Interval( // we could mutate newInterval here also
	                Math.min(newInterval.start, intervals.get(i).start),
	                Math.max(newInterval.end, intervals.get(i).end));
	        i++;
	    }
	    
	    result.add(newInterval); // add the union of intervals we got
	    
	    // add all the rest
		while (i < intervals.size())
			result.add(intervals.get(i++));
		
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12691/short-java-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/37056/clean-and-short-java-solution-with-explanation
	 */
	public List<Interval> insert2(List<Interval> intervals, Interval newInterval) {
		List<Interval> result = new ArrayList<Interval>();
		for (Interval i : intervals) {
			if (newInterval == null || i.end < newInterval.start)
				result.add(i);
			else if (i.start > newInterval.end) {
				result.add(newInterval);
				result.add(i);
				newInterval = null;
			} 
			else {
				newInterval.start = Math.min(newInterval.start, i.start);
				newInterval.end = Math.max(newInterval.end, i.end);
			}
		}
		if (newInterval != null)
			result.add(newInterval);
		return result;
	}
	
	/**
	 * https://discuss.leetcode.com/topic/40838/java-2ms-o-log-n-o-1-binary-search-solution-beats-97-7-with-clear-explaination
	 * 
	 * Since the original list is sorted and all intervals are disjoint,
	 * apply binary search to find the insertion index for the new interval. [LC35]
	 * 
	 * 1. Find insIdx = the insertion index of new.start, i.e., the first
	 * index i such that list.get(i).start >= new.start.
	 * 
	 * 2. Find nxtIdx = the insertion index of new.end, i.e., the first
	 * index i such that list.get(i).end >= new.end.
	 * 
	 * 3. Remove all elements of the list with indices insIdx <= i < nxtIdx.
	 * 
	 * 4. Merge new with list.get(insIdx-1) or list.get(nxtIdx) or both.
	 */
	public List<Interval> insert_binary_delete(List<Interval> intervals, Interval newInterval) {
		int n = intervals.size();
		if (n == 0) {
			intervals.add(newInterval);
			return intervals;
		}

		int low = 0, high = n - 1, mid = 0;
		int temp, target = newInterval.start;
		while (low <= high) {
			mid = (low + high) / 2;
			temp = intervals.get(mid).start;
			if (temp == target)
				break;
			if (temp < target)
				low = mid + 1;
			else
				high = mid - 1;
		}

		// insIdx = the index where new interval to be inserted
		int insIdx = (low <= high) ? mid : low;
		Interval pre = (insIdx == 0) ? null : intervals.get(insIdx - 1);
		// 0 <= insIdx <= n, pre = [insIdx - 1], pre.start < new.start

		low = insIdx;
		high = n - 1;
		target = newInterval.end;
		while (low <= high) {
			mid = (low + high) / 2;
			temp = intervals.get(mid).end;
			if (temp == target)
				break;
			if (temp < target)
				low = mid + 1;
			else
				high = mid - 1;
		}

		// nxtIdx = the next index after the inserted new interval
		int nxtIdx = (low <= high) ? mid : low;
		Interval nxt = (nxtIdx == n) ? null : intervals.get(nxtIdx);
		// insIdx <= nxtIdx <= n, nxt = [nxtIdx], nxt.end >= new.end

		// [0]...[insIdx-1] <--> [insIdx]...[nxtIdx-1][nxtIdx]...[n]
		intervals.subList(insIdx, nxtIdx).clear();

		// check whether newInterval can be merged with pre or nxt
		boolean isMerged = false, isMerged2 = false;
		if (insIdx > 0 && pre.end >= newInterval.start) {
			pre.end = Math.max(pre.end, newInterval.end);
			isMerged = true;
		}

		if (nxtIdx < n && newInterval.end >= nxt.start) {
			nxt.start = Math.min(nxt.start, newInterval.start);
			isMerged2 = isMerged;
			isMerged = true;
		}

		// Didn't enter any if condition, non-overlapping with any intervals
		if (!isMerged) {
			intervals.add(insIdx, newInterval);
			return intervals;
		}

		// Enter both if condition, overlapping with pre and nxt. Need to remove one
		if (isMerged2) {
			nxt.start = pre.start; // pre.start < new.start, nxt.start;
			intervals.remove(insIdx - 1); // remove pre
		}

		return intervals;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/41004/my-binary-search-approach-implementation-2ms
	 * 
	 * For me, I want to get the position of the interval that is the first to have 
	 * start equal to or larger than the inserted interval's start. Similarly, I get 
	 * the position of the interval that is the first to have end equal to or larger 
	 * than the new interval's end. Then I can get the range of intervals that the 
	 * new interval connects. 
	 */
	public List<Interval> insert_binarySearch(List<Interval> intervals, Interval newInterval) {
		List<Interval> result = new ArrayList<>();
		if (intervals == null || newInterval == null)
			return result;
		
		int iStart = findStartPos(intervals, newInterval.start);
		int iEnd = findEndPos(intervals, newInterval.end);
		
		if (iStart > 0 && intervals.get(iStart - 1).end >= newInterval.start)
			iStart--;
		if (iEnd == intervals.size() || intervals.get(iEnd).start > newInterval.end)
			iEnd--;

		// If not in the corner cases, this condition should apply.
		if (iStart <= iEnd) {
			newInterval = new Interval(Math.min(newInterval.start, intervals.get(iStart).start),
					Math.max(newInterval.end, intervals.get(iEnd).end));
		}

		int i = 0;
		while (i < iStart)
			result.add(intervals.get(i++));
		
		result.add(newInterval);
		i = iEnd + 1;
		
		while (i < intervals.size())
			result.add(intervals.get(i++));
		return result;
	}
	private int findStartPos(List<Interval> intervals, int value) {
		int l = 0, r = intervals.size() - 1;
		while (l <= r) {
			int m = (l + r) >> 1;
			if (intervals.get(m).start == value)
				return m;
			else if (intervals.get(m).start < value)
				l = m + 1;
			else
				r = m - 1;
		}
		return l;
	}
	private int findEndPos(List<Interval> intervals, int value) {
		int l = 0, r = intervals.size() - 1;
		while (l <= r) {
			int m = (l + r) >> 1;
			if (intervals.get(m).end == value)
				return m;
			else if (intervals.get(m).end < value)
				l = m + 1;
			else
				r = m - 1;
		}
		return l;
	}
	
	// by myself
	public List<Interval> insert_self1(List<Interval> intervals, Interval newInterval) {
        List<Interval> list = new ArrayList<>();
        if (intervals.isEmpty()) {
            list.add(newInterval);
            return list;
        }
        
        int insert = Collections.binarySearch(intervals, newInterval, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                return o1.start - o2.start;
            }
        });
        if (insert < 0)
            insert = -(insert + 1);
        intervals.add(insert, newInterval);
        
        Interval compare = intervals.get(0);
        
        for (Interval i : intervals) {
            if (i.start <= compare.end) {
                compare.end = Math.max(compare.end, i.end);
            }
            else {
                list.add(compare);
                compare = i;
            }
        }
        list.add(compare);
        
        return list;
    }
	
	/*
	 * by myself
	 * 
	 * Rf : https://discuss.leetcode.com/topic/28202/easy-java-solution
	 */
	public List<Interval> insert_self2(List<Interval> intervals, Interval newInterval) {
        List<Interval> list = new ArrayList<>();
        if (intervals.isEmpty()) {
            list.add(newInterval);
            return list;
        }
        
        Interval compare = intervals.get(0);
        if (newInterval.start <= compare.start) 
            compare = newInterval;
        
        for (Interval i : intervals) {
            if (newInterval.start <= i.start) {
                if (newInterval.start <= compare.end) {
                    compare.end = Math.max(compare.end, newInterval.end);
                }
                else {
                    list.add(compare);
                    compare = newInterval;
                }
            }
            
            if (i.start <= compare.end) {
                compare.end = Math.max(compare.end, i.end);
            }
            else {
                list.add(compare);
                compare = i;
            }
        }
        if (newInterval.start > compare.end) {
            list.add(compare);
            list.add(newInterval);
        }
        else {
            compare.end = Math.max(compare.end, newInterval.end);
            list.add(compare);
        }
        
        return list;
    }
	
	// https://discuss.leetcode.com/topic/8781/short-simple-o-n-in-place-java-solution-with-explanation
	
	// Interval class
	public class Interval {
		int start;
		int end;
		Interval() { start = 0; end = 0; }
		Interval(int s, int e) { start = s; end = e; }
	}

}
