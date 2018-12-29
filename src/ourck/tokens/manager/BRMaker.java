package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.*;

/**
 * HTML换行符管理类。<br>
 * 在HTML中，字符'\n'及需要做特殊转义处理。<br>
 * @deprecated 虽说'\n'需要做特殊转义处理为br标签<br>
 * 				但实际上，可以通过在HTML文本中嵌入pre标签来跳过人工转义。
 * @author ourck
 */
public class BRMaker extends TokenManager {
	
	private LinkedList<BrChar> brList = new LinkedList<BrChar>();
	private static final String BRCHAR = "<br>";
	
	/**
	 * 对所提供的代码全文进行扫描，<br>
	 * 将普通文本文件中的换行符('\n')进行替换
	 * @param txt 源代码全文的{@link StringBuilder}
	 * @return 经过处理的{@link StringBuilder}
	 */
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		//1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals('\n')) {
				brList.add(new BrChar(i));// Backward
			}
		}
		
		//2.Add <br>.
		int baseIndex = 0;
		for(Token t : brList) {
			int pos = t.getIndex();
			txt.insert(pos + baseIndex, BRCHAR);
			baseIndex += BRCHAR.length();
		}
		
		doNext(txt);
		return txt;
	}

	@Override
	public void reset() { brList.clear(); }
}

