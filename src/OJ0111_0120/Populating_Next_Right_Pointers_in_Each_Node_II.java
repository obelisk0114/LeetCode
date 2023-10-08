package OJ0111_0120;

import java.util.Deque;
import java.util.ArrayDeque;

public class Populating_Next_Right_Pointers_in_Each_Node_II {
	/*
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37828/O(1)-space-O(n)-complexity-Iterative-Solution
	 * 
	 * based on level order traversal
	 */
	public Node connect_2_while(Node root) {
		Node head = null; // head of the next level
		Node prev = null; // the leading node on the next level
		Node cur = root;  // current node of current level

		while (cur != null) {
			
			// iterate on the current level
			while (cur != null) {
				
				// left child
				if (cur.left != null) {
					if (prev != null) {
						prev.next = cur.left;
					} 
					else {
						head = cur.left;
					}
					
					prev = cur.left;
				}
				
				// right child
				if (cur.right != null) {
					if (prev != null) {
						prev.next = cur.right;
					} 
					else {
						head = cur.right;
					}
					
					prev = cur.right;
				}
				
				// move to next node
				cur = cur.next;
			}

			// move to next level
			cur = head;
			head = null;
			prev = null;
		}

		return root;
	}
	
	/*
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space/35836
	 * 
	 * level-order traversal
	 * 
	 * tempChild is dummy head of root's next level. 
	 * So root = tempChild.next moves to loop next level's nodes.
	 * 
	 * when "Node currentChild = tempChild;" the currentChild is the address of the 
	 * object, so every change to currentChild is the change to tempChild. So in the 
	 * following two if(){}..., because of "currentChild.next = root.right;" or 
	 * "currentChild.next = root.right;" the tempChild.next is change into the first 
	 * node of this level. However, because of "currentChild = currentChild.next;" 
	 * the currentChild change to the address of another object. So the tempChild will 
	 * not change with it anymore and it will stay on the first node of this level.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37828/O(1)-space-O(n)-complexity-Iterative-Solution/35875
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space/35829
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space/35833
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space/177585
	 * 
	 * Other code:
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37811/Simple-solution-using-constant-space/165405
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37979/O(1).-Concise.-Fast.-What's-so-hard
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37967/Easy-understand-and-precise-Java-code
	 */
	public Node connect_dummy_2_while(Node root) {
		Node cur = root;
		
		// tempChild is sentinel, keeps track of start node of next level
		Node tempChild = new Node(0);
		
		// this loop is for different levels
		while (cur != null) {
			
			// currentChild is sewing next fields in current level
            // first time in a level, it is same as tempChild (with null next)
            // but as soon as we get a non null child from node
            // currentChild threads that child into its next, 
            //      --------->>  thus making that child as next tempChild
			Node currentChild = tempChild;
			
			// this loop moves node in current level
			while (cur != null) {
				if (cur.left != null) {
					currentChild.next = cur.left;
					currentChild = currentChild.next;
				}
				
				if (cur.right != null) {
					currentChild.next = cur.right;
					currentChild = currentChild.next;
				}
				
				// move cur to next in same level, end up null at rightmost
				cur = cur.next;
			}
			
			// current level ended in cur being null
            // take node from sentinel's next, which is next levels start
			cur = tempChild.next;
			
			// we must add this to reset
			// Consider the last row in the tree, 
			// if not set to null, code will loop on that row forever.
			tempChild.next = null;
		}
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37955/*Java*-Iterative-solution-with-explanations
	 * 
	 * Suppose we have already constructed the next pointer for the current layer 
	 * (call it L1), and we need to construct for the layer below (call it L2):

                 1 -> NULL
               /  \
              2 -> 3 -> NULL  // current layer, L1
             / \    \  
            4   5    7       // layer below, L2
	 * 
	 * First, it is obvious that we need to track the `first` node of L2.
	 * Second, for every node, it can have either 0, 1, or 2 children.
	 * + 0 children, we don't need anything
	 * + 1 child, we connect the child with a node to its left, calling it a `prev` 
	 *   node (when it is the `first` node, we don't have a `prev` node). The `prev` 
	 *   node needs to be moved to the child afterwards
	 * + 2 children, we first need to connect `prev` to the left child, and connect 
	 *   left with the right. Then `prev` node needs to be moved to the right child 
	 * 
	 * we can achieve it in two if blocks:
	 * + if has left child: connect prev with left child and move prev to left child
	 * + if has right child: connect prev with right child and move prev to right 
	 *   child (This is not an else if!)
	 * 
	 */
	public Node connect_1_while(Node root) {
		Node first = null, prev = null, child = null, current = root;
		while (current != null) {
			if ((child = current.left) != null) {
				if (first == null)
					first = child;
				else
					prev.next = child;
				
				prev = child;
			}
			
			if ((child = current.right) != null) {
				if (first == null)
					first = child;
				else
					prev.next = child;
				
				prev = child;
			}

			if (current.next != null) {
				current = current.next;
			} 
			else {
				current = first;
				first = null;
			}
		}
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37813/Java-solution-with-constant-space/612733
	 * 
	 * pre=dummyHead; means you first 'pre.next=root.left' in every level is same as 
	 * 'dummyHead.next=root.left', so dummyHead save the first points
	 * 
	 * Rf :
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37813/Java-solution-with-constant-space/138495
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37813/Java-solution-with-constant-space/223201
	 */
	public Node connect_dummy_1_while(Node root) {
		// this head will always point to the first element in the current layer 
		// we are searching
		Node dummyHead = new Node(0);
		
		// this 'pre' will be the "current node" that builds every single layer
		Node pre = dummyHead;
		
		// just for return statement
		Node real_root = root;

		while (root != null) {
			if (root.left != null) {
				pre.next = root.left;
				pre = pre.next;
			}
			
			if (root.right != null) {
				pre.next = root.right;
				pre = pre.next;
			}
			
			root = root.next;
			
			// reach the end of current layer
			if (root == null) {
				
				// shift pre back to the beginning, 
				// get ready to point to the first element in next layer
				pre = dummyHead;
				
				// root comes down one level below to the first available 
				// non null node
				root = dummyHead.next;
				
				// reset dummyhead back to default null
				// If you get rid of this line, you will get a TLE because there is 
				// a loop in the bottom level.
				// root will be assigned to the original dummy.next (left most node)
				dummyHead.next = null;
			}
		}
		return real_root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37877/O(1)-space-O(n)-time-Java-solution/35917
	 * 
	 * The inner while loop has already taken care of all the nodes at the same depth 
	 * (all the nodes at the right side of root).
	 * 
	 * We only need to find the left most node at each depth, and run the while loop 
	 * once for that depth, all the other operations are just doing the same thing.
	 */
	public Node connect_leftMost(Node root) {
		if (root == null)
			return null;

		Node leftMost = root;
		while (leftMost != null) {
			Node cur = leftMost;
			leftMost = null;

			while (cur != null) {
				if (cur.left != null) {
					cur.left.next = cur.right == null ? 
							getNext_leftMost(cur) : cur.right;
					
					if (leftMost == null)
						leftMost = cur.left;
				}
				
				if (cur.right != null) {
					cur.right.next = getNext_leftMost(cur);
					
					if (leftMost == null)
						leftMost = cur.right;
				}
				
				cur = cur.next;
			}
		}

		return root;
	}

	private Node getNext_leftMost(Node root) {
		Node cur = root.next;
		
		while (cur != null) {
			if (cur.left != null)
				return cur.left;
			if (cur.right != null)
				return cur.right;
			
			cur = cur.next;
		}
		
		return null;
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37877/O(1)-space-O(n)-time-Java-solution
	 * 
	 * 先用 while 把這一層的 next 找完
	 * 再 connect(root.left); connect(root.right)
	 */
	public Node connect_DFS(Node root) {
		if (root == null)
			return null;

		Node cur = root;
		while (cur != null) {
			if (cur.left != null) {
				cur.left.next = (cur.right != null) ? cur.right : getNext_DFS(cur);
			}

			if (cur.right != null) {
				cur.right.next = getNext_DFS(cur);
			}

			cur = cur.next;
		}

		connect_DFS(root.left);
		connect_DFS(root.right);
		return root;
	}
    
	private Node getNext_DFS(Node root) {
		Node temp = root.next;

		while (temp != null) {
			if (temp.left != null)
				return temp.left;
			if (temp.right != null)
				return temp.right;

			temp = temp.next;
		}

		return null;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/472675/Short-Java-solution-with-explanation-100-runtime-and-100-memory
	 * 
	 * Right sub tree should be fully connected before connecting your left tree so 
	 * that you can use next pointers on all the nodes at any level. if your right 
	 * subtree is not connected, findNext() call will not be able to connect to nodes 
	 * of right subtree.
	 * 
	 * Since we need to find "next" left most, we should call right first to make sure 
	 * the nexts(on the right side) are already well connected, otherwise there would 
	 * be gap in calling findNext. For example this is what would happen for this 
	 * input if we call left first:
	 * 
	 *             2
                  /  \
                 1 -> 3
                / \  / \
               0->7->9 1
              /  / \  / \
            2-> 1->"0"8  8
	 * 
	 * When the recursion comes to the "0" in the lowest level, the next step is to 
	 * connect 0 with 8. However since 9 and 1 on the upper level haven't been 
	 * connected yet (because we execute left part before we do right), findNext is 
	 * not able to reach the left most child of 1, due to the gap between 9 and 1.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37877/O(1)-space-O(n)-time-Java-solution/313438
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/172861/Mostly-recursive-solution-O(n)-time-(beats-99.32)-and-O(1)-space-(without-considering-stack)/260137
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/172861/Mostly-recursive-solution-O(n)-time-(beats-99.32)-and-O(1)-space-(without-considering-stack)/219440
	 */
	public Node connect_DFS2(Node root) {
		if (root == null)
			return null;

		// update left next
		if (root.left != null) {
			
			// if right child exists - simple connect left.next to right
			if (root.right != null)
				root.left.next = root.right;
			
			// if not - scan parent next node until we find the first left 
			//          or right child
			else
				root.left.next = findNext_DFS2(root);
		}
		
		// update right next
		if (root.right != null) {
			root.right.next = findNext_DFS2(root);
		}

		// update the right nodes first
		connect_DFS2(root.right);
		connect_DFS2(root.left);
		return root;
	}
    
	// get parent node
	private Node findNext_DFS2(Node root) {
		// scan all next parent nodes until we find the first left or right child
		while (root.next != null) {
			root = root.next;
			
			if (root.left != null)
				return root.left;
			if (root.right != null)
				return root.right;
		}
		return null;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/669795/java-100.-neat-solution.
	 * 
	 * Right sub tree should be fully connected before connecting your left tree so 
	 * that you can use next pointers on all the nodes at any level. if your right 
	 * subtree is not connected, findNext() call will not be able to connect to nodes 
	 * of right subtree.
	 * 
	 *        2
             /  \
            1     3
           / \   /  \
          0   4  5   6
         /   / \    / \ 
        9   10  11 8  12
                /
               7
	 * 
	 * If we connect the left first and then the right. We will have a connection 
	 * sequence as following: 1->3, 0->4, 4->5, 9->10, 10->11. Now, we want to connect 
	 * the 11 and 8. However, I will need the 5's next, which is 6 to do that. But, we 
	 * haven't connect the 5 and 6 yet. Then the program will make 11's next as null. 
	 * Then it will keep connecting 7->null, 5->6 and 8->12. But the 11 and 8 will not 
	 * be connected. To avoid this, we connect everything from right to left to avoid 
	 * the situation such that there's some nodes's next is null where should not be.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37877/O(1)-space-O(n)-time-Java-solution/313438
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/669795/java-100.-neat-solution./782823
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/669795/java-100.-neat-solution./762553
	 */
	public Node connect_DFS3(Node root) {
		if (null == root) {
			return null;
		}

		if (root.left != null && root.right != null) {
			root.left.next = root.right;
			root.right.next = findNext_DFS3(root.next);
		} 
		else if (root.left != null) {
			root.left.next = findNext_DFS3(root.next);
		} 
		else if (root.right != null) {
			root.right.next = findNext_DFS3(root.next);
		}

		/** right first */
		connect_DFS3(root.right);
		connect_DFS3(root.left);
		return root;
	}

	private Node findNext_DFS3(Node root) {
		if (null == root) {
			return null;
		} 
		else if (null != root.left) {
			return root.left;
		} 
		else if (null != root.right) {
			return root.right;
		}
		return findNext_DFS3(root.next);
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/961938/JAVA-BFS-SOLUTION-oror-1ms
	 */
	public Node connect_self(Node root) {
        if (root == null) {
            return root;
        }
        
        Deque<Node> queue = new ArrayDeque<>();
        queue.offerLast(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node cur = queue.pollFirst();
                
                if (i < size - 1) {
                    cur.next = queue.peekFirst();
                }
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
        }
        
        return root;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/961868/Python-O(n)-solution-explained
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37824/AC-Python-O(1)-space-solution-12-lines-and-easy-to-understand
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/38035/Just-convert-common-BFS-solution-to-O(1)-space-a-simple-python-code
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/389389/Simply-Simple-Python-Solutions-Level-order-traversal-and-O(1)-space-both-approach
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/521193/Python-O(1)-aux-space-DFS-sol.-85%2B-with-Diagram
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/962414/C%2B%2B-Recursion-and-Iteration-with-comments-and-figure-illustration
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37947/Very-simple-iterative-and-recursive-solutions-yet-still-accepted-as-best-in-cpp
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/962162/C%2B%2B-Iterative-Constant-Space-Solution-Explained-~100-Time-~75-Space
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/38015/Simple-40ms-c%2B%2B-o(n)-o(1)-solution-with-only-one-while-loop
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/172861/Mostly-recursive-solution-O(n)-time-(beats-99.32)-and-O(1)-space-(without-considering-stack)
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/37859/Share-my-concise-dfs-solution
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/849887/JavaScript-Solution-Iterative-Approach
	 */

}

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
}
