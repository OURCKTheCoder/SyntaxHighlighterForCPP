package ourck.mainconsole;

import ourck.swing.SyntaxHighlighter;

/**
 * 程序主控制台入口点
 * @author ourck
 */
public class MainConsole {
	/**
	 * 程序主入口点。
	 * @param args
	 */
	public static void main(String[] args) {
		SyntaxHighlighter mainFrm = new SyntaxHighlighter();
		mainFrm.setVisible(true);
		return;
	}
}
