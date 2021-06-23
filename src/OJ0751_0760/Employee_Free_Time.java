package OJ0751_0760;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.PriorityQueue;

public class Employee_Free_Time {
	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/195308/Java-PriorityQueue-Solution-Time-complexity-O(N-log-K)
	 * 
	 * Because each person's time schedule is already sorted, we can use that info 
	 * and optimize the algorithm by introducing a PriorityQueue storing a pointer to 
	 * the person's schedule. Then the overall time complexity can be reduced to 
	 * min(O(n log K), where K is the total number of people, n is the total number 
	 * of intervals.
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * It only needs to compare first node of each list, to get the minimum. Once a 
	 * node polled from priority queue, we want to add node.next to priority queue.
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * After intervals added into the heap, they would be sorted in the order of 
	 * start time. And the interval's start time should be expressed as 
	 * schedule.get(employee).get(interval).start. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/199830
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/818465
	 * https://leetcode.com/problems/employee-free-time/discuss/195308/Java-PriorityQueue-Solution-Time-complexity-O(N-log-K)/575406
	 * https://leetcode.com/problems/employee-free-time/discuss/195308/Java-PriorityQueue-Solution-Time-complexity-O(N-log-K)/820511
	 * 
	 * Other code:
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/876636
	 * https://leetcode.com/problems/employee-free-time/discuss/195308/Java-PriorityQueue-Solution-Time-complexity-O(N-log-K)/932576
	 */
	public List<Interval> employeeFreeTime_pq(List<List<Interval>> schedule) {
		PriorityQueue<int[]> pq = new PriorityQueue<>(
			(a, b) -> 
			schedule.get(a[0]).get(a[1]).start - schedule.get(b[0]).get(b[1]).start);
		
		for (int i = 0; i < schedule.size(); i++) {
			pq.add(new int[] { i, 0 });
		}
		
		List<Interval> res = new ArrayList<>();
		int prev = schedule.get(pq.peek()[0]).get(pq.peek()[1]).end;
		
		while (!pq.isEmpty()) {
			int[] index = pq.poll();
			Interval interval = schedule.get(index[0]).get(index[1]);
			
			if (interval.start > prev) {
				res.add(new Interval(prev, interval.start));
			}
			
			prev = Math.max(prev, interval.end);
			
			if (schedule.get(index[0]).size() > index[1] + 1) {
				pq.add(new int[] { index[0], index[1] + 1 });
			}
		}
		return res;
	}

	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/113121/O(n-*-lg(n))-short-java-solution
	 */
	public List<Interval> employeeFreeTime_addAll(List<List<Interval>> avails) {
		List<Interval> ans = new ArrayList<>(), all = new ArrayList<>();
		for (List<Interval> list : avails)
			all.addAll(list);

		Collections.sort(all, (a, b) -> (a.start - b.start));
		
		int end = all.get(0).end;
		for (Interval x : all) {
			if (x.start > end) {				
				ans.add(new Interval(end, x.start));
			}
			
			end = Math.max(end, x.end);
		}
		return ans;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/employee-free-time/discuss/113122/Merge-Sort-O(nlgK)-(Java)
	 * 
	 * use the property intervals are sorted for each employee.
	 * using merge sort, the time complexity will be n*lg(k), k is the number of 
	 * employees.
	 */
	public List<Interval> employeeFreeTime_merge(List<List<Interval>> schedule) {
		int n = schedule.size();
		List<Interval> time = mergeSort_merge(schedule, 0, n - 1);
		
		List<Interval> free = new ArrayList<>();
		int end = time.get(0).end;
		
		for (int i = 1; i < time.size(); i++) {
			if (time.get(i).start > end) {
				free.add(new Interval(end, time.get(i).start));
			}
			
			end = Math.max(end, time.get(i).end);
		}
		return free;
	}

	private List<Interval> mergeSort_merge(List<List<Interval>> schedule, 
			int l, int r) {
		
		if (l == r)
			return schedule.get(l);
		
		int mid = (l + r) / 2;
		
		List<Interval> left = mergeSort_merge(schedule, l, mid);
		List<Interval> right = mergeSort_merge(schedule, mid + 1, r);
		
		return merge_merge(left, right);
	}

	private List<Interval> merge_merge(List<Interval> A, List<Interval> B) {
		List<Interval> res = new ArrayList<>();
		
		int m = A.size(), n = B.size();
		int i = 0, j = 0;
		
		while (i < m || j < n) {
			if (i == m) {
				res.add(B.get(j++));
			} 
			else if (j == n) {
				res.add(A.get(i++));
			} 
			else if (A.get(i).start < B.get(j).start) {
				res.add(A.get(i++));
			} 
			else {				
				res.add(B.get(j++));
			}
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList
	 * 
	 * it is not matter how many different people are there for the algorithm. 
	 * because we just need to find a gap in the time line.
	 * 
	 * 1. sorted by start time, and for same start time sort by either largest end 
	 *    time or smallest (it is not matter).
	 * 2. make sure it doesn't intersect with previous interval.
	 *    This mean that there is no common interval. Everyone is free time.
	 * 
	 * The time complexity of the sort algorithm is O(nlogn) where n is the total 
	 * number of intervals.
	 * Because each person's time schedule is already sorted, we can use that info 
	 * and optimize the algorithm by introducing a PriorityQueue storing a pointer to 
	 * the person's schedule. Then the overall time complexity can be reduced to 
	 * min(O(n log K), where K is the total number of people.
	 * 
	 * List<Interval> sch = schedule
	 *                 .stream()
	 *                .flatMap(List::stream)
	 *                .sorted(Comparator.comparingInt(o -> o.start))
	 *                .collect(Collectors.toList());
	 * 
	 * Rf :
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/199830
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/383583
	 */
	public List<Interval> employeeFreeTime_sortAll(List<List<Interval>> avails) {
		List<Interval> result = new ArrayList<>();

		List<Interval> timeLine = new ArrayList<>();
		avails.forEach(e -> timeLine.addAll(e));

		Collections.sort(timeLine, ((a, b) -> a.start - b.start));

		Interval temp = timeLine.get(0);
		for (Interval each : timeLine) {
			if (temp.end < each.start) {
				result.add(new Interval(temp.end, each.start));
				temp = each;
			} 
			else {
				temp = temp.end < each.end ? each : temp;
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList
	 * 
	 * The idea is to just add all the intervals to the priority queue. (NOTE that 
	 * it is not matter how many different people are there for the algorithm. 
	 * because we just need to find a gap in the time line.
	 * 
	 * 1. priority queue - sorted by start time, and for same start time sort by 
	 *                     either largest end time or smallest (it is not matter).
	 * 2. Everytime you poll from priority queue, just make sure it doesn't intersect 
	 *    with previous interval.
	 *    This mean that there is no common interval. Everyone is free time.
	 * 
	 * The time complexity of the sort algorithm is O(nlogn) where n is the total 
	 * number of intervals.
	 * Because each person's time schedule is already sorted, we can use that info 
	 * and optimize the algorithm by introducing a PriorityQueue storing a pointer to 
	 * the person's schedule. Then the overall time complexity can be reduced to 
	 * min(O(n log K), where K is the total number of people.
	 * 
	 * List<Interval> sch = schedule
	 *                 .stream()
	 *                .flatMap(List::stream)
	 *                .sorted(Comparator.comparingInt(o -> o.start))
	 *                .collect(Collectors.toList());
	 * 
	 * Rf :
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/199830
	 * 
	 * Other code:
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/114886
	 * https://leetcode.com/problems/employee-free-time/discuss/113134/Simple-Java-Sort-Solution-Using-(Priority-Queue)-or-Just-ArrayList/383583
	 */
	public List<Interval> employeeFreeTime_pq2(List<List<Interval>> avails) {
		List<Interval> result = new ArrayList<>();

		PriorityQueue<Interval> pq = new PriorityQueue<>((a, b) -> a.start - b.start);
		avails.forEach(e -> pq.addAll(e));

		Interval temp = pq.poll();
		while (!pq.isEmpty()) {
			// no intersect
			if (temp.end < pq.peek().start) {
				result.add(new Interval(temp.end, pq.peek().start));
				
				// becomes the next temp interval
				temp = pq.poll();
			}
			// intersect or sub merged
			else {
				temp = temp.end < pq.peek().end ? pq.peek() : temp;
				pq.poll();
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/650527/Java-PriorityQueue-loud-and-clear
	 * 
	 * We don't need to care which interval belongs to which one. Just put them all 
	 * in one PQ, and merge the overlapped ones. Finally get the free period for 
	 * everybody.
	 * 
	 * we could remove this line:
	 * cur.start = Math.min(pre.start, cur.start);
	 * As we only care about prev.end
	 * 
	 * Rf :
	 * https://leetcode.com/problems/employee-free-time/discuss/650527/Java-PriorityQueue-loud-and-clear/740238
	 */
	public List<Interval> employeeFreeTime_pq3(List<List<Interval>> schedule) {
		List<Interval> res = new ArrayList<>();
		
		// (a, b) -> a.start - b.start 就可以
		PriorityQueue<Interval> pq = new PriorityQueue<>(
				(a, b) -> a.start != b.start ? a.start - b.start : a.end - b.end);

		for (List<Interval> intervals : schedule) {
			for (Interval interval : intervals) {
				pq.offer(interval);
			}
		}

		if (pq.isEmpty())
			return res;

		Interval pre = pq.poll();

		while (!pq.isEmpty()) {
			Interval cur = pq.poll();
			
			if (pre.end < cur.start) {
				res.add(new Interval(pre.end, cur.start));
			} 
			else {
				cur.start = Math.min(pre.start, cur.start);
				cur.end = Math.max(pre.end, cur.end);
			}
			
			pre = cur;
		}

		return res;
	}

	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/175081/Sweep-line-Java-with-Explanations
	 * 
	 * Most interval-related problems can be solved by the Sweep-line algorithm.
	 * 
	 * Free time is a period of time such that no employees scheduled to work, 
	 * i.e. the gap between every two non-overlapping intervals. If we meet a start 
	 * of interval, score++; or else(if we meet an end of interval), score--. 
	 * Non-overlapping intervals exist when score equals to 0.
	 * We add current gap interval to the result list when score is not 0 any more.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/employee-free-time/discuss/175081/Sweep-line-Java-with-Explanations/562142
	 */
	public List<Interval> employeeFreeTime_sweepLine(List<List<Interval>> schedule) {
		List<Interval> result = new ArrayList<>();

		// Key: time point, value: score.
		Map<Integer, Integer> map = new TreeMap<>();
		for (List<Interval> list : schedule) {
			for (Interval interval : list) {
				map.put(interval.start, map.getOrDefault(interval.start, 0) + 1);
				map.put(interval.end, map.getOrDefault(interval.end, 0) - 1);
			}
		}

		int start = -1, score = 0;
		for (int point : map.keySet()) {
			score += map.get(point);
			
			// start == -1 可以省略
			if (score == 0 && start == -1) {
				start = point;
			} 
			// score != 0 可以省略
			else if (start != -1 && score != 0) {
				result.add(new Interval(start, point));
				start = -1;
			}
		}

		return result;
    }
	
	/*
	 * https://leetcode.com/problems/employee-free-time/discuss/117564/Java-Simple-solution-sort-start-and-end-time-separately.
	 * 
	 * The main idea is after sorting start and end time list, there are gaps when 
	 * starts.get(i + 1) > ends.get(i).
	 */
	public List<Interval> employeeFreeTime_separate(List<List<Interval>> schedule) {
        int n = schedule.size();
        
        List<Interval> ret = new ArrayList<>();
        if (n == 0)
            return ret;
        
        List<Integer> starts = new ArrayList<Integer>();
        List<Integer> ends = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            List<Interval> employee = schedule.get(i);
            for (Interval it : employee) {
                starts.add(it.start);
                ends.add(it.end);
            }
        }
        
        Collections.sort(starts);
        Collections.sort(ends);
        
        n = starts.size();
        for (int i = 0; i < n - 1; i++) {
            if (starts.get(i + 1) > ends.get(i)) {
                ret.add(new Interval(ends.get(i), starts.get(i + 1)));
            }
        }
        
        return ret;
    }
	
	/*
	 * by myself
	 * 
	 * Merge intervals
	 * 
	 * Rf :
	 * https://leetcode.com/problems/merge-intervals/discuss/21560/fast-ana-simple-java-code
	 */
	public List<Interval> employeeFreeTime_self(List<List<Interval>> schedule) {
        List<Interval> times = new ArrayList<>();
        for (List<Interval> schs : schedule) {
            for (Interval sch : schs) {
                times.add(sch);
            }
        }
        
        Collections.sort(times, (a, b) -> a.start - b.start);
        
        List<Interval> busy = new ArrayList<>();
        Interval curr = times.get(0);
        for (Interval i : times) {
            if (curr.end >= i.start) {
                curr.end = Math.max(curr.end, i.end);
            }
            else {
                busy.add(curr);
                curr = i;
            }
        }
        busy.add(curr);
        
        List<Interval> res = new ArrayList<>();
        curr = busy.get(0);
        for (int i = 1; i < busy.size(); i++) {
            if (busy.get(i).start > curr.end) {
                Interval free = new Interval(curr.end, busy.get(i).start);
                res.add(free);
            }
            
            curr = busy.get(i);
        }
        
        return res;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/employee-free-time/discuss/170551/Simple-Python-9-liner-beats-97-(with-explanation)
     * https://leetcode.com/problems/employee-free-time/discuss/767485/Easy-Python-Beats-95-with-Comments-and-Explanation!
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/employee-free-time/discuss/113127/C%2B%2B-Clean-Code
     */

}

class Interval {
    public int start;
    public int end;

    public Interval() {}

    public Interval(int _start, int _end) {
        start = _start;
        end = _end;
    }
}
