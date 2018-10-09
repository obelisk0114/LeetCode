package OJ0321_0330;

import definition.ListNode;

public class Odd_Even_Linked_List {
	/*
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78231/Simple-Java-Solution-with-Explanation
	 * 
	 * We just need to form a linked list of all odd nodes(X) and another linked list 
	 * of all even nodes(Y). Afterwards, we link Y to the end of X, and return the 
	 * head of X.
	 * 
	 * Rf : 
	 * https://leetcode.com/articles/odd-even-linked-list/
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78079/Simple-O(N)-time-O(1)-space-Java-solution.
	 * 
	 * Other code : 
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/152117/3ms-Java-solution
	 */
	public ListNode oddEvenList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}

		ListNode odd = head;
		ListNode even = head.next;
		ListNode even_head = head.next;

		while (even != null && even.next != null) {
			odd.next = odd.next.next;
			even.next = even.next.next;
			odd = odd.next;
			even = even.next;
		}
		odd.next = even_head;
		return head;
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78197/1ms-Java-Solution
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78120/Straigntforward-Java-solution-O(1)-space-O(n)-time
	 */
	public ListNode oddEvenList_self(ListNode head) {
        if (head == null || head.next == null || head.next.next == null)
            return head;
        
        ListNode second = head.next;
        ListNode odd = head;
        ListNode even = second;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = second;
        return head;
    }
	
	// https://leetcode.com/problems/odd-even-linked-list/discuss/78246/Java-solution-O(n)-time-O(1)-space
	public ListNode oddEvenList_go_through_each_node(ListNode head) {
		if (head == null || head.next == null)
			return head;

		ListNode firstEven = head.next;
		ListNode lastOdd = head;
		ListNode cur = head;
		int n = 1;
		
		while (true) {
			if (n % 2 == 1)
				lastOdd = cur;

			if (cur.next == null) {
				lastOdd.next = firstEven;
				break;
			}

			ListNode nextNode = cur.next;
			cur.next = cur.next.next;
			cur = nextNode;

			n++;
		}
		return head;
	}
	
	/*
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/111755/JAVAfastslow-pointers-in-place-solution:-commented-explained-illlustrated
	 * 
	 * Use slow and fast two pointers to traverse at the same time, slow moves 1 step 
	 * each iteration, while fast 2. At each iteration, I first delete out fast from 
	 * its original position, then insert it in front of slow.
	 */
	public ListNode oddEvenList_move_forward(ListNode head) {
		ListNode slow = head, fast = head;
		while (fast != null) {
			ListNode slowParent = slow;
			slow = slow.next;
			
			if ((fast = fast.next) == null)
				break;
			ListNode fastParent = fast;
			if ((fast = fast.next) == null)
				break;
			
			/*
			 * Two lessons: 
			 * 1. always a good habit to cache next_pointer in linked list manipulation.
			 * 2. try thinking conceptually about what you did if you are stuck 
			 *    trying to see patterns simply from trace.
			 */
			ListNode nextSlow = fast, nextFast = fastParent;
			
			fastParent.next = fast.next;
			fast.next = slow;
			slowParent.next = fast;
			
			/* Advance */
			slow = nextSlow;
			fast = nextFast;
		}
		return head;
	}
	
	/**
	 * Other programming language collections
	 * 
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78095/Clear-Python-Solution
	 * https://leetcode.com/problems/odd-even-linked-list/discuss/78123/Python-solution-with-two-pointers-O(N)
	 */

}
