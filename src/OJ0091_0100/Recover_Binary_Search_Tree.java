package OJ0091_0100;

import definition.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;

/*
 * tools:
 * 
 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32539/Tree-Deserializer-and-Visualizer-for-Python
 * 
 * test cases:
 * 
 * [10,5,15,0,8,13,20,2,-5,6,9,12,14,18,25]
 * [146,71,-13,55,null,231,399,321,null,null,null,null,null,-33]
 */

public class Recover_Binary_Search_Tree {
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * private void in_order_traverse (TreeNode root) {
	 *     if (root == null)
	 *         return;
	 *         
	 *     traverse(root.left);
	 *     // Do some business
	 *     traverse(root.right);
	 * }
	 * 
	 * Rf :
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal
	 */
	public void recoverTree_recur_self(TreeNode root) {
        TreeNode pre = null;
        TreeNode first = null;
        TreeNode second = null;
        
        TreeNode[] wrap = { first, second, pre };
		
		// use inorder traversal to detect incorrect node
		inOrder_recur_self(root, wrap);

		int temp = wrap[0].val;
		wrap[0].val = wrap[1].val;
		wrap[1].val = temp;
	}

	public void inOrder_recur_self(TreeNode root, TreeNode[] wrap) {
		if (root == null)
			return;
		
		inOrder_recur_self(root.left, wrap);

		if (wrap[2] != null && wrap[2].val > root.val) {
			
			// incorrect smaller node is always found as prev node
			if (wrap[0] == null)
				wrap[0] = wrap[2];
			
			// incorrect larger node is always found as curr(root) node
			wrap[1] = root;
		}

		// update prev node
		wrap[2] = root;

		inOrder_recur_self(root.right, wrap);
	}
	
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32562/Share-my-solutions-and-detailed-explanation-with-recursiveiterative-in-order-traversal-and-Morris-traversal
	 * 
	 * private void in_order_traverse (TreeNode root) {
	 *     if (root == null)
	 *         return;
	 *         
	 *     traverse(root.left);
	 *     // Do some business
	 *     traverse(root.right);
	 * }
	 * 
	 * Following in-order traversal, we should have following order: 
	 * prev.val < curr.val. If not, then we found at least one incorrectly placed node
	 * 
	 * We will visit the incorrect smaller node first, and this node will be detected 
	 * when we compare its value with next.val, i.e. when it is treated as prev node. 
	 * The incorrect larger node will be detected when we compare its value with 
	 * prev.val. We don't know if it is close or not close to incorrect smaller node, 
	 * so we should continue search BST and update it if we found another incorrect 
	 * node.
	 * 
	 * Therefore if it is the first time we found an incorrect pair, the prev node 
	 * must be the first incorrect node. If it is not the first time we found an 
	 * incorrect pair, the curr node must be the second incorrect node, though we may 
	 * have corner case that two incorrect nodes are in same pair.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/917307/JAVA-DFS-%2B-comments
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/256063/Java-in-order-traversal-with-detailed-analysis-of-the-in-order-solution
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32609/4ms-Java-Solution
	 */
	public void recoverTree_recurNull(TreeNode root) {
		
		// use inorder traversal to detect incorrect node
		inOrder_recurNull(root);

		int temp = first_recurNull.val;
		first_recurNull.val = second_recurNull.val;
		second_recurNull.val = temp;
	}

	TreeNode prev_recurNull = null;
	TreeNode first_recurNull = null;
	TreeNode second_recurNull = null;

	public void inOrder_recurNull(TreeNode root) {
		if (root == null)
			return;
		
		// search left tree
		inOrder_recurNull(root.left);

		// in inorder traversal of BST, prev should always have smaller value than
		// current value
		if (prev_recurNull != null && prev_recurNull.val >= root.val) {
			
			// incorrect smaller node is always found as prev node
			if (first_recurNull == null)
				first_recurNull = prev_recurNull;
			
			// incorrect larger node is always found as curr(root) node
			second_recurNull = root;
		}

		// update prev node
		prev_recurNull = root;

		// search right tree
		inOrder_recurNull(root.right);
	}
	
	/*
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32559/Detail-Explain-about-How-Morris-Traversal-Finds-two-Incorrect-Pointer
	 * 
	 * Morris Traversal
	 * 
	 * 當 left child 為空，輸出當前節點並走到 right child。
	 * 當 left child 不為空，在當前節點的 left tree 中找到當前節點在in order traversal 的前一個節點。
	 * a. 如果前一個節點的 right child 為空，將它的 right child 設置為當前節點。
	 *    當前節點更新為當前節點的 left child。
	 * b. 如果前一個節點的 right child 為當前節點，將它的 right child 重新設為空（恢復樹的形狀）。
	 *    輸出當前節點。當前節點更新為當前節點的 right child。
	 * 
	 * left tree 最右邊的 node 只有 2 種情形，null 或是自己
	 * 
	 * Morris Traversal or Morris Threading Traversal.
	 * It take use of leaf nodes' right/left pointer to achieve O(1) space Traversal 
	 * on a Binary Tree.
	 * 
	 * Before we visiting the left tree of a root, we will build a back-edge between 
	 * rightmost node in left tree and the root. So we can go back to the root node 
	 * after we are done with the left tree. Then we locate the rightmost node in 
	 * left subtree again, cut the back-edge, recover the tree structure and start 
	 * visit right subtree.
	 * 
	 * When they are not consecutive, the first time we meet pre.val > root.val ensure 
	 * us the first node is the pre node, since root should be traversal ahead of pre, 
	 * pre should be at least at small as root. The second time we meet 
	 * pre.val > root.val ensure us the second node is the root node, since we are now 
	 * looking for a node to replace with out first node, which is found before.
	 * 
	 * When they are consecutive, which means the case pre.val > cur.val will appear 
	 * only once. The first node will still be pre, and the second will be just set to 
	 * root. Once we meet this case again, the first node will not be affected.
	 * 
	 * Rf :
	 * http://www.cnblogs.com/AnnieKim/archive/2013/06/15/morristraversal.html
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32562/Share-my-solutions-and-detailed-explanation-with-recursiveiterative-in-order-traversal-and-Morris-traversal
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32650/Share-My-java-Solution-using-Morris-Traversal
	 */
	public void recoverTree_Morris(TreeNode root) {
		TreeNode pre = null;
		TreeNode first = null, second = null;
		
		// Morris Traversal
		TreeNode temp = null;
		while (root != null) {
			if (root.left != null) {
				
				// connect threading for root
				
				// got left tree, then let's locate its rightmost node in left tree
				temp = root.left;
				
				// we may have visited the left tree before, and connect the 
				// rightmost node with curr node (root node)
				while (temp.right != null && temp.right != root)
					temp = temp.right;
				
				// There are two cases,
		        // null: first time we access current, set node.right to current and 
				//       move to left child of current;
		        // current: we accessed current before, thus we've finished 
				//          traversing left subtree, set node.right back to null;
				
				// the threading already exists
				if (temp.right != null) {
					if (pre != null && pre.val > root.val) {
						if (first == null) {
							first = pre;
							second = root;
						} 
						else {
							second = root;
						}
					}
					pre = root;

					temp.right = null;
					root = root.right;
				} 
				// construct the threading
				else {
					temp.right = root;
					root = root.left;
				}
			} 
			else {
				if (pre != null && pre.val > root.val) {
					if (first == null) {
						first = pre;
						second = root;
					} 
					else {
						second = root;
					}
				}
				pre = root;
				root = root.right;
			}
		}
		
		// swap two node values;
		if (first != null && second != null) {
			int t = first.val;
			first.val = second.val;
			second.val = t;
		}
	}

	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal
	 * 
	 * 6, 3, 4, 5, 2
	 * 
	 * private void in_order_traverse (TreeNode root) {
	 *     if (root == null)
	 *         return;
	 *         
	 *     traverse(root.left);
	 *     // Do some business
	 *     traverse(root.right);
	 * }
	 * 
	 * what we are comparing is the current node and its previous node in the 
	 * "in order traversal".
	 * 
	 * The number of times that you will find prev.val > root.val depends on whether 
	 * the two nodes that get swapped are next to each other in the sequence of 
	 * in-order traversal.
	 * 
	 * original in-order traversal: 1 2 3 4 5
	 * If 2 and 3 get swapped, it becomes 1 3 2 4 5 and there is only one time that 
	 * you will have prev.val > root.val
	 * If 2 and 4 get swapped, it becomes 1 4 3 2 5 and there are two times that you 
	 * will have prev.val > root.val
	 * 
	 * If during the first time when you find prev.val > root.val, the previous node 
	 * "prev" MUST be one of two nodes that get swapped. However, the current node 
	 * MAY OR MAY NOT be another node that gets swapped, which will depend on whether 
	 * later during in-order traversal, there is another prev.val > root.val or not. 
	 * If there is, then the current node "root" during the 2nd time of 
	 * prev.val > root.val will be the other node that gets swapped
	 * 
	 * The firstElement will only be put on value once. 
	 * The secondElement will be put on value once or twice, depends on the 
	 * 2 swapped elements are next to each other or not.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal/275044
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal/31300
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal/328304
	 * 
	 * Other code:
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/543745/Simple-and-Efficient-Java-solution-(Runs-in-1ms)-In-Order-traversal
	 */
	TreeNode firstElement_recur2 = null;
	TreeNode secondElement_recur2 = null;
	
	// The reason for this initialization is to avoid null pointer exception in the
	// first comparison when prevElement has not been initialized
	TreeNode prevElement_recur2 = new TreeNode(Integer.MIN_VALUE);

	public void recoverTree_recur2(TreeNode root) {

		// In order traversal to find the two elements
		traverse_recur2(root);

		// Swap the values of the two nodes
		int temp = firstElement_recur2.val;
		firstElement_recur2.val = secondElement_recur2.val;
		secondElement_recur2.val = temp;
	}

	private void traverse_recur2(TreeNode root) {
		if (root == null)
			return;

		traverse_recur2(root.left);

		// Start of "do some business",
		
		// If first element has not been found, assign it to prevElement 
		// (refer to 6 in the example above)
		if (firstElement_recur2 == null && prevElement_recur2.val > root.val) {
			firstElement_recur2 = prevElement_recur2;
		}

		// If first element is found, assign the second element to the root 
		// (refer to 2 in the example above)
		if (firstElement_recur2 != null && prevElement_recur2.val > root.val) {
			secondElement_recur2 = root;
		}
		
		prevElement_recur2 = root;

		// End of "do some business"

		traverse_recur2(root.right);
	}
	
	/*
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32607/Beat-99-Fast-Java-Solution-O(h)-Space-with-Explanation
	 * 
	 * Use stack to do in-order traversal. In the processing of traversal, keep 
	 * comparing the current value with the previous value. Since each previous value 
	 * should be less than the current value, once an exception is found, record the 
	 * previous node as the First Mistaken Node and the current node as Second. If one 
	 * more exceptions are found, override the current node to the Second Mistaken 
	 * Node. Because if a series of mistaken nodes are found, the only possible way 
	 * to correct them with one swap is to switch the head and tail node.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32535/No-Fancy-Algorithm-just-Simple-and-Powerful-In-Order-Traversal/31306
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32562/Share-my-solutions-and-detailed-explanation-with-recursiveiterative-in-order-traversal-and-Morris-traversal
	 */
	public void recoverTree_stack(TreeNode root) {
		TreeNode pre = null, first = null, second = null;
		
		Deque<TreeNode> stack = new LinkedList<TreeNode>();
		while (root != null) {
			stack.push(root);
			root = root.left;
		}
		
		while (!stack.isEmpty()) {
			TreeNode temp = stack.pop();
			if (pre != null) {				
				if (pre.val > temp.val) {
					if (first == null)
						first = pre;
					second = temp;
				}
			}
			
			pre = temp;
			
			if (temp.right != null) {
				temp = temp.right;
				
				while (temp != null) {
					stack.push(temp);
					temp = temp.left;
				}
			}
		}

		int temp = first.val;
		first.val = second.val;
		second.val = temp;
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32624/Python-easy-to-understand-solutions
	 * https://leetcode.com/problems/recover-binary-search-tree/discuss/32604/18ms-Java-solution-with-in-order-traversal-and-sorting-O(nlogn)-time-and-O(n)-space
	 */
	public void recoverTree_self3(TreeNode root) {
		List<TreeNode> list = new ArrayList<>();
		inOrder_self3(root, list);
		
		int[] misplace = { -1, -1 };
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).val > list.get(i + 1).val) {
				if (misplace[0] == -1) {
					misplace[0] = i;
					misplace[1] = i + 1;
				}
				else {
					misplace[1] = i + 1;
					break;
				}
			}
		}
		
		int tmp = list.get(misplace[0]).val;
		list.get(misplace[0]).val = list.get(misplace[1]).val;
		list.get(misplace[1]).val = tmp;
	}
	
	private void inOrder_self3(TreeNode root, List<TreeNode> list) {
		if (root == null) {
			return;
		}
		
		inOrder_self3(root.left, list);
		list.add(root);
		inOrder_self3(root.right, list);
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public void recoverTree_self2(TreeNode root) {
        Map<Integer, TreeNode> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        
		inOrder_self2(root, map, list);

		int[] two = { -1, -1 };

		int start = 0;
        int end = list.size() - 1;
        while (two[0] == -1 || two[1] == -1) {
            if (list.get(start) > list.get(start + 1)) {
                two[0] = start;
            }
            if (list.get(end) < list.get(end - 1)) {
                two[1] = end;
            }
            
            if (two[0] == -1) {
                start++;
            }
            if (two[1] == -1) {
                end--;
            }
        }
        
        TreeNode first = map.get(list.get(two[0]));
        TreeNode second = map.get(list.get(two[1]));
        
        int tmp = first.val;
        first.val = second.val;
        second.val = tmp;
    }
    
    private void inOrder_self2(TreeNode root, Map<Integer, TreeNode> map, 
    		List<Integer> list) {
    	
        if (root == null) {
            return;
        }
        
        inOrder_self2(root.left, map, list);
        
        int val = root.val;
        list.add(val);
        map.put(val, root);
        
        inOrder_self2(root.right, map, list);
    }
	
	/*
	 * The following 2 functions are by myself.
	 */
	public void recoverTree_self(TreeNode root) {
        Map<Integer, TreeNode> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        
		inOrder_self(root, map, list);

		int[] two = { -1, -1 };

		List<Integer> ansList = new ArrayList<>(list);
		Collections.sort(ansList);
        for (int i = 0; i < list.size(); i++) {
            if (ansList.get(i) != list.get(i)) {
                if (two[0] == -1) {
                    two[0] = i;
                }
                else {
                    two[1] = i;
                    break;
                }
            }
        }
        
        TreeNode first = map.get(list.get(two[0]));
        TreeNode second = map.get(list.get(two[1]));
        
        int tmp = first.val;
        first.val = second.val;
        second.val = tmp;
    }
    
    private void inOrder_self(TreeNode root, Map<Integer, TreeNode> map, 
    		List<Integer> list) {
    	
        if (root == null) {
            return;
        }
        
        inOrder_self(root.left, map, list);
        
        int val = root.val;
        list.add(val);
        map.put(val, root);
        
        inOrder_self(root.right, map, list);
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/32604/18ms-Java-solution-with-in-order-traversal-and-sorting-O(nlogn)-time-and-O(n)-space
     */
	public void recoverTree_list(TreeNode root) {
		// in-order traversal of treenodes, followed by sorting and reassignment of
		// values
		List<TreeNode> inorder = inorder_list(root);
		List<Integer> inorderNumbers = new ArrayList<Integer>();
		for (TreeNode node : inorder) {
			inorderNumbers.add(node.val);
		}
		
		inorderNumbers.sort(null);
		
		for (int i = 0; i < inorder.size(); i++) {
			inorder.get(i).val = inorderNumbers.get(i);
		}
	}

	private List<TreeNode> inorder_list(TreeNode root) {
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (root == null) {
			return result;
		}
		
		result.addAll(inorder_list(root.left));
		result.add(root);
		result.addAll(inorder_list(root.right));
		return result;
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/256055/Python-Inorder-Traversal
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/32546/Python-solution-with-detailed-explanation
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/187407/Python-short-and-slick-solution-(108ms-beats-100)-both-stack-and-Morris-versions
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/917430/Python-O(n)O(1)-Morris-traversal-explained
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/32646/Python-Inorder-Traversal-solution-based-on-Validate-Binary-Search-Tree
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/917502/C%2B%2B-O(n)-time-O(1)-space-short-easy-code-with-explanation
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/423225/C%2B%2B-Clear-Solution-(with-Explanation)
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/32613/REAL-O(1)-Space-(No-recursionNo-stack-etc)-O(n)-Time-solution.-48ms-C%2B%2B
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/recover-binary-search-tree/discuss/32532/My-JavaScript-solution
     */

}
