package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.*;

/**
 * 常量Token管理类
 * @author ourck
 */
public class ConstManager extends TokenHighlighter{
	
	private final String PREFIX;
	private final String SUFFIX;
	{
		PREFIX = "<font color=\"" + getClrVal() + "\">";
		SUFFIX = "</font>";
	}
	private ArrayList<Token> cslst = new ArrayList<Token>();
	
	/**
	 * 内部工具类。<br>
	 * 该类包含一系列实用方法，用于更方便地判断某个字符的属性
	 * @author ourck
	 */
	private static class Utils {
		
		/**
		 * 判断某个字符是否为数字字符
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isNum(Character c) {
			return (c - '0' >= 0) && ('9' - c >= 0);
		}
		
		/**
		 * 判断某个字符是否为16进制数码
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isHex(Character c) {
			return (c - '0' >= 0) && ('9' - c >= 0) ||
					(c - 'A' >= 0) && ('F' - c >= 0) ||
					(c - 'a' >= 0) && ('f' - c >= 0);
		}
		
		/**
		 * 判断某个字符是否为特殊数字类型的后缀符。<br>
		 * 例如，C++允许 long i = 1234L形式的声明，表示1234L为长整型（而不是int）。
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isSfx(Character c) {
			return c == 'U' || c == 'u' ||
					c == 'F' || c == 'f' ||
					c == 'L' || c == 'l';
		}
		
		/**
		 * 判断某个字符是否为科学计数后缀符。
		 * 例如：int i = 123e3
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isStf(Character c) {
			return c == 'E' || c == 'e';
		}
		
		/**
		 * 判断某个字符是否为边界符。
		 * TODO 有些符号逻辑上似乎不应该归到这一类
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isNbsp(Character c) {
			return c == ' ' || c == '\t' || c == ';' || c == '>' ||
					c == '\n' || c == ',' || c == '}' || c == ')' ||
					 c == ']' || c == '<' || c == '{' || c == '(' ||
					 c == '[';
		}
		
		/**
		 * 判断某个字符是否为运算符。
		 * @param c 输入的字符
		 * @return 是或否
		 */
		public static boolean isOp(Character c) {
			return c == '+' || c == '-' || c == '*' || c == '/' ||
					 c == '^' ||  c == '%' || c == '=';
		}
	}
	
	@Override
	protected void add(Token t) { cslst.add(t); }
	
	@Override
	protected void highlightAll(StringBuilder txt) {
		int baseIndex = 0;
		for(Token t : cslst) {
			int head = t.getIndex(), rear = t.getRear();
			txt.insert(rear + baseIndex, SUFFIX);
			txt.insert(head + baseIndex, PREFIX);
			
			for(Token cmtBlock : CmtManager.getCmtList()) {
				// 若标签插入位置在某个注释块之前，注释块的位置 应对应后移 一对标签的长度。
				if(CmtManager.ptrPosByBlock(cmtBlock, rear + baseIndex) == -1) {
					cmtBlock.moveOff(SUFFIX.length() + PREFIX.length());
				}
			}
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
	}
	
	/**
	 * 构造器。指定该ConstManager对注释块的高亮色。
	 */
	protected ConstManager(String clrVal) { super(clrVal); }

	@Override
	public StringBuilder analyze(StringBuilder txt) {
		
		char[] chars = txt.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it)) continue;
			Character c = chars[it];
			
			switch(state) {
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
					// Got a Integer.
					rear = it;
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
					// Got a float.
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
				// Got a scientific number( has +/- ).
				rear = it;
				add(new Const(head, rear));
				state = 0;
				break;
			case 11:
				currentWord.append(c);
				if(Utils.isSfx(c)) state = 12;
				else{
					// Got a number with 'u' at the end. [!!!]THIS IS A WRONG WAY!!!
					rear = it;
					add(new Const(head, rear));
					state = 0;
				}	
				break;
			case 12:
				currentWord.append(c);
				// Got a multi-suffix Integer.
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
					// Got zero.
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
					// Got a scientific number( no +/- ).
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
		highlightAll(txt);
		doNext(txt);
		
		return txt;
	}
	
	@Override
	public void reset() { cslst.clear(); }
}
