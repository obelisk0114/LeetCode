package OJ0081_0090;

import definition.ListNode;

public class Remove_Duplicates_from_Sorted_List {
	// https://discuss.leetcode.com/topic/23381/clean-java-solution
	public ListNode deleteDuplicates(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode dummy = head;
		while (dummy.next != null) {
			if (dummy.next.val == dummy.val) {
				dummy.next = dummy.next.next;
			} else
				dummy = dummy.next;
		}
		return head;
	}
	
	// https://leetcode.com/articles/remove-duplicates-sorted-list/
	public ListNode deleteDuplicates1(ListNode head) {
	    ListNode current = head;
	    while (current != null && current.next != null) {
	        if (current.next.val == current.val) {
	            current.next = current.next.next;
	        } else {
	            current = current.next;
	        }
	    }
	    return head;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/26521/clean-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8345/my-pretty-solution-java
	 */
	public ListNode deleteDuplicates2(ListNode head) {
		ListNode tmp = head;
		while (tmp != null) {
			if (tmp.next != null && tmp.next.val == tmp.val)
				tmp.next = tmp.next.next;
			else
				tmp = tmp.next;
		}
		return head;
	}
	
	// https://discuss.leetcode.com/topic/12312/two-pointers-java-solution
	public ListNode deleteDuplicates_2_pointer(ListNode head) {
		if (head == null || head.next == null)
			return head;
		ListNode p = head;
		ListNode curr = p.next;
		while (curr != null) {
			if (curr.val != p.val) {
				p.next = curr;
				p = curr;
			}
			curr = curr.next;
		}
		p.next = curr;       // p.next = null;
		return head;
	}
	
	// https://discuss.leetcode.com/topic/14775/3-line-java-recursive-solution
	public ListNode deleteDuplicates_recursive(ListNode head) {
		if (head == null || head.next == null)
			return head;
		head.next = deleteDuplicates_recursive(head.next);
		return head.val == head.next.val ? head.next : head;
	}
	
	// https://discuss.leetcode.com/topic/32440/clear-java-solution
	public ListNode deleteDuplicates_cur_after_pre(ListNode head) {
		if (head == null)
			return null;
		ListNode pre = head;
		ListNode cur = head.next;
		while (cur != null) {
			if (cur.val == pre.val) {
				pre.next = cur.next;
				cur = cur.next;
			} else {
				pre = pre.next;
				cur = pre.next;
			}
		}
		return head;
	}
	
	// by myself
	public ListNode deleteDuplicates_self(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode pre = head;
        ListNode process = head.next;
        while (process != null) {
            pre.next = process;
            if (process.val == pre.val) {
                process = process.next;
                if (process == null)
                    pre.next = null;
            }
            else {
                pre = pre.next;
                process = process.next;
            }
        }
        return head;
    }

}
