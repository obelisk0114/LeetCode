package OJ0201_0210;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;

public class Course_Schedule_II {
	/*
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59472/Java-13ms-Iterative-DFS-solution-with-Explanation
	 * 
	 * We define in degree as the number of edges into a node in the graph. What we do 
	 * is we remove the nodes that has in degree equals to 0, decrease the in degree 
	 * of the nodes that require the current node, and repeat, until we've removed all 
	 * the nodes (the successful case), or there's no node with in degree equals to 0 
	 * (the failed case).
	 * 
	 * Kahn's Algorithm
	 * 沒有被任何邊連向的點，就可以作為第一點。如果有很多個第一點，那麼找哪一點都行。
	 * 決定第一點之後，就可以刪去此點，以及刪去由此點連出去的邊
	 * 
	 * Rf :
	 * http://www.csie.ntnu.edu.tw/~u91029/DirectedAcyclicGraph.html
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59390/Java-6ms-topological-sort-solution-with-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59475/A-simple-Java-solution-with-Queue
	 */
	public int[] findOrder(int numCourses, int[][] prerequisites) {
		int[] indegree = new int[numCourses];
		List<List<Integer>> graph = new ArrayList<>();
		for (int i = 0; i < numCourses; i++) {
			graph.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < prerequisites.length; i++) {
			int course = prerequisites[i][0], prere = prerequisites[i][1];
			indegree[course]++;
			graph.get(prere).add(course);
		}

		Queue<Integer> queue = new ArrayDeque<>();
		for (int i = 0; i < numCourses; i++) {
			if (indegree[i] == 0)
				queue.offer(i);
		}

		int count = 0;
		int[] order = new int[numCourses];
		while (!queue.isEmpty()) {
			Integer prere = queue.poll();
			order[count++] = prere;
			for (Integer course : graph.get(prere)) {
				if (--indegree[course] == 0)
					queue.offer(course);
			}
		}

		if (count == numCourses)
			return order;
		else
			return new int[0];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59472/Java-13ms-Iterative-DFS-solution-with-Explanation
	 * 
	 * Mapping of element value in visited array to colors in CLRS DFS algorithm:
	 * 0 -> White, -1 -> Gray, 1 -> Black
	 * 
	 * We need to peek a node in stack for the first time, and pop it for the second 
	 * time we meet it. In other words, we need to keep a node in stack until the dfs 
	 * search rooted at it has been finished, which is equivalent to the end of a 
	 * recursive call. For the end of the corresponding dfs search rooted at prere: 
	 * -1 corresponding to prere as a normal node; 1 corresponding to prere as an end 
	 * node of a cross edge.
	 * 
	 * DFS 離開點的順序，顛倒之後，正好是拓撲順序。
	 * 
	 * DFS 優先走到最深的點，直到不能再深為止。 DFS 也會優先找出所有最深的點，離開點的原則是最深的點先離開。
	 * 最深的點當然就是拓撲順序最後的點。
	 * 
	 * Rf :
	 * http://www.csie.ntnu.edu.tw/~u91029/DirectedAcyclicGraph.html
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59317/Two-AC-solution-in-Java-using-BFS-and-DFS-with-explanation
	 */
	public int[] findOrder_DFS(int numCourses, int[][] prerequisites) {
		List<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < numCourses; i++) {
			graph.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < prerequisites.length; i++) {
			graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
		}

		int[] visited = new int[numCourses];
		ArrayDeque<Integer> stack = new ArrayDeque<Integer>();
		for (int i = 0; i < numCourses; i++) {
			if (visited[i] == 0 && dfsDetectCycle(graph, visited, stack, i))
				return new int[0];
		}
		
		int[] order = new int[numCourses];
		for (int i = 0; i < numCourses; i++)
			order[i] = stack.pop();
		return order;
	}

	private boolean dfsDetectCycle(List<ArrayList<Integer>> graph, int[] visited, 
			ArrayDeque<Integer> stack, int prere) {
		
		visited[prere] = -1; // Gray
		for (int course : graph.get(prere)) {
			if (visited[course] == -1)
				return true;
			else if (visited[course] == 0 && dfsDetectCycle(graph, visited, stack, course))
				return true;
		}
		visited[prere] = 1; // Black
		stack.push(prere);
		return false;
	}
	
	// by myself
	public int[] findOrder_self(int numCourses, int[][] prerequisites) {
        List<List<Integer>> edgeList = new ArrayList<List<Integer>>(numCourses);
        int[] income = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            edgeList.add(new ArrayList<Integer>());
        }
        for (int[] need : prerequisites) {
            edgeList.get(need[1]).add(need[0]);
            income[need[0]]++;
        }
        
        boolean[] visited = new boolean[numCourses];
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (income[i] == 0) {
                queue.add(i);
            }
        }
        
        int[] ans = new int[numCourses];
        int i = 0;
        while (!queue.isEmpty()) {
            int cur = queue.removeFirst();
            if (!visited[cur] && income[cur] == 0) {
                ans[i] = cur;
                visited[cur] = true;
                
                for (Integer edge : edgeList.get(cur)) {
                    income[edge]--;
                    queue.add(edge);
                }
                i++;
            }
        }
       
        if (i != numCourses)
            return new int[0];
        else
            return ans;
    }
	
	/*
	 * https://leetcode.com/problems/course-schedule-ii/discuss/59538/Standard-BFS-method-for-Course-Schedule-II
	 * 
	 * The idea is to start from courses upon which no other courses will depend
	 * (These courses all have the least priority and should come at the last in the 
	 * order list). Then do standard BFS on these courses and try to find courses that 
	 * have next level of priority(These courses will come right before the least 
	 * priority courses in the order list). Continue the process until we have no more 
	 * courses to deal with. If there is no cycle in the prerequisites graph, then in 
	 * this way we should be able to deal with all the courses. Otherwise there will 
	 * be remaining courses left untended. 
	 */
	public int[] findOrder_Map(int numCourses, int[][] prerequisites) {
		// for each course in the map nextVertex, the corresponding set contains 
		// prerequisite courses for this course 
	    Map<Integer, Set<Integer>> nextVertex = new HashMap<>();
	    // preVertex[i] indicates the number of courses that depend on course i
	    int[] preVertex = new int[numCourses];
	    
	    // set up nextVertex and preVertex
	    for (int i = 0; i < prerequisites.length; i++) {
	    	if (!nextVertex.containsKey(prerequisites[i][0])) {
	    		nextVertex.put(prerequisites[i][0], new HashSet<>());
	    	}
	    	
	    	if (nextVertex.get(prerequisites[i][0]).add(prerequisites[i][1])) {
	    		preVertex[prerequisites[i][1]]++;
	    	}
	    }
	    
	    // queue for BFS, which will only hold courses currently upon which no other courses depend
	    Deque<Integer> queue = new ArrayDeque<>();
	    
	    for (int i = 0; i < preVertex.length; i++) {
	    	// start from courses upon which no other courses depend. 
	    	// These courses should come last in the order list
	    	if (preVertex[i] == 0) {
	    		queue.offerLast(i);
	    	}
	    }
	    
	    // array for the result, which will be filled up from the end by index
	    int[] res = new int[numCourses];
	    int index = res.length - 1;
	    
	    while (!queue.isEmpty()) {
	    	int key = queue.pollFirst(); // this is a course that no other courses will depend upon
	    	res[index--] = key;          // so we put it at the end of the order list
	    	
	    	// since we are done with course "key", for any other course that course 
	    	// "key" is dependent on, we can decrease the corrresponding preVertex by
	    	// one and check if it is qualified to be added to the queue.
	    	if (nextVertex.containsKey(key)) {
	    		for (int i : nextVertex.get(key)) {
	    			if (--preVertex[i] == 0) {
	    				queue.offerLast(i);
	    			}
	    		}
	    	}
	    	
	    	--numCourses; // we are done with course "key", so reduce the remaining number of courses by 1
	    }
	    
	    // if the remaining number of courses is not zero, then we cannot complete all 
	    // the courses; otherwise return the result
	    return numCourses == 0 ? res : new int[0];
	}

}
