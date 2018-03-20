package ourck.tokens;

import java.util.*;

public class PPManager extends TokenManager {
	private static final String PREFIX = "<font color=\"#EC691E\"><b>" ;
	private static final String SUFFIX = "</b></font>";
	private static final HashSet<String> PPLIST = new HashSet<String>();
	static {
		Collections.addAll(PPLIST,
				"include",	"define",	"undef",	"if",
				"ifdef",	"ifndef",	"elif",	"endif",
				"else",	"error");
	}
	private static LinkedList<Token> pps = new LinkedList<Token>();
	
	public boolean cmp(String t) {
		if(PPLIST.contains(t)) { return true; }
		else { return false; }
	}
	
	public void add(Token t) {
		pps.add(t);
	}
	
	public StringBuilder analyze(StringBuilder token) {
		//1.Divide by chars
		char[] chars = token.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it)) continue;
			Character c = new Character(chars[it]);
			switch(state) {
			case 0:
				currentWord.delete(0, currentWord.length());
				if(c.equals('#')) {
					head = it;
//					currentWord.append(c);
					state = 1;
				}
				else state = 0;
				break;
			case 1:
				if(c.equals('\n')) {
					state = 0;
				}
				else {
					if(!c.equals(' '))
						currentWord.append(c);
					state = 1;
				}
				
				if(cmp(currentWord.toString())) {
					state = 2;
				}
				break;
			case 2:
				if(c.equals(' ') || c.equals('<') || c.equals('\"') ) // TODO OK?
					state = 3;
				break;
			case 3:
				it--; c = chars[it];
				rear = it;
				PP pp = new PP(currentWord.toString(), head, rear);
				add(pp);
				state = 0;
			}
		}
		
		//2.HighLight all.
		int baseIndex = 0;
		for(Token t : pps) {
			head = t.getIndex(); rear = t.getRear();
			// [!] Rear goes first.
			token.insert(rear + baseIndex, SUFFIX);
			token.insert(head + baseIndex, PREFIX);
			
			for(Token cmtBlock : CmtManager.SharedCmtBlockList) {
				if(CmtManager.ptrPosByBlock(cmtBlock, rear + baseIndex) == -1) {
					cmtBlock.setIndex(cmtBlock.getIndex() + SUFFIX.length() + PREFIX.length());
					cmtBlock.setRear(cmtBlock.getRear() + SUFFIX.length() + PREFIX.length());
				}
			}
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
		
		return token;
	}
	
//	public static void main(String[] args) {
//		// ------------- Used for debugging. ---------------
//		PPManager km = new PPManager();
//		System.out.println(km.cmp("test"));
//		System.out.println(km.cmp("#if"));
//	}
	
}

class PP extends Token {
	PP(String str, int head, int rear) {
		super(str, head, rear);
	}
	public void highlightIt() {
		//TODO Pick a color!
	}
}