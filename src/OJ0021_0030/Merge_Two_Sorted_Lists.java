package OJ0021_0030;

public class Merge_Two_Sorted_Lists {
	private class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { val = x; }
	}
	
	ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null && l2 == null) {
			return l1;
		}
		if (l1 == null && l2 != null) {
			return l2;
		}
		if (l1 != null && l2 == null) {
			return l1;
		}
		
		ListNode l3 = new ListNode(-1);
		ListNode current = l3;
		while (l1 != null && l2 != null) {
			if (l1.val < l2.val) {
				current.next = l1;
				l1 = l1.next;
			}
			else {
				current.next = l2;
				l2 = l2.next;
			}
			
			current = current.next;
		}
		while (l1 != null) {
			current.next = l1;
			l1 = l1.next;
			current = current.next;
		}
		while (l2 != null) {
			current.next = l2;
			l2 = l2.next;
			current = current.next;
		}
		return l3.next;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Merge_Two_Sorted_Lists sortlist = new Merge_Two_Sorted_Lists();
		
		Merge_Two_Sorted_Lists.ListNode l1 = sortlist.new ListNode(1);
		Merge_Two_Sorted_Lists.ListNode l2 = sortlist.new ListNode(3);
		Merge_Two_Sorted_Lists.ListNode l3 = sortlist.new ListNode(5);
		Merge_Two_Sorted_Lists.ListNode l4 = sortlist.new ListNode(6);
		Merge_Two_Sorted_Lists.ListNode l5 = sortlist.new ListNode(9);
		Merge_Two_Sorted_Lists.ListNode l6 = sortlist.new ListNode(2);
		Merge_Two_Sorted_Lists.ListNode l7 = sortlist.new ListNode(4);
		Merge_Two_Sorted_Lists.ListNode l8 = sortlist.new ListNode(7);
		Merge_Two_Sorted_Lists.ListNode l9 = sortlist.new ListNode(8);
		Merge_Two_Sorted_Lists.ListNode l10 = sortlist.new ListNode(10);
		Merge_Two_Sorted_Lists.ListNode l11 = sortlist.new ListNode(11);
		
		l1.next = l2;     l2.next = l3;     l3.next = l4;     l4.next = l5;
		l6.next = l7;     l7.next = l8;     l8.next = l9;     l9.next = l10;
		l10.next = l11;
		
//		while(l6 != null) {
//			System.out.print(l6.val + " ");
//			l6 = l6.next;
//		}
//		System.out.println();
		
		Merge_Two_Sorted_Lists.ListNode merge = sortlist.mergeTwoLists(l1, l6);
		while(merge != null) {
			System.out.print(merge.val + " ");
			merge = merge.next;
		}

	}

}
