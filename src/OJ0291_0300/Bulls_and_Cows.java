package OJ0291_0300;

import java.util.Map;
import java.util.HashMap;

public class Bulls_and_Cows {
	// https://discuss.leetcode.com/topic/28463/one-pass-java-solution
	public String getHint(String secret, String guess) {
		int bulls = 0;
		int cows = 0;
		int[] numbers = new int[10];
		for (int i = 0; i < secret.length(); i++) {
			int s = Character.getNumericValue(secret.charAt(i));
			int g = Character.getNumericValue(guess.charAt(i));
			if (s == g)
				bulls++;
			else {
				if (numbers[s] < 0)
					cows++;
				if (numbers[g] > 0)
					cows++;
				numbers[s]++;
				numbers[g]--;
			}
		}
		return bulls + "A" + cows + "B";
	}
	
	// https://discuss.leetcode.com/topic/38600/my-3ms-java-solution-may-help-u
	public String getHint_twoPass(String secret, String guess) {
        int len = secret.length();
		int[] secretarr = new int[10];
		int[] guessarr = new int[10];
		int bull = 0, cow = 0;
		for (int i = 0; i < len; i++) {
			if (secret.charAt(i) == guess.charAt(i)) {
				bull++;
			} else {
				secretarr[secret.charAt(i) - '0']++;
				guessarr[guess.charAt(i) - '0']++;
			}
		}
		for (int i = 0; i < 10; ++i) {
			cow += Math.min(secretarr[i], guessarr[i]);
		}
		return bull + "A" + cow + "B";
    }
	
	public String getHint_self(String secret, String guess) {
        int bull = 0;
        int cow = 0;
        Map<Character, Integer> map = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                bull++;
            }
            else {
                map.put(secret.charAt(i), map.getOrDefault(secret.charAt(i), 0) + 1);
                map2.put(guess.charAt(i), map2.getOrDefault(guess.charAt(i), 0) + 1);
            }
        }
        for (Character key : map.keySet()) {
            if (map2.containsKey(key)) {
                cow = cow + Math.min(map.get(key), map2.get(key));
            }
        }
        return bull + "A" + cow + "B";
    }
	
	public static void main(String[] args) {
		Bulls_and_Cows bullCows = new Bulls_and_Cows();
		String secret = "1807";
		String guess = "7810";
		System.out.println("getHint : " + bullCows.getHint(secret, guess));
		System.out.println("getHint_twoPass : " + bullCows.getHint_twoPass(secret, guess));
		System.out.println("getHint_self : " + bullCows.getHint_self(secret, guess));
	}

}
