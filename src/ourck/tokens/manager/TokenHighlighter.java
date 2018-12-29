package ourck.tokens.manager;

import ourck.tokens.token.Token;

/**
 * 词法单元管理器 抽象类接口定义
 * @author ourck
 */
public abstract class TokenHighlighter extends TokenManager{
	
	private String clrVal = "#000000";
	
	/**
	 * 指定某个TokenHighlighter对该类Token高亮时的颜色值。
	 * @param clrVal 高亮颜色值
	 */
	protected TokenHighlighter(String clrVal) { this.clrVal = clrVal; }
	
	/**
	 * 设置某个TokenHighlighter对该类Token高亮时的颜色值。
	 * @param hexRgb 要设置的颜色值
	 */
	protected void setClrVal(String hexRgb) { this.clrVal = hexRgb; }
	
	/**
	 * 获得某个TokenHighlighter对该类Token高亮时的颜色值。
	 * @return 颜色值
	 */
	protected String getClrVal() { return clrVal; }
	
	/**
	 * 添加某个{@link Token}对象进入该{@link TokenHighlighter}所管理的容器中。
	 * @param t 要添加的Token
	 */
	protected abstract void add(Token t);
	
	/**
	 * 对源代码文本根据解析（analyze()）的结果，在原文基础上嵌入HTML标签。<br>
	 * @param txt 源代码全文的{@link StringBuilder}
	 * @return 经过处理的的{@link StringBuilder}
	 */
	protected abstract void highlightAll(StringBuilder txt);
	
	/**
	 * 对源代码文本执行词法分析动作，同时嵌入HTML标签以实现HTML高亮着色。<br>
	 * @param txt 源代码全文的{@link StringBuilder}
	 * @return 经过处理的的{@link StringBuilder}
	 */
	public abstract StringBuilder analyze(StringBuilder txt);

}
