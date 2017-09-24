package OJ0001_0010;

import definition.ListNode;

public class Add_Two_Numbers {
	// https://leetcode.com/articles/add-two-numbers/
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode dummyHead = new ListNode(0);
		ListNode p = l1, q = l2, curr = dummyHead;
		int carry = 0;
		while (p != null || q != null) {
			int x = (p != null) ? p.val : 0;
			int y = (q != null) ? q.val : 0;
			int sum = carry + x + y;
			carry = sum / 10;
			curr.next = new ListNode(sum % 10);
			curr = curr.next;
			if (p != null)
				p = p.next;
			if (q != null)
				q = q.next;
		}
		if (carry > 0) {
			curr.next = new ListNode(carry);
		}
		return dummyHead.next;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/24026/java-solution-concise-and-easy-to-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/799/is-this-algorithm-optimal-or-what
	 */
	public ListNode addTwoNumbers_take_carry(ListNode l1, ListNode l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;

		ListNode head = new ListNode(0);
		ListNode p = head;

		int tmp = 0;
		while (l1 != null || l2 != null || tmp != 0) {
			if (l1 != null) {
				tmp += l1.val;
				l1 = l1.next;
			}
			if (l2 != null) {
				tmp += l2.val;
				l2 = l2.next;
			}

			p.next = new ListNode(tmp % 10);
			p = p.next;
			tmp = tmp / 10;
		}
		return head.next;
	}
	
	public ListNode addTwoNumbers_self(ListNode l1, ListNode l2) {
        if (l2 == null)
            return l1;
        if (l1 == null)
            return l2;
        
        ListNode head = new ListNode(0);
        ListNode cur = head;
        ListNode cur1 = l1;
        ListNode cur2 = l2;
        int propergate = 0;
        
        while (cur1 != null && cur2 != null) {
            int value = cur1.val + cur2.val + propergate;
            if (value >= 10) {
                value -= 10;
                propergate = 1;
            }
            else {
                propergate = 0;
            }
            cur.next = new ListNode(value);
            cur1 = cur1.next;
            cur2 = cur2.next;
            cur = cur.next;
        }
        
        while (cur1 != null) {
            int value = cur1.val + propergate;
            if (value >= 10) {
                value -= 10;
                propergate = 1;
            }
            else {
                propergate = 0;
            }
            cur.next = new ListNode(value);
            cur1 = cur1.next;
            cur = cur.next;
        }
        
        while (cur2 != null) {
            int value = cur2.val + propergate;
            if (value >= 10) {
                value -= 10;
                propergate = 1;
            }
            else {
                propergate = 0;
            }
            cur.next = new ListNode(value);
            cur2 = cur2.next;
            cur = cur.next;
        }
        
        if (propergate == 1)
            cur.next = new ListNode(1);
        return head.next;
    }

}
