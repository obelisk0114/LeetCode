package OJ0021_0030;

import definition.ListNode;

import java.util.PriorityQueue;
import java.util.Comparator;

public class Merge_k_Sorted_Lists {
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/152022/Divide-and-Conquer-Heap-with-Explanations
	 * 
	 * 1. Pair up k lists and merge each pair.
	 * 2. After the first pairing, k lists are merged into k/2 lists with average 
	 *    2N/k length, then k/4, k/8 and so on.
	 * 3. Repeat this procedure until we get the final sorted linked list.
	 * 
	 * Rf :
	 * https://leetcode.com/articles/merge-k-sorted-list/
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10640/Simple-Java-Merge-Sort/11528
	 */
	public ListNode mergeKLists(ListNode[] lists) {
        // Corner cases.
        if (lists == null || lists.length == 0)
            return null;
        
        return mergeKLists(lists, 0, lists.length - 1);
    }
    
    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        // Base cases.
        if (end < start) {
            return null;
        }
        if (end - start == 0) {
            return lists[start];
        }
        if (end - start == 1) {
            return mergeTwoLists(lists[start], lists[end]);
        }
        
        // Divide lists into 2 sublists and sort them as a whole recursively.
        int mid = start + ((end - start) >> 1);
        ListNode lower = mergeKLists(lists, start, mid);
        ListNode upper = mergeKLists(lists, mid + 1, end);
        
        return mergeTwoLists(lower, upper);
    }
    
    private ListNode mergeTwoLists(ListNode head1, ListNode head2) {
        ListNode dummyHead = new ListNode(0), ptr = dummyHead;
        
        while (head1 != null && head2 != null) {
            if (head1.val < head2.val) {
                ptr.next = head1;
                head1 = head1.next;
            } 
            else {
                ptr.next = head2;
                head2 = head2.next;
            }
            ptr = ptr.next;
        }
        if (head1 != null) {
            ptr.next = head1;
        } 
        else if (head2 != null) {
            ptr.next = head2;
        }
        
        return dummyHead.next;
    }
	
	/*
	 * by myself
	 * 
	 * Rf : https://stackoverflow.com/questions/34585444/java-lambdas-20-times-slower-than-anonymous-classes
	 * 
	 * Other code :
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10528/A-java-solution-based-on-Priority-Queue
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10809/13-lines-in-Java
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10888/My-Accepted-Java-Solution-Using-PriorityQueue
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10859/Simple-java-sol-using-PriorityQueue
	 */
	public ListNode mergeKLists_self2(ListNode[] lists) {
        ListNode dummy = new ListNode(0);
        if (lists == null || lists.length == 0)
            return dummy.next;
        
        PriorityQueue<ListNode> pq = new PriorityQueue<>(lists.length, 
        	new Comparator<ListNode>() {
        	    @Override
        	    public int compare(ListNode n1, ListNode n2) {
        		    return n1.val - n2.val;
        		}
        	});
        for (ListNode n : lists) {
            if (n != null)
                pq.offer(n);
        }
        
        ListNode cur = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            cur.next = node;
            cur = cur.next;
            
            if (node.next != null) {
                pq.offer(node.next);
            }
        }
        return dummy.next;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10522/My-simple-java-Solution-use-recursion
	 * 
	 * Observe that this is exactly the situation we see in merge sort at the log nth 
	 * level of the recursion tree! We can thus apply the same idea as the merge sort 
	 * algorithm, by merging the first array with the second, the third with the 
	 * fourth, and so on and then repeatedly apply this until all of the arrays have 
	 * been merged.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10522/My-simple-java-Solution-use-recursion/121480
	 * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10522/My-simple-java-Solution-use-recursion/11302
	 */
	public ListNode mergeKLists_recur(ListNode[] lists) {
		return partion(lists, 0, lists.length - 1);
	}

	public ListNode partion(ListNode[] lists, int s, int e) {
		if (s == e)
			return lists[s];
		if (s < e) {
			int q = (s + e) / 2;
			
			ListNode l1 = partion(lists, s, q);
			ListNode l2 = partion(lists, q + 1, e);
			return merge(l1, l2);
		} 
		else
			return null;
	}

	// This function is from Merge Two Sorted Lists.
	public ListNode merge(ListNode l1, ListNode l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;
		
		if (l1.val < l2.val) {
			l1.next = merge(l1.next, l2);
			return l1;
		} 
		else {
			l2.next = merge(l1, l2.next);
			return l2;
		}
	}
	
	// The following 2 functions are by myself.
	public ListNode mergeKLists_self(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;
        
        ListNode cur = lists[0];
        for (int i = 1; i < lists.length; i++) {
            cur = merge2Lists(cur, lists[i]);
        }
        return cur;
    }
    
    private ListNode merge2Lists(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null)
            return null;
        
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            }
            else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        
        if (l1 != null)
            cur.next = l1;
        else if (l2 != null)
            cur.next = l2;
        
        return dummy.next;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10511/10-line-python-solution-with-priority-queue
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10552/Python-133ms-solution
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10607/Three-ways-to-solve-this-problem-in-Python-(build-in-sort-merge-and-priority-queue)
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10513/108ms-python-solution-with-heapq-and-avoid-changing-heap-size
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10531/Sharing-my-straightforward-C%2B%2B-solution-without-data-structure-other-than-vector
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10527/Difference-between-Priority-Queue-and-Heap-and-C%2B%2B-implementation
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10543/Brief-C%2B%2B-solution-with-priority_queue
     * https://leetcode.com/problems/merge-k-sorted-lists/discuss/10882/C%2B%2B-solution-using-Merge-Sort
     */

}
