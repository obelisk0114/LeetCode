package OJ0231_0240;

import definition.ListNode;

public class Delete_Node_in_a_Linked_List {
	/*
	 * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65455/1-3-lines-C%2B%2BJavaPythonCCJavaScriptRuby
	 * 
	 * We can't really delete the node, but we can kinda achieve the same effect by 
	 * instead removing the next node after copying its data into the node that we 
	 * were asked to delete.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65573/Pointer-to-head-of-linked-list-missing-from-expected-solution-for-"Delete-Node-in-a-Singly-Linked-List"/67486
	 * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65464/Easy-solution-in-java
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65567/My-java-easy-answer
	 * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65505/Java-Solution
	 */
	public void deleteNode(ListNode node) {
		node.val = node.next.val;
		node.next = node.next.next;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65456/Python-two-lines-solution-copy-value-and-then-delete-the-next-node.
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65547/My-C%2B%2B-solution-in-1-line
     * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65461/This-question-is-wrong.You-cannot-delete-the-node
     * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65621/Simple-C%2B%2B-solution
     * https://leetcode.com/problems/delete-node-in-a-linked-list/discuss/65575/4ms-C-solution-(with-explanation)
     */

}
