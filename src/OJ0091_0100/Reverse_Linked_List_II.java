package OJ0091_0100;

import definition.ListNode;

public class Reverse_Linked_List_II {
	/*
	 * https://discuss.leetcode.com/topic/13782/240ms-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8976/simple-java-solution-with-clear-explanation
	 */
	public ListNode reverseBetween(ListNode head, int m, int n) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode pre = dummy; // pre is the node before orignal M
		ListNode M = head; // M is after pre

		for (int i = 1; i < m; i++) { // Move pre and M to orignal place
			pre = pre.next;
			M = M.next;
		}

		for (int i = 0; i < n - m; i++) {
			ListNode current = M.next; // Both pre and M are all fixed, only current is assigned every time to M.next. M is pushed back everytime
			M.next = current.next; // Move current to the position after pre
			current.next = pre.next;
			pre.next = current;
		}

		return dummy.next;
	}
	
	// Rf : https://discuss.leetcode.com/topic/24873/easy-understanding-java-solution
	public ListNode reverseBetween_self(ListNode head, int m, int n) {
        ListNode pre = head;
        ListNode cur = head;
        for (int i = 0; i < m - 1; i++) {
            pre = cur;
            cur = cur.next;
        }
        ListNode first = cur;
        ListNode precur = cur;
        cur = cur.next;
        for (int i = m; i < n; i++) {
            ListNode tmp = cur.next;
            cur.next = precur;
            precur = cur;
            cur = tmp;
        }
        pre.next = precur;
        first.next = cur;
        if (m == 1)
            return precur;
        else
            return head;
    }
	
	// https://discuss.leetcode.com/topic/64610/why-nobody-does-it-with-recursion-shouldn-t-the-code-be-simpler
	public ListNode reverseBetween_recursive(ListNode head, int m, int n) {
		if (m == n) {
			return head;
		}
		if (m > 1) {
			ListNode newHead = head;
			newHead.next = reverseBetween_recursive(head.next, m - 1, n - 1);
			return newHead;
		} else {
			ListNode next = head.next;
			ListNode newHead = reverseBetween_recursive(next, 1, n - 1);
			ListNode nextnext = next.next;
			next.next = head;
			head.next = nextnext;
			return newHead;
		}
	}

}
