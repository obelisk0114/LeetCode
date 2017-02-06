package OJ201_210;

public class Remove_Linked_List_Elements {
	private class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { val = x; }
	}
	
	public ListNode removeElements(ListNode head, int val) {
		if (head == null) {
			return head;
		}
		while (head != null && head.val == val) {
			head = head.next;
		}
		
		ListNode current = head;
		while(current != null && current.next != null) {
			if (current.next.val == val) {
				current.next = current.next.next;
			}
			else {				
				current = current.next;
			}
		}
		return head;
	}
	
	private ListNode removeElements2(ListNode head, int val) {
        ListNode fakeHead = new ListNode(-1);
        fakeHead.next = head;
        ListNode curr = head, prev = fakeHead;
        while (curr != null) {
            if (curr.val == val) {
                prev.next = curr.next;
            } else {
                prev = prev.next;
            }
            curr = curr.next;
        }
        return fakeHead.next;
    }
	
	private ListNode removeElements3(ListNode head, int val) {
		ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        
        while (head.next != null) {
            if (head.next.val == val) {
                head.next = head.next.next;
            } else {
                head = head.next;
            }
        }
        
        return dummy.next;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Remove_Linked_List_Elements removeList = new Remove_Linked_List_Elements();
		
		Remove_Linked_List_Elements.ListNode l1 = removeList.new ListNode(2);
		Remove_Linked_List_Elements.ListNode l2 = removeList.new ListNode(2);
		Remove_Linked_List_Elements.ListNode l3 = removeList.new ListNode(2);
		Remove_Linked_List_Elements.ListNode l4 = removeList.new ListNode(4);
		Remove_Linked_List_Elements.ListNode l5 = removeList.new ListNode(4);
		Remove_Linked_List_Elements.ListNode l6 = removeList.new ListNode(6);
		Remove_Linked_List_Elements.ListNode l7 = removeList.new ListNode(9);
		Remove_Linked_List_Elements.ListNode l8 = removeList.new ListNode(2);
		Remove_Linked_List_Elements.ListNode l9 = removeList.new ListNode(9);
		Remove_Linked_List_Elements.ListNode l10 = removeList.new ListNode(10);
		
		l1.next = l2;     l2.next = l3;     l3.next = l4;     l4.next = l5;
		l5.next = l6;     l6.next = l7;     l7.next = l8;     l8.next = l9;
		l9.next = l10;
		
		System.out.println("Original : ");
		ListNode o = l1;
		while(o != null){
            System.out.print(o.val+" ");
            o = o.next;
        }
		
		ListNode p = removeList.removeElements(l1, 2);
		System.out.println("\nAfter : ");
        while(p != null){
            System.out.print(p.val+" ");
            p = p.next;
        }
        ListNode p2 = removeList.removeElements2(l1, 4);
        System.out.println("\nTest 1 : ");
        while(p2 != null){
            System.out.print(p2.val+" ");
            p2 = p2.next;
        }
        ListNode p3 = removeList.removeElements3(l1, 9);
        System.out.println("\nTest 2 : ");
        while(p3 != null){
            System.out.print(p3.val+" ");
            p3 = p3.next;
        }
	}

}
