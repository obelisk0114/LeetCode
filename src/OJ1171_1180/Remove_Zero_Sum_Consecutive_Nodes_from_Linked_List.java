package OJ1171_1180;

import definition.ListNode;

import java.util.Map;
import java.util.HashMap;

public class Remove_Zero_Sum_Consecutive_Nodes_from_Linked_List {
	/*
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/413134/Java-O(N)-with-detail-explanation
	 * 
	 * The observation here is that the sum from index 0 to index M will be equal to 
	 * sum from index 0 to index N if sum from index (M+1) to index N is 0.
	 * Thus, here we track the sum from index 0 to each index, using a Map to indicate
	 * the farthest index N that we can remove from index M, then we shall be able to
	 * remove M+1 -> N and continue from N+1. This works since we don't have to 
	 * optimize for the number of sequences to be removed
	 * 
	 * Rf :
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366319/JavaC%2B%2BPython-Greedily-Skip-with-HashMap
	 * 
	 * Other code:
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/580406/Java-92.78-time-100.00-space
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/477112/Very-easy-Java-solution-with-explanation
	 */
	public ListNode removeZeroSumSublists_2_pass(ListNode head) {
		// Map sum from index 0 to the farthest value that the sum stays unchanged.
		Map<Integer, ListNode> sumMap = new HashMap<>();

		// Need the dummy node to track the new head if changed.
		ListNode preHead = new ListNode(0);
		preHead.next = head;

		// First iteration to compute the map.
		int sum = 0;
		for (ListNode p = preHead; p != null; p = p.next) {
			sum += p.val;
			sumMap.put(sum, p);
		}

		// Second iteration to re-connect the nodes to the farthest node where
		// the sum stays unchanged
		sum = 0;
		for (ListNode p = preHead; p != null; p = p.next) {
			sum += p.val;
			p.next = sumMap.get(sum).next;
		}

		// Done, return the head from preHead
		return preHead.next;
	}
	
	/*
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366319/JavaC%2B%2BPython-Greedily-Skip-with-HashMap
	 * 
	 * Because the head ListNode can be removed in the end,
	 * I create a dummy ListNode and set it as a previous node of head.
	 * prefix calculates the prefix sum from the first node to the current cur node.
	 * 
	 * Next step, we need an important hashmap m (no good name for it),
	 * It takes a prefix sum as key, and the related node as the value.
	 * 
	 * Then we scan the linked list, accumulate the node's value as prefix sum.
	 * 1. If it's a prefix that we've never seen, we set m[prefix] = cur.
	 * 2. If we have seen this prefix, m[prefix] is the node we achieve this prefix 
	 *    sum.
	 *    We want to skip all nodes between m[prefix] and cur.next (exclusive).
	 *    So we simplely do m[prefix].next = cur.next.
	 *    
	 *    Also, remove those in-between nodes from hashmap, to make sure that we 
	 *    don't use the old removed node again in future.
	 * 
	 * If the prefix sum exists in hashmap, that means the nodes between when we 
	 * found the same sum and current node are zero sum.
	 * 
	 * We keep doing these and it's done.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/507108/Java-simple-O(n)-solution-with-explanation
	 */
	public ListNode removeZeroSumSublists_remove_middle(ListNode head) {
		ListNode dummy = new ListNode(0), cur = dummy;
		dummy.next = head;
		
		int prefix = 0;
		Map<Integer, ListNode> m = new HashMap<>();
		
		while (cur != null) {
			prefix += cur.val;
			if (m.containsKey(prefix)) {
				cur = m.get(prefix).next;
				int p = prefix + cur.val;
				
				// Remove zero-sum in-between nodes from m
				while (p != prefix) {
					m.remove(p);
					cur = cur.next;
					p += cur.val;
				}
				
				// Point old node to current next node
				m.get(prefix).next = cur.next;
			} 
			else {
				m.put(prefix, cur);
			}
			
			cur = cur.next;
		}
		return dummy.next;
	}
	
	/*
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366382/Intuitive-Java-Solution-With-Explanation
	 * 
	 * Similar to finding subarray sum = 0. "sums" keep track of prefix sums seen so 
	 * far. If we find the current sum "sum" in the prefix sums map, then we found a 
	 * subarray sum = 0 from the matching entry till the current element. So remove 
	 * that portion and move on exploring other nodes.
	 */
	public ListNode removeZeroSumSublists_map_remove(ListNode head) {
		if (head == null)
			return head;
		
		Map<Integer, ListNode> sums = new HashMap<>();
		int sum = 0;
		
		ListNode curr = head;
		while (curr != null) {
			sum += curr.val;
			if (sum == 0)
				head = curr.next;
			
			if (sums.containsKey(sum)) {
				sums.get(sum).next = curr.next;
				return removeZeroSumSublists_map_remove(head);
			}
			
			sums.put(sum, curr);
			curr = curr.next;
		}
		return head;
	}
	
	// https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366426/clean-N2-AC-solution
	public ListNode removeZeroSumSublists_n_2(ListNode head) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		for (ListNode i = dummy; i != null; i = i.next) {
			int sum = 0;
			for (ListNode j = i.next; j != null;) {
				sum += j.val;
				if (sum == 0) {
					i.next = j.next;
				}
				
				j = j.next;
			}
		}
		return dummy.next;
	}
	
	/*
	 * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366337/Java-Iterative-and-Recursive-solution
	 * 
	 * 1. Remove all the nodes have zero sum starting from head.
	 * 2. Make sure the next node already has a non-zero-sum linked list.
	 *    head.next = removeZeroSumSublists(head.next);
	 * 3. Use a dummy head to simplify code.
	 */
	public ListNode removeZeroSumSublists_recursive(ListNode head) {
		if (head == null)
			return head;
		
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		ListNode cur = head;
		int sum = 0;
		while (cur != null) {
			if (cur.val + sum == 0)
				dummy.next = cur.next;
			
			sum += cur.val;
			cur = cur.next;
		}
		
		if (dummy.next != null)
			dummy.next.next = removeZeroSumSublists_recursive(dummy.next.next);
		
		return dummy.next;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/414285/Python-easy-to-understand-solution-with-explanations
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366510/python-solution-with-dict
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/367211/Super-Simple-Python-Solution-(Commented)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366350/C%2B%2B-O(n)-(explained-with-pictures)
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/685304/C%2B%2B-O(N)-solution-using-hashing.-Detailed-Explanation
     * https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/discuss/366323/Easy-and-Short-Solution-C%2B%2B
     */

}
