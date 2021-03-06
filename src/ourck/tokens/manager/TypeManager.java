package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.KeyWord;
import ourck.tokens.token.Token;

/**
 * C++基本数据类型词法单元管理器
 * @author ourck
 */
public class TypeManager extends TokenHighlighter {

	private final String PREFIX;
	private final String SUFFIX;
	{
		PREFIX = "<font color=\"" + getClrVal() + "\">";
		SUFFIX = "</font>";
	}
	private static final HashSet<String> TYPELIST = new HashSet<String>();
	static {
		Collections.addAll(TYPELIST,
				"char",		"float",	"double",	"int",
				"long",		"short",	"true",		"void",
				"false");
	}
	private ArrayList<Token> kwds = new ArrayList<Token>();
	
	/**
	 * 判断某个字符串是否为C++集本数据类型关键字
	 * @param t 输入的字符串
	 * @return 是 / 否
	 */
	private boolean cmp(String t) {
		if(TYPELIST.contains(t)) { return true; }
		else { return false; }
	}
	
	/**
	 * 判断某个字符是否为英文字符
	 * @param cc 输入的字符
	 * @return 是 / 否
	 */
	private boolean isChar(Character cc) {
		char c = (char)cc;
		return ((c - 'A' >= 0) && ('Z' - c >= 0)) ||
				((c - 'a' >= 0) && ('z' - c >= 0));
	}
	
	@Override
	protected void highlightAll(StringBuilder txt) {
		int baseIndex = 0;
		for(Token t : kwds) {
			int head = t.getIndex(), rear = t.getRear();
			
			txt.insert(rear + baseIndex, SUFFIX);
			txt.insert(head + baseIndex, PREFIX);
			for(Token cmtBlock : CmtManager.getCmtList()) {
				if(CmtManager.ptrPosByBlock(cmtBlock, rear + baseIndex) == -1) {
					cmtBlock.moveOff(SUFFIX.length() + PREFIX.length());
				}
			}
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
	}
	
	@Override
	protected void add(Token t) {
		kwds.add(t);
	}

	/**
	 * 构造器，指定该TypeManager对注释块的高亮色。
	 * @param clrVal 高亮颜色值
	 */
	protected TypeManager(String clrVal) { super(clrVal); }
	
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		//1.Divide by chars.
		char[] chars = txt.toString().toCharArray();
		int state = 0;
		int head = 0, rear = 0;
		StringBuilder currentWord = new StringBuilder();
		for(int it = 0; it < chars.length; it++) {
			if(CmtManager.isPtrInsideCmtBlock(it))
				continue;
			
			Character c = chars[it];
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
				rear = it; // 额外的的判断状态 指针又前移一次
				KeyWord kwd = new KeyWord(currentWord.toString(), head, rear);
				add(kwd);
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
	public void reset() { kwds.clear(); }
}
