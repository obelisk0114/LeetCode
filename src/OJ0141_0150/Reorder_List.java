package OJ0141_0150;

import definition.ListNode;
import java.util.Deque;
import java.util.ArrayDeque;

public class Reorder_List {
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/18092/java-solution-with-3-steps
	 */
	public void reorderList(ListNode head) {
		if (head == null || head.next == null)
			return;

		// step 1. cut the list to two halves
		// prev will be the tail of 1st half
		// slow will be the head of 2nd half
		ListNode prev = null, slow = head, fast = head, l1 = head;

		while (fast != null && fast.next != null) {
			prev = slow;
			slow = slow.next;
			fast = fast.next.next;
		}

		prev.next = null;

		// step 2. reverse the 2nd half
		ListNode l2 = reverse(slow);

		// step 3. merge the two halves
		merge(l1, l2);
	}
	ListNode reverse(ListNode head) {
		ListNode prev = null, curr = head, next = null;

		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}

		return prev;
	}
	void merge(ListNode l1, ListNode l2) {
		while (l1 != null) {
			ListNode n1 = l1.next, n2 = l2.next;
			l1.next = l2;

			if (n1 == null)
				break;

			l2.next = n1;
			l1 = n1;
			l2 = n2;
		}
	}
	
	// https://discuss.leetcode.com/topic/13869/java-solution-with-3-steps
	public void reorderList2(ListNode head) {
		if (head == null || head.next == null)
			return;

		// Find the middle of the list
		ListNode p1 = head;
		ListNode p2 = head;
		while (p2.next != null && p2.next.next != null) {
			p1 = p1.next;
			p2 = p2.next.next;
		}

		// Reverse the half after middle 1->2->3->4->5->6 to 1->2->3->6->5->4
		ListNode preMiddle = p1;
		ListNode preCurrent = p1.next;
		while (preCurrent.next != null) {
			ListNode current = preCurrent.next;
			preCurrent.next = current.next;
			current.next = preMiddle.next;
			preMiddle.next = current;
		}

		// Start reorder one by one 1->2->3->6->5->4 to 1->6->2->5->3->4
		p1 = head;
		p2 = preMiddle.next;
		while (p1 != preMiddle) {
			preMiddle.next = p2.next;
			p2.next = p1.next;
			p1.next = p2;
			p1 = p2.next;
			p2 = preMiddle.next;
		}
	}
	
	// myself
	public void reorderList_self(ListNode head) {
        if (head == null)
            return;
        
        int count = 0;
        ListNode tmp = head;
        ListNode ret = head;
        while (tmp.next != null) {
            tmp = tmp.next;
            count++;
        }
        
        ListNode tmp2 = head;
        for (int i = 0; i < count / 2; i++) {
            tmp2 = tmp2.next;
        }
        
        ListNode tmp3 = tmp2.next;
        tmp2.next = null;
        tmp2 = tmp3;
        
        ListNode pre = null;
        while (tmp2 != null) {
            ListNode temp = tmp2.next;
            tmp2.next = pre;
            pre = tmp2;
            tmp2 = temp;
        }
        
        while (pre != null && ret != null) {
            tmp = ret.next;
            ret.next = pre;
            tmp2 = pre.next;
            pre.next = tmp;
            pre = tmp2;
            ret = tmp;
        }
    }
	
	/*
	 * https://discuss.leetcode.com/topic/6144/java-solution-with-stack
	 * 
	 * space : O(n)
	 */
	public void reorderList_stack(ListNode head) {
		if (head == null || head.next == null)
			return;
		Deque<ListNode> stack = new ArrayDeque<ListNode>();
		ListNode ptr = head;
		while (ptr != null) {
			stack.push(ptr);
			ptr = ptr.next;
		}
		int cnt = (stack.size() - 1) / 2;
		ptr = head;
		while (cnt-- > 0) {
			ListNode top = stack.pop();
			ListNode tmp = ptr.next;
			ptr.next = top;
			top.next = tmp;
			ptr = tmp;
		}
		stack.pop().next = null;
	}

}
