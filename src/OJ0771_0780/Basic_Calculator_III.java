package OJ0771_0780;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;

public class Basic_Calculator_III {
	/*
	 * The following 3 functions are modified by myself.
	 * 
	 * Shunting Yard Algorithm
	 * 
	 * 當某個符號準備 push 到符號 stack 的時候要進行判斷，
	 * 確保 stack 中 "壓" 在自己下的符號的優先級要比自己低，
	 * 否則需要將 stack.top pop 出來執行計算。
	 * 然後將計算結果 push 到數字 stack 中
	 * 
	 * 若 stack.top 優先級和自己的一樣，優先讓它先 “走”（執行運算）
	 * 
	 * 遇到 ()，就相當於進入到子 stack 運算環節，() 越深表明優先級越高，越需要先計算；
	 * 計算完成之後，肯定變成一個數字，之後再進入到其父單元的四則運算邏輯中
	 * 
	 * 遇到 "(" 表明進入子 stack，遇到 ")" 表示該子 stack 完結，
	 * 需要將子 stack 裡殘餘的符號都清空，將該子 stack 中運算執行完變成 子計算結果值 放到數字 stack 中
	 * 
	 * 括號的優先級最高，但由於它表示的 子 stack 的分隔符
	 * （左括號表示子 stack 的開始，右括號表示子 stack 的結束），所以不參與符號優先級的對比
	 * 
	 * 如果遇到 "(" 也先壓入符號 stack，直到遇到 第一個 ")"，表明是一個子 stack 結束，
	 * 依次彈出符號 stack 中元素做運算直到遇到 "(" 為止
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/276366
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/395431
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks
	 * https://boycgit.github.io/algorithm-shunting-yard/
	 * https://www.geeksforgeeks.org/expression-evaluation/
	 */
	public int calculate_2Stack_modify(String s) {
		Deque<Integer> operands = new ArrayDeque<>();
		Deque<Character> operators = new ArrayDeque<>();
		
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			
			if (Character.isDigit(c)) {
				int val = s.charAt(i) - '0';
				while (i + 1 < n && Character.isDigit(s.charAt(i + 1))) {
					val = val * 10 + s.charAt(i + 1) - '0';
					i++;
				}
				
				operands.offerLast(val);
			} 
			else if (c == ' ') {
				continue;
			} 
			else if (c == '(') {
				operators.offerLast(c);
			} 
			else if (c == ')') {
				while (operators.peekLast() != '(') {
					operands.offerLast(operate_2Stack_modify(operands, operators));
				}
				
				// remove "("
				operators.pollLast();
			} 
			else if (c == '+' || c == '-' || c == '*' || c == '/') {
				while (!operators.isEmpty() && operators.peekLast() != '(' 
				  && comparePrecedence_2Stack_modify(c, operators.peekLast()) <= 0) {
					
					operands.offerLast(operate_2Stack_modify(operands, operators));
				}
				
				operators.offerLast(c);
			}
		}

		while (!operators.isEmpty()) {
			operands.offerLast(operate_2Stack_modify(operands, operators));
		}

		return operands.pollLast();
	}

	private int comparePrecedence_2Stack_modify(char a, char b) {
		Map<Character, Integer> map = new HashMap<>();
		map.put('(', -1);
		map.put('+', 0);
		map.put('-', 0);
		map.put('*', 1);
		map.put('/', 1);
		
		return map.get(a) - map.get(b);
	}
    
	private int operate_2Stack_modify(Deque<Integer> operands, 
			Deque<Character> operators) {
		
		int a = operands.pollLast();
		int b = operands.pollLast();
		char c = operators.pollLast();

		switch (c) {
            case '+': return b + a;
            case '-': return b - a;
            case '*': return b * a;
            case '/': return b / a;
            default: return 0;
        }
    }
	
	/*
	 * Modified by myself
	 * 
	 * sign 是現在正在處理的 num 前面的運算符號
	 * 碰到 +, - 直接處理正負，放入 stack
	 * 碰到 *, / 從 stack 末端取出數字運算完再放入 stack
	 * 
	 * 碰到 () 使用 recursive call 來得到結果
	 * 因為 () 是運算後的結果以及 ( 前面必定是運算符號，所以可以同樣用 num 來儲存
	 * 
	 * stack 就是已經處理完 *, / 這些高優先的運算
	 * 全部加起來就是答案
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113603/Java-StraightForward
	 * https://leetcode.com/problems/basic-calculator-ii/discuss/63003/share-my-java-solution
	 * https://discuss.leetcode.com/topic/16935/share-my-java-solution/19
	 * https://leetcode.com/problems/basic-calculator/discuss/62362/java-easy-version-to-understand
	 */
	public int calculate_stack_recur_modify(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int num = 0;
        char sign = '+';
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                continue;
            }
            
            if (Character.isDigit(s.charAt(i))) {
                num = s.charAt(i) - '0';
                while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    num = num * 10 + (s.charAt(i + 1) - '0');
                    i++;
                }
            }
            
            if (s.charAt(i) == '(') {
                int open = 1;
                int j = i + 1;
                
                while (j < s.length()) {
                    if (s.charAt(j) == '(') {
                        open++;
                    }
                    else if (s.charAt(j) == ')') {
                        open--;
                    }
                    
                    if (open == 0) {
                        break;
                    }
                    
                    j++;
                }
                
                num = calculate_stack_recur_modify(s.substring(i + 1, j));
                i = j + 1;
            }
            
            if ((i < s.length() && !Character.isDigit(s.charAt(i))) 
            		|| i >= s.length() - 1) {
                
                if (sign == '+') {
                    stack.offerLast(num);
                }
                else if (sign == '-') {
                    stack.offerLast(-num);
                }
                else if (sign == '*') {
                    int result = stack.pollLast() * num;
                    stack.offerLast(result);
                }
                else {
                    int result = stack.pollLast() / num;
                    stack.offerLast(result);
                }
                
                if (i < s.length()) {
                    sign = s.charAt(i);
                    num = 0;
                }
            }
        }
        
        int ans = 0;
        for (int i : stack) {
            ans += i;
        }
        return ans;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/276366
	 * 
	 * polish notation, reverse polish notion, expression tree
	 * 
	 * -----------------------------------------------------------------
	 * 
	 * We can view a negative number -N as (0 - N). Negative numbers can exist at 
	 * the beginning of the expression, or after (.
	 * So we add an 0 to operands at start. And when never we see (, we check if the 
	 * next non-empty character is -, if so, we add an 0 to operands.
	 * 
	 * -----------------------------------------------------------------
	 * 
	 * In `private int operate(Deque<Integer> operands, Deque<Character> operators)`, 
	 * we can update `int b = operands.pop();` to 
	 * `int b = operands.isEmpty() ? 0 : operands.pop();` to handle the case 
	 * where - is a unary operator (that denotes a negative number).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/841532
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/395431
	 * 
	 * Other code:
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/289012
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/906554
	 */
	public int calculate_2Stack(String s) {
		Deque<Integer> operands = new ArrayDeque<>();
		Deque<Character> operators = new ArrayDeque<>();
		
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			
			if (Character.isDigit(c)) {
				int val = Character.getNumericValue(s.charAt(i));
				while (i + 1 < n && Character.isDigit(s.charAt(i + 1))) {
					val = val * 10 + Character.getNumericValue(s.charAt(i + 1));
					i++;
				}
				
				operands.push(val);
			} 
			else if (c == ' ') {
				continue;
			} 
			else if (c == '(') {
				operators.push(c);
			} 
			else if (c == ')') {
				while (operators.peek() != '(') {
					operands.push(operate_2Stack(operands, operators));
				}
				
				operators.pop();
			} 
			// +, -, *, /
			else {
				while (!operators.isEmpty() 
						&& comparePrecedence_2Stack(c, operators.peek()) <= 0) {
					
					operands.push(operate_2Stack(operands, operators));
				}
				
				operators.push(c);
			}
		}

		while (!operators.isEmpty()) {
			operands.push(operate_2Stack(operands, operators));
		}

		return operands.pop();
	}

	private int comparePrecedence_2Stack(char a, char b) {
		Map<Character, Integer> map = new HashMap<>();
		map.put('(', -1);
		map.put('+', 0);
		map.put('-', 0);
		map.put('*', 1);
		map.put('/', 1);
		return map.get(a) - map.get(b);
	}
    
	private int operate_2Stack(Deque<Integer> operands, Deque<Character> operators) {
		int a = operands.pop();
		int b = operands.pop();
		char c = operators.pop();

		switch (c) {
            case '+': return a + b;
            case '-': return b - a;
            case '*': return a * b;
            case '/': return b / a;
            default: return 0;
        }
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks
	 * 
	 * To deal with "-1+4*3/3/3": add 0 to num stack if the first char is '-';
	 * To deal with "1-(-7)": add 0 to num stack if the first char after '(' is '-'.
	 * 
	 * If we have a + b * c. Num stack is a and b and op stack is ' + ' when we have 
	 * current c as ' * ' , the precedence check prevent us from doing a + b first
	 * 
	 * Reset num = 0 is not necessary and in precedence method, "op2==')'" is not 
	 * necessary.
	 * 
	 * Rf :
	 * https://www.geeksforgeeks.org/expression-evaluation/
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/317165
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/279525
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/180228
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/181526
	 * 
	 * Other code:
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/318829
	 */
	public int calculate4(String s) {
		if (s == null || s.length() == 0)
			return 0;
		
		// the stack that stores numbers
		Stack<Integer> nums = new Stack<>();
		
		// the stack that stores operators (including parentheses)
		Stack<Character> ops = new Stack<>();
		
		int num = 0;
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ' ') {				
				continue;
			}
			
			if (Character.isDigit(c)) {
				num = c - '0';
				
				// iteratively calculate each number
				while (i < s.length() - 1 && Character.isDigit(s.charAt(i + 1))) {
					num = num * 10 + (s.charAt(i + 1) - '0');
					i++;
				}
				nums.push(num);
				
				// reset the number to 0 before next calculation
				// not necessary
				num = 0;
			} 
			else if (c == '(') {
				ops.push(c);
			} 
			else if (c == ')') {
				
				// do the math when we encounter a ')' until '('
				while (ops.peek() != '(') {					
					nums.push(operation4(ops.pop(), nums.pop(), nums.pop()));
				}
				
				// get rid of '(' in the ops stack
				ops.pop();
			} 
			else if (c == '+' || c == '-' || c == '*' || c == '/') {
				while (!ops.isEmpty() && precedence4(c, ops.peek())) {					
					nums.push(operation4(ops.pop(), nums.pop(), nums.pop()));
				}
				
				ops.push(c);
			}
		}

		while (!ops.isEmpty()) {
			nums.push(operation4(ops.pop(), nums.pop(), nums.pop()));
		}
		return nums.pop();
	}

    private int operation4(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b; // assume b is not 0
        }
        
        return 0;
    }
    
    // helper function to check precedence of current operator and 
    // the uppermost operator in the ops stack 
	private boolean precedence4(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
			return false;
		
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113603/Java-StraightForward
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113603/Java-StraightForward/261830
	 */
	public int calculate_recur_stack(String s) {
		
		// remove all occurrences of 1 or more consecutive space character.
		s = s.replaceAll("\\s+", "");
		
		Stack<Integer> stack = new Stack<>();
		char sign = '+';
		
		for (int i = 0; i < s.length();) {
			char c = s.charAt(i);
			
			if (c == '(') {
				// find the block and use the recursive to solve
				int l = 1;
				int j = i + 1;
				while (j < s.length() && l > 0) {
					if (s.charAt(j) == '(')
						l++;
					else if (s.charAt(j) == ')')
						l--;
					
					j++;
				}
				
				int blockValue = calculate_recur_stack(s.substring(i + 1, j - 1));
				i = j;
				
				if (sign == '+') {
					stack.push(blockValue);
				} 
				else if (sign == '-') {
					stack.push(-blockValue);
				} 
				else if (sign == '*') {
					stack.push(stack.pop() * blockValue);
				} 
				else if (sign == '/') {
					stack.push(stack.pop() / blockValue);
				}
			} 
			else if (Character.isDigit(c)) {
				int j = i;
				int value = 0;
				while (j < s.length() && Character.isDigit(s.charAt(j))) {
					value = 10 * value + (s.charAt(j) - '0');
					j++;
				}
				
				i = j;
				
				if (sign == '+') {
					stack.push(value);
				} 
				else if (sign == '-') {
					stack.push(-value);
				} 
				else if (sign == '*') {
					stack.push(stack.pop() * value);
				} 
				else if (sign == '/') {
					stack.push(stack.pop() / value);
				}
			} 
			else {
				sign = c;
				i++;
			}
		}
		
		int res = 0;
		while (!stack.isEmpty()) {
			res += stack.pop();
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113590/Java-O(n)-using-two-stack
	 * 
	 * We go through expression once, and for each character, and there are 5 cases
	 * case1: digit:
	 *    case1.1: if operator now is '+', push digit
	 *    case1.2: if operator now is '-', push -digit
	 *    case1.3: if operator now is '*', push (poll * digit)
	 *    case1.4: if operator now is '/', push (poll / digit)
	 * case2: space: do nothing
	 * case3: operators: update operator
	 * case4: (: push the operator into stack2, push '(' into stack1 (use +inf as '(')
	 * case5: ): continues poll and sum polled value until poll '(', then poll the 
	 *           operator from stack2, do calculate, then push back to stack1
	 * 
	 * Use stack1 to store digit and '(', why use long? because we want to use 
	 * Long.MAX_VALUE to represent special char '('
	 * Use stack2 to store operator before '('
	 */
	public int calculate2(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		// initialize operator
		char sign = '+';
		
		// store digit and '('
		Deque<Long> stack1 = new LinkedList<>();
		
		// store sign before '('
		Deque<Character> stack2 = new LinkedList<>();
		
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			
			if (Character.isDigit(ch)) {
				long num = 0;
				while (i < s.length() && Character.isDigit(s.charAt(i))) {
					num = num * 10 + s.charAt(i++) - '0';
				}
				
				i--;
				
				stack1.offerFirst(eval2(sign, stack1, num));
			} 
			else if (ch == ' ') {
				continue;
			} 
			else if (ch == '(') {
				stack1.offerFirst(Long.MAX_VALUE);
				stack2.offerFirst(sign);
				
				sign = '+';
			} 
			else if (ch == ')') {
				long num = 0;
				while (stack1.peekFirst() != Long.MAX_VALUE) {
					num += stack1.pollFirst();
				}
				
				// pop out '(' (Long.MAX_VALUE)
				stack1.pollFirst();
				
				char operator = stack2.pollFirst();
				stack1.offerFirst(eval2(operator, stack1, num));
			} 
			else {
				sign = ch;
			}
		}
		
		// what we need to do is just sum up all num in stack
		int result = 0;
		while (!stack1.isEmpty()) {
			result += stack1.pollFirst();
		}
		return result;
	}

	private long eval2(char sign, Deque<Long> stack1, long num) {
		if (sign == '+') {
			return num;
		} 
		else if (sign == '-') {
			return -num;
		} 
		else if (sign == '*') {
			return stack1.pollFirst() * num;
		} 
		else {
			return stack1.pollFirst() / num;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/202979/A-generic-solution-for-Basic-Calculator-I-II-III
	 * 
	 * We use two stacks to store our operators and operands that haven't been 
	 * processed. When we get a new number, we just push it into numStack and wait 
	 * for an operator to process it. Each operator has a priority and when we get a 
	 * new operator, we can exhaust all the operators on the top of the stack that 
	 * has higher or equal priority to the current operator.
	 * 
	 * What we need to pay attention to is that only ')' can exhaust '(', so when we 
	 * peek '(' on the top of opStack, we will stop the exhausting.
	 * 
	 * Shunting Yard Algorithm
	 * 
	 * Rf :
	 * https://boycgit.github.io/algorithm-shunting-yard/
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/202979/A-generic-solution-for-Basic-Calculator-I-II-III/368802
	 */
	public int calculate3(String s) {
		LinkedList<Character> opStack = new LinkedList<>();
		LinkedList<Integer> numStack = new LinkedList<>();

		Map<Character, Integer> map = new HashMap<>();
		map.put('+', 0);   map.put('-', 0);
		map.put('*', 1);   map.put('/', 1);
		map.put('(', 2);   map.put(')', -1);

		s = s + "+";
		char[] str = s.toCharArray();

		int i = 0;
		while (i < str.length) {
			if (Character.isDigit(str[i])) {
				int num = 0;
				while (i < str.length && Character.isDigit(str[i])) {
					num = num * 10 + (str[i] - '0');
					i++;
				}
				numStack.push(num);
			} 
			else if (str[i] == '+' || str[i] == '-' || 
                     str[i] == '*' || str[i] == '/' || 
                     str[i] == '(' || str[i] == ')') {
                
				while (!opStack.isEmpty() && 
                       opStack.peek() != '(' 
                       && map.get(opStack.peek()) >= map.get(str[i])) {
					
					int num1 = numStack.pop();
					int num2 = numStack.pop();
					numStack.push(getRes3(num2, num1, opStack.pop()));
				}
				
				if (str[i] == ')')
					opStack.pop();
				else
					opStack.push(str[i]);
				
				i++;
			} 
			else {
				i++;
			}
		}
		return numStack.pop();
	}

	private int getRes3(int i, int j, char op) {
		switch (op) {
		case '+':
			return i + j;
		case '-':
			return i - j;
		case '*':
			return i * j;
		case '/':
			return i / j;
		}
		return Integer.MIN_VALUE;
	}
	
	/*
	 * The following 5 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/202979/A-generic-solution-for-Basic-Calculator-I-II-III
	 * 
	 * We use two stacks to store our operators and operands that haven't been 
	 * processed. When we get a new number, we just push it into numStack and wait 
	 * for an operator to process it. Each operator has a priority and when we get a 
	 * new operator, we can exhaust all the operators on the top of the stack that 
	 * has higher or equal priority to the current operator.
	 * 
	 * What we need to pay attention to is that only ')' can exhaust '(', so when we 
	 * peek '(' on the top of opStack, we will stop the exhausting.
	 * 
	 * To process plus sign '+' and minus sign '-' , we just need to change the 
	 * original code a little. All we have to do is that when we see a '+' or '-' 
	 * sign , we push an additional 0 into the numStack. Then we exhaust the 
	 * opStack as usual.
	 * 
	 * Shunting Yard Algorithm
	 * 
	 * Rf :
	 * https://boycgit.github.io/algorithm-shunting-yard/
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/202979/A-generic-solution-for-Basic-Calculator-I-II-III/368802
	 */
	public int calculate_sign(String s) {
		LinkedList<Character> opStack = new LinkedList<>();
		LinkedList<Integer> numStack = new LinkedList<>();
		
		Map<Character, Integer> map = new HashMap<>();
		map.put('+', 0);   map.put('-', 0);
		map.put('*', 1);   map.put('/', 1);
		map.put('(', 2);   map.put(')', -1);
		
		char[] str = getCleanCharArray_sign(s);
		int i = 0;
		
		while (i < str.length) {
			if (Character.isDigit(str[i])) {
				int num = 0;
				while (i < str.length && Character.isDigit(str[i])) {
					num = num * 10 + (str[i] - '0');
					i++;
				}
				numStack.push(num);
			} 
			else if (str[i] == '+' || str[i] == '-' || 
                     str[i] == '*' || str[i] == '/' || 
                     str[i] == '(' || str[i] == ')') {
				
                // judge that if the str[i] is a + or - sign instead of operator
				// if the char is a sign , add 0 to process the sign
				if (isSign_sign(i, str)) {
					numStack.push(0);
				}
				
				while (!opStack.isEmpty() && 
                       opStack.peek() != '(' 
                       && map.get(opStack.peek()) >= map.get(str[i])) {
					
					int num1 = numStack.pop();
					int num2 = numStack.pop();
					numStack.push(getRes_sign(num2, num1, opStack.pop()));
				}
				
				if (str[i] == ')')
					opStack.pop();
				else
					opStack.push(str[i]);
				
				i++;
			} 
			else {
				i++;
			}
		}
		
		while (!opStack.isEmpty()) {
			int num1 = numStack.pop();
			int num2 = numStack.pop();
			numStack.push(getRes_sign(num2, num1, opStack.pop()));
		}
		return numStack.pop();
	}

	private int getRes_sign(int i, int j, char op) {
		switch (op) {
		case '+':
			return i + j;
		case '-':
			return i - j;
		case '*':
			return i * j;
		case '/':
			return i / j;
		}
		return Integer.MIN_VALUE;
	}

	private char[] getCleanCharArray_sign(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (isValidChar_sign(str.charAt(i))) {
				sb.append(str.charAt(i));
			}
		}

		return sb.toString().toCharArray();
	}

	private boolean isValidChar_sign(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' 
				|| c == '(' || c == ')' || Character.isDigit(c);
	}

	private boolean isSign_sign(int idx, char[] str) {
		if (str[idx] == '+' || str[idx] == '-') {
			if (idx == 0)
				return true;
			else {
				if (!Character.isDigit(str[idx - 1]) && str[idx - 1] != ')') {
					return true;
				} 
				else {
					return false;
				}
			}
		} 
		else {
			return false;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/202979/A-generic-solution-for-Basic-Calculator-I-II-III/443739
	 * 
	 * We evaluate expressions from the left to the right. When we see two adjacent 
	 * operators, we can't do the first operation directly unless the precedence of 
	 * the second operation is equal to or less than the first operator.
	 */
	class Solution_precedence {
		Map<Character, Integer> precedence;

		public int calculate(String s) {
			if (s == null || s.length() == 0)
				return 0;

			precedence = new HashMap<>();
			precedence.put('+', 1);
			precedence.put('-', 1);
			precedence.put('*', 2);
			precedence.put('/', 2);
			precedence.put('(', 0); // !!

			return evalExpression(s);
		}

		private int evalExpression(String s) {
			// Assert s is valid
			Stack<Character> ops = new Stack<>();
			Stack<Integer> values = new Stack<>();

			char[] tokens = s.toCharArray();
			char pre = ' ';
			for (int i = 0; i < tokens.length; i++) {
				char c = tokens[i];
				if (c == ' ') {
					continue;
				}

				if (Character.isDigit(c)) {
					int num = 0;
					while (i < tokens.length && Character.isDigit(tokens[i]))
						num = 10 * num + tokens[i++] - '0';
					
					values.push(num);
					i--;
				} 
				else if (c == '(') {
					ops.push(c);
				} 
				else if (c == ')') {
					while (ops.peek() != '(')
						values.push(applyOp(ops.pop(), values.pop(), values.pop()));
				
					// pop out '('
					ops.pop();
				} 
				else if (c == '+' || c == '-' || c == '*' || c == '/') {
					// Dealing with the negative number
					if (i == 0 && c == '-') {
						values.push(0);
					} 
					else if (pre == '(' && c == '-') {
						values.push(0);
					}

					// While top of 'ops' has same or greater precedence to current
					// token, which is an operator. Apply operator on top of 'ops'
					// to top two elements in values stack
					while (!ops.isEmpty() 
							&& precedence.get(c) <= precedence.get(ops.peek())) {
						
						values.push(applyOp(ops.pop(), values.pop(), values.pop()));
					}

					ops.push(c);
				}
				pre = c;
			}

			while (!ops.isEmpty())
				values.push(applyOp(ops.pop(), values.pop(), values.pop()));

			return values.pop();
		}

		private int applyOp(Character op, int b, int a) { // b is before a (!!)
			switch (op) {
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/': {
				assert b != 0;
				return a / b;
			}
			}
			return Integer.MIN_VALUE;
		}
	}
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113600/Java-and-Python-O(n)-Solution-Using-Two-Stacks/887965
	 */
	public int calculate_stack(String s) {
		Deque<Integer> numStack = new ArrayDeque<>();
		Deque<Character> opStack = new ArrayDeque<>();

		int ans = 0;
		int preNum = 0;
		int curNum = 0;
		char op = '+';

		for (int i = 0; i <= s.length(); i++) {
			char c = i < s.length() ? s.charAt(i) : '+';
			
			if (Character.isDigit(c)) {
				curNum = curNum * 10 + c - '0';
            }
			else if (c == '(') {
				/*
				 * 2+3*(1+2) ans=5, prev=3, op=*
				 */
				numStack.push(preNum);
				numStack.push(ans);
				opStack.push(op);
				
				ans = preNum = curNum = 0;
				op = '+';
            }
			// +-*/ and ')'
			else {
				if (op == '+') {
					ans = ans + curNum;
					preNum = curNum;
                }
				else if (op == '-') {
					ans = ans - curNum;
					preNum = -curNum;
                }
				else if (op == '*') {
					ans = ans - preNum + preNum * curNum;
					preNum = preNum * curNum;
                }
				else { // '/'
					ans = ans - preNum + preNum / curNum;
					preNum = preNum / curNum;
                }
                curNum = 0;
                op = c;
                
				if (c == ')') {
					/*
					 * 2+3*(1+2) ans=3, prev=2, op=')'
					 */
					curNum = ans;
					ans = numStack.pop();
					preNum = numStack.pop();
					op = opStack.pop();
				}
            }
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems
	 * 
	 * + and - have level one precedence, while * and / have level two precedence 
	 * (the higher the level is, the higher the precedence is).
	 * 
	 * For level one, the partial result starts from 0 and the initial operator in 
	 * effect is +; for level two, the partial result starts from 1 and the initial 
	 * operator in effect is *.
	 * 
	 * We will use l1 and o1 to denote respectively the partial result and the 
	 * operator in effect for level one; l2 and o2 for level two. 
	 * o1 == 1 means +; o1 == -1 means - ;
	 * o2 == 1 means *; o2 == -1 means /.
	 * By default we have l1 = 0, o1 = 1, and l2 = 1, o2 = 1.
	 * 
	 * Each operand in the expression will be associated with a precedence of level 
	 * two by default, meaning they can only take part in calculations of precedence 
	 * level two, not level one.
	 * 
	 * The partial result l2 of precedence level two can be demoted to level one. 
	 * Upon demotion, l2 becomes the operand for precedence level one and will be 
	 * evaluated together with l1 under the action prescribed by o1.
	 * 
	 * The demotion happens when either a level one operator (i.e., + or -) is hit 
	 * or the end of the expression is reached. After demotion, l2 and o2 will be 
	 * reset for following calculations.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/290023
	 */
	public int basicCalculatorIII_recur(String s) {
	    int l1 = 0, o1 = 1;
	    int l2 = 1, o2 = 1;
	        
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	            
	        if (Character.isDigit(c)) {
	            int num = c - '0';
	                
	            while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
	                num = num * 10 + (s.charAt(++i) - '0');
	            }
	            
	            l2 = (o2 == 1 ? l2 * num : l2 / num);
	                
	        } 
	        else if (c == '(') {
				int j = i;

				for (int cnt = 0; i < s.length(); i++) {
					if (s.charAt(i) == '(')
						cnt++;
					if (s.charAt(i) == ')')
						cnt--;
					if (cnt == 0)
						break;
				}

				int num = basicCalculatorIII_recur(s.substring(j + 1, i));

				l2 = (o2 == 1 ? l2 * num : l2 / num);
	                
	        } 
	        else if (c == '*' || c == '/') {
	            o2 = (c == '*' ? 1 : -1);
	        } 
	        else if (c == '+' || c == '-') {
	            l1 = l1 + o1 * l2;
	            o1 = (c == '+' ? 1 : -1);

	            l2 = 1; o2 = 1;
	        }
	    }
	        
	    return (l1 + o1 * l2);
	}
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems
	 * 
	 * + and - have level one precedence, while * and / have level two precedence 
	 * (the higher the level is, the higher the precedence is).
	 * 
	 * For level one, the partial result starts from 0 and the initial operator in 
	 * effect is +; for level two, the partial result starts from 1 and the initial 
	 * operator in effect is *.
	 * 
	 * We will use l1 and o1 to denote respectively the partial result and the 
	 * operator in effect for level one; l2 and o2 for level two. 
	 * o1 == 1 means +; o1 == -1 means - ;
	 * o2 == 1 means *; o2 == -1 means /.
	 * By default we have l1 = 0, o1 = 1, and l2 = 1, o2 = 1.
	 * 
	 * Each operand in the expression will be associated with a precedence of level 
	 * two by default, meaning they can only take part in calculations of precedence 
	 * level two, not level one.
	 * 
	 * The partial result l2 of precedence level two can be demoted to level one. 
	 * Upon demotion, l2 becomes the operand for precedence level one and will be 
	 * evaluated together with l1 under the action prescribed by o1.
	 * 
	 * The demotion happens when either a level one operator (i.e., + or -) is hit 
	 * or the end of the expression is reached. After demotion, l2 and o2 will be 
	 * reset for following calculations.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113592/Development-of-a-generic-solution-for-the-series-of-the-calculator-problems/290023
	 */
	public int basicCalculatorIII_stack(String s) {
		int l1 = 0, o1 = 1;
		int l2 = 1, o2 = 1;

		// stack to simulate recursion
		Deque<Integer> stack = new ArrayDeque<>();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (Character.isDigit(c)) {
				int num = c - '0';

	            while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
	                num = num * 10 + (s.charAt(++i) - '0');
	            }

	            l2 = (o2 == 1 ? l2 * num : l2 / num);

	        } 
			else if (c == '(') {
	            // First preserve current calculation status
	            stack.offerFirst(l1); stack.offerFirst(o1);
	            stack.offerFirst(l2); stack.offerFirst(o2);
	            
	            // Then reset it for next calculation
	            l1 = 0; o1 = 1;
	            l2 = 1; o2 = 1;

	        } 
			else if (c == ')') {
	            // First preserve the result of current calculation
	            int num = l1 + o1 * l2;

	            // Then restore previous calculation status
	            o2 = stack.poll(); l2 = stack.poll();
	            o1 = stack.poll(); l1 = stack.poll();
	            
	            // Previous calculation status is now in effect
	            l2 = (o2 == 1 ? l2 * num : l2 / num);

	        } 
			else if (c == '*' || c == '/') {
	            o2 = (c == '*' ? 1 : -1);
	        } 
			else if (c == '+' || c == '-') {
	            l1 = l1 + o1 * l2;
	            o1 = (c == '+' ? 1 : -1);

	            l2 = 1; o2 = 1;
	        }
	    }

	    return (l1 + o1 * l2);
	}
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/129179/Java-Solution-with-little-adjustment-of-Basic-Calculator-II/174373
	 */
	public int calculate_recur2(String s) {
		int preVal = 0, res = 0;
		char preSign = '+';

		int n = s.length();
		for (int i = 0; i < n; i++) {
			char ch = s.charAt(i);
			if (ch == ' ')
				continue;

			if (Character.isDigit(ch) || ch == '(') {
				int val = 0;
				
				if (ch == '(') {
					int j = i;
					int count = 1;
					while (count != 0) {
						if (s.charAt(j + 1) == '(')
							count++;
						else if (s.charAt(j + 1) == ')')
							count--;
						j++;
					}
					
					val += calculate_recur2(s.substring(i + 1, j));
					i = j;
				} 
				else {
					val = ch - '0';
					while (i + 1 < n && Character.isDigit(s.charAt(i + 1))) {
						val = val * 10 + s.charAt(i + 1) - '0';
						i++;
					}
				}

				if (preSign == '+') {
					res += preVal;
					preVal = val;
				} 
				else if (preSign == '-') {
					res += preVal;
					preVal = -val;
				} 
				else if (preSign == '*') {
					preVal *= val;
				} 
				else if (preSign == '/') {
					preVal /= val;
				}
			} 
			else {
				preSign = ch;
			}
		}

		return res + preVal;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/129179/Java-Solution-with-little-adjustment-of-Basic-Calculator-II/174373
	 */
	public int calculate_recur5(String s) {
		return calculate_recur5(s, 0)[0];
	}

	private int[] calculate_recur5(String s, int start) {
		int preVal = 0, res = 0;
		char preSign = '+';

		int n = s.length();
		for (int i = start; i < n; i++) {
			char ch = s.charAt(i);
			if (ch == ' ')
				continue;

			if (Character.isDigit(ch) || ch == '(') {
				int val = 0;
				
				if (ch == '(') {
					int[] ret = calculate_recur5(s, i + 1);
					val += ret[0];
					i = ret[1];
				} 
				else {
					val = ch - '0';
					while (i + 1 < n && Character.isDigit(s.charAt(i + 1))) {
						val = val * 10 + s.charAt(i + 1) - '0';
						i++;
					}
				}

				if (preSign == '+') {
					res += preVal;
					preVal = val;
				} 
				else if (preSign == '-') {
					res += preVal;
					preVal = -val;
				} 
				else if (preSign == '*') {
					preVal *= val;
				} 
				else if (preSign == '/') {
					preVal /= val;
				}
			} 
			else if (ch == ')') {
				return new int[] { res + preVal, i };
			} 
			else {
				preSign = ch;
			}
		}

		return new int[] { res + preVal, n - 1 };
	}

	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/129179/Java-Solution-with-little-adjustment-of-Basic-Calculator-II
	 * 
	 * The only difference between the solution of Basic Calculator II is we added 
	 * a recursive call of our function when we encounter a parenthesis. Let the 
	 * function calculate the number for us and the scan cursor will increment to 
	 * the next of pairing parentheses.
	 * 
	 * Rf : 
	 * https://www.cnblogs.com/grandyang/p/8873471.html
	 * 
	 * Other code:
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/344371/Java-Common-template-for-Basic-Calculator-I-II-and-III-using-Stack
	 */
	public int calculate_recur_stack2(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		LinkedList<Integer> buff = new LinkedList<>();
		int num = 0;
		char sign = '+';

		int n = s.length();
		for (int i = 0; i < n; ++i) {
			char cur = s.charAt(i);

			if (cur >= '0' && cur <= '9') {
				num = 10 * num + (int) (cur - '0');
			} 
			else if (cur == '(') {
				int j = i + 1;
				int cnt = 1;
				for (; j < n; ++j) {
					if (s.charAt(j) == '(')
						++cnt;
					if (s.charAt(j) == ')')
						--cnt;
					if (cnt == 0)
						break;
				}

				num = calculate_recur_stack2(s.substring(i + 1, j));
				i = j;
			}

			if (cur == '+' || cur == '-' || cur == '*' || cur == '/' || i == n - 1) {
				switch (sign) {
				case '+':
					buff.addFirst(num);
					break;
				case '-':
					buff.addFirst(-num);
					break;
				case '*':
					int tmp = buff.removeFirst() * num;
					buff.addFirst(tmp);
					break;
				case '/':
					int tmp2 = buff.removeFirst() / num;
					buff.addFirst(tmp2);
					break;
				}
				
				num = 0;
				sign = cur;
			}
		}

		int res = 0;
		for (int i : buff) {
			res += i;
		}

		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113596/Java-One-stack-and-recursive
	 */
	public int calculate_recur3(String s) {
		Stack<Long> stack = new Stack<Long>();
		
		int len = s.length();
		int i = 0;
		
		int res = 0;
		long num = 0;
		char sign = '+';
		
		while (i < len) {
			if (Character.isDigit(s.charAt(i))) {
				num = num * 10 + (s.charAt(i) - '0');
			}
			if (s.charAt(i) == '(') {
				int count = countValid_recur3(s.substring(i));
				num = calculate_recur3(s.substring(i + 1, i + count));
				i += count;
			}

			if (i == len - 1 || 
					(s.charAt(i) != ' ' && !Character.isDigit(s.charAt(i)))) {
				
				if (sign == '+')
					stack.push(num);
				else if (sign == '-')
					stack.push(-num);
				else if (sign == '*')
					stack.push(stack.pop() * num);
				else if (sign == '/')
					stack.push(stack.pop() / num);
				
				num = 0;
				sign = s.charAt(i);
			}
			
			i++;
		}

		while (!stack.isEmpty()) {
			res += stack.pop();
		}

		return (int) res;
	}

	int countValid_recur3(String s) {
		int counter = 0;
		int i = 0;
		while (i < s.length()) {
			if (s.charAt(i) == '(')
				counter++;
			else if (s.charAt(i) == ')')
				counter--;
			
			if (counter == 0)
				break;
			
			i++;
		}
		return i;
	}
	
	/*
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/152092/O(n)-Java-Recursive-Simple-Solution/235579
	 */
	public int calculate_recur4(String s) {
		int num = 0, res = 0, i = 0;
		char sign = '+';
		
		Stack<Integer> st = new Stack<>();
		
		while (i <= s.length()) {
			// <= rather than = so as not to skip the last number
			char ch = i == s.length() ? '+' : s.charAt(i);
			i++;
			
			if (ch == ' ')
				continue;
			
			if (ch <= '9' && ch >= '0') {
				num = 10 * num + (ch - '0');
			} 
			else {
				if (ch == '(') {
					// once detect the ( find the matched ) and recursive 
					// result as num
					int left = 1;
					int end = i;
					while (left != 0) {
						if (s.charAt(end) == '(')
							left++;
						if (s.charAt(end) == ')')
							left--;
						
						end++;
					}
					num = calculate_recur4(s.substring(i, end - 1));
					
					// do not forget to push the i forward and skip the substring 
					// part
					i = end;
				} 
				else {
					// once you meet the sign, operate the number based on the 
					// previous sign
					if (sign == '+') {
						st.push(num);
					}
					if (sign == '-') {
						st.push(-num);
					}
					if (sign == '*') {
						int top = st.pop();
						st.push(top * num);
					}
					if (sign == '/') {
						int top = st.pop();
						st.push(top / num);
					}
					
					num = 0;
					sign = ch;
				}
			}
		}
		
		// cumulate all sub result
		while (!st.empty()) {
			res += st.pop();
		}
		
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/152092/O(n)-Java-Recursive-Simple-Solution/227715
	 * 
	 * The reason to add the "+" at the end of the queue is you need to push the 
	 * last number into stack.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/152092/O(n)-Java-Recursive-Simple-Solution/603564
	 */
	public int calculate_no_stack(String s) {
		Queue<Character> tokens = new ArrayDeque<Character>();

		for (char c : s.toCharArray()) {
			if (c != ' ')
				tokens.offer(c);
		}

		tokens.offer('+');
		return calculate_no_stack(tokens);
	}

	private int calculate_no_stack(Queue<Character> tokens) {

		char preOp = '+';
		int num = 0, sum = 0, prev = 0;

		while (!tokens.isEmpty()) {
			char c = tokens.poll();

			if ('0' <= c && c <= '9') {
				num = num * 10 + c - '0';
			} 
			else if (c == '(') {
				num = calculate_no_stack(tokens);
			} 
			else {
				switch (preOp) {
				case '+':
					sum += prev;
					prev = num;
					break;
				case '-':
					sum += prev;
					prev = -num;
					break;
				case '*':
					prev *= num;
					break;
				case '/':
					prev /= num;
					break;
				}

				if (c == ')')
					break;

				preOp = c;
				num = 0;
			}
		}

		return sum + prev;
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/152092/O(n)-Java-Recursive-Simple-Solution
	 * 
	 * The reason to add the "+" at the end of the queue is you need to push the 
	 * last number into stack.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/152092/O(n)-Java-Recursive-Simple-Solution/603564
	 */
	public int calculate_queue_split(String s) {
		if (s == null) {
			return 0;
		}

		Queue<Character> q = new LinkedList<>();
		for (char c : s.toCharArray()) {
			q.offer(c);
		}
		q.offer('+');

		return cal_queue_split(q);
	}

	private int cal_queue_split(Queue<Character> q) {
		char sign = '+';
		int num = 0;
		
		Stack<Integer> stack = new Stack<>();
		
		while (!q.isEmpty()) {
			char c = q.poll();
			if (c == ' ') {
				continue;
			}
			
			if (Character.isDigit(c)) {
				num = 10 * num + c - '0';
			} 
			else if (c == '(') {
				num = cal_queue_split(q);
			} 
			else {
				if (sign == '+') {
					stack.push(num);
				} 
				else if (sign == '-') {
					stack.push(-num);
				} 
				else if (sign == '*') {
					stack.push(stack.pop() * num);
				} 
				else if (sign == '/') {
					stack.push(stack.pop() / num);
				}
				
				num = 0;
				sign = c;
				
				if (c == ')') {
					break;
				}
			}
		}
		
		int sum = 0;
		while (!stack.isEmpty()) {
			sum += stack.pop();
		}
		return sum;
	}

	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/686492/Optimal-Generic-Solution-for-Basic-Calculator-I-II-III-Time-and-Space-O(N)
	 */
	int i_recur = 0;

	public int calculate_recur(String s) {
		if (s == null || s.length() == 0)
			return 0;
		
		int result = 0, tmp = 0, num = 0;
		char op = '+';

		while (i_recur < s.length()) {
			char c = s.charAt(i_recur++);
			
			if (Character.isDigit(c)) {
				tmp = tmp * 10 + c - '0';
			} 
			// string parse index is tracked by i
			else if (c == '(') {
				tmp = calculate_recur(s);
			} 
			else if (c == ')') {
				break;
			} 
			else if (c != ' ') {
				// process the numerical value of string so far; 
				// based on what 'op' we have before it
				num = cal_recur(num, tmp, op);
				
				if (c == '+' || c == '-') {
					result += num;
					num = 0;
				}
				
				// reset 'tmp' and op for next character processing
				tmp = 0;
				op = c;
			}
		}
		
		return result + cal_recur(num, tmp, op);
	}

	private int cal_recur(int num, int tmp, char op) {
		if (op == '+')
			return num + tmp;
		else if (op == '-')
			return num - tmp;
		else if (op == '*')
			return num * tmp;
		else
			return num / tmp;
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/basic-calculator-iii/discuss/113603/Java-StraightForward/231295
	 * 
	 * whenever I find a '(' we push current status into stack, and whenever a ')' we 
	 * recover the status from stack to current.
	 */
	class Solution_status_stack {
		Stack<Status> stack = new Stack<>();

		Integer pre = null;
		int res = 0, num = 0, sign = 1;
		boolean isMul = true;

		public int calculate(String s) {

			for (char c : s.toCharArray()) {
				if (c >= '0' && c <= '9') {
					num = num * 10 + (c - '0');
				} 
				else if (c == '+' || c == '-') {
					cal();
					sign = c == '+' ? 1 : -1;
				} 
				else if (c == '*' || c == '/') {
					// deal with case: 3 * 5 / 2
					if (pre != null) {
						num = isMul ? pre * num : pre / num;
						pre = null;
					}
					
					pre = new Integer(num);
					num = 0;
					isMul = c == '*';
				} 
				else if (c == '(') {
					stack.push(new Status(pre, res, sign, isMul));
					
					pre = null;
					res = 0;
					sign = 1;
				} 
				else if (c == ')') {
					cal();
					
					// compute all inside parentheses, see it as a num
					num = res;
					
					Status status = stack.pop();
					pre = status.pre;
					res = status.res;
					sign = status.sign;
					isMul = status.isMul;
				}
			}
			
			cal();
			return res;
		}

		private void cal() {
			if (pre != null) {
				num = isMul ? pre * num : pre / num;
				pre = null;
			}
			
			res += sign * num;
			num = 0;
		}

		class Status {
			Integer pre;
			int res;
			int sign;
			boolean isMul;

			Status(Integer _pre, int _res, int _sign, boolean _isMul) {
				pre = _pre;
				res = _res;
				sign = _sign;
				isMul = _isMul;
			}
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/basic-calculator-iii/discuss/127881/Python-O(n)-Solution-using-recursion
     * https://leetcode.com/problems/basic-calculator-iii/discuss/159550/Python-short-iterative-solution-with-brief-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/basic-calculator-iii/discuss/113593/C%2B%2B-Consise-Solution
     */

}
