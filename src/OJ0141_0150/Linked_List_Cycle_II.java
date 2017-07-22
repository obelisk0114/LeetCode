package OJ0141_0150;

import definition.ListNode;
import java.util.Set;
import java.util.HashSet;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Function.html#4
 */

public class Linked_List_Cycle_II {
	// http://fisherlei.blogspot.com/2013/11/leetcode-linked-list-cycle-ii-solution.html
	public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null)
            return null;
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                slow = head;
                while (fast != slow) {
                    fast = fast.next;
                    slow = slow.next;
                }
                return slow;
            }
        }        
        return null;
    }
	
	// https://discuss.leetcode.com/topic/19367/java-o-1-space-solution-with-detailed-explanation
	public ListNode detectCycle2(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;

		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;

			if (fast == slow) {
				ListNode slow2 = head;
				while (slow2 != slow) {
					slow = slow.next;
					slow2 = slow2.next;
				}
				return slow;
			}
		}
		return null;
	}
	
	// https://discuss.leetcode.com/topic/41570/java-solution-without-extra-space-with-explanation
	public ListNode detectCycle3(ListNode head) {
		if (head == null || head.next == null || head.next.next == null)
			return null;
		ListNode pointer1 = head.next;
		ListNode pointer2 = head.next.next;
		// Step 1
		while (pointer1 != pointer2) {
			if (pointer2.next == null || pointer2.next.next == null)
				return null;
			pointer1 = pointer1.next;
			pointer2 = pointer2.next.next;
		}
		pointer1 = head;
		// Step 2
		while (pointer1 != pointer2) {
			pointer1 = pointer1.next;
			pointer2 = pointer2.next;
		}
		return pointer1;
	}
	
	// space : O(n)
	ListNode detectCycle_self(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            if (!set.add(head))
                return head;
            head = head.next;
        }
        return null;
    }

}
