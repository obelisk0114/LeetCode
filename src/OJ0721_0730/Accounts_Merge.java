package OJ0721_0730;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class Accounts_Merge {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/140978/Easy-to-Understand-Union-Find-in-Java-95
	 * 
	 * Other code:
	 * https://leetcode.com/problems/accounts-merge/discuss/109160/HashMap-plus-union-found-solution-using-Java-programming!
	 */
	public List<List<String>> accountsMerge_findParent(List<List<String>> accounts) {
		int[] parents = new int[accounts.size()];
		for (int i = 0; i < accounts.size(); i++) {
			parents[i] = i;
		}
		
		Map<String, Integer> owners = new HashMap<>();
		for (int i = 0; i < accounts.size(); i++) {
			for (int j = 1; j < accounts.get(i).size(); j++) {
				String email = accounts.get(i).get(j);
				if (owners.containsKey(email)) {
					int person = owners.get(email);
					int p1 = findParent(parents, i);
					int p2 = findParent(parents, person);
					
					if (p1 != p2) {
						parents[p2] = p1;
					}
				} 
				else {
					owners.put(email, i);
				}
			}
		}

		Map<Integer, TreeSet<String>> users = new HashMap<>();
		for (int i = 0; i < accounts.size(); i++) {
			int parent = findParent(parents, i);
			List<String> emails = accounts.get(i);
			
			users.putIfAbsent(parent, new TreeSet<String>());
			users.get(parent).addAll(emails.subList(1, emails.size()));
		}

		List<List<String>> res = new ArrayList<List<String>>();
		for (Integer idx : users.keySet()) {
			String name = accounts.get(idx).get(0);
			ArrayList<String> emails = new ArrayList<>(users.get(idx));
			emails.add(0, name);
			res.add(emails);
		}
		return res;
	}

	private int findParent(int[] parents, int idx) {
		while (idx != parents[idx]) {
			parents[idx] = parents[parents[idx]];
			idx = parents[idx];
		}
		return idx;
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/326777/Java-Disjoint-Union-Set-with-path-compression-and-union-by-rank-heuristics
	 * 
	 * Union Find - important
	 * 
	 * We use a hash map to map emails to account's indices (index of the account in 
	 * accounts list). We perform union operation on account ids. This might possibly 
	 * reduce the number of union operations, compared to when we perform union on all 
	 * emails. If an email has been observed before, we fetch one is the current 
	 * account's index, and the other is the account index from the map. We then 
	 * perform union on the account indices.
	 * 
	 * In the end, all account ids which belong to the same user get grouped together 
	 * and return the same account id on calling findSet. For each email, we check 
	 * which account id it belongs to, get that account's parent's id from the union 
	 * structure, and add the email to that id in a new map.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/accounts-merge/discuss/279085/java-33ms-union-find-solution-with-detailed-explanation
	 */
	class UnionSet {
		int[] parents;
		int[] ranks;

		UnionSet(int n) {
			parents = new int[n];
			ranks = new int[n];
			for (int i = 0; i < n; i++)
				parents[i] = i;
		}

		public int findSet(int idx) {
			if (parents[idx] != idx)
				parents[idx] = findSet(parents[idx]);
			return parents[idx];
		}

		public void union(int idA, int idB) {
			int rootA = findSet(idA);
			int rootB = findSet(idB);
			if (rootA != rootB) {
				if (ranks[rootA] > ranks[rootB]) {
					parents[rootB] = rootA;
				} 
				else {
					parents[rootA] = rootB;
					if (ranks[rootA] == ranks[rootB]) {
						ranks[rootB]++;
					}
				}
			}
		}
	}

	public List<List<String>> accountsMerge_Union(List<List<String>> accounts) {
		int n = accounts.size();
		Map<String, Integer> mp = new TreeMap<>();

		UnionSet unionSet = new UnionSet(n);

		for (int i = 0; i < n; i++) {
			List<String> account = accounts.get(i);
			for (int j = 1; j < account.size(); j++) {
				String email = account.get(j);
				if (mp.containsKey(email)) {
					unionSet.union(mp.get(email), i);
				} 
				else {
					mp.put(email, i);
				}
			}
		}
		
		Map<Integer, List<String>> mp2 = new HashMap<>();
		for (Map.Entry<String, Integer> e : mp.entrySet()) {
			String email = e.getKey();
			Integer userIdx = e.getValue();
			int rootUser = unionSet.findSet(userIdx);
			if (!mp2.containsKey(rootUser)) {
				mp2.put(rootUser, new ArrayList<String>());
				
				// add account owner's name to list
				mp2.get(rootUser).add(accounts.get(rootUser).get(0));
			}
			mp2.get(rootUser).add(email);
		}

		return new ArrayList<>(mp2.values());
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/109157/JavaC%2B%2B-Union-Find
	 * 
	 * 1. The key task here is to connect those emails.
	 * 2. to group these emails, each group need to have a representative, or parent.
	 * 3. At the beginning, set each email as its own representative.
	 * 4. Emails in each account naturally belong to a same group, and should be 
	 *    joined by assigning to the same parent (let's use the parent of first email 
	 *    in that list);
	 *    
	 * The reason we do not use "equals" is we are actually comparing the string's 
	 * references.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/accounts-merge/discuss/109157/JavaC++-Union-Find/113864
	 * 
	 * Other code:
	 * https://leetcode.com/problems/accounts-merge/discuss/109157/JavaC++-Union-Find/241144
	 */
	public List<List<String>> accountsMerge_HashMaps(List<List<String>> acts) {
		Map<String, String> owner = new HashMap<>();
		Map<String, String> parents = new HashMap<>();
		Map<String, TreeSet<String>> unions = new HashMap<>();
		for (List<String> a : acts) {
			for (int i = 1; i < a.size(); i++) {
				parents.put(a.get(i), a.get(i));
				owner.put(a.get(i), a.get(0));
			}
		}
		
		for (List<String> a : acts) {
			String p = find(a.get(1), parents);
			for (int i = 2; i < a.size(); i++)
				parents.put(find(a.get(i), parents), p);
		}
		
		for (List<String> a : acts) {
			String p = find(a.get(1), parents);
			if (!unions.containsKey(p))
				unions.put(p, new TreeSet<>());
			for (int i = 1; i < a.size(); i++)
				unions.get(p).add(a.get(i));
		}
		
		List<List<String>> res = new ArrayList<>();
		for (String p : unions.keySet()) {
			List<String> emails = new ArrayList<>(unions.get(p));
			emails.add(0, owner.get(p));
			res.add(emails);
		}
		return res;
	}

	private String find(String s, Map<String, String> p) {
		return p.get(s) == s ? s : find(p.get(s), p);
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/109181/Java-Union-Find-solution-with-explanation/121988
	 * 
	 * 1. Given an email address, we need to know if it occurred before; 
	 *    if it did, we need to know the user associate with it.
	 * 2. Given a user name,we know which account it belongs to
	 * 
	 * For (2), we could assume every account is unique and use the index as the 
	 * account number. So the user name could be easily found with 
	 * accounts.get(index).get(0); Then we build Map<Integer, Set> map to map the 
	 * account index with all its emails
	 * For (1), we build Map<String, Integer> m1 to record email and its account 
	 * number. And use Union-Find to build account connection: When we find current 
	 * email address is already in m1, we union current account number.
	 */
	public List<List<String>> accountsMerge_UnionFind(List<List<String>> accounts) {
        List<List<String>> res = new LinkedList<>();        
        if(accounts == null||accounts.size() == 0) 
            return res;
        
        int n = accounts.size();
        UnionFind uf = new UnionFind(n);
        
        // m1 records "email,accountIndex" pairs
        Map<String,Integer> emailAcct = new HashMap<>();
       
        for(int i = 0; i < n; i++){            
            for(int j = 1; j < accounts.get(i).size(); j++){
                String email = accounts.get(i).get(j);
                
                // If we encounter email before, simply merge two cluster.
                if(emailAcct.containsKey(email)){
                    uf.union(emailAcct.get(email), i);
                } 
                else {
                    emailAcct.put(email, i);
                }
            }
        }
        
        // Use map to record the merging result, Set<String> to remove duplicates.
        Map<Integer, Set<String>> map = new HashMap<>();
        for(int i = 0; i < n; i++){
            int root = uf.find(i);
            // merge all emails associate with i to p
            if(!map.containsKey(root)) 
                map.put(root, new HashSet<>());
            
            List<String> emails = accounts.get(i);
            map.get(root).addAll(emails.subList(1, emails.size()));
        }
        
        // Adding entries to the list
        for(int key : map.keySet()){
            List<String> oneres = new LinkedList<>();
            // adding emails
            oneres.addAll(map.get(key));
            // Sort the list
            Collections.sort(oneres);            
            // get "name" using index 
            oneres.add(0,accounts.get(key).get(0));
            res.add(oneres);
        }
        return res;
    }
	
	class UnionFind {
	    int[] id;
	    int count;
	    
	    public UnionFind(int n) {
	        id = new int[n];
	        count = 0;
	        for (int i = 0; i < n; i++){
	            id[i] = i;
	            count++;
	        }
	    }
	    
	    public int find(int x) {
	        while (x != id[x]) {
	            id[x] = id[id[x]];
	            x = id[x];
	        }
	        return x;
	    }
	    
	    public boolean connected(int x, int y) {
	        return find(x) == find(y);
	    }
	    
	    public void union(int x, int y) {
	        int i = find(x), j = find(y);
	        if (i == j) return;
	        id[i] = j;
	        count--;
	    }
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/109158/Java-Solution-(Build-graph-%2B-DFS-search)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/accounts-merge/discuss/109158/Java-Solution-(Build-graph-+-DFS-search)/111223
	 * 
	 * Other code:
	 * https://leetcode.com/problems/accounts-merge/discuss/339010/solutions-does-not-take-care-of-one-edge-case-where-accounti's-length-is-1
	 */
	public List<List<String>> accountsMerge_dfs2(List<List<String>> accounts) {
		// <email node, neighbor nodes>
		Map<String, Set<String>> graph = new HashMap<>();
		// <email, username>
		Map<String, String> name = new HashMap<>();
		
		// Build the graph;
		for (List<String> account : accounts) {
			String userName = account.get(0);
			for (int i = 1; i < account.size(); i++) {
				if (!graph.containsKey(account.get(i))) {
					graph.put(account.get(i), new HashSet<>());
				}
				name.put(account.get(i), userName);

				if (i == 1)
					continue;
				
				graph.get(account.get(i)).add(account.get(i - 1));
				graph.get(account.get(i - 1)).add(account.get(i));
			}
		}

		Set<String> visited = new HashSet<>();
		List<List<String>> res = new LinkedList<>();
		
		// DFS search the graph;
		for (String email : name.keySet()) {
			List<String> list = new LinkedList<>();
			if (visited.add(email)) {
				dfs2(graph, email, visited, list);
				Collections.sort(list);
				list.add(0, name.get(email));
				res.add(list);
			}
		}

		return res;
	}

	public void dfs2(Map<String, Set<String>> graph, String email, 
			Set<String> visited, List<String> list) {
		
		list.add(email);
		for (String next : graph.get(email)) {
			if (visited.add(next)) {
				dfs2(graph, next, visited, list);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/109161/Python-Simple-DFS-with-explanation!!!/111233
	 * 
	 * # emails_accounts_map (names) of email to account ID
	 * {
	 *   "johnsmith@mail.com": [0, 2],
	 *   "john00@mail.com": [0],
	 *   "johnnybravo@mail.com": [1],
	 *   "john_newyork@mail.com": [2],
	 *   "mary@mail.com": [3]
	 * }
	 * 
	 * Next we do a DFS on each account in accounts list and look up "names" to tell 
	 * us which accounts are linked to that particular account via common emails. 
	 * This will make sure we visit each account only once. This is a recursive 
	 * process and we should collect all the emails that we encounter along the way.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/accounts-merge/discuss/112723/My-easy-Java-Solution-using-dfs-(iterative)
	 */
	public List<List<String>> accountsMerge_dfs3(List<List<String>> accounts) {
		// map email to names using indexes
		Map<String, List<Integer>> names = new HashMap<>();
		for (int i = 0; i < accounts.size(); i++) {
			List<String> data = accounts.get(i);
			for (int j = 1; j < data.size(); j++) {
				String email = data.get(j);
				List<Integer> list = names.get(email);
				if (list == null) {
					list = new ArrayList<Integer>();
					names.put(email, list);
				}
				list.add(i);
			}
		}
		
		boolean[] visited = new boolean[accounts.size()];
		List<List<String>> res = new LinkedList<>();
		
		for (int i = 0; i < accounts.size(); i++) {
			Set<String> set = new TreeSet<String>();
			dfs3(i, accounts, names, visited, set);
			
			if (!set.isEmpty()) {
				List<String> list = new LinkedList<String>(set);
				list.add(0, accounts.get(i).get(0));
				res.add(list);
			}
		}
		return res;
	}

	private void dfs3(int cur, List<List<String>> accounts, 
			Map<String, List<Integer>> names, boolean[] visited, Set<String> set) {
		if (visited[cur]) {
			return;
		}
		
		visited[cur] = true;
		for (int i = 1; i < accounts.get(cur).size(); i++) {
			String email = accounts.get(cur).get(i);
			set.add(email);
			for (int index : names.get(email)) {
				dfs3(index, accounts, names, visited, set);
			}
		}
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/accounts-merge/discuss/202354/java-Simple-HashMap-solution
	 */
	public List<List<String>> accountsMerge_account(List<List<String>> accounts) {
		HashMap<String, Account> mp = new HashMap<>();
		for (List<String> list : accounts) {
			String name = list.get(0);
			Account acc = new Account(name);
			
			for (int i = 1; i < list.size(); i++) {
				String email = list.get(i);
				if (mp.containsKey(email)) {
					// merge
					Account mergeAcc = mp.get(email);
					if (acc != mergeAcc) {
						// Add emails in mergeAcc to acc 
						for (String mEmail : mergeAcc.emails) {
							acc.addEmail(mEmail);
							mp.put(mEmail, acc);
						}
					}
				} 
				else {
					// add to same acc
					acc.addEmail(email);
					mp.put(email, acc);
				}
			}
		}
		
		// merge done
		HashSet<Account> unique = new HashSet<>();
		for (Account acc : mp.values()) {
			unique.add(acc);
		}
		
		List<List<String>> result = new ArrayList<>();
		for (Account acc : unique) {
			List<String> res = new ArrayList<>();
			res.add(acc.name);
			res.addAll(acc.getEmails());
			result.add(res);
		}
		return result;
	}

	class Account {
		String name;
		HashSet<String> emails;

		public Account(String name) {
			this.name = name;
			emails = new HashSet<>();
		}

		public void addEmail(String email) {
			emails.add(email);
		}

		public List<String> getEmails() {
			List<String> res = new ArrayList<>();
			for (String email : emails)
				res.add(email);
			Collections.sort(res);
			return res;
		}
	}
	
	// by myself
	public List<List<String>> accountsMerge_self(List<List<String>> accounts) {
        List<List<String>> ans = new ArrayList<>();
        if (accounts == null || accounts.size() == 0)
            return ans;
        
        List<List<String>> record = new ArrayList<>();
        for (int i = 1; i < accounts.size(); i++) {
            List<String> account = accounts.get(i);
            
            Set<String> set = new HashSet<>();
            for (int j = 1; j < account.size(); j++) {
                set.add(account.get(j));
            }
            List<String> list = new ArrayList<>(set);
            
            list.add(0, account.get(0));
            record.add(list);
        }
        
        List<String> first = accounts.get(0);
        String firstName = first.get(0);
        Set<String> set = new HashSet<>();
        for (int i = 1; i < first.size(); i++) {
            set.add(first.get(i));
        }
        first = new ArrayList<>(set);
        first.add(0, firstName);
        
        ans.add(first);
        while (true) {
            List<String> curList = ans.get(ans.size() - 1);
            for (int i = 1; i < curList.size(); i++) {
                String cur = curList.get(i);
                
                Iterator<List<String>> it = record.iterator();
                while (it.hasNext()) {
                    List<String> process = it.next();
                    if (process.contains(cur)) {
                        for (int j = 1; j < process.size(); j++) {
                            if (!curList.contains(process.get(j))) {
                                curList.add(process.get(j));
                            }
                        }
                        it.remove();
                    }
                }
            }
            
            if (!record.isEmpty()) {
                ans.add(record.get(0));
                record.remove(0);
            }
            else {
                for (List<String> s : ans) {
                    String name = s.get(0);
                    s.remove(0);
                    Collections.sort(s);
                    s.add(0, name);
                }
                return ans;
            }
        }
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/accounts-merge/discuss/109161/Python-Simple-DFS-with-explanation!!!
     * https://leetcode.com/problems/accounts-merge/discuss/109186/python-union-find-with-path-compression-and-building-disjoint-set-on-the-fly
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/accounts-merge/discuss/109162/Summary-for-DFS-Templates
     * https://leetcode.com/problems/accounts-merge/discuss/172568/Beats-99.18-solution-C%2B%2B
     */

}
