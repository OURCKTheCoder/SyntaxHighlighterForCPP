package ourck.tokens.manager;

/**
 * pre标签管理器。
 * @author ourck
 */
public class PreMaker extends TokenManager{
	
	private static final String PRE_TAG = "<pre>";
	private static final String CLOUSURE_TAG = "</font>\n</pre>";
	private final String FONTSIZE_TAG = "<font color=\"#d3d7cf\" size=\"4\">";
	
	/**
	 * 向指定的HTML文件 <b>首部</b> 和 <b>末尾</b> <b>直接添加</b>pre标签和字体大小标签。
	 */
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		
		txt.insert(0, PRE_TAG + FONTSIZE_TAG);
		txt.append(CLOUSURE_TAG);
		
		doNext(txt);
		return null;
	}

	@Override
	public void reset() { 
		// throw new UnsupportedOperationException(); // Or..
		// Do nothing.
	}
}
