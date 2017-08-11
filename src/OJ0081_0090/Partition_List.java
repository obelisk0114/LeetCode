package OJ0081_0090;

import definition.ListNode;

public class Partition_List {
	/*
	 * https://discuss.leetcode.com/topic/2920/my-accepted-solution-any-improvement
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/7795/concise-java-code-with-explanation-one-pass/3
	 * https://discuss.leetcode.com/topic/7795/concise-java-code-with-explanation-one-pass
	 */
	public ListNode partition(ListNode head, int x) {
		ListNode cur = head;

		ListNode smaller_sentinel = new ListNode(0);     // dummy heads
		ListNode larger_sentinel = new ListNode(0);      // dummy heads
		ListNode smaller_cur = smaller_sentinel;         // current tails
		ListNode larger_cur = larger_sentinel;           // current tails
		
		// Now, go along the list, partitioning into two halves.
		while (cur != null) {
			if (cur.val < x) {
				smaller_cur.next = cur;
				smaller_cur = smaller_cur.next;

			} else {
				larger_cur.next = cur;
				larger_cur = larger_cur.next;
			}
			cur = cur.next;
		}
		// Do the concatenation of two halves. Make sure the last node points to null
		larger_cur.next = null;    //important! avoid cycle in linked list. otherwise u will get TLE.
		smaller_cur.next = larger_sentinel.next;
		return smaller_sentinel.next;
	}
	
	// https://discuss.leetcode.com/topic/23951/java-solution-pick-out-larger-nodes-and-append-to-the-end
	public ListNode partition_next(ListNode head, int x) {
		if (head == null || head.next == null)
			return head;

		ListNode l1 = new ListNode(0);
		ListNode l2 = new ListNode(0);
		ListNode p1 = l1, p2 = l2;

		p1.next = head;
		while (p1.next != null) {
			// keep moving larger node to list 2;

			if (p1.next.val >= x) {
				ListNode tmp = p1.next;
				p1.next = tmp.next;

				p2.next = tmp;
				p2 = p2.next;
			} 
			else {
				p1 = p1.next;
			}
		}

		// conbine lists 1 and 2;
		p2.next = null;
		p1.next = l2.next;
		return l1.next;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/2976/my-o-n-o-1-solution
	 * 
	 * Use tail to keep track of the end point where the nodes before it are smaller than x.
	 */
	public ListNode partition_keep_one_line(ListNode head, int x) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode p = dummy;
		ListNode tail = dummy;
		while (p != null && p.next != null) {
			if (p.next.val >= x)
				p = p.next;
			else {
				if (p == tail) { // don't forget the edge cases when p==tail
					tail = tail.next;
					p = p.next;
				} 
				else {
					ListNode tmp = p.next;
					p.next = tmp.next;
					tmp.next = tail.next;
					tail.next = tmp;
					tail = tmp; // don't forget to move tail.
				}
			}
		}
		return dummy.next;
	}

}
