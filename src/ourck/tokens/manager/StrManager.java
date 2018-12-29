package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.*;

/**
 * 普通文本常量（带双引号的字符串常量）词法单元管理器
 * @author ourck
 */
public class StrManager extends TokenHighlighter {
	
	private final String PREFIX;
	private final String SUFFIX;
	{
		PREFIX = "<font color=\"" + getClrVal() + "\">";
		SUFFIX = "</font>";
	}
	private ArrayList<Token> strlst = new ArrayList<Token>();

	/**
	 * 内部工具类。<br>
	 * 该类包含一系列实用方法，用于更方便地判断某个字符的属性
	 * @author ourck
	 */
	private static class Utils {
		
		/**
		 * 判断某个字符是否为（英文）双引号
		 * @param c 输入的字符
		 * @return 是 / 否
		 */
		public static boolean isQts(Character c) {
			return c == '\"';
		}
		
		/**
		 * 判断某个字符是否为（英文）单引号
		 * @param c 输入的字符
		 * @return 是 / 否
		 */
		public static boolean isQt(Character c) {
			return c == '\'';
		}
		
		/**
		 * 判断某个字符是否为文本换行符
		 * @param c 输入的字符
		 * @return 是 / 否
		 */
		public static boolean isNl(Character c) {
			return c == '\n';
		}		
	}

	@Override
	protected void add(Token t) { strlst.add(t); }
	
	@Override
	protected void highlightAll(StringBuilder token) {
		//2.HighLight all.
		int baseIndex = 0;
		for(Token t : strlst) {
			int head = t.getIndex(), rear = t.getRear();
			token.insert(rear + baseIndex, SUFFIX);
			token.insert(head + baseIndex, PREFIX);
			
			for(Token cmtBlock : CmtManager.getCmtList()) {
				if(CmtManager.ptrPosByBlock(cmtBlock, rear + baseIndex) == -1) {
					cmtBlock.moveOff(SUFFIX.length() + PREFIX.length());
				}
			}
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
	}

	/**
	 * 构造器。指定该StrManager对注释块的高亮色。
	 */
	protected StrManager(String clrVal) { super(clrVal); }

	@Override
	public StringBuilder analyze(StringBuilder txt) {
		char[] chars = txt.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it))
				continue;
			Character c = chars[it];
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
		
		highlightAll(txt);
		doNext(txt);

		return txt;
	}

	@Override
	public void reset() { strlst.clear(); }
}
