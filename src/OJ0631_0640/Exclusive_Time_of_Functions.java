package OJ0631_0640;

import java.util.List;
import java.util.Stack;

public class Exclusive_Time_of_Functions {
	/*
	 * https://discuss.leetcode.com/topic/96153/java-solution-stack
	 * 
	 * Rf : https://leetcode.com/articles/exclusive-time-of-functions/
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/96068/java-stack-solution-o-n-time-o-n-space
	 * https://discuss.leetcode.com/topic/96064/simple-java-solution-stack
	 */
	public static int[] exclusiveTime(int n, List<String> logs) {
		int[] result = new int[n];
		//if (logs == null || logs.size() == 0)
			//return result;

		int id = 0, time = 0;
		Stack<Integer> stack = new Stack<>();
		for (String log : logs) {
			String[] arr = log.split("\\:");
			if (arr[1].equals("start")) {
				if (!stack.isEmpty()) {
					id = stack.peek();
					result[id] += Integer.parseInt(arr[2]) - time;
				}
				stack.push(Integer.parseInt(arr[0]));
				time = Integer.parseInt(arr[2]);
			} else { // end
				id = stack.pop();
				result[id] += Integer.parseInt(arr[2]) - time + 1;
				time = Integer.parseInt(arr[2]) + 1;
			}
		}

		return result;
	}
	
	/*
	 * The following class and function are from this link.
	 * https://discuss.leetcode.com/topic/96104/java-use-stack-clear-solution
	 */
	private class Function {
		int id;
		//int time;

		public Function(int id, int time) {
			this.id = id;
			//this.time = time;
		}
	}
	int[] exclusiveTime_define_class(int n, List<String> logs) {
		int[] res = new int[n];
		Stack<Function> stack = new Stack<Function>();
		int prev = 0;
		for (int i = 0; i < logs.size(); i++) {
			String log = logs.get(i);
			String[] str = log.split(":");
			int id = Integer.parseInt(str[0]);
			boolean isStart = str[1].equals("start");
			int time = Integer.parseInt(str[2]);

			if (stack.isEmpty()) {
				stack.push(new Function(id, time));
			} else if (isStart) {
				res[stack.peek().id] += (time - prev);
				stack.push(new Function(id, time));
			} else {
				res[id] += (time - prev + 1);
				stack.pop();
			}

			if (isStart) {
				prev = time;
			} else {
				prev = time + 1;
			}
		}

		return res;
	}
	
	// https://discuss.leetcode.com/topic/96190/java-o-n-easy-to-understand-dfs-solution

}
