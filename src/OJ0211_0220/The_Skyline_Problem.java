package OJ0211_0220;

import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Queue;
import java.util.PriorityQueue;

/*
 * https://briangordon.github.io/2014/08/the-skyline-problem.html
 * 
 * skyline 只會在建築物的左端點和右端點改變，因此我們只要找出這些臨界點的高度就可以決定 skyline。 
 * 在臨界點進行向右的橫向掃描，每一點取最大值，可以得到天際線。注意：不包含右端點
 * 參照程式碼：getSkyline_all_critical(int[][] buildings)
 * 
 * 由以上過程可發現，我們只需要更新比建築物低的臨界點
 * 參照程式碼：getSkyline_low_critical(int[][] buildings)
 * 
 * 我們可以嘗試調換 nested for loop 的順序，這樣有機會增加效率
 * 參照程式碼：getSkyline_low_critical_outside(int[][] buildings)
 * 
 * 更好的做法是將臨界點依照 x 座標，由小到大排序
 * 由左而右，向右橫向掃描這些臨界點，同時使用一個 set 來儲存當前的有效建築物
 * 到達一個臨界點時，更新這個 set，並將這個 set 的建築物賦予這個臨界點
 * 如此每個臨界點就可以知道所有比它高的建築物
 * for each critical point c
 *     c.y gets the height of the tallest rectangle over c
 * 
 * 最終解法：
 * 1. 將臨界點依照 x 座標，由小到大排序
 * 2. 由左至右 scan 這些臨界點
 * 3-1. 遇到建築物的左端點，將建築物加入 maxHeap，並使用高度作為 key
 * 3-2. 遇到建築物的右端點，將建築物從 maxHeap 中移除 (需要額外指標來記錄)
 * 4. 任何時候當我們遇到臨界點，更新完 maxHeap 之後，將 maxHeap.top() 作為臨界點的高度
 * 
 * 改進：
 * 不用額外指標來記錄 maxHeap，遇到建築物的右端點，不斷 pop 直到 maxHeap.top() 是有效的
 * 這樣 maxHeap 有可能包含已經過去的建築物，
 * 但是我們只取 maxHeap.top()，因此只要 maxHeap.top() 是有效的就好
 * 參照程式碼：getSkyline_building_pq_check_top(int[][] buildings)
 */

/*
 * 解法二：Divide-and-Conquer (類似 merge sort)
 * 
 * Ch4Ch5 p.20
 * 
 * 若我們已知 前 n-1 個 building 的合併結果，如何加入第 n 個 Bn (Ln, Hn, Rn)？
 * 
 * 先找出 Ln 之前的那個 x_1，接著取出和 Bn 重疊的部分
 * (x_1, h1), (x_2, h2), ..., (x_m, hm)，其中 x_m < Rn 而且 x_m+1 >= Rn (或者 x_m 是最後一個)
 * 
 * 若 hi < Hn，則這一段會被 Bn 的高度 Hn 所蓋住
 * 若不存在 x_m+1，則可以修改最後一段 x_m 為 (x_m, Hn) ，並新增額外的一段 (Rn, 0)
 * 我們同時也檢查相鄰的兩段高度是否相同，若相同則合併在一起
 * 
 * 使用 Divide-and-Conquer 改進：T(n) = 2T(n/2) + O(n). T(n) = O(n log n).
 * 我們可以遞迴將所有建築物切成 2 堆並合併
 * 
 * 2 個 skylines A = (a1, ha1, a2, ha2, ..., an, 0); B = (b1, hb1, b2, hb2, ..., bm, 0)
 * 合併出 (c1, hc1, c2, hc2, ..., c_n+m, 0).
 * 我們使用 CurH1 和 CurH2 (這些是遇見 2 個 lists 之前的高度) 去儲存現在這 2 個 skylines 的高度
 * 當比較這 2 個 skylines 的 head entries (CurH1, CurH2) 時，我們新增一段 (接在 output skyline)
 * x 座標是 minimum of the entries’ x 座標，高度為 maximum of CurH1 and CurH2.
 */

/*
 * test cases
 * 
 * 1. [[4,9,10],[4,9,15],[4,9,12],[10,12,10],[10,12,8]]
 * 2. [[1,2,1], [1,2,2], [1,2,3], [1,2,4], [2,3,2], [2,3,4]]
 */

public class The_Skyline_Problem {
	/*
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61194/108-ms-17-lines-body-explained
	 * 
	 * It sweeps from left to right. (left, right, height) pairs are kept in the 
	 * priority queue and they stay in there as long as there's a larger height in 
	 * there. It doesn't remove them as soon as their building is left behind. 
	 * 
	 * In each loop, we first check what has the smaller x-coordinate: adding the 
	 * next building from the input, or removing the next building from the queue. 
	 * In case of a tie, adding buildings wins, as that guarantees correctness. We 
	 * then either add all input buildings starting at that x-coordinate or we remove 
	 * all queued buildings ending at that x-coordinate or earlier (remember we keep 
	 * buildings in the queue as long as they're "under the roof" of a larger 
	 * actually alive building). And then, if the current maximum height in the 
	 * queue differs from the last in the skyline, we add it to the skyline.
	 */
	public List<List<Integer>> getSkyline_pointer2(int[][] buildings) {
		List<List<Integer>> ans = new ArrayList<>();

		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[2] - a[2]);
		
		int i = 0, xPosition = 0, height = 0;
		
		// 只要全部的左右端點尚未處理完，繼續執行
		while (i < buildings.length || !pq.isEmpty()) {
			// 下一個轉折點是左端點 (進入點)。若左右端點相同，先加入
			// 若 PriorityQueue 為空，也加入
			if (pq.isEmpty() || 
					(i < buildings.length && buildings[i][0] <= pq.peek()[1])) {
				
				xPosition = buildings[i][0];
				
				// 若之後的建築物左端點也相同，繼續加入
				while (i < buildings.length && xPosition == buildings[i][0]) {
					pq.offer(buildings[i]);
					i++;
				}
			} 
			// 下一個轉折點是右端點 (離開點)
			else {
				xPosition = pq.peek()[1];
				
				// 若右端點 <= 現在 x 座標，表示這個建築物已經離開了，將之移除
				while (!pq.isEmpty() && pq.peek()[1] <= xPosition)
					pq.poll();
			}

			// 這個 x 座標處理完畢，求高度
			height = pq.isEmpty() ? 0 : pq.peek()[2];
			if (ans.isEmpty() || ans.get(ans.size() - 1).get(1) != height) {
				ans.add(Arrays.asList(xPosition, height));
			}
		}

		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/225750
	 * 
	 * Sweepline is used in solving the problem. Save each of the line segments 
	 * including both start and end point. The trick here is to set the start 
	 * segment as negative height. 
	 * 
	 * 1. Make sure the start segment comes before the end one after sorting.
	 * 2. When pushing into the queue, it is very each to distinguish either to add 
	 *    or remove a segment.
	 * 3. When the two adjacent building share same start and end x value, the next 
	 *    start segment always come before due to the negative height. When the 
	 *    first building is lower, when we peek the queue, we get the height of the 
	 *    second building, and the first building will be removed in the next round 
	 *    of iteration. When the second building is lower, the second peek returns 
	 *    the first building and since it equals to prev, the height will not be 
	 *    added.
	 * 
	 * Add start first and delete end second, and also start from large to small 
	 * while end from small to large.
	 * 
	 * Sort the height array such that it considers following conditions:
	 * i. When the two points are not equal then sort them by coordinate values.
	 * ii. When two coordinate values are same, then check
	 *   a. if both of them are start/left coordinates. If so, consider the largest 
	 *      height. (That's why left coordinate heights are marked negative).
	 *   b. if both of them are end/right coordinates. If so, consider the shortest 
	 *      height.
	 *   c. If one of them is end/right and other is start/left then consider the 
	 *      start/left height.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61304/Java-solution-using-priority-queue-and-sweepline
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/62401
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/120321
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61192/Once-for-all-explanation-with-clean-Java-code(O(n2)time-O(n)-space)/121896
	 * 
	 * Other code:
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/62394
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/62419
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/468614
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/62463
	 */
	public List<List<Integer>> getSkyline_treeMap_remove(int[][] buildings) {
		List<List<Integer>> res = new ArrayList<>();
		
		List<int[]> height = new ArrayList<>();
		for (int[] building : buildings) {
			
			// start point has negative height value
			height.add(new int[] { building[0], -building[2] });
			
			// end point has normal height value
			height.add(new int[] { building[1], building[2] });
		}
		
		Collections.sort(height, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				if (a[0] == b[0]) {
					return a[1] - b[1];
				} 
				else {
					return a[0] - b[0];
				}
			}
		});
		
		// Use a maxHeap to store possible heights
		// But priority queue does not support remove in (log n) time
		// TreeMap support add, remove, get max in (log n) time, so use TreeMap here
		// key: height, value: number of this height
		TreeMap<Integer, Integer> pq = new TreeMap<>();
		
		// When there are no buildings for current TreeMap, it means there will be a
		// horizontal line
		pq.put(0, 1);
		
		// Before starting, the previous max height is 0;
		int prev = 0;
		
		// visit all points in order
		for (int[] h : height) {
			
			// a start point, add height
			if (h[1] < 0) {
				pq.put(-h[1], pq.getOrDefault(-h[1], 0) + 1);
			}
			// a end point, remove height
			else {
				if (pq.get(h[1]) > 1) {
					pq.put(h[1], pq.get(h[1]) - 1);
				} 
				else {
					pq.remove(h[1]);
				}
			}
			
			int cur = pq.lastKey();
			
			// compare current max height with previous max height, update result and
			// previous max height if necessary
			if (cur != prev) {
				res.add(Arrays.asList(h[0], cur));
				prev = cur;
			}
		}
		return res;
	}
	
	/*
	 * by myself
	 * 
	 * Rf : Update (Feb 2019)
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * 1. 取出建築物的左右端點做為臨界點，將它們依照 x 座標，由小到大排序
	 * 2. 由左至右 scan 這些臨界點
	 * 3. 更新 maxHeap
	 *   3-1. 遇到建築物的左端點，將建築物加入 maxHeap，並使用高度作為 key
	 *   3-2. 若 maxHeap.top() 的右端點 <= 現在要處理的臨界點 x 座標，將它移除 maxHeap
	 * 4. 該臨界點的高度為 maxHeap.top() 的高度
	 * 
	 * 注意 test cases:
	 * Input:    [[1,2,1],[1,2,2],[1,2,3]]
	 * Expected: [[1,3],[2,0]]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61192/Once-for-all-explanation-with-clean-Java-code(O(n2)time-O(n)-space)/62345
	 * 
	 * Other code:
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61264/easy-to-understand-O(nlogn)-java-solution-with-detailed-explanation
	 */
	public List<List<Integer>> getSkyline_building_pq_check_top(int[][] buildings) {
		// 取出所有建築物的左右端點，它們是臨界點。
		// 高度初始化為 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// 將臨界點依照 x 座標，由小到大排序
		Collections.sort(criticals, new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return a[0] - b[0];
			}
		});

		// 儲存建築物的 maxHeap，比較高度
		Queue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return b[2] - a[2];
			}
		});

		// scan 臨界點
		// 更新 maxHeap 並找出該臨界點的高度
		int i = 0;
		for (Integer[] critical : criticals) {
			
			// 建築物的左端點 <= 臨界點 x 座標，放入 maxHeap
			while (i < buildings.length && buildings[i][0] <= critical[0]) {
				pq.offer(buildings[i]);
				i++;
			}

			// maxHeap.top() 的右端點 <= 臨界點 x 座標，將它移除 maxHeap
			while (!pq.isEmpty() && pq.peek()[1] <= critical[0]) {
				pq.poll();
			}

			// 若 maxHeap 不為空，可以更新高度
			if (!pq.isEmpty()) {
				critical[1] = pq.peek()[2];
			}
		}

		List<List<Integer>> result = new ArrayList<>();
		int pre = 0;
		for (Integer[] critical : criticals) {
			// System.out.println(Arrays.toString(critical));

			if (critical[1] != pre) {
				result.add(Arrays.asList(critical));
				pre = critical[1];
			}
		}

		return result;
	}
	
	/*
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61279/My-C++-code-using-one-priority-queue-(812-ms)/126604
	 * 
	 * The idea is to do line sweep and just process the buildings only at the start 
	 * and end points. The key is to use a priority queue to save all the buildings 
	 * that are still "alive". The queue is sorted by its height and end time 
	 * (the larger height first and if equal height, the one with a bigger end time 
	 * first). For each iteration, we first find the current process time, which is 
	 * either the next new building start time or the end time of the top entry of 
	 * the live queue. If the new building start time is larger than the top one end 
	 * time, then process the one in the queue first (pop them until it is empty or 
	 * find the first one that ends after the new building); otherwise, if the new 
	 * building starts before the top one ends, then process the new building (just 
	 * put them in the queue). After processing, output it to the resulting list if 
	 * the height changes. Complexity is the worst case O(NlogN)
	 */
	public List<List<Integer>> getSkyline_pointer(int[][] buildings) {
		LinkedList<List<Integer>> ans = new LinkedList<>();

		PriorityQueue<int[]> pq = new PriorityQueue<>(
				(a, b) -> a[2] == b[2] ? b[1] - a[1] : b[2] - a[2]);
		
		int i = 0, pos = 0, height = 0;
		
		// either some new building is not processed or live building queue is 
		// not empty
		while (i < buildings.length || !pq.isEmpty()) {
			
			// next timing point to process
			pos = pq.isEmpty() ? buildings[i][0] : pq.peek()[1];
			
			// first check if the current tallest building will end before the 
			// next timing point
			if (i >= buildings.length || pos < buildings[i][0]) {
				
				// pop up the processed buildings, i.e. those have height no larger 
				// than `height` and end before the top one
				while (!pq.isEmpty() && pq.peek()[1] <= pos)
					pq.poll();
			} 
			// if the next new building starts before the top one ends, process the 
			// new building in the array
			else {
				pos = buildings[i][0];
				
				// go through all the new buildings that starts at the same point
				// just push them in the queue
				while (i < buildings.length && pos == buildings[i][0])
					pq.offer(buildings[i++]);
			}

			// output the top one
			height = pq.isEmpty() ? 0 : pq.peek()[2];
			if (ans.isEmpty() || ans.getLast().get(1) != height) {
				ans.add(Arrays.asList(pos, height));
			}
		}

		return ans;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61246/Share-my-divide-and-conquer-java-solution-464-ms
	 * 
	 * The basic idea is divide the buildings into two subarrays, calculate their 
	 * skylines respectively, then merge two skylines together.
	 * 
	 * Ch4Ch5 p.20
	 * 
	 * We use two variables h1 and h2 (note that these are the heights prior to 
	 * encountering the heads of the lists) to store the current height of the first 
	 * and the second skyline. When comparing the head entries (h1, h2) of the two 
	 * skylines, we introduce a new strip (and append to the output skyline) whose 
	 * x-coordinate is the minimum of the entries’ x-coordinates and whose height is 
	 * the maximum of h1 and h2. 
	 * 
	 * Let T(n) denote the running time of this algorithm for n buildings. Since 
	 * merging two skylines of size n/2 takes O(n), we find that T(n) satisfies the 
	 * recurrence T(n) = 2T(n/2) + O(n). This is just like Mergesort. Thus, we 
	 * conclude that the divide-and-conquer algorithm for the skyline problem is 
	 * O(n log n). 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61281/Java-divide-and-conquer-solution-beats-96
	 * 
	 * Other code:
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61281/Java-divide-and-conquer-solution-beats-96/188836
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61211/Java-570ms-Heap%2BBST-and-430ms-Divide-and-Conquer-Solution-with-Explanation
	 */
	public List<List<Integer>> getSkyline_divide_and_conquer(int[][] buildings) {
		if (buildings.length == 0)
			return new LinkedList<List<Integer>>();
		
		return recurSkyline(buildings, 0, buildings.length - 1);
	}

	private LinkedList<List<Integer>> recurSkyline(int[][] buildings, int p, int q) {
		if (p < q) {
			int mid = p + (q - p) / 2;
			return merge(recurSkyline(buildings, p, mid), 
					recurSkyline(buildings, mid + 1, q));
		} 
		else {
			LinkedList<List<Integer>> rs = new LinkedList<List<Integer>>();
			rs.add(Arrays.asList(new Integer[] { buildings[p][0], buildings[p][2] }));
			rs.add(Arrays.asList(new Integer[] { buildings[p][1], 0 }));
			return rs;
		}
	}

	private LinkedList<List<Integer>> merge(LinkedList<List<Integer>> l1, 
			LinkedList<List<Integer>> l2) {
		
		LinkedList<List<Integer>> rs = new LinkedList<>();
		int h1 = 0, h2 = 0;
		
		while (!l1.isEmpty() && !l2.isEmpty()) {
			int x = 0, h = 0;
			
			if (l1.peekFirst().get(0) < l2.peekFirst().get(0)) {
				List<Integer> l1First = l1.pollFirst();
				x = l1First.get(0);
				h1 = l1First.get(1);
			} 
			else if (l1.peekFirst().get(0) > l2.peekFirst().get(0)) {
				List<Integer> l2First = l2.pollFirst();
				x = l2First.get(0);
				h2 = l2First.get(1);
			} 
			else {
				List<Integer> l1First = l1.pollFirst();
				List<Integer> l2First = l2.pollFirst();
				x = l1First.get(0);
				h1 = l1First.get(1);
				h2 = l2First.get(1);
			}
			
			h = Math.max(h1, h2);
			
			// 和前一個點的高度不同才加入
			if (rs.isEmpty() || h != rs.getLast().get(1)) {
				rs.add(Arrays.asList(new Integer[] { x, h }));
			}
		}
		
		rs.addAll(l1);
		rs.addAll(l2);
		return rs;
	}
	
	/*
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61192/Once-for-all-explanation-with-clean-Java-code(O(n2)time-O(n)-space)
	 * 
	 * find the critical points that change the max height among the buildings on the 
	 * left
	 * 
	 * Our target exists at the position where height change happens and there is 
	 * nothing above it shadowing it. We need a mechanism to store current taking 
	 * effect heights, meanwhile, figure out which one is the maximum, delete it if 
	 * needed efficiently, which hints us to use a priority queue or BST.
	 * 
	 * 1. visit all start points and all end points in order;
	 * 2. when visiting a point, we need to know whether it is a start point or a end 
	 *    point, based on which we can add a height or delete a height from our data 
	 *    structure;
	 * 
	 *  [start, - height] and [end, height] for every building;
	 *  sort it, firstly based on the first value, then use the second
	 * 1. we can visit all points in order;
	 * 2. when points have the same value, higher height will shadow the lower one;
	 * 3. we know whether current point is a start point or a end point based on the 
	 *    sign of its height;
	 * 
	 * When there are no buildings for current queue, it means there will be a 
	 * horizontal line which follows the contour, so we should first put 0 in the 
	 * queue. When we reach the end of the height arr, the value 0 also plays an 
	 * important role in forming the horizontal line.
	 * 
	 * The remove operation itself is calling equals() rather than Arrays.equals() or 
	 * Arrays.deepEquals()
	 * 
	 * Rf :
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61193/Short-Java-solution/179503
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61192/Once-for-all-explanation-with-clean-Java-code(O(n2)time-O(n)-space)/121896
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/62449
	 * 
	 * Other code:
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/62461
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/190968
	 */
	public List<List<Integer>> getSkyline_pq(int[][] buildings) {
		List<List<Integer>> result = new ArrayList<>();

		List<int[]> height = new ArrayList<>();
		for (int[] b : buildings) {
			// start point has negative height value
			height.add(new int[] { b[0], -b[2] });
			// end point has normal height value
			height.add(new int[] { b[1], b[2] });
		}

		// sort $height, based on the first value, if necessary, use the second to
		// break ties
		Collections.sort(height, (a, b) -> {
			if (a[0] != b[0])
				return a[0] - b[0];
			
			return a[1] - b[1];
		});

		// Use a maxHeap to store possible heights
		Queue<Integer> pq = new PriorityQueue<>((a, b) -> (b - a));

		// When there are no buildings for current queue, it means there will be a
		// horizontal line
		pq.offer(0);

		// Before starting, the previous max height is 0;
		int prev = 0;

		// visit all points in order
		for (int[] h : height) {
			if (h[1] < 0) {  // a start point, add height
				pq.offer(-h[1]);
			} 
			else {           // a end point, remove height
				pq.remove(h[1]);
			}
			
			int cur = pq.peek(); // current max height;

			// compare current max height with previous max height, update result and
			// previous max height if necessary
			if (prev != cur) {
				result.add(Arrays.asList(h[0], cur));
				prev = cur;
			}
		}
		return result;
	}
	
	/*
	 * The following 2 classes are from this link.
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61316/Accepted-38ms-Java-solution-O(nlogn)-with-explanation
	 * 
	 * For any x-coordinate, the only visible building, will be the highest one.
	 * 
	 * Those points where the highest available building potentially changes turn out 
	 * to be the beginning/end of every building. As all buildings have constant 
	 * height, a building becomes the highest only when it appears (and there is no 
	 * one higher), or when the current highest ends (and there is no one higher).
	 * 
	 * Consider a sorted (by the x-coordinate) array of beginnings/ends for all 
	 * buildings and process such events in the following way:
	 * 
	 * + Events with same x-coordinate should be processed together.
	 * + Every time we find a beginning, we add its height to some collection with 
	 *   the currently available heights.
	 * + Every time we find an end, we remove its height from that collection.
	 * 
	 * What we are doing here is a line swept algorithm: we move an imaginary line 
	 * along the x-axis, stopping it just at some discrete points where something 
	 * crucial happens. Here after processing all events taking place at some 
	 * x-coordinate X, we query our collection for the largest available height H, and 
	 * if it is larger than our current largest, we update it and add the pair X, H to 
	 * the result list. The heights collection needs to be efficient on adding, 
	 * removing, and querying for the largest value. Also, we have to manage that we 
	 * can have duplicates. In order to achieve O(log n) complexity for those 
	 * operations, we may code a segment tree (tree leaves will be all buildings), use 
	 * a TreeSet of some custom <building, height> pair representation, or just use a 
	 * TreeMap of <height, current count>. 
	 * 
	 * Since we have exactly 2N events, we sort them, and then for each one we perform 
	 * O(logn) operations, overall complexity is O(nlogn), n the number of buildings.
	 */
	public class Solution_TreeMap_remove_height {
		public List<List<Integer>> getSkyline(int[][] buildings) {
			TreeMap<Integer, Integer> availableHeights = new TreeMap<>();
			
			int N = buildings.length;
			List<List<Integer>> view = new ArrayList<>(N);
			if (N == 0) {
				return view;
			}
			
			Event[] events = new Event[N << 1];
			for (int i = 0; i < N; ++i) {
				int[] building = buildings[i];
				events[i << 1] = new Event(building[0], building[2], false);
				events[1 + (i << 1)] = new Event(building[1], building[2], true);
			}
			
			Arrays.sort(events);
			
			int currentHeight = 0;
			availableHeights.put(0, 1);
			
			for (int i = 0, j; i < N << 1; i = j) {
				
				// j 要從 i 開始，因為在內層 for loop 做 height 的處理
				for (j = i; j < N << 1 && events[i].x == events[j].x; ++j) {
					Event event = events[j];
					if (event.closing) {
						int counter = availableHeights.get(event.height);
						
						if (counter == 1) {
							availableHeights.remove(event.height);
						} 
						else {
							availableHeights.put(event.height, counter - 1);
						}
					} 
					else {
						Integer counter = availableHeights.get(event.height);
						
						if (counter == null) {
							availableHeights.put(event.height, 1);
						} 
						else {
							availableHeights.put(event.height, counter + 1);
						}
					}
				}
				
				int x = events[i].x;
				int height = availableHeights.lastKey();
				
				if (height != currentHeight) {
					view.add(Arrays.asList(new Integer[] { x, height }));
					currentHeight = height;
				}
			}
			return view;
		}
	}

	class Event implements Comparable<Event> {
		int x, height;
		boolean closing;

		public Event(int a, int b, boolean c) {
			x = a;
			height = b;
			closing = c;
		}

		// 1. x 由小排到大
		// 2. closing 由 false 排到 true
		@Override
		public int compareTo(Event that) {
			return x != that.x ? x - that.x : Boolean.compare(closing, that.closing);
		}
	}
	
	/*
	 * The following class and 4 functions are from this link.
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61230/Java-Segment-Tree-Solution-47-ms
	 * 
	 * Other code:
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61313/A-segment-tree-solution/185639
	 */
	private class Node {
		int start, end;
		Node left, right;
		int height;

		public Node(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	public List<List<Integer>> getSkyline_segment_tree(int[][] buildings) {
		Set<Integer> endpointSet = new HashSet<Integer>();
		for (int[] building : buildings) {
			endpointSet.add(building[0]);
			endpointSet.add(building[1]);
		}

		List<Integer> endpointList = new ArrayList<Integer>(endpointSet);
		Collections.sort(endpointList);

		Map<Integer, Integer> endpointMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < endpointList.size(); i++) {
			endpointMap.put(endpointList.get(i), i);
		}

		Node root = buildNode_segment_tree(0, endpointList.size() - 1);
		for (int[] building : buildings) {
			add_segment_tree(root, endpointMap.get(building[0]), 
					endpointMap.get(building[1]), building[2]);
		}

		List<List<Integer>> result = new ArrayList<>();
		explore_segment_tree(result, endpointList, root);

		// 最後一個 end 沒有被算到，手動加入
		// 沒有建築物才不用加入
		// 若把 if 移除，則在最前面要加上 empty array 的判斷
		if (endpointList.size() > 0) {
			result.add(Arrays.asList(
					new Integer[] { endpointList.get(endpointList.size() - 1), 0 }));
		}

		return result;
	}

	private Node buildNode_segment_tree(int start, int end) {
		// 也可以
		// if (start > end)
		if (start >= end) {
			return null;
		} 
		
		// 只包含 start，不算 end
		Node result = new Node(start, end);
		if (start + 1 < end) {
			int center = start + (end - start) / 2;
			
			result.left = buildNode_segment_tree(start, center);
			result.right = buildNode_segment_tree(center, end);
		}
		
		return result;
	}

	private void add_segment_tree(Node node, int start, int end, int height) {
		// 以建築物 start, end, height 更新 node 以及子 node
		// 因此超過範圍和高度不夠就停止更新
		if (node == null || start >= node.end || end <= node.start 
				|| height < node.height) {
			
			return;
		}
		
		if (node.left == null && node.right == null) {
			node.height = Math.max(node.height, height);
		} 
		else {
			add_segment_tree(node.left, start, end, height);
			add_segment_tree(node.right, start, end, height);
			
			// 利用前面的 height < node.height 來剪枝 
			// 省略仍然 accept，但是會變慢
			node.height = Math.min(node.left.height, node.right.height);
		}
	}

	private void explore_segment_tree(List<List<Integer>> result, 
			List<Integer> endpointList, Node node) {
		
		if (node == null) {
			return;
		} 
		
		if (node.left == null && node.right == null
				&& (result.size() == 0 
					|| result.get(result.size() - 1).get(1) != node.height)) {
			
			result.add(Arrays.asList(
					new Integer[] { endpointList.get(node.start), node.height }));
		} 
		else {
			explore_segment_tree(result, endpointList, node.left);
			explore_segment_tree(result, endpointList, node.right);
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61198/My-O(nlogn)-solution-using-Binary-Indexed-Tree(BIT)Fenwick-Tree/62473
	 * 
	 * 碰到左端點，就在右端點建立 suffix range max (不包含右端點)
	 * 使用 BIT 可以只用 log n 的點的 max 來得到 suffix max
	 * 
	 * 將原始 BIT 更新、求和的方向反過來，可以看成是 suffix range max
	 * 
	 * 若長度為 13，求 5 到 13 的 max
	 * Max(5, 13) = Max(Max(5, 6), Max(6, 8), Max(8, 最後一個 13))
	 * 
	 * 更新 5
	 * Update(5, 6), Update(4, 5)
	 * 
	 * Difficulty 1: Remove height
	 * 
	 * We need to change it suffix range query, to get the max value in 
	 * [index, INFINITE). The implementation is just change the index calculation in 
	 * the query loop from index-=lowerbit(index) to index+=lowerbit(index). We have 
	 * to change "update" accordingly, and same idea applies.
	 * 
	 * Sort the buildings array, use sweep line algorithm to scan the buildings in x 
	 * direction, from left to right. Only when we hit a building's starting point x1, 
	 * we add the build's height into BIT at its ENDING point x2. So in BIT, [x1, x2] 
	 * contains the height of the building, and [x2 + 1, INFINITE) does not include 
	 * the height of the building(because we use suffix range BIT). This way we don't 
	 * need to remove the building height of the case, because all < x1 coordinates 
	 * have been processed already, no one care about them anymore, so it is fine to 
	 * leave them along. On the other hand, before scanning the building's start 
	 * point x1, we do not add the height of the building into BIT because it does 
	 * not impact the height yet.
	 * 
	 * Difficulty 2: How to determine whether a point is a key point?
	 * 
	 * If we use the suffix BIT, at coordinate x1, we can easy query max height in 
	 * [x1+1, INFINITE) to exclude all the ending buildings at x1, without adding 
	 * any side effect even if x1 is a starting point. But if you use the regular 
	 * prefix BIT, you will run into the difficulties to exclude the ending builds.
	 * 
	 * Besides the above difficulties, coordinates compression is a typical technique 
	 * we use in BIT/Segment tree
	 * 
	 * Rf :
	 * https://leetcode.com/problems/the-skyline-problem/discuss/433833/explanations-on-difficulties-applying-binary-indexed-treebitfenwick-solution
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61198/My-O(nlogn)-solution-using-Binary-Indexed-Tree(BIT)Fenwick-Tree/62476
	 */
	public List<List<Integer>> getSkyline_Binary_Indexed_Tree(int[][] buildings) {
		List<List<Integer>> ret = new ArrayList<>();
		if (buildings.length == 0)
			return ret;

		List<int[]> points = new ArrayList<>();

		for (int i = 0; i < buildings.length; i++) {
			int[] b = buildings[i];
			points.add(new int[] { b[0], 1, i });
			points.add(new int[] { b[1], 2, i });
		}

		Collections.sort(points, (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
		
		// binary indexed tree
		// stores the max height for each segment, bit[i] is the max height of 
		// segment between point i-1 and i
		int[] bit = new int[points.size() + 1];

		Map<Integer, Integer> index = new HashMap<>();
		for (int i = 0; i < points.size(); i++) {
			index.putIfAbsent(points.get(i)[0], i);
		}

		int prevHeight = -1;

		for (int i = 0; i < points.size(); i++) {
			int[] pt = points.get(i);
			if (pt[1] == 1) {
				// start of a building
				// put height in scope, scope ends when building end
				int[] building = buildings[pt[2]];
				add_BIT(bit, index.get(building[1]), building[2]);
			}
			
			int cur = find_BIT(bit, index.get(pt[0]) + 1);
			
			if (cur != prevHeight) {
				if (ret.size() > 0 && ret.get(ret.size() - 1).get(0) == pt[0]) {
					int curHeight = Math.max(cur, ret.get(ret.size() - 1).get(1));
					ret.get(ret.size() - 1).set(1, curHeight);
				} 
				else {
					// 新增這段來解決下面 test case 的 bug
					// [[1,2,1], [1,2,2], [1,2,3], [1,2,4], [2,3,2], [2,3,4]]
					if (ret.size() > 1 && 
							ret.get(ret.size() - 1).get(1) 
							== ret.get(ret.size() - 2).get(1)) {
						
						ret.remove(ret.size() - 1);
					}
					
					ret.add(Arrays.asList(pt[0], cur));
				}
				prevHeight = cur;
			}
		}

		return ret;
	}

	void add_BIT(int[] bit, int i, int h) {
		while (i > 0) {
			bit[i] = Math.max(bit[i], h);
			i -= (i & -i);
		}
	}

	int find_BIT(int[] bit, int i) {
		int h = 0;
		while (i < bit.length) {
			h = Math.max(h, bit[i]);
			i += (i & -i);
		}
		return h;
	}

	/*
	 * by myself
	 * 
	 * Rf : 第 5 個 pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * 在更新臨界點的高度時，只更新比建築物低的臨界點
	 * 同時翻轉 nested for loop 內外順序：先走臨界點，再走建築物
	 */
	public List<List<Integer>> getSkyline_low_critical_outside(int[][] buildings) {
		// 取出所有建築物的左右端點，它們是臨界點。
		// 高度初始化為 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// 建築物的高度不會變，對建築物高度做一次排序即可
		Arrays.sort(buildings, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return b[2] - a[2];
			}
		});

		// 只需要更新比建築物低的臨界點：
		// 外層先走臨界點，內層才走建築物。
		// 
		// 若臨界點在建築物內 (不包含右端點)，而且比建築物矮，
		// 則更新臨界點高度 (取最大值，找天際線)
		for (Integer[] critical : criticals) {
			for (int[] building : buildings) {
				if (building[2] < critical[1]) {
					break;
				}

				int left = building[0];
				int right = building[1];

				if (critical[0] >= left && critical[0] < right) {
					// 若 building[2] < critical[1] 則會 break
					// 到達這裡可以說明 
					// building[2] >= critical[1]
					// 
					// 所以其實可以簡化為
					// critical[1] = building[2];
					critical[1] = Math.max(critical[1], building[2]);
				}
			}
		}

		// 臨界點依照 x 座標，由小到大排序
		Collections.sort(criticals, new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return a[0] - b[0];
			}
		});

		List<List<Integer>> result = new ArrayList<>();
		int pre = 0;
		for (Integer[] critical : criticals) {
			//System.out.println(Arrays.toString(critical));

			if (critical[1] != pre) {
				result.add(Arrays.asList(critical));
				pre = critical[1];
			}
		}

		return result;
	}
	
	/*
	 * by myself
	 * 
	 * Rf : 第 3 個 pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * 在更新臨界點的高度時，只更新比建築物低的臨界點
	 * 
	 * 若使用 PriorityQueue 來處理臨界點，會 Time Limit Exceeded
	 * https://leetcode.com/submissions/detail/408481072/
	 */
	public List<List<Integer>> getSkyline_low_critical(int[][] buildings) {
		// 取出所有建築物的左右端點，它們是臨界點。
		// 高度初始化為 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// 只需要更新比建築物低的臨界點：
		// 所有建築物都走一遍，若臨界點在建築物內 (不包含右端點)，而且比建築物矮，
		// 則更新臨界點高度 (取最大值，找天際線)
		// 
		// 尋找比建築物低的臨界點：將臨界點排序
		// 將臨界點依照高度，由小到大排序
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			Collections.sort(criticals, (new Comparator<Integer[]>() {
				public int compare(Integer[] a, Integer[] b) {
					return a[1] - b[1];
				}
			}));

			for (int i = 0; i < criticals.size(); i++) {
				Integer[] critical = criticals.get(i);

				if (critical[1] >= building[2]) {
					break;
				}

				if (critical[0] >= left && critical[0] < right) {
					critical[1] = Math.max(critical[1], building[2]);
				}
			}
		}

		// 臨界點依照 x 座標，由小到大排序
		Collections.sort(criticals, new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return a[0] - b[0];
			}
		});

		List<List<Integer>> result = new ArrayList<>();
		int pre = 0;
		for (Integer[] critical : criticals) {
			// System.out.println(Arrays.toString(critical));

			if (critical[1] != pre) {
				result.add(Arrays.asList(critical));
				pre = critical[1];
			}
		}

		return result;
	}
	
	/*
	 * by myself
	 * 
	 * Rf : 第 2 個 pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * skyline 只會在建築物的左端點和右端點改變，因此我們只要找出這些臨界點的高度就可以決定 skyline。
	 */
	public List<List<Integer>> getSkyline_all_critical(int[][] buildings) {
		// 取出所有建築物的左右端點，它們是臨界點。
		// 高度初始化為 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// 更新臨界點的高度：
		// 所有建築物都走一遍，若臨界點在建築物內 (不包含右端點)，則更新臨界點高度 (取最大值，找天際線)
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			for (Integer[] critical : criticals) {
				if (critical[0] >= left && critical[0] < right) {
					critical[1] = Math.max(building[2], critical[1]);
				}
			}
		}

		// 臨界點依照 x 座標，由小到大排序
		Collections.sort(criticals, new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return a[0] - b[0];
			}
		});

		List<List<Integer>> result = new ArrayList<>();
		int pre = 0;
		for (Integer[] critical : criticals) {
			// System.out.println(Arrays.toString(critical));

			if (critical[1] != pre) {
				result.add(Arrays.asList(critical));
				pre = critical[1];
			}
		}

		return result;
	}
	
	/**
	 * 這個做法是 錯 的 !!!
	 * 
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * 建立 heightMap。 將所有建築物的高度投影至 1D array，這個 array 儲存每個座標的 maxHeight。找出轉折點
	 * 錯誤：x 座標會有誤差
	 * 
	 * 輸入：[[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
	 * 正確：[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
	 * 結果：[[2,10],[3,15],[8,12],[13,0],[15,10],[21,8],[24,0]]
	 * 
	 * 錯誤原因：轉折應該發生在建築物右端點，然而 Math.max 卻還是取到這個即將過去的建築物，導致轉折點被往後移一點
	 * 
	 * 以上面為例：
	 * [3,7] 高度 15 的建築物在 7 時即將過去，因此要取比它矮一點的，高度為 12 的建築 [5,12]
	 * 然而在 7 的 Math.max 卻還是 15，所以轉折被往後移至 8，而在 8 的 Math.max 是 12
	 * 
	 * 同理發生在 
	 * 12，[5,12] 高度為 12 的建築
	 * 20，[15,20] 高度為 10 的建築
	 * 
	 * 若不考慮建築物右端點，也就是將內層 for loop 改成
	 * for (int i = left - minX; i < right - minX; i++) {
	 *     hMap[i] = Math.max(hMap[i], building[2]);
	 * }
	 * 
	 * 仍會面臨考慮的 array 太大而導致 Memory Limit Exceeded 或是 Time Limit Exceeded 的問題
	 * 1. 輸入：[[0,2147483647,2147483647]] 
	 * 2. 輸入：[[1,2,1],[2147483646,2147483647,2147483647]]
	 * 
	 * 若建築物的 x 座標是浮點數，這個做法的精度也有問題
	 * https://leetcode.com/problems/the-skyline-problem/discuss/61197/(Guaranteed)-Really-Detailed-and-Good-(Perfect)-Explanation-of-The-Skyline-Problem/147623
	 */
	/*
	public List<List<Integer>> getSkyline(int[][] buildings) {
	    if (buildings == null || buildings.length == 0 || buildings[0] == null) {
            return new ArrayList<>();
        }
        
        int minX = Integer.MAX_VALUE;
        int maxX = -1;
        for (int[] i : buildings) {
            minX = Math.min(minX, i[0]);
            maxX = Math.max(maxX, i[1]);
        }
        
        int[] hMap = new int[maxX - minX + 1];
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            
            for (int i = left - minX; i <= right - minX; i++) {
                hMap[i] = Math.max(hMap[i], building[2]);
            }
        }
        
        List<List<Integer>> result = new ArrayList<>();
        int pre = 0;
        for (int i = 0; i < hMap.length; i++) {
            if (hMap[i] != pre) {
                Integer[] tmp = {i + minX, hMap[i]};
                result.add(Arrays.asList(tmp));
                pre = hMap[i];
            }
        }
        Integer[] last = {maxX, 0};
        result.add(Arrays.asList(last));
        
        return result;
    }
    */
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/the-skyline-problem/discuss/61194/108-ms-17-lines-body-explained
     * https://leetcode.com/problems/the-skyline-problem/discuss/61261/10-line-Python-solution-104-ms
     * https://leetcode.com/problems/the-skyline-problem/discuss/61210/14-line-python-code-straightforward-and-easy-to-understand
     * https://leetcode.com/problems/the-skyline-problem/discuss/61202/My-220ms-divide-and-conquer-solution-in-Python-O(nlogn)
     * https://leetcode.com/problems/the-skyline-problem/discuss/325070/Simple-Python-solutions
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/the-skyline-problem/discuss/61279/My-C%2B%2B-code-using-one-priority-queue-(812-ms)
     * https://leetcode.com/problems/the-skyline-problem/discuss/61273/C%2B%2B-69ms-19-lines-O(nlogn)-clean-solution-with-comments
     * https://leetcode.com/problems/the-skyline-problem/discuss/61222/17-Line-O(n-log-n)-time-O(n)-space-C%2B%2B-Accepted-Easy-Solution-w-Explanations
     * https://leetcode.com/problems/the-skyline-problem/discuss/61323/28-line-of-Code-My-C%2B%2B-solution-(840ms)-O(NlogN)-with-multiset-and-multimap
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/the-skyline-problem/discuss/395923/JavaScript-Easy-and-Straightforward-with-picture-illustrations
	 */

}
