package OJ0021_0030;

import definition.ListNode;

public class Reverse_Nodes_in_k_Group {
	/*
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11582/Simple-Java-iterative-solution-with-explanation
	 * 
	 * Walk through the list nodes, while at the same time, reverse the nodes and keep 
	 * counting the nodes reversed. Start the next round after reversing k nodes Group. 
	 * When the number of the nodes n is a multiply of k, after N iteration, the list 
	 * is k reversed. But when N is not a multiply of k, a recursive call is made to 
	 * undo the reverse of the left-out nodes.
	 * 
	 * 1 -> 2 -> 3 -> 4 -> 5, k = 3: dummy(0) -> 1 -> 2 -> 3 -> 4 -> 5
	 * 
	 * prev   head, tail  newHead
	 *  0   ->    1     ->   2 ->  3 -> 4 -> 5
	 * 
	 * prev      tail, head   newHead
	 *  0 -> 2 ->     1     ->   3   -> 4 -> 5
	 * 
	 * prev            tail, head 
	 *  0  -> 3 -> 2 ->    1     -> 4 -> 5
	 * 
	 * Rf : leetcode.com/problems/reverse-nodes-in-k-group/discuss/11582/Simple-Java-iterative-solution-with-explanation/12223
	 */
	public ListNode reverseKGroup(ListNode head, int k) {
		if (k < 2 || head == null)
			return head;
		
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		ListNode prev = dummy, tail = head, newHead;
		while (true) {
			int count = k - 1;
			while (count > 0) {
				if (tail.next != null) {
					newHead = tail.next;
					tail.next = newHead.next;
					newHead.next = prev.next;
					prev.next = newHead;
					
					count--;
				} 
				else {
					// list size is not multiple of k, a recursive call on the
					// left-out nodes to undo the reverse
					prev.next = reverseKGroup(prev.next, k - count);
					return dummy.next;
				}
			}
			
			if (tail.next == null)
				return dummy.next; /// list size is multiple of k, safely return
			
			prev = tail;
			tail = tail.next;
		}
	}
	
	/*
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11638/Java-O(n)-elegant-code-solution
	 * 
	 * We advance tail for k steps to check for that. 
	 * If there is no k nodes, the program exits from there.
	 * 
	 * 1 -> 2 -> 3 -> 4 -> 5, k = 3: dummy(0) -> 1 -> 2 -> 3 -> 4 -> 5
	 * 
	 * prev  head, start  then  tail
	 *  0   ->    1     ->  2 ->  3 -> 4 -> 5
	 * 
	 * prev     start  tail, then
	 *  0 -> 2 -> 1 ->     3     -> 4 -> 5
	 * 
	 * prev  tail       head
	 *  0  ->  3 -> 2 ->  1 -> 4 -> 5
	 * 
	 */
	public ListNode reverseKGroup_advanced_k(ListNode head, int k) {
        if (k < 2 || head == null || head.next == null)
            return head;
        
        ListNode newHead = new ListNode(0);
        newHead.next = head;
        
        ListNode prev, start, then, tail;
        
        tail = newHead;
        prev = newHead;
        start = prev.next;
        while (true) {
            // check if there's k nodes left-out
            for (int i = 0; i < k; i++) {
                tail = tail.next;
                if (tail == null)
                    return newHead.next;
            }
            
            // reverse k nodes
            for (int i = 0; i < k - 1; i++) {
                then = start.next;
                start.next = then.next;
                then.next = prev.next;
                prev.next = then;
            }
            
            tail = start;
            prev = start;
            start = prev.next;
        }
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11440/Non-recursive-Java-solution-and-idea
	 * 
	 * Reverse a link list between begin and end exclusively
	 * 
	 * begin                 end
	 *   0  -> 1 -> 2 -> 3 -> 4 -> 5 -> 6
	 * 
	 * after call begin = reverse(begin, end)
	 * 
	 *              begin end
	 * 0 -> 3 -> 2 -> 1 -> 4 -> 5 -> 6
	 * 
	 * return the reversed list's 'begin' node, which is the precedence of node end
	 */
	public ListNode reverseKGroup_reverse(ListNode head, int k) {
		if (head == null || head.next == null || k == 1)
			return head;
		
		ListNode dummyhead = new ListNode(-1);
		dummyhead.next = head;
		
		ListNode begin = dummyhead;
		int i = 0;
		while (head != null) {
			i++;
			if (i % k == 0) {
				begin = reverse(begin, head.next);
				head = begin.next;
			} 
			else {
				head = head.next;
			}
		}
		return dummyhead.next;
	}

	public ListNode reverse(ListNode begin, ListNode end) {
		ListNode curr = begin.next;
		ListNode prev = begin;
		
		ListNode next, first;
		first = curr;
		while (curr != end) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		
		begin.next = prev;
		first.next = curr;
		return first;
	}
	
	// https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11423/Short-but-recursive-Java-code-with-comments
	public ListNode reverseKGroup_reverse_recursive(ListNode head, int k) {
	    ListNode curr = head;
	    int count = 0;
	    while (curr != null && count != k) { // find the k+1 node
	        curr = curr.next;
	        count++;
	    }
	    
	    if (count == k) { // if k+1 node is found
	        curr = reverseKGroup_reverse_recursive(curr, k); // reverse list with k+1 node as head
	        // head - head-pointer to direct part, 
	        // curr - head-pointer to reversed part;
	        while (count-- > 0) { // reverse current k-group: 
	            ListNode tmp = head.next; // tmp - next head in direct part
	            head.next = curr; // preappending "direct" head to the reversed list 
	            curr = head; // move head of reversed part to a new node
	            head = tmp; // move "direct" head to the next node in direct part
	        }
	        head = curr;
	    }
	    return head;
	}
	
	/*
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11413/Share-my-Java-Solution-with-comments-in-line
	 * 
	 * 1 -> 2 -> 3 -> 4 -> 5, k = 3: dummy(0) -> 1 -> 2 -> 3 -> 4 -> 5 
	 * 
	 * prev  head, temp        tail
	 *  0   ->    1     -> 2 ->  3 -> 4 -> 5
	 *  
	 * prev  temp  tail  head
	 *  0  ->  2 ->  3  -> 1 -> 4 -> 5
	 *  
	 * prev  tail       head
	 *  0  ->  3 -> 2 ->  1 -> 4 -> 5
	 * 
	 * Rf : leetcode.com/problems/reverse-nodes-in-k-group/discuss/11413/Share-my-Java-Solution-with-comments-in-line/12122
	 */
	public ListNode reverseKGroup_iterative(ListNode head, int k) {
		if (head == null || head.next == null || k < 2)
			return head;

		ListNode dummy = new ListNode(0);
		dummy.next = head;

		ListNode tail = dummy, prev = dummy, temp;
		int count;
		while (true) {
			count = k;
			while (count > 0 && tail != null) {
				count--;
				tail = tail.next;
			}
			if (tail == null)
				break;// Has reached the end

			head = prev.next;// for next cycle
			
			// prev-->temp-->...--->....--->tail-->....
			// Delete @temp and insert to the next position of @tail
			// prev-->...-->...-->tail-->head-->...
			// Assign @temp to the next node of @prev
			// prev-->temp-->...-->tail-->...-->...
			// Keep doing until @tail is the next node of @prev
			while (prev.next != tail) {
				temp = prev.next;// Assign
				prev.next = temp.next;// Delete

				temp.next = tail.next;
				tail.next = temp;// Insert
			}

			tail = head;
			prev = head;
		}
		return dummy.next;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/160490/Java-recursive-solution-only-15-lines-!!!
	 * 
	 * Rf : leetcode.com/problems/reverse-nodes-in-k-group/discuss/11423/Short-but-recursive-Java-code-with-comments/12145
	 */
	public ListNode reverseKGroup_recursive(ListNode head, int k) {
		int cnt = 0;
		for (ListNode curr = head; curr != null; curr = curr.next)
			++cnt;
		return reverseKGroup_recursive(head, k, cnt);
	}

	public ListNode reverseKGroup_recursive(ListNode head, int k, int cnt) {
		if (cnt < k)
			return head;
		
		ListNode curr = head, tmp = null, ans = null;
		for (int i = 0; i < k; ++i) {
			tmp = curr;
			curr = curr.next;
			tmp.next = ans;
			ans = tmp;
		}
		
		head.next = reverseKGroup_recursive(curr, k, cnt - k);
		return ans;
	}

	/**
	 * Other programming language collections
	 * 
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11491/Succinct-iterative-Python-O(n)-time-O(1)-space
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11425/Simple-Python-solution-one-pass-no-additional-space-109ms
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11457/20-line-iterative-C++-solution
	 * https://leetcode.com/problems/reverse-nodes-in-k-group/discuss/11543/24ms-Easy-C++-Iterative-Solution-with-Explanations
	 */

}
