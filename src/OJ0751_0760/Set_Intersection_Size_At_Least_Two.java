package OJ0751_0760;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;

/*
 * 將 2D intervals array 以 end 由小到大排序
 * 若 end 相同，則由 start 由大到小排序
 * 選擇 2 個數字 (small, large) 作為被取出的數字，來和下一個 interval 做比較
 * 1. small >= interval.start
 *    表示這 2 個數字都在下一個 interval 中，不做處理
 * 2. large >= interval.start && small < interval.start
 *    表示只有 large 在下一個 interval 中，將現在的 large 改為 small
 *    下一個 interval 的 end 做為 large，取出數量 + 1
 * 3. 若不是以上情況，表示並未產生重疊
 *    small = interval.end - 1 和 large = interval.end
 * 
 * 因為要選出 overlap 最多的數字來盡量降低 intersection size
 * 由小到大排序，在前面 interval 最後面的數字 (end-1, end) 最有可能和後面 interval 產生 overlap
 * 因此我們可以由選出的 2 個數字 (small, large) 和下一個 interval 的 start 做比較
 * 若較小的數字 small 也比下一個 interval 的 start 大，表示這 2 個數字都和下一個 interval 有重疊 
 * 
 * 因為排序過，若是當前 interval 最大的數字 (end) 不在後一個 interval 當中
 * 當前 interval 中其他數字也不會在後一個 interval 中
 * 
 * 為了避免當前 interval 的 end 超過下一個 interval 的 end，而產生錯誤的比較順序
 * 所以選擇將 end 由小到大排序
 * 
 * ex: [[1,5],[4,5],[5,99],[7,9],[9,10]]
 * 若 start 由小到大排序，
 * 以 small 和 large 表示當前要和下一個 interval 比對的數字
 * [1,5]  => small: 4, large: 5, size: 2
 * [4,5]  => small: 4, large: 5, size: 2
 * [5,99] => small: 5, large: 99, size: 3
 * [7,9]  => small: 99, large: 9, size: 4 ，比較順序出現問題
 * [9,10] => small: 99, large: 9, size: 4 ，錯誤結果，答案應為 5
 * 
 * 若 end 相同，則優先處理較短的 interval
 * 避免在和下一個 interval 的 start 比較時，產生取到相同數字的情況
 * 
 * 考慮 [a1, b], [a2, b], [a3, b], [a4, b]. a1 < a2 < a3 < a4. 開始前 small = c, large = d
 * 處理 [a1, b], 若 (small) c < a1 <= (large) d < b
 * 更新為 small = d, large = b
 * 處理 [a2, b], 若 d < a2
 * (small) d < a2 < (large) b
 * 更新為 small = b, large = b, 產生重複選取, a3 開始出現少取
 * 
 * 因為每次只將 end 取入，遇到相同 end 但是不同 start 時，會重複選取 end
 * 當這些 array 超過 2 個，這個重複選取的 end 就會因為 start <= small 不做處理而導致少取 1 個數字
 * 
 * 因此 start 由大到小排序
 * 而且先處理完較短的 interval，後面較長的 interval 就會自動處理完
 * 
 * ex: [[1,5],[4,5],[5,9],[7,9],[9,10]]
 * 若 end 由小到大排序，end 相同則由 start 由小到大排序，
 * 以 small 和 large 表示當前要和下一個 interval 比對的數字
 * [1,5]  => small: 4, large: 5, size: 2
 * [4,5]  => small: 4, large: 5, size: 2
 * [5,9]  => small: 5, large: 9, size: 3
 * [7,9]  => small: 9, large: 9, size: 4 ，取到相同的數字
 * [9,10] => small: 9, large: 9, size: 4 ，錯誤結果，答案應為 5
 */

public class Set_Intersection_Size_At_Least_Two {
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/113086/Hope-you-enjoy-this-problem.-%3A-)-O(NlogN)JavaGreedy-Easy-to-understand-solution
	 * 
	 * 因為要選出 overlap 最多的數字來盡量降低 intersection size
	 * 由小到大排序，在前面 interval 最後面的數字 (end-1, end) 最有可能和後面 interval 產生 overlap
	 * 因此我們可以由選出的 2 個數字 (small, large) 和下一個 interval 的 start 做比較
	 * 若較小的數字 small >= interval.start，表示這 2 個數字都和下一個 interval 有重疊
	 *  
	 * 因為排序過，若是當前 interval 最大的數字 (end) 不在後一個 interval 當中
	 * 當前 interval 中其他數字也不會在後一個 interval 中
	 * 
	 * 為了避免當前 interval 的 end 超過下一個 interval 的 end，而產生錯誤的比較順序
	 * 所以選擇將 end 由小到大排序
	 * 
	 * 若 end 相同，則優先處理較短的 interval
	 * 避免在和下一個 interval 的 start 比較時，產生取到相同數字的情況
	 * 因此 start 由大到小排序
	 * 而且先處理完較短的 interval，後面較長的 interval 就會自動處理完
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
	 * 使用 end 由小到大排序，這樣我們選取前面 interval 的 end <= 後面 interval 的 end
	 * 因此由前往後，分為以下情況
	 * 1. start > max1，表示這個 interval 沒有數字被包含，取 end 和 end - 1 並更新 max1, max2
	 * 2. start > max2，表示這個 interval 有 1 個數字被包含
	 * 
	 * 2.1 max1 = end，表示之前有一個 interval_p
	 * interval_p.end = interval.end && interval_p.start < interval.start
	 * ∵ interval_p.start > 當時的 max2，而只有將 interval_p.end 取入
	 * 又因為 interval.start < interval.end = interval_p.end = max1
	 * 所以不符合條件 1
	 * 
	 * 若前面的 interval_p2 是
	 * interval_p2.end = interval.end && interval_p2.start > interval.start
	 * 則 interval_p2 是 interval 的子區間
	 * 若 interval_p2 已經有 2 個數字被選取，這 2 個數字必定在 interval 中
	 * 而且 max2 >= interval_p2.start > interval.start
	 * 不在這個條件 2 
	 * 
	 * 為了避免重複選到這個相同的 interval.end，我們改取 interval.end - 1  並更新 max1, max2
	 * WLOG, 考慮區間 [a1, b], [a2, b], ..., [an, b]
	 * 無論 a1, a2, ..., an 之間大小關係如何，b 和 b - 1 必定存在所有區間
	 * 所以我們選 b 和 b - 1 作為 max1, max2 的同時，也保證所有區間都有 2 個數字被選取
	 * 
	 * 2.2 max1 != end，表示這個 interval.end 並未被選取，選取 interval.end 並更新 max1, max2
	 * 3. 其餘的情況表示這個 interval 已經有 2 個數字被選取，繼續下一個 interval
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
				
				// 避免重複選取相同數字
				max2 = max1 == end ? max1 - 1 : max1;
				max1 = end;
			}
		}
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/set-intersection-size-at-least-two/discuss/767649/Java-O(nlogn)-greedy-fast-solution-sort-the-intervals-beforehand
	 * 
	 * 參考最上方的解釋
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
	 * 若是有區間完全被另一區間包含，我們可以捨棄較大的區間
	 * 
	 * 首先，我們以 start 由小到大排序，若 start 相同，則由 end 由大到小排序
	 * 再以 stack 捨棄較大的區間
	 * 因為先以 start 排序，所以 stack 越往底部 interval 的 start 越小，因此我們只需考慮 end 即可
	 * 
	 * 所有 interval 符合
	 * interval.start >= stack.top.start
	 * 
	 * 現在處理 interval 若符合
	 * interval.end   <= stack.top.end
	 * 
	 * 表示 interval 是 stack.top 的子區間，我們可以捨棄 stack.top
	 * interval 接著比較新的 stack.top
	 * 
	 * 處理完所有的 interval 後，在 stack 的部分即為互不完全包含的 interval
	 * 
	 * 我們可以取當前 interval 的 end 和 end - 1，因為他們是最有可能被後面 interval 包含的數字
	 * 若下一個 interval 已經包含 1 個數字，我們只需選取 end 即可
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
	 * start == last 無條件取 end 進去
	 * size 是對的，但是取出的數字是錯的
	 * [[1,3],[3,7],[5,7],[7,8]]
	 * 
	 * 參考 intersectionSizeTwo_sort_end(int[][] intervals)
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
	 * start == last 無條件取 end 進去
	 * size 是對的，但是取出的數字是錯的
	 * [[1,3],[3,7],[5,7],[7,8]]
	 * 
	 * start == last 只會出現一次 (在重複情況下)，因為之後會馬上被 end 更新
	 * 雖然取進去的數字是重複的，但是只考慮 size，所以 +1 還是會得到正確的結果
	 * 若要不重複，只需要用 end - 1 替換掉 secondLast 就行
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
			// lower != null 可以移除
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
	 * 經過排序，選擇最靠近下一個區間的數字，可以盡可能少取
	 * 這個解法以 start 由小到大排序
	 * 因此可以取 start 和 start + 1 來避免出現取的數字因區間順序而有錯誤
	 * 
	 * 因為取起點，所以要從後往前取
	 * 並與下一個區間的終點比較
	 * 
	 * start 相同時，改以終點由大到小排序
	 * 因為我們希望相同 start 時，我們能先處理區間較小的
	 * 若是先處理區間大的 (end 較大)，會產生重複選取相同數字的情況
	 * 若前面有區間的 end 剛好等於被重複選取的 start
	 * 這個區間就會少取
	 * 
	 * test case: [[1, 2], [2, 3], [2, 4], [4, 5]]
	 * 從後往前處理
	 * [4, 5] 時，todo: [2, 2, 1, 0]，取走: [4, 5]
	 * [2, 4] 時，todo: [1, 1, 0, 0]，取走: [2, 4, 5]
	 * [2, 3] 時，todo: [0, 0, 0, 0]，取走: [2, 2, 4, 5]
	 * [1, 2] 時，todo: [0, 0, 0, 0]，取走: [2, 2, 4, 5]
	 * 
	 * 可以看出，若有多個 start 相同的情況
	 * 若先處理區間大的，會重複選取這些相同的 start
	 * 若前面有區間的 end 剛好等於 這些相同的 start
	 * 這個區間就會少取
	 * 
	 * 這裡使用一個 array 記錄每個區間要取幾個數字
	 * 若由後往前處理時，發現取進去的這個數字落在這個區間中，這個區間之後就可以少取
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
			
			// 該區間剩下該被選進去的數量
			int m = todo[t];
			
			// 從 start 開始，選擇 m 個進去 intersection
			for (int p = s; p < s + m; ++p) {
				// 把選進去的數字 p 和前面區間的終點做比較
				// 若 p <= 終點，表示 p 也在這個區間
				// 該區間剩餘該選取數量 todo[i]--
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
