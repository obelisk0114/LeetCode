package OJ0531_0540;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Encode_and_Decode_TinyURL {
	/*
	 * leetcode.com/problems/encode-and-decode-tinyurl/discuss/100268/Two-solutions-and-thoughts/148088
	 * 
	 * Rf :
	 * leetcode.com/problems/encode-and-decode-tinyurl/discuss/100268/Two-solutions-and-thoughts/104466
	 * leetcode.com/problems/encode-and-decode-tinyurl/discuss/100268/Two-solutions-and-thoughts/104457
	 * leetcode.com/problems/encode-and-decode-tinyurl/discuss/100268/Two-solutions-and-thoughts/104426
	 */
	public class Codec {
		private static final String BASE_HOST = "http://tinyurl.com/";
        private static final String SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        private Map<String, String> keyToUrl = new HashMap<>();
        private Map<String, String> urlToKey = new HashMap<>();

        // Encodes a URL to a shortened URL.
        public String encode(String longUrl) {
            if (urlToKey.containsKey(longUrl)) {
                return BASE_HOST + urlToKey.get(longUrl);
            }

            String key = null;
            do {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    int r = (int)(Math.random() * SEED.length());
                    sb.append(SEED.charAt(r));
                }
                key = sb.toString();
            } while (keyToUrl.containsKey(key));

            keyToUrl.put(key, longUrl);
            urlToKey.put(longUrl, key);
            return BASE_HOST + key;
        }

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl) {
            return keyToUrl.get(shortUrl.replace(BASE_HOST, ""));
        }
	}
	
	/*
	 * https://leetcode.com/problems/encode-and-decode-tinyurl/discuss/100276/Easy-solution-in-java-5-line-code.
	 * 
	 * In industry, most of shorten url service is by database, one auto increasing 
	 * long number as primary key. whenever a long url need to be shorten, append to 
	 * the database, and return the primary key number.
	 */
	public class Codec_list {
		List<String> urls = new ArrayList<String>();

		// Encodes a URL to a shortened URL.
		public String encode(String longUrl) {
			urls.add(longUrl);
			return String.valueOf(urls.size() - 1);
		}

		// Decodes a shortened URL to its original URL.
		public String decode(String shortUrl) {
			int index = Integer.valueOf(shortUrl);
			return (index < urls.size()) ? urls.get(index) : "";
		}
	}
	
	/*
	 * https://leetcode.com/problems/encode-and-decode-tinyurl/discuss/100270/Three-different-approaches-in-java
	 * 
	 * Other code:
	 * https://leetcode.com/problems/encode-and-decode-tinyurl/discuss/100336/Simple-4-lines-in-Java-6ms
	 */
	public class Codec_counter {
		Map<Integer, String> map = new HashMap<>();
		int i = 0;

		public String encode(String longUrl) {
			map.put(i, longUrl);
			return "http://tinyurl.com/" + i++;
		}

		public String decode(String shortUrl) {
			return map.get(Integer.parseInt(shortUrl.replace("http://tinyurl.com/", "")));
		}
	}
	
	/*
	 * https://leetcode.com/problems/encode-and-decode-tinyurl/discuss/100270/Three-different-approaches-in-java
	 * 
	 * What if two different urls generate the same hashCode ?
	 */
	public class Codec_hashcode {
		Map<Integer, String> map = new HashMap<>();

		public String encode(String longUrl) {
			int code = longUrl.hashCode();
			map.put(code, longUrl);
			return "http://tinyurl.com/" + code;
		}

		public String decode(String shortUrl) {
			return map.get(Integer.parseInt(shortUrl.replace("http://tinyurl.com/", "")));
		}
	}
	
	// by myself
	public class Codec_self {
	    private int last = 0;
	    private Map<String, String> map = new HashMap<>();

	    // Encodes a URL to a shortened URL.
	    public String encode(String longUrl) {
	        String s = Integer.toString(last);
	        map.put(s, longUrl);
	        return ("http://tinyurl.com/" + s);
	    }

	    // Decodes a shortened URL to its original URL.
	    public String decode(String shortUrl) {
	        String s = shortUrl.substring(19);
	        return map.get(s);
	    }
	}
		
	// Your Codec object will be instantiated and called as such:
	// Codec codec = new Codec();
	// codec.decode(codec.encode(url));

}
