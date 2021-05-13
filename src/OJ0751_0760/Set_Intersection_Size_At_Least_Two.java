package OJ0751_0760;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;

/*
 * �N 2D intervals array �H end �Ѥp��j�Ƨ�
 * �Y end �ۦP�A�h�� start �Ѥj��p�Ƨ�
 * ��� 2 �ӼƦr (small, large) �@���Q���X���Ʀr�A�өM�U�@�� interval �����
 * 1. small >= interval.start
 *    ��ܳo 2 �ӼƦr���b�U�@�� interval ���A�����B�z
 * 2. large >= interval.start && small < interval.start
 *    ��ܥu�� large �b�U�@�� interval ���A�N�{�b�� large �אּ small
 *    �U�@�� interval �� end ���� large�A���X�ƶq + 1
 * 3. �Y���O�H�W���p�A��ܨå����ͭ��|
 *    small = interval.end - 1 �M large = interval.end
 * 
 * �]���n��X overlap �̦h���Ʀr�Ӻɶq���C intersection size
 * �Ѥp��j�ƧǡA�b�e�� interval �̫᭱���Ʀr (end-1, end) �̦��i��M�᭱ interval ���� overlap
 * �]���ڭ̥i�H�ѿ�X�� 2 �ӼƦr (small, large) �M�U�@�� interval �� start �����
 * �Y���p���Ʀr small �]��U�@�� interval �� start �j�A��ܳo 2 �ӼƦr���M�U�@�� interval �����| 
 * 
 * �]���ƧǹL�A�Y�O��e interval �̤j���Ʀr (end) ���b��@�� interval ��
 * ��e interval ����L�Ʀr�]���|�b��@�� interval ��
 * 
 * ���F�קK��e interval �� end �W�L�U�@�� interval �� end�A�Ӳ��Ϳ��~���������
 * �ҥH��ܱN end �Ѥp��j�Ƨ�
 * 
 * ex: [[1,5],[4,5],[5,99],[7,9],[9,10]]
 * �Y start �Ѥp��j�ƧǡA
 * �H small �M large ��ܷ�e�n�M�U�@�� interval ��諸�Ʀr
 * [1,5]  => small: 4, large: 5, size: 2
 * [4,5]  => small: 4, large: 5, size: 2
 * [5,99] => small: 5, large: 99, size: 3
 * [7,9]  => small: 99, large: 9, size: 4 �A������ǥX�{���D
 * [9,10] => small: 99, large: 9, size: 4 �A���~���G�A�������� 5
 * 
 * �Y end �ۦP�A�h�u���B�z���u�� interval
 * �קK�b�M�U�@�� interval �� start ����ɡA���ͨ���ۦP�Ʀr�����p
 * 
 * �Ҽ{ [a1, b], [a2, b], [a3, b], [a4, b]. a1 < a2 < a3 < a4. �}�l�e small = c, large = d
 * �B�z [a1, b], �Y (small) c < a1 <= (large) d < b
 * ��s�� small = d, large = b
 * �B�z [a2, b], �Y d < a2
 * (small) d < a2 < (large) b
 * ��s�� small = b, large = b, ���ͭ��ƿ��, a3 �}�l�X�{�֨�
 * 
 * �]���C���u�N end ���J�A�J��ۦP end ���O���P start �ɡA�|���ƿ�� end
 * ��o�� array �W�L 2 �ӡA�o�ӭ��ƿ���� end �N�|�]�� start <= small �����B�z�ӾɭP�֨� 1 �ӼƦr
 * 
 * �]�� start �Ѥj��p�Ƨ�
 * �ӥB���B�z�����u�� interval�A�᭱������ interval �N�|�۰ʳB�z��
 * 
 * ex: [[1,5],[4,5],[5,9],[7,9],[9,10]]
 * �Y end �Ѥp��j�ƧǡAend �ۦP�h�� start �Ѥp��j�ƧǡA
 * �H small �M large ��ܷ�e�n�M�U�@�� interval ��諸�Ʀr
 * [1,5]  => small: 4, large: 5, size: 2
 * [4,5]  => small: 4, large: 5, size: 2
 * [5,9]  => small: 5, large: 9, size: 3
 * [7,9]  => small: 9, large: 9, size: 4 �A����ۦP���Ʀr
 * [9,10] => small: 9, large: 9, size: 4 �A���~���G�A�������� 5
 */

public class Set_Intersection_Size_At_Least_Two {
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113086/Hope-you-enjoy-this-problem.-%3A-)-O(NlogN)JavaGreedy-Easy-to-understand-solution
	 * 
	 * �]���n��X overlap �̦h���Ʀr�Ӻɶq���C intersection size
	 * �Ѥp��j�ƧǡA�b�e�� interval �̫᭱���Ʀr (end-1, end) �̦��i��M�᭱ interval ���� overlap
	 * �]���ڭ̥i�H�ѿ�X�� 2 �ӼƦr (small, large) �M�U�@�� interval �� start �����
	 * �Y���p���Ʀr small >= interval.start�A��ܳo 2 �ӼƦr���M�U�@�� interval �����|
	 *  
	 * �]���ƧǹL�A�Y�O��e interval �̤j���Ʀr (end) ���b��@�� interval ��
	 * ��e interval ����L�Ʀr�]���|�b��@�� interval ��
	 * 
	 * ���F�קK��e interval �� end �W�L�U�@�� interval �� end�A�Ӳ��Ϳ��~���������
	 * �ҥH��ܱN end �Ѥp��j�Ƨ�
	 * 
	 * �Y end �ۦP�A�h�u���B�z���u�� interval
	 * �קK�b�M�U�@�� interval �� start ����ɡA���ͨ���ۦP�Ʀr�����p
	 * �]�� start �Ѥj��p�Ƨ�
	 * �ӥB���B�z�����u�� interval�A�᭱������ interval �N�|�۰ʳB�z��
	 * 
	 * 1. Sort the array according to their end point in ascending order, AND if two 
	 *    intervals have same end, sort them according to their start point in 
	 *    descending order. 
	 *    e.g [[1,5], [4,5], [5,9], [7,9], [9,10]] 
	 *    => [[4,5], [1,5], [7,9], [5,9], [9,10]]
	 * 2. Greedy to get the rightmost two point
	 */
	public int intersectionSizeTwo_sort_end_asc_start_dsc(int[][] intervals) {
		int res = 0;
		if (intervals == null || intervals.length == 0) {
			return res;
		}

		Arrays.sort(intervals, (a, b) -> a[1] != b[1] ? a[1] - b[1] : b[0] - a[0]);

        // known two rightmost point in the set/res
        int left = intervals[0][1] - 1;
        int right = intervals[0][1];
        res += 2;
        
        for (int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
            
            // 1. one element of the set is in the interval
            // 2. no element of the set is in the interval
            if (left < curr[0] && curr[0] <= right) {
                res++;
                left = right;
                right = curr[1];
            } 
            else if (curr[0] > right) {
                res += 2;
                left = curr[1] - 1;
                right = curr[1];
            }
        }
        
        return res;
    }
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113076/Java-O(nlogn)-Solution-Greedy/114191
	 * 
	 * �ϥ� end �Ѥp��j�ƧǡA�o�˧ڭ̿���e�� interval �� end <= �᭱ interval �� end
	 * �]���ѫe����A�����H�U���p
	 * 1. start > max1�A��ܳo�� interval �S���Ʀr�Q�]�t�A�� end �M end - 1 �ç�s max1, max2
	 * 2. start > max2�A��ܳo�� interval �� 1 �ӼƦr�Q�]�t
	 * 
	 * 2.1 max1 = end�A��ܤ��e���@�� interval_p
	 * interval_p.end = interval.end && interval_p.start < interval.start
	 * �� interval_p.start > ��ɪ� max2�A�ӥu���N interval_p.end ���J
	 * �S�]�� interval.start < interval.end = interval_p.end = max1
	 * �ҥH���ŦX���� 1
	 * 
	 * �Y�e���� interval_p2 �O
	 * interval_p2.end = interval.end && interval_p2.start > interval.start
	 * �h interval_p2 �O interval ���l�϶�
	 * �Y interval_p2 �w�g�� 2 �ӼƦr�Q����A�o 2 �ӼƦr���w�b interval ��
	 * �ӥB max2 >= interval_p2.start > interval.start
	 * ���b�o�ӱ��� 2 
	 * 
	 * ���F�קK���ƿ��o�ӬۦP�� interval.end�A�ڭ̧�� interval.end - 1  �ç�s max1, max2
	 * WLOG, �Ҽ{�϶� [a1, b], [a2, b], ..., [an, b]
	 * �L�� a1, a2, ..., an �����j�p���Y�p��Ab �M b - 1 ���w�s�b�Ҧ��϶�
	 * �ҥH�ڭ̿� b �M b - 1 �@�� max1, max2 ���P�ɡA�]�O�ҩҦ��϶����� 2 �ӼƦr�Q���
	 * 
	 * 2.2 max1 != end�A��ܳo�� interval.end �å��Q����A��� interval.end �ç�s max1, max2
	 * 3. ��l�����p��ܳo�� interval �w�g�� 2 �ӼƦr�Q����A�~��U�@�� interval
	 * 
	 *  [ [1, 2], [2, 4], [3, 4], [4, 5] ],
	 *  after [1, 2]: max1 = 2, max2 = 1;
	 *  after [2, 4]: max1 = 4, max2 = 2;
	 *  when dealing with [3, 4], max1 = interval[1] = 4.
	 *  interval[1] is the endpoint of the interval.
	 * 
	 * The two maximum numbers in the interval count when we solve the problem in 
	 * ascending order. If there are some numbers overlapping with the rest of the 
	 * intervals, the maximum ones can't be worse than the minimum ones
	 * 
	 * Rf :
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113076/Java-O(nlogn)-Solution-Greedy/114186
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113076/Java-O(nlogn)-Solution-Greedy/120224
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113076/Java-O(nlogn)-Solution-Greedy/114188
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!/931121
	 */
	public int intersectionSizeTwo_sort_end2(int[][] intervals) {
		Arrays.sort(intervals, 
				(interval1, interval2) -> (interval1[1] - interval2[1]));
		
		int max1 = -1, max2 = -1, ans = 0;
		for (int[] interval : intervals) {
			int start = interval[0], end = interval[1];
			
			if (start > max1) {
				ans += 2;
				max2 = end - 1;
				max1 = end;
			} 
			else if (start > max2) {
				ans++;
				
				// �קK���ƿ���ۦP�Ʀr
				max2 = max1 == end ? max1 - 1 : max1;
				max1 = end;
			}
		}
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/767649/Java-O(nlogn)-greedy-fast-solution-sort-the-intervals-beforehand
	 * 
	 * �Ѧҳ̤W�誺����
	 * 
	 * Rf :
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!
	 */
	public int intersectionSizeTwo_sort_end_asc_start_dsc3(int[][] intervals) {
		Arrays.sort(intervals, (a, b) -> a[1] == b[1] ? b[0] - a[0] : a[1] - b[1]);

		int count = 0;
		int first = Integer.MIN_VALUE;
		int second = Integer.MIN_VALUE;

		for (int i = 0; i < intervals.length; i++) {
			int[] interval = intervals[i];

			if (first >= interval[0]) {
				continue;
			} 
			else if (second >= interval[0]) {
				first = second;
				second = interval[1];
				count += 1;
			} 
			else {
				first = interval[1] - 1;
				second = interval[1];
				count += 2;
			}
		}

		return count;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113076/Java-O(nlogn)-Solution-Greedy
	 * 
	 * �Y�O���϶������Q�t�@�϶��]�t�A�ڭ̥i�H�˱���j���϶�
	 * 
	 * �����A�ڭ̥H start �Ѥp��j�ƧǡA�Y start �ۦP�A�h�� end �Ѥj��p�Ƨ�
	 * �A�H stack �˱���j���϶�
	 * �]�����H start �ƧǡA�ҥH stack �V������ interval �� start �V�p�A�]���ڭ̥u�ݦҼ{ end �Y�i
	 * 
	 * �Ҧ� interval �ŦX
	 * interval.start >= stack.top.start
	 * 
	 * �{�b�B�z interval �Y�ŦX
	 * interval.end   <= stack.top.end
	 * 
	 * ��� interval �O stack.top ���l�϶��A�ڭ̥i�H�˱� stack.top
	 * interval ���ۤ���s�� stack.top
	 * 
	 * �B�z���Ҧ��� interval ��A�b stack �������Y�����������]�t�� interval
	 * 
	 * �ڭ̥i�H����e interval �� end �M end - 1�A�]���L�̬O�̦��i��Q�᭱ interval �]�t���Ʀr
	 * �Y�U�@�� interval �w�g�]�t 1 �ӼƦr�A�ڭ̥u�ݿ�� end �Y�i
	 * 
	 * First sort the intervals, with their starting points from low to high
	 * Then use a stack to eliminate the intervals which fully overlap another 
	 * interval.
	 * For example, if we have [2,9] and [1,10], we can get rid of [1,10]. Because as 
	 * long as we pick up two numbers in [2,9], the requirement for [1,10] can be 
	 * achieved automatically.
	 * 
	 * Finally we deal with the sorted intervals one by one.
	 * (1) If there is no number in this interval being chosen before, we pick up 
	 *     2 biggest number in this interval. (the biggest number have the most 
	 *     possibility to be used by next interval)
	 * (2) If there is one number in this interval being chosen before, we pick up 
	 *     the biggest number in this interval.
	 * (3) If there are already two numbers in this interval being chosen before, 
	 *     we can skip this interval since the requirement has been fulfilled.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/727840/Java-Greedy
	 */
	public int intersectionSizeTwo_sort_start_asc_end_dsc(int[][] intervals) {
		Arrays.sort(intervals, (a, b) -> ((a[0] == b[0]) ? 
				(b[1] - a[1]) : (a[0] - b[0])));
		
		Stack<int[]> st = new Stack<>();
		
		for (int[] in : intervals) {
			while (!st.isEmpty() && st.peek()[1] >= in[1])
				st.pop();
			
			st.push(in);
		}
		
		int n = st.size();
		
		int[][] nonOverlap = new int[n][2];
		for (int i = n - 1; i >= 0; i--) {
			nonOverlap[i][0] = st.peek()[0];
			nonOverlap[i][1] = st.pop()[1];
		}
		
		int ans = 2;
		int small = nonOverlap[0][1] - 1, large = nonOverlap[0][1];
		
		for (int i = 1; i < n; i++) {
			boolean smallInside = 
						(small >= nonOverlap[i][0] && small <= nonOverlap[i][1]), 
					largeInside = 
						(large >= nonOverlap[i][0] && large <= nonOverlap[i][1]);
			
			if (smallInside && largeInside)
				continue;
			
			if (largeInside) {
				small = large;
				large = nonOverlap[i][1];
				ans++;
			}
			else {				
				small = nonOverlap[i][1] - 1;
				large = nonOverlap[i][1];
				ans += 2;
			}
		}
		
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!
	 * 
	 * an intersection set for a given array (containing intervals) as a set S such 
	 * that for every interval A in the given array, the intersection of S with A 
	 * has size at least 2.
	 * 
	 * of all the intersection sets for a given array, let m be the minimum value of 
	 * their sizes, then any one of the intersection sets with size m will be 
	 * referred to as a minimum intersection set.
	 * 
	 * let intervals be the input array with length n; intervals[0, i] denote the 
	 * subarray containing intervals of indices from 0 up to i; S_i be the minimum 
	 * intersection set for the subarray intervals[0, i]; m_i be the size of S_i.
	 * 
	 * we will sort them with ascending end points, and if two intervals have the 
	 * same end points, the one with larger start point will come first (we want to 
	 * process the shorter interval before the longer one).
	 * 
	 * all elements in S_(i-1) will be no greater than the end point of the interval 
	 * intervals[i]. This is because every element e in S_(i-1) will intersect with 
	 * at least one interval in the subarray intervals[0, i-1] (otherwise, we can 
	 * remove e to make S_(i-1) smaller without violating the intersection 
	 * requirement). Therefore we only need to check the largest two elements 
	 * (denoted as largest and second) in S_(i-1) to see if they intersect with the 
	 * interval intervals[i]. This is because if they don't, other elements won't 
	 * either. Note this also implies that of all the minimum intersection sets for 
	 * the subarray intervals[0, i-1], we will choose S_(i-1) to be the one with its 
	 * largest two elements maximized. That is, for the subarray intervals[0, i-1], 
	 * we not only minimize the size of the intersection set, but also maximize its 
	 * largest two elements (after the size is minimized).
	 * 
	 * Checking if the largest two elements intersect with interval[i] is equivalent 
	 * to comparing them with the start point of interval[i].
	 * 
	 * Case 1: both elements intersect with intervals[i]. For this case, we show 
	 *         m_i = m_(i-1), and no updates are needed for the largest two elements 
	 *         of S_i.
	 * Case 2: only the largest element intersects with intervals[i]. For this case, 
	 *         we show m_i = 1 + m_(i-1), and the largest two elements of S_i need to 
	 *         be updated. S_i can be constructed by simply adding the end point of 
	 *         intervals[i] to S_(i-1).
	 * Case 3: neither of them intersects with intervals[i]. For this case, we show 
	 *         m_i = 2 + m_(i-1), and the largest two elements of S_i need to be 
	 *         updated. S_i can be constructed by simply adding the end point and the 
	 *         point immediately before the end point of intervals[i] to S_(i-1).
	 * 
	 * -------------------------------------------------------------
	 * 
	 * [[1,3],[3,7],[5,7],[7,8]]
	 * 
	 * Suppose we process the intervals in the given order: after processing [1,3], 
	 * we have m = 2 and largest = 3, second = 2; after processing [3,7], we have 
	 * m = 3 and largest = 7, second = 3; after processing [5,7], we have m = 4 and 
	 * largest = 7, second = 7; after processing [7,8], we have m = 4 and 
	 * largest = 7, second = 7. Both largest and second are the same, which is not 
	 * what we expected, and the final answer should be 5, instead of 4. Conceptually 
	 * when two intervals have the same end points, we always want to process the 
	 * shorter one first.
	 * 
	 * -------------------------------------------------------------
	 * 
	 * [[0,1],[1,4],[3,4],[4,5]]
	 * 
	 * if we only sort using end time, the intervals will be already in order, after 
	 * processing first two interval, the largest is 4, second is 1, then because 
	 * only one element falls in [3,4], the code will make second=largest(4), 
	 * largest=interval[1]=4;
	 * 
	 * Rf :
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!/143982
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!/774544
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113085/Ever-wonder-why-the-greedy-algorithm-works-Here-is-the-explanation!/855679
	 */
	public int intersectionSizeTwo_sort_end_asc_start_dsc2(int[][] intervals) {
		Arrays.sort(intervals, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return (a[1] != b[1] ? Integer.compare(a[1], b[1]) 
						: Integer.compare(b[0], a[0]));
			}
		});

		int m = 0, largest = -1, second = -1;

		for (int[] interval : intervals) {
			int a = interval[0], b = interval[1];

			boolean is_largest_in = (a <= largest);
			boolean is_second_in = (a <= second);

			if (is_largest_in && is_second_in)
				continue;

			m += (is_largest_in ? 1 : 2);

			second = (is_largest_in ? largest : b - 1);
			largest = b;
		}

		return m;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/1050159/Java-A-solution-based-on-sorting-the-intervals-O(nlogn)-with-comments
	 * 
	 * start == last �L����� end �i�h
	 * size �O�諸�A���O���X���Ʀr�O����
	 * [[1,3],[3,7],[5,7],[7,8]]
	 * 
	 * �Ѧ� intersectionSizeTwo_sort_end(int[][] intervals)
	 * 
	 * Firstly, we need to sort the intervals. We add the end and end-1 of the 
	 * first interval. Then, we look at the boundaries of the current interval. 
	 * We need to either add zero, one, or at most two points to make the current 
	 * interval fit the problem condition.
	 */
	public int intersectionSizeTwo_sort_end_start_asc(int[][] intervals) {
		int n = intervals.length;
		
		// Sort intervals: 1- end 2- start- O(nlogn)
		Arrays.sort(intervals, (a, b) -> a[1] == b[1] ? a[0] - b[0] : a[1] - b[1]);

		List<Integer> res = new ArrayList<>();
		
		// Add one before end
		res.add(intervals[0][1] - 1);
		
		// Add end
		res.add(intervals[0][1]);
		
		for (int i = 1; i < n; i++) {
			int start = intervals[i][0];
			int end = intervals[i][1];
			
			int size = res.size();
			int last = res.get(size - 1);
			int secondLast = res.get(size - 2);
			
			// We need to add two fresh points
			if (start > last) {
				res.add(end - 1);
				res.add(end);
			} 
			// We already added one. We need to add the end of this interval
			else if (start == last) {				
				res.add(end);
			}
			// We already added last. We need one more
			else if (start > secondLast) {				
				res.add(end);
			}
		}
		
		return res.size();
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/1163623/A-simple-solution-with-proper-naming
	 * 
	 * Sort intervals with end and keep two pointers. 
	 * We need to figure out whenever we must any of two pointers and increment result
	 * 
	 * start == last �L����� end �i�h
	 * size �O�諸�A���O���X���Ʀr�O����
	 * [[1,3],[3,7],[5,7],[7,8]]
	 * 
	 * start == last �u�|�X�{�@�� (�b���Ʊ��p�U)�A�]������|���W�Q end ��s
	 * ���M���i�h���Ʀr�O���ƪ��A���O�u�Ҽ{ size�A�ҥH +1 �٬O�|�o�쥿�T�����G
	 * �Y�n�����ơA�u�ݭn�� end - 1 ������ secondLast �N��
	 */
	public int intersectionSizeTwo_sort_end(int[][] intervals) {
		Arrays.sort(intervals, (a1, a2) -> a1[1] - a2[1]);

		// always keep check of last two elements in result set
		int secondLast = intervals[0][1] - 1;
		int last = secondLast + 1;
		int ans = 2;
		
		for (int i = 1; i < intervals.length; i++) {
			int start = intervals[i][0];
			int end = intervals[i][1];
			
			if (start > last) {
				last = end;
				secondLast = end - 1;
				ans += 2;
			} 
			else if (start == last || (start < last && start > secondLast)) {
				secondLast = last;
				last = end;
				ans += 1;
			}
		}
		
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/191344/Java-TreeSet-easy-to-understand
	 * 
	 * 1. Sort by interval's end
	 * 2. Iterate over intervals checking if set contains two integers in 
	 *    interval's range.
	 *   2.a If not contains any, add 2 last integers from the range (highest 
	 *       chance that further interval will contain them)
	 *   2.b If contains only one integer from the range, add the last integer from 
	 *       the current interval or if it's already there add last - 1.
	 */
	public int intersectionSizeTwo_sort_end_treeSet(int[][] intervals) {
		Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
		TreeSet<Integer> set = new TreeSet<>();

		for (int[] interval : intervals) {
			int start = interval[0];
			int end = interval[1];
			
			// lower or equal to end or null
			Integer higher = set.floor(end);
			
			// lower than higher or null
			Integer lower = higher != null ? set.lower(higher) : null;

			// zero integers of that range in set
			if (higher == null || higher < start) {
				set.add(end);
				set.add(end - 1);
			} 
			// one element from that range in set
			// lower != null �i�H����
			else if (higher >= start && lower != null && lower < start) {
				if (set.contains(end)) {
					set.add(end - 1);
				} 
				else {
					set.add(end);
				}
			}
		}
		
		return set.size();
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/solution/
	 * 
	 * �g�L�ƧǡA��ܳ̾a��U�@�Ӱ϶����Ʀr�A�i�H�ɥi��֨�
	 * �o�ӸѪk�H start �Ѥp��j�Ƨ�
	 * �]���i�H�� start �M start + 1 ���קK�X�{�����Ʀr�]�϶����ǦӦ����~
	 * 
	 * �]�����_�I�A�ҥH�n�q�᩹�e��
	 * �ûP�U�@�Ӱ϶������I���
	 * 
	 * start �ۦP�ɡA��H���I�Ѥj��p�Ƨ�
	 * �]���ڭ̧Ʊ�ۦP start �ɡA�ڭ̯���B�z�϶����p��
	 * �Y�O���B�z�϶��j�� (end ���j)�A�|���ͭ��ƿ���ۦP�Ʀr�����p
	 * �Y�e�����϶��� end ��n����Q���ƿ���� start
	 * �o�Ӱ϶��N�|�֨�
	 * 
	 * test case: [[1, 2], [2, 3], [2, 4], [4, 5]]
	 * �q�᩹�e�B�z
	 * [4, 5] �ɡAtodo: [2, 2, 1, 0]�A����: [4, 5]
	 * [2, 4] �ɡAtodo: [1, 1, 0, 0]�A����: [2, 4, 5]
	 * [2, 3] �ɡAtodo: [0, 0, 0, 0]�A����: [2, 2, 4, 5]
	 * [1, 2] �ɡAtodo: [0, 0, 0, 0]�A����: [2, 2, 4, 5]
	 * 
	 * �i�H�ݥX�A�Y���h�� start �ۦP�����p
	 * �Y���B�z�϶��j���A�|���ƿ���o�ǬۦP�� start
	 * �Y�e�����϶��� end ��n���� �o�ǬۦP�� start
	 * �o�Ӱ϶��N�|�֨�
	 * 
	 * �o�̨ϥΤ@�� array �O���C�Ӱ϶��n���X�ӼƦr
	 * �Y�ѫ᩹�e�B�z�ɡA�o�{���i�h���o�ӼƦr���b�o�Ӱ϶����A�o�Ӱ϶�����N�i�H�֨�
	 * 
	 * what is the answer when the set intersection size is at least one?
	 * Sort the points. Take the last interval [s, e], which point on this interval 
	 * will be in S? Since every other interval has start point <= s, it is strictly 
	 * better to choose s as the start. So we can repeatedly take s in our set S and 
	 * remove all intervals containing s.
	 * 
	 * For each interval, we will perform the algorithm described above, storing a 
	 * todo multiplicity which starts at 2. As we identify points in S, we will 
	 * subtract from these multiplicities as appropriate.
	 * 
	 * One case that is important to handle is the following: 
	 * [[1, 2], [2, 3], [2, 4], [4, 5]]. If we put 4, 5 in S, then we put 2 in S, 
	 * when handling [2, 3] we need to put 3 in S, not 2 which was already put.
	 * 
	 * We can handle this case succinctly by sorting intervals [s, e] by s ascending, 
	 * then e descending. This makes it so that any interval encountered with the 
	 * same s has the lowest possible e, and so it has the highest multiplicity. 
	 * When at interval [s, e] and choosing points to be included into S, it will 
	 * always be the case that the start of the interval (either s or s, s+1) 
	 * will be unused.
	 */
	public int intersectionSizeTwo_record(int[][] intervals) {
		Arrays.sort(intervals, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
		
		int[] todo = new int[intervals.length];
		Arrays.fill(todo, 2);
		
		int ans = 0, t = intervals.length;
		while (--t >= 0) {
			int s = intervals[t][0];
			
			// �Ӱ϶��ѤU�ӳQ��i�h���ƶq
			int m = todo[t];
			
			// �q start �}�l�A��� m �Ӷi�h intersection
			for (int p = s; p < s + m; ++p) {
				// ���i�h���Ʀr p �M�e���϶������I�����
				// �Y p <= ���I�A��� p �]�b�o�Ӱ϶�
				// �Ӱ϶��Ѿl�ӿ���ƶq todo[i]--
				for (int i = 0; i <= t; ++i)
					if (todo[i] > 0 && p <= intervals[i][1])
						todo[i]--;
				
				ans++;
			}
		}
		return ans;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/178769/9-Line-Python-Solution-O(1)-Extra-Space-Detailed-explanation
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/201263/Not-most-efficient-but-easy-to-understand!
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/966290/Python-solution-O(n-log(n))
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/439986/Python-keep-track-of-the-right-most-2-points/812854
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113089/C%2B%2B-concise-solution-O(nlogn)-greedy-39-ms
     * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113088/C%2B%2B-Greedy-O(nlogn)-with-explanations
     */

}
