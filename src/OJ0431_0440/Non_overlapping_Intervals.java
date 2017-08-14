package OJ0431_0440;

import java.util.Arrays;
import java.util.Comparator;

public class Non_overlapping_Intervals {
	/*
	 * The following variable and function are from this link.
	 * https://discuss.leetcode.com/topic/65594/java-least-is-most
	 * 
	 * Sorting Interval.end in ascending order O(nlogn), 
	 * traverse intervals array to get the maximum number of non-overlapping intervals O(n).
	 */
	public int eraseOverlapIntervals(Interval[] intervals) {
		if (intervals.length == 0)
			return 0;

		Arrays.sort(intervals, new myComparator());
		int end = intervals[0].end;
		int count = 1;

		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i].start >= end) {
				end = intervals[i].end;
				count++;
			}
		}
		return intervals.length - count;
	}
	class myComparator implements Comparator<Interval> {
		public int compare(Interval a, Interval b) {
			return a.end - b.end;
		}
	}
	
	/*
	 * myself
	 * Arrays.sort(intervals, (a, b) -> (a.end == b.end ? b.start - a.start : a.end - b.end));
	 * 
	 * Rf : https://discuss.leetcode.com/topic/65828/java-solution-with-clear-explain
	 */
	public int eraseOverlapIntervals_self(Interval[] intervals) {
		Arrays.sort(intervals, new Comparator<Interval>() {
			public int compare(Interval o1, Interval o2) {
				return Integer.compare(o1.end, o2.end);
			}
		});
		int count = 0;
		int pre = 0;
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i].start < intervals[pre].end) {
				count++;
			} 
			else {
				pre = i;
			}
		}
		return count;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/65772/share-my-simple-greedy-solution-java-6ms
	 * 
	 * Rf : https://discuss.leetcode.com/topic/66200/simple-solution
	 */
	public int eraseOverlapIntervals_sort_start(Interval[] intervals) {
		if (intervals.length < 2)
			return 0;
		Arrays.sort(intervals, new Comparator<Interval>() {
			public int compare(Interval A, Interval B) {
				if (A.start != B.start)
					return A.start - B.start;
				else
					return A.end - B.end;
			}
		});
		int ret = 0;
		Interval pre = intervals[0];
		for (int i = 1; i < intervals.length; i++) {
			if (pre.end <= intervals[i].start)
				pre = intervals[i];
			else {
				ret++;
				if (pre.end > intervals[i].end)
					pre = intervals[i];
			}
		}
		return ret;
	}
	
	// https://discuss.leetcode.com/topic/72802/java-o-n-2-using-dp-accepted

	// initialize
	public class Interval {
		int start;
		int end;

		Interval() {
			start = 0;
			end = 0;
		}

		Interval(int s, int e) {
			start = s;
			end = e;
		}
	}

}
