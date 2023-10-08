package OJ0351_0360;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author 15T-J000
 *
 * 尚未整理
 * 
 * https://leetcode.com/problems/logger-rate-limiter/solution/
 * https://leetcode.com/problems/logger-rate-limiter/discuss/391558/Review-of-four-different-solutions%3A-HashMap-Two-Sets-Queue-with-Set-Radix-buckets-(Java-centric)
 * https://leetcode.com/problems/logger-rate-limiter/discuss/83273/Short-C%2B%2BJavaPython-bit-different
 * https://leetcode.com/problems/logger-rate-limiter/discuss/83256/Java-Circular-Buffer-Solution-similar-to-Hit-Counter
 * https://leetcode.com/problems/logger-rate-limiter/discuss/83284/A-solution-that-only-keeps-part-of-the-messages
 * https://leetcode.com/problems/logger-rate-limiter/discuss/365306/Simple-Two-HashMap-Solution-with-O(1)-time-and-little-memory
 * https://leetcode.com/problems/logger-rate-limiter/discuss/349733/Simple-Java-solution-using-Queue-and-Set-for-slow-learners-like-myself
 * https://leetcode.com/problems/logger-rate-limiter/discuss/83298/Thread-Safe-Solution
 *
 */

public class Logger_Rate_Limiter {
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83254/Java-with-a-LinkedHashMap-and-using-removeEldestEntry
	 * 
	 * eliminate entries that are older than 10 seconds, making the algorithm more 
	 * efficient when large amounts of data are coming through.
	 * 
	 * linkedhashmap is not synchronized
	 * 
	 * Rf :
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83254/Java-with-a-LinkedHashMap-and-using-removeEldestEntry/250677
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83254/Java-with-a-LinkedHashMap-and-using-removeEldestEntry/338424
	 */
	class Logger_LinkedHashMap {

		public Map<String, Integer> map;
		int lastSecond = 0;

		/** Initialize your data structure here. */
		public Logger_LinkedHashMap() {
			map = new LinkedHashMap<String, Integer>(100, 0.6f, true) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
					return lastSecond - eldest.getValue() > 10;
				}
			};
		}

		/**
		 * Returns true if the message should be printed in the given timestamp,
		 * otherwise returns false. If this method returns false, the message will not
		 * be printed. The timestamp is in seconds granularity.
		 */
		public boolean shouldPrintMessage(int timestamp, String message) {
			lastSecond = timestamp;
			if (!map.containsKey(message) || timestamp - map.get(message) >= 10) {
				map.put(message, timestamp);
				return true;
			}
			return false;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83303/Super-easy-Java-HashMap-solution
	 * 
	 * The problem with this approach is that your map size will keep growing. 
	 * It will have messages that have come since the beginning even though we need 
	 * to keep only the words that have come only 10 seconds before the current 
	 * timestamp.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83270/A-Java-Solution/87509
	 * 
	 * Other code:
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83270/A-Java-Solution
	 */
	class Logger_simple {
		private Map<String, Integer> map;

		/** Initialize your data structure here. */
		public Logger_simple() {
			map = new HashMap<>();
		}

		/**
		 * Returns true if the message should be printed in the given timestamp,
		 * otherwise returns false. The timestamp is in seconds granularity.
		 */
		public boolean shouldPrintMessage(int timestamp, String message) {
			if (map.containsKey(message) && (timestamp - map.get(message)) < 10) {
				return false;
			}
			map.put(message, timestamp);
			return true;
		}
	}

	// by myself
	class Logger_self {
	    private Map<String, Integer> map;

	    /** Initialize your data structure here. */
	    public Logger_self() {
	        map = new HashMap<>();
	    }
	    
	    /** Returns true if the message should be printed in the given timestamp, otherwise returns false.
	        If this method returns false, the message will not be printed.
	        The timestamp is in seconds granularity. */
	    public boolean shouldPrintMessage(int timestamp, String message) {
	        if (map.containsKey(message)) {
	            int allowTime = map.get(message) + 10;
	            
	            if (allowTime > timestamp) {
	                return false;
	            }
	            else {
	                map.put(message, timestamp);
	                return true;
	            }
	        }
	        else {
	            map.put(message, timestamp);
	            return true;
	        }
	    }
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/logger-rate-limiter/discuss/83300/Java-ConcurrentHashMap-solution
	 */
	public class Logger_ConcurrentHashMap {
	    ConcurrentHashMap<String, Integer> lastPrintTime;

	    /** Initialize your data structure here. */
	    public Logger_ConcurrentHashMap() {
	        lastPrintTime = new ConcurrentHashMap<String, Integer>();
	    }
	    
	    /** Returns true if the message should be printed in the given timestamp, otherwise returns false. The timestamp is in seconds granularity. */
	    public boolean shouldPrintMessage(int timestamp, String message) {
			Integer last = lastPrintTime.get(message);

			return last == null 
						&& lastPrintTime.putIfAbsent(message, timestamp) == null
					|| last != null && timestamp - last >= 10 
						&& lastPrintTime.replace(message, last, timestamp);

	    }
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/logger-rate-limiter/discuss/200705/memory-efficient-python-solution-using-queue-and-set
     * https://leetcode.com/problems/logger-rate-limiter/discuss/83294/Straight-forward-Python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/logger-rate-limiter/discuss/114199/concise-HashMap-%2B-Queue-solution-in-C%2B%2B
     */

}

/**
 * Your Logger object will be instantiated and called as such:
 * Logger obj = new Logger();
 * boolean param_1 = obj.shouldPrintMessage(timestamp,message);
 */
