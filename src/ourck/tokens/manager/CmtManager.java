package ourck.tokens.manager;

import java.util.*;

import ourck.tokens.token.*;

/**
 * 注释块管理类<br>
 * 该类有一个较为特殊的地方：该类通过analyze方法解析的注释块是需要共享的<br>
 * @author ourck
 */
public class CmtManager extends TokenHighlighter{
	
	private final String PREFIX;
	private final String SUFFIX;
	{
		PREFIX = "<font color=\"" + getClrVal() + "\">";
		SUFFIX = "</font>";
	}
	/**
	 * 设置为静态的注释块词法单元容器，以便(不依赖对象而)共享。
	 */
	private static ArrayList<Token> cmtBlockList = new ArrayList<Token>();
	
	@Override
	protected void highlightAll(StringBuilder newToken) {
		int baseIndex = 0; 
		for(Token t : cmtBlockList) {
			int head = t.getIndex(), rear = t.getRear();
			// Rear goes first.
			newToken.insert(rear + baseIndex, SUFFIX);
			newToken.insert(head + baseIndex, PREFIX);
			
			t.setIndex(head + baseIndex);
			t.setRear(rear + baseIndex + SUFFIX.length() + PREFIX.length());
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
	}
	
	@Override
	protected void add(Token t) { 
		cmtBlockList.add(t); 
	}

	/**
	 * 构造器。指定该CmtManager对注释块的高亮色。
	 */
	protected CmtManager(String clrVal) { super(clrVal); }

	/**
	 * 用于指定当前文本指针是否位于某个注释块之内。<br>
	 * <br>
	 * 由于语法高亮遵循这样一条原则：注释块内的语法元素（关键字、宏定义等）不应该高亮。<br>
	 * 该方法用于判断该规则成立的条件。
	 * @param index 文本指针，即文本当中的字符序号
	 * @return boolean标志，true表示在注释块之内，false则表示否。
	 */
	public static boolean isPtrInsideCmtBlock(int index) {
		for(Token cmt : cmtBlockList) {
			if(index >= cmt.getIndex() && index <= cmt.getRear()) return true;
		}
		return false;
	}
	
	/**
	 * 判断文本指针关于某注释块的相对位置。
	 * @param cmtBlock 需要比较位置的注释块
	 * @param ptr 文本指针
	 * @return int值。-1表示文本指针在注释块之前，0表示在注释块中，1表示在注释块之后
	 */
	public static int ptrPosByBlock(Token cmtBlock, int ptr) {
		if(cmtBlock.getIndex() > ptr) return -1;
		else if(cmtBlock.getRear() < ptr) return 1;
		else return 0;
	}
	
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		cmtBlockList.clear();
		
		char[] charary = txt.toString().toCharArray();
		Character[] chars = new Character[charary.length];
		for(int i = 0; i < charary.length; i++) {
			chars[i] = charary[i];
		}
		
		// Get all tokens' positions
		int state = 0;
		int head = 0, rear = 0;
		for(int it = 0; it < chars.length; it++) {
			if(isPtrInsideCmtBlock(it)) continue;
			Character c = chars[it];
			switch(state) {
			case 0:
				if(c.equals(' ') || c.equals('\t')) state = 0;
				else if(c.equals('/')) {
					head = it;
					state = 1;
				}
				break;
			case 1:
				if(c.equals('/')) state = 3;
				else if(c.equals('*')) state = 2;
				else state = 0;
				break;
			case 2:
				rear = it; // [!] Avoid analyzing to file's end.
				if(c.equals('*')) state = 4;
				else state = 2;
				break;
			case 3:
				if(!c.equals('\n')) { state = 3; }
				else {
					rear = it;
					Comment cmt = new Comment(head, rear);
					add(cmt);
					state = 5;
				}
				break;
			case 4:
				if(c.equals('/')) {
					rear = it + 1;
					Comment cmt = new Comment(head, rear);
					add(cmt);
					state = 5;
				}
				else state = 2;
				break;
			case 5: 
				state = 0;
				break;
			}
		}
		
		highlightAll(txt);
		doNext(txt);
		
		return txt;
	}
	
	/**
	 * 对注释块列表提供单一的、全局的访问入口点。
	 * @return 注释块列表
	 */
	public static List<Token> getCmtList() { return cmtBlockList; }

	/**
	 * 重置CmtManager所持有的注释块词法单元容器，以便复用。
	 */
	@Override
	public void reset() { cmtBlockList.clear(); }
}
