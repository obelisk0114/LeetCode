package OJ0141_0150;

import definition.ListNode;
import java.util.Set;
import java.util.HashSet;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Function.html#4
 */

public class Linked_List_Cycle {
	/*
	 * 
	  runner.next != null && runner.next.next != null. 
	  We need to make sure that the runner can really move two steps.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/12516/o-1-space-solution/12
	 * https://discuss.leetcode.com/topic/12516/o-1-space-solution/17
	 * https://discuss.leetcode.com/topic/12516/o-1-space-solution/40?page=2
	 */
	public boolean hasCycle(ListNode head) {
		if (head == null)
			return false;
		ListNode walker = head;
		ListNode runner = head;
		while (runner.next != null && runner.next.next != null) {
			walker = walker.next;
			runner = runner.next.next;
			if (walker == runner)
				return true;
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/618/by-saying-using-no-extra-space-does-it-mean-o-0-in-space
	public boolean hasCycle2(ListNode head) {
		// set two runners
		ListNode slow = head;
		ListNode fast = head;

		// fast runner move 2 steps at one time while slow runner move 1 step,
		// if traverse to a null, there must be no loop
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) {
				return true;
			}
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/22551/simple-and-easy-understanding-java-solution-time-o-n-space-o-1
	// https://discuss.leetcode.com/topic/8912/shorter-solution-in-java
	
	// space : O(n)
	public boolean hasCycle_self(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            if (!set.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

}
