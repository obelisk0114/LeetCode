package OJ0021_0030;

import definition.ListNode;

public class Swap_Nodes_in_Pairs {
	// https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11046/My-simple-JAVA-solution-for-share
	public ListNode swapPairs(ListNode head) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		ListNode current = dummy;
		while (current.next != null && current.next.next != null) {
			ListNode first = current.next;
			ListNode second = current.next.next;
			
			first.next = second.next;
			current.next = second;
			second.next = first;
			
			current = current.next.next;
		}
		return dummy.next;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/swap-nodes-in-pairs/discuss/11254/Seeking-for-a-better-solution/12047
	 * 
	 * The addition of an empty node at the start removes the problem of "head" being a
	 * special case, as the start of the list is now defined as "what follows start," 
	 * and the middle of the list is easy to swap. As we also know that start is not 
	 * null, cur is not null at the beginning, and checking cur.next.next during the 
	 * while loop means it cannot be null in following loops. If cur.next is null, 
	 * then the condition short circuits to false before needing to evaluate to a 
	 * nullpointerexception. Therefore, the conditions at the start are unneeded.
	 */
	public ListNode swapPairs_forLoops(ListNode head) {
		ListNode start = new ListNode(0); // make head no longer a special case
		start.next = head;

		for (ListNode cur = start; cur.next != null && cur.next.next != null; cur = cur.next.next) {
			cur.next = swap(cur.next, cur.next.next);
		}
		return start.next;
	}

	private ListNode swap(ListNode next1, ListNode next2) {
		next1.next = next2.next;
		next2.next = next1;
		return next2;
	}
	
	/*
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11111/Java-simple-recursive-solution
	 * 
	 * Rf : https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11030/My-accepted-java-code.-used-recursion.
	 */
	public ListNode swapPairs_recursive(ListNode head) {
		if (head == null || head.next == null)
			return head;
		
		ListNode second = head.next;
		ListNode third = second.next;

		second.next = head;
		head.next = swapPairs_recursive(third);

		return second;
	}
	
	/*
	 * by myself
	 * 
	 * Rf : leetcode.com/problems/swap-nodes-in-pairs/discuss/11030/My-accepted-java-code.-used-recursion./11889
	 */
	public ListNode swapPairs_self(ListNode head) {
        if (head == null || head.next == null)
            return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode cur = head.next;
        ListNode pre = head;
        ListNode pre2 = dummy;
        int i = 1;
        while (cur != null) {
            i++;
            
            if (i % 2 == 0) {
                ListNode next = cur.next;
                cur.next = pre;
                pre.next = next;
                pre2.next = cur;
                
                pre = cur;
                cur = cur.next;
            }
            
            cur = cur.next;
            pre = pre.next;
            pre2 = pre2.next;
        }
        return dummy.next;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11254/Seeking-for-a-better-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11028/My-straight-forward-Java-solution-without-recursion-or-dummy-nodes-(0ms)
	 */
	public ListNode swapPairs_swap_next2(ListNode head) {
		ListNode cur = head, next1, next2;
		if (head == null)
			return null;
		if (head.next == null)
			return head;

		// head change with next is a special case, so we deal with it first
		ListNode temp = cur.next;
		cur.next = temp.next;
		temp.next = cur;
		head = temp;

		while (cur.next != null && cur.next.next != null) {
			next1 = cur.next;
			next2 = cur.next.next;
			swap_next2(cur, next1, next2);
			cur = cur.next.next;
		}
		return head;
	}

	private void swap_next2(ListNode cur, ListNode next1, ListNode next2) {
		cur.next = next2;
		next1.next = next2.next;
		next2.next = next1;
	}
	
	/**
	 * Other programming language collections
	 * 
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11019/7-8-lines-C++-Python-Ruby
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11312/Python-concise-iterative-and-recursive-solutions.
	 * https://leetcode.com/problems/swap-nodes-in-pairs/discuss/11175/C++-recursive-and-iterative-solutions.
	 */

}
