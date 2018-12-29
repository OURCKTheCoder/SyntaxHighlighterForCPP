package ourck.tokens.manager;

import java.util.*;

import ourck.config.PaletteConfigBean;

/**
 * 词法单元高亮代理类。<br>
 * 客户端最终是通过访问本类，来对文本执行高亮动作的。
 * @author ourck
 */
public class TokenHandler extends TokenManager{

	private static TokenHandler handler = null;
	private TokenManager firstMgr = null;
	private PaletteConfigBean palette = null;
	
	/**
	 * private构造器，用于装配职责链。
	 */
	private TokenHandler() {
		palette = new PaletteConfigBean();
		
		// Generating TokenManagers.
		TokenManager mgrs[]  = {
			new LTGTMaker(),
			new CmtManager(palette.cmtClr),
			new StrManager(palette.strClr),
			new ConstManager(palette.cstClr),
			new MacroManager(palette.macClr),
			new KwdManager(palette.kwdClr),
			new TypeManager(palette.typClr),
			new PreMaker()
		};
		
		// Wiring responsibility chain. 
		List<TokenManager> chain = new LinkedList<TokenManager>();
		Collections.addAll(chain, mgrs);
		Iterator<TokenManager> prv = chain.listIterator();
		Iterator<TokenManager> nxt = chain.listIterator();
		nxt.next();
		while(nxt.hasNext()) {
			prv.next().setSuccessor(nxt.next());
		}
		
		firstMgr = chain.get(0);
	}
	
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		firstMgr.analyze(txt);
		reset();
		return txt;
	}
	
	/**
	 * 单例模式的全局对象访问入口点。<br>
	 * @return TokenHandler全局静态对象。可通过调用该对象的analyze()方法执行转换成HTML文本并高亮。
	 */
	public static TokenHandler getHandler() {
		if(handler == null) handler = new TokenHandler();
		return handler;
	}

	/**
	 * 清空职责链上每一环TokenManager的状态。<br>
	 * 由于采用单例设计模式，因此每次解析完毕之后，
	 * 必须reset各个TokenManager的状态，以便复用。
	 */
	@Override
	public void reset() {
		TokenManager ptr = firstMgr;
		while(ptr != null) {
			ptr.reset();
			ptr = ptr.getSuccessor();
		}
	}
}
