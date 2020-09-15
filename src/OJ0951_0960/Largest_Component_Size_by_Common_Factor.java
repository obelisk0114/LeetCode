package OJ0951_0960;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;

public class Largest_Component_Size_by_Common_Factor {
	/*
	 * The following class is by myself.
	 * 
	 * Union by rank + path compression + remember size
	 * 
	 * union by rank + 記錄每個 root 的大小
	 * 因為只有 union 才會改變大小，所以 union 時改變 root 的大小，同時更新 max
	 * 只有 root 的 size 才有意義，其他的只是中間狀態
	 * 
	 * 每個數字找出除了 1 之外的所有因數，並用 map 記錄這個因數出現過了沒
	 * 第一次出現就放入 map，之後再出現就和第一次出現的 index 做 union
	 * 最後 return uf 的 max
	 * 
	 * Rf :
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200959/Simplest-Solution-(Union-Find-only)-No-Prime-Calculation
	 */
	class Solution_union_by_rank_record_size_self {
	    class UnionFind {
	        int[] parents;
	        int[] rank;
	        int[] size;
	        int max;
	        
	        public UnionFind(int n) {
	            parents = new int[n];
	            rank = new int[n];
	            size = new int[n];
	            max = 1;
	            
	            for (int i = 0; i < n; i++) {
	                parents[i] = i;
	                rank[i] = 0;
	                size[i] = 1;
	            }
	        }
	        
	        public int find(int n) {
	            if (parents[n] != n) {
	                parents[n] = find(parents[n]);
	            }
	            return parents[n];
	        }
	        
	        public void union(int a, int b) {
	            int rootA = find(a);
	            int rootB = find(b);
	            
	            if (rootA != rootB) {
	                if (rank[rootA] > rank[rootB]) {
	                    parents[rootB] = rootA;
	                    size[rootA] += size[rootB];
	                    
	                    max = Math.max(max, size[rootA]);
	                }
	                else {
	                    parents[rootA] = rootB;
	                    
	                    if (rank[rootA] == rank[rootB]) {
	                        rank[rootB]++;
	                    }
	                    
	                    size[rootB] += size[rootA];
	                    max = Math.max(max, size[rootB]);
	                }
	            }
	        }
	    }
	    
	    public int largestComponentSize(int[] A) {
	        UnionFind uf = new UnionFind(A.length);
	        
	        Map<Integer, Integer> map = new HashMap<>();
	        for (int i = 0; i < A.length; i++) {
	            for (int j = 2; j * j <= A[i]; j++) {
	                if (A[i] % j == 0) {
	                    if (map.containsKey(j)) {
	                        uf.union(i, map.get(j));
	                    }
	                    else {
	                        map.put(j, i);
	                    }
	                    
	                    if (map.containsKey(A[i] / j)) {
	                        uf.union(i, map.get(A[i] / j));
	                    }
	                    else {
	                        map.put(A[i] / j, i);
	                    }
	                }
	            }
	            
	            if (map.containsKey(A[i])) {
	                uf.union(i, map.get(A[i]));
	            }
	            else {
	                map.put(A[i], i);
	            }
	        }
	        
	        return uf.max;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200959/Simplest-Solution-(Union-Find-only)-No-Prime-Calculation
	 * 
	 * Union Find template. The only additional stuff is one hashmap which is used to 
	 * convert factor to the node index in A for union.
	 * 
	 * HashMap: key is the factor, val is the index in A
	 * 
	 * Other code:
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200959/Simplest-Solution-(Union-Find-only)-No-Prime-Calculation/277687
	 */
	class Solution_union_record_size {
		class UF {
			int[] parent;
			int[] size;
			int max;

			public UF(int N) {
				parent = new int[N];
				size = new int[N];
				max = 1;
				
				for (int i = 0; i < N; i++) {
					parent[i] = i;
					size[i] = 1;
				}
			}

			public int find(int x) {
				if (x == parent[x]) {
					return x;
				}
				return parent[x] = find(parent[x]);
			}

			public void union(int x, int y) {
				int rootX = find(x);
				int rootY = find(y);
				
				if (rootX != rootY) {
					parent[rootX] = rootY;
					size[rootY] += size[rootX];
					
					max = Math.max(max, size[rootY]);
				}
			}
		}

		public int largestComponentSize(int[] A) {
			int N = A.length;
			
			// key is the factor, val is the node index
			Map<Integer, Integer> map = new HashMap<>();
			
			UF uf = new UF(N);
			for (int i = 0; i < N; i++) {
				int a = A[i];
				for (int j = 2; j * j <= a; j++) {
					if (a % j == 0) {
						// this means that no index has claimed the factor yet
						if (!map.containsKey(j)) {
							map.put(j, i);
						}
						// this means that one index already claimed, 
						// so union that one with current
						else {
							uf.union(i, map.get(j));
						}
						
						if (!map.containsKey(a / j)) {
							map.put(a / j, i);
						} 
						else {
							uf.union(i, map.get(a / j));
						}
					}
				}
				
				// a could be factor too. Don't miss this
				if (!map.containsKey(a)) {
					map.put(a, i);
				} 
				else {
					uf.union(i, map.get(a));
				}
			}
			return uf.max;
		}
	}
	
	/*
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200712/Fast-than-100-concise-java-solution
	 * 
	 * Step 1. for each element make it as distinct min prime factor save it to a map
	 * (key is prime factor, value is element index set).
	 * eg. {10, 30, 18}->{2: {0,1,2}, 3 {1,2}, 5: {0,1}}
	 * 
	 * Step 2. for each set do union and find max cnt
	 */
	class solution_factorization {
		int[] par;
		int[] cnt;

		private int find(int i) {
			if (i == par[i])
				return i;
			
			return par[i] = find(par[i]);
		}

		private void union(int i, int j) {
			int pi = find(i);
			int pj = find(j);
			
			if (pi == pj)
				return;
			
			par[pi] = pj;
			cnt[pj] += cnt[pi];
		}

		public int largestComponentSize(int[] A) {
			int N = A.length;
			par = new int[N];
			cnt = new int[N];
			
			Map<Integer, Set<Integer>> prime2Idx = new HashMap<>();
			for (int i = 0; i < N; i++) {
				int d = 2, x = A[i];
				while (d * d <= x) {
					if (x % d == 0) {
						while (x % d == 0)
							x /= d;
						
						prime2Idx.putIfAbsent(d, new HashSet<>());
						prime2Idx.get(d).add(i);
					}
					d++;
				}
				
				if (x > 1) {
					prime2Idx.putIfAbsent(x, new HashSet<>());
					prime2Idx.get(x).add(i);
				}
			}
			
			for (int i = 0; i < N; i++)
				par[i] = i;
			
			Arrays.fill(cnt, 1);
			
			int max = 1;
			for (Set<Integer> s : prime2Idx.values()) {
				int fir = s.iterator().next();
				
				for (int idx : s) {
					union(idx, fir);
					max = Math.max(cnt[find(idx)], max);
				}
			}
			return max;
		}
	}
	
	/*
	 * The following variable and 3 functions are modified by myself.
	 * 
	 * 1. Calculate all prime numbers less than 100000.
	 * 2. For each number in A, we say A[i]
	 *    a. Do Prime Factorization (Brute force using the primes set in step 1), we 
	 *       say the prime factor is p.
	 *    b. If p has presented in the primeToIndex, union i and primeToIndex[p].
	 *    c. Update primeToIndex[p] to i.
	 * 3. Return the maximum count.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200546/Prime-Factorization-and-Union-Find
	 */
	int max_limit_prime = 1;

	public int largestComponentSize_limit_prime(int[] A) {
		boolean[] isPrime = new boolean[100001];
		Arrays.fill(isPrime, true);
		
		Set<Integer> primes = new HashSet<>();
		
		// all primes less than 100000
		int limit = (int) Math.sqrt(100000);
		for (int i = 2; i <= 100000; i++) {
			if (isPrime[i]) {
				primes.add(i);
				
				if (i > limit) {
					continue;
				}
				for (int j = i; j * i <= 100000; j++) {
					isPrime[j * i] = false;
				}
			}
		}
		
		int n = A.length;
		int[] counts = new int[n];
		int[] parents = new int[n];
		
		int[] primeToIndex = new int[100001];
		Arrays.fill(primeToIndex, -1);
		
		for (int i = 0; i < n; i++) {
			parents[i] = i;
			counts[i] = 1;
		}
		
		for (int i = 0; i < n; i++) {
			int a = A[i];
			for (int p : primes) {
				// Faster. It will be better if it is moved to upper level.
				if (primes.contains(a)) {
					p = a;
				}
				
				if (a % p == 0) {
					if (primeToIndex[p] > -1) {
						union_limit_prime(parents, counts, primeToIndex[p], i);
					}
					primeToIndex[p] = i;
					
					while (a % p == 0) {
						a /= p;
					}
				}
				
				if (a == 1) {
					break;
				}
			}
		}
		return max_limit_prime;
	}

	private int find_limit_prime(int[] parents, int a) {
		if (parents[a] != a) {
			parents[a] = find_limit_prime(parents, parents[a]);
		}
		
		return parents[a];
	}

	private void union_limit_prime(int[] parents, int[] counts, int a, int b) {
		int root1 = find_limit_prime(parents, a), 
			root2 = find_limit_prime(parents, b);
		
		if (root1 == root2) {
			return;
		}
		
		// union
		int count = counts[root2] + counts[root1];
        max_limit_prime = Math.max(count, max_limit_prime);
        parents[root1] = root2;
        counts[root2] = count;
		
		// or union by size
        /*
		if (counts[root1] > counts[root2]) {
			parents[root2] = root1;
			counts[root1] += counts[root2];
			max_limit_prime = Math.max(counts[root1], max_limit_prime);
		}
		else {
			parents[root1] = root2;
			counts[root2] += counts[root1];
			max_limit_prime = Math.max(counts[root2], max_limit_prime);
		}
		*/		
	}
	
	/*
	 * by myself
	 * 
	 * 用 array 中最大的數字建立質數表，我們就可以利用質數表對每一個數字做質因數分解
	 * 質數表用 LinkedHashSet 儲存，方便按照順序放入和快速搜尋
	 * Map <質因數，位置> 方便使用 UnionFind
	 * 最後每個數字 find parent 做最後合併
	 * 
	 * Use the maximum number in array to build the prime number table
	 * We can use the prime number table to perform prime factorization
	 */
	class solution_prime_number_table_self {
		public int largestComponentSize(int[] A) {
	        UnionFind uf = new UnionFind(A.length);
	        
	        int max = A[0];
	        for (int i : A) {
	            max = Math.max(max, i);
	        }
	        
	        Map<Integer, Integer> map = new HashMap<>();
	        Set<Integer> primes = getPrime(max);
	        for (int i = 0; i < A.length; i++) {
	            if (primes.contains(A[i])) {
	                if (map.containsKey(A[i])) {
	                    uf.union(i, map.get(A[i]));
	                }
	                else {
	                    map.put(A[i], i);
	                }
	                
	                continue;
	            }
	            
	            int cur = A[i];
	            for (int p : primes) {
	                if (p * p > A[i]) {
	                    break;
	                }
	                
	                if (cur % p == 0) {
	                    //System.out.println(A[i] + " = " + p + " * " + (A[i]/p));
	                    if (map.containsKey(p)) {
	                        uf.union(i, map.get(p));
	                    }
	                    else {
	                        map.put(p, i);
	                    }
	                    
	                    while (cur % p == 0) {
	                        cur /= p;
	                    }
	                    
	                    if (primes.contains(cur)) {
	                        if (map.containsKey(cur)) {
	                            uf.union(i, map.get(cur));
	                        }
	                        else {
	                            map.put(cur, i);
	                        }
	                    }
	                }
	            }
	            //System.out.println(A[i] + " = " + Arrays.toString(uf.parents));
	        }
	        
	        for (int i = 0; i < A.length; i++) {
	            uf.find(i);
	        }
	        
	        return uf.getLargestSize();
	    }
		
		private Set<Integer> getPrime(int n) {
	        Set<Integer> res = new LinkedHashSet<>();
	        
	        boolean[] p = new boolean[n + 1];
	        for (int i = 2; i <= n; i++) {
	            if (!p[i]) {
	                res.add(i);
	                
	                for (int j = i; i * j <= n; j++) {
	                    if (i * j < 0) {
	                        break;
	                    }
	                    p[i * j] = true;
	                }
	            }
	        }
	        
	        return res;
	    }
		
		class UnionFind {
			int[] parents;
			int[] rank;
			
			public UnionFind(int n) {
				parents = new int[n];
				for (int i = 0; i < n; i++) {
					parents[i] = i;
				}
				
				rank = new int[n];
			}
			
			public int find(int n) {
				if (parents[n] != n) {
					parents[n] = find(parents[n]);
				}
				return parents[n];
			}
			
			public void union(int a, int b) {
				int rootA = find(a);
				int rootB = find(b);
				
				if (rootA != rootB) {
					if (rank[rootA] > rank[rootB]) {
						parents[rootB] = rootA;
					}
					else {
						parents[rootA] = rootB;
						
						if (rank[rootA] == rank[rootB]) {
							rank[rootB]++;
						}
					}
				}
			}
			
			public int getLargestSize() {
				Map<Integer, Integer> map = new HashMap<>();
				for (int i = 0; i < parents.length; i++) {
					int count = map.getOrDefault(parents[i], 0);
					map.put(parents[i], count + 1);
				}
				
				int max = 0;
				for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
					max = Math.max(max, entry.getValue());
				}
				return max;
			}
		}
	}
	
	/*
	 * The following 3 variables and 3 functions are from this link.
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/821162/Java-2-simple-solutions-one-using-DFS-and-another-using-union-find
	 * 
	 * We call dfs in loop and keep track of size of each component. 
	 * There are duplicate nodes in the adjacency list but it doesn't affect that much 
	 * due to the use of visited array.
	 */
	List<List<Integer>> graph_dfs;
	boolean[] visited_dfs;
	int count_dfs = 0;

	public int largestComponentSize_dfs(int[] A) {

		// Getting prime factors of every element in the array A
		List<Set<Integer>> primes = new ArrayList<>(A.length);
		for (int i = 0; i < A.length; i++) {
			primes.add(primeFact_dfs(A[i]));
		}

		// Generating HashMap with every prime factor as key and list of
		// elements' index that has the particular number as prime factor as value
		Map<Integer, List<Integer>> mapping = new HashMap<>();
		for (int i = 0; i < primes.size(); i++) {
			for (int fact : primes.get(i)) {
				mapping.putIfAbsent(fact, new ArrayList<Integer>());
				mapping.get(fact).add(i);
			}
		}

		// Initializing graph
		graph_dfs = new ArrayList<>(A.length);
		for (int i = 0; i < A.length; i++) {
			graph_dfs.add(new ArrayList<>());
		}
		
		visited_dfs = new boolean[A.length];

		// Adding edges between nodes that share common prime factors
		// There will be duplicate node in this adjacency list. We can use HashSet but
		// overhead is big!
		// As during dfs we do use visited array so every node is visited once so
		// duplicate entries don't bother that much
		for (List<Integer> arr : mapping.values()) {
			for (int i = 1; i < arr.size(); i++) {
				graph_dfs.get(arr.get(i - 1)).add(arr.get(i));
				graph_dfs.get(arr.get(i)).add(arr.get(i - 1));
			}
		}

		// finding the size of largest connected component
		int max = 0;
		for (int i = 0; i < A.length; i++) {
			if (!visited_dfs[i]) {
				count_dfs = 0;
				dfs(i);
				max = Math.max(max, count_dfs);
			}
		}

		return max;
	}

	private void dfs(int node) {
		visited_dfs[node] = true;
		count_dfs++;
		
		for (int nbr : graph_dfs.get(node)) {
			if (!visited_dfs[nbr]) {
				dfs(nbr);
			}
		}
	}

	private Set<Integer> primeFact_dfs(int n) {
		Set<Integer> ans = new HashSet<>();
		if (n == 1)
			return ans;

		if (n % 2 == 0) {
			ans.add(2);
			
			while (n % 2 == 0) {
				n /= 2;
			}
		}
		
		for (int i = 3; i <= Math.sqrt(n); i += 2) {
			if (n % i == 0) {
				ans.add(i);
				
				while (n % i == 0) {
					n /= i;
				}
			}
		}
		if (n > 2)
			ans.add(n);
		
		return ans;
	}
	
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * Build a map with keys: prime numbers, 
	 * and values: the numbers which have this prime in their decomposition.
	 * spf (smallest prime factor)
	 * 
	 * 1. We build the Sieve and the primes dictionary.
	 * 2. For each number that we haven't visited yet, we run a BFS over the primes 
	 *    in its decomposition.
	 * 3. For each prime, we find other numbers which have this prime in their 
	 *    decomposition, and we increment the number of components in our 
	 *    current ('cur') component. Also, we add to our queue other factors which we 
	 *    haven't visited.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/822954/C%2B%2B-and-Python-or-Explained-or-2-solutions-based-on-Sieve-or-O(n*log(m))-O(m)
	 */
	public int largestComponentSize_BFS(int[] A) {
        int max = A[0];
        for (int i : A) {
            max = Math.max(max, i);
        }
        
        int[] spf = sieve_BFS(max);
        Map<Integer, Set<Integer>> prime2Number = new HashMap<>();
        Map<Integer, Set<Integer>> factors = new HashMap<>();
        Set<Integer> visited_nums = new HashSet<>();
        Set<Integer> visited_factors = new HashSet<>();
        int ans = 1;
        
        for (int num : A) {
            int x = num;
            while (x != 1) {
                prime2Number.putIfAbsent(spf[x], new HashSet<>());
                prime2Number.get(spf[x]).add(num);
                
                factors.putIfAbsent(num, new HashSet<>());
                factors.get(num).add(spf[x]);
                
                x /= spf[x];
            }
        }
        
        for (int num : A) {
            if (visited_nums.contains(num))
                continue;
                
            visited_nums.add(num);
            
            int cur = 1;
            Deque<Integer> queue = new LinkedList<>();
            if (factors.containsKey(num)) {
                for (int factor : factors.get(num)) {
                    queue.offerLast(factor);
                }
            }
                
            while (!queue.isEmpty()) { 
                int factor = queue.pollFirst();
                visited_factors.add(factor);
                
                for (int next_num : prime2Number.get(factor)) {
                    if (visited_nums.contains(next_num))
                        continue;
                        
                    visited_nums.add(next_num);
                    cur += 1;
                    
                    for (int next_factor : factors.get(next_num)) {
                        if (visited_factors.contains(next_factor))
                            continue;
                        
                        visited_factors.add(next_factor);
                        queue.offerLast(next_factor);
                    }
                }
            }
            
            ans = Math.max(ans, cur);
        }
        
        return ans;
    }
    
    public int[] sieve_BFS(int n) {
        int[] spf = new int[n + 1];
        for (int i = 0; i < spf.length; i++) {
            spf[i] = i;
        }
        
        for (int i = 2; i < spf.length; i += 2) {
            spf[i] = 2;
        }
        
        for (int i = 3; i * i < spf.length; i += 2) {
            if (spf[i] == i) {
                int j = i * i;
                while (j < spf.length) {
                    spf[j] = i;
                    j += i;
                }
            }
        }
        return spf;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/819957/Java%3A-Short-and-concise
	 */
	public int largestComponentSize_union_factor(int[] A) {
		Map<Integer, Integer> parent = new HashMap<>();

		for (int num : A)
			for (int fact = 2; fact * fact <= num; fact++)
				if (num % fact == 0) {
					union_union_factor(num, fact, parent);
					union_union_factor(num, num / fact, parent);
				}

		int max = 1;
		Map<Integer, Integer> freq = new HashMap<>();
		for (Integer v : A) {
			int f = find_union_factor(v, parent);
			
			if (freq.containsKey(f)) {
				freq.put(f, freq.get(f) + 1);
				max = Math.max(max, freq.get(f));
			} 
			else
				freq.put(f, 1);
		}
		return max;
	}

	public void union_union_factor(int n, int m, Map<Integer, Integer> p) {
		int findN = find_union_factor(n, p);
		int findM = find_union_factor(m, p);
		
		if (findN < findM)
			p.put(findM, findN);
		else
			p.put(findN, findM);
	}

	public int find_union_factor(Integer i, Map<Integer, Integer> parent) {
		if (parent.get(i) == null)
			parent.put(i, i);
		
		while (i != parent.get(i))
			i = parent.get(i);
		return i;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/819919/Python-Union-find-solution-explained
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200643/Python-1112-ms-beats-100-Union-Find-and-Prime-factor-decomposition-with-Optimization
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/581394/Python-1032ms-beat-100-time-and-space
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/201046/C%2B%2B-232ms-Union-Find-Solution
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/788988/C%2B%2B-simple-compact-Union-Find-97-space-and-time
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/200613/C%2B%2B-Sieve-of-Erastosthenes-219ms-roughly-O(n-*-log2(n))
     * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/820634/C-Union-find-solution
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/largest-component-size-by-common-factor/discuss/204260/Javascript-Python3-C%2B%2B-Union-Find
	 */

}
