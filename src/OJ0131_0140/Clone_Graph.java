package OJ0131_0140;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Clone_Graph {
	/*
	 * https://leetcode.com/problems/clone-graph/discuss/250216/BFS-and-DFS-simple-Java
	 * 
	 * Use HashMap to look up nodes and add connection to them while performing BFS.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/clone-graph/discuss/42482/Java-BFS-solution
	 * https://leetcode.com/problems/clone-graph/discuss/42319/Simple-Java-iterative-BFS-solution-with-HashMap-and-queue
	 */
	public Node cloneGraph(Node node) {
		if (node == null)
			return null;
		
		// use a map to save cloned nodes
		Map<Node, Node> map = new HashMap<Node, Node>();
		
		// clone the root
		map.put(node, new Node(node.val, new ArrayList<Node>()));
		
		// store **original** nodes need to be visited
		Queue<Node> q = new LinkedList<Node>();
		q.add(node);

		while (!q.isEmpty()) {
			Node top = q.remove();
			
			// handle the neighbors
			for (Node neighbor : top.neighbors) {
				// visited
				if (!map.containsKey(neighbor)) {
					// clone the neighbor
					map.put(neighbor, new Node(neighbor.val, new ArrayList<Node>()));
					// add it to the next level
					q.add(neighbor);
				}
				
				// copy the neighbor
				map.get(top).neighbors.add(map.get(neighbor));
			}
		}
		return map.get(node);
	}
	
	// https://leetcode.com/problems/clone-graph/discuss/42481/Java-DFS-solution-(iterative)
	public Node cloneGraph_stack(Node root) {
		if (root == null)
			return null;

		// use a stack to help DFS
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		// use a map to save cloned nodes
		Map<Node, Node> map = new HashMap<Node, Node>();

		// clone the root
		map.put(root, new Node(root.val, new ArrayList<>()));

		while (!stack.isEmpty()) {
			Node node = stack.pop();

			// handle the neighbors
			for (Node neighbor : node.neighbors) {
				if (!map.containsKey(neighbor)) {
					// clone the neighbor
					map.put(neighbor, new Node(neighbor.val, new ArrayList<>()));
					// add it to the next level
					stack.push(neighbor);
				}

				// copy the neighbor
				map.get(node).neighbors.add(map.get(neighbor));
			}
		}

		return map.get(root);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/40718
	 * 
	 * Rf :
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/40695
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/177408
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/121755
	 * 
	 * Other code:
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/40711
	 * https://leetcode.com/problems/clone-graph/discuss/42309/Depth-First-Simple-Java-Solution/195236
	 */
	public Node cloneGraph_dfs(Node node) {
		HashMap<Integer, Node> map = new HashMap<>();
		return dfs(node, map);
	}

	private Node dfs(Node node, HashMap<Integer, Node> map) {
		if (node == null)
			return null;
		if (map.containsKey(node.val)) {
			return map.get(node.val);
		}
		
		Node clone = new Node(node.val, new ArrayList<>());
		map.put(node.val, clone);
		for (int i = 0; i < node.neighbors.size(); i++) {
			clone.neighbors.add(dfs(node.neighbors.get(i), map));
		}
		return clone;
	}

	// by myself
	public Node cloneGraph_self(Node node) {
        if (node == null)
            return null;
        
        LinkedList<Node> queue = new LinkedList<>();
        Map<Node, Node> map = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        queue.offerLast(node);
        visited.add(node);
        
        while (!queue.isEmpty()) {
            Node cur = queue.pollFirst();
            map.put(cur, new Node());
            
            for (Node neighbor : cur.neighbors) {
                if (visited.add(neighbor)) {
                    queue.offerLast(neighbor);
                }
            }
        }
        
        for (Map.Entry<Node, Node> entry : map.entrySet()) {
            Node originNode = entry.getKey();
            Node cloneNode = entry.getValue();
            
            cloneNode.val = originNode.val;
            List<Node> list = new ArrayList<>();
            for (Node neighbor : originNode.neighbors) {
                Node neighborClone = map.get(neighbor);
                list.add(neighborClone);
            }
            cloneNode.neighbors = list;
        }
        return map.get(node);
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/clone-graph/discuss/42314/Python-solutions-(BFS-DFS-iteratively-DFS-recursively).
     * https://leetcode.com/problems/clone-graph/discuss/42465/4-7-lines-Python-~7-lines-C%2B%2BJava
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/clone-graph/discuss/42313/C%2B%2B-BFSDFS
     * https://leetcode.com/problems/clone-graph/discuss/42508/Simple-C%2B%2B-solution-using-DFS-and-recursion.
     * https://leetcode.com/problems/clone-graph/discuss/42502/Accepted-recursive-depth-first-search-solution
     */

	class Node {
		public int val;
		public List<Node> neighbors;
		
		public Node() {}
		
		public Node(int _val,List<Node> _neighbors) {
			val = _val;
			neighbors = _neighbors;
		}
	}
	
}
