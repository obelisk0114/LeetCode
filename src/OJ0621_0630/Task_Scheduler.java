package OJ0621_0630;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

public class Task_Scheduler {
	/*
	 * https://leetcode.com/problems/task-scheduler/discuss/176214/Java-Solution.-Without-re-modify-any-existing-variables-in-magic-if-branch.-O(N)-time-with-O(1)-spac
	 * 
	 * A B _
	 * A B _ 
	 * A B _ 
	 * A B _
	 * A B
	 * 
	 * Any blank spaces can be filled top-to-bottom, left-to-right in descending order 
	 * of letter frequency. Since we order by frequency, there will never be too many 
	 * of one type of element for a column (so max one per row), and by this 
	 * construction we're safe from elements coming too close.
	 * 
	 * If the tasks left are more than the slots, instead of thinking to append them 
	 * after the last A, we should expand the length of row (or the distance).
	 * 
	 * (AAABBCCDDE)   => most frequency: A
	 * Axx | xx... |  => ABC | DE
	 * Axx | x... |   => ABC | D
	 * A              => A
	 * 
	 * The length of each row does not necessarily to be equal. We shall also fill the 
	 * rest elements from top to bottom until we use all of them. All the elements 
	 * will still keep a safe distance from each other. So there is a guarantee that 
	 * if we fill all the slots in the first step, we can always expand that construct 
	 * to fill all elements without creating new idles.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/task-scheduler/discuss/760729/Simple-explanation-for-optimal-formula.
	 * https://leetcode.com/problems/task-scheduler/discuss/176214/Java-Solution.-Without-re-modify-any-existing-variables-in-magic-if-branch.-O(N)-time-with-O(1)-spac/615067
	 * https://leetcode.com/problems/task-scheduler/discuss/761070/Python-or-Heavily-visualized-%2B-Detailed-explanation
	 * https://leetcode.com/problems/task-scheduler/discuss/104500/Java-O(n)-time-O(1)-space-1-pass-no-sorting-solution-with-detailed-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/task-scheduler/discuss/104495/Java-O(n)-solution-beats-99.76-use-only-array-easy-understanding
	 */
	public int leastInterval_calculate(char[] tasks, int n) {
		Map<Character, Integer> map = new HashMap<>();
		for (char c : tasks) {
			map.put(c, map.getOrDefault(c, 0) + 1);
		}

		// Most frequent task.
		int max = 0;
		for (int val : map.values()) {
			max = Math.max(val, max);
		}

		// how many tasks that has the same frequency as the top frequent task.
		// (include itself)
		int p = 0;
		for (int val : map.values()) {
			if (val == max) {
				p++;
			}
		}

		// Totally intervals to fill out all empty space.
		int total = (max - 1) * (n + 1) + p;

		// After fill out all empty space, there are still some tasks that I have not
		// use them.
		if (total < tasks.length) {
			return tasks.length;
		} 
		// Task is not enough, need some idles.
		else {
			return total;
		}
	}
	
	/*
	 * https://leetcode.com/problems/task-scheduler/discuss/104501/Java-PriorityQueue-solution-Similar-problem-Rearrange-string-K-distance-apart
	 * 
	 * Similar to - https://leetcode.com/problems/rearrange-string-k-distance-apart
	 * 
	 * 0. To work on the same task again, CPU has to wait for time n, therefore we can 
	 *    think of as if there is a cycle, of time n+1, regardless whether you 
	 *    schedule some other task in the cycle or not.
	 *    
	 * 1. To avoid leave the CPU with limited choice of tasks and having to sit there 
	 *    cooling down frequently at the end, it is critical the keep the diversity of 
	 *    the task pool for as long as possible.
	 *    
	 * 2. In order to do that, we should try to schedule the CPU to always try round 
	 *    robin between the most popular tasks at any time.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/task-scheduler/discuss/104493/C%2B%2B-Java-Clean-Code-Priority-Queue
	 * https://leetcode.com/problems/task-scheduler/discuss/104501/Java-PriorityQueue-solution-Similar-problem-Rearrange-string-K-distance-apart/120103
	 * https://leetcode.com/problems/task-scheduler/discuss/104501/Java-PriorityQueue-solution-Similar-problem-Rearrange-string-K-distance-apart/296445
	 * https://leetcode.com/problems/task-scheduler/discuss/104501/Java-PriorityQueue-solution-Similar-problem-Rearrange-string-K-distance-apart/176627
	 */
	public int leastInterval_PriorityQueue(char[] tasks, int n) {
		// map key is TaskName, and value is number of times to be executed.
		Map<Character, Integer> map = new HashMap<>();
		for (int i = 0; i < tasks.length; i++) {
			map.put(tasks[i], map.getOrDefault(tasks[i], 0) + 1);
		}
		
		// frequency sort
		// If 2 tasks have same frequency, we use order of keys to compare. (stable)
		// It would matter only if they are asking for order itself.
		PriorityQueue<Map.Entry<Character, Integer>> q = new PriorityQueue<>(
				(a, b) -> a.getValue() != b.getValue() ? 
						b.getValue() - a.getValue() : a.getKey() - b.getKey());

		q.addAll(map.entrySet());

		int count = 0;
		while (!q.isEmpty()) {
			// for each interval
			int k = n + 1;
			
			// list used to update queue
			List<Map.Entry<Character, Integer>> tempList = new ArrayList<>();
			
			// cycle process
			while (k > 0 && !q.isEmpty()) {
				// most frequency task
				Map.Entry<Character, Integer> top = q.poll();
				
				// decrease frequency, meaning it got executed
				top.setValue(top.getValue() - 1);
				
				// collect task to add back to queue
				tempList.add(top);
				
				k--;
				
				// successfully executed task
				count++;
			}

			// update the value in the map
			for (Map.Entry<Character, Integer> e : tempList) {
				// add valid tasks
				if (e.getValue() > 0)
					q.add(e);
			}

			if (q.isEmpty())
				break;
			
			// if k > 0, then it means we need to be idle
			count = count + k;
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/task-scheduler/discuss/104496/concise-Java-Solution-O(N)-time-O(26)-space
	 * 
	 * First consider the most frequent characters, we can determine their relative 
	 * positions first and use them as a frame to insert the remaining less frequent 
	 * characters.
	 * 
	 * Let F be the set of most frequent chars with frequency k. We can create k 
	 * chunks, each chunk is identical and is a string consists of chars in F in a 
	 * specific fixed order.
	 * Append the less frequent characters to the end of each chunk of the first k-1 
	 * chunks sequentially and round and round, then join the chunks and keep their 
	 * heads' relative distance from each other to be at least n.
	 * 
	 * AAAACCCCBBBEEFFGG 3, here X represents a space gap: "AC" is a frame
	 * 'B' has higher frequency than the other characters, insert it first.
	 * each time try to fill the k-1 gaps as full or evenly as possible.
	 * Frame: "ACXXXACXXXACXXXAC"
	 * insert 'B': "ACBXXACBXXACBXXAC"
	 * insert 'E': "ACBEXACBEXACBXXAC"
	 * insert 'F': "ACBEFACBEXACBFXAC"
	 * insert 'G': "ACBEFACBEGACBFGAC"
	 * 
	 * ACCCEEE 2
	 * 3 identical chunks "CE", "CE CE CE" <-- this is a frame
	 * Begin to insert 'A' --> "CEACE CE"
	 * result is (c[25] - 1) * (n + 1) + 25 -i = 2 * 3 + 2 = 8
	 * 
	 * Fill all chunk but the last one, the length is (c[25] - 1) * (n + 1). Then fill 
	 * the last chunk, then length is 25 - i.
	 * 
	 * (ii) No empty slots in Frames means we have to decide where to put the 
	 * remaining chars.
	 * Suppose the first char of the remaining chars is 'V', and the length of 'V's is 
	 * L, with L <= F (or it'll be in the Delimiters). And there are K of them filled 
	 * into the Frames at the last positions. So there are L-K 'V's left and F-K 
	 * Frames containing no 'V'. Because L-K <= F-K, we can append L-K 'V' to F-K 
	 * Frames without violating the rule.
	 * After we fill the first remaining chars 'V', we pick the next one and append it 
	 * to the Frames (length of the chars <= F) . And keep doing this until no char 
	 * remains.
	 * The length of the sequence for (ii) is length(chars)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/task-scheduler/discuss/104496/concise-Java-Solution-O(N)-time-O(26)-space/146410
	 * https://leetcode.com/problems/task-scheduler/discuss/104496/concise-Java-Solution-O(N)-time-O(26)-space/163326
	 */
	public int leastInterval_calculate2(char[] tasks, int n) {
		int[] c = new int[26];
		for (char t : tasks) {
			c[t - 'A']++;
		}
		
		Arrays.sort(c);
		int i = 25;
		
		while (i >= 0 && c[i] == c[25])
			i--;

		return Math.max(tasks.length, (c[25] - 1) * (n + 1) + 25 - i);
	}
	
	/*
	 * https://leetcode.com/problems/task-scheduler/discuss/104511/Java-Solution-PriorityQueue-and-HashMap
	 * 
	 * 1. Greedy - We should always process the task which has largest amount left.
	 * 2. Put tasks (only their counts are enough, we don't care they are 'A' or 'B') 
	 *    in a PriorityQueue in descending order.
	 * 3. Start to process tasks from front of the queue. If amount left > 0, put it 
	 *    into a coolDown HashMap
	 * 4. If there's task which cool-down expired, put it into the queue and wait to 
	 *    be processed.
	 * 5. Repeat step 3, 4 till there is no task left.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/task-scheduler/discuss/104494/Java-greedy-algorithm-with-correctness-proof-using-PriorityQueue-and-waiting-list
	 * 
	 * other code:
	 * https://leetcode.com/problems/task-scheduler/discuss/104531/Java-Solution-PriorityQueue-CoolDownTable
	 */
	public int leastInterval_PriorityQueue2(char[] tasks, int n) {
		if (n == 0)
			return tasks.length;

		Map<Character, Integer> taskToCount = new HashMap<>();
		for (char c : tasks) {
			taskToCount.put(c, taskToCount.getOrDefault(c, 0) + 1);
		}

		Queue<Integer> queue = new PriorityQueue<>((i1, i2) -> i2 - i1);
		for (char c : taskToCount.keySet())
			queue.offer(taskToCount.get(c));

		Map<Integer, Integer> coolDown = new HashMap<>();
		int currTime = 0;
		
		while (!queue.isEmpty() || !coolDown.isEmpty()) {
			// cool-down expired
			if (coolDown.containsKey(currTime - n - 1)) {
				queue.offer(coolDown.remove(currTime - n - 1));
			}
			if (!queue.isEmpty()) {
				int left = queue.poll() - 1;
				if (left != 0)
					coolDown.put(currTime, left);
			}
			
			currTime++;
		}

		return currTime;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/task-scheduler/discuss/760266/Python-4-lines-linear-solution-detailed-explanation
     * https://leetcode.com/problems/task-scheduler/discuss/104507/Python-Straightforward-with-Explanation
     * https://leetcode.com/problems/task-scheduler/discuss/130786/Python-solution-with-detailed-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/task-scheduler/discuss/370755/C%2B%2B-solution-95-time-and-space-with-good-explanation
     * https://leetcode.com/problems/task-scheduler/discuss/760120/My-C%2B%2B-Solution-easy-to-understand
     * https://leetcode.com/problems/task-scheduler/discuss/104504/C%2B%2B-8lines-O(n)
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/task-scheduler/discuss/401103/simple-Javascript-idle-slots-1-pass-with-detailed-description
	 */

}
