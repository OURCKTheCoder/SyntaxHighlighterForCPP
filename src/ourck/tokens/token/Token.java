package ourck.tokens.token;

/**
 * 词法单元 抽象类定义。<br>
 * 每个词法单元都具有三个属性值：词法单元本身的字符串值，在文本中的起始位置，在文本中的结束位置。<br>
 * <br>
 * ---------------------------------------------------------------
 * <br>
 * P.S. 什么是“词法单元（Token）”？
 * <ol>
 * <li>通常，一个所谓的“词法单元”并不一定是一个具体的英文单词；</li>
 * <li>在编译原理中，一个词法单元是一串字符序列，仅此而已。</li>
 * <li>词法分析器按 <b>字符</b> 对文本文件进行扫描，根据一定的规则判断某一串字符是否为词法单元。</li>
 * </ol>
 * 例如，语句
 * <pre align="center">int i = 777; </pre>
 * 当中，可以有5个词法单元：
 * <pre align="center">[int] [i] [=] [777] [;]</pre>
 * 在词法分析阶段，我们不去探究各个词法单元代表什么含义，只需了解他们的类型就足够了。
 * 这样在语法高亮时，就可以根据不同的词法单元类型来嵌入不同的HTML颜色标签。<br>
 * 例如，在上面的例子中，可以把词法单元分为3类：<br>
 * <ol>
 * <li>[int]：C++基本数据类型 </li>
 * <li>[i]、[=]、[;]：普通文本</li>
 * <li>[777]：数值 </li>
 * </ol>
 * 这样分类之后，就可以根据不同的类型来在语法高亮时上不同的色。
 * 
 * @author ourck
 *
 */
public abstract class Token {
	private String str;
	private Integer head;
	private Integer rear;
	
	public Token(String str, int head, int rear){
		this.str = str;
		this.head = head;
		this.rear = rear;
	}
	
	/**
	 * 获取词法单元的开始位置
	 * @return int值。指定了某个词法单元在某个文本中的起始位置（第几个字符）
	 */
	public Integer getIndex() { return head; }
	
	/**
	 * 获取词法单元的结束位置
	 * @return int值。指定了某个词法单元在某个文本中的结束位置（第几个字符）
	 */
	public Integer getRear() { return rear; }
	
	/**
	 * 设置词法单元的开始位置
	 */
	public void setIndex(Integer val) { head = val;}
	
	/**
	 * 设置词法单元的结束位置
	 */
	public void setRear(Integer val) { rear = val;}
	
	/**
	 * 获取词法单元的字符值
	 */
	public String getStr() { return str; }
	
	/**
	 * 将词法单元在文本当中整体后移，通过修改head和rear属性值。<br>
	 * <br>
	 * 该方法的一个用途是：在原文某个位置嵌入HTML标签后，更新其余的词法单元位置，即将该位置之后 所有词法单元的位置往后推。<br>
	 * 后推的长度即为HTML标签的长度。
	 * @param off 后移的字符数
	 */
	public void moveOff(Integer off) { head += off; rear += off; }
	
	@Override
	public String toString() {
		return "[" + head + " - " + rear + ", " + str + "]";
	}
	
	/**
	 * 比较两个词法单元。<br>
	 * 两个词法单元相等的充要条件为：
	 * <ol>
	 * <li>两个词法单元的起始位置相同；</li>
	 * <li>两个词法单元的字符串值相同。</li>
	 * </ol>
	 * @param obj 要比较的对象。注意：对于非Token类型的对象也会返回false
	 * @return 是 / 否
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Token)) return false;
		if(str == ((Token)obj).getStr() && head == ((Token)obj).getIndex())
			return true;
		else
			return false;
	}
}
