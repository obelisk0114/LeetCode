class Solution {
  /*
    Search a substring of given length
    that occurs at least 2 times.
    Return start position if the substring exits and -1 otherwise.
    */
  public int search(int L, int n, String S) {
    HashSet<Integer> seen = new HashSet();
    String tmp;
    int h;
    for(int start = 0; start < n - L + 1; ++start) {
      tmp = S.substring(start, start + L);
      h = tmp.hashCode();
      if (seen.contains(h)) return start;
      seen.add(h);
    }
    return -1;
  }

  public int longestRepeatingSubstring(String S) {
    int n = S.length();
    // binary search, L = repeating string length
    int left = 1, right = n;
    int L;
    while (left <= right) {
      L = left + (right - left) / 2;
      if (search(L, n, S) != -1) left = L + 1;
      else right = L - 1;
    }

    return left - 1;
  }
}