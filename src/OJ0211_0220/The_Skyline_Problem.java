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
 * skyline �u�|�b�ؿv���������I�M�k���I���ܡA�]���ڭ̥u�n��X�o���{���I�����״N�i�H�M�w skyline�C 
 * �b�{���I�i��V�k����V���y�A�C�@�I���̤j�ȡA�i�H�o��ѻڽu�C�`�N�G���]�t�k���I
 * �ѷӵ{���X�GgetSkyline_all_critical(int[][] buildings)
 * 
 * �ѥH�W�L�{�i�o�{�A�ڭ̥u�ݭn��s��ؿv���C���{���I
 * �ѷӵ{���X�GgetSkyline_low_critical(int[][] buildings)
 * 
 * �ڭ̥i�H���սմ� nested for loop �����ǡA�o�˦����|�W�[�Ĳv
 * �ѷӵ{���X�GgetSkyline_low_critical_outside(int[][] buildings)
 * 
 * ��n�����k�O�N�{���I�̷� x �y�СA�Ѥp��j�Ƨ�
 * �ѥ��ӥk�A�V�k��V���y�o���{���I�A�P�ɨϥΤ@�� set ���x�s��e�����īؿv��
 * ��F�@���{���I�ɡA��s�o�� set�A�ñN�o�� set ���ؿv���ᤩ�o���{���I
 * �p���C���{���I�N�i�H���D�Ҧ��񥦰����ؿv��
 * for each critical point c
 *     c.y gets the height of the tallest rectangle over c
 * 
 * �̲׸Ѫk�G
 * 1. �N�{���I�̷� x �y�СA�Ѥp��j�Ƨ�
 * 2. �ѥ��ܥk scan �o���{���I
 * 3-1. �J��ؿv���������I�A�N�ؿv���[�J maxHeap�A�èϥΰ��ק@�� key
 * 3-2. �J��ؿv�����k���I�A�N�ؿv���q maxHeap ������ (�ݭn�B�~���ШӰO��)
 * 4. ����ɭԷ�ڭ̹J���{���I�A��s�� maxHeap ����A�N maxHeap.top() �@���{���I������
 * 
 * ��i�G
 * �����B�~���ШӰO�� maxHeap�A�J��ؿv�����k���I�A���_ pop ���� maxHeap.top() �O���Ī�
 * �o�� maxHeap ���i��]�t�w�g�L�h���ؿv���A
 * ���O�ڭ̥u�� maxHeap.top()�A�]���u�n maxHeap.top() �O���Ī��N�n
 * �ѷӵ{���X�GgetSkyline_building_pq_check_top(int[][] buildings)
 */

/*
 * �Ѫk�G�GDivide-and-Conquer (���� merge sort)
 * 
 * Ch4Ch5 p.20
 * 
 * �Y�ڭ̤w�� �e n-1 �� building ���X�ֵ��G�A�p��[�J�� n �� Bn (Ln, Hn, Rn)�H
 * 
 * ����X Ln ���e������ x_1�A���ۨ��X�M Bn ���|������
 * (x_1, h1), (x_2, h2), ..., (x_m, hm)�A�䤤 x_m < Rn �ӥB x_m+1 >= Rn (�Ϊ� x_m �O�̫�@��)
 * 
 * �Y hi < Hn�A�h�o�@�q�|�Q Bn ������ Hn �һ\��
 * �Y���s�b x_m+1�A�h�i�H�ק�̫�@�q x_m �� (x_m, Hn) �A�÷s�W�B�~���@�q (Rn, 0)
 * �ڭ̦P�ɤ]�ˬd�۾F����q���׬O�_�ۦP�A�Y�ۦP�h�X�֦b�@�_
 * 
 * �ϥ� Divide-and-Conquer ��i�GT(n) = 2T(n/2) + O(n). T(n) = O(n log n).
 * �ڭ̥i�H���j�N�Ҧ��ؿv������ 2 ��æX��
 * 
 * 2 �� skylines A = (a1, ha1, a2, ha2, ..., an, 0); B = (b1, hb1, b2, hb2, ..., bm, 0)
 * �X�֥X (c1, hc1, c2, hc2, ..., c_n+m, 0).
 * �ڭ̨ϥ� CurH1 �M CurH2 (�o�ǬO�J�� 2 �� lists ���e������) �h�x�s�{�b�o 2 �� skylines ������
 * �����o 2 �� skylines �� head entries (CurH1, CurH2) �ɡA�ڭ̷s�W�@�q (���b output skyline)
 * x �y�ЬO minimum of the entries�� x �y�СA���׬� maximum of CurH1 and CurH2.
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
		
		// �u�n���������k���I�|���B�z���A�~�����
		while (i < buildings.length || !pq.isEmpty()) {
			// �U�@������I�O�����I (�i�J�I)�C�Y���k���I�ۦP�A���[�J
			// �Y PriorityQueue ���šA�]�[�J
			if (pq.isEmpty() || 
					(i < buildings.length && buildings[i][0] <= pq.peek()[1])) {
				
				xPosition = buildings[i][0];
				
				// �Y���᪺�ؿv�������I�]�ۦP�A�~��[�J
				while (i < buildings.length && xPosition == buildings[i][0]) {
					pq.offer(buildings[i]);
					i++;
				}
			} 
			// �U�@������I�O�k���I (���}�I)
			else {
				xPosition = pq.peek()[1];
				
				// �Y�k���I <= �{�b x �y�СA��ܳo�ӫؿv���w�g���}�F�A�N������
				while (!pq.isEmpty() && pq.peek()[1] <= xPosition)
					pq.poll();
			}

			// �o�� x �y�гB�z�����A�D����
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
	 * 1. ���X�ؿv�������k���I�����{���I�A�N���̷̨� x �y�СA�Ѥp��j�Ƨ�
	 * 2. �ѥ��ܥk scan �o���{���I
	 * 3. ��s maxHeap
	 *   3-1. �J��ؿv���������I�A�N�ؿv���[�J maxHeap�A�èϥΰ��ק@�� key
	 *   3-2. �Y maxHeap.top() ���k���I <= �{�b�n�B�z���{���I x �y�СA�N������ maxHeap
	 * 4. ���{���I�����׬� maxHeap.top() ������
	 * 
	 * �`�N test cases:
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
		// ���X�Ҧ��ؿv�������k���I�A���̬O�{���I�C
		// ���ת�l�Ƭ� 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// �N�{���I�̷� x �y�СA�Ѥp��j�Ƨ�
		Collections.sort(criticals, new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return a[0] - b[0];
			}
		});

		// �x�s�ؿv���� maxHeap�A�������
		Queue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return b[2] - a[2];
			}
		});

		// scan �{���I
		// ��s maxHeap �ç�X���{���I������
		int i = 0;
		for (Integer[] critical : criticals) {
			
			// �ؿv���������I <= �{���I x �y�СA��J maxHeap
			while (i < buildings.length && buildings[i][0] <= critical[0]) {
				pq.offer(buildings[i]);
				i++;
			}

			// maxHeap.top() ���k���I <= �{���I x �y�СA�N������ maxHeap
			while (!pq.isEmpty() && pq.peek()[1] <= critical[0]) {
				pq.poll();
			}

			// �Y maxHeap �����šA�i�H��s����
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
	 * x-coordinate is the minimum of the entries�� x-coordinates and whose height is 
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
			
			// �M�e�@���I�����פ��P�~�[�J
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
				
				// j �n�q i �}�l�A�]���b���h for loop �� height ���B�z
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

		// 1. x �Ѥp�ƨ�j
		// 2. closing �� false �ƨ� true
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

		// �̫�@�� end �S���Q���A��ʥ[�J
		// �S���ؿv���~���Υ[�J
		// �Y�� if �����A�h�b�̫e���n�[�W empty array ���P�_
		if (endpointList.size() > 0) {
			result.add(Arrays.asList(
					new Integer[] { endpointList.get(endpointList.size() - 1), 0 }));
		}

		return result;
	}

	private Node buildNode_segment_tree(int start, int end) {
		// �]�i�H
		// if (start > end)
		if (start >= end) {
			return null;
		} 
		
		// �u�]�t start�A���� end
		Node result = new Node(start, end);
		if (start + 1 < end) {
			int center = start + (end - start) / 2;
			
			result.left = buildNode_segment_tree(start, center);
			result.right = buildNode_segment_tree(center, end);
		}
		
		return result;
	}

	private void add_segment_tree(Node node, int start, int end, int height) {
		// �H�ؿv�� start, end, height ��s node �H�Τl node
		// �]���W�L�d��M���פ����N�����s
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
			
			// �Q�Ϋe���� height < node.height �ӰŪK 
			// �ٲ����M accept�A���O�|�ܺC
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
	 * �I�쥪���I�A�N�b�k���I�إ� suffix range max (���]�t�k���I)
	 * �ϥ� BIT �i�H�u�� log n ���I�� max �ӱo�� suffix max
	 * 
	 * �N��l BIT ��s�B�D�M����V�ϹL�ӡA�i�H�ݦ��O suffix range max
	 * 
	 * �Y���׬� 13�A�D 5 �� 13 �� max
	 * Max(5, 13) = Max(Max(5, 6), Max(6, 8), Max(8, �̫�@�� 13))
	 * 
	 * ��s 5
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
					// �s�W�o�q�ӸѨM�U�� test case �� bug
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
	 * Rf : �� 5 �� pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * �b��s�{���I�����׮ɡA�u��s��ؿv���C���{���I
	 * �P��½�� nested for loop ���~���ǡG�����{���I�A�A���ؿv��
	 */
	public List<List<Integer>> getSkyline_low_critical_outside(int[][] buildings) {
		// ���X�Ҧ��ؿv�������k���I�A���̬O�{���I�C
		// ���ת�l�Ƭ� 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// �ؿv�������פ��|�ܡA��ؿv�����װ��@���ƧǧY�i
		Arrays.sort(buildings, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return b[2] - a[2];
			}
		});

		// �u�ݭn��s��ؿv���C���{���I�G
		// �~�h�����{���I�A���h�~���ؿv���C
		// 
		// �Y�{���I�b�ؿv���� (���]�t�k���I)�A�ӥB��ؿv���G�A
		// �h��s�{���I���� (���̤j�ȡA��ѻڽu)
		for (Integer[] critical : criticals) {
			for (int[] building : buildings) {
				if (building[2] < critical[1]) {
					break;
				}

				int left = building[0];
				int right = building[1];

				if (critical[0] >= left && critical[0] < right) {
					// �Y building[2] < critical[1] �h�| break
					// ��F�o�̥i�H���� 
					// building[2] >= critical[1]
					// 
					// �ҥH���i�H²�Ƭ�
					// critical[1] = building[2];
					critical[1] = Math.max(critical[1], building[2]);
				}
			}
		}

		// �{���I�̷� x �y�СA�Ѥp��j�Ƨ�
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
	 * Rf : �� 3 �� pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * �b��s�{���I�����׮ɡA�u��s��ؿv���C���{���I
	 * 
	 * �Y�ϥ� PriorityQueue �ӳB�z�{���I�A�| Time Limit Exceeded
	 * https://leetcode.com/submissions/detail/408481072/
	 */
	public List<List<Integer>> getSkyline_low_critical(int[][] buildings) {
		// ���X�Ҧ��ؿv�������k���I�A���̬O�{���I�C
		// ���ת�l�Ƭ� 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// �u�ݭn��s��ؿv���C���{���I�G
		// �Ҧ��ؿv�������@�M�A�Y�{���I�b�ؿv���� (���]�t�k���I)�A�ӥB��ؿv���G�A
		// �h��s�{���I���� (���̤j�ȡA��ѻڽu)
		// 
		// �M���ؿv���C���{���I�G�N�{���I�Ƨ�
		// �N�{���I�̷Ӱ��סA�Ѥp��j�Ƨ�
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

		// �{���I�̷� x �y�СA�Ѥp��j�Ƨ�
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
	 * Rf : �� 2 �� pseudo code
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * skyline �u�|�b�ؿv���������I�M�k���I���ܡA�]���ڭ̥u�n��X�o���{���I�����״N�i�H�M�w skyline�C
	 */
	public List<List<Integer>> getSkyline_all_critical(int[][] buildings) {
		// ���X�Ҧ��ؿv�������k���I�A���̬O�{���I�C
		// ���ת�l�Ƭ� 0
		List<Integer[]> criticals = new ArrayList<>();
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			criticals.add(new Integer[] { left, 0 });
			criticals.add(new Integer[] { right, 0 });
		}

		// ��s�{���I�����סG
		// �Ҧ��ؿv�������@�M�A�Y�{���I�b�ؿv���� (���]�t�k���I)�A�h��s�{���I���� (���̤j�ȡA��ѻڽu)
		for (int[] building : buildings) {
			int left = building[0];
			int right = building[1];

			for (Integer[] critical : criticals) {
				if (critical[0] >= left && critical[0] < right) {
					critical[1] = Math.max(building[2], critical[1]);
				}
			}
		}

		// �{���I�̷� x �y�СA�Ѥp��j�Ƨ�
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
	 * �o�Ӱ��k�O �� �� !!!
	 * 
	 * https://briangordon.github.io/2014/08/the-skyline-problem.html
	 * 
	 * �إ� heightMap�C �N�Ҧ��ؿv�������ק�v�� 1D array�A�o�� array �x�s�C�Ӯy�Ъ� maxHeight�C��X����I
	 * ���~�Gx �y�з|���~�t
	 * 
	 * ��J�G[[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
	 * ���T�G[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
	 * ���G�G[[2,10],[3,15],[8,12],[13,0],[15,10],[21,8],[24,0]]
	 * 
	 * ���~��]�G������ӵo�ͦb�ؿv���k���I�A�M�� Math.max �o�٬O����o�ӧY�N�L�h���ؿv���A�ɭP����I�Q���Ჾ�@�I
	 * 
	 * �H�W�����ҡG
	 * [3,7] ���� 15 ���ؿv���b 7 �ɧY�N�L�h�A�]���n���񥦸G�@�I���A���׬� 12 ���ؿv [5,12]
	 * �M�Ӧb 7 �� Math.max �o�٬O 15�A�ҥH���Q���Ჾ�� 8�A�Ӧb 8 �� Math.max �O 12
	 * 
	 * �P�z�o�ͦb 
	 * 12�A[5,12] ���׬� 12 ���ؿv
	 * 20�A[15,20] ���׬� 10 ���ؿv
	 * 
	 * �Y���Ҽ{�ؿv���k���I�A�]�N�O�N���h for loop �令
	 * for (int i = left - minX; i < right - minX; i++) {
	 *     hMap[i] = Math.max(hMap[i], building[2]);
	 * }
	 * 
	 * ���|���{�Ҽ{�� array �Ӥj�ӾɭP Memory Limit Exceeded �άO Time Limit Exceeded �����D
	 * 1. ��J�G[[0,2147483647,2147483647]] 
	 * 2. ��J�G[[1,2,1],[2147483646,2147483647,2147483647]]
	 * 
	 * �Y�ؿv���� x �y�ЬO�B�I�ơA�o�Ӱ��k����פ]�����D
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
