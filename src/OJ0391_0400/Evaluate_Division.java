package OJ0391_0400;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Evaluate_Division {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/evaluate-division/discuss/136188/Logical-Thinking-with-Java-Code-Beats-97.40
	 * 
	 * We utilize the graph model to represent connections:
	 * Take A/B = k for example, A and B are regarded as nodes. Since k is provided, 
	 * there is an edge from A to B with the weight k, at the same time, there is an 
	 * edge from B to A with the weight 1.0 / k.
	 * We apply the Depth First Search in calcEquationFrom() to see if query[i][0] 
	 * and query[i][1] are connected.
	 * 
	 * Rf :
	 * leetcode.com/problems/evaluate-division/discuss/88346/Java-Solution-Using-HashMap-and-DFS/120551
	 * https://leetcode.com/problems/evaluate-division/discuss/88228/Java-AC-solution-with-explanation
	 * https://leetcode.com/problems/evaluate-division/discuss/88169/Java-AC-Solution-using-graph
	 * 
	 * Other code:
	 * https://leetcode.com/problems/evaluate-division/discuss/88346/Java-Solution-Using-HashMap-and-DFS
	 */
	public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
		Map<String, Map<String, Double>> graph = new HashMap<>();
		for (int i = 0; i < equations.length; i++) {
			String u = equations[i][0], v = equations[i][1];
			if (!graph.containsKey(u)) {
				graph.put(u, new HashMap<>());
			}
			if (!graph.containsKey(v)) {
				graph.put(v, new HashMap<>());
			}
			
			graph.get(u).put(v, values[i]);
			graph.get(v).put(u, 1.0 / values[i]);
		}

		double[] result = new double[queries.length];
		int ri = 0;
		for (String[] query : queries) {
			String start = query[0], end = query[1];
			result[ri++] = calcEquationFrom(graph, start, end, new HashSet<>());
		}
		return result;
	}

	private double calcEquationFrom(Map<String, Map<String, Double>> graph, 
			String start, String end, Set<String> visited) {

		if (!graph.containsKey(start) || visited.contains(start)) {
			return -1.0;
		}
		if (graph.get(start).containsKey(end)) {
			return graph.get(start).get(end);
		}
		
		visited.add(start);
		for (String neighbour : graph.get(start).keySet()) {
			double val = calcEquationFrom(graph, neighbour, end, visited);
			if (val == -1.0) {
				continue;
			}
			
			return val * graph.get(start).get(neighbour);
		}
		return -1.0;
	}
	
	/*
	 * https://leetcode.com/problems/evaluate-division/discuss/88207/java-solution-using-FloydWarshall-algorithm
	 * 
	 * Rf : https://leetcode.com/problems/evaluate-division/discuss/88175/9-lines-%22FloydWarshall%22-in-Python
	 * 
	 * Other code :
	 * https://leetcode.com/problems/evaluate-division/discuss/88311/Java-Graph-based-solution.-No-DFS-is-needed.-3ms
	 */
	public double[] calcEquation_Floyd_Warshall(String[][] equations, double[] values, 
			String[][] queries) {
        HashMap<String, HashMap<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String src = equations[i][0], dst = equations[i][1];
            if (!graph.containsKey(src)) {
                graph.put(src, new HashMap<>());
            }
            if (!graph.containsKey(dst)) {
                graph.put(dst, new HashMap<>());
            }
            graph.get(src).put(src, 1.0);
            graph.get(dst).put(dst, 1.0);
            graph.get(src).put(dst, values[i]);
            graph.get(dst).put(src, 1 / values[i]);
        }
        
        for (String mid : graph.keySet()) {
            for (String src : graph.get(mid).keySet()) {
                for (String dst : graph.get(mid).keySet()) {
                    double val = graph.get(src).get(mid) * graph.get(mid).get(dst);
                    graph.get(src).put(dst, val);
                }
            }
        }
        
        double[] result = new double[queries.length];
        for (int i = 0; i < result.length; i++) {
            if (!graph.containsKey(queries[i][0])) {
                result[i] = -1;
            } else {
                result[i] = graph.get(queries[i][0]).getOrDefault(queries[i][1], -1.0);
            }
        }
        return result;
    }
	
	// by myself
	public double[] calcEquation_self(String[][] equations, double[] values, String[][] queries) {
        Map<String, Map<String, Double>> map = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            if (!map.containsKey(equations[i][0])) {
                Map<String, Double> list = new HashMap<>();
                map.put(equations[i][0], list);
            }
            if (!map.containsKey(equations[i][1])) {
                Map<String, Double> list = new HashMap<>();
                map.put(equations[i][1], list);
            }
            
            map.get(equations[i][0]).put(equations[i][1], values[i]);
            map.get(equations[i][1]).put(equations[i][0], 1 / values[i]);
        }
        
        double[] res = new double[queries.length];
        for (int i = 0; i < res.length; i++) {
            if (!map.containsKey(queries[i][0]) || !map.containsKey(queries[i][1])) {
                res[i] = -1.0;
            }
            else {
                if (queries[i][0] == queries[i][1]) {
                    res[i] = 1.0;
                }
                else {
                    LinkedList<String[]> queue = new LinkedList<>();
                    Set<String> set = new HashSet<>();
                    for (Map.Entry<String, Double> entry : map.get(queries[i][0]).entrySet()) {
                        String[] tmp = new String[2];
                        tmp[0] = entry.getKey();
                        tmp[1] = Double.toString(entry.getValue());
                        
                        if (set.add(tmp[0]))
                            queue.add(tmp);
                    }
                    
                    boolean done = false;
                    while (!queue.isEmpty() && !done) {
                        String[] out = queue.removeFirst();
                        for (Map.Entry<String, Double> entry : map.get(out[0]).entrySet()) {
                            String[] tmp = new String[2];
                            tmp[0] = entry.getKey();
                            double cur = Double.parseDouble(out[1]) * entry.getValue();
                            tmp[1] = Double.toString(cur);
                            
                            if (tmp[0].equals(queries[i][1])) {
                                res[i] = cur;
                                done = true;
                                break;
                            }
                            
                            if (set.add(tmp[0]))
                                queue.add(tmp);
                        }
                    }
                    
                    if (!done)
                        res[i] = -1.0;
                }
            }
        }
        return res;
    }
	
	// by myself
	public double[] calcEquation_self2(String[][] equations, double[] values, String[][] queries) {
        Map<String, List<String>> val = new HashMap<>();
        Map<String, List<Double>> calc = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            if (!val.containsKey(equations[i][0])) {
                List<String> list = new ArrayList<>();
                val.put(equations[i][0], list);
                
                List<Double> list2 = new ArrayList<>();
                calc.put(equations[i][0], list2);
            }
            if (!val.containsKey(equations[i][1])) {
                List<String> list = new ArrayList<>();
                val.put(equations[i][1], list);
                
                List<Double> list2 = new ArrayList<>();
                calc.put(equations[i][1], list2);
            }
            
            val.get(equations[i][0]).add(equations[i][1]);
            calc.get(equations[i][0]).add(values[i]);
            
            val.get(equations[i][1]).add(equations[i][0]);
            calc.get(equations[i][1]).add(1 / values[i]);
        }
        
        double[] res = new double[queries.length];
        for (int i = 0; i < res.length; i++) {
            if (!val.containsKey(queries[i][0]) || !val.containsKey(queries[i][1])) {
                res[i] = -1.0;
            }
            else {
                if (queries[i][0] == queries[i][1]) {
                    res[i] = 1.0;
                }
                else {
                    LinkedList<String[]> queue = new LinkedList<>();
                    Set<String> set = new HashSet<>();
                    for (int j = 0; j < val.get(queries[i][0]).size(); j++) {
                        String[] tmp = new String[2];
                        tmp[0] = val.get(queries[i][0]).get(j);
                        tmp[1] = Double.toString(calc.get(queries[i][0]).get(j));
                        
                        if (set.add(tmp[0]))
                            queue.add(tmp);
                    }
                    
                    boolean done = false;
                    while (!queue.isEmpty() && !done) {
                        String[] out = queue.removeFirst();
                        for (int j = 0; j < val.get(out[0]).size(); j++) {
                            String[] tmp = new String[2];
                            tmp[0] = val.get(out[0]).get(j);
                            double cur = Double.parseDouble(out[1]) * calc.get(out[0]).get(j);
                            tmp[1] = Double.toString(cur);
                            
                            if (val.get(out[0]).get(j).equals(queries[i][1])) {
                                res[i] = cur;
                                done = true;
                                break;
                            }
                            
                            if (set.add(tmp[0]))
                                queue.add(tmp);
                        }
                    }
                    
                    if (!done)
                        res[i] = -1.0;
                }
            }
        }
        return res;
    }
	
	// https://leetcode.com/problems/evaluate-division/discuss/88170/0ms-C++-Union-Find-Solution-EASY-to-UNDERSTAND
	
	// https://leetcode.com/problems/evaluate-division/discuss/88287/Esay-understand-Java-solution-3ms
}
