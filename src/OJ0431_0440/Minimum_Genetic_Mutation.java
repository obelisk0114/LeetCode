package OJ0431_0440;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class Minimum_Genetic_Mutation {	
	/*
	 * by myself
	 * 
	 * Rf : https://leetcode.com/problems/minimum-genetic-mutation/discuss/91550/Java-Bidirectional-Search-Solution-(faster-than-BFS)
	 */
	public int minMutation_self(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<String>();
        for (String s : bank) {
            bankSet.add(s);
        }
        if (!bankSet.contains(end)) {
            return -1;
        }
        
        Set<String> front = new HashSet<String>();
        Set<String> back = new HashSet<String>();
        front.add(start);
        back.add(end);
        
        char[] gene = {'A', 'G', 'C', 'T'};
        int step = 0;
        
        while (!front.isEmpty() && !back.isEmpty()) {
            if (back.size() < front.size()) {
                Set<String> tmp = front;
                front = back;
                back = tmp;
            }
            
            Set<String> nextLevel = new HashSet<String>();
            for (String s : front) {
                char[] cur = s.toCharArray();
                for (int i = 0; i < cur.length; i++) {
                    char origin = cur[i];
                    for (int j = 0; j < gene.length; j++) {
                        if (gene[j] == origin) {
                            continue;
                        }
                        
                        cur[i] = gene[j];
                        String curS = new String(cur);
                        
                        if (back.contains(curS)) {
                            return step + 1;
                        }
                        
                        if (bankSet.contains(curS)) {
                            nextLevel.add(curS);
                            bankSet.remove(curS);
                        }
                    }
                    cur[i] = origin;
                }
            }
            front = nextLevel;
            step++;
        }
        return -1;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/minimum-genetic-mutation/discuss/91491/DFS-java
	 * 
	 * check 
	 * if two seqs only diff in one char it is a valid mutation
	 * find all the seq has one diff
	 * get closer to the end.
	 * keep a explored Set
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-genetic-mutation/discuss/91493/Java-2ms-BackTracking-solution.
	 */
	public int minMutation_DFS(String start, String end, String[] bank) {
		boolean[] explored = new boolean[bank.length];
		if (bank.length == 0)
			return -1;
		return minMutation_DFS(explored, start, end, bank);
	}

	int minMutation_DFS(boolean[] explored, String start, String end, String[] bank) {
		if (start.equals(end))
			return 0;

		int step = bank.length + 1;
		for (int i = 0; i < bank.length; i++) {
			if (diffOne(start, bank[i]) && !explored[i]) {
				explored[i] = true;
				int temp = minMutation_DFS(explored, bank[i], end, bank);
				if (temp != -1) {
					step = Math.min(step, temp);
				}
				explored[i] = false;
			}
		}
		return step == bank.length + 1 ? -1 : 1 + step;
	}

	boolean diffOne(String s1, String s2) {
		char[] s1c = s1.toCharArray();
		char[] s2c = s2.toCharArray();
		int count = 0;
		for (int i = 0; i < s1c.length; i++) {
			if (s1c[i] != s2c[i])
				count++;
			if (count >= 2)
				return false;
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-genetic-mutation/discuss/91514/Easy-Java-Solution-(Beats-most-of-the-solutions)
	 * 
	 * Rf : https://leetcode.com/problems/minimum-genetic-mutation/discuss/91484/Java-Solution-using-BFS
	 */
	public int minMutation_BFS(String start, String end, String[] bank) {
		Queue<String> queue = new LinkedList<>();
		char[] chars = { 'A', 'C', 'G', 'T' };
		Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
		
		int level = 0;
		queue.offer(start);
		while (!queue.isEmpty()) {
			int size = queue.size();
			while (size-- > 0) {
				String currString = queue.poll();
				if (currString.equals(end))
					return level;
				
				for (int i = 0; i < currString.length(); i++) {
					for (char ch : chars) {
						char[] currChars = currString.toCharArray();
						currChars[i] = ch;
						String modString = new String(currChars);
						
						if (bankSet.contains(modString)) {
							queue.add(modString);
							bankSet.remove(modString);
						}
					}
				}
			}
			level++;
		}
		return -1;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/minimum-genetic-mutation/discuss/91502/Java-Solution-using-Dijkstra's-shortest-path-algorithm
	 */
	public int minMutation_Dijkstra(String start, String end, String[] bank) {
		int n = bank.length + 1;

		String[] banks = new String[n];
		banks[0] = start;
		System.arraycopy(bank, 0, banks, 1, bank.length);

		int[] dis = new int[n];
		for (int i = 0; i < n; i++) {
			dis[i] = -1;
		}
		boolean[] vis = new boolean[n];
		dis[0] = 0;

		for (int i = 0; i < n; i++) {
			// Find the shortest distance point
			int minPos = 0;
			int minDis = Integer.MAX_VALUE;
			for (int j = 0; j < n; j++) {
				if (!vis[j] && dis[j] != -1 && minDis > dis[j]) {
					minPos = j;
					minDis = dis[j];
				}
			}

			// Relax adjacent point
			vis[minPos] = true;
			for (int j = 0; j < n; j++) {
				if (!vis[j] && canVisit(banks, minPos, j)) {
					if (dis[j] == -1)
						dis[j] = dis[minPos] + 1;
					else
						dis[j] = Math.min(dis[minPos] + 1, dis[j]);
				}
			}
		}

		for (int i = 0; i < n; i++) {
			if (distance(banks[i], end) == 0)
				return dis[i];
		}
		return -1;
	}

	private boolean canVisit(String[] banks, int i, int j) {
		return distance(banks[i], banks[j]) == 1;
	}

	private int distance(String a, String b) {
		int cnt = 0;
		for (int p = 0; p < 8; p++) {
			if (a.charAt(p) != b.charAt(p))
				cnt++;
		}
		return cnt;
	}

}
