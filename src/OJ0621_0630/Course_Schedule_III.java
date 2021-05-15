package OJ0621_0630;

import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

// leetcode.com/problems/course-schedule-iii/discuss/104847/Python-Straightforward-with-Explanation/107619

public class Course_Schedule_III {
	/*
	 * Sort all the courses by their ending time. For each K, we will greedily remove 
	 * the largest-length course until the total duration start is <= end. To select 
	 * these largest-length courses, we will use a max heap. start will maintain the 
	 * loop invariant that it is the sum of the lengths of the courses we have 
	 * currently taken.
	 * 
	 * ---------------------------------------------
	 * 
	 * (a, x), (b, y); y > x
	 * 
	 * 1. a + b > x, a > b, a + b <= y
	 * take order [(a, x), (b, y)]
	 * 2. a + b > x, b > a, a + b <= y
	 * take order [(a, x), (b, y)]
	 * 
	 * It is always profitable to take the course with a smaller end day prior to a 
	 * course with a larger end day. 
	 * 
	 * Based on this idea, firstly, we sort the given courses array based on their 
	 * end days. Then, we try to take the courses in a serial order from this sorted 
	 * courses array.
	 * 
	 * -----------------------------------------------
	 * 
	 * test case: [[3,2],[4,3]]
	 * expect: 0
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/course-schedule-iii/discuss/104855/Java-Solution-using-Array-sort-and-Heap
	 * https://leetcode.com/problems/course-schedule-iii/discuss/104847/Python-Straightforward-with-Explanation
	 * https://leetcode.com/problems/course-schedule-iii/solution/
	 */
	public int scheduleCourse1(int[][] courses) {
		Arrays.sort(courses, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return o1[1] - o2[1];
			}
		});

		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

		int t = 0;
		for (int i = 0; i < courses.length; i++) {
			int[] c = courses[i];
			if (c[0] + t <= c[1]) {
				t += c[0];
				maxHeap.add(c[0]);
			} 
			else {
				if (!maxHeap.isEmpty() && c[0] < maxHeap.peek()) {
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
	
	/*
	 * https://leetcode.com/problems/course-schedule-iii/solution/
	 * Approach 5: Extra List
	 * 
	 * The course with a smaller duration, if can be taken, can surely be taken only 
	 * if it is taken prior to a course with a larger end day.
	 * 
	 * We need to sort the given courses array based on the end days.
	 * 
	 * If we aren't able to take the current course, we can try to take this course 
	 * by removing other course that have already been taken. But, the current course 
	 * can fit in by removing some other course, only if the duration of the course 
	 * jth being removed is larger than the current course's duration.
	 * 
	 * By replacing the jth course, with the ith course of a relatively smaller 
	 * duration, an extra duration_j - duration_i time can be made available.
	 * 
	 * We should search among only those courses which have been taken to find a 
	 * course with the maximum duration which is larger than the current course's 
	 * duration.
	 * 
	 * We can maintain a separate list valid_list which is the list of those courses 
	 * that have been taken till now.
	 * 
	 * To find the max_i course, we need to search in this list only. Further, when 
	 * replacing this max_ith course with the current course, we can replace this 
	 * max_i course in the list with current course directly. 
	 */
	public int scheduleCourse_backward_list(int[][] courses) {
		Arrays.sort(courses, (a, b) -> a[1] - b[1]);
		List<Integer> valid_list = new ArrayList<>();
		int time = 0;
		
		for (int[] c : courses) {
			if (time + c[0] <= c[1]) {
				valid_list.add(c[0]);
				time += c[0];
			} 
			else {
				int max_i = 0;

				for (int i = 1; i < valid_list.size(); i++) {
					if (valid_list.get(i) > valid_list.get(max_i))
						max_i = i;
				}

				if (!valid_list.isEmpty() && valid_list.get(max_i) > c[0]) {
					time += c[0] - valid_list.get(max_i);
					valid_list.set(max_i, c[0]);
				}
			}
		}
		return valid_list.size();
	}

	/*
	 * https://leetcode.com/problems/course-schedule-iii/solution/
	 * Approach 4: Optimized Iterative
	 * 
	 * The course with a smaller duration, if can be taken, can surely be taken only 
	 * if it is taken prior to a course with a larger end day.
	 * 
	 * We need to sort the given courses array based on the end days.
	 * 
	 * If we aren't able to take the current course, we can try to take this course 
	 * by removing other course that have already been taken. But, the current course 
	 * can fit in by removing some other course, only if the duration of the course 
	 * jth being removed is larger than the current course's duration.
	 * 
	 * By replacing the jth course, with the ith course of a relatively smaller 
	 * duration, an extra duration_j - duration_i time can be made available.
	 * 
	 * We need to traverse back in the courses array till the beginning to find a 
	 * course with the maximum duration which is larger than the current course's 
	 * duration. We should search among only those courses which have been taken
	 * 
	 * As we iterate over the courses array, we also keep on updating it, such that 
	 * the first `count` number of elements in this array now correspond to only 
	 * those `count` number of courses which have been taken till now.
	 * 
	 * Thus, whenever we update the `count` to indicate that one more course has been 
	 * taken, we also update the courses[count] entry to reflect the current course 
	 * that has just been taken.
	 * 
	 * Whenever, we find a course for which time + duration_i > endDay_i, we find a 
	 * max_i course from only amongst these first `count` number of courses in the 
	 * courses array, which indicate the courses that have been taken till now.
	 * 
	 * Also, instead of marking this max_ith course with a -1, we can simply replace 
	 * this course with the current course. Thus, the first `count` courses still 
	 * reflect the courses that have been taken till now.
	 */
	public int scheduleCourse_backward_scan2(int[][] courses) {
        System.out.println(courses.length);
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        int time = 0, count = 0;
        for (int i = 0; i < courses.length; i++) {
            if (time + courses[i][0] <= courses[i][1]) {
                time += courses[i][0];
                courses[count++] = courses[i];
            } 
            else {
                int max_i = i;
                
                for (int j = 0; j < count; j++) {
                    if (courses[j][0] > courses[max_i][0])
                        max_i = j;
                }
                
                if (courses[max_i][0] > courses[i][0]) {
                    time += courses[i][0] - courses[max_i][0];
                    courses[max_i] = courses[i];
                }
            }
        }
        return count;
    }
	
	/*
	 * https://leetcode.com/problems/course-schedule-iii/solution/
	 * Approach 3: Iterative Solution
	 * 
	 * The course with a smaller duration, if can be taken, can surely be taken only 
	 * if it is taken prior to a course with a larger end day.
	 * 
	 * We need to sort the given courses array based on the end days.
	 * 
	 * For each course being considered currently (let's say ith course), we try to 
	 * take this course. 
	 * If we aren't able to take the current course, we can try to take this course 
	 * by removing some other course from amongst the courses that have already been 
	 * taken. But, the current course can fit in by removing some other course, only 
	 * if the duration of the course jth being removed is larger than the current 
	 * course's duration.
	 * 
	 * By replacing the jth course, with the ith course of a relatively smaller 
	 * duration, we can increase the time available for upcoming courses to be taken. 
	 * An extra duration_j - duration_i time can be made available by doing so.
	 * 
	 * Now, for this saving in time to be maximum, the course taken for the 
	 * replacement should be the one with the maximum duration. Thus, from amongst 
	 * the courses that have been taken till now, we find the course having the 
	 * maximum duration (max_i) which should be more than the duration of the 
	 * current course (which can't be taken).
	 * 
	 * If such a course, max_i, is found, we remove this course from the taken 
	 * courses and consider the current course as taken. We also mark this course 
	 * with -1 to indicate that this course has not been taken and should not be 
	 * considered in the future again for replacement.
	 * 
	 * But, if such a course isn't found, we can't take the current course at any 
	 * cost. Thus, we mark the current course with -1 to indicate that the current 
	 * course has not been taken.
	 * 
	 * At the end, the value of count gives the required result.
	 */
	public int scheduleCourse_backward_scan(int[][] courses) {
        System.out.println(courses.length);
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        int time = 0, count = 0;
        for (int i = 0; i < courses.length; i++) {
            if (time + courses[i][0] <= courses[i][1]) {
                time += courses[i][0];
                count++;
            } 
            else {
                int max_i = i;
                
                for (int j = 0; j < i; j++) {
                    if (courses[j][0] > courses[max_i][0])
                        max_i = j;
                }
                
                if (courses[max_i][0] > courses[i][0]) {
                    time += courses[i][0] - courses[max_i][0];
                }
                courses[max_i][0] = -1;
            }
        }
        return count;
    }
	
	// https://leetcode.com/problems/course-schedule-iii/discuss/104854/A-little-Difficult-just-do-some-optimization-on-a-O(N2)-algorithm

}
