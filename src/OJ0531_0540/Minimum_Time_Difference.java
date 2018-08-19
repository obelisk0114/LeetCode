package OJ0531_0540;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Minimum_Time_Difference {
	/*
	 * https://leetcode.com/problems/minimum-time-difference/discuss/100640/Verbose-Java-Solution-Bucket
	 * 
	 * Rf : https://leetcode.com/problems/minimum-time-difference/discuss/135416/Java-Bucket-Sort-beats-99.78-(detailed-explanation)
	 */
	public int findMinDifference(List<String> timePoints) {
		boolean[] mark = new boolean[24 * 60];
		for (String time : timePoints) {
			String[] t = time.split(":");
			int h = Integer.parseInt(t[0]);  // h = (time.charAt(0) - '0') * 10 + (time.charAt(1) - '0');
			int m = Integer.parseInt(t[1]);  // m = (time.charAt(3) - '0') * 10 + (time.charAt(4) - '0');
			if (mark[h * 60 + m])
				return 0;
			
			mark[h * 60 + m] = true;
		}

		int prev = 0, min = Integer.MAX_VALUE;
		int first = Integer.MAX_VALUE;
		for (int i = 0; i < 24 * 60; i++) {
			if (mark[i]) {
				if (first == Integer.MAX_VALUE) {
					first = i;
					prev = i;
					continue;
				}
				
				min = Math.min(min, i - prev);
				prev = i;
			}
		}

		min = Math.min(min, (24 * 60 - prev + first));
		return min;
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-time-difference/discuss/100644/Java-O(nlog(n))O(n)-Time-O(1)-Space-Solutions
	 */
	public int findMinDifference_self(List<String> timePoints) {
        int[] minute = new int[1440];
        for (String points : timePoints) {
            String[] time = points.split(":");
            int cur = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
            minute[cur]++;
        }
        
        int first = -1;
        int pre = -1;
        int min = 1441;
        for (int i = 0; i < minute.length; i++) {
            if (minute[i] == 0)
                continue;
            else if (minute[i] > 1)
                return 0;
            
            if (first == -1) {
                first = i;
                pre = i;
                continue;
            }
            
            min = Math.min(min, i - pre);
            pre = i;
        }
        return Math.min(min, first + 1440 - pre);
    }
	
	/*
	 * https://leetcode.com/problems/minimum-time-difference/discuss/100636/Java-10-liner-solution.-Simplest-so-far
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-time-difference/discuss/155216/539.-Minimum-Time-Difference-in-C++-and-Java-and-Python
	 */
	public int findMinDifference_sort(List<String> timePoints) {
		int mm = Integer.MAX_VALUE;
		List<Integer> time = new ArrayList<>();

		for (int i = 0; i < timePoints.size(); i++) {
			Integer h = Integer.valueOf(timePoints.get(i).substring(0, 2));
			time.add(60 * h + Integer.valueOf(timePoints.get(i).substring(3, 5)));
		}

		Collections.sort(time, (Integer a, Integer b) -> a - b);

		for (int i = 1; i < time.size(); i++) {
			mm = Math.min(mm, time.get(i) - time.get(i - 1));
		}

		int corner = time.get(0) + (1440 - time.get(time.size() - 1));
		return Math.min(mm, corner);
	}
	
	// https://leetcode.com/problems/minimum-time-difference/discuss/130689/Java-8-solution

}
