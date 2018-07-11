package OJ0651_0660;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Split_Array_into_Consecutive_Subsequences {
	/*
	 * https://leetcode.com/problems/split-array-into-consecutive-subsequences/discuss/106496/Java-O(n)-Time-O(n)-Space
	 * 
	 * 1. We iterate through the array once to get the frequency of all the elements 
	 *    in the array
	 * 2. We iterate through the array once more and for each element we either see 
	 *    if it can be appended to a previously constructed consecutive sequence or 
	 *    if it can be the start of a new consecutive sequence. If neither are true, 
	 *    then we return false.
	 * 
	 * appendfreq is used to track previous consecutive sequences next elements' 
	 * values. If current element can be next element of one of previous consecutive 
	 * sequences, it means we can append it to that sequence. We don't need to worry 
	 * about whether we can use this element to be a new start point of a new 
	 * consecutive sequence, that's because even though the current element can be a 
	 * new start point of a consecutive sequence, we can simply append those 
	 * consecutive elements following this current element at the end of the previous 
	 * consecutive sequence.
	 * 
	 * Rf :
	 * leetcode.com/problems/split-array-into-consecutive-subsequences/discuss/106496/Java-O(n)-Time-O(n)-Space/108834
	 * https://leetcode.com/problems/split-array-into-consecutive-subsequences/discuss/106516/Simple-C++-Greedy-O(nlogn)-Solution-(with-explanation)
	 */
	public boolean isPossible(int[] nums) {
		Map<Integer, Integer> freq = new HashMap<>(), appendfreq = new HashMap<>();
		for (int i : nums)
			freq.put(i, freq.getOrDefault(i, 0) + 1);
		
		for (int i : nums) {
			if (freq.get(i) == 0)
				continue;
			else if (appendfreq.getOrDefault(i, 0) > 0) {
				appendfreq.put(i, appendfreq.get(i) - 1);
				appendfreq.put(i + 1, appendfreq.getOrDefault(i + 1, 0) + 1);
			} 
			else if (freq.getOrDefault(i + 1, 0) > 0 && freq.getOrDefault(i + 2, 0) > 0) {
				freq.put(i + 1, freq.get(i + 1) - 1);
				freq.put(i + 2, freq.get(i + 2) - 1);
				appendfreq.put(i + 3, appendfreq.getOrDefault(i + 3, 0) + 1);
			} 
			else
				return false;
			
			freq.put(i, freq.get(i) - 1);
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/split-array-into-consecutive-subsequences/discuss/106495/Java-O(n)-time-and-O(1)-space-solution-greedily-extending-shorter-subsequence
	 * 
	 * Since all the subsequences are ending at m - 1, they can only differ by length. 
	 * Now what if two subsequences have the same length? This suggests that we also 
	 * need to keep track of the number of appearance of each length. Therefore in 
	 * summary, we need to maintain information of subsequences ending at m - 1 with 
	 * various lengths and their corresponding number of appearance.
	 * We don't really care about subsequences of length >= 3, since they already meet 
	 * the required condition.
	 * 
	 * For each distinct element ele in the input array, we only need to maintain 
	 * three pieces of information: the number of consecutive subsequences ending at 
	 * ele with length of 1, length of 2 and length >= 3.
	 * 
	 * Let cur be the element currently being examined and cnt as its number of 
	 * appearance. pre is the element processed immediately before cur. The number of 
	 * consecutive subsequences ending at pre with length of 1, length of 2 and 
	 * length >= 3 are denoted as p1, p2 and p3
	 * 
	 * Let c1, c2, c3 be the number of consecutive subsequences ending at cur with 
	 * length of 1, length of 2 and length >= 3, respectively
	 * 
	 * 1. cur != pre + 1: for this case, cur cannot be added to any consecutive 
	 *    subsequences ending at pre, therefore, we must have p1 == 0 && p2 == 0; 
	 *    otherwise the input array cannot be split into consecutive subsequences of 
	 *    length >= 3. We will have c1 = cnt, c2 = 0, c3 = 0, which means we only have 
	 *    consecutive subsequence ending at cur with length of 1 and its number given 
	 *    by cnt.
	 * 
	 * 2. cur == pre + 1: for this case, cur can be added to consecutive subsequences 
	 *    ending at pre and thus extend those subsequences. But priorities should be 
	 *    given to those with length of 1 first, then length of 2 and lastly 
	 *    length >= 3. Also we must have cnt >= p1 + p2; otherwise the input array 
	 *    cannot be split into consecutive subsequences of length >= 3. We will have: 
	 *    c2 = p1, c3 = p2 + min(p3, cnt - (p1 + p2)), c1 = max(cnt - (p1 + p2 + p3), 0). 
	 *    The meaning is as follows: first adding cur to the end of subsequences of 
	 *    length 1 will make them subsequences of length 2, and we have p1 such 
	 *    subsequences, therefore c2 = p1. Then adding cur to the end of subsequences 
	 *    of length 2 will make them subsequences of length 3, and we have p2 such 
	 *    subsequences, therefore c3 is at least p2. If cnt > p1 + p2, we can add the 
	 *    remaining cur to the end of subsequences of length >= 3 to make them even 
	 *    longer subsequences. The number of such subsequences is the smaller one of 
	 *    p3 and cnt - (p1 + p2). In total, c3 = p2 + min(p3, cnt - (p1 + p2)). If 
	 *    cnt > p1 + p2 + p3, then we still have remaining cur that cannot be added to 
	 *    any subsequences. These residue cur will form subsequences of length 1, 
	 *    hence c1 = max(cnt - (p1 + p2 + p3), 0).
	 * 
	 * For either case, we need to update: pre = cur, p1 = c1, p2 = c2, p3 = c3 after 
	 * processing the current element. When all the elements are done, we check the 
	 * values of p1 and p2. The input array can be split into consecutive subsequences 
	 * of length >= 3 if and only if p1 == 0 && p2 == 0.
	 */
	public boolean isPossible2(int[] nums) {
		int pre = Integer.MIN_VALUE, p1 = 0, p2 = 0, p3 = 0;
		int cur = 0, cnt = 0, c1 = 0, c2 = 0, c3 = 0;

		for (int i = 0; i < nums.length; pre = cur, p1 = c1, p2 = c2, p3 = c3) {
			cur = nums[i];
			cnt = 0;
			
			while (i < nums.length && cur == nums[i]) {
				cnt++;
				i++;
			}

			if (cur != pre + 1) {
				if (p1 != 0 || p2 != 0)
					return false;
				
				c1 = cnt;
				c2 = 0;
				c3 = 0;
			} 
			else {
				if (cnt < p1 + p2)
					return false;
				
				c1 = Math.max(0, cnt - (p1 + p2 + p3));
				c2 = p1;
				c3 = p2 + Math.min(p3, cnt - (p1 + p2));
			}
		}

		return (p1 == 0 && p2 == 0);
	}
	
	/*
	 * https://leetcode.com/articles/split-array-into-consecutive-subsequences/
	 * 
	 * In general, when considering a chain of consecutive integers x, we must have 
	 * C = count[x+1] - count[x] sequences start at x+1 when C > 0, and -C sequences 
	 * end at x if C < 0. Even if there are more endpoints on the intervals we draw, 
	 * there must be at least this many endpoints.
	 * 
	 * nums = [10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13]; count[11] - count[10] = 2 
	 * and count[13] - count[12] = -3 show that two sequences start at 11, and three 
	 * sequences end at 12.
	 * 
	 * count > prev_count then we will add this many t's to starts; and if 
	 * count < prev_count then we will attempt to pair starts.popleft() with t-1.
	 */
	public boolean isPossible_start_end_match(int[] nums) {
		Integer prev = null;
		int prevCount = 0;
		Queue<Integer> starts = new LinkedList<>();
		int anchor = 0;
		for (int i = 0; i < nums.length; ++i) {
			int t = nums[i];
			if (i == nums.length - 1 || nums[i + 1] != t) {
				int count = i - anchor + 1;
				
				// Can't connect to previous one
				if (prev != null && t - prev != 1) {
					while (prevCount > 0) {
						// prev is the end point. Pair it with start point.
						if (prev < starts.poll() + 2)
							return false;
						
						prevCount--;
					}
					
					starts.add(t);
					prev = t;
					prevCount = count;
					anchor = i + 1;
				}

				// if (prev == null || t - prev == 1): Can connect to previous one
				else {
					// previous count is larger than current count. The excessive
					// parts will become the end points.
					while (prevCount > count) {
						// Pair it with start point.
						if (prev < starts.poll() + 2)
							return false;
						
						prevCount--;
					}
					
					// current count is larger than previous count. The excessive
					// parts will become the start points.
					while (prevCount < count) {
						// Add to start queue
						starts.add(t);
						prevCount++;
					}
					
					prev = t;
					prevCount = count;
					anchor = i + 1;
				}
			}
		}

		while (prevCount > 0) {
			if (nums[nums.length - 1] < starts.poll() + 2)
				return false;
			
			prevCount--;
		}
		return true;
	}
	
	/*
	 * The following function and class are from this link.
	 * https://leetcode.com/problems/split-array-into-consecutive-subsequences/discuss/106520/Easy-to-understand.-Java-using-heap-and-greedy-O(nlogn)-time-and-O(n)-space-44ms-beats-87
	 * 
	 * Tuple class represents each sequence, num is the tail or the max number of a 
	 * sequence and count is the length of a sequence.
	 * 
	 * we want the sequence with minimum 'tail' on top of the heap, if they all have 
	 * the same 'tail', the one with minimum length should be on top.
	 * 
	 * 1. it can be appended to one of the sequences when i == pq.peek().num + 1. eg. we 
	 *    have a sequence of 1,2 and i is 3 we need to update the num and count:
	 * 
	 * 2. it's the head of a new sequence when i == pq.peek().num. eg. we have a 
	 *    sequence of 1,2, and i=2 we can just simply add it to the heap.
	 * 
	 * 3. This happens when i > pq.peek().num + 1. eg. we have 1,2 and i = 6. In this 
	 *    case, it can be the head of a new sequence, or it can be appended to some 
	 *    other sequences.
	 *    So we can try first to find the sequence we can append i to it by popping 
	 *    sequences from the heap one by one until we find the one with num == i-1. If
	 *    we find one, go to case 1. If we can't find one(heap is empty), it means i 
	 *    is the head of a new sequence which is case 2.
	 */
	public boolean isPossible_PriorityQueue(int[] nums) {
		PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>(1, new Comparator<Tuple>() {
			@Override
			public int compare(Tuple x, Tuple y) {
				if (x.num != y.num)
					return x.num - y.num;
				else
					return x.count - y.count;
			}
		});

		for (int i : nums) {
			if (pq.isEmpty() || pq.peek().num == i)
				pq.offer(new Tuple(i, 1));
			else {
				while (!pq.isEmpty() && i > pq.peek().num + 1) {
					if (pq.poll().count < 3)
						return false;
				}
				
				if (pq.isEmpty()) {
					pq.offer(new Tuple(i, 1));
				} 
				else {
					Tuple pk = pq.poll();
					pk.num = i;
					pk.count++;
					pq.offer(pk);
				}
			}
		}
		
		while (!pq.isEmpty()) {
			if (pq.poll().count < 3)
				return false;
		}
		return true;
	}

	class Tuple {
		int num, count;

		public Tuple(int num, int count) {
			this.num = num;
			this.count = count;
		}
	}

}
