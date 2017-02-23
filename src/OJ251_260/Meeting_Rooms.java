package OJ251_260;

/*
 * Slow, but use some api
 * https://discuss.leetcode.com/topic/77707/simple-solution-by-java-8-stream
 */

import java.util.Arrays;
import java.util.Comparator;

public class Meeting_Rooms {
	private class Interval {
		int start;
		int end;
		Interval() { start = 0; end = 0; }
		Interval(int s, int e) { start = s; end = e; }
	}
	
	boolean canAttendMeetings_end(Interval[] intervals) {
		Arrays.sort(intervals, new Comparator<Interval> () {
			public int compare(Interval interval1, Interval interval2) {
				return interval1.end - interval2.end;
			}
		});
		
		int end = -1;
		for (Interval interval : intervals) {
			if (interval.start < end)
				return false;
			end = interval.end;
		}
		return true;
	}
	
	boolean canAttendMeetings_start(Interval[] intervals) {
	    Arrays.sort(intervals, new Comparator<Interval>() {
	        public int compare(Interval i1, Interval i2) {
	            return i1.start - i2.start;
	        }        
	    });
	    for (int i = 0; i < intervals.length - 1; i++) {
	        if (intervals[i].end > intervals[i + 1].start) return false;
	    }
	    return true;
	}
	
	// The following 2 functions are from this link.
	// https://discuss.leetcode.com/topic/20959/ac-clean-java-solution/2
	public boolean canAttendMeetings_sort_with_flag(Interval[] intervals) {
	    try {
		    Arrays.sort(intervals, new IntervalComparator());
	    } catch (Exception e) {
		    return false;
	    }
	    return true;
    }

    private class IntervalComparator implements Comparator<Interval> {
	    public int compare(Interval o1, Interval o2) {
		    if (o1.start < o2.start && o1.end <= o2.start)
			    return -1;
		    else if (o1.start > o2.start && o1.start >= o2.end)
			    return 1;
		    throw new RuntimeException();
	    }
    }
    
    // https://discuss.leetcode.com/topic/61946/share-my-java-o-n-solution-without-sort
	boolean canAttendMeetings_mapping(Interval[] intervals) {
		int max = 0;
		for (Interval i : intervals) {
			max = Math.max(i.end, max);
		}
		int[] result = new int[max + 1];
		for (Interval i : intervals) {
			result[i.start] += 1;
			result[i.end] += -1;
		}
		int sum = 0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
			if (sum > 1) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/31306/easy-java-solution-beat-98
	 * 
	 * Overlap means there's an interval 
	 * which start time is earlier than another's end time 
	 * which begins before this one 
	 * 
	 * Sort the start time array and end time array to find if this happens
	 */
	boolean canAttendMeetings(Interval[] intervals) {
		int len = intervals.length;
		if (len == 0) {
			return true;
		}
		int[] begin = new int[len];
		int[] stop = new int[len];
		for (int i = 0; i < len; i++) {
			begin[i] = intervals[i].start;
			stop[i] = intervals[i].end;
		}
		Arrays.sort(begin);
		Arrays.sort(stop);
		for (int i = 1; i < len; i++) {
			if (begin[i] < stop[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	// The same as canAttendMeetings, but used radix sort to sort the arrays
	boolean canAttendMeetings2(Interval[] intervals) {
		int len = intervals.length;
		if (len == 0 || len == 1) {
			return true;
		}
		int[] begin = new int[len];
		int[] stop = new int[len];
		for (int i = 0; i < len; i++) {
			begin[i] = intervals[i].start;
			stop[i] = intervals[i].end;
		}
		radixLSD(begin);
		radixLSD(stop);
		for (int i = 1; i < len; i++) {
			if (begin[i] < stop[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	private void radixLSD(int[] arr) {
		int max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		int d = 1;
		while ((max /= 10) > 0) {
			d++;
		}
		
		int k = 0;
		int n = 1;
		d = (int) Math.pow(10, d);
		
		int[][] temp = new int[10][arr.length];
		int[] order = new int[10];
		
		while (n < d) {
			for (int element : arr) {
				int lsd = (element / n) % 10;
				temp[lsd][order[lsd]] = element;
				order[lsd]++;
			}
			
			for (int i = 0; i < 10; i++) {
				if (order[i] != 0) {
					for (int j = 0; j < order[i]; j++) {
						arr[k] = temp[i][j];
						k++;
					}
					order[i] = 0;
				}
			}
			
			n *= 10;
			k = 0;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Meeting_Rooms meeting = new Meeting_Rooms();
		
		Meeting_Rooms.Interval i1 = meeting.new Interval(50, 60);
		Meeting_Rooms.Interval i2 = meeting.new Interval();
		Meeting_Rooms.Interval i3 = meeting.new Interval(40, 50);
		Meeting_Rooms.Interval i4 = meeting.new Interval(22, 33);
		Meeting_Rooms.Interval i5 = meeting.new Interval(35, 35);
		Meeting_Rooms.Interval i6 = meeting.new Interval(1, 10);
		Meeting_Rooms.Interval i7 = meeting.new Interval(77, 88);
		
		Interval[] interval = {i1, i2, i3, i4, i5, i6, i7};
		System.out.println("end : " + meeting.canAttendMeetings_end(interval));
		System.out.println("start : " + meeting.canAttendMeetings_start(interval));
		System.out.println("flag : " + 
		meeting.canAttendMeetings_sort_with_flag(interval));
		System.out.println("canAttendMeetings : " + meeting.canAttendMeetings(interval));

	}

}
