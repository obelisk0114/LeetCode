package OJ0331_0340;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class Nested_List_Weight_Sum {
	// https://discuss.leetcode.com/topic/41495/java-solution-similar-to-tree-level-order-traversal
	public int depthSum(List<NestedInteger> nestedList) {
		if (nestedList == null) {
			return 0;
		}

		int sum = 0;
		int level = 1;

		Queue<NestedInteger> queue = new LinkedList<NestedInteger>(nestedList);
		while (queue.size() > 0) {
			int size = queue.size();

			for (int i = 0; i < size; i++) {
				NestedInteger ni = queue.poll();

				if (ni.isInteger()) {
					sum += ni.getInteger() * level;
				} 
				else {
					queue.addAll(ni.getList());
				}
			}

			level++;
		}

		return sum;
	}
	
	// myself ?
	public int depthSum_BFS2(List<NestedInteger> nestedList) {
		int sum = 0;
		int level = 1;
		LinkedList<NestedInteger> nest = new LinkedList<NestedInteger>(nestedList);
		int len = nest.size();
		
		while (!nest.isEmpty()) {
			NestedInteger cur = nest.removeFirst(); 
			if (cur.getInteger() == null) {
				nest.addAll(cur.getList());
			}
			else {
				sum += level * cur.getInteger();
			}
			len--;
			
			if (len == 0) {
				level++;
				len = nest.size();
			}
		}
		return sum;
	}
	
	// https://discuss.leetcode.com/topic/41357/2ms-easy-to-understand-java-solution
	public int depthSum_DFS(List<NestedInteger> nestedList) {
	    return depthSum_DFS(nestedList, 1);
	}

	private int depthSum_DFS(List<NestedInteger> list, int depth) {
		int sum = 0;
	    for (NestedInteger n : list) {
	        if (n.isInteger()) {
	            sum += n.getInteger() * depth;
	        } else {
	            sum += depthSum_DFS(n.getList(), depth + 1);
	        }
	    }
	    return sum;
	}
	
	// https://discuss.leetcode.com/topic/65020/java-clean-code-with-explanations-and-running-time-5-solutions
	// https://discuss.leetcode.com/topic/41413/simple-java-bfs-2ms-solution-with-queue
	// https://discuss.leetcode.com/topic/41458/java-iterative-solution

	/*
	 * provided interface
	 */
	public interface NestedInteger {
		// @return true if this NestedInteger holds a single integer, rather
		// than a nested list.
		public boolean isInteger();

		// @return the single integer that this NestedInteger holds, if it holds
		// a single integer
		// Return null if this NestedInteger holds a nested list
		public Integer getInteger();

		// @return the nested list that this NestedInteger holds, if it holds a
		// nested list
		// Return null if this NestedInteger holds a single integer
		public List<NestedInteger> getList();
	}
}
