package OJ411_420;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * https://discuss.leetcode.com/topic/78329/java-fuzz-buzz-follow-up-no-if-else-and-extendable
 */

public class Fizz_Buzz {
	List<String> fizzBuzz(int n) {
		List<String> out = new ArrayList<String>();
		for (int i = 1; i <= n; i++) {
			if ( (i % 3 == 0) && (i % 5 != 0) ) {
				out.add("Fizz");
			}
			else if ( (i % 5 == 0) && (i % 3 != 0) ) {
				out.add("Buzz");
			}
			else if ( (i % 5 == 0) && (i % 3 == 0) ) {
				out.add("FizzBuzz");
			}
			else {
				out.add(String.valueOf(i));
			}
		}
		return out;
	}
	
	// https://discuss.leetcode.com/topic/69048/java-3-ms-solution
	public List<String> fizzBuzz2(int n) {
        String[] arr = new String[n];
        for (int i = 0, j = 1; i < n; i++, j++) {
            if      (j % 15 == 0) arr[i] = "FizzBuzz";
            else if (j %  3 == 0) arr[i] = "Fizz";
            else if (j %  5 == 0) arr[i] = "Buzz";
            else                  arr[i] = String.valueOf(j);
        }
        return Arrays.asList(arr);
    }
	
	// https://discuss.leetcode.com/topic/63995/java-4ms-solution-not-using-operation
	public List<String> fizzBuzz3(int n) {
        List<String> ret = new ArrayList<String>(n);
        for(int i=1,fizz=0,buzz=0;i<=n ;i++){
            fizz++;
            buzz++;
            if(fizz==3 && buzz==5){
                ret.add("FizzBuzz");
                fizz=0;
                buzz=0;
            }else if(fizz==3){
                ret.add("Fizz");
                fizz=0;
            }else if(buzz==5){
                ret.add("Buzz");
                buzz=0;
            }else{
                ret.add(String.valueOf(i));
            }
        } 
        return ret;
    }

}
