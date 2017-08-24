package OJ0201_0210;

import definition.ListNode;

public class Reverse_Linked_List {
	public ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/33829/ac-solution-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/13268/in-place-iterative-and-recursive-java-solution
	 */
	public ListNode reverseList_recursive(ListNode head) {
		return reverseList_recursive(head, null);
	}	  
	public ListNode reverseList_recursive(ListNode curr, ListNode prev) {
		if (curr == null)
			return prev;
		ListNode next = curr.next;// Save current's next 
		curr.next = prev;// Point current.next to prev
		return reverseList_recursive(next, curr);// // Forward current node to next; Set the current node as prev
	}
	
	/*
	 * Approach #2 (Recursive)
	 * https://leetcode.com/articles/reverse-linked-list/
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/16506/my-java-recursive-solution
	 */
	public ListNode reverseList_recursive2(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode p = reverseList(head.next);
		head.next.next = head;
		head.next = null;
		return p;
	}
	
	// https://discuss.leetcode.com/topic/36999/java-iterative-0ms-solution-with-explanation
	public ListNode reverseList_iterative(ListNode head) {
	    // is there something to reverse?
	    if (head != null && head.next != null) {
	        ListNode pivot = head;
	        ListNode frontier = null;
	        while (pivot != null && pivot.next != null) {
	            frontier = pivot.next;
	            pivot.next = pivot.next.next;
	            frontier.next = head;
	            head = frontier;
	        }
	    }
	    
	    return head;
	} 

}
