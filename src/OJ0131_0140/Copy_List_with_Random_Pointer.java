package OJ0131_0140;

import java.util.Map;
import java.util.HashMap;

public class Copy_List_with_Random_Pointer {
	/*
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43488/Java-O(n)-solution
	 * 
	 * key: old; value: new. 
	 * key: old.next; value: corresponding node
	 * 
	 * Rf :
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43488/Java-O(n)-solution/154984
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43488/Java-O(n)-solution/151868
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43488/Java-O(n)-solution/42596
	 * 
	 * Other code:
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43540/Very-short-JAVA-solution-with-Map
	 */
	public Node copyRandomList_HashMap(Node head) {
		if (head == null)
			return null;

		Map<Node, Node> map = new HashMap<Node, Node>();

		// loop 1. copy all the nodes
		Node node = head;
		while (node != null) {
			map.put(node, new Node());
			node = node.next;
		}

		// loop 2. assign next and random pointers
		node = head;
		while (node != null) {
			map.get(node).val = node.val;
			map.get(node).next = map.get(node.next);
			map.get(node).random = map.get(node.random);
			node = node.next;
		}

		return map.get(head);
	}
	
	/*
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43515/My-accepted-Java-code.-O(n)-but-need-to-iterate-the-list-3-times
	 * 
	 * The idea is to associate the original node with its copy node in a single 
	 * linked list. In this way, we don't need extra space to keep track of the new 
	 * nodes.
	 * 
	 * Step 1: create a new node for each existing node and join them together
	 *         eg: A->B->C will be A->A'->B->B'->C->C'
	 * Step 2: copy the random links: for each new node n', n'.random = n.random.next
	 * Step 3: detach the list: basically n.next = n.next.next; n'.next = n'.next.next
	 * 
	 * Rf :
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43491/A-solution-with-constant-space-complexity-O(1)-and-linear-time-complexity-O(N)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43632/Java-very-simple-and-clean-solution-with-O(n)-time-O(1)-space-(with-algorithm)
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43765/O(n)-Time-(3-Passes)-O(1)-Memory-usage-solution!
	 */
	public Node copyRandomList_3n(Node head) {
		if (head == null) {
			return null;
		}
		
		// insert new nodes between originals
		Node walkNode = head;
		while (walkNode != null) {
			Node mirrorNode = new Node();
			Node tmp = walkNode.next;
			
			mirrorNode.val = walkNode.val;
			walkNode.next = mirrorNode;
			mirrorNode.next = tmp;
			
			walkNode = tmp;
		}

		// set the random of each new Nodes
		walkNode = head;
		while (walkNode != null) {
			Node mirrorNode = walkNode.next;
			
			if (walkNode.random != null)
				mirrorNode.random = walkNode.random.next;
			else
				mirrorNode.random = null;
			
			walkNode = walkNode.next.next;
		}

		// detach list
		walkNode = head;
		Node mirrorNode = head.next;
		Node res = head.next;
		while (mirrorNode != null && walkNode != null) {
			walkNode.next = walkNode.next.next;
			if (mirrorNode.next == null) {
				break;
			}
			mirrorNode.next = mirrorNode.next.next;

			mirrorNode = mirrorNode.next;
			walkNode = walkNode.next;
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43488/Java-O(n)-solution/217138
	 */
	public Node copyRandomList_DFS(Node head) {
		Map<Node, Node> map = new HashMap<Node, Node>();
		return copyRandomList_DFS(head, map);
	}

	private Node copyRandomList_DFS(Node head, Map<Node, Node> map) {
		if (head == null) {
			return null;
		}
		if (map.containsKey(head)) {
			return map.get(head);
		}

		Node newHead = new Node();
		newHead.val = head.val;
		map.put(head, newHead);
		
		newHead.next = copyRandomList_DFS(head.next, map);
		newHead.random = copyRandomList_DFS(head.random, map);
		return newHead;
	}
	
	// https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43726/IS-there-any-faster-method
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43485/Clear-and-short-python-O(2n)-and-O(n)-solution
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43689/Python-solution-without-using-dictionary.
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/258935/Detailed-Explanation-with-Pictures-C%2B%2BJavaScript
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43497/2-clean-C%2B%2B-algorithms-without-using-extra-arrayhash-table.-Algorithms-are-explained-step-by-step.
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43567/C%2B%2B-simple-recursive-solution
     * https://leetcode.com/problems/copy-list-with-random-pointer/discuss/43714/Accepted-C-solution-Rt-O(n)-Sp-O(1)
     */

	class Node {
		public int val;
		public Node next;
		public Node random;
		
		public Node() {}
		
		public Node(int _val,Node _next,Node _random) {
			val = _val;
			next = _next;
			random = _random;
		}
	}
}

