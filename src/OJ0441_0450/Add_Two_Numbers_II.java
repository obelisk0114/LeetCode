package OJ0441_0450;

import definition.ListNode;
import java.util.Stack;
import java.util.HashMap;

public class Add_Two_Numbers_II {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/66699/java-iterative-o-1-space-lastnot9-solution-changed-from-plus-one-linked-list
	 */
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		int len1 = getLength(l1), len2 = getLength(l2);
		if (len1 < len2) { // swap l1 and l2 to make sure l1 is the longer one
			ListNode tmp = l1;
			l1 = l2;
			l2 = tmp;
		}
		int diff = Math.abs(len1 - len2);

		ListNode dummy = new ListNode(0);
		ListNode tail = dummy;
		ListNode lastnot9node = dummy;

		while (diff > 0) {
			// create new node
			tail.next = new ListNode(l1.val);

			// update lastnot9node
			if (l1.val != 9)
				lastnot9node = tail.next;

			// update tails
			tail = tail.next;
			l1 = l1.next;
			diff--;
		}

		while (l1 != null) {
			int val = l1.val + l2.val;

			if (val >= 10) {
				val -= 10;
				// update previous nodes
				lastnot9node.val++;
				lastnot9node = lastnot9node.next;
				
				// ex: 1 -> 9 -> 9 -> 9
				//  lastnot9         tail
				while (lastnot9node != null) {
					lastnot9node.val = 0;
					lastnot9node = lastnot9node.next;
				}
				
				// ex: 2 -> 0 -> 0 -> 0
				//                   tail & lastnot9
				lastnot9node = tail;
			}

			// create new node
			tail.next = new ListNode(val);

			// update lastnot9node
			if (val != 9)
				lastnot9node = tail.next;

			// update tails
			tail = tail.next;
			l1 = l1.next;
			l2 = l2.next;
		}

		if (dummy.val == 1)
			return dummy;
		return dummy.next;
	}
	private int getLength(ListNode node) {
		int len = 0;
		while (node != null) {
			len++;
			node = node.next;
		}
		return len;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/65279/easy-o-n-java-solution-using-stack
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/72961/brilliant-java-solution
	 * https://discuss.leetcode.com/topic/67076/ac-follow-up-java
	 */
	public ListNode addTwoNumbers_stack(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<Integer>();
        Stack<Integer> s2 = new Stack<Integer>();
        
        while(l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        while(l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }
        
        int sum = 0;
        ListNode list = new ListNode(0);
        while (!s1.empty() || !s2.empty()) {
            if (!s1.empty()) sum += s1.pop();
            if (!s2.empty()) sum += s2.pop();
            list.val = sum % 10;
            ListNode head = new ListNode(sum / 10);
            head.next = list;
            list = head;
            sum /= 10;
        }
        
        return list.val == 0 ? list.next : list;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/65306/java-o-n-recursive-solution-by-counting-the-difference-of-length
	 */
	public ListNode addTwoNumbers_recursive(ListNode l1, ListNode l2) {
		int size1 = getLength_recursive(l1);
		int size2 = getLength_recursive(l2);
		
		ListNode head = new ListNode(1);
		// Make sure l1.length >= l2.length
		head.next = size1 < size2 ? helper(l2, l1, size2 - size1) : helper(l1, l2, size1 - size2);
		
		// Handle the first digit
		if (head.next.val > 9) {
			head.next.val = head.next.val % 10;
			return head;
		}
		return head.next;
	}
	// get length of the list
	public int getLength_recursive(ListNode l) {
		int count = 0;
		while (l != null) {
			l = l.next;
			count++;
		}
		return count;
	}
	// offset is the difference of length between l1 and l2
	public ListNode helper(ListNode l1, ListNode l2, int offset) {
		if (l1 == null)
			return null;
		
		// check whether l1 becomes the same length as l2
		ListNode result = offset == 0 ? new ListNode(l1.val + l2.val) : new ListNode(l1.val);
		ListNode post = offset == 0 ? helper(l1.next, l2.next, 0) : helper(l1.next, l2, offset - 1);
		
		// handle carry
		if (post != null && post.val > 9) {
			result.val += 1;
			post.val = post.val % 10;
		}
		
		// combine nodes
		result.next = post;
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/65271/straightforward-o-n-java-solution-without-modifying-input-lists
	 */
	public ListNode addTwoNumbers_HashMap(ListNode l1, ListNode l2) {
		// Store the 'index' and the value of List1  
		HashMap<Integer, Integer> hm1 = new HashMap<>();
		// Store the 'index' and the value of List2
		HashMap<Integer, Integer> hm2 = new HashMap<>(); 
		
		int i = 1, j = 1;

		while (l1 != null) {
			hm1.put(i, l1.val);
			l1 = l1.next;
			i++;
		}
		while (l2 != null) {
			hm2.put(j, l2.val);
			l2 = l2.next;
			j++;
		}

		int carry = 0;
		i--;
		j--;
		ListNode head = null;

		// Create new nodes to the front of a new LinkedList
		while (i > 0 || j > 0 || carry > 0) {
			int a = i > 0 ? hm1.get(i) : 0;
			int b = j > 0 ? hm2.get(j) : 0;
			int res = (a + b + carry) % 10;

			ListNode newNode = new ListNode(res);
			newNode.next = head;
			head = newNode;

			carry = (a + b + carry) / 10;
			i--;
			j--;
		}
		return head;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/81208/java-solution-by-reversing-linkedlist-beating-96
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/65298/java-o-n-scanning-twice-simple-with-comments
	 */
	public ListNode addTwoNumbers_reverse(ListNode l1, ListNode l2) {
		ListNode n1 = reverse(l1);
		ListNode n2 = reverse(l2);
		int carry = 0;
		ListNode temp = n1;
		ListNode pre = n1;
		while (n1 != null || n2 != null || carry != 0) {
			int v1 = n1 == null ? 0 : n1.val;
			int v2 = n2 == null ? 0 : n2.val;
			if (n1 == null) {
				n1 = new ListNode((v1 + v2 + carry) % 10);
				pre.next = n1;
			} else {
				n1.val = (v1 + v2 + carry) % 10;
			}
			carry = (v1 + v2 + carry) / 10;
			pre = n1;
			n1 = n1 == null ? null : n1.next;
			n2 = n2 == null ? null : n2.next;
		}
		return reverse(temp);
	}
	public ListNode reverse(ListNode head) {
		ListNode newHead = null;
		while (head != null) {
			ListNode next = head.next;
			head.next = newHead;
			newHead = head;
			head = next;
		}
		return newHead;
	}

}
