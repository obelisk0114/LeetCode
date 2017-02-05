package OJ201_210;

public class Count_Primes {
	private int countPrimes(int n) {
        n--;
        switch (n) {
            case -1:
                return 0;
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 1;
        }
        int length = n/2;
		if (n % 2 == 1)
			length ++;
		
		int count = 0;
		boolean[] prime = new boolean[length];
		// 2¡B3¡B5¡B7¡B9...
		for (int i = 0; i < length; i++) {
			prime[i] = true;
			count++;
		}

		int end = (int) Math.sqrt(n);
		int endLength = (end - 1) / 2;
		for (int i = 1; i <= endLength; i++) {
			if (prime[i]) {
				//for (int j = ((n / (2 * i + 1)) - 1) / 2; j >= i; j--) {
					//if (prime[j])
						//prime[(((2*i + 1) * (2*j + 1)) - 1)/2] = false;
				//}
				for (int j = ((n / ((i<<1) + 1)) - 1)>>1; j >= i; j--) {
					if (prime[j]) {
						prime[((((i<<1) + 1) * ((j<<1) + 1)) - 1)>>1] = false;
						count--;
					}
				}
			}
		}
		
		return count;
    }
	
	// https://discuss.leetcode.com/topic/35033/12-ms-java-solution-modified-from-the-hint-method-beats-99-95
	private int countPrimes2(int n) {
	    if (n < 3)
	        return 0;
	        
	    boolean[] f = new boolean[n];
	    //Arrays.fill(f, true); boolean[] are initialed as false by default
	    int count = n / 2;
	    for (int i = 3; i * i < n; i += 2) {
	        if (f[i])
	            continue;
	        
	        for (int j = i * i; j < n; j += 2 * i) {
	            if (!f[j]) {
	                --count;
	                f[j] = true;
	            }
	        }
	    }
	    return count;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Count_Primes countprime = new Count_Primes();
		int a = 300000000;
		long preTime = System.currentTimeMillis();
		int ma = (int) Math.sqrt(a);
		long aftTime = System.currentTimeMillis();
		long currentTime = aftTime - preTime;
		System.out.println("Math.sqrt : " + currentTime + " ms");
		
		preTime = System.currentTimeMillis();
		int it = 0;
		for (; it * it < a; it++) {
			;
		}
		it--;
		aftTime = System.currentTimeMillis();
		currentTime = aftTime - preTime;
		System.out.println("Iteration : " + currentTime + " ms");
		System.out.println("iteration : " + it + " ; Math.sqrt : " + ma);
		
		preTime = System.currentTimeMillis();
		int result = countprime.countPrimes(a);
		aftTime = System.currentTimeMillis();
		currentTime = aftTime - preTime;
		System.out.println("Odd count : " + currentTime + " ms");
		System.out.println("Odd count : " + result);
		
		preTime = System.currentTimeMillis();
		result = countprime.countPrimes2(a);
		aftTime = System.currentTimeMillis();
		currentTime = aftTime - preTime;
		System.out.println("12ms count : " + currentTime + " ms");
		System.out.println("12ms count : " + result);

	}

}
