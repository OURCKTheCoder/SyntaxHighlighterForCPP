package ourck.tokens;

import java.util.*;

public class KwdManager extends TokenManager {
	private static final String PREFIX = "<font color=\"#9CF828\"><b>" ;
	private static final String SUFFIX = "</b></font>";
	private static final String PREFIX2 = "<font color=\"#66ccff\"><b>" ;
	private static final String SUFFIX2 = "</b></font>";
	private static final HashSet<String> KWDLIST = new HashSet<String>();
	static {
		Collections.addAll(KWDLIST,
				"asm",		"else",		"new",		"this",
				"auto", 	"enum",		"operator",	"throw",
				"bool",		"explicit",	"private",	
				"break",	"export",	"protected","try",
				"case",		"extern",	"public",	"typedef",
				"catch",	"register",	"typeid",
				"reinterpret_cast",
				"class",	"for",		"return",	"union",
				"const",	"friend", 	"unsigned",
				"const_cast","goto", 	"signed",	"using",
				"continue", "if", 		"sizeof", 	"virtual",
				"default", 	"inline", 	"static",	
				"delete", 	"static_cast",			"volatile",
				"do", 		"struct", 	"wchar_t",
				"mutable", 	"switch", 	"while",
				"dynamic_cast", "typename",	"namespace","template",
				"char",		"float",	"double",	"int",
				"long",		"short",	"true",		"void",
				"false");
	}
	private static final HashSet<String> TYPELIST = new HashSet<String>();
	static {
		Collections.addAll(TYPELIST,
				"char",		"float",	"double",	"int",
				"long",		"short",	"true",		"void",
				"false"
				);
	}
	private static ArrayList<Token> kwds = new ArrayList<Token>();
	
	private boolean isChar(Character cc) {
		char c = (char)cc;
		return ((c - 'A' >= 0) && ('Z' - c >= 0)) ||
				((c - 'a' >= 0) && ('z' - c >= 0));
	}
	
	public boolean cmp(String t) {
		if(KWDLIST.contains(t)) { return true; }
		else { return false; }
	}
	
	private boolean cmpToTypeList(String t) {
		boolean flag = false;
		for(String str : TYPELIST) {
			if(t.equals(str)) {
				flag = true; break;
			}
		}
		return flag;
	}
	
	public void add(Token t) {
		kwds.add(t);
	}
	
	public StringBuilder analyze(StringBuilder token) {
		//1.Divide by chars.
		char[] chars = token.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it))
				continue;
			
			Character c = new Character(chars[it]);
			switch(state) {
			case 0:
				currentWord.delete(0, currentWord.length());
				if(isChar(c)) {
					head = it;
					currentWord.append(c);
					state = 1;
				}
				else state = 0;
				break;
			case 1:
				if(!isChar(c)) {
					it--; c = chars[it];
					state = 2;
				}
				else {
					currentWord.append(c);
					state = 1;
				}
				break;
			case 2:
				it--; c = chars[it];
				if(cmp(currentWord.toString())) 
					state = 4; // TODO If certain kwd is the last word, the progress may abort too early
				else 
					state = 3;
				break;
			case 3:
				state = 0;
				break;
			case 4:
				rear = it; // [!] 额外的的判断状态指针又前移一次
				KeyWord kwd = new KeyWord(currentWord.toString(), head, rear);
				add(kwd);
				state = 0;
				break;
			}
		}
		
		//2.HighLight all.
		int baseIndex = 0;
		for(Token t : kwds) {
			head = t.getIndex(); rear = t.getRear();
			if(!cmpToTypeList(t.getStr())) {//TODO Different color
				token.insert(rear + baseIndex, SUFFIX);
				token.insert(head + baseIndex, PREFIX);
			}
			else {
				token.insert(rear + baseIndex, SUFFIX2);
				token.insert(head + baseIndex, PREFIX2);
			}
			
			for(Token cmtBlock : CmtManager.SharedCmtBlockList) {
				if(CmtManager.ptrPosByBlock(cmtBlock, rear + baseIndex) == -1) {
					cmtBlock.setIndex(cmtBlock.getIndex() 
							+ SUFFIX.length() + PREFIX.length()); //SUFFIX.length() equals SUFFIX2.length()
					cmtBlock.setRear(cmtBlock.getRear() 
							+ SUFFIX.length() + PREFIX.length());
				}
			}
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
		return token;
	}
	
//	public static void main(String[] args) {
//		// ------------- Used for debugging. ---------------
//		KwdManager km = new KwdManager();
//		System.out.println(km.cmp("if"));
//		System.out.println(km.cmp("ifsdddddd"));
//	}
}

class KeyWord extends Token {
	public KeyWord(String str, int head, int rear) {
		super(str, head, rear);
	}
}