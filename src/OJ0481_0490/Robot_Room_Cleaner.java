package OJ0481_0490;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Set;
import java.util.HashSet;

/*
 * https://en.wikipedia.org/wiki/Maze_solving_algorithm#Wall_follower
 */

public class Robot_Room_Cleaner {
	/*
	 * The following 3 functions are modified by myself.
	 * 
	 * 初始位置定為 0,0
	 * x 和 y 為相對位置, 同時利用 face 來記錄 robot 面向哪個方向
	 * 每次進入一個新的區域, 我們都可以有 4 個方向來清理
	 * 因為進入這個區域的面對方向會不同, 所以用 (face + 1) % 4 來決定下次的面對方向
	 * 這個方向會影響到 nextX, nextY
	 * 
	 * The core of this problem is that we need to know the direction the robot is 
	 * facing before we move to the next cell, only then we can move to the correct 
	 * cell.
	 * 
	 *     0
	 * 3  -|-  1
	 *     2
	 * 
	 * move forward - move; orientation x
	 * move right - turnRight, move; orientation (x + 1) % 4
	 * move backward - turnRight, turnRight, move; orientation (x + 2) % 4
	 * move left - turnRight, turnRight, turnRight, move;  orientation (x + 3) % 4
	 * 
	 * Using DFS, we have to backtrack after we explore as far as possible along a 
	 * branch, i.e. robot moves backward one step while maintaining its orientation.
	 * 
	 * 清理完畢回溯後, 使用 goBack(robot) 回到前一個位置, 並且將面對方向設置和原來相同
	 * goBack(robot) 為調轉 180 度, 前進一格 (此步驟為倒退一格). 接著再轉 180 度來讓面對方向相同
	 * 
	 * 前面被擋住時, 永遠向右轉
	 * 因為清理完畢回溯回來後, 需要換另一個方向來繼續清理, 所以也向右轉
	 * 
	 * 前面被擋住時 (robot.move() = false), 先 robot.turnRight() 來右轉
	 * 下一輪時, nextFace 是用來表示面對方向, 所以要調整 nextFace 來正確表示現在面對的方向
	 * 若可以移動 (robot.move() = true), nextX 和 nextY 為新的相對座標
	 * 
	 * we want always to go clockwise (like how we defined our directions array), 
	 * this is to follow the right-hand rule, otherwise we will get lost in the maze.
	 * If you simply did something like for(int[] dir : dirs) this won't work as you 
	 * always picking the first dir from the list to go to which is Up, what you 
	 * need to do is to continue in clockwise fashion from your current direction
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/315943/How-to-tackle-this-problem-in-the-interviewJAVA
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/1028277/C%2B%2B-Algorithm-walk-through-and-code-with-detailed-comments.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/419175
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/239683
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/281911
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/292923
	 * https://leetcode.com/problems/robot-room-cleaner/solution/299272
	 */
	public void cleanRoom_self_modified(Robot robot) {
        cleanDFS_self_modified(robot, 0, 0, 0, new HashSet<>());
    }
    
    private void cleanDFS_self_modified(Robot robot, int x, int y, int face, 
    		Set<String> visited) {
    	
        String current = x + "," + y;
        if (visited.contains(current)) {
            return;
        }
        
        // clean 現在這個區域
        robot.clean();
        visited.add(current);
        
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int i = face; i < face + 4; i++) {
            int nextFace = i % 4;
            int nextX = x + dirs[nextFace][0];
            int nextY = y + dirs[nextFace][1];
            
            // 若 if condition 改成下面這樣
            // goBack_self_modified(robot) 可以移動到 for loop 後面
            //
            // if (!visited.contains(nextX + "," + nextY) && robot.move())
            if (robot.move()) {
                cleanDFS_self_modified(robot, nextX, nextY, nextFace, visited);
                goBack_self_modified(robot);
            }
            
            // 前面無路, 或是 goBack 回來 (表示前面清理完畢), 向右轉 (換方向, 繼續清理)
            robot.turnRight();
        }
    }
    
    private void goBack_self_modified(Robot robot) {
    	robot.turnRight();
    	robot.turnRight();
    	
    	robot.move();
    	
    	robot.turnRight();
    	robot.turnRight();
    }
	
	/*
	 * The following 6 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/136392/Javascript-DFS-solution/154094
	 * 
	 * 移動完後，仍然保持相同的面對方向
	 */
	private boolean moveUp_pointSame(Robot robot) {
		boolean res = robot.move();
		return res;
	}

	private boolean moveLeft_pointSame(Robot robot) {
		robot.turnLeft();
		boolean res = robot.move();
		robot.turnRight();
		return res;
	}

	private boolean moveRight_pointSame(Robot robot) {
		robot.turnRight();
		boolean res = robot.move();
		robot.turnLeft();
		return res;
	}

	private boolean moveDown_pointSame(Robot robot) {
		robot.turnLeft();
		robot.turnLeft();
		boolean res = robot.move();
		robot.turnRight();
		robot.turnRight();
		return res;
	}

	private void DFS_cleanRoom_pointSame(Robot robot, int x, int y, 
			HashSet<String> visited) {
		
		String temp = String.valueOf(x) + ',' + String.valueOf(y);
		if (visited.contains(temp)) {
			return;
		}
		
		visited.add(temp);
		robot.clean();
		
		if (moveUp_pointSame(robot)) {
			DFS_cleanRoom_pointSame(robot, x - 1, y, visited);
			moveDown_pointSame(robot);
		}
		if (moveLeft_pointSame(robot)) {
			DFS_cleanRoom_pointSame(robot, x, y - 1, visited);
			moveRight_pointSame(robot);
		}
		if (moveRight_pointSame(robot)) {
			DFS_cleanRoom_pointSame(robot, x, y + 1, visited);
			moveLeft_pointSame(robot);
		}
		if (moveDown_pointSame(robot)) {
			DFS_cleanRoom_pointSame(robot, x + 1, y, visited);
			moveUp_pointSame(robot);
		}

	}

	public void cleanRoom_pointSame(Robot robot) {
		DFS_cleanRoom_pointSame(robot, 0, 0, new HashSet<String>());
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/223351
	 * 
	 * curDir is to ensure moving direction and facing direction are consistent. 
	 * For example, if we are facing up, we cannot move right unless we 
	 * do .turnRight() first.
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * -> constrained programming. <-
	 * 
	 * That basically means to put restrictions after each robot move. Robot moves, 
	 * and the cell is marked as visited. That propagates constraints and helps to 
	 * reduce the number of combinations to consider.
	 * 
	 * -> backtracking. <-
	 * 
	 * That means to come back to that cell, and to explore the alternative path.
	 * 
	 * This solution is based on the same idea as maze solving algorithm called 
	 * right-hand rule. Go forward, cleaning and marking all the cells on the way as 
	 * visited. Always turn right at the obstacles and then go forward.
	 * 
	 * If after the right turn there is an obstacle just in front ? Turn right again.
	 * 
	 * How to explore the alternative paths from the cell ?
	 * Go back to that cell and then turn right from your last explored direction.
	 * 
	 * Stop when you explored all possible paths, i.e. all 4 directions (up, right, 
	 * down, and left) for each visited cell.
	 * 
	 * + Mark the cell as visited and clean it up.
	 * + Explore 4 directions : up, right, down, and left (the order is important 
	 *   since the idea is always to turn right):
	 *   + Check the next cell in the chosen direction : 
	 *     + If it's not visited yet and there is no obstacles :
	 *       + Move forward.
	 *       + Explore next cells backtrack(new_cell, new_direction).
	 *       + Backtrack, i.e. go back to the previous cell.
	 *     + Turn right because now there is an obstacle (or a virtual obstacle) 
	 *       just in front.
	 * 
	 * ------------------------------------------------------------------------
	 * 
	 * The direction should reflect the real robot movement which is robot.turnRight() 
	 * here. That means that we want to turn right from the last explored direction 
	 * and hence the new direction is new_d = (d + i) % 4 and not just i % 4.
	 * 
	 * we want always to go clockwise (like how we defined our directions array), 
	 * this is to follow the right-hand rule, otherwise we will get lost in the maze.
	 * If you simply did something like for(int[] dir : dirs) this won't work as you 
	 * always picking the first dir from the list to go to which is Up, what you 
	 * need to do is to continue in clockwise fashion from your current direction
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/solution/
	 * https://leetcode.com/problems/robot-room-cleaner/solution/258351
	 * https://leetcode.com/problems/robot-room-cleaner/solution/299272
	 */
	// these four values are clockwise: up, right, down, left. 
	// (0,1) meaning y+1 -> go up
	int[] dx_dfs2 = { 0, 1, 0, -1 };
	int[] dy_dfs2 = { 1, 0, -1, 0 };

	public void cleanRoom_dfs2(Robot robot) {
		Set<String> hs = new HashSet<>();
		
		// the start position is seen as the original point. facing up originally
		dfs_dfs2(robot, 0, 0, 0, hs);
	}

	public void dfs_dfs2(Robot robot, int x, int y, int curDir, Set<String> hs) {
		
		// curDir is the current facing direction
		
		hs.add(x + "#" + y);
		robot.clean();

		for (int i = 0; i < 4; i++) {
			// moving direction, let's say we are facing right (1), 
			// nextDir will be 1 as well.
			int nextDir = (curDir + i) % 4; 
			
			int nextX = x + dx_dfs2[nextDir];
			int nextY = y + dy_dfs2[nextDir];
			
			// robot.move() not only checks wall but also moves
			if (!hs.contains(nextX + "#" + nextY) && robot.move()) {
				dfs_dfs2(robot, nextX, nextY, nextDir, hs);

				// go back to start cell
				robot.turnRight();
				robot.turnRight();
				robot.move();
				
				// go back to the original direction
				robot.turnRight();
				robot.turnRight();
			}
			
			// because we purposely arranged dx, dy to be clockwise.
			// If we are facing right, we will be facing down in the next iteration
			robot.turnRight();
		}
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/315943/How-to-tackle-this-problem-in-the-interviewJAVA
	 * 
	 * If we get ourselves into a niche where we are surrounded by all blocks, we can 
	 * only go back to the previous cell and from there we go to a different 
	 * direction. We may go back on a cell where have visited and cleaned before, a 
	 * good way to record that is to use a Set to store the coordinates.
	 * There are 4 directions that we can go, we use an array to store the 
	 * UP{-1,0}, LEFT{0,-1}, DOWN{1,0}, and RIGHT{0,1}.
	 * On the 2D map, two variants we need to know: coordinates and direction.
	 * 
	 * 1. skip and return if the coordinate we are visiting has already been visited
	 * 2. if not, clean it and record it in the visited set
	 * 3. move the robot along its original direction, if it cannot, there are still 
	 *    3 other directions we can go. We will stick with turning right as our only 
	 *    choice. Remember the FPS and NPC game concept we talked about, we will not 
	 *    only make the robot to turnRight(), we still need to make the direction on 
	 *    the 2D map along with the robot's right turn.
	 * 
	 * Turning right twice is like making a U-turn
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/315943/How-to-tackle-this-problem-in-the-interviewJAVA/298172
	 * 
	 * Other code:
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/188647
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/731220
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/175320/Very-consice-Java-DFS-solution-9ms
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/151942/Java-DFS-Solution-with-Detailed-Explanation-and-6ms-(99)-Solution
	 */
	private Set<String> visited2 = new HashSet<>();
	private final int[][] dirs2 = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };

	public void cleanRoom2(Robot robot) {
		dfs2(robot, 0, 0, 0);
	}

	private void dfs2(Robot robot, int x, int y, int dir) {
		// 4 directions that the robot can go: up, left, down, right. 
		// Each increment will make the robot turnLeft.

		// if this cell is already visited(AKA cleaned), we will skip and return
		if (visited2.contains(x + "," + y)) {
			return;
		}
		
		// otherwise we will clean this cell and mark this cell in the visited
		robot.clean();
		visited2.add(x + "," + y);
		
		// backtracking: at current cell, the robot can go 4 direction and we will try
		// at its current direction, if the robot cannot move, we will turn and
		// increment the dir by 1 until we can move or go back to the last recursion.
		for (int i = 0; i < 4; i++) {

			// if the robot can move(meaning there's no physical obstacle)
			if (robot.move()) {
				dfs2(robot, x + dirs2[dir][0], y + dirs2[dir][1], dir);
				
				robot.turnRight();
				robot.turnRight();
				
				robot.move();
				
				robot.turnRight();
				robot.turnRight();
			}
			
			robot.turnLeft();
			dir = (dir + 1) % 4;
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking
	 * 
	 * To indicate whether a cell has been cleaned(visited), we assume the start 
	 * point is (0, 0) and initial orientation is 0 as follows
	 * 
	 *     0
	 * 3  -|-  1
	 *     2
	 * 
	 * each orientation is associated with a direction. 
	 * directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; indicates movement.
	 * 
	 * We can try moving in 4 directions for each step (starting from orientation x)
	 * 
	 * move forward - move; orientation x
	 * move right - turnRight, move; orientation (x + 1) % 4
	 * move backward - turnRight, turnRight, move; orientation (x + 2) % 4
	 * move left - turnRight, turnRight, turnRight, move;  orientation (x + 3) % 4
	 * 
	 * Using DFS, we have to backtrack after we explore as far as possible along a 
	 * branch, i.e. robot moves backward one step while maintaining its orientation.
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * Actually curDirection marks the next coordinate in the board, which the robot 
	 * will move to. And turnRight() actually changes the direction the robot face 
	 * to, and then we call move() to make a real move.
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * I think it's better to put the "backward" logic right after the recursive 
	 * call. Both styles are accepted for this problem, however, the current one will 
	 * put robot one cell downward the original position (if it's not blocked).
	 * 
	 * 每次結束一層都會 Moves backward one step，所以最後結束會在後一格
	 * 若放在 recursive call 後面，只有執行 recursive call 返回後才會執行 
	 * Moves backward one step，所以最後結束會回到原先位置
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * the robot will not end up the same place, since the calling of clean would 
	 * move back at the end. Therefore, the ending location is (1,0) instead of (0,0).
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * notice that the 'reset' part, whether it's inside the for loop or outside the 
	 * for loop, all works!
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/419175
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/239683
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/473683
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/153530/DFS-Logical-Thinking/281911
	 * 
	 * Other code:
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/151659/Concise-Java-Solution
	 */
	private int[][] directions_dfs = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

	public void cleanRoom_dfs(Robot robot) {
		clean_dfs(robot, 0, 0, 0, new HashSet<>());
	}

	private void clean_dfs(Robot robot, int x, int y, int curDirection, 
			Set<String> visited) {
		
		// Cleans current cell.
		robot.clean();
		visited.add(x + " " + y);

		for (int nDirection = curDirection; nDirection < curDirection + 4; nDirection++) {
			int nx = directions_dfs[nDirection % 4][0] + x;
			int ny = directions_dfs[nDirection % 4][1] + y;
			
			if (!visited.contains(nx + " " + ny) && robot.move()) {
				clean_dfs(robot, nx, ny, nDirection % 4, visited);
			}
			
			// Changed orientation.
			robot.turnRight();
		}

		// Moves backward one step while maintaining the orientation.
		robot.turnRight();
		robot.turnRight();
		robot.move();
		robot.turnRight();
		robot.turnRight();
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/188647
	 * 
	 * the directions are for our convenience to understand the current directions 
	 * of the robot and because we want the robot to reach all the 4 ends of the room
	 * 
	 * The arrow can only be valid between 0,1,2,3, however for some case we will 
	 * start from 3, then 0,1,2;.
	 * So we used the way how we handle the circler buffer, just % 4 to move 3 to 
	 * next direction which is 0.
	 * 
	 * After you've done one step, you want to go back to the 'original' position. 
	 * In this case, we do left,left == turn around, move == move back one step, 
	 * left,left == turn around. So now you are at the original position, facing the 
	 * original direction.
	 * 
	 * When we visit a cell that is already cleaned, which we will after making one 
	 * complete round, we need to backtrack one step. Again, as we are not storing 
	 * indices of the array, we ask the robot to either turn left twice or turn right 
	 * twice, so that direction of the robot will be reversed (Remember one turn 
	 * results in robot turning 90 degrees, twice is 180 degrees , so the robot 
	 * direction is reversed and then we make one move to the previous cell. Now, we 
	 * need to change back to the original direction or else, we will keep 
	 * backtracking and will miss cleaning other accessible cells in the room. So, we 
	 * again make the same rotation we made earlier (turn right twice or turn left 
	 * twice), to make the robot point in the original direction and then we move 
	 * right to the next accessible cell.
	 * 
	 * So the dirs here in our method is to only use as reference to track the cells 
	 * in the map but not the robot itself. When the robot finds itself nowhere to 
	 * go, we have to implement its internal command to ask it to turn left or right 
	 * so the robot can turn. And the reason why we need (dir+1)%4 is to virtually 
	 * guide ourselves on the cell map that after the robot turns right, the natural 
	 * direction will be the original (direction +1) % 4 as the moving forward 
	 * direction.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/188948
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/210745
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/292923
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/620389
	 */
	public void cleanRoom4(Robot robot) {
		Set<String> visited = new HashSet<>();
		backtracing4(robot, visited, 0, 0, 0);
		return;
	}

	// be careful! has to be one direction v,>,^,<
	// int[][] direction = {{1,0}, {0,1}, {-1,0}, {0,-1}};
	// v,<,^,> also works
	int[][] direction4 = { { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } };
    
	private void backtracing4(Robot r, Set<String> visited, 
			int x, int y, int arrow) {
		
		r.clean();

		for (int i = 0; i < 4; i++) {
			int nx = x + direction4[arrow][0];
			int ny = y + direction4[arrow][1];
			
			String path = nx + "-" + ny;
			if (!visited.contains(path) && r.move()) {
				visited.add(path);
				backtracing4(r, visited, nx, ny, arrow);
			}
			
			// always turn right
			r.turnRight();
			arrow = (arrow + 1) % 4;
		}
		
		r.turnLeft();
		r.turnLeft();
		r.move();
		r.turnLeft();
		r.turnLeft();
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution
	 * 
	 * 1. To track the cells the robot has cleaned, start a index pair (i, j) 
	 *    from (0, 0). When go up, i-1; go down, i+1; go left, j-1; go right: j+1.
	 * 2. Also use DIR to record the current direction of the robot
	 * 
	 * From 100,0 back to 99,0 and another DFS will be started again at 99,0. 
	 * When DFS at 99,0 is done, it will go back to 98,0 so on and so forth until 0,0
	 * 
	 * cur_dir is to let you know which direction u came in when doing dfs
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/510209
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/208646
	 */
	public void cleanRoom3(Robot robot) {
		// A number can be added to each visited cell
		// use string to identify the class
		Set<String> set = new HashSet<>();
		
		// 0: up, 90: right, 180: down, 270: left
		int cur_dir = 0;
		
		backtrack3(robot, set, 0, 0, cur_dir);
	}

	public void backtrack3(Robot robot, Set<String> set, int i, int j, int cur_dir) {
		String tmp = i + "->" + j;
		if (set.contains(tmp)) {
			return;
		}

		robot.clean();
		set.add(tmp);

		for (int n = 0; n < 4; n++) {
			// the robot can to four directions, we use right turn
			if (robot.move()) {
				// can go directly. Find the (x, y) for the next cell based on 
				// current direction
				int x = i, y = j;
				
				switch (cur_dir) {
				case 0:
					// go up, reduce i
					x = i - 1;
					break;
				case 90:
					// go right
					y = j + 1;
					break;
				case 180:
					// go down
					x = i + 1;
					break;
				case 270:
					// go left
					y = j - 1;
					break;
				default:
					break;
				}

				backtrack3(robot, set, x, y, cur_dir);
				
				// go back to the starting position
				robot.turnLeft();
				robot.turnLeft();
				robot.move();
				robot.turnRight();
				robot.turnRight();

			}
			
			// turn to next direction
			robot.turnRight();
			cur_dir += 90;
			cur_dir %= 360;
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/164917
	 * 
	 * p means the robot's pointing direction when first entering the grid. 
	 * and (p + i) % 4 can make sure after traversing all 4 directions, the robot can 
	 * back track to its original status.
	 * 
	 * every time the direction the robot faced could be different. So we need to 
	 * use the relative position
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/171064
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/139057/Very-easy-to-understand-Java-solution/171076
	 */
	int[][] dirs5 = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

	public void cleanRoom5(Robot robot) {
		int[] pos = { 0, 0 };
		Set<String> visited = new HashSet<String>();
		backtracking5(robot, pos, visited, 0);
	}

	private void backtracking5(Robot robot, int[] pos, Set<String> visited, int p) {
		String position = pos[0] + "," + pos[1];
		if (visited.contains(position)) {
			return;
		} 
		else {
			visited.add(position);
			robot.clean();
			
			for (int i = 0; i < 4; i++) {
				int newP = (p + i) % 4;
				
				if (!robot.move()) {
					robot.turnRight();
					continue;
				}
				
				int[] newPos = { pos[0] + dirs5[newP][0], pos[1] + dirs5[newP][1] };
				backtracking5(robot, newPos, visited, newP);
				
				// backtracking; move the robot to its original position 
				// and facing direction
				robot.turnRight();
				robot.turnRight();
				robot.move();
				
				// here equals to 3 turnright(); two of them are for backtracking
				robot.turnLeft();
			}
		}
	}

	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/151942/Java-DFS-Solution-with-Detailed-Explanation-and-6ms-(99)-Solution
	 * 
	 * The following details are different in this problem than a common dfs problem:
	 * 1. A way to encode already visited positions (The easy solution is just use 
	 *    relative positions from the starting point)
	 * 2. A way to backtrack (The concept is the same, we want to reset the position 
	 *    to what it originally was. In this case the direction is also added in, so 
	 *    we want to reset the direction it's facing to the direction it originally 
	 *    was facing with the original position).
	 * 
	 * I assume the robot is starting facing upwards, but any direction works as long 
	 * as the order of the directions match the direction with the turning.
	 * 
	 * The algorithm starts off at 0,0 which is our starting point (the boards 
	 * starting point doesn't matter, and it doesn't matter that there are negative 
	 * positions), as long as all the values are consistent. For each of the 4 
	 * directions, we calculate its next row and next column (remember that we are 
	 * currently facing in the last direction moved, in this code's case the variable 
	 * curDirection) if we went in that direction. If the next position is unvisited 
	 * we try moving there. If we are able to move we call it recursively down the 
	 * next branch.
	 * 
	 * After we return from the recursive call we need to backtrack:
	 * 
	 * robot.turnLeft();
     * robot.turnLeft();
     * robot.move();
     * robot.turnRight();
     * robot.turnRight();
     * 
     * or we can use 2 robot.turnLeft()s like I did.
     * We reset the state by turning 180 degrees, moving, and then changing the 
     * direction back to what it originally was by turning another 180 degrees.
     * Then we try the next direction (turning it to the right). After all 4 
     * directions are tried it is automatically turned to the original direction 
     * after the loop since we turned right 4 times.
     * 
     * An optimization would be to use a custom class with equals and hashcode 
     * implemented (so that hashcode has no collisions) , and we can reduce 1 turn 
     * per valid move by noticing that we need to move to the next position after the 
     * loop, so we don't need to backtrack completely. We can backtrack to the next 
     * position by turning 90 degrees back instead of 180. Then we only turn right if 
     * it wasn't a valid move.
     * 
     * ------------------------------------------------------------------------
     * 
     * direction is the index of the next direction vector since we are turning the 
     * robot right once every iteration. I pretend it starts facing upwards, -1,0 is 
     * the direction it will go in facing upwards, 0,1 is the direction it will go 
     * facing right, 1,0 is the direction it will go facing down, and 0,-1 is the 
     * direction it will go facing left.
     * 
     * Rf :
     * https://leetcode.com/problems/robot-room-cleaner/discuss/151942/Java-DFS-Solution-with-Detailed-Explanation-and-6ms-(99)-Solution/187798
	 */
	class Solution_override {
		final int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

		class Node {
			int row;
			int col;

			public Node(int row, int col) {
				this.row = row;
				this.col = col;
			}

			@Override
			public boolean equals(Object o) {
				Node node = (Node) o;
				if (node.row == row && node.col == col) {
					return true;
				}
				return false;
			}

			@Override
			public int hashCode() {
				int res = 17;
				res = res * 31 + row;
				res = res * 31 + col;
				return res;
			}
		}

		private void find(Robot robot, Set<Node> visited, 
				int curDirection, int row, int col) {
			
			Node node = new Node(row, col);
			visited.add(node);
			robot.clean();
			
			for (int i = 0; i < 4; ++i) {
				int direction = (curDirection + i) % 4;
				int[] next = directions[direction];
				
				int nextRow = row + next[0];
				int nextCol = col + next[1];
				node = new Node(nextRow, nextCol);
				
				if (!visited.contains(node) && robot.move()) {
					find(robot, visited, direction, nextRow, nextCol);
					
					robot.turnLeft();
					robot.turnLeft();
					robot.move();
					robot.turnLeft();
				} 
				else {
					robot.turnRight();
				}
			}
		}

		public void cleanRoom(Robot robot) {
			Set<Node> offset = new HashSet<>();
			find(robot, offset, 0, 0, 0);
		}
	}

	/*
	 * Modified by myself
	 * 有時會掛掉
	 * 
	 * Rf :
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/396260/GOD-WILL-GUIDE-YOUR-STEPS-A-fully-randomized-algorithm
	 * https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
	 */
	public void cleanRoom_random(Robot robot) {
		boolean f = true;

		for (int i = 0; i < 1000000; ++i) {
			robot.clean();

			// must make a turn if blocked last time
			int min = f ? 0 : 1;
			int max = 3;
			int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

			switch (randomNum) {
			case 0:   // no turn
				break;
			case 1:   // u turn
				robot.turnRight();
			case 2:   // turn right
				robot.turnRight();
				break;
			case 3:   // turn left
				robot.turnLeft();
				break;
			}

			// am i blocked?
			f = robot.move();
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/robot-room-cleaner/discuss/150132/Very-clear-Python-DFS-code-beat-99-%2B
     * https://leetcode.com/problems/robot-room-cleaner/discuss/152571/Python-16-lines-simple-backtracking-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/robot-room-cleaner/discuss/1028277/C%2B%2B-Algorithm-walk-through-and-code-with-detailed-comments.
     * https://leetcode.com/problems/robot-room-cleaner/discuss/148751/Very-short-C%2B%2B-recursive-DFS-4ms
     * https://leetcode.com/problems/robot-room-cleaner/discuss/396260/GOD-WILL-GUIDE-YOUR-STEPS-A-fully-randomized-algorithm
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/robot-room-cleaner/discuss/136392/Javascript-DFS-solution
	 */

}


// This is the robot's control interface.
// You should not implement it, or speculate about its implementation
interface Robot {
    // Returns true if the cell in front is open and robot moves into the cell.
    // Returns false if the cell in front is blocked and robot stays in the current cell.
    public boolean move();

    // Robot will stay in the same cell after calling turnLeft/turnRight.
    // Each turn will be 90 degrees.
    public void turnLeft();
    public void turnRight();

    // Clean the current cell.
    public void clean();
}
