package OJ0081_0090;

import definition.ListNode;

public class Remove_Duplicates_from_Sorted_List_II {
	/*
	 * https://discuss.leetcode.com/topic/3890/my-accepted-java-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/24470/java-simple-and-clean-code-with-comment
	 */
	public ListNode deleteDuplicates(ListNode head) {
		if (head == null)
			return null;
		ListNode FakeHead = new ListNode(0);
		FakeHead.next = head;
		ListNode pre = FakeHead;
		ListNode cur = head;
		while (cur != null) {
			while (cur.next != null && cur.val == cur.next.val) {
				cur = cur.next;      // while loop to find the last node of the dups.
			}
			if (pre.next == cur) {     // no dup, move down
				pre = pre.next;
			} else {                   // duplicates detected.
				pre.next = cur.next;   // remove the dups.
			}
			cur = cur.next;
		}
		return FakeHead.next;
	}
	
	// https://discuss.leetcode.com/topic/11234/a-short-and-simple-java-solution
	public ListNode deleteDuplicates2(ListNode head) {
		ListNode dummy = new ListNode(0);
		ListNode d = dummy;
		while (head != null) {
			if (head.next != null && head.val == head.next.val) {
				while (head.next != null && head.val == head.next.val)
					head = head.next;
			} else {
				d.next = head;
				d = d.next;
			}
			head = head.next;
		}
		d.next = null;
		return dummy.next;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/7136/264ms-java-o-n-no-extra-space/2
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/12143/java-solution-with-dummy-node-18-lines-o-n
	 */
	public ListNode deleteDuplicates_see_2_backward(ListNode head) {
		// dummy head
		ListNode temphead = new ListNode(0);
		temphead.next = head;
		ListNode current = temphead;

		while (current.next != null && current.next.next != null) {
			if (current.next.val == current.next.next.val) {
				ListNode t = current.next.next;
				int tt = t.val;

				while (t != null && t.val == tt) {
					t = t.next;
				}
				current.next = t;
			} else {
				current = current.next;
			}
		}
		return temphead.next;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5206/my-recursive-java-solution
	 * 
	 * if current node is not unique, return deleteDuplicates with head.next.
     * If current node is unique, link it to the result of next list made by recursive call.
	 */
	public ListNode deleteDuplicates_recursive(ListNode head) {
		if (head == null)
			return null;

		if (head.next != null && head.val == head.next.val) {
			while (head.next != null && head.val == head.next.val) {
				head = head.next;
			}
			return deleteDuplicates_recursive(head.next);
		} else {
			head.next = deleteDuplicates_recursive(head.next);
		}
		return head;
	}

}
