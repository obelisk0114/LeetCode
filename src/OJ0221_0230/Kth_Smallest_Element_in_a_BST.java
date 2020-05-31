package OJ0221_0230;

import definition.TreeNode;

import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.PriorityQueue;

public class Kth_Smallest_Element_in_a_BST {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
	 */
	public int kthSmallest_DFS_List(TreeNode root, int k) {
		ArrayList<Integer> nums = inorder(root, new ArrayList<Integer>());
		return nums.get(k - 1);
	}
	
	public ArrayList<Integer> inorder(TreeNode root, ArrayList<Integer> arr) {
		if (root == null)
			return arr;
		
		inorder(root.left, arr);
		arr.add(root.val);
		inorder(root.right, arr);
		return arr;
	}
	
	/*
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63789/Iterative-in-order-traversal-using-stack-(Java-solution)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63783/Two-Easiest-In-Order-Traverse-(Java)
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/642294/Java-Using-Stack-as-Iterator-Picture-explain-Clean-code-O(H-%2B-k)
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63660/3-ways-implemented-in-JAVA-(Python)%3A-Binary-Search-in-order-iterative-and-recursive
	 */
	public int kthSmallest_iteration(TreeNode root, int k) {
		Stack<TreeNode> stack = new Stack<>();
		TreeNode p = root;
		while (p != null || !stack.isEmpty()) {
			while (p != null) {
				stack.push(p);
				p = p.left;
			}
			
			p = stack.pop();
			if (--k == 0)
				break;
			
			p = p.right;
		}
		return p.val;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63783/Two-Easiest-In-Order-Traverse-(Java)
	 * 
	 * In order traverse for BST gives the natural order of numbers.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63783/Two-Easiest-In-Order-Traverse-(Java)/65435
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63660/3-ways-implemented-in-JAVA-(Python)%3A-Binary-Search-in-order-iterative-and-recursive
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63660/3-ways-implemented-in-JAVA-(Python):-Binary-Search-in-order-iterative-and-recursive/65230
	 */
	int count_k_stop = 0;
	int result_k_stop = Integer.MIN_VALUE;

	public int kthSmallest_k_stop(TreeNode root, int k) {
		traverse(root, k);
		return result_k_stop;
	}

	public void traverse(TreeNode root, int k) {
		if (root == null || count_k_stop >= k)
			return;
		
		traverse(root.left, k);
		
		count_k_stop++;
		if (count_k_stop == k) {
			result_k_stop = root.val;
		}
		
		traverse(root.right, k);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63772/O(k)-Java-solution
	 * 
	 * inorder traverse the tree and it stops when it finds the Kth node. 
	 * 
	 * Goes to the left bottom node first and start from there to search for the Kth 
	 * smallest.
	 * 
	 * The time complexity is O(log(n) + K) only if the tree is balanced. 
	 * In the worst-case scenario the tree can have a height of n, thus the 
	 * time complexity could be O(n)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63772/O(k)-Java-solution/65420
	 */
	public int kthSmallest_recursive_stop(TreeNode root, int k) {
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		inorderSearch(root, buffer, k);
		return buffer.get(k - 1);
	}

	public void inorderSearch(TreeNode node, ArrayList<Integer> buffer, int k) {
		if (node == null || buffer.size() >= k)
			return;
		
		inorderSearch(node.left, buffer, k);
		buffer.add(node.val);
		inorderSearch(node.right, buffer, k);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63790/Easy-to-understand-answer-the-follow-up-question
	 * 
	 * Using maxHeap to store the smallest k elements only. Therefore, you can have 
	 * constant time look up. Since the BST adjust a lot, if there is an element go to 
	 * this BST but smaller than the maxHeap then you pop out the maxHeap element and 
	 * insert the element. This way, even we constantly insert / delete elements, the 
	 * look up time would be O(1) not O(N), but insert / delete time would O(logN). 
	 * However, insert and delete into BST already took O(logN).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63790/Easy-to-understand-answer-the-follow-up-question/256120
	 */
	public int kthSmallest_pq(TreeNode root, int k) {
		PriorityQueue<Integer> queue = 
				new PriorityQueue<>(k, Collections.reverseOrder());

		kthHelper_pq(queue, root, k);
		return queue.peek();
	}

	private void kthHelper_pq(PriorityQueue<Integer> queue, TreeNode root, int k) {
		if (root == null || queue.size() >= k)
			return;

		kthHelper_pq(queue, root.left, k);
		if (queue.size() >= k)
			return;

		queue.add(root.val);

		kthHelper_pq(queue, root.right, k);
	}

	// Following two functions are DFS recursion.
	private int kthSmallest_DFS_recursive(TreeNode root, int k) {
		if (root == null) {
			return -1;
		}
		ArrayList<Integer> nodeValue = new ArrayList<Integer>();
		kthSmallest_recursive(root, nodeValue);
		return nodeValue.get(k-1);
	}
	
	private void kthSmallest_recursive(TreeNode root, ArrayList<Integer> node) {
		if (root.left != null) {
			kthSmallest_recursive(root.left, node);
		}
		node.add(root.val);
		if (root.right != null) {			
			kthSmallest_recursive(root.right, node);
		}
	} 
	
	// DFS iteration
	private int kthSmallest_DFS_iteration(TreeNode root, int k) {
		if (root == null) {
			return -1;
		}
		LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
		TreeNode cur = root;
		int count = 0;
		while (cur != null || !stack.isEmpty()) {
			while (cur != null) {
				stack.add(cur);
				cur = cur.left;
			}
			cur = stack.removeLast();
			count++;
			if (count == k) {
				return cur.val;
			}
			cur = cur.right;
		}

		return -1;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63660/3-ways-implemented-in-java-python-binary-search-in-order-iterative-recursive
	 * 
	 * Binary search
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63743/Java-divide-and-conquer-solution-considering-augmenting-tree-structure-for-the-follow-up
	 * 
	 * Other code:
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63896/Simple-and-Clean-Java-solution-with-explanation
	 */
	private int kthSmallest_binarySearch(TreeNode root, int k) {
        int count = countNodes(root.left);
        if (k <= count) {
            return kthSmallest_binarySearch(root.left, k);
        } 
        else if (k > count + 1) {
        	// 1 is counted as current node
			return kthSmallest_binarySearch(root.right, k - 1 - count);
        }

        // k == count + 1
		return root.val;
	}

	private int countNodes(TreeNode n) {
		if (n == null)
			return 0;
		return 1 + countNodes(n.left) + countNodes(n.right);
	}
	
	/*
	 * For follow up:
	 * BST is modified (insert/delete) often and need to find the kth smallest 
	 * frequently? How would you optimize the kthSmallest routine?
	 * 
	 * https://leetcode.com/articles/kth-smallest-element-in-a-bst/
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63660/3-ways-implemented-in-JAVA-(Python):-Binary-Search-in-order-iterative-and-recursive/65276
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63679/Any-thoughts-on-follow-up/454285
	 */
	
	/*
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63659/What-if-you-could-modify-the-BST-node's-structure/308939
	 * 
	 * For follow up question: insert/delete many times and we can modify tree
	 * 
	 * If we could add a count field in the BST node class, it will take O(n) time 
	 * when we calculate the count value for the whole tree, but after that, it will 
	 * take O(h) time when insert/delete a node or calculate the kth smallest 
	 * element.
	 * 
	 * you just need to maintain the nodes on the path. If adding a new node, the 
	 * count of the nodes on path will +1, deleting would be -1. Therefore, the time 
	 * complexity is O(h).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63659/What-if-you-could-modify-the-BST-node's-structure/195861
	 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63659/What-if-you-could-modify-the-BST-node's-structure/65218
	 */
	class Solution_follow_up {
		class tTreeNode {
			int val;
			int count;
			tTreeNode left;
			tTreeNode right;
			
			tTreeNode(int x) {
				val = x;
				count = 1;
			}
		}
		
		tTreeNode tRoot;

		public int kthSmallest(TreeNode root, int k) {
			tRoot = buildtTree(root);
			return helper(tRoot, k);
		}

		public Integer helper(tTreeNode tRoot, int k) {
			int leftCount = (tRoot.left == null) ? 0 : tRoot.left.count;
			
			if (k == leftCount + 1)
				return tRoot.val;
			else if (k > leftCount + 1)
				return helper(tRoot.right, k - leftCount - 1);
			else
				return helper(tRoot.left, k);
		}

		public tTreeNode buildtTree(TreeNode root) {
			if (root == null)
				return null;
			
			tTreeNode tNode = new tTreeNode(root.val);
			tNode.left = buildtTree(root.left);
			tNode.right = buildtTree(root.right);
			
			tNode.count += (tNode.left == null) ? 0 : tNode.left.count;
			tNode.count += (tNode.right == null) ? 0 : tNode.right.count;
			
			return tNode;
		}
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63829/Python-Easy-Iterative-and-Recursive-Solution
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63703/Pythonic-approach-with-generator
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63673/4-Lines-in-C%2B%2B.
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63734/O(k)-space-O(n)-time-10%2B-short-lines-3-solutions
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63824/C++-solution-using-in-order-traversal/65459
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63696/Share-my-C%2B%2B-iterative-ALG.
     */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kth_Smallest_Element_in_a_BST kthMinBST = new Kth_Smallest_Element_in_a_BST();
				
		TreeNode n1 = new TreeNode(30);
		TreeNode n2 = new TreeNode(20);
		TreeNode n3 = new TreeNode(40);
		//TreeNode n4 = new TreeNode(15);
		//TreeNode n5 = new TreeNode(22);
		TreeNode n6 = new TreeNode(33);
		TreeNode n7 = new TreeNode(50);
		
		n1.left = n2;     n1.right = n3;    // n2.left = n4;     n2.right = n5;
		n3.left = n6;     n3.right = n7;
		
		int weWant = 3;
		System.out.println("Recursion kth BST : " + kthMinBST.kthSmallest_DFS_recursive(n1, weWant));
		System.out.println("Iteration kth BST : " + kthMinBST.kthSmallest_DFS_iteration(n1, weWant));
		System.out.println("Binary search kth BST : " + kthMinBST.kthSmallest_binarySearch(n1, weWant));

	}

}
