package OJ0621_0630;

import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.PriorityQueue;

// leetcode.com/problems/course-schedule-iii/discuss/104847/Python-Straightforward-with-Explanation/107619

public class Course_Schedule_III {
	/*
	 * Sort all the courses by their ending time. For each K, we will greedily remove 
	 * the largest-length course until the total duration start is <= end. To select 
	 * these largest-length courses, we will use a max heap. start will maintain the 
	 * loop invariant that it is the sum of the lengths of the courses we have 
	 * currently taken.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/course-schedule-iii/discuss/104855/Java-Solution-using-Array-sort-and-Heap
	 * https://leetcode.com/problems/course-schedule-iii/discuss/104847/Python-Straightforward-with-Explanation
	 */
	public int scheduleCourse(int[][] courses) {
		Arrays.sort(courses, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return o1[1] - o2[1];
			}
		});

		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

		int t = courses[0][0];
		maxHeap.add(courses[0][0]);
		for (int i = 1; i < courses.length; i++) {
			int[] c = courses[i];
			if (c[0] + t <= c[1]) {
				t += c[0];
				maxHeap.add(c[0]);
			} 
			else {
				if (c[0] < maxHeap.peek()) {
					t -= maxHeap.remove();
					t += c[0];
					maxHeap.add(c[0]);
				}
			}
		}
		return maxHeap.size();
	}
	
	// https://leetcode.com/articles/course-schedule-iii/
	public int scheduleCourse2(int[][] courses) {
		Arrays.sort(courses, (a, b) -> a[1] - b[1]);
		PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
		int time = 0;
		for (int[] c : courses) {
			if (time + c[0] <= c[1]) {
				queue.offer(c[0]);
				time += c[0];
			} 
			else if (!queue.isEmpty() && queue.peek() > c[0]) {
				time += c[0] - queue.poll();
				queue.offer(c[0]);
			}
		}
		return queue.size();
	}
	
	/*
	 * https://leetcode.com/problems/course-schedule-iii/discuss/104845/Short-Java-code-using-PriorityQueue
	 * 
	 * First, we sort courses by the end date, this way, when we're iterating through 
	 * the courses, we can switch out any previous course with the current one without 
	 * worrying about end date.
	 * 
	 * Next, we iterate through each course, if we have enough days, we'll add it to 
	 * our PriorityQueue. If we don't have enough days, then we can either ignore this 
	 * course, or we can use it to replace a longer course we added earlier.
	 * 
	 * When we replace a longer course with a much shorter one, does that mean we'll 
	 * have enough room to take some courses previously ignored for being too long?
	 * 
	 * No, because any courses we missed would be longer than what's in PriorityQueue pq. 
	 * So the increase in number of days cannot be larger than the largest element in 
	 * pq, and certainly will be less than a previously ignored course which has to be 
	 * even longer.
	 * 
	 * Rf : https://leetcode.com/problems/course-schedule-iii/discuss/104840/C++-13-lines-With-Explanation
	 */
	public int scheduleCourse3(int[][] courses) {
		// Sort the courses by their deadlines (Greedy! We have to deal with courses 
		// with early deadlines first)
		Arrays.sort(courses, (a, b) -> a[1] - b[1]);
		
		PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
		
		int time = 0;
		for (int[] c : courses) {
			time += c[0]; // add current course to a priority queue
			pq.add(c[0]);
		
			// If time exceeds, drop the previous course which costs the most time. 
			// (That must be the best choice!)
			if (time > c[1])
				time -= pq.poll();
		}
		return pq.size();
	}
	
	/*
	 * by myself
	 * 
	 * Rf: https://leetcode.com/problems/course-schedule-iii/discuss/104852/Simple-Java-Solution
	 */
	public int scheduleCourse_self(int[][] courses) {
        if (courses.length == 0 || courses[0].length == 0)
            return 0;
        
        Arrays.sort(courses, new Comparator<int[]>() {
            public int compare(int[] i1, int[] i2) {
                return i1[1] - i2[1];
            }
        });
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b){
                return b[0] - a[0];
            }
        });
        
        int ans = 1;
        int time = courses[0][0];
        pq.add(courses[0]);
        for (int i = 1; i < courses.length; i++) {
            if (time + courses[i][0] <= courses[i][1]) {
                time += courses[i][0];
                pq.add(courses[i]);
                ans++;
            }
            /*
             * time < courses[i - j][1] < courses[i][1]
             * If courses[i][0] < pq.peek()[0], we can confirm that
             *    time + courses[i][0] - pq.peek()[0] <= courses[i][1]
             */
            else if (courses[i][0] < pq.peek()[0]) {
                time -= pq.poll()[0];
                time += courses[i][0];
                pq.add(courses[i]);
            }
        }
        return ans;
    }
	
	// https://leetcode.com/problems/course-schedule-iii/discuss/104854/A-little-Difficult-just-do-some-optimization-on-a-O(N2)-algorithm

}
