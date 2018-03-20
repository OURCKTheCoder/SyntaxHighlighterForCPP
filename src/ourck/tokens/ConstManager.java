package ourck.tokens;

import java.util.*;

public class ConstManager extends TokenManager{
	private static final String PREFIX = "<font color=\"#bcd1dc\">" ;
	private static final String SUFFIX = "</font>";
	private static ArrayList<Token> cslst = new ArrayList<Token>();
	
	private static class Utils {
		public static boolean isNum(Character c) {
			return (c - '0' >= 0) && ('9' - c >= 0);
		}
		
		public static boolean isHex(Character c) {
			return (c - '0' >= 0) && ('9' - c >= 0) ||
					(c - 'A' >= 0) && ('F' - c >= 0) ||
					(c - 'a' >= 0) && ('f' - c >= 0);
		}
		
		public static boolean isSfx(Character c) {
			return c == 'U' || c == 'u' ||
					c == 'F' || c == 'f' ||
					c == 'L' || c == 'l';
		}
		
		public static boolean isStf(Character c) {
			return c == 'E' || c == 'e';
		}
		
		public static boolean isNbsp(Character c) {
			return c == ' ' || c == '\t' || c == ';' || c == '>' ||
					c == '\n' || c == ',' || c == '}' || c == ')' ||
					 c == ']' || c == '<' || c == '{' || c == '(' ||
					 c == '[';
		}
		
		public static boolean isOp(Character c) {
			return c == '+' || c == '-' || c == '*' || c == '/' ||
					 c == '^' ||  c == '%' ;
		}
	}
	
	public void add(Token t) { cslst.add(t); }
	
	public boolean cmp(String s) { return false; } //Fake method.
	
	public StringBuilder analyze(StringBuilder token) {
		char[] chars = token.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it)) continue;
			Character c = new Character(chars[it]);
			switch(state) {
			//----------Busy bee!----------
			case 0:
				currentWord.delete(0, currentWord.length());
				head = it;
				if(Utils.isNum(c) && c != '0'){
					currentWord.append(c);
					state = 1;
				}
				else if(Utils.isNum(c) && c == '0'){
					currentWord.append(c);
					state = 13;
				}
				else {
					while(!Utils.isNbsp(c) && it < chars.length - 1 && !Utils.isOp(c)) {
						it++; c = chars[it]; // Jump over this token.
					}
					state = 0;
				}
				break;
			case 1:
				if(Utils.isNum(c)) {
					currentWord.append(c);
					state = 1;
				}
				else {
					state = 2;
				}
				break;
			case 2:
				it--; c = chars[it]; // Backwards.
				if(c == '.') {
					currentWord.append(c);
					state = 3;
				}
				else if(Utils.isSfx(c)) state = 5;
				else if(Utils.isStf(c)) {
					currentWord.append(c); // Append e/E.
					state = 6;
				}
				else if(Utils.isNbsp(c) || Utils.isOp(c)){
					// TODO Got a Integer.
					rear = it; // TODO --?
					add(new Const(head, rear));
					state = 0;
				}
				else state = 0;	
				break;
			case 3:
				if(Utils.isNum(c)) {
					currentWord.append(c);
					state = 3;
				}
				else state = 4;
				break;
			case 4:
				it--; c = chars[it];
				if(Utils.isSfx(c)) state = 5;
				else if(Utils.isStf(c)) {
					currentWord.append(c); // Append e/E.
					state = 6;
				}
				else if(Utils.isNbsp(c) || Utils.isOp(c)) {
					// TODO Got a float.
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}
				else state = 0;
				break;
			case 5:
				it--; c = chars[it];
				if(c == 'U' || c == 'u') state = 11;
				else state = 12;
				break;
			case 6:
				if(Utils.isNum(c)) {
					currentWord.append(c);
					state = 16;
				}
				else state = 7;
				break;
			case 7:
				it--; c = chars[it];
				if(c == '+' || c == '-') {
					currentWord.append(c);
					state = 8;
				}
				else state = 9;
				break;
			case 8:
				if(Utils.isNum(c)) {
					currentWord.append(c);
				}
				else state = 10;
				break;
			case 9:
				it--; c = chars[it];
				state = 0;
				break;
			case 10:
				// TODO Got a scientific number( has +/- ).
				rear = it;
				add(new Const(head, rear));
				state = 0;
				break;
			case 11:
				currentWord.append(c);
				if(Utils.isSfx(c)) state = 12;
				else{
					// TODO Got a number with 'u' at the end. [!!!]THIS IS A WRONG WAY!!!
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}	
				break;
			case 12:
				currentWord.append(c);
				// TODO Got a multi-suffix Integer.
				rear = it;
				add(new Const(head, rear));
				state = 0;
				break;
			case 13:
				if(Utils.isNum(c)) {
					currentWord.append(c);
					state = 1;
				}
				else if(c == 'X' || c == 'x') {
					currentWord.append(c);
					state = 14;
				}
				else if(Utils.isNbsp(c) || Utils.isOp(c)) {
					// TODO Got zero.
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}
				else state = 0;
				break;
			case 14:
				if(Utils.isHex(c)) {
					currentWord.append(c);
					state = 14;
				}
				else if(Utils.isNbsp(c) || Utils.isOp(c)) {
					// TODO 
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}
				else state = 15;
				break;
			case 15:
				it--; c = chars[it];
				if(c == 'l' || c == 'L') {
					state = 17;
				}
				else state = 0;
				break;
			case 16:
				if(Utils.isNum(c)) currentWord.append(c);
				else {
					// TODO Got a scientific number( no +/- ).
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}
				break;
			case 17:
				//TODO
				rear = it;
				add(new Const(head, rear));
				state = 0;
				break;
			}
		}
		
		//2.HighLight all.
		int baseIndex = 0;
		for(Token t : cslst) {
			head = t.getIndex(); rear = t.getRear();
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
}

class Const extends Token {
	Const(int head, int rear){
		super("(CONST)", head, rear);
	}
}
