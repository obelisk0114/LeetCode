package OJ0271_0280;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Encode_and_Decode_Strings {
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-%22escaping%22
	 * 
	 * 使用單一的 # (頭尾加上空白，形成 “ # ”) 作為分隔，
	 * 為了避免和內文原有的 # 混淆，將內文所有 # 重複一次
	 * 因此若出現 “ # ” (單一的 #)，是分隔；若是連續的 #，除以 2 能得到原先的 #
	 * 
	 * Double any hashes inside the strings, then use standalone hashes 
	 * (surrounded by spaces) to mark string endings. For example:
	 * 
	 * {"abc", "def"}    =>  "abc # def # "
	 * {'abc', '#def'}   =>  "abc # ##def # "
	 * {'abc##', 'def'}  =>  "abc#### # def # "
	 * 
	 * For decoding, just do the reverse: First split at standalone hashes, then 
	 * undo the doubling in each string.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * If n is non-positive then the pattern will be applied as many times as 
	 * possible and the array can have any length. If n is zero then the pattern will 
	 * be applied as many times as possible, the array can have any length, and 
	 * trailing empty strings will be discarded.
	 * 
	 * so I think you can pass in any negative number in split() method, then it will 
	 * not discard empty strings.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * the default limit is 0, which would discard the trailing empty strings. 
	 * It has to be a negative value.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * first parameter of String.split() function is a regular expression, 
	 * that's why '*' is treated as a special character.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-"escaping"/72570
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-"escaping"/72568
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-"escaping"/72571
	 * http://www.geeksforgeeks.org/split-string-java-examples/
	 */
	class Codec_double {

		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			StringBuffer out = new StringBuffer();
			for (String s : strs)
				out.append(s.replace("#", "##")).append(" # ");
			return out.toString();
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			List<String> strs = new ArrayList<>();
			
			String[] array = s.split(" # ", -1);
			for (int i = 0; i < array.length - 1; ++i)
				strs.add(array[i].replace("##", "#"));
			return strs;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/151048/Java-with-Explanations
	 * 
	 * We ought to define a delimiter to separate words within the list, however, 
	 * any character delimiter can be part of the original word. Thus, we use 
	 * `lengthOfStr + :` to delimit a word.
	 * 
	 * Take ["a:b", "c"] for example, the encoded word is s = "3:a:b1:c".
	 * 
	 * To decode s, we keep track of the position of `endIndex` - the end index of 
	 * the last word we have decoded already. In this way, the section in front of 
	 * the first occurrence of colon after `endIndex` must be the length of the 
	 * next word to decode.
	 * 
	 * The ":" here is just used as a separator, between the length and the actually 
	 * string. It is act as a boundary to show where the length string ends, when 
	 * the length has multiple digits.
	 * 
	 * --------------------------------------------------------------------
	 * 
	 * ["aa2/bb"] will get encoded as "6/aa2/bb". Then, when decoding, it will get 
	 * ["aa2/bb"]. When decoding, since it sees the 6 as length, then it will take 
	 * the whole string "aa2/bb", and will finish parsing the string. So the 
	 * algorithm does not see the "2/bb".
	 * 
	 * Rf :
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70412/AC-Java-Solution/72586
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70412/AC-Java-Solution/687569
	 * 
	 * Other code:
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70412/AC-Java-Solution/72584
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/151048/Java-with-Explanations/846732
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70412/AC-Java-Solution
	 */
	public class Codec_delimiter {

		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			StringBuilder encoded = new StringBuilder();
			for (String str : strs)
				encoded.append(str.length()).append(":").append(str);

			return encoded.toString();
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			int endIndex = 0;
			List<String> result = new ArrayList<>();

			while (endIndex != s.length()) {
				int colonIndex = s.indexOf(":", endIndex);
				int wordLength = Integer.parseInt(s.substring(endIndex, colonIndex));
				
				endIndex = colonIndex + 1 + wordLength;
				result.add(s.substring(colonIndex + 1, endIndex));
			}
			return result;
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/solution/
	 * Approach 2: Chunked Transfer Encoding
	 * 
	 * Data stream is divided into chunks. Each chunk is preceded by its size in bytes.
	 * 
	 * Encode:
	 * 
	 * + For each chunk compute its length, and convert that length into 4-bytes 
	 *   string.
	 * + Append to encoded string :
	 *   + 4-bytes string with information about chunk size in bytes.
	 *   + Chunk itself.
	 * 
	 * Decode:
	 * 
	 * + Read 4 bytes s[i: i + 4]. It's chunk size in bytes. Convert this 4-bytes 
	 *   string to integer length.
	 * + Move the pointer by 4 bytes i += 4.
	 * + Append to the decoded array string s[i: i + length].
	 * + Move the pointer by length bytes i += length.
	 * 
	 * --------------------------------------------------------------
	 * 
	 * 4 bytes - integer size - 4 bytes = [8bits, 8bits, 8bits, 8bits]
	 * 0xff is 8 1's which represents 11111111 = a BYTE
	 * AND(&) any number with 0xff = it gives you the right most 8 bits of the number
	 * 
	 * Rf :
	 * https://leetcode.com/problems/encode-and-decode-strings/solution/462088
	 * https://en.wikipedia.org/wiki/Chunked_transfer_encoding
	 */
	class Codec_http {
		// Encodes string length to bytes string
		public String intToString(String s) {
			int x = s.length();
			char[] bytes = new char[4];
			for (int i = 3; i > -1; --i) {
				bytes[3 - i] = (char) (x >> (i * 8) & 0xff);
			}
			return new String(bytes);
		}

		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			StringBuilder sb = new StringBuilder();
			for (String s : strs) {
				sb.append(intToString(s));
				sb.append(s);
			}
			return sb.toString();
		}

		// Decodes bytes string to integer
		public int stringToInt(String bytesStr) {
			int result = 0;
			for (char b : bytesStr.toCharArray())
				result = (result << 8) + (int) b;
			return result;
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			int i = 0, n = s.length();
			List<String> output = new ArrayList<>();
			while (i < n) {
				int length = stringToInt(s.substring(i, i + 4));
				i += 4;
				output.add(s.substring(i, i + length));
				i += length;
			}
			return output;
		}
	}

	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/solution/
	 * Approach 1: Non-ASCII Delimiter
	 * 
	 * Naive solution here is to join strings using delimiters.
	 * 
	 * Seems like one has to use non-ASCII unichar character, for example unichr(257) 
	 * in Python and Character.toString((char)257) in Java.
	 * 
	 * It's convenient to use two different non-ASCII characters, to distinguish 
	 * between situations of "empty array" and of "array of empty strings".
	 * 
	 * Use split in Java with a second argument -1 to make it work as split in Python.
	 */
	class Codec_non_ascii {
		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			if (strs.size() == 0)
				return Character.toString((char) 258);

			String d = Character.toString((char) 257);
			StringBuilder sb = new StringBuilder();
			for (String s : strs) {
				sb.append(s);
				sb.append(d);
			}
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			String d = Character.toString((char) 258);
			if (s.equals(d))
				return new ArrayList<>();

			d = Character.toString((char) 257);
			return Arrays.asList(s.split(d, -1));
		}
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-%22escaping%22
	 * 
	 * Double any hashes inside the strings, then use standalone hashes 
	 * (surrounded by spaces) to mark string endings. For example:
	 * 
	 * {"abc", "def"}    =>  "abc # def # "
	 * {'abc', '#def'}   =>  "abc # ##def # "
	 * {'abc##', 'def'}  =>  "abc#### # def # "
	 * 
	 * For decoding, just do the reverse: First split at standalone hashes, then 
	 * undo the doubling in each string.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * If n is non-positive then the pattern will be applied as many times as 
	 * possible and the array can have any length. If n is zero then the pattern will 
	 * be applied as many times as possible, the array can have any length, and 
	 * trailing empty strings will be discarded.
	 * 
	 * so I think you can pass in any negative number in split() method, then it will 
	 * not discard empty strings.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70402/Java-with-"escaping"/72570
	 */
	class Codec_double_stream {

		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			return strs.stream()
					   .map(s -> s.replace("#", "##") + " # ")
					   .collect(Collectors.joining());
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			List<String> strs = Stream.of(s.split(" # ", -1))
									  .map(t -> t.replace("##", "#"))
									  .collect(Collectors.toList());
			
			strs.remove(strs.size() - 1);
			return strs;
		}
	}

	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70451/Java-solution-pretty-straight-forward
	 * 
	 * Streams make your code more functional and arguably better describe what your 
	 * code is doing. They are equally fast for operations on collections of 
	 * non-primitive objects as regular loops. The big motivation of having streams, 
	 * to be precise parallelStreams, is that they simplify the execution of long 
	 * iterations on multiple processes, which makes them noticeably faster than 
	 * running the entire iteration on a single process. Other than that, it's a 
	 * stylistic choice if you are into functional languages
	 * 
	 * Rf :
	 * https://leetcode.com/problems/encode-and-decode-strings/discuss/70451/Java-solution-pretty-straight-forward/72641
	 * https://www.overops.com/blog/benchmark-how-java-8-lambdas-and-streams-can-make-your-code-5-times-slower/
	 * https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html
	 */
	class Codec_stream {

		// Encodes a list of strings to a single string.
		public String encode(List<String> strs) {
			return strs.stream()
					   .map(s -> s.replace("/", "//").replace("*", "/*") + "*")
					   .collect(Collectors.joining());
		}

		// Decodes a single string to a list of strings.
		public List<String> decode(String s) {
			List<String> res = new ArrayList<>();
			StringBuilder str = new StringBuilder();

			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == '/') {
					str.append(s.charAt(++i));
				} 
				else if (s.charAt(i) == '*') {
					res.add(str.toString());
					str.setLength(0);
				} 
				else {
					str.append(s.charAt(i));
				}
			}

			return res;
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/encode-and-decode-strings/discuss/70448/1%2B7-lines-Python-(length-prefixes)
     * https://leetcode.com/problems/encode-and-decode-strings/discuss/70426/Python-solution
     * https://leetcode.com/problems/encode-and-decode-strings/discuss/70465/1-liners-Ruby%2BPython
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/encode-and-decode-strings/discuss/70443/Accepted-simple-C%2B%2B-solution
     * https://leetcode.com/problems/encode-and-decode-strings/discuss/70460/A-solution-without-delimiter
     */
	
	// Your Codec object will be instantiated and called as such:
	// Codec codec = new Codec();
	// codec.decode(codec.encode(strs));

}
