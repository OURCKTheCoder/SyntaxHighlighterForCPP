package ourck.config;

/**
 * 颜色配置文件。<br>
 * TODO 所有配色都被标记为final，这意味着配色方案只有一种。
 * 可以进一步考虑从文件加载 从而拓展出不同的配色方案。
 * @author ourck
 */
public class PaletteConfigBean {

	/**
	 * 注释块词法单元配色
	 */
	public final String cmtClr;
	
	/**
	 * 字符串常量词法单元配色
	 */
	public final String strClr;
	
	/**
	 * 常数词法单元配色
	 */
	public final String cstClr;
	
	/**
	 * 宏 / 预处理器词法单元配色
	 */
	public final String macClr;
	
	/**
	 * 关键字词法单元配色
	 */
	public final String kwdClr;
	
	/**
	 * 基本数据类型词法单元配色
	 */
	public final String typClr;
	
	/**
	 * 默认构造器。<br>
	 * 该配色方案基于Vibrant Ink.
	 */
	public PaletteConfigBean() {
		cmtClr = "#8C3FC8";
		strClr = "#f0c837";
		cstClr = "#477488";
		macClr = "#EC691E";
		kwdClr = "#9CF828";
		typClr = "#66ccff";
	}
	
	
}
