package OJ0101_0110;

import definition.TreeNode;
import definition.ListNode;

import java.util.List;
import java.util.ArrayList;

public class Convert_Sorted_List_to_Binary_Search_Tree {
	/*
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35470/Recursive-BST-construction-using-slow-fast-traversal-on-linked-list
	 * 
	 * Construct from root to leaves:
	 * Traverse the list to get the middle element and make that the root. (2 pointers)
	 * Break the list to 2 parts (prevent finding the same middle element) 
	 * left side of the list (Start to prev) forms left sub-tree and 
	 * right side of the middle element (slow.next to fast) forms the right sub-tree.
	 * 
	 * Rf :
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35470/Recursive-BST-construction-using-slow-fast-traversal-on-linked-list/33613
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/169275/Java-2-Solutions-O(n)-and-O(nlogn)-solutions-with-explanation
	 * https://leetcode.com/articles/convert-sorted-list-to-binary-search-tree/
	 * 
	 * Other code : 
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35470/Recursive-BST-construction-using-slow-fast-traversal-on-linked-list/33612
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35470/Recursive-BST-construction-using-slow-fast-traversal-on-linked-list/186912
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35589/Share-My-Easy-Understatnd-Java-Solution.
	 */
	public TreeNode sortedListToBST_3_nodes(ListNode head) {
		if (head == null)
			return null;
		if (head.next == null)
			return new TreeNode(head.val);
		
		ListNode fast = head;
		ListNode slow = head;
		ListNode prev = null;         // prev.next = slow
		
		// find the mid node
		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			prev = slow;
			slow = slow.next;
		}
		
		prev.next = null;   // break the link
		TreeNode root = new TreeNode(slow.val);

		root.left = sortedListToBST_3_nodes(head);
		root.right = sortedListToBST_3_nodes(slow.next);
		return root;
	}
	
	// The following 2 functions are by myself.
	public TreeNode sortedListToBST_self(ListNode head) {
		List<Integer> list = new ArrayList<>();
		ListNode h = head;
		while (h != null) {
			list.add(h.val);
			h = h.next;
		}
		return plantBST_self(list, 0, list.size() - 1);
	}

	public TreeNode plantBST_self(List<Integer> list, int start, int end) {
		if (start == end) {
			return new TreeNode(list.get(start));
		}
		if (start > end) {
			return null;
		}

		int pos = (start + end) / 2;
		TreeNode cur = new TreeNode(list.get(pos));
		cur.left = plantBST_self(list, start, pos - 1);
		cur.right = plantBST_self(list, pos + 1, end);
		return cur;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35472/Share-my-O(1)-space-and-O(n)-time-Java-code
	 * 
	 * Construct from leaves to root: 
	 * time complexity O(n)
	 * space complexity O(log n); extra space is used by the recursion stack and 
	 * we are building a height balanced BST
	 * 
	 * The first time the program reaches "node = node.next" will be when the program 
	 * call inorderHelper(start, mid - 1) for the left most node. 
	 * We insert the first element of linked list to the left most node.
	 * And then we do "node = node.next", we have the second element ready, which will 
	 * be assigned to the parent node of left most node. 
	 * And then node = node.next happens again, the third element is ready for the 
	 * parent node's right node.
	 * 
	 * The leftmost element in the inorder traversal has to be the head of our given 
	 * linked list. Similarly, the next element in the inorder traversal will be the 
	 * second element in the linked list and so on. This is made possible because the 
	 * initial list given to us is sorted in ascending order.
	 * 
	 * 1. Iterate over the linked list to find out it's length. Use two different 
	 *    pointer to mark the beginning and the end of the list.
	 * 2. Simulate the inorder traversal. 
	 *    Use middle element to make recursive calls on the two halves.
	 * 3. Recurse on the left half by (start, mid - 1) as (starting, ending) points.
	 * 4. The invariance that we maintain in this algorithm is that whenever we are 
	 *    done building the left half of the BST, the head pointer in the linked list 
	 *    will point to the root node or the middle node (which becomes the root). So, 
	 *    we simply use the current value pointed to by head as the root node and 
	 *    progress the head node by once i.e. head = head.next
	 * 5. Recurse on the right hand side using mid + 1, end as the starting and ending points.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35472/Share-my-O(1)-space-and-O(n)-time-Java-code/194055
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/169275/Java-2-Solutions-O(n)-and-O(nlogn)-solutions-with-explanation
	 * https://leetcode.com/articles/convert-sorted-list-to-binary-search-tree/
	 */
	private ListNode node;
	
	public TreeNode sortedListToBST_inorder(ListNode head) {
		if (head == null) {
			return null;
		}

		// Get the size of the linked list first
		int size = 0;
		ListNode runner = head;
		node = head;

		while (runner != null) {
			runner = runner.next;
			size++;
		}

		// Form the BST now that we know the size
		return inorderHelper(0, size - 1);
	}

	public TreeNode inorderHelper(int start, int end) {
		// Invalid case
		if (start > end) {
			return null;
		}

		int mid = start + (end - start) / 2;
		
		// First step of simulated inorder traversal.
	    // Recursively form the left half (from 0 to n/2)
		TreeNode left = inorderHelper(start, mid - 1);

		// Once left half is traversed, process the current node (n/2)
		TreeNode treenode = new TreeNode(node.val);
		treenode.left = left;
		
		// Maintain the invariance mentioned in the algorithm
		node = node.next;

		// Recurse on the right hand side and form BST out of them (n/2 + 1 till n)
		TreeNode right = inorderHelper(mid + 1, end);
		treenode.right = right;

		return treenode;
	}
	
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35472/Share-my-O(1)-space-and-O(n)-time-Java-code
	 */
	public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
			return null;
		}

		// Get the size of the linked list first
		int size = 0;
		ListNode runner = head;

		while (runner != null) {
			runner = runner.next;
			size++;
		}

        ListNode[] passRef = {head};     // Pass the reference
		return inorderHelper2(passRef, 0, size - 1);
    }
    
    public TreeNode inorderHelper2(ListNode[] node, int start, int end) {
		if (start > end) {
			return null;
		}
		if (start == end) {
			TreeNode treenode = new TreeNode(node[0].val);
			node[0] = node[0].next;
			return treenode;
		}

		int mid = start + (end - start) / 2;
		
		// First step of simulated inorder traversal.
	    // Recursively form the left half (from 0 to n/2)
		TreeNode left = inorderHelper2(node, start, mid - 1);

		// Once left half is traversed, process the current node (n/2)
		TreeNode treenode = new TreeNode(node[0].val);
		treenode.left = left;
		
		// Maintain the invariance mentioned in the algorithm
		node[0] = node[0].next;

		// Recurse on the right hand side and form BST out of them (n/2 + 1 till n)
		TreeNode right = inorderHelper2(node, mid + 1, end);
		treenode.right = right;

		return treenode;
	}
	
	/*
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35530/My-Java-solution-using-bottom-up-approach-O(n)-time-and-O(logn)-space
	 * 
	 * Bottom-up approach, O(n) time and O(logn) space
	 */
	private TreeNode createBSTBottomUp(ListNode[] head, int len) {
		if (len == 0) {
			return null;
		}
		
		TreeNode leftNode = createBSTBottomUp(head, len / 2);
		
		TreeNode root = new TreeNode(head[0].val);
		root.left = leftNode;
		
		head[0] = head[0].next;
		
		root.right = createBSTBottomUp(head, len - len / 2 - 1);
		return root;
	}

	public TreeNode sortedListToBST_BottomUp(ListNode head) {
		int len = 0;
		for (ListNode cur = head; cur != null; cur = cur.next, len++)
			;
		return createBSTBottomUp(new ListNode[] { head }, len);
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35476/Share-my-JAVA-solution-1ms-very-short-and-concise.
	 * 
	 * Set tail to be exclusive.
	 * Use null to represent the initial tail.
	 * 
	 * Rf :
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35476/Share-my-JAVA-solution-1ms-very-short-and-concise./33681
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35476/Share-my-JAVA-solution-1ms-very-short-and-concise./33673
	 * leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35476/Share-my-JAVA-solution-1ms-very-short-and-concise./187563
	 */
	public TreeNode sortedListToBST_no_break(ListNode head) {
		if (head == null)
			return null;
		return toBST(head, null);
	}

	public TreeNode toBST(ListNode head, ListNode tail) {
		if (head == tail)
			return null;
		
		ListNode slow = head;
		ListNode fast = head;

		while (fast != tail && fast.next != tail) {
			fast = fast.next.next;
			slow = slow.next;
		}
		
		TreeNode thead = new TreeNode(slow.val);
		thead.left = toBST(head, slow);
		thead.right = toBST(slow.next, tail);
		return thead;
	}
	
	/*
	 * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35596/Java-1ms-solution.-Easy-understood.-The-main-idea-of-the-solution-is-similar-to-merge-sort.
	 * 
	 * The main idea of the solution is similar to merge sort.
	 * (#148 Sort List https://leetcode.com/problems/sort-list/)
	 * 
	 * Divide the sorted list into halves.
	 * The middle of the list is root. 
	 * The left half of the list is the left child of root.
	 * The right half of the list is the right child of root.
	 * Then do the same to the left child and right child recursively.
	 */
	public TreeNode sortedListToBST_2_nodes(ListNode head) {
		if (head == null) {
			return null;
		}
		if (head.next == null) {
			return new TreeNode(head.val);
		}
		
		ListNode slow = head;
		ListNode fast = head.next.next;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		
		TreeNode root = new TreeNode(slow.next.val);
		ListNode temp = slow.next.next;
		
		slow.next = null;
		
		root.left = sortedListToBST_2_nodes(head);
		root.right = sortedListToBST_2_nodes(temp);
		return root;
	}
	    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35526/Python-solutions-(convert-to-array-first-top-down-approach-bottom-up-approach)
     * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35474/Python-recursive-solution-with-detailed-comments-(operate-linked-list-directly).
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35555/Clean-C++-solution.-Recursion.-O(nlogn).-With-comment
     * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35525/Share-my-code-with-O(n)-time-and-O(1)-space
     * https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/discuss/35483/My-Accepted-C++-solution
     */

}
