package OJ0201_0210;

import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Course_Schedule {
	/*
	 * https://leetcode.com/problems/course-schedule/discuss/58516/Easy-BFS-Topological-sort-Java
	 * 
	 * put NODE with 0 indgree into the queue, then make indegree of NODE's successor 
	 * decreasing 1. Keep the above steps with BFS.
	 * 
	 * Finally, if each node' indgree equals 0, then it is validated DAG 
	 * (Directed Acyclic Graph), which means the course schedule can be finished.
	 * 
	 * 可以作為第一點的點，想必它不必排在其他點後方。也就是說，沒有被任何邊連向的點，就可以作為第一點。
	 * 如果有很多個第一點，那麼找哪一點都行。
	 * 
	 * 決定第一點之後，那麼剩下所有點都會在第一點後方。所有關於第一點的先後規定，都已經符合了，規定存不存在都無所謂。
	 * 因此，決定第一點之後，就可以刪去此點，以及刪去由此點連出去的邊 ── 原問題可以遞迴地縮小！
	 * 
	 * 只要反覆尋找沒有被任何邊連向的點，然後刪去此點以及刪去由此點連出去的邊，就可以找出一個合理的排列順序了。
	 * 要找出合理的排列順序，也可以由最後一點開始決定！無論要從第一點找到最後一點，或是從最後一點找到第一點，都是可以的。
	 * 
	 * Rf :
	 * http://www.csie.ntnu.edu.tw/~u91029/DirectedAcyclicGraph.html
	 * https://blog.csdn.net/lighthear/article/details/79737011
	 * 
	 * https://leetcode.com/problems/course-schedule/discuss/58669/Concise-JAVA-solutions-based-on-BFS-and-DFS-with-explanation
	 * https://leetcode.com/problems/course-schedule/discuss/58509/18-22-lines-C++-BFSDFS-Solutions
	 * leetcode.com/problems/course-schedule/discuss/58516/Easy-BFS-Topological-sort-Java/59977
	 */
	public boolean canFinish(int numCourses, int[][] prerequisites) {
		int[][] matrix = new int[numCourses][numCourses]; // i -> j
		int[] indegree = new int[numCourses];

		for (int i = 0; i < prerequisites.length; i++) {
			int ready = prerequisites[i][0];
			int pre = prerequisites[i][1];
			if (matrix[pre][ready] == 0)
				indegree[ready]++; // duplicate case
			matrix[pre][ready] = 1;
		}

		int count = 0;
		Queue<Integer> queue = new LinkedList<Integer>();
		for (int i = 0; i < indegree.length; i++) {
			if (indegree[i] == 0)
				queue.offer(i);
		}
		
		while (!queue.isEmpty()) {
			int course = queue.poll();
			count++;
			for (int i = 0; i < numCourses; i++) {
				if (matrix[course][i] != 0) {
					if (--indegree[i] == 0)
						queue.offer(i);
				}
			}
		}
		return count == numCourses;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/course-schedule/discuss/58524/Java-DFS-and-BFS-solution/60014
	 * 
	 * For DFS, it will first visit a node, then one neighbor of it, then one neighbor 
	 * of this neighbor... and so on. If it meets a node which was visited in the 
	 * current process of DFS visit, a cycle is detected and we will return false. 
	 * Otherwise it will start from another unvisited node and repeat this process 
	 * till all the nodes have been visited. 
	 * Note that you should make two records: one is to record all the visited nodes 
	 * and the other is to record the visited nodes in the current DFS visit.
	 * 
	 * Rf : https://leetcode.com/problems/course-schedule/discuss/58509/18-22-lines-C++-BFSDFS-Solutions
	 */
	public boolean canFinish_DFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<List<Integer>>(numCourses);
        for (int i = 0; i < numCourses; i++)
            graph.add(new ArrayList<Integer>());
        for (int i = 0; i < prerequisites.length; i++) {
            graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
        }

        boolean[] memo = new boolean[numCourses];
        boolean[] visited = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if(!dfs(graph, visited, i, memo)) {
                return false;    
            }
        }
        return true;
    }

    private boolean dfs(List<List<Integer>> graph, boolean[] visited, int course, boolean[] memo) {
        if (visited[course]) {
            return false;
        }
        if (memo[course]) {
            return true;
        }

        visited[course] = true;
        for (int i = 0; i < graph.get(course).size(); i++) {
            if (!dfs(graph, visited, graph.get(course).get(i), memo)) {
                return false;
            }
        }
        visited[course] = false;
        memo[course] = true;
        return true;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/course-schedule/discuss/58524/Java-DFS-and-BFS-solution/60036
	 * 
	 * Other code:
	 * leetcode.com/problems/course-schedule/discuss/58524/Java-DFS-and-BFS-solution/60030
	 * https://leetcode.com/problems/course-schedule/discuss/58730/Explained-Java-12ms-Iterative-DFS-solution-based-on-DFS-algorithm-in-CLRS
	 */
	public boolean canFinish_DFS2(int numCourses, int[][] prerequisites) {
		if (numCourses == 0 || prerequisites == null || prerequisites.length == 0)
			return true; // ??

		// create the array lists to represent the courses
		List<List<Integer>> courses = new ArrayList<List<Integer>>(numCourses);
		for (int i = 0; i < numCourses; i++) {
			courses.add(new ArrayList<Integer>());
		}

		// create the dependency graph
		for (int i = 0; i < prerequisites.length; i++) {
			courses.get(prerequisites[i][1]).add(prerequisites[i][0]);
		}

		int[] visited = new int[numCourses];

		// dfs visit each course
		for (int i = 0; i < numCourses; i++) {
			if (!dfs2(i, courses, visited))
				return false;
		}
		return true;
	}

	private boolean dfs2(int course, List<List<Integer>> courses, int[] visited) {
		visited[course] = 1; // mark it being visited

		List<Integer> eligibleCourses = courses.get(course); // get its children
		// dfs its children
		for (int i = 0; i < eligibleCourses.size(); i++) {
			int eligibleCourse = eligibleCourses.get(i).intValue();

			if (visited[eligibleCourse] == 1)
				return false; // has been visited while visiting its children - cycle !!!!
			if (visited[eligibleCourse] == 0) { // not visited
				if (!dfs2(eligibleCourse, courses, visited))
					return false;
			}

		}

		visited[course] = 2; // mark it done visiting
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/course-schedule/discuss/58775/My-Java-BFS-solution
	 * 
	 * Create a queue to store all vertices with 0 in-degree, and an array to store 
	 * each vertex's in-degree.
	 * 
	 * Then check if there is a cycle. If yes, return false, otherwise return true.
	 * 
	 * After that, select a random vertex with 0 in-degree from set. Use it to relax 
	 * other vertices, i.e. reduce other vertices' in-degree by 1 if there is an edge 
	 * coming from that vertex. Furthermore, if any vertex has 0 in-degree after relax, 
	 * add it to set.
	 * 
	 * Repeat step above until queue becomes empty.
	 * 
	 * Rf : https://leetcode.com/problems/course-schedule/discuss/58626/Java-AC-Solution-with-Top-Sort
	 */
	public boolean canFinish_Map_fromLast(int numCourses, int[][] prerequisites) {
		Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < numCourses; i++) {
			map.put(i, new ArrayList<Integer>());
		}
		
		int[] indegree = new int[numCourses];
		for (int i = 0; i < prerequisites.length; i++) {
			map.get(prerequisites[i][0]).add(prerequisites[i][1]);
			indegree[prerequisites[i][1]]++;
		}
		
		Queue<Integer> queue = new LinkedList<Integer>();
		for (int i = 0; i < numCourses; i++) {
			if (indegree[i] == 0) {
				queue.offer(i);
			}
		}
		
		int count = numCourses;		
		while (!queue.isEmpty()) {
			int current = queue.poll();
			for (int i : map.get(current)) {
				if (--indegree[i] == 0) {
					queue.offer(i);
				}
			}
			count--;
		}
		return count == 0;
	}
	
	// https://leetcode.com/problems/course-schedule/discuss/58523/JAVA-Easy-Version-To-UnderStand!!!!!!!!!!!!!!!!!
	public boolean canFinish_BFS_fromLast(int numCourses, int[][] prerequisites) {
		if (numCourses <= 0)
			return false;
		
		Queue<Integer> queue = new LinkedList<>();
		int[] inDegree = new int[numCourses];
		for (int i = 0; i < prerequisites.length; i++) {
			inDegree[prerequisites[i][1]]++;
		}
		
		for (int i = 0; i < inDegree.length; i++) {
			if (inDegree[i] == 0)
				queue.offer(i);
		}
		
		while (!queue.isEmpty()) {
			int x = queue.poll();
			for (int i = 0; i < prerequisites.length; i++) {
				if (x == prerequisites[i][0]) {
					inDegree[prerequisites[i][1]]--;
					if (inDegree[prerequisites[i][1]] == 0)
						queue.offer(prerequisites[i][1]);
				}
			}
		}
		
		for (int i = 0; i < inDegree.length; i++) {
			if (inDegree[i] != 0)
				return false;
		}
		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/course-schedule/discuss/58521/O(E)-Solution-DFS-based
	 * 
	 * It is a dfs solution that tries to find a cycle. The code doesn't even use the 
	 * first parameter numCources. The graph is stored in a map, which allows to 
	 * completely ignore courses that no other courses depend on. While navigating the 
	 * graph (using dfs) the dependencies get removed one by one until the graph is 
	 * empty or a cycle was found.
	 */
	public boolean canFinish_removeMap(int numCourses, int[][] prerequisites) {
		Map<Integer, Set<Integer>> map = new HashMap<>();
		for (int[] p : prerequisites) {
			if (!map.containsKey(p[0])) {
				map.put(p[0], new HashSet<>());
			}
			map.get(p[0]).add(p[1]);
		}
		
		while (!map.isEmpty()) {
			if (dfs_removeMap(map.keySet().iterator().next(), map, new HashSet<>()))
				return false;
		}
		return true;
	}

	private boolean dfs_removeMap(int start, Map<Integer, Set<Integer>> map, Set<Integer> visited) {
		if (visited.contains(start))
			return true;
		if (!map.containsKey(start))
			return false;
		
		visited.add(start);
		
		while (!map.get(start).isEmpty()) {
			int n = map.get(start).iterator().next();
			if (dfs_removeMap(n, map, visited))
				return true;
			
			map.get(start).remove(n);
		}
		
		map.remove(start);
		visited.remove(start);
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/course-schedule/discuss/58692/5ms-98.6-Java-DFS-Topology-Sort-Solution
	 * leetcode.com/problems/course-schedule/discuss/58524/Java-DFS-and-BFS-solution/60027
	 */
	
	// https://leetcode.com/problems/course-schedule/discuss/58713/OO-easy-to-read-java-solution

}
