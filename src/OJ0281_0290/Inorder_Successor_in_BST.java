package OJ0281_0290;

import definition.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Stack;

/*
 * 最下面有 in-order predecessor
 */

public class Inorder_Successor_in_BST {
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72700/Iterative-and-Recursive-Java-Solution-with-Detailed-Explanation
	 * 
	 * 1. If cur.val > p.val, store cur (update it if we have smaller values > p), 
	 *    then it can be two cases:
	 *    a) cur is an ancestor of p with value > p 
	 *       --> go left to find smaller values > p
	 *    b) cur is in right subtree of p (actually must be a right child of p) 
	 *       --> go left to find smaller values > p
	 * 2. If cur.val <= p.val, it can be:
	 *    a) cur == p --> go right to find values > p
	 *    b) cur is an ancestor of p with value < p --> go right to find values > p
	 *    
	 * ------------------------------------------------------------------
	 * 
	 * + root.val > p.val. In this case, root can be a possible answer, so we store 
	 *   the root node first and call it prev. However, we don't know if there is 
	 *   anymore node on root's left that is larger than p.val. So we move root to 
	 *   its left and check again.
	 * + root.val <= p.val. In this case, root cannot be p's inorder successor, 
	 *   neither can root's left child. So we only need to consider root's right 
	 *   child, thus we move root to its right and check again.
	 * 
	 * ------------------------------------------------------------------
	 * 
	 * + It is the immediately bigger node than current node. If a node has right 
	 *   subtree, it is the smallest node found in the right subtree.
	 * + If the right subtree is null, it is the last node whose left subtree you 
	 *   are under.
	 * 
	 * On equality of the value whose inorder successor we are after, it makes it go 
	 * to the right subtree. As all elements in the right subtree are larger than the 
	 * current node, from that point onward the algorithm will be drawn towards 
	 * finding the smallest value in right subtree. As we keep traversing down 
	 * smaller nodes, we modify value of successor.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72662/*Java*-5ms-short-code-with-explanations
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72656/JavaPython-solution-O(h)-time-and-O(1)-space-iterative/75105
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72656/JavaPython-solution-O(h)-time-and-O(1)-space-iterative/249361
	 */
	public TreeNode inorderSuccessor_iter(TreeNode root, TreeNode p) {
		TreeNode cur = root, prev = null;
		while (cur != null) {
			if (cur.val > p.val) {
				prev = cur;
				cur = cur.left;
			} 
			else {
				cur = cur.right;
			}
		}
		return prev;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/inorder-successor-in-bst/solution/
	 * Approach 1: Without using BST properties
	 * 
	 * 1. When the node has a right child
	 * The inorder successor in this case is the leftmost node in the tree rooted at 
	 * the right child.
	 * 
	 * 2. When the node doesn't have a right child
	 * One of the ancestors acts as the inorder successor. That ancestor can be the 
	 * immediate parent, or, it can be one of the ancestors further up the tree.
	 * 
	 * In this case, we need to perform the inorder traversal on the tree and keep 
	 * track of a previous node which is the predecessor to the current node we are 
	 * processing. If at any point the predecessor previous is equal to the node 
	 * given to us then the current node will be its inorder successor.
	 */
	private TreeNode previous_general;
    private TreeNode inorderSuccessorNode_general;
    
    public TreeNode inorderSuccessor_general(TreeNode root, TreeNode p) {
        
        // Case 1: We simply need to find the leftmost node in the subtree 
    	//         rooted at p.right.
        if (p.right != null) {
            TreeNode leftmost = p.right;
            
            while (leftmost.left != null) {
                leftmost = leftmost.left;
            }
            
            this.inorderSuccessorNode_general = leftmost;
        } 
        // Case 2: We need to perform the standard inorder traversal and 
        //         keep track of the previous node.
        else {
            this.inorderCase2_general(root, p);
        }
        
        return this.inorderSuccessorNode_general;
    }
    
    private void inorderCase2_general(TreeNode node, TreeNode p) {
        if (node == null) {
            return;
        }
        
        // Recurse on the left side
        this.inorderCase2_general(node.left, p);
        
        // Check if previous is the inorder predecessor of node
        if (this.previous_general == p && this.inorderSuccessorNode_general == null) {
            this.inorderSuccessorNode_general = node;
            return;
        }
        
        // Keeping previous up-to-date for further recursions
        this.previous_general = node;
        
        // Recurse on the right side
        this.inorderCase2_general(node.right, p);
    }
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution
	 * 
	 * That node can either be p's parent or the smallest node in p' right branch.
	 * 
	 * When the code runs into the else block, that means the current root is either 
	 * p's parent or a node in p's right branch.
	 * 
	 * If it's p's parent node, there are two scenarios: 
	 * 1. p doesn't have right child, in this case, the recursion will eventually 
	 *    return null, so p's parent is the successor; 
	 * 2. p has right child, then the recursion will return the smallest node in the 
	 *    right sub tree, and that will be the answer.
	 * 
	 * If it's p's right child, there are two scenarios: 
	 * 1. the right child has left sub tree, eventually the smallest node from the 
	 *    left sub tree will be the answer; 
	 * 2. the right child has no left sub tree, the recursion will return null, then 
	 *    the right child (root) is our answer.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * Keep moving right till you have a node that > p.val (this is because your 
	 * successor is greater than p.val). Once you've reached a point where you can't 
	 * move right (we're already at the first node greater than p.val that we've 
	 * found up till now. Let's call it n1), the successor may be in the left subtree 
	 * of n1, so we try and find it there. If it isn't there, we return this node n1 
	 * itself, else we return the successor from the left subtree.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * 1. First we go to exact path of node till end which we want to find out using 
	 *    BST property.
	 * 2. Use back track, Consideration for every node while back track
	 *    (a). For every node if we backtrack from right side then simply return 
	 *         because successor will be its parent.
	 *    (b). If we backtrack from left side, then successor will be Either current 
	 *         node OR any successor found in left subtree.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75088
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/416161
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/232276
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72700/Iterative-and-Recursive-Java-Solution-with-Detailed-Explanation
	 */
	public TreeNode inorderSuccessor_recur(TreeNode root, TreeNode p) {
		if (root == null)
			return null;

		if (root.val <= p.val) {
			return inorderSuccessor_recur(root.right, p);
		} 
		else {
			TreeNode left = inorderSuccessor_recur(root.left, p);
			return (left != null) ? left : root;
		}
	}
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/440105/More-efficient-shorter-and-commented-Java-solution-than-Leetcode's-one-(no-recursion-no-stack)
	 * 
	 * * case 1: p has a right child:
	 * note the shortcut when p has a right child: we traverse only p.root, not 
	 * root. It does not improve the asymptotic time complexity, that is O(h), 
	 * but we have less work to do. Indeed, when p has a right child, it means 
	 * the successor is in the subtree of its right child. More precisely, by 
	 * construction/definition of a Binary Search Tree, the successor is the 
	 * leftmost node in the right child tree of p. Because it is the right child, 
	 * all nodes in this tree will be greater than p. It means than 
	 * node.val > p.val will be always true and only the if-then branch will be 
	 * evaluated. The loop will be terminated when there is no more left child.
	 * This last node is the successor (remember that it is the leftmost node in 
	 * the right child tree of p).
	 * 
	 * * case 2: p has _no_ right child:
	 * In this case, by construction/definition of a Binary Search Tree, the 
	 * successor is the leftmost ancestor of p. It means that the successor is the 
	 * parent of a tree where p is the rightmost tree. This tree is the left child of 
	 * the successor.
	 * Another way to formulate is to say that the successor is the first ancestor of 
	 * the node whose left sub-tree contains the node.
	 * So in the case where p.right is null, the following code will traverse the 
	 * tree from the root to p. At a moment, it will reach the successor of p 
	 * (but we don't know which one it is until p is reached).
	 * Because by definition the successor of p is greater than p, the successor will 
	 * be visited in the if-then statement. And due to the fact that p is the 
	 * rightmost node in the ancestor left sub-tree, the if-then will be no more
	 * evaluated from the successor until p is reached. The variable successor will 
	 * be no more updated until the end.
	 * The loop will be terminated by the fact that p has no right child.
	 * 
	 */
	public TreeNode inorderSuccessor_use_p(TreeNode root, TreeNode p) {
		TreeNode successor = null;
		TreeNode node = p.right == null ? root : p.right;
		while (node != null) {
			if (node.val > p.val) {
				successor = node;
				node = node.left;
			} else {
				node = node.right;
			}
		}
		return successor;
	}
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72700/Iterative-and-Recursive-Java-Solution-with-Detailed-Explanation
	 * 
	 * 1. p has a right child, then the inorder successor is the left most leaf of 
	 *    the right child.
	 * 2. p doesn't have a right child, there are 2 cases:
	 *    a) p is the largest value in the BST and there is no inorder successor.
	 *    b) There are nodes at upper levels with values > p, and we need to find 
	 *       the smallest of those.
	 *       We can combine 2a and 2b together. Use a temporary TreeNode cur to 
	 *       traverse the tree. If cur <= p, go right to find nodes > p. 
	 *       Else if cur > p, store cur, and go left to find smaller nodes > p. 
	 *       We stop when we reach p. The last stored cur (smallest among nodes > p) 
	 *       is our answer. If cur is null, then we can't find a node > p, and there 
	 *       is no inorder successor.
	 * 
	 */
	public TreeNode inorderSuccessor_rightChild(TreeNode root, TreeNode p) {
		if (root == null)
			return null;
		
		if (p.right != null) {
			TreeNode cur = p.right;
			while (cur.left != null) {
				cur = cur.left;
			}
			return cur;
		} 
		else {
			TreeNode cur = root, prev = null;
			while (cur != p) {
				if (cur.val > p.val) {
					prev = cur;
					cur = cur.left;
				} 
				else if (cur.val < p.val) {
					cur = cur.right;
				}
			}
			return prev;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72656/JavaPython-solution-O(h)-time-and-O(1)-space-iterative/208334
	 * 
	 * The key idea is that (1) if p node has a right subtree, successor would be the 
	 * smallest note in p's right subtree (in a BST, it is just the leftmost node); 
	 * (2) if p doesn't have a right subtree, it is the last node whose left subtree 
	 * it is are under (this can be done through standard BST search and record its 
	 * last right parent).
	 */
	public TreeNode inorderSuccessor5(TreeNode root, TreeNode p) {
		if (root == null)
			return null;
		
		// case 1:
		// pre is used to save the last node whose left substree
		TreeNode pre = null;
		
		// find node p
		while (root.val != p.val) {
			// only update pre when p.val < root.val
			if (p.val < root.val) {
				pre = root;
				root = root.left;
			} 
			else if (p.val > root.val)
				root = root.right;
		}

		// at this point root.val = p.val
		if (root.right == null)
			return pre;
		else
			// case 2
			return getLeftMost5(root.right);
	}

	// find the leftmode/smallest node in a tree
	public TreeNode getLeftMost5(TreeNode root) {
		while (root.left != null) {
			root = root.left;
		}
		
		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72656/JavaPython-solution-O(h)-time-and-O(1)-space-iterative/75122
	 * 
	 * 1. first try to find it in p's right subtree;
	 * 2. if p's right subtree is empty, go upwards, go "northwest" till the end, 
	 *    then the first "northeast" node is the successor.
	 * 
	 */
	public TreeNode inorderSuccessor6(TreeNode root, TreeNode p) {
		if (root == null || p == null) {
			return null;
		}
		
		// first try to find it in p's right subtree
		if (p.right != null) {
			TreeNode q = p.right;
			
			while (q.left != null) {
				q = q.left;
			}
			
			return q;
		}
		
		// if not found, next go upwards
		TreeNode succ = dfs6(root, p);
		return succ == p ? null : succ;
	}

	private TreeNode dfs6(TreeNode node, TreeNode p) {
		if (node == null || node == p) {
			return node;
		}
		
		TreeNode left = dfs6(node.left, p);
		TreeNode right = dfs6(node.right, p);
		
		if (right == p) {
			return p;
		}
		if (left == p) {
			return node;
		}
		
		return left == null ? right : left;
	}

	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75099
	 * 
	 * inorderSuccessor(root.left, p) will only be called when root.val > p.val.
	 * So the in-order successor will either be root or in the left-subtree of root 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75074
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75093
	 */
	public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
		while (root != null && root.val <= p.val)
			root = root.right;
		
		if (root == null)
			return null;
		
		TreeNode left = inorderSuccessor2(root.left, p);
		return (left != null) ? left : root;
	}
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75099
	 * 
	 * inorderSuccessor(root.left, p) will only be called when root.val > p.val.
	 * So the in-order successor will either be root or in the left-subtree of root 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75074
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75093
	 */
	public TreeNode inorderSuccessor3(TreeNode root, TreeNode p) {
		while (root != null && root.val <= p.val)
			root = root.right;
		
		TreeNode left = root == null ? null : inorderSuccessor3(root.left, p);
		return (left != null) ? left : root;
	}
	
	// by myself
	public TreeNode inorderSuccessor_self2(TreeNode root, TreeNode p) {
        TreeNode cur = root;
        TreeNode parent = null;
        
        List<TreeNode> parents = new ArrayList<>();
        
        while (cur.val != p.val) {
            if (cur.val > p.val) {
                parent = cur;
                parents.add(parent);
                cur = cur.left;
            }
            else if (cur.val < p.val) {
                parent = cur;
                parents.add(parent);
                cur = cur.right;
            }
        }
        
        if (cur.right == null) {
            if (!parents.isEmpty()) {
                for (int i = parents.size() - 1; i >= 0; i--) {
                    if (parents.get(i).val > p.val) {
                        return parents.get(i);
                    }
                }
                return null;
            }
            else {
                return null;
            }
        }
        
        cur = cur.right;
        while (cur.left != null) {
            cur = cur.left;
        }
        
        return cur;
    }
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72741/C++-iterative/270005
	 * 
	 * The judgment won't depend on whether the structure of p. Instead, it only need 
	 * the value of p, and it is searching for the successor of p through the process 
	 * of doing in-order traversal of the entire binary search tree
	 * 
	 * Other code:
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72662/*Java*-5ms-short-code-with-explanations/570839
	 */
	public TreeNode inorderSuccessor_stack_inOrder(TreeNode root, TreeNode p) {
		if (root == null || p == null)
			return null;

		Deque<TreeNode> stack = new ArrayDeque<>();
		TreeNode curr = root;

		boolean hasFound = false;

		while (curr != null || !stack.isEmpty()) {
			while (curr != null) {
				stack.addFirst(curr);
				curr = curr.left;
			}

			curr = stack.removeFirst();
			if (curr.val == p.val)
				hasFound = true;
			else if (hasFound)
				return curr;

			curr = curr.right;
		}

		return null;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/440105/More-efficient-shorter-and-commented-Java-solution-than-Leetcode's-one-(no-recursion-no-stack)
	 */
	public TreeNode inorderSuccessor_ugly(TreeNode root, TreeNode p) {
        TreeNode successor;
        
        // the successor is the first ancestor of the node whose left sub-tree 
        // contains the node.
        if (p.right == null) {
            Stack<TreeNode> stack = new Stack<>();
            gotoTreeNode_ugly(stack, root, p);
            
            successor = ascendToNextLeftmostAncestor_ugly(stack);
        } 
        // The successor of the node is the left-most node in its right sub-tree
        else {
            successor = descendToLeftmostNode_ugly(p.right);
        }
        
        return successor;
    }
    
    private void gotoTreeNode_ugly(Stack<TreeNode> stack, TreeNode root, TreeNode p) {
        boolean found = false;
        while (!found && root != null) {
            stack.push(root);
            
            if (root.val == p.val) {
                found = true;
            } 
            else if (p.val < root.val) {
                root = root.left;
            } 
            else {
                root = root.right;
            }
        }
    }

    // No need of a stack in the caller context because we never have to 
    // backtrack/ascend after this call
    //
    // @param root must be non null
    private TreeNode descendToLeftmostNode_ugly(TreeNode root) {
        TreeNode previousNode;
        do {
            previousNode = root;
            root = root.left;
        } while (root != null);
        
        return previousNode;
    }
    
    // This method take us to the next leftmost ancestor regarding the head of the 
    // stack.
    // 
    // Return null if there is no next leftmost ancestor
    private TreeNode ascendToNextLeftmostAncestor_ugly(Stack<TreeNode> stack) {
        TreeNode node;
        do {
            node = stack.pop();
        } while (!stack.empty() && node == stack.peek().right);
        
        return stack.empty() ? null : stack.peek();
    }
    
    /*
     * The following variable and 2 functions are from this link.
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution/75083
     */
	TreeNode lastLeftParent4;

	public TreeNode inorderSuccessor4(TreeNode root, TreeNode p) {
		lastLeftParent4 = null;
		successor4(root, p);
		return lastLeftParent4;
	}

	public void successor4(TreeNode root, TreeNode p) {
		if (root != null) {
			if (p.val >= root.val)
				successor4(root.right, p);
			else {
				lastLeftParent4 = root;
				successor4(root.left, p);
			}
		}
	}

	/*
	 * The following 2 functions are by myself.
	 */
	public TreeNode inorderSuccessor_self(TreeNode root, TreeNode p) {
        List<Integer> list = new ArrayList<>();
        Map<Integer, TreeNode> map = new HashMap<>();
        
        dfs_self(root, list, map);
        
        int pos = Collections.binarySearch(list, p.val + 1);
        if (pos < 0) {
            pos = -(pos + 1);
        }
        
        if (pos < list.size()) {
            return map.get(list.get(pos));
        }
        else {
            return null;
        }
    }
    
    private void dfs_self(TreeNode root, List<Integer> list, 
    		Map<Integer, TreeNode> map) {
    	
        if (root == null) {
            return;
        }
        
        dfs_self(root.left, list, map);
        
        list.add(root.val);
        map.put(root.val, root);
        
        dfs_self(root.right, list, map);
    }
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/165576/Python-solution
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/158315/Python-or-DFS-tm
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72721/10-(and-4)-lines-O(h)-JavaC%2B%2B
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72671/C%2B%2B-O(h)-solution-in-one-pass
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72741/C%2B%2B-iterative
     */
    
    /*
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72662/*Java*-5ms-short-code-with-explanations/75125
     * 
     * + If the left subtree exist, predecessor is the largest node found in left 
     *   subtree.
     * + If the left subtree does not exist, it is the last node whose right subtree 
     *   the node is under.
     * 
     * On equality of the value whose inorder predecessor we are after, it makes 
     * traversal go to the left subtree. As all elements in left subtree are smaller 
     * than the node's value, algorithm will be drawn towards the largest value in 
     * that subtree. As we keep going right, we update the predecessor.
     * 
     * Rf :
     * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72656/JavaPython-solution-O(h)-time-and-O(1)-space-iterative/75105
     */
	public TreeNode inorderPredecessor(TreeNode root, TreeNode p) {
		TreeNode pre = null;
		while (root != null) {
			if (root.val < p.val) {
				pre = root;
				root = root.right;
			} 
			else
				root = root.left;
		}
		return pre;
	}
	
	/*
	 * https://leetcode.com/problems/inorder-successor-in-bst/discuss/72653/Share-my-Java-recursive-solution
	 */
	public TreeNode predecessor(TreeNode root, TreeNode p) {
		if (root == null)
			return null;

		if (root.val >= p.val) {
			return predecessor(root.left, p);
		} 
		else {
			TreeNode right = predecessor(root.right, p);
			return (right != null) ? right : root;
		}
	}

}
