package ourck.tokens.manager;

/**
 * 通用的Token管理器类接口。<br>
 * 通常而言，每个TokenManager都应持有一个容器，用于存储所解析的词法单元。<br>
 * 采用职责链设计模式，一个TokenManager负责处理一种词法单元的请求<br>
 * 多个TokenManager通过设置后继者链接在一起，处理一个文本文件中的不同词法单元。
 * @author ourck
 */
public abstract class TokenManager {
	
	private TokenManager successor = null;
	
	/**
	 * 执行职责链的下一环的analyze()
	 * @param txt 源代码文本的{@link StringBuilder}
	 * @return 经过处理后的源代码文本的{@link StringBuilder}
	 */
	protected StringBuilder doNext(StringBuilder txt) {
		if(successor != null) successor.analyze(txt);
		return txt;
	}
	
	/**
	 * 设置职责链的successor
	 * @param mgr 要设置的successor
	 */
	protected void setSuccessor(TokenManager mgr) { this.successor = mgr; }
	
	/**
	 * 获取职责链的successor
	 * @return 当前对象的下一个请求处理器
	 */
	protected TokenManager getSuccessor() { return successor; }
	
	/**
	 * 对源代码文本文件进行解析并在原文本内嵌入相应HTML标签
	 * @param txt 源代码文本的{@link StringBuilder}
	 * @return 经过处理后的源代码文本的{@link StringBuilder}
	 */
	public abstract StringBuilder analyze(StringBuilder txt);
	
	/**
	 * 清除当前TokenManager所有的状态，以便复用。<br>
	 * 例如，该方法可作为清空词法单元存储容器的一个实现。
	 */
	public abstract void reset();
}
