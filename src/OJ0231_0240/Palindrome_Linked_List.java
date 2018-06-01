package OJ0231_0240;

import definition.ListNode;

public class Palindrome_Linked_List {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64501/Java-easy-to-understand
	 * 
	 * This can be solved by reversing the 2nd half and compare the two halves.
	 */
	public boolean isPalindrome(ListNode head) {
	    ListNode fast = head, slow = head;
	    while (fast != null && fast.next != null) {
	        fast = fast.next.next;
	        slow = slow.next;
	    }
	    
	    if (fast != null) { // odd nodes: let right half smaller. Can be removed
	        slow = slow.next;
	    }
	    
	    slow = reverse(slow);
	    fast = head;
	    
	    while (slow != null) {
	        if (fast.val != slow.val) {
	            return false;
	        }
	        fast = fast.next;
	        slow = slow.next;
	    }
	    return true;
	}
	public ListNode reverse(ListNode head) {
	    ListNode prev = null;
	    while (head != null) {
	        ListNode next = head.next;
	        head.next = prev;
	        prev = head;
	        head = next;
	    }
	    return prev;
	}
	
	/*
	 * leetcode.com/problems/palindrome-linked-list/discuss/64500/11-lines-12-with-restore-O(n)-time-O(1)-space/117753
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64500/11-lines-12-with-restore-O(n)-time-O(1)-space
	 * 
	 * Phase 1: Reverse the first half while finding the middle.
	 * Phase 2: Compare the reversed first half with the second half.

	 * Other code:
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64573/Easy-understand-JAVA-solution-(O(1)-space-cost)
	 */
	public boolean isPalindrome_traverse_and_reverse(ListNode head) {
		if (head == null) {
			return true;
		}
		
		ListNode slow = head, fast = head, rev = null;
		while (fast != null && fast.next != null) {
			ListNode temp = rev;
			rev = slow;
			slow = slow.next;
			fast = fast.next.next;
			rev.next = temp;
		}
		
		if (fast != null) {
			slow = slow.next;
		}
		
		while (rev != null) {
			if (slow.val != rev.val) {
				return false;
			}
			slow = slow.next;
			rev = rev.next;
		}
		return true;
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64728/My-java-1ms-solution-with-reverse-half-list
	 */
	public boolean isPalindrome_self(ListNode head) {
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            cur = cur.next;
            n++;
        }
        
        if (n < 2) {
            return true;
        }
        
        cur = head;
        ListNode first = head;
        int count = (n + 1) / 2;
        while (count > 0) {
            cur = cur.next;
            count--;
        }
        
        ListNode pre = null;
        while (cur != null) {
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        
        while (pre != null) {
            if (pre.val != first.val) {
                return false;
            }
            
            pre = pre.next;
            first = first.next;
        }
        return true;
    }
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64533/Concise-O(N)-O(N)-Java-Solution-without-reversing-the-list
	 */
	public ListNode root;
	public boolean isPalindrome_recursive(ListNode head) {
		root = head;
		return (head == null) ? true : _isPalindrome(head);
	}
	public boolean _isPalindrome(ListNode head) {
		boolean flag = true;
		if (head.next != null) {
			flag = _isPalindrome(head.next);
		}
		
		if (flag && root.val == head.val) {
			root = root.next;
			return true;
		}
		return false;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/palindrome-linked-list/discuss/64532/Share-my-Java-answer
	 */
	ListNode h;
    public boolean isPalindrome_recursive2(ListNode head) {
		if (head == null)
			return true;

		if (h == null)
			h = head;

		boolean tmp = true;
		if (head.next != null)
			tmp &= isPalindrome_recursive2(head.next);

		tmp &= (head.val == h.val);
		h = h.next;
		return tmp;
    }
	
	// https://leetcode.com/problems/palindrome-linked-list/discuss/64493/Reversing-a-list-is-not-considered-%22O(1)-space%22

}
