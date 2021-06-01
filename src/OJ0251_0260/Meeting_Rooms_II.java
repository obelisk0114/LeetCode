package OJ0251_0260;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * https://leetcode.com/problems/meeting-rooms-ii/discuss/203658/HashMapTreeMap-resolves-Scheduling-Problem
 * 
 * 1. Load all intervals to the TreeMap, where keys are intervals' start/end 
 *    boundaries, and values accumulate the changes at that point in time.
 * 2. Traverse the TreeMap (in other words, sweep the timeline). If a new interval 
 *    starts, increase the counter (k value) by 1, and the counter decreases by 1, 
 *    if an interval has finished.
 * 3. Calculate the number of the active ongoing intervals.
 * 
 * https://leetcode.com/problems/meeting-rooms-ii/solution/
 * 
 * At any point in time we have multiple rooms that can be occupied and we don't 
 * really care which room is free as long as we find one when required for a new 
 * meeting.
 * 
 * When we encounter an ending event, that means that some meeting that started 
 * earlier has ended now. We are not really concerned with which meeting has ended. 
 * All we need is that some meeting ended thus making a room available.
 */

public class Meeting_Rooms_II {
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/134561
	 * 
	 * Add the meeting room to the queue, if no overlap, poll it and add the next 
	 * meeting. Whenever there's an overlap, add the next meeting to create an 
	 * additional room. Finally, return the size of the priority queue for the 
	 * total # of meeting rooms.
	 * 
	 * The heap stores all conflicting events, which must be resolved by independent 
	 * rooms. The heap's head is the event that has earliest end/finish time. All 
	 * other events collide with each other mutually in the heap.
	 * 
	 * When a new event comes (this is the reason that we need to sort by 
	 * event.start), we greedily choose the event A that finished the earliest (this 
	 * is the reason that we use minheap on end time). If the new event does not 
	 * collide with A, then the new event can re-use A's room, or simply extend A's 
	 * room to the new event's end time.
	 * 
	 * If the new event collides with A, then it must collide with all events in the 
	 * heap. So a new room must be created.
	 * 
	 * The reason for correctness is the invariant: heap size is always the minimum 
	 * number of rooms we need so far. If the new event collides with everyone, then 
	 * a new room must be created; if the new event does not collide with someone, 
	 * then it must not collide with the earliest finish one, so greedily choose that 
	 * one and re-use that room. So the invariant is maintained.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/69761
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/69742
	 * 
	 * Other code:
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/867512
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/451832
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/69775
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/655094
	 */
	public int minMeetingRooms_pq_sort2(int[][] intervals) {
		if (intervals == null || intervals.length == 0)
			return 0;
		
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

		PriorityQueue<int[]> q = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		q.add(intervals[0]);

		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i][0] >= q.peek()[1])
				q.poll();

			q.offer(intervals[i]);
		}

		return q.size();
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/solution/
	 * Approach 2: Chronological Ordering
	 * 
	 * Arranging the meetings according to their start times helps us know the 
	 * natural order of meetings throughout the day.
	 * 
	 * We also need the meetings sorted by their ending times because an ending event 
	 * essentially tells us that there must have been a corresponding starting event 
	 * and more importantly, an ending event tell us that a previously occupied room 
	 * has now become free.
	 * 
	 * When we encounter an ending event, that means that some meeting that started 
	 * earlier has ended now. We are not really concerned with which meeting has 
	 * ended. All we need is that some meeting ended thus making a room available.
	 * 
	 * Rf :
	 * https://www.geeksforgeeks.org/find-the-point-where-maximum-intervals-overlap/
	 */
	public int minMeetingRooms_start_end3(int[][] intervals) {

		// Check for the base case. If there are no intervals, return 0
		if (intervals.length == 0) {
			return 0;
		}

		int[] start = new int[intervals.length];
		int[] end = new int[intervals.length];

		for (int i = 0; i < intervals.length; i++) {
			start[i] = intervals[i][0];
			end[i] = intervals[i][1];
		}

		// Sort the intervals by end time
		Arrays.sort(end);

		// Sort the intervals by start time
		Arrays.sort(start);

		int startPointer = 0, endPointer = 0;

		// Variables to keep track of maximum number of rooms used.
		int usedRooms = 0;

		// Iterate over intervals.
		while (startPointer < intervals.length) {

			// If there is a meeting that has ended by the time the meeting at
			// `start_pointer` starts
			if (start[startPointer] >= end[endPointer]) {
				usedRooms -= 1;
				endPointer += 1;
			}

			// We do this irrespective of whether a room frees up or not.
			// If a room got free, then this used_rooms += 1 wouldn't have any
			// effect. used_rooms would remain the same in that case. If no room was 
			// free, then this would increase used_rooms
			usedRooms += 1;
			startPointer += 1;

		}

		return usedRooms;
	}

	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/278270/JavaC%2B%2BPython-Sort-All-Time-Point
	 * 
	 * Track the change of room numbers in time order.
	 * 1. Save all time points and the change on current meeting rooms.
	 * 2. Sort all the changes on the key of time points.
	 * 3. Track the current number of using rooms cur and update result res.
	 * 
	 * A bit like Valid Parentheses: we need to pair an open time of meeting with 
	 * exactly a close time of meeting followed by it. So example 1(start), 2(start),
	 * 10(end), 12(end), 14(start), 15(end) -> open, open, close, close, open, close. 
	 * We can easily see there are two consecutive open, we need 2 meeting rooms.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/203658/HashMapTreeMap-resolves-Scheduling-Problem
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/203658/HashMapTreeMap-resolves-Scheduling-Problem/546283
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/203658/HashMapTreeMap-resolves-Scheduling-Problem/221920
	 */
	public int minMeetingRooms_pair(int[][] intervals) {
		Map<Integer, Integer> m = new TreeMap<>();
		for (int[] t : intervals) {
			m.put(t[0], m.getOrDefault(t[0], 0) + 1);
			m.put(t[1], m.getOrDefault(t[1], 0) - 1);
		}
		
		int res = 0, cur = 0;
		for (int v : m.values()) {
			res = Math.max(res, cur += v);
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap
	 * 
	 * The heap stores all conflicting events, which must be resolved by independent 
	 * rooms. The heap's head is the event that has earliest end/finish time. All 
	 * other events collide with each other mutually in the heap.
	 * 
	 * When a new event comes (this is the reason that we need to sort by 
	 * event.start), we greedily choose the event A that finished the earliest (this 
	 * is the reason that we use minheap on end time). If the new event does not 
	 * collide with A, then the new event can re-use A's room, or simply extend A's 
	 * room to the new event's end time.
	 * 
	 * If the new event collides with A, then it must collide with all events in the 
	 * heap. So a new room must be created.
	 * 
	 * The reason for correctness is the invariant: heap size is always the minimum 
	 * number of rooms we need so far. If the new event collides with everyone, then 
	 * a new room must be created; if the new event does not collide with someone, 
	 * then it must not collide with the earliest finish one, so greedily choose that 
	 * one and re-use that room. So the invariant is maintained.
	 * 
	 * [Remember A is the interval that started not later than the new interval and 
	 * finishes earlier than all other intervals in the heap.]
	 * 
	 * Because A is the interval that is furthest apart (among all the intervals in 
	 * the heap) from the new interval. If the furthest apart interval has no gap 
	 * with the new interval, the other intervals in the heap that less far apart 
	 * from the new interval (than A) will not have gap either -- which would mean 
	 * the new interval needs its own separate room.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/69761
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67857/AC-Java-solution-using-min-heap/69742
	 */
	public int minMeetingRooms_pq_sort(int[][] intervals) {
	    if (intervals == null || intervals.length == 0)
	        return 0;
	        
	    // Sort the intervals by start time
	    Arrays.sort(intervals, new Comparator<int[]>() {
	        public int compare(int[] a, int[] b) { return a[0] - b[0]; }
	    });
	    
	    // Use a min heap to track the minimum end time of merged intervals
	    PriorityQueue<int[]> heap = new PriorityQueue<int[]>(intervals.length, 
	    		new Comparator<int[]>() {
	    			public int compare(int[] a, int[] b) { return a[1] - b[1]; }
	    });
	    
	    // start with the first meeting, put it to a meeting room
	    heap.offer(intervals[0]);
	    
	    for (int i = 1; i < intervals.length; i++) {
	        // get the meeting room that finishes earliest
	        int[] interval = heap.poll();
	        
			// if the current meeting starts right after
			// there's no need for a new room, merge the interval
			if (intervals[i][0] >= interval[1]) {
				interval[1] = intervals[i][1];
			}
			// otherwise, this meeting needs a new room
			else {
				heap.offer(intervals[i]);
			}

	        // don't forget to put the meeting room back
	        heap.offer(interval);
	    }
	    
	    return heap.size();
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-"Super-Easy-Java-Solution-Beats-98.8"-from-@pinkfloyda/69710
	 * 
	 * This is a two pointer problem (greedy solution).
	 * Sort start and end intervals. Take two pointers, one for start time and one 
	 * for end time. if the start interval is less than the end interval increment 
	 * the room counter since we would need a extra room, else decrement the count 
	 * since we have freed up the room.
	 * 
	 * Rf :
	 * https://www.geeksforgeeks.org/find-the-point-where-maximum-intervals-overlap/
	 */
	public int minMeetingRooms_start_end2(int[][] intervals) {
		int[] start = new int[intervals.length];
		int[] end = new int[intervals.length];
		for (int i = 0; i < intervals.length; ++i) {
			start[i] = intervals[i][0];
			end[i] = intervals[i][1];
		}
		
		Arrays.sort(start);
		Arrays.sort(end);

		int i = 0;
		int j = 0;
		int count = 0;
		int len = 0;
		while (i < intervals.length && j < intervals.length) {
			if (start[i] < end[j]) {
				count++;
				i++;
				
				len = Math.max(len, count);
			} 
			else {
				j++;
				count--;
			}
		}
		return len;
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-%22Super-Easy-Java-Solution-Beats-98.8%22-from-%40pinkfloyda
	 * 
	 * whenever there is a start meeting, we need to add one room. But before adding 
	 * rooms, we check to see if any previous meeting ends, which is why we check 
	 * start with the first end. When the start is bigger than end, it means at this 
	 * time one of the previous meeting ends, and it can take and reuse that room. 
	 * Then the next meeting need to compare with the second end because the first 
	 * end's room is already taken. One thing is also good to know: meetings start is 
	 * always smaller than end. Whenever we pass a end, one room is released.
	 * 
	 * -----------------------------------------------------------
	 * 
	 * checking how many meetings begin before the earliest-ended meeting ends.
	 * For eg:
	 * Starts 1,  5,  6,  9, 10
	 * Ends   8, 11, 12, 13, 14
	 * 
	 * Initially, endsItr points to the first end event, and we move i which is the 
	 * start event pointer.
	 * 
	 * so meeting 1,5,6 start before first meeting ends at 8 so we need 3 rooms.
	 * For 9 and 8 we just move i++ and endsItr++ ( think of as it took the spot of 
	 * the meeting ended at 8.)
	 * then for 10 and 11.. all previous rooms are occupied and one of them ends 
	 * after 10... so we need a room for a meeting starting at 10
	 * so total 4 rooms
	 * 
	 * Rf :
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-"Super-Easy-Java-Solution-Beats-98.8"-from-@pinkfloyda/228318
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-"Super-Easy-Java-Solution-Beats-98.8"-from-@pinkfloyda/69724
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67883/Super-Easy-Java-Solution-Beats-98.8/244951
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67883/Super-Easy-Java-Solution-Beats-98.8
	 * 
	 * Other code:
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-"Super-Easy-Java-Solution-Beats-98.8"-from-@pinkfloyda/69730
	 */
	public int minMeetingRooms_start_end(int[][] intervals) {
		int[] starts = new int[intervals.length];
		int[] ends = new int[intervals.length];
		for (int i = 0; i < intervals.length; i++) {
			starts[i] = intervals[i][0];
			ends[i] = intervals[i][1];
		}
		
		Arrays.sort(starts);
		Arrays.sort(ends);
		int rooms = 0;
		int endsItr = 0;
		
		for (int i = 0; i < starts.length; i++) {
			if (starts[i] < ends[endsItr])
				rooms++;
			else
				endsItr++;
		}
		return rooms;
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/377759/Simple-java-O(maxTime)-soln.-No-TreeMap-No-Heap.-Simplest-solution-with-comments
	 * 
	 * Rf :
	 * https://www.geeksforgeeks.org/find-the-point-where-maximum-intervals-overlap/
	 */
	public int minMeetingRooms_go_through(int[][] intervals) {
		if (intervals.length == 0)
			return 0;

		int end = Integer.MIN_VALUE;

		// first get the max time till rooms would be required
		for (int[] i : intervals) {
			end = Math.max(end, i[1]);
		}

		// make an array to record how many rooms required at a time
		int[] overlaps = new int[end + 1];

		// O(N) way to fill the array.
		for (int[] i : intervals) {
			overlaps[i[0]] += 1;
			overlaps[i[1]] -= 1;
		}

		// 累加之前的 meeting room
		for (int i = 1; i < overlaps.length; i++) {
			overlaps[i] += overlaps[i - 1];
		}
		
		int rooms = 1;
		
		// max rooms required is the max room required at one time t.
		for (int i = 1; i < overlaps.length; i++) {
			rooms = Math.max(rooms, overlaps[i]);
		}
		return rooms;
	}
	
	/*
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67860/My-Python-Solution-With-Explanation/261747
	 * 
	 * Sort all starts and ends in an array, add one room when it is a start and 
	 * subtract one room when it is an end. That's it. 2*n lg (2*n) if you care 
	 * about the constant factor, but much more concise.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67855/Explanation-of-"Super-Easy-Java-Solution-Beats-98.8"-from-@pinkfloyda/203101
	 * 
	 * Other code:
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67995/Elegant-9-line-Java-using-heap-and-6-ms-greedy-Java-(-greater92.03)
	 */
	public int minMeetingRooms_sortAll(int[][] intervals) { 
		
		// put points in a a list. arr[1] is a tag
        List<int[]> time = new ArrayList<>();
        for (int[] interval : intervals) {
            int[] start = { interval[0], 1 };
            int[] end = { interval[1], -1 };
            
            time.add(start);
            time.add(end);
        }
        
        // if start != end, we order it with time, else we want end to happen first, 
        // and because end is -1 and we want it to be put in front of 1, it is 
        // a[1] - b[1]
        Collections.sort(time, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            }
            else {
                return a[1] - b[1];
            }
        });
        
        int count = 0;
        int maxcount = 0;
        
        for (int[] t : time) {
            count = count + t[1];
            maxcount = Math.max(maxcount, count);
        }
        
        return maxcount;
	}
	
	/*
	 * The following class and its functions are from this link.
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67920/Java-Another-thinking%3A-process-event-queue
	 * 
	 * Simulate event queue procession. Create event for each start and end of 
	 * intervals. Then for start event, open one more room; for end event, close 
	 * one meeting room. At the same time, update the most rooms that is required.
	 * 
	 * Be careful of events like [(end at 11), (start at 11)]. Put end before start 
	 * event when they share the same happening time, so that two events can share 
	 * one meeting room.
	 */
	class Solution_event_queue {
	    private static final int START = 1;
	    private static final int END = 0;
	    
	    private class Event {
	        int time;
	        int type; // end event is 0; start event is 1

	        public Event(int time, int type) {
	            this.time = time;
	            this.type = type;
	        }
	    }
	    
	    public int minMeetingRooms(int[][] intervals) {
	        int rooms = 0; // occupied meeting rooms
	        int res = 0;

	        // initialize an event queue based on event's happening time
	        Queue<Event> events = new PriorityQueue<>(new Comparator<Event>() {
	            @Override
	            public int compare(Event e1, Event e2) {
	                // for same time, let END event happens first to save rooms
	                return e1.time != e2.time ? 
	                       e1.time - e2.time : e1.type - e2.type;
	            }
	        });

	        // create event and push into event queue
	        for (int[] interval : intervals) {
	            events.offer(new Event(interval[0], START));
	            events.offer(new Event(interval[1], END));
	        }
	        
	        // process events
	        while (!events.isEmpty()) {
	            Event event = events.poll();
	            
	            if (event.type == START) {
	                rooms++;
	                res = Math.max(res, rooms);
	            } 
	            else {
	                rooms--; 
	            }
	        }
	        
	        return res;
	    }
	}
	
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67998/JAVA-AC-Solution-Greedy-beats-92.03
	 * 
	 * According to greedy, you get one interval, then add the one right behind it. 
	 * Then recursively deal with the rest.
	 */
	public int minMeetingRooms_level(int[][] intervals) {
		Arrays.sort(intervals, new Comparator<int[]>() {
			public int compare(int[] o1, int[] o2) {
				return o1[0] - o2[0];
			}
		});
		
		return helper_level(new ArrayList<>(Arrays.asList(intervals)));
	}

	private int helper_level(List<int[]> li) {
		if (li.size() == 0)
			return 0;
		
		int[] pre = li.get(0);
		List<int[]> nextLi = new ArrayList<>();
		
		for (int i = 1; i < li.size(); i++) {
			int[] inter = li.get(i);
			
			if (inter[0] < pre[1]) {
				nextLi.add(inter);
			} 
			else {
				pre = inter;
			}
		}
		return 1 + helper_level(nextLi);
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/373235/Python3-Min-heap-with-explaination
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/272822/Python-Greedy-Interval-Partition-Problem
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/67917/Python-heap-solution-with-comments.
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/67860/My-Python-Solution-With-Explanation
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/322622/Simple-Python-solutions
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/67996/C%2B%2B-O(n-log-n)-584%2B-ms-3-solutions
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/67866/C%2B%2B-solution-using-a-map.-total-11-lines
     * https://leetcode.com/problems/meeting-rooms-ii/discuss/67989/Concise-C%2B%2B-Solution-with-min_heap-sort-greedy
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/meeting-rooms-ii/discuss/67955/Javascript-beats-100
	 */

}
