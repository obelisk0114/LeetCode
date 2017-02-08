package OJ141_150;

import definition.ListNode;

public class Insertion_Sort_List {
	private ListNode insertionSortList(ListNode head) {
		if( head == null || head.next == null){
			return head;
		}
		
		ListNode current = head;
		ListNode dummy = new ListNode(-1);
		dummy.next = head;
		ListNode pre = dummy;
		while (current.next != null) {
			pre = dummy;          // Reset
			if (current.next.val >= current.val) {
				current = current.next;
				continue;
			}
			while (pre.next != current && pre.next.val < current.next.val) {
				pre = pre.next;
			}
			ListNode tmp = current.next;
			current.next = current.next.next;
			tmp.next = pre.next;
			pre.next = tmp;
		}
		return dummy.next;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Insertion_Sort_List insertionSortList = new Insertion_Sort_List();
		ListNode l1 = new ListNode(3);  // 3
		ListNode l2 = new ListNode(7);  // 2
		ListNode l3 = new ListNode(6);  // 7
		ListNode l4 = new ListNode(2);  // 6
		ListNode l5 = new ListNode(5);  // 5
		ListNode l6 = new ListNode(1);  // 1
		ListNode l7 = new ListNode(4);  // 4
		
		l1.next = l2;     l2.next = l3;     l3.next = l4;     l4.next = l5;
		l5.next = l6;     l6.next = l7;
		
		System.out.println("Original : ");
		ListNode o = l1;
		while(o != null){
            System.out.print(o.val+" ");
            o = o.next;
        }
		ListNode p = insertionSortList.insertionSortList(l1);
		System.out.println("\nAfter : ");
        while(p != null){
            System.out.print(p.val+" ");
            p = p.next;
        }

	}

}
