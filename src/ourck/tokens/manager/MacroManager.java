package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.*;

/**
 * 宏定义词法单元管理器
 * @author ourck
 */
public class MacroManager extends TokenHighlighter {
	
	private final String PREFIX;
	private final String SUFFIX;
	{
		PREFIX = "<font color=\"" + getClrVal() + "\">";
		SUFFIX = "</font>";
	}
	private static final HashSet<String> MACRO_LIST = new HashSet<String>();
	static {
		Collections.addAll(MACRO_LIST,
				"include",	"define",	"undef",	"if",
				"ifdef",	"ifndef",	"elif",	"endif",
				"else",	"error");
	}
	private LinkedList<Token> macros = new LinkedList<Token>();
	
	/**
	 * 判断某个字符串是否为C++预处理器关键字。
	 * @param t 输入的字符串
	 * @return 是 / 否
	 */
	private boolean cmp(String t) {
		if(MACRO_LIST.contains(t)) { return true; }
		else { return false; }
	}
	
	@Override
	protected void add(Token t) {
		macros.add(t);
	}
	
	@Override
	protected void highlightAll(StringBuilder txt) {
		int baseIndex = 0;
		for(Token t : macros) {
			int head = t.getIndex(), rear = t.getRear();
			// [!] Rear goes first.
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

	/**
	 * 构造器。指定该MacroManager对注释块的高亮色。
	 */
	protected MacroManager(String clrVal) { super(clrVal); }

	@Override
	public StringBuilder analyze(StringBuilder txt) {
		//1.Divide by chars
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
				if(c.equals(' ') || c.equals('<') || c.equals('\"'))
					state = 3;
				break;
			case 3:
				it--; c = chars[it];
				rear = it;
				MacroWord pp = new MacroWord(currentWord.toString(), head, rear);
				add(pp);
				state = 0;
			}
		}
		
		//2.HighLight all.
		highlightAll(txt);
		doNext(txt);

		return txt;
	}

	@Override
	public void reset() { macros.clear(); }
}