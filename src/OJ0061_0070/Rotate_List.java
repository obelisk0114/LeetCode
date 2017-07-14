package OJ0061_0070;

import definition.ListNode;

public class Rotate_List {
	// https://discuss.leetcode.com/topic/30449/easy-and-straightforward-java-solution
	ListNode rotateRight_NoDummy(ListNode head, int k) {
		if (head == null || head.next == null)
			return head;

		// Get length
		int len = 1;
		ListNode tail = head;
		while (tail.next != null) {
			tail = tail.next;
			len++;
		}

		// Go to position k distance to tail
		k = k % len;
		if (k == 0)
			return head;
		ListNode newTail = head;
		for (int i = 0; i < len - k - 1; i++) {
			newTail = newTail.next;
		}

		// Join two parts
		ListNode newHead = newTail.next;
		newTail.next = null;
		tail.next = head;

		return newHead;
	}
	
	// https://discuss.leetcode.com/topic/2861/share-my-java-solution-with-explanation
	ListNode rotateRight_dummy(ListNode head, int n) {
		if (head == null || head.next == null)
			return head;
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode fast = dummy, slow = dummy;

		int i;
		for (i = 0; fast.next != null; i++)// Get the total length
			fast = fast.next;

		for (int j = i - n % i; j > 0; j--) // Get the i-n%i th node
			slow = slow.next;

		fast.next = dummy.next; // Do the rotation
		dummy.next = slow.next;
		slow.next = null;

		return dummy.next;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/26364/clean-java-solution-with-brief-explanation
	 * The basic idea is to link the tail of the list with the head, make it a cycle.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/42445/java-clean-solution-only-one-pointer-used
	 * https://discuss.leetcode.com/topic/2312/anyone-solve-the-problem-without-counting-the-length-of-list
	 */
	ListNode rotateRight_cycle(ListNode head, int k) {
		if (head == null)
			return head;

		ListNode copyHead = head;

		int len = 1;
		while (copyHead.next != null) {
			copyHead = copyHead.next;
			len++;
		}

		copyHead.next = head;

		for (int i = len - k % len; i > 1; i--)
			head = head.next;

		copyHead = head.next;
		head.next = null;

		return copyHead;
	}
	
	ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null)
            return head;
        ListNode tmp = head;
        ListNode tmp2 = head;
        int n = 0;
        while (tmp != null) {
            tmp = tmp.next;
            n++;
        }
        k %= n;
        if (k == 0)
            return head;
        for (int i = 1; i < n-k; i++) {
            tmp2 = tmp2.next;
        }
        ListNode tmp3 = tmp2.next;
        tmp = tmp2.next;
        tmp2.next = null;
        for (int i = 1; i < k; i++) {
            tmp3 = tmp3.next;
        }
        tmp3.next = head;
        return tmp;
    }

}
