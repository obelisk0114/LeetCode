package OJ0401_0410;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Queue_Reconstruction_by_Height {
	/*
	 * https://discuss.leetcode.com/topic/60394/easy-concept-with-python-c-java-solution
	 * 
	   1. Pick out tallest group of people and sort them in a subarray (S). Since 
	      there's no other groups of people taller than them, therefore each guy's 
	      index will be just as same as his k value.
	      
	   2. For 2nd tallest group (and the rest), insert each one of them into (S) 
	      by k value. So on and so forth.
	      
	 * Rf :
	 * https://discuss.leetcode.com/topic/60981/explanation-of-the-neat-sort-insert-solution
	 * https://discuss.leetcode.com/topic/60437/java-solution-using-priorityqueue-and-linkedlist
	 * 
	 * Simple version : https://discuss.leetcode.com/topic/60366/simple-commented-java-solution-using-priority-queue
	 */
	public int[][] reconstructQueue(int[][] people) {
		// pick up the tallest guy first
		// when insert the next tall guy, just need to insert him into kth position
		// repeat until all people are inserted into list
		Arrays.sort(people, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return o1[0] != o2[0] ? -o1[0] + o2[0] : o1[1] - o2[1];
			}
		});
		List<int[]> res = new ArrayList<>();
		for (int[] cur : people) {
			res.add(cur[1], cur);
		}
		return res.toArray(new int[people.length][]);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/61528/java-o-n-2-solution-by-picking-the-front-of-the-queue-one-by-one
	 * 
	 * We know in the (h, k) model, the front element of the "ordered" queue will be 
	 * the one with k = 0 and minimum h value in the "unordered" queue.
	 * 
	 * If we have found the first front element. We want to "remove" it from the 
	 * "ordered" queue so that we are up to find the "next" front element.
	 * 
	 * For those with height greater than that of the front element, removing the 
	 * front element won't matter. But for those with height no more than that of 
	 * the front element, removing the front should decrease their k value by 1.
	 */
	public int[][] reconstructQueue_pick_one(int[][] people) {
		int n = people.length;
		int[][] copy = new int[n][];
		int[][] res = new int[n][];

		for (int i = 0; i < n; i++)
			copy[i] = Arrays.copyOf(people[i], 2);

		for (int i = 0; i < n; i++) {
			int k = -1;

			// pick the front element
			for (int j = 0; j < n; j++) {
				if (copy[j][1] == 0 && (k == -1 || copy[j][0] < copy[k][0]))
					k = j;
			}

			res[i] = people[k]; // set the result

			// modify the k values of those with smaller or equal h values
			for (int j = 0; j < n; j++) {
				if (copy[j][0] <= copy[k][0])
					copy[j][1]--;
			}
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/65045/java-solution-using-array-sort-and-greedy
	 * 
	 * Sort array by the height. (The smaller height has the higher priority. 
	 * But If people have the same height, for example [5,0] and [5,2], we 
	 * should consider [5,2] first, because it has more people higher or 
	 * equal to it, we can treat it is a little shorter than [5,0]).
	 */
	public int[][] reconstructQueue_sort_number_change_shortest_height(int[][] people) {
		Arrays.sort(people, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				if (a[0] != b[0]) {
					return a[0] - b[0];
				} else {
					return b[1] - a[1];
				}
			}
		});
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < people.length; i++) {
			list.add(i);
		}
		int[][] res = new int[people.length][2];
		for (int i = 0; i < people.length; i++) {
			int index = list.get(people[i][1]);
			res[index][0] = people[i][0];
			res[index][1] = people[i][1];
			list.remove(people[i][1]);
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/60423/java-o-n-2-greedy-solution
	 * 
	 * We always choose the current shortest height (so we need to sort input first), 
	 * and then try to put it into the right position. 
	 * We simply scan from the left and count how many persons are 
	 * really >= its own height. Then we put the person into the empty slot.
	 */
	public int[][] reconstructQueue_sort_from_shortest_height(int[][] people) {
		if (people == null || people.length <= 1) {
			return people;
		}
		Arrays.sort(people, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0];
			}
		});
		int n = people.length;
		int[][] ret = new int[n][];
		for (int i = 0; i < n; i++) {
			for (int j = 0, ahead = 0; j < n; j++) {
				if (ahead < people[i][1]) {
					// Since shortest height first, the remaining spaces are for taller one.
					ahead += (ret[j] == null || ret[j][0] >= people[i][0]) ? 1 : 0;
				} else if (ret[j] == null) {
					ret[j] = people[i];
					break;
				}
			}
		}
		return ret;
	}
	
	// https://discuss.leetcode.com/topic/60550/o-n-sqrt-n-solution
	
	/*
	 * https://discuss.leetcode.com/topic/60694/o-nlogn-binary-index-tree-c-solution
	 * 
	 * Rf : 
	 * http://www.csie.ntnu.edu.tw/~u91029/Sequence.html#4
	 * https://en.wikipedia.org/wiki/Fenwick_tree
	 */
}
