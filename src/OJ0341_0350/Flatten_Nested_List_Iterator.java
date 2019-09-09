package OJ0341_0350;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Iterator;
import java.util.ListIterator;

public class Flatten_Nested_List_Iterator {
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/84868
	 * 
	 * Need recursion to solve it. But since we need to access each NestedInteger at a 
	 * time, we will use a stack to help.
	 * 
	 * In the constructor, we push all the nestedList into the stack from back to 
	 * front, so when we pop the stack, it returns the first element. Second, in the 
	 * hasNext() function, we peek the first element in stack currently, and if it is 
	 * an Integer, we will return true and pop the element. If it is a list, we will 
	 * further flatten it. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/84848
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/84850
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/335847/Multiple-solution-Beat-100-using-(list-queue-and-stack-)
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/84873
	 */
	public class NestedIterator_stack implements Iterator<Integer> {
		private Stack<NestedInteger> stack;
		
	    public NestedIterator_stack(List<NestedInteger> nestedList) {
	    	stack = new Stack<>();
	    	flattenList(nestedList);
	    }
	    
	    private void flattenList(List<NestedInteger> list) {
	        for (int i = list.size() - 1; i >= 0; i--) {
	            stack.push(list.get(i));
	        }
	    }

	    @Override
	    public Integer next() {
	    	return hasNext() ? stack.pop().getInteger() : null;
	    }

	    @Override
	    public boolean hasNext() {
	    	// Keep iterating until the top element is an integer
	        while(!stack.isEmpty()) {
	            NestedInteger curr = stack.peek();
	            if(curr.isInteger()) {
	                return true;
	            }
	            flattenList(stack.pop().getList());
	        }
	        return false;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/142999/Load-data-on-next()-instead-of-hasNext()-JAVA-AC-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/84851
	 */
	public class NestedIterator_enstack implements Iterator<Integer> {
		private Stack<NestedInteger> stack;

		public NestedIterator_enstack(List<NestedInteger> nestedList) {
			stack = new Stack<>();
			enstack(nestedList);
		}

		private void enstack(List<NestedInteger> list) {
			for (int i = list.size() - 1; i >= 0; i--) {
				stack.push(list.get(i));
			}

			while (!stack.isEmpty() && !stack.peek().isInteger()) {
				enstack(stack.pop().getList());
			}
		}

		@Override
		public Integer next() {
			Integer res = stack.pop().getInteger();
			if (!stack.isEmpty() && !stack.peek().isInteger()) {
				enstack(stack.pop().getList());
			}
			return res;
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80146/Real-iterator-in-Python-Java-C%2B%2B
	 * 
	 * Keep the current progress in a stack. hasNext() tries to find an integer. 
	 * next() returns it and moves on. Call hasNext() in next() because hasNext() is 
	 * optional. Some user of the iterator might call only next and never hasNext.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80146/Real-iterator-in-Python-Java-C++/192002
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80175/Share-my-Java-neat-solution-8ms/84901
	 */
	public class NestedIteratorListIterator implements Iterator<Integer> {
		private Stack<ListIterator<NestedInteger>> lists;

		public NestedIteratorListIterator(List<NestedInteger> nestedList) {
			lists = new Stack<>();
			lists.push(nestedList.listIterator());
		}

		public Integer next() {
			hasNext();
			return lists.peek().next().getInteger();
		}

		public boolean hasNext() {
			while (!lists.empty()) {
				if (!lists.peek().hasNext()) {
					lists.pop();
				} 
				else {
					NestedInteger x = lists.peek().next();
					if (x.isInteger()) {
						// Move cursor backwards for next() called
						lists.peek().previous();
						return true;
					}
					lists.push(x.getList().listIterator());
				}
			}
			return false;
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80156/Flatten-the-list-and-iterate-with-plain-next()-and-hasNext()-(Java)
	 * 
	 * First flatten the list to a list of Integer by using DFS, then just call the 
	 * plain next() and hasNext()
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80156/Flatten-the-list-and-iterate-with-plain-next()-and-hasNext()-(Java)/140393
	 */
	public class NestedIterator_iter_construct_all implements Iterator<Integer> {
		private List<Integer> flattenedList;
		private Iterator<Integer> it;

		public NestedIterator_iter_construct_all(List<NestedInteger> nestedList) {
			flattenedList = new LinkedList<Integer>();
			flatten(nestedList);
			it = flattenedList.iterator();
		}

		private void flatten(List<NestedInteger> nestedList) {
			for (NestedInteger i : nestedList) {
				if (i.isInteger()) {
					flattenedList.add(i.getInteger());
				} 
				else {
					flatten(i.getList());
				}
			}
		}

		@Override
		public Integer next() {
			return it.next();
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/302547/Java-Solution%3A-Faster-than-100-and-Memory-less-than-100
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80147/Simple-Java-solution-using-a-stack-with-explanation/230644
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80357/Easy-to-understand-java-solution-with-recursion
	 */
	public class NestedIterator_Queue_construct_all implements Iterator<Integer> {
		private Queue<Integer> queue = new LinkedList<>();

		public NestedIterator_Queue_construct_all(List<NestedInteger> nestedList) {
			addToQueue(nestedList);
		}

		@Override
		public Integer next() {
			return queue.poll();
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		private void addToQueue(List<NestedInteger> nestedList) {
			if (nestedList.isEmpty()) {
				return;
			}

			for (NestedInteger i : nestedList) {
				if (i.isInteger()) {
					queue.offer(i.getInteger());
				} 
				else {
					addToQueue(i.getList());
				}
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80282/Purely-recursive-solution-without-storing-list.
	 * 
	 * iterator - a list iterator of type ListIterator<NestedInteger>
	 * subiter - a nested iterator of type NestedIterator
	 * current - pointer to the current NestedInteger at hand.
	 * 
	 * A recursive structure - think of the whole nested list as root of a tree, the 
	 * child elements being either purely integer or roots of similar subtrees. To 
	 * iterate over the next child of a root, we'll first have to finish iterating 
	 * over the current subtree.
	 */
	public class NestedIterator_tree implements Iterator<Integer> {
		ListIterator<NestedInteger> iterator; // iterator over nodes
		NestedIterator_tree subiter; // sub-iterator for each of those nodes
		NestedInteger current; // current list

		public NestedIterator_tree(List<NestedInteger> nestedList) {
			if (nestedList != null)
				iterator = nestedList.listIterator();
		}

		@Override
		public Integer next() {
			// if 'current' is integer, return it
			if (current.isInteger())
				return current.getInteger();
			
			// otherwise, the 'next' lies in the sub-iterator for 'current'
			return subiter.next();
		}

		@Override
		public boolean hasNext() {
			// first we will finish iterating over all elements
			// in the subtree through sub-iterator
			if (subiter != null && subiter.hasNext())
				return true;
			
			// if sub-iterator has not been initialized or has reached the end of
			// 'current', start processing the next child of the parent list
			if (iterator.hasNext()) {
				current = iterator.next();
				
				// integer
				if (current.isInteger())
					return true;
				
				// empty list i.e. []
				if (current.getList().size() == 0)
					return hasNext();
				
				// initialize a sub-iterator on this non integer list
				subiter = new NestedIterator_tree(current.getList());
				
				// if sub-iterator has an element remaining or
				// this list has an element remaining
				return subiter.hasNext() || hasNext();
			}
			return false;
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80380/Java-recursive-solution-straightforward-to-understand
	 * 
	 * When we visit an element (current element), it has following three conditions:
	 * (a) it is an integer (b) it is a non-empty list (c) it is an empty list.
	 * 
	 * If (a), we just output the integer and move to next element
	 * if (b), actually we are back to original problem (i.e. iterating a list of 
	 *         NestedInteger) before we move to next element
	 * if (c), we skip it and move to next element
	 * 
	 * Similarly, when we check hasNext() integer, we have two conditions:
	 * (1) we are in a nested list
	 * (2) we just move to the next element in top level
	 * what hasNext() can bring us is that if it returns 'true', when calling 'next()', 
	 * we can output an integer; otherwise the iterator already reaches the end of the 
	 * list. Therefore we won't encounter (c) when calling next().
	 * 
	 * Thus, the iterating solution is using a variable to hold current nestedInteger 
	 * in top level, if it is an integer, we output the integer and move to the next 
	 * nestedInteger; else we product a NestedIterator for the current nestedInteger 
	 * (which is a list).
	 */
	public class NestedIterator_call_repeated implements Iterator<Integer> {
		private List<NestedInteger> nestedIntegerList;
		private NestedIterator_call_repeated nextNestedIterator;
		private NestedInteger nextNestedInteger;
		private int currentPosition;

		public NestedIterator_call_repeated(List<NestedInteger> nestedList) {
			this.nestedIntegerList = nestedList;
			this.currentPosition = 0;
		}

		@Override
		public Integer next() {
			return nextNestedInteger.isInteger() ? 
					nextNestedInteger.getInteger() : nextNestedIterator.next();
		}

		@Override
		public boolean hasNext() {
			if (nextNestedIterator != null && nextNestedIterator.hasNext()) {
				return true;
			}
			if (currentPosition >= nestedIntegerList.size()) {
				return false;
			}

			nextNestedInteger = nestedIntegerList.get(currentPosition++);
			nextNestedIterator = new NestedIterator_call_repeated
					(nextNestedInteger.getList());
			if (nextNestedInteger.isInteger() || nextNestedIterator.hasNext()) {
				return true;
			} 
			else {
				return hasNext();
			}
		}
	}
	
	// by myself
	public class NestedIterator_self implements Iterator<Integer> {
	    private List<NestedInteger> nestedList;
	    private int index;

	    public NestedIterator_self(List<NestedInteger> nestedList) {
	        this.nestedList = nestedList;
	        index = 0;
	    }

	    @Override
	    public Integer next() {
	    	hasNext();
	        NestedInteger nest = nestedList.get(index);
	        index++;
	        return nest.getInteger();
	    }

	    @Override
	    public boolean hasNext() {
	        if (nestedList.size() == 0)
	            return false;
	        
	        if (index == nestedList.size()) {
	            return false;
	        }
	        else {
	            NestedInteger nest = nestedList.get(index);
	            if (nest.isInteger())
	                return true;
	            // [[]] => []
	            else if (nest.getList().isEmpty()) {
	                index++;
	                return hasNext();
	            }
	            // [[[[]]],[]] => []
	            else {
	                List<NestedInteger> expand = nest.getList();
	                nestedList.remove(index);
	                nestedList.addAll(index, expand);
	                return hasNext();
	            }
	        }
	    }
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80142/8-line-Python-Solution
     * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80247/Python-Generators-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80169/Concise-C%2B%2B-without-storing-all-values-at-initialization
     * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80234/Evolve-from-intuition-to-optimal-a-review-of-top-solutions
     * https://leetcode.com/problems/flatten-nested-list-iterator/discuss/80362/Simple-iteration-solution-in-C%2B%2B
     */

	// This is the interface that allows for creating nested lists.
	// You should not implement it, or speculate about its implementation
	public interface NestedInteger {

		// @return true if this NestedInteger holds a single integer, rather than a
		// nested list.
		public boolean isInteger();

		// @return the single integer that this NestedInteger holds, if it holds a
		// single integer
		// Return null if this NestedInteger holds a nested list
		public Integer getInteger();

		// @return the nested list that this NestedInteger holds, if it holds a nested
		// list
		// Return null if this NestedInteger holds a single integer
		public List<NestedInteger> getList();
	}

}

/**
 * Your NestedIterator object will be instantiated and called as such:
 * NestedIterator i = new NestedIterator(nestedList);
 * while (i.hasNext()) v[f()] = i.next();
 */
