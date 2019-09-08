package OJ0421_0430;

import java.util.LinkedList;
import java.util.Stack;

public class Flatten_a_Multilevel_Doubly_Linked_List {
	// https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/155576/Java-solution-using-stack.-readable
	public Node flatten_stack(Node head) {
		Stack<Node> stack = new Stack<>();
		Node travel = head;
		while (travel != null) {
			if (travel.child != null) {
				if (travel.next != null)
					stack.push(travel.next);
				
				travel.next = travel.child;
				travel.next.prev = travel;
				travel.child = null;
			} 
			else {
				if (travel.next == null && !stack.isEmpty()) {
					travel.next = stack.pop();
					travel.next.prev = travel;
				}
			}
			travel = travel.next;
		}
		return head;
	}
	
	// https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/210375/Simple-Java-Pre-Order-Solution
	public Node flatten_stack_pop_next(Node h) {
		if (h == null)
			return h;

		Stack<Node> st = new Stack<>();
		Node prev = null;
		st.push(h);
		while (!st.isEmpty()) {
			Node cur = st.pop();
			if (prev != null) {
				prev.next = cur;
				cur.prev = prev;
				prev.child = null;
			}
			
			if (cur.next != null)
				st.push(cur.next);
			if (cur.child != null)
				st.push(cur.child);
			
			prev = cur;
		}
		return h;
	}
	
	// by myself
	public Node flatten_self(Node head) {
        if (head == null || (head.next == null && head.child == null))
            return head;
        
        Node cur = head;
        LinkedList<Node> stack = new LinkedList<>();
        while (cur != null) {
            if (cur.child != null) {
                stack.offerLast(cur.next);
                cur.next = cur.child;
                cur.next.prev = cur;
                cur.child = null;
                cur = cur.next;
            }
            else {
                if (cur.next != null) {
                    cur.next.prev = cur;
                    cur = cur.next;
                }
                else {
                    cur.next = stack.pollLast();
                    if (cur.next != null) {
                        cur.next.prev = cur;
                    }
                    
                    cur = cur.next;
                }
            }
        }
        return head;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/159746/Java-1ms-Recursion-beats-100-with-explaination
	 * 
	 * 1. Node cur.child == null, skip, don't go into recursion.
	 * 2. Node cur.child != null, recurse and get child's tail node. 
	 *    Node tmp = cur.next, (1) connect cur <-> child, 
	 *    (2) connect child's tail <->tmp.
	 * 
	 * The helper_tail function returns the tail node of current level.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/240914/JAVA-Recursion-with-1-pass-1ms-faster-than-100.
	 */
	public Node flatten_tail(Node head) {
		helper_tail(head);
		return head;
	}

	private Node helper_tail(Node head) {
		Node cur = head, pre = head;
		while (cur != null) {
			if (cur.child == null) {
				pre = cur;
				cur = cur.next;
			} 
			else {
				Node tmp = cur.next;
				Node child = helper_tail(cur.child);
				
				cur.next = cur.child;
				cur.child.prev = cur;
				cur.child = null;
				child.next = tmp;
				if (tmp != null)
					tmp.prev = child;
				
				pre = child;
				cur = tmp;
			}
		}
		return pre;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/157606/Java-Recursive-Solution
	 * 
	 * flattentail: flatten the node "head" and return the tail in its child 
	 *              (if exists)
	 * the tail means the last node after flattening "head"
	 * 
	 * Five situations:
	 * 1. null - no need to flatten, just return it
	 * 2. no child, no next - no need to flatten, it is the last element, return it
	 * 3. no child, next - no need to flatten, go next
	 * 4. child, no next - flatten the child and done
	 * 5. child, next - flatten the child, connect it with the next, go next
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/157606/Java-Recursive-Solution/179450
	 */
	public Node flatten_tail2(Node head) {
		flattentail(head);
		return head;
	}

	private Node flattentail(Node head) {
		if (head == null)                    // CASE 1
			return head;
		
		if (head.child == null) {
			if (head.next == null)
				return head;                 // CASE 2
			
			return flattentail(head.next);   // CASE 3
		} 
		else {
			Node child = head.child;
			head.child = null;
			Node next = head.next;
			Node childtail = flattentail(child);
			head.next = child;
			child.prev = head;
			if (next != null) {              // CASE 5
				childtail.next = next;
				next.prev = childtail;
				return flattentail(next);
			}
			
			return childtail;                // CASE 4
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/345124/Simple-and-concise-java-solution-beats-100-using-stack
	 * 
	 * Use stack to store next nodes list and iterate child list, once child's list 
	 * is iterated check stack and continue iterating next nodes.
	 */
	public Node flatten_stack_append_next(Node head) {
		if (head == null)
			return null;
		
		Stack<Node> stack = new Stack<Node>();    // stack to hold the next nodes list
		Node temp = head;
		
		// When first node doesn't have next node but does have child node
		while (temp.next != null || temp.child != null) {
			if (temp.child != null) {
				// do not push if next nodes list is empty
				if (temp.next != null)
					stack.push(temp.next);
				
				temp.next = temp.child;
				temp.child = null;         // do not forget this
				temp.next.prev = temp;     // make child's previous parent
			}
			
			temp = temp.next;              // move to next node
			
			// if nodes next is null and there are next nodes list to visit
			// if temp.child != null, temp.next will be pushed into stack in next run
			if (temp.next == null && stack.size() != 0) {
				temp.next = stack.pop();
				temp.next.prev = temp;
			}
		}
		return head;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/313261/Java-15-lines-solution-with-one-'if'-statement-with-explanation-Beat-100
	 * 
	 * Idea is like "swap" next and child for each node. 
	 * Step 1. for each node : set next to the node child if there is a child.
	 * Setp 2. find the previous child tail.
	 * Step 3. set child.next = previous next.
	 * 
	 * case 0. p null. break while. end.
	 * case 1. no next. no child.
	 * case 2. no child: p = p.next.
	 * case 3. no next. p.next = child. p.child = null.
	 * case 4. contains child + next.
	 */
	public Node flatten_process_one_and_link(Node head) {
		Node p = head;
		while (p != null) {
			if (p.child != null)
				combine(p);
			
			p = p.next;
		}
		return head;
	}

	public void combine(Node p) {
		Node child = p.child;    // step 1.
		Node next = p.next;
		p.child = null;
		p.next = child;
		child.prev = p;

		Node tail = child;       // step 2.
		while (tail.next != null)
			tail = tail.next;

		tail.next = next;        // step 3.
		if (next != null) {
			next.prev = tail;
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/150321/Easy-Understanding-Java-beat-95.7-with-Explanation
	 * 
	 * 1. Start form the head, move one step each time to the next node
	 * 2. When meet with a node with child, say node p, follow its child chain to the 
	 *    end and connect the tail node with p.next, by doing this we merged the 
	 *    child chain back to the main thread
	 * 3. Return to p and proceed until find next node with child.
	 * 4. Repeat until reach null
	 * 
	 * This is more like a top down flattening, when encounter a node with child node, 
	 * we directly flatten the current node, then move to the next node.
	 * The recursive method is more like bottom up flattening, means when we encounter 
	 * a node with child node, we flatten the child node first, then flatten the 
	 * current node.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/150321/Easy-Understanding-Java-beat-95.7-with-Explanation/179451
	 */
	public Node flatten_process_one_and_link_iterative(Node head) {
		if (head == null)
			return head;
		
		// Pointer
		Node p = head;
		while (p != null) {
			/* CASE 1: if no child, proceed */
			if (p.child == null) {
				p = p.next;
				continue;
			}
			
			/* CASE 2: got child, find the tail of the child and link it to p.next */
			
			Node temp = p.child;
			
			// Find the tail of the child
			while (temp.next != null)
				temp = temp.next;
			
			// Connect tail with p.next, if it is not null
			temp.next = p.next;
			if (p.next != null)
				p.next.prev = temp;
			
			// Connect p with p.child, and remove p.child
			p.next = p.child;
			p.child.prev = p;
			p.child = null;
		}
		return head;
	}

	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/137649/Simple-Java-Solution
	 * 
	 * Whenever you find a node with a child, call flatten function recursively on 
	 * that child node.
	 * Maintain a class variable which will keep last visited node which will act as 
	 * a tail pointer.
	 * 
	 * Rf : https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/252772/Easily-understable-Java-Solution-Runtime-1ms-beat-100-Recursive
	 */
	Node prev_global_tail;
    public Node flatten_global_tail(Node head) {
        Node cur = head;
        while (cur != null) {
          if (cur.child != null) {
            Node next = cur.next;
            cur.child.prev = cur;
            cur.next = flatten_global_tail(cur.child);
            cur.child = null;
						
            if (next != null) {
              prev_global_tail.next = next;
              next.prev = prev_global_tail;
            }
          }

          prev_global_tail = cur;
          cur = cur.next;
        }

        return head;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/152996/composite-recursion-method
     * 
     * 114. Flatten Binary Tree to Linked List
     * Using head and tail as parameters in recursive method.
     */
	public Node flatten_head_tail(Node head) {
		return flatten_head_tail(head, null);
	}

	private Node flatten_head_tail(Node head, Node tail) {
		if (head == null)
			return tail;
		
		if (head.child == null) { // no child, just convert head.next
			Node next = flatten_head_tail(head.next, tail);
			head.next = next;
			if (next != null)
				next.prev = head;
		} 
		else {        // child is flattened and inserted between head and head.next
			Node child = flatten_head_tail( head.child, 
					flatten_head_tail(head.next, tail) );
			head.next = child;
			if (child != null)    // Always true, can be omitted
				child.prev = head;
			
			head.child = null;
		}
		return head;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/266357/Java-Recursive-Solution-Beats-100-Similar-to-Flatten-Binary-Tree-to-LinkedList
	 * 
	 * Recursively reaches the last node, and then recursively calls its child node. 
	 * If it is null, then the function returns, else it will recursively traverse 
	 * the child list.
	 * When it reaches the end, it will set its next to previous_post_order 
	 * (initialized to null) and child as null. previous_post_order is now updated to 
	 * the current node. Similarly continue the process for all nodes.
	 */
	Node previous_post_order = null;

	public Node flatten_post_order(Node head) {
		if (head == null) {
			return head;
		}
		
		flatten_post_order(head.next);
		flatten_post_order(head.child);
		
		head.next = previous_post_order;
		if (previous_post_order != null) {
			previous_post_order.prev = head;
		}
		head.child = null;
		
		previous_post_order = head;
		return head;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/184156/Easy-Understand-Java-Recursive-solution-beat-100-with-Explanation
	 * 
	 * We always keep a preNode global variable to keep track of the last node we 
	 * visited and connect current node head with this preNode node. So for each 
	 * recursive call, we do
	 * 
	 * 1. Connect last visited node with current node by letting preNode.next point to 
	 *    current node head and current node's prev point to preNode
	 * 2. Mark current node as preNode. preNode = head
	 * 3. If there is head.child, we recursively visit and flatten its child node
	 * 4. Recursively visit and flatten its next node
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/302421/Java-pre-order-and-post-order-solution-same-idea-with-114.-Flatten-Binary-Tree-to-Linked-List
	 */
    Node preNode = null; // Global variable preNode to track the last node we visited 

	public Node flatten_pre_order(Node head) {
		if (head == null) {
			return null;
		}

		/* Connect last visited node with current node */
		if (preNode != null) {
			preNode.next = head;
			head.prev = preNode;
		}

		Node next = head.next;
		Node child = head.child;
		head.child = null;
		
		preNode = head;

		flatten_pre_order(child);
		flatten_pre_order(next);
		return head;
	}
	
	/*
	 * 可能不符題意
	 * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/314871/Java-0-ms-solution
	 */
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/152725/Python-solution-with-explanation
     * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/154908/Python-easy-solution-using-stack
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/discuss/152066/c%2B%2B-about-10-lines-solution
     */
	
	class Node {
		public int val;
	    public Node prev;
	    public Node next;
	    public Node child;

	    public Node() {}

	    public Node(int _val,Node _prev,Node _next,Node _child) {
	        val = _val;
	        prev = _prev;
	        next = _next;
	        child = _child;
	    }

	}

}
