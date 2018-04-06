package OJ0251_0260;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Binary_Tree_Paths {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-paths/discuss/68471/*Java*-Easy-to-understand-solution-3-line-DFS
	 * 
	 * Rf : https://leetcode.com/problems/binary-tree-paths/discuss/68258/Accepted-Java-simple-solution-in-8-lines
	 * 
	 * Other code :
	 * https://leetcode.com/problems/binary-tree-paths/discuss/68463/2ms-Java-recursive-solution-with-explaination
	 */
	public List<String> binaryTreePaths(TreeNode root) {
		List<String> res = new ArrayList<>(); // stores the final output
		if (root == null)
			return res;
		helper(root, "", res);
		return res;
	}

	private void helper(TreeNode root, String str, List<String> res) {
		if (root.left == null && root.right == null)
			res.add(str + root.val); // reach a leaf node, completes a path
		if (root.left != null)
			helper(root.left, str + root.val + "->", res); // recursively checks its left child
		if (root.right != null)
			helper(root.right, str + root.val + "->", res); // recursively checks its right child
	}
	
	// The following 2 functions are by myself.
	public List<String> binaryTreePaths_self3(TreeNode root) {
        List<String> result = new ArrayList<String>();
        if (root == null)
            return result;
        BTPaths_self3(root, new StringBuilder(), result);
        
        return result;
    }
    private void BTPaths_self3(TreeNode root, StringBuilder sb, List<String> res) {
        int length = sb.length();
        if (root.left == null && root.right == null) {
            sb.append(root.val);
            res.add(sb.toString());
            sb.setLength(length);
            return;
        }
        
        if (root.left != null) {
            sb.append(root.val);
            sb.append("->");
            BTPaths_self3(root.left, sb, res);
            sb.setLength(length);
        }
        
        if (root.right != null) {
            sb.append(root.val);
            sb.append("->");
            BTPaths_self3(root.right, sb, res);
            sb.setLength(length);
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/binary-tree-paths/discuss/68265/Java-solution-using-StringBuilder-instead-of-string-manipulation.
     */
	public List<String> binaryTreePaths_StringBuilder(TreeNode root) {
		List<String> rst = new ArrayList<String>();
		if (root == null)
			return rst;
		
		StringBuilder sb = new StringBuilder();
		helper_StringBuilder(rst, sb, root);
		return rst;
	}
	public void helper_StringBuilder(List<String> rst, StringBuilder sb, TreeNode root) {
		if (root == null)
			return;
		
		int tmp = sb.length();
		if (root.left == null && root.right == null) {
			sb.append(root.val);
			rst.add(sb.toString());
			sb.delete(tmp, sb.length());
			return;
		}
		sb.append(root.val + "->");
		helper_StringBuilder(rst, sb, root.left);
		helper_StringBuilder(rst, sb, root.right);
		sb.delete(tmp, sb.length());
		return;
	}
	
	// The following 2 functions are by myself.
	public List<String> binaryTreePaths_self2(TreeNode root) {
        List<String> result = new ArrayList<String>();
        if (root == null)
            return result;
        BTPaths_self2(root, new StringBuilder(), result);
        
        return result;
    }
    private void BTPaths_self2(TreeNode root, StringBuilder sb, List<String> res) {
        if (root.left == null && root.right == null) {
            StringBuilder sb2 = new StringBuilder(sb);
            sb2.append(root.val);
            res.add(sb2.toString());
            return;
        }
        
        if (root.left != null) {
            String value = Integer.toString(root.val);
            sb.append(value);
            sb.append("->");
            BTPaths_self2(root.left, sb, res);
            int length = value.length() + 2;
            sb.delete(sb.length() - length, sb.length());
        }
        
        if (root.right != null) {
            String value = Integer.toString(root.val);
            sb.append(value);
            sb.append("->");
            BTPaths_self2(root.right, sb, res);
            int length = value.length() + 2;
            sb.delete(sb.length() - length, sb.length());
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/binary-tree-paths/discuss/68443/My-java-and-c++-solutionc++-4ms
     * 
     * preorder to visit tree
     */
	public List<String> binaryTreePaths_preorder(TreeNode root) {
		List<String> l = new ArrayList<>();
		if (root != null)
			pre(l, root, "");
		
		return l;
	}
	void pre(List<String> l, TreeNode r, String s) {
		if (r == null)
			return;
		
		if (s.isEmpty())
			s += r.val;
		else
			s += ("->" + r.val);
		
		if (r.left != null || r.right != null) {
			pre(l, r.left, s);
			pre(l, r.right, s);
		} 
		else
			l.add(s);
	}
	
	// The following 2 functions are by myself.
	public List<String> binaryTreePaths_self(TreeNode root) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        List<String> result = new ArrayList<String>();
        if (root == null)
            return result;
        BTPaths_self(root, new ArrayList<Integer>(), res);
        for (List<Integer> list : res) {
            StringBuilder sb = new StringBuilder();
            for (Integer i : list) {
                sb.append(i);
                sb.append("->");
            }
            result.add(sb.substring(0, sb.length() - 2));
        }
        
        return result;
    }
    private void BTPaths_self(TreeNode root, List<Integer> list, List<List<Integer>> res) {
        if (root.left == null && root.right == null) {
            List<Integer> list2 = new ArrayList<Integer>(list);
            list2.add(root.val);
            res.add(list2);
            return;
        }
        
        if (root.left != null) {
            list.add(root.val);
            BTPaths_self(root.left, list, res);
            list.remove(list.size() - 1);
        }
        
        if (root.right != null) {
            list.add(root.val);
            BTPaths_self(root.right, list, res);
            list.remove(list.size() - 1);
        }
    }
    
    // https://leetcode.com/problems/binary-tree-paths/discuss/68278/My-Java-solution-in-DFS-BFS-recursion
	public List<String> binaryTreePaths_BFS(TreeNode root) {
		List<String> list = new ArrayList<String>();
		Queue<TreeNode> qNode = new LinkedList<TreeNode>();
		Queue<String> qStr = new LinkedList<String>();

		if (root == null)
			return list;
		
		qNode.add(root);
		qStr.add("");
		
		while (!qNode.isEmpty()) {
			TreeNode curNode = qNode.remove();
			String curStr = qStr.remove();

			if (curNode.left == null && curNode.right == null)
				list.add(curStr + curNode.val);
			
			if (curNode.left != null) {
				qNode.add(curNode.left);
				qStr.add(curStr + curNode.val + "->");
			}
			if (curNode.right != null) {
				qNode.add(curNode.right);
				qStr.add(curStr + curNode.val + "->");
			}
		}
		return list;
	}
	
	// https://leetcode.com/problems/binary-tree-paths/discuss/68278/My-Java-solution-in-DFS-BFS-recursion
	public List<String> binaryTreePaths_DFS(TreeNode root) {
		List<String> list = new ArrayList<String>();
		Stack<TreeNode> sNode = new Stack<TreeNode>();
		Stack<String> sStr = new Stack<String>();

		if (root == null)
			return list;
		
		sNode.push(root);
		sStr.push("");
		
		while (!sNode.isEmpty()) {
			TreeNode curNode = sNode.pop();
			String curStr = sStr.pop();

			if (curNode.left == null && curNode.right == null)
				list.add(curStr + curNode.val);
			
			if (curNode.left != null) {
				sNode.push(curNode.left);
				sStr.push(curStr + curNode.val + "->");
			}
			if (curNode.right != null) {
				sNode.push(curNode.right);
				sStr.push(curStr + curNode.val + "->");
			}
		}
		return list;
	}
    
    // https://leetcode.com/problems/binary-tree-paths/discuss/68282/Clean-Java-solution-(Accepted)-without-any-helper-recursive-function
	public List<String> binaryTreePaths_merge(TreeNode root) {
		List<String> paths = new LinkedList<>();
		if (root == null)
			return paths;

		if (root.left == null && root.right == null) {
			paths.add(root.val + "");
			return paths;
		}

		for (String path : binaryTreePaths_merge(root.left)) {
			paths.add(root.val + "->" + path);
		}

		for (String path : binaryTreePaths_merge(root.right)) {
			paths.add(root.val + "->" + path);
		}

		return paths;
	}
	
	// https://leetcode.com/problems/binary-tree-paths/discuss/68453/My-java-non-recursion-solution-using-stack-and-wrapper

}
