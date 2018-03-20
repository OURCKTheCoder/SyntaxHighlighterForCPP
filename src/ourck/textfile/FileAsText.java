package ourck.textfile;
/*-------------------------------------
 *	OURCK - 用于读取文本文件的类
 *	2018年3月16日 下午10:05:03
 *-------------------------------------

/* 
 * (Inspired by TIJ4 TextFile)
 */

import java.io.*;
import java.util.*;

// TODO All possible exceptions should be checked.
// TODO Give it a thorough comprehension!
@SuppressWarnings("serial") //由于本项目不需要序列化实例，忽略警告。
public class FileAsText extends ArrayList<String>{
	private static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in= new BufferedReader(new FileReader(
			new File(fileName).getAbsoluteFile()));
			try {
				String s;
				while((s = in.readLine()) != null) {
					sb.append(s);
					sb.append("\n");
				}
			} finally {
				in.close();
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	public FileAsText(String path) {
		//Divide by lines:
		ArrayList<String> txtByLines = new ArrayList<>();
		Collections.addAll(txtByLines, read(path).split("\n"));
		for(String str : txtByLines) {
			// [!] String is decorated with "final". Use StringBuilder instead.
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append("\n");
			this.add(sb.toString());
		}
	}

	// TODO 这个要不要都可以...都继承了toArray方法，还要这个干嘛？
	public String[] toStrAryByLines() {
		return this.toArray(new String[this.size()]);
	}
	
	public String[] toStrAryByWords() {
		ArrayList<String> words = new ArrayList<>();
		for(String line : this) {
			Collections.addAll(words, line.split(" "));
		}
		return words.toArray(new String[this.size()]);
	}
	
	public Character[] toCharAry() {
		String[] strs = toStrAryByLines();
		ArrayList<Character> chars = new ArrayList<Character>();
		for(String str : strs) {
			char[] tmp = str.toCharArray();
			for(char c : tmp) {
				chars.add(c);
			}
		}
		return chars.toArray(new Character[chars.size()]);
	}
	
	public static void main(String[] args) {
		// ------------- Used for debugging. ---------------
		System.out.println();
		System.out.println("=========Unit test.========");
		FileAsText t = new FileAsText("main.cpp");
		for(String str : t.toStrAryByLines()) {
			System.out.print(str);
		}
		System.out.println();
		for(String str : t.toStrAryByWords()) {
			System.out.print(str);
		}
		System.out.println();
		for(char str : t.toCharAry()) {
			System.out.print(str);
		}
	}
}
