package OJ1191_1200;

import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Minimum_Knight_Moves {
	/*
	 * Modified by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/392053/Here-is-how-I-get-the-formula-(with-graphs)/901985
	 */
	public int minKnightMoves_bidirectional_modified(int x, int y) {
		Deque<int[]> startQueue = new ArrayDeque<>();
		Deque<int[]> endQueue = new ArrayDeque<>();

		startQueue.add(new int[] { 0, 0 });
		endQueue.add(new int[] { x, y });

		Set<String> startVisited = new HashSet<>();
		Set<String> endVisited = new HashSet<>();

		startVisited.add("0,0");
		endVisited.add(x + "," + y);

		int[][] dirs = { { 1, 2 }, { 2, 1 }, { -1, 2 }, { 2, -1 }, 
				{ 1, -2 }, { -2, 1 }, { -1, -2 }, { -2, -1 } };
		
		int count = 0;

		while (!startQueue.isEmpty() && !endQueue.isEmpty()) {
			if (startQueue.size() > endQueue.size()) {
				Deque<int[]> tempQueue = startQueue;
				startQueue = endQueue;
				endQueue = tempQueue;

				Set<String> tempVisited = startVisited;
				startVisited = endVisited;
				endVisited = tempVisited;
			}

			int size = startQueue.size();
			for (int i = 0; i < size; i++) {
				int[] point = startQueue.pollFirst();
				if (endVisited.contains(point[0] + "," + point[1])) {
					return count;
				}

				for (int[] dir : dirs) {
					int m = point[0] + dir[0];
					int n = point[1] + dir[1];
					String next = m + "," + n;

					if (!startVisited.contains(next)) {
						startQueue.add(new int[] { m, n });
						startVisited.add(next);
					}
				}
			}
			
			count++;
		}
		return count;
	}

	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution
	 * 
	 * The key idea is that the problem is symmetric in all 4 quadrants, so just 
	 * translate (x,y) into the first quadrant and carry out the search inside the 
	 * first quadrant. The hardest part is realizing that you might need to go 
	 * outside the first quadrant a little in order to reach some of the (x,y)'s 
	 * closer to the origin. Specifically, finding the optimal answer for 
	 * coordinates: (0,0) (1,1) (2,0) (0,2). 
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * The coordinates in general to compute the knight moves are: (x-2, y-1) 
	 * (x-2, y+1), (x-1, y-2) ... where for all x,y>=2 the next "move" will always 
	 * be >=0 (ie in the first quadrant). Only for x=1/y=1, the next move may fall in 
	 * the negative quad example (x-2,y-1) or (x-1, y-2), and hence x=-1 y=-1 
	 * boundary is considered.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * The key thing to note here is
	 * x = Math.abs(x);
	 * y = Math.abs(y);
	 * 
	 * Here we are forcing the original co-ordinates to be in 1st Quadrant only. 
	 * ( since we can use symmetry )
	 * 
	 * you cannot reach from 0,0 to 1,1 using only 1st quadrant. hence we allow 
	 * x >=-1 y>=-1 instead of x>=0, y>=0 limit
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * you are currently at (-2, -2), then you will make neither its next step's 
	 * index be at least one positive value
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * since the adjusted target is in the first quadrant, we'd like to explore 
	 * towards that direction rather than the opposite. So whenever there's a step 
	 * that lies <= (-2, -2), we'd like to stop exploring that direction to save time.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/639069
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/947138/Python-3-or-BFS-DFS-Math-or-Explanation
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/464187
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/831951
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/500837
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/498906
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/666528
	 */
	// no need to have (-1, -2) and (-2, -1) since it only goes 1 direction
	private final int[][] DIRECTIONS_bfs = { { 2, 1 }, { 1, 2 }, { -1, 2 }, 
			{ -2, 1 }, { -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 } };

	public int minKnightMoves_bfs(int x, int y) {
		x = Math.abs(x);
		y = Math.abs(y);

		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] { 0, 0 });

		Set<String> visited = new HashSet<>();
		visited.add("0,0");

		int result = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				int[] cur = queue.remove();
				int curX = cur[0];
				int curY = cur[1];
				
				if (curX == x && curY == y) {
					return result;
				}

				for (int[] d : DIRECTIONS_bfs) {
					int newX = curX + d[0];
					int newY = curY + d[1];
					
					if (!visited.contains(newX + "," + newY) 
							&& newX >= -1 && newY >= -1) {
						
						queue.add(new int[] { newX, newY });
						visited.add(newX + "," + newY);
					}
				}
			}
			result++;
		}
		return -1;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/solution/
	 * Approach 3: DFS (Depth-First Search) with Memoization
	 * 
	 * We claim that the target (x,y), its horizontally, vertically, and diagonally 
	 * symmetric points (i.e. (x, -y), (-x, y), (-x, -y)) share the same answer as 
	 * the target point.
	 * Because the symmetry of the board (i.e. from -infinity to +infinity) and the 
	 * symmetry of the allowed movements.
	 * 
	 * we can focus on the first quadrant of the coordinate plane where both x and y 
	 * are positive. Any target that is outside of the first quadrant, can be 
	 * shifted to its symmetric point in the first quadrant by taking the absolute 
	 * value of each coordinate, i.e. (|x|, |y|).
	 * 
	 * At the beginning of the DFS as well as during the process of DFS, we will 
	 * always shift the exploration to the first quadrant.
	 * 
	 * Before we reach the immediate neighborhood of the origin, we only need to 
	 * explore the two left-down directions (with offsets of (-1, -2) and (-2, -1)), 
	 * since the rest of the directions will deviate further away from the origin.
	 * 
	 * The immediate neighborhood of the origin, refers to the points of where the 
	 * sum of coordinates is less than or equal to 2, i.e. x + y <= 2. In order to 
	 * reach an immediate neighbor point from the origin, we need to do a bit of 
	 * zigzag. Any immediate neighbors with (x + y = 2), takes exactly 2 steps to 
	 * reach when starting from the origin.
	 * 
	 * We start from the target and walk backwards to reach the origin. Also, 
	 * instead of exploring all 8 directions, we only need to explore the two 
	 * left-down directions as we discussed before.
	 * 
	 * dfs(x,y) = min(dfs(|x-2|, |y-1|), dfs(|x-1|, |y-2|)) + 1
	 * 
	 * At each step of the backward exploration process, by only exploring the 
	 * left-down directions we can obtain the shortest path.
	 * 
	 * There are in general two base cases:
	 * 1. x = 0, y = 0, when we reach the origin, no further steps are required to 
	 *    reach our goal, i.e. dfs(x, y) = 0.
	 * 2. x + y = 2, when we are at a immediate neighbor as we discussed before, it 
	 *    takes two more steps to reach our goal, i.e. dfs(x, y) = 2.
	 * 
	 * x + y = 1, e.g. x=1, y=0 will be reduced down to our base case 2), 
	 * i.e. |x-1| + |y-2| = 2.
	 * 
	 * The above form of recursion with memoization is also known as Top-Down 
	 * Dynamic Programming, where we work out the solutions from top to down (base 
	 * cases), and we reuse the intermediate results (with memoization) to speed up 
	 * the calculation.
	 */
	private Map<String, Integer> memo_dfs2 = new HashMap<>();

    private int dfs_dfs2(int x, int y) {
        String key = x + "," + y;
        if (memo_dfs2.containsKey(key)) {
            return memo_dfs2.get(key);
        }

        if (x + y == 0) {
            return 0;
        } 
        else if (x + y == 2) {
            return 2;
        } 
        else {
            Integer ret = Math.min(dfs_dfs2(Math.abs(x - 1), Math.abs(y - 2)),
                    dfs_dfs2(Math.abs(x - 2), Math.abs(y - 1))) + 1;
            
            memo_dfs2.put(key, ret);
            return ret;
        }
    }

    public int minKnightMoves_dfs2(int x, int y) {
        return dfs_dfs2(Math.abs(x), Math.abs(y));
    }
	
	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/621922
	 * 
	 * On the right/top side, we also limit the movement by max 2 blocks out of the 
	 * target. I think the performance gain is likely because of this reduced board 
	 * size.
	 * Because I am using an array instead of a hashmap, we have to expand the board 
	 * by 2 blocks at each side, from (0,0)->(x,y) to (-2, -2)->(x+2, y+2). Since 
	 * array doesn't allow negative indices, I transform the expanded board to 
	 * (0,0)->(x+4, y+4).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/634494
	 */
	public int minKnightMoves_bfs3(int x, int y) {
		final int[][] MOVE = { { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, 
				{ -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 } };

		x = Math.abs(x);
		y = Math.abs(y);

		final int sizeX = x + 4, sizeY = y + 4;
		boolean[][] board = new boolean[sizeX][sizeY];

		ArrayDeque<int[]> q = new ArrayDeque<>();

		q.add(new int[] { 0, 0, 0 });
		board[2][2] = true;

		while (!q.isEmpty()) {
			int[] n = q.poll();
			int count = n[2];

			if (n[0] == x && n[1] == y)
				return count;

			++count;

			for (int[] move : MOVE) {
				int i = n[0] + move[0] + 2, j = n[1] + move[1] + 2;
				
				if (i < 0 || j < 0 || i >= sizeX || j >= sizeY || board[i][j])
					continue;

				board[i][j] = true;
				q.add(new int[] { i - 2, j - 2, count });
			}
		}
		return -1;
	}

	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/401580/Clean-Java-BFS-solution/647045
	 * 
	 * The only time (newX >= -1 && newY >= -1) is necessary is if the final 
	 * destination is (1,1). We can also use (newX >= 0 && newY >= 0) but then we 
	 * need to handle the destination (1,1) as a special case at the beginning of the 
	 * code
	 */
	public int minKnightMoves_bfs2(int x, int y) {
		// If we can reach x,y in one quadrant then we can do it for all others
		// in the same number of moves too.
		x = Math.abs(x);
		y = Math.abs(y);

		// Special case for (1,1) because there is a more efficient way to reach it 
		// if we allow negative positions.
		if (x == 1 && y == 1)
			return 2;
		
		int[][] dirs = { { -2, -1 }, { -2, 1 }, { -1, 2 }, { 1, 2 }, 
				{ 2, 1 }, { 2, -1 }, { 1, -2 }, { -1, -2 } };
		
		Deque<int[]> queue = new ArrayDeque<>();
		Set<String> seen = new HashSet<>();
		
		queue.addLast(new int[] { 0, 0 });
		seen.add("0,0");
		
		int result = 0;
		while (queue.size() > 0) {
			// Number of unique positions reachable with exactly result moves.
			int size = queue.size();
			for (int i = 0; i < size; ++i) {
				int[] currPos = queue.removeFirst();
				
				if (currPos[0] == x && currPos[1] == y)
					return result;
				
				for (int[] currDir : dirs) {
					int newX = currPos[0] + currDir[0];
					int newY = currPos[1] + currDir[1];
					
					if (newX >= 0 && newY >= 0 && !seen.contains(newX + "," + newY)) {
						seen.add(newX + "," + newY);
						queue.addLast(new int[] { newX, newY });
					}
				}
			}
			result++;
		}
		
		// Can't be reached on an infinite chess board.
		return -1;
	}

	/*
	 * Modified by myself
	 * 
	 * The basic idea is to make sure the total number of moves required always 
	 * decrease if we move along the long edge.
	 * 
	 * I found the boundary 4 by drawing a graph, then solve the remaining number of 
	 * steps by BFS.
	 * 
	 * 若用 3, (5, 5) 會出錯
	 * 
	 *            邊界用 4  --> 從 (3, 4) 到 (0, 0) 需要 3 步
	 * (5, 5) -> (3, 4) -|
	 *            邊界用 3  --> (2, 2) -> 從 (2, 2) 到 (0, 0) 需要 4 步
	 * ------------------------------------------------------------------------
	 * 
	 * We just take greedy steps to try to move the x, y goal as close to the origin 
	 * as possible, then we can do a BFS search. Also, for x > 4 or y > 4, we can 
	 * always increase '4' to a larger number and it still works
	 * 
	 * ------------------------------------------------------------------------
	 * 
	 * In greedy part we only use absolute value of x and y.
	 * "y -= 1 if y >= 1 else -1" is equivalent to "y = abs(y - 1)"
	 * 
	 * 因為迴圈條件 x > 4 || y > 4, 所以 y 不會小於 0
	 * 若出現 (4, -1), 表示前一次是 (6, 0), 然而 (6, 0) 的下一次是 (4, 1)
	 * 其他情況同理 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/540658/Python-greedy-%2B-bfs-solution-12ms-beats-100
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/540658/Python-greedy-+-bfs-solution-12ms-beats-100/720992
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/540658/Python-greedy-+-bfs-solution-12ms-beats-100/534278
	 */
	public int minKnightMoves_greedy(int x, int y) {
        x = Math.abs(x);
        y = Math.abs(y);
        
        int res = 0;
        
        // greedy
        while (x > 4 || y > 4) {
            res++;
            
            if (x >= y) {
                x -= 2;
                
                if (y >= 1) {
                    y -= 1;
                }
                else {
                    y -= -1;
                }
            }
            else {
                if (x >= 1) {
                    x -= 1;
                }
                else {
                    x -= -1;
                }
                
                y -= 2;
            }
        }
        
        // bfs        
		int[][] moves = { { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, 
				{ -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 } };
		
		Deque<int[]> queue = new ArrayDeque<>();
		queue.offerLast(new int[] { 0, 0, 0 });

		while (!queue.isEmpty()) {
            int[] cur = queue.pollFirst();
            int i = cur[0];
            int j = cur[1];
            int steps = cur[2];
            
            if (i == x && j == y) {
                return res + steps;
            }
            
            for (int[] move : moves) {
                int di = move[0];
                int dj = move[1];
                
                // move towards (x, y) at least in one direction
                if ((x - i) * di > 0 || (y - j) * dj > 0) {
                    queue.offerLast(new int[] { i + di, j + dj, steps + 1 });
                }
            }
        }
        
        return -1;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/388995/DFS-with-Memorization-beat-82-and-100
	 * 
	 * from the below pattern and state transition equation, there are only 4 base 
	 * cases.
	 * 5 4 5 4 5 4 5 6
	 * 4 3 4 3 4 5 4
	 * 3 4 3 4 3 4
	 * 2 3 2 3 4
	 * 3 2 3 2
	 * 2 1 4
	 * 3 2
	 * 0
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/388995/DFS-with-Memorization-beat-82-and-100/353177
	 */
	public int minKnightMoves_dfs(int x, int y) {
		Map<String, Integer> map = new HashMap<>();

		// base case
		map.put("0,0", 0);
		map.put("1,0", 3);
		map.put("1,1", 2);
		map.put("2,0", 2);

		return helper2_dfs(x, y, map);
	}

	private int helper2_dfs(int x, int y, Map<String, Integer> map) {
		// Symmetrical of axis
		x = Math.abs(x);
		y = Math.abs(y);

		// Symmetrical of diagonal
		// 可以省略
		if (x < y) {
			int temp = x;
			x = y;
			y = temp;
		}

		String s = x + "," + y;
		if (map.containsKey(s))
			return map.get(s);
		
		int temp = Math.min(helper2_dfs(x - 2, y - 1, map), 
				helper2_dfs(x - 1, y - 2, map)) + 1;
		
		map.put(s, temp);
		return temp;
	}
	
	/*
	 * The following 2 variables and function are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/387071/JavaPython-3-BFS-code-using-symmetry
	 * 
	 * Encode the (x, y) to x * 601 * y. Do BFS in range: x >= -2 && y >= -2.
	 * 
	 * you assign the numbers for the cells in N * N grid:
	 * row 0: 0 * N + 0, 0 * N + 1, 0 * N + 2. ..., 0 * N + (N - 1)
	 * row 1: 1 * N + 0, 1 * N + 1, 1 * N + 2. ..., 1 * N + (N - 1)
	 * ...
	 * row N - 1: (N - 1) * N + 0, (N - 1) * N + 1, ..., (N - 1) * N + (N - 1)
	 * 
	 * The number must be greater than 300 to avoid Hash Collision, and 301 is enough.
	 * 
	 * -------------------------------------------------------------------
	 * 
	 * Though the final position must be on a 300 * 300 area, it does not restrict 
	 * the intermediate positions.
	 * 
	 * Position (1, 302) and (2, 1) have the same hashing value 603.
	 * We could simply construct the hashing key with string "x_y".
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/387071/JavaPython-3-BFS-code-using-symmetry/348135
	 */
	private int[] d_multiply = { 2, 1, 2, -1, 2, -1, -2, 1, 2 }; // 8 possible moves
	private int p_multiply = 601;

	public int minKnightMoves_multiply(int x, int y) {
		
		// Use the symmetric property.
		x = Math.abs(x);
		y = Math.abs(y);
		
		Queue<Integer> q = new LinkedList<>();
		q.offer(0);
		
		Set<Integer> seen = new HashSet<>(q);
		
		for (int steps = 0; !q.isEmpty(); ++steps) {
			for (int sz = q.size(); sz > 0; --sz) {
				int i = q.peek() / p_multiply, j = q.poll() % p_multiply;
				
				if (i == x && j == y)
					return steps;
		
				for (int k = 0; k < 8; ++k) {
					int r = i + d_multiply[k], c = j + d_multiply[k + 1];
					
					if (r >= -2 && c >= -2 && seen.add(r * p_multiply + c)) {
						q.offer(r * p_multiply + c);
					}
				}
			}
		}
		return -1;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/388191/Java-BFS-beats-100-explained
	 * 
	 * We can simulate all steps because the limits for the possible x and y are low 
	 * (+-300). Work with abs(x) and abs(y) - it makes code simpler and doesn't 
	 * affect the answer - just imagine that it's a mirrored image in case of 
	 * negative x and y.
	 * 
	 * We do the BFS style - from every cell we make all possible moves checking if 
	 * we reach the target and if the cell has been visited before. If not - mark the 
	 * cell as visited, store it in the BFS queue and continue the same loop.
	 * 
	 * Because it's BFS we'll get the minimum number of moves. If we met this cell 
	 * before that it's picked up by some other previous path and we can discard this 
	 * current path.
	 * 
	 * We store possible moves in 2D array, 8 elements that store increments of x and 
	 * y coordinates. To store next cell for the BFS and visited cells we can use 
	 * encoding - just multiply x by something > 600 (from -300 to 300) and add y. 
	 * Multiplication can be replaced by bit shift - it's faster. 10 bits are 
	 * enough - it gives 1024.
	 */
	//store possible moves from one point as an array of changes in coordinates
	int[][] d_encodeBit = { { -1, -2 }, { -2, -1 }, { -2, 1 }, { -1, 2 }, 
			{ 1, 2 }, { 2, 1 }, { 2, -1 }, { 1, -2 } };

	public int minKnightMoves_encodeBit(int x, int y) {
		
		// we can invert the sign of the number - it doesn't affect the result
		x = Math.abs(x);
		y = Math.abs(y);

		// store seen cells
		Set<Integer> seen = new HashSet<>();

		// this is queue for the BFS, initialize it with 0,0 position
		Queue<Integer> q = new LinkedList<>();
		q.add(0);

		// this will store the number of moves
		int res = 0;

		// start BFS
		while (!q.isEmpty()) {
			// on each step we only poll number of cells that we have in the queue 
			// now. everything added after this will be counted at the next step
			int size = q.size();
			
			for (int i = 0; i < size; i++) {
				// get the encoded num, convert it to coordinated and check 
				// if it's our target
				int next = q.poll();
				int curX = (next >> 10), curY = next - (curX << 10);
				
				if (curX == x && curY == y)
					return res;
				
				// if not the target - make all possible moves
				for (int k = 0; k < d_encodeBit.length; k++) {
					// each next move
					int x1 = curX + d_encodeBit[k][0], y1 = curY + d_encodeBit[k][1];
					
					// encode the move to store it in the set of visited cells
					int curEnc = y1 + (x1 << 10);
					
					if (x1 >= -2 && y1 >= -2 && seen.add(curEnc)) {
						q.add(curEnc);
					}
				}
			}
			
			// after we done with all moves from all points stored in the queue 
			// at the beginning - increment the step counter
			res++;
		}
		return -1;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/392053/Here-is-how-I-get-the-formula-(with-graphs)/901985
	 * 
	 * I create a boolean[1200][1200] visited array, and put original to [600][600] 
	 * as we know the requirement is |x| + |y| <= 300, and I take 2 as the safety 
	 * factor, put center in x = 600, y = 600 which create area to fit a circle of 
	 * radius 600, which is double the requirement of 300.
	 */
	public int minKnightMoves_Bidirectional_bfs(int x, int y) {
		Queue<int[]> startQueue = new LinkedList<>();
		Queue<int[]> endQueue = new LinkedList<>();
		
		startQueue.add(new int[] { 0, 0 });
		endQueue.add(new int[] { x, y });
		
		boolean[][] startVisited = new boolean[1200][1200];
		boolean[][] endVisited = new boolean[1200][1200];
		
		startVisited[0 + 600][0 + 600] = true;
		endVisited[x + 600][y + 600] = true;
		
		int[][] dirs = { { 1, 2 }, { 2, 1 }, { -1, 2 }, { 2, -1 }, 
				{ 1, -2 }, { -2, 1 }, { -1, -2 }, { -2, -1 } };
		
		int count = 0;
		
		while (!startQueue.isEmpty() && !endQueue.isEmpty()) {
			if (startQueue.size() > endQueue.size()) {
				Queue<int[]> tempQueue = startQueue;
				startQueue = endQueue;
				endQueue = tempQueue;
				
				boolean[][] tempArr = startVisited;
				startVisited = endVisited;
				endVisited = tempArr;
			}
			
			int size = startQueue.size();
			for (int i = 0; i < size; i++) {
				int[] point = startQueue.poll();
				
				if (endVisited[point[0] + 600][point[1] + 600])
					return count;
				
				for (int[] dir : dirs) {
					int m = point[0] + dir[0];
					int n = point[1] + dir[1];
					
					if (!startVisited[m + 600][n + 600]) {
						startQueue.add(new int[] { m, n });
						startVisited[m + 600][n + 600] = true;
					}
				}
			}
			count++;
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-knight-moves/solution/
	 * Approach 2: Bidirectional BFS
	 * 
	 * To implement the bidirectional BFS algorithm, we will double the usage of the 
	 * data structures in the unidirectional BFS. Additionally, we need to make the 
	 * following adaptations:
	 * 
	 * + Instead of using the set data structure to keep track of the visited places, 
	 *   we use the map data structure, which contains not only the information of 
	 *   visited places but also the distance between each place and the starting 
	 *   point.
	 * + Instead of only storing the coordinates of the next places to be visited in 
	 *   the queue, we also store the distance between each place and the starting 
	 *   point. This way we don't need an extra variable to keep track of distance.
	 * 
	 * In theory, bidirectional BFS should be faster than the unidirectional BFS. 
	 * However, in reality, this is not the case for the Java implementation, due to 
	 * heavy usage of sophisticated data structures, which are inefficient compared 
	 * to simple arrays.
	 */
	public int minKnightMoves_Bidirectional_bfs3(int x, int y) {
        // the offsets in the eight directions
        int[][] offsets = {{1, 2}, {2, 1}, {2, -1}, {1, -2},
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};

        // data structures needed to move from the origin point
		Deque<int[]> originQueue = new LinkedList<>();
		originQueue.addLast(new int[] { 0, 0, 0 });
		Map<String, Integer> originDistance = new HashMap<>();
		originDistance.put("0,0", 0);

        // data structures needed to move from the target point
		Deque<int[]> targetQueue = new LinkedList<>();
		targetQueue.addLast(new int[] { x, y, 0 });
		Map<String, Integer> targetDistance = new HashMap<>();
		targetDistance.put(x + "," + y, 0);

		while (true) {
            // check if we reach the circle of target
			int[] origin = originQueue.removeFirst();
			String originXY = origin[0] + "," + origin[1];
			if (targetDistance.containsKey(originXY)) {
				return origin[2] + targetDistance.get(originXY);
			}

            // check if we reach the circle of origin
			int[] target = targetQueue.removeFirst();
			String targetXY = target[0] + "," + target[1];
			if (originDistance.containsKey(targetXY)) {
				return target[2] + originDistance.get(targetXY);
			}

			for (int[] offset : offsets) {
                // expand the circle of origin
				int[] nextOrigin = { origin[0] + offset[0], origin[1] + offset[1] };
				String nextOriginXY = nextOrigin[0] + "," + nextOrigin[1];
				if (!originDistance.containsKey(nextOriginXY)) {
					originQueue.addLast(new int[] { nextOrigin[0], nextOrigin[1], 
							origin[2] + 1 });
					originDistance.put(nextOriginXY, origin[2] + 1);
				}

                // expand the circle of target
				int[] nextTarget = { target[0] + offset[0], target[1] + offset[1] };
				String nextTargetXY = nextTarget[0] + "," + nextTarget[1];
				if (!targetDistance.containsKey(nextTargetXY)) {
					targetQueue.addLast(new int[] { nextTarget[0], nextTarget[1], 
							target[2] + 1 });
					targetDistance.put(nextTargetXY, target[2] + 1);
				}
            }
        }
    }
	
	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)
	 * 
	 * Applied A* algorithm in this problem. Following is my code:
	 * g(n) = step * 3 (Since each step is equal to 3-square moves)
	 * h(n) = Euclidean Distance(current, destination) < actual distance, 
	 *        so it is guaranteed to find the optimal solution.
	 * f(n) = g(n) + h(n)
	 * 
	 * g is the cost of start to current position. 
	 * h is estimation of current position to destination.
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * As long as H < actual minimum distance from current to destination it is 
	 * guaranteed to find shortest path and less than G is not necessary. Usually we 
	 * use Euclidean Distance because it is the geometry minimum distance. F will 
	 * never be higher than the actual distance of start to destination(the shortest 
	 * path), so it is guaranteed that we will find the optimal solution at first. 
	 * You can think H = 0 and F = G + 0 in Dijkstra algorithm.
	 * 
	 * ------------------------------------------------------------------------
	 * 
	 * I am trying make G and H in same scale otherwise we can not compare with them. 
	 * You can image 1 step = move right, move right, move up. There are three 
	 * movement in square-level. So everything become in the same coordinate system.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/420634
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/420738
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/420678
	 */
	public int minKnightMoves_AStar(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		
		Comparator<int[]> comp = new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return a[3] - b[3];
			}
		};
		
		HashSet<Integer> visited = new HashSet<>();
		PriorityQueue<int[]> pq = new PriorityQueue<>(comp);
		
		int[][] dirs = { { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, 
				{ -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 } };
		
		visited.add(0);
		pq.offer(new int[] { 0, 0, 0, 0 });
		
		while (!pq.isEmpty()) {
			int[] cur = pq.poll();
			int x = cur[0], y = cur[1], step = cur[2];
			
			if (x == a && y == b)
				return step;
			
			for (int[] dir : dirs) {
				int xx = x + dir[0];
				int yy = y + dir[1];
				
				int score = 3 * step 
						+ (int) Math.sqrt((a - xx) * (a - xx) + (b - yy) * (b - yy));
				
				int status = xx * 600 + yy;
				
				if (!visited.contains(status)) {
					visited.add(status);
					pq.offer(new int[] { xx, yy, step + 1, score });
				}
			}
		}
		
		return -1;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/458928
	 * 
	 * use manhattan for h
	 * 
	 * For A* it's possible to visit a node twice when the heuristic is only 
	 * admissible (but not consistent). When the heuristic is consistent, we will 
	 * only visit a node once (and hence it's useful to maintain a visited set).
	 * 
	 * It seems to have chosen a consistent heuristic because for two neighboring 
	 * nodes, the heuristic cost will be always be = 2 + 1 or 1 + 2 = 3. The "actual" 
	 * step cost is 3. This is by definition a consistent heuristic because we never 
	 * overestimate the "actual" cost between two nodes.
	 * 
	 * ------------------------------------------------------------------------
	 * 
	 * I am trying make G and H in same scale otherwise we can not compare with them. 
	 * You can image 1 step = move right, move right, move up. There are three 
	 * movement in square-level. So everything become in the same coordinate system.
	 * 
	 * Rf :
	 * https://stackoverflow.com/questions/21441662/can-astar-visit-nodes-more-than-once
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/641546
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/420738
	 */
	public int minKnightMoves_AStar2(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);

		Comparator<int[]> comp = new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return a[3] - b[3];
			}
		};

		HashSet<Integer> visited = new HashSet<>();
		PriorityQueue<int[]> pq = new PriorityQueue<>(comp);

		int[][] dirs = { { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, 
				{ -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 } };

		Map<Integer, Integer> cost = new HashMap<>();

		cost.put(0, 0);
		pq.offer(new int[] { 0, 0, 0, 0 });

		while (!pq.isEmpty()) {
			int[] cur = pq.poll();
			int x = cur[0], y = cur[1], step = cur[2];

			int ss = x * 600 + y;

			if (visited.contains(ss))
				continue;

			visited.add(ss);

			if (x == a && y == b)
				return step;
			
			for (int[] dir : dirs) {
				int xx = x + dir[0];
				int yy = y + dir[1];
				
				int score = 3 * step + Math.abs(a - xx) + Math.abs(b - yy);
				int status = xx * 600 + yy;
				
				if (!visited.contains(status) 
						&& score < cost.getOrDefault(status, 9999999)) {
					
					cost.put(status, score);
					pq.offer(new int[] { xx, yy, step + 1, score });
				}
			}
		}
		return -1;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/446194/Java-A*-Search-Solution-(No-need-for-pruning)/641602
	 * 
	 * It's much faster to add a node to visited as soon as you add it to 
	 * PriorityQueue.
	 * 
	 * naive BFS takes too long. can use A*. Intuitively makes sense to give higher 
	 * priority to nodes which are literally closer to (x,y).
	 * 
	 * In order for A* to return lowest cost path though, need at least an admissible 
	 * heuristic: h(n) = cost from n to final goal of (x,y) <= actual cost from n 
	 * to (x,y).
	 * 
	 * That is to say, the heuristic never overestimates the cost from current 
	 * position to end goal.
	 * 
	 * Another issue: how do we define "actual" cost? We could use cost = 1 for each 
	 * knight move, but it's more elegant to define "actual" cost as the literal 
	 * number of hops it takes. For example, (0,0) to (2,1) literally takes 3 hops.
	 * 
	 * Naturally, h(n) := euclidean distance. If (x,y) = (2,1), then 
	 * h((0,0)) = sqrt(4 + 1) = 2.23, which we can underestimate to 2. 
	 * Indeed, euclid distance is a good choice for admissible heuristic because no 
	 * matter which coordinate you're at, the euclid distance to (x,y) should always 
	 * be <= hops it takes to get to (x,y).
	 * 
	 * Furthermore, this heuristic is consistent too because for neighboring nodes, 
	 * the heuristic always underestimates the actual cost. Consistent heuristic 
	 * implies that never need to visit a node twice, hence it's useful to maintain a 
	 * visited set -- to skip nodes we've already visited. 
	 */
	public int minKnightMoves_AStar3(int x, int y) {
		int[][] dir = { { 1, 2 }, { 2, 1 }, { 1, -2 }, { 2, -1 }, 
				{ -1, -2 }, { -2, -1 }, { -1, 2 }, { -2, 1 } };

		PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> {
			// f(n) = g(n) + h(n)
			// g(n) := cost from start to n
			// h(n) := heuristic; cost from n to (x,y) using euclid distance
			// we can have p[2] store g(n), and p[3] store h(n)
			return (p1[2] + p1[3]) - (p2[2] + p2[3]);
		});

		pq.add(new int[] { 0, 0, 0, euclid_AStar3(0, 0, x, y) });

		HashSet<String> visited = new HashSet<>();

		while (!pq.isEmpty()) {
			int[] curr = pq.poll();
			String currs = curr[0] + "," + curr[1];

			if (curr[0] == x && curr[1] == y)
				return curr[2] / 3;

			if (visited.contains(currs))
				continue;
			
			visited.add(currs);

			// process this position, try all directions
			for (int[] d : dir) {
				int newx = curr[0] + d[0];
				int newy = curr[1] + d[1];
				
				int gn = curr[2] + 3; // g(n)
				int hn = euclid_AStar3(newx, newy, x, y);

				int[] newEntry = new int[] { newx, newy, gn, hn };
				pq.add(newEntry);
			}
		}

		return -1; // should never get here
	}

	private int euclid_AStar3(int x1, int y1, int x2, int y2) {
		int a = x1 - x2;
		int b = y1 - y2;
		return (int) Math.sqrt(a * a + b * b);
	}

	/*
	 * by myself
	 * 
	 * based on the BFS strategy.
	 * 
	 * All BFS algorithms share the usage of two important data structures: queue 
	 * and set (or map). A queue is used to maintain the order in which places are 
	 * visited, while a set/map is used to mark which places have already been 
	 * visited.
	 * 
	 * To avoid the TLE exception, we use a bitmap (i.e. a 2D array of boolean 
	 * values) instead of a HashSet. The range of the array is set according to the 
	 * constraint of the input (i.e. |x| + |y| <= 300).
	 * 
	 * ----------------------------------------------------------------------
	 * 
	 * when x = 300, y = 0, move [2,1] -> [302, 1]. 
	 * same for x = 0, y = 300, move [1, 2] -> [1, 302].
	 * So actually the range after our move for edge cases will be -302 < x < 302, 
	 * -302 < y < 302. And there are 605 points.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/solution/
	 * https://leetcode.com/problems/minimum-knight-moves/solution/938030
	 */
	public int minKnightMoves_self2(int x, int y) {
        int[][] dirs = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, 
        		{-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
        
        int offsetX = 302;
        int offsetY = 302;
        
        boolean[][] visited = new boolean[offsetX + offsetY + 1][offsetX + offsetY + 1];
        Deque<int[]> queue = new ArrayDeque<>();
        
        visited[offsetX][offsetY] = true;
        queue.offerLast(new int[] {0, 0});
        
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.pollFirst();
                int curX = cur[0];
                int curY = cur[1];
                
                if (curX == x && curY == y) {
                    return step;
                }
                
                for (int[] dir : dirs) {
                    int nextX = curX + dir[0];
                    int nextY = curY + dir[1];
                    
                    if (!visited[nextX + offsetX][nextY + offsetY]) {
                        queue.offerLast(new int[] {curX + dir[0], curY + dir[1]});
                        visited[nextX + offsetX][nextY + offsetY] = true;
                    }
                }
            }
            
            step++;
        }
        
        return -1;
    }
	
	/*
	 * by myself
	 */
	public int minKnightMoves_self(int x, int y) {
        if (x == 0 && y == 0) {
            return 0;
        }
        
        int[][] dirs = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, 
        		{-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
        
        int offsetX = 300;
        int offsetY = 300;
        
        boolean[][] visited = new boolean[offsetX + offsetY + 1][offsetX + offsetY + 1];
        Deque<int[]> queue = new ArrayDeque<>();
        
        visited[offsetX][offsetY] = true;
        queue.offerLast(new int[] {0, 0});
        
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.pollFirst();
                int curX = cur[0];
                int curY = cur[1];
                
                for (int[] dir : dirs) {
                    int nextX = curX + dir[0];
                    int nextY = curY + dir[1];
                    
                    if (nextX == x && nextY == y) {
                        return step + 1;
                    }
                    
                    if (!visited[nextX + offsetX][nextY + offsetY]) {
                        queue.offerLast(new int[] {curX + dir[0], curY + dir[1]});
                        visited[nextX + offsetX][nextY + offsetY] = true;
                    }
                }
            }
            
            step++;
        }
        
        return -1;
    }
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/392053/Here-is-how-I-get-the-formula-(with-graphs)
	 * 
	 * It is symmetric vertically, horizontally and diagonally.
	 * The blocks in Group T has values T and (T + 1) interpolated.
	 * 
	 * For a block (x, y), its value should be:
	 * 
	 * groupId + ((x + y + groupId) % 2)
	 * 
	 * In the vertical region, you move a blockward right two times to enter a new 
	 * group, so the group id should be this (notice that Group T should have blocks 
	 * with value T and (T + 1)):
	 * 
	 * groupId = (x - 1) / 2 + 1;
	 * 
	 * In the diagonal region, you move a block three times either upward or 
	 * rightward , then you enter a new block, so the group id should be:
	 * 
	 * groupId = (x + y - 2) / 3 + 1;
	 * 
	 * ----------------------------------------------------------------------
	 * 
	 * Note that the elements within a group form a checked pattern:
	 * 1. this means that if you go in the horizontal (x-) direction the parity will 
	 *    switch from x to x+1
	 * 2. similar for the vertical direction
	 * 3. from 1. and 2. (x+y) should be included in determining the parity
	 * 4. then note that the checkered pattern alternates between groupId's 
	 *    therefore add that to the parity too: (x + y + groupId)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/392053/Here-is-how-I-get-the-formula-(with-graphs)/483430
	 */
	private int[][] localRegion_formula2 = { 
			{ 0, 3, 2 }, 
			{ 3, 2, 1 }, 
			{ 2, 1, 4 } 
	};

	public int minKnightMoves_formula2(int x, int y) {
		x = Math.abs(x);
		y = Math.abs(y);
		
		if (x < y) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		
		if (x <= 2 && y <= 2)
			return localRegion_formula2[x][y];

		int groupId;
		if ((x - 3) >= (y - 3) * 2)
			groupId = (x - 1) / 2 + 1;
		else
			groupId = (x + y - 2) / 3 + 1;

		return groupId + ((x + y + groupId) % 2);
	}

	/*
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/387036/O(1)-formula.
	 * 
	 * Notice the patterns that emerge when you draw the board.
	 * The minimum number of moves needed to reach any square
	 * 
	 * 5 4 5 4 5 4 5 6
	 * 4 3 4 3 4 5 4
	 * 3 4 3 4 3 4
	 * 2 3 2 3 4
	 * 3 2 3 2
	 * 2 1 4
	 * 3 2
	 * 0
	 * 
	 * Best part is solution is symmetrical across the axes and the diagonals.
	 * 
	 * Rf :
	 * https://stackoverflow.com/questions/2339101/knights-shortest-path-on-chessboard/8778592#8778592
	 * https://math.stackexchange.com/questions/1135683/minimum-number-of-steps-for-knight-in-chess
	 * https://stackoverflow.com/a/35968663
	 * https://leetcode.com/problems/minimum-knight-moves/discuss/387036/O(1)-formula./349181
	 */
	public int minKnightMoves_formula(int x, int y) {
		// Symmetry for axes
		x = Math.abs(x);
		y = Math.abs(y);

		// Symmetry for diagonal
		if (x < y) {
			int temp = x;
			x = y;
			y = temp;
		}

		if (x == 1 && y == 0) {
			return 3;
		}
		if (x == 2 && y == 2) {
			return 4;
		}

		int delta = x - y;
		if (y > delta) {
			return (int) (delta - 2 * Math.floor((float) (delta - y) / 3));
		} 
		else {
			return (int) (delta - 2 * Math.floor((delta - y) / 4));
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/minimum-knight-moves/discuss/947138/Python-3-or-BFS-DFS-Math-or-Explanation
     * https://leetcode.com/problems/minimum-knight-moves/discuss/387120/Python3-Clear-short-and-easy-DP-solution-No-Need-for-BFS-or-math
     * https://leetcode.com/problems/minimum-knight-moves/discuss/540658/Python-greedy-%2B-bfs-solution-12ms-beats-100
     * https://leetcode.com/problems/minimum-knight-moves/discuss/760686/Clear-Explanation-of-BFS-%2B-Optimizations
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/minimum-knight-moves/discuss/387004/Everything-is-in-First-Quadrant
     * https://leetcode.com/problems/minimum-knight-moves/discuss/682850/C%2B%2B-O(1)-Formula-solution-with-plot-explanation
     */

}
