package OJ0371_0380;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Find_K_Pairs_with_Smallest_Sums {
	/*
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/426722/Java-Solution-beats-99.66-with-explaination
	 * 
	 * Basic idea: Use min_heap to keep track on next minimum pair sum, and we only 
	 *             need to maintain K possible candidates in the data structure.
	 * 
	 * Some observations: 
	 * For every numbers in nums1, its best partner(yields min sum) always starts from 
	 * nums2[0] since arrays are all sorted; And for a specific number in nums1, its 
	 * next candidate should be 
	 * [this specific number] + nums2[current_associated_index + 1], unless out of 
	 * boundary;)
	 * 
	 * Time complexity is O(kLogk) since queue.size <= k and we do at most k loop.
	 * 
	 * Remember how we do in "merge k sorted list"? We simply add the head of the 
	 * list into the heap and when a node is poll(), we just add the node.next.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84551/simple-Java-O(KlogK)-solution-with-explanation
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84551/simple-Java-O(KlogK)-solution-with-explanation/232946
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/423953/Three-Solutions
	 */
	public List<List<Integer>> kSmallestPairs_pq(int[] nums1, int[] nums2, int k) {
		PriorityQueue<int[]> queue = new PriorityQueue<>(
				(a, b) -> a[0] + a[1] - b[0] - b[1]); // min heap

		List<List<Integer>> result = new ArrayList<>();        // result list
		if (nums1.length == 0 || nums2.length == 0 || k == 0)  // edge case
			return result;

		// insert into heap, 0 is index of nums2 . O(m)
		for (int i = 0; i < nums1.length && i < k; i++)
			queue.offer(new int[] { nums1[i], nums2[0], 0 });

		// O(k)
		while (k-- > 0 && !queue.isEmpty()) {
			int[] cur = queue.poll();      // poll the smallest value

			result.add(Arrays.asList(cur[0], cur[1]));   // add to result

			// put next element in nums2 into heap
			if (cur[2] + 1 < nums2.length) {
				queue.offer(new int[] { cur[0], nums2[cur[2] + 1], cur[2] + 1 });
			}

		}
		return result;
	}
	
	/*
	 * The following function and class are from this link.
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84635/9ms-Java-solution-with-explanation
	 * 
	 * First, we take the first k elements of nums1 and paired with nums2[0] as the 
	 * starting pairs so that we have (0,0), (1,0), (2,0),.....(k-1,0) in the heap.
	 * Each time after we pick the pair with min sum, we put the new pair with the 
	 * second index +1. ie, pick (0,0), we put back (0,1). Therefore, the heap always 
	 * maintains at most min(k, len(nums1)) elements.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84569/Java-9ms-heap-queue-solution-k-log(k)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84566/Share-My-Solution-which-beat-96.42
	 */
	public List<List<Integer>> kSmallestPairs_pq2(int[] nums1, int[] nums2, int k) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 
				|| k == 0)
			return result;

		PriorityQueue<Triple> queue = new PriorityQueue<Triple>(nums1.length);

		// add the first column
		for (int i = 0; i < nums1.length; i++) {
			queue.offer(new Triple(nums1[i] + nums2[0], i, 0));
		}

		while (k > 0 && !queue.isEmpty()) {
			Triple current = queue.poll();
			
			List<Integer> list = new ArrayList<>();
			list.add(nums1[current.one]);
			list.add(nums2[current.two]);
			result.add(list);
			
			k--;
			
			// if the current one has a right candidate, add it to the queue.
			if (current.two + 1 < nums2.length)
				queue.offer(new Triple(nums1[current.one] + nums2[current.two + 1], 
						current.one, current.two + 1));
		}

		return result;
	}

	// Triple is used to store the sum, the index in nums1 and the index in nums2.
	class Triple implements Comparable<Triple> {
		int val;
		int one;
		int two;

		Triple(int val, int one, int two) {
			this.val = val;
			this.one = one;
			this.two = two;
		}
		
		@Override
	    public int compareTo (Triple that) {
	        return this.val - that.val;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/128291/Java-Heap-with-Explanations
	 * 
	 * 1 2 3
	 * 4 5 6
	 * 
	 * [1, 4] = i++ => [2, 4] = i++ => [3, 4] 
	 *                        = j++ => [2, 5] duplicated
	 *        = j++ => [1, 5] = j++ => [1, 6] 
	 *                        = i++ => [2, 5] duplicated
	 *                        
	 * i++ only if j = 0 can avoid duplicates
	 * j++ only if i = 0 can also avoid duplicates
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84556/Java-PriorityQueue-9ms-without-helper-class
	 */
	public List<List<Integer>> kSmallestPairs_pq3(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> kPairs = new ArrayList<>();
        int len1 = nums1.length, len2 = nums2.length;
        if (len1 == 0 || len2 == 0)
            return kPairs;
        
        // Min heap of indices of u, v.
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(nums1[a[0]] + nums2[a[1]], nums1[b[0]] + nums2[b[1]])
        );
        minHeap.add(new int[]{0, 0});
        
        while (k > 0 && !minHeap.isEmpty()) {
            int[] pair = minHeap.poll();
            
            List<Integer> list = new ArrayList<>();
            list.add(nums1[pair[0]]);
            list.add(nums2[pair[1]]);
            
            kPairs.add(list);
            k--;
            
            if (pair[0] + 1 < len1)
                minHeap.add(new int[]{pair[0] + 1, pair[1]});
            if (pair[0] == 0 && pair[1] + 1 < len2)
                minHeap.add(new int[]{pair[0], pair[1] + 1});
        }
        
        return kPairs;
    }
	
	/*
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84611/Java-10ms-solution-no-priority-queue/89153
	 * 
	 * This solution works by running K rounds of finding the minimum pair of 
	 * comparing each element in nums1 to the first element in nums2 it hasn't been 
	 * paired with AND added to the return list. Each round, the minimum pair is added 
	 * to the return list and the next number nums1 will be paired with in nums2 is 
	 * incremented.
	 * 
	 * The idea is that we are comparing to find the smallest unused pair creatable 
	 * from any of the elements in nums1 and the smallest element in nums2 it hasn't 
	 * been successfully paired with. In practice, we track this using an array, 
	 * index2. This array is the same length as nums1 and maps from an index in nums1 
	 * to the first index in nums2 it hasn't been paired with and added to the results 
	 * list.
	 * 
	 * Rf : https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84611/Java-10ms-solution-no-priority-queue/383285
	 */
	public List<List<Integer>> kSmallestPairs_array(int[] nums1, int[] nums2, int k) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		if (nums1.length == 0 || nums2.length == 0 || k == 0)
			return ret;

		// index2 is used for recording position in nums2 corresponding to given
		// position in nums1
		int[] index2 = new int[nums1.length];
		while (k-- > 0) {
			int min = Integer.MAX_VALUE;
			
			// every time we should start from the first place in nums2 to find proper
			// position
			int index = -1;
			for (int index1 = 0; index1 < nums1.length; index1++) {
				if (index2[index1] >= nums2.length)
					continue;

				if (nums1[index1] + nums2[index2[index1]] < min) {
					min = nums1[index1] + nums2[index2[index1]];
					
					// keep record the index in nums1
					index = index1;
				}
			}
			if (index == -1)
				break;

			ret.add(Arrays.asList(nums1[index], nums2[index2[index]]));
			index2[index]++;
		}
		return ret;
	}
	
	/*
	 * by myself
	 * 
	 * Other code :
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84656/dijkstra-like-solution-in-Java
	 * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84633/Java-easy-understandable-bfs-with-PriorityQueue
	 */
	public List<List<Integer>> kSmallestPairs_self(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        if (k == 0 || nums1.length == 0 || nums2.length == 0)
            return ans;
        
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] i1, int[] i2) {
                return (nums1[i1[0]] + nums2[i1[1]]) - (nums1[i2[0]] + nums2[i2[1]]);
            }
        });
        boolean[][] visited = new boolean[nums1.length][nums2.length];
        
        pq.offer(new int[] {0, 0});
        visited[0][0] = true;
        
        while (ans.size() < k && !pq.isEmpty()) {
            int[] cur = pq.poll();
            List<Integer> list = new ArrayList<>(2);
            list.add(nums1[cur[0]]);
            list.add(nums2[cur[1]]);
            ans.add(list);
            
            int[] next1 = {cur[0] + 1, cur[1]};
            int[] next2 = {cur[0], cur[1] + 1};
            
            if (next1[0] < nums1.length && !visited[next1[0]][next1[1]]) {
                pq.offer(next1);
                visited[next1[0]][next1[1]] = true;
            }
            if (next2[1] < nums2.length && !visited[next2[0]][next2[1]]) {
                pq.offer(next2);
                visited[next2[0]][next2[1]] = true;
            }
        }
        
        return ans;
    }
	
	// https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84577/O(k)-solution
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84550/Slow-1-liner-to-Fast-solutions
     * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84629/BFS-Python-104ms-with-comments
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84568/c%2B%2B-priority_queue-solution
     * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84607/Clean-16ms-C%2B%2B-O(N)-Space-O(KlogN)-Time-Solution-using-Priority-queue
     * https://leetcode.com/problems/find-k-pairs-with-smallest-sums/discuss/84583/Three-different-solutions-in-C%2B%2B-well-explained
     */

}
