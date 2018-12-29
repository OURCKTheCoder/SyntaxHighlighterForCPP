package ourck.tokens.manager;

import java.util.LinkedList;

import ourck.tokens.token.*;

/**
 * 制表符管理器。<br>
 * 在HTML中，字符'\t'需要做特殊转义处理。<br>
 * @deprecated 虽说'\t'可以做特殊转义处理为若干个空格<br>
 * 				但实际上，可以通过在HTML文本中嵌入pre标签来跳过人工转义。
 * @author ourck
 */
public class TABMaker extends TokenManager {
	
	private static final String TABCHAR = "&nbsp;&nbsp;&nbsp;"; // 3x '_' = 1x TAB
	private LinkedList<TabChar> tabList = new LinkedList<TabChar>();
	
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		//1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals('\t')) {
				tabList.add(new TabChar(i));// Backward
			}
		}
		
		//2.Add "TAB".
		int baseIndex = 0;
		for(Token t : tabList) {
			int pos = t.getIndex();
			txt.deleteCharAt(pos + baseIndex);
			txt.insert(pos + baseIndex, TABCHAR);
			baseIndex += TABCHAR.length() - 1;
			// TODO Delete \t in the txt maybe?.
		}
		
		doNext(txt);

		return txt;
	}

	@Override
	public void reset() { tabList.clear(); }
}
