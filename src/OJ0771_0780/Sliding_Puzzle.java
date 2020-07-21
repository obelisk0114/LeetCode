package OJ0771_0780;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Sliding_Puzzle {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/sliding-puzzle/discuss/222556/Easy-understandable-JavaPython3-Solution-also-easy-to-extend-to-M*N
	 * 
	 * Consider each state in the board as a graph node, we just need to find out the 
	 * min distance between start node and final target node "123450". Since it's a 
	 * single point to single point questions, Dijkstra is not needed here. We can 
	 * simply use BFS, and also count the level we passed. Every time we swap 0 
	 * position in the String to find the next state. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/sliding-puzzle/discuss/146652/Java-8ms-BFS-with-algorithm-explained
	 * 
	 * Other code:
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113620/JavaPython-3-BFS-clean-codes-w-comment-Time-and-space%3A-O(4320).
	 * https://leetcode.com/problems/sliding-puzzle/discuss/582656/Java-BFS-beat-95
	 * https://leetcode.com/problems/sliding-puzzle/discuss/133498/Readable-java-solution
	 * (DFS can work) https://leetcode.com/problems/sliding-puzzle/discuss/673710/DFS-will-not-always-work-because-of-this-reason.
	 */
	public int slidingPuzzle_BFS(int[][] board) {
		String target = "123450";
		int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		
		int M = 2, N = 3;
		StringBuilder sb = new StringBuilder();
		for (int[] row : board) {
			for (int i : row)
				sb.append(i);
		}
		String start = sb.toString();
		
		// Can also use this line to get the initial state string
		// String start = Arrays.deepToString(board).replaceAll("\\[|\\]|,|\\s", ""); // e.g., [[1,2,3],[4,0,5]] -> "123405"
		
		Queue<String> queue = new LinkedList<>();
		Set<String> visited = new HashSet<>();
		queue.add(start);
		visited.add(start);
		
		int steps = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int k = 0; k < size; k++) {
				String cur = queue.poll();
				if (cur.equals(target))
					return steps;
				
				int i = cur.indexOf("0");
				int m = i / N, n = i % N;
				
				for (int[] move : moves) {
					int mt = m + move[0], nt = n + move[1];
					if (mt < 0 || mt >= M || nt < 0 || nt >= N)
						continue;
					
					String nxt = swap_BFS(cur, i, mt * N + nt);
					if (visited.contains(nxt))
						continue;
					
					queue.add(nxt);
					visited.add(nxt);
				}
			}
			steps++;
		}
		return -1;
	}

	private String swap_BFS(String s, int i, int j) {
		char[] arr = s.toCharArray();
		char tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
		return new String(arr);
	}
	
	/*
	 * ----- READ -----
	 * The following function and class are modified by myself.
	 * 
	 * Selects the path that minimizes: f(n) = g(n) + h(n)
	 * 
	 * where n is the next node on the path, g(n) is the cost from the start to n,
	 * h(n) is a heuristic function that estimates the cost of the cheapest path from 
	 * n to the goal.
	 * 
	 * Using Manhattan distance in h(n)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search/114616
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search
	 * https://leetcode.com/problems/sliding-puzzle/discuss/133498/Readable-java-solution
	 */
	public int slidingPuzzle_pq_self(int[][] puzzle) {
		int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		PriorityQueue<State_pq_self> pq = new PriorityQueue<>();
		Set<State_pq_self> been = new HashSet<>();
		
		State_pq_self start = new State_pq_self(puzzle, 0);
		
		pq.offer(start);
		been.add(start);
		while (!pq.isEmpty()) {
			State_pq_self pop = pq.poll();
			if (pop.isGoal())
				return pop.taken;
			
			for (int[] move : moves) {
				int nextBlankI = pop.blankI + move[0];
				int nextBlankJ = pop.blankJ + move[1];
				
				if (nextBlankI >= 0 && nextBlankI < puzzle.length 
						&& nextBlankJ >= 0 && nextBlankJ < puzzle[0].length) {
					
					State_pq_self newState = 
							new State_pq_self(pop.puzzle, pop.taken + 1);
					
					newState.swap(nextBlankI, nextBlankJ);
					
					if (!been.contains(newState)) {
						been.add(newState);
						pq.add(newState);
					}
				}				
			}
		}
		return -1;
	}

	private class State_pq_self implements Comparable<State_pq_self> {
		int[][] puzzle;
		int taken;
		int blankI, blankJ;

		public State_pq_self(int[][] puzzle, int taken) {
			this.puzzle = new int[puzzle.length][puzzle[0].length];
			for (int i = 0; i < puzzle.length; i++) {
				for (int j = 0; j < puzzle[0].length; j++) {
					if (puzzle[i][j] == 0) {
						this.blankI = i;
						this.blankJ = j;
					}
					
					this.puzzle[i][j] = puzzle[i][j];
				}
			}
			
			this.taken = taken;
		}

		public void swap(int i, int j) {
			int temp = puzzle[i][j];
			puzzle[i][j] = puzzle[blankI][blankJ];
			puzzle[blankI][blankJ] = temp;
			
			blankI = i;
			blankJ = j;
		}

		public int distance() {
			int res = 0;
			for (int i = 0; i < puzzle.length; i++) {
				for (int j = 0; j < puzzle[i].length; j++) {
					if (puzzle[i][j] == 0)
						continue;
					
					int val = puzzle[i][j] - 1;
					int si = val / puzzle[i].length;
					int sj = val % puzzle[i].length;
					
					res += Math.abs(si - i) + Math.abs(sj - j);
				}
			}
			return res;
		}

		public boolean isGoal() {
			return distance() == 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;

			State_pq_self that = (State_pq_self) obj;
			return Arrays.deepEquals(this.puzzle, that.puzzle);
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(this.puzzle);
		}

		@Override
		public int compareTo(State_pq_self that) {
			return this.distance() + this.taken - that.distance() - that.taken;
		}
		
		@Override
        public String toString() {
            return Arrays.deepToString(puzzle);
        }
	}
	
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search/114616
	 * 
	 * Use A* search to solve n-puzzle problem, and using Manhattan distance will make 
	 * the heuristic consistent, in which case, we don't have to expand nodes that are 
	 * already expanded and moved to the closed state.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search
	 */
	public int slidingPuzzle_PriorityQueue(int[][] a) {
		int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		PriorityQueue<State_PriorityQueue> pq = new PriorityQueue<>();
		Set<State_PriorityQueue> been = new HashSet<>();
		
		int zi = 0, zj = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				if (a[i][j] == 0) {
					zi = i;
					zj = j;
					break;
				}
			}
		}
		State_PriorityQueue start = new State_PriorityQueue(a, 0, zi, zj);
		
		pq.add(start);
		been.add(start);
		while (!pq.isEmpty()) {
			State_PriorityQueue pop = pq.remove();
			if (pop.isGoal())
				return pop.taken;
			
			for (int[] move : moves) {
				int nzi = pop.zi + move[0];
				int nzj = pop.zj + move[1];
				
				State_PriorityQueue newState = pop.swap(nzi, nzj);
				if (newState == null || been.contains(newState))
					continue;
				
				been.add(newState);
				pq.add(newState);
			}
		}
		return -1;
	}

	private class State_PriorityQueue implements Comparable<State_PriorityQueue> {
		int[][] a;
		int taken;
		int zi, zj;

		public State_PriorityQueue(int[][] a, int taken, int zi, int zj) {
			this.a = new int[2][3];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 3; j++)
					this.a[i][j] = a[i][j];
			}
			
			this.taken = taken;
			this.zi = zi;
			this.zj = zj;
		}

		public State_PriorityQueue swap(int i, int j) {
			if (i < 0 || i >= 2 || j < 0 || j >= 3)
				return null;
			
			State_PriorityQueue res = 
					new State_PriorityQueue(this.a, this.taken + 1, i, j);
			
			int temp = res.a[i][j];
			res.a[i][j] = res.a[zi][zj];
			res.a[zi][zj] = temp;
			
			return res;
		}

		public int distance() {
			int res = 0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 3; j++) {
					if (a[i][j] == 0)
						continue;
					
					int val = a[i][j] - 1;
					int si = val / 3;
					int sj = val % 3;
					
					res += Math.abs(si - i) + Math.abs(sj - j);
				}
			}
			return res;
		}

		public boolean isGoal() {
			return distance() == 0;
		}

		@Override
		public boolean equals(Object obj) {
			State_PriorityQueue that = (State_PriorityQueue) obj;
			return Arrays.deepEquals(this.a, that.a);
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(a);
		}

		@Override
		public int compareTo(State_PriorityQueue that) {
			return this.distance() + taken - that.distance() - that.taken;
		}
	}
	
	/*
	 * The following 2 variables and 4 functions are from this link.
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113615/Java-Intuitive-DFS%2BBacktracking
	 * 
	 * Rf :
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113615/Java-Intuitive-DFS+Backtracking/499165
	 */
	Map<Integer, Integer> map_dfs = new HashMap<>();
	int min_move_dfs = Integer.MAX_VALUE;

	public int slidingPuzzle_dfs(int[][] board) {
		map_dfs.put(123450, 0);
		int[] zero = new int[2];
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == 0) {
					zero[0] = i;
					zero[1] = j;
					break;
				}
			}
		}
		
		helper_dfs(board, zero[0], zero[1], 0);
		return min_move_dfs == Integer.MAX_VALUE ? -1 : min_move_dfs;
	}

	private void helper_dfs(int[][] board, int x, int y, int move) {
		if (move > min_move_dfs)
			return;
		
		int code = encode_dfs(board);
		if (code == 123450) {
			min_move_dfs = move;
			return;
		}
		if (map_dfs.containsKey(code)) {
			// We want to get optimal solution, so only continue when move is less.
			if (move > map_dfs.get(code))
				return;
		}
		
		map_dfs.put(code, move);
		
		int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		for (int[] dir : dirs) {
			int nx = x + dir[0], ny = y + dir[1];
			if (nx >= 0 && nx < 2 && ny >= 0 && ny < 3) {
				swap_dfs(board, x, y, nx, ny);
				helper_dfs(board, nx, ny, move + 1);
				swap_dfs(board, nx, ny, x, y);
			}
		}
	}

	private void swap_dfs(int[][] board, int i, int j, int ii, int jj) {
		int temp = board[i][j];
		board[i][j] = board[ii][jj];
		board[ii][jj] = temp;
	}

	private int encode_dfs(int[][] board) {
		int code = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				code *= 10;
				code += board[i][j];
			}
		}
		return code;
	}
	
	/*
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113620/JavaPython-3-BFS-clean-codes-w-comment-Time-and-space:-O(4320)./158249
	 * 
	 * two-end BFS
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder/discuss/40711/Two-end-BFS-in-Java-31ms.
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113620/JavaPython-3-BFS-clean-codes-w-comment-Time-and-space:-O(4320)./249569
	 */
	public int slidingPuzzle_two_end(int[][] board) {
	    Set<String> visited = new HashSet<>();
		int[] dir = { 1, -1, 3, -3 };

	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[0].length; j++) {
	            sb.append(board[i][j]);
	        }
	    }
	    
	    Set<String> begin = new HashSet<>();
	    Set<String> end = new HashSet<>();
	    begin.add(sb.toString());
	    end.add("123450");
	    
	    if (begin.contains("123450")) {
	    	return 0;
	    }
	    
	    int count = 0;
	    while (begin.size() > 0) {
	        if (begin.size() > end.size()) {
	            Set<String> temp = begin;
	            begin = end;
	            end = temp;
	        }
	        
	        Set<String> set = new HashSet<>();
	        for (String s : begin) {
	            int i = s.indexOf('0');
	            for (int d : dir) {
	                char[] ch = s.toCharArray();
	                
	                int j = i + d;
	                if (j >= ch.length || j < 0 
	                		|| (i == 2 && j == 3) || (i == 3 && j == 2)) {
	                	
	                	continue;
	                }
	                
	                char temp = ch[i];
	                ch[i] = ch[j];
	                ch[j] = temp;
	                String newBoard = new String(ch);
	                
	                if (end.contains(newBoard)) {	                	
	                	return count + 1;
	                }
	                if (visited.add(newBoard)) {	                	
	                	set.add(newBoard);
	                }
	            }
	        }
	        count++;
	        begin = set;
	    }
	    
	    return -1;
	}
	
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search/114616
	 * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search
	 */
	public int slidingPuzzle_pq_self2(int[][] puzzle) {
		int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                int hA = a[a.length - 1];
                int hB = b[b.length - 1];
                
                for (int i = 0; i < a.length - 1; i++) {
                    if (a[i] != 0) {
                        int x = Math.abs(i % puzzle[0].length - 
                                         (a[i] - 1) % puzzle[0].length);
                        int y = Math.abs(i / puzzle[0].length - 
                                         (a[i] - 1) / puzzle[0].length);
                        hA = hA + x + y;
                    }
                }
                for (int i = 0; i < b.length - 1; i++) {
                    if (b[i] != 0) {
                        int x = Math.abs(i % puzzle[0].length - 
                                         (b[i] - 1) % puzzle[0].length);
                        int y = Math.abs(i / puzzle[0].length - 
                                         (b[i] - 1) / puzzle[0].length);
                        hB = hB + x + y;
                    }
                }
                return hA - hB;
            }
        });
		Set<List<Integer>> been = new HashSet<>();
        
        int[] start = new int[puzzle.length * puzzle[0].length + 1];
        start[start.length - 1] = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                start[i * puzzle[i].length + j] = puzzle[i][j];
            }
        }
        
		pq.add(start);
        
        List<Integer> startList = new ArrayList<>();
        for (int i = 0; i < start.length - 1; i++) {
            startList.add(start[i]);
        }
		been.add(startList);
        
        //int phase = 0;
		while (!pq.isEmpty()) {
			int[] pop = pq.remove();
            //System.out.println(phase++);
            /*
            if (phase++ > 360) {
                return -1;
            }
            */
            
            int goal = 0;
            int blankX = -1;
            int blankY = -1;
            for (int i = 0; i < pop.length - 1; i++) {
                if (pop[i] != 0) {
                    int x = Math.abs(i % puzzle[0].length - 
                                     (pop[i] - 1) % puzzle[0].length);
                    int y = Math.abs(i / puzzle[0].length - 
                                     (pop[i] - 1) / puzzle[0].length);
                    goal = goal + x + y;
                }
                else {
                    blankX = i % puzzle[0].length;
                    blankY = i / puzzle[0].length;
                }
            }
			if (goal == 0) {
                return pop[pop.length - 1];
            }
			
			for (int[] move : moves) {
				int nextX = blankX + move[0];
				int nextY = blankY + move[1];
				
				if (nextX >= 0 && nextY >= 0 
						&& nextX < puzzle[0].length && nextY < puzzle.length) {
					
                    int[] next = new int[pop.length];
                    for (int i = 0; i < pop.length; i++) {
                        next[i] = pop[i];
                    }
                    swap_pq_self(next, blankY * puzzle[0].length + blankX, 
                    		nextY * puzzle[0].length + nextX);
                    
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < next.length - 1; i++) {
                        list.add(next[i]);
                    }
                    if (!been.contains(list)) {
                        //System.out.println(Arrays.toString(next));
                        //System.out.println(list.toString());
                        been.add(list);
                        pq.add(next);
                    }
                }
			}
		}
		return -1;
	}

	private void swap_pq_self(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        
        a[a.length - 1] = a[a.length - 1] + 1;
    }
	
	// https://leetcode.com/problems/sliding-puzzle/discuss/536729/Java-Hardcoded-minimum-number-of-moves-faster-than-100
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/sliding-puzzle/discuss/269125/Python-Short-BFS-and-Long-A*
     * https://leetcode.com/problems/sliding-puzzle/discuss/113614/Simple-Python-solution-using-A*-search
     * https://leetcode.com/problems/sliding-puzzle/discuss/117797/Simple-fast-Python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/sliding-puzzle/discuss/113694/C%2B%2B-BFS-with-explanation-and-example-(-EASY-to-understand-)
     * https://leetcode.com/problems/sliding-puzzle/discuss/682629/C%2B%2B-BFS-template-based-solution.
     * https://leetcode.com/problems/sliding-puzzle/discuss/113613/C%2B%2B-9-lines-DFS-and-BFS
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/sliding-puzzle/discuss/140657/JavaScript-DFS-solution
	 */

}
