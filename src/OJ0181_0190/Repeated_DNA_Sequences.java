package OJ0181_0190;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Repeated_DNA_Sequences {
	/*
	 * https://discuss.leetcode.com/topic/27517/7-lines-simple-java-o-n
	 * 
	 * Other code : 
	 * https://discuss.leetcode.com/topic/33745/easy-understand-and-straightforward-java-solution
	 */
	public List<String> findRepeatedDnaSequences_2_HashSet(String s) {
		Set<String> seen = new HashSet<>(), repeated = new HashSet<>();
	    for (int i = 0; i + 9 < s.length(); i++) {
	        String ten = s.substring(i, i + 10);
	        if (!seen.add(ten))
	            repeated.add(ten);
	    }
	    return new ArrayList<>(repeated);
	}
	
	// by myself
	public List<String> findRepeatedDnaSequences_self(String s) {
        List<String> list = new ArrayList<>();
        if (s.length() <= 10)
            return list;
        
        Map<String, Integer> go = new HashMap<>();
        for (int i = 0; i < s.length() - 9; i++) {
            String s1 = s.substring(i, i + 10);
            int count = go.getOrDefault(s1, 0) + 1;
            go.put(s1, count);
            if (count == 2)
                list.add(s1);
        }
        
        return list;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/22737/easy-to-understand-java-solution-with-well-commented-code
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/29796/java-28ms-solution-beats-100-of-java-submissions
	 * https://discuss.leetcode.com/topic/8894/clean-java-solution-hashmap-bits-manipulation/34
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/8894/clean-java-solution-hashmap-bits-manipulation/9
	 * https://discuss.leetcode.com/topic/8894/clean-java-solution-hashmap-bits-manipulation
	 */
	public List<String> findRepeatedDnaSequences_HashSet_with_bit(String s) {
		Set<Integer> firstTime = new HashSet<Integer>();
		Set<Integer> secondTime = new HashSet<Integer>();
		List<String> list = new ArrayList<String>();

		char[] map = new char[26];
		int len = s.length();

		// Hashing function. We have only 4 letters which we can represent by 2 bits.
		map['A' - 'A'] = 0; // A = 00
		map['C' - 'A'] = 1; // B = 01
		map['G' - 'A'] = 2; // C = 10
		map['T' - 'A'] = 3; // D = 11

		for (int i = 0; i <= len - 10; i++) {
			int sequence = 0;
			for (int j = i; j < i + 10; j++) {
				// Shift existing sequence by two to make space for the new character coming
				sequence = sequence << 2;

				// Copy the character from the map and paste those two bits in the newly
				// created space. Read bit wise OR.
				sequence = sequence | map[s.charAt(j) - 'A'];
			}

			// For this number to be added in the list, this should be the
			// second time this number is appearing
			// For this if condition to be true, firstTime.add() should be false.
			// firstTime.add() will be false when there is already the same number present.
			// How it will behave?
			// First time - firstTime.add(sequence) will return T
			// !firstTime.add(sequence) will become F
			// secondTime.add(sequence) will NOT be executed

			// Second time addition:
			// First time - firstTime.add(sequence) will return F
			// !firstTime.add(sequence) will become T
			// secondTime.add(sequence) will be executed
			if (!firstTime.add(sequence) && secondTime.add(sequence)) {
				list.add(s.substring(i, i + 10));
			}
		}

		return list;
	}
	
	// https://discuss.leetcode.com/topic/8539/short-java-rolling-hash-solution

}
