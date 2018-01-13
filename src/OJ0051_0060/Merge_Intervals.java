package OJ0051_0060;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

public class Merge_Intervals {
	/*
	 * https://discuss.leetcode.com/topic/8571/fast-ana-simple-java-code
	 * 
	 * Sort intervals based on start and iterate all intervals to merge them if:
	 * curr.end >= iter.start
	 * 
	 * Rf : https://leetcode.com/articles/merge-intervals/
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/12788/a-clean-java-solution
	 */
	public List<Interval> merge(List<Interval> intervals) {
		List<Interval> res = new LinkedList<Interval>();
		if (intervals.size() < 2)
			return intervals;
		
		Collections.sort(intervals, new Comparator<Interval>() {
			@Override
			public int compare(Interval o1, Interval o2) {
				return o1.start - o2.start;
			}
		});
		Interval curr = intervals.get(0);
		for (Interval iter : intervals) {
			if (curr.end >= iter.start) {
				curr.end = Math.max(curr.end, iter.end);
			} 
			else {
				res.add(curr);
				curr = iter;
			}
		}
		res.add(curr);
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/4319/a-simple-java-solution
	 * 
	 * Sort the intervals by their starting points. Then, we take the first interval 
	 * and compare its end with the next intervals starts. As long as they overlap, 
	 * we update the end to be the max end of the overlapping intervals. 
	 * Once we find a non overlapping interval, we can add the previous 
	 * "extended" interval and start over.
	 * 
	 * Sorting takes O(n log(n)) and merging the intervals takes O(n).
	 */
	public List<Interval> merge_record_start_end(List<Interval> intervals) {
		if (intervals.size() <= 1)
			return intervals;

		// Sort by ascending starting point using an anonymous Comparator
		intervals.sort((i1, i2) -> Integer.compare(i1.start, i2.start));

		List<Interval> result = new LinkedList<Interval>();
		int start = intervals.get(0).start;
		int end = intervals.get(0).end;

		for (Interval interval : intervals) {
			// Overlapping intervals, move the end if needed
			if (interval.start <= end)
				end = Math.max(end, interval.end);
			// Disjoint intervals, add the previous one and reset bounds
			else { 
				result.add(new Interval(start, end));
				start = interval.start;
				end = interval.end;
			}
		}

		// Add the last interval
		result.add(new Interval(start, end));
		return result;
	}
	
	// by myself
	public List<Interval> merge_self(List<Interval> intervals) {
        List<Interval> list = new ArrayList<>();
        if (intervals.size() == 0)
            return list;
        
        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        });
        int pre = -1;
        
        for (Interval i : intervals) {
            int cur = i.start;
            if (cur > pre) {
                list.add(i);
                pre = i.end;
            }
            else {
                Interval last = list.remove(list.size() - 1);
                list.add(new Interval(last.start, Math.max(i.end, last.end)));
                pre = Math.max(i.end, last.end);
            }
        }
        
        return list;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/38628/beat-98-java-sort-start-end-respectively
	 * 
	 * The idea is that for the result distinct Interval, 
	 * the latter one's start must > previous one's end.
	 */
	public List<Interval> merge_sort_start_end(List<Interval> intervals) {
		// sort start & end
		int n = intervals.size();
		int[] starts = new int[n];
		int[] ends = new int[n];
		for (int i = 0; i < n; i++) {
			starts[i] = intervals.get(i).start;
			ends[i] = intervals.get(i).end;
		}
		Arrays.sort(starts);
		Arrays.sort(ends);
		
		// loop through
		List<Interval> res = new ArrayList<Interval>();
		for (int i = 0, j = 0; i < n; i++) {            // j is start of interval.
			if (i == n - 1 || starts[i + 1] > ends[i]) {
				res.add(new Interval(starts[j], ends[i]));
				j = i + 1;
			}
		}
		return res;
	}
	
	// https://discuss.leetcode.com/topic/33690/14ms-java-in-place-merge-solution
	public List<Interval> merge_sort_end(List<Interval> intervals) {
		int N = intervals.size();
		Collections.sort(intervals, new Comparator<Interval>() {
			public int compare(Interval i, Interval j) {
				return i.end - j.end;
			}
		});
		
		for (int i = N - 1; i > 0; i--) {
			Interval inter1 = intervals.get(i - 1);
			Interval inter2 = intervals.get(i);
			if (inter1.end >= inter2.start) {
				inter1.start = Math.min(inter1.start, inter2.start);
				inter1.end = inter2.end; // inter1.end is always smaller than
										// inter2.end because of the sort,
										// so no need to use Math.max()
				intervals.remove(i);
			}
		}
		return intervals;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://discuss.leetcode.com/topic/14551/java-solution-sort-and-merge-intervals-at-same-time
	 * 
	 * Sort and merge intervals at same time, instead of sorting first then merge 
	 * intervals. Implementation is mergeSort algorithm, using divided and conquer.
	 */
	public List<Interval> merge_mergesort(List<Interval> intervals) {
		return mergeSort(0, intervals.size() - 1, intervals);
	}
	public List<Interval> mergeSort(int lo, int hi, List<Interval> intervals) {
		List<Interval> ret = new ArrayList<Interval>();
		if (lo > hi)
			return ret;
		if (lo == hi) {
			ret.add(intervals.get(lo));
			return ret;
		}

		/*
		if (lo + 1 == hi) {
			Interval i1 = intervals.get(lo);
			Interval i2 = intervals.get(hi);
			if (i1.start < i2.start) {
				addOrMerge(ret, i1);
				addOrMerge(ret, i2);
			} else {
				addOrMerge(ret, i2);
				addOrMerge(ret, i1);
			}
			return ret;
		}
		*/

		int mid = (hi - lo) / 2 + lo;
		return merge_mergesort(mergeSort(lo, mid, intervals), 
				mergeSort(mid + 1, hi, intervals));
	}

	/** merge two sorted list **/
	private List<Interval> merge_mergesort(List<Interval> list1, List<Interval> list2) {
		List<Interval> ret = new ArrayList<Interval>();
		int i = 0, j = 0;
		int len1 = list1.size(), len2 = list2.size();

		while (i < len1 && j < len2) {
			if (list1.get(i).start < list2.get(j).start) {
				addOrMerge(ret, list1.get(i++));
			} 
			else {
				addOrMerge(ret, list2.get(j++));
			}
		}

		while (i < len1) {
			addOrMerge(ret, list1.get(i++));
		}

		while (j < len2) {
			addOrMerge(ret, list2.get(j++));
		}
		return ret;
	}

	/** Append or merge an interval to a sorted list */
	void addOrMerge(List<Interval> list, Interval interval) {
		if (list.size() == 0) {
			list.add(interval);
		} 
		else {
			Interval last = list.get(list.size() - 1);
			if (last.end >= interval.start) {
				last.end = Math.max(last.end, interval.end);
			} 
			else {
				list.add(interval);
			}
		}
	}
	
	// Interval class
	public class Interval {
		int start;
		int end;
		Interval() { start = 0; end = 0; }
		Interval(int s, int e) { start = s; end = e; }
	}

}
