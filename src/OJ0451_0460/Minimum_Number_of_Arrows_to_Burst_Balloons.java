package OJ0451_0460;

import java.util.Arrays;
import java.util.Comparator;

public class Minimum_Number_of_Arrows_to_Burst_Balloons {
	/*
	 * https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/discuss/93703/share-my-explained-greedy-solution
	 * 
	 * Sort the array by their ending position. While we take care of each balloon in 
	 * order, we can shoot as many following balloons as possible.
	 * 
	 * We should shoot as to the right as possible, because since balloons are sorted, 
	 * this gives you the best chance to take down more balloons. Therefore the 
	 * position should always be balloon[i][1] for the ith balloon.
	 */
	public int findMinArrowShots(int[][] points) {
		if (points.length == 0) {
			return 0;
		}
		Arrays.sort(points, (a, b) -> a[1] - b[1]);
		int arrowPos = points[0][1];
		int arrowCnt = 1;
		for (int i = 1; i < points.length; i++) {
			if (arrowPos >= points[i][0]) {
				continue;
			}
			arrowCnt++;
			arrowPos = points[i][1];
		}
		return arrowCnt;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/66579/java-greedy-soution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/72901/a-concise-template-for-overlapping-interval-problem
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/66520/share-my-greedy-solution-simple-and-easy
	 */
	public int findMinArrowShots_start(int[][] points) {
		if (points == null || points.length == 0 || points[0].length == 0)
			return 0;
		Arrays.sort(points, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				if (a[0] == b[0])
					return a[1] - b[1];
				else
					return a[0] - b[0];
			}
		});

		int minArrows = 1;
		int arrowLimit = points[0][1];
		for (int i = 1; i < points.length; i++) {
			int[] baloon = points[i];
			if (baloon[0] <= arrowLimit) {
				arrowLimit = Math.min(arrowLimit, baloon[1]);
			} 
			else {
				minArrows++;
				arrowLimit = baloon[1];
			}
		}
		return minArrows;
	}
	
	public int findMinArrowShots_self(int[][] points) {
		Arrays.sort(points, new Comparator<int[]>() {
			public int compare(int[] o1, int[] o2) {
				return Integer.compare(o1[1], o2[1]);
			}
		});
		int count = 0;
		int i = 0;
		while (i < points.length) {
			int shot = points[i][1];
			int start = i + 1;
			while (start < points.length && points[start][0] <= shot && shot <= points[start][1]) {
				start++;
			}
			i = start;
			count++;
		}
		return count;
	}

}
