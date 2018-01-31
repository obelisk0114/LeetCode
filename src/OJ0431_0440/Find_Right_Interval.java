package OJ0431_0440;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;

public class Find_Right_Interval {
	// https://discuss.leetcode.com/topic/65817/java-clear-o-n-logn-solution-based-on-treemap
	public int[] findRightInterval(Interval[] intervals) {
        int[] result = new int[intervals.length];
        TreeMap<Integer, Integer> intervalMap = new TreeMap<>();
        
        for (int i = 0; i < intervals.length; ++i) {
            intervalMap.put(intervals[i].start, i);    
        }
        
        for (int i = 0; i < intervals.length; ++i) {
            Integer key = intervalMap.ceilingKey(intervals[i].end);
            result[i] = (key != null) ? intervalMap.get(key) : -1;
        }
        
        return result;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/65905/java-o-nlogn-solution-with-sorting-binary-searching
	 * 
	 * One tricky point is that we need to know the index of the element in the 
	 * original input array but sorting will break the order. 
	 * The solution is to bind the original index of each interval to its start point 
	 * so we still have the index info after sorting the start points. 
	 * This can be done using either a TreeMap (start point as key and original 
	 * index as value) or simply a new n-by-2 array (start point as the first element 
	 * and original index as the second one).
	 * 
	 * The following solution uses an n-by-2 array. One advantage is that the binary 
	 * search can start from the index of each interval in the new array, instead of 
	 * from the beginning every time. 
	 */
	public int[] findRightInterval_binarySearch_array(Interval[] intervals) {
		int[] res = new int[intervals.length];
		int[][] arr = new int[intervals.length][2];

		for (int i = 0; i < intervals.length; i++) {
			arr[i][0] = intervals[i].start;
			arr[i][1] = i;
		}

		Arrays.sort(arr, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return Integer.compare(a[0], b[0]);
			}
		});

		for (int i = 0; i < arr.length; i++) {
			int l = i + 1, r = arr.length - 1, m = 0;

			while (l <= r) {
				m = l + ((r - l) >>> 1);
				if (intervals[arr[i][1]].end <= arr[m][0]) {
					r = m - 1;
				} 
				else {
					l = m + 1;
				}
			}

			res[arr[i][1]] = (l < arr.length ? arr[l][1] : -1);
		}

		return res;
	}
	
	// by myself
	public int[] findRightInterval_myself(Interval[] intervals) {
        int[] ans = new int[intervals.length];
        int[][] startArray = new int[intervals.length][2];
        for (int i = 0; i < intervals.length; i++) {
            startArray[i][0] = intervals[i].start;
            startArray[i][1] = i;
        }
        
        Arrays.sort(startArray, new Comparator<int[]>() {
            public int compare(int[] arr1, int[] arr2) {
                return arr1[0] - arr2[0];
            }
        });
        
        int[] tmp = new int[startArray.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = startArray[i][0];
        }
        
        for (int i = 0; i < startArray.length; i++) {
            int end = intervals[i].end;
            int pos = Arrays.binarySearch(tmp, end);
            if (pos < 0)
                pos = -(pos + 1);
            ans[i] = (pos < startArray.length) ? startArray[pos][1] : -1;
        }
        return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/67399/java-concise-binary-search
	 */
	public int[] findRightInterval_binarySearch(Interval[] intervals) {
	    Map<Integer, Integer> map = new HashMap<>();
	    List<Integer> starts = new ArrayList<>();
	    for (int i = 0; i < intervals.length; i++) {
	        map.put(intervals[i].start, i);
	        starts.add(intervals[i].start);
	    }
	    
	    Collections.sort(starts);
	    int[] res = new int[intervals.length];
	    for (int i = 0; i < intervals.length; i++) {
	        int end = intervals[i].end;
	        int start = binarySearch(starts, end);
	        if (start < end) {
	            res[i] = -1;
	        } else {
	            res[i] = map.get(start);
	        }
	    }
	    return res;
	}
	public int binarySearch(List<Integer> list, int x) {
	    int left = 0, right = list.size() - 1;
	    while (left < right) {
	        int mid = left + (right - left) / 2;
	        if (list.get(mid) < x) { 
	            left = mid + 1;
	        } else {
	            right = mid;
	        }
	    }
	    return list.get(left);
	}
	
	// https://discuss.leetcode.com/topic/65641/commented-java-o-n-logn-solution-sort-binary-search
	public int[] findRightInterval_Interval(Interval[] intervals) {
		int n;
		// boundary case
		if (intervals == null || (n = intervals.length) == 0)
			return new int[] {};

		// output
		int[] res = new int[intervals.length];
		// auxiliary array to store sorted intervals
		Interval[] sintervals = new Interval[n];

		// sintervals don't have any use of 'end', so let's use it for tracking
		// original index
		for (int i = 0; i < n; ++i) {
			sintervals[i] = new Interval(intervals[i].start, i);
		}

		// sort
		Arrays.sort(sintervals, (a, b) -> a.start - b.start);

		int i = 0;
		for (; i < n; ++i) {
			int key = intervals[i].end;
			// binary search in sintervals for key
			int l = 0, r = n - 1;
			int right = -1;
			while (l <= r) {
				int m = l + (r - l) / 2;
				if (sintervals[m].start == key) {
					right = sintervals[m].end; // original index is stored in end
					break;
				} 
				else if (sintervals[m].start < key) {
					l = m + 1;
				} 
				else {
					r = m - 1;
				}
			}

			// if we haven't found the key, try looking for 'start' that's just greater
			if ((right == -1) && (l < n)) {
				right = sintervals[l].end; // original index is stored in end
			}

			res[i] = right;
		}

		return res;
	}

	/*
	 * The following class and function are from this link.
	 * https://discuss.leetcode.com/topic/65585/java-sweep-line-solution-o-nlogn
	 * 
	 * 1. Whenever meet a end point, keep a list(prevIdxs) before next start, 
	 * save original index of curr interval to the list.
	 * 2. Whenever meet a start point, this start point is the right interval to the 
	 * intervals in the list (prevIdxs). 
	 * Take out each index in it and update to result.
	 */
	class Point implements Comparable<Point> {
		int val;      // value
		int flag;     // 1 start, 0 end
		int index;    // original position in intervals array

		public Point(int val, int flag, int index) {
			this.val = val;
			this.flag = flag;
			this.index = index;
		}

		// sort by value ascending, end in front of start if they have same value.
		public int compareTo(Point o) {
			if (this.val == o.val)
				return this.flag - o.flag; // end in front of start
			return this.val - o.val;
		}
	}
	public int[] findRightInterval_Point(Interval[] intervals) {
		if (intervals == null || intervals.length == 0)
			return new int[] {};

		int[] res = new int[intervals.length];
		Arrays.fill(res, -1);

		List<Point> points = new ArrayList<>();
		for (int i = 0; i < intervals.length; i++) {
			points.add(new Point(intervals[i].start, 1, i));
			points.add(new Point(intervals[i].end, 0, i));
		}
		Collections.sort(points);

		List<Integer> prevIdxs = new ArrayList<>();
		for (Point point : points) {
			if (point.flag == 1) {
				for (Integer prevIdx : prevIdxs) {
					res[prevIdx] = point.index;
				}
				prevIdxs.clear();
			} else {
				prevIdxs.add(point.index);
			}
		}

		return res;
	}
	
	// Definition for an interval.
	class Interval {
		int start;
		int end;
		Interval() { start = 0; end = 0; }
		Interval(int s, int e) { start = s; end = e; }
	}

}
