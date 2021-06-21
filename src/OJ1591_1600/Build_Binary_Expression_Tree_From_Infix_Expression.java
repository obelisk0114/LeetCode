package OJ1591_1600;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class Build_Binary_Expression_Tree_From_Infix_Expression {
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/867995/Almost-identical-to-772.-Basic-Calculator-III
	 * 
	 * shunting-yard algorithm
	 * 
	 * Rf :
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/867995/Almost-identical-to-772.-Basic-Calculator-III/803049
	 */
	public Node expTree_2Stack(String s) {
		Stack<Node> nodes = new Stack<>();     // stores nodes (new Node(ch))
		Stack<Character> ops = new Stack<>();  // stores operators and parentheses

		for (final char c : s.toCharArray()) {			
			if (Character.isDigit(c)) {
				nodes.push(new Node(c));
			} 
			else if (c == '(') {
				ops.push(c);
			} 
			else if (c == ')') {
				while (ops.peek() != '(') {
					nodes.push(buildNode_2Stack(ops.pop(), nodes.pop(), nodes.pop()));
				}
				
				// remove '('
				ops.pop();
			}
			// c == '+' || c == '-' || c == '*' || c == '/'
			else {
				while (!ops.isEmpty() && compare_2Stack(ops.peek(), c)) {
					nodes.push(buildNode_2Stack(ops.pop(), nodes.pop(), nodes.pop()));
				}
				
				ops.push(c);
			}
		}

		while (!ops.isEmpty()) {			
			nodes.push(buildNode_2Stack(ops.pop(), nodes.pop(), nodes.pop()));
		}

		return nodes.peek();
	}

	private Node buildNode_2Stack(char op, Node right, Node left) {
		return new Node(op, left, right);
	}

	// return true if op1 is a operator and priority(op1) >= priority(op2)
	boolean compare_2Stack(char op1, char op2) {
		if (op1 == '(' || op1 == ')')
			return false;
		
		return op1 == '*' || op1 == '/' || op2 == '+' || op2 == '-';
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/865804/Java-O(N)-Infix-greater-Postfix-greater-expression-tree
	 */
	public Node expTree_postfix(String s) {
		Stack<Character> OperatorStack = new Stack<Character>();
		Queue<Character> OutputQueue = new LinkedList<Character>();

		// Infix -> postfix
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {				
				OutputQueue.add(s.charAt(i));
			}
			else if (IsOperator_postfix(s.charAt(i))) {
				while (!OperatorStack.isEmpty()
						&& Precedence_postfix(OperatorStack.peek()) 
							>= Precedence_postfix(s.charAt(i))) {
					
					OutputQueue.add(OperatorStack.pop());
				}

				OperatorStack.push(s.charAt(i));
			} 
			else if (s.charAt(i) == '(') {				
				OperatorStack.push(s.charAt(i));
			}
			else {
				while (!OperatorStack.isEmpty() && OperatorStack.peek() != '(') {					
					OutputQueue.add(OperatorStack.pop());
				}

				OperatorStack.pop();
			}
		}

		while (!OperatorStack.isEmpty()) {			
			OutputQueue.add(OperatorStack.pop());
		}

		// Postfix -> expression tree
		Stack<Node> resultStack = new Stack<Node>();

		while (!OutputQueue.isEmpty()) {
			Character ch = OutputQueue.poll();
			
			if (IsOperator_postfix(ch)) {
				Node right = resultStack.pop();
				Node left = resultStack.pop();
				
				resultStack.push(new Node(ch, left, right));
			} 
			else {				
				resultStack.push(new Node(ch));
			}
		}

		return resultStack.peek();
	}

	private boolean IsOperator_postfix(Character c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private int Precedence_postfix(Character c) {
		int pred = 0;

		switch (c) {
		case '+':
			pred = 2;
			break;
		case '-':
			pred = 2;
			break;
		case '*':
			pred = 3;
			break;
		case '/':
			pred = 4;
			break;
		default:
			break;
		}

		return pred;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/864596/Python-Standard-parser-implementation/728952
	 * 
	 * If you remember from the dusty memories of compiler class, this calls for a 
	 * standard expression parser implementation where you call parse-functions on 
	 * those with the lowest precedence and recursively invoke parse-functions of 
	 * things with higher precedence.
	 * 
	 * Standard parser implementation based on this BNF
     *   s := expression
     *   expression := term | term { [+,-] term] }
     *   term := factor | factor { [*,/] factor] }
     *   factor :== digit | '(' expression ')'
     *   digit := [0..9]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/864596/Python-Standard-parser-implementation
	 */
	public Node expTree_parser(String s) {
		return parseExpression_parser(s.toCharArray(), new int[1]);
	}

	public Node parseExpression_parser(char[] arr, int[] idx) {
		Node lhs = parseTerm_parser(arr, idx);
		while (idx[0] < arr.length && (arr[idx[0]] == '+' || arr[idx[0]] == '-')) {
			char op = arr[idx[0]++];
			Node rhs = parseTerm_parser(arr, idx);
			lhs = new Node(op, lhs, rhs);
		}
		return lhs;
	}

	public Node parseTerm_parser(char[] arr, int[] idx) {
		Node lhs = parseFactor_parser(arr, idx);
		while (idx[0] < arr.length && (arr[idx[0]] == '*' || arr[idx[0]] == '/')) {
			char op = arr[idx[0]++];
			Node rhs = parseFactor_parser(arr, idx);
			lhs = new Node(op, lhs, rhs);
		}
		return lhs;
	}

	public Node parseFactor_parser(char[] arr, int[] idx) {
		if (arr[idx[0]] == '(') {
			idx[0]++; // pass '('
			
			Node n = parseExpression_parser(arr, idx);
			
			idx[0]++; // pass ')'
			return n;
		}
		
		// Single operand
		return new Node(arr[idx[0]++], null, null);
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/861513/Recursion-+-2-Passes/710219
	 * 
	 * The tricky part of this question is the data structure. I decided I would 
	 * convert all characters to nodes, and store them in a list.
	 * 
	 * 1. Conversion with recursion:
	 *    + Convert all characters into nodes and store them in list l.
	 *    + Recursion: if we detect a group (...), call expTree recursively and 
	 *                 insert the returned tree root into l.
	 * 2. Pass 1 for elements in l:
	 *    + For * and / leaf nodes, 'adopt' left and right neighbors to be its 
	 *      children (neighbors are removed from the list).
	 * 3. Pass 2 for the remaining elements in l:
	 *    + Do the same as in pass 1 for + and - leaf nodes.
	 * 
	 * After this, we should have one element remaining in l - our root node.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/861513/Recursion-%2B-2-Passes
	 */
	public Node expTree_2Pass(String s) {
		if (s.isEmpty())
			return null;
		
		Deque<Node> deque = new ArrayDeque<>();
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				int j = i + 1;
				for (int bal = 1; j < s.length(); j++) {
					if (s.charAt(j) == '(')
						bal++;
					else if (s.charAt(j) == ')')
						bal--;
					
					if (bal == 0)
						break;
				}
				
				Node tmp = expTree_2Pass(s.substring(i + 1, j));
				if (tmp != null)
					deque.add(tmp);
				
				i = j;
			} 
			else {
				deque.add(new Node(s.charAt(i)));
			}
		}
		
		return helper_2Pass(helper_2Pass(deque, '*', '/'), '+', '-').poll();
	}

	private Deque<Node> helper_2Pass(Deque<Node> deque, char op1, char op2) {
		if (deque.isEmpty())
			return null;
		
		Deque<Node> tmp = new ArrayDeque<>();
		tmp.offer(deque.poll());
		
		while (!deque.isEmpty()) {
			Node oper = deque.poll();
			
			if (oper.left == null && (oper.val == op1 || oper.val == op2)) {
				oper.left = tmp.pollLast();
				oper.right = deque.poll();
			}
			
			tmp.offer(oper);
		}
		
		return tmp;
	}

	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/864428/Java-O(n)-stack-one-pass-with-comment
	 */
	// stack to build tree?
    // operator are node
	int p_stack_recur = 0;

	public Node expTree_stack_recur(String s) {
		Deque<Node> operators = new ArrayDeque<>();
		Deque<Node> operands = new ArrayDeque<>();
		
		while (p_stack_recur < s.length()) {
			char cur = s.charAt(p_stack_recur++);
			
			if (cur >= '0' && cur <= '9') {
				operands.push(new Node(cur));
			} 
			else if (cur == '-' || cur == '+' || cur == '*' || cur == '/') {
				while (!operators.isEmpty() 
						&& compareOperator_stack_recur(cur, operators.peek().val)) {
					
					Node r = operands.pop(), l = operands.pop();
					Node op = operators.pop();
					
					op.left = l;
					op.right = r;
					
					operands.push(op);
				}
				
				operators.push(new Node(cur));
			} 
			else if (cur == '(') {
				operands.push(expTree_stack_recur(s));
			} 
			else if (cur == ')') {
				break;
			}
		}

		// handle rest numbers in stack
		// because we use loop inside, so directly use pop is fine
		// the only reason stack has two operator is that first priority low
		while (!operators.isEmpty()) {
			Node r = operands.pop(), l = operands.pop();
			Node op = operators.pop();
			
			op.left = l;
			op.right = r;
			
			operands.push(op);
		}

		return operands.pop();
	}

	// check l's priority is less or equal than r's priority
	private boolean compareOperator_stack_recur(char l, char r) {
		if (l == '*' || l == '/') {
			
			// true means can pop
			if (r == '*' || r == '/') {				
				return true;
			}
			
			return false;
		}

		// if l is + or -, all operator should pop
		return true;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/862518/Java.-Recursive.-Single-Pass
	 * 
	 * The idea was to separate parsing styles into two:
	 * + Full expressions including + and -. This includes sub-expressions in 
	 *   parentheses.
	 * + Those bonded with * or /.
	 */
	public Node expTree_split(String s) {
		if (s.length() == 0)
			return null;

		ArrayDeque<Character> chars = new ArrayDeque<>();
		for (int i = 0; i < s.length(); i++)
			chars.add(s.charAt(i));

		return parseExpression_split(chars);
	}

	private Node parseExpression_split(ArrayDeque<Character> chars) {
		Node top = parseAddend_split(chars);

		while (!chars.isEmpty()) {
			char op = chars.pollFirst();
			
			// end of sub-expression
			if (op == ')')
				break;
			
			// + or -
			Node newTop = new Node(op);
			newTop.left = top;
			newTop.right = parseAddend_split(chars);
			
			top = newTop;
		}
		
		return top;
	}

	private Node parseAddend_split(ArrayDeque<Character> chars) {
		Node top = parseMultiplicant_split(chars);

		while (!chars.isEmpty()) {
			// check to see if we're still chaining multiplication/division
			char c = chars.peekFirst();
			
			// end of addend (chain of * or / has ended)
			if (c == '+' || c == '-' || c == ')') {
				break;
			}
			
			// consume the char from the deque
			chars.removeFirst();
			
			Node newTop = new Node(c);
			newTop.left = top;
			newTop.right = parseMultiplicant_split(chars);
			
			top = newTop;
		}
		
		return top;
	}

	private Node parseMultiplicant_split(ArrayDeque<Character> chars) {
		char c = chars.pollFirst();
		
		// sub-expression start
		if (c == '(') {
			return parseExpression_split(chars);
		} 
		// digit
		else {
			return new Node(c);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/1125808/Java-0ms-easy-to-understand-solutions
	 * 
	 * Basic idea is:
	 * when do plus '+' operation, the result on the left and right around '+' both 
	 * won't be influenced
	 * when do minus '-' calculation, if there is parenthesis right after '-', 
	 * the '+', and '-' in the parenthesis should be reverse. 
	 * Like 2 - (3 - 4 + 5) = 2 - 3 + 4 - 5.
	 * when do '/' and '*' calculation, they has higher priority than '+' and '-'
	 * 
	 * So the sequence of build tree based on operation is '+' --> '-' --> '*' or '/'
	 * 
	 * Steps are
	 * 1. try to find '+' as root (the '+' should not be covered by brackets）
	 * 2. if there is no '+', then try to find '-' as root
	 * 3. if there isn't '-' neither, try to find '*' or '/' as root
	 * 4. the number '0' - '9' char will always be a leaf node.
	 * 
	 * 因為 expression tree 越上面越晚處理 (先處理 leaf 才處理 root)
	 * 所以先找順位低, 再找高的, '+' --> '-' --> '*' or '/'
	 * 
	 * 除了 '+' 用第一個以外, 其他都先找最後一個 ('+' 也可以找最後一個)
	 * 只有 '+' 和 '*' 可以找第一個, 因為不會影響後面
	 * '-' 和 '/' 需要找最後一個, 因為會影響後面符號
	 * ex: 2 - (3 + 4) = 2 - 3 - 4; 8 / (2 * 3) = 8 / 2 / 3
	 */
	public Node expTree_start_end(String s) {
		return helper_start_end(s, 0, s.length() - 1);
	}

	private Node helper_start_end(String str, int s, int e) {
		if (s > e)
			return null;
		
		// if s == e, here must be a number character and directly return it.
		if (s == e) {
			Node res = new Node(str.charAt(s));
			return res;
		}
		
		// p is used to count whether '+', '-', '*', and '/' are in a brackets
		// plus, min, div, mul used to count the location of '+', '-', '/', '*'
		int p = 0, plus = s, min = s, div = s, mul = s;

		while (plus < e) {
			if (str.charAt(plus) == '(') {
				p++;
			} 
			else if (str.charAt(plus) == ')') {
				p--;
			} 
			// found the root location
			else if (str.charAt(plus) == '+' && p == 0) {
				break;
			} 
			else if (str.charAt(plus) == '-' && p == 0) {
				min = plus;
			} 
			else if (str.charAt(plus) == '*' && p == 0) {
				mul = plus;
			} 
			else if (str.charAt(plus) == '/' && p == 0) {
				div = plus;
			}
			
			plus++;
		}
		
		Node root = null;

		// find '+' without cover by brackets
		if (plus > s && plus < e) {
			root = new Node(str.charAt(plus));
		} 
		// find '-' without cover by brackets and there isn't a '+'
		else if (min > s && min < e) {
			root = new Node(str.charAt(min));
			plus = min;
		} 
		// find '*' without cover by brackets and there isn't a '+', '-'
		else if (mul > s && mul < e) {
			root = new Node(str.charAt(mul));
			plus = mul;
		} 
		// find '/' without cover by brackets and there isn't a '+', '-', '*'
		else if (div > s && div < e) {
			root = new Node(str.charAt(div));
			plus = div;
		} 
		// no '+', '-', '*', '/', it means it's covered by brackets
		else if (str.charAt(s) == '(' && str.charAt(e) == ')') {
			return helper_start_end(str, s + 1, e - 1);
		}

		root.left = helper_start_end(str, s, plus - 1);
		root.right = helper_start_end(str, plus + 1, e);

		return root;
	}
	
	/*
	 * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/878008/Java-O(n)-solution-with-single-stack
	 * 
	 * adding a pair of parenthesis around the given string, which ensures that stack 
	 * will end up having a single node (the result)
	 * we look back to see if we can combine some nodes whenever we meet an operand 
	 * (`+`, `-`, `*`, and `/`) or a closing parenthesis `)`
	 * + if the current char is `*` or `/`, we can combine the previous 2 numbers 
	 *   only if the previous char is a `*` or `/`.
	 *    + Why? Think about `2+3*5`, when we are at `*`, the previous 2 numbers 
	 *      cannot be combined, because we have to wait `3*5` to be executed first
	 * + if the current char is `+`, `-` or `)`, we need to use `while` loop to 
	 *   combine all adjacent nodes backward until hitting an opening parenthesis `(`
	 *    + Why? Think about `2+3*5+2`, when we are at the 2nd `+`, we first combine 
	 *      `3` and `5`, but we cannot stop there, we have to continue to combine 
	 *      `2+(RESULTING_NODE_OF_3+5)`
	 */
	public Node expTree_look_back(String s) {
        s = '(' + s + ')';
        
        Stack<Node> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) && c != '(') {
                Node right = stack.pop();
                
                if (c == '*' || c == '/') {
                    if (stack.peek().val == '*' || stack.peek().val == '/') {
                        Node ops = stack.pop();
                        Node left = stack.pop();
                        
                        ops.left = left;
                        ops.right = right;
                        
                        right = ops;
                    }
                } 
                else {
                    while (stack.peek().val != '(') {
                        Node ops = stack.pop();
                        Node left = stack.pop();
                        
                        ops.left = left;
                        ops.right = right;
                        
                        right = ops;
                    }
                    
                    if (c == ')') {
                        stack.pop();
                    }
                }
                
                stack.push(right);
            }
            
            if (c != ')') {
                stack.push(new Node(c));
            }
        }
        return stack.pop();
    }

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/864596/Python-Standard-parser-implementation
     * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/949053/Python3-Two-stacks-approach-ops-%2B-nums-stacks
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/build-binary-expression-tree-from-infix-expression/discuss/861513/Recursion-%2B-2-Passes
     */

}

class Node {
    char val;
    Node left;
    Node right;
    Node() {this.val = ' ';}
    Node(char val) { this.val = val; }
    Node(char val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
 }
