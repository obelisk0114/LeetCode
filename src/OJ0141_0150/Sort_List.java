package OJ0141_0150;

/*
 * https://discuss.leetcode.com/topic/4860/java-solution-with-strict-o-1-auxiliary-space-complexity/2
 */

import definition.ListNode;

public class Sort_List {
	// Top-down merge sort
	private ListNode sortList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}

		// step 1. cut the list to two halves
		ListNode prev = null, slow = head, fast = head;

		while (fast != null && fast.next != null) { // {3, 2, 4}, {2, 1}
			prev = slow; // {2, 1}
			slow = slow.next;
			fast = fast.next.next;
		}

		prev.next = null;

		// step 2. sort each half
		ListNode l1 = sortList(head);
		ListNode l2 = sortList(slow);

		// step 3. merge l1 and l2
		return merge(l1, l2);
	}

	private ListNode merge(ListNode l1, ListNode l2) {
		ListNode l = new ListNode(0), p = l;

		while (l1 != null && l2 != null) {
			if (l1.val < l2.val) {
				p.next = l1;
				l1 = l1.next;
			} else {
				p.next = l2;
				l2 = l2.next;
			}
			p = p.next;
		}
		
		if (l1 != null)
			p.next = l1;

		if (l2 != null)
			p.next = l2;
		
		// Can also change to these
		/*
		while (l1 != null) {
			p.next = l1;
			l1 = l1.next;
			p = p.next;
		}

		while (l2 != null) {
			p.next = l2;
			l2 = l2.next;
			p = p.next;
		}
		*/

		return l.next;
	}
	
	// https://discuss.leetcode.com/topic/3085/my-o-n-log-n-time-o-1-space-solution/2
	// Bottom up merge sort
	/*
	 * Round #1 block_size = 1
       (a1, a2), (a3, a4), (a5, a6), (a7, a8)

       Compare a1 with a2, a3 with a4 ...

       Round #2 block_size = 2
       (a1, a2, a3, a4), (a5, a6, a7, a8)

       merge two sorted arrays (a1, a2) and (a3, a4), 
       then merge tow sorted arrays(a5, a6) and (a7, a8)

       Round #3 block_size = 4
       (a1, a2, a3, a4, a5, a6, a7, a8)

       merge two sorted arrays (a1, a2, a3, a4), and (a5, a6, a7, a8)
	 */
	private int count_size(ListNode node){
        int n = 0;
        while (node != null){
            node = node.next;
            n++;
        }
        return n;
    }
	
	ListNode sortList_bottom_up(ListNode head) {
        int block_size = 1, n = count_size(head), iter = 0, i = 0, a = 0, b = 0;
        ListNode virtual_head = new ListNode(0);
        ListNode last = null, it = null, A = null, B = null, tmp = null;
        virtual_head.next = head;
        while (block_size < n){
            iter = 0;
            last = virtual_head;
            it = virtual_head.next;
            while (iter <  n){
                a = Math.min(n - iter, block_size);
                b = Math.min(n - iter - a, block_size);
                
                A = it;
                if (b != 0){
                    for (i = 0; i < a - 1; ++i) it = it.next;
                    B = it.next;
                    it.next = null;
                    it = B;
                    
                    for (i = 0; i < b - 1; ++i) it = it.next;
                    tmp = it.next;
                    it.next = null;
                    it = tmp;
                }
                
                while (A != null || B != null){
                    if (B == null || (A != null && A.val <= B.val)){
                        last.next = A;
                        last = last.next;
                        A = A.next;
                    } else {
                        last.next = B;
                        last = last.next;
                        B = B.next;
                    }
                }
                last.next = null;
                iter += a + b;
            }
            block_size <<= 1;
        }
        return virtual_head.next;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sort_List sortList = new Sort_List();
		
		ListNode l1 = new ListNode(6);   // 5
		ListNode l2 = new ListNode(1);   // 1
		ListNode l3 = new ListNode(5);   // 6
		ListNode l4 = new ListNode(8);   // 8
		ListNode l5 = new ListNode(9);   // 9
		ListNode l6 = new ListNode(7);   // 7
		ListNode l7 = new ListNode(4);   // 4
		ListNode l8 = new ListNode(2);   // 2
		ListNode l9 = new ListNode(3);   // 3
		
		l1.next = l2;     l2.next = l3;     l3.next = l4;     l4.next = l5;
		l5.next = l6;     l6.next = l7;     l7.next = l8;     l8.next = l9;
		ListNode s = sortList.sortList_bottom_up(l1);
		while (s != null) {
			System.out.print(s.val + " ");
			s = s.next;
		}
		System.out.println();
		
		s = sortList.sortList(l1);
		while (s != null) {
			System.out.print(s.val + " ");
			s = s.next;
		}
	}

}
