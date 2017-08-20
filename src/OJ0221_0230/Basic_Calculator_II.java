package OJ0221_0230;

import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Basic_Calculator_II {
	/*
	 * https://discuss.leetcode.com/topic/17435/java-straight-forward-iteration-solution-with-comments-no-stack-o-n-o-1
	 */
	public int calculate(String s) {
	    if (s == null) return 0;
	    s = s.trim().replaceAll(" +", "");
	    int length = s.length();
	    
	    int res = 0;
	    long preVal = 0; // initial preVal is 0
	    char sign = '+'; // initial sign is +
	    int i = 0;
	    while (i < length) {
	        long curVal = 0;
	        while (i < length && (int)s.charAt(i) <= 57 && (int)s.charAt(i) >= 48) { // int
	            curVal = curVal*10 + (s.charAt(i) - '0');
	            i++;
	        }
	        if (sign == '+') {
	            res += preVal;  // update res
	            preVal = curVal;
	        } else if (sign == '-') {
	            res += preVal;  // update res
	            preVal = -curVal;
	        } else if (sign == '*') {
	            preVal = preVal * curVal; // not update res, combine preVal & curVal and keep loop
	        } else if (sign == '/') {
	            preVal = preVal / curVal; // not update res, combine preVal & curVal and keep loop
	        }
	        if (i < length) { // getting new sign
	            sign = s.charAt(i);
	            i++;
	        }
	    }
	    res += preVal;
	    return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/42684/explanation-for-java-o-n-time-o-1-space-solution/2
	 * 
	 * First, we will have a variable "num" to represent the current number involved in the operations.
     * For the lower level, we use a variable "pre" to denote the partial result. And as usual we will have a variable "sign" to indicate the sign of the incoming result.
     * For the higher level, we use a variable "curr" to represent the partial result, and another variable "op" to indicate what operation should be performed:
     * 
       1. If op = 0, no '*' or '/' operation is needed and we simply assign num to curr;
       2. if op = 1, we perform multiplication: curr = curr * num;
       3. if op = -1, we perform division: curr = curr / num.
       
     * Rf : https://discuss.leetcode.com/topic/21126/java-344ms-o-n-time-o-1-space-with-comments
	 */
	public int calculate2(String s) {
	    int pre = 0, curr = 0, sign = 1, op = 0, num = 0;
	    
	    for (int i = 0; i < s.length(); i++) {
	        if (Character.isDigit(s.charAt(i))) {
	            num = num * 10 + (s.charAt(i) - '0');
	            if (i == s.length() - 1 || !Character.isDigit(s.charAt(i + 1))) {
	            	curr = (op == 0 ? num : (op == 1 ? curr * num : curr / num));
	            }
	            
	        } else if (s.charAt(i) == '*' || s.charAt(i) == '/') {
	            op = (s.charAt(i) == '*' ? 1 : -1);
	            num = 0;
	            
	        } else if (s.charAt(i) == '+' || s.charAt(i) == '-') {
	            pre += sign * curr;
	            sign = (s.charAt(i) == '+' ? 1 : -1);
	            op = 0;
	            num = 0;
	        }
	    }
	    
	    return pre + sign * curr;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/16935/share-my-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/16935/share-my-java-solution/19
	 */
	public int calculate_stack(String s) {
		int len;
		if (s == null || (len = s.length()) == 0)
			return 0;
		Stack<Integer> stack = new Stack<Integer>();
		int num = 0;
		char sign = '+';
		for (int i = 0; i < len; i++) {
			if (Character.isDigit(s.charAt(i))) {
				num = num * 10 + s.charAt(i) - '0';
			}
			if ((!Character.isDigit(s.charAt(i)) && ' ' != s.charAt(i)) || i == len - 1) {
				if (sign == '-') {
					stack.push(-num);
				}
				if (sign == '+') {
					stack.push(num);
				}
				if (sign == '*') {
					stack.push(stack.pop() * num);
				}
				if (sign == '/') {
					stack.push(stack.pop() / num);
				}
				sign = s.charAt(i);
				num = 0;
			}
		}

		int re = 0;
		for (int i : stack) {
			re += i;
		}
		return re;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/16817/clean-java-solution-using-two-queue
	 * 
	 * For case * or /, use two queues to store the number and operator.
     * For case + or -, if the queue is not empty, then we must have previous part 
     * with * or / need to be calculated.
	 */
	public int calculate_2_queue(String s) {
		Queue<Integer> queue = new LinkedList<Integer>();
		Queue<Character> cQueue = new LinkedList<Character>();
		int temp = 0;
		int sign = 1;
		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c)) {
				temp = 10 * temp + (int) (c - '0');
			} 
			else if (c == '+') {
				if (!queue.isEmpty()) {
					temp = calculateQueue(queue, cQueue, temp);
				}
				result += sign * temp;
				temp = 0;
				sign = 1;
			} 
			else if (c == '-') {
				if (!queue.isEmpty()) {
					temp = calculateQueue(queue, cQueue, temp);
				}
				result += sign * temp;
				temp = 0;
				sign = -1;
			} 
			else if (c == '*' || c == '/') {
				queue.add(temp);
				cQueue.add(c);
				temp = 0;
			}
		}
		// handle the remaining part
		if (temp != 0) {
			if (!queue.isEmpty()) {
				temp = calculateQueue(queue, cQueue, temp);
			}
			result += sign * temp;
		}
		return result;
	}

	// calculate previous temp with * or /
	public int calculateQueue(Queue<Integer> queue, Queue<Character> cQueue, int temp) {
		int num = 0;
		char sign2 = ' ';
		if (!queue.isEmpty()) {
			num = queue.poll();
			sign2 = cQueue.poll();
		}
		while (!queue.isEmpty()) {
			int num2 = queue.poll();
			if (sign2 == '*') {
				num = num * num2;
			} 
			else if (sign2 == '/') {
				num = num / num2;
			}
			sign2 = cQueue.poll();
		}
		
		if (sign2 == '*') {
			temp = num * temp;
		} 
		else if (sign2 == '/') {
			temp = num / temp;
		}
		return temp;
	}
	
	// https://discuss.leetcode.com/topic/38800/share-my-clean-java-solution-o-1-space-no-stack-beats-99-72
	
	/*
	 * by myself
	 */
	public int calculate_self(String s) {
        s = s.replaceAll(" ", "");
        int res = 0;
        int tmp = 0;
        LinkedList<Integer> stack = new LinkedList<>();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '+') {
                stack.add(res);
                stack.add(1);
                res = 0;
                i++;
            }
            else if (s.charAt(i) == '-') {
                stack.add(res);
                stack.add(-1);
                res = 0;
                i++;
            }
            else if (s.charAt(i) == '*') {
                i++;
                while (i < s.length() && 0 <= s.charAt(i) - '0' && s.charAt(i) - '0' <= 9) {
                    tmp = tmp * 10 + (s.charAt(i) - '0');
                    i++;
                }
                res *= tmp;
                tmp = 0;
            }
            else if (s.charAt(i) == '/') {
                i++;
                while (i < s.length() && 0 <= s.charAt(i) - '0' && s.charAt(i) - '0' <= 9) {
                    tmp = tmp * 10 + (s.charAt(i) - '0');
                    i++;
                }
                res /= tmp;
                tmp = 0;
            }
            else {
                res = res * 10 + (s.charAt(i) - '0');
                i++;
            }
        }
        if (stack.isEmpty())
            return res;
        
        tmp = stack.removeFirst();
        while (stack.size() != 1) {
            tmp = tmp + stack.removeFirst() * stack.removeFirst();
        }
        tmp = tmp + stack.removeFirst() * res;
        return tmp;
    }
	
	/*
	 * by myself. Combine postfix notation
	 */
	public int calculate_self_postfix(String s) {
        LinkedList<Integer> stack = new LinkedList<>();
        LinkedList<Character> operator = new LinkedList<>();
        int i = 0;
        int res = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                stack.add(res);
                while (!operator.isEmpty()) {
                    int a = stack.removeLast();
                    int b = stack.removeLast();
                    char op = operator.removeLast();
                    
                    if (op == '/') 
                        b /= a;
                    else if (op == '*') 
                        b *= a;
                    else if (op == '-') 
                        b -= a;
                    else 
                        b += a;
                    
                    stack.add(b);
                }
                operator.add(s.charAt(i));
                res = 0;
                i++;
            }
            else if (s.charAt(i) == '*' || s.charAt(i) == '/') {
                stack.add(res);
                while (!operator.isEmpty() && (operator.getLast() == '*' || operator.getLast() == '/')) {
                    int a = stack.removeLast();
                    int b = stack.removeLast();
                    char op = operator.removeLast();
                    
                    if (op == '/') 
                        b /= a;
                    else  
                        b *= a;
                    
                    stack.add(b);
                }
                operator.add(s.charAt(i));
                res = 0;
                i++;
            }
            else if (s.charAt(i) == ' ') {
                i++;
            }
            else {
                res = res * 10 + (s.charAt(i) - '0');
                i++;
            }
        }
        stack.add(res);
        while (stack.size() != 1) {
            int a = stack.removeLast();
            int b = stack.removeLast();
            char op = operator.removeLast();
            
            if (op == '/') 
                b /= a;
            else if (op == '*') 
                b *= a;
            else if (op == '-') 
                b -= a;
            else 
                b += a;
            
            stack.add(b);
        }
        return stack.getLast();
    }
	
	/*
	 * The following 2 variables and 3 functions are modified by myself. 
	 */
	private StringTokenizer tokenizer;
	private String token;
    public int calculate_modified(String s) {
        s = s.replaceAll(" ", "");
        tokenizer = new StringTokenizer(s, "+-*/", true);
		token = tokenizer.nextToken();
        return Expression();
    }
    private int Term() {
		int result = Integer.parseInt(token);
        if (tokenizer.hasMoreTokens())
            token = tokenizer.nextToken();

		while (token.equals("*") || token.equals("/")) {
			if (token.equals("*")) {
				token = tokenizer.nextToken();
				result *= Integer.parseInt(token);
			} 
			else if (token.equals("/")) {
				token = tokenizer.nextToken();
				result /= Integer.parseInt(token);
			}
            if (tokenizer.hasMoreTokens())
                token = tokenizer.nextToken();
		}

		return result;
	}
    private int Expression() {
		int nextValue;
		int result = Term();

		while (token.equals("+") || token.equals("-")) {
			if (token.equals("+")) {
				token = tokenizer.nextToken();
				nextValue = Term();
				result += nextValue;
			} 
			else if (token.equals("-")) {
				token = tokenizer.nextToken();
				nextValue = Term();
				result -= nextValue;
			}
		}

		return result;
	}
    
    // https://discuss.leetcode.com/topic/16803/easy-7-12-lines-three-solutions

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
