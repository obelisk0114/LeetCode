package OJ0551_0560;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Brick_Wall {
	/* 
	 * https://leetcode.com/problems/brick-wall/discuss/101728/I-DON'T-THINK-THERE-IS-A-BETTER-PERSON-THAN-ME-TO-ANSWER-THIS-QUESTION
	 * 
	 * We want to cut from the edge of the most common location among all the levels, 
	 * hence using a map to record the locations and their corresponding occurrence.
	 */
	public int leastBricks(List<List<Integer>> wall) {
        if(wall.size() == 0) return 0;
        int count = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(List<Integer> list : wall){
            int length = 0;
            for(int i = 0; i < list.size() - 1; i++){
                length += list.get(i);
                map.put(length, map.getOrDefault(length, 0) + 1);
                count = Math.max(count, map.get(length));
            }
        }
        return wall.size() - count;
    }
	
	//
	public int leastBricks_PriorityQueue(List<List<Integer>> wall) {
        int R = wall.size(), min = R;
        if (R == 1 && wall.get(0).size() > 1) return 0;
        
        // [0: end, 1: row, 2: col]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[0] - b[0]));
        
        for (int i = 0; i < R; i++) {
            pq.add(new int[] {wall.get(i).get(0), i, 0});
        }
        
        while (!pq.isEmpty()) {
            int end = pq.peek()[0], count = 0;
            
            while (!pq.isEmpty() && pq.peek()[0] == end) {
                count++;
                int[] brick = pq.poll();
                if (brick[2] < wall.get(brick[1]).size() - 1) {
                    pq.add(new int[] {end + wall.get(brick[1]).get(brick[2] + 1), brick[1], brick[2] + 1});
                }
            }
            
            if (!pq.isEmpty()) {
                min = Math.min(min, R - count);
            }
        }
        
        return min;
    }
	
	// https://leetcode.com/problems/brick-wall/discuss/101752/Neat-Java-Solution-O(n)-using-hashmap

}
