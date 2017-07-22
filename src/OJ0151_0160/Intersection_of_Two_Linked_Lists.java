package OJ0151_0160;

import definition.ListNode;
import java.util.Set;
import java.util.HashSet;

public class Intersection_of_Two_Linked_Lists {
	/*
	 * https://discuss.leetcode.com/topic/28067/java-solution-without-knowing-the-difference-in-len
	 * 
	   1. Scan both lists
       2. For each list once it reaches the end, continue scanning the other list
       3. Once the two runner equal to each other, return the position
       
       A length = a + c, B length = b + c
       After switching pointer, 
       pointer A will move another b + c steps, pointer B will move a + c more steps, 
       since a + c + b + c = b + c + a + c, it does not matter what value c is. 
       Pointer A and B must meet after a + c + b (b + c + a) steps. 
       If c == 0, they meet at NULL.
        
     * Rf : https://discuss.leetcode.com/topic/28067/java-solution-without-knowing-the-difference-in-len/11
	 */
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
	    //boundary check
	    if(headA == null || headB == null) return null;
	    
	    ListNode a = headA;
	    ListNode b = headB;
	    
	    //if a & b have different len, then we will stop the loop after second iteration
	    while( a != b){
	    	//for the end of first iteration, we just reset the pointer to the head of another linkedlist
	        a = a == null? headB : a.next;
	        b = b == null? headA : b.next;    
	    }
	    
	    return a;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/41948/java-beats-99-56
	 * 
	   1. Get the length of the two lists.
       2. Align them to the same start point.
       3. Move them together until finding the intersection point, or the end null
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8245/sharing-my-fast-and-easy-to-understand-accepted-java-solution
	 */
	public ListNode getIntersectionNode_length(ListNode headA, ListNode headB) {
		ListNode p1 = headA, p2 = headB;
		int len1 = 0, len2 = 0;
		while (p1 != null) {
			p1 = p1.next;
			len1++;
		}
		while (p2 != null) {
			p2 = p2.next;
			len2++;
		}

		p1 = headA;
		p2 = headB;
		if (len1 > len2) {
			for (int i = 0; i < len1 - len2; i++) {
				p1 = p1.next;
			}
		} else {
			for (int i = 0; i < len2 - len1; i++) {
				p2 = p2.next;
			}
		}
	    
	    while (p1 != p2) {
	        p1 = p1.next;
	        p2 = p2.next;
	    }
	    return p1;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/5492/concise-java-solution-o-1-memory-o-n-time
	 * 
	   1. Get the length of the two lists.
       2. Align them to the same start point.
       3. Move them together until finding the intersection point, or the end null
	 */
	public ListNode getIntersectionNode_length2(ListNode headA, ListNode headB) {
	    int lenA = length(headA), lenB = length(headB);
	    // move headA and headB to the same start point
	    while (lenA > lenB) {
	        headA = headA.next;
	        lenA--;
	    }
	    while (lenA < lenB) {
	        headB = headB.next;
	        lenB--;
	    }
	    // find the intersection until end
	    while (headA != headB) {
	        headA = headA.next;
	        headB = headB.next;
	    }
	    return headA;
	}
	private int length(ListNode node) {
	    int length = 0;
	    while (node != null) {
	        node = node.next;
	        length++;
	    }
	    return length;
	}
	
	// https://discuss.leetcode.com/topic/30134/java-o-n-time-o-1-space-solution-by-using-assume-there-are-no-cycles
	public ListNode getIntersectionNode_cycle(ListNode headA, ListNode headB) {
		if (headA == null || headB == null) return null;
		// find last node of list A (c3)
		ListNode endA = headA;
		while (endA.next != null) {
			endA = endA.next;
		}
		// join c3 to b1 making a c1...c3-b1...b3-c1 loop (if b3 indeed points to c1)
		endA.next = headB;

		ListNode start = null; // if there's no cycle this will stay null
		// Floyd's cycle finder
		ListNode slow = headA, fast = headA;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) { // found a cycle
				// reset to beginning to find cycle start point (c1)
				start = headA;
				while (slow != start) {
					slow = slow.next;
					start = start.next;
				}
				break;
			}
		}
		// unjoin c3-b1
		endA.next = null;
		return start;
	}
	
	// space : O(n)
	public ListNode getIntersectionNode_hash(ListNode headA, ListNode headB) {
        if (headA == null || headB == null)
            return null;
        
        Set<ListNode> set = new HashSet<>();
        ListNode tr = headA;
        while (tr != null) {
            set.add(tr);
            tr = tr.next;
        }
        
        ListNode tr2 = headB;
        while (tr2 != null) {
            if (!set.add(tr2))
                return tr2;
            tr2 = tr2.next;
        }
        return null;
    }

}
