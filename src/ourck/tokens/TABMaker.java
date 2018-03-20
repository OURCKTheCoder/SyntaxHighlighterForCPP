package ourck.tokens;

import java.util.LinkedList;

public class TABMaker implements SPCharManager {
	private static final String TABCHAR = "&nbsp;&nbsp;&nbsp;";
	private LinkedList<TabChar> TabList = new LinkedList<TabChar>();
	public StringBuilder analyze(StringBuilder txt) {
		//1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals('\t')) {
				TabList.add(new TabChar(i));// Backward
			}
		}
		
		//2.Add "TAB".
		int baseIndex = 0;
		for(Token t : TabList) {
			int pos = t.getIndex();
			txt.deleteCharAt(pos + baseIndex);
			txt.insert(pos + baseIndex, TABCHAR);
			baseIndex += TABCHAR.length() - 1;
			// TODO Delete \t in the txt.
		}
		
		return txt;
	}
}

class TabChar extends Token {
	public TabChar(int pos) {
		super("(TABCHAR)", pos, pos);
	}
}
