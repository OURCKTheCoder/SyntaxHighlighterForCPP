package ourck.tokens;

import java.util.*;

public class StrManager extends TokenManager {
	private static final String PREFIX = "<font color=\"#f0c837\">" ;
	private static final String SUFFIX = "</font>";
	private static ArrayList<Token> strlst = new ArrayList<Token>();

	public void add(Token t) { strlst.add(t); }
	
	private static class Utils {
		public static boolean isQts(Character c) {
			return c == '\"';
		}
		
		public static boolean isQt(Character c) {
			return c == '\'';
		}
		
		public static boolean isNl(Character c) {
			return c == '\n';
		}		
	}
	
	public boolean cmp(String s) { return false; } //Fake method.
	
	public StringBuilder analyze(StringBuilder token) {
		char[] chars = token.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it))
				continue;
			Character c = new Character(chars[it]);
			switch (state) {
			case 0:
				currentWord.delete(0, currentWord.length());
				head = it;
				if(Utils.isQts(c)) {
					currentWord.append(c);
					state = 1;
				}
				else if(Utils.isQt(c)) {
					currentWord.append(c);
					state = 3;
				}
//				else if(c == '<') {
//					currentWord.append(c);
//					state = 7;
//				}
				else state = 0;
				break;
			case 1:
				if(Utils.isQts(c) || Utils.isNl(c)) {
					currentWord.append(c); 
					state = 2;
				}
				else currentWord.append(c); 
				break;
			case 2:
				//TODO
				rear = it;
				add(new StrToken(currentWord.toString(), head, rear));
				state = 0;
				break;
			case 3:
				currentWord.append(c); 
				state = 4;
				break;
			case 4:
				//TODO
				it--; c = chars[it];
				if(c == '\\') state = 5;
				else state = 6;
				break;
			case 5:
				currentWord.append(c); 
				state = 6;
				break;
			case 6:
				if(Utils.isQt(c)) {
					currentWord.append(c); 
					rear = it + 1;
					add(new StrToken(currentWord.toString(), head, rear));
				}
				state = 0;
				break;
//			case 7:
//				if(c != '>') {
//					currentWord.append(c);
//					state = 7;
//				}
//				else {
//					currentWord.append(c); 
//					rear = it + 1;
//					add(new StrToken(currentWord.toString(), head, rear));
//				}
//				
//				if(c =='\n') state = 0;
//				break;
			}
		}
		
		highlightAll(token);
		return token;
	}
	
	private void highlightAll(StringBuilder token) {
		//2.HighLight all.
		int baseIndex = 0;
		for(Token t : strlst) {
			int head = t.getIndex(), rear = t.getRear();
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
	}
}

class StrToken extends Token {
	public StrToken(String str, int head, int rear) {
		super(str, head, rear);
	}
}
