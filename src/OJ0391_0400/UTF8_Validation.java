package OJ0391_0400;

public class UTF8_Validation {
	/*
	 * https://discuss.leetcode.com/topic/58338/bit-manipulation-java-6ms/4
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/61818/simple-one-pass-concise-java-solution-beating-99
	 * https://discuss.leetcode.com/topic/95561/java-solution
	 */
	public boolean validUtf8(int[] data) {
		int count = 0;
		for (int d : data) {
			if (count == 0) {
				if ((d >> 5) == 0b110)
					count = 1;
				else if ((d >> 4) == 0b1110)
					count = 2;
				else if ((d >> 3) == 0b11110)
					count = 3;
				else if ((d >> 7) == 1)
					return false;
			} else {
				if ((d >> 6) != 0b10)
					return false;
				else
					count--;
			}
		}
		return count == 0;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/70751/o-n-java-solution-with-detailed-explaination
	 * 
     * Thought-way: 
     * As long as every byte in the array is of right type, it is a valid UTF-8 encoding.
     * 
     * Method: 
     * Start from index 0, determine each byte's type and check its validity.
     *
     * There are five kinds of valid byte type: 0**, 10**, 110**,1110** and 11110**
     * Give them type numbers, 0, 1, 2, 3, 4 which are the index of the first 0 from left. 
     * So, the index of the first 0 determines the byte type.
     *
     * if a byte belongs to one of them:
        1 : if it is type 0, continue
        2 : if it is type 2 or 3 or 4, check whether the following 1, 2, and 3 byte(s) are of type 1 or not
                if not, return false;
     * else if a byte is type 1 or not of valid type, return false
     *
     * Time O(n), space O(1)
     * 
     * Rf : https://discuss.leetcode.com/topic/95758/java-o-n-time-o-1-space-ac-solution-concise-and-easy-to-understand
     */
    // Hard code "masks" array to find the index of the first appearance of 0 in the lower 8 bits of each integer.
	private int[] masks = { 128, 64, 32, 16, 8 };
	boolean validUtf8_nest_loop(int[] data) {
		int len = data.length;
		for (int i = 0; i < len; i++) {
			int curr = data[i];
			int type = getType(curr);
			if (type == 0) {
				continue;
			} else if (type > 1 && i + type <= len) {
				while (type-- > 1) {
					if (getType(data[++i]) != 1) {
						return false;
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}
	public int getType(int num) {
		for (int i = 0; i < 5; i++) {
			if ((masks[i] & num) == 0) {
				return i;
			}
		}
		return -1;
	}
	
	// self
	public boolean validUtf8_self(int[] data) {
        int bytes = 0;
        int order = 0;
        for (int i = 0; i < data.length; i++) {
            if ((data[i] - 128 < 64) && (data[i] - 128 >= 0) && bytes == 0) {
                return false;
            }
            if ((data[i] - 128 - 64 < 32) && (data[i] - 128 - 64 >= 0) && order == 0) {
            	if (data.length - i < 2)
            		return false;
                bytes = 2;
                order = 1;
                continue;
            }
            if ((data[i] - 128 - 64 - 32 < 16) && (data[i] - 128 - 64 - 32 >= 0) && order == 0) {
            	if (data.length - i < 3)
            		return false;
            	bytes = 3;
                order = 1;
                continue;
            }
            if ((data[i] - 128 - 64 - 32 - 16 < 8) && (data[i] - 128 - 64 - 32 - 16 >= 0) && order == 0) {
            	if (data.length - i < 4)
            		return false;
            	bytes = 4;
                order = 1;
                continue;
            }
            if (data[i] - 128 < 64 && (data[i] - 128 >= 0)) {
                switch(bytes) {
                	case 2 :
                		if (order == 1) {
                			order = 0;
                			bytes = 0;
                		}
                		break;
                	case 3 :
                		if (order == 1) {
                			order++;
                		}
                		else {
                			order = 0;
                			bytes = 0;
                		}
                		break;
                	case 4 :
                		if (order == 1 || order == 2) {
                			order++;
                		}
                		else {
                			order = 0;
                			bytes = 0;
                		}
                		break;
                	default :
                		return false;
                }
            }
            if (data[i] < 128 && bytes != 0) {
            	return false;
            }
            if (data[i] >= 248)
                return false;
            if ((data[i] - 128 - 64 >= 0) && order != 0) {
                return false;
            }
        }
        return true;
    }
	
	/*
	 * [32,196,147,225,184,165,246,149,170,129,204,153,243,188,141,147,0,217,149,234,176,176,243,178,133,144,213,181,193,187,238,137,134,218,155,33,231,134,162,243,184,144,131,71,201,131,244,133,189,140,242,178,128,156,207,154,230,165,181,240,181,134,180,227,129,199,172,226,158,164,214,183,224,137,141,20,194,188,232,177,151,242,157,180,153,200,189,239,153,186,240,153,181,154]   false
	 * [240,162,138,147]   true
	 * [237]   false
	 * [255]   false
	 */

}
