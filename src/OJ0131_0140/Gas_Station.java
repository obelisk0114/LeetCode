package OJ0131_0140;

public class Gas_Station {
	/*
	 * https://leetcode.com/problems/gas-station/discuss/860348/Java-or-Very-simple-one-or-O(N)-time-or-With-detailed-explanation
	 * 
	 * If proceed from A, and found A cannot reach B, then for any points C between A 
	 * and B; C cannot reach B too. (because if A reached C, then the fuel left when 
	 * reached C will always >= 0, which is always equal or better than start from C)
	 * 
	 * 1. Start from index start (initialized as 0) and proceeds, record the fuel left 
	 *    in tank.
	 * 2. If we are successfully returned to point start, than we return index `start`
	 * 3. If at index i, we found that we can't proceed to i + 1, then we record how 
	 *    many gas are we lacking (stored in `required`), and re-start from i + 1, and 
	 *    we update the start index.
	 * 4. Keep running until we back to start. Now the variable `required` stores the
	 *    information that how many fuel we need in order to start from 0 and 
	 *    successfully reach to index start (which records the last start position),
	 *    and we also have a filled variable that tells us how many fuel we left when
	 *    we start from `start` and reach end, if filled >= required, then we can 
	 *    return to the start point, if not, we can't.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/gas-station/discuss/167542/10-lines-Python-that-beats-100-NO-PROOF-IN-HERE
	 */
	public int canCompleteCircuit_add_back(int[] gas, int[] cost) {
		int start = 0, filled = 0, required = 0;
		for (int i = 0; i < gas.length; i++) {
			filled += gas[i] - cost[i];
			
			if (filled < 0) {
				required += filled;
				start = i + 1;
				filled = 0;
			}
		}
		return filled >= (-required) ? start : -1;
	}
	
	/*
	 * Modified by myself
	 * https://leetcode.com/problems/gas-station/discuss/42702/11ms-c%2B%2B-solution.-visiting-each-station-once.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/gas-station/discuss/42565/My-AC-is-O(1)-space-O(n)-running-time-solution.-Does-anybody-have-posted-this-solution
	 * https://leetcode.com/problems/gas-station/discuss/42565/My-AC-is-O(1)-space-O(n)-running-time-solution.-Does-anybody-have-posted-this-solution/40934
	 */
	public int canCompleteCircuit_double_end_self_modify(int[] gas, int[] cost) {
		// Start from an arbitrarily chosen index, let's say 0.
		// Accumulate the remaining gas (gas - cost).
		//
		// If there is enough gas to advance to the next station
		// then advance to the next station (i++). Continue to do this
		// expanding the range of traveled stations until we have
		// circled back to the starting point(found a solution)
		// or we have ran out of gas.
		//
		// If we ran out of gas it means that we should have entered the
		// range with more gas, so we expand the current range to the left
		// in hope to accumulate enough gas.
		//
		// And so on, expand to the right if we have gas, expand to the
		// left if we don't have gas.
		//
		// Once we completed a circle we have the left side of the range (j)
		// as the starting station index.
		//

		// The range of stations is given by the indexes:
		// j, j+1, j+2, ... , gas.size() - 1, 0, 1, 2, ..., i.
		
		// Right side of the range.
		int i = 0;
		
		// Left side of the range
		int j = gas.length;
		
		// Current index to be added to the range.
		int crt = 0;

		// Remaining gas in the tank
		int gasSum = 0;

		while (i != j) {
			gasSum += gas[crt] - cost[crt];

			// Move right
			if (gasSum >= 0) {
				i = i + 1;
				crt = i;
			} 
			// Move left
			else {
				j = j - 1;
				crt = j;
			}
		}

		if (gasSum >= 0) {
			return j % gas.length;
		} 
		else {
			return -1;
		}
	}
	
	/*
	 * https://leetcode.com/problems/gas-station/discuss/42667/Straightforward-Java-Linear-Solution-with-O(1)-space-explanation-and-Math-proof
	 * 
	 * A --- C1 --- C2  --- ... Ck --- B
	 * 1. A cannot reach B
	 * 2. There are C1,C2, ..., Ck between A and B
	 * 3. A can reach C1, C2, ..., Ck
	 * 
	 * Assume: C1 can reach B
	 * A can reach C1 (by 3.) & C1 can reach B => A can reach B (Contradict with 1.)
	 * => assumption is wrong, C1 cannot reach B
	 * Same proof could be applied to C2 ~ Ck
	 * => any station between A and B that A can reach cannot reach B
	 * 
	 * Rf :
	 * https://leetcode.com/problems/gas-station/discuss/42568/Share-some-of-my-ideas./177253
	 * https://leetcode.com/problems/gas-station/discuss/805989/Brute-Force-and-One-Pass-oror-Step-by-step-Explanation(with-proof-graph-and-code)-oror-Beginners-Friendly
	 * https://leetcode.com/problems/gas-station/discuss/42572/Proof-of-%22if-total-gas-is-greater-than-total-cost-there-is-a-solution%22.-C%2B%2B
	 * https://leetcode.com/problems/gas-station/discuss/42667/Straightforward-Java-Linear-Solution-with-O(1)-space-explanation-and-Math-proof/41095
	 * 
	 * Other code:
	 * https://leetcode.com/problems/gas-station/discuss/391095/Java-B-FGreedy-Solutions-with-Explanation-Comments-and-Illustration-(easy-understand)
	 * https://leetcode.com/problems/gas-station/discuss/215701/Simple-Java-solution-beats-100-with-detailed-explain
	 * https://leetcode.com/problems/gas-station/discuss/860776/Java-2-Solutions-or-Brute-Force-O(n2)-or-Optimal-O(n)-or-Well-Commented
	 */
	public int canCompleteCircuit_greedy_go(int[] gas, int[] cost) {
		int tank = 0;
		for (int i = 0; i < gas.length; i++)
			tank += gas[i] - cost[i];
		if (tank < 0)
			return -1;

		int start = 0;
		int accumulate = 0;
		for (int i = 0; i < gas.length; i++) {
			int curGain = gas[i] - cost[i];
			accumulate += curGain;

			if (accumulate < 0) {
				start = i + 1;
				accumulate = 0;
			}
		}

		return start;
	}
	
	/*
	 * https://leetcode.com/problems/gas-station/discuss/42600/My-O(N)-time-O(1)-extra-space-solution.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/gas-station/discuss/42565/My-AC-is-O(1)-space-O(n)-running-time-solution.-Does-anybody-have-posted-this-solution/193039
	 */
	public int canCompleteCircuit_cycle(int[] gas, int[] cost) {
		for (int i = 0; i < gas.length; i++) {
			gas[i] -= cost[i];
		}

		int sum = 0;
		int result = 0;
		int n = gas.length;

		for (int i = 0; i < n * 2 - 1; i++) {
			sum += gas[i % n];

			if (sum < 0) {
				result = i + 1;
				
				if (result >= n) {
					return -1;
				}
				
				sum = 0;
			}
		}
		return result;
	}

	// by myself
	public int canCompleteCircuit_self(int[] gas, int[] cost) {
        int[] gain = new int[gas.length * 2];
        for (int i = 0; i < gas.length; i++) {
            gain[i] = gas[i] - cost[i];
        }
        for (int i = gas.length; i < gain.length; i++) {
            gain[i] = gain[i - gas.length];
        }
        
        int left = 0;
        int cur = 0;
        int length = 0;
        for (int i = 0; i < gain.length; i++) {
            cur += gain[i];
            length++;
            
            while (cur < 0 && left < i) {
                cur -= gain[left];
                left++;
                length--;
            }
            
            if (left == i && cur < 0) {
                left = i + 1;
                length--;
                cur = 0;
            }
            if (length == gas.length) {
                return left;
            }
            
            //System.out.println("i = " + i + " ; left = " + left + " ; cur = " + cur);
        }
        return -1;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/gas-station/discuss/167542/10-lines-Python-that-beats-100-NO-PROOF-IN-HERE
     * https://leetcode.com/problems/gas-station/discuss/42578/Easy-and-simple-proof-with-Python-solution.
     * https://leetcode.com/problems/gas-station/discuss/274646/Python-One-Pass-Greedy
     * https://leetcode.com/problems/gas-station/discuss/860396/Python-O(n)-greedy-solution-explained
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/gas-station/discuss/42648/My-one-pass-solution.
     * https://leetcode.com/problems/gas-station/discuss/42568/Share-some-of-my-ideas.
     * https://leetcode.com/problems/gas-station/discuss/42572/Proof-of-%22if-total-gas-is-greater-than-total-cost-there-is-a-solution%22.-C%2B%2B
     * https://leetcode.com/problems/gas-station/discuss/860567/C%2B%2B-100-efficient-code-with-video-explanation
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/gas-station/discuss/420532/JavaScript-Solution
	 */

}
