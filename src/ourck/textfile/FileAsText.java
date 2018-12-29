package ourck.textfile;

import java.io.*;
import java.util.*;

/**
 * 用于读取文本文件的工具类。<br>
 * 继承自List容器，分行将字符串保存到容器中。<br>
 * (Inspired by TIJ4 TextFile)
 * @author ourck
 */
@SuppressWarnings("serial") //由于本项目不需要序列化对象，忽略警告。
public class FileAsText extends ArrayList<String>{
	
	/**
	 * 按照文件名来打开某个文件的文件流，并返回该文件的 <b>全部</b> 字符串值。
	 * @param fileName 文件名
	 * @return 文件字符串
	 */
	private static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in =
					new BufferedReader(
							new FileReader(
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

	/**
	 * 按照文件路径构造对象，读取文件，并分行装入容器中。
	 * @param path 文件路径
	 */
	public FileAsText(String path) {
		//Divide by lines:
		List<String> txtByLines = new ArrayList<>();
		Collections.addAll(txtByLines, read(path).split("\n"));
		for(String str : txtByLines) {
			// [!] String is decorated with "final". Use StringBuilder instead.
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append("\n");
			this.add(sb.toString());
		}
	}
	
	/**
	 * 获得原文本文件按行分割的字符串数组。
	 * @return 按行分割的字符串数组
	 */
	public String[] toStrAryByLines() {
		return this.toArray(new String[this.size()]);
	}
	
	/**
	 * 获得原文本文件按词分割的字符串数组。
	 * @return 按词分割的字符串数组
	 */
	public String[] toStrAryByWords() {
		ArrayList<String> words = new ArrayList<>();
		for(String line : this) {
			Collections.addAll(words, line.split(" "));
		}
		return words.toArray(new String[this.size()]);
	}
	
	/**
	 * 获得原文本文件按字符分割的字符串数组。
	 * @return 按字符分割的字符串数组
	 */
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
}
