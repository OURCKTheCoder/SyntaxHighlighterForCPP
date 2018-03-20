package ourck.tokens;

import java.util.*;

public class BRMaker implements SPCharManager {
	private static final String BRCHAR = "<br>";
	private LinkedList<BrChar> brList = new LinkedList<BrChar>();
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
		
		return txt;
	}
}

class BrChar extends Token {
	public BrChar(int pos) {
		super("(BR)", pos, pos);
	}
}
