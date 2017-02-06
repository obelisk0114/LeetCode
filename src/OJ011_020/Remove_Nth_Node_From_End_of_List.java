package OJ011_020;

import definition.ListNode;

public class Remove_Nth_Node_From_End_of_List {
	public ListNode removeNthFromEnd(ListNode head, int n) {
		if (n == 0 || head == null) {
			return head;
		}
		
		ListNode fast = head;
		ListNode slow = head;
		for (int i = 0; i < n; i++) {
			fast = fast.next;
		}
		
		// It is the end of the list. Need to delete the head;
		if (fast == null) {
			return head.next;
		}
		
		while (fast.next != null) {
			fast = fast.next;
			slow = slow.next;
		}
		slow.next = slow.next.next;
		return head;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Remove_Nth_Node_From_End_of_List removeN = new Remove_Nth_Node_From_End_of_List();
		ListNode l1 = new ListNode(1);
		ListNode l2 = new ListNode(2);
		ListNode l3 = new ListNode(3);
		ListNode l4 = new ListNode(4);
		ListNode l5 = new ListNode(5);
		ListNode l6 = new ListNode(6);
		ListNode l7 = new ListNode(7);
		
		l1.next = l2;     l2.next = l3;     l3.next = l4;     l4.next = l5;
		l5.next = l6;     l6.next = l7;
		
		System.out.println("Original : ");
		ListNode o = l1;
		while(o != null){
            System.out.print(o.val+" ");
            o = o.next;
        }
		
		ListNode p = removeN.removeNthFromEnd(l1, 7);
		System.out.println("\nAfter : ");
        while(p != null){
            System.out.print(p.val+" ");
            p = p.next;
        }

	}

}
