package OJ361_370;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Nested_List_Weight_Sum_II {
	public interface NestedInteger {
		/*
		// Constructor initializes an empty nested list.
		public NestedInteger();

		// Constructor initializes a single integer.
		public NestedInteger(int value);
		*/

		// @return true if this NestedInteger holds a single integer, rather
		// than a nested list.
		public boolean isInteger();

		// @return the single integer that this NestedInteger holds, if it holds
		// a single integer
		// Return null if this NestedInteger holds a nested list
		public Integer getInteger();

		// Set this NestedInteger to hold a single integer.
		public void setInteger(int value);

		// Set this NestedInteger to hold a nested list and adds a nested
		// integer to it.
		public void add(NestedInteger ni);

		// @return the nested list that this NestedInteger holds, if it holds a
		// nested list
		// Return null if this NestedInteger holds a single integer
		public List<NestedInteger> getList();
	}
	
	// https://discuss.leetcode.com/topic/49488/java-ac-bfs-solution
	public int depthSumInverse_BFS(List<NestedInteger> nestedList) {
        if (nestedList == null) return 0;
        Queue<NestedInteger> queue = new LinkedList<NestedInteger>(nestedList);
        int prev = 0;
        int total = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            //int levelSum = 0;
            for (int i = 0; i < size; i++) {
                NestedInteger current = queue.poll();
                if (current.isInteger()) {
                	//levelSum += current.getInteger();
                	prev += current.getInteger();    // Modify line 53 to this
                }
                else {                	
                	List<NestedInteger> nextList = current.getList();
                	for (NestedInteger next: nextList) {
                		queue.offer(next);
                	}
                }
            }
            //prev += levelSum;
            total += prev;
        }
        return total;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/49041/no-depth-variable-no-multiplication
	 * 
	 * Add integers multiple times 
	 * (by going level by level and adding the unweighted sum to the weighted sum 
	 * after each level)
	 * 
	 */
	public int depthSumInverse(List<NestedInteger> nestedList) {
	    int unweighted = 0, weighted = 0;
	    while (!nestedList.isEmpty()) {
	        List<NestedInteger> nextLevel = new ArrayList<>();
	        for (NestedInteger ni : nestedList) {
	            if (ni.isInteger())
	                unweighted += ni.getInteger();
	            else
	                nextLevel.addAll(ni.getList());
	        }
	        weighted += unweighted;
	        nestedList = nextLevel;
	    }
	    return weighted;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/49023/share-my-2ms-intuitive-one-pass-no-multiplication-solution
	 * 
	 * Repeat adding integer in this level to next level
	 */
	public int depthSumInverse_parameterAdd(List<NestedInteger> nestedList) {
        return helper_parameterAdd(nestedList, 0);
    }
    
    private int helper_parameterAdd(List<NestedInteger> niList, int prev) {
        int intSum = prev;
        List<NestedInteger> levelBreak = new ArrayList<>();
        
        for (NestedInteger ni : niList) {
            if (ni.isInteger()) {
                intSum += ni.getInteger();
            } else {
                levelBreak.addAll(ni.getList());
            }
        }
        
        int listSum = levelBreak.isEmpty()? 0 : helper_parameterAdd(levelBreak, intSum);

        return listSum + intSum;
    }
    
    /*
     * https://discuss.leetcode.com/topic/68836/easy-java-bfs-using-queue-with-less-variable
     * 
     * Add a queue to record the sum in each level. 
     * The size() would be the depth of each sum.
     */
    public int depthSumInverse_queue(List<NestedInteger> nestedList) {
        Queue<Integer> queue = new LinkedList<>();
        while (!nestedList.isEmpty()) {
            int sum = 0;
            List<NestedInteger> next = new LinkedList<>();
            for (NestedInteger in : nestedList) {
                if (in.isInteger()) {
                    sum += in.getInteger();
                } else {
                    next.addAll(in.getList());
                }
            }
            queue.offer(sum);
            nestedList = next;
        }
        int rst = 0;
        while (queue.size() != 0) {
            rst += queue.size() * queue.peek();
            queue.poll();
        }
        return rst;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://discuss.leetcode.com/topic/49038/one-pass-java-using-a-list-dfs
     * 
     * Store the values per level in a list, 
     * then traverse the result list backwards and multiply by the level number.
     */
    public int depthSumInverse_List(List<NestedInteger> nestedList) {
        List<Integer> result = dfs(nestedList, 0, new ArrayList<>());
        
        int total = 0;
        for (int index = result.size() - 1; index >= 0; index--) {
            total += result.get(index) * (result.size() - index);
        }
        
        return total;
    }
    
    List<Integer> dfs(Iterable<NestedInteger> list, int depth, List<Integer> result) {
        if (depth == result.size()) {
            result.add(0);
        }
        
        for (NestedInteger integer : list) {
            if (integer.isInteger()) {
                result.set(depth, result.get(depth) + integer.getInteger());
            } else {
                dfs(integer.getList(), depth + 1, result);
            }
        }
        
        return result;
    }
    
    /*
     * The following 2 functions and one variable are from this link.
     * https://discuss.leetcode.com/topic/49005/hashmap-java-solution-with-explanation
     * 
     * In the HashMap we don't need to record all integers we visited, 
     * we just need to record the sum of integers in current depth.
     */
	int maxDepth = 0;
	public int depthSumInverse_map2 (List<NestedInteger> nestedList) {
		// HashMap solution. We use HashMap to store nums in each depth before
		// we find the maxDepth
		// we will do the sum calculation in the last

		HashMap<Integer, Integer> hs = new HashMap<Integer, Integer>();
		DFS_map(nestedList, 1, hs);

		int sum = 0;

		// get sum
		for (int i = 1; i <= maxDepth; i++) {
			// put a checker here in case we dont have integer in one layer
			if (hs.containsKey(i))
				sum += hs.get(i) * (maxDepth + 1 - i);
		}
		return sum;
	}

	private void DFS_map (List<NestedInteger> nestedList, int depth, HashMap<Integer, Integer> hs) {
		// boundary check
		if (nestedList.isEmpty())
			return;

		// update maxDepth if possible
		maxDepth = Math.max(maxDepth, depth);

		for (NestedInteger temp : nestedList) {
			if (temp.isInteger()) {
				// if temp is integer
				if (!hs.containsKey(depth)) {
					hs.put(depth, temp.getInteger());
				} else {
					hs.put(depth, hs.get(depth) + temp.getInteger());
				}
			} else {
				// if temp is list
				DFS_map(temp.getList(), depth + 1, hs);
			}
		}
	}
    
    // The following 2 functions and one variable are from this link.
    // https://discuss.leetcode.com/topic/65574/super-easy-ac-java-solution-using-hashmap
    Map<Integer, Integer> map_levelSum = new HashMap<Integer, Integer>();
    public int depthSumInverse_map(List<NestedInteger> nestedList) {
        for (NestedInteger v : nestedList) {
            DFS_map(v, 1);
        }
        int sum = 0;
        if (map_levelSum.size() == 0) return 0;
        int maxDepth = Collections.max(map_levelSum.keySet()) + 1;
        for (Map.Entry<Integer, Integer> entry : map_levelSum.entrySet()) {
            int depth = maxDepth - entry.getKey();
            sum += depth * entry.getValue();
        }
        return sum;
    }
    
    private void DFS_map(NestedInteger cur, int depth) {
        if (cur.isInteger()) {
            map_levelSum.put(depth, map_levelSum.getOrDefault(depth, 0) + cur.getInteger());
        } else {
            for (NestedInteger c : cur.getList()) {
                DFS_map(c, depth + 1);
            }
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://discuss.leetcode.com/topic/50516/java-easy-to-understand-solution-using-recursion
     * 
     * Use List<List<Integer>> to store numbers by their levels
     */
	public int depthSumInverse_storeLevelList(List<NestedInteger> nestedList) {
		if (nestedList.size() == 0)
			return 0;

		List<List<Integer>> list = new ArrayList<>();

		helper_store_to_list(nestedList, 0, list);

		int result = 0;
		int n = list.size();
		for (int i = 0; i < n; i++) {
			List<Integer> l = list.get(i);
			int power = n - i;
			for (int j : l) {
				result += (power * j);
			}
		}
		return result;
	}

	private void helper_store_to_list(List<NestedInteger> nestedList, int level, List<List<Integer>> list) {
		if (list.size() == level) {
			list.add(new ArrayList<>());
		}

		for (int i = 0; i < nestedList.size(); i++) {
			NestedInteger j = nestedList.get(i);
			if (j.isInteger()) {
				list.get(level).add(j.getInteger());
			} else {
				helper_store_to_list(j.getList(), level + 1, list);
			}
		}
	}
	
	public int depthSumInverse_ListofList(List<NestedInteger> nestedList) {
		int sum = 0;
		List<List<Integer>> levelPair = new ArrayList<List<Integer>>();
		depthSumInverse_ListofList(nestedList, levelPair, 1);
		
		int depth = 1;
		for (List<Integer> element : levelPair) {
			if (element.get(1) > depth) {
				depth = element.get(1);
			}
		}
		
		for (List<Integer> element : levelPair) {
			sum = sum + element.get(0) * (depth - element.get(1) + 1);
		}
		return sum;
	}
	
	private void depthSumInverse_ListofList(List<NestedInteger> nestedList, List<List<Integer>> pair, int level) {
		for (NestedInteger element : nestedList) {
			if (element.getInteger() == null) {
				depthSumInverse_ListofList(element.getList(), pair, level + 1);
			}
			else {
				List<Integer> n = new ArrayList<Integer>(2);
				n.add(element.getInteger());
				n.add(level);
				pair.add(n);
			}
		}
	}
	
	/*
	 * The following 3 functions are from this link
	 * https://discuss.leetcode.com/topic/49017/java-two-pass-dfs-solution
	 */
	public int depthSumInverse_2pass(List<NestedInteger> nestedList) {
		if (nestedList == null || nestedList.size() == 0)
			return 0;
		int h = helper_getDepth(nestedList);
		int res = getSum(nestedList, h);
		return res;
	}

	private int getSum(List<NestedInteger> l, int layer) {
		int sum = 0;
		if (l == null || l.size() == 0)
			return sum;
		for (NestedInteger n : l) {
			if (n.isInteger())
				sum += n.getInteger() * layer;
			else
				sum += getSum(n.getList(), layer - 1);
		}
		return sum;
	}

	private int helper_getDepth(List<NestedInteger> l) {
		if (l == null || l.size() == 0)
			return 0;
		int max = 0;
		for (NestedInteger n : l) {
			if (n.isInteger())
				max = Math.max(max, 1);
			else
				max = Math.max(max, helper_getDepth(n.getList()) + 1);
		}
		return max;
	}
	
	/*
	 * The following 2 functions and 2 variables are from this link.
	 * https://discuss.leetcode.com/topic/78923/fast-and-directly-java-dfs-solution
	 * 
	 * When meet a sublist, recursive DFS and depth minus 1. 
	 * Final add the compensation
	 */
	private int sum = 0; // The sum of all element
	private int Maxdepth = 1;

	int depthSumInverse_negative_compensation(List<NestedInteger> nestedList) {
		int result = dfs_negative(nestedList, -1);
		result += sum * (Maxdepth + 1); // Compensate the deviation
		return result;
	}

	private int dfs_negative(List<NestedInteger> nestedList, int depth) {
		int result = 0;
		for (NestedInteger nested : nestedList) {
			// When an element is a number, renew result and Maxdepth
			if (nested.isInteger()) {
				sum += nested.getInteger();
				result += nested.getInteger() * depth;
				// Find the Maxdepth
				Maxdepth = Math.max(Maxdepth, -depth);
			} else {
				// When an element is also list, recursively call the DFS
				// function
				result += dfs_negative(nested.getList(), depth - 1);
			}
		}
		return result;
	}

}
