package OJ0291_0300;

import definition.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Queue;
import java.util.Stack;

public class Serialize_and_Deserialize_Binary_Tree {
	/*
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74264/Short-and-straight-forward-BFS-Java-code-with-a-queue
	 * 
	 * Use typical BFS method. Use string n to represent null values. The string of 
	 * the binary tree in the example will be "1 2 3 n n 4 5 n n n n ".
	 * 
	 * When deserialize the string, I assign left and right child for each not-null 
	 * node, and add the not-null children to the queue, waiting to be handled later.
	 * 
	 * 1) the implementation of Queue to allow null value
	 *    (LinkedList rather than ArrayDeque)
	 * 2) the algorithm to deserialize using Queue
	 * 
	 * Rf :
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/285111/Java-Solution-Level-Order-Traversal
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74264/Short-and-straight-forward-BFS-Java-code-with-a-queue/185700
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74264/Short-and-straight-forward-BFS-Java-code-with-a-queue/77443
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74264/Short-and-straight-forward-BFS-Java-code-with-a-queue/77445
	 */
	public class Codec {
		public String serialize(TreeNode root) {
			if (root == null)
				return "n";
			
			Queue<TreeNode> q = new LinkedList<>();
			StringBuilder res = new StringBuilder();
			q.add(root);
			
			while (!q.isEmpty()) {
				TreeNode node = q.poll();
				if (node == null) {
					res.append("n ");
					continue;
				}
				
				res.append(node.val + " ");
				q.add(node.left);
				q.add(node.right);
			}
			return res.toString();
		}

		public TreeNode deserialize(String data) {
			if (data.equals("n"))
				return null;
			
			Queue<TreeNode> q = new LinkedList<>();
			String[] values = data.split(" ");
			TreeNode root = new TreeNode(Integer.parseInt(values[0]));
			q.add(root);
			
			for (int i = 1; i < values.length; i++) {
				TreeNode parent = q.poll();
				
				if (!values[i].equals("n")) {
					TreeNode left = new TreeNode(Integer.parseInt(values[i]));
					parent.left = left;
					q.add(left);
				}
				
				if (!values[++i].equals("n")) {
					TreeNode right = new TreeNode(Integer.parseInt(values[i]));
					parent.right = right;
					q.add(right);
				}
			}
			return root;
		}
	}
	
	/*
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74253/Easy-to-understand-Java-Solution
	 * 
	 * Pre-order traversal; "X" to denote null node; split node with "," 
	 * For deserializing, we use a Queue to store the pre-order traversal and since 
	 * we have "X" as null node, we know exactly where to end building subtress.
	 * 
	 * sb.append(NN).append(spliter); in serialize whenever there's a null node.
	 * In deserialize, he uses if (val.equals(NN)) return null; to return a null node 
	 * whenever detecting one.
	 * So it will no longer check the queue when it's empty, because it will have 
	 * returned a "null" for that node already.
	 * 
	 * It only recurses whenever there's a value other than "NN" in the queue. And the 
	 * serialize step would have encoded any null children as "NN", so the queue will 
	 * never be empty when .remove() is called.
	 * 
	 * The key point is that since we keep the null nodes in serialization, so every 
	 * node(except null nodes) consumes 2 nodes, if all the nodes(except null nodes) 
	 * in the left subtree are paired with 2 nodes, then the rest nodes belongs to the 
	 * right subtree.
	 * 
	 * #105 preorder/postorder + inorder: why we have to use 2 lists/traversals?
	 * The lists does not preserve the null, so we do not have an indicator to check 
	 * if a node is in the left subtree or right subtree, so 2 traversals are needed.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74253/Easy-to-understand-Java-Solution/138957
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74253/Easy-to-understand-Java-Solution/270397
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74253/Easy-to-understand-Java-Solution/269310
	 * 
	 * Other code:
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74261/Easy-to-understand-java-solution
	 */
	public class Codec_preorder {
		private static final String spliter = ",";
		private static final String NN = "X";

		// Encodes a tree to a single string.
		public String serialize(TreeNode root) {
			StringBuilder sb = new StringBuilder();
			buildString(root, sb);
			return sb.toString();
		}

		private void buildString(TreeNode node, StringBuilder sb) {
			if (node == null) {
				sb.append(NN).append(spliter);
			} 
			else {
				sb.append(node.val).append(spliter);
				buildString(node.left, sb);
				buildString(node.right, sb);
			}
		}

		// Decodes your encoded data to tree.
		public TreeNode deserialize(String data) {
			Deque<String> nodes = new LinkedList<>();
			nodes.addAll(Arrays.asList(data.split(spliter)));
			return buildTree(nodes);
		}

		private TreeNode buildTree(Deque<String> nodes) {
			String val = nodes.remove();
			if (val.equals(NN))
				return null;
			else {
				TreeNode node = new TreeNode(Integer.valueOf(val));
				node.left = buildTree(nodes);
				node.right = buildTree(nodes);
				return node;
			}
		}
	}

	/*
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/281714/Clean-Java-Solution
	 * 
	 * The decoded string is well built such that every node (except for those "#") it 
	 * will have left and right children, this becomes the hidden contract that once 
	 * we finish the loop of every node and its children, the queue will be empty. 
	 * i.e. the queue won't be empty before we finish.
	 * 
	 * Rf : https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/281714/Clean-Java-Solution/405886
	 * 
	 * Other code:
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74417/Short-and-clear-recursive-Java-solution
	 */
	public class Codec_preorder2 {
		public String serialize(TreeNode root) {
			if (root == null)
				return "#";
			return root.val + "," + serialize(root.left) + "," + serialize(root.right);
		}

		public TreeNode deserialize(String data) {
			Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
			return helper(queue);
		}

		private TreeNode helper(Queue<String> queue) {
			String s = queue.poll();
			if (s.equals("#"))
				return null;
			
			TreeNode root = new TreeNode(Integer.valueOf(s));
			root.left = helper(queue);
			root.right = helper(queue);
			return root;
		}
	}
	
	// by myself
	public class Codec_self {

	    // Encodes a tree to a single string.
	    public String serialize(TreeNode root) {
	        if (root == null)
	            return null;
	        
	        LinkedList<TreeNode> queue = new LinkedList<>();
	        queue.offerLast(root);
	        
	        StringBuilder sb = new StringBuilder("[");
	        while (!queue.isEmpty()) {
	            LinkedList<TreeNode> temp = new LinkedList<>();
	            boolean hasItem = false;
	            
	            int size = queue.size();
	            for (int i = 0; i < size; i++) {
	                TreeNode cur = queue.pollFirst();
	            
	                if (cur != null) {
	                    sb.append(cur.val + ",");
	                }
	                else {
	                    sb.append("null,");
	                }
	                
	                if (cur != null) {
	                    temp.offerLast(cur.left);
	                    temp.offerLast(cur.right);
	                    
	                    if (cur.left != null || cur.right != null)
	                        hasItem = true;
	                }
	                else {
	                    //temp.offerLast(null);
	                    //temp.offerLast(null);
	                }
	            }
	            
	            if (hasItem) {
	                queue.addAll(temp);
	            }
	        }
	        
	        sb.setLength(sb.length() - 1);
	        sb.append("]");
	        System.out.println(sb.toString());
	        return sb.toString();
	    }

	    // Decodes your encoded data to tree.
	    public TreeNode deserialize(String data) {
	        if (data == null || data.length() == 0 || data.equals("null"))
	            return null;
	        
	        String realData = data.substring(1, data.length() - 1);
	        String[] dataArray = realData.split(",");
	        
	        TreeNode root = new TreeNode(Integer.parseInt(dataArray[0]));
	        LinkedList<TreeNode> queue = new LinkedList<>();
	        queue.offerLast(root);
	        
	        System.out.println("data length = " + dataArray.length);
	        for (int i = 1; i < dataArray.length; i += 2) {
	            TreeNode cur = queue.pollFirst();
	            
	            String left = dataArray[i];
	            String right = dataArray[i + 1];
	            
	            if (!left.equals("null")) {
	                cur.left = new TreeNode(Integer.parseInt(left));
	                queue.offerLast(cur.left);
	            }
	            else {
	                //queue.offerLast(null);
	            }
	            if (!right.equals("null")) {
	                cur.right = new TreeNode(Integer.parseInt(right));
	                queue.offerLast(cur.right);
	            }
	            else {
	                //queue.offerLast(null);
	            }
	        }
	        return root;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74464/Share-my-Java-iterative-preorder-solution
	 * 
	 * Preorder Traversal, time complexity for both encoding and decoding is O(n).
	 * 
	 * In decoding solution, I use split() to store the values in list[], we can also 
	 * use two pointers to get each component separated by the comma, that will make 
	 * the space complexity down to O(h) where h is the height of the tree.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74260/Recursive-DFS-Iterative-DFS-and-BFS
	 */
	public class Codec_iterative_preorder {

		// Encodes a tree to a single string.
		public String serialize(TreeNode root) {
			if (root == null)
				return null;

			String delim = "";
			StringBuilder sb = new StringBuilder();

			Stack<TreeNode> stack = new Stack<>();
			stack.push(root);

			// preorder traversal
			while (!stack.isEmpty()) {
				TreeNode node = stack.pop();
				sb.append(delim).append(node == null ? "#" : String.valueOf(node.val));
				delim = ",";

				if (node != null) {
					stack.push(node.right);
					stack.push(node.left);
				}
			}

			return sb.toString();
		}

		// Decodes your encoded data to tree.
		public TreeNode deserialize(String data) {
			if (data == null)
				return null;

			String[] list = data.split(",");

			// create the root node and push it to the stack
			TreeNode root = new TreeNode(Integer.valueOf(list[0]));
			Stack<TreeNode> stack = new Stack<>();
			stack.push(root);

			// direction flag
			boolean left = true;

			for (int i = 1; i < list.length; i++) {
				TreeNode node = list[i].equals("#") ? 
						null : new TreeNode(Integer.valueOf(list[i]));

				if (left) {
					stack.peek().left = node;
					if (node == null)
						left = false;
				} 
				else {
					stack.pop().right = node;
					if (node != null)
						left = true;
				}

				if (node != null)
					stack.push(node);
			}

			return root;
		}

	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74259/Recursive-preorder-Python-and-C%2B%2B-O(n)
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/396124/Python-very-easy-to-understand-recursive-preorder-with-comments
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/166904/Python-or-BFS-tm
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74430/Tuplify-%2B-json-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74252/Clean-C%2B%2B-solution
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74385/Leetcode-way-in-C%2B%2B-and-Python
     */
	
	// Your Codec object will be instantiated and called as such:
	// Codec codec = new Codec();
	// codec.deserialize(codec.serialize(root));

}
